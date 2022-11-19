package reascer.wom.client.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.compress.utils.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.ClientModel;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.main.EpicFightMod;

public class EFClientModels extends Models<ClientModel> implements PreparableReloadListener {
	public static final EFClientModels LOGICAL_CLIENT = new EFClientModels();
	
	/** Particles **/
	public final ClientModel ruine_plunde_sword;
	
	public EFClientModels() {
		this.ruine_plunde_sword = register(new ResourceLocation(EpicFightMod.MODID, "particle/ruine_plunder_sword"));
	}
	
	@Override
	public ClientModel register(ResourceLocation rl) {
		ClientModel model = new ClientModel(rl);
		this.register(rl, model);
		return model;
	}
	
	public void register(ResourceLocation rl, ClientModel model) {
		this.models.put(rl, model);
	}
	
	public void loadModels(ResourceManager resourceManager) {
		List<ResourceLocation> emptyResourceLocations = Lists.newArrayList();
		
		this.models.entrySet().forEach((entry) -> {
			if (!entry.getValue().loadMeshAndProperties(resourceManager)) {
				emptyResourceLocations.add(entry.getKey());
			}
		});
		
		emptyResourceLocations.forEach(this.models::remove);
	}
	
	@Override
	public Models<?> getModels(boolean isLogicalClient) {
		return isLogicalClient ? LOGICAL_CLIENT : LOGICAL_SERVER;
	}
	
	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return CompletableFuture.runAsync(() -> {
			this.loadModels(resourceManager);
			this.loadArmatures(resourceManager);
		}, gameExecutor).thenCompose(stage::wait);
	}
}