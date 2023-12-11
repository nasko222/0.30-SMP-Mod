package com.mojang.minecraft.level.tile;

public final class BookshelfTile extends Tile {
	public BookshelfTile(int i1, int i2) {
		super(47, 35);
	}

	protected final int getTexture(int face) {
		return face <= 1 ? 4 : this.tex;
	}

	public final int resourceCount() {
		return 0;
	}
}