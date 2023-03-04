package reascer.wom.skill;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import yesman.epicfight.skill.GuardSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class CounterAttack extends GuardSkill {
	private static final SkillDataKey<Integer> LAST_ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> PARRY_MOTION_COUNTER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	public static GuardSkill.Builder createCounterAttackBuilder() {
		return GuardSkill.createGuardBuilder()
				.addAdvancedGuardMotion(WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == Styles.ONE_HAND ?
					WOMAnimations.SWORD_ONEHAND_AUTO_4 :
					Animations.SWORD_DUAL_COMBO3)
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
				.addAdvancedGuardMotion(WOMWeaponCategories.ANTITHEUS, (itemCap, playerpatch) ->
				WOMAnimations.ANTITHEUS_GUARD_HIT_1)
				.addGuardMotion(WOMWeaponCategories.ANTITHEUS, (item, player) -> 
				WOMAnimations.ANTITHEUS_GUARD_HIT_3)
				.addGuardBreakMotion(WOMWeaponCategories.ANTITHEUS, (item, player) -> Animations.COMMON_GUARD_BREAK)
				;
	}
	
	public CounterAttack(GuardSkill.Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		
		container.getDataManager().registerData(LAST_ACTIVE);
		container.getDataManager().registerData(PARRY_MOTION_COUNTER);
		
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND);
			
			if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
				event.getPlayerPatch().getOriginal().startUsingItem(InteractionHand.MAIN_HAND);
			}
			
			if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD) && event.getPlayerPatch().getOriginal().tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) > 20 && !(event.getPlayerPatch().getOriginal().isFallFlying() || event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL || !event.getPlayerPatch().getEntityState().canUseSkill() || !event.getPlayerPatch().getEntityState().canBasicAttack())) {
				StaticAnimation animation = Animations.SWORD_GUARD_ACTIVE_HIT1;
				if (itemCapability.getWeaponCollider() == WOMColliders.AGONY) {
					animation = new Random().nextBoolean() ? WOMAnimations.AGONY_GUARD_HIT_1 : WOMAnimations.AGONY_GUARD_HIT_2;
				}
				
				event.getPlayerPatch().playAnimationSynchronized(animation, -0.05f);
				container.getDataManager().setData(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount);
			} else {
				container.getDataManager().setData(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount-3);
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
	}
	
	@Override
	public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced) {
		if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.ADVANCED_GUARD)) {
			DamageSource damageSource = event.getDamageSource();
			
			if (this.isBlockableSource(damageSource, true)) {
				ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
				boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 3;
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
					event.getPlayerPatch().playAnimationSynchronized(animation, -0.1F);
				}
				
				if (blockType == BlockType.GUARD_BREAK) {
					event.getPlayerPatch().playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
				}
				
				this.dealEvent(event.getPlayerPatch(), event);
				
				return;
			}
		}
		
		super.guard(container, itemCapability, event, knockback, impact, false);
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
			} else if (itemCapability.getWeaponCollider() == WOMColliders.ANTITHEUS) {
				return WOMAnimations.ANTITHEUS_GUARD_HIT_3;
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
			
			if (itemCapability.getWeaponCollider() == WOMColliders.ANTITHEUS) {
				if (new Random().nextBoolean()) {
					return WOMAnimations.ANTITHEUS_GUARD_HIT_1;
				} else if (new Random().nextBoolean()) {
					return WOMAnimations.ANTITHEUS_GUARD_HIT_2;
				} else {
					return WOMAnimations.ANTITHEUS_GUARD_HIT_3;
				}
			}
		}
		

		return super.getGuardMotion(playerpatch, itemCapability, blockType);
	}
	
	@Override
	public Skill getPriorSkill() {
		return EpicFightSkills.GUARD;
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