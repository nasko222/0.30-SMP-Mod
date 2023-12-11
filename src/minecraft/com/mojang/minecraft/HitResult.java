package com.mojang.minecraft;

import com.mojang.minecraft.model.Vec3;

public final class HitResult {
	public int type;
	public int x;
	public int y;
	public int z;
	public int f;
	public Vec3 vec;
	public Entity entity;

	public HitResult(int x, int y, int z, int f, Vec3 hitVec) {
		this.type = 0;
		this.x = x;
		this.y = y;
		this.z = z;
		this.f = f;
		this.vec = new Vec3(hitVec.x, hitVec.y, hitVec.z);
	}

	public HitResult(Entity entity) {
		this.type = 1;
		this.entity = entity;
	}
}