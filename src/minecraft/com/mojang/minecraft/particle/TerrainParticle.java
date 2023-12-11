package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;

public class TerrainParticle extends Particle {
	private static final long serialVersionUID = 1L;

	public TerrainParticle(Level level, float x, float y, float z, float xr, float yr, float zr, Tile tile) {
		super(level, x, y, z, xr, yr, zr);
		this.tex = tile.tex;
		this.gravity = tile.particleGravity;
		this.rCol = this.gCol = this.bCol = 0.6F;
	}

	public int getParticleTexture() {
		return 1;
	}

	public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float ya2) {
		float f8;
		float f9 = (f8 = ((float)(this.tex % 16) + this.uo / 4.0F) / 16.0F) + 0.015609375F;
		float f10;
		float f11 = (f10 = ((float)(this.tex / 16) + this.vo / 4.0F) / 16.0F) + 0.015609375F;
		float f12 = 0.1F * this.size;
		float f13 = this.xo + (this.x - this.xo) * a;
		float f14 = this.yo + (this.y - this.yo) * a;
		float f15 = this.zo + (this.z - this.zo) * a;
		a = this.getBrightness(a);
		t.color(a * this.rCol, a * this.gCol, a * this.bCol);
		t.vertexUV(f13 - xa * f12 - xa2 * f12, f14 - ya * f12, f15 - za * f12 - ya2 * f12, f8, f11);
		t.vertexUV(f13 - xa * f12 + xa2 * f12, f14 + ya * f12, f15 - za * f12 + ya2 * f12, f8, f10);
		t.vertexUV(f13 + xa * f12 + xa2 * f12, f14 + ya * f12, f15 + za * f12 + ya2 * f12, f9, f10);
		t.vertexUV(f13 + xa * f12 - xa2 * f12, f14 - ya * f12, f15 + za * f12 - ya2 * f12, f9, f11);
	}
}