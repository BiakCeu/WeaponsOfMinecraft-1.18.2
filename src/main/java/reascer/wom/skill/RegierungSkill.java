package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.world.capabilities.item.WOMWeaponCategories;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.SkillEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class RegierungSkill extends WomMultipleAnimationSkill {
	private static final SkillDataKey<Integer> COMBO = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	private static final UUID EVENT_UUID = UUID.fromString("63c38d4f-cc97-4339-bedf-d9bba36ba29f");
    
	public RegierungSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			int combo = executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(COMBO);
			return combo;
			
		},  WOMAnimations.GESETZ_AUTO_1, 
			WOMAnimations.GESETZ_AUTO_2, 
			WOMAnimations.GESETZ_AUTO_3);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(COMBO);
		container.getDataManager().registerData(COOLDOWN);
		
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if(!container.getExecuter().isLogicalClient()) {
				container.getDataManager().setDataSync(COOLDOWN, 40, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
		});
	}

	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
	}
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		ServerPlayer player = executer.getOriginal();
		if ((!player.isOnGround() && !player.isInWater()) && player.fallDistance < 0.1f && (player.level.isEmptyBlock(player.blockPosition().below()) || (player.yo - player.blockPosition().getY()) > 0.2D)) {
			executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 3], 0);
		} else {
			if(executer.getOriginal().isSprinting()) {
				executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 1], 0);
				executer.getSkill(this).getDataManager().setDataSync(COMBO, 0, executer.getOriginal());
			} else {
				int animation = this.getAnimationInCondition(executer);
				executer.playAnimationSynchronized(this.attackAnimations[animation], 0);
				if (executer.getSkill(this).getDataManager().getDataValue(COMBO) < 2) {
					executer.getSkill(this).getDataManager().setDataSync(COMBO, executer.getSkill(this).getDataManager().getDataValue(COMBO)+1, executer.getOriginal());
				}
				else {
					executer.getSkill(this).getDataManager().setDataSync(COMBO, 0, executer.getOriginal());
				}
				
			}
		}
		executer.getSkill(this).getDataManager().setDataSync(COOLDOWN, 40, executer.getOriginal());
		executer.getSkill(this).activate();
	}
	
	@Override
	public boolean canExecute(PlayerPatch<?> executer) {
		if (executer.isLogicalClient()) {
			return true;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		executer.updateEntityState();
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginal().isFallFlying() || executer.currentLivingMotion == LivingMotions.FALL || !playerState.canUseSkill() || !executer.getEntityState().canBasicAttack());
	}
	
	@Override
	public boolean resourcePredicate(PlayerPatch<?> playerpatch) {
		return true;
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = Lists.<Component>newArrayList();
		
		return list;
	}
	
	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(COOLDOWN) > 0) {
			if(!container.getExecuter().isLogicalClient()) {
				container.getDataManager().setDataSync(COOLDOWN, container.getDataManager().getDataValue(COOLDOWN)-1,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
		} else {
			if(!container.getExecuter().isLogicalClient()) {
				container.getExecuter().getSkill(this).getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
		}
	}
}