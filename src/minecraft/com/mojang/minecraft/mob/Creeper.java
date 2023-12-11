package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;

import util.Mth;

public class Creeper extends Mob {
	public static final long serialVersionUID = 0L;

	public Creeper(Level level, float x, float y, float z) {
		super(level);
		this.heightOffset = 1.62F;
		this.modelName = "creeper";
		this.textureName = "/mob/creeper.png";
		this.ai = new Creeper$1(this);
		this.ai.defaultLookAngle = 45;
		this.deathScore = 200;
		this.setPos(x, y, z);
	}

	public float getBrightness(float f1) {
		float f2 = (float)(20 - this.health) / 20.0F;
		return ((Mth.sin((float)this.tickCount + f1) * 0.5F + 0.5F) * f2 * 0.5F + 0.25F + f2 * 0.25F) * super.getBrightness(f1);
	}
}