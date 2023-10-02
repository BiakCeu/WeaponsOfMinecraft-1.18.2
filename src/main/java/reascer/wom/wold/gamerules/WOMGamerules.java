package reascer.wom.wold.gamerules;

import net.minecraft.world.level.GameRules;
import reascer.wom.config.WOMConfigManager;

public class WOMGamerules {
	public static GameRules.Key<GameRules.BooleanValue> SPAWN_STONGER_MOB_OVER_DISTANCE;
	public static GameRules.Key<GameRules.BooleanValue> STONGER_MOB_DROP_EMERALDS;
	public static GameRules.Key<GameRules.BooleanValue> STONGER_MOB_GIVE_MORE_EXP;
	
	public static GameRules.Key<GameRules.IntegerValue> SKELETON_MELEE_PERCENTAGE;
	
	public static void registerRules() {
		SPAWN_STONGER_MOB_OVER_DISTANCE = GameRules.register("spawnStrongerMobs", GameRules.Category.MOBS, GameRules.BooleanValue.create(WOMConfigManager.SPAWN_STONGER_MOB_OVER_DISTANCE.get()));
		STONGER_MOB_DROP_EMERALDS = GameRules.register("strongerMobsDropEmeralds", GameRules.Category.MOBS, GameRules.BooleanValue.create(WOMConfigManager.STONGER_MOB_DROP_EMERALDS.get()));
		STONGER_MOB_GIVE_MORE_EXP = GameRules.register("strongerMobsMoreEXP", GameRules.Category.MOBS, GameRules.BooleanValue.create(WOMConfigManager.STONGER_MOB_GIVE_MORE_EXP.get()));
		
		SKELETON_MELEE_PERCENTAGE = GameRules.register("skeletonMoreMelee", GameRules.Category.MOBS, GameRules.IntegerValue.create(WOMConfigManager.SKELETON_MELEE_PERCENTAGE.get()));
	}
}