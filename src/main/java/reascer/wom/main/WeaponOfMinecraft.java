package reascer.wom.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMEnchantment;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.particle.WOMParticles;
import reascer.wom.world.entity.projectile.WOMEntities;
import reascer.wom.world.item.WOMItems;
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
    	bus.addListener(WOMAnimations::registerAnimations);
    	
    	WOMItems.ITEMS.register(bus);
    	WOMParticles.PARTICLES.register(bus);
    	WOMEnchantment.ENCHANTEMENTS.register(bus);
    	WOMEntities.ENTITIES.register(bus);
    	WOMSkills.registerSkills();
    	
    	MinecraftForge.EVENT_BUS.register(this);
        
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    }
}
