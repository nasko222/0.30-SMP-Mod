package com.mojang.minecraft.level.tile;

public class ChestTile extends Tile {
	
	public boolean full;
	
	protected ChestTile(int id) {
		super(id);
		this.tex = 50;
	}

	protected final int getTexture(int face) {
		if (face == 3) return tex;
		return face == 0 ? this.tex + 1  : (face == 1 ? this.tex + 2 + (full ? 1 : 0): this.tex + 1);
	}
}
