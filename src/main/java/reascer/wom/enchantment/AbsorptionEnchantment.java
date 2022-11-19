package reascer.wom.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Enchantment granting bonus absorption hearts
 */
@SuppressWarnings("WeakerAccess")
public class AbsorptionEnchantment extends Enchantment {
  public AbsorptionEnchantment(Rarity rarity, EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.ARMOR, slots);
  }

  @Override
  public int getMaxLevel() {
    return Math.max(10, 1);
  }

  @Override
  protected boolean checkCompatibility(Enchantment enchant) {
    return this != enchant;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return 10 > 0 && stack.canApplyAtEnchantingTable(this);
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    if (stack.is(Item.byId(355))) {
      return true;
    }
    return super.canEnchant(stack);
  }

  @Override
  public boolean isAllowedOnBooks() {
    return 10 > 0;
  }
}
