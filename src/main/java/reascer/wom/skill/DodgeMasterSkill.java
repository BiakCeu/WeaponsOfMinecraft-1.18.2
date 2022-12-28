package reascer.wom.skill;

import java.util.UUID;

import com.ibm.icu.util.BytesTrie.Result;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult.ResultType;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class DodgeMasterSkill extends DodgeSkill {
	private static final UUID EVENT_UUID = UUID.fromString("691d9d1e-05ce-11ed-b939-0242ac120002");
	private static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> DIRECTION = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Float> ROTATION = SkillDataKey.createDataKey(SkillDataManager.ValueType.FLOAT);
	
	public DodgeMasterSkill(Builder builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(DIRECTION);
		container.getDataManager().registerData(ROTATION);
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(TIMER) > 0) {
				container.getDataManager().setDataSync(TIMER, 6,event.getPlayerPatch().getOriginal());
				event.getPlayerPatch().playAnimationSynchronized(this.animations[container.getDataManager().getDataValue(DIRECTION)], 0);
				event.getPlayerPatch().changeModelYRot(container.getDataManager().getDataValue(ROTATION));
				event.setCanceled(true);
				event.setResult(ResultType.FAILED);
			}
        });
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_PRE, EVENT_UUID);
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
		int degree = vertic == 0 ? 0 : -(90 * horizon * (1 - Math.abs(vertic)) + 45 * vertic * horizon);
		int animation;
		
		if (vertic == 0) {
			if (horizon == 0) {
				animation = 1;
			} else {
				animation = horizon >= 0 ? 2 : 3;
			}
		} else {
			animation = vertic >= 0 ? 0 : 1;
		}
		
		CPExecuteSkill packet = new CPExecuteSkill(this.category.universalOrdinal());
		packet.getBuffer().writeInt(animation);
		packet.getBuffer().writeFloat(degree);
		
		return packet;
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
		int i = args.readInt();
		float yaw = args.readFloat();
		if (executer.getSkill(this.category).getDataManager().getDataValue(TIMER) > 0) {
			executer.playAnimationSynchronized(this.animations[i], 0);
			executer.changeModelYRot(yaw);
			executer.setStamina(executer.getStamina() - 3f);
		}
		executer.getSkill(this.category).getDataManager().setDataSync(TIMER, 6,executer.getOriginal());
		executer.getSkill(this.category).getDataManager().setDataSync(DIRECTION, i,executer.getOriginal());
		executer.getSkill(this.category).getDataManager().setDataSync(ROTATION, yaw,executer.getOriginal());
	}
	
	@Override
	public Skill getPriorSkill() {
		return EpicFightSkills.STEP;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
	super.updateContainer(container);
		if (container.getDataManager().getDataValue(TIMER) > 0) {
			if(!container.getExecuter().isLogicalClient()) {
				container.getDataManager().setDataSync(TIMER, container.getDataManager().getDataValue(TIMER)-1,((ServerPlayerPatch) container.getExecuter()).getOriginal());
				if (container.getDataManager().getDataValue(TIMER) == 0) {
				}
			}
		}
	}
}
