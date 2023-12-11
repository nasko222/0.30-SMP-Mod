package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public class BaseLeafTile extends Tile {
	private boolean renderAdjacentFaces = true;

	protected BaseLeafTile(int i1, int i2, boolean z3) {
		super(i1, i2);
	}

	public final boolean isSolid() {
		return false;
	}

	public final boolean shouldRenderFace(Level level, int x, int y, int z, int layer) {
		int i6 = level.getTile(x, y, z);
		return !this.renderAdjacentFaces && i6 == this.id ? false : super.shouldRenderFace(level, x, y, z, layer);
	}

	public final boolean blocksLight() {
		return false;
	}
}