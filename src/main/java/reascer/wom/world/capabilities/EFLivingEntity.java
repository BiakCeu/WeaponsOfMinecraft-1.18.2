package reascer.wom.world.capabilities;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.EFEnchantment;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.FORGE)
public class EFLivingEntity {
	private static final Map<EquipmentSlot,UUID> STAMINAR_ADD = makeUUIDMap(WeaponOfMinecraft.MODID + "_staminar_add");
	
	/** Generates a UUID map for all slot types from a string key */
	private static Map<EquipmentSlot,UUID> makeUUIDMap(String key) {
		Map<EquipmentSlot,UUID> map = new EnumMap<>(EquipmentSlot.class);
		for (EquipmentSlot type : EquipmentSlot.values()) {
			map.put(type, UUID.nameUUIDFromBytes((key + type.getName()).getBytes()));
		}
		return map;
	}
	
	@SubscribeEvent
	public static void itemAttributeModifiers(ItemAttributeModifierEvent event) {
		// must be in the right slot
		float invigoration = 0;
		ItemStack stack = event.getItemStack();
		EquipmentSlot slot = event.getSlotType();
		if (slot == LivingEntity.getEquipmentSlotForItem(stack)) {
			// boost from enchant
			invigoration += EnchantmentHelper.getItemEnchantmentLevel(EFEnchantment.INVIGORATION.get(), stack);
			//System.out.println("in. \n invig lvl = "+ invigoration);
		}
		//System.out.println("out. \n Stack:"+LivingEntity.getEquipmentSlotForItem(stack)+" Slot:"+slot.getName()+"\n");
		// add the attributes if we have any changes
		if (invigoration != 0) event.addModifier(EpicFightAttributes.MAX_STAMINA.get(), new AttributeModifier(STAMINAR_ADD.get(slot), "invigoration_stamina_add", invigoration*2, Operation.ADDITION));
	}
}
