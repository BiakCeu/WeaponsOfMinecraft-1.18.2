package reascer.wom.client.model;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoader;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Mesh.RawMesh;
import yesman.epicfight.api.client.model.Mesh.RenderProperties;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;
import yesman.epicfight.api.client.model.VertexIndicator.AnimatedVertexIndicator;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.api.model.JsonModelLoader;
import yesman.epicfight.client.mesh.CreeperMesh;
import yesman.epicfight.client.mesh.DragonMesh;
import yesman.epicfight.client.mesh.EndermanMesh;
import yesman.epicfight.client.mesh.HoglinMesh;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.mesh.IronGolemMesh;
import yesman.epicfight.client.mesh.PiglinMesh;
import yesman.epicfight.client.mesh.RavagerMesh;
import yesman.epicfight.client.mesh.SpiderMesh;
import yesman.epicfight.client.mesh.VexMesh;
import yesman.epicfight.client.mesh.VillagerMesh;
import yesman.epicfight.client.mesh.WitherMesh;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class WomMeshes implements PreparableReloadListener {
	
	public static final WomMeshes INSTANCE = new WomMeshes();
	
	@FunctionalInterface
	public static interface MeshContructor<V extends VertexIndicator, M extends Mesh<V>> {
		public M invoke(Map<String, float[]> arrayMap, M parent, RenderProperties properties, Map<String, ModelPart<V>> parts);
	}
		
	private static final Map<ResourceLocation, Mesh<?>> MESHES = Maps.newHashMap();
	
	public static RawMesh RUINE_PLUNDER_SWORD;
	
	public static void build(ResourceManager resourceManager) {
		MESHES.clear();
		ModelBuildEvent.MeshBuild event = new ModelBuildEvent.MeshBuild(resourceManager, MESHES);
		RUINE_PLUNDER_SWORD = event.getRaw(EpicFightMod.MODID, "particle/ruine_plunder_sword", RawMesh::new);
		
		ModLoader.get().postEvent(event);
	}
	
	public static void addMesh(ResourceLocation rl, AnimatedMesh animatedMesh) {
		MESHES.put(rl, animatedMesh);
	}
	
	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return CompletableFuture.runAsync(() -> {
			WomMeshes.build(resourceManager);
		}, gameExecutor).thenCompose(stage::wait);
	}
}