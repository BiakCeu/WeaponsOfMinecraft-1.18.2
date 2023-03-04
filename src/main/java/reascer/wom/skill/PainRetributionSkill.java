package reascer.wom.skill;

import java.util.UUID;

import reascer.wom.gameasset.WOMSkills;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class PainRetributionSkill extends PainAnticipationSkill {
	private static final UUID EVENT_UUID = UUID.fromString("44fd425a-043d-11ed-b939-0242ac120002");
	
	public PainRetributionSkill(Builder<? extends Skill> builder) {
		super(builder);
		maxtimer = 80;
		maxduree = 80;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(ACTIVE)) {
				float attackDamage = event.getAttackDamage();
				event.setAttackDamage(attackDamage * 1.2f);
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
	}
	
	@Override
	public Skill getPriorSkill() {
		return WOMSkills.PAIN_ANTICIPATION;
	}
}