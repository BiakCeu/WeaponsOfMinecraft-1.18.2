package reascer.wom.gameasset;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.AgonyPlungeSkill;
import reascer.wom.skill.ArrowTenacitySkill;
import reascer.wom.skill.BlossomSkill;
import reascer.wom.skill.CharybdisSkill;
import reascer.wom.skill.CounterAttack;
import reascer.wom.skill.CriticalKnowledgeSkill;
import reascer.wom.skill.DemonMarkPassiveSkill;
import reascer.wom.skill.DemonicAscensionSkill;
import reascer.wom.skill.DodgeMasterSkill;
import reascer.wom.skill.EFKatanaPassive;
import reascer.wom.skill.EnderBlastSkill;
import reascer.wom.skill.EnderFusionSkill;
import reascer.wom.skill.EnderStepSkill;
import reascer.wom.skill.FatalDrawSkill;
import reascer.wom.skill.PainAnticipationSkill;
import reascer.wom.skill.PainRetributionSkill;
import reascer.wom.skill.PlunderPerditionSkill;
import reascer.wom.skill.TrueBerserkSkill;
import reascer.wom.skill.VampirizeSkill;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.forgeevent.SkillRegistryEvent;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.math.ValueCorrector;
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

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFSkills {
	public static Skill ENDERSTEP;
	public static Skill DODGEMASTER;
	public static Skill KNIGHT_ROLL;
	
	public static Skill KICK;
	
	public static Skill BLOSSOM;
	public static Skill CHARYBDIS;
	
	public static Skill AGONY_PLUNGE;
	public static Skill TRUE_BERSERK;
	public static Skill DEMONIC_ASCENSION;
	public static Skill PLUNDER_PERDITION;
	
	public static Skill COUNTERATTACK_PASSIVE;
	
	public static Skill KATANA_PASSIVE_EF;
	public static Skill DEMON_MARK_PASSIVE;

	public static Skill FATAL_DRAW_EF;
	public static Skill ARROW_TENACITY;
	public static Skill COUNTER_ATTACK;
	public static Skill PAIN_ANTICIPATION;
	public static Skill PAIN_RETRIBUTION;
	public static Skill VAMPIRIZE;
	public static Skill CRITICAL_KNOWLEDGE;
	
	public static Skill ENDER_BLAST;
	public static Skill ENDER_FUSION;
	
	@SubscribeEvent
	public static void registerSkills(SkillRegistryEvent event) {
		ENDERSTEP = event.registerSkill(new EnderStepSkill(DodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "enderstep")).setConsumption(5.0F)
				.setAnimations(EFAnimations.ENDERSTEP_FORWARD, EFAnimations.ENDERSTEP_BACKWARD, EFAnimations.ENDERSTEP_LEFT, EFAnimations.ENDERSTEP_RIGHT)),true);
		
		DODGEMASTER = event.registerSkill(new DodgeMasterSkill(DodgeMasterSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "dodgemaster")).setConsumption(2.0F)
				.setAnimations(EFAnimations.DODGEMASTER_BACKWARD, EFAnimations.DODGEMASTER_BACKWARD, EFAnimations.DODGEMASTER_RIGHT, EFAnimations.DODGEMASTER_LEFT)),true);
		
		KNIGHT_ROLL = event.registerSkill(new DodgeSkill(DodgeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "knight_roll")).setConsumption(3.5F)
				.setAnimations(EFAnimations.KNIGHT_ROLL_FORWARD, EFAnimations.KNIGHT_ROLL_BACKWARD, EFAnimations.KNIGHT_ROLL_LEFT, EFAnimations.KNIGHT_ROLL_RIGHT)),true);
		
		BLOSSOM = event.registerSkill(new BlossomSkill(BlossomSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "blossom")).setConsumption(60.0F)),false);
		CHARYBDIS = event.registerSkill(new CharybdisSkill(CharybdisSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "charybdis")).setConsumption(6.0F).setAnimations(EFAnimations.STAFF_CHARYBDIS))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.registerPropertiesToAnimation(),false);
		
		AGONY_PLUNGE = event.registerSkill(new AgonyPlungeSkill(AgonyPlungeSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "agony_plunge")).setConsumption(50.0F).setMaxStack(3).setAnimations(EFAnimations.AGONY_PLUNGE_FORWARD))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.5F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10))
				.registerPropertiesToAnimation(),false);
		
		TRUE_BERSERK = event.registerSkill(new TrueBerserkSkill(TrueBerserkSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "true_berserk")).setConsumption(120.0F).setMaxDuration(3).setActivateType(ActivateType.DURATION_INFINITE).setAnimations(EFAnimations.TORMENT_BERSERK_CONVERT))
				.newPropertyLine()
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10))
				.registerPropertiesToAnimation(),false);
		
		PLUNDER_PERDITION = event.registerSkill(new PlunderPerditionSkill(PlunderPerditionSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "plunder_perdition")).setConsumption(120.0F).setActivateType(ActivateType.ONE_SHOT))
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
		
		FATAL_DRAW_EF = event.registerSkill(new FatalDrawSkill(SpecialAttackSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "fatal_draw_ef")).setConsumption(40.0F).setMaxStack(12))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.7F))
				.addProperty(AttackPhaseProperty.ARMOR_NEGATION, ValueCorrector.adder(50.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(6))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.registerPropertiesToAnimation(),false);
		
		ENDER_BLAST = event.registerSkill(new EnderBlastSkill(SimpleSpecialAttackSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "ender_blast")).setConsumption(6.0F).setMaxStack(12))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(2.5F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(4))
				.registerPropertiesToAnimation(), false);
		
		ENDER_FUSION = event.registerSkill(new EnderFusionSkill(SimpleSpecialAttackSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "ender_fusion")).setConsumption(12.0F).setMaxStack(24))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.newPropertyLine()
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(2.5F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(4))
				.registerPropertiesToAnimation(), false);
		
		DEMON_MARK_PASSIVE = event.registerSkill(new DemonMarkPassiveSkill(Skill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "demon_mark_passive"))
				.setCategory(SkillCategories.WEAPON_PASSIVE)
				.setConsumption(0.0F)
				.setActivateType(ActivateType.ONE_SHOT)
				.setResource(Resource.COOLDOWN)),false);
		
		DEMONIC_ASCENSION = event.registerSkill(new DemonicAscensionSkill(DemonicAscensionSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "demonic_ascension")).setConsumption(240.0F).setMaxDuration(3).setActivateType(ActivateType.DURATION_INFINITE).setAnimations(EFAnimations.ANTITHEUS_ASCENSION)),false);
		
		COUNTER_ATTACK = event.registerSkill(new CounterAttack(CounterAttack.createBuilder(new ResourceLocation(EpicFightMod.MODID, "counter_attack")).setRequiredXp(8)),true);
		//KICK = event.registerSkill(new KickSkill(KickSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "kick")).setConsumption(2.0F).setRequiredXp(3)),true);
		
		
		ARROW_TENACITY = event.registerSkill(new ArrowTenacitySkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "arrow_tenacity")).setRequiredXp(0)),true);
		PAIN_ANTICIPATION = event.registerSkill(new PainAnticipationSkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "pain_anticipation")).setRequiredXp(5)),true);
		PAIN_RETRIBUTION = event.registerSkill(new PainRetributionSkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "pain_retribution")).setRequiredXp(8)),true);
		VAMPIRIZE = event.registerSkill(new VampirizeSkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "vampirize")).setRequiredXp(8)),true);
		CRITICAL_KNOWLEDGE = event.registerSkill(new CriticalKnowledgeSkill(PassiveSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "critical_knowledge")).setRequiredXp(8)),true);
		
	}
}
