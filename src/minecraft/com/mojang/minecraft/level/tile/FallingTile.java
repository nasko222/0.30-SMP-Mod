package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;

public final class FallingTile extends Tile {
	public FallingTile(int i1, int i2) {
		super(i1, i2);
	}

	public final void onPlace(Level level, int x, int y, int z) {
		this.tryToFall(level, x, y, z);
	}

	public final void neighborChanged(Level level, int x, int y, int z, int type) {
		this.tryToFall(level, x, y, z);
	}

	private void tryToFall(Level level, int x, int y, int z) {
		int i11 = x;
		int i5 = y;
		int i6 = z;

		while(true) {
			int i9 = i5 - 1;
			int i7;
			Liquid liquid12;
			if(!((i7 = level.getTile(i11, i9, i6)) == 0 ? true : ((liquid12 = Tile.tiles[i7].getLiquidType()) == Liquid.water ? true : liquid12 == Liquid.lava)) || i5 <= 0) {
				if(i5 != y) {
					if((i7 = level.getTile(i11, i5, i6)) > 0 && Tile.tiles[i7].getLiquidType() != Liquid.none) {
						level.setTileNoUpdate(i11, i5, i6, 0);
					}

					level.swap(x, y, z, i11, i5, i6);
				}

				return;
			}

			--i5;
		}
	}
}