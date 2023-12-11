package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;

public final class SlabTile extends Tile {
	private boolean half;

	public SlabTile(int id, boolean half) {
		super(id, 6);
		this.half = half;
		if(!half) {
			this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

	}

	protected final int getTexture(int face) {
		return face <= 1 ? 6 : 5;
	}

	public final boolean isSolid() {
		return this.half;
	}

	public final void neighborChanged(Level level, int x, int y, int z, int type) {
		if(this == Tile.slabHalf) {
			;
		}
	}

	public final void onTileAdded(Level level, int x, int y, int z) {
		if(this != Tile.slabHalf) {
			super.onTileAdded(level, x, y, z);
		}

		if(level.getTile(x, y - 1, z) == slabHalf.id) {
			level.setTile(x, y, z, 0);
			level.setTile(x, y - 1, z, Tile.slabFull.id);
		}

	}

	public final int getId() {
		return Tile.slabHalf.id;
	}

	public final boolean isOpaque() {
		return this.half;
	}

	public final boolean shouldRenderFace(Level level, int x, int y, int z, int layer) {
		if(this != Tile.slabHalf) {
			super.shouldRenderFace(level, x, y, z, layer);
		}

		return layer == 1 ? true : (!super.shouldRenderFace(level, x, y, z, layer) ? false : (layer == 0 ? true : level.getTile(x, y, z) != this.id));
	}
}