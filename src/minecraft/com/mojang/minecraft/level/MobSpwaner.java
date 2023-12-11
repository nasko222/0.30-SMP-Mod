package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.mob.Creeper;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.Pig;
import com.mojang.minecraft.mob.Sheep;
import com.mojang.minecraft.mob.Skeleton;
import com.mojang.minecraft.mob.Spider;
import com.mojang.minecraft.mob.Zombie;

public final class MobSpwaner {
	public Level level;

	public MobSpwaner(Level level) {
		this.level = level;
	}

	public final int spawnMobs(int count, Entity entity, LevelLoaderListener levelLoaderListener) {
		int i4 = 0;

		for(int i5 = 0; i5 < count; ++i5) {
			if(levelLoaderListener != null) {
				levelLoaderListener.setLoadingProgress(i5 * 100 / (count - 1));
			}

			int i6 = this.level.random.nextInt(6);
			int i7 = this.level.random.nextInt(this.level.width);
			int i8 = (int)(Math.min(this.level.random.nextFloat(), this.level.random.nextFloat()) * (float)this.level.depth);
			int i9 = this.level.random.nextInt(this.level.height);
			if(!this.level.isSolidTile(i7, i8, i9) && this.level.getLiquid(i7, i8, i9) == Liquid.none && (!this.level.isLit(i7, i8, i9) || this.level.random.nextInt(5) == 0)) {
				for(int i10 = 0; i10 < 3; ++i10) {
					int i11 = i7;
					int i12 = i8;
					int i13 = i9;

					for(int i14 = 0; i14 < 3; ++i14) {
						i11 += this.level.random.nextInt(6) - this.level.random.nextInt(6);
						i12 += this.level.random.nextInt(1) - this.level.random.nextInt(1);
						i13 += this.level.random.nextInt(6) - this.level.random.nextInt(6);
						if(i11 >= 0 && i13 >= 1 && i12 >= 0 && i12 < this.level.depth - 2 && i11 < this.level.width && i13 < this.level.height && this.level.isSolidTile(i11, i12 - 1, i13) && !this.level.isSolidTile(i11, i12, i13) && !this.level.isSolidTile(i11, i12 + 1, i13)) {
							float f15 = (float)i11 + 0.5F;
							float f16 = (float)i12 + 1.0F;
							float f17 = (float)i13 + 0.5F;
							float f18;
							float f19;
							float f20;
							if(entity != null) {
								f18 = f15 - entity.x;
								f19 = f16 - entity.y;
								f20 = f17 - entity.z;
								if(f18 * f18 + f19 * f19 + f20 * f20 < 256.0F) {
									continue;
								}
							} else {
								f18 = f15 - (float)this.level.xSpawn;
								f19 = f16 - (float)this.level.ySpawn;
								f20 = f17 - (float)this.level.zSpawn;
								if(f18 * f18 + f19 * f19 + f20 * f20 < 256.0F) {
									continue;
								}
							}

							Object object21 = null;
							if(i6 == 0) {
								object21 = new Zombie(this.level, f15, f16, f17);
							}

							if(i6 == 1) {
								object21 = new Skeleton(this.level, f15, f16, f17);
							}

							if(i6 == 2) {
								object21 = new Pig(this.level, f15, f16, f17);
							}

							if(i6 == 3) {
								object21 = new Creeper(this.level, f15, f16, f17);
							}

							if(i6 == 4) {
								object21 = new Spider(this.level, f15, f16, f17);
							}

							if(i6 == 5) {
								object21 = new Sheep(this.level, f15, f16, f17);
							}

							if(this.level.isFree(((Mob)object21).bb)) {
								++i4;
								this.level.addEntity((Entity)object21);
							}
						}
					}
				}
			}
		}

		return i4;
	}
}