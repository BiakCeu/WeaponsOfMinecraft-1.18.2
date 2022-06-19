package reascer.wom.world.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.WeaponItem;

public class StaffItem extends WeaponItem {
	protected static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("16295ED8-B092-4A75-9A94-BCD8D56668BB");
	
	private final float attackDamage;
	private final float attackSpeed;
	
	public StaffItem(Item.Properties build, Tier tier) {
		super(tier, 0, 0.0F, build.defaultDurability(tier.getUses()*3));
		this.attackDamage = 1.5F + (tier.getAttackDamageBonus()/2);
		if (tier.getLevel() == 2) {
			this.attackSpeed = -1.25F - (0.2F * tier.getLevel());
		} else if (tier.getLevel() == 3) {
			this.attackSpeed = -1.25F - (0.05F * tier.getLevel());
		} else {
			this.attackSpeed = -1.25F - (0.1F * tier.getLevel());
		}
	}
	
	@Override
	public int getEnchantmentValue() {
		return 5;
	}
    
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
    		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, Operation.ADDITION));
    		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, Operation.ADDITION));
    	    return builder.build();
        }
        
        return super.getAttributeModifiers(slot, stack);
    }
}