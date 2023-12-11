package com.mojang.minecraft.particle;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Tesselator;

import util.Mth;

public class Particle extends Entity {
	private static final long serialVersionUID = 1L;
	protected float xd;
	protected float yd;
	protected float zd;
	protected int tex;
	protected float uo;
	protected float vo;
	protected int age = 0;
	protected int lifetime = 0;
	protected float size;
	protected float gravity;
	protected float rCol;
	protected float gCol;
	protected float bCol;

	public Particle(Level level, float x, float y, float z, float xr, float yr, float zr) {
		super(level);
		this.setSize(0.2F, 0.2F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(x, y, z);
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.xd = xr + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.yd = yr + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		this.zd = zr + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
		float level1 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		x = Mth.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
		this.xd = this.xd / x * level1 * 0.4F;
		this.yd = this.yd / x * level1 * 0.4F + 0.1F;
		this.zd = this.zd / x * level1 * 0.4F;
		this.uo = (float)Math.random() * 3.0F;
		this.vo = (float)Math.random() * 3.0F;
		this.size = (float)(Math.random() * 0.5D + 0.5D);
		this.lifetime = (int)(4.0D / (Math.random() * 0.9D + 0.1D));
		this.age = 0;
		this.makeStepSound = false;
	}

	public Particle setPower(float power) {
		this.xd *= power;
		this.yd = (this.yd - 0.1F) * power + 0.1F;
		this.zd *= power;
		return this;
	}

	public Particle scale(float scale) {
		this.setSize(0.2F * scale, 0.2F * scale);
		this.size *= scale;
		return this;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd = (float)((double)this.yd - 0.04D * (double)this.gravity);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
		}

	}

	public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float ya2) {
		float f8;
		float f9 = (f8 = (float)(this.tex % 16) / 16.0F) + 0.0624375F;
		float f10;
		float f11 = (f10 = (float)(this.tex / 16) / 16.0F) + 0.0624375F;
		float f12 = 0.1F * this.size;
		float f13 = this.xo + (this.x - this.xo) * a;
		float f14 = this.yo + (this.y - this.yo) * a;
		float f15 = this.zo + (this.z - this.zo) * a;
		a = this.getBrightness(a);
		t.color(this.rCol * a, this.gCol * a, this.bCol * a);
		t.vertexUV(f13 - xa * f12 - xa2 * f12, f14 - ya * f12, f15 - za * f12 - ya2 * f12, f8, f11);
		t.vertexUV(f13 - xa * f12 + xa2 * f12, f14 + ya * f12, f15 - za * f12 + ya2 * f12, f8, f10);
		t.vertexUV(f13 + xa * f12 + xa2 * f12, f14 + ya * f12, f15 + za * f12 + ya2 * f12, f9, f10);
		t.vertexUV(f13 + xa * f12 - xa2 * f12, f14 - ya * f12, f15 + za * f12 - ya2 * f12, f9, f11);
	}

	public int getParticleTexture() {
		return 0;
	}
}