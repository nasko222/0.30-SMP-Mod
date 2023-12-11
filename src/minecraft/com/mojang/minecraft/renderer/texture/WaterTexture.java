package com.mojang.minecraft.renderer.texture;

import com.mojang.minecraft.level.tile.Tile;

public final class WaterTexture extends DynamicTexture {
	private float[] red = new float[256];
	private float[] green = new float[256];
	private float[] blue = new float[256];
	private float[] alpha = new float[256];
	private int tickCounter = 0;

	public WaterTexture() {
		super(Tile.water.tex);
	}

	public final void tick() {
		++this.tickCounter;

		int i1;
		int i2;
		float f3;
		int i4;
		int i5;
		int i6;
		for(i1 = 0; i1 < 16; ++i1) {
			for(i2 = 0; i2 < 16; ++i2) {
				f3 = 0.0F;

				for(i4 = i1 - 1; i4 <= i1 + 1; ++i4) {
					i5 = i4 & 15;
					i6 = i2 & 15;
					f3 += this.red[i5 + (i6 << 4)];
				}

				this.green[i1 + (i2 << 4)] = f3 / 3.3F + this.blue[i1 + (i2 << 4)] * 0.8F;
			}
		}

		for(i1 = 0; i1 < 16; ++i1) {
			for(i2 = 0; i2 < 16; ++i2) {
				this.blue[i1 + (i2 << 4)] += this.alpha[i1 + (i2 << 4)] * 0.05F;
				if(this.blue[i1 + (i2 << 4)] < 0.0F) {
					this.blue[i1 + (i2 << 4)] = 0.0F;
				}

				this.alpha[i1 + (i2 << 4)] -= 0.1F;
				if(Math.random() < 0.05D) {
					this.alpha[i1 + (i2 << 4)] = 0.5F;
				}
			}
		}

		float[] f8 = this.green;
		this.green = this.red;
		this.red = f8;

		for(i2 = 0; i2 < 256; ++i2) {
			if((f3 = this.red[i2]) > 1.0F) {
				f3 = 1.0F;
			}

			if(f3 < 0.0F) {
				f3 = 0.0F;
			}

			float f9 = f3 * f3;
			i5 = (int)(32.0F + f9 * 32.0F);
			i6 = (int)(50.0F + f9 * 64.0F);
			i1 = 255;
			int i10 = (int)(146.0F + f9 * 50.0F);
			if(this.anaglyph) {
				i1 = (i5 * 30 + i6 * 59 + 2805) / 100;
				i4 = (i5 * 30 + i6 * 70) / 100;
				int i7 = (i5 * 30 + 17850) / 100;
				i5 = i1;
				i6 = i4;
				i1 = i7;
			}

			this.pixels[i2 << 2] = (byte)i5;
			this.pixels[(i2 << 2) + 1] = (byte)i6;
			this.pixels[(i2 << 2) + 2] = (byte)i1;
			this.pixels[(i2 << 2) + 3] = (byte)i10;
		}

	}
}