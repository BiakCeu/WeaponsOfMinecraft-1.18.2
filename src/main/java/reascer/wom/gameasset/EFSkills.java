package reascer.wom.gameasset;

import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.AgonyPlungeSkill;
import reascer.wom.skill.ArrowTenacitySkill;
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
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.DodgeSkill;
import yesman.epicfight.skill.PassiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.WeaponInnateSkill;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.Skill.Resource;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFSkills {
	public static Skill ENDERSTEP;
	public static Skill DODGEMASTER;
	public static Skill KNIGHT_ROLL;
	
	public static Skill KICK;
	
	//public static Skill BLOSSOM;
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
	
	public static void registerSkills() {
		SkillManager.register(EnderStepSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_backward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_left"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_right")), 
				WeaponOfMinecraft.MODID,"ender_step");
		
		SkillManager.register(DodgeMasterSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_back"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_back"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_right"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_left")),
				WeaponOfMinecraft.MODID,"dodge_master");
		
		SkillManager.register(DodgeSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_backward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_left"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_right")),
				WeaponOfMinecraft.MODID,"precise_roll");
		
		//BLOSSOM = event.registerSkill(new BlossomSkill(BlossomSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "blossom")).setConsumption(60.0F)),false);
		
		SkillManager.register(CharybdisSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"charybdis");

		SkillManager.register(AgonyPlungeSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"agony_plunge");
		
		SkillManager.register(TrueBerserkSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"true_berserk");
		
		SkillManager.register(PlunderPerditionSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"plunder_perdition");
	
		SkillManager.register(EFKatanaPassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.COOLDOWN),
				WeaponOfMinecraft.MODID,"katana_passive_ef");

		SkillManager.register(FatalDrawSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"fatal_draw_ef");

		SkillManager.register(EnderBlastSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"ender_blast");

		SkillManager.register(EnderFusionSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"ender_fusion");

		SkillManager.register(DemonMarkPassiveSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.COOLDOWN),
				WeaponOfMinecraft.MODID,"demon_mark_passive");

		SkillManager.register(DemonicAscensionSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setActivateType(ActivateType.DURATION_INFINITE),
				WeaponOfMinecraft.MODID,"demonic_ascension");
		
		
		SkillManager.register(CounterAttack::new, CounterAttack.createCounterAttackBuilder(),
				WeaponOfMinecraft.MODID,"counter_attack");

		
		SkillManager.register(ArrowTenacitySkill::new, PassiveSkill.createPassiveBuilder(),
				WeaponOfMinecraft.MODID,"arrow_tenacity");

		SkillManager.register(PainAnticipationSkill::new, PassiveSkill.createPassiveBuilder(),
				WeaponOfMinecraft.MODID,"pain_anticipation");

		SkillManager.register(PainRetributionSkill::new, PassiveSkill.createPassiveBuilder(),
				WeaponOfMinecraft.MODID,"pain_retribution");

		SkillManager.register(VampirizeSkill::new, PassiveSkill.createPassiveBuilder(),
				WeaponOfMinecraft.MODID,"vampirize");

		SkillManager.register(CriticalKnowledgeSkill::new, PassiveSkill.createPassiveBuilder(),
				WeaponOfMinecraft.MODID,"critical_knowledge");
	}
	
	@SubscribeEvent
	public static void buildSkillEvent(SkillBuildEvent onBuild) {
		ENDERSTEP = onBuild.build(WeaponOfMinecraft.MODID, "ender_step");
		KNIGHT_ROLL = onBuild.build(WeaponOfMinecraft.MODID, "precise_roll");		
		DODGEMASTER = onBuild.build(WeaponOfMinecraft.MODID, "dodge_master");
		
		COUNTER_ATTACK = onBuild.build(WeaponOfMinecraft.MODID, "counter_attack");
		
		ARROW_TENACITY = onBuild.build(WeaponOfMinecraft.MODID, "arrow_tenacity");
		PAIN_ANTICIPATION = onBuild.build(WeaponOfMinecraft.MODID, "pain_anticipation");
		PAIN_RETRIBUTION = onBuild.build(WeaponOfMinecraft.MODID, "pain_retribution");
		VAMPIRIZE = onBuild.build(WeaponOfMinecraft.MODID, "vampirize");
		CRITICAL_KNOWLEDGE = onBuild.build(WeaponOfMinecraft.MODID, "critical_knowledge");
		
		WeaponInnateSkill charybdisSkill  = onBuild.build(WeaponOfMinecraft.MODID, "charybdis");
		charybdisSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35f))
			.registerPropertiesToAnimation();
		CHARYBDIS = charybdisSkill;
		
		WeaponInnateSkill AgonyPlungeSkill = onBuild.build(WeaponOfMinecraft.MODID, "agony_plunge");
		AgonyPlungeSkill.newProperty()
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10))
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
		AGONY_PLUNGE = AgonyPlungeSkill;
		
		WeaponInnateSkill trueBerserkSkill = onBuild.build(WeaponOfMinecraft.MODID, "true_berserk");
		trueBerserkSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.4f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
		TRUE_BERSERK = trueBerserkSkill;
		
		WeaponInnateSkill plunderPerditionSkill = onBuild.build(WeaponOfMinecraft.MODID, "plunder_perdition");
		plunderPerditionSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.3f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(20))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
		PLUNDER_PERDITION = plunderPerditionSkill;
		
		KATANA_PASSIVE_EF = onBuild.build(WeaponOfMinecraft.MODID, "katana_passive_ef");
		
		WeaponInnateSkill fatalDrawEFsSkill = onBuild.build(WeaponOfMinecraft.MODID, "fatal_draw_ef");
		fatalDrawEFsSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.7f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
			.addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50))
			.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
			
		FATAL_DRAW_EF = fatalDrawEFsSkill;
		
		WeaponInnateSkill enderblastSkill = onBuild.build(WeaponOfMinecraft.MODID, "ender_blast");
		enderblastSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
		ENDER_BLAST = enderblastSkill;
		
		WeaponInnateSkill enderfusionSkill = onBuild.build(WeaponOfMinecraft.MODID, "ender_fusion");
		enderfusionSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
		ENDER_FUSION = enderfusionSkill;
		
		DEMON_MARK_PASSIVE = onBuild.build(WeaponOfMinecraft.MODID, "demon_mark_passive");
		DEMONIC_ASCENSION = onBuild.build(WeaponOfMinecraft.MODID, "demonic_ascension");
		
	}
}
