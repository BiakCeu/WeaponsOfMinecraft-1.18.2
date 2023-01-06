package reascer.wom.skill;

import net.minecraft.network.FriendlyByteBuf;
import reascer.wom.gameasset.EFAnimations;
import yesman.epicfight.skill.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class BlossomSkill extends ConditionalWeaponInnateSkill {
	private static final SkillDataKey<Integer> COMBO = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	public BlossomSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			int combo = executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO);
			return combo;
			
		},  EFAnimations.TACHI_TWOHAND_BLOSSOM_CHARGE,
			EFAnimations.TACHI_TWOHAND_BLOSSOM_SLASHES,
			EFAnimations.TACHI_TWOHAND_BLOSSOM_FINAL);
	}
	
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(COMBO);
		container.getDataManager().registerData(COOLDOWN);
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(COMBO, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			container.getDataManager().setDataSync(COOLDOWN, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		switch (executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO)) {
			case 0: {
				executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COOLDOWN, 8, executer.getOriginal());
				break;
			}
			case 1: {
				executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COOLDOWN, 38, executer.getOriginal());
				break;
			}
			case 2: {
				executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COOLDOWN, 30, executer.getOriginal());
				break;
			}
		}
		if (executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO) < 2) {
			executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COMBO, executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(COMBO)+1, executer.getOriginal());
		}
		else {
			executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(COMBO, 0, executer.getOriginal());
			this.setConsumptionSynchronize(executer, 0);
			this.setStackSynchronize(executer, executer.getSkill(this.category).getStack() - 1);
			this.setDurationSynchronize(executer, this.maxDuration);
		}
		executer.getSkill(this.category).activate();
	}
	
}