package reascer.wom.skill;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class MindSetSkill extends PassiveSkill {
	private static final UUID EVENT_UUID = UUID.fromString("417b1eb8-05e1-11ed-b939-0242ac120002");
	public float lifeThreshhold;
	public float DamageIncrease;
	public float DamageReduciton;
	
	public MindSetSkill(Builder<? extends Skill> builder) {
		super(builder);
		lifeThreshhold = 0.8f;
		DamageIncrease = 1.3f;
		DamageReduciton = 0.8f;
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		container.getExecuter().getEventListener().addEventListener(EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, (event) -> {
			if (event.getPlayerPatch().getOriginal().getHealth() > event.getPlayerPatch().getOriginal().getMaxHealth() * lifeThreshhold) {
				event.setDamage(event.getDamage() * DamageIncrease);
			}
        });
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
			if (event.getPlayerPatch().getOriginal().getHealth() > event.getPlayerPatch().getOriginal().getMaxHealth() * lifeThreshhold) {
				event.setAmount(event.getAmount() * DamageReduciton);
			}
        });
	}
	
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_POST, EVENT_UUID);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean shouldDraw(SkillContainer container) {
		return container.getExecuter().getOriginal().getHealth() > container.getExecuter().getOriginal().getMaxHealth() * lifeThreshhold;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, PoseStack matStackIn, float x, float y, float scale, int width, int height) {
		matStackIn.pushPose();
		matStackIn.scale(scale, scale, 1.0F);
		matStackIn.translate(0, (float)gui.getSlidingProgression() * 1.0F / scale, 0);
		RenderSystem.setShaderTexture(0, this.getSkillTexture());
		float scaleMultiply = 1.0f / scale;
		gui.drawTexturedModalRectFixCoord(matStackIn.last().pose(), (width - x) * scaleMultiply, (height - y) * scaleMultiply, 0, 0, 255, 255);
		matStackIn.scale(scaleMultiply, scaleMultiply, 1.0F);
		matStackIn.popPose();
	}
}