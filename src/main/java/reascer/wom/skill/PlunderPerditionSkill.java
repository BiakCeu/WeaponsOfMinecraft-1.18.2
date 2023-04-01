package reascer.wom.skill;

import java.util.List;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;


public class PlunderPerditionSkill extends WeaponInnateSkill{
	private static final UUID EVENT_UUID = UUID.fromString("b9d719ba-bcb8-11ec-8422-0242ac120002");
	private static final SkillDataKey<Boolean> BUFFED = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Boolean> BUFFING = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> TIMER = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Integer> STRENGHT = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	protected final StaticAnimation attackAnimation;
	protected Boolean registerdata = true;
	
	public AttributeModifier stolen_move_speed;
	public AttributeModifier stolen_attack_speed;
	
	public PlunderPerditionSkill(Builder<? extends Skill> builder) {
		super(builder);
		this.attackAnimation = WOMAnimations.RUINE_PLUNDER;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getDataManager().registerData(BUFFED);
		container.getDataManager().registerData(BUFFING);
		container.getDataManager().registerData(STRENGHT);
		
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
					if (container.getDataManager().getDataValue(STRENGHT) < 40) {
						container.getDataManager().setData(STRENGHT, container.getDataManager().getDataValue(STRENGHT)+1);
					}
					
					event.getPlayerPatch().setStamina(event.getPlayerPatch().getStamina() + (event.getPlayerPatch().getMaxStamina() * 0.05f));
					event.getPlayerPatch().getOriginal().heal(1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal()));
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles(ParticleTypes.REVERSE_PORTAL, 
							event.getTarget().xo, 
							event.getTarget().yo + 0.2f, 
							event.getTarget().zo, 
							20, 0, 0, 0, 0.4);
					((ServerLevel) container.getExecuter().getOriginal().level).sendParticles(ParticleTypes.PORTAL, 
							event.getTarget().xo, 
							event.getTarget().yo+ 0.2f, 
							event.getTarget().zo, 
							20, 0, 0, 0, 0.4);
					 event.getPlayerPatch().getOriginal().level.playSound(null, container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(),
				    			SoundEvents.CHAIN_BREAK, container.getExecuter().getOriginal().getSoundSource(), 2.0F, 0.5F);
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
			if (event.getAnimationId() == WOMAnimations.RUINE_PLUNDER.getId() && container.getDataManager().getDataValue(STRENGHT) > 0) {
				if (!container.getExecuter().isLogicalClient()) {
					container.getDataManager().setDataSync(BUFFED, true, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(BUFFING, false, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
					container.getDataManager().setDataSync(TIMER, 200 * (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, container.getExecuter().getOriginal())),((ServerPlayerPatch) container.getExecuter()).getOriginal());
					stolen_move_speed = new AttributeModifier(EVENT_UUID, "ruine.stolen_move_speed", (( 0.03D * container.getDataManager().getDataValue(STRENGHT))), Operation.MULTIPLY_TOTAL);
					stolen_attack_speed = new AttributeModifier(EVENT_UUID, "ruine.stolen_attack_speed", (( 0.015D * container.getDataManager().getDataValue(STRENGHT))), Operation.MULTIPLY_TOTAL);
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(stolen_move_speed);
					container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).removeModifier(stolen_attack_speed);
					
					container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(stolen_move_speed);
					container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).addPermanentModifier(stolen_attack_speed);
				}
			}
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			if (event.getAnimation().getId() != WOMAnimations.RUINE_PLUNDER.getId()) {
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
		container.getExecuter().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(stolen_move_speed);
		container.getExecuter().getOriginal().getAttribute(Attributes.ATTACK_SPEED).removeModifier(stolen_attack_speed);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.executeOnServer(executer, args);
		executer.playAnimationSynchronized(this.attackAnimation, 0);
		executer.getSkill(this).getDataManager().setData(BUFFING, true);
		executer.getSkill(this).getDataManager().setData(BUFFED, false);
		executer.getSkill(this).getDataManager().setData(STRENGHT, 0);
		executer.getSkill(this).getDataManager().setData(TIMER, 200 * (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())));
		executer.getSkill(this).deactivate();
	}
	
	@Override
	public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
		List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Thrust :");
		this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Rip out :");
		
		return list;
	}
	
	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		return this;
	}
	
	@Override
	public void cancelOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		super.cancelOnServer(executer, args);
		executer.getSkill(this).getDataManager().setData(BUFFED, false);
		executer.getSkill(this).getDataManager().setData(STRENGHT, 0);
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
						((ServerLevel) container.getExecuter().getOriginal().level).sendParticles( container.getDataManager().getDataValue(STRENGHT) == 40 ? ParticleTypes.END_ROD : ParticleTypes.SOUL_FIRE_FLAME, 
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