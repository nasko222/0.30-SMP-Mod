package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.particle.TerrainParticle;

import util.Mth;

final class Creeper$1 extends BasicAttackAI {
	public static final long serialVersionUID = 0L;
	final Creeper this$0;

	Creeper$1(Creeper creeper1) {
		this.this$0 = creeper1;
	}

	public final boolean attack(Entity entity1) {
		if(!super.attack(entity1)) {
			return false;
		} else {
			this.mob.hurt(entity1, 6);
			return true;
		}
	}

	public final void beforeRemove() {
		float f1 = 4.0F;
		this.level.explode(this.mob, this.mob.x, this.mob.y, this.mob.z, f1);

		for(int i2 = 0; i2 < 500; ++i2) {
			float f3 = (float)this.random.nextGaussian() * f1 / 4.0F;
			float f4 = (float)this.random.nextGaussian() * f1 / 4.0F;
			float f5 = (float)this.random.nextGaussian() * f1 / 4.0F;
			float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
			float f7 = f3 / f6 / f6;
			float f8 = f4 / f6 / f6;
			f6 = f5 / f6 / f6;
			this.level.particleEngine.addParticle(new TerrainParticle(this.level, this.mob.x + f3, this.mob.y + f4, this.mob.z + f5, f7, f8, f6, Tile.leaf));
		}

	}
}
