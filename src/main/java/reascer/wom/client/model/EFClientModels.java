package reascer.wom.client.model;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.api.client.model.Mesh.RawMesh;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;

@Mod.EventBusSubscriber(modid = WeaponsOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFClientModels{
	public static RawMesh RUINE_PLUNDER_SWORD;
	public static RawMesh ENDER_BULLET;
	public static RawMesh ANTITHEUS_DARKNESS;
	
	@SubscribeEvent
	public static void registerMeshes(ModelBuildEvent.MeshBuild event) {
		RUINE_PLUNDER_SWORD = event.getRaw(WeaponsOfMinecraft.MODID, "particle/ruine_plunder_sword", RawMesh::new);
		ENDER_BULLET = event.getRaw(WeaponsOfMinecraft.MODID, "entity/enderbullet", RawMesh::new);
		ANTITHEUS_DARKNESS = event.getRaw(WeaponsOfMinecraft.MODID, "entity/antitheus_darkness", RawMesh::new);
	}
}
	