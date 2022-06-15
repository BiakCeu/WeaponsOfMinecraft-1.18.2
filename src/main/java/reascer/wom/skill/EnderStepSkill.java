package reascer.wom.skill;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import yesman.epicfight.gameasset.Skills;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.StepSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EnderStepSkill extends StepSkill {
	public EnderStepSkill(DodgeSkill.Builder builder) {
		super(builder);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
	}
	
	@Override
	public Skill getPriorSkill() {
		return Skills.STEP;
	}
}
