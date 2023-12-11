package com.mojang.minecraft.model;

import util.Mth;

public final class Vec3 {
	public float x;
	public float y;
	public float z;

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final Vec3 subtract(Vec3 t) {
		return new Vec3(this.x - t.x, this.y - t.y, this.z - t.z);
	}

	public final Vec3 normalize() {
		float f1 = Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		return new Vec3(this.x / f1, this.y / f1, this.z / f1);
	}

	public final Vec3 add(float x, float y, float z) {
		return new Vec3(this.x + x, this.y + y, this.z + z);
	}

	public final float distanceTo(Vec3 t) {
		float f2 = t.x - this.x;
		float f3 = t.y - this.y;
		float f4 = t.z - this.z;
		return Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public final float distanceToSqr(Vec3 t) {
		float f2 = t.x - this.x;
		float f3 = t.y - this.y;
		float f4 = t.z - this.z;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	public final Vec3 clipX(Vec3 t, float xa) {
		float f3 = t.x - this.x;
		float f4 = t.y - this.y;
		float t1 = t.z - this.z;
		return f3 * f3 < 1.0E-7F ? null : ((xa = (xa - this.x) / f3) >= 0.0F && xa <= 1.0F ? new Vec3(this.x + f3 * xa, this.y + f4 * xa, this.z + t1 * xa) : null);
	}

	public final Vec3 clipY(Vec3 t, float ya) {
		float f3 = t.x - this.x;
		float f4 = t.y - this.y;
		float t1 = t.z - this.z;
		return f4 * f4 < 1.0E-7F ? null : ((ya = (ya - this.y) / f4) >= 0.0F && ya <= 1.0F ? new Vec3(this.x + f3 * ya, this.y + f4 * ya, this.z + t1 * ya) : null);
	}

	public final Vec3 clipZ(Vec3 t, float za) {
		float f3 = t.x - this.x;
		float f4 = t.y - this.y;
		float t1;
		return (t1 = t.z - this.z) * t1 < 1.0E-7F ? null : ((za = (za - this.z) / t1) >= 0.0F && za <= 1.0F ? new Vec3(this.x + f3 * za, this.y + f4 * za, this.z + t1 * za) : null);
	}

	public final String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}