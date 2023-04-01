package reascer.wom.client.model;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.api.client.model.Mesh.RawMesh;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.main.EpicFightMod;
 
@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFClientModels{
	public static RawMesh RUINE_PLUNDER_SWORD;
	public static RawMesh ENDER_BULLET;
	public static RawMesh ANTITHEUS_DARKNESS;
	
	@SubscribeEvent
	public static void registerMeshes(ModelBuildEvent.MeshBuild event) {
		RUINE_PLUNDER_SWORD = event.getRaw(WeaponOfMinecraft.MODID, "particle/ruine_plunder_sword", RawMesh::new);
		ENDER_BULLET = event.getRaw(WeaponOfMinecraft.MODID, "entity/enderbullet", RawMesh::new);
		ANTITHEUS_DARKNESS = event.getRaw(WeaponOfMinecraft.MODID, "entity/antitheus_darkness", RawMesh::new);
	}
}
	