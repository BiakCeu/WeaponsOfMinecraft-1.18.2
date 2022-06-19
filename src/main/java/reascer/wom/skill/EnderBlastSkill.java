package reascer.wom.skill;

import java.util.UUID;

import io.netty.buffer.Unpooled;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules.Key;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.EFAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.SeperativeMotionSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class EnderBlastSkill extends SeperativeMotionSkill {
	private static final SkillDataKey<Integer> COMBO = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> ZOOM = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("b9023f5e-ee42-11ec-8ea0-0242ac120002");

	public EnderBlastSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			int combo = executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().getDataValue(COMBO);
			return combo;
			
		},  EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_1, 
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2, 
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_3, 
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_FORWARD, 
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_LEFT, 
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_RIGHT,
			EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
			EFAnimations.ENDERBLASTER_ONEHAND_AIRSHOOT);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(COMBO);
		container.getDataManager().registerData(COOLDOWN);
		container.getDataManager().registerData(ZOOM);
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT, EVENT_UUID, (event) -> {
			if (event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_1.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_3.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_FORWARD.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_LEFT.getId() ||
				event.getAnimation().getId() != EFAnimations.ENDERBLASTER_ONEHAND_SHOOT_2_RIGHT.getId()
					) {
				if(!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(COOLDOWN, 40, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(ZOOM, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				}
			}
		});
		
	}

	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT, EVENT_UUID);
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
	public void executeOnClient(LocalPlayerPatch executer, FriendlyByteBuf args) {
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
		
		EpicFightNetworkManager.sendToServer(packet);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		int i = args.readInt();
		ServerPlayer player = executer.getOriginal();
		if ((!player.isOnGround() && !player.isInWater())) {
			executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 1], 0);
		} else {
			if(executer.getOriginal().isSprinting()) {
				executer.playAnimationSynchronized(this.attackAnimations[this.attackAnimations.length - 2], 0);
			} else {
				if (i != -2 && executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().getDataValue(COMBO) == 1) {
					executer.playAnimationSynchronized(this.attackAnimations[i+3], 0);
					executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(COMBO, 1, executer.getOriginal());
				} else {
					executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);			
				}
				if (executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().getDataValue(COMBO) < 2) {
					executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(COMBO, executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().getDataValue(COMBO)+1, executer.getOriginal());	
				}
				else {
					executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(COMBO, 0, executer.getOriginal());
				}
				
			}
		}
		executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(COOLDOWN, 40, executer.getOriginal());
		executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(ZOOM, true, executer.getOriginal());
		this.setStackSynchronize(executer, executer.getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getStack()-1);
		executer.getSkill(this.category).activate();
	}
	
	@Override
	public boolean isExecutableState(PlayerPatch<?> executer) {
		executer.updateEntityState();
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginal().isFallFlying() || executer.currentLivingMotion == LivingMotions.FALL || !playerState.canUseSkill() || !executer.getEntityState().canBasicAttack());
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(COOLDOWN) > 0) {
			container.getDataManager().setData(COOLDOWN, container.getDataManager().getDataValue(COOLDOWN)-1);
			if (container.getDataManager().getDataValue(ZOOM)) {
				ClientEngine.instance.renderEngine.zoomIn();
			}
		} else {
			if(!container.getExecuter().isLogicalClient()) {
				container.getExecuter().getSkill(this.category).getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
				ClientEngine.instance.renderEngine.zoomOut(0);
			}
		}
	
	}
}