package reascer.wom.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.HitParticleType;

public class EFEpicFightParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EpicFightMod.MODID);
	
	public static final RegistryObject<SimpleParticleType> ANTITHEUS_CUT = PARTICLES.register("antitheus_cut", () -> new SimpleParticleType(true));
	public static final RegistryObject<HitParticleType> ANTITHEUS_HIT = PARTICLES.register("antitheus_hit", () -> new HitParticleType(true, HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO));
	
	public static final RegistryObject<SimpleParticleType> ANTITHEUS_PUNCH = PARTICLES.register("antitheus_punch", () -> new SimpleParticleType(true));
	public static final RegistryObject<HitParticleType> ANTITHEUS_PUNCH_HIT = PARTICLES.register("antitheus_punch_hit", () -> new HitParticleType(true, HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO));

	public static final RegistryObject<SimpleParticleType> KATANA_SHEATHED_CUT = PARTICLES.register("katana_sheathed_cut", () -> new SimpleParticleType(true));
	public static final RegistryObject<HitParticleType> KATANA_SHEATHED_HIT = PARTICLES.register("katana_sheathed_hit", () -> new HitParticleType(true, HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO));

}