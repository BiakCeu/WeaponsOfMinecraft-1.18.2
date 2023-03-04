package reascer.wom.world.loot;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.data.loot.function.SetRandomSkillFunction;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID)
public class EFModLootTables {
	@SubscribeEvent
	public static void modifyVanillaLootPools(final LootTableLoadEvent event) {
    	if (event.getName().equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)) {
    		event.getTable().addPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 2.0F))
    			.add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get()).setWeight(1).apply(SetRandomSkillFunction.builder()))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(1))
    		.build());
    		
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.RUINE.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(3))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.STRONGHOLD_CROSSING)) {
    		event.getTable().addPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 2.0F))
    			.add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get()).setWeight(1).apply(SetRandomSkillFunction.builder()))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(1))
    		.build());
    		
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.RUINE.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(3))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.SIMPLE_DUNGEON)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.AGONY.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(5))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.WOODLAND_MANSION)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.TORMENTED_MIND.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(3))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.PILLAGER_OUTPOST)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.TORMENTED_MIND.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(5))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.END_CITY_TREASURE)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.ENDER_BLASTER.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(5))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.NETHER_BRIDGE)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.DEMON_SEAL.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(20))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.BASTION_TREASURE)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.DEMON_SEAL.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(5))
    		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.UNDERWATER_RUIN_BIG)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.HERRSCHER.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(20))
    		.build());
    		
    		event.getTable().addPool(LootPool.lootPool()
        			.add(LootItem.lootTableItem(WOMItems.GESETZ.get()).setWeight(1))
        			.add(LootItem.lootTableItem(Items.AIR).setWeight(20))
        		.build());
    	}
    	
    	if (event.getName().equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL)) {
    		event.getTable().addPool(LootPool.lootPool()
    			.add(LootItem.lootTableItem(WOMItems.HERRSCHER.get()).setWeight(1))
    			.add(LootItem.lootTableItem(Items.AIR).setWeight(50))
    		.build());
    		
    		event.getTable().addPool(LootPool.lootPool()
        			.add(LootItem.lootTableItem(WOMItems.GESETZ.get()).setWeight(1))
        			.add(LootItem.lootTableItem(Items.AIR).setWeight(20))
        		.build());
    	}
    	
    }
}

