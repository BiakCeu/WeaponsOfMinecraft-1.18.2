package reascer.wom.skill;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.EFAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.game.ExtendedDamageSource;
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
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class CounterAttack extends GuardSkill {
	private static final SkillDataKey<Integer> LAST_ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> AVAILABLE_WEAPON_TYPES = Maps.<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>>newLinkedHashMap();
	
	static {
		AVAILABLE_WEAPON_TYPES.put(WeaponCategory.KATANA, (item, playerpatch) -> EFAnimations.KATANA_AUTO_3);
		AVAILABLE_WEAPON_TYPES.put(WeaponCategory.LONGSWORD, (item, playerpatch) -> EFAnimations.RUINE_AUTO_2);
		AVAILABLE_WEAPON_TYPES.put(WeaponCategory.SWORD, (item, playerpatch) -> item.getStyle(playerpatch) == Styles.ONE_HAND ? Animations.SWORD_AUTO_3 : Animations.SWORD_DUAL_AUTO3);
		AVAILABLE_WEAPON_TYPES.put(WeaponCategory.TACHI, (item, playerpatch) -> EFAnimations.RUINE_DASH);
		AVAILABLE_WEAPON_TYPES.put(WeaponCategory.SPEAR, (item, playerpatch) -> EFAnimations.AGONY_DASH);
	}
	
	public CounterAttack(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(LAST_ACTIVE);
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND);
			
			if (GuardSkill.AVAILABLE_WEAPON_TYPES.getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, event.getPlayerPatch()) != null && this.isExecutableState(event.getPlayerPatch())) {
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
	public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean reinforced) {
		if (this.getAvailableWeaponTypes(2).getOrDefault(itemCapability.getWeaponCategory(), (a, b) -> null).apply(itemCapability, event.getPlayerPatch()) != null) {
			DamageSource damageSource = event.getDamageSource();
			
			if (this.isBlockableSource(damageSource, true)) {
				ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
				boolean successParrying = playerentity.tickCount - container.getDataManager().getDataValue(LAST_ACTIVE) < 8;
				float penalty = container.getDataManager().getDataValue(PENALTY);
				event.getPlayerPatch().playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
				EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(((ServerLevel)playerentity.level), HitParticleType.POSITION_FRONT_OF_EYE_POSITION, HitParticleType.ARGUMENT_ZERO, playerentity, damageSource.getDirectEntity());
				
				if (successParrying) {
					penalty = 0.1F;
					knockback *= 0.4F;
					//container.getExecuter().playAnimationSynchronized(itemCapability.getAutoAttckMotion(container.getExecuter()).get(itemCapability.getAutoAttckMotion(container.getExecuter()).size()), -0.30F);
				} else {
					penalty += this.getPenaltyStamina(itemCapability);
					container.getDataManager().setDataSync(PENALTY, penalty, playerentity);
				}
				
				if (damageSource.getDirectEntity() instanceof LivingEntity) {
					knockback += EnchantmentHelper.getKnockbackBonus((LivingEntity)damageSource.getDirectEntity()) * 0.1F;
				}
				
				event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
				float stamina = event.getPlayerPatch().getStamina() - penalty * impact;
				event.getPlayerPatch().setStamina(stamina);
				
				StaticAnimation animation = this.getAvailableWeaponTypes(successParrying ? 2 : stamina >= 0.0F ? 1 : 0).get(itemCapability.getWeaponCategory()).apply(itemCapability, container.getExecuter());
				
				if (animation != null) {
					event.getPlayerPatch().playAnimationSynchronized(animation, -0.10f);
				}
				
				if (stamina >= 0.0) {
					if (event.getDamageSource() instanceof ExtendedDamageSource) {
						((ExtendedDamageSource)event.getDamageSource()).setStunType(ExtendedDamageSource.StunType.NONE);
					}
					this.dealEvent(event.getPlayerPatch(), event);
				}
				
				return;
			}
		}
		
		super.guard(container, itemCapability, event, knockback, impact, false);
	}
	
	@Override
	protected boolean isBlockableSource(DamageSource damageSource, boolean highLevelSkill) {
		return (damageSource.isProjectile() && highLevelSkill) || super.isBlockableSource(damageSource, false);
	}
	
	@Override
	public Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> getAvailableWeaponTypes(int meta) {
		if (meta == 2) {
			return AVAILABLE_WEAPON_TYPES;
		} else {
			return super.getAvailableWeaponTypes(meta);
		}
	}
	
	@Override
	public Skill getPriorSkill() {
		return Skills.ACTIVE_GUARD;
	}
	
	@Override
	protected boolean isAdvancedGuard() {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public List<Object> getTooltipArgs() {
		List<Object> list = Lists.<Object>newArrayList();
		list.add(String.format("%s, %s, %s, %s, %s", WeaponCategory.KATANA, WeaponCategory.LONGSWORD, WeaponCategory.SWORD, WeaponCategory.TACHI, WeaponCategory.SPEAR).toLowerCase());
		return list;
	}
}