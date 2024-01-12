package com.mojang.minecraft.level.tile;

public final class LeafTile extends BaseLeafTile {
	protected LeafTile(int i1, int i2) {
		super(18, 22, true);
	}

	public final int resourceCount() {
		return random.nextInt(5) == 0 ? 1 : 0;
	}

	public final int getId() {
		return random.nextInt(3) == 0 ? Tile.bush.id : Tile.apple.id;
	}
}