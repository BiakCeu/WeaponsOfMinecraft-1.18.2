package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SpecialAttackSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class DemonicAscensionSkill extends SpecialAttackSkill {
	public static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> ASCENDING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Boolean> SUPERARMOR = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final UUID EVENT_UUID = UUID.fromString("61ec318a-10f6-11ed-861d-0242ac120002");
	
	protected final StaticAnimation activateAnimation;
	
	public DemonicAscensionSkill(Builder builder) {
		super(builder);

		this.activateAnimation = builder.activateAnimation;
	}
	
	public static class Builder extends Skill.Builder<AgonyPlungeSkill> {
		
		protected StaticAnimation activateAnimation;
		
		public Builder(ResourceLocation resourceLocation) {
			super(resourceLocation);
		}
		
		public Builder setCategory(SkillCategories category) {
			this.category = category;
			return this;
		}
		
		public Builder setConsumption(float consumption) {
			this.consumption = consumption;
			return this;
		}
		
		public Builder setMaxDuration(int maxDuration) {
			this.maxDuration = maxDuration;
			return this;
		}
		
		public Builder setMaxStack(int maxStack) {
			this.maxStack = maxStack;
			return this;
		}
		
		public Builder setRequiredXp(int requiredXp) {
			this.requiredXp = requiredXp;
			return this;
		}
		
		public Builder setActivateType(ActivateType activateType) {
			this.activateType = activateType;
			return this;
		}
		
		public Builder setResource(Resource resource) {
			this.resource = resource;
			return this;
		}
		
		public Builder setAnimations(StaticAnimation activateAnimation) {
			this.activateAnimation = activateAnimation;
			return this;
		}
	}
	
	public static Builder createBuilder(ResourceLocation resourceLocation) {
		return (new Builder(resourceLocation)).setCategory(SkillCategories.WEAPON_SPECIAL_ATTACK).setResource(Resource.SPECIAL_GAUAGE);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(TIMER);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(ASCENDING);
		container.getDataManager().registerData(SUPERARMOR);
		container.getDataManager().setData(ACTIVE,false);
		
		if (!container.getExecuter().isLogicalClient()) {
			this.setMaxDurationSynchronize((ServerPlayerPatch)container.getExecuter(), 3*20*60);
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(ASCENDING)) {
				if (event.getTarget().hasEffect(MobEffects.WITHER)) {
					((ServerLevel) event.getPlayerPatch().getOriginal().level).sendParticles( ParticleTypes.SOUL, 
							event.getTarget().getX(), 
							event.getTarget().getY() + 1.2D, 
							event.getTarget().getZ(), 
							48, 0.0D, 0.0D, 0.0D, 0.05D);
					event.getTarget().playSound(SoundEvents.WITHER_HURT, 1.5f, 0.5f);
					float WitherCatharsis = (float) (0.5f * (event.getTarget().getEffect(MobEffects.WITHER).getDuration()/20) * Math.pow(2 , event.getTarget().getEffect(MobEffects.WITHER).getAmplifier()));
					//container.getExecuter().getOriginal().sendMessage(new TextComponent("Damage dealt: " + WitherCatharsis + " on " + event.getTarget().getMaxHealth() + "/" + event.getTarget().getHealth()), UUID.randomUUID());
					//container.getExecuter().getOriginal().sendMessage(new TextComponent("Catarsis healing: " + WitherCatharsis/2 + " on " + event.getPlayerPatch().getOriginal().getMaxHealth() + "/" + event.getPlayerPatch().getOriginal().getHealth()), UUID.randomUUID());
					event.getTarget().hurt((DamageSource) event.getDamageSource(), WitherCatharsis);
					event.getPlayerPatch().getOriginal().setHealth(Math.min(event.getPlayerPatch().getOriginal().getHealth()
							+ WitherCatharsis/2, event.getPlayerPatch().getOriginal().getMaxHealth()));
					event.getTarget().removeEffect(MobEffects.WITHER);
					container.getDataManager().setDataSync(TIMER, container.getDataManager().getDataValue(TIMER) + ((3 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()))*20), event.getPlayerPatch().getOriginal());
				}
			}
			if (container.getDataManager().getDataValue(ACTIVE) && !container.getDataManager().getDataValue(ASCENDING)) {
				if (event.getTarget().hasEffect(MobEffects.WITHER)) {
					if (event.getTarget().getEffect(MobEffects.WITHER).getAmplifier() == 2) {
						((ServerLevel) event.getPlayerPatch().getOriginal().level).sendParticles( ParticleTypes.SOUL, 
								event.getTarget().getX(), 
								event.getTarget().getY() + 1.2D, 
								event.getTarget().getZ(), 
								48, 0.0D, 0.0D, 0.0D, 0.05D);
						event.getTarget().playSound(SoundEvents.WITHER_HURT, 1.5f, 0.5f);
						float WitherCatharsis = (float) (0.5f * (event.getTarget().getEffect(MobEffects.WITHER).getDuration()/20) * Math.pow(2 , event.getTarget().getEffect(MobEffects.WITHER).getAmplifier()));
						//container.getExecuter().getOriginal().sendMessage(new TextComponent("Damage dealt: " + WitherCatharsis + " on " + event.getTarget().getMaxHealth() + "/" + event.getTarget().getHealth()), UUID.randomUUID());
						//container.getExecuter().getOriginal().sendMessage(new TextComponent("Catarsis healing: " + WitherCatharsis/2 + " on " + event.getPlayerPatch().getOriginal().getMaxHealth() + "/" + event.getPlayerPatch().getOriginal().getHealth()), UUID.randomUUID());
						event.getTarget().hurt((DamageSource) event.getDamageSource(), WitherCatharsis);
						event.getPlayerPatch().getOriginal().setHealth(Math.min(event.getPlayerPatch().getOriginal().getHealth()
								+ WitherCatharsis/2, event.getPlayerPatch().getOriginal().getMaxHealth()));
						event.getTarget().removeEffect(MobEffects.WITHER);
						container.getDataManager().setDataSync(TIMER, container.getDataManager().getDataValue(TIMER) + 2*20, event.getPlayerPatch().getOriginal());
					}
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(ASCENDING)) {
				if (!event.getPlayerPatch().getSkill(SkillCategories.WEAPON_PASSIVE).isEmpty()) {
					event.getPlayerPatch().getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.ACTIVE, true, event.getPlayerPatch().getOriginal());					
				}
				event.getPlayerPatch().getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(DemonicAscensionSkill.ASCENDING, false, event.getPlayerPatch().getOriginal());
				event.getPlayerPatch().getSkill(SkillCategories.WEAPON_SPECIAL_ATTACK).getDataManager().setDataSync(DemonicAscensionSkill.SUPERARMOR, false, event.getPlayerPatch().getOriginal());
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(SUPERARMOR)) {
				event.getDamageSource().setStunType(StunType.NONE);
			}
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_POST, EVENT_UUID);
		if(!container.getExecuter().isLogicalClient()) {
			container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
		}
		container.deactivate();
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getSkill(this.category).isActivated()) { 
			cancelOnServer(executer, args);
			executer.getSkill(this.category).getDataManager().setDataSync(ACTIVE, false, executer.getOriginal());
			this.setDurationSynchronize(executer, 0);
			this.setStackSynchronize(executer, executer.getSkill(this.category).getStack() - 1);
			executer.getSkill(this.category).deactivate();
			executer.modifyLivingMotionByCurrentItem();
		} else {
			executer.playAnimationSynchronized(this.activateAnimation, 0);
			executer.getSkill(this.category).getDataManager().setData(TIMER, 12*20);
			executer.getSkill(this.category).getDataManager().setDataSync(SUPERARMOR, true, executer.getOriginal());
			executer.getSkill(this.category).activate();
			executer.modifyLivingMotionByCurrentItem();
		}
	}
	
	@Override
	public void cancelOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.cancelOnServer(executer, args);
		executer.getSkill(this.category).getDataManager().setDataSync(ACTIVE, false,executer.getOriginal());
		if (!executer.getSkill(SkillCategories.WEAPON_PASSIVE).isEmpty()) {
			if (executer.getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().getDataValue(DemonMarkPassiveSkill.ACTIVE)) {
				executer.getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.ACTIVE, false, executer.getOriginal());
			}
		}
		executer.getOriginal().removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
		this.setStackSynchronize(executer, executer.getSkill(this.category).getStack() - 1);
		this.setDurationSynchronize(executer, 0);
		executer.modifyLivingMotionByCurrentItem();
	}
	
	@Override
	public boolean canExecute(PlayerPatch<?> executer) {
		if (executer.isLogicalClient()) {
			return super.canExecute(executer);
		} else {
			return executer.getHoldingItemCapability(InteractionHand.MAIN_HAND).getSpecialAttack(executer) == this && executer.getOriginal().getVehicle() == null;
		}
	}
	
	@Override
	public SpecialAttackSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Auto attack :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(2), "Dash attack :");
		
		return list;
	}
	
	/*
	@OnlyIn(Dist.CLIENT)
	@Override
	public void onScreen(LocalPlayerPatch playerpatch, float resolutionX, float resolutionY) {
		if (playerpatch.getSkill(this.category).isActivated()) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, new ResourceLocation(EpicFightMod.MODID, "textures/gui/overlay/true_berserk.png"));
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
	*/
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(ACTIVE)) {
			if(!container.getExecuter().isLogicalClient()) {
				if (!container.getExecuter().getSkill(SkillCategories.WEAPON_PASSIVE).isEmpty()) {
					if (!container.getExecuter().getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().getDataValue(DemonMarkPassiveSkill.ACTIVE)) {
						container.getExecuter().getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().setDataSync(DemonMarkPassiveSkill.ACTIVE, true, (ServerPlayer) container.getExecuter().getOriginal());					
					}
				}
				if (container.getDataManager().getDataValue(TIMER) > 0) {
					container.getDataManager().setData(TIMER, container.getDataManager().getDataValue(TIMER)-1);
					this.setDurationSynchronize((ServerPlayerPatch) container.getExecuter(),(int)(container.getDataManager().getDataValue(TIMER)/20F));
				} else {
					container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
					this.setDurationSynchronize((ServerPlayerPatch) container.getExecuter(), 0);
					container.deactivate();
				}
			}
		}
	}
}