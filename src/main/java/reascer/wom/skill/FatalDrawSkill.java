package reascer.wom.skill;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.particle.WOMParticles;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightEntityDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.SkillEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class FatalDrawSkill extends WomMultipleAnimationSkill {
	private static final UUID EVENT_UUID = UUID.fromString("1a56d169-416a-4206-ba3d-e7100d55d603");
	private static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> SECOND_DRAW = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	
	public static final SkillDataKey<Boolean> TIMEDSLASH = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> FREQUENCY = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> ATTACKS = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Float> DAMAGE = SkillDataKey.createDataKey(SkillDataManager.ValueType.FLOAT);
	
	private List<Integer> timedSlashEntities = new ArrayList<Integer>();
	private List<Integer> AttackedSlashEntities = new ArrayList<Integer>();
	private List<Integer> prevTimedSlashEntities = new ArrayList<Integer>();
	
	public FatalDrawSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			if (executer.getOriginal().isSprinting()) {
				executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, false, executer.getOriginal());
				return 2;
			} else if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(ACTIVE)) {
				if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(SECOND_DRAW)) {
					executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, false, executer.getOriginal());
					return 1;
				} else {
					executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, true, executer.getOriginal());
					return 0;
				}
			} else {
				executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, true, executer.getOriginal());
				return 0;
			}
		}, WOMAnimations.KATANA_FATAL_DRAW, WOMAnimations.KATANA_FATAL_DRAW_SECOND, WOMAnimations.KATANA_FATAL_DRAW_DASH);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(SECOND_DRAW);
		container.getDataManager().registerData(COOLDOWN);
		
		container.getDataManager().registerData(TIMEDSLASH);
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(FREQUENCY);
		container.getDataManager().registerData(ATTACKS);
		container.getDataManager().registerData(DAMAGE);
		
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			container.getDataManager().setDataSync(COOLDOWN, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			container.getDataManager().setData(TIMEDSLASH, false);
			ServerPlayer serverPlayer = event.getPlayerPatch().getOriginal();
			StaticAnimation[] resetAnimations = {
					WOMAnimations.KATANA_SHEATHED_AUTO_1,
					WOMAnimations.KATANA_SHEATHED_AUTO_2,
					WOMAnimations.KATANA_SHEATHED_AUTO_3,
					WOMAnimations.KATANA_FATAL_DRAW,
					WOMAnimations.KATANA_FATAL_DRAW_SECOND,
					WOMAnimations.KATANA_FATAL_DRAW_DASH};
			
			if (event.getAnimation() == WOMAnimations.KATANA_SHEATHED_DASH) {
				this.resetTimedSlashes(container, serverPlayer);
			}
			
			if (event.getAnimation() == WOMAnimations.KATANA_FATAL_DRAW_DASH) {
				this.resetTimedSlashes(container, serverPlayer);
			}
			
			for (StaticAnimation staticAnimation : resetAnimations) {
				if (event.getAnimation() == staticAnimation) {
					if (container.getExecuter().getStamina() > 0) {
						container.getDataManager().setDataSync(TIMER, 30,serverPlayer);
					}
					if (staticAnimation != WOMAnimations.KATANA_FATAL_DRAW) {
						container.getDataManager().setDataSync(SECOND_DRAW, false,serverPlayer);
					}
				}
			}
			
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			container.getDataManager().setData(DAMAGE, event.getAttackDamage());
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getExecuter().getStamina() > 0) {
				if(container.getDataManager().getDataValue(TIMEDSLASH)){
					timedSlashEntities.add(event.getTarget().getId());
					/*
					if (prevTimedSlashEntities.size() > 0) {
						for (Integer e : prevTimedSlashEntities) {
							if (e == event.getTarget().getId()) {
								timedSlashEntities.add(event.getTarget().getId());
							}
						}
					}
					*/
				}
			}
		});
	}

	protected void resetTimedSlashes(SkillContainer container, ServerPlayer serverPlayer) {
		container.getDataManager().setDataSync(TIMEDSLASH, true,serverPlayer);
		container.getDataManager().setDataSync(FREQUENCY, 1,serverPlayer);
		container.getDataManager().setDataSync(ATTACKS, 3 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()),serverPlayer);
		container.getDataManager().setDataSync(SECOND_DRAW, false,serverPlayer);
		if (container.getExecuter().getStamina() > 0) {
			container.getDataManager().setDataSync(TIMER, 30,serverPlayer);
		}
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getSkill(this).getDataManager().getDataValue(COOLDOWN) < 80) {
			executer.getSkill(this).getDataManager().setData(COOLDOWN, 80);
			boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(EFKatanaPassive.SHEATH);
			if (isSheathed || executer.getSkill(this).getDataManager().getDataValue(ACTIVE)) {
				executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], -0.45F);
			} else {
				executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
			}
			if (!executer.getOriginal().isCreative()) {
				//this.setConsumptionSynchronize(executer, 0);
				this.setDurationSynchronize(executer, 0);
				SkillEvent.Consume event = new SkillEvent.Consume(executer, this, this.resource);
				executer.getEventListener().triggerEvents(EventType.SKILL_CONSUME_EVENT, event);
				
				if (!event.isCanceled()) {
					this.resource.consume.accept(this, executer);
				}
			}
			
			if (!executer.getSkill(this).getDataManager().getDataValue(ACTIVE)) {
				//executer.getOriginal().sendMessage(new TextComponent("katana stack:"+Math.round(Math.min(6,executer.getSkill(this).getStack()+1) * (1.0f + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())/3.0f))-1)), UUID.randomUUID());
				this.setStackSynchronize(executer, (int) Math.min(12,Math.round(Math.min(6,executer.getSkill(this).getStack()+1)* (1.0f + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())/3.0f)))-1));
				executer.getSkill(this).getDataManager().setDataSync(ACTIVE, true, executer.getOriginal());
			}
			if (executer.getSkill(this).getStack() == 0) {
				executer.getSkill(this).getDataManager().setDataSync(ACTIVE, false, executer.getOriginal());
			}
			executer.getSkill(this).activate();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void onScreen(LocalPlayerPatch playerpatch, float resolutionX, float resolutionY) {
		if (playerpatch.getSkill(this).getDataManager().getDataValue(ACTIVE)) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, new ResourceLocation(WeaponOfMinecraft.MODID, "textures/gui/overlay/katana_eternity.png"));
			GlStateManager._enableBlend();
			GlStateManager._disableDepthTest();
			GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tesselator tessellator = Tesselator.getInstance();
		    BufferBuilder bufferbuilder = tessellator.getBuilder();
		    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		    bufferbuilder.vertex(0, 0, 1).uv(0, 0).endVertex();
		    bufferbuilder.vertex(0, resolutionY, 1).uv(0, 1).endVertex();
		    bufferbuilder.vertex(resolutionX, resolutionY, 1).uv(1, 1).endVertex();
		    bufferbuilder.vertex(resolutionX, 0, 1).uv(1, 0).endVertex();
		    tessellator.end();
		}
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(COOLDOWN) > 0) {
			container.getDataManager().setData(COOLDOWN, container.getDataManager().getDataValue(COOLDOWN)-1);
		} else {
			if (container.getDataManager().getDataValue(ACTIVE)) {
				if(!container.getExecuter().isLogicalClient()) {
					this.setStackSynchronize((ServerPlayerPatch)container.getExecuter(),0);
					container.getExecuter().getSkill(this).getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch)container.getExecuter()).getOriginal());
				}
			}
		}
		if(!container.getExecuter().isLogicalClient()) {
			if (container.getDataManager().getDataValue(TIMER) > 0) {
				container.getDataManager().setData(TIMER, container.getDataManager().getDataValue(TIMER)-1);
					if (container.getDataManager().getDataValue(TIMER) == 0) {
						AttackedSlashEntities = new ArrayList<Integer>(timedSlashEntities);
						timedSlashEntities.clear();
					}
			} else {
				if (container.getDataManager().getDataValue(FREQUENCY) > 0) {
					container.getDataManager().setData(FREQUENCY, container.getDataManager().getDataValue(FREQUENCY)-1);
				} else {
					if (container.getDataManager().getDataValue(ATTACKS) > 0) {
						container.getDataManager().setData(ATTACKS, container.getDataManager().getDataValue(ATTACKS)-1);
						container.getDataManager().setData(FREQUENCY, 1);
						for (Integer entityID : AttackedSlashEntities) {
							Entity e = container.getExecuter().getOriginal().getLevel().getEntity(entityID);
							if (e != null) {
								if(e.isAlive()) {
									EpicFightEntityDamageSource epicFightDamageSource = new EpicFightEntityDamageSource("timed_Katana_slashes", container.getExecuter().getOriginal(),WOMAnimations.KATANA_SHEATHED_DASH);
									epicFightDamageSource.setImpact(2.0f);
									epicFightDamageSource.setStunType(StunType.HOLD);
									DamageSource damage = epicFightDamageSource;
									e.invulnerableTime = 0;
									if (e.hurt(damage, container.getDataManager().getDataValue(DAMAGE) * 0.25f)) {
										e.playSound(EpicFightSounds.BLADE_HIT, 1, 1);
										WOMParticles.KATANA_SHEATHED_HIT.get().spawnParticleWithArgument(((ServerLevel) container.getExecuter().getOriginal().level), null, null, e, container.getExecuter().getOriginal());
									}
								}
							}
						}
					} else {
						if (AttackedSlashEntities.size() > 0) {
							AttackedSlashEntities.clear();
						}
					}
				}
			}
		}
	
	}
}