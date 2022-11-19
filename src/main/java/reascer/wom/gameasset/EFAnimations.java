package reascer.wom.gameasset;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.commands.ParticleCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import reascer.wom.animation.BasicMultipleAttackAnimation;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.particle.EFEpicFightParticles;
import reascer.wom.skill.DemonMarkPassiveSkill;
import reascer.wom.skill.DemonicAscensionSkill;
import reascer.wom.skill.EFKatanaPassive;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.AttackPhaseProperty;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation.Phase;
import yesman.epicfight.api.animation.types.StaticAnimation.Event;
import yesman.epicfight.api.animation.types.StaticAnimation.Event.Side;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.DashAttackAnimation;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.api.animation.types.SpecialAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueCorrector;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mod.EventBusSubscriber(modid = WeaponOfMinecraft.MODID , bus = EventBusSubscriber.Bus.MOD)
public class EFAnimations {
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
	public static StaticAnimation AGONY_DASH;
	public static StaticAnimation AGONY_IDLE;
	public static StaticAnimation AGONY_RUN;
	public static StaticAnimation AGONY_WALK;
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
	public static StaticAnimation ANTITHEUS_RUN;
	public static StaticAnimation ANTITHEUS_WALK;
	public static StaticAnimation ANTITHEUS_ASCENSION;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_1;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_2;
	public static StaticAnimation ANTITHEUS_ASCENDED_AUTO_3;
	public static StaticAnimation ANTITHEUS_ASCENDED_DEATHFALL;
	public static StaticAnimation ANTITHEUS_ASCENDED_BLINK;
	public static StaticAnimation ANTITHEUS_ASCENDED_IDLE;
	public static StaticAnimation ANTITHEUS_ASCENDED_RUN;
	public static StaticAnimation ANTITHEUS_ASCENDED_WALK;
	
	@SubscribeEvent
	public static void registerAnimations(AnimationRegistryEvent event) {
		event.getRegistryMap().put(WeaponOfMinecraft.MODID, EFAnimations::build);
	}
	
	private static void build() {
		Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
		Model biped = models.biped;
		
		EATING = new StaticAnimation(0.11F, true, "biped/living/eating", biped);
		
		KICK = new BasicAttackAnimation(0.1F, 0.05F, 0.15F, 0.2F, null, "Tool_R", "biped/skill/kick", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(2))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(5))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		DODGEMASTER_BACKWARD = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_back", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.00F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT)});
		DODGEMASTER_LEFT = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_left", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.00F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT)});
		DODGEMASTER_RIGHT = new DodgeAnimation(0.00F, "biped/skill/dodgemaster_right", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.00F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().playSound(EpicFightSounds.WHOOSH_BIG, 1.0F, 2.0F);
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT)});
		
		
		ENDERSTEP_FORWARD = new DodgeAnimation(0.05F, "biped/skill/enderstep_forward", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, (entitypatch) -> {
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
				}, Side.CLIENT)});
		ENDERSTEP_BACKWARD = new DodgeAnimation(0.05F, "biped/skill/enderstep_backward", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, (entitypatch) -> {
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
					}, Side.CLIENT)});
		ENDERSTEP_LEFT = new DodgeAnimation(0.05F, "biped/skill/enderstep_left", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, (entitypatch) -> {
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
				}, Side.CLIENT)});
		ENDERSTEP_RIGHT = new DodgeAnimation(0.05F, "biped/skill/enderstep_right", 0.6F, 1.65F, biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, (entitypatch) -> {
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
					}, Side.CLIENT)});
		
		KNIGHT_ROLL_FORWARD = new DodgeAnimation(0.1F, "biped/skill/roll_forward", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_BACKWARD = new DodgeAnimation(0.1F, "biped/skill/roll_backward", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_LEFT = new DodgeAnimation(0.1F, "biped/skill/roll_left", 0.6F, 0.8F, biped);
		KNIGHT_ROLL_RIGHT = new DodgeAnimation(0.1F, "biped/skill/roll_right", 0.6F, 0.8F, biped);
		
		SWORD_ONEHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/sword_onehand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		
		TACHI_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/tachi_twohand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/tachi_twohand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/tachi_twohand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/tachi_twohand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		TACHI_TWOHAND_BLOSSOM_CHARGE = new ActionAnimation(0.05F, "biped/skill/tachi_twohand_blossom_charge", biped);
		TACHI_TWOHAND_BLOSSOM_SLASHES = new BasicMultipleAttackAnimation(0.05F, "biped/skill/tachi_twohand_blossom_slashes", biped,
				new Phase(0.0F, 0.1F, 0.2F, 0.3F, 0.3F, "Tool_R", null),
				new Phase(0.3F, 0.3F, 0.4F, 0.5F, 0.5F, "Tool_R", null),
				new Phase(0.5F, 0.5F, 0.6F, 0.8F, 0.8F, "Tool_R", null),
				new Phase(0.8F, 0.8F, 0.9F, 1.7F ,Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),3)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		TACHI_TWOHAND_BLOSSOM_FINAL = new BasicAttackAnimation(0.1F, 0.05F, 0.2F, 0.3F, null, "Tool_R", "biped/skill/tachi_twohand_blossom_final", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.25F))
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		LONGSWORD_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/longsword_twohand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/longsword_twohand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/longsword_twohand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		LONGSWORD_TWOHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/combat/longsword_twohand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.35F);
		
		GREATSWORD_TWOHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/combat/greatsword_twohand_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		GREATSWORD_TWOHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/combat/greatsword_twohand_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		GREATSWORD_TWOHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.4F, 0.5F, 0.6F, null, "Tool_R", "biped/combat/greatsword_twohand_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F);
		
		AGONY_AUTO_1 = new BasicAttackAnimation(0.05F, 0.05F, 0.15F, 0.20F, null, "Tool_R", "biped/combat/agony_auto_1", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F);
		AGONY_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_2", biped,
				new Phase(0.00F, 0.4F, 0.55F, 0.6F, 0.6F, "Tool_R", null),
				new Phase(0.6F, 0.6F, 0.75F, 0.9F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		AGONY_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_3", biped,
				new Phase(0.0F, 0.25F, 0.5F, 0.55F, 0.55F, "Tool_R", null),
				new Phase(0.55F, 0.75F, 1.0F, 1.20F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.5F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		AGONY_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_4", biped,
				new Phase(0.0F, 0.10F, 0.2F, 0.25F, 0.25F, "Tool_R", null),
				new Phase(0.25F, 0.25F, 0.35F, 0.55F, 0.55F, "Tool_R", null),
				new Phase(0.55F, 0.55F, 0.7F, 0.95F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F);
		
		AGONY_CLAWSTRIKE = new BasicMultipleAttackAnimation(0.0F, "biped/combat/agony_clawstrike", biped,
				new Phase(0.0F, 0.25F, 0.5F, 0.55F, 0.55F, "Tool_R", EFColliders.AGONY_AIRSLASH),
				new Phase(0.55F, 0.85F, 1.0F, 1.20F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.0F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.3F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.7F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		AGONY_DASH = new DashAttackAnimation(0.15F, 0.05F, 0.2F, 0.458F, 0.625F, EFColliders.AGONY_AIRSLASH, "Tool_R", "biped/combat/agony_dash", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.1F)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.4F));
		AGONY_AIR_SLASH = new BasicAttackAnimation(0.0F, 0.45F, 0.6F, 0.75F, EFColliders.AGONY_AIRSLASH, "Tool_R", "biped/combat/agony_airslash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.1F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.7F, ReuseableEvents.RUINE_COMET_GROUNDTHRUST, Side.CLIENT)});;
		
		AGONY_IDLE = new StaticAnimation(0.1f,true, "biped/living/agony_idle", biped);
		AGONY_RUN = new MovementAnimation(0.1f,true, "biped/living/agony_run", biped);
		AGONY_WALK = new MovementAnimation(0.1f,true, "biped/living/agony_walk", biped);
		
		AGONY_PLUNGE_FORWARD = new SpecialAttackAnimation(0.05F, "biped/skill/agony_plunge_forward", biped,
				new Phase(0.0F, 0.166F, 0.20F, 1.0F, 1.0F, "Root", EFColliders.AGONY_PLUNGE), 
				new Phase(1.0F, 1.1F, 1.45F, 1.65F, Float.MAX_VALUE, "Root", EFColliders.AGONY_PLUNGE))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.WHOOSH_BIG,0)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10),0)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(9.0F),0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.5F),0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2f),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.15F, ReuseableEvents.AGONY_AIRBURST_JUMP, Side.CLIENT),
						Event.create(0.2F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						Event.create(0.25F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						Event.create(0.3F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						Event.create(0.35F, ReuseableEvents.AGONY_ENCHANTED_JUMP, Side.CLIENT),
						Event.create(1.3F, ReuseableEvents.AGONY_PLUNGE_GROUNDTHRUST, Side.CLIENT)});
		
		RUINE_AUTO_1 = new BasicAttackAnimation(0.20F, 0.25F, 0.45F, 0.55F, null, "Tool_R", "biped/combat/ruine_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.0F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		RUINE_AUTO_2 = new BasicMultipleAttackAnimation(0.15F, "biped/combat/ruine_auto_2", biped,
				new Phase(0.0F, 0.05F, 0.3F, 0.65F, 0.65F, "Tool_R", null),
				new Phase(0.65F, 0.65F, 0.75F, 0.80F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )				
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_AUTO_3 = new BasicMultipleAttackAnimation(0.15F, "biped/combat/ruine_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.35F, 0.4F, 0.4F, "Tool_R", null),
				new Phase(0.4F, 0.5F, 0.7F, 0.75F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.50F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.80F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD )
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F);
		
		RUINE_AUTO_4 = new BasicMultipleAttackAnimation(0.15F, "biped/combat/ruine_auto_4", biped,
				new Phase(0.0F, 0.4F, 0.5F, 0.55F, 0.55F, "Tool_R", EFColliders.RUINE_COMET),
				new Phase(0.55F, 0.8F, 1.1F, 1.55F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.80F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.70F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.8F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD )
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1 )
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.4F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT)});
				
		
		RUINE_COUNTER = new BasicMultipleAttackAnimation(0.05F, "biped/skill/ruine_counterattack", biped,
				new Phase(0.0F, 0.2F, 0.25F, 0.3F, 0.3F, "Root", EFColliders.SHOULDER_BUMP),
				new Phase(0.3F, 0.5F, 0.7F, 1.0F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(2.0F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.20F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.3F),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)			
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)			
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)			
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_DASH = new BasicAttackAnimation(0.1F, 0.15F, 0.65F, 0.75F, null, "Tool_R", "biped/combat/ruine_dash", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.0F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.4F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		RUINE_COMET = new BasicAttackAnimation(0.15F, 0.25F, 0.55F, 0.75F, EFColliders.RUINE_COMET, "Tool_R", "biped/combat/ruine_comet", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
						Event.create(0.55F, ReuseableEvents.RUINE_COMET_GROUNDTHRUST, Side.CLIENT)});
		
		RUINE_BLOCK = new StaticAnimation(0.1F, true, "biped/skill/ruine_block", biped);
		RUINE_IDLE = new StaticAnimation(0.1f,true, "biped/living/ruine_idle", biped);
		RUINE_RUN = new MovementAnimation(0.1f,true, "biped/living/ruine_run", biped);
		RUINE_WALK = new MovementAnimation(0.1f,true, "biped/living/ruine_walk", biped);
		
		RUINE_PLUNDER = new SpecialAttackAnimation(0.05F, "biped/skill/ruine_plunder", biped,
				new Phase(0.0F, 1.1F, 1.25F, 1.85F, 1.85F, "Root", EFColliders.PLUNDER_PERDITION), 
				new Phase(1.85F, 1.85F, 2.2F, 3.35F, Float.MAX_VALUE, "Root", EFColliders.PLUNDER_PERDITION))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,0)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.RUINE_PLUNDER_SWORD,0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20),0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		
		TORMENT_AUTO_1 = new BasicAttackAnimation(0.20F, 0.2F, 0.5F, 0.6F, null, "Tool_R", "biped/combat/torment_auto_1", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F);
		
		TORMENT_AUTO_2 = new BasicAttackAnimation(0.25F, 0.05F, 0.3F, 0.55F, null, "Tool_R", "biped/combat/torment_auto_2", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.2F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.9F))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.20F);
		
		TORMENT_AUTO_3 = new BasicMultipleAttackAnimation(0.25F, "biped/combat/torment_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Tool_R", null),
				new Phase(0.4F, 0.4F, 0.55F, 0.9F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.20F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT)});
	
		TORMENT_AUTO_4 = new BasicAttackAnimation(0.20F, 0.2F, 0.65F, 0.70F, null, "Tool_R", "biped/combat/torment_auto_4", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F);
		
		TORMENT_DASH = new DashAttackAnimation(0.05F, 0.25F, 0.25F, 0.5F, 0.75F, null, "Tool_R", "biped/combat/torment_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.10F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.55F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT)});
		
		TORMENT_AIRSLAM = new BasicMultipleAttackAnimation(0.15F, "biped/combat/torment_airslam", biped,
				new Phase(0.0F, 0.45F, 0.55F, 0.6F, 0.6F, "Tool_R", null),
				new Phase(0.6F, 0.6F, 0.7F, 0.8F, Float.MAX_VALUE, "Root", EFColliders.TORMENT_AIRSLAM))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.5F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT)});
		
		TORMENT_IDLE = new StaticAnimation(0.1f,true, "biped/living/torment_idle", biped);
		TORMENT_RUN = new MovementAnimation(0.1f,true, "biped/living/torment_run", biped);
		TORMENT_WALK = new MovementAnimation(0.1f,true, "biped/living/torment_walk", biped);
		
		TORMENT_BERSERK_IDLE = new StaticAnimation(true, "biped/living/torment_berserk_idle", biped);
		
		TORMENT_BERSERK_AUTO_1 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.35F, 0.65F, null, "Tool_R", "biped/skill/torment_berserk_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.OVERBLOOD_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_AUTO_2 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.55F, 0.70F, null, "Tool_R", "biped/skill/torment_berserk_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.OVERBLOOD_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_DASH = new BasicMultipleAttackAnimation(0.15F, "biped/skill/torment_berserk_dash", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, "Root", EFColliders.TORMENT_BERSERK_DASHSLAM),
				new Phase(0.35F, 0.5F, 0.6F, 0.65F, 0.65F, "Root", EFColliders.TORMENT_BERSERK_DASHSLAM),
				new Phase(0.65F, 0.9F, 1.0F, 1.2F, Float.MAX_VALUE, "Root", EFColliders.TORMENT_BERSERK_DASHSLAM))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F),2)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.4F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT),
						Event.create(0.7F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT),
						Event.create(1.15F, ReuseableEvents.TORMENT_GROUNDSLAM_SMALL, Side.CLIENT)});
		
		TORMENT_BERSERK_AIRSLAM = new BasicMultipleAttackAnimation(0.15F, 0.4F, 0.7F, 1.2F, EFColliders.TORMENT_BERSERK_AIRSLAM, "Root", "biped/skill/torment_berserk_airslam", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.GROUND_SLAM)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.0F, (entitypatch) -> {
							if (entitypatch instanceof PlayerPatch) {
								((PlayerPatch)entitypatch).setStamina(((PlayerPatch)entitypatch).getStamina() - 2.0f);
							}
						}, Side.CLIENT),
						Event.create(0.6F, ReuseableEvents.TORMENT_GROUNDSLAM, Side.CLIENT)
						});
		
		TORMENT_BERSERK_CONVERT = new BasicAttackAnimation(0.05F, 0.6F, 1.35F, 1.7F, EFColliders.PLUNDER_PERDITION, "Root", "biped/skill/torment_berserk_convert", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.ENDERMAN_SCREAM)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.CHAIN_BREAK)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(8.0F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_RUN = new MovementAnimation(0.1f,true, "biped/living/torment_berserk_run", biped);
		TORMENT_BERSERK_WALK = new MovementAnimation(0.1f,true, "biped/living/torment_berserk_walk", biped);
		
		KATANA_SHEATHED_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_sheathed_auto_1", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.25F, 0.3F, 0.40F, 0.55F, Float.MAX_VALUE, "Root", EFColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.25F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT), Event.create(0.35F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT), Event.create(0.85F, (entitypatch) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Event.Side.SERVER)});
		
		KATANA_SHEATHED_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_sheathed_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.25F, 0.3F, 0.39F, 0.40F, 0.40F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.40F, 0.5F, 0.59F, 0.7F, Float.MAX_VALUE, "Root", EFColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.25F, (entitypatch) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT), Event.create(0.35F, (entitypatch) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT), Event.create(0.50F, (entitypatch) -> {
						Entity entity = entitypatch.getOriginal();
						entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
					}, Side.CLIENT), Event.create(1.05F, (entitypatch) -> {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
							entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
						}
						entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
					}, Event.Side.SERVER)});
		
		
		KATANA_SHEATHED_AUTO_3 = new BasicAttackAnimation(0.15F, 0.05F, 0.25F, 0.50F, EFColliders.KATANA_SHEATHED_AUTO, "Root", "biped/combat/katana_sheathed_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.1F))
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(3F))
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.25F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT), Event.create(0.80F, (entitypatch) -> {
					((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Event.Side.SERVER)});
		
		KATANA_SHEATHED_DASH = new BasicMultipleAttackAnimation(0.10F, "biped/combat/katana_sheathed_dash", biped,
				new Phase(0.0F, 0.15F, 0.4F, 0.7F, 0.7F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.7F, 0.7F, 0.80F, 0.9F, 0.9F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(0.90F, 0.90F, 0.95F, 1.0F, 1.0F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.0F, 1.0F, 1.10F, 1.15F, 1.15F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.15F, 1.15F, 1.25F, 1.35F, 1.35F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.35F, 1.35F, 1.45F, 1.5F, Float.MAX_VALUE, "Root", EFColliders.KATANA_SHEATHED_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),3)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),4)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),5)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,4)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,5)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,3)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,4)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,5)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.0F, (entitypatch) -> {
					if (entitypatch instanceof PlayerPatch) {
						((PlayerPatch)entitypatch).setStamina(((PlayerPatch)entitypatch).getStamina() - 4.0f);
					}
				}, Side.CLIENT),Event.create(0.15F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT), Event.create(0.8F, (entitypatch) -> {
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Event.Side.SERVER)});
		
		KATANA_SHEATHED_COUNTER = new BasicMultipleAttackAnimation(0.1F, "biped/combat/katana_sheathed_dash", biped,
				new Phase(0.0F, 0.15F, 0.4F, 0.7F, 0.7F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.7F, 0.7F, 0.80F, 0.80F, Float.MAX_VALUE, "Root", EFColliders.KATANA_SHEATHED_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(8.0F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.15F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT), Event.create(0.8F, (entitypatch) -> {
					if (entitypatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill() != null) {
						((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(EFKatanaPassive.SHEATH, true,(ServerPlayer)entitypatch.getOriginal());
						entitypatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getPassiveSkill().setConsumption(((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE), 3);
					}
					entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
				}, Event.Side.SERVER)});
		
		KATANA_SHEATHE = new StaticAnimation(0.1f,false, "biped/living/katana_sheathe", biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.2F, ReuseableEvents.KATANA_IN, Event.Side.CLIENT)});
		
		KATANA_SHEATHED_IDLE = new StaticAnimation(0.1f,true, "biped/living/katana_sheathed_idle", biped);
		KATANA_SHEATHED_RUN = new MovementAnimation(0.1f,true, "biped/living/katana_sheathed_run", biped);

		KATANA_AUTO_1 = new BasicAttackAnimation(0.1F, 0.00F, 0.2F, 0.25F, null, "Tool_R", "biped/combat/katana_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Tool_R", null),
				new Phase(0.4F, 0.4F, 0.65F, 0.65F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F),0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_3 = new BasicAttackAnimation(0.05F, 0.25F, 0.4F, 0.75F, null, "Tool_R", "biped/combat/katana_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		
		KATANA_DASH = new BasicAttackAnimation(0.05F, 0.4F, 0.7F, 1.05F, null, "Tool_R", "biped/combat/katana_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.3F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(4.2F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true);
		
		KATANA_IDLE = new StaticAnimation(0.1f,true, "biped/living/katana_idle", biped);
		
		KATANA_FATAL_DRAW = new SpecialAttackAnimation(0.15F, 0.0F, 0.55F, 0.70F, 1.0F, EFColliders.FATAL_DRAW, "Root", "biped/skill/katana_fatal_draw", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)});
		
		KATANA_FATAL_DRAW_SECOND = new SpecialAttackAnimation(0.15F, 0.0F, 0.50F, 0.70F, 1.0F, EFColliders.FATAL_DRAW, "Root", "biped/skill/katana_fatal_draw_second", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)});
		
		KATANA_FATAL_DRAW_DASH = new SpecialAttackAnimation(0.15F, 0.35F, 0.50F, 0.70F, 1.05F, EFColliders.FATAL_DRAW_DASH, "Root", "biped/skill/katana_fatal_draw_dash", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.KATANA_SHEATHED_HIT)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)})
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.6F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT)});
		
		
		ENDERBLASTER_ONEHAND_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_1", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Leg_L", EFColliders.KICK),
				new Phase(0.25F, 0.3F, 0.34F, 0.35F, Float.MAX_VALUE, "Leg_L", EFColliders.KICK))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.24F, 0.25F, 0.25F, "Leg_R", EFColliders.KICK),
				new Phase(0.25F, 0.3F, 0.44F, 0.45F, 0.45F, "Tool_R", null),
				new Phase(0.45F, 0.5F, 0.54F, 0.55F, Float.MAX_VALUE, "Elbow_L", EFColliders.KNEE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_3 = new BasicMultipleAttackAnimation(0.10F, "biped/combat/enderblaster_onehand_auto_3", biped,
				new Phase(0.0F, 0.05F, 0.19F, 0.2F, 0.2F, "Tool_R", null),
				new Phase(0.2F, 0.25F, 0.35F, 0.35F, Float.MAX_VALUE, "Tool_L", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_4", biped,
				new Phase(0.0F, 0.3F, 0.4F, 0.55F, Float.MAX_VALUE, "Leg_L", EFColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.00F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_DASH = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_dash", biped,
				new Phase(0.0F, 0.15F, 0.3F, 0.4F, 0.4F, "Leg_L", EFColliders.KICK_HUGE),
				new Phase(0.4F, 0.4F, 0.75F, 0.85F, Float.MAX_VALUE, "Leg_L", EFColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(4.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false);
		
		ENDERBLASTER_ONEHAND_JUMPKICK = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_jumpkick", biped,
				new Phase(0.0F, 0.05F, 0.24F, 0.25F, 0.25F, "Leg_L", EFColliders.KICK_HUGE),
				new Phase(0.25F, 0.3F, 0.40F, 0.50F, 0.50F, "Leg_R", EFColliders.KICK_HUGE),
				new Phase(0.50F, 0.55F, 0.7F, 0.80F, Float.MAX_VALUE, "Leg_R", EFColliders.KICK_HUGE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.50F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false);
		
		ENDERBLASTER_ONEHAND_SHOOT_1 = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_1", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.2F, 0.2F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.2F, 0.25F, 0.35F, 0.55F, 0.55F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.55F, 0.6F, 0.7F, 0.75F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.35F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.6F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_2 = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.35F, 0.4F, 0.5F, 0.65F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_2_FORWARD = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_forward", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.35F, 0.4F, 0.5F, 0.65F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_2_LEFT = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_left", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, "Tool_R", EFColliders.ENDER_BULLET_WIDE),
				new Phase(0.35F, 0.4F, 0.5F, 0.65F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET_WIDE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_2_RIGHT = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_2_right", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.35F, 0.35F, "Tool_R", EFColliders.ENDER_BULLET_WIDE),
				new Phase(0.35F, 0.4F, 0.5F, 0.65F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET_WIDE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_3 = new SpecialAttackAnimation(0.05F, 0.7F, 0.7F, 0.75F, 1.2F, EFColliders.ENDER_BULLET, "Tool_R", "biped/skill/enderblaster_onehand_shoot_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(2.5F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.00F, (entitypatch) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
					}
				}, Side.CLIENT),Event.create(0.75F, (entitypatch) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1F);
					}
					OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
					OpenMatrix4f transformMatrix3 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
					transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrix3.translate(new Vec3f(0.0f,-2F,-0.3F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix3,transformMatrix3);
					HitResult ray = entitypatch.getOriginal().pick(10.D, 0.7F, false);
					for (int i = 0; i < 20; i++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
							(transformMatrix.m30 + entitypatch.getOriginal().getX()),
							(transformMatrix.m31 + entitypatch.getOriginal().getY()),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
							(transformMatrix3.m30 + (new Random().nextFloat() - 0.5F)*2),
							(transformMatrix3.m31 + (new Random().nextFloat() - 0.5F)*2)-1,
							(transformMatrix3.m32 + (new Random().nextFloat() - 0.5F)*2));
					}
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrix.m30 + entitypatch.getOriginal().getX()),
							(transformMatrix.m31 + entitypatch.getOriginal().getY()),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y),
							(ray.getLocation().z));
				}, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_AIRSHOOT = new SpecialAttackAnimation(0.15F, "biped/skill/enderblaster_onehand_airshoot", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.25F, 0.25F, "Tool_R", EFColliders.ENDER_BULLET_DASH),
				new Phase(0.25F, 0.25F, 0.35F, 0.4F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.15F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.3F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_SHOOT_DASH = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_onehand_shoot_dash", biped,
				new Phase(0.0F, 0.0F, 0.25F, 0.5F, 0.5F, "Root", EFColliders.ENDER_DASH),
				new Phase(0.5F, 0.5F, 0.6F, 0.70F, 0.70F, "Tool_R", EFColliders.ENDER_BULLET_DASH),
				new Phase(0.70F, 0.75F, 0.85F, 1F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(6.0F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_BIG)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.6F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.85F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_ONEHAND_IDLE = new StaticAnimation(0.1f,true, "biped/living/enderblaster_onehand_idle", biped);
		ENDERBLASTER_ONEHAND_WALK = new MovementAnimation(0.1f,true, "biped/living/enderblaster_onehand_walk", biped);
		ENDERBLASTER_ONEHAND_RUN = new MovementAnimation(0.1f,true, "biped/living/enderblaster_onehand_run", biped);
		
		ENDERBLASTER_TWOHAND_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_1", biped,
				new Phase(0.0F, 0.10F, 0.20F, 0.3F, 0.3F, InteractionHand.OFF_HAND, "Leg_L", EFColliders.KICK),
				new Phase(0.3F, 0.3F, 0.4F, 0.4F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Leg_L", EFColliders.KICK))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.20F, 0.25F, 0.25F, "Leg_R", EFColliders.KICK),
				new Phase(0.25F, 0.30F, 0.40F, 0.45F, 0.45F, "Tool_R", null),
				new Phase(0.45F, 0.5F, 0.6F, 0.6F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Elbow_L", EFColliders.KNEE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_3 = new BasicMultipleAttackAnimation(0.10F, "biped/combat/enderblaster_onehand_auto_3", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.2F, 0.2F, "Tool_R", null),
				new Phase(0.2F, 0.25F, 0.35F, 0.35F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Tool_L", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_AUTO_4 = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.55F, EFColliders.KICK_HUGE, "Leg_L", "biped/combat/enderblaster_onehand_auto_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.00F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_TWOHAND_TISHNAW = new BasicAttackAnimation(0.05F, 0.3F, 0.5F, 0.65F, EFColliders.ENDER_TISHNAW, "Leg_R", "biped/combat/enderblaster_twohand_tishnaw", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(3.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.3F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
						Event.create(0.50F, ReuseableEvents.GROUND_BODYSCRAPE_LAND, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_TOMAHAWK = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_twohand_dash", biped,
				new Phase(0.0F, 0.3F, 0.44F, 0.45F, 0.45F, "Leg_L", EFColliders.KICK_HUGE),
				new Phase(0.45F, 0.5F, 0.6F, 0.65F, Float.MAX_VALUE, "Root", EFColliders.TORMENT_AIRSLAM ))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.3F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(5.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(5.0F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.45F, ReuseableEvents.GROUND_BODYSCRAPE_LAND, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_SHOOT_1 = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_1", biped,
				new Phase(0.0F, 0.05F, 0.15F, 0.30F, 0.30F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.30F, 0.35F, 0.4F, 0.55F, 0.55F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.55F, 0.6F, 0.65F, 0.70F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.1F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.4F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.65F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_SHOOT_2 = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_2", biped,
				new Phase(0.0F, 0.4F, 0.44F, 0.45F, 0.45F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.45F, 0.5F, 0.54F, 0.55F, 0.55F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.55F, 0.6F, 0.64F, 0.65F, 0.65F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.65F, 0.7F, 0.74F, 0.75F, 0.75F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.75F, 0.8F, 0.84F, 0.85F, 0.85F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.85F, 0.9F, 0.94F, 0.95F, 0.95F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.95F, 1.0F, 1.04F, 1.05F, 1.05F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(1.05F, 1.1F, 1.14F, 1.2F, Float.MAX_VALUE, "Tool_R", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),4)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,4)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,4)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,4)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),5)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,5)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,5)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,5)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),6)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,6)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,6)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,6)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.5F),7)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,7)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,7)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,7)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.45F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.55F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.65F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.75F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.85F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.95F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(1.05F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(1.15F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_SHOOT_3 = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_shoot_3", biped,
				new Phase(0.0F, 0.25F, 0.35F, 0.5F, 0.5F, InteractionHand.OFF_HAND, "Head", EFColliders.ENDER_BULLET_WIDE),
				new Phase(0.5F, 0.5F, 0.6F, 0.75F, 0.75F, "Head", EFColliders.ENDER_BULLET_WIDE),
				new Phase(0.75F, 0.75F, 0.85F, 0.9F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Head", EFColliders.ENDER_BULLET_WIDE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.0F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.85F),2)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2.0F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.25F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT),
						Event.create(0.55F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT),
						Event.create(0.80F, ReuseableEvents.SHOTGUN_SHOOT_BOTH, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_SHOOT_4 = new SpecialAttackAnimation(0.05F, 0.35F, 0.35F, 0.45F, 0.8F, EFColliders.ENDER_BULLET, "Tool_R", "biped/skill/enderblaster_twohand_shoot_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(4.0F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.00F, (entitypatch) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
					}
				}, Side.CLIENT),Event.create(0.35F, (entitypatch) -> {
					if (entitypatch instanceof PlayerPatch) {
						entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1F);
					}
					HitResult ray = entitypatch.getOriginal().pick(10.D, 0.7F, false);
					
					OpenMatrix4f transformMatrixl = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
					OpenMatrix4f transformMatrixl2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
					transformMatrixl.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrixl2.translate(new Vec3f(0.0f,-2F,-0.3F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixl,transformMatrixl);
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixl2,transformMatrixl2);
					for (int i = 0; i < 20; i++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
							(transformMatrixl.m30 + entitypatch.getOriginal().getX()),
							(transformMatrixl.m31 + entitypatch.getOriginal().getY()),
							(transformMatrixl.m32 + entitypatch.getOriginal().getZ()),
							(transformMatrixl2.m30 + (new Random().nextFloat() - 0.5F)*2),
							(transformMatrixl2.m31 + (new Random().nextFloat() - 0.5F)*2)-1,
							(transformMatrixl2.m32 + (new Random().nextFloat() - 0.5F)*2));
					}
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrixl.m30 + entitypatch.getOriginal().getX()),
							(transformMatrixl.m31 + entitypatch.getOriginal().getY()),
							(transformMatrixl.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y-2),
							(ray.getLocation().z));
					
					OpenMatrix4f transformMatrixr = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
					OpenMatrix4f transformMatrixr2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
					transformMatrixr.translate(new Vec3f(0,-0.6F,-0.3F));
					transformMatrixr2.translate(new Vec3f(0.0f,-2F,-0.3F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixr,transformMatrixr);
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0)),transformMatrixr2,transformMatrixr2);
					for (int i = 0; i < 20; i++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
								(transformMatrixr.m30 + entitypatch.getOriginal().getX()),
								(transformMatrixr.m31 + entitypatch.getOriginal().getY()),
								(transformMatrixr.m32 + entitypatch.getOriginal().getZ()),
								(transformMatrixr2.m30 + (new Random().nextFloat() - 0.5F)*2),
								(transformMatrixr2.m31 + (new Random().nextFloat() - 0.5F)*2)-1,
								(transformMatrixr2.m32 + (new Random().nextFloat() - 0.5F)*2));
					}
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(),
							(transformMatrixr.m30 + entitypatch.getOriginal().getX()),
							(transformMatrixr.m31 + entitypatch.getOriginal().getY()),
							(transformMatrixr.m32 + entitypatch.getOriginal().getZ()),
							(ray.getLocation().x),
							(ray.getLocation().y-2),
							(ray.getLocation().z));
				}, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_AIRSHOOT = new SpecialAttackAnimation(0.05F, "biped/skill/enderblaster_twohand_airshoot", biped,
				new Phase(0.0F, 0.4F, 0.44F, 0.45F, 0.45F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.45F, 0.50F, 0.54F, 0.55F, 0.55F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.55F, 0.6F, 0.64F, 0.65F, 0.65F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.65F, 0.7F, 0.74F, 0.75F, 0.75F, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET),
				new Phase(0.75F, 0.8F, 0.84F, 0.85F, 0.85F, "Tool_R", EFColliders.ENDER_BULLET),
				new Phase(0.85F, 0.9F, 0.94F, 0.95F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Tool_L", EFColliders.ENDER_BULLET))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),4)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,4)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,4)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,4)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),5)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,5)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,5)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,5)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.45F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.55F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.65F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.75F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT),
						Event.create(0.85F, ReuseableEvents.SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.95F, ReuseableEvents.SHOOT_LEFT, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_PISTOLERO = new SpecialAttackAnimation(0.1F, "biped/skill/enderblaster_twohand_shoot_dash", biped,
				new Phase(0.0F, 0.25F, 0.29F, 0.3F, 0.3F, "Root", EFColliders.ENDER_PISTOLERO),
				new Phase(0.3F, 0.35F, 0.39F, 0.4F, 0.4F, InteractionHand.OFF_HAND, "Root", EFColliders.ENDER_PISTOLERO),
				new Phase(0.4F, 0.45F, 0.49F, 0.5F, 0.5F, "Root", EFColliders.ENDER_PISTOLERO),
				new Phase(0.5F, 0.55F, 0.6F, 1.1F, Float.MAX_VALUE, InteractionHand.OFF_HAND, "Root", EFColliders.ENDER_PISTOLERO))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),2)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.DAMAGE,ValueCorrector.multiplier(0.6F),3)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT,3)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.ROTATE_X, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.3F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.4F, ReuseableEvents.SHOTGUN_SHOOT_LEFT, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.SHOTGUN_SHOOT_RIGHT, Side.CLIENT),
						Event.create(0.6F, ReuseableEvents.SHOTGUN_SHOOT_LEFT, Side.CLIENT)});
		
		ENDERBLASTER_TWOHAND_IDLE = new StaticAnimation(0.1f,true, "biped/living/enderblaster_twohand_idle", biped);
		
		STAFF_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_1", biped,
				new Phase(0.0F, 0.1F, 0.19F, 0.2F, 0.2F, "Tool_R", null),
				new Phase(0.2F, 0.25F, 0.34F, 0.35F, 0.35F, "Tool_R", null),
				new Phase(0.35F, 0.4F, 0.49F, 0.50F, 0.50F, "Tool_R", null),
				new Phase(0.5F, 0.55F, 0.7F, 0.7F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.40F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.40F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.40F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.40F),3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_2", biped,
				new Phase(0.0F, 0.15F, 0.25F, 0.35F, 0.35F, "Tool_R", null),
				new Phase(0.35F, 0.35F, 0.45F, 0.55F, 0.55F, "Tool_R", null),
				new Phase(0.55F, 0.55F, 0.65F, 0.65F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.60F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.60F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.60F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_auto_3", biped,
				new Phase(0.0F, 0.15F, 0.45F, 0.65F, 0.65F, "Tool_R", null),
				new Phase(0.65F, 0.65F, 0.85F, 0.85F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.20F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.20F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_DASH = new BasicMultipleAttackAnimation(0.05F, "biped/combat/staff_dash", biped,
				new Phase(0.0F, 0.15F, 0.25F, 0.3F, 0.3F, "Tool_R", null),
				new Phase(0.30F, 0.30F, 0.40F, 0.50F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.40F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.3F);
		
		STAFF_CHARYBDIS = new SpecialAttackAnimation(0.1F, "biped/skill/staff_charybdisandscylla", biped,
				new Phase(0.0F, 0.1F, 0.14F, 0.15F, 0.15F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.15F, 0.2F, 0.24F, 0.25F, 0.25F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.25F, 0.3F, 0.34F, 0.35F, 0.35F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.35F, 0.4F, 0.44F, 0.45F, 0.45F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.45F, 0.55F, 0.59F, 0.6F, 0.6F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.6F, 0.65F, 0.69F, 0.7F, 0.7F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.7F, 0.75F, 0.79F, 0.8F, 0.8F, "Tool_R", EFColliders.STAFF_CHARYBDIS),
				new Phase(0.8F, 0.85F, 0.89F, 0.9F, Float.MAX_VALUE, "Tool_R", EFColliders.STAFF_CHARYBDIS))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),3)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,3)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),4)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,4)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),5)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,5)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),6)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,6)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),7)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(1.7F),7)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,7)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,3)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,4)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,5)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,6)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,7)
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.65F);
		
		
		
		STAFF_IDLE = new StaticAnimation(0.1f,true, "biped/living/staff_idle", biped);
		STAFF_RUN = new MovementAnimation(0.1f,true, "biped/living/staff_run", biped);
		
		ANTITHEUS_AGRESSION = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_agression", biped,
				new Phase(0.0F, 0.25F, 0.4F, 0.5F, 0.5F, "Tool_R", null),
				new Phase(0.5F, 0.6F, 0.8F, 0.85F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.adder(-1),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)		
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, ReuseableEvents.RUINE_COMET_AIRBURST, Side.CLIENT),
						Event.create(0.15F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.45F, ReuseableEvents.RUINE_COMET_GROUNDTHRUST, Side.CLIENT),
						Event.create(0.8F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER)});
		
		ANTITHEUS_GUILLOTINE = new BasicMultipleAttackAnimation(0.00F, "biped/combat/antitheus_guillotine", biped,
				new Phase(0.0F, 0.6F, 0.8F, 0.8F, 0.8F, "Root", EFColliders.ANTITHEUS_GUILLOTINE),
				new Phase(0.8F, 0.9F, 1.0F, 1.1F, Float.MAX_VALUE, "Root", EFColliders.ANTITHEUS_GUILLOTINE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.95F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.1F),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT,1)				
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
						Event.create(0.5F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(1.0F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER),
						});
		
		ANTITHEUS_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_auto_1", biped,
				new Phase(0.0F, 0.35F, 0.6F, 0.6F, 0.6F, "Tool_R", null),
				new Phase(0.6F, 0.70F, 0.9F, 0.9F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
						Event.create(0.25F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.9F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER),
						});
		
		ANTITHEUS_AUTO_2 = new BasicAttackAnimation(0.05F, 0.20F, 0.4F, 0.4F,null, "Tool_R", "biped/combat/antitheus_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.1F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.4F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER),
						});
		
		ANTITHEUS_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/antitheus_auto_3", biped,
				new Phase(0.0F, 0.3F, 0.55F, 0.55F, 0.55F, "Tool_R", null),
				new Phase(0.55F, 0.65F, 0.75F, 0.80F, Float.MAX_VALUE, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)				
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT,1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.adder(-1),1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG, 1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.35F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.75F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER),
						});
		
		ANTITHEUS_AUTO_4 = new BasicAttackAnimation(0.05F, 0.55F, 0.75F, 0.9F, null, "Tool_R", "biped/combat/antitheus_auto_4", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.8F)
				.addProperty(AttackAnimationProperty.COLLIDER_ADDER, 2)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.55F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.75F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER),
						});
		
		ANTITHEUS_IDLE = new StaticAnimation(0.2f,true, "biped/living/antitheus_idle", biped);
		ANTITHEUS_RUN = new MovementAnimation(0.2f,true, "biped/living/antitheus_run", biped);
		ANTITHEUS_WALK = new MovementAnimation(0.2f,true, "biped/living/antitheus_walk", biped);
		
		ANTITHEUS_ASCENSION = new SpecialAttackAnimation(0.1f, "biped/skill/antitheus_ascension", biped,
				new Phase(0.0f, 0.5f, 0.6f, 0.6f, 0.6f, "Root", EFColliders.PLUNDER_PERDITION),
				new Phase(0.6f, 1.75f, 2.05f, 2.85f, Float.MAX_VALUE, "Root", EFColliders.PLUNDER_PERDITION))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(4.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20))
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT,1)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.5F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 0.5F);
						}, Side.CLIENT),
						Event.create(1.75F, (entitypatch) -> {
							((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.ACTIVE, true, (ServerPlayer)entitypatch.getOriginal());
							((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(DemonicAscensionSkill.ACTIVE, true, (ServerPlayer)entitypatch.getOriginal());
							((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(DemonicAscensionSkill.ASCENDING, true, (ServerPlayer)entitypatch.getOriginal());
							entitypatch.getOriginal().level.playSound(null, entitypatch.getOriginal(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 1.0F, 0.5F);
							entitypatch.getOriginal().level.playSound(null, entitypatch.getOriginal(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 0.5F);
						}, Side.SERVER)});
		
		ANTITHEUS_ASCENDED_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.4F, EFColliders.ANTITHEUS_ASCENDED_PUNCHES, "Root", "biped/skill/antitheus_ascended_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.9F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT)});
		
		ANTITHEUS_ASCENDED_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/skill/antitheus_ascended_auto_2", biped,
				new Phase(0.0F, 0.3F, 0.4F, 0.4F, 0.4F, "Root", EFColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.4F, 0.6F, 0.7F, 0.7F, Float.MAX_VALUE, "Root", EFColliders.ANTITHEUS_ASCENDED_PUNCHES))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.7F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT)});
		
		ANTITHEUS_ASCENDED_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/skill/antitheus_ascended_auto_3", biped,
				new Phase(0.0F, 0.2F, 0.3F, 0.3F, 0.3F, "Root", EFColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.3F, 0.4F, 0.5F, 0.5F, 0.5F, "Root", EFColliders.ANTITHEUS_ASCENDED_PUNCHES),
				new Phase(0.5F, 0.7F, 0.85F, 0.85F, Float.MAX_VALUE, "Root", EFColliders.ANTITHEUS_ASCENDED_PUNCHES))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.9F),2)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,1)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)	
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT)});
		
		ANTITHEUS_ASCENDED_DEATHFALL = new BasicMultipleAttackAnimation(0.05F, 0.35F, 0.4F, 0.6F, EFColliders.ANTITHEUS_GUILLOTINE, "Root", "biped/skill/antitheus_ascended_deathfall", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10.0F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_PUNCH_HIT)		
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)		
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(ActionAnimationProperty.MOVE_VERTICAL, true)
				.addProperty(ActionAnimationProperty.STOP_MOVEMENT, false)
				.addProperty(ActionAnimationProperty.CANCELABLE_MOVE, false)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
						Event.create(0.30F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
						Event.create(0.35F, (entitypatch) -> {
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
						}, Side.CLIENT)});
		
		ANTITHEUS_ASCENDED_BLINK = new BasicMultipleAttackAnimation(0.05F, 0.3F, 0.4F, 0.4F, EFColliders.ANTITHEUS_ASCENDED_BLINK, "Root", "biped/skill/antitheus_ascended_blink", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(0.8F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.PARTICLE, EFEpicFightParticles.ANTITHEUS_HIT)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)	
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT)	
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.9F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {
						Event.create(0.05F, (entitypatch) -> {
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.7F, 0.7F);
							entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS, 1.0F, 1.0F);
						}, Side.CLIENT),
						Event.create(0.30F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_ON, Side.SERVER),
						Event.create(0.45F, ReuseableEvents.ANTITHEUS_WEAPON_TRAIL_OFF, Side.SERVER)});
		
		ANTITHEUS_ASCENDED_IDLE = new StaticAnimation(0.1f,true, "biped/living/antitheus_ascended_idle", biped);
		ANTITHEUS_ASCENDED_RUN = new MovementAnimation(0.1f,true, "biped/living/antitheus_ascended_run", biped);
		ANTITHEUS_ASCENDED_WALK = new MovementAnimation(0.1f,true, "biped/living/antitheus_ascended_walk", biped);
		
	}
	
	private static class ReuseableEvents {
		private static final Consumer<LivingEntityPatch<?>> KATANA_IN = (entitypatch) -> entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
		private static final Consumer<LivingEntityPatch<?>> SHOOT_RIGHT = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			OpenMatrix4f transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-2F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			for (int i = 0; i < 20; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrix2.m30 - transformMatrix.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m31 - transformMatrix.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m32 - transformMatrix.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		private static final Consumer<LivingEntityPatch<?>> SHOTGUN_SHOOT_RIGHT = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound(null, entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			OpenMatrix4f transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-5F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrix2.m30 - transformMatrix.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m31 - transformMatrix.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m32 - transformMatrix.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		private static final Consumer<LivingEntityPatch<?>> SHOOT_LEFT = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-2F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			for (int i = 0; i < 20; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrix2.m30 - transformMatrix.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m31 - transformMatrix.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m32 - transformMatrix.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		private static final Consumer<LivingEntityPatch<?>> SHOTGUN_SHOOT_LEFT = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			transformMatrix.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrix2.translate(new Vec3f(0.0f,-5F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrix,transformMatrix);
			OpenMatrix4f.mul(CORRECTION,transformMatrix2,transformMatrix2);
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrix.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrix2.m30 - transformMatrix.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m31 - transformMatrix.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrix2.m32 - transformMatrix.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		private static final Consumer<LivingEntityPatch<?>> SHOOT_BOTH = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 1.5F);
			}
			OpenMatrix4f transformMatrixl = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrixl2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrixr = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrixr2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
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
		private static final Consumer<LivingEntityPatch<?>> SHOTGUN_SHOOT_BOTH = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.6F, 0.7F);
			}
			OpenMatrix4f transformMatrixl = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrixl2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_L");
			OpenMatrix4f transformMatrixr = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			OpenMatrix4f transformMatrixr2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
			transformMatrixl.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixl2.translate(new Vec3f(0.0f,-5F,-0.3F));
			transformMatrixr.translate(new Vec3f(0,-0.6F,-0.3F));
			transformMatrixr2.translate(new Vec3f(0.0f,-5F,-0.3F));
			OpenMatrix4f CORRECTION = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yRotO + 180F), new Vec3f(0, 1, 0));
			OpenMatrix4f.mul(CORRECTION,transformMatrixl,transformMatrixl);
			OpenMatrix4f.mul(CORRECTION,transformMatrixl2,transformMatrixl2);
			OpenMatrix4f.mul(CORRECTION,transformMatrixr,transformMatrixr);
			OpenMatrix4f.mul(CORRECTION,transformMatrixr2,transformMatrixr2);
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
					(transformMatrixl.m30 + entitypatch.getOriginal().getX()),
					(transformMatrixl.m31 + entitypatch.getOriginal().getY()),
					(transformMatrixl.m32 + entitypatch.getOriginal().getZ()),
					((transformMatrixl2.m30 - transformMatrixl.m30) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrixl2.m31 - transformMatrixl.m31) + (new Random().nextFloat() - 0.5F)*2),
					((transformMatrixl2.m32 - transformMatrixl.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
			for (int i = 0; i < 60; i++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.REVERSE_PORTAL,
						(transformMatrixr.m30 + entitypatch.getOriginal().getX()),
						(transformMatrixr.m31 + entitypatch.getOriginal().getY()),
						(transformMatrixr.m32 + entitypatch.getOriginal().getZ()),
						((transformMatrixr2.m30 - transformMatrixr.m30) + (new Random().nextFloat() - 0.5F)*2),
						((transformMatrixr2.m31 - transformMatrixr.m31) + (new Random().nextFloat() - 0.5F)*2),
						((transformMatrixr2.m32 - transformMatrixr.m32) + (new Random().nextFloat() - 0.5F)*2));
			}
		};
		
		private static final Consumer<LivingEntityPatch<?>> AGONY_AIRBURST_JUMP = (entitypatch) -> {
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
		private static final Consumer<LivingEntityPatch<?>> AGONY_ENCHANTED_JUMP = (entitypatch) -> {
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
		private static final Consumer<LivingEntityPatch<?>> AGONY_PLUNGE_GROUNDTHRUST = (entitypatch) -> {
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
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
		
		private static final Consumer<LivingEntityPatch<?>> TORMENT_GROUNDSLAM = (entitypatch) -> {
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
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
		private static final Consumer<LivingEntityPatch<?>> TORMENT_GROUNDSLAM_SMALL = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 1.0F, 0.5F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
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
		
		private static final Consumer<LivingEntityPatch<?>> RUINE_COMET_GROUNDTHRUST = (entitypatch) -> {
			if (entitypatch instanceof PlayerPatch) {
				entitypatch.getOriginal().level.playSound((Player)entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BLUNT_HIT_HARD, SoundSource.PLAYERS, 0.7F, 0.7F);
			}
			OpenMatrix4f transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(0.1F), entitypatch.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature(), "Tool_R");
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
		private static final Consumer<LivingEntityPatch<?>> RUINE_COMET_AIRBURST = (entitypatch) -> {
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
		
		private static final Consumer<LivingEntityPatch<?>> GROUND_BODYSCRAPE_LAND = (entitypatch) -> {
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
		private static final Consumer<LivingEntityPatch<?>> GROUND_BODYSCRAPE_TRAIL = (entitypatch) -> {
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
		
		private static final Consumer<LivingEntityPatch<?>> ANTITHEUS_WEAPON_TRAIL_ON = (entitypatch) -> {
			((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.BASIC_ATTACK, true, (ServerPlayer)entitypatch.getOriginal());
		};
		
		private static final Consumer<LivingEntityPatch<?>> ANTITHEUS_WEAPON_TRAIL_OFF = (entitypatch) -> {
			((PlayerPatch<?>) entitypatch).getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.BASIC_ATTACK, false, (ServerPlayer)entitypatch.getOriginal());
		};
	}
}
