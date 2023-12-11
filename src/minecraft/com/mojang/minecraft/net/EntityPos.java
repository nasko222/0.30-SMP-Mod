package com.mojang.minecraft.net;

public final class EntityPos {
	public float x;
	public float y;
	public float z;
	public float yRot;
	public float xRot;
	public boolean rotating = false;
	public boolean moving = false;

	public EntityPos(float x, float y, float z, float yr, float xr) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yRot = yr;
		this.xRot = xr;
		this.rotating = true;
		this.moving = true;
	}

	public EntityPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.moving = true;
		this.rotating = false;
	}

	public EntityPos(float yr, float xr) {
		this.yRot = yr;
		this.xRot = xr;
		this.rotating = true;
		this.moving = false;
	}
}