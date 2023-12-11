package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

import util.Mth;

public abstract class BaseSoundPos implements SoundPos {
	private Entity listener;

	public BaseSoundPos(Entity listener) {
		this.listener = listener;
	}

	public final float getRotationDiff(float x, float z) {
		x -= this.listener.x;
		z -= this.listener.z;
		float f3 = Mth.sqrt(x * x + z * z);
		x /= f3;
		z /= f3;
		if((f3 /= 2.0F) > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = Mth.cos(-this.listener.yRot * 0.017453292F + (float)Math.PI);
		return (Mth.sin(-this.listener.yRot * 0.017453292F + (float)Math.PI) * z - f4 * x) * f3;
	}

	public final float getDistanceSq(float x, float y, float z) {
		x -= this.listener.x;
		y -= this.listener.y;
		float f4 = z - this.listener.z;
		f4 = Mth.sqrt(x * x + y * y + f4 * f4);
		if((f4 = 1.0F - f4 / 32.0F) < 0.0F) {
			f4 = 0.0F;
		}

		return f4;
	}
}