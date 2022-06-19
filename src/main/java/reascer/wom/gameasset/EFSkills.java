package reascer.wom.gameasset;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.AgonyPlungeSkill;
import reascer.wom.skill.ArrowTenacitySkill;
import reascer.wom.skill.CounterAttack;
import reascer.wom.skill.EFKatanaPassive;
import reascer.wom.skill.EnderBlastSkill;
import reascer.wom.skill.EnderStepSkill;
import reascer.wom.skill.FatalDrawSkill;
import reascer.wom.skill.PlunderPerditionSkill;
import reascer.wom.skill.TrueBerserkSkill;
import yesman.epicfight.api.animation.property.Property.AttackPhaseProperty;
import yesman.epicfight.api.forgeevent.SkillRegistryEvent;
import yesman.epicfight.api.utils.game.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.math.ValueCorrector;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.GuardSkill;
import yesman.epicfight.skill.PassiveSkill;
import yesman.epicfight.skill.SimpleSpecialAttackSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.Skill.Resource;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SpecialAttackSkill;
import yesman.epicfight.skill.StaminaPillagerSkill;
import yesman.epicfight.skill.StepSkill;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFSkills {
	public static Skill ENDERSTEP;
	public static Skill KNIGHT_ROLL;
	
	public static Skill AGONY_PLUNGE;
	public static Skill TRUE_BERSERK;
	
	public static Skill PLUNDER_PERDITION;
	public static Skill COUNTERATTACK_PASSIVE;
	
	public static Skill KATANA_PASSIVE_EF;
	public static Skill FATAL_DRAW_EF;
	public static Skill ARROW_TENACITY;
	public static Skill COUNTER_ATTACK;
	
	public static Skill ENDER_BLAST;
	
	@SubscribeEvent
	public static void registerSkills(SkillRegistryEvent event) {
		ENDERSTEP = event.registerSkill(new EnderStepSkill(DodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "enderstep")).setConsumption(5.0F)
				.setAnimations(EFAnimations.ENDERSTEP_FORWARD, EFAnimations.ENDERSTEP_BACKWARD, EFAnimations.ENDERSTEP_LEFT, EFAnimations.ENDERSTEP_RIGHT)),true);
		
		KNIGHT_ROLL = event.registerSkill(new DodgeSkill(DodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "knight_roll")).setConsumption(3.5F)
				.setAnimations(EFAnimations.KNIGHT_ROLL_FORWARD, EFAnimations.KNIGHT_ROLL_BACKWARD, EFAnimations.KNIGHT_ROLL_LEFT, EFAnimations.KNIGHT_ROLL_RIGHT)),true);
		
		
		AGONY_PLUNGE = event.registerSkill(new AgonyPlungeSkill(AgonyPlungeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "agony_plunge")).setConsumption(30.0F).setMaxStack(3).setAnimations(EFAnimations.AGONY_PLUNGE))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(16))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(16))
				.registerPropertiesToAnimation(),false);
		
		TRUE_BERSERK = event.registerSkill(new TrueBerserkSkill(TrueBerserkSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "true_berserk")).setConsumption(160.0F).setMaxDuration(3).setActivateType(ActivateType.DURATION_INFINITE).setAnimations(EFAnimations.TORMENT_BERSERK_CONVERT))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10))
				.registerPropertiesToAnimation(),false);
		
		PLUNDER_PERDITION = event.registerSkill(new PlunderPerditionSkill(PlunderPerditionSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "plunder_perdition")).setConsumption(160.0F).setActivateType(ActivateType.ONE_SHOT))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.3F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20))
				.newPropertyLine()
				.registerPropertiesToAnimation(),false);
		
		KATANA_PASSIVE_EF = event.registerSkill(new EFKatanaPassive(Skill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "katana_passive_ef"))
				.setCategory(SkillCategories.WEAPON_PASSIVE)
				.setConsumption(2.0F)
				.setActivateType(ActivateType.ONE_SHOT)
				.setResource(Resource.COOLDOWN)),false);
		
		FATAL_DRAW_EF = event.registerSkill(new FatalDrawSkill(SpecialAttackSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "fatal_draw_ef")).setConsumption(60.0F).setMaxStack(12))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.7F))
				.addProperty(AttackPhaseProperty.ARMOR_NEGATION, ValueCorrector.adder(50.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(6))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.registerPropertiesToAnimation(),false);
		
		ENDER_BLAST = event.registerSkill(new EnderBlastSkill(SimpleSpecialAttackSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "ender_blast")).setConsumption(20.0F).setMaxStack(12)), false);
		
		ARROW_TENACITY = event.registerSkill(new ArrowTenacitySkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "arrow_tenacity")).setRequiredXp(0)),true);
		COUNTER_ATTACK = event.registerSkill(new CounterAttack(GuardSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "counter_attack")).setRequiredXp(8)),true);
		
	}
}
