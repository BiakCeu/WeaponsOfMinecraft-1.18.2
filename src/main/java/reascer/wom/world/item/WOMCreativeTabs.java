package reascer.wom.world.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.main.EpicFightMod;

public class WOMCreativeTabs {
	public static final CreativeModeTab ITEMS = new CreativeModeTab(WeaponOfMinecraft.MODID + ".items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(WOMItems.AGONY.get());
        }
    };
}