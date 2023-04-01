package reascer.wom.world.capabilities;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.yggdrasil.request.TelemetryEventsRequest.Event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.WOMEnchantment;
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
			invigoration += EnchantmentHelper.getItemEnchantmentLevel(WOMEnchantment.INVIGORATION.get(), stack);
			//System.out.println("in. \n invig lvl = "+ invigoration);
		}
		//System.out.println("out. \n Stack:"+LivingEntity.getEquipmentSlotForItem(stack)+" Slot:"+slot.getName()+"\n");
		// add the attributes if we have any changes
		if (invigoration != 0) event.addModifier(EpicFightAttributes.MAX_STAMINA.get(), new AttributeModifier(STAMINAR_ADD.get(slot), "invigoration_stamina_add", invigoration*2, Operation.ADDITION));
	}
	
	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		float from_health = 0;
		float to_health = 0;
		
		if (event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).size() != 0) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				//event.getEntityLiving().setHealth((float) (event.getEntityLiving().getHealth()-event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount()));
			}
			from_health = (float) event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount();
		}
		
		if (event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).size() != 0) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				//event.getEntityLiving().setHealth((float) (event.getEntityLiving().getHealth()-event.getFrom().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount()));
			}
			to_health = (float) event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next().getAmount();
		}
		
		if (from_health > to_health) {
			if (event.getEntityLiving().getHealth() == event.getEntityLiving().getMaxHealth()) {
				event.getEntityLiving().setHealth(event.getEntityLiving().getHealth() - from_health);
			}
			//System.out.println(event.getTo().getAttributeModifiers(event.getSlot()).get(Attributes.MAX_HEALTH).iterator().next());
		}
	}
	
	@SubscribeEvent
	public static void onUpdateEvent(LivingUpdateEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			for (String tag : event.getEntityLiving().getTags()) {
				if (tag.contains("wom_health_fix:")) {
					if (Float.valueOf(tag.substring(15)) <= event.getEntityLiving().getMaxHealth()) {
						event.getEntityLiving().setHealth(Float.valueOf(tag.substring(15)));
						event.getEntityLiving().getTags().remove(tag);
					}
					break;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onleaveEvent(EntityLeaveWorldEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			for (String tag : p.getTags()) {
				if (tag.contains("wom_health_fix:")) {
					p.getTags().remove(tag);
					break;
				}
			}
			if (p.isAlive()) {
				p.addTag("wom_health_fix:"+p.getHealth());
			}
		}
	}
	
	@SubscribeEvent
	public static void onDisconectEvent(PlayerLoggedOutEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			for (String tag : p.getTags()) {
				if (tag.contains("wom_health_fix:")) {
					p.getTags().remove(tag);
					break;
				}
			}
			if (p.isAlive()) {
				p.addTag("wom_health_fix:"+p.getHealth());
			}
		}
	}
}
