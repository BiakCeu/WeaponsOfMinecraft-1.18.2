package reascer.wom.animation;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class SpecialAttackAnimation extends AttackAnimation {
	public SpecialAttackAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, antic, antic, contact, recovery, collider, colliderJoint, path, armature);
	}
	
	public SpecialAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, path, armature, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
	}
	
	public SpecialAttackAnimation(float convertTime, float antic, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, path, armature, new Phase(0.0F, antic, antic, contact, recovery, Float.MAX_VALUE, hand, colliderJoint, collider));
	}
	
	public SpecialAttackAnimation(float convertTime, String path, Armature armature, boolean Coordsetter, Phase... phases) {
		super(convertTime, path, armature, phases);
	}
	
	public SpecialAttackAnimation(float convertTime, String path, Armature armature, Phase... phases) {
		super(convertTime, path, armature, phases);
		this.newTimePair(0, Float.MAX_VALUE);
		this.addStateRemoveOld(EntityState.TURNING_LOCKED, false);
		
		this.addProperty(ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET);
		this.addProperty(ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
			LivingEntity attackTarget = entitypatch.getTarget();
			
			if (!self.getRealAnimation().getProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
				TransformSheet transform = self.getTransfroms().get("Root").copyAll();
				Keyframe[] keyframes = transform.getKeyframes();
				int startFrame = 0;
				int endFrame = transform.getKeyframes().length - 1;
				Vec3f keyLast = keyframes[endFrame].transform().translation();
				Vec3 pos = entitypatch.getOriginal().getEyePosition();
				Vec3 targetpos = attackTarget.position();
				float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance()*1.75f - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()) * 0.75F, 0.0F);
				Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
				float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);
				
				for (int i = startFrame; i <= endFrame; i++) {
					Vec3f translation = keyframes[i].transform().translation();
					translation.z *= scale;
				}
				
				transformSheet.readFrom(transform);
			} else {
				transformSheet.readFrom(self.getTransfroms().get("Root"));
			}
		});
	}
	
	@Override
	protected void attackTick(LivingEntityPatch<?> entitypatch) {
		AnimationPlayer player = entitypatch.getAnimator().getPlayerFor(this);
		float elapsedTime = player.getElapsedTime();
		float prevElapsedTime = player.getPrevElapsedTime();
		EntityState state = this.getState(entitypatch, elapsedTime);
		EntityState prevState = this.getState(entitypatch, prevElapsedTime);
		Phase phase = this.getPhaseByTime(elapsedTime);
		
		if (state.getLevel() == 1 && !state.turningLocked()) {
			if (entitypatch instanceof MobPatch) {
				((Mob)entitypatch.getOriginal()).getNavigation().stop();
				entitypatch.getOriginal().attackAnim = 2;
				LivingEntity target = entitypatch.getTarget();
				
				if (target != null) {
					entitypatch.rotateTo(target, entitypatch.getYRotLimit(), false);
				}
			}
		}
		if (prevState.attacking() || state.attacking() || (prevState.getLevel() < 2 && state.getLevel() > 2)) {
			if (!prevState.attacking() || (phase != this.getPhaseByTime(prevElapsedTime) && (state.attacking() || (prevState.getLevel() < 2 && state.getLevel() > 2)))) {
				entitypatch.playSound(this.getSwingSound(entitypatch, phase), 0.0F, 0.0F);
				entitypatch.getCurrenltyAttackedEntities().clear();
			}
			
			this.hurtCollidingEntities(entitypatch, prevElapsedTime, elapsedTime, prevState, state, phase);
		}
	}
	
	@Override
	protected void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, Phase phase) {
		LivingEntity entity = entitypatch.getOriginal();
		entitypatch.getArmature().initializeTransform();
		float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
		float poseTime = state.attacking() ? elapsedTime : phase.contact;
		List<Entity> list = this.getPhaseByTime(elapsedTime).getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch));
		
		if (list.size() > 0) {
			HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
			int maxStrikes = this.getMaxStrikes(entitypatch, phase);
			
			while (entitypatch.getCurrenltyAttackedEntities().size() < maxStrikes && hitEntities.next()) {
				Entity hitten = hitEntities.getEntity();
				LivingEntity trueEntity = this.getTrueEntity(hitten);
				
				if (trueEntity != null && trueEntity.isAlive() && !entitypatch.getCurrenltyAttackedEntities().contains(trueEntity) && !entitypatch.isTeammate(hitten)) {
					if (hitten instanceof LivingEntity || hitten instanceof PartEntity) {
						HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(hitten, HurtableEntityPatch.class);
						if (hitHurtableEntityPatch != null && entity.hasLineOfSight(hitten)) {
							EpicFightDamageSource source;
							float anti_stunlock = 1;
							if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).isPresent()) {
								if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).get() == StunType.NONE) {
									source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
									source.setStunType(StunType.HOLD);
								} else {
									source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
								}
							} else {
								source = this.getEpicFightDamageSource(entitypatch, hitten, phase);
							}
							String replaceTag = "anti_stunlock:"+ anti_stunlock +":"+hitten.tickCount+":"+this.getId()+"-"+phase.contact;
							if (hitHurtableEntityPatch.isStunned()) {
								for (String tag : hitten.getTags()) {
									if (tag.contains("anti_stunlock:")) {
										anti_stunlock = this.applyAntiStunLock(hitten, anti_stunlock, source, phase, tag, replaceTag);
										break;
									}
								}
							} else {
								boolean firstAttack = true;
								for (String tag : hitten.getTags()) {
									if (tag.contains("anti_stunlock:")) {
										if (hitten.tickCount - Float.valueOf(tag.split(":")[2]) > 10) {
											anti_stunlock = 1;
										} else {
											anti_stunlock = this.applyAntiStunLock(hitten, anti_stunlock, source, phase, tag, replaceTag);
											firstAttack = false;
										}
										break;
									}

								}
								
								if (firstAttack) {
									int i = 0;  
									while (i < hitten.getTags().size()) {
										if (((String) hitten.getTags().toArray()[i]).contains("anti_stunlock:")) {
											hitten.getTags().remove(hitten.getTags().toArray()[i]);
										} else {
											i++;
										}
										
									}
									hitten.addTag(replaceTag);
								}
								//entitypatch.playSound(SoundEvents.ARROW_HIT_PLAYER, 1, 1);
							}
							if (anti_stunlock < 0.4f) {
								for (String tag : hitten.getTags()) {
									if (tag.contains("anti_stunlock:")) {
										hitten.removeTag(tag);
										break;
									}
								}
								source.setStunType(StunType.KNOCKDOWN);
							}
							source.setImpact(source.getImpact() * anti_stunlock);
							int prevInvulTime = hitten.invulnerableTime;
							hitten.invulnerableTime = 0;
							AttackResult attackResult = entitypatch.attack(source, hitten, phase.hand);
							hitten.invulnerableTime = prevInvulTime;
							
							if (attackResult.resultType.dealtDamage()) {
								if (entitypatch instanceof ServerPlayerPatch) {
									ServerPlayerPatch playerpatch = ((ServerPlayerPatch) entitypatch);
									playerpatch.getEventListener().triggerEvents(EventType.DEALT_DAMAGE_EVENT_POST, new DealtDamageEvent(playerpatch, trueEntity, source, attackResult.damage));
								}
								
								hitten.level.playSound(null, hitten.getX(), hitten.getY(), hitten.getZ(), this.getHitSound(entitypatch, phase), hitten.getSoundSource(), 1.0F, 1.0F);
								this.spawnHitParticle(((ServerLevel) hitten.level), entitypatch, hitten, phase);
								
								if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).isPresent()) {
									if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).get() == StunType.NONE) {
										float stunTime = (float) (source.getImpact() * 0.3f * (1.0F - ((LivingEntity) hitten).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
										if (hitHurtableEntityPatch.getOriginal().isAlive()) {
											hitHurtableEntityPatch.setStunReductionOnHit();
											
											hitHurtableEntityPatch.applyStun((anti_stunlock > 0.4f ?StunType.LONG:StunType.KNOCKDOWN), stunTime);
											float impact = source.getImpact();
											hitHurtableEntityPatch.knockBackEntity(entitypatch.getOriginal().getPosition(1),source.getImpact() * 0.25f);
										}
									}
									
									if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
										float stunTime = (float) (source.getImpact() * 0.5f * (1.0F - ((LivingEntity) hitten).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
										if (hitHurtableEntityPatch.getOriginal().isAlive()) {
											hitHurtableEntityPatch.setStunReductionOnHit();
											hitHurtableEntityPatch.applyStun((anti_stunlock > 0.4f ? StunType.SHORT:StunType.KNOCKDOWN), stunTime);
											double power = (source.getImpact() / anti_stunlock) * 0.25f;
											double d1 = entity.getX() - hitten.getX();
											double d2 = entity.getY()-8 - hitten.getY();
											double d0;
											
											for (d0 = entity.getZ() - hitten.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
												d1 = (Math.random() - Math.random()) * 0.01D;
											}
											
											if (hitten instanceof LivingEntity) {
												power *= 1.0D - ((LivingEntity) hitten).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
											}
											if (!(power <= 0.0D)) {
												hitten.hasImpulse = true;
												Vec3 vec3 = entity.getDeltaMovement();
												Vec3 vec31 = (new Vec3(d1, d2, d0)).normalize().scale(power);
												hitten.setDeltaMovement(vec3.x / 2.0D - vec31.x, vec3.y / 2.0D - vec31.y, vec3.z / 2.0D - vec31.z);
											}
										}
									}
								}
							}
							
							if (attackResult.resultType.shouldCount()) {
								entitypatch.getCurrenltyAttackedEntities().add(trueEntity);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public Vec3 getCoordVector(LivingEntityPatch<?> entitypatch, DynamicAnimation dynamicAnimation) {
		Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
		
		if (entitypatch.shouldBlockMoving() && this.getProperty(ActionAnimationProperty.CANCELABLE_MOVE).orElse(true)) {
			vec3.scale(0.0F);
		}
		
		return vec3;
	}
	
	@Override
	public Pose getPoseByTime(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
		Pose pose = super.getPoseByTime(entitypatch, time, partialTicks);
		return pose;
	}
	
	@Override
	public boolean isBasicAttackAnimation() {
		return false;
	}
	
	public float applyAntiStunLock(Entity hitten, float anti_stunlock, EpicFightDamageSource source, Phase phase, String tag, String replaceTag) {
		boolean isPhaseFromSameAnimnation = false;
		if (hitten.level.getBlockState(new BlockPos(new Vec3(hitten.getX(), hitten.getY()-1, hitten.getZ()))).isAir() && source.getStunType() != StunType.FALL ) {
			String phaseID = String.valueOf(this.getId())+"-"+String.valueOf(phase.contact);
			if (tag.split(":").length > 3) {
				if ((String.valueOf(this.getId()).equals(tag.split(":")[3].split("-")[0])) && (!String.valueOf(phase.contact).equals(tag.split(":")[3].split("-")[1]))) {
					anti_stunlock = Float.valueOf(tag.split(":")[1])* 0.975f;
					isPhaseFromSameAnimnation = true;
				} else {
					anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.95f;
					isPhaseFromSameAnimnation = false;
				}
			}
			for (int i = 3; i < tag.split(":").length && i < 7; i++) {
				if (tag.split(":")[i].equals(phaseID)) {
					anti_stunlock *= 0.80f;
				}
			}
		} else {
			String phaseID = String.valueOf(this.getId())+"-"+String.valueOf(phase.contact);
			if (tag.split(":").length > 3) {
				if ((String.valueOf(this.getId()).equals(tag.split(":")[3].split("-")[0])) && (!String.valueOf(phase.contact).equals(tag.split(":")[3].split("-")[1]))) {
					anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.95f;
					isPhaseFromSameAnimnation = true;
				} else {
					anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.9f;
					isPhaseFromSameAnimnation = false;
				}
			}
			for (int i = 3; i < tag.split(":").length && i < 7; i++) {
				if (tag.split(":")[i].equals(phaseID)) {
					anti_stunlock *= 0.80f;
				}
			}
		}
		hitten.removeTag(tag);
		int maxSavedAttack = 7;
		
		if (isPhaseFromSameAnimnation) {
			replaceTag = "anti_stunlock:"+ anti_stunlock+":"+hitten.tickCount;
			maxSavedAttack = 8;
		} else {
			replaceTag = "anti_stunlock:"+ anti_stunlock+":"+hitten.tickCount+":"+this.getId()+"-"+phase.contact;
			maxSavedAttack = 7;
		}
		
		for (int i = 3; i < tag.split(":").length && i < maxSavedAttack; i++) {
			replaceTag = replaceTag.concat(":"+tag.split(":")[i]);
		}
		hitten.addTag(replaceTag);
		return anti_stunlock;
	}
}
