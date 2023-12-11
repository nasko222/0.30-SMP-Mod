package com.mojang.minecraft.level.tile;

public final class MetalTile extends Tile {
	public MetalTile(int i1, int i2) {
		super(i1);
		this.tex = i2;
	}

	protected final int getTexture(int face) {
		return face == 1 ? this.tex - 16 : (face == 0 ? this.tex + 16 : this.tex);
	}
}