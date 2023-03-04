package reascer.wom.gameasset;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.stringtemplate.v4.compiler.STParser.ifstat_return;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderTooltipEvent.Color;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import reascer.wom.animation.BasicMultipleAttackAnimation;
import reascer.wom.animation.EnderblasterShootAttackAnimation;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.particle.WOMParticles;
import reascer.wom.particle.WOMParticles;
import reascer.wom.skill.DemonMarkPassiveSkill;
import reascer.wom.skill.DemonicAscensionSkill;
import reascer.wom.skill.EFKatanaPassive;
import yesman.epicfight.api.animation.property.AnimationEvent.Side;
import yesman.epicfight.api.animation.property.AnimationEvent.TimeStampedEvent;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.animation.types.GuardAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.CreeperArmature;
import yesman.epicfight.model.armature.DragonArmature;
import yesman.epicfight.model.armature.EndermanArmature;
import yesman.epicfight.model.armature.HoglinArmature;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.model.armature.IronGolemArmature;
import yesman.epicfight.model.armature.PiglinArmature;
import yesman.epicfight.model.armature.RavagerArmature;
import yesman.epicfight.model.armature.SpiderArmature;
import yesman.epicfight.model.armature.VexArmature;
import yesman.epicfight.model.armature.WitherArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class WOMAnimations {
	public static StaticAnimation EATING;
	
	public static StaticAnimation ENDERSTEP_FORWARD;
	public static StaticAnimation ENDERSTEP_BACKWARD;
	public static StaticAnimation ENDERSTEP_LEFT;
	public static StaticAnimation ENDERSTEP_RIGHT;
	
	public static StaticAnimation DODGEMASTER_BACKWARD;
	public static StaticAnimation DODGEMASTER_LEFT;
	public static StaticAnimation DODGEMASTER_RIGHT;
	
	public static StaticAnimation KNIGHT_ROLL_FORWARD;
	public static StaticAnimation KNIGHT_ROLL_BACKWARD;
	public static StaticAnimation KNIGHT_ROLL_LEFT;
	public static StaticAnimation KNIGHT_ROLL_RIGHT;
	
	public static StaticAnimation KICK;
	
	public static StaticAnimation SWORD_ONEHAND_AUTO_1;
	public static StaticAnimation SWORD_ONEHAND_AUTO_2;
	public static StaticAnimation SWORD_ONEHAND_AUTO_3;
	public static StaticAnimation SWORD_ONEHAND_AUTO_4;
	
	public static StaticAnimation TACHI_TWOHAND_AUTO_1;
	public static StaticAnimation TACHI_TWOHAND_AUTO_2;
	public static StaticAnimation TACHI_TWOHAND_AUTO_3;
	public static StaticAnimation TACHI_TWOHAND_AUTO_4;
	public static StaticAnimation TACHI_TWOHAND_BLOSSOM_CHARGE;
	public static StaticAnimation TACHI_TWOHAND_BLOSSOM_SLASHES;
	public static StaticAnimation TACHI_TWOHAND_BLOSSOM_FINAL;
	
	public static StaticAnimation LONGSWORD_TWOHAND_AUTO_1;
	public static StaticAnimation LONGSWORD_TWOHAND_AUTO_2;
	public static StaticAnimation LONGSWORD_TWOHAND_AUTO_3;
	public static StaticAnimation LONGSWORD_TWOHAND_AUTO_4;
	
	public static StaticAnimation GREATSWORD_TWOHAND_AUTO_1;
	public static StaticAnimation GREATSWORD_TWOHAND_AUTO_2;
	public static StaticAnimation GREATSWORD_TWOHAND_AUTO_3;
	
	public static StaticAnimation STAFF_AUTO_1;
	public static StaticAnimation STAFF_AUTO_2;
	public static StaticAnimation STAFF_AUTO_3;
	public static StaticAnimation STAFF_DASH;
	public static StaticAnimation STAFF_CHARYBDIS;
	public static StaticAnimation STAFF_IDLE;
	public static StaticAnimation STAFF_RUN;
	
	public static StaticAnimation AGONY_AUTO_1;
	public static StaticAnimation AGONY_AUTO_2;
	public static StaticAnimation AGONY_AUTO_3;
	public static StaticAnimation AGONY_AUTO_4;
	public static StaticAnimation AGONY_HEAVY_1;
	public static StaticAnimation AGONY_HEAVY_2;
	public static StaticAnimation AGONY_HEAVY_3;
	public static StaticAnimation AGONY_HEAVY_CHARGE_1;
	public static StaticAnimation AGONY_HEAVY_CHARGE_2;
	public static StaticAnimation AGONY_HEAVY_CHARGE_3;
	public static StaticAnimation AGONY_AIR_SLASH;
	public static StaticAnimation AGONY_CLAWSTRIKE;
	public static StaticAnimation AGONY_COUNTER;
	public static StaticAnimation AGONY_IDLE;
	public static StaticAnimation AGONY_RUN;
	public static StaticAnimation AGONY_WALK;
	public static StaticAnimation AGONY_GUARD;
	public static StaticAnimation AGONY_GUARD_HIT_1;
	public static StaticAnimation AGONY_GUARD_HIT_2;
	public static StaticAnimation AGONY_PLUNGE_MIDDLE;
	public static StaticAnimation AGONY_PLUNGE_FORWARD;
	public static StaticAnimation AGONY_PLUNGE_BACKWARD;
	public static StaticAnimation AGONY_PLUNGE_LEFT;
	public static StaticAnimation AGONY_PLUNGE_RIGHT;
	
	public static StaticAnimation RUINE_AUTO_1;
	public static StaticAnimation RUINE_AUTO_2;
	public static StaticAnimation RUINE_AUTO_3;
	public static StaticAnimation RUINE_AUTO_4;
	public static StaticAnimation RUINE_COMET;
	public static StaticAnimation RUINE_DASH;
	public static StaticAnimation RUINE_COUNTER;
	public static StaticAnimation RUINE_BLOCK;
	public static StaticAnimation RUINE_IDLE;
	public static StaticAnimation RUINE_RUN;
	public static StaticAnimation RUINE_WALK;
	
	public static StaticAnimation RUINE_PLUNDER;
	
	public static StaticAnimation TORMENT_AUTO_1;
	public static StaticAnimation TORMENT_AUTO_2;
	public static StaticAnimation TORMENT_AUTO_3;
	public static StaticAnimation TORMENT_AUTO_4;
	public static StaticAnimation TORMENT_AIR_SLASH;
	public static StaticAnimation TORMENT_DASH;
	public static StaticAnimation TORMENT_AIRSLAM;
	public static StaticAnimation TORMENT_IDLE;
	public static StaticAnimation TORMENT_RUN;
	public static StaticAnimation TORMENT_WALK;
	public static StaticAnimation TORMENT_BERSERK_AUTO_1;
	public static StaticAnimation TORMENT_BERSERK_AUTO_2;
	public static StaticAnimation TORMENT_BERSERK_DASH;
	public static StaticAnimation TORMENT_BERSERK_AIRSLAM;
	public static StaticAnimation TORMENT_BERSERK_CONVERT;
	public static StaticAnimation TORMENT_BERSERK_IDLE;
	public static StaticAnimation TORMENT_BERSERK_RUN;
	public static StaticAnimation TORMENT_BERSERK_WALK;
	
	public static StaticAnimation KATANA_AUTO_1;
	public static StaticAnimation KATANA_AUTO_2;
	public static StaticAnimation KATANA_AUTO_3;
	public static StaticAnimation KATANA_DASH;
	public static StaticAnimation KATANA_IDLE;
	public static StaticAnimation KATANA_SHEATHED_AUTO_1;
	public static StaticAnimation KATANA_SHEATHED_AUTO_2;
	public static StaticAnimation KATANA_SHEATHED_AUTO_3;
	public static StaticAnimation KATANA_SHEATHED_DASH;
	public static StaticAnimation KATANA_SHEATHED_COUNTER;
	public static StaticAnimation KATANA_SHEATHE;
	public static StaticAnimation KATANA_SHEATHED_IDLE;
	public static StaticAnimation KATANA_SHEATHED_RUN;
	public static StaticAnimation KATANA_FATAL_DRAW;
	public static StaticAnimation KATANA_FATAL_DRAW_SECOND;
	public static StaticAnimation KATANA_FATAL_DRAW_DASH;
	
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_1;//ONEHAND
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_2;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_3;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_4;
	public static StaticAnimation ENDERBLASTER_ONEHAND_DASH;
	public static StaticAnimation ENDERBLASTER_ONEHAND_JUMPKICK;
	public static StaticAnimation ENDERBLASTER_ONEHAND_IDLE;
	public static StaticAnimation ENDERBLASTER_ONEHAND_WALK;
	public static StaticAnimation ENDERBLASTER_ONEHAND_RUN;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_1;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_2;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_2_FORWARD;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_2_LEFT;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_2_RIGHT;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_3;
	public static StaticAnimation ENDERBLASTER_ONEHAND_SHOOT_DASH;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AIRSHOOT;
	public static StaticAnimation ENDERBLASTER_TWOHAND_TISHNAW;
	public static StaticAnimation ENDERBLASTER_TWOHAND_TOMAHAWK;
	public static StaticAnimation ENDERBLASTER_TWOHAND_AUTO_1;//ONEHAND
	public static StaticAnimation ENDERBLASTER_TWOHAND_AUTO_2;
	public static StaticAnimation ENDERBLASTER_TWOHAND_AUTO_3;
	public static StaticAnimation ENDERBLASTER_TWOHAND_AUTO_4;
	public static StaticAnimation ENDERBLASTER_TWOHAND_SHOOT_1;// TWOHAND
	public static StaticAnimation ENDERBLASTER_TWOHAND_SHOOT_2;
	public static StaticAnimation ENDERBLASTER_TWOHAND_SHOOT_3;
	public static StaticAnimation ENDERBLASTER_TWOHAND_SHOOT_4;
	public static StaticAnimation ENDERBLASTER_TWOHAND_AIRSHOOT;
	public static StaticAnimation ENDERBLASTER_TWOHAND_PISTOLERO;
	public static StaticAnimation ENDERBLASTER_TWOHAND_IDLE;
	
	public static StaticAnimation ANTITHEUS_AGRESSION;
	public static StaticAnimation ANTITHEUS_GUILLOTINE;
	public static StaticAnimation ANTITHEUS_AUTO_1;
	public static StaticAnimation ANTITHEUS_AUTO_2;
	public static StaticAnimation ANTITHEUS_AUTO_3;
	public static StaticAnimation ANTITHEUS_AUTO_4;
	public static StaticAnimation ANTITHEUS_IDLE;
	public static StaticAnimation ANTITHEUS_IDLE_TWIST;
	public static StaticAnimation ANTITHEUS_RUN;
	public static StaticAnimation ANTITHEUS_WALK;
	public static StaticAnimation ANTITHEUS_GUARD;
	public static StaticAnimation ANTITHEUS_GUARD_HIT_1;
	public static StaticAnimation ANTITHEUS_GUARD_HIT_2;
	public static StaticAnimation ANTITHEUS_GUARD_HIT_3;
	public static StaticAnimation ANTITHEUS_ASCENSION;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_1;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_2;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_3;
	public static StaticAnimation ANTITHEUS_ASCENDED_DEATHFALL;
	public static StaticAnimation ANTITHEUS_ASCENDED_BLINK;
	public static StaticAnimation ANTITHEUS_ASCENDED_IDLE;
	public static StaticAnimation ANTITHEUS_ASCENDED_RUN;
	public static StaticAnimation ANTITHEUS_ASCENDED_WALK;
	
	public static StaticAnimation HERRSCHER_IDLE;
	public static StaticAnimation HERRSCHER_RUN;
	public static StaticAnimation HERRSCHER_WALK;
	
	@SubscribeEvent
	public static void registerAnimations(AnimationRegistryEvent event) {
		event.getRegistryMap().put(WeaponOfMinecraft.MODID, WOMAnimations::build);
	}
	
	private static void build() {
		HumanoidArmature biped = Armatures.BIPED;
		
		EATING = new StaticAnimation(0.11F, true, "biped/living/eating", biped);
		
		KICK = new BasicAttackAnimation(0.1F, 0.05F, 0.15F, 0.2F, null, biped.toolR, "biped/skill/kick", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(5))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		DODGEMASTER_BACKWARD = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_back", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.00F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT));
		DODGEMASTER_LEFT = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_left", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.00F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT));
		DODGEMASTER_RIGHT = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_right", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.00F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT));
		
		
		ENDERSTEP_FORWARD = new DodgeAnimation(0.05F, "biped/skill/enderstep_forward", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					for (int i = 0; i < 60; i++) {
						entity.level.addParticle( ParticleTypes.REVERSE_PORTAL, 
								entity.getX() + (new Random().nextDouble()*0.5D)-0.25D, 
								entity.getY() + new Random().nextDouble() * 1.65, 
								entity.getZ( )+ (new Random().nextDouble()*0.5D)-0.25D, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2);
					}
					entity.level.playSound(null, 
							entity.xo, 
							entity.yo + 1, 
							entity.zo,
			    			SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
					entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT));
		ENDERSTEP_BACKWARD = new DodgeAnimation(0.05F, "biped/skill/enderstep_backward", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					for (int i = 0; i < 60; i++) {
						entity.level.addParticle( ParticleTypes.REVERSE_PORTAL, 
								entity.getX() + (new Random().nextDouble()*0.5D)-0.25D, 
								entity.getY() + new Random().nextDouble() * 1.65, 
								entity.getZ( )+ (new Random().nextDouble()*0.5D)-0.25D, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2);
					}
						entity.level.playSound(null, 
								entity.xo, 
								entity.yo + 1, 
								entity.zo,
				    			SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
						entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT));
		ENDERSTEP_LEFT = new DodgeAnimation(0.05F, "biped/skill/enderstep_left", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					for (int i = 0; i < 60; i++) {
						entity.level.addParticle( ParticleTypes.REVERSE_PORTAL, 
								entity.getX() + (new Random().nextDouble()*0.5D)-0.25D, 
								entity.getY() + new Random().nextDouble() * 1.65, 
								entity.getZ( )+ (new Random().nextDouble()*0.5D)-0.25D, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2);
					}
					entity.level.playSound(null, 
							entity.xo, 
							entity.yo + 1, 
							entity.zo,
				    		SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
					entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT));
		ENDERSTEP_RIGHT = new DodgeAnimation(0.05F, "biped/skill/enderstep_right", 0.6F, 1.65F, biped)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					for (int i = 0; i < 60; i++) {
						entity.level.addParticle( ParticleTypes.REVERSE_PORTAL, 
								entity.getX() + (new Random().nextDouble()*0.5D)-0.25D, 
								entity.getY() + new Random().nextDouble() * 1.65, 
								entity.getZ( )+ (new Random().nextDouble()*0.5D)-0.25D, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2, 
								(new Random().nextDouble()-0.5D)*1.2);
					}
					entity.level.playSound(null, 
							entity.xo, 
							entity.yo + 1, 
							entity.zo,
			    			SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
					entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT));
		
		KNIGHT_ROLL_FORWARD = new DodgeAnimation(0.1F, "biped/skill/roll_forward", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_BACKWARD = new DodgeAnimation(0.1F, "biped/skill/roll_backward", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_LEFT = new DodgeAnimation(0.1F, "biped/skill/roll_left", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_RIGHT = new DodgeAnimation(0.1F, "biped/skill/roll_right", 0.6F, 0.8F, biped);
		
		SWORD_ONEHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/sword_onehand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/sword_onehand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/sword_onehand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, biped.toolR, "biped/combat/sword_onehand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		
		TACHI_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/tachi_twohand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/tachi_twohand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, biped.toolR, "biped/combat/tachi_twohand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, biped.toolR, "biped/combat/tachi_twohand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		
		TACHI_TWOHAND_BLOSSOM_CHARGE = new ActionAnimation(0.05F, "biped/skill/tachi_twohand_blossom_charge", biped);
		TACHI_TWOHAND_BLOSSOM_SLASHES = new BasicMultipleAttackAnimation(0.05F, "biped/skill/tachi_twohand_blossom_slashes", biped,
				new Phase(0.0F, 0.1F, 0.2F, 0.3F, 0.3F, biped.toolR, null),
				new Phase(0.3F, 0.3F, 0.4F, 0.5F, 0.5F, biped.toolR, null),
				new Phase(0.5F, 0.5F, 0.6F, 0.8F, 0.8F, biped.toolR, null),
				new Phase(0.8F, 0.8F, 0.9F, 1.7F ,Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F),2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F),3)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		TACHI_TWOHAND_BLOSSOM_FINAL = new BasicAttackAnimation(0.1F, 0.05F, 0.2F, 0.3F, null, biped.toolR, "biped/skill/tachi_twohand_blossom_final", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.25F))
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		LONGSWORD_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, biped.toolR, "biped/combat/longsword_twohand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, biped.toolR, "biped/combat/longsword_twohand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, biped.toolR, "biped/combat/longsword_twohand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, biped.toolR, "biped/combat/longsword_twohand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		
		GREATSWORD_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, biped.toolR, "biped/combat/greatsword_twohand_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		GREATSWORD_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, biped.toolR, "biped/combat/greatsword_twohand_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		GREATSWORD_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.4F, 0.5F, 0.6F, null, biped.toolR, "biped/combat/greatsword_twohand_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		
		AGONY_AUTO_1 = new BasicAttackAnimation(0.05F, 0.05F, 0.15F, 0.2F, null, biped.toolR, "biped/combat/agony_auto_1", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F);
		AGONY_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_2", biped,
				new Phase(0.00F, 0.3F, 0.45F, 0.5F, 0.5F, biped.toolR, null),
				new Phase(0.5F, 0.5F, 0.65F, 0.8F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		AGONY_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_3", biped,
				new Phase(0.0F, 0.25F, 0.5F, 0.55F, 0.55F, biped.toolR, null),
				new Phase(0.55F, 0.75F, 1.0F, 1.20F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F),1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		AGONY_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_4", biped,
				new Phase(0.0F, 0.10F, 0.2F, 0.25F, 0.25F, biped.toolR, null),
				new Phase(0.25F, 0.25F, 0.35F, 0.55F, 0.55F, biped.toolR, null),
				new Phase(0.55F, 0.55F, 0.7F, 0.95F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.0F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F);
		
		AGONY_CLAWSTRIKE = new BasicMultipleAttackAnimation(0.0F, "biped/combat/agony_clawstrike", biped,
				new Phase(0.0F, 0.35F, 0.6F, 0.95F, Float.MAX_VALUE, biped.toolR, WOMColliders.AGONY_AIRSLASH))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.TARGET_LOST_HEALTH.create(0.1f)))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		AGONY_COUNTER = new DashAttackAnimation(0.05F, 0.20F, 0.20F, 0.5F,0.55f, WOMColliders.KATANA_SHEATHED_DASH, biped.rootJoint, "biped/combat/agony_counter", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F);
		
		AGONY_AIR_SLASH = new BasicAttackAnimation(0.0F, 0.45F, 0.6F, 0.85F, WOMColliders.AGONY_AIRSLASH, biped.toolR, "biped/combat/agony_airslash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.7F, ReuseableEvents.AGONY_GROUNDSLAM, Side.CLIENT));
		
		AGONY_IDLE = new StaticAnimation(0.1f,true, "biped/living/agony_idle", biped);
		AGONY_RUN = new MovementAnimation(0.1f,true, "biped/living/agony_run", biped);
		AGONY_WALK = new MovementAnimation(0.1f,true, "biped/living/agony_walk", biped);
		AGONY_GUARD = new StaticAnimation(0.05F, true, "biped/skill/agony_guard", biped);
		AGONY_GUARD_HIT_1 = new GuardAnimation(0.05F, "biped/skill/agony_guard_hit1", biped);
		AGONY_GUARD_HIT_2 = new GuardAnimation(0.05F, "biped/skill/agony_guard_hit2", biped);
		
		AGONY_PLUNGE_FORWARD = new AttackAnimation(0.05F, "biped/skill/agony_plunge_forward", biped,
				new Phase(0.0F, 0.166F, 0.20F, 0.2F, 0.2F, biped.rootJoint, WOMColliders.AGONY_PLUNGE), 
				new Phase(0.2F, 1.1F, 1.45F, 1.65F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.AGONY_PLUNGE))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.WHOOSH_BIG,0)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10),0)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(9.0F),0)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.5F),0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,0)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2f),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.15F, ReuseableEvents.AGONY_AIRBURST_JUMP, Side.CLIENT),
						TimeStampedEvent.create(0.2F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						TimeStampedEvent.create(0.25F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						TimeStampedEvent.create(0.3F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						TimeStampedEvent.create(0.35F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						TimeStampedEvent.create(1.3F, ReuseableEvents.AGONY_PLUNGE_GROUNDTHRUST, Side.CLIENT),
						TimeStampedEvent.create(1.55F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT));
		
		RUINE_AUTO_1 = new BasicAttackAnimation(0.05F, 0.25F, 0.45F, 0.55F, null, biped.toolR, "biped/combat/ruine_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.0F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		RUINE_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/ruine_auto_2", biped,
				new Phase(0.0F, 0.05F, 0.3F, 0.65F, 0.65F, biped.toolR, null),
				new Phase(0.65F, 0.65F, 0.75F, 0.80F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.65F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )				
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/ruine_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.35F, 0.4F, 0.4F, biped.toolR, null),
				new Phase(0.4F, 0.5F, 0.7F, 0.75F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.50F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.80F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD )
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F);
		
		RUINE_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/ruine_auto_4", biped,
				new Phase(0.0F, 0.4F, 0.5F, 0.55F, 0.55F, biped.toolR, WOMColliders.RUINE_COMET),
				new Phase(0.55F, 0.8F, 1.1F, 1.55F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.80F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.70F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.8F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD )
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F)
				.addEvents(TimeStampedEvent.create(0.4F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT));
				
		
		RUINE_COUNTER = new BasicMultipleAttackAnimation(0.05F, "biped/skill/ruine_counterattack", biped,
				new Phase(0.0F, 0.2F, 0.25F, 0.3F, 0.3F, biped.rootJoint, WOMColliders.SHOULDER_BUMP),
				new Phase(0.3F, 0.5F, 0.7F, 1.0F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(2.0F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.20F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.3F),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)			
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)			
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)			
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_DASH = new BasicAttackAnimation(0.1F, 0.15F, 0.65F, 0.75F, null, biped.toolR, "biped/combat/ruine_dash", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.0F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		RUINE_COMET = new BasicAttackAnimation(0.15F, 0.25F, 0.55F, 0.75F, WOMColliders.RUINE_COMET, biped.toolR, "biped/combat/ruine_comet", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
						TimeStampedEvent.create(0.55F, ReuseableEvents.RUINE_COMET_GROUNDTHRUST, Side.CLIENT));
		
		RUINE_BLOCK = new StaticAnimation(0.05F, true, "biped/skill/ruine_block", biped);
		RUINE_IDLE = new StaticAnimation(0.1f,true, "biped/living/ruine_idle", biped);
		RUINE_RUN = new MovementAnimation(0.1f,true, "biped/living/ruine_run", biped);
		RUINE_WALK = new MovementAnimation(0.1f,true, "biped/living/ruine_walk", biped);
		
		RUINE_PLUNDER = new AttackAnimation(0.05F, "biped/skill/ruine_plunder", biped,
				new Phase(0.0F, 1.1F, 1.25F, 1.85F, 1.85F, biped.rootJoint, WOMColliders.PLUNDER_PERDITION), 
				new Phase(1.85F, 1.85F, 2.2F, 3.35F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.PLUNDER_PERDITION))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,0)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.RUINE_PLUNDER_SWORD,0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20),0)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),0)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F),0)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		
		TORMENT_AUTO_1 = new BasicAttackAnimation(0.20F, 0.2F, 0.5F, 0.6F, null, biped.toolR, "biped/combat/torment_auto_1", biped)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F);
		
		TORMENT_AUTO_2 = new BasicAttackAnimation(0.25F, 0.05F, 0.3F, 0.55F, null, biped.toolR, "biped/combat/torment_auto_2", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.9F))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.20F);
		
		TORMENT_AUTO_3 = new BasicMultipleAttackAnimation(0.25F, "biped/combat/torment_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, biped.toolR, null),
				new Phase(0.4F, 0.4F, 0.55F, 0.9F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.20F)
				.addEvents(TimeStampedEvent.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT));
	
		TORMENT_AUTO_4 = new BasicAttackAnimation(0.20F, 0.2F, 0.65F, 0.70F, null, biped.toolR, "biped/combat/torment_auto_4", biped)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F);
		
		TORMENT_DASH = new DashAttackAnimation(0.05F, 0.25F, 0.25F, 0.5F, 0.75F, null, biped.toolR, "biped/combat/torment_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.55F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT));
		
		TORMENT_AIRSLAM = new BasicMultipleAttackAnimation(0.15F, "biped/combat/torment_airslam", biped,
				new Phase(0.0F, 0.45F, 0.55F, 0.6F, 0.6F, biped.toolR, null),
				new Phase(0.6F, 0.6F, 0.7F, 0.8F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.TORMENT_AIRSLAM))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.5F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addEvents(TimeStampedEvent.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT));
		
		TORMENT_IDLE = new StaticAnimation(0.1f,true, "biped/living/torment_idle", biped);
		TORMENT_RUN = new MovementAnimation(0.1f,true, "biped/living/torment_run", biped);
		TORMENT_WALK = new MovementAnimation(0.1f,true, "biped/living/torment_walk", biped);
		
		TORMENT_BERSERK_IDLE = new StaticAnimation(true, "biped/living/torment_berserk_idle", biped);
		
		TORMENT_BERSERK_AUTO_1 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.35F, 0.65F, null, biped.toolR, "biped/skill/torment_berserk_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8F))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.OVERBLOOD_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_AUTO_2 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.55F, 0.70F, null, biped.toolR, "biped/skill/torment_berserk_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8F))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.OVERBLOOD_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_DASH = new BasicMultipleAttackAnimation(0.15F, "biped/skill/torment_berserk_dash", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, biped.rootJoint, WOMColliders.TORMENT_BERSERK_DASHSLAM),
				new Phase(0.35F, 0.5F, 0.6F, 0.65F, 0.65F, biped.rootJoint, WOMColliders.TORMENT_BERSERK_DASHSLAM),
				new Phase(0.65F, 0.9F, 1.0F, 1.2F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.TORMENT_BERSERK_DASHSLAM))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(8F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, false)
				.addEvents(TimeStampedEvent.create(0.4F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT),
					TimeStampedEvent.create(0.7F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT),
					TimeStampedEvent.create(1.15F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT));
		
		TORMENT_BERSERK_AIRSLAM = new BasicMultipleAttackAnimation(0.15F, 0.4F, 0.7F, 1.2F, WOMColliders.TORMENT_BERSERK_AIRSLAM, biped.rootJoint, "biped/skill/torment_berserk_airslam", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.GROUND_SLAM)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.0F, (entitypatch, params) -> {
							if (entitypatch instanceof PlayerPatch) {
								((PlayerPatch<?>)entitypatch).setStamina(((PlayerPatch<?>)entitypatch).getStamina() - 2.0f);
							}
						}, Side.CLIENT),
				TimeStampedEvent.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM, Side.CLIENT));
		
		TORMENT_BERSERK_CONVERT = new BasicAttackAnimation(0.05F, 0.6F, 1.35F, 1.7F, WOMColliders.PLUNDER_PERDITION, biped.rootJoint, "biped/skill/torment_berserk_convert", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.ENDERMAN_SCREAM)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.CHAIN_BREAK)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(8.0F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_RUN = new MovementAnimation(0.1f,true, "biped/living/torment_berserk_run", biped);
		TORMENT_BERSERK_WALK = new MovementAnimation(0.1f,true, "biped/living/torment_berserk_walk", biped);
		
		KATANA_SHEATHED_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_sheathed_auto_1", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.25F, 0.3F, 0.40F, 0.55F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addEvents(TimeStampedEvent.create(0.25F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.35F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.85F, (entitypatch, params) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Side.SERVER));
		
		KATANA_SHEATHED_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_sheathed_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.25F, 0.3F, 0.39F, 0.40F, 0.40F, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.40F, 0.5F, 0.59F, 0.7F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addEvents(TimeStampedEvent.create(0.25F, (entitypatch, params) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT),TimeStampedEvent.create(0.35F, (entitypatch, params) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT),TimeStampedEvent.create(0.50F, (entitypatch, params) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT),TimeStampedEvent.create(1.05F, (entitypatch, params) -> {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
							entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
						}
						entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
					}, Side.SERVER));
				
		
		KATANA_SHEATHED_AUTO_3 = new BasicAttackAnimation(0.15F, 0.05F, 0.25F, 0.50F, WOMColliders.KATANA_SHEATHED_AUTO, biped.rootJoint, "biped/combat/katana_sheathed_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.1F))
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(3F))
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addEvents(TimeStampedEvent.create(0.25F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.80F, (entitypatch, params) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Side.SERVER));
		
		KATANA_SHEATHED_DASH = new BasicAttackAnimation(0.10F, 0.15F, 0.4F, 1.0F,WOMColliders.KATANA_SHEATHED_AUTO, biped.rootJoint, "biped/combat/katana_sheathed_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10F))
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.0F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						ServerPlayerPatch serverPlayerPatch = ((ServerPlayerPatch)entitypatch); 
						if(serverPlayerPatch.getStamina() > 4) {
							serverPlayerPatch.setStamina(serverPlayerPatch.getStamina() - 4.0f);
						} else {
							serverPlayerPatch.setStamina(0f);
						}
					}
				}, Side.SERVER),TimeStampedEvent.create(0.15F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.8F, (entitypatch, params) -> {
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Side.SERVER));
		
		KATANA_SHEATHED_COUNTER = new BasicMultipleAttackAnimation(0.1F, "biped/combat/katana_sheathed_dash", biped,
				new Phase(0.0F, 0.15F, 0.4F, 0.7F, 0.7F, biped.rootJoint, WOMColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.7F, 0.7F, 0.80F, 0.80F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.KATANA_SHEATHED_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(8.0F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.15F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.8F, (entitypatch, params) -> {
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Side.SERVER));
		
		KATANA_SHEATHE = new StaticAnimation(0.1f,false, "biped/living/katana_sheathe", biped)
				.addEvents(TimeStampedEvent.create(0.2F, ReuseableEvents.KATANA_IN, Side.CLIENT));
		
		KATANA_SHEATHED_IDLE = new StaticAnimation(0.1f,true, "biped/living/katana_sheathed_idle", biped);
		KATANA_SHEATHED_RUN = new MovementAnimation(0.1f,true, "biped/living/katana_sheathed_run", biped);

		KATANA_AUTO_1 = new BasicAttackAnimation(0.1F, 0.00F, 0.2F, 0.25F, null, biped.toolR, "biped/combat/katana_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, biped.toolR, null),
				new Phase(0.4F, 0.4F, 0.65F, 0.65F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F),0)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_3 = new BasicAttackAnimation(0.05F, 0.25F, 0.4F, 0.75F, null, biped.toolR, "biped/combat/katana_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		
		KATANA_DASH = new BasicAttackAnimation(0.05F, 0.4F, 0.7F, 1.05F, null, biped.toolR, "biped/combat/katana_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.3F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(4.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true);
		
		KATANA_IDLE = new StaticAnimation(0.1f,true, "biped/living/katana_idle", biped);
		
		KATANA_FATAL_DRAW = new AttackAnimation(0.15F, 0.0F, 0.50F, 0.70F, 0.70F, WOMColliders.FATAL_DRAW, biped.rootJoint, "biped/skill/katana_fatal_draw", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addEvents(TimeStampedEvent.create(0.05F, ReuseableEvents.KATANA_IN, Side.SERVER),
						TimeStampedEvent.create(0.5F, (entitypatch, params) -> {
							if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
								((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
								entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
							}
							entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
						}, Side.SERVER));
		
		KATANA_FATAL_DRAW_SECOND = new AttackAnimation(0.15F, 0.0F, 0.50F, 0.70F, 0.70F, WOMColliders.FATAL_DRAW, biped.rootJoint, "biped/skill/katana_fatal_draw_second", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addEvents(TimeStampedEvent.create(0.05F, ReuseableEvents.KATANA_IN, Side.SERVER),
						TimeStampedEvent.create(0.5F, (entitypatch, params) -> {
							if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
								((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
								entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
							}
							entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
						}, Side.SERVER));
		
		KATANA_FATAL_DRAW_DASH = new AttackAnimation(0.15F, 0.35F, 0.50F, 0.70F, 1.05F, WOMColliders.FATAL_DRAW_DASH, biped.rootJoint, "biped/skill/katana_fatal_draw_dash", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addEvents(TimeStampedEvent.create(0.05F, ReuseableEvents.KATANA_IN, Side.SERVER),
				TimeStampedEvent.create(0.5F, (entitypatch, params) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT),TimeStampedEvent.create(0.8F, (entitypatch, params) -> {
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Side.SERVER));
		
		
		ENDERBLASTER_ONEHAND_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_1", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, biped.legL, WOMColliders.KICK),
				new Phase(0.25F, 0.3F, 0.34F, 0.35F, Float.MAX_VALUE, biped.legL, WOMColliders.KICK))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, biped.legR, WOMColliders.KICK),
				new Phase(0.25F, 0.3F, 0.44F, 0.45F, 0.45F, biped.toolR, null),
				new Phase(0.45F, 0.5F, 0.54F, 0.55F, Float.MAX_VALUE, biped.elbowL, WOMColliders.KNEE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_3 = new BasicMultipleAttackAnimation(0.10F, "biped/combat/enderblaster_onehand_auto_3", biped,
				new Phase(0.0F, 0.05F, 0.19F, 0.2F, 0.2F, biped.toolR, null),
				new Phase(0.2F, 0.25F, 0.35F, 0.35F, Float.MAX_VALUE, biped.toolL, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_4", biped,
				new Phase(0.0F, 0.3F, 0.4F, 0.75F, Float.MAX_VALUE, biped.legL, WOMColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2.00F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_DASH = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_dash", biped,
				new Phase(0.0F, 0.15F, 0.20F, 0.45F, 0.45F, biped.legL, WOMColliders.KICK_HUGE),
				new Phase(0.45F, 0.45F, 0.75F, 0.85F, Float.MAX_VALUE, biped.legL, WOMColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(4.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		ENDERBLASTER_ONEHAND_JUMPKICK = new BasicMultipleAttackAnimation(0.00F, "biped/combat/enderblaster_onehand_jumpkick", biped,
				new Phase(0.0F, 0.15F, 0.19F, 0.2F, 0.2F, biped.legL, WOMColliders.KICK_HUGE),
				new Phase(0.2F, 0.25F, 0.29F, 0.30F, 0.30F, biped.legR, WOMColliders.KICK_HUGE),
				new Phase(0.30F, 0.35F, 0.39F, 0.6F, 0.6F, biped.legL, WOMColliders.KICK_HUGE),
				new Phase(0.6F, 0.6F, 0.7F, 0.80F, Float.MAX_VALUE, biped.legR, WOMColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.50F),3)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,3)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false);
		
		ENDERBLASTER_ONEHAND_SHOOT_1 = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_1", biped,
				new Phase(0.0F, 0.1F, 0.2F, 0.25F, 0.25F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.25F, 0.30F, 0.45F, 0.55F, 0.55F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.55F, 0.6F, 0.7F, 0.75F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.35F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.6F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_2 = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2", biped,
				new Phase(0.0F, 0.25F, 0.3F, 0.35F, 0.35F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.35F, 0.5F, 0.55F, 0.65F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_2_FORWARD = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_forward", biped,
				new Phase(0.0F, 0.25F, 0.3F, 0.35F, 0.35F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.35F, 0.5F, 0.55F, 0.65F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE,WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_2_LEFT = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_left", biped,
				new Phase(0.0F, 0.25F, 0.3F, 0.35F, 0.35F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.35F, 0.5F, 0.55F, 0.65F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						TimeStampedEvent.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_2_RIGHT = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_right", biped,
				new Phase(0.0F, 0.25F, 0.3F, 0.35F, 0.35F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.35F, 0.5F, 0.55F, 0.65F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_3 = new AttackAnimation(0.05F, 0.7F, 0.7F, 0.75F, 1.2F, WOMColliders.ENDER_LASER, biped.toolR, "biped/skill/enderblaster_onehand_shoot_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.5F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.00F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
					}
				}, Side.CLIENT),TimeStampedEvent.create(0.75F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1F);
					}
					OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
					OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
					transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrix2.translate(new Vec3f(0.0f,-0.8F,-0.3F));
					OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
					OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
					OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
					
					int n = 40; // set the number of particles to emit
					double r = 0.2; // set the radius of the disk to 1
					double t = 0.01; // set the thickness of the disk to 0.1
					
					for (int i = 0; i < n; i++) {
					    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
					    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

					    // calculate the emission direction in Cartesian coordinates using the polar coordinates
					    double x = r * Math.cos(phi) * Math.cos(theta);
					    double y = r * Math.cos(phi) * Math.sin(theta);
					    double z = r * Math.sin(phi);
					    
					 // create a Vector3f object to represent the emission direction
					    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

					    // rotate the direction vector to align with the forward vector
					    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
					    rotation.rotate((float) ((transformMatrix.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
					    OpenMatrix4f.transform3v(rotation, direction, direction);
					    
					    // emit the particle in the calculated direction, with some random velocity added
					    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
					        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
					        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
					        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					        (float)((transformMatrix2.m30 - transformMatrix.m30) + direction.x),
					        (float)((transformMatrix2.m31 - transformMatrix.m31) + direction.y),
					        (float)((transformMatrix2.m32 - transformMatrix.m32) + direction.z));
					}
					
					HitResult ray = entitypatch.getOriginal().pick(10.D, 0.7F, false);

					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrix.m30 + entitypatch.getOriginal().getX()),
							(transformMatrix.m31 + entitypatch.getOriginal().getY()),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y),
							(ray.getLocation().z));
				}, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_AIRSHOOT = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_airshoot", biped,
				new Phase(0.0F, 0.15F, 0.2F, 0.25F, 0.25F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.25F, 0.3F, 0.35F, 0.4F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.15F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.3F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_SHOOT_DASH = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_dash", biped,
				new Phase(0.0F, 0.0F, 0.25F, 0.55F, 0.55F, biped.rootJoint, WOMColliders.ENDER_DASH),
				new Phase(0.55F, 0.6F, 0.65F, 0.70F, 0.70F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.70F, 0.85F, 0.9F, 1F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(6.0F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_BIG)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.6F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.85F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_ONEHAND_IDLE = new StaticAnimation(0.1f,true, "biped/living/enderblaster_onehand_idle", biped);
		ENDERBLASTER_ONEHAND_WALK = new MovementAnimation(0.1f,true, "biped/living/enderblaster_onehand_walk", biped);
		ENDERBLASTER_ONEHAND_RUN = new MovementAnimation(0.1f,true, "biped/living/enderblaster_onehand_run", biped);
		
		ENDERBLASTER_TWOHAND_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_1", biped,
				new Phase(0.0F, 0.10F, 0.20F, 0.3F, 0.3F, InteractionHand.OFF_HAND, biped.legL, WOMColliders.KICK),
				new Phase(0.3F, 0.3F, 0.4F, 0.4F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.legL, WOMColliders.KICK))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.20F, 0.25F, 0.25F, biped.legR, WOMColliders.KICK),
				new Phase(0.25F, 0.30F, 0.40F, 0.45F, 0.45F, biped.toolR, null),
				new Phase(0.45F, 0.5F, 0.6F, 0.6F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.elbowL, WOMColliders.KNEE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_3 = new BasicMultipleAttackAnimation(0.10F, "biped/combat/enderblaster_onehand_auto_3", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.2F, 0.2F, biped.toolR, null),
				new Phase(0.2F, 0.25F, 0.35F, 0.35F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.toolL, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.75F, WOMColliders.KICK_HUGE, biped.legL, "biped/combat/enderblaster_onehand_auto_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2.00F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_TISHNAW = new BasicAttackAnimation(0.05F, 0.3F, 0.5F, 0.65F, WOMColliders.ENDER_TISHNAW, biped.legR, "biped/combat/enderblaster_twohand_tishnaw", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.65F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(3.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.3F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
					TimeStampedEvent.create(0.50F, ReuseableEvents.GROUND_BODYSCRAPE_LAND, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_TOMAHAWK = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_twohand_dash", biped,
				new Phase(0.0F, 0.3F, 0.44F, 0.45F, 0.45F, biped.legL, WOMColliders.KICK_HUGE),
				new Phase(0.45F, 0.5F, 0.6F, 0.65F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.TORMENT_AIRSLAM ))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.3F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(5.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(5.0F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.45F, ReuseableEvents.GROUND_BODYSCRAPE_LAND, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_SHOOT_1 = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_1", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.30F, 0.30F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.30F, 0.35F, 0.4F, 0.55F, 0.55F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.55F, 0.6F, 0.65F, 0.70F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.4F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.65F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));

		ENDERBLASTER_TWOHAND_SHOOT_2 = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_2", biped,
				new Phase(0.0F, 0.4F, 0.44F, 0.45F, 0.45F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.45F, 0.5F, 0.54F, 0.55F, 0.55F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.55F, 0.6F, 0.64F, 0.65F, 0.65F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.65F, 0.7F, 0.74F, 0.75F, 0.75F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.75F, 0.8F, 0.84F, 0.85F, 0.85F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.85F, 0.9F, 0.94F, 0.95F, 0.95F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.95F, 1.0F, 1.04F, 1.05F, 1.05F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(1.05F, 1.1F, 1.14F, 1.2F, Float.MAX_VALUE, biped.toolR, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.5F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),3)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,3)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),4)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),4)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,4)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,4)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,4)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),5)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),5)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,5)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,5)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,5)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),6)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),6)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,6)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,6)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,6)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.5F),7)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),7)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,7)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,7)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,7)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.45F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.55F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.65F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.75F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.85F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.95F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(1.05F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(1.15F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_SHOOT_3 = new EnderblasterShootAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_3", biped,
				new Phase(0.0F, 0.25F, 0.35F, 0.5F, 0.5F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_BULLET_BACKANDFORTH),
				new Phase(0.5F, 0.5F, 0.6F, 0.75F, 0.75F, biped.toolR, WOMColliders.ENDER_BULLET_BACKANDFORTH),
				new Phase(0.75F, 0.75F, 0.85F, 0.9F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.rootJoint, WOMColliders.ENDER_BULLET_WIDE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.85F),2)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(2.0F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.25F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT),
					TimeStampedEvent.create(0.55F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT),
					TimeStampedEvent.create(0.80F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_SHOOT_4 = new AttackAnimation(0.05F, 0.35F, 0.35F, 0.45F, 0.8F, WOMColliders.ENDER_LASER, biped.toolR, "biped/skill/enderblaster_twohand_shoot_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.00F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
					}
				}, Side.CLIENT),TimeStampedEvent.create(0.35F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1F);
					}
					OpenMatrix4f transformMatrixR = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
					OpenMatrix4f transformMatrixR2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
					transformMatrixR.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrixR2.translate(new Vec3f(0.0f,-0.8F,-0.3F));
					OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
					OpenMatrix4f.mul(CORRECTION,transformMatrixR,transformMatrixR);
					OpenMatrix4f.mul(CORRECTION,transformMatrixR2,transformMatrixR2);
					
					int n = 40; // set the number of particles to emit
					double r = 0.2; // set the radius of the disk to 1
					double t = 0.01; // set the thickness of the disk to 0.1
					
					for (int i = 0; i < n; i++) {
					    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
					    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

					    // calculate the emission direction in Cartesian coordinates using the polar coordinates
					    double x = r * Math.cos(phi) * Math.cos(theta);
					    double y = r * Math.cos(phi) * Math.sin(theta);
					    double z = r * Math.sin(phi);
					    
					 // create a Vector3f object to represent the emission direction
					    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

					    // rotate the direction vector to align with the forward vector
					    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
					    rotation.rotate((float) ((transformMatrixR.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
					    OpenMatrix4f.transform3v(rotation, direction, direction);
					    
					    // emit the particle in the calculated direction, with some random velocity added
					    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
					        (transformMatrixR.m30 + entitypatch.getOriginal().getX()),
					        (transformMatrixR.m31 + entitypatch.getOriginal().getY()),
					        (transformMatrixR.m32 + entitypatch.getOriginal().getZ()),
					        (float)((transformMatrixR2.m30 - transformMatrixR.m30) + direction.x),
					        (float)((transformMatrixR2.m31 - transformMatrixR.m31) + direction.y),
					        (float)((transformMatrixR2.m32 - transformMatrixR.m32) + direction.z));
					}
					
					HitResult ray = entitypatch.getOriginal().pick(20.D, 0.7F, false);

					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrixR.m30 + entitypatch.getOriginal().getX()),
							(transformMatrixR.m31 + entitypatch.getOriginal().getY()),
							(transformMatrixR.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y),
							(ray.getLocation().z));
					
					
					
					OpenMatrix4f transformMatrixL = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
					OpenMatrix4f transformMatrixL2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
					transformMatrixL.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrixL2.translate(new Vec3f(0.0f,-0.8F,-0.3F));
					OpenMatrix4f CORRECTION2 = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
					OpenMatrix4f.mul(CORRECTION2,transformMatrixL,transformMatrixL);
					OpenMatrix4f.mul(CORRECTION2,transformMatrixL2,transformMatrixL2);
					
					for (int i = 0; i < n; i++) {
					    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
					    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

					    // calculate the emission direction in Cartesian coordinates using the polar coordinates
					    double x = r * Math.cos(phi) * Math.cos(theta);
					    double y = r * Math.cos(phi) * Math.sin(theta);
					    double z = r * Math.sin(phi);
					    
					 // create a Vector3f object to represent the emission direction
					    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

					    // rotate the direction vector to align with the forward vector
					    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
					    rotation.rotate((float) ((transformMatrixL.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
					    OpenMatrix4f.transform3v(rotation, direction, direction);
					    
					    // emit the particle in the calculated direction, with some random velocity added
					    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
					        (transformMatrixL.m30 + entitypatch.getOriginal().getX()),
					        (transformMatrixL.m31 + entitypatch.getOriginal().getY()),
					        (transformMatrixL.m32 + entitypatch.getOriginal().getZ()),
					        (float)((transformMatrixL2.m30 - transformMatrixL.m30) + direction.x),
					        (float)((transformMatrixL2.m31 - transformMatrixL.m31) + direction.y),
					        (float)((transformMatrixL2.m32 - transformMatrixL.m32) + direction.z));
					}

					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrixL.m30 + entitypatch.getOriginal().getX()),
							(transformMatrixL.m31 + entitypatch.getOriginal().getY()),
							(transformMatrixL.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y),
							(ray.getLocation().z));
				}, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_AIRSHOOT = new EnderblasterShootAttackAnimation(0.00F, "biped/skill/enderblaster_twohand_airshoot", biped,
				new Phase(0.0F, 0.4F, 0.45F, 0.49F, 0.49F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.49F, 0.5F, 0.55F, 0.59F, 0.59F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.59F, 0.6F, 0.65F, 0.69F, 0.69F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.69F, 0.7F, 0.75F, 0.79F, 0.79F, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT),
				new Phase(0.79F, 0.8F, 0.85F, 0.89F, 0.89F, biped.toolR, WOMColliders.ENDER_SHOOT),
				new Phase(0.89F, 0.9F, 0.95F, 1.3F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.toolL, WOMColliders.ENDER_SHOOT))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),3)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,3)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),4)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),4)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,4)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,4)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,4)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(1.2F),5)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),5)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,5)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,5)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,5)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.45F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.55F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.65F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.75F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.85F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.95F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_PISTOLERO = new AttackAnimation(0.00F, "biped/skill/enderblaster_twohand_shoot_dash", biped,
				new Phase(0.0F, 0.35F, 0.39F, 0.4F, 0.4F, biped.rootJoint, WOMColliders.ENDER_PISTOLERO),
				new Phase(0.4F, 0.45F, 0.49F, 0.5F, 0.5F, InteractionHand.OFF_HAND, biped.rootJoint, WOMColliders.ENDER_PISTOLERO),
				new Phase(0.5F, 0.55F, 0.59F, 0.6F, 0.6F, biped.rootJoint, WOMColliders.ENDER_PISTOLERO),
				new Phase(0.6F, 0.65F, 0.69F, 0.7F, 0.7F, InteractionHand.OFF_HAND, biped.rootJoint, WOMColliders.ENDER_PISTOLERO),
				new Phase(0.7F, 0.75F, 0.79F, 0.8F, 0.8F, biped.rootJoint, WOMColliders.ENDER_PISTOLERO),
				new Phase(0.8F, 0.85F, 0.9F, 1.5F, Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.rootJoint, WOMColliders.ENDER_PISTOLERO))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),3)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,3)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),4)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),4)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,4)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,4)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,4)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER,ValueModifier.multiplier(0.6F),5)
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()),5)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,5)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,5)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ENDERBLASTER_BULLET_HIT,5)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.4F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.45F, ReuseableEvents.SHOTGUN_SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.55F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.65F, ReuseableEvents.SHOTGUN_SHOOT_LEFT, Side.CLIENT),
					TimeStampedEvent.create(0.75F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
					TimeStampedEvent.create(0.85F, ReuseableEvents.SHOTGUN_SHOOT_LEFT, Side.CLIENT));
		
		ENDERBLASTER_TWOHAND_IDLE = new StaticAnimation(0.1f,true, "biped/living/enderblaster_twohand_idle", biped);
		
		STAFF_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_1", biped,
				new Phase(0.0F, 0.1F, 0.19F, 0.2F, 0.2F, biped.toolR, null),
				new Phase(0.2F, 0.25F, 0.34F, 0.35F, 0.35F, biped.toolR, null),
				new Phase(0.35F, 0.4F, 0.49F, 0.50F, 0.50F, biped.toolR, null),
				new Phase(0.5F, 0.55F, 0.7F, 0.7F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.40F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.40F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.40F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.40F),3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.25F, 0.35F, 0.35F, biped.toolR, null),
				new Phase(0.35F, 0.35F, 0.45F, 0.55F, 0.55F, biped.toolR, null),
				new Phase(0.55F, 0.55F, 0.65F, 0.65F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.60F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.60F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.60F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.45F, 0.65F, 0.65F, biped.toolR, null),
				new Phase(0.65F, 0.65F, 0.85F, 0.85F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.95F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.20F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.95F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.20F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_DASH = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_dash", biped,
				new Phase(0.0F, 0.15F, 0.25F, 0.3F, 0.3F, biped.toolR, null),
				new Phase(0.30F, 0.30F, 0.40F, 0.50F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.40F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_CHARYBDIS = new AttackAnimation(0.1F, "biped/skill/staff_charybdisandscylla", biped,
				new Phase(0.0F, 0.1F, 0.14F, 0.15F, 0.15F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.15F, 0.2F, 0.24F, 0.25F, 0.25F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.25F, 0.3F, 0.34F, 0.35F, 0.35F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.35F, 0.4F, 0.44F, 0.45F, 0.45F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.45F, 0.55F, 0.59F, 0.6F, 0.6F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.6F, 0.65F, 0.69F, 0.7F, 0.7F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.7F, 0.75F, 0.79F, 0.8F, 0.8F, biped.toolR, WOMColliders.STAFF_CHARYBDIS),
				new Phase(0.8F, 0.85F, 0.89F, 0.9F, Float.MAX_VALUE, biped.toolR, WOMColliders.STAFF_CHARYBDIS))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),3)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,3)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),4)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,4)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),5)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,5)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),6)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F),7)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,4)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,5)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,6)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,7)
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.65F);
		
		STAFF_IDLE = new StaticAnimation(0.1f,true, "biped/living/staff_idle", biped);
		STAFF_RUN = new MovementAnimation(0.1f,true, "biped/living/staff_run", biped);
		
		ANTITHEUS_AGRESSION = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_agression", biped,
				new Phase(0.0F, 0.25F, 0.4F, 0.55F, 0.55F, biped.toolR, null),
				new Phase(0.55F, 0.6F, 0.8F, 0.85F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(-1),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)		
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.05F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
					TimeStampedEvent.create(0.15F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(0.45F, ReuseableEvents.RUINE_COMET_GROUNDTHRUST, Side.CLIENT),
					TimeStampedEvent.create(0.8F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_GUILLOTINE = new BasicMultipleAttackAnimation(0.00F, "biped/combat/antitheus_guillotine", biped,
				new Phase(0.0F, 0.6F, 0.75F, 0.8F, 0.8F, biped.rootJoint, WOMColliders.ANTITHEUS_GUILLOTINE),
				new Phase(0.8F, 0.9F, 1.0F, 1.1F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.ANTITHEUS_GUILLOTINE))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.40F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.60F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F),1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT,1)				
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT),
					TimeStampedEvent.create(0.5F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(1.0F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));

		ANTITHEUS_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_auto_1", biped,
				new Phase(0.0F, 0.35F, 0.55F, 0.6F, 0.6F, biped.toolR, null),
				new Phase(0.6F, 0.70F, 0.9F, 0.9F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT),
					TimeStampedEvent.create(0.25F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(0.9F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_AUTO_2 = new BasicAttackAnimation(0.05F, 0.15F, 0.45F, 0.45F,null, biped.toolR, "biped/combat/antitheus_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addEvents(TimeStampedEvent.create(0.1F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						TimeStampedEvent.create(0.45F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.35F, 0.5F, 0.5F, biped.toolR, null),
				new Phase(0.5F, 0.55F, 0.70F, 0.75F, Float.MAX_VALUE, biped.toolR, null))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.8F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(-1),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addEvents(TimeStampedEvent.create(0.10F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(0.75F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_AUTO_4 = new BasicAttackAnimation(0.05F, 0.55F, 0.75F, 0.9F, null, biped.toolR, "biped/combat/antitheus_auto_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(AttackAnimationProperty.EXTRA_COLLIDERS, 2)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addEvents(TimeStampedEvent.create(0.55F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(0.75F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_IDLE = new StaticAnimation(0.2f,true, "biped/living/antitheus_idle", biped)
				.addEvents(TimeStampedEvent.create(6.00F, (entitypatch, params) -> {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setData(DemonMarkPassiveSkill.IDLE, true);
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.2F, 0.5F);
				}, Side.CLIENT),
					TimeStampedEvent.create(8.60F, (entitypatch, params) -> {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.5F);
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setData(DemonMarkPassiveSkill.IDLE, false);
						OpenMatrix4f transformMatrix;
						transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0), Armatures.BIPED.toolL);
						transformMatrix.translate(new Vec3f(0,0.0F,0));
						OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
						int n = 70; // set the number of particles to emit
						double r = 0.1; // set the radius of the hemisphere to 1

						for (int i = 0; i < n; i++) {
						    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
						    double phi = Math.acos(2 * new Random().nextDouble() - 1); // generate a random polar angle in the upper hemisphere

						    // calculate the emission direction in Cartesian coordinates using the polar coordinates
						    double x = r * Math.sin(phi) * Math.cos(theta);
						    double y = r * Math.sin(phi) * Math.sin(theta);
						    double z = r * Math.cos(phi);

						    // emit the particle in the calculated direction, with some random velocity added
						    entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
						        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
						        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
						        (float)(x),
						        (float)(y),
						        (float)(z));
						}
				}, Side.CLIENT));
		
		ANTITHEUS_RUN = new MovementAnimation(0.2f,true, "biped/living/antitheus_run", biped);
		ANTITHEUS_WALK = new MovementAnimation(0.2f,true, "biped/living/antitheus_walk", biped);
		ANTITHEUS_GUARD = new StaticAnimation(0.05F, true, "biped/skill/antitheus_guard", biped);
		ANTITHEUS_GUARD_HIT_1 = new GuardAnimation(0.05F, "biped/skill/antitheus_guard_hit_1", biped);
		ANTITHEUS_GUARD_HIT_2 = new GuardAnimation(0.05F, "biped/skill/antitheus_guard_hit_2", biped);
		ANTITHEUS_GUARD_HIT_3 = new GuardAnimation(0.05F, "biped/skill/antitheus_guard_hit_3", biped);
		
		ANTITHEUS_ASCENSION = new AttackAnimation(0.1f, "biped/skill/antitheus_ascension", biped,
				new Phase(0.0f, 0.5f, 0.6f, 0.65f, 0.65f, biped.rootJoint, WOMColliders.PLUNDER_PERDITION),
				new Phase(0.65f, 1.75f, 2.05f, 2.85f, Float.MAX_VALUE, biped.rootJoint, WOMColliders.PLUNDER_PERDITION))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20))
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(20),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT,1)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addEvents(TimeStampedEvent.create(0.5F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 0.5F);
				
				}, Side.CLIENT),TimeStampedEvent.create(1.75F, (entitypatch, params) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.ACTIVE, true, (ServerPlayer)entitypatch.getOriginal());
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(DemonicAscensionSkill.ACTIVE, true, (ServerPlayer)entitypatch.getOriginal());
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(DemonicAscensionSkill.ASCENDING, true, (ServerPlayer)entitypatch.getOriginal());
					entitypatch.getOriginal().level.playSound(null, entitypatch.getOriginal(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 1.0F, 0.5F);
					entitypatch.getOriginal().level.playSound(null, entitypatch.getOriginal(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.5F);
				}, Side.SERVER));
		
		ANTITHEUS_ASCENDED_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.4F, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES, biped.rootJoint, "biped/skill/antitheus_ascended_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.9F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT));
		
		ANTITHEUS_ASCENDED_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/skill/antitheus_ascended_auto_2", biped,
				new Phase(0.0F, 0.3F, 0.4F, 0.5F, 0.5F, biped.rootJoint, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.5F, 0.6F, 0.7F, 0.7F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT));
		
		ANTITHEUS_ASCENDED_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/skill/antitheus_ascended_auto_3", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, biped.rootJoint, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.35F, 0.4F, 0.5F, 0.55F, 0.55F, biped.rootJoint, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.55F, 0.7F, 0.80F, 0.85F, Float.MAX_VALUE, biped.rootJoint, WOMColliders.ANTITHEUS_ASCENDED_PUNCHES))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.9F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT));
		
		ANTITHEUS_ASCENDED_DEATHFALL = new BasicMultipleAttackAnimation(0.05F, 0.5F, 0.6F, 0.6F, WOMColliders.ANTITHEUS_ASCENDED_DEATHFALL, biped.rootJoint, "biped/skill/antitheus_ascended_deathfall", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)		
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT),
				TimeStampedEvent.create(0.35F, (entitypatch, params) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(DemonicAscensionSkill.SUPERARMOR, true, (ServerPlayer)entitypatch.getOriginal());
					entitypatch.getOriginal().resetFallDistance();
				}, Side.SERVER),
				TimeStampedEvent.create(0.45F, (entitypatch, params) -> {
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
					entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
				}, Side.CLIENT),
				TimeStampedEvent.create(0.5F, (entitypatch, params) -> {
					entitypatch.getOriginal().resetFallDistance();
				}, Side.SERVER),
				TimeStampedEvent.create(0.55F, (entitypatch, params) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.7F, 0.5F);
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 0.7F, 0.7F);
					}
					
					for (int i = 0; i < 140; i++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
							entitypatch.getOriginal().getX(),
							entitypatch.getOriginal().getY(),
							entitypatch.getOriginal().getZ(),
							(new Random().nextFloat() - 0.5F) * 0.6f,
							(new Random().nextFloat() * 0.8F) + 0.02f,
							(new Random().nextFloat() - 0.5F) * 0.6f);
					}
					for (int i = 0; i < 60; i++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
							entitypatch.getOriginal().getX(),
							entitypatch.getOriginal().getY() + 0.2F,
							entitypatch.getOriginal().getZ(),
							((new Random().nextFloat() - 0.5F) * 0.9F),
							((new Random().nextFloat()) * 0.01F) + 0.05F,
							((new Random().nextFloat() - 0.5F) * 0.9F));
					}
				}, Side.CLIENT),
				TimeStampedEvent.create(0.55F, (entitypatch, params) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(DemonicAscensionSkill.SUPERARMOR, false, (ServerPlayer)entitypatch.getOriginal());
					entitypatch.getOriginal().resetFallDistance();
				}, Side.SERVER));
		
		ANTITHEUS_ASCENDED_BLINK = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.4F, WOMColliders.ANTITHEUS_ASCENDED_BLINK, biped.rootJoint, "biped/skill/antitheus_ascended_blink", biped)
				.addProperty(AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
				.addProperty(AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F))
				.addProperty(AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.3F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.PARTICLE, WOMParticles.ANTITHEUS_HIT)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT)	
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addEvents(TimeStampedEvent.create(0.05F, (entitypatch, params) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
					TimeStampedEvent.create(0.30F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
					TimeStampedEvent.create(0.40F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER));
		
		ANTITHEUS_ASCENDED_IDLE = new StaticAnimation(0.1f, true, "biped/living/antitheus_ascended_idle", biped);
		ANTITHEUS_ASCENDED_RUN = new MovementAnimation(0.1f, true, "biped/living/antitheus_ascended_run", biped);
		ANTITHEUS_ASCENDED_WALK = new MovementAnimation(0.1f, true, "biped/living/antitheus_ascended_walk", biped);
		
		HERRSCHER_IDLE = new StaticAnimation(0.1f,true, "biped/living/herrscher_idle", biped);
		
	}
	
	private static class ReuseableEvents {
		
		public static final BiConsumer<LivingEntityPatch<?>, Object[]> KATANA_IN = (entitypatch, params) -> entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOOT_RIGHT = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			
			//System.out.println((transformMatrix.m11+0.07f)*1.2f);
			
			int n = 20; // set the number of particles to emit
			double r = 0.1; // set the radius of the disk to 1
			double t = 0.01; // set the thickness of the disk to 0.1
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			 // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrix.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrix2.m30 - transformMatrix.m30) + direction.x),
			        (float)((transformMatrix2.m31 - transformMatrix.m31) + direction.y),
			        (float)((transformMatrix2.m32 - transformMatrix.m32) + direction.z));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOTGUN_SHOOT_RIGHT = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			
			int n = 30; // set the number of particles to emit
			double r = 0.15; // set the radius of the disk to 1
			double t = 0.01; // set the thickness of the disk to 0.1
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			    // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrix.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrix2.m30 - transformMatrix.m30) + direction.x),
			        (float)((transformMatrix2.m31 - transformMatrix.m31) + direction.y),
			        (float)((transformMatrix2.m32 - transformMatrix.m32) + direction.z));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOOT_LEFT = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			
			int n = 20; // set the number of particles to emit
			double r = 0.1; // set the radius of the disk to 1
			double t = 0.01; // set the thickness of the disk to 0.1
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			    // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrix.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrix2.m30 - transformMatrix.m30) + direction.x),
			        (float)((transformMatrix2.m31 - transformMatrix.m31) + direction.y),
			        (float)((transformMatrix2.m32 - transformMatrix.m32) + direction.z));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOTGUN_SHOOT_LEFT = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			
			int n = 30; // set the number of particles to emit
			double r = 0.15; // set the radius of the disk to 1
			double t = 0.01; // set the thickness of the disk to 0.1
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			    // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrix.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrix.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrix.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrix.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrix2.m30 - transformMatrix.m30) + direction.x),
			        (float)((transformMatrix2.m31 - transformMatrix.m31) + direction.y),
			        (float)((transformMatrix2.m32 - transformMatrix.m32) + direction.z));
			}
		};
		/*
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOOT_BOTH = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrixl = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrixl2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrixr = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrixr2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			transformMatrixl.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixl2.translate(new Vec3f(0.0f,-2F,-0.3F));
			transformMatrixr.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixr2.translate(new Vec3f(0.0f,-2F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrixl,transformMatrixl);
			OpenMatrix4f.mul(CORRECTION,transformMatrixl2,transformMatrixl2);
			OpenMatrix4f.mul(CORRECTION,transformMatrixr,transformMatrixr);
			OpenMatrix4f.mul(CORRECTION,transformMatrixr2,transformMatrixr2);
			for (int i = 0; i < 20; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrixl.m30 + entitypatch.getOriginal().getX()),
					(transformMatrixl.m31 + entitypatch.getOriginal().getY()),
					(transformMatrixl.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrixl2.m30 - transformMatrixl.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrixl2.m31 - transformMatrixl.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrixl2.m32 - transformMatrixl.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
			for (int i = 0; i < 20; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
						(transformMatrixr.m30 + entitypatch.getOriginal().getX()),
						(transformMatrixr.m31 + entitypatch.getOriginal().getY()),
						(transformMatrixr.m32 + entitypatch.getOriginal().getZ()),
						((transformMatrixr2.m30 - transformMatrixr.m30) + (new Random().nextFloat() - 0.5F)*2),
						((transformMatrixr2.m31 - transformMatrixr.m31) + (new Random().nextFloat() - 0.5F)*2),
						((transformMatrixr2.m32 - transformMatrixr.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		*/
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> SHOTGUN_SHOOT_BOTH = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrixl = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			OpenMatrix4f transformMatrixl2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolL);
			transformMatrixl.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixl2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixl,transformMatrixl);
			OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixl2,transformMatrixl2);
			
			int n = 20; // set the number of particles to emit
			double r = 0.1; // set the radius of the disk to 1
			double t = 0.01; // set the thickness of the disk to 0.1
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			    // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrixl.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrixl.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrixl.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrixl.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrixl2.m30 - transformMatrixl.m30) + direction.x),
			        (float)((transformMatrixl2.m31 - transformMatrixl.m31) + direction.y),
			        (float)((transformMatrixl2.m32 - transformMatrixl.m32) + direction.z));
			}
			
			OpenMatrix4f transformMatrixr = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			OpenMatrix4f transformMatrixr2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrixr.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixr2.translate(new Vec3f(0.0f,-0.7F,-0.3F));
			OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixr,transformMatrixr);
			OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixr2,transformMatrixr2);
			
			for (int i = 0; i < n; i++) {
			    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random azimuthal angle
			    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

			    // calculate the emission direction in Cartesian coordinates using the polar coordinates
			    double x = r * Math.cos(phi) * Math.cos(theta);
			    double y = r * Math.cos(phi) * Math.sin(theta);
			    double z = r * Math.sin(phi);
			    
			 // create a Vector3f object to represent the emission direction
			    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

			    // rotate the direction vector to align with the forward vector
			    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0, 1, 0));
			    rotation.rotate((float) ((transformMatrixr.m11+0.07f)*1.5f), new Vec3f(1, 0, 0));
			    OpenMatrix4f.transform3v(rotation, direction, direction);
			    
			    // emit the particle in the calculated direction, with some random velocity added
			    entitypatch.getOriginal().level.addParticle(ParticleTypes.DRAGON_BREATH,
			        (transformMatrixr.m30 + entitypatch.getOriginal().getX()),
			        (transformMatrixr.m31 + entitypatch.getOriginal().getY()),
			        (transformMatrixr.m32 + entitypatch.getOriginal().getZ()),
			        (float)((transformMatrixr2.m30 - transformMatrixr.m30) + direction.x),
			        (float)((transformMatrixr2.m31 - transformMatrixr.m31) + direction.y),
			        (float)((transformMatrixr2.m32 - transformMatrixr.m32) + direction.z));
			}
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> AGONY_AIRBURST_JUMP = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.PLAYERS, 0.7F, 0.7F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			for (int i = 0; i < 25; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CLOUD,
					entitypatch.getOriginal().getX(),
					entitypatch.getOriginal().getY() + 0.2F,
					entitypatch.getOriginal().getZ(),
					((new Random().nextFloat() - 0.5F) * 0.8F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 0.8F));
			}
			
			for (int i = 0; i < 12; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CLOUD,
					entitypatch.getOriginal().getX() + ((new Random().nextFloat() - 0.5F)),
					entitypatch.getOriginal().getY() + 0.2F,
					entitypatch.getOriginal().getZ() + ((new Random().nextFloat() - 0.5F)),
					((new Random().nextFloat() - 0.5F) * 0.05F),
					((new Random().nextFloat()) * 0.5F)+ 0.2F,
					((new Random().nextFloat() - 0.5F) * 0.05F));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> AGONY_ENCHANTED_JUMP = (entitypatch, params) -> {
			float lineXpos = 0;
			float lineYpos = 0;
			float lineZpos = 0;
			for (int i = 0; i < 10; i++) {
				lineXpos = (new Random().nextFloat() - 0.5F)*16;
				lineYpos = (new Random().nextFloat())*2.5f;
				lineZpos = (new Random().nextFloat() - 0.5F)*16;
				for (int j = 0; j < 10; j++) {
					entitypatch.getOriginal().level.addAlwaysVisibleParticle(ParticleTypes.ENCHANT,
						entitypatch.getOriginal().getX() + lineXpos,
						entitypatch.getOriginal().getY() + (0.3f*j) + lineYpos + 5F,
						entitypatch.getOriginal().getZ() + lineZpos,
						0,
						-7.5F,
						0);
				}
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> AGONY_PLUNGE_GROUNDTHRUST = (entitypatch, params) -> {
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,0,-1.5F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			for (int i = 0; i < 100; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F)*5),
					(new Random().nextFloat()*5)+0.8f,
					((new Random().nextFloat() - 0.5F)*5));
			}
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()) + 0.2f,
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F) * 8.0F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 8.0F));
			}
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> TORMENT_GROUNDSLAM = (entitypatch, params) -> {
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,-0.2F,-1.4F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			for (int i = 0; i < 100; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F)*4),
					(new Random().nextFloat()*5)+1.0f,
					((new Random().nextFloat() - 0.5F)*4));
			}
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()) + 0.2f,
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F) * 6.0F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 6.0F));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> TORMENT_GROUNDSLAM_SMALL = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 1.0F, 0.5F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,-0.2F,-1.4F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			for (int i = 0; i < 50; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F)*2.5),
					(new Random().nextFloat()*3.0F)+0.5f,
					((new Random().nextFloat() - 0.5F)*2.5));
			}
			for (int i = 0; i < 30; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()) + 0.2f,
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F) * 4.0F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 4.0F));
			}
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> RUINE_COMET_GROUNDTHRUST = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0, 0,-1.4F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			for (int i = 0; i < 40; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F)*2.0),
					(new Random().nextFloat()*2.0F)+0.2f,
					((new Random().nextFloat() - 0.5F)*2.0));
			}
			for (int i = 0; i < 30; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()) + 0.2f,
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F) * 3.5F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 3.5F));
			}
		};
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> RUINE_COMET_AIRBURST = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.PLAYERS, 0.7F, 0.7F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			for (int i = 0; i < 20; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CLOUD,
					entitypatch.getOriginal().getX(),
					entitypatch.getOriginal().getY() + 1.0F,
					entitypatch.getOriginal().getZ(),
					((new Random().nextFloat() - 0.5F) * 0.3F),
					((new Random().nextFloat() - 0.5F) * 0.3F),
					((new Random().nextFloat() - 0.5F) * 0.3F));
			}
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> GROUND_BODYSCRAPE_LAND = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			for (int i = 0; i < 40; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					entitypatch.getOriginal().getX(),
					entitypatch.getOriginal().getY(),
					entitypatch.getOriginal().getZ(),
					(new Random().nextFloat() - 0.5F) * 2.0f,
					(new Random().nextFloat() * 2.0F) + 0.2f,
					(new Random().nextFloat() - 0.5F) * 2.0f);
			}
			for (int i = 0; i < 30; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					entitypatch.getOriginal().getX(),
					entitypatch.getOriginal().getY() + 0.2F,
					entitypatch.getOriginal().getZ(),
					((new Random().nextFloat() - 0.5F) * 3.5F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 3.5F));
			}
		};
		/*
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> GROUND_BODYSCRAPE_TRAIL = (entitypatch, params) -> {
			for (int i = 0; i < 10; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					entitypatch.getOriginal().getX(),
					entitypatch.getOriginal().getY(),
					entitypatch.getOriginal().getZ(),
					(new Random().nextFloat() - 0.5F) * 1.5,
					(new Random().nextFloat() * 0.5) + 0.2f,
					(new Random().nextFloat() - 0.5F) * 1.5);
			}
		};
		*/
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> AGONY_GROUNDSLAM = (entitypatch, params) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(0.0f), Armatures.BIPED.toolR);
			transformMatrix.translate(new Vec3f(0,0.0F,-2.4F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			for (int i = 0; i < 50; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F)*2.5),
					(new Random().nextFloat()*3.0F)+0.5f,
					((new Random().nextFloat() - 0.5F)*2.5));
			}
			for (int i = 0; i < 30; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.CRIT,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()) + 0.2f,
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((new Random().nextFloat() - 0.5F) * 4.0F),
					((new Random().nextFloat()) * 0.05F) + 0.05F,
					((new Random().nextFloat() - 0.5F) * 4.0F));
			}
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> ANTITHEUS_WEAPON_TRAIL_ON = (entitypatch, params) -> {
			((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.BASIC_ATTACK, true, (ServerPlayer)entitypatch.getOriginal());
		};
		
		private static final BiConsumer<LivingEntityPatch<?>, Object[]> ANTITHEUS_WEAPON_TRAIL_OFF = (entitypatch, params) -> {
			((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.BASIC_ATTACK, false, (ServerPlayer)entitypatch.getOriginal());
		};
	}
}
