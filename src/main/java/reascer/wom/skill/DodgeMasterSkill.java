package reascer.wom.skill;

import java.util.UUID;

import com.ibm.icu.util.BytesTrie.Result;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.game.AttackResult;
import yesman.epicfight.api.utils.game.AttackResult.ResultType;
import yesman.epicfight.api.utils.game.ExtendedDamageSource.StunType;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Skills;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class DodgeMasterSkill extends Skill {
	private static final UUID EVENT_UUID = UUID.fromString("691d9d1e-05ce-11ed-b939-0242ac120002");
	private static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> DIRECTION = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Float> ROTATION = SkillDataKey.createDataKey(SkillDataManager.ValueType.FLOAT);
	
	public static class Builder extends Skill.Builder<DodgeSkill> {
		protected StaticAnimation[] animations;
		
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
		
		public Builder setAnimations(StaticAnimation... animations) {
			this.animations = animations;
			return this;
		}
	}
	
	public static Builder createBuilder(ResourceLocation registryName) {
		return (new Builder(registryName)).setCategory(SkillCategories.DODGE).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.STAMINA).setRequiredXp(8);
	}
	
	protected final StaticAnimation[] animations;
	
	public DodgeMasterSkill(Builder builder) {
		super(builder);
		this.animations = builder.animations;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(DIRECTION);
		container.getDataManager().registerData(ROTATION);
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(TIMER) > 0) {
				event.getPlayerPatch().playAnimationSynchronized(this.animations[container.getDataManager().getDataValue(DIRECTION)], 0);
				event.getPlayerPatch().changeYaw(container.getDataManager().getDataValue(ROTATION));
				event.setCanceled(true);
				event.setResult(AttackResult.ResultType.FAILED);
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
	public void executeOnClient(LocalPlayerPatch executer, FriendlyByteBuf args) {
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
		
		EpicFightNetworkManager.sendToServer(packet);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
		int i = args.readInt();
		float yaw = args.readFloat();
		executer.getSkill(this.category).getDataManager().setDataSync(TIMER, 7,executer.getOriginal());
		executer.getSkill(this.category).getDataManager().setDataSync(DIRECTION, i,executer.getOriginal());
		executer.getSkill(this.category).getDataManager().setDataSync(ROTATION, yaw,executer.getOriginal());
	}
	
	@Override
	public Skill getPriorSkill() {
		return Skills.STEP;
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
