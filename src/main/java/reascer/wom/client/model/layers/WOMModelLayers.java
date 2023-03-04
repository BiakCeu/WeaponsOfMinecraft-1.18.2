package reascer.wom.client.model.layers;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Stream;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WOMModelLayers {
	private static final String DEFAULT_LAYER = "main";
	private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
	public static final ModelLayerLocation EnderBlast = register("enderbullet");
   
 private static ModelLayerLocation register(String p_171294_) {
      return register(p_171294_, "Entity");
   }

   private static ModelLayerLocation register(String p_171296_, String p_171297_) {
      ModelLayerLocation modellayerlocation = createLocation(p_171296_, p_171297_);
      if (!ALL_MODELS.add(modellayerlocation)) {
         throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
      } else {
         return modellayerlocation;
      }
   }

   private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
      return new ModelLayerLocation(new ResourceLocation("wom", p_171301_), p_171302_);
   }

   private static ModelLayerLocation registerInnerArmor(String p_171299_) {
      return register(p_171299_, "inner_armor");
   }

   private static ModelLayerLocation registerOuterArmor(String p_171304_) {
      return register(p_171304_, "outer_armor");
   }

   public static ModelLayerLocation createBoatModelName(Boat.Type p_171290_) {
      return createLocation("boat/" + p_171290_.getName(), "main");
   }

   public static ModelLayerLocation createSignModelName(WoodType p_171292_) {
      ResourceLocation location = new ResourceLocation(p_171292_.name());
      return new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "sign/" + location.getPath()), "main");
   }

   public static Stream<ModelLayerLocation> getKnownLocations() {
      return ALL_MODELS.stream();
   }
}
