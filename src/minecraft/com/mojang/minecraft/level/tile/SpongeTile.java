package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class SpongeTile extends Tile {
	protected SpongeTile(int i1) {
		super(19);
		this.tex = 48;
	}

	public final void onTileAdded(Level level, int x, int y, int z) {
		for(int i7 = x - 2; i7 <= x + 2; ++i7) {
			for(int i5 = y - 2; i5 <= y + 2; ++i5) {
				for(int i6 = z - 2; i6 <= z + 2; ++i6) {
					if(level.isWater(i7, i5, i6)) {
						level.setTileNoNeighborChange(i7, i5, i6, 0);
					}
				}
			}
		}

	}

	public final void onTileRemoved(Level level, int x, int y, int z) {
		for(int i7 = x - 2; i7 <= x + 2; ++i7) {
			for(int i5 = y - 2; i5 <= y + 2; ++i5) {
				for(int i6 = z - 2; i6 <= z + 2; ++i6) {
					level.updateNeighborsAt(i7, i5, i6, level.getTile(i7, i5, i6));
				}
			}
		}

	}
}