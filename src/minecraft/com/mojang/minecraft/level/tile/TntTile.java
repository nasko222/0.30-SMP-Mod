package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.item.PrimedTnt;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.particle.ParticleEngine;

public final class TntTile extends Tile {
	public TntTile(int i1, int i2) {
		super(46, 8);
	}

	protected final int getTexture(int face) {
		return face == 0 ? this.tex + 2 : (face == 1 ? this.tex + 1 : this.tex);
	}

	public final int resourceCount() {
		return 0;
	}
}