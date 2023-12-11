package com.mojang.minecraft.mob;

import com.mojang.minecraft.item.Arrow;
import com.mojang.minecraft.level.Level;

public class Skeleton extends Zombie {
	public static final long serialVersionUID = 0L;

	public Skeleton(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.modelName = "skeleton";
		this.textureName = "/mob/skeleton.png";
		Skeleton$1 skeleton$15 = new Skeleton$1(this);
		this.deathScore = 120;
		skeleton$15.runSpeed = 0.3F;
		skeleton$15.damage = 8;
		this.ai = skeleton$15;
	}

	public void shootArrow(Level level) {
		level.addEntity(new Arrow(level, this, this.x, this.y, this.z, this.yRot + 180.0F + (float)(Math.random() * 45.0D - 22.5D), this.xRot - (float)(Math.random() * 45.0D - 10.0D), 1.0F));
	}

	static void access$000(Skeleton skeleton0) {
		skeleton0 = skeleton0;
		int i1 = (int)((Math.random() + Math.random()) * 3.0D + 4.0D);

		for(int i2 = 0; i2 < i1; ++i2) {
			skeleton0.level.addEntity(new Arrow(skeleton0.level, skeleton0.level.getPlayer(), skeleton0.x, skeleton0.y - 0.2F, skeleton0.z, (float)Math.random() * 360.0F, -((float)Math.random()) * 60.0F, 0.4F));
		}

	}
}