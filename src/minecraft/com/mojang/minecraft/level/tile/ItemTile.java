package com.mojang.minecraft.level.tile;

public class ItemTile extends Tile {

	protected ItemTile(int id, int tex) {
		super(id);
		this.tex = tex;
	}
	
	public final int resourceCount() {
		return 0;
	}
	
	public static ItemMode getItemMode(int id) {
		if (id > 120) return ItemMode.NONE;
		if (id < 101) return ItemMode.NONE;
		int selected = id - 101;
		if (selected % 3 == 0) return ItemMode.PICKAXE;
		if (selected % 3 == 1) return ItemMode.AXE;
		if (selected % 3 == 2) return ItemMode.SHOVEL;
		return ItemMode.NONE;
	}
	
	public static int getItemPower(int id) {
		if (id > 120) return 0;
		if (id < 101) return 0;
		int selected = id - 101;
		int power = 2;
		int prob = selected / 3;
		for (int i = 0; i < prob; i++) power+=2;
		return power;
	}
	
	public static int itemPower;
	
	public static ItemMode mode;
	
	public enum ItemMode
	{
		NONE,
		PICKAXE,
		AXE,
		SHOVEL
	}

}
