package com.mojang.minecraft.level;

public final class Coord {
	public int x;
	public int y;
	public int z;
	public int id;
	public int time;

	public Coord(int x, int y, int z, int id) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}
}