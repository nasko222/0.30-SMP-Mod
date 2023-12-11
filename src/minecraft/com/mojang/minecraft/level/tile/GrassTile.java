package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

import java.util.Random;

public final class GrassTile extends Tile {
	protected GrassTile(int i1) {
		super(2);
		this.tex = 3;
		this.setTicking(true);
	}

	protected final int getTexture(int face) {
		return face == 1 ? 0 : (face == 0 ? 2 : 3);
	}

	public final void tick(Level level, int x, int y, int z, Random random) {
		if(random.nextInt(4) == 0) {
			if(!level.isLit(x, y, z)) {
				level.setTile(x, y, z, Tile.dirt.id);
			} else {
				for(int i9 = 0; i9 < 4; ++i9) {
					int i6 = x + random.nextInt(3) - 1;
					int i7 = y + random.nextInt(5) - 3;
					int i8 = z + random.nextInt(3) - 1;
					if(level.getTile(i6, i7, i8) == Tile.dirt.id && level.isLit(i6, i7, i8)) {
						level.setTile(i6, i7, i8, Tile.grass.id);
					}
				}

			}
		}
	}

	public final int getId() {
		return Tile.dirt.getId();
	}
}