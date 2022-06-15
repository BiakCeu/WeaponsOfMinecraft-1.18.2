package reascer.wom.world.capabilities.item;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

public class EFWeaponCapability extends WeaponCapability{
	protected final Map<Style, List<StaticAnimation>> heavyAutoAttackMotions;
	
	public EFWeaponCapability(WeaponCapability.Builder builder, EFWeaponCapability.Builder efbuilder) {
		super(builder);
		this.heavyAutoAttackMotions = efbuilder.heavyAutoAttackMotionMap;
	}

	public static EFWeaponCapability.Builder EFbuilder() {
		return new EFWeaponCapability.Builder();
	}
	
	public static class Builder {
		Map<Style, List<StaticAnimation>> heavyAutoAttackMotionMap;
		
		public Builder() {
			this.heavyAutoAttackMotionMap = Maps.<Style, List<StaticAnimation>>newHashMap();
		}
		
		public Builder newHeavyStyleCombo(Style style, StaticAnimation... animation) {
			this.heavyAutoAttackMotionMap.put(style, Lists.newArrayList(animation));
			return this;
		}
	}

}
