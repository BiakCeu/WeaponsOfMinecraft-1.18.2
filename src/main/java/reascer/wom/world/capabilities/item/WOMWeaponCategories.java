package reascer.wom.world.capabilities.item;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum WOMWeaponCategories implements WeaponCategory {
	AGONY,TORMENT,RUINE,ENDERBLASTER,STAFF,GREATAXE,ANTITHEUS;
	
	final int id;
	
	WOMWeaponCategories() {
		this.id = WeaponCategory.ENUM_MANAGER.assign(this);
	}
	
	@Override
	public int universalOrdinal() {
		return this.id;
	}
}
