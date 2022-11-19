package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.particle.EFEpicFightParticles;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.ExtendedDamageSource.StunType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SpecialAttackSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;


public class PlunderPerditionSkill extends SpecialAttackSkill {
	private static final UUID EVENT_UUID = UUID.fromString("b9d719ba-bcb8-11ec-8422-0242ac120002");
	private static final SkillDataKey<Boolean> BUFFED = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> BUFFING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> STRENGHT = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	protected final StaticAnimation attackAnimation;
	protected Boolean registerdata = true;
	
	public PlunderPerditionSkill(Builder<? extends Skill> builder) {
		super(builder);
		this.attackAnimation = EFAnimations.RUINE_PLUNDER;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(BUFFED);
		container.getDataManager().registerData(BUFFING);
		container.getDataManager().registerData(STRENGHT);
		
		container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D);
		container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
		
		container.getDataManager().registerData(TIMER);
		if (!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setData(TIMER, 200 * (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal())));
		}
		
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(BUFFING)) {
				event.getDamageSource().setStunType(StunType.NONE);
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			if (container.getDataManager().getDataValue(BUFFING)) {
				if (!container.getDataManager().getDataValue(BUFFED)) {
					container.getDataManager().setData(STRENGHT, container.getDataManager().getDataValue(STRENGHT)+1);
					event.getPlayerPatch().setStamina(event.getPlayerPatch().getStamina() + (event.getPlayerPatch().getMaxStamina() * 0.05f));
					event.getPlayerPatch().getOriginal().setHealth(event.getPlayerPatch().getOriginal().getHealth() + (event.getPlayerPatch().getOriginal().getMaxHealth() * 0.05f));
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles(ParticleTypes.REVERSE_PORTAL, 
							event.getTarget().xo, 
							event.getTarget().yo + 1.0D, 
							event.getTarget().zo, 
							40, 0, 0, 0, 0.4);
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles(ParticleTypes.PORTAL, 
							event.getPlayerPatch().getOriginal().xo, 
							event.getPlayerPatch().getOriginal().yo + 1.0D, 
							event.getPlayerPatch().getOriginal().zo, 
							10, 0, 0, 0, 0.8);
					container.getExecuter().playSound(SoundEvents.CHAIN_BREAK, 2.0f, 1, 1);
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			container.getExecuter().getOriginal().level.addAlwaysVisibleParticle(EFEpicFightParticles.RUINE_PLUNDER_SWORD.get(), event.getTarget().xo, event.getTarget().yo, event.getTarget().zo, 0, 0, 0);
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
			if (event.getAnimationId() == EFAnimations.RUINE_PLUNDER.getId() && container.getDataManager().getDataValue(STRENGHT) > 0) {
				if (!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(BUFFED, true, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(BUFFING, false, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(TIMER, 200 * (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal())),((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D);
					container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * ( 1 + ( 0.03D * container.getDataManager().getDataValue(STRENGHT))));
					container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).setBaseValue(Math.min(5.0F,container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).getBaseValue() * ( 1 + ( 0.015D * container.getDataManager().getDataValue(STRENGHT)))));
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation().getId() != EFAnimations.RUINE_PLUNDER.getId()) {
				container.getDataManager().setData(BUFFING, false);
			}
		});
		
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getDataManager().setData(BUFFED, false);
		container.getDataManager().setData(STRENGHT, 0);
		container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D);
		container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
		executer.playAnimationSynchronized(this.attackAnimation, 0);
		executer.getSkill(this.category).getDataManager().setData(BUFFING, true);
		executer.getSkill(this.category).getDataManager().setData(BUFFED, false);
		executer.getSkill(this.category).getDataManager().setData(STRENGHT, 0);
		executer.getSkill(this.category).getDataManager().setData(TIMER, 200 * (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())));
		executer.getSkill(this.category).deactivate();
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Plunder :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(2), "Harvest :");
		
		return list;
	}
	
	@Override
	public SpecialAttackSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public void cancelOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.cancelOnServer(executer, args);
		executer.getSkill(this.category).getDataManager().setData(BUFFED, false);
		executer.getSkill(this.category).getDataManager().setData(STRENGHT, 0);
		executer.getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D);
		executer.getOriginal().getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (container.getDataManager().getDataValue(BUFFED)) {
			if (container.getDataManager().getDataValue(TIMER) > 0) {
				if(!container.getExecuter().isLogicalClient()) {
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( ParticleTypes.REVERSE_PORTAL, 
							container.getExecuter().getOriginal().getX() - 0.15D, 
							container.getExecuter().getOriginal().getY() + 1.05D, 
							container.getExecuter().getOriginal().getZ() - 0.15D, 
							4, 0.3D, 0.4D, 0.3D, 0.05);
					if (container.getDataManager().getDataValue(TIMER) % 20 == 0) {
						((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).getBaseValue() == 5.0D ? ParticleTypes.END_ROD : ParticleTypes.SOUL_FIRE_FLAME, 
								container.getExecuter().getOriginal().getX() - 0.15D, 
								container.getExecuter().getOriginal().getY() + 1.05D, 
								container.getExecuter().getOriginal().getZ() - 0.15D, 
								container.getDataManager().getDataValue(STRENGHT), 0.3D, 0.4D, 0.3D, 0.01);
					}
					container.getDataManager().setDataSync(TIMER, container.getDataManager().getDataValue(TIMER)-1,((ServerPlayerPatch) container.getExecuter()).getOriginal());
				}
			} else {
				if(!container.getExecuter().isLogicalClient()) {
					container.getSkill().cancelOnServer((ServerPlayerPatch)container.getExecuter(), null);
				}
			}
		}
	}
}