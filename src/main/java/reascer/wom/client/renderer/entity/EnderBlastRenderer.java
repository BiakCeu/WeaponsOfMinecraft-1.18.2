package reascer.wom.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.client.model.EnderBlastModel;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.world.entity.projectile.EnderBullet;

@OnlyIn(Dist.CLIENT)
public class EnderBlastRenderer extends EntityRenderer<EnderBullet> {
   private final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(WeaponOfMinecraft.MODID,"textures/item/ruine_texture.png");
   private final Model model;

   public EnderBlastRenderer(EntityRendererProvider.Context p_174449_) {
      super(p_174449_);
      this.model = new EnderBlastModel(RenderType::entityTranslucent);
   }

   protected int getBlockLightLevel(EnderBullet p_116491_, BlockPos p_116492_) {
      return 15;
   }

   public void render(EnderBullet enderBullet, float p_116485_, float p_116486_, PoseStack poseStack, MultiBufferSource p_116488_, int p_116489_) {
      poseStack.pushPose();
      poseStack.scale(-1.0F, -1.0F, 1.0F);
      float f = Mth.lerp(1, enderBullet.yRotO, enderBullet.getYRot());
      float f1 = Mth.lerp(1, enderBullet.xRotO, enderBullet.getXRot());
      VertexConsumer vertexconsumer = p_116488_.getBuffer(this.model.renderType(this.getTextureLocation(enderBullet)));
      poseStack.mulPose(Vector3f.YP.rotationDegrees(f));
      poseStack.mulPose(Vector3f.XP.rotationDegrees(f1));
      this.model.renderToBuffer(poseStack, vertexconsumer, p_116489_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      poseStack.popPose();
      super.render(enderBullet, p_116485_, p_116486_, poseStack, p_116488_, p_116489_);
   }
   
   public ResourceLocation getTextureLocation(EnderBullet p_116482_) {
      return TEXTURE_LOCATION;
   }
}