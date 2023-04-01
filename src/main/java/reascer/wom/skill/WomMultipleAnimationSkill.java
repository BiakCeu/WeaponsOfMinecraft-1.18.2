package reascer.wom.skill;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public abstract class WomMultipleAnimationSkill extends WeaponInnateSkill {
	
	public static WomMultipleAnimationSkill.Builder createConditionalWeaponInnateBuilder() {
		return (new WomMultipleAnimationSkill.Builder()).setCategory(SkillCategories.WEAPON_INNATE).setResource(Resource.WEAPON_INNATE_ENERGY);
	}
	
	protected final StaticAnimation[] attackAnimations;
	protected final Function<ServerPlayerPatch, Integer> selector;
	
	public WomMultipleAnimationSkill(Builder<? extends Skill> builder, Function<ServerPlayerPatch, Integer> func, StaticAnimation... animations) {
		super(builder);
		this.properties = Lists.<Map<AttackPhaseProperty<?>, Object>>newArrayList();
		this.attackAnimations = animations;
		this.selector = func;
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Each Strikes:");
		
		return list;
	}
	
	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		for (StaticAnimation animation : this.attackAnimations) {
			AttackAnimation anim = ((AttackAnimation)animation);
			for (Phase phase : anim.phases) {
				phase.addProperties(this.properties.get(0).entrySet());
			}
		}
		
		return this;
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		super.executeOnServer(executer, args);
	}
	
	public int getAnimationInCondition(ServerPlayerPatch executer) {
		return selector.apply(executer);
	}
}
