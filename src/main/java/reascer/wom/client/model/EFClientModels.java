package reascer.wom.client.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.compress.utils.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.api.client.model.Mesh.RawMesh;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.main.EpicFightMod;
 
@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFClientModels{
	public static final EFClientModels LOGICAL_CLIENT = new EFClientModels();
	
	//public final RawMesh RUINE_PLUNDER_SWORD;
	
	@SubscribeEvent
	public static void registerMeshes(ModelBuildEvent event) {
		//event.
	}
}
	