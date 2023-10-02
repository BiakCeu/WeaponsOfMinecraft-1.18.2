package reascer.wom.world.entity.ai.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reascer.wom.main.WeaponsOfMinecraft;

public class WOMAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, WeaponsOfMinecraft.MODID);
    public static final RegistryObject<Attribute> SHIELD = ATTRIBUTES.register("shield", () -> new RangedAttribute("attribute.name." + WeaponsOfMinecraft.MODID + ".shield", 0.0D, 0.0D, 1024.0D).setSyncable(true));
}