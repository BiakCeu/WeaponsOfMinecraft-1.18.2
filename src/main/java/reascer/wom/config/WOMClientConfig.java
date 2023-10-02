package reascer.wom.config;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import yesman.epicfight.api.utils.math.Vec2i;

public class WOMClientConfig {
	public final BooleanValue stongerMobs;
	public final BooleanValue mobsDropEmeralds;
	public final BooleanValue mobsMoreXP;
	public final BooleanValue skeletonMelee;
	public final IntValue skeletonMeleePercent;
	
	public WOMClientConfig(ForgeConfigSpec.Builder config) {
		this.stongerMobs = config.define("ingame.boosted_mob_over_distance", () -> true);
		this.mobsDropEmeralds = config.define("ingame.mob_drop_emerald_over_health", () -> true);
		this.mobsMoreXP = config.define("ingame.mob_more_xp_over_health", () -> true);
		this.skeletonMelee = config.define("ingame.skeleton_melee", () -> true);
		this.skeletonMeleePercent = config.defineInRange("ingame.skeleton_mele_percentage", 50, 1, 100);
		
	}
	
	private static final BiFunction<Integer, Integer, Integer> ORIGIN = ((screenLength, value) -> value);
	private static final BiFunction<Integer, Integer, Integer> SCREEN_EDGE = ((screenLength, value) -> screenLength - value);
	private static final BiFunction<Integer, Integer, Integer> CENTER = ((screenLength, value) -> screenLength / 2 + value);
	private static final BiFunction<Integer, Integer, Integer> CENTER_SAVE = ((screenLength, value) -> value - screenLength / 2);
	
	public static enum HorizontalBasis {
		LEFT(WOMClientConfig.ORIGIN, WOMClientConfig.ORIGIN), RIGHT(WOMClientConfig.SCREEN_EDGE, WOMClientConfig.SCREEN_EDGE), CENTER(WOMClientConfig.CENTER, WOMClientConfig.CENTER_SAVE);
		
		public BiFunction<Integer, Integer, Integer> positionGetter;
		public BiFunction<Integer, Integer, Integer> saveCoordGetter;
		
		HorizontalBasis(BiFunction<Integer, Integer, Integer> positionGetter, BiFunction<Integer, Integer, Integer> saveCoordGetter) {
			this.positionGetter = positionGetter;
			this.saveCoordGetter = saveCoordGetter;
		}
	}
	
	public static enum VerticalBasis {
		TOP(WOMClientConfig.ORIGIN, WOMClientConfig.ORIGIN), BOTTOM(WOMClientConfig.SCREEN_EDGE, WOMClientConfig.SCREEN_EDGE), CENTER(WOMClientConfig.CENTER, WOMClientConfig.CENTER_SAVE);
		
		public BiFunction<Integer, Integer, Integer> positionGetter;
		public BiFunction<Integer, Integer, Integer> saveCoordGetter;
		
		VerticalBasis(BiFunction<Integer, Integer, Integer> positionGetter, BiFunction<Integer, Integer, Integer> saveCoordGetter) {
			this.positionGetter = positionGetter;
			this.saveCoordGetter = saveCoordGetter;
		}
	}
	
	@FunctionalInterface
	public interface StartCoordGetter {
		public Vec2i get(int x, int y, int width, int height, int icons, HorizontalBasis horBasis, VerticalBasis verBasis);
	}
	
	private static final StartCoordGetter START_HORIZONTAL = (x, y, width, height, icons, horBasis, verBasis) -> {
		if (horBasis == HorizontalBasis.CENTER) {
			return new Vec2i(x - width * (icons - 1) / 2, y);
		} else {
			return new Vec2i(x, y);
		}
	};
	
	private static final StartCoordGetter START_VERTICAL = (x, y, width, height, icons, horBasis, verBasis) -> {
		if (verBasis == VerticalBasis.CENTER) {
			return new Vec2i(x, y - height * (icons - 1) / 2);
		} else {
			return new Vec2i(x, y);
		}
	};
	
	@FunctionalInterface
	public interface NextCoordGetter {
		public Vec2i getNext(HorizontalBasis horBasis, VerticalBasis verBasis, Vec2i prevCoord, int width, int height);
	}
	
	private static final NextCoordGetter NEXT_HORIZONTAL = (horBasis, verBasis, oldPos, width, height) -> {
		if (horBasis == HorizontalBasis.LEFT || horBasis == HorizontalBasis.CENTER) {
			return new Vec2i(oldPos.x + width, oldPos.y);
		} else {
			return new Vec2i(oldPos.x - width, oldPos.y);
		}
	};
	
	private static final NextCoordGetter NEXT_VERTICAL = (horBasis, verBasis, oldPos, width, height) -> {
		if (verBasis == VerticalBasis.TOP || verBasis == VerticalBasis.CENTER) {
			return new Vec2i(oldPos.x, oldPos.y + height);
		} else {
			return new Vec2i(oldPos.x, oldPos.y - height);
		}
	};
	
	public static enum AlignDirection {
		HORIZONTAL(START_HORIZONTAL, NEXT_HORIZONTAL), VERTICAL(START_VERTICAL, NEXT_VERTICAL);
		
		public StartCoordGetter startCoordGetter;
		public NextCoordGetter nextPositionGetter;
		
		AlignDirection(StartCoordGetter startCoordGetter, NextCoordGetter nextPositionGetter) {
			this.startCoordGetter = startCoordGetter;
			this.nextPositionGetter = nextPositionGetter;
		}
	}
	
	public static enum HealthBarShowOptions {
		NONE, HURT, TARGET;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
		
		public HealthBarShowOptions nextOption() {
			return HealthBarShowOptions.values()[(this.ordinal() + 1) % 3];
		}
	}
}