package reascer.wom.world.capabilities.item;

import java.util.function.Function;

import com.ibm.icu.impl.units.UnitsData.Categories;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.gameasset.EFColliders;
import reascer.wom.gameasset.EFSkills;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.EFKatanaPassive;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFWeaponCapabilityPresets {
	public static final Function<Item, CapabilityItem.Builder> SWORD = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.SWORD)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD ? Styles.TWO_HAND : Styles.ONE_HAND)
			.collider(ColliderPreset.SWORD)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.newStyleCombo(Styles.ONE_HAND, EFAnimations.SWORD_ONEHAND_AUTO_1, EFAnimations.SWORD_ONEHAND_AUTO_2, EFAnimations.SWORD_ONEHAND_AUTO_3, EFAnimations.SWORD_ONEHAND_AUTO_4, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND,(itemstack) -> EpicFightSkills.SWEEPING_EDGE)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EpicFightSkills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == WeaponCategories.SWORD);
		
		if (item instanceof TieredItem) {
			int harvestLevel = ((TieredItem)item).getTier().getLevel();
			builder.addStyleAttibutes(CapabilityItem.Styles.COMMON, Pair.of(EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier(0.5D + 0.2D * harvestLevel)));
			builder.addStyleAttibutes(CapabilityItem.Styles.COMMON, Pair.of(EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(1)));
		}
		
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> GREATSWORD = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.GREATSWORD)
			.styleProvider((playerpatch) -> Styles.TWO_HAND)
			.collider(EFColliders.GREATSWORD)
			.swingSound(EpicFightSounds.WHOOSH_BIG)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.GREATSWORD_TWOHAND_AUTO_1, EFAnimations.GREATSWORD_TWOHAND_AUTO_2, EFAnimations.GREATSWORD_TWOHAND_AUTO_3, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EpicFightSkills.GIANT_WHIRLWIND)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
	    	.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> LONGSWORD = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.LONGSWORD)
			.styleProvider((entitypatch) -> {
				if (entitypatch instanceof PlayerPatch<?>) {
					if (((PlayerPatch<?>)entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getRemainDuration() > 0) {
						return Styles.LIECHTENAUER;
					}
				}
				return Styles.TWO_HAND;
			})
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(ColliderPreset.LONGSWORD)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.LONGSWORD_TWOHAND_AUTO_1,EFAnimations.LONGSWORD_TWOHAND_AUTO_2, EFAnimations.LONGSWORD_TWOHAND_AUTO_3,EFAnimations.LONGSWORD_TWOHAND_AUTO_4, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.LIECHTENAUER,EFAnimations.LONGSWORD_TWOHAND_AUTO_1,EFAnimations.LONGSWORD_TWOHAND_AUTO_2, EFAnimations.LONGSWORD_TWOHAND_AUTO_3,EFAnimations.LONGSWORD_TWOHAND_AUTO_4, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EpicFightSkills.LIECHTENAUER)
			.innateSkill(Styles.LIECHTENAUER,(itemstack) -> EpicFightSkills.LIECHTENAUER)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.WALK, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.CHASE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.RUN, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.SNEAK, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.KNEEL, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.JUMP, Animations.BIPED_HOLD_LONGSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> TACHI = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.TACHI)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.TACHI ? Styles.TWO_HAND : Styles.ONE_HAND)
			.collider(ColliderPreset.KATANA)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.newStyleCombo(Styles.ONE_HAND, EFAnimations.TACHI_TWOHAND_AUTO_1, EFAnimations.TACHI_TWOHAND_AUTO_2, EFAnimations.TACHI_TWOHAND_AUTO_3, EFAnimations.TACHI_TWOHAND_AUTO_4, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND,(itemstack) -> EpicFightSkills.LETHAL_SLICE)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EpicFightSkills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == WeaponCategories.TACHI);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> STAFF = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.SPEAR)
			.styleProvider((playerpatch) -> Styles.TWO_HAND)
			.collider(EFColliders.STAFF)
			.hitSound(EpicFightSounds.BLUNT_HIT)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.STAFF_AUTO_1, EFAnimations.STAFF_AUTO_2, EFAnimations.STAFF_AUTO_3, EFAnimations.STAFF_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.CHARYBDIS)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.STAFF_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.STAFF_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.STAFF_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> AGONY = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
				.category(WeaponCategories.SPEAR)
				.styleProvider((playerpatch) -> Styles.TWO_HAND)
				.collider(EFColliders.AGONY)
				.hitSound(EpicFightSounds.BLADE_HIT)
				.canBePlacedOffhand(false)
				.newStyleCombo(Styles.TWO_HAND, EFAnimations.AGONY_AUTO_1, EFAnimations.AGONY_AUTO_2, EFAnimations.AGONY_AUTO_3, EFAnimations.AGONY_AUTO_4, EFAnimations.AGONY_CLAWSTRIKE, EFAnimations.AGONY_AIR_SLASH)
				.newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
				.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.AGONY_PLUNGE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.AGONY_IDLE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.AGONY_WALK)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.AGONY_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.AGONY_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> TORMENT = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
				.category(WomWeaponCategories.TORMENT)
				.styleProvider((entitypatch) -> {
					if (entitypatch instanceof PlayerPatch<?>) {
						if (((PlayerPatch<?>)entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getRemainDuration() > 0) {
							return Styles.LIECHTENAUER;
						}
					}
					return Styles.TWO_HAND;
				})
				.collider(EFColliders.TORMENT)
				.hitSound(EpicFightSounds.BLADE_HIT)
				.swingSound(EpicFightSounds.WHOOSH_BIG)
				.canBePlacedOffhand(false)
				.newStyleCombo(Styles.TWO_HAND, EFAnimations.TORMENT_AUTO_4, EFAnimations.TORMENT_AUTO_1, EFAnimations.TORMENT_AUTO_2, EFAnimations.TORMENT_AUTO_3, EFAnimations.TORMENT_DASH, EFAnimations.TORMENT_AIRSLAM)
				.newStyleCombo(Styles.LIECHTENAUER, EFAnimations.TORMENT_BERSERK_AUTO_1, EFAnimations.TORMENT_BERSERK_AUTO_2, EFAnimations.TORMENT_BERSERK_DASH, EFAnimations.TORMENT_BERSERK_AIRSLAM)
				.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
				.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.TRUE_BERSERK)
				.innateSkill(Styles.LIECHTENAUER,(itemstack) -> EFSkills.TRUE_BERSERK)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.TORMENT_IDLE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.TORMENT_WALK)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.TORMENT_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.TORMENT_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.IDLE, EFAnimations.TORMENT_BERSERK_IDLE)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.WALK, EFAnimations.TORMENT_BERSERK_WALK)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.CHASE, EFAnimations.TORMENT_BERSERK_RUN)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.RUN, EFAnimations.TORMENT_BERSERK_RUN)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> RUINE = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.LONGSWORD)
			.styleProvider((playerpatch) -> Styles.TWO_HAND)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(EFColliders.RUINE)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.RUINE_AUTO_1, EFAnimations.RUINE_AUTO_2, EFAnimations.RUINE_AUTO_3, EFAnimations.RUINE_AUTO_4, EFAnimations.RUINE_DASH, EFAnimations.RUINE_COMET)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.PLUNDER_PERDITION)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.RUINE_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.RUINE_WALK)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.RUINE_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, EFAnimations.RUINE_BLOCK);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> EFKATANA = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WeaponCategories.KATANA)
			.styleProvider((entitypatch) -> {
				if (entitypatch instanceof PlayerPatch) {
					PlayerPatch<?> playerpatch = (PlayerPatch<?>)entitypatch;
					if (playerpatch.getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().hasData(EFKatanaPassive.SHEATH) && 
							playerpatch.getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().getDataValue(EFKatanaPassive.SHEATH)) {
						return Styles.SHEATH;
					}
				}
				return Styles.TWO_HAND;
			})
			.passiveSkill(EFSkills.KATANA_PASSIVE_EF)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(EFColliders.KATANA)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.SHEATH, EFAnimations.KATANA_SHEATHED_AUTO_1, EFAnimations.KATANA_SHEATHED_AUTO_2, EFAnimations.KATANA_SHEATHED_AUTO_3, EFAnimations.KATANA_SHEATHED_DASH, Animations.KATANA_SHEATH_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.KATANA_AUTO_1, EFAnimations.KATANA_AUTO_2, EFAnimations.KATANA_AUTO_3, EFAnimations.KATANA_DASH, Animations.KATANA_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.SHEATH,(itemstack) -> EFSkills.FATAL_DRAW_EF)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.FATAL_DRAW_EF)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_UNSHEATHING)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_RUN_UNSHEATHING)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.WALK,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE,  EFAnimations.KATANA_SHEATHED_RUN)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.RUN,  EFAnimations.KATANA_SHEATHED_RUN)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM,  EFAnimations.KATANA_SHEATHED_RUN)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.KATANA_GUARD);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> ENDER_BLASTER = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
			.category(WomWeaponCategories.ENDERBLASTER)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WomWeaponCategories.ENDERBLASTER ? Styles.TWO_HAND : Styles.ONE_HAND)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(EFColliders.ENDER_BLASTER)
			.newStyleCombo(Styles.ONE_HAND, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_1, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_2, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_3, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_4, EFAnimations.ENDERBLASTER_ONEHAND_DASH, EFAnimations.ENDERBLASTER_ONEHAND_JUMPKICK)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.ENDERBLASTER_TWOHAND_AUTO_1, EFAnimations.ENDERBLASTER_TWOHAND_AUTO_2, EFAnimations.ENDERBLASTER_TWOHAND_AUTO_3, EFAnimations.ENDERBLASTER_TWOHAND_AUTO_4, EFAnimations.ENDERBLASTER_TWOHAND_TOMAHAWK, EFAnimations.ENDERBLASTER_TWOHAND_TISHNAW)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.innateSkill(Styles.ONE_HAND,(itemstack) -> EFSkills.ENDER_BLAST)
			.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.ENDER_FUSION)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.IDLE, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.KNEEL, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.WALK, EFAnimations.ENDERBLASTER_ONEHAND_WALK)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.CHASE, EFAnimations.ENDERBLASTER_ONEHAND_RUN)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, EFAnimations.ENDERBLASTER_ONEHAND_RUN)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.SNEAK, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.SWIM, Animations.BIPED_SWIM)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.FLOAT, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.FALL, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.ENDERBLASTER_ONEHAND_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.ENDERBLASTER_ONEHAND_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_SWIM)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, EFAnimations.ENDERBLASTER_TWOHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCollider() == EFColliders.ENDER_BLASTER);
		return builder;
	};
	
	public static final Function<Item, CapabilityItem.Builder> ANTITHEUS = (item) -> {
		CapabilityItem.Builder builder = WeaponCapability.builder()
				.category(WomWeaponCategories.ANTITHEUS)
				.styleProvider((entitypatch) -> {
					if (entitypatch instanceof PlayerPatch<?>) {
						if (((PlayerPatch<?>)entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getRemainDuration() > 0) {
							return Styles.LIECHTENAUER;
						}
					}
					return Styles.TWO_HAND;
				})
				.collider(EFColliders.ANTITHEUS)
				.hitSound(EpicFightSounds.BLADE_HIT)
				.swingSound(EpicFightSounds.WHOOSH)
				.passiveSkill(EFSkills.DEMON_MARK_PASSIVE)
				.canBePlacedOffhand(false)
				.newStyleCombo(Styles.TWO_HAND, EFAnimations.ANTITHEUS_AUTO_1, EFAnimations.ANTITHEUS_AUTO_2, EFAnimations.ANTITHEUS_AUTO_3, EFAnimations.ANTITHEUS_AUTO_4, EFAnimations.ANTITHEUS_AGRESSION, EFAnimations.ANTITHEUS_GUILLOTINE)
				.newStyleCombo(Styles.LIECHTENAUER, EFAnimations.ANTITHEUS_ASCENDED_AUTO_1, EFAnimations.ANTITHEUS_ASCENDED_AUTO_2, EFAnimations.ANTITHEUS_ASCENDED_AUTO_3, EFAnimations.ANTITHEUS_ASCENDED_BLINK, EFAnimations.ANTITHEUS_ASCENDED_DEATHFALL)
				.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
				.innateSkill(Styles.TWO_HAND,(itemstack) -> EFSkills.DEMONIC_ASCENSION)
				.innateSkill(Styles.LIECHTENAUER,(itemstack) -> EFSkills.DEMONIC_ASCENSION)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.ANTITHEUS_IDLE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.ANTITHEUS_WALK)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.ANTITHEUS_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.ANTITHEUS_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.IDLE, EFAnimations.ANTITHEUS_ASCENDED_IDLE)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.WALK, EFAnimations.ANTITHEUS_ASCENDED_WALK)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.CHASE, EFAnimations.ANTITHEUS_ASCENDED_RUN)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.RUN, EFAnimations.ANTITHEUS_ASCENDED_RUN)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.SWIM, Animations.BIPED_SWIM)
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.BLOCK, Animations.SWORD_GUARD);
		return builder;
	};
	
	@SubscribeEvent
	public static void register(WeaponCapabilityPresetRegistryEvent event) {
		event.getTypeEntry().put("sword", SWORD);
		event.getTypeEntry().put("tachi", TACHI);
		event.getTypeEntry().put("longsword", LONGSWORD);
		event.getTypeEntry().put("greatsword", GREATSWORD);
		event.getTypeEntry().put("staff", STAFF);
		event.getTypeEntry().put("agony", AGONY);
		event.getTypeEntry().put("torment", TORMENT);
		event.getTypeEntry().put("ruine", RUINE);
		event.getTypeEntry().put("katana", EFKATANA);
		event.getTypeEntry().put("ender_blaster", ENDER_BLASTER);
		event.getTypeEntry().put("antitheus", ANTITHEUS);
	}
	
}
