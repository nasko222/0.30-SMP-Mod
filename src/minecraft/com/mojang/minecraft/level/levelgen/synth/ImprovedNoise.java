package com.mojang.minecraft.level.levelgen.synth;

import java.util.Random;

public final class ImprovedNoise extends Synth {
	private int[] p;

	public ImprovedNoise() {
		this(new Random());
	}

	public ImprovedNoise(Random rando) {
		this.p = new int[512];

		int i2;
		for(i2 = 0; i2 < 256; this.p[i2] = i2++) {
		}

		for(i2 = 0; i2 < 256; ++i2) {
			int i3 = rando.nextInt(256 - i2) + i2;
			int i4 = this.p[i2];
			this.p[i2] = this.p[i3];
			this.p[i3] = i4;
			this.p[i2 + 256] = this.p[i2];
		}

	}

	private static double fade(double t) {
		return t * t * t * (t * (t * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double a, double a1, double b) {
		return a1 + a * (b - a1);
	}

	private static double grad(int hash, double x, double y, double z) {
		double d8 = (hash &= 15) < 8 ? x : y;
		double d10 = hash < 4 ? y : (hash != 12 && hash != 14 ? z : x);
		return ((hash & 1) == 0 ? d8 : -d8) + ((hash & 2) == 0 ? d10 : -d10);
	}

	public final double getValue(double x, double y) {
		double d10 = 0.0D;
		double d8 = y;
		double d6 = x;
		int x1 = (int)Math.floor(x) & 255;
		int i2 = (int)Math.floor(y) & 255;
		int y1 = (int)Math.floor(0.0D) & 255;
		d6 -= Math.floor(d6);
		d8 -= Math.floor(d8);
		d10 = 0.0D - Math.floor(0.0D);
		double d15 = fade(d6);
		double d17 = fade(d8);
		double d19 = fade(d10);
		int i4 = this.p[x1] + i2;
		int i5 = this.p[i4] + y1;
		i4 = this.p[i4 + 1] + y1;
		x1 = this.p[x1 + 1] + i2;
		i2 = this.p[x1] + y1;
		x1 = this.p[x1 + 1] + y1;
		return lerp(d19, lerp(d17, lerp(d15, grad(this.p[i5], d6, d8, d10), grad(this.p[i2], d6 - 1.0D, d8, d10)), lerp(d15, grad(this.p[i4], d6, d8 - 1.0D, d10), grad(this.p[x1], d6 - 1.0D, d8 - 1.0D, d10))), lerp(d17, lerp(d15, grad(this.p[i5 + 1], d6, d8, d10 - 1.0D), grad(this.p[i2 + 1], d6 - 1.0D, d8, d10 - 1.0D)), lerp(d15, grad(this.p[i4 + 1], d6, d8 - 1.0D, d10 - 1.0D), grad(this.p[x1 + 1], d6 - 1.0D, d8 - 1.0D, d10 - 1.0D))));
	}
}