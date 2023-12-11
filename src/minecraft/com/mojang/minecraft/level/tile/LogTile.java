package com.mojang.minecraft.level.tile;

public final class LogTile extends Tile {
	protected LogTile(int i1) {
		super(17);
		this.tex = 20;
	}

	public final int resourceCount() {
		return random.nextInt(3) + 3;
	}

	public final int getId() {
		return Tile.wood.id;
	}

	protected final int getTexture(int face) {
		return face == 1 ? 21 : (face == 0 ? 21 : 20);
	}
}