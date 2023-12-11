package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;

import org.lwjgl.opengl.GL11;

public class HumanoidMob extends Mob {
	public static final long serialVersionUID = 0L;
	public boolean helmet = Math.random() < (double)0.2F;
	public boolean armor = Math.random() < (double)0.2F;

	public HumanoidMob(Level level, float x, float y, float z) {
		super(level);
		this.modelName = "humanoid";
		this.setPos(x, y, z);
	}

	public void renderModel(Textures textures, float x, float y, float z, float xr, float yr, float zr) {
		super.renderModel(textures, x, y, z, xr, yr, zr);
		BaseModel x1 = modelCache.getModel(this.modelName);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if(this.allowAlpha) {
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.hasHair) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			HumanoidModel y1;
			(y1 = (HumanoidModel)x1).hair.yRot = y1.head.yRot;
			y1.hair.xRot = y1.head.xRot;
			y1.hair.render(zr);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		if(this.armor || this.helmet) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.loadTexture("/armor/plate.png"));
			GL11.glDisable(GL11.GL_CULL_FACE);
			HumanoidModel textures1;
			(textures1 = (HumanoidModel)modelCache.getModel("humanoid.armor")).head.showModel = this.helmet;
			textures1.body.showModel = this.armor;
			textures1.rightArm.showModel = this.armor;
			textures1.leftArm.showModel = this.armor;
			textures1.rightLeg.showModel = false;
			textures1.leftLeg.showModel = false;
			HumanoidModel z1 = (HumanoidModel)x1;
			textures1.head.yRot = z1.head.yRot;
			textures1.head.xRot = z1.head.xRot;
			textures1.rightArm.xRot = z1.rightArm.xRot;
			textures1.rightArm.zRot = z1.rightArm.zRot;
			textures1.leftArm.xRot = z1.leftArm.xRot;
			textures1.leftArm.zRot = z1.leftArm.zRot;
			textures1.rightLeg.xRot = z1.rightLeg.xRot;
			textures1.leftLeg.xRot = z1.leftLeg.xRot;
			textures1.head.render(zr);
			textures1.body.render(zr);
			textures1.rightArm.render(zr);
			textures1.leftArm.render(zr);
			textures1.rightLeg.render(zr);
			textures1.leftLeg.render(zr);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}
}