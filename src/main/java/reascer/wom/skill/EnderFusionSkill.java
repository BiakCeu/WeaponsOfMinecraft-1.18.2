package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.ibm.icu.util.RangeDateRule;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.gameasset.EFColliders;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class EnderFusionSkill extends ConditionalWeaponInnateSkill {
	private static final SkillDataKey<Integer> COMBO = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> ZOOM = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> NOFALLDAMAGE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("b9023f5e-ee42-11ec-8ea0-0242ac120002");
    private static final int cooldown = 60;
	public EnderFusionSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			int combo = executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO);
			return combo;
			
		},  EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_1, 
			EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_2, 
			EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_3,
			EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,
			EFAnimations.ENDERBLASTER_TWOHAND_PISTOLERO,
			EFAnimations.ENDERBLASTER_TWOHAND_AIRSHOOT);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(COMBO);
		container.getDataManager().registerData(COOLDOWN);
		container.getDataManager().registerData(ZOOM);
		container.getDataManager().registerData(NOFALLDAMAGE);
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation().getId() != EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_1.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_2.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_3.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_TWOHAND_SHOOT_4.getId()
					) {
				if(!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(COOLDOWN, cooldown, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(ZOOM, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				}
			}
		});
		
	}

	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public FriendlyByteBuf gatherArguments(LocalPlayerPatch executer, ControllEngine controllEngine) {
        int forward = controllEngine.isKeyDown(Minecraft.getInstance().options.keyUp) ? 1 : 0;
        int backward = controllEngine.isKeyDown(Minecraft.getInstance().options.keyDown) ? -1 : 0;
        int left = controllEngine.isKeyDown(Minecraft.getInstance().options.keyLeft) ? 1 : 0;
        int right = controllEngine.isKeyDown(Minecraft.getInstance().options.keyRight) ? -1 : 0;
		
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(forward);
		buf.writeInt(backward);
		buf.writeInt(left);
		buf.writeInt(right);
		
		return buf;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public Object getExecutionPacket(LocalPlayerPatch executer, FriendlyByteBuf args) {
		int forward = args.readInt();
		int backward = args.readInt();
		int left = args.readInt();
		int right = args.readInt();
		int vertic = forward + backward;
		int horizon = left + right;
		int animation;
		
		if (vertic == 0) {
			if (horizon == 0) {
				animation = -2;
			} else {
				animation = horizon >= 0 ? 1 : 2;
			}
		} else {
			animation = vertic <= 0 ? -2 : 0;
		}
		
		CPExecuteSkill packet = new CPExecuteSkill(this.category.universalOrdinal());
		packet.getBuffer().writeInt(animation);
		
		return packet;
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		int i = args.readInt();
		ServerPlayer player = executer.getOriginal();
		if ((!player.isOnGround() && !player.isInWater()) && player.fallDistance < 0.1f && (player.level.isEmptyBlock(player.blockPosition().below()) || (player.yo - player.blockPosition().getY()) > 0.2D)) {
			executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 1], 0);
			executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(NOFALLDAMAGE, true, executer.getOriginal());
		} else {
			if(executer.getOriginal().isSprinting()) {
				executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 2], 0);
				executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(NOFALLDAMAGE, true, executer.getOriginal());
			} else {
				executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
				if (executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO) < 3) {
					executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COMBO, executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO)+1, executer.getOriginal());	
				}
				else {
					executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COMBO, 0, executer.getOriginal());
				}
				
			}
		}
		executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COOLDOWN, cooldown, executer.getOriginal());
		executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(ZOOM, true, executer.getOriginal());
		this.setStackSynchronize(executer, executer.getSkill(SkillCategories.WEAPON_INNATE).getStack()-1);
		if (executer.getSkill(SkillCategories.WEAPON_INNATE).getResource() == 0 && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getValidItemInHand(InteractionHand.MAIN_HAND)) + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getValidItemInHand(InteractionHand.OFF_HAND)) == 0) {
			this.setStackSynchronize(executer, executer.getSkill(SkillCategories.WEAPON_INNATE).getStack()+1);
		}
		this.setConsumptionSynchronize(executer,executer.getSkill(SkillCategories.WEAPON_INNATE).getResource()
				+ (1.0F * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getValidItemInHand(InteractionHand.MAIN_HAND)))
				+ (executer.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCollider() == EFColliders.ENDER_BLASTER ? 
						(1.0F * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getValidItemInHand(InteractionHand.OFF_HAND))):
						0));		
		executer.getSkill(this.category).activate();
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		executer.updateEntityState();
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginal().isFallFlying() || executer.currentLivingMotion == LivingMotions.FALL || !playerState.canUseSkill() || !executer.getEntityState().canBasicAttack());
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Bullet blast :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Shotgun blast :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(2), "Charged beam :");
		
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
			container.getDataManager().setData(COOLDOWN, container.getDataManager().getDataValue(COOLDOWN)-1);
			if(container.getExecuter().isLogicalClient()) {
				if (container.getDataManager().getDataValue(ZOOM)) {
					ClientEngine.instance.renderEngine.zoomIn();
				}
			} else {
				if (container.getDataManager().getDataValue(NOFALLDAMAGE) && container.getDataManager().getDataValue(COOLDOWN) > cooldown-22) {
					container.getExecuter().getOriginal().resetFallDistance();
				} else {
					container.getExecuter().getSkill(this.category).getDataManager().setDataSync(NOFALLDAMAGE, false,((ServerPlayerPatch)container.getExecuter()).getOriginal());
				}
			}
		} else {
			if(!container.getExecuter().isLogicalClient()) {
				container.getExecuter().getSkill(this.category).getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
			if(container.getExecuter().isLogicalClient()) {
				ClientEngine.instance.renderEngine.zoomOut(0);
			}
		}
	}
}