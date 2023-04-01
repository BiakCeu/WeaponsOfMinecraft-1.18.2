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
public class AntitheusDarknessModel extends Model {
	
	private final RawMesh darknessMesh;
	
	public AntitheusDarknessModel(Function<ResourceLocation, RenderType> p_103110_) {
		super(p_103110_);
		this.darknessMesh = EFClientModels.ANTITHEUS_DARKNESS;
	}
	
	@Override
	public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_,
			float p_103116_, float p_103117_, float p_103118_) {
		this.darknessMesh.drawRawModel(p_103111_, p_103112_, p_103113_, p_103115_, p_103116_, p_103117_, p_103118_, p_103114_);
	}
}