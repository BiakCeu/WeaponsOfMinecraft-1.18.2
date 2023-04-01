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
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.particle.HitParticle;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class AntitheusBlackholeEndParticle extends HitParticle {
	public AntitheusBlackholeEndParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
		super(world, x, y, z, animatedSprite);
	    this.rCol = 0.0F;
	    this.gCol = 0.0F;
	    this.bCol = 0.0F;
	    this.quadSize = 5F;
		this.lifetime = 26;
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
	         this.remove();
	        
	      } else {
	         this.setSpriteFromAge(this.animatedSprite);
	         if (age == 12) {
	        	 int n = 80; // set the number of particles to emit
	 			double r = 0.6; // set the radius of the disk to 1
	 			double t = 0.01; // set the thickness of the disk to 0.1
	 			
	 			for (int i = 0; i < n; i++) {
	 			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
	 			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

	 			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
	 			    double x = r * Math.cos(phi) * Math.cos(theta);
	 			    double y = r * Math.cos(phi) * Math.sin(theta);
	 			    double z = r * Math.sin(phi);
	 			    
	 				 // create a Vector3f object to represent the emission direction
	 				    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

	 				 // rotate the direction vector to align with the forward vector
	 				    OpenMatrix4f rotation = new OpenMatrix4f().rotate((float) Math.toRadians(100), new Vec3f(1, 0, 0));
	 				    OpenMatrix4f.transform3v(rotation, direction, direction);
	 			    
	 			    // emit the particle in the calculated direction, with some random velocity added
	 			    this.level.addParticle(ParticleTypes.LARGE_SMOKE,
	 			        (this.x),
	 			        (this.y),
	 			        (this.z),
	 			        (float)(direction.x),
	 			        (float)(direction.y),
	 			        (float)(direction.z));
	 			}
	 			for (int i = 0; i < n; i++) {
		 			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
		 			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

		 			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
		 			    double x = r * Math.cos(phi) * Math.cos(theta);
		 			    double y = r * Math.cos(phi) * Math.sin(theta);
		 			    double z = r * Math.sin(phi);
		 			    
		 				 // create a Vector3f object to represent the emission direction
		 				    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

		 				 // rotate the direction vector to align with the forward vector
		 				    OpenMatrix4f rotation = new OpenMatrix4f().rotate((float) Math.toRadians(80), new Vec3f(1, 0, 0));
		 				    OpenMatrix4f.transform3v(rotation, direction, direction);
		 			    
		 			    // emit the particle in the calculated direction, with some random velocity added
		 			    this.level.addParticle(ParticleTypes.LARGE_SMOKE,
		 			        (this.x),
		 			        (this.y),
		 			        (this.z),
		 			        (float)(direction.x),
		 			        (float)(direction.y),
		 			        (float)(direction.z));
	 			}
	         }
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
			AntitheusBlackholeEndParticle particle = new AntitheusBlackholeEndParticle(worldIn, x, y, z, spriteSet);
			return particle;
		}
	}
}