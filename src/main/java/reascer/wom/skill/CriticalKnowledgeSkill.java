package reascer.wom.skill;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class CriticalKnowledgeSkill extends PassiveSkill {
	private static final UUID EVENT_UUID = UUID.fromString("1d807798-1a3f-11ed-861d-0242ac120002");
	public CriticalKnowledgeSkill(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
			int thorn = 0;
			for (ItemStack ArmorPiece : container.getExecuter().getOriginal().getArmorSlots()) {
				thorn += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, ArmorPiece);
			}
			
			int chance = Math.abs(new Random().nextInt()) % 100;
			//container.getExecuter().getOriginal().sendMessage(new TextComponent("chance: " + chance + " | thorn: " + thorn + " | Critchance: " + (((thorn/12f)*90f)+5f)), UUID.randomUUID());
			if (chance < (((thorn/12f)*80f)+20f)) {
				event.getTarget().hurt((DamageSource) event.getDamageSource(), event.getAttackDamage()/2);
				if(!event.getPlayerPatch().isLogicalClient()) {
					ServerPlayerPatch executer = (ServerPlayerPatch) event.getPlayerPatch();
					event.getTarget().playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
					event.getTarget().playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);
					((ServerLevel) executer.getOriginal().level).sendParticles( ParticleTypes.ELECTRIC_SPARK,
							event.getTarget().getX(), 
							event.getTarget().getY() + 1.2D, 
							event.getTarget().getZ(), 
							40, 0.0D, 0.0D, 0.0D, 1.5D);
				}
			}
        });
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
	}
}