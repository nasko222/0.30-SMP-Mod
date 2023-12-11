package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;

public class Pig extends QuadrupedMob {
	public static final long serialVersionUID = 0L;

	public Pig(Level level1, float f2, float f3, float f4) {
		super(level1, f2, f3, f4);
		this.heightOffset = 1.72F;
		this.modelName = "pig";
		this.textureName = "/mob/pig.png";
	}

	public void die(Entity entity1) {
		if(entity1 != null) {
			entity1.awardKillScore(this, 10);
		}

		int i2 = (int)(Math.random() + Math.random() + 1.0D);
		super.die(entity1);
	}
}