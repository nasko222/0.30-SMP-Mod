package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

import java.util.Random;

public final class Mushroom extends Flower {
	protected Mushroom(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.2F;
		this.setShape(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 2.0F, f3 + 0.5F);
	}

	public final void tick(Level level, int x, int y, int z, Random random) {
		int i6 = level.getTile(x, y - 1, z);
		if(level.isLit(x, y, z) || i6 != Tile.rock.id && i6 != Tile.gravel.id && i6 != Tile.stoneBrick.id) {
			level.setTile(x, y, z, 0);
		}

	}
}