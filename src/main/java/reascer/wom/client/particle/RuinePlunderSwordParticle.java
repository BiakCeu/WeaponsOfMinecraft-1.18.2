package reascer.wom.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.client.model.EFClientModels;
import yesman.epicfight.api.client.model.ClientModel;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;
import yesman.epicfight.client.particle.TexturedCustomModelParticle;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class RuinePlunderSwordParticle extends TexturedCustomModelParticle {
	private float yO;
	
	public RuinePlunderSwordParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, ClientModel particleMesh, ResourceLocation texture) {
		super(level, x, y-10.0f, z, xd, yd, zd, particleMesh, texture);
		this.lifetime = 50;
		this.hasPhysics = false;
		this.roll = (float)xd;
		this.pitch = (float)zd;
		this.yaw = random.nextFloat()*90;
		this.yawO = this.yaw;
		this.scale = -1;
		this.yO = (float) y-1.5f;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return EpicFightParticleRenderTypes.PARTICLE_MODELED;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (((float) this.y + 2.0f) < yO) {
			this.yo = this.y;
			this.y += 2.0f;
		} else {
			this.y = this.yO;
			this.yo = this.y;
		}
		this.alpha = (float)(this.lifetime - this.age) / (float)this.lifetime;
	}
	
	@Override
	protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
		float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
		Vec3 vec3 = camera.getPosition();
		float x = (float)(Mth.lerp((double)partialTicks, this.xo, this.x) - vec3.x());
		float y = (float)(Mth.lerp((double)partialTicks, this.yo, this.y) - vec3.y());
		float z = (float)(Mth.lerp((double)partialTicks, this.zo, this.z) - vec3.z());
		float scale = (float)Mth.lerp((double)partialTicks, this.scaleO, this.scale);
		poseStack.translate(x, y, z);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(this.pitch));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(this.roll));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
		poseStack.scale(scale, scale, scale);
	}
	
	@Override
	public int getLightColor(float p_107086_) {
		int i = super.getLightColor(p_107086_);
		int k = i >> 16 & 255;
		return 240 | k << 16;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RuinePlunderSwordParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, EFClientModels.LOGICAL_CLIENT.ruine_plunde_sword, new ResourceLocation(EpicFightMod.MODID, "textures/item/ruine_texture.png"));
		}
	}
}