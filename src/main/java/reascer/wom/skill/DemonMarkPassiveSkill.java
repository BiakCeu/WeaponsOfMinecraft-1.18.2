package reascer.wom.skill;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class DemonMarkPassiveSkill extends PassiveSkill {
	public static final SkillDataKey<Boolean> CATHARSIS = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> PARTICLE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> WITHERCURSE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> BLINK = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> BASIC_ATTACK = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> IDLE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("bc38699e-0de8-11ed-861d-0242ac120002");
	
	public DemonMarkPassiveSkill(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(CATHARSIS);
		container.getDataManager().registerData(PARTICLE);
		container.getDataManager().registerData(WITHERCURSE);
		container.getDataManager().registerData(BLINK);
		container.getDataManager().setData(WITHERCURSE,true);
		container.getDataManager().registerData(BASIC_ATTACK);
		container.getDataManager().registerData(IDLE);
		container.getDataManager().setData(IDLE,false);
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (event.getDamageSource().cast().getMsgId() != "demon_fee") {
				if (container.getDataManager().getDataValue(WITHERCURSE)) {
					int chance = Math.abs(new Random().nextInt()) % 100;
					int sweping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal());
					if (chance < ( (20f + (sweping*10f) ) * (container.getDataManager().getDataValue(BLINK) ? 2F : 1F) ) ) {
						if (event.getTarget().hasEffect(MobEffects.WITHER)) {
							event.getTarget().removeEffect(MobEffects.WITHER);
							event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, (6 + (2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()))) *20, 2, false, true));
						} else {
							event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, (6 + (2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()))) *20, 1, false, true));
						}
					}
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			container.getDataManager().setDataSync(BASIC_ATTACK,false,event.getPlayerPatch().getOriginal());
			
			if (!container.getDataManager().getDataValue(WITHERCURSE)) {
				container.getDataManager().setDataSync(WITHERCURSE,true,event.getPlayerPatch().getOriginal());
			}
			
			if (container.getDataManager().getDataValue(BLINK)) {
				container.getDataManager().setDataSync(BLINK,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_BLINK) {
				container.getDataManager().setDataSync(BLINK,true,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_DEATHFALL) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_AUTO_1) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_AUTO_2) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_SHOOT) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
			
			if (event.getAnimation() == WOMAnimations.ANTITHEUS_ASCENDED_BLACKHOLE) {
				container.getDataManager().setDataSync(WITHERCURSE,false,event.getPlayerPatch().getOriginal());
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
	}
	
	@Override
	public boolean shouldDeactivateAutomatically(PlayerPatch<?> executer) {
		return true;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(PARTICLE)) {
			PlayerPatch<?> entitypatch = container.getExecuter();
			int numberOf = 3;
			float partialScale = 1.0F / (numberOf - 1);
			float interpolation = 0.0F;
			OpenMatrix4f transformMatrix;
			for (int i = 0; i < numberOf; i++) {
				
				transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolL);
				transformMatrix.translate(new Vec3f(0,0.0F,0.0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
				for (int j = 0; j < 1; j++) {
					entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						(transformMatrix.m30 + entitypatch.getOriginal().getX()),
						(transformMatrix.m31 + entitypatch.getOriginal().getY()),
						(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f);
				}
				for (int j = 0; j < 1; j++) {
					entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						(transformMatrix.m30 + entitypatch.getOriginal().getX()),
						(transformMatrix.m31 + entitypatch.getOriginal().getY()),
						(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
						0,
						0,
						0);
				}
				interpolation += partialScale;
			}
			OpenMatrix4f transformMatrix2;
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
				transformMatrix2.translate(new Vec3f(0,0.0F,1.8F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				transformMatrix2.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 4.0f)));
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ()),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX()),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY()),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ()),
					0,
					0,
					0);
				interpolation += partialScale;
			}
			// BLINK
			if (container.getDataManager().getDataValue(BASIC_ATTACK)) {
				int n = 30; // set the number of particles to emit
				double r = 4.8; // set the radius of the disk to 1
				double t = 0.01; // set the thickness of the disk to 0.1
				
				for (int j = 0; j < n; j++) {
				    double theta = 2 * Math.PI * (new Random().nextFloat()); // generate a random azimuthal angle
				    double phi = (new Random().nextFloat() - 0.5f) * Math.PI * t / r; // generate a random angle within the disk thickness

				    // calculate the emission direction in Cartesian coordinates using the polar coordinates
				    double x = r * Math.cos(phi) * Math.cos(theta);
				    double y = r * Math.cos(phi) * Math.sin(theta);
				    double z = r * Math.sin(phi);
				    
				 // create a Vector3f object to represent the emission direction
				    float randomVelocity =  (new Random().nextFloat()+ 0.4f) ;
				    Vec3f direction = new Vec3f((float)x * randomVelocity, (float)y * randomVelocity, (float)z * randomVelocity);

				 // rotate the direction vector to align with the forward vector
				    OpenMatrix4f rotation = new OpenMatrix4f().rotate((float) Math.toRadians(90), new Vec3f(1, 0, 0));
				    OpenMatrix4f.transform3v(rotation, direction, direction);
				    
				    // emit the particle in the calculated direction, with some random velocity added
				    entitypatch.getOriginal().level.addParticle(ParticleTypes.LARGE_SMOKE,
				        (entitypatch.getOriginal().getX()) + direction.x,
				        (entitypatch.getOriginal().getY()) + direction.y + 1.25f,
				        (entitypatch.getOriginal().getZ()) + direction.z,
				        (float)(0),
				        (float)(-0.05f),
				        (float)(0));
				}
			}
			
			for (int j = 0; j < 14; j++) {
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(entitypatch.getOriginal().getX()),
					(entitypatch.getOriginal().getY())+0.03f,
					(entitypatch.getOriginal().getZ()),
					(new Random().nextFloat() - 0.5F)*0.65f,
					(new Random().nextFloat() - 0.5F)*0.05f,
					(new Random().nextFloat() - 0.5F)*0.65f);
			}
			
			numberOf = 1;
			partialScale = 1.0F / (numberOf - 1);
			
			//HEAD
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.head);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() + 0.3F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//CHEST
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.chest);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//LEFT ARM
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.armL);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//RIGHT ARM
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.armR);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//BELLY
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.torso);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//LEFT THIGH
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.thighL);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//RIGHT THIGH
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.thighR);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//LEFT LEG
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.legL);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			//RIGHT LEG
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.legR);
				transformMatrix2.translate(new Vec3f(0,0.0F,0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					(transformMatrix2.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
					(transformMatrix2.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
					(new Random().nextFloat() - 0.5F)*0.15f,
					(new Random().nextFloat() - 1.0F)*0.55f,
					(new Random().nextFloat() - 0.5F)*0.15f);
				interpolation += partialScale;
			}
			
		} else {
			PlayerPatch<?> entitypatch = container.getExecuter();
			int numberOf = 7;
			float partialScale = 1.0F / (numberOf - 1);
			float interpolation = 0.0F;
			Armature armature = entitypatch.getArmature();
			OpenMatrix4f transformMatrix;
			OpenMatrix4f transformMatrix2;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
				transformMatrix.translate(new Vec3f(0,0.0F,1.8F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
				for (int j = 0; j < 1; j++) {
					transformMatrix.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 4.0f)));
					entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						(transformMatrix.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.25f),
						(transformMatrix.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.25f),
						(transformMatrix.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.25f),
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f);
				}
				
				transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
				transformMatrix2.translate(new Vec3f(0,0.7F,-1.8F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
				for (int j = 0; j < 1; j++) {
					float blade = -(new Random().nextFloat() * 1.8f);
					transformMatrix2.translate(new Vec3f(0,blade,-(new Random().nextFloat() * 0.4f)-(blade/4)));
					entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						(transformMatrix2.m30 + entitypatch.getOriginal().getX()),
						(transformMatrix2.m31 + entitypatch.getOriginal().getY()),
						(transformMatrix2.m32 + entitypatch.getOriginal().getZ()),
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f,
						(new Random().nextFloat() - 0.5F)*0.15f);
				}
				interpolation += partialScale;
			}
			if (container.getDataManager().getDataValue(BASIC_ATTACK)) {
				int numberOf2 = 28;
				float partialScale2 = 1.0F / (numberOf2 - 1);
				float interpolation2 = 0.0F;
				for (int i = 0; i < numberOf2; i++) {
					transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation2), Armatures.BIPED.toolR);
					transformMatrix2.translate(new Vec3f(0,0.7F,-1.8F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
					float blade = -(new Random().nextFloat() * 1.8f);
					transformMatrix2.translate(new Vec3f(0,blade,-(new Random().nextFloat() * 0.4f)-(blade/4)));
					entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
						(transformMatrix2.m30 + entitypatch.getOriginal().getX()),
						(transformMatrix2.m31 + entitypatch.getOriginal().getY()),
						(transformMatrix2.m32 + entitypatch.getOriginal().getZ()),
						0,
						0,
						0);
					interpolation2 += partialScale2;
				}
			}
			
			if (entitypatch.currentLivingMotion != LivingMotions.IDLE) {
				container.getDataManager().setData(IDLE, false);
			}
			if (container.getDataManager().getDataValue(IDLE)) {
				int numberOf2 = 4;
				float partialScale2 = 1.0F / (numberOf2 - 1);
				float interpolation2 = 0.0F;
				for (int i = 0; i < numberOf2; i++) {
					transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation2), Armatures.BIPED.toolL);
					transformMatrix.translate(new Vec3f(0,0.0F,0.0F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
					for (int j = 0; j < 1; j++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
							(transformMatrix.m30 + entitypatch.getOriginal().getX()),
							(transformMatrix.m31 + entitypatch.getOriginal().getY()),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
							(new Random().nextFloat() - 0.5F)*0.15f,
							(new Random().nextFloat() - 0.5F)*0.15f,
							(new Random().nextFloat() - 0.5F)*0.15f);
					}
					for (int j = 0; j < 1; j++) {
						entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
							(transformMatrix.m30 + entitypatch.getOriginal().getX()),
							(transformMatrix.m31 + entitypatch.getOriginal().getY()),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ()),
							0,
							0.05,
							0);
					}
					interpolation += partialScale2;
				}
			}
			if (entitypatch.getOriginal().isUsingItem() && !(entitypatch.isUnstable() || entitypatch.getEntityState().hurt()) && entitypatch.isBattleMode() && container.getExecuter().getEntityState().canBasicAttack() ) {
				int numberOf2 = 20;
				float partialScale2 = 1.0F / (numberOf2 - 1);
				float interpolation2 = 0.0F;
				for (int i = 0; i < numberOf2; i++) {
					transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation2), Armatures.BIPED.toolL);
					transformMatrix.translate(new Vec3f(0,0.0F,0.0F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
					int n = 1; // set the number of particles to emit
					double r = 1.0; // set the radius of the disk to 1
					double t = 0.1; // set the thickness of the disk to 0.1

					for (int j = 0; j < n; j++) {
					    double theta = 2 * Math.PI * new Random().nextDouble(); // generate a random angle around the z-axis
					    double phi = (new Random().nextDouble() - 0.5) * Math.PI * t / r; // generate a random angle within the disk thickness

					    // calculate the emission direction in Cartesian coordinates using the polar coordinates
					    double x = r * Math.cos(phi) * Math.cos(theta);
					    double y = r * Math.cos(phi) * Math.sin(theta);
					    double z = r * Math.sin(phi);

					    // create a Vector3f object to represent the emission direction
					    Vec3f direction = new Vec3f((float)x, (float)y, (float)z);

					    // rotate the direction vector to align with the forward vector
					    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().getViewYRot(1) + 180F), new Vec3f(0, 1, 0));
					    rotation.rotate(-(float) ((transformMatrix.m11-0.07f)*1.5f), new Vec3f(1, 0, 0));
					    OpenMatrix4f.transform3v(rotation, direction, direction);

					    // emit the particle in the calculated direction, with some random velocity added
					    entitypatch.getOriginal().level.addParticle(ParticleTypes.SMOKE,
					        (direction.x + transformMatrix.m30 + entitypatch.getOriginal().getX()),
					        (direction.y + transformMatrix.m31 + entitypatch.getOriginal().getY()),
					        (direction.z + transformMatrix.m32 + entitypatch.getOriginal().getZ()),
					        0 ,
					        0 ,
					        0 );
					}

					interpolation += partialScale2;
				}
			}
		}
	}
}