package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.JumpAttackAI;

public class Spider extends QuadrupedMob {
	public static final long serialVersionUID = 0L;

	public Spider(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.heightOffset = 0.72F;
		this.modelName = "spider";
		this.textureName = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.setPos(f2, f3, f4);
		this.deathScore = 105;
		this.bobStrength = 0.0F;
		this.ai = new JumpAttackAI();
	}
}