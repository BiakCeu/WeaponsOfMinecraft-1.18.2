package reascer.wom.skill;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMColliders;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class CounterAttack extends GuardSkill {
	private static final UUID EVENT_UUID = UUID.fromString("ad8def54-20a4-4806-be95-ce3f5054627c");
	private static final SkillDataKey<Integer> LAST_ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> PARRYING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	
	public static GuardSkill.Builder createCounterAttackBuilder() {
		return GuardSkill.createGuardBuilder()
				.addAdvancedGuardMotion(WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == Styles.ONE_HAND ?
					Animations.SWORD_AUTO3 :
					Animations.SWORD_DUAL_AUTO3)
				.addAdvancedGuardMotion(WeaponCategories.LONGSWORD, (itemCap, playerpatch) ->
					Animations.LONGSWORD_DASH)
				.addAdvancedGuardMotion(WeaponCategories.TACHI, (itemCap, playerpatch) ->
					Animations.TACHI_DASH)
				.addAdvancedGuardMotion(WeaponCategories.SPEAR, (itemCap, playerpatch) ->
					Animations.SPEAR_DASH)
				.addAdvancedGuardMotion(WOMWeaponCategories.AGONY, (itemCap, playerpatch) ->
					WOMAnimations.AGONY_CLAWSTRIKE)
				.addAdvancedGuardMotion(WOMWeaponCategories.RUINE, (itemCap, playerpatch) ->
					WOMAnimations.RUINE_COUNTER)
				.addAdvancedGuardMotion(WOMWeaponCategories.STAFF, (itemCap, playerpatch) ->
					WOMAnimations.STAFF_DASH)
				.addAdvancedGuardMotion(WeaponCategories.KATANA, (itemCap, playerpatch) ->
					WOMAnimations.KATANA_SHEATHED_DASH )
				;
	}
	
	public CounterAttack(GuardSkill.Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(LAST_ACTIVE);
		container.getDataManager().registerData(PARRYING);
		container.getDataManager().setData(PARRYING, false);
		
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, CounterAttack.EVENT_UUID, (event) -> {
			CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND);
			
			if (event.getPlayerPatch().getOriginal().tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) > 20) {
				container.getDataManager().setDataSync(PARRYING, false,event.getPlayerPatch().getOriginal());
			}
			
			if (!container.getDataManager().getDataValue(PARRYING) && event.getPlayerPatch().getStamina() > 0) {
				if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD) && event.getPlayerPatch().getOriginal().tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) > 20 && !(event.getPlayerPatch().getOriginal().isFallFlying() || event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL || !event.getPlayerPatch().getEntityState().canUseSkill() || !event.getPlayerPatch().getEntityState().canBasicAttack())) {
					StaticAnimation animation = new Random().nextBoolean() ? Animations.SWORD_GUARD_ACTIVE_HIT1 : Animations.SWORD_GUARD_ACTIVE_HIT2;
					float convert = -0.05F;
					
					if (itemCapability.getWeaponCollider() == WOMColliders.AGONY) {
						animation = WOMAnimations.AGONY_GUARD_HIT_1;
					}
					
					if (itemCapability.getWeaponCollider() == WOMColliders.RUINE) {
						animation = Animations.LONGSWORD_GUARD_HIT;
					}
					
					if (itemCapability.getWeaponCollider() == WOMColliders.KATANA) {
							animation = WOMAnimations.KATANA_GUARD_HIT;
							convert = -0.15F;
							//((ServerLevel) event.getPlayerPatch().getOriginal().level).sendParticles(WOMParticles.KATANA_SHEATHED_CUT.get(), event.getPlayerPatch().getOriginal().getX(), event.getPlayerPatch().getOriginal().getY() + (event.getPlayerPatch().getOriginal().getBbHeight()/2), event.getPlayerPatch().getOriginal().getZ(), 0, 0, 0,1 ,0);
							//WOMParticles.KATANA_SHEATHED_HIT.get().spawnParticleWithArgument(((ServerLevel) event.getPlayerPatch().getOriginal().level), null, null, event.getPlayerPatch().getOriginal(), event.getPlayerPatch().getOriginal());
					}
					
					if(event.getPlayerPatch().getStamina() - 3 < 0){
						event.getPlayerPatch().setStamina(0);
					} else {
						event.getPlayerPatch().setStamina(event.getPlayerPatch().getStamina() - 3f);
					}
					event.getPlayerPatch().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
			    			EpicFightSounds.CLASH, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 2.0F);
					event.getPlayerPatch().playAnimationSynchronized(animation, convert);
					event.getPlayerPatch().currentLivingMotion = LivingMotions.BLOCK;
						container.getDataManager().setDataSync(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount,event.getPlayerPatch().getOriginal());
						container.getDataManager().setDataSync(PARRYING, true,event.getPlayerPatch().getOriginal());
					
					
				} else {
					if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
						event.getPlayerPatch().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
								SoundEvents.LAVA_EXTINGUISH, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 2.0F);
						container.getDataManager().setDataSync(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount - 4,event.getPlayerPatch().getOriginal());
					}
				}
			}
			
			if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
				if (event.getPlayerPatch().getStamina() == 0 || event.getPlayerPatch().getOriginal().tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) > 10) {
					event.getPlayerPatch().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
							SoundEvents.LAVA_EXTINGUISH, container.getExecuter().getOriginal().getSoundSource(), 1.0F, 2.0F);
					container.getDataManager().setDataSync(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount - 4,event.getPlayerPatch().getOriginal());
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(event.getPlayerPatch().getOriginal().getUsedItemHand());
			
			if (event.getPlayerPatch().getOriginal().tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 10 && container.getDataManager().getDataValue(PARRYING) && !event.getPlayerPatch().getOriginal().isUsingItem()) {
				DamageSource damageSource = event.getDamageSource();
				boolean isFront = false;
				Vec3 sourceLocation = damageSource.getSourcePosition();
				
				if (sourceLocation != null) {
					Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
					Vec3 toSourceLocation = sourceLocation.subtract(event.getPlayerPatch().getOriginal().position()).normalize();
					
					if (toSourceLocation.dot(viewVector) > 0.0D) {
						isFront = true;
					}
				}
				
				if (isFront) {
					float impact = 0.5F;
					float knockback = 0.25F;
					
					if (event.getDamageSource() instanceof EpicFightDamageSource) {
						impact = ((EpicFightDamageSource)event.getDamageSource()).getImpact();
						knockback += Math.min(impact * 0.1F, 1.0F);
					}
					
					this.guard(container, itemCapability, event, knockback, impact, false);
				}
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.SERVER_ITEM_USE_EVENT, CounterAttack.EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_PRE, CounterAttack.EVENT_UUID);
		super.onRemoved(container);
	}
	
	@Override
	public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced) {
		if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
			DamageSource damageSource = event.getDamageSource();
			
			if (this.isBlockableSource(damageSource, true)) {
				ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
				int timing = (playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE));
					//container.getExecuter().getOriginal().sendMessage(new TextComponent("timming : " + timing), UUID.randomUUID());
				
				boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 4;
				
				float penalty = container.getDataManager().getDataValue(PENALTY);
				event.getPlayerPatch().playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
				EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(((ServerLevel)playerentity.level), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
				
				if (successParrying) {
					penalty = 0.1F;
					knockback *= 0.4F;
				} else {
					penalty += this.getPenalizer(itemCapability);
					container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
				}
				
				if (damageSource.getDirectEntity() instanceof LivingEntity) {
					knockback += EnchantmentHelper.getKnockbackBonus((LivingEntity)damageSource.getDirectEntity()) * 0.1F;
				}
				
				event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
				
				float stamina = event.getPlayerPatch().getStamina() - penalty * impact;
				event.getPlayerPatch().setStamina(stamina);
				
				BlockType blockType = successParrying ? BlockType.ADVANCED_GUARD : stamina >= 0.0F ? BlockType.GUARD : BlockType.GUARD_BREAK;
				StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
				
				if (animation != null) {
					event.getPlayerPatch().playAnimationSynchronized(animation, timing <= 1 ? -0.1F : timing < 3 ? -0.05f : 0.0f);
				}
				
				if (blockType == BlockType.GUARD_BREAK) {
					event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
				}
				
				this.dealEvent(event.getPlayerPatch(), event,advanced);
				
				return;
			}
		}
		
		//super.guard(container, itemCapability, event, knockback, impact, false);
	}
	
	@Override
	protected boolean isBlockableSource(DamageSource damageSource, boolean advanced) {
		return (damageSource.isProjectile() && advanced) || super.isBlockableSource(damageSource, false);
	}
	
	@Nullable
	protected StaticAnimation getGuardMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, BlockType blockType) {
		if (blockType == BlockType.ADVANCED_GUARD) {
			if (itemCapability.getWeaponCollider() == WOMColliders.AGONY) {
				return WOMAnimations.AGONY_COUNTER;
			} else if (itemCapability.getWeaponCollider() == WOMColliders.RUINE) {
				return WOMAnimations.RUINE_COUNTER;
			} else if (itemCapability.getWeaponCollider() == WOMColliders.STAFF) {
				return WOMAnimations.STAFF_AUTO_3;
			}
			
			
			StaticAnimation motion = (StaticAnimation)this.getGuradMotionMap(blockType).getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, playerpatch);
			if (motion != null) {
				return motion;
			}
		}
		
		if (blockType == BlockType.GUARD) {
			if (itemCapability.getWeaponCollider() == WOMColliders.AGONY) {
				return new Random().nextBoolean() ? WOMAnimations.AGONY_GUARD_HIT_1 : WOMAnimations.AGONY_GUARD_HIT_2;
			}
			
			if (itemCapability.getWeaponCollider() == WOMColliders.KATANA) {
				return WOMAnimations.KATANA_GUARD_HIT;
			}			
		}
		

		return super.getGuardMotion(playerpatch, itemCapability, blockType);
	}
	
	@Override
	public Skill getPriorSkill() {
		return EpicFightSkills.PARRYING;
	}
	
	@Override
	protected boolean isAdvancedGuard() {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public List<Object> getTooltipArgs(List<Object> list) {
		list.clear();
		list.add(String.format("%s, %s, %s, %s, %s, %s, %s, %s", WeaponCategories.KATANA, WeaponCategories.LONGSWORD, WeaponCategories.SWORD, WeaponCategories.TACHI, WeaponCategories.SPEAR, WOMWeaponCategories.AGONY , WOMWeaponCategories.RUINE, WOMWeaponCategories.STAFF).toLowerCase());
		return list;
	}
}