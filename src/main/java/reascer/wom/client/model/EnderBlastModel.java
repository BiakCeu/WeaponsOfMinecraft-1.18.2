package reascer.wom.client.model;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.Mesh.RawMesh;

@OnlyIn(Dist.CLIENT)
public class EnderBlastModel extends Model {
	
	private final RawMesh Enderbullet;
	
	public EnderBlastModel(Function<ResourceLocation, RenderType> p_103110_) {
		super(p_103110_);
		this.Enderbullet = EFClientModels.ENDER_BULLET;
	}
	
	@Override
	public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_,
			float p_103116_, float p_103117_, float p_103118_) {
		this.Enderbullet.drawRawModel(p_103111_, p_103112_, p_103113_, p_103115_, p_103116_, p_103117_, p_103118_, p_103114_);
	}
}