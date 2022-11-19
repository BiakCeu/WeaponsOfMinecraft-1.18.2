package reascer.wom.gameasset;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class EFColliders {
	public static final Collider STAFF = new MultiOBBCollider(4, 0.25D, 0.25D, 1.9D, 0D, 0D, 0D);
	public static final Collider STAFF_CHARYBDIS = new MultiOBBCollider(4, 0.6D, 0.6D, 2.3D, 0D, 0D, 0D);
	
	public static final Collider GREATSWORD = new MultiOBBCollider(3, 0.2D, 0.8D, 1.0D, 0D, 0D, -1.2D);
	
	public static final Collider AGONY = new MultiOBBCollider(5, 0.45D, 0.45D, 1.4D, 0D, 0D, -1.2D);
	public static final Collider AGONY_AIRSLASH = new MultiOBBCollider(4, 0.55D, 0.55D, 1.4D, 0D, 0D, -1.2D);
	public static final Collider AGONY_PLUNGE = new OBBCollider(5.0F, 2.0F, 5.0F, 0F, 0F, 0F);
	
	public static final Collider PLUNDER_PERDITION = new OBBCollider(8.0F, 4.0F, 8.0F, 0F, 0F, 0F);
	public static final Collider RUINE = new MultiOBBCollider(5, 0.4D, 0.4D, 1.45D, 0D, 0D, -1.05D);
	public static final Collider RUINE_COMET = new MultiOBBCollider(4, 0.6D, 0.6D, 1.35D, 0D, 0D, -0.95D);
	public static final Collider SHOULDER_BUMP = new OBBCollider( 0.8D, 0.8D, 0.8D, 0D, 1.2D, -1.2D);
	
	public static final Collider TORMENT = new MultiOBBCollider(3, 0.4F, 0.6F, 1.0F, 0F, -0.2F, -0.8F);
	public static final Collider TORMENT_AIRSLAM = new OBBCollider( 1.4F, 0.8F, 1.4F, 0F, 0.8F, -1.6F);
	public static final Collider TORMENT_BERSERK_AIRSLAM = new OBBCollider( 3.2F, 3F, 3.2F, 0F, 3F, -3.6F);
	public static final Collider TORMENT_BERSERK_DASHSLAM = new OBBCollider( 1.8F, 1.6F, 1.8F, 0F, 1.6F, -1.8F);
	
	public static final Collider KATANA_SHEATHED_AUTO = new OBBCollider(2.00F, 1.0F, 2.0F, 0F, 1.0F, -1F);
	public static final Collider KATANA_SHEATHED_DASH = new OBBCollider(2.00F, 1.0F, 3.5F, 0F, 1.0F, 2.50F);
	public static final Collider KATANA = new MultiOBBCollider(5, 0.2D, 0.3D, 1.0D, 0D, 0D, -1.0D);
	public static final Collider FATAL_DRAW = new OBBCollider(1.75D, 0.7D, 1.35D, 0D, 1.0D, -1.0D);
	public static final Collider FATAL_DRAW_DASH = new OBBCollider(1.0D, 1.0D, 2.75D, 0D, 1.2D, -0.2D);
	
	public static final Collider ENDER_BLASTER = new MultiOBBCollider(4, 0.4D, 0.6D, 0.4D, 0D, 0D, 0D);
	public static final Collider ENDER_BLASTER_CROSS = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0D, 0.6D);
	public static final Collider KICK = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0.6D, 0D);
	public static final Collider KICK_HUGE = new MultiOBBCollider(4, 0.8D, 0.8D, 0.8D, 0D, 0.9D, 0D);
	public static final Collider ENDER_DASH = new MultiOBBCollider(4, 1.0D, 1.0D, 1.0D, 0D, 1.0D, 1.0D);
	public static final Collider ENDER_TISHNAW = new MultiOBBCollider(4, 1.5D, 1.5D, 1.5D, 0D, 1.0D, 1.0D);
	public static final Collider KNEE = new MultiOBBCollider(4, 0.4D, 0.4D, 0.4D, 0D, 0.4D, 0D);
	public static final Collider ENDER_BULLET = new OBBCollider( 0.2D, 10.0D, 0.2D, 0.0D, -10.2D, 0.0D);
	public static final Collider ENDER_BULLET_DASH = new OBBCollider( 1.2D, 5.0D, 1.2D, 0.0D, -5.1D, 0.0D);
	public static final Collider ENDER_BULLET_WIDE = new OBBCollider( 4.6D, 1.5D, 4.6D, 0.0D, 0.0D, -4.6D);
	public static final Collider ENDER_PISTOLERO = new OBBCollider( 2.5D, 2D, 2.5D, 0.0D, -1.5D, 0.0D);
	
	public static final Collider ANTITHEUS = new MultiOBBCollider(3, 0.4F, 1.0F, 1.6F, 0F, -0.4F, -0.9F);
	public static final Collider ANTITHEUS_GUILLOTINE = new OBBCollider(3.5F, 1.5F, 3.5F, 0F, 0F, 0F);
	public static final Collider ANTITHEUS_ASCENDED_PUNCHES = new OBBCollider(1.0F, 1.0F, 1.0F, 0F, 1.0F, -1.0F);
	public static final Collider ANTITHEUS_ASCENDED_BLINK = new OBBCollider(2.5F, 1.5F, 2.5F, 0F, 0.75F, 0.0F);
	
}
