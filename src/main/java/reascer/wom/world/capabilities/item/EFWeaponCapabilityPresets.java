package reascer.wom.world.capabilities.item;

import java.util.function.Function;

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
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Skills;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategory;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFWeaponCapabilityPresets {
	public static final Function<Item, CapabilityItem> SWORD = (item) -> {
		WeaponCapability cap = new WeaponCapability(WeaponCapability.builder()
			.category(WeaponCategory.SWORD)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategory.SWORD ? Styles.TWO_HAND : Styles.ONE_HAND)
			.collider(ColliderPreset.SWORD)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.newStyleCombo(Styles.ONE_HAND, EFAnimations.SWORD_ONEHAND_AUTO_1, EFAnimations.SWORD_ONEHAND_AUTO_2, EFAnimations.SWORD_ONEHAND_AUTO_3, EFAnimations.SWORD_ONEHAND_AUTO_4, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.specialAttack(Styles.ONE_HAND, Skills.SWEEPING_EDGE)
			.specialAttack(Styles.TWO_HAND, Skills.DANCING_EDGE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
			.weaponCombinationPredicator((itemstack) -> EpicFightCapabilities.getItemStackCapability(itemstack).getWeaponCategory() == WeaponCategory.SWORD)
		);
		
		if (item instanceof TieredItem) {
			int harvestLevel = ((TieredItem)item).getTier().getLevel();
			cap.addStyleAttibute(CapabilityItem.Styles.COMMON, Pair.of(EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier(0.5D + 0.2D * harvestLevel)));
			cap.addStyleAttibute(CapabilityItem.Styles.COMMON, Pair.of(EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(1)));
		}
		
		return cap;
	};
	
	public static final Function<Item, CapabilityItem> AGONY = (item) -> {
		EFWeaponCapability cap = new EFWeaponCapability(WeaponCapability.builder()
				.category(WeaponCategory.SPEAR)
				.styleProvider((playerpatch) -> Styles.TWO_HAND)
				.collider(ColliderPreset.SPEAR)
				.hitSound(EpicFightSounds.BLADE_HIT)
				.canBePlacedOffhand(false)
				.newStyleCombo(Styles.TWO_HAND, EFAnimations.AGONY_AUTO_1, EFAnimations.AGONY_AUTO_2, EFAnimations.AGONY_AUTO_3, EFAnimations.AGONY_DASH, EFAnimations.AGONY_AIR_SLASH)
				.newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
				.specialAttack(Styles.TWO_HAND, EFSkills.AGONY_PLUNGE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.AGONY_IDLE)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.AGONY_WALK)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.AGONY_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.AGONY_RUN)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
				.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD)
					,EFWeaponCapability.EFbuilder()
				.newHeavyStyleCombo(Styles.TWO_HAND, EFAnimations.AGONY_AUTO_1, EFAnimations.AGONY_AUTO_2, EFAnimations.AGONY_AUTO_3, EFAnimations.AGONY_DASH, EFAnimations.AGONY_AIR_SLASH)
		);
		return cap;
	};
	
	public static final Function<Item, CapabilityItem> TORMENT = (item) -> {
		EFWeaponCapability cap = new EFWeaponCapability(WeaponCapability.builder()
				.category(WeaponCategory.GREATSWORD)
				.styleProvider((entitypatch) -> {
					if (entitypatch instanceof PlayerPatch<?>) {
						if (((PlayerPatch<?>)entitypatch).getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getRemainDuration() > 0) {
							return Styles.LIECHTENAUER;
						}
					}
					return Styles.TWO_HAND;
				})
				.collider(EFColliders.TORMENT)
				.hitSound(EpicFightSounds.BLADE_HIT)
				.swingSound(EpicFightSounds.WHOOSH_BIG)
				.canBePlacedOffhand(false)
				.newStyleCombo(Styles.TWO_HAND, EFAnimations.TORMENT_AUTO_1, EFAnimations.TORMENT_AUTO_2, EFAnimations.TORMENT_AUTO_3, EFAnimations.TORMENT_DASH, Animations.GREATSWORD_AIR_SLASH)
				.newStyleCombo(Styles.LIECHTENAUER, EFAnimations.TORMENT_BERSERK_AUTO_1, EFAnimations.TORMENT_BERSERK_AUTO_2, EFAnimations.TORMENT_BERSERK_DASH, Animations.GREATSWORD_AIR_SLASH)
				.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
				.specialAttack(Styles.TWO_HAND, EFSkills.TRUE_BERSERK)
				.specialAttack(Styles.LIECHTENAUER, EFSkills.TRUE_BERSERK)
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
				.livingMotionModifier(Styles.LIECHTENAUER, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD)
					,EFWeaponCapability.EFbuilder()
				.newHeavyStyleCombo(Styles.TWO_HAND, EFAnimations.TORMENT_AUTO_1, EFAnimations.TORMENT_AUTO_2, EFAnimations.TORMENT_AUTO_3, EFAnimations.TORMENT_DASH, Animations.GREATSWORD_AIR_SLASH)
		);
		return cap;
	};
	
	public static final Function<Item, CapabilityItem> RUINE = (item) -> {
		WeaponCapability weaponCapability = new WeaponCapability(WeaponCapability.builder()
			.category(WeaponCategory.LONGSWORD)
			.styleProvider((playerpatch) -> Styles.TWO_HAND)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(EFColliders.RUINE)
			.canBePlacedOffhand(false)
			.newStyleCombo(Styles.TWO_HAND, EFAnimations.RUINE_AUTO_1, EFAnimations.RUINE_AUTO_2, EFAnimations.RUINE_AUTO_3, EFAnimations.RUINE_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.specialAttack(Styles.TWO_HAND, EFSkills.PLUNDER_PERDITION)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.RUINE_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.RUINE_WALK)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, EFAnimations.RUINE_RUN)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.INACTION, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
		);
		return weaponCapability;
	};
	
	public static final Function<Item, CapabilityItem> EFKATANA = (item) -> {
		WeaponCapability cap = new WeaponCapability(WeaponCapability.builder()
			.category(WeaponCategory.KATANA)
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
			.specialAttack(Styles.SHEATH, EFSkills.FATAL_DRAW_EF)
			.specialAttack(Styles.TWO_HAND, EFSkills.FATAL_DRAW_EF)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_UNSHEATHING)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, EFAnimations.KATANA_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.WALK,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE,  EFAnimations.KATANA_SHEATHED_RUN)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.RUN,  EFAnimations.KATANA_SHEATHED_RUN)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT,  EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, EFAnimations.KATANA_SHEATHED_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.KATANA_GUARD)
		);
		return cap;
	};
	
	public static final Function<Item, CapabilityItem> ENDER_BLASTER = (item) -> {
		WeaponCapability weaponCapability = new WeaponCapability(WeaponCapability.builder()
			.category(WeaponCategory.RANGED)
			.styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategory.RANGED ? Styles.TWO_HAND : Styles.ONE_HAND)
			.hitSound(EpicFightSounds.BLADE_HIT)
			.collider(EFColliders.ENDER_BLASTER)
			.newStyleCombo(Styles.ONE_HAND, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_1, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_2, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_3, EFAnimations.ENDERBLASTER_ONEHAND_AUTO_4, EFAnimations.RUINE_DASH, Animations.LONGSWORD_AIR_SLASH)
			.newStyleCombo(Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
			.newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
			.specialAttack(Styles.ONE_HAND, EFSkills.TRUE_BERSERK)
			.specialAttack(Styles.TWO_HAND, EFSkills.TRUE_BERSERK)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.IDLE, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, EFAnimations.ENDERBLASTER_ONEHAND_IDLE)
			.livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
			.livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
		);
		return weaponCapability;
	};
	
	@SubscribeEvent
	public static void register(WeaponCapabilityPresetRegistryEvent event) {
	  event.getTypeEntry().put("agony", AGONY);
	  event.getTypeEntry().put("torment", TORMENT);
	  event.getTypeEntry().put("ruine", RUINE);
	  event.getTypeEntry().put("katana", EFKATANA);
	  event.getTypeEntry().put("ender_blaster", ENDER_BLASTER);
	  event.getTypeEntry().put("sword", SWORD);
	}
	
}
