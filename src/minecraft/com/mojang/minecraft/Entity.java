package com.mojang.minecraft;

import com.mojang.minecraft.level.BlockMap;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.tile.Tile$SoundType;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.net.EntityPos;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Textures;

import java.io.Serializable;
import java.util.ArrayList;

import util.Mth;

public abstract class Entity implements Serializable {
	public static final long serialVersionUID = 0L;
	public Level level;
	public float xo;
	public float yo;
	public float zo;
	public float x;
	public float y;
	public float z;
	public float xd;
	public float yd;
	public float zd;
	public float yRot;
	public float xRot;
	public float yRotO;
	public float xRotO;
	public AABB bb;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	public boolean collision = false;
	public boolean slide = true;
	public boolean removed = false;
	public float heightOffset = 0.0F;
	public float bbWidth = 0.6F;
	public float bbHeight = 1.8F;
	public float walkDistO = 0.0F;
	public float walkDist = 0.0F;
	public boolean makeStepSound = true;
	public float fallDistance = 0.0F;
	private int nextStep = 1;
	public BlockMap blockMap;
	public float xOld;
	public float yOld;
	public float zOld;
	public int textureId = 0;
	public float ySlideOffset = 0.0F;
	public float footSize = 0.0F;
	public boolean noPhysics = false;
	public float pushthrough = 0.0F;
	public boolean hovered = false;

	public Entity(Level level) {
		this.level = level;
		this.setPos(0.0F, 0.0F, 0.0F);
	}

	public void resetPos() {
		if(this.level != null) {
			float f1 = (float)this.level.xSpawn + 0.5F;
			float f2 = (float)this.level.ySpawn;

			for(float f3 = (float)this.level.zSpawn + 0.5F; f2 > 0.0F; ++f2) {
				this.setPos(f1, f2, f3);
				if(this.level.getCubes(this.bb).size() == 0) {
					break;
				}
			}

			this.xd = this.yd = this.zd = 0.0F;
			this.yRot = this.level.rotSpawn;
			this.xRot = 0.0F;
		}
	}

	public void remove() {
		this.removed = true;
	}

	public void setSize(float bbWidth, float bbHeight) {
		this.bbWidth = bbWidth;
		this.bbHeight = bbHeight;
	}

	public void setPos(EntityPos pos) {
		if(pos.moving) {
			this.setPos(pos.x, pos.y, pos.z);
		} else {
			this.setPos(this.x, this.y, this.z);
		}

		if(pos.rotating) {
			this.setRot(pos.yRot, pos.xRot);
		} else {
			this.setRot(this.yRot, this.xRot);
		}
	}

	protected void setRot(float xo, float yo) {
		this.yRot = xo;
		this.xRot = yo;
	}

	public void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		float f4 = this.bbWidth / 2.0F;
		float f5 = this.bbHeight / 2.0F;
		this.bb = new AABB(x - f4, y - f5, z - f4, x + f4, y + f5, z + f4);
	}

	public void turn(float y, float x) {
		float f3 = this.xRot;
		float f4 = this.yRot;
		this.yRot = (float)((double)this.yRot + (double)y * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)x * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

		this.xRotO += this.xRot - f3;
		this.yRotO += this.yRot - f4;
	}

	public void interpolateTurn(float y, float x) {
		this.yRot = (float)((double)this.yRot + (double)y * 0.15D);
		this.xRot = (float)((double)this.xRot - (double)x * 0.15D);
		if(this.xRot < -90.0F) {
			this.xRot = -90.0F;
		}

		if(this.xRot > 90.0F) {
			this.xRot = 90.0F;
		}

	}

	public void tick() {
		this.walkDistO = this.walkDist;
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
	}

	public boolean isFree(float x, float y, float z, float b) {
		AABB x1 = this.bb.grow(b, b, b).cloneMove(x, y, z);
		return this.level.getCubes(x1).size() > 0 ? false : !this.level.containsAnyLiquid(x1);
	}

	public boolean isFree(float x, float y, float z) {
		AABB x1 = this.bb.cloneMove(x, y, z);
		return this.level.getCubes(x1).size() > 0 ? false : !this.level.containsAnyLiquid(x1);
	}

	public void move(float x, float y, float z) {
		if(this.noPhysics) {
			this.bb.move(x, y, z);
			this.x = (this.bb.x0 + this.bb.x1) / 2.0F;
			this.y = this.bb.y0 + this.heightOffset - this.ySlideOffset;
			this.z = (this.bb.z0 + this.bb.z1) / 2.0F;
		} else {
			float f4 = this.x;
			float f5 = this.z;
			float f6 = x;
			float f7 = y;
			float f8 = z;
			AABB aABB9 = this.bb.copy();
			ArrayList arrayList10 = this.level.getCubes(this.bb.expand(x, y, z));

			for(int i11 = 0; i11 < arrayList10.size(); ++i11) {
				y = ((AABB)arrayList10.get(i11)).clipYCollide(this.bb, y);
			}

			this.bb.move(0.0F, y, 0.0F);
			if(!this.slide && f7 != y) {
				z = 0.0F;
				y = 0.0F;
				x = 0.0F;
			}

			boolean z16 = this.onGround || f7 != y && f7 < 0.0F;

			int i12;
			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				x = ((AABB)arrayList10.get(i12)).clipXCollide(this.bb, x);
			}

			this.bb.move(x, 0.0F, 0.0F);
			if(!this.slide && f6 != x) {
				z = 0.0F;
				y = 0.0F;
				x = 0.0F;
			}

			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				z = ((AABB)arrayList10.get(i12)).clipZCollide(this.bb, z);
			}

			this.bb.move(0.0F, 0.0F, z);
			if(!this.slide && f8 != z) {
				z = 0.0F;
				y = 0.0F;
				x = 0.0F;
			}

			float f17;
			float f18;
			if(this.footSize > 0.0F && z16 && this.ySlideOffset < 0.05F && (f6 != x || f8 != z)) {
				f18 = x;
				f17 = y;
				float f13 = z;
				x = f6;
				y = this.footSize;
				z = f8;
				AABB aABB14 = this.bb.copy();
				this.bb = aABB9.copy();
				arrayList10 = this.level.getCubes(this.bb.expand(f6, y, f8));

				int i15;
				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					y = ((AABB)arrayList10.get(i15)).clipYCollide(this.bb, y);
				}

				this.bb.move(0.0F, y, 0.0F);
				if(!this.slide && f7 != y) {
					z = 0.0F;
					y = 0.0F;
					x = 0.0F;
				}

				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					x = ((AABB)arrayList10.get(i15)).clipXCollide(this.bb, x);
				}

				this.bb.move(x, 0.0F, 0.0F);
				if(!this.slide && f6 != x) {
					z = 0.0F;
					y = 0.0F;
					x = 0.0F;
				}

				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					z = ((AABB)arrayList10.get(i15)).clipZCollide(this.bb, z);
				}

				this.bb.move(0.0F, 0.0F, z);
				if(!this.slide && f8 != z) {
					z = 0.0F;
					y = 0.0F;
					x = 0.0F;
				}

				if(f18 * f18 + f13 * f13 >= x * x + z * z) {
					x = f18;
					y = f17;
					z = f13;
					this.bb = aABB14.copy();
				} else {
					this.ySlideOffset = (float)((double)this.ySlideOffset + 0.5D);
				}
			}

			this.horizontalCollision = f6 != x || f8 != z;
			this.onGround = f7 != y && f7 < 0.0F;
			this.collision = this.horizontalCollision || f7 != y;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.causeFallDamage(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(y < 0.0F) {
				this.fallDistance -= y;
			}

			if(f6 != x) {
				this.xd = 0.0F;
			}

			if(f7 != y) {
				this.yd = 0.0F;
			}

			if(f8 != z) {
				this.zd = 0.0F;
			}

			this.x = (this.bb.x0 + this.bb.x1) / 2.0F;
			this.y = this.bb.y0 + this.heightOffset - this.ySlideOffset;
			this.z = (this.bb.z0 + this.bb.z1) / 2.0F;
			f18 = this.x - f4;
			f17 = this.z - f5;
			this.walkDist = (float)((double)this.walkDist + (double)Mth.sqrt(f18 * f18 + f17 * f17) * 0.6D);
			if(this.makeStepSound) {
				int i19 = this.level.getTile((int)this.x, (int)(this.y - 0.2F - this.heightOffset), (int)this.z);
				if(this.walkDist > (float)this.nextStep && i19 > 0) {
					++this.nextStep;
					Tile$SoundType tile$SoundType20;
					if((tile$SoundType20 = Tile.tiles[i19].soundType) != Tile$SoundType.none) {
						this.playSound("step." + tile$SoundType20.name, tile$SoundType20.getVolume() * 0.75F, tile$SoundType20.getPitch());
					}
				}
			}

			this.ySlideOffset *= 0.4F;
		}
	}

	protected void causeFallDamage(float distance) {
	}

	public boolean isInWater() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.water);
	}

	public boolean isUnderWater() {
		int i1;
		return (i1 = this.level.getTile((int)this.x, (int)(this.y + 0.12F), (int)this.z)) != 0 ? Tile.tiles[i1].getLiquidType().equals(Liquid.water) : false;
	}

	public boolean isInLava() {
		return this.level.containsLiquid(this.bb.grow(0.0F, -0.4F, 0.0F), Liquid.lava);
	}

	public void moveRelative(float x, float y, float z) {
		float f4;
		if((f4 = Mth.sqrt(x * x + y * y)) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = z / f4;
			x *= f4;
			y *= f4;
			z = Mth.sin(this.yRot * (float)Math.PI / 180.0F);
			f4 = Mth.cos(this.yRot * (float)Math.PI / 180.0F);
			this.xd += x * f4 - y * z;
			this.zd += y * f4 + x * z;
		}
	}

	public boolean isLit() {
		int i1 = (int)this.x;
		int i2 = (int)this.y;
		int i3 = (int)this.z;
		return this.level.isLit(i1, i2, i3);
	}

	public float getBrightness(float lightValue) {
		int lightValue1 = (int)this.x;
		int i2 = (int)(this.y + this.heightOffset / 2.0F - 0.5F);
		int i3 = (int)this.z;
		return this.level.getBrightness(lightValue1, i2, i3);
	}

	public void render(Textures textures, float translation) {
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void playSound(String soundName, float volume, float pitch) {
		this.level.playSound(soundName, this, volume, pitch);
	}

	public void moveTo(float x, float y, float z, float xRot, float yRot) {
		this.xo = this.x = x;
		this.yo = this.y = y;
		this.zo = this.z = z;
		this.yRot = xRot;
		this.xRot = yRot;
		this.setPos(x, y, z);
	}

	public float distanceTo(Entity entity) {
		float f2 = this.x - entity.x;
		float f3 = this.y - entity.y;
		float f4 = this.z - entity.z;
		return Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public float distanceTo(float x, float y, float z) {
		x = this.x - x;
		y = this.y - y;
		float f4 = this.z - z;
		return Mth.sqrt(x * x + y * y + f4 * f4);
	}

	public float distanceToSqr(Entity entity) {
		float f2 = this.x - entity.x;
		float f3 = this.y - entity.y;
		float f4 = this.z - entity.z;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	public void playerTouch(Entity player) {
	}

	public void push(Entity entity) {
		float f2 = entity.x - this.x;
		float f3 = entity.z - this.z;
		float f4;
		if((f4 = f2 * f2 + f3 * f3) >= 0.01F) {
			f4 = Mth.sqrt(f4);
			f2 /= f4;
			f3 /= f4;
			f2 /= f4;
			f3 /= f4;
			f2 *= 0.05F;
			f3 *= 0.05F;
			f2 *= 1.0F - this.pushthrough;
			f3 *= 1.0F - this.pushthrough;
			this.push(-f2, 0.0F, -f3);
			entity.push(f2, 0.0F, f3);
		}

	}

	protected void push(float x, float y, float z) {
		this.xd += x;
		this.yd += y;
		this.zd += z;
	}

	public void hurt(Entity entity, int amount) {
	}

	public boolean intersects(float x0, float y0, float z0, float x1, float y1, float z1) {
		return this.bb.intersects(x0, y0, z0, x1, y1, z1);
	}

	public boolean isPickable() {
		return false;
	}

	public boolean isPushable() {
		return false;
	}

	public boolean isShootable() {
		return false;
	}

	public void awardKillScore(Entity entity, int integer) {
	}

	public boolean shouldRender(Vec3 t) {
		float f2 = this.x - t.x;
		float f3 = this.y - t.y;
		float t1 = this.z - t.z;
		t1 = f2 * f2 + f3 * f3 + t1 * t1;
		return this.shouldRenderAtSqrDistance(t1);
	}

	public boolean shouldRenderAtSqrDistance(float float1) {
		float f2 = this.bb.getSize() * 64.0F;
		return float1 < f2 * f2;
	}

	public int getTexture() {
		return this.textureId;
	}

	public boolean isCreativeModeAllowed() {
		return false;
	}

	public void renderHover(Textures textures, float t) {
	}
}