package reascer.wom.animation;

import yesman.epicfight.api.animation.LivingMotion;

public enum EFLivingMotions implements LivingMotion{
	EATING;
	
	final int id;
	
	EFLivingMotions() {
		this.id = LivingMotion.ENUM_MANAGER.assign(this);
		
	}
	
	public int universalOrdinal() {
		return id;
	}
}