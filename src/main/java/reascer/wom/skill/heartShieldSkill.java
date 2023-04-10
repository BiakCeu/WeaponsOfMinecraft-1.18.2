package reascer.wom.skill;

import java.util.Random;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class heartShieldSkill extends PassiveSkill {
	public static final SkillDataKey<Integer> MAX_SHIELD = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> RECOVERY_COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> RECOVERY_RATE = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	
	private static final UUID EVENT_UUID = UUID.fromString("42580b91-53a6-4d7f-92b4-487aa585cd0b");
	public heartShieldSkill(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(MAX_SHIELD);
		container.getDataManager().registerData(RECOVERY_COOLDOWN);
		container.getDataManager().registerData(RECOVERY_RATE);

		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			container.getDataManager().setDataSync(RECOVERY_COOLDOWN, 100, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
		});
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		super.onRemoved(container);
		container.getExecuter().getOriginal().setAbsorptionAmount(0);
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean shouldDraw(SkillContainer container) {
		return container.getDataManager().getDataValue(RECOVERY_COOLDOWN) > 0 || container.getExecuter().getOriginal().getAbsorptionAmount() < container.getDataManager().getDataValue(MAX_SHIELD);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, PoseStack matStackIn, float x, float y, float scale, int width, int height) {
		matStackIn.pushPose();
		matStackIn.scale(scale, scale, 1.0F);
		matStackIn.translate(0, (float)gui.getSlidingProgression() * 1.0F / scale, 0);
		RenderSystem.setShaderTexture(0, this.getSkillTexture());
		float scaleMultiply = 1.0f / scale;
		if (container.getDataManager().getDataValue(RECOVERY_COOLDOWN) > 0) {
			RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.5F);
		} else {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
		gui.drawTexturedModalRectFixCoord(matStackIn.last().pose(), (width - x) * scaleMultiply, (height - y) * scaleMultiply, 0, 0, 255, 255);
		matStackIn.scale(scaleMultiply, scaleMultiply, 1.0F);
		if (container.getDataManager().getDataValue(RECOVERY_COOLDOWN) > 0) {
			gui.font.drawShadow(matStackIn, String.valueOf((container.getDataManager().getDataValue(RECOVERY_COOLDOWN)/20)+1), ((float)width - x+7), ((float)height - y+13), 16777215);
		}
		matStackIn.popPose();
	}
	
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		if (!container.getExecuter().isLogicalClient()) {
			if (container.getDataManager().getDataValue(RECOVERY_COOLDOWN) > 0) {
				container.getDataManager().setDataSync(RECOVERY_COOLDOWN, container.getDataManager().getDataValue(RECOVERY_COOLDOWN) -1, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
				container.getDataManager().setDataSync(MAX_SHIELD, (int) container.getExecuter().getOriginal().getMaxHealth()/3, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
			} else {
				if (container.getExecuter().getOriginal().getAbsorptionAmount() < container.getDataManager().getDataValue(MAX_SHIELD)) {
					if (container.getDataManager().getDataValue(RECOVERY_RATE) > 0) {
						container.getDataManager().setDataSync(RECOVERY_RATE, container.getDataManager().getDataValue(RECOVERY_RATE) -1, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
					} else {
						int thorn = 0;
						for (ItemStack ArmorPiece : container.getExecuter().getOriginal().getArmorSlots()) {
							thorn += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, ArmorPiece);
						}
						container.getDataManager().setDataSync(RECOVERY_RATE, 40 / (1 + (thorn/4)) , ((ServerPlayerPatch) container.getExecuter()).getOriginal());
						if (container.getExecuter().getOriginal().getAbsorptionAmount()+1 >= container.getDataManager().getDataValue(MAX_SHIELD)) {
							container.getExecuter().getOriginal().setAbsorptionAmount(container.getDataManager().getDataValue(MAX_SHIELD));
						} else {
							container.getExecuter().getOriginal().setAbsorptionAmount(container.getExecuter().getOriginal().getAbsorptionAmount()+1);
						}
					}
				}
			}
		}
	}
}