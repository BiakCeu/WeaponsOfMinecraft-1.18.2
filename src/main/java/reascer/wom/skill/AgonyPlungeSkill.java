package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SpecialAttackSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;


public class AgonyPlungeSkill extends SpecialAttackSkill {
	private static final UUID EVENT_UUID = UUID.fromString("b9d719ba-bcb8-11ec-8422-0242ac120002");
	private static final SkillDataKey<Boolean> PLUNGING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> STACK = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static class Builder extends Skill.Builder<AgonyPlungeSkill> {
	
		protected StaticAnimation attackAnimation;
		
		public Builder(ResourceLocation resourceLocation) {
			super(resourceLocation);
		}
		
		public Builder setCategory(SkillCategories category) {
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
		return (new Builder(resourceLocation)).setCategory(SkillCategories.WEAPON_SPECIAL_ATTACK).setResource(Resource.SPECIAL_GAUAGE);
	}
	
	protected final StaticAnimation attackAnimation;
	
	public AgonyPlungeSkill(Builder builder) {
		super(builder);
		this.attackAnimation = builder.attackAnimation;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(PLUNGING);
		container.getDataManager().setData(PLUNGING, false);
		
		container.getDataManager().registerData(STACK);
		container.getDataManager().setData(STACK, 0);
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(PLUNGING) && container.getDataManager().getDataValue(STACK) > 0 && event.getAttackDamage() > 1.0F) {
				float attackDamage = event.getAttackDamage();
				event.setAttackDamage(attackDamage * container.getDataManager().getDataValue(STACK));
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(PLUNGING) && container.getDataManager().getDataValue(STACK) > 0) {
				container.getDataManager().setData(PLUNGING, false);
				container.getDataManager().setData(STACK, 0);
			}
		});
		
		
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT, EVENT_UUID);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		executer.playAnimationSynchronized(this.attackAnimation, 0);
		executer.getSkill(this.category).getDataManager().setData(PLUNGING, true);
		executer.getSkill(this.category).getDataManager().setData(STACK, executer.getSkill(this.category).getStack());
		this.setStackSynchronize(executer, 0);
		super.executeOnServer(executer, args);
		executer.getSkill(this.category).deactivate();
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Jump :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(2), "Plunge :");
		
		return list;
	}
	
	@Override
	public SpecialAttackSkill registerPropertiesToAnimation() {
		AttackAnimation anim = ((AttackAnimation)this.attackAnimation);
		for(Phase phase : anim.phases) {
			phase.addProperties(this.properties.get(0).entrySet());
		}
		
		return this;
	}
}