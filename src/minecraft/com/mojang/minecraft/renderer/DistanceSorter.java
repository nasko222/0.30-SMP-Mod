package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DistanceSorter implements Comparator {
	private Player player;

	public DistanceSorter(Player player) {
		this.player = player;
	}

	public final int compare(Object c0, Object c1) {
		Chunk chunk10001 = (Chunk)c0;
		Chunk c11 = (Chunk)c1;
		Chunk c01 = chunk10001;
		return c01.compare(this.player) < c11.compare(this.player) ? -1 : 1;
	}
}