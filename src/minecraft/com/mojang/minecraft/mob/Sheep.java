package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.QuadrupedModel;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Textures;

import org.lwjgl.opengl.GL11;

public class Sheep extends QuadrupedMob {
	public static final long serialVersionUID = 0L;
	public boolean hasFur = true;
	public boolean grazing = false;
	public int grazingTime = 0;
	public float graze;
	public float grazeO;

	public Sheep(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.setSize(1.4F, 1.72F);
		this.setPos(f2, f3, f4);
		this.heightOffset = 1.72F;
		this.modelName = "sheep";
		this.textureName = "/mob/sheep.png";
		this.ai = new Sheep$1(this);
	}

	public void aiStep() {
		super.aiStep();
		this.grazeO = this.graze;
		if(this.grazing) {
			this.graze += 0.2F;
		} else {
			this.graze -= 0.2F;
		}

		if(this.graze < 0.0F) {
			this.graze = 0.0F;
		}

		if(this.graze > 1.0F) {
			this.graze = 1.0F;
		}

	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 10);
		}

		int i2 = (int)(Math.random() + Math.random() + 1.0D);


		super.die(entity1);
	}

	public void hurt(Entity entity1, int i2) {
		if(this.hasFur && entity1 instanceof Player) {
			this.hasFur = false;
			int i3 = (int)(Math.random() * 3.0D + 1.0D);


		} else {
			super.hurt(entity1, i2);
		}
	}

	public void renderModel(Textures textures, float x, float y, float z, float xr, float yr, float zr) {
		QuadrupedModel quadrupedModel8;
		float f9 = (quadrupedModel8 = (QuadrupedModel)modelCache.getModel(this.modelName)).head.y;
		float f10 = quadrupedModel8.head.z;
		quadrupedModel8.head.y += (this.grazeO + (this.graze - this.grazeO) * y) * 8.0F;
		quadrupedModel8.head.z -= this.grazeO + (this.graze - this.grazeO) * y;
		super.renderModel(textures, x, y, z, xr, yr, zr);
		if(this.hasFur) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.loadTexture("/mob/sheep_fur.png"));
			GL11.glDisable(GL11.GL_CULL_FACE);
			QuadrupedModel quadrupedModel11;
			(quadrupedModel11 = (QuadrupedModel)modelCache.getModel("sheep.fur")).head.yRot = quadrupedModel8.head.yRot;
			quadrupedModel11.head.xRot = quadrupedModel8.head.xRot;
			quadrupedModel11.head.y = quadrupedModel8.head.y;
			quadrupedModel11.head.x = quadrupedModel8.head.x;
			quadrupedModel11.body.yRot = quadrupedModel8.body.yRot;
			quadrupedModel11.body.xRot = quadrupedModel8.body.xRot;
			quadrupedModel11.leg1.xRot = quadrupedModel8.leg1.xRot;
			quadrupedModel11.leg2.xRot = quadrupedModel8.leg2.xRot;
			quadrupedModel11.leg3.xRot = quadrupedModel8.leg3.xRot;
			quadrupedModel11.leg4.xRot = quadrupedModel8.leg4.xRot;
			quadrupedModel11.head.render(zr);
			quadrupedModel11.body.render(zr);
			quadrupedModel11.leg1.render(zr);
			quadrupedModel11.leg2.render(zr);
			quadrupedModel11.leg3.render(zr);
			quadrupedModel11.leg4.render(zr);
		}

		quadrupedModel8.head.y = f9;
		quadrupedModel8.head.z = f10;
	}
}
