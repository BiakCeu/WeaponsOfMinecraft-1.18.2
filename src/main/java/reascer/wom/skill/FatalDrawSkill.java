package reascer.wom.skill;

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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.gameasset.EFAnimations;
import reascer.wom.main.WeaponOfMinecraft;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class FatalDrawSkill extends ConditionalWeaponInnateSkill {
	private static final SkillDataKey<Boolean> ACTIVE = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	private static final SkillDataKey<Integer> COOLDOWN = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	private static final SkillDataKey<Boolean> SECOND_DRAW = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);

	public FatalDrawSkill(Builder<? extends Skill> builder) {
		super(builder, (executer) -> {
			if (executer.getOriginal().isSprinting()) {
				return 2;
			} else if (executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(ACTIVE)) {
				if (executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().getDataValue(SECOND_DRAW)) {
					executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, false, executer.getOriginal());
					return 1;
				} else {
					executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, true, executer.getOriginal());
					return 0;
				}
			} else {
				executer.getSkill(SkillCategories.WEAPON_INNATE).getDataManager().setDataSync(SECOND_DRAW, true, executer.getOriginal());
				return 0;
			}
		}, EFAnimations.KATANA_FATAL_DRAW, EFAnimations.KATANA_FATAL_DRAW_SECOND, EFAnimations.KATANA_FATAL_DRAW_DASH);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(ACTIVE);
		container.getDataManager().registerData(SECOND_DRAW);
		container.getDataManager().registerData(COOLDOWN);
		if(!container.getExecuter().isLogicalClient()) {
			container.getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch)container.getExecuter()).getOriginal());
			container.getDataManager().setDataSync(COOLDOWN, 0,((ServerPlayerPatch)container.getExecuter()).getOriginal());
		}
	}

	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		if (executer.getSkill(this.category).getDataManager().getDataValue(COOLDOWN) < 50) {
			executer.getSkill(this.category).getDataManager().setData(COOLDOWN, 60);
			boolean isSheathed = executer.getSkill(SkillCategories.WEAPON_PASSIVE).getDataManager().getDataValue(EFKatanaPassive.SHEATH);
			if (isSheathed || executer.getSkill(this.category).getDataManager().getDataValue(ACTIVE)) {
				executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], -0.45F);
			} else {
				executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
			}
			
			this.setConsumptionSynchronize(executer, 0);
			this.setDurationSynchronize(executer, 0);
			if (!executer.getSkill(this.category).getDataManager().getDataValue(ACTIVE)) {
				//executer.getOriginal().sendMessage(new TextComponent("katana stack:"+Math.round(Math.min(6,executer.getSkill(this.category).getStack()+1) * (1.0f + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())/3.0f))-1)), UUID.randomUUID());
				this.setStackSynchronize(executer, (int) Math.min(12,Math.round(Math.min(6,executer.getSkill(this.category).getStack()+1)* (1.0f + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING_EDGE, executer.getOriginal())/3.0f)))-1));
				executer.getSkill(this.category).getDataManager().setDataSync(ACTIVE, true, executer.getOriginal());
			}
			if (executer.getSkill(this.category).getStack() == 0) {
				executer.getSkill(this.category).getDataManager().setDataSync(ACTIVE, false, executer.getOriginal());
			}
			executer.getSkill(this.category).activate();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void onScreen(LocalPlayerPatch playerpatch, float resolutionX, float resolutionY) {
		if (playerpatch.getSkill(this.category).getDataManager().getDataValue(ACTIVE)) {
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
					container.getExecuter().getSkill(this.category).getDataManager().setDataSync(ACTIVE, false,((ServerPlayerPatch)container.getExecuter()).getOriginal());
				}
			}
		}
	
	}
}