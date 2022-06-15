package reascer.wom.world.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.EpicFightItemGroup;
import yesman.epicfight.main.EpicFightMod;

public class EFECItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EpicFightMod.MODID);
	
	public static final RegistryObject<Item> AGONY = ITEMS.register("agony", () -> new AgonySpearItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> TORMENTED_MIND = ITEMS.register("tormented_mind", () -> new TormentedMindItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RUINE = ITEMS.register("ruine", () -> new RuineItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS).rarity(Rarity.RARE)));
	//public static final RegistryObject<Item> ENDER_BLASTER = ITEMS.register("ender_blaster", () -> new RuineItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS).rarity(Rarity.RARE)));
	
	public static final RegistryObject<Item> IRON_GREATAXE = ITEMS.register("iron_greataxe", () -> new GreataxeItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS), Tiers.IRON));
	public static final RegistryObject<Item> GOLDEN_GREATAXE = ITEMS.register("golden_greataxe", () -> new GreataxeItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS), Tiers.GOLD));
	public static final RegistryObject<Item> DIAMOND_GREATAXE = ITEMS.register("diamond_greataxe", () -> new GreataxeItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS), Tiers.DIAMOND));
	public static final RegistryObject<Item> NETHERITE_GREATAXE = ITEMS.register("netherite_greataxe", () -> new GreataxeItem(new Item.Properties().tab(EpicFightItemGroup.ITEMS), Tiers.NETHERITE));
}
