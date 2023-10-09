package reascer.wom.events.engine;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponsOfMinecraft;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

@Mod.EventBusSubscriber(modid = WeaponsOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WOMRenderEngine {
	
	@SubscribeEvent
	public static void registerRenderer(PatchedRenderersEvent.Add event) {
		event.addItemRenderer(WOMItems.SATSUJIN.get(), new RenderSatsujin());
		event.addItemRenderer(WOMItems.GESETZ.get(), new RenderGesetz());
		event.addItemRenderer(WOMItems.MOONLESS.get(), new RenderMoonless());
	}
}
