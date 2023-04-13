package reascer.wom.animation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import reascer.wom.gameasset.WOMColliders;
import reascer.wom.world.entity.projectile.EnderBullet;
import reascer.wom.world.entity.projectile.WOMEntities;
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
import yesman.epicfight.api.animation.types.LinkAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class ChargeAttackAnimation extends AttackAnimation {
	public ChargeAttackAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, antic, antic, contact, recovery, collider, colliderJoint, path, armature);
	}
	
	public ChargeAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, path, armature, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
	}
	
	public ChargeAttackAnimation(float convertTime, float antic, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
		this(convertTime, path, armature, new Phase(0.0F, antic, antic, contact, recovery, Float.MAX_VALUE, hand, colliderJoint, collider));
	}
	
	public ChargeAttackAnimation(float convertTime, String path, Armature armature, boolean Coordsetter, Phase... phases) {
		super(convertTime, path, armature, phases);
	}
	
	public ChargeAttackAnimation(float convertTime, String path, Armature armature, Phase... phases) {
		super(convertTime, path, armature, phases);
		
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
				float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance()*3.00f - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()) * 0.75F, 0.0F);
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
		Collider collider = this.getCollider(entitypatch, elapsedTime);
		LivingEntity entity = entitypatch.getOriginal();
		entitypatch.getArmature().initializeTransform();
		float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
		float poseTime = state.attacking() ? elapsedTime : phase.contact;
		List<Entity> list = collider.updateAndSelectCollideEntity(entitypatch, this, prevPoseTime, poseTime, phase.getColliderJoint(), this.getPlaySpeed(entitypatch));
		
		if (list.size() > 0) {
			HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
			int maxStrikes = this.getMaxStrikes(entitypatch, phase);
			
			while (entitypatch.getCurrenltyAttackedEntities().size() < maxStrikes && hitEntities.next()) {
				Entity hitten = hitEntities.getEntity();
				LivingEntity trueEntity = this.getTrueEntity(hitten);
				
				if (trueEntity != null && trueEntity.isAlive() && !entitypatch.getCurrenltyAttackedEntities().contains(trueEntity) && !entitypatch.isTeammate(hitten)) {
					if (hitten instanceof LivingEntity || hitten instanceof PartEntity) {
						HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(hitten, HurtableEntityPatch.class);
						if (entity.hasLineOfSight(hitten)) {
							EpicFightDamageSource source;
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
							if (hitHurtableEntityPatch.isStunned()) {
								float anti_stunlock = 1;
								for (String tag : hitten.getTags()) {
									if (tag.contains("anti_stunlock:")) {
										anti_stunlock = Float.valueOf(tag.substring(14)) * 0.90f;
										hitten.removeTag(tag);
										break;
									}
								}
								source.setImpact(source.getImpact() * anti_stunlock);
								hitten.addTag("anti_stunlock:"+anti_stunlock);
							} else {
								for (String tag : hitten.getTags()) {
									if (tag.contains("anti_stunlock:")) {
										hitten.removeTag(tag);
										break;
									}
								}
								//entitypatch.playSound(SoundEvents.ARROW_HIT_PLAYER, 1, 1);
							}
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
											hitHurtableEntityPatch.applyStun(StunType.LONG, stunTime);
											float impact = source.getImpact();
											hitHurtableEntityPatch.knockBackEntity(entitypatch.getOriginal().getPosition(1),source.getImpact() * 0.25f);
										}
									}
									
									if (phase.getProperty(AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
										float stunTime = (float) (source.getImpact() * 0.3f * (1.0F - ((LivingEntity) hitten).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
										if (hitHurtableEntityPatch.getOriginal().isAlive()) {
											hitHurtableEntityPatch.setStunReductionOnHit();
											hitHurtableEntityPatch.applyStun(StunType.SHORT, stunTime);
											double power = source.getImpact() * 0.25f;
											double d1 = entity.getX() - hitten.getX();
											double d2 = entity.getY()-5 - hitten.getY();
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
	public void setLinkAnimation(Pose pose1, float timeModifier, LivingEntityPatch<?> entitypatch, LinkAnimation dest) {
		float extTime = Math.max(this.convertTime + timeModifier, 0);
		
		if (entitypatch instanceof PlayerPatch<?>) {
			PlayerPatch<?> playerpatch = (PlayerPatch<?>)entitypatch;
			Phase phase = this.getPhaseByTime(playerpatch.getAnimator().getPlayerFor(this).getElapsedTime());
			extTime *= (float)(this.totalTime * playerpatch.getAttackSpeed(phase.getHand()));
		}
		
		extTime = Math.max(extTime - this.convertTime, 0);
		super.setLinkAnimation(pose1, extTime, entitypatch, dest);
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
		return true;
	}
}
