package reascer.wom.skill;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

public class RuinePassive extends PassiveSkill {
	public RuinePassive(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean shouldDraw(SkillContainer container) {
		if (container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getSkill() instanceof PlunderPerditionSkill) {
			return container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(PlunderPerditionSkill.TIMER) > 0;
		}
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, PoseStack matStackIn, float x, float y, float scale, int width, int height) {
		matStackIn.pushPose();
		matStackIn.scale(scale, scale, 1.0F);
		matStackIn.translate(0, (float)gui.getSlidingProgression() * 1.0F / scale, 0);
		RenderSystem.setShaderTexture(0, container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getSkill().getSkillTexture());
		float scaleMultiply = 1.0f / scale;
		gui.drawTexturedModalRectFixCoord(matStackIn.last().pose(), (width - x) * scaleMultiply, (height - y) * scaleMultiply, 0, 0, 255, 255);
		matStackIn.scale(scaleMultiply, scaleMultiply, 1.0F);
		gui.font.drawShadow(matStackIn, String.valueOf(container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(PlunderPerditionSkill.TIMER)/20), ((float)width - x+4), ((float)height - y+13), 16777215);
		gui.font.drawShadow(matStackIn, (String.valueOf((int)((container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(PlunderPerditionSkill.STRENGHT)/40.0f)*100f)) + "%"), ((float)width - x+4), ((float)height - y+4), 16777215);
		matStackIn.popPose();
	}
}