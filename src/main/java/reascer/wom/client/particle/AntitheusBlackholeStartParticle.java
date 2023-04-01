package reascer.wom.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMSounds;
import reascer.wom.particle.WOMParticles;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;
import yesman.epicfight.client.particle.HitParticle;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class AntitheusBlackholeStartParticle extends HitParticle {
	public AntitheusBlackholeStartParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
		super(world, x, y, z, animatedSprite);
	    this.rCol = 0.0F;
	    this.gCol = 0.0F;
	    this.bCol = 0.0F;
	    this.quadSize = 5F;
		this.lifetime = 21;
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
	    	 this.level.addParticle(WOMParticles.ANTITHEUS_BLACKHOLE_HOLD.get(), this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
	         this.remove();
	      } else {
	         this.setSpriteFromAge(this.animatedSprite);
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
			AntitheusBlackholeStartParticle particle = new AntitheusBlackholeStartParticle(worldIn, x, y, z, spriteSet);
			return particle;
		}
	}
}