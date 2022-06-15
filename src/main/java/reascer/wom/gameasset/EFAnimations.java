package reascer.wom.gameasset;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import reascer.wom.animation.BasicMultipleAttackAnimation;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.skill.EFKatanaPassive;
import yesman.epicfight.api.animation.property.Property.AttackAnimationProperty;
import yesman.epicfight.api.animation.property.Property.AttackPhaseProperty;
import yesman.epicfight.api.animation.property.Property.StaticAnimationProperty;
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
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.game.ExtendedDamageSource.StunType;
import yesman.epicfight.api.utils.math.ValueCorrector;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.particle.EpicFightParticles;
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
	
	public static StaticAnimation SWORD_ONEHAND_AUTO_1;
	public static StaticAnimation SWORD_ONEHAND_AUTO_2;
	public static StaticAnimation SWORD_ONEHAND_AUTO_3;
	public static StaticAnimation SWORD_ONEHAND_AUTO_4;
	
	public static StaticAnimation AGONY_AUTO_1;
	public static StaticAnimation AGONY_AUTO_2;
	public static StaticAnimation AGONY_AUTO_3;
	public static StaticAnimation AGONY_HEAVY_1;
	public static StaticAnimation AGONY_HEAVY_2;
	public static StaticAnimation AGONY_HEAVY_3;
	public static StaticAnimation AGONY_HEAVY_CHARGE_1;
	public static StaticAnimation AGONY_HEAVY_CHARGE_2;
	public static StaticAnimation AGONY_HEAVY_CHARGE_3;
	public static StaticAnimation AGONY_AIR_SLASH;
	public static StaticAnimation AGONY_DASH;
	public static StaticAnimation AGONY_IDLE;
	public static StaticAnimation AGONY_RUN;
	public static StaticAnimation AGONY_WALK;
	public static StaticAnimation AGONY_PLUNGE;
	
	public static StaticAnimation RUINE_AUTO_1;
	public static StaticAnimation RUINE_AUTO_2;
	public static StaticAnimation RUINE_AUTO_3;
	public static StaticAnimation RUINE_AIR_SLASH;
	public static StaticAnimation RUINE_DASH;
	public static StaticAnimation RUINE_IDLE;
	public static StaticAnimation RUINE_RUN;
	public static StaticAnimation RUINE_WALK;
	public static StaticAnimation RUINE_PLUNDER;
	
	public static StaticAnimation TORMENT_AUTO_1;
	public static StaticAnimation TORMENT_AUTO_2;
	public static StaticAnimation TORMENT_AUTO_3;
	public static StaticAnimation TORMENT_AIR_SLASH;
	public static StaticAnimation TORMENT_DASH;
	public static StaticAnimation TORMENT_IDLE;
	public static StaticAnimation TORMENT_RUN;
	public static StaticAnimation TORMENT_WALK;
	public static StaticAnimation TORMENT_BERSERK_AUTO_1;
	public static StaticAnimation TORMENT_BERSERK_AUTO_2;
	public static StaticAnimation TORMENT_BERSERK_DASH;
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
	public static StaticAnimation KATANA_SHEATHE;
	public static StaticAnimation KATANA_SHEATHED_IDLE;
	public static StaticAnimation KATANA_SHEATHED_RUN;
	public static StaticAnimation KATANA_FATAL_DRAW;
	public static StaticAnimation KATANA_FATAL_DRAW_SECOND;
	public static StaticAnimation KATANA_FATAL_DRAW_DASH;
	
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_1;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_2;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_3;
	public static StaticAnimation ENDERBLASTER_ONEHAND_AUTO_4;
	public static StaticAnimation ENDERBLASTER_ONEHAND_IDLE;
	
	@SubscribeEvent
	public static void registerAnimations(AnimationRegistryEvent event) {
		event.getRegistryMap().put(WeaponOfMinecraft.MODID, EFAnimations::build);
	}
	
	private static void build() {
		Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
		Model biped = models.biped;
		
		EATING = new StaticAnimation(0.11F, true, "biped/living/eating", biped);
		
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
		
		SWORD_ONEHAND_AUTO_1 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_2 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_2", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_3 = new BasicAttackAnimation(0.1F, 0.1F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/sword_onehand_auto_3", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		SWORD_ONEHAND_AUTO_4 = new BasicAttackAnimation(0.1F, 0.2F, 0.3F, 0.4F, null, "Tool_R", "biped/combat/sword_onehand_auto_4", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		
		
		AGONY_AUTO_1 = new BasicAttackAnimation(0.1F, 0.12F, 0.25F, 0.33F, null, "Tool_R", "biped/combat/agony_auto_1", biped);
		AGONY_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_2", biped,
				new Phase(0.08F, 0.08F, 0.33F, 0.41F, "Tool_R", null),
				new Phase(0.41F, 0.41F, 0.54F, 0.625F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		AGONY_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/agony_auto_3", biped,
				new Phase(0.04F, 0.04F, 0.125F, 0.208F, "Tool_R", null),
				new Phase(0.208F, 0.208F, 0.334F, 0.5F, "Tool_R", null),
				new Phase(0.5F, 0.5F, 0.667F, 0.91F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.8F),2)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F),2)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
		AGONY_DASH = new DashAttackAnimation(0.16F, 0.05F, 0.2F, 0.458F, 0.625F, null, "Tool_R", "biped/combat/agony_dash", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F));
		AGONY_AIR_SLASH = new AirSlashAnimation(0.2F, 0.25F, 0.42F, 0.7F, null, "Tool_R", "biped/combat/agony_airslash", biped)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.ROTATE_X, false);
		
		AGONY_IDLE = new StaticAnimation(true, "biped/living/agony_idle", biped);
		AGONY_RUN = new MovementAnimation(true, "biped/living/agony_run", biped);
		AGONY_WALK = new MovementAnimation(true, "biped/living/agony_walk", biped);
		
		AGONY_PLUNGE = new SpecialAttackAnimation(0.05F, "biped/skill/agony_plunge", biped,
				new Phase(0.166F, 0.166F, 0.208F, 1.042F, "Root", EFColliders.AGONY_PLUNGE), 
				new Phase(1.042F, 1.042F, 1.291F, 1.666F, "Root", EFColliders.AGONY_PLUNGE))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.WHOOSH_BIG,0)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,0)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(16),0)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(6.0F),0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F),0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD,0)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.LONG,1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(16),1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.EVISCERATE,1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE,1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.3F);
		
		RUINE_AUTO_1 = new BasicAttackAnimation(0.1F, 0.333F, 0.583F, 0.667F, null, "Tool_R", "biped/combat/ruine_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.1F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		RUINE_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/ruine_auto_2", biped,
				new Phase(0.05F, 0.05F, 0.25F, 0.583F, "Tool_R", null),
				new Phase(0.583F, 0.583F, 0.833F, 0.933F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_AUTO_3 = new BasicAttackAnimation(0.05F, 0.167F, 0.333F, 0.667F, null, "Tool_R", "biped/combat/ruine_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.3F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.25F);
		
		RUINE_DASH = new DashAttackAnimation(0.05F, 0.333F, 0.333F, 0.583F, 0.75F, null, "Tool_R", "biped/combat/ruine_dash", biped)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F);
		
		RUINE_IDLE = new StaticAnimation(true, "biped/living/ruine_idle", biped);
		RUINE_RUN = new MovementAnimation(true, "biped/living/ruine_run", biped);
		RUINE_WALK = new MovementAnimation(true, "biped/living/ruine_walk", biped);
		
		RUINE_PLUNDER = new SpecialAttackAnimation(0.05F, "biped/skill/ruine_plunder", biped,
				new Phase(1.1F, 1.1F, 1.25F, 1.85F, "Root", EFColliders.PLUNDER_PERDITION), 
				new Phase(1.85F, 1.85F, 2.2F, 2.5F, "Root", EFColliders.PLUNDER_PERDITION))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,0)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,0)
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
		
		
		TORMENT_AUTO_1 = new BasicAttackAnimation(0.2F, 0.1F, 0.35F, 0.75F, null, "Tool_R", "biped/combat/torment_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.80F);
		
		TORMENT_AUTO_2 = new BasicAttackAnimation(0.05F, 0.3F, 0.4F, 0.5F, null, "Tool_R", "biped/combat/torment_auto_2", biped)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.2F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.6F))
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.80F);
		
		TORMENT_AUTO_3 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/torment_auto_3", biped,
				new Phase(0.2F, 0.2F, 0.4F, 0.55F, "Tool_R", null),
				new Phase(0.55F, 0.55F, 0.75F, 1.1F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F),1)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.4F),1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.20F);
	
		TORMENT_DASH = new DashAttackAnimation(0.04F, 0.25F, 0.25F, 0.5F, 0.75F, null, "Tool_R", "biped/combat/torment_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.30F);
		
		TORMENT_IDLE = new StaticAnimation(true, "biped/living/torment_idle", biped);
		TORMENT_RUN = new MovementAnimation(true, "biped/living/torment_run", biped);
		TORMENT_WALK = new MovementAnimation(true, "biped/living/torment_walk", biped);
		
		TORMENT_BERSERK_IDLE = new StaticAnimation(true, "biped/living/torment_berserk_idle", biped);
		
		TORMENT_BERSERK_AUTO_1 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.35F, 0.65F, null, "Tool_R", "biped/skill/torment_berserk_auto_1", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_AUTO_2 = new BasicAttackAnimation(0.2F, 0.1F, 0.1F, 0.55F, 0.70F, null, "Tool_R", "biped/skill/torment_berserk_auto_2", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(8F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 0.75F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_DASH = new BasicAttackAnimation(0.04F, 0.35F, 0.35F, 0.85F, 1.0F, null, "Tool_R", "biped/skill/torment_berserk_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.4F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(10F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_CONVERT = new BasicAttackAnimation(0.05F, 0.6F, 1.35F, 1.7F, EFColliders.PLUNDER_PERDITION, "Root", "biped/skill/torment_berserk_convert", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, SoundEvents.ENDERMAN_SCREAM)
				.addProperty(AttackPhaseProperty.HIT_SOUND, SoundEvents.CHAIN_BREAK)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.setter(8.0F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.setter(1.0F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.setter(20F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 1.50F);
		
		TORMENT_BERSERK_RUN = new MovementAnimation(true, "biped/living/torment_berserk_run", biped);
		TORMENT_BERSERK_WALK = new MovementAnimation(true, "biped/living/torment_berserk_walk", biped);
		
		KATANA_SHEATHED_AUTO_1 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/katana_sheathed_auto_1", biped,
				new Phase(0.15F, 0.15F, 0.25F, 0.3F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.3F, 0.3F, 0.40F, 0.55F, "Root", EFColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
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
				new Phase(0.15F, 0.15F, 0.25F, 0.3F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.3F, 0.3F, 0.35F, 0.45F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.45F, 0.45F, 0.60F, 0.7F, "Root", EFColliders.KATANA_SHEATHED_AUTO))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F),2)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
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
		
		
		KATANA_SHEATHED_AUTO_3 = new BasicAttackAnimation(0.10F, 0.10F, 0.25F, 0.50F, EFColliders.KATANA_SHEATHED_AUTO, "Root", "biped/combat/katana_sheathed_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(1.8F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(3F))
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
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
		
		KATANA_SHEATHED_DASH = new BasicMultipleAttackAnimation(0.15F, "biped/combat/katana_sheathed_dash", biped,
				new Phase(0.15F, 0.15F, 0.3F, 0.35F, "Root", EFColliders.KATANA_SHEATHED_AUTO),
				new Phase(0.7F, 0.7F, 0.80F, 0.90F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(0.90F, 0.90F, 0.95F, 1.0F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.0F, 1.0F, 1.10F, 1.15F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.15F, 1.15F, 1.25F, 1.35F, "Root", EFColliders.KATANA_SHEATHED_DASH),
				new Phase(1.35F, 1.35F, 1.45F, 1.5F, "Root", EFColliders.KATANA_SHEATHED_DASH))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.75F))
				.addProperty(AttackPhaseProperty.MAX_STRIKES, ValueCorrector.multiplier(2F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),3)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),4)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.25F),5)
				.addProperty(AttackAnimationProperty.ROTATE_X, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F)
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
		
		KATANA_SHEATHE = new StaticAnimation(false, "biped/living/katana_sheathe", biped)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.2F, ReuseableEvents.KATANA_IN, Event.Side.CLIENT)});
		
		KATANA_SHEATHED_IDLE = new StaticAnimation(true, "biped/living/katana_sheathed_idle", biped);
		KATANA_SHEATHED_RUN = new MovementAnimation(true, "biped/living/katana_sheathed_run", biped);

		KATANA_AUTO_1 = new BasicAttackAnimation(0.1F, 0.05F, 0.2F, 0.3F, null, "Tool_R", "biped/combat/katana_auto_1", biped)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_2 = new BasicMultipleAttackAnimation(0.1F, "biped/combat/katana_auto_2", biped,
				new Phase(0.10F, 0.10F, 0.25F, 0.25F, "Tool_R", null),
				new Phase(0.35F, 0.35F, 0.6F, 0.65F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.5F),0)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.6F),1)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		KATANA_AUTO_3 = new BasicAttackAnimation(0.1F, 0.25F, 0.4F, 0.75F, null, "Tool_R", "biped/combat/katana_auto_3", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.2F))
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		
		KATANA_DASH = new BasicAttackAnimation(0.05F, 0.4F, 0.5F, 1.05F, null, "Tool_R", "biped/combat/katana_dash", biped)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(1.3F))
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(3.0F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.SHORT)
				.addProperty(AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.5F);
		
		KATANA_IDLE = new StaticAnimation(true, "biped/living/katana_idle", biped);
		
		KATANA_FATAL_DRAW = new SpecialAttackAnimation(0.15F, 0.0F, 0.55F, 0.70F, 1.0F, EFColliders.FATAL_DRAW, "Root", "biped/skill/katana_fatal_draw", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)});
		
		KATANA_FATAL_DRAW_SECOND = new SpecialAttackAnimation(0.15F, 0.0F, 0.50F, 0.70F, 1.0F, EFColliders.FATAL_DRAW, "Root", "biped/skill/katana_fatal_draw_second", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, true)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)});
		
		KATANA_FATAL_DRAW_DASH = new SpecialAttackAnimation(0.15F, 0.35F, 0.50F, 0.70F, 1.05F, EFColliders.FATAL_DRAW_DASH, "Root", "biped/skill/katana_fatal_draw_dash", biped)
				.addProperty(AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
				.addProperty(AttackAnimationProperty.LOCK_ROTATION, false)
				.addProperty(AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.05F, ReuseableEvents.KATANA_IN, Event.Side.SERVER)})
				.addProperty(StaticAnimationProperty.EVENTS, new Event[] {Event.create(0.6F, (entitypatch) -> {
					Entity entity = entitypatch.getOriginal();
					entitypatch.getOriginal().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
				}, Side.CLIENT)});
		
		
		ENDERBLASTER_ONEHAND_AUTO_1 = new BasicMultipleAttackAnimation(0.1F, "biped/combat/enderblaster_onehand_auto_1", biped,
				new Phase(0.1F, 0.1F, 0.2F, 0.2F, "Leg_L", EFColliders.KICK),
				new Phase(0.3F, 0.3F, 0.4F, 0.45F, "Leg_L", EFColliders.KICK))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.45F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_2 = new BasicMultipleAttackAnimation(0.05F, "biped/combat/enderblaster_onehand_auto_2", biped,
				new Phase(0.1F, 0.1F, 0.2F, 0.2F, "Leg_R", EFColliders.KICK),
				new Phase(0.25F, 0.25F, 0.4F, 0.4F, "Tool_R", null),
				new Phase(0.55F, 0.55F, 0.6F, 0.65F, "Tool_R", null))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,2)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD,2)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.65F),1)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.35F),2)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(2.0F),2)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_3 = new BasicMultipleAttackAnimation(0.1F, "biped/combat/enderblaster_onehand_auto_3", biped,
				new Phase(0.05F, 0.05F, 0.2F, 0.2F, "Tool_R", EFColliders.ENDER_BLASTER_CROSS),
				new Phase(0.25F, 0.25F, 0.35F, 0.4F, "Tool_L", ColliderPreset.FIST))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.55F),1)
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT,1)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT,1)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		ENDERBLASTER_ONEHAND_AUTO_4 = new BasicMultipleAttackAnimation(0.1F, "biped/combat/enderblaster_onehand_auto_4", biped,
				new Phase(0.2F, 0.2F, 0.3F, 0.4F, "Knee_R", EFColliders.KNEE))
				.addProperty(AttackPhaseProperty.DAMAGE, ValueCorrector.multiplier(0.85F))
				.addProperty(AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
				.addProperty(AttackPhaseProperty.IMPACT, ValueCorrector.multiplier(3.0F))
				.addProperty(AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLUNT)
				.addProperty(AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD)
				.addProperty(AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F);
		
		
		ENDERBLASTER_ONEHAND_IDLE = new StaticAnimation(true, "biped/living/enderblaster_onehand_idle", biped);
	}
	
	private static class ReuseableEvents {
		private static final Consumer<LivingEntityPatch<?>> KATANA_IN = (entitypatch) -> entitypatch.playSound(EpicFightSounds.SWORD_IN, 0, 0);
	}
}
