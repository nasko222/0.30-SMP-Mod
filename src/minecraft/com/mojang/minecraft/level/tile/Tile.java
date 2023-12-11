package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.particle.TerrainParticle;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;

import java.util.Random;

public class Tile {
	protected static Random random = new Random();
	public static final Tile[] tiles = new Tile[256];
	public static final boolean[] shouldTick = new boolean[256];
	private static boolean[] isSolid = new boolean[256];
	private static boolean[] isOpaque = new boolean[256];
	public static final boolean[] isLiquid = new boolean[256];
	private static int[] tickSpeed = new int[256];
	
	public static int amounts = 50;
	
	public static final Tile rock;
	public static final Tile grass;
	public static final Tile dirt;
	public static final Tile stoneBrick;
	public static final Tile wood;
	public static final Tile bush;
	public static final Tile unbreakable;
	public static final Tile water;
	public static final Tile calmWater;
	public static final Tile lava;
	public static final Tile calmLava;
	public static final Tile sand;
	public static final Tile gravel;
	public static final Tile goldOre;
	public static final Tile ironOre;
	public static final Tile coalOre;
	public static final Tile log;
	public static final Tile leaf;
	public static final Tile sponge;
	public static final Tile glass;
	public static final Tile clothRed;
	public static final Tile clothOrange;
	public static final Tile clothYellow;
	public static final Tile clothChartreuse;
	public static final Tile clothGreen;
	public static final Tile clothSpringGreen;
	public static final Tile clothCyan;
	public static final Tile clothCapri;
	public static final Tile clothUltramarine;
	public static final Tile clothViolet;
	public static final Tile clothPurple;
	public static final Tile clothMagenta;
	public static final Tile clothRose;
	public static final Tile clothDarkGray;
	public static final Tile clothGray;
	public static final Tile clothWhite;
	public static final Tile flower;
	public static final Tile rose;
	public static final Tile mushroom1;
	public static final Tile mushroom2;
	public static final Tile gold;
	public static final Tile iron;
	public static final Tile slabFull;
	public static final Tile slabHalf;
	public static final Tile brick;
	public static final Tile tnt;
	public static final Tile bookshelf;
	public static final Tile mossStone;
	public static final Tile obsidian;
	//public static final Tile chest;
	public int tex;
	public final int id;
	public Tile$SoundType soundType;
	private int destroyProgress;
	private boolean explodeable;
	public float xx0;
	public float yy0;
	public float zz0;
	public float xx1;
	public float yy1;
	public float zz1;
	public float particleGravity;

	protected Tile(int id) {
		this.explodeable = true;
		tiles[id] = this;
		this.id = id;
		this.setShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		isSolid[id] = this.isSolid();
		isOpaque[id] = this.isOpaque();
		isLiquid[id] = false;
	}

	public boolean isOpaque() {
		return true;
	}

	protected final Tile setSoundAndGravity(Tile$SoundType soundType, float volume, float gravity, float pitch) {
		this.particleGravity = gravity;
		this.soundType = soundType;
		this.destroyProgress = (int)(pitch * 20.0F);
		return this;
	}

	protected final void setTicking(boolean tick) {
		shouldTick[this.id] = tick;
	}

	protected final void setShape(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.xx0 = x0;
		this.yy0 = y0;
		this.zz0 = z0;
		this.xx1 = x1;
		this.yy1 = y1;
		this.zz1 = z1;
	}

	protected Tile(int id, int tex) {
		this(id);
		this.tex = tex;
	}

	public final void setTickSpeed(int ts) {
		tickSpeed[this.id] = 16;
	}

	public void render(Tesselator t) {
		float f2 = 0.5F;
		float f3 = 0.8F;
		float f4 = 0.6F;
		t.color(f2, f2, f2);
		this.renderFace(t, -2, 0, 0, 0);
		t.color(1.0F, 1.0F, 1.0F);
		this.renderFace(t, -2, 0, 0, 1);
		t.color(f3, f3, f3);
		this.renderFace(t, -2, 0, 0, 2);
		t.color(f3, f3, f3);
		this.renderFace(t, -2, 0, 0, 3);
		t.color(f4, f4, f4);
		this.renderFace(t, -2, 0, 0, 4);
		t.color(f4, f4, f4);
		this.renderFace(t, -2, 0, 0, 5);
	}

	protected float getBrightness(Level level, int x, int y, int z) {
		return level.getBrightness(x, y, z);
	}

	public boolean shouldRenderFace(Level level, int x, int y, int z, int layer) {
		return !level.isSolidTile(x, y, z);
	}

	protected int getTexture(int face) {
		return this.tex;
	}

	public void renderFace(Tesselator t, int x, int y, int z, int face) {
		int i6 = this.getTexture(face);
		this.renderFaceNoTexture(t, x, y, z, face, i6);
	}

	public final void renderFaceNoTexture(Tesselator t, int x, int y, int z, int face, int tex) {
		int i7 = tex % 16 << 4;
		int i8 = tex / 16 << 4;
		float f9 = (float)i7 / 256.0F;
		float f17 = ((float)i7 + 15.99F) / 256.0F;
		float f10 = (float)i8 / 256.0F;
		float f11 = ((float)i8 + 15.99F) / 256.0F;
		if(face >= 2 && tex < 240) {
			if(this.yy0 >= 0.0F && this.yy1 <= 1.0F) {
				f10 = ((float)i8 + this.yy0 * 15.99F) / 256.0F;
				f11 = ((float)i8 + this.yy1 * 15.99F) / 256.0F;
			} else {
				f10 = (float)i8 / 256.0F;
				f11 = ((float)i8 + 15.99F) / 256.0F;
			}
		}

		float tex1 = (float)x + this.xx0;
		float x1 = (float)x + this.xx1;
		float f18 = (float)y + this.yy0;
		float y1 = (float)y + this.yy1;
		float f12 = (float)z + this.zz0;
		float f13 = (float)z + this.zz1;
		if(face == 0) {
			t.vertexUV(tex1, f18, f13, f9, f11);
			t.vertexUV(tex1, f18, f12, f9, f10);
			t.vertexUV(x1, f18, f12, f17, f10);
			t.vertexUV(x1, f18, f13, f17, f11);
		} else if(face == 1) {
			t.vertexUV(x1, y1, f13, f17, f11);
			t.vertexUV(x1, y1, f12, f17, f10);
			t.vertexUV(tex1, y1, f12, f9, f10);
			t.vertexUV(tex1, y1, f13, f9, f11);
		} else if(face == 2) {
			t.vertexUV(tex1, y1, f12, f17, f10);
			t.vertexUV(x1, y1, f12, f9, f10);
			t.vertexUV(x1, f18, f12, f9, f11);
			t.vertexUV(tex1, f18, f12, f17, f11);
		} else if(face == 3) {
			t.vertexUV(tex1, y1, f13, f9, f10);
			t.vertexUV(tex1, f18, f13, f9, f11);
			t.vertexUV(x1, f18, f13, f17, f11);
			t.vertexUV(x1, y1, f13, f17, f10);
		} else if(face == 4) {
			t.vertexUV(tex1, y1, f13, f17, f10);
			t.vertexUV(tex1, y1, f12, f9, f10);
			t.vertexUV(tex1, f18, f12, f9, f11);
			t.vertexUV(tex1, f18, f13, f17, f11);
		} else if(face == 5) {
			t.vertexUV(x1, f18, f13, f9, f11);
			t.vertexUV(x1, f18, f12, f17, f11);
			t.vertexUV(x1, y1, f12, f17, f10);
			t.vertexUV(x1, y1, f13, f9, f10);
		}
	}

	public final void renderBackFace(Tesselator t, int x, int y, int z, int face) {
		int i6;
		float f7;
		float f8 = (f7 = (float)((i6 = this.getTexture(face)) % 16) / 16.0F) + 0.0624375F;
		float f16;
		float f9 = (f16 = (float)(i6 / 16) / 16.0F) + 0.0624375F;
		float f10 = (float)x + this.xx0;
		float x1 = (float)x + this.xx1;
		float f11 = (float)y + this.yy0;
		float y1 = (float)y + this.yy1;
		float f12 = (float)z + this.zz0;
		float f13 = (float)z + this.zz1;
		if(face == 0) {
			t.vertexUV(x1, f11, f13, f8, f9);
			t.vertexUV(x1, f11, f12, f8, f16);
			t.vertexUV(f10, f11, f12, f7, f16);
			t.vertexUV(f10, f11, f13, f7, f9);
		}

		if(face == 1) {
			t.vertexUV(f10, y1, f13, f7, f9);
			t.vertexUV(f10, y1, f12, f7, f16);
			t.vertexUV(x1, y1, f12, f8, f16);
			t.vertexUV(x1, y1, f13, f8, f9);
		}

		if(face == 2) {
			t.vertexUV(f10, f11, f12, f8, f9);
			t.vertexUV(x1, f11, f12, f7, f9);
			t.vertexUV(x1, y1, f12, f7, f16);
			t.vertexUV(f10, y1, f12, f8, f16);
		}

		if(face == 3) {
			t.vertexUV(x1, y1, f13, f8, f16);
			t.vertexUV(x1, f11, f13, f8, f9);
			t.vertexUV(f10, f11, f13, f7, f9);
			t.vertexUV(f10, y1, f13, f7, f16);
		}

		if(face == 4) {
			t.vertexUV(f10, f11, f13, f8, f9);
			t.vertexUV(f10, f11, f12, f7, f9);
			t.vertexUV(f10, y1, f12, f7, f16);
			t.vertexUV(f10, y1, f13, f8, f16);
		}

		if(face == 5) {
			t.vertexUV(x1, y1, f13, f7, f16);
			t.vertexUV(x1, y1, f12, f8, f16);
			t.vertexUV(x1, f11, f12, f8, f9);
			t.vertexUV(x1, f11, f13, f7, f9);
		}

	}

	public final AABB getAABB(int x, int y, int z) {
		return new AABB((float)x + this.xx0, (float)y + this.yy0, (float)z + this.zz0, (float)x + this.xx1, (float)y + this.yy1, (float)z + this.zz1);
	}

	public AABB getTileAABB(int x, int y, int z) {
		return new AABB((float)x + this.xx0, (float)y + this.yy0, (float)z + this.zz0, (float)x + this.xx1, (float)y + this.yy1, (float)z + this.zz1);
	}

	public boolean blocksLight() {
		return true;
	}

	public boolean isSolid() {
		return true;
	}

	public void tick(Level level, int x, int y, int z, Random random) {
	}

	public void destroy(Level level, int x, int y, int z, ParticleEngine particleEngine) {
		for(int i6 = 0; i6 < 4; ++i6) {
			for(int i7 = 0; i7 < 4; ++i7) {
				for(int i8 = 0; i8 < 4; ++i8) {
					float f9 = (float)x + ((float)i6 + 0.5F) / (float)4;
					float f10 = (float)y + ((float)i7 + 0.5F) / (float)4;
					float f11 = (float)z + ((float)i8 + 0.5F) / (float)4;
					particleEngine.addParticle(new TerrainParticle(level, f9, f10, f11, f9 - (float)x - 0.5F, f10 - (float)y - 0.5F, f11 - (float)z - 0.5F, this));
				}
			}
		}

	}

	public final void destroy(Level level, int x, int y, int z, int face, ParticleEngine particleEngine) {
		float f7 = 0.1F;
		float f8 = (float)x + random.nextFloat() * (this.xx1 - this.xx0 - f7 * 2.0F) + f7 + this.xx0;
		float f9 = (float)y + random.nextFloat() * (this.yy1 - this.yy0 - f7 * 2.0F) + f7 + this.yy0;
		float f10 = (float)z + random.nextFloat() * (this.zz1 - this.zz0 - f7 * 2.0F) + f7 + this.zz0;
		if(face == 0) {
			f9 = (float)y + this.yy0 - f7;
		}

		if(face == 1) {
			f9 = (float)y + this.yy1 + f7;
		}

		if(face == 2) {
			f10 = (float)z + this.zz0 - f7;
		}

		if(face == 3) {
			f10 = (float)z + this.zz1 + f7;
		}

		if(face == 4) {
			f8 = (float)x + this.xx0 - f7;
		}

		if(face == 5) {
			f8 = (float)x + this.xx1 + f7;
		}

		particleEngine.addParticle((new TerrainParticle(level, f8, f9, f10, 0.0F, 0.0F, 0.0F, this)).setPower(0.2F).scale(0.6F));
	}

	public Liquid getLiquidType() {
		return Liquid.none;
	}

	public void neighborChanged(Level level, int x, int y, int z, int type) {
	}

	public void onPlace(Level level, int x, int y, int z) {
	}

	public int getTickDelay() {
		return 0;
	}

	public void onTileAdded(Level level, int x, int y, int z) {
	}

	public void onTileRemoved(Level level, int x, int y, int z) {
	}

	public int resourceCount() {
		return 1;
	}

	public int getId() {
		return this.id;
	}

	public final int getDestroyProgress() {
		return this.destroyProgress;
	}

	public void spawnResources(Level level, int x, int y, int z) {
		this.spawnResources(level, x, y, z, 1.0F);
	}

	public void spawnResources(Level level, int x, int y, int z, float chance) {
		if(!level.creativeMode) {
			int i6 = this.resourceCount();

			for(int i7 = 0; i7 < i6; ++i7) {
				if(random.nextFloat() <= chance) {
					((Player)level.player).addResource(this.getId());
				}
			}

		}
	}

	public void renderGuiTile(Tesselator t) {
		t.begin();

		for(int i2 = 0; i2 < 6; ++i2) {
			if(i2 == 0) {
				t.normal(0.0F, 1.0F, 0.0F);
			}

			if(i2 == 1) {
				t.normal(0.0F, -1.0F, 0.0F);
			}

			if(i2 == 2) {
				t.normal(0.0F, 0.0F, 1.0F);
			}

			if(i2 == 3) {
				t.normal(0.0F, 0.0F, -1.0F);
			}

			if(i2 == 4) {
				t.normal(1.0F, 0.0F, 0.0F);
			}

			if(i2 == 5) {
				t.normal(-1.0F, 0.0F, 0.0F);
			}

			this.renderFace(t, 0, 0, 0, i2);
		}

		t.end();
	}

	public final boolean isExplodeable() {
		return this.explodeable;
	}

	public final HitResult clip(int x, int y, int z, Vec3 v0, Vec3 v1) {
		v0 = v0.add((float)(-x), (float)(-y), (float)(-z));
		v1 = v1.add((float)(-x), (float)(-y), (float)(-z));
		Vec3 vec36 = v0.clipX(v1, this.xx0);
		Vec3 vec37 = v0.clipX(v1, this.xx1);
		Vec3 vec38 = v0.clipY(v1, this.yy0);
		Vec3 vec39 = v0.clipY(v1, this.yy1);
		Vec3 vec310 = v0.clipZ(v1, this.zz0);
		v1 = v0.clipZ(v1, this.zz1);
		if(!this.containsX(vec36)) {
			vec36 = null;
		}

		if(!this.containsX(vec37)) {
			vec37 = null;
		}

		if(!this.containsY(vec38)) {
			vec38 = null;
		}

		if(!this.containsY(vec39)) {
			vec39 = null;
		}

		if(!this.containsZ(vec310)) {
			vec310 = null;
		}

		if(!this.containsZ(v1)) {
			v1 = null;
		}

		Vec3 vec311 = null;
		if(vec36 != null) {
			vec311 = vec36;
		}

		if(vec37 != null && (vec311 == null || v0.distanceTo(vec37) < v0.distanceTo(vec311))) {
			vec311 = vec37;
		}

		if(vec38 != null && (vec311 == null || v0.distanceTo(vec38) < v0.distanceTo(vec311))) {
			vec311 = vec38;
		}

		if(vec39 != null && (vec311 == null || v0.distanceTo(vec39) < v0.distanceTo(vec311))) {
			vec311 = vec39;
		}

		if(vec310 != null && (vec311 == null || v0.distanceTo(vec310) < v0.distanceTo(vec311))) {
			vec311 = vec310;
		}

		if(v1 != null && (vec311 == null || v0.distanceTo(v1) < v0.distanceTo(vec311))) {
			vec311 = v1;
		}

		if(vec311 == null) {
			return null;
		} else {
			byte v01 = -1;
			if(vec311 == vec36) {
				v01 = 4;
			}

			if(vec311 == vec37) {
				v01 = 5;
			}

			if(vec311 == vec38) {
				v01 = 0;
			}

			if(vec311 == vec39) {
				v01 = 1;
			}

			if(vec311 == vec310) {
				v01 = 2;
			}

			if(vec311 == v1) {
				v01 = 3;
			}

			return new HitResult(x, y, z, v01, vec311.add((float)x, (float)y, (float)z));
		}
	}

	private boolean containsX(Vec3 t) {
		return t == null ? false : t.y >= this.yy0 && t.y <= this.yy1 && t.z >= this.zz0 && t.z <= this.zz1;
	}

	private boolean containsY(Vec3 t) {
		return t == null ? false : t.x >= this.xx0 && t.x <= this.xx1 && t.z >= this.zz0 && t.z <= this.zz1;
	}

	private boolean containsZ(Vec3 t) {
		return t == null ? false : t.x >= this.xx0 && t.x <= this.xx1 && t.y >= this.yy0 && t.y <= this.yy1;
	}

	public void wasExploded(Level level, int x, int y, int z) {
	}

	public boolean render(Level level, int x, int y, int z, Tesselator t) {
		boolean z6 = false;
		float f7 = 0.5F;
		float f8 = 0.8F;
		float f9 = 0.6F;
		float f10;
		if(this.shouldRenderFace(level, x, y - 1, z, 0)) {
			f10 = this.getBrightness(level, x, y - 1, z);
			t.color(f7 * f10, f7 * f10, f7 * f10);
			this.renderFace(t, x, y, z, 0);
			z6 = true;
		}

		if(this.shouldRenderFace(level, x, y + 1, z, 1)) {
			f10 = this.getBrightness(level, x, y + 1, z);
			t.color(f10 * 1.0F, f10 * 1.0F, f10 * 1.0F);
			this.renderFace(t, x, y, z, 1);
			z6 = true;
		}

		if(this.shouldRenderFace(level, x, y, z - 1, 2)) {
			f10 = this.getBrightness(level, x, y, z - 1);
			t.color(f8 * f10, f8 * f10, f8 * f10);
			this.renderFace(t, x, y, z, 2);
			z6 = true;
		}

		if(this.shouldRenderFace(level, x, y, z + 1, 3)) {
			f10 = this.getBrightness(level, x, y, z + 1);
			t.color(f8 * f10, f8 * f10, f8 * f10);
			this.renderFace(t, x, y, z, 3);
			z6 = true;
		}

		if(this.shouldRenderFace(level, x - 1, y, z, 4)) {
			f10 = this.getBrightness(level, x - 1, y, z);
			t.color(f9 * f10, f9 * f10, f9 * f10);
			this.renderFace(t, x, y, z, 4);
			z6 = true;
		}

		if(this.shouldRenderFace(level, x + 1, y, z, 5)) {
			f10 = this.getBrightness(level, x + 1, y, z);
			t.color(f9 * f10, f9 * f10, f9 * f10);
			this.renderFace(t, x, y, z, 5);
			z6 = true;
		}

		return z6;
	}

	public int getRenderLayer() {
		return 0;
	}

	static {
		Tile tile10000 = (new StoneTile(1, 1)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		boolean z0 = false;
		Tile tile1 = tile10000;
		tile10000.explodeable = false;
		rock = tile1;
		grass = (new GrassTile(2)).setSoundAndGravity(Tile$SoundType.grass, 0.9F, 1.0F, 0.6F);
		dirt = (new DirtTile(3, 2)).setSoundAndGravity(Tile$SoundType.grass, 0.8F, 1.0F, 0.5F);
		tile10000 = (new Tile(4, 16)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 1.5F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		stoneBrick = tile1;
		wood = (new Tile(5, 4)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		bush = (new Bush(6, 15)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);
		tile10000 = (new Tile(7, 17)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 999.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		unbreakable = tile1;
		water = (new LiquidTile(8, Liquid.water)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		calmWater = (new CalmLiquidTile(9, Liquid.water)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		lava = (new LiquidTile(10, Liquid.lava)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		calmLava = (new CalmLiquidTile(11, Liquid.lava)).setSoundAndGravity(Tile$SoundType.none, 1.0F, 1.0F, 100.0F);
		sand = (new FallingTile(12, 18)).setSoundAndGravity(Tile$SoundType.gravel, 0.8F, 1.0F, 0.5F);
		gravel = (new FallingTile(13, 19)).setSoundAndGravity(Tile$SoundType.gravel, 0.8F, 1.0F, 0.6F);
		tile10000 = (new OreTile(14, 32)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		goldOre = tile1;
		tile10000 = (new OreTile(15, 33)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		ironOre = tile1;
		tile10000 = (new OreTile(16, 34)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 3.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		coalOre = tile1;
		log = (new LogTile(17)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F, 2.5F);
		leaf = (new LeafTile(18, 22)).setSoundAndGravity(Tile$SoundType.grass, 1.0F, 0.4F, 0.2F);
		sponge = (new SpongeTile(19)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 0.9F, 0.6F);
		glass = (new GlassTile(20, 49, false)).setSoundAndGravity(Tile$SoundType.metal, 1.0F, 1.0F, 0.3F);
		clothRed = (new Tile(21, 64)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothOrange = (new Tile(22, 65)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothYellow = (new Tile(23, 66)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothChartreuse = (new Tile(24, 67)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothGreen = (new Tile(25, 68)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothSpringGreen = (new Tile(26, 69)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothCyan = (new Tile(27, 70)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothCapri = (new Tile(28, 71)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothUltramarine = (new Tile(29, 72)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothViolet = (new Tile(30, 73)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothPurple = (new Tile(31, 74)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothMagenta = (new Tile(32, 75)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothRose = (new Tile(33, 76)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothDarkGray = (new Tile(34, 77)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothGray = (new Tile(35, 78)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		clothWhite = (new Tile(36, 79)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.8F);
		flower = (new Flower(37, 13)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);
		rose = (new Flower(38, 12)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);
		mushroom1 = (new Mushroom(39, 29)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);
		mushroom2 = (new Mushroom(40, 28)).setSoundAndGravity(Tile$SoundType.none, 0.7F, 1.0F, 0.0F);
		tile10000 = (new MetalTile(41, 40)).setSoundAndGravity(Tile$SoundType.metal, 0.7F, 1.0F, 3.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		gold = tile1;
		tile10000 = (new MetalTile(42, 39)).setSoundAndGravity(Tile$SoundType.metal, 0.7F, 1.0F, 5.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		iron = tile1;
		tile10000 = (new SlabTile(43, true)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		slabFull = tile1;
		tile10000 = (new SlabTile(44, false)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		slabHalf = tile1;
		tile10000 = (new Tile(45, 7)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 2.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		brick = tile1;
		tnt = (new TntTile(46, 8)).setSoundAndGravity(Tile$SoundType.cloth, 1.0F, 1.0F, 0.0F);
		bookshelf = (new BookshelfTile(47, 35)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		tile10000 = (new Tile(48, 36)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 1.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		mossStone = tile1;
		tile10000 = (new StoneTile(49, 37)).setSoundAndGravity(Tile$SoundType.stone, 1.0F, 1.0F, 10.0F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		obsidian = tile1;
		/*tile10000 = (new ChestTile(50)).setSoundAndGravity(Tile$SoundType.wood, 1.0F, 1.0F, 1.5F);
		z0 = false;
		tile1 = tile10000;
		tile10000.explodeable = false;
		chest = tile1;*/
	}
}