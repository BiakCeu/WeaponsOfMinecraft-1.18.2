package reascer.wom.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import reascer.wom.config.WOMConfigurationIngame;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.gameasset.WOMEnchantment;
import reascer.wom.gameasset.WOMSkills;
import reascer.wom.particle.WOMParticles;
import reascer.wom.wold.gamerules.WOMGamerules;
import reascer.wom.world.entity.projectile.WOMEntities;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.client.gui.screen.IngameConfigurationScreen;
import yesman.epicfight.config.ConfigManager;
import yesman.epicfight.config.ConfigurationIngame;
import yesman.epicfight.data.loot.EpicFightLootModifiers;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.server.commands.arguments.SkillArgument;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;
import yesman.epicfight.world.capabilities.provider.EntityPatchProvider;
import yesman.epicfight.world.capabilities.provider.ItemCapabilityProvider;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.effect.EpicFightPotions;
import yesman.epicfight.world.entity.EpicFightEntities;
import yesman.epicfight.world.gamerule.EpicFightGamerules;
// The value here should match an entry in the META-INF/mods.toml file
@Mod("wom")
public class WeaponsOfMinecraft
{
	public static final String MODID = "wom";
	public static final String CONFIG_FILE_PATH = WeaponsOfMinecraft.MODID + ".toml";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	//public static WOMConfigurationIngame CLIENT_INGAME_CONFIG;
	private static WeaponsOfMinecraft instance;
	
	public static WeaponsOfMinecraft getInstance() {
		return instance;
	}
	
    public WeaponsOfMinecraft() {
    	instance = this;
    	//ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_CONFIG);
    	
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	bus.addListener(this::doClientStuff);
    	bus.addListener(this::doCommonStuff);
    	bus.addListener(WOMAnimations::registerAnimations);
    	
    	WOMItems.ITEMS.register(bus);
    	WOMParticles.PARTICLES.register(bus);
    	WOMEnchantment.ENCHANTEMENTS.register(bus);
    	WOMEntities.ENTITIES.register(bus);
    	WOMSkills.registerSkills();
    	
    	MinecraftForge.EVENT_BUS.register(this);
    	//ConfigManager.loadConfig(ConfigManager.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml").toString());
        //ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_PATH).toString());
        //ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(IngameConfigurationScreen::new));
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    	//CLIENT_INGAME_CONFIG = new WOMConfigurationIngame();
    }
    
    private void doCommonStuff(final FMLCommonSetupEvent event) {
		event.enqueueWork(WOMGamerules::registerRules);
    }
}
