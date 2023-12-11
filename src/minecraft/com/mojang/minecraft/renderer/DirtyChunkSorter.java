package com.mojang.minecraft.renderer;

import com.mojang.minecraft.player.Player;

import java.util.Comparator;

public final class DirtyChunkSorter implements Comparator {
	private Player player;

	public DirtyChunkSorter(Player player) {
		this.player = player;
	}

	public final int compare(Object object1, Object object2) {
		Chunk chunk10001 = (Chunk)object1;
		Chunk chunk6 = (Chunk)object2;
		Chunk chunk5 = chunk10001;
		boolean z3 = chunk5.isInFrustum;
		boolean z4 = chunk6.isInFrustum;
		return z3 && !z4 ? 1 : ((!z4 || z3) && chunk5.compare(this.player) < chunk6.compare(this.player) ? 1 : -1);
	}
}