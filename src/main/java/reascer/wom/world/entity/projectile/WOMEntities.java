package reascer.wom.world.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.main.EpicFightMod;

public class WOMEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, WeaponOfMinecraft.MODID);
	
	public static final RegistryObject<EntityType<EnderBullet>> ENDERBLAST = ENTITIES.register("enderblast", () ->
		EntityType.Builder.<EnderBullet>of(EnderBullet::new, MobCategory.MISC)
		.sized(1f, 1f).clientTrackingRange(4).updateInterval(20).build("enderblast")
		);
}