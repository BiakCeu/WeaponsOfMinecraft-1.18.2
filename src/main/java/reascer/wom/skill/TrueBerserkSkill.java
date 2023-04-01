package reascer.wom.skill;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.WOMAnimations;
import reascer.wom.main.WeaponOfMinecraft;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill.ActivateType;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.IndirectEpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.SkillEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class TrueBerserkSkill extends WeaponInnateSkill {
	private static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Integer> CHARGING_TIME = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> SAVED_CHARGE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> CHARGING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> SUPER_ARMOR = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> CHARGED = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> CHARGED_ATTACK = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	

	private static final UUID EVENT_UUID = UUID.fromString("16c6748a-1c74-4681-9edb-e9ea4d69e54f");
	
	protected final StaticAnimation activateAnimation;
	
	public TrueBerserkSkill(Builder<?> builder) {
		super(builder.setActivateType(ActivateType.DURATION_INFINITE));

		this.activateAnimation = WOMAnimations.TORMENT_BERSERK_CONVERT;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(CHARGING);
		container.getDataManager().registerData(CHARGING_TIME);
		container.getDataManager().registerData(SAVED_CHARGE);
		container.getDataManager().registerData(SUPER_ARMOR);
		container.getDataManager().registerData(CHARGED);
		container.getDataManager().registerData(CHARGED_ATTACK);
		
		if (!container.getExecuter().isLogicalClient()) {
			this.setMaxDurationSynchronize((ServerPlayerPatch)container.getExecuter(), this.maxDuration + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()));
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			if (event.getPlayerPatch().getOriginal().getItemInHand(InteractionHand.MAIN_HAND).getItem() == WOMItems.TORMENTED_MIND.get() && container.getExecuter().getEntityState().canBasicAttack()) {
				if (container.getExecuter().getStamina() > 0) {
					event.getPlayerPatch().getOriginal().startUsingItem(InteractionHand.MAIN_HAND);
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			if (event.getPlayerPatch().getOriginal().getItemInHand(InteractionHand.MAIN_HAND).getItem() == WOMItems.TORMENTED_MIND.get() && container.getExecuter().getEntityState().canBasicAttack()) {
				if (container.getExecuter().getStamina() > 0) {
					event.getPlayerPatch().getOriginal().startUsingItem(InteractionHand.MAIN_HAND);
					if(!container.getExecuter().isLogicalClient()) {
						container.getDataManager().setDataSync(CHARGING, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
					}
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(CHARGED_ATTACK) || container.getDataManager().getDataValue(CHARGED)) {
				event.setDamage(event.getDamage()*2);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(CHARGED_ATTACK)) {
				container.getDataManager().setDataSync(CHARGED, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				((ServerLevel) event.getPlayerPatch().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
						event.getTarget().getX() - 0.15D, 
						event.getTarget().getY() + 1.2D, 
						event.getTarget().getZ() - 0.15D, 
						25, 0.0D, 0.0D, 0.0D,
						0.2D);
				((ServerLevel) event.getPlayerPatch().getOriginal().level).sendParticles( ParticleTypes.LAVA, 
						event.getTarget().getX() - 0.15D, 
						event.getTarget().getY() + 1.2D, 
						event.getTarget().getZ() - 0.15D, 
						25, 0.0D, 0.0D, 0.0D,
						1.0D);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(SUPER_ARMOR) || container.getDataManager().getDataValue(CHARGING)) {
				event.setAmount(event.getAmount()*0.8f);
				event.getDamageSource().setStunType(StunType.NONE);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation().getId() != WOMAnimations.TORMENT_AUTO_1.getId() &&
				event.getAnimation().getId() != WOMAnimations.TORMENT_AUTO_2.getId() &&
				event.getAnimation().getId() != WOMAnimations.TORMENT_AUTO_3.getId() &&
				event.getAnimation().getId() != WOMAnimations.TORMENT_AUTO_4.getId()) {
			}
			container.getDataManager().setDataSync(CHARGED_ATTACK, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			if (container.getDataManager().getDataValue(CHARGED)) {
				container.getDataManager().setDataSync(CHARGED_ATTACK, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
			event.getPlayerPatch().getOriginal().stopUsingItem();
			container.getDataManager().setDataSync(CHARGING, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			if (container.getDataManager().getDataValue(SAVED_CHARGE) < 20) {
				container.getDataManager().setDataSync(SAVED_CHARGE, container.getDataManager().getDataValue(CHARGING_TIME) , ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			}
			container.getDataManager().setDataSync(CHARGING_TIME, 0 , ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
			if (event.getAnimationId() != WOMAnimations.TORMENT_AUTO_1.getId() &&
				event.getAnimationId() != WOMAnimations.TORMENT_AUTO_2.getId() &&
				event.getAnimationId() != WOMAnimations.TORMENT_AUTO_3.getId() &&
				event.getAnimationId() != WOMAnimations.TORMENT_AUTO_4.getId()) {
			}
			container.getDataManager().setDataSync(CHARGED_ATTACK, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
			container.getDataManager().setDataSync(CHARGED, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
		});
		
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
		if(!container.getExecuter().isLogicalClient()) {
			container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
		}
		container.deactivate();
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getSkill(this).isActivated()) { 
			super.cancelOnServer(executer, args);
			executer.getOriginal().removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
			executer.getSkill(this).getDataManager().setDataSync(ACTIVE, false,executer.getOriginal());
			this.setDurationSynchronize(executer, 0);
			this.setStackSynchronize(executer, executer.getSkill(this).getStack() - 1);
			executer.getSkill(this).deactivate();
			executer.modifyLivingMotionByCurrentItem();
		} else {
			executer.playAnimationSynchronized(this.activateAnimation, 0);
			executer.getOriginal().level.playSound(null, executer.getOriginal().xo, executer.getOriginal().yo, executer.getOriginal().zo,
	    			SoundEvents.DRAGON_FIREBALL_EXPLODE, executer.getOriginal().getSoundSource(), 1.0F, 0.5F);
			((ServerLevel) executer.getOriginal().level).sendParticles( ParticleTypes.LARGE_SMOKE, 
					executer.getOriginal().getX() - 0.15D, 
					executer.getOriginal().getY() + 1.2D, 
					executer.getOriginal().getZ() - 0.15D, 
					150, 0.3D, 0.6D, 0.3D, 0.1D);
			executer.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 120000));
			executer.getSkill(this).getDataManager().setData(TIMER, 2);
			executer.getSkill(this).getDataManager().setDataSync(ACTIVE, true,executer.getOriginal());
			this.setDurationSynchronize(executer, this.maxDuration + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal()));
			executer.getSkill(this).activate();
			executer.modifyLivingMotionByCurrentItem();
			SkillEvent.Consume event = new SkillEvent.Consume(executer, this, this.resource);
			executer.getEventListener().triggerEvents(EventType.SKILL_CONSUME_EVENT, event);
			
			if (!event.isCanceled()) {
				this.resource.consume.accept(this, executer);
			}
		}
	}
	
	@Override
	public void cancelOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.cancelOnServer(executer, args);
		executer.getOriginal().removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
		executer.getSkill(this).getDataManager().setDataSync(ACTIVE, false,executer.getOriginal());
		this.setStackSynchronize(executer, executer.getSkill(this).getStack() - 1);
		executer.modifyLivingMotionByCurrentItem();
	}
	
	@Override
	public boolean canExecute(PlayerPatch<?> executer) {
		if (executer.isLogicalClient()) {
			return super.canExecute(executer);
		} else {
			ItemStack itemstack = executer.getOriginal().getMainHandItem();
			
			return EpicFightCapabilities.getItemStackCapability(itemstack).getInnateSkill(executer, itemstack) == this && executer.getOriginal().getVehicle() == null;
		}
	}
	
	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Auto attack :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Dash attack :");
		
		return list;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void onScreen(LocalPlayerPatch playerpatch, float resolutionX, float resolutionY) {
		if (playerpatch.getSkill(this).isActivated()) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, new ResourceLocation(WeaponOfMinecraft.MODID, "textures/gui/overlay/true_berserk.png"));
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
		if (container.getDataManager().getDataValue(CHARGED)) {
			PlayerPatch<?> entitypatch = container.getExecuter();
			int numberOf = 2;
			float partialScale = 1.0F / (numberOf - 1);
			float interpolation = 0.0F;
			OpenMatrix4f transformMatrix;
			for (int i = 0; i < numberOf; i++) {
				transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(interpolation), Armatures.BIPED.toolR);
				transformMatrix.translate(new Vec3f(0,0.0F,-1.0F));
				OpenMatrix4f.mul(new OpenMatrix4f().rotate(-(float) Math.toRadians(entitypatch.getOriginal().yBodyRotO + 180F), new Vec3f(0, 1, 0)),transformMatrix,transformMatrix);
					transformMatrix.translate(new Vec3f(0,0.0F,-(new Random().nextFloat() * 1.0f)));
					entitypatch.getOriginal().level.addParticle(new DustParticleOptions(new Vector3f(0.8f,0.6f,0f),1.0f),
						(transformMatrix.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.55f),
						(transformMatrix.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.55f),
						(transformMatrix.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.55f),
						0,
						0,
						0);
					entitypatch.getOriginal().level.addParticle(ParticleTypes.FLAME,
							(transformMatrix.m30 + entitypatch.getOriginal().getX() + (new Random().nextFloat() - 0.5F)*0.75f),
							(transformMatrix.m31 + entitypatch.getOriginal().getY() + (new Random().nextFloat() - 0.5F)*0.75f),
							(transformMatrix.m32 + entitypatch.getOriginal().getZ() + (new Random().nextFloat() - 0.5F)*0.75f),
							0,
							0,
							0);
				interpolation += partialScale;
			}
		}
		if (container.isActivated()) {
			if (container.getDataManager().getDataValue(ACTIVE)) {
				if (container.getDataManager().getDataValue(TIMER) > 0) {
					container.getDataManager().setData(TIMER, container.getDataManager().getDataValue(TIMER)-1);
				} else {
					container.getDataManager().setData(TIMER,3);
					if (container.getRemainDuration() > 1) {
						if(!container.getExecuter().isLogicalClient()) {
							this.setDurationSynchronize((ServerPlayerPatch) container.getExecuter(), container.getRemainDuration()-1);
						}
					} else {
						if (container.getExecuter().getOriginal().getHealth() - (container.getExecuter().getOriginal().getMaxHealth() * 0.02f) > 0f) {
							DamageSource damage = new IndirectEpicFightDamageSource("Heartattack_from_wrath", container.getExecuter().getOriginal(), container.getExecuter().getOriginal(), StunType.NONE).bypassArmor().bypassMagic();
							container.getExecuter().getOriginal().hurt(damage,(container.getExecuter().getOriginal().getMaxHealth() * 0.02f));
							if(!container.getExecuter().isLogicalClient()) {
								if (!container.getExecuter().getOriginal().isCreative()) {
								((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.SMOKE, 
										container.getExecuter().getOriginal().getX() - 0.2D, 
										container.getExecuter().getOriginal().getY() + 1.3D, 
										container.getExecuter().getOriginal().getZ() - 0.2D, 
										40, 0.6D, 0.8D, 0.6D, 0.05);
								}
								this.setDurationSynchronize((ServerPlayerPatch) container.getExecuter(),(this.maxDuration + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal())));
							}
						} else {
							if(!container.getExecuter().isLogicalClient()) {
								container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
								this.setDurationSynchronize((ServerPlayerPatch) container.getExecuter(), 0);
							}
							container.deactivate();
						}
					}
				}
			} else {
				if(!container.getExecuter().isLogicalClient()) {
					container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
				}
			}
		}
		if(!container.getExecuter().isLogicalClient()) {
			AttributeModifier charging_Movementspeed = new AttributeModifier(EVENT_UUID, "torment.charging_movespeed", 4, Operation.MULTIPLY_TOTAL);
			ServerPlayerPatch executer = (ServerPlayerPatch) container.getExecuter();
			int sweeping_edge = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal());
			
			if (container.getDataManager().getDataValue(CHARGING) && !container.getExecuter().getOriginal().isUsingItem() && container.getExecuter().getEntityState().canBasicAttack()){
				int animation_timer = container.getDataManager().getDataValue(CHARGING_TIME);
				if (container.getDataManager().getDataValue(CHARGING_TIME) < 20 && container.getDataManager().getDataValue(SAVED_CHARGE) >= 20) {
					animation_timer = container.getDataManager().getDataValue(SAVED_CHARGE);
				}
				if (container.getDataManager().getDataValue(CHARGING_TIME) >= 110) {
					container.getExecuter().getOriginal().level.playSound(null,
							container.getExecuter().getOriginal().getX(),
							container.getExecuter().getOriginal().getY(),
							container.getExecuter().getOriginal().getZ(),
							EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS,
							1.0F, 1.2F);
				} else if (animation_timer >= 80) {
					container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_3, 0);
				} else if (animation_timer >= 50) {
					container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_2, 0);
				} else if (animation_timer >= 20) {
					container.getExecuter().playAnimationSynchronized(WOMAnimations.TORMENT_CHARGED_ATTACK_1, 0);
				} else {
					container.getExecuter().getOriginal().level.playSound(null,
							container.getExecuter().getOriginal().getX(),
							container.getExecuter().getOriginal().getY(),
							container.getExecuter().getOriginal().getZ(),
							EpicFightSounds.WHOOSH_BIG, SoundSource.PLAYERS,
							1.0F, 1.2F);
				}
				container.getDataManager().setDataSync(CHARGING, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				container.getDataManager().setDataSync(CHARGING_TIME, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				container.getDataManager().setDataSync(SAVED_CHARGE, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				if (!container.getExecuter().getOriginal().isCreative()) {
					if (!container.getExecuter().consumeStamina(5)) {
						container.getExecuter().setStamina(0);
					}
				}
				container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
			}
			if (container.getDataManager().getDataValue(CHARGING)) {
				if (container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getModifier(EVENT_UUID) == null) {
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(charging_Movementspeed);
				}

				container.getDataManager().setDataSync(CHARGING_TIME, container.getDataManager().getDataValue(CHARGING_TIME) +1, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
				if (container.getDataManager().getDataValue(CHARGING_TIME) <= 130) {
					if (container.getDataManager().getDataValue(CHARGING_TIME) == 20) {
						container.getExecuter().getOriginal().level.playSound(null,
								container.getExecuter().getOriginal().getX(),
								container.getExecuter().getOriginal().getY(),
								container.getExecuter().getOriginal().getZ(),
								SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
								1.0F, 0.6F);
					}
					if (container.getDataManager().getDataValue(CHARGING_TIME) == 50) {
						container.getExecuter().getOriginal().level.playSound(null,
								container.getExecuter().getOriginal().getX(),
								container.getExecuter().getOriginal().getY(),
								container.getExecuter().getOriginal().getZ(),
								SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
								1.0F, 0.65F);
					}
					if (container.getDataManager().getDataValue(CHARGING_TIME) == 80) {
						container.getExecuter().getOriginal().level.playSound(null,
								container.getExecuter().getOriginal().getX(),
								container.getExecuter().getOriginal().getY(),
								container.getExecuter().getOriginal().getZ(),
								SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
								1.0F, 0.7F);
					}
					if (container.getDataManager().getDataValue(CHARGING_TIME) == 110) {
						container.getDataManager().setDataSync(CHARGED, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
						container.getExecuter().getOriginal().level.playSound(null,
								container.getExecuter().getOriginal().getX(),
								container.getExecuter().getOriginal().getY(),
								container.getExecuter().getOriginal().getZ(),
								SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
								1.0F, 0.5F);
						container.getExecuter().getOriginal().level.playSound(null,
								container.getExecuter().getOriginal().getX(),
								container.getExecuter().getOriginal().getY(),
								container.getExecuter().getOriginal().getZ(),
								SoundEvents.BELL_BLOCK, SoundSource.MASTER,
								2.5F, 0.5F);
					}
					if (container.getDataManager().getDataValue(CHARGING_TIME) == 130) {
						container.getDataManager().setDataSync(CHARGING_TIME, 0, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
						container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
					}
				}
			} else {
				container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(charging_Movementspeed);
			}
		}
	}
}