package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public final class LevelSoundPos extends BaseSoundPos {
	private float x;
	private float y;
	private float z;

	public LevelSoundPos(float x, float y, float z, Entity listener) {
		super(listener);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final float getRotationDiff() {
		return super.getRotationDiff(this.x, this.z);
	}

	public final float getDistanceSq() {
		return super.getDistanceSq(this.x, this.y, this.z);
	}
}