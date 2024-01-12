package com.mojang.minecraft.level.tile;

public class FoodTile extends Tile {

	protected FoodTile(int id, int tex) {
		super(id);
		this.tex = tex;
	}
	
	public final int resourceCount() {
		return 0;
	}

}
