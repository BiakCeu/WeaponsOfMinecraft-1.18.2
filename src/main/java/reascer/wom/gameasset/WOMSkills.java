package reascer.wom.gameasset;

import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.AdrenalineSkill;
import reascer.wom.skill.AgonyPlungeSkill;
import reascer.wom.skill.ArrowTenacitySkill;
import reascer.wom.skill.ChargeSkill;
import reascer.wom.skill.CharybdisSkill;
import reascer.wom.skill.CounterAttack;
import reascer.wom.skill.CriticalKnowledgeSkill;
import reascer.wom.skill.DancingBladeSkill;
import reascer.wom.skill.DemonMarkPassiveSkill;
import reascer.wom.skill.DemonicAscensionSkill;
import reascer.wom.skill.DodgeMasterSkill;
import reascer.wom.skill.SatsujinPassive;
import reascer.wom.skill.EnderBlastSkill;
import reascer.wom.skill.EnderFusionSkill;
import reascer.wom.skill.EnderObscurisSkill;
import reascer.wom.skill.EnderStepSkill;
import reascer.wom.skill.MindSetSkill;
import reascer.wom.skill.SakuraStateSkill;
import reascer.wom.skill.heartShieldSkill;
import reascer.wom.skill.PainAnticipationSkill;
import reascer.wom.skill.PainRetributionSkill;
import reascer.wom.skill.SoulSnatchSkill;
import reascer.wom.skill.RegierungSkill;
import reascer.wom.skill.RuinePassive;
import reascer.wom.skill.ShadowStepSkill;
import reascer.wom.skill.TormentPassiveSkill;
import reascer.wom.skill.TrueBerserkSkill;
import reascer.wom.skill.VampirizeSkill;
import reascer.wom.world.damagesources.WOMExtraDamageInstance;
import reascer.wom.world.item.WOMCreativeTabs;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.Skill.Resource;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class WOMSkills {
	public static Skill ENDERSTEP;
	public static Skill ENDEROBSCURIS;
	public static Skill SHADOWSTEP;
	public static Skill DODGEMASTER;
	public static Skill KNIGHT_ROLL;
	public static Skill BULL_CHARGE;
	
	public static Skill KICK;
	
	//public static Skill BLOSSOM;
	public static Skill CHARYBDIS;
	public static Skill AGONY_PLUNGE;
	public static Skill TRUE_BERSERK;
	public static Skill DEMONIC_ASCENSION;
	public static Skill PLUNDER_PERDITION;
	public static Skill REGIERUNG;
	
	public static Skill COUNTERATTACK_PASSIVE;
	
	public static Skill SATSUJIN_PASSIVE;
	public static Skill DEMON_MARK_PASSIVE;
	public static Skill RUINE_PASSIVE;
	public static Skill TORMENT_PASSIVE;

	public static Skill SAKURA_STATE;
	public static Skill ARROW_TENACITY;
	public static Skill COUNTER_ATTACK;
	public static Skill PAIN_ANTICIPATION;
	public static Skill PAIN_RETRIBUTION;
	public static Skill VAMPIRIZE;
	public static Skill CRITICAL_KNOWLEDGE;
	public static Skill HEART_SHIELD;
	public static Skill MINDSET;
	public static Skill ADRENALINE;
	public static Skill DANCING_BLADE;
	
	public static Skill ENDER_BLAST;
	public static Skill ENDER_FUSION;
	
	public static void registerSkills() {
		SkillManager.register(EnderStepSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_backward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_left"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_right")).setCreativeTab(WOMCreativeTabs.ITEMS), 
				WeaponOfMinecraft.MODID,"ender_step");
		
		SkillManager.register(EnderObscurisSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_backward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_left"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/enderstep_right"), 
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/ender_obscuris")).setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"ender_obscuris");
		
		SkillManager.register(DodgeMasterSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_back"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_back"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_right"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/dodgemaster_left")).setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"dodge_master");
		
		SkillManager.register(DodgeSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_backward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_left"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/roll_right")),
				WeaponOfMinecraft.MODID,"precise_roll");
		
		SkillManager.register(ShadowStepSkill::new, DodgeSkill.createDodgeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/shadow_step_forward"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/shadow_step_backward")).setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"shadow_step");
		
		SkillManager.register(ChargeSkill::new, ChargeSkill.createChargeBuilder().setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/bull_charge")).setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"bull_charge");
		
		
		//BLOSSOM = event.registerSkill(new BlossomSkill(BlossomSkill.createBuilder(new ResourceLocation(EpicFightMod.MODID, "blossom")).setConsumption(60.0F)),false);
		
		SkillManager.register(CharybdisSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"charybdis");

		SkillManager.register(AgonyPlungeSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"agony_plunge");
		
		SkillManager.register(TrueBerserkSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"true_berserk");
		
		SkillManager.register(TormentPassiveSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE),
				WeaponOfMinecraft.MODID,"torment_passive");
		
		SkillManager.register(SoulSnatchSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"plunder_perdition");
	
		SkillManager.register(RuinePassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE),
				WeaponOfMinecraft.MODID,"ruine_passive");
		
		SkillManager.register(SatsujinPassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.COOLDOWN),
				WeaponOfMinecraft.MODID,"satsujin_passive");
		
		SkillManager.register(SakuraStateSkill::new, ConditionalWeaponInnateSkill.createConditionalWeaponInnateBuilder().setSelector((executer) -> {
			if (executer.getOriginal().isSprinting()) {
				executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SakuraStateSkill.SECOND_DRAW, false, executer.getOriginal());
				return 2;
			} else if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(SakuraStateSkill.ACTIVE)) {
				if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(SakuraStateSkill.SECOND_DRAW)) {
					executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SakuraStateSkill.SECOND_DRAW, false, executer.getOriginal());
					return 1;
				} else {
					executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SakuraStateSkill.SECOND_DRAW, true, executer.getOriginal());
					return 0;
				}
			} else {
				executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SakuraStateSkill.SECOND_DRAW, true, executer.getOriginal());
				return 0;
			}
		}).setAnimations(
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/katana_fatal_draw"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/katana_fatal_draw_second"),
				new ResourceLocation(WeaponOfMinecraft.MODID, "biped/skill/katana_fatal_draw_dash")),
				WeaponOfMinecraft.MODID,"sakura_state");

		SkillManager.register(EnderBlastSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"ender_blast");

		SkillManager.register(EnderFusionSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"ender_fusion");

		SkillManager.register(DemonMarkPassiveSkill::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(ActivateType.DURATION_INFINITE).setResource(Resource.COOLDOWN),
				WeaponOfMinecraft.MODID,"demon_mark_passive");

		SkillManager.register(DemonicAscensionSkill::new, WeaponInnateSkill.createWeaponInnateBuilder().setActivateType(ActivateType.DURATION_INFINITE),
				WeaponOfMinecraft.MODID,"demonic_ascension");
		
		SkillManager.register(RegierungSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(),
				WeaponOfMinecraft.MODID,"regierung");
		
		SkillManager.register(CounterAttack::new, CounterAttack.createCounterAttackBuilder(),
				WeaponOfMinecraft.MODID,"counter_attack");

		
		SkillManager.register(ArrowTenacitySkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"arrow_tenacity");

		SkillManager.register(PainAnticipationSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"pain_anticipation");

		SkillManager.register(PainRetributionSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"pain_retribution");

		SkillManager.register(VampirizeSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"vampirize");

		SkillManager.register(CriticalKnowledgeSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"critical_knowledge");
		
		SkillManager.register(heartShieldSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"heart_shield");
		
		SkillManager.register(MindSetSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"mindset");
		
		SkillManager.register(AdrenalineSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"adrenaline");
		
		SkillManager.register(DancingBladeSkill::new, PassiveSkill.createPassiveBuilder().setCreativeTab(WOMCreativeTabs.ITEMS),
				WeaponOfMinecraft.MODID,"dancing_blade");
	}
	
	@SubscribeEvent
	public static void buildSkillEvent(SkillBuildEvent onBuild) {
		ENDERSTEP = onBuild.build(WeaponOfMinecraft.MODID, "ender_step");
		ENDEROBSCURIS = onBuild.build(WeaponOfMinecraft.MODID, "ender_obscuris");
		SHADOWSTEP = onBuild.build(WeaponOfMinecraft.MODID, "shadow_step");
		KNIGHT_ROLL = onBuild.build(WeaponOfMinecraft.MODID, "precise_roll");		
		DODGEMASTER = onBuild.build(WeaponOfMinecraft.MODID, "dodge_master");
		BULL_CHARGE = onBuild.build(WeaponOfMinecraft.MODID, "bull_charge");
		
		COUNTER_ATTACK = onBuild.build(WeaponOfMinecraft.MODID, "counter_attack");
		
		ARROW_TENACITY = onBuild.build(WeaponOfMinecraft.MODID, "arrow_tenacity");
		PAIN_ANTICIPATION = onBuild.build(WeaponOfMinecraft.MODID, "pain_anticipation");
		PAIN_RETRIBUTION = onBuild.build(WeaponOfMinecraft.MODID, "pain_retribution");
		VAMPIRIZE = onBuild.build(WeaponOfMinecraft.MODID, "vampirize");
		CRITICAL_KNOWLEDGE = onBuild.build(WeaponOfMinecraft.MODID, "critical_knowledge");
		HEART_SHIELD = onBuild.build(WeaponOfMinecraft.MODID, "heart_shield");
		MINDSET = onBuild.build(WeaponOfMinecraft.MODID, "mindset");
		ADRENALINE = onBuild.build(WeaponOfMinecraft.MODID, "adrenaline");
		DANCING_BLADE = onBuild.build(WeaponOfMinecraft.MODID, "dancing_blade");
		
		WeaponInnateSkill charybdisSkill = onBuild.build(WeaponOfMinecraft.MODID, "charybdis");
		charybdisSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35f));
		CHARYBDIS = charybdisSkill;
		
		WeaponInnateSkill AgonyPlungeSkill = onBuild.build(WeaponOfMinecraft.MODID, "agony_plunge");
		AgonyPlungeSkill.newProperty()
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10))
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1))
			.newProperty()
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(1.2f)));
		AGONY_PLUNGE = AgonyPlungeSkill;
		
		TORMENT_PASSIVE = onBuild.build(WeaponOfMinecraft.MODID, "torment_passive");
		
		WeaponInnateSkill trueBerserkSkill = onBuild.build(WeaponOfMinecraft.MODID, "true_berserk");
		trueBerserkSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()));
		TRUE_BERSERK = trueBerserkSkill;
		
		RUINE_PASSIVE = onBuild.build(WeaponOfMinecraft.MODID, "ruine_passive");
		
		WeaponInnateSkill plunderPerditionSkill = onBuild.build(WeaponOfMinecraft.MODID, "plunder_perdition");
		plunderPerditionSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.3f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()));
		PLUNDER_PERDITION = plunderPerditionSkill;
		
		SATSUJIN_PASSIVE = onBuild.build(WeaponOfMinecraft.MODID, "satsujin_passive");
		
		WeaponInnateSkill sakuraStateskill = onBuild.build(WeaponOfMinecraft.MODID, "sakura_state");
		sakuraStateskill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
			.addProperty(AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
			.registerPropertiesToAnimation();
			
		SAKURA_STATE = sakuraStateskill;
		
		WeaponInnateSkill enderblastSkill = onBuild.build(WeaponOfMinecraft.MODID, "ender_blast");
		enderblastSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(0.7f)))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(0.6f)))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.1f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(4.0F))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(1.05f)));
		ENDER_BLAST = enderblastSkill;
		
		WeaponInnateSkill enderfusionSkill = onBuild.build(WeaponOfMinecraft.MODID, "ender_fusion");
		enderfusionSkill.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(0.7f)))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6f))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(0.6f)))
			.newProperty()
			.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(4.2f))
			.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(4.0F))
			.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(WOMExtraDamageInstance.WOM_SWEEPING_EDGE_ENCHANTMENT.create(2.1f)));
		ENDER_FUSION = enderfusionSkill;
		
		REGIERUNG = onBuild.build(WeaponOfMinecraft.MODID, "regierung");
		
		DEMON_MARK_PASSIVE = onBuild.build(WeaponOfMinecraft.MODID, "demon_mark_passive");
		DEMONIC_ASCENSION = onBuild.build(WeaponOfMinecraft.MODID, "demonic_ascension");
		
	}
}
