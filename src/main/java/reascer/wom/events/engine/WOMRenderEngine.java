package reascer.wom.events.engine;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.renderer.patched.item.RenderKatana;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class WOMRenderEngine {
	@SubscribeEvent
	public static void registerRenderer(PatchedRenderersEvent.Add event) {
		event.addItemRenderer(WOMItems.SATSUJIN.get(), new RenderSatsujin());
		event.addItemRenderer(WOMItems.GESETZ.get(), new RenderGesetz());
	}
}
