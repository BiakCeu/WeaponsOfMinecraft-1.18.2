package reascer.wom.skill;

import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SpecialAttackSkill;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.utils.math.Formulars;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class CharybdisSkill extends SpecialAttackSkill {
	public static class Builder extends Skill.Builder<CharybdisSkill> {
		protected StaticAnimation attackAnimation;
		
		public Builder(ResourceLocation resourceLocation) {
			super(resourceLocation);
		}
		
		public Builder setCategory(SkillCategory category) {
			this.category = category;
			return this;
		}
		
		public Builder setConsumption(float consumption) {
			this.consumption = consumption;
			return this;
		}
		
		public Builder setMaxDuration(int maxDuration) {
			this.maxDuration = maxDuration;
			return this;
		}
		
		public Builder setMaxStack(int maxStack) {
			this.maxStack = maxStack;
			return this;
		}
		
		public Builder setRequiredXp(int requiredXp) {
			this.requiredXp = requiredXp;
			return this;
		}
		
		public Builder setActivateType(ActivateType activateType) {
			this.activateType = activateType;
			return this;
		}
		
		public Builder setResource(Resource resource) {
			this.resource = resource;
			return this;
		}
		
		public Builder setAnimations(StaticAnimation attackAnimation) {
			this.attackAnimation = attackAnimation;
			return this;
		}
	}
	
	public static Builder createBuilder(ResourceLocation resourceLocation) {
		return (new Builder(resourceLocation)).setCategory(SkillCategories.WEAPON_SPECIAL_ATTACK).setResource(Resource.NONE);
	}
	
	protected final StaticAnimation attackAnimation;
	
	public CharybdisSkill(Builder builder) {
		super(builder);
		this.attackAnimation = builder.attackAnimation;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		if(!container.getExecuter().isLogicalClient()) {
			this.setConsumption(container, 6.0f);
			this.setConsumptionSynchronize((ServerPlayerPatch) container.getExecuter(), 6.0f);
		}
		container.setResource(6.0f);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getStamina() - Formulars.getStaminarConsumePenalty(executer.getWeight(), this.consumption - EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal()), executer) >= 0 ) {
			executer.setStamina(executer.getStamina() - Formulars.getStaminarConsumePenalty(executer.getWeight(), this.consumption - EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal()), executer));
			executer.playAnimationSynchronized(this.attackAnimation, 0);
		super.executeOnServer(executer, args);
		}
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Each Strike:");
		
		return list;
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		executer.updateEntityState();
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginal().isFallFlying() || executer.currentLivingMotion == LivingMotions.FALL || !playerState.canUseSkill() || !executer.getEntityState().canBasicAttack());
	}
	
	@Override
	public SpecialAttackSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		if(!container.getExecuter().isLogicalClient()) {
			this.setConsumption(container, 6.0f);
			this.setConsumptionSynchronize((ServerPlayerPatch) container.getExecuter(), 6.0f);
		}
		container.setResource(6.0f);
		container.deactivate();
	}
	
}