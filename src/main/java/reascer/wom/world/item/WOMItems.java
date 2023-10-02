package reascer.wom.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.main.EpicFightMod;

public class WOMItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WeaponsOfMinecraft.MODID);
	
	public static final RegistryObject<Item> AGONY = ITEMS.register("agony", () -> new AgonySpearItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> TORMENTED_MIND = ITEMS.register("tormented_mind", () -> new TormentedMindItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> RUINE = ITEMS.register("ruine", () -> new RuineItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> ENDER_BLASTER = ITEMS.register("ender_blaster", () -> new EnderBlasterItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> ANTITHEUS = ITEMS.register("antitheus", () -> new AntitheusItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> SATSUJIN = ITEMS.register("satsujin", () -> new SatsujinItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> SATSUJIN_SHEATH = ITEMS.register("satsujin_sheath", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> HERRSCHER = ITEMS.register("herrscher", () -> new HerscherItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE).defaultDurability(1582).durability(1582)));
	public static final RegistryObject<Item> GESETZ = ITEMS.register("gesetz", () -> new MagneticShieldItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE).defaultDurability(4157).durability(4157)));
	public static final RegistryObject<Item> GESETZ_HANDLE = ITEMS.register("gesetz_handle", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MOONLESS = ITEMS.register("moonless", () -> new MoonlessItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> MOONLESS_HANDLE = ITEMS.register("moonless_handle", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
	
	public static final RegistryObject<Item> IRON_GREATAXE = ITEMS.register("iron_greataxe", () -> new GreataxeItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.IRON));
	public static final RegistryObject<Item> GOLDEN_GREATAXE = ITEMS.register("golden_greataxe", () -> new GreataxeItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.GOLD));
	public static final RegistryObject<Item> DIAMOND_GREATAXE = ITEMS.register("diamond_greataxe", () -> new GreataxeItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.DIAMOND));
	public static final RegistryObject<Item> NETHERITE_GREATAXE = ITEMS.register("netherite_greataxe", () -> new GreataxeItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.NETHERITE));
	
	public static final RegistryObject<Item> WOODEN_STAFF = ITEMS.register("wooden_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.WOOD));
	public static final RegistryObject<Item> STONE_STAFF = ITEMS.register("stone_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.STONE));
	public static final RegistryObject<Item> IRON_STAFF = ITEMS.register("iron_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.IRON));
	public static final RegistryObject<Item> GOLDEN_STAFF = ITEMS.register("golden_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.GOLD));
	public static final RegistryObject<Item> DIAMOND_STAFF = ITEMS.register("diamond_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.DIAMOND));
	public static final RegistryObject<Item> NETHERITE_STAFF = ITEMS.register("netherite_staff", () -> new StaffItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS), Tiers.NETHERITE));
	
	public static final RegistryObject<Item> NETHERITE_MASK = ITEMS.register("netherite_mask", () -> new ArtefactsItem(WomArmorMaterials.NETHERITE, EquipmentSlot.HEAD, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> NETHERITE_MANICLE = ITEMS.register("netherite_manicle", () -> new ArtefactsItem(WomArmorMaterials.NETHERITE, EquipmentSlot.CHEST, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> NETHERITE_BELT = ITEMS.register("netherite_belt", () -> new ArtefactsItem(WomArmorMaterials.NETHERITE, EquipmentSlot.LEGS, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> NETHERITE_CHAINS = ITEMS.register("netherite_chains", () -> new ArtefactsItem(WomArmorMaterials.NETHERITE, EquipmentSlot.FEET, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	
	public static final RegistryObject<Item> EMERALD_EARRINGS = ITEMS.register("emerald_earrings", () -> new ArtefactsItem(WomArmorMaterials.EMERALD, EquipmentSlot.HEAD, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> EMERALD_CHAKRA = ITEMS.register("emerald_chakra", () -> new ArtefactsItem(WomArmorMaterials.EMERALD, EquipmentSlot.CHEST, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> EMERALD_TASSET = ITEMS.register("emerald_tasset", () -> new ArtefactsItem(WomArmorMaterials.EMERALD, EquipmentSlot.LEGS, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> EMERALD_ANKLEBRACELET = ITEMS.register("emerald_anklebracelet", () -> new ArtefactsItem(WomArmorMaterials.EMERALD, EquipmentSlot.FEET, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	
	public static final RegistryObject<Item> DIAMOND_CROWN = ITEMS.register("diamond_crown", () -> new ArtefactsItem(WomArmorMaterials.DIAMOND, EquipmentSlot.HEAD, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> DIAMOND_ARMBRACELET = ITEMS.register("diamond_armbracelet", () -> new ArtefactsItem(WomArmorMaterials.DIAMOND, EquipmentSlot.CHEST, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> DIAMOND_LEGTOPSEAL = ITEMS.register("diamond_legtopseal", () -> new ArtefactsItem(WomArmorMaterials.DIAMOND, EquipmentSlot.LEGS, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> DIAMOND_LEGBOTTOMSEAL = ITEMS.register("diamond_legbottomseal", () -> new ArtefactsItem(WomArmorMaterials.DIAMOND, EquipmentSlot.FEET, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	
	public static final RegistryObject<Item> GOLDEN_MONOCLE = ITEMS.register("golden_monocle", () -> new ArtefactsItem(WomArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> GOLDEN_KIT = ITEMS.register("golden_kit", () -> new ArtefactsItem(WomArmorMaterials.GOLD, EquipmentSlot.CHEST, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> GOLDEN_CHRONO = ITEMS.register("golden_chrono", () -> new ArtefactsItem(WomArmorMaterials.GOLD, EquipmentSlot.LEGS, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	public static final RegistryObject<Item> GOLDEN_MOKASSIN = ITEMS.register("golden_mokassin", () -> new ArtefactsItem(WomArmorMaterials.GOLD, EquipmentSlot.FEET, new Item.Properties().tab(WOMCreativeTabs.ITEMS)));
	
	
	public static final RegistryObject<Item> DEMON_SEAL = ITEMS.register("demon_seal", () -> new DemonSealItem(new Item.Properties().tab(WOMCreativeTabs.ITEMS).rarity(Rarity.RARE).stacksTo(1)));
}
