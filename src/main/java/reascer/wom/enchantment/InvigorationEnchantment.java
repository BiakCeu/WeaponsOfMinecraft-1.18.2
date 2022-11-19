package reascer.wom.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Enchantment granting efficiency
 */
@SuppressWarnings("WeakerAccess")
public class InvigorationEnchantment extends Enchantment {
  public InvigorationEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.ARMOR, slots);
  }

  @Override
  public int getMaxLevel() {
    return 5;
  }

  @Override
  protected boolean checkCompatibility(Enchantment enchant) {
    return this != enchant;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return stack.canApplyAtEnchantingTable(this);
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    // apply to shields on anvils if enabled
	if (stack.is(Item.byId(355))) {
      return true;
    }
    return super.canEnchant(stack);
  }

  @Override
  public boolean isAllowedOnBooks() {
    return true;
  }
}
