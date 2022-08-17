package reascer.wom.skill;

import java.util.Random;
import java.util.UUID;

import net.minecraft.advancements.critereon.MobEffectsPredicate.MobEffectInstancePredicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import reascer.wom.gameasset.EFAnimations;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.ServerAnimator;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class DemonMarkPassiveSkill extends Skill {
	public static final SkillDataKey<Boolean> CATHARSIS = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> BASIC_ATTACK = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("bc38699e-0de8-11ed-861d-0242ac120002");
	
	public DemonMarkPassiveSkill(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(CATHARSIS);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(BASIC_ATTACK);
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			int chance = Math.abs(new Random().nextInt()) % 100;
			int sweping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal());
			if (chance < (20f + (sweping*10f))) {
				if (event.getTarget().hasEffect(MobEffects.WITHER)) {
					event.getTarget().removeEffect(MobEffects.WITHER);
					event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, (6 + (2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()))) *20, 2, false, true));
				} else {
					event.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, (6 + (2 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()))) *20, 1, false, true));
				}
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
	}
	
	@Override
	public boolean shouldDeactivateAutomatically(PlayerPatch<?> executer) {
		return true;
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(ACTIVE)) {
			PlayerPatch<?> entitypatch = container.getExecuter();
			int numberOf = 3;
			float partialScale = 1.0F / (numberOf - 1);
			float interpolation = 0.0F;
			Armature armature = entitypatch.getEntityModel(Models.LOGICAL_SERVER).getArmature();
			OpenMatrix4f transformMatrix;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Tool_L");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Tool_R");
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
			
			if (container.getDataManager().getDataValue(BASIC_ATTACK)) {
				int numberOf2 = 128;
				float partialScale2 = 1.0F / (numberOf2 - 1);
				float interpolation2 = 0.0F;
				for (int i = 0; i < numberOf2; i++) {
					transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation2), armature, "Tool_R");
					transformMatrix2.translate(new Vec3f(0,0.0F,1.8F));
					OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix2,transformMatrix2);
					transformMatrix2.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 4.0f)));
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Head");
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
			//CHEST
			interpolation = 0.0F;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Chest");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Arm_L");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Arm_R");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Torso");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Thigh_L");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Thigh_R");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Leg_L");
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
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Leg_R");
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
			Armature armature = entitypatch.getEntityModel(Models.LOGICAL_SERVER).getArmature();
			OpenMatrix4f transformMatrix;
			OpenMatrix4f transformMatrix2;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Tool_R");
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
				
				transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation), armature, "Tool_R");
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
					transformMatrix2 = Animator.getBindedJointTransformByName(entitypatch.getAnimator().getPose(interpolation2), armature, "Tool_R");
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
		}
	}
}