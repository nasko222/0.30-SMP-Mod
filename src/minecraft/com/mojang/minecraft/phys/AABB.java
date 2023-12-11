package com.mojang.minecraft.phys;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.model.Vec3;

import java.io.Serializable;

public class AABB implements Serializable {
	public static final long serialVersionUID = 0L;
	private float epsilon = 0.0F;
	public float x0;
	public float y0;
	public float z0;
	public float x1;
	public float y1;
	public float z1;

	public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
	}

	public AABB expand(float xa, float ya, float za) {
		float f4 = this.x0;
		float f5 = this.y0;
		float f6 = this.z0;
		float f7 = this.x1;
		float f8 = this.y1;
		float f9 = this.z1;
		if(xa < 0.0F) {
			f4 += xa;
		}

		if(xa > 0.0F) {
			f7 += xa;
		}

		if(ya < 0.0F) {
			f5 += ya;
		}

		if(ya > 0.0F) {
			f8 += ya;
		}

		if(za < 0.0F) {
			f6 += za;
		}

		if(za > 0.0F) {
			f9 += za;
		}

		return new AABB(f4, f5, f6, f7, f8, f9);
	}

	public AABB grow(float xa, float ya, float za) {
		float f4 = this.x0 - xa;
		float f5 = this.y0 - ya;
		float f6 = this.z0 - za;
		xa += this.x1;
		ya += this.y1;
		float f7 = this.z1 + za;
		return new AABB(f4, f5, f6, xa, ya, f7);
	}

	public AABB cloneMove(float xa, float ya, float za) {
		return new AABB(this.x0 + za, this.y0 + ya, this.z0 + za, this.x1 + xa, this.y1 + ya, this.z1 + za);
	}

	public float clipXCollide(AABB c, float xa) {
		if(c.y1 > this.y0 && c.y0 < this.y1) {
			if(c.z1 > this.z0 && c.z0 < this.z1) {
				float f3;
				if(xa > 0.0F && c.x1 <= this.x0 && (f3 = this.x0 - c.x1 - this.epsilon) < xa) {
					xa = f3;
				}

				if(xa < 0.0F && c.x0 >= this.x1 && (f3 = this.x1 - c.x0 + this.epsilon) > xa) {
					xa = f3;
				}

				return xa;
			} else {
				return xa;
			}
		} else {
			return xa;
		}
	}

	public float clipYCollide(AABB c, float ya) {
		if(c.x1 > this.x0 && c.x0 < this.x1) {
			if(c.z1 > this.z0 && c.z0 < this.z1) {
				float f3;
				if(ya > 0.0F && c.y1 <= this.y0 && (f3 = this.y0 - c.y1 - this.epsilon) < ya) {
					ya = f3;
				}

				if(ya < 0.0F && c.y0 >= this.y1 && (f3 = this.y1 - c.y0 + this.epsilon) > ya) {
					ya = f3;
				}

				return ya;
			} else {
				return ya;
			}
		} else {
			return ya;
		}
	}

	public float clipZCollide(AABB c, float za) {
		if(c.x1 > this.x0 && c.x0 < this.x1) {
			if(c.y1 > this.y0 && c.y0 < this.y1) {
				float f3;
				if(za > 0.0F && c.z1 <= this.z0 && (f3 = this.z0 - c.z1 - this.epsilon) < za) {
					za = f3;
				}

				if(za < 0.0F && c.z0 >= this.z1 && (f3 = this.z1 - c.z0 + this.epsilon) > za) {
					za = f3;
				}

				return za;
			} else {
				return za;
			}
		} else {
			return za;
		}
	}

	public boolean intersects(AABB c) {
		return c.x1 > this.x0 && c.x0 < this.x1 ? (c.y1 > this.y0 && c.y0 < this.y1 ? c.z1 > this.z0 && c.z0 < this.z1 : false) : false;
	}

	public boolean intersectsInner(AABB c) {
		return c.x1 >= this.x0 && c.x0 <= this.x1 ? (c.y1 >= this.y0 && c.y0 <= this.y1 ? c.z1 >= this.z0 && c.z0 <= this.z1 : false) : false;
	}

	public void move(float xa, float ya, float za) {
		this.x0 += xa;
		this.y0 += ya;
		this.z0 += za;
		this.x1 += xa;
		this.y1 += ya;
		this.z1 += za;
	}

	public boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x2 > this.x0 && x1 < this.x1 ? (y2 > this.y0 && y1 < this.y1 ? z2 > this.z0 && z1 < this.z1 : false) : false;
	}

	public boolean contains(Vec3 t) {
		return t.x > this.x0 && t.x < this.x1 ? (t.y > this.y0 && t.y < this.y1 ? t.z > this.z0 && t.z < this.z1 : false) : false;
	}

	public float getSize() {
		float f1 = this.x1 - this.x0;
		float f2 = this.y1 - this.y0;
		float f3 = this.z1 - this.z0;
		return (f1 + f2 + f3) / 3.0F;
	}

	public AABB shrink(float xa, float ya, float za) {
		float f4 = this.x0;
		float f5 = this.y0;
		float f6 = this.z0;
		float f7 = this.x1;
		float f8 = this.y1;
		float f9 = this.z1;
		if(xa < 0.0F) {
			f4 -= xa;
		}

		if(xa > 0.0F) {
			f7 -= xa;
		}

		if(ya < 0.0F) {
			f5 -= ya;
		}

		if(ya > 0.0F) {
			f8 -= ya;
		}

		if(za < 0.0F) {
			f6 -= za;
		}

		if(za > 0.0F) {
			f9 -= za;
		}

		return new AABB(f4, f5, f6, f7, f8, f9);
	}

	public AABB copy() {
		return new AABB(this.x0, this.y0, this.z0, this.x1, this.y1, this.z1);
	}

	public HitResult clip(Vec3 v0, Vec3 v1) {
		Vec3 vec33 = v0.clipX(v1, this.x0);
		Vec3 vec34 = v0.clipX(v1, this.x1);
		Vec3 vec35 = v0.clipY(v1, this.y0);
		Vec3 vec36 = v0.clipY(v1, this.y1);
		Vec3 vec37 = v0.clipZ(v1, this.z0);
		v1 = v0.clipZ(v1, this.z1);
		if(!this.containsX(vec33)) {
			vec33 = null;
		}

		if(!this.containsX(vec34)) {
			vec34 = null;
		}

		if(!this.containsY(vec35)) {
			vec35 = null;
		}

		if(!this.containsY(vec36)) {
			vec36 = null;
		}

		if(!this.containsZ(vec37)) {
			vec37 = null;
		}

		if(!this.containsZ(v1)) {
			v1 = null;
		}

		Vec3 vec38 = null;
		if(vec33 != null) {
			vec38 = vec33;
		}

		if(vec34 != null && (vec38 == null || v0.distanceToSqr(vec34) < v0.distanceToSqr(vec38))) {
			vec38 = vec34;
		}

		if(vec35 != null && (vec38 == null || v0.distanceToSqr(vec35) < v0.distanceToSqr(vec38))) {
			vec38 = vec35;
		}

		if(vec36 != null && (vec38 == null || v0.distanceToSqr(vec36) < v0.distanceToSqr(vec38))) {
			vec38 = vec36;
		}

		if(vec37 != null && (vec38 == null || v0.distanceToSqr(vec37) < v0.distanceToSqr(vec38))) {
			vec38 = vec37;
		}

		if(v1 != null && (vec38 == null || v0.distanceToSqr(v1) < v0.distanceToSqr(vec38))) {
			vec38 = v1;
		}

		if(vec38 == null) {
			return null;
		} else {
			byte v01 = -1;
			if(vec38 == vec33) {
				v01 = 4;
			}

			if(vec38 == vec34) {
				v01 = 5;
			}

			if(vec38 == vec35) {
				v01 = 0;
			}

			if(vec38 == vec36) {
				v01 = 1;
			}

			if(vec38 == vec37) {
				v01 = 2;
			}

			if(vec38 == v1) {
				v01 = 3;
			}

			return new HitResult(0, 0, 0, v01, vec38);
		}
	}

	private boolean containsX(Vec3 xa) {
		return xa == null ? false : xa.y >= this.y0 && xa.y <= this.y1 && xa.z >= this.z0 && xa.z <= this.z1;
	}

	private boolean containsY(Vec3 ya) {
		return ya == null ? false : ya.x >= this.x0 && ya.x <= this.x1 && ya.z >= this.z0 && ya.z <= this.z1;
	}

	private boolean containsZ(Vec3 za) {
		return za == null ? false : za.x >= this.x0 && za.x <= this.x1 && za.y >= this.y0 && za.y <= this.y1;
	}
}