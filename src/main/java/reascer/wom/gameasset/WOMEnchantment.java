package reascer.wom.gameasset;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reascer.wom.enchantment.InvigorationEnchantment;
import yesman.epicfight.main.EpicFightMod;

public class WOMEnchantment {
	public static final DeferredRegister<Enchantment> ENCHANTEMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, EpicFightMod.MODID);
    public static final RegistryObject<Enchantment> INVIGORATION = ENCHANTEMENTS.register("invigoration", () -> new InvigorationEnchantment(Rarity.RARE, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET));
}
