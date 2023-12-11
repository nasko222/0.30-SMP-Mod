package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

import java.util.Random;

public final class Bush extends Flower {
	protected Bush(int i1, int i2) {
		super(6, 15);
		float f3 = 0.4F;
		this.setShape(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 2.0F, f3 + 0.5F);
	}

	public final void tick(Level level, int x, int y, int z, Random random) {
		int i6 = level.getTile(x, y - 1, z);
		if(level.isLit(x, y, z) && (i6 == Tile.dirt.id || i6 == Tile.grass.id)) {
			if(random.nextInt(5) == 0) {
				level.setTileNoUpdate(x, y, z, 0);
				if(!level.maybeGrowTree(x, y, z)) {
					level.setTileNoUpdate(x, y, z, this.id);
				}
			}

		} else {
			level.setTile(x, y, z, 0);
		}
	}
}