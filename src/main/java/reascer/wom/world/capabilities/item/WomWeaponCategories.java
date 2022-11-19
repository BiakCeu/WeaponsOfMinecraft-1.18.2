package reascer.wom.world.capabilities.item;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum WomWeaponCategories implements WeaponCategory {
	AGONY,TORMENT,RUINE,ENDERBLASTER,STAFF,GREATAXE,ANTITHEUS;
	
	final int id;
	
	WomWeaponCategories() {
		this.id = WeaponCategory.ENUM_MANAGER.assign(this);
	}
	
	@Override
	public int universalOrdinal() {
		return this.id;
	}
}
