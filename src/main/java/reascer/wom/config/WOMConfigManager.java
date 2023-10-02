package reascer.wom.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import reascer.wom.main.WeaponsOfMinecraft;
import yesman.epicfight.main.EpicFightMod;

public class WOMConfigManager {
	public static final ForgeConfigSpec COMMON_CONFIG;
	public static final ForgeConfigSpec CLIENT_CONFIG;
	public static final WOMClientConfig INGAME_CONFIG;
	
	public static final ForgeConfigSpec.BooleanValue SPAWN_STONGER_MOB_OVER_DISTANCE;
	public static final ForgeConfigSpec.BooleanValue STONGER_MOB_DROP_EMERALDS;
	public static final ForgeConfigSpec.BooleanValue STONGER_MOB_GIVE_MORE_EXP;
	
	public static final ForgeConfigSpec.IntValue SKELETON_MELEE_PERCENTAGE;
	
	static {
		CommentedFileConfig file = CommentedFileConfig.builder(new File(FMLPaths.CONFIGDIR.get().resolve(WeaponsOfMinecraft.CONFIG_FILE_PATH).toString())).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		ForgeConfigSpec.Builder client = new ForgeConfigSpec.Builder();
		ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();
		
		SPAWN_STONGER_MOB_OVER_DISTANCE = server.define("default_gamerule.spawnStrongerMobs", false);
		STONGER_MOB_DROP_EMERALDS = server.define("default_gamerule.strongerMobsDropEmeralds", false);
		STONGER_MOB_GIVE_MORE_EXP = server.define("default_gamerule.strongerMobsMoreEXP", false);
		
		SKELETON_MELEE_PERCENTAGE = server.defineInRange("default_gamerule.skeletonMoreMelee", 50, 0, 100);
		
		INGAME_CONFIG = new WOMClientConfig(client);
		CLIENT_CONFIG = client.build();
		COMMON_CONFIG = server.build();
	}
	
	public static void loadConfig(ForgeConfigSpec config, String path) {
		CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}
}