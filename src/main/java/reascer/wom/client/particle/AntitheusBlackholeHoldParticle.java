package reascer.wom.client.particle;

import java.util.Random;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.particle.WOMParticles;
import yesman.epicfight.client.particle.HitParticle;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class AntitheusBlackholeHoldParticle extends HitParticle {
	public AntitheusBlackholeHoldParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
		super(world, x, y, z, animatedSprite);
	    this.rCol = 0.0F;
	    this.gCol = 0.0F;
	    this.bCol = 0.0F;
	    this.quadSize = 5F;
		this.lifetime = 80;
		this.setSpriteFromAge(animatedSprite);
		this.alpha = 2;
		float angle = 0.0f;
		this.oRoll = angle;
		this.roll = angle;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	
	@Override
	public void tick() {
		  this.xo = this.x;
		  this.yo = this.y;
		  this.zo = this.z;
		  if (this.age++ >= this.lifetime) {
			 this.level.addParticle(WOMParticles.ANTITHEUS_BLACKHOLE_END.get(), this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
		     this.remove();
		  } else {
			  if (!this.removed) {
			         this.setSprite(animatedSprite.get(this.age%12, 12));
			  }
		  }
		  double r = 10.0; // set the radius of the hemisphere to 1
		  int n = 10;
		  for (int i = 0; i < n; i++) {
		    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
		    double phi = Math.acos(2 * new Random().nextDouble() - 1); // generate a random polar angle in the upper hemisphere
	
		    // calculate the emission direction in Cartesian coordinates using the polar coordinates
		    double x = r * Math.sin(phi) * Math.cos(theta);
		    double y = r * Math.sin(phi) * Math.sin(theta);
		    double z = r * Math.cos(phi);
	
		    // emit the particle in the calculated direction, with some random velocity added
		    this.level.addParticle(ParticleTypes.SMOKE,
		        (this.x + x),
		        (this.y-0.5f + y),
		        (this.z + z),
		        (float)(-x * 0.09f),
		        (float)(-y * 0.09f),
		        (float)(-z * 0.09f));
		  }
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet) {
	         this.spriteSet = spriteSet;
	    }
	    
		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			AntitheusBlackholeHoldParticle particle = new AntitheusBlackholeHoldParticle(worldIn, x, y, z, spriteSet);
			return particle;
		}
	}
}