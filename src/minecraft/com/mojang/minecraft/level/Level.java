package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.sound.AudioInfo;
import com.mojang.minecraft.sound.EntitySoundPos;
import com.mojang.minecraft.sound.LevelSoundPos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import util.Mth;

public class Level implements Serializable {
	public static final long serialVersionUID = 0L;
	public int width;
	public int height;
	public int depth;
	public byte[] blocks;
	public String name;
	public String creator;
	public long createTime;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	private transient ArrayList levelListeners = new ArrayList();
	private transient int[] heightMap;
	public transient Random random = new Random();
	private transient int randValue = this.random.nextInt();
	private transient ArrayList tickNextTickList = new ArrayList();
	public BlockMap blockMap;
	private boolean networkMode = false;
	public transient Minecraft rendererContext;
	public boolean creativeMode;
	public int waterLevel;
	public int skyColor;
	public int fogColor;
	public int cloudColor;
	int unprocessed = 0;
	private int tickCount = 0;
	public Entity player;
	public transient ParticleEngine particleEngine;
	public transient Object font;
	public boolean growTrees = false;

	public void initTransient() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.levelListeners = new ArrayList();
			this.heightMap = new int[this.width * this.height];
			Arrays.fill(this.heightMap, this.depth);
			this.calcLightDepths(0, 0, this.width, this.height);
			this.random = new Random();
			this.randValue = this.random.nextInt();
			this.tickNextTickList = new ArrayList();
			if(this.waterLevel == 0) {
				this.waterLevel = this.depth / 2;
			}

			if(this.skyColor == 0) {
				this.skyColor = 10079487;
			}

			if(this.fogColor == 0) {
				this.fogColor = 0xFFFFFF;
			}

			if(this.cloudColor == 0) {
				this.cloudColor = 0xFFFFFF;
			}

			if(this.xSpawn == 0 && this.ySpawn == 0 && this.zSpawn == 0) {
				this.findSpawn();
			}

			if(this.blockMap == null) {
				this.blockMap = new BlockMap(this.width, this.depth, this.height);
			}

		}
	}

	public void setData(int w, int h, int d, byte[] blocks) {
		this.width = w;
		this.height = d;
		this.depth = h;
		this.blocks = blocks;
		this.heightMap = new int[w * d];
		Arrays.fill(this.heightMap, this.depth);
		this.calcLightDepths(0, 0, w, d);

		for(w = 0; w < this.levelListeners.size(); ++w) {
			((LevelRenderer)this.levelListeners.get(w)).compileSurroundingGround();
		}

		this.tickNextTickList.clear();
		this.findSpawn();
		this.initTransient();
		System.gc();
	}

	public void findSpawn() {
		Random random1 = new Random();
		int i2 = 0;

		int i3;
		int i4;
		int i5;
		do {
			++i2;
			i3 = random1.nextInt(this.width / 2) + this.width / 4;
			i4 = random1.nextInt(this.height / 2) + this.height / 4;
			i5 = this.getHighestTile(i3, i4) + 1;
			if(i2 == 10000) {
				this.xSpawn = i3;
				this.ySpawn = -100;
				this.zSpawn = i4;
				return;
			}
		} while((float)i5 <= this.getWaterLevel());

		this.xSpawn = i3;
		this.ySpawn = i5;
		this.zSpawn = i4;
	}

	public void calcLightDepths(int x0, int y0, int x1, int y1) {
		for(int i5 = x0; i5 < x0 + x1; ++i5) {
			for(int i6 = y0; i6 < y0 + y1; ++i6) {
				int i7 = this.heightMap[i5 + i6 * this.width];

				int i8;
				for(i8 = this.depth - 1; i8 > 0 && !this.isLightBlocker(i5, i8, i6); --i8) {
				}

				this.heightMap[i5 + i6 * this.width] = i8;
				if(i7 != i8) {
					int i9 = i7 < i8 ? i7 : i8;
					i7 = i7 > i8 ? i7 : i8;

					for(i8 = 0; i8 < this.levelListeners.size(); ++i8) {
						((LevelRenderer)this.levelListeners.get(i8)).setDirty(i5 - 1, i9 - 1, i6 - 1, i5 + 1, i7 + 1, i6 + 1);
					}
				}
			}
		}

	}

	public void addListener(LevelRenderer levelRenderer) {
		this.levelListeners.add(levelRenderer);
	}

	public void finalize() {
	}

	public void removeListener(LevelRenderer levelRenderer) {
		this.levelListeners.remove(levelRenderer);
	}

	public boolean isLightBlocker(int x, int y, int z) {
		Tile tile4;
		return (tile4 = Tile.tiles[this.getTile(x, y, z)]) == null ? false : tile4.blocksLight();
	}

	public ArrayList getCubes(AABB c) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = (int)c.x0;
		int i4 = (int)c.x1 + 1;
		int i5 = (int)c.y0;
		int i6 = (int)c.y1 + 1;
		int i7 = (int)c.z0;
		int i8 = (int)c.z1 + 1;
		if(c.x0 < 0.0F) {
			--i3;
		}

		if(c.y0 < 0.0F) {
			--i5;
		}

		if(c.z0 < 0.0F) {
			--i7;
		}

		for(i3 = i3; i3 < i4; ++i3) {
			for(int i9 = i5; i9 < i6; ++i9) {
				for(int i10 = i7; i10 < i8; ++i10) {
					AABB aABB11;
					if(i3 >= 0 && i9 >= 0 && i10 >= 0 && i3 < this.width && i9 < this.depth && i10 < this.height) {
						Tile tile12;
						if((tile12 = Tile.tiles[this.getTile(i3, i9, i10)]) != null && (aABB11 = tile12.getTileAABB(i3, i9, i10)) != null && c.intersectsInner(aABB11)) {
							arrayList2.add(aABB11);
						}
					} else if((i3 < 0 || i9 < 0 || i10 < 0 || i3 >= this.width || i10 >= this.height) && (aABB11 = Tile.unbreakable.getTileAABB(i3, i9, i10)) != null && c.intersectsInner(aABB11)) {
						arrayList2.add(aABB11);
					}
				}
			}
		}

		return arrayList2;
	}

	public void swap(int x0, int y0, int z0, int x1, int y1, int z1) {
		if(!this.networkMode) {
			int i7 = this.getTile(x0, y0, z0);
			int i8 = this.getTile(x1, y1, z1);
			this.setTileNoNeighborChange(x0, y0, z0, i8);
			this.setTileNoNeighborChange(x1, y1, z1, i7);
			this.updateNeighborsAt(x0, y0, z0, i8);
			this.updateNeighborsAt(x1, y1, z1, i7);
		}
	}

	public boolean setTileNoNeighborChange(int x, int y, int z, int id) {
		return this.networkMode ? false : this.netSetTileNoNeighborChange(x, y, z, id);
	}

	public boolean netSetTileNoNeighborChange(int x, int y, int z, int id) {
		if(x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
			if(id == this.blocks[(y * this.height + z) * this.width + x]) {
				return false;
			} else {
				if(id == 0 && (x == 0 || z == 0 || x == this.width - 1 || z == this.height - 1) && (float)y >= this.getGroundLevel() && (float)y < this.getWaterLevel()) {
					id = Tile.water.id;
				}

				byte b5 = this.blocks[(y * this.height + z) * this.width + x];
				this.blocks[(y * this.height + z) * this.width + x] = (byte)id;
				if(b5 != 0) {
					Tile.tiles[b5].onTileRemoved(this, x, y, z);
				}

				if(id != 0) {
					Tile.tiles[id].onTileAdded(this, x, y, z);
				}

				this.calcLightDepths(x, z, 1, 1);

				for(id = 0; id < this.levelListeners.size(); ++id) {
					((LevelRenderer)this.levelListeners.get(id)).setDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setTile(int x, int y, int z, int id) {
		if(this.networkMode) {
			return false;
		} else if(this.setTileNoNeighborChange(x, y, z, id)) {
			this.updateNeighborsAt(x, y, z, id);
			return true;
		} else {
			return false;
		}
	}

	public boolean netSetTile(int x, int y, int z, int id) {
		if(this.netSetTileNoNeighborChange(x, y, z, id)) {
			this.updateNeighborsAt(x, y, z, id);
			return true;
		} else {
			return false;
		}
	}

	public void updateNeighborsAt(int x, int y, int z, int id) {
		this.neighborChanged(x - 1, y, z, id);
		this.neighborChanged(x + 1, y, z, id);
		this.neighborChanged(x, y - 1, z, id);
		this.neighborChanged(x, y + 1, z, id);
		this.neighborChanged(x, y, z - 1, id);
		this.neighborChanged(x, y, z + 1, id);
	}

	public boolean setTileNoUpdate(int x, int y, int z, int id) {
		if(x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
			if(id == this.blocks[(y * this.height + z) * this.width + x]) {
				return false;
			} else {
				this.blocks[(y * this.height + z) * this.width + x] = (byte)id;
				return true;
			}
		} else {
			return false;
		}
	}

	private void neighborChanged(int x, int y, int z, int id) {
		if(x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
			Tile tile5;
			if((tile5 = Tile.tiles[this.blocks[(y * this.height + z) * this.width + x]]) != null) {
				tile5.neighborChanged(this, x, y, z, id);
			}

		}
	}

	public boolean isLit(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height ? y >= this.heightMap[x + z * this.width] : true;
	}

	public int getTile(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height ? this.blocks[(y * this.height + z) * this.width + x] & 255 : 0;
	}

	public boolean isSolidTile(int x, int y, int z) {
		Tile tile4;
		return (tile4 = Tile.tiles[this.getTile(x, y, z)]) == null ? false : tile4.isSolid();
	}

	public void tickEntities() {
		this.blockMap.tickAll();
	}

	public void tick() {
		++this.tickCount;
		int i1 = 1;

		int i2;
		for(i2 = 1; 1 << i1 < this.width; ++i1) {
		}

		while(1 << i2 < this.height) {
			++i2;
		}

		int i3 = this.height - 1;
		int i4 = this.width - 1;
		int i5 = this.depth - 1;
		int i6;
		int i7;
		if(this.tickCount % 5 == 0) {
			i6 = this.tickNextTickList.size();

			for(i7 = 0; i7 < i6; ++i7) {
				Coord coord8;
				if((coord8 = (Coord)this.tickNextTickList.remove(0)).time > 0) {
					--coord8.time;
					this.tickNextTickList.add(coord8);
				} else {
					byte b9;
					if(this.isInLevelBounds(coord8.x, coord8.y, coord8.z) && (b9 = this.blocks[(coord8.y * this.height + coord8.z) * this.width + coord8.x]) == coord8.id && b9 > 0) {
						Tile.tiles[b9].tick(this, coord8.x, coord8.y, coord8.z, this.random);
					}
				}
			}
		}

		this.unprocessed += this.width * this.height * this.depth;
		i6 = this.unprocessed / 200;
		this.unprocessed -= i6 * 200;

		for(i7 = 0; i7 < i6; ++i7) {
			this.randValue = this.randValue * 3 + 1013904223;
			int i12;
			int i13 = (i12 = this.randValue >> 2) & i4;
			int i10 = i12 >> i1 & i3;
			i12 = i12 >> i1 + i2 & i5;
			byte b11 = this.blocks[(i12 * this.height + i10) * this.width + i13];
			if(Tile.shouldTick[b11]) {
				Tile.tiles[b11].tick(this, i13, i12, i10, this.random);
			}
		}

	}

	public int countInstanceOf(Class c) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.blockMap.all.size(); ++i3) {
			Entity entity4 = (Entity)this.blockMap.all.get(i3);
			if(c.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	private boolean isInLevelBounds(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height;
	}

	public float getGroundLevel() {
		return this.getWaterLevel() - 2.0F;
	}

	public float getWaterLevel() {
		return (float)this.waterLevel;
	}

	public boolean containsAnyLiquid(AABB c) {
		int i2 = (int)c.x0;
		int i3 = (int)c.x1 + 1;
		int i4 = (int)c.y0;
		int i5 = (int)c.y1 + 1;
		int i6 = (int)c.z0;
		int i7 = (int)c.z1 + 1;
		if(c.x0 < 0.0F) {
			--i2;
		}

		if(c.y0 < 0.0F) {
			--i4;
		}

		if(c.z0 < 0.0F) {
			--i6;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i4 < 0) {
			i4 = 0;
		}

		if(i6 < 0) {
			i6 = 0;
		}

		if(i3 > this.width) {
			i3 = this.width;
		}

		if(i5 > this.depth) {
			i5 = this.depth;
		}

		if(i7 > this.height) {
			i7 = this.height;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Tile tile9;
					if((tile9 = Tile.tiles[this.getTile(i10, i2, i8)]) != null && tile9.getLiquidType() != Liquid.none) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean containsLiquid(AABB c, Liquid liquid) {
		int i3 = (int)c.x0;
		int i4 = (int)c.x1 + 1;
		int i5 = (int)c.y0;
		int i6 = (int)c.y1 + 1;
		int i7 = (int)c.z0;
		int i8 = (int)c.z1 + 1;
		if(c.x0 < 0.0F) {
			--i3;
		}

		if(c.y0 < 0.0F) {
			--i5;
		}

		if(c.z0 < 0.0F) {
			--i7;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i5 < 0) {
			i5 = 0;
		}

		if(i7 < 0) {
			i7 = 0;
		}

		if(i4 > this.width) {
			i4 = this.width;
		}

		if(i6 > this.depth) {
			i6 = this.depth;
		}

		if(i8 > this.height) {
			i8 = this.height;
		}

		for(int i11 = i3; i11 < i4; ++i11) {
			for(i3 = i5; i3 < i6; ++i3) {
				for(int i9 = i7; i9 < i8; ++i9) {
					Tile tile10;
					if((tile10 = Tile.tiles[this.getTile(i11, i3, i9)]) != null && tile10.getLiquidType() == liquid) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void addToTickNextTick(int x, int y, int z, int id) {
		if(!this.networkMode) {
			Coord x1 = new Coord(x, y, z, id);
			if(id > 0) {
				z = Tile.tiles[id].getTickDelay();
				x1.time = z;
			}

			this.tickNextTickList.add(x1);
		}
	}

	public boolean isFree(AABB box) {
		return this.blockMap.getEntities((Entity)null, box).size() == 0;
	}

	public List findEntities(Entity entity, AABB box) {
		return this.blockMap.getEntities(entity, box);
	}

	public boolean isSolid(float x, float y, float z, float offset) {
		return this.isSolid(x - offset, y - offset, z - offset) ? true : (this.isSolid(x - offset, y - offset, z + offset) ? true : (this.isSolid(x - offset, y + offset, z - offset) ? true : (this.isSolid(x - offset, y + offset, z + offset) ? true : (this.isSolid(x + offset, y - offset, z - offset) ? true : (this.isSolid(x + offset, y - offset, z + offset) ? true : (this.isSolid(x + offset, y + offset, z - offset) ? true : this.isSolid(x + offset, y + offset, z + offset)))))));
	}

	private boolean isSolid(float x, float y, float z) {
		int i4;
		return (i4 = this.getTile((int)x, (int)y, (int)z)) > 0 && Tile.tiles[i4].isSolid();
	}

	public int getHighestTile(int x, int z) {
		int i3;
		for(i3 = this.depth; (this.getTile(x, i3 - 1, z) == 0 || Tile.tiles[this.getTile(x, i3 - 1, z)].getLiquidType() != Liquid.none) && i3 > 0; --i3) {
		}

		return i3;
	}

	public void setSpawnPos(int x, int y, int z, float rot) {
		this.xSpawn = x;
		this.ySpawn = y;
		this.zSpawn = z;
		this.rotSpawn = rot;
	}

	public float getBrightness(int x, int y, int z) {
		return this.isLit(x, y, z) ? 1.0F : 0.6F;
	}

	public float getCaveness(float x, float y, float z, float angle) {
		int i5 = (int)x;
		int i14 = (int)y;
		int i6 = (int)z;
		float f7 = 0.0F;
		float f8 = 0.0F;

		for(int i9 = i5 - 6; i9 <= i5 + 6; ++i9) {
			for(int i10 = i6 - 6; i10 <= i6 + 6; ++i10) {
				if(this.isInLevelBounds(i9, i14, i10) && !this.isSolidTile(i9, i14, i10)) {
					float f11 = (float)i9 + 0.5F - x;

					float f12;
					float f13;
					for(f13 = (float)(Math.atan2((double)(f12 = (float)i10 + 0.5F - z), (double)f11) - (double)(angle * (float)Math.PI / 180.0F) + 1.5707963705062866D); f13 < -3.1415927F; f13 += 6.2831855F) {
					}

					while(f13 >= (float)Math.PI) {
						f13 -= 6.2831855F;
					}

					if(f13 < 0.0F) {
						f13 = -f13;
					}

					f11 = Mth.sqrt(f11 * f11 + 4.0F + f12 * f12);
					f11 = 1.0F / f11;
					if(f13 > 1.0F) {
						f11 = 0.0F;
					}

					if(f11 < 0.0F) {
						f11 = 0.0F;
					}

					f8 += f11;
					if(this.isLit(i9, i14, i10)) {
						f7 += f11;
					}
				}
			}
		}

		if(f8 == 0.0F) {
			return 0.0F;
		} else {
			return f7 / f8;
		}
	}

	public float getCaveness(Entity entity) {
		float f2 = Mth.cos(-entity.yRot * 0.017453292F + (float)Math.PI);
		float f3 = Mth.sin(-entity.yRot * 0.017453292F + (float)Math.PI);
		float f4 = Mth.cos(-entity.xRot * 0.017453292F);
		float f5 = Mth.sin(-entity.xRot * 0.017453292F);
		float f6 = entity.x;
		float f7 = entity.y;
		float f21 = entity.z;
		float f8 = 1.6F;
		float f9 = 0.0F;
		float f10 = 0.0F;

		for(int i11 = 0; i11 <= 200; ++i11) {
			float f12 = ((float)i11 / (float)200 - 0.5F) * 2.0F;

			for(int i13 = 0; i13 <= 200; ++i13) {
				float f14 = ((float)i13 / (float)200 - 0.5F) * f8;
				float f16 = f4 * f14 + f5;
				f14 = f4 - f5 * f14;
				float f17 = f2 * f12 + f3 * f14;
				f16 = f16;
				f14 = f2 * f14 - f3 * f12;

				for(int i15 = 0; i15 < 10; ++i15) {
					float f18 = f6 + f17 * (float)i15 * 0.8F;
					float f19 = f7 + f16 * (float)i15 * 0.8F;
					float f20 = f21 + f14 * (float)i15 * 0.8F;
					if(this.isSolid(f18, f19, f20)) {
						break;
					}

					++f9;
					if(this.isLit((int)f18, (int)f19, (int)f20)) {
						++f10;
					}
				}
			}
		}

		if(f9 == 0.0F) {
			return 0.0F;
		} else {
			float f22;
			if((f22 = f10 / f9 / 0.1F) > 1.0F) {
				f22 = 1.0F;
			}

			f22 = 1.0F - f22;
			return 1.0F - f22 * f22 * f22;
		}
	}

	public byte[] copyBlocks() {
		return Arrays.copyOf(this.blocks, this.blocks.length);
	}

	public Liquid getLiquid(int x, int y, int z) {
		int i4;
		return (i4 = this.getTile(x, y, z)) == 0 ? Liquid.none : Tile.tiles[i4].getLiquidType();
	}

	public boolean isWater(int x, int y, int z) {
		int i4;
		return (i4 = this.getTile(x, y, z)) > 0 && Tile.tiles[i4].getLiquidType() == Liquid.water;
	}

	public void setNetworkMode(boolean online) {
		this.networkMode = online;
	}

	public HitResult clip(Vec3 v0, Vec3 v1) {
		if(!Float.isNaN(v0.x) && !Float.isNaN(v0.y) && !Float.isNaN(v0.z)) {
			if(!Float.isNaN(v1.x) && !Float.isNaN(v1.y) && !Float.isNaN(v1.z)) {
				int i3 = (int)Math.floor((double)v1.x);
				int i4 = (int)Math.floor((double)v1.y);
				int i5 = (int)Math.floor((double)v1.z);
				int i6 = (int)Math.floor((double)v0.x);
				int i7 = (int)Math.floor((double)v0.y);
				int i8 = (int)Math.floor((double)v0.z);
				int i9 = 20;

				while(i9-- >= 0) {
					if(Float.isNaN(v0.x) || Float.isNaN(v0.y) || Float.isNaN(v0.z)) {
						return null;
					}

					if(i6 == i3 && i7 == i4 && i8 == i5) {
						return null;
					}

					float f10 = 999.0F;
					float f11 = 999.0F;
					float f12 = 999.0F;
					if(i3 > i6) {
						f10 = (float)i6 + 1.0F;
					}

					if(i3 < i6) {
						f10 = (float)i6;
					}

					if(i4 > i7) {
						f11 = (float)i7 + 1.0F;
					}

					if(i4 < i7) {
						f11 = (float)i7;
					}

					if(i5 > i8) {
						f12 = (float)i8 + 1.0F;
					}

					if(i5 < i8) {
						f12 = (float)i8;
					}

					float f13 = 999.0F;
					float f14 = 999.0F;
					float f15 = 999.0F;
					float f16 = v1.x - v0.x;
					float f17 = v1.y - v0.y;
					float f18 = v1.z - v0.z;
					if(f10 != 999.0F) {
						f13 = (f10 - v0.x) / f16;
					}

					if(f11 != 999.0F) {
						f14 = (f11 - v0.y) / f17;
					}

					if(f12 != 999.0F) {
						f15 = (f12 - v0.z) / f18;
					}

					boolean z19 = false;
					byte b24;
					if(f13 < f14 && f13 < f15) {
						if(i3 > i6) {
							b24 = 4;
						} else {
							b24 = 5;
						}

						v0.x = f10;
						v0.y += f17 * f13;
						v0.z += f18 * f13;
					} else if(f14 < f15) {
						if(i4 > i7) {
							b24 = 0;
						} else {
							b24 = 1;
						}

						v0.x += f16 * f14;
						v0.y = f11;
						v0.z += f18 * f14;
					} else {
						if(i5 > i8) {
							b24 = 2;
						} else {
							b24 = 3;
						}

						v0.x += f16 * f15;
						v0.y += f17 * f15;
						v0.z = f12;
					}

					Vec3 vec320;
					i6 = (int)((vec320 = new Vec3(v0.x, v0.y, v0.z)).x = (float)Math.floor((double)v0.x));
					if(b24 == 5) {
						--i6;
						++vec320.x;
					}

					i7 = (int)(vec320.y = (float)Math.floor((double)v0.y));
					if(b24 == 1) {
						--i7;
						++vec320.y;
					}

					i8 = (int)(vec320.z = (float)Math.floor((double)v0.z));
					if(b24 == 3) {
						--i8;
						++vec320.z;
					}

					int i21 = this.getTile(i6, i7, i8);
					Tile tile23 = Tile.tiles[i21];
					if(i21 > 0 && tile23.getLiquidType() == Liquid.none) {
						HitResult hitResult22;
						if(tile23.isOpaque()) {
							if((hitResult22 = tile23.clip(i6, i7, i8, v0, v1)) != null) {
								return hitResult22;
							}
						} else if((hitResult22 = tile23.clip(i6, i7, i8, v0, v1)) != null) {
							return hitResult22;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSound(String soundName, Entity entity, float volume, float pitch) {
		if(this.rendererContext != null) {
			Minecraft minecraft5;
			if((minecraft5 = this.rendererContext).soundPlayer == null || !minecraft5.options.sound) {
				return;
			}

			AudioInfo soundName1;
			if(entity.distanceToSqr(minecraft5.player) < 1024.0F && (soundName1 = minecraft5.soundEngine.getAudioInfo(soundName, volume, pitch)) != null) {
				minecraft5.soundPlayer.play(soundName1, new EntitySoundPos(entity, minecraft5.player));
			}
		}

	}

	public void playSound(String soundName, float x, float y, float z, float volume, float pitch) {
		if(this.rendererContext != null) {
			Minecraft minecraft7;
			if((minecraft7 = this.rendererContext).soundPlayer == null || !minecraft7.options.sound) {
				return;
			}

			AudioInfo soundName1;
			if((soundName1 = minecraft7.soundEngine.getAudioInfo(soundName, volume, pitch)) != null) {
				minecraft7.soundPlayer.play(soundName1, new LevelSoundPos(x, y, z, minecraft7.player));
			}
		}

	}

	public boolean maybeGrowTree(int x, int y, int z) {
		int i4 = this.random.nextInt(3) + 4;
		boolean z5 = true;

		int i6;
		int i8;
		int i9;
		for(i6 = y; i6 <= y + 1 + i4; ++i6) {
			byte b7 = 1;
			if(i6 == y) {
				b7 = 0;
			}

			if(i6 >= y + 1 + i4 - 2) {
				b7 = 2;
			}

			for(i8 = x - b7; i8 <= x + b7 && z5; ++i8) {
				for(i9 = z - b7; i9 <= z + b7 && z5; ++i9) {
					if(i8 >= 0 && i6 >= 0 && i9 >= 0 && i8 < this.width && i6 < this.depth && i9 < this.height) {
						if((this.blocks[(i6 * this.height + i9) * this.width + i8] & 255) != 0) {
							z5 = false;
						}
					} else {
						z5 = false;
					}
				}
			}
		}

		if(!z5) {
			return false;
		} else if((this.blocks[((y - 1) * this.height + z) * this.width + x] & 255) == Tile.grass.id && y < this.depth - i4 - 1) {
			this.setTile(x, y - 1, z, Tile.dirt.id);

			int i13;
			for(i13 = y - 3 + i4; i13 <= y + i4; ++i13) {
				i8 = i13 - (y + i4);
				i9 = 1 - i8 / 2;

				for(int i10 = x - i9; i10 <= x + i9; ++i10) {
					int i12 = i10 - x;

					for(i6 = z - i9; i6 <= z + i9; ++i6) {
						int i11 = i6 - z;
						if(Math.abs(i12) != i9 || Math.abs(i11) != i9 || this.random.nextInt(2) != 0 && i8 != 0) {
							this.setTile(i10, i13, i6, Tile.leaf.id);
						}
					}
				}
			}

			for(i13 = 0; i13 < i4; ++i13) {
				this.setTile(x, y + i13, z, Tile.log.id);
			}

			return true;
		} else {
			return false;
		}
	}

	public Entity getPlayer() {
		return this.player;
	}

	public void addEntity(Entity entity) {
		this.blockMap.insert(entity);
		entity.setLevel(this);
	}

	public void removeEntity(Entity entity) {
		this.blockMap.remove(entity);
	}

	public void explode(Entity entity, float x, float y, float z, float radius) {
		int i6 = (int)(x - radius - 1.0F);
		int i7 = (int)(x + radius + 1.0F);
		int i8 = (int)(y - radius - 1.0F);
		int i9 = (int)(y + radius + 1.0F);
		int i10 = (int)(z - radius - 1.0F);
		int i11 = (int)(z + radius + 1.0F);

		int i13;
		float f15;
		float f16;
		for(int i12 = i6; i12 < i7; ++i12) {
			for(i13 = i9 - 1; i13 >= i8; --i13) {
				for(int i14 = i10; i14 < i11; ++i14) {
					f15 = (float)i12 + 0.5F - x;
					f16 = (float)i13 + 0.5F - y;
					float f17 = (float)i14 + 0.5F - z;
					int i20;
					if(i12 >= 0 && i13 >= 0 && i14 >= 0 && i12 < this.width && i13 < this.depth && i14 < this.height && f15 * f15 + f16 * f16 + f17 * f17 < radius * radius && (i20 = this.getTile(i12, i13, i14)) > 0 && Tile.tiles[i20].isExplodeable()) {
						Tile.tiles[i20].spawnResources(this, i12, i13, i14, 0.3F);
						this.setTile(i12, i13, i14, 0);
						Tile.tiles[i20].wasExploded(this, i12, i13, i14);
					}
				}
			}
		}

		List list18 = this.blockMap.getEntities(entity, (float)i6, (float)i8, (float)i10, (float)i7, (float)i9, (float)i11);

		for(i13 = 0; i13 < list18.size(); ++i13) {
			Entity entity19;
			if((f15 = (entity19 = (Entity)list18.get(i13)).distanceTo(x, y, z) / radius) <= 1.0F) {
				f16 = 1.0F - f15;
				entity19.hurt(entity, (int)(f16 * 15.0F + 1.0F));
			}
		}

	}

	public Entity findSubclassOf(Class c) {
		for(int i2 = 0; i2 < this.blockMap.all.size(); ++i2) {
			Entity entity3 = (Entity)this.blockMap.all.get(i2);
			if(c.isAssignableFrom(entity3.getClass())) {
				return entity3;
			}
		}

		return null;
	}

	public void removeAllNonCreativeModeEntities() {
		this.blockMap.removeAllNonCreativeModeEntities();
	}
}