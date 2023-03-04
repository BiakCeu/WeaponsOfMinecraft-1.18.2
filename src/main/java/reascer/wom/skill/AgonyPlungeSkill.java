package reascer.wom.skill;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.IndirectEpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;


public class AgonyPlungeSkill extends WeaponInnateSkill {
	private static final UUID EVENT_UUID = UUID.fromString("c7a0ee46-56b3-4008-9fba-d2594b1e2676");
	private static final SkillDataKey<Boolean> PLUNGING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> STACK = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private StaticAnimation attackAnimations;
	
	public AgonyPlungeSkill(Builder builder) {
		super(builder);
		this.attackAnimations = WOMAnimations.AGONY_PLUNGE_FORWARD;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(PLUNGING);
		container.getDataManager().setData(PLUNGING, false);
		
		container.getDataManager().registerData(STACK);
		container.getDataManager().setData(STACK, 0);
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			//container.getExecuter().getOriginal().sendMessage(new TextComponent("Plunging: " + container.getDataManager().getDataValue(PLUNGING) + " | Stack: " + container.getDataManager().getDataValue(STACK) + " | Plunging: " + event.getAttackDamage() ), UUID.randomUUID());
			if (container.getDataManager().getDataValue(PLUNGING) && container.getDataManager().getDataValue(STACK) > 0 && event.getAttackDamage() > 1.0F) {
				float attackDamage = event.getAttackDamage();
				event.setAttackDamage(attackDamage * container.getDataManager().getDataValue(STACK));
				//container.getExecuter().getOriginal().sendMessage(new TextComponent("Plunge attack damge: " + (attackDamage * container.getDataManager().getDataValue(STACK))), UUID.randomUUID());
				container.getExecuter().getOriginal().resetFallDistance();
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(PLUNGING) && container.getDataManager().getDataValue(STACK) > 0) {
				container.getDataManager().setData(PLUNGING, false);
				container.getDataManager().setData(STACK, 0);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(PLUNGING) && container.getDataManager().getDataValue(STACK) > 0) {
				container.getDataManager().setData(PLUNGING, false);
				container.getDataManager().setData(STACK, 0);
			}
		});
		
		
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		executer.playAnimationSynchronized(this.attackAnimations, 0);
		executer.getSkill(this.category).getDataManager().setDataSync(PLUNGING, true, executer.getOriginal());
		executer.getSkill(this.category).getDataManager().setDataSync(STACK, executer.getSkill(this.category).getStack(), executer.getOriginal());
		//executer.getOriginal().sendMessage(new TextComponent("number of stack: " + executer.getSkill(this.category).getDataManager().getDataValue(STACK)), UUID.randomUUID());
		//executer.setStamina(executer.getStamina() - (5f - EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())));
		if (!executer.getOriginal().isCreative()) {
			executer.getOriginal().level.playSound(null, executer.getOriginal().xo, executer.getOriginal().yo, executer.getOriginal().zo,
	    			SoundEvents.PLAYER_HURT, executer.getOriginal().getSoundSource(), 1.0F, 1.0F);
			DamageSource damage = new IndirectEpicFightDamageSource("agonized_to_death", executer.getOriginal(), executer.getOriginal(), StunType.NONE).bypassArmor().bypassMagic();
			executer.getOriginal().hurt(damage, executer.getOriginal().getHealth() * (0.40f - (0.10f * EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal()))));
		}
		this.setStackSynchronize(executer, 0);
		super.executeOnServer(executer, args);
		executer.getSkill(this.category).deactivate();
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Jump :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Plunge :");
		
		return list;
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		executer.updateEntityState();
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginal().isFallFlying() || executer.currentLivingMotion == LivingMotions.FALL || !playerState.canUseSkill() || !executer.getEntityState().canBasicAttack());
	}
	
	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(PLUNGING)) {
			container.getExecuter().getOriginal().resetFallDistance();
		}
	}
}