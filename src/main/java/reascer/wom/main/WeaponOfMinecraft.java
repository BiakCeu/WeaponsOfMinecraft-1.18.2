package reascer.wom.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import reascer.wom.client.model.EFClientModels;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.gameasset.EFEnchantment;
import reascer.wom.gameasset.EFSkills;
import reascer.wom.particle.EFEpicFightParticles;
import reascer.wom.world.capabilities.EFLivingEntity;
import reascer.wom.world.item.EFECItems;
// The value here should match an entry in the META-INF/mods.toml file
@Mod("wom")
public class WeaponOfMinecraft
{
	public static final String MODID = "wom";
	public static final String CONFIG_FILE_PATH = WeaponOfMinecraft.MODID + ".toml";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
    public WeaponOfMinecraft()
    {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	bus.addListener(this::doClientStuff);
    	bus.addListener(EFAnimations::registerAnimations);
    	bus.addListener(EFSkills::registerSkills);
    	
    	EFECItems.ITEMS.register(bus);
    	EFEpicFightParticles.PARTICLES.register(bus);
    	EFEnchantment.ENCHANTEMENTS.register(bus);
    	
    	MinecraftForge.EVENT_BUS.register(this);
        
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    	ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
    	EFClientModels.LOGICAL_CLIENT.loadModels(resourceManager);
    }
}
