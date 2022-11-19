package reascer.wom.events;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.main.WeaponOfMinecraft;

@Mod.EventBusSubscriber(modid=WeaponOfMinecraft.MODID, bus=EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
	public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
    }
}