package reascer.wom.skill;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.gameasset.EFColliders;
import reascer.wom.world.capabilities.item.WomWeaponCategories;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Skills;
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
	
	public static GuardSkill.Builder createBuilder(ResourceLocation resourceLocation) {
		return GuardSkill.createBuilder(resourceLocation)
				.addAdvancedGuardMotion(WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == Styles.ONE_HAND ?
					Animations.SWORD_DASH :
					Animations.SWORD_DUAL_AUTO3)
				.addAdvancedGuardMotion(WeaponCategories.LONGSWORD, (itemCap, playerpatch) ->
					Animations.LONGSWORD_DASH)
				.addAdvancedGuardMotion(WeaponCategories.TACHI, (itemCap, playerpatch) ->
					Animations.TACHI_DASH)
				.addAdvancedGuardMotion(WeaponCategories.SPEAR, (itemCap, playerpatch) ->
					Animations.SPEAR_DASH)
				.addAdvancedGuardMotion(WomWeaponCategories.AGONY, (itemCap, playerpatch) ->
					EFAnimations.AGONY_DASH)
				.addAdvancedGuardMotion(WomWeaponCategories.RUINE, (itemCap, playerpatch) ->
					EFAnimations.RUINE_COUNTER)
				.addAdvancedGuardMotion(WomWeaponCategories.STAFF, (itemCap, playerpatch) ->
					EFAnimations.STAFF_DASH)
				.addAdvancedGuardMotion(WeaponCategories.KATANA, (itemCap, playerpatch) ->
					EFAnimations.KATANA_SHEATHED_COUNTER );
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
			
			container.getDataManager().setData(LAST_ACTIVE, event.getPlayerPatch().getOriginal().tickCount);
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
				boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 4;
				float penalty = container.getDataManager().getDataValue(PENALTY);
				event.getPlayerPatch().playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
				EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(((ServerLevel)playerentity.level), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());
				
				if (successParrying) {
					penalty = 0.1F;
					knockback *= 0.4F;
				} else {
					penalty += this.getPenaltyMultiplier(itemCapability);
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
					event.getPlayerPatch().playAnimationSynchronized(animation, -0.2F);
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
			if (itemCapability.getWeaponCollider() == EFColliders.AGONY) {
				return EFAnimations.AGONY_AUTO_2;
			} else if (itemCapability.getWeaponCollider() == EFColliders.RUINE) {
				return EFAnimations.RUINE_COUNTER;
			} else if (itemCapability.getWeaponCollider() == EFColliders.STAFF) {
				return EFAnimations.STAFF_DASH;
			}
			
			
			StaticAnimation motion = (StaticAnimation)this.getGuradMotionMap(blockType).getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, playerpatch);
			if (motion != null) {
				return motion;
			}
		}
		
		return super.getGuardMotion(playerpatch, itemCapability, blockType);
	}
	
	@Override
	public Skill getPriorSkill() {
		return Skills.GUARD;
	}
	
	@Override
	protected boolean isAdvancedGuard() {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public List<Object> getTooltipArgs() {
		List<Object> list = Lists.<Object>newArrayList();
		list.add(String.format("%s, %s, %s, %s, %s", WeaponCategories.KATANA, WeaponCategories.LONGSWORD, WeaponCategories.SWORD, WeaponCategories.TACHI, WeaponCategories.SPEAR, WomWeaponCategories.AGONY , WomWeaponCategories.RUINE, WomWeaponCategories.STAFF).toLowerCase());
		return list;
	}
}