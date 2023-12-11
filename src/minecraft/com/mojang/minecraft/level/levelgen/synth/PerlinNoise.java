package com.mojang.minecraft.level.levelgen.synth;

import java.util.Random;

public final class PerlinNoise extends Synth {
	private ImprovedNoise[] noiseLevels;
	private int levels;

	public PerlinNoise(Random random, int levels) {
		this.levels = levels;
		this.noiseLevels = new ImprovedNoise[levels];

		for(int i3 = 0; i3 < levels; ++i3) {
			this.noiseLevels[i3] = new ImprovedNoise(random);
		}

	}

	public final double getValue(double x, double y) {
		double d5 = 0.0D;
		double d7 = 1.0D;

		for(int i9 = 0; i9 < this.levels; ++i9) {
			d5 += this.noiseLevels[i9].getValue(x / d7, y / d7) * d7;
			d7 *= 2.0D;
		}

		return d5;
	}
}