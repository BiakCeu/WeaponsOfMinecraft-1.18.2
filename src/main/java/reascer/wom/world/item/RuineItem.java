package reascer.wom.world.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.item.EpicFightItemTier;
import yesman.epicfight.world.item.WeaponItem;

public class RuineItem extends WeaponItem {
	@OnlyIn(Dist.CLIENT)
	private List<Component> tooltipExpand;

	public RuineItem(Item.Properties build) {
		super(EpicFightItemTier.KATANA, 0, -2.45F, build);
		if (EpicFightMod.isPhysicalClient()) {
			this.tooltipExpand = new ArrayList<Component> ();
			this.tooltipExpand.add(new TextComponent(""));
			this.tooltipExpand.add(new TranslatableComponent("item." + EpicFightMod.MODID + ".ruine.tooltip"));
		}
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Items.OBSIDIAN;
	}
    
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		for (Component txtComp : tooltipExpand) {
			tooltip.add(txtComp);
		}
	}
}