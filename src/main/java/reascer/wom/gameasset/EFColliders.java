package reascer.wom.gameasset;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class EFColliders {
	public static final Collider STAFF = new MultiOBBCollider(3, 0.2D, 0.2D, 1.9D, 0D, 0D, 0D);
	
	public static final Collider AGONY_PLUNGE = new OBBCollider(5.0F, 5.0F, 5.0F, 0F, 0F, 0F);
	
	public static final Collider PLUNDER_PERDITION = new OBBCollider(10.0F, 5.0F, 10.0F, 0F, 0F, 0F);
	public static final Collider RUINE = new MultiOBBCollider(4, 0.4D, 0.4D, 1.45D, 0D, 0D, -1.05D);
	
	public static final Collider TORMENT = new MultiOBBCollider(3, 0.4F, 0.6F, 0.8F, 0F, -0.2F, -1.0F);
	
	public static final Collider KATANA_SHEATHED_AUTO = new OBBCollider(2.00F, 1.0F, 2.0F, 0F, 1.0F, -1F);
	public static final Collider KATANA_SHEATHED_DASH = new OBBCollider(2.00F, 1.0F, 3.5F, 0F, 1.0F, 2.50F);
	public static final Collider KATANA = new MultiOBBCollider(3, 0.4D, 0.4D, 1.0D, 0D, 0D, -1.0D);
	public static final Collider FATAL_DRAW = new OBBCollider(1.75D, 0.7D, 1.35D, 0D, 1.0D, -1.0D);
	public static final Collider FATAL_DRAW_DASH = new OBBCollider(0.7D, 0.7D, 1.75D, 0D, 1.0D, -0.8D);
	
	public static final Collider ENDER_BLASTER = new MultiOBBCollider(4, 0.4D, 0.6D, 0.4D, 0D, 0D, 0D);
	public static final Collider ENDER_BLASTER_CROSS = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0D, 0.6D);
	public static final Collider KICK = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0.6D, 0D);
	public static final Collider KICK_HUGE = new MultiOBBCollider(4, 0.8D, 0.8D, 0.8D, 0D, 0.9D, 0D);
	public static final Collider KNEE = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0.4D, 0D);
	public static final Collider ENDER_BULLET = new MultiOBBCollider(6, 0.2D, 10.0D, 0.2D, 0.0D, -10.3D, 0.0D);
}
