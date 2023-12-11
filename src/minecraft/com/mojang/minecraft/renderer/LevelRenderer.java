package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.player.Player;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class LevelRenderer {
	public Level level;
	public Textures textures;
	public int surroundLists;
	public IntBuffer ib = BufferUtils.createIntBuffer(65536);
	public List allDirtyChunks = new ArrayList();
	private Chunk[] sortedChunks;
	public Chunk[] chunks;
	private int xChunks;
	private int yChunks;
	private int zChunks;
	private int glLists;
	public Minecraft minecraft;
	private int[] chunkBuffer = new int[50000];
	public int cloudTickCounter = 0;
	private float lX = -9999.0F;
	private float lY = -9999.0F;
	private float lZ = -9999.0F;
	public float hurtTime;

	public LevelRenderer(Minecraft minecraft, Textures textures) {
		this.minecraft = minecraft;
		this.textures = textures;
		this.surroundLists = GL11.glGenLists(2);
		this.glLists = GL11.glGenLists(4096 << 6 << 1);
	}

	public final void compileSurroundingGround() {
		int i1;
		if(this.chunks != null) {
			for(i1 = 0; i1 < this.chunks.length; ++i1) {
				this.chunks[i1].clear();
			}
		}

		this.xChunks = this.level.width / 16;
		this.yChunks = this.level.depth / 16;
		this.zChunks = this.level.height / 16;
		this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		this.sortedChunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];
		i1 = 0;

		int i2;
		int i4;
		for(i2 = 0; i2 < this.xChunks; ++i2) {
			for(int i3 = 0; i3 < this.yChunks; ++i3) {
				for(i4 = 0; i4 < this.zChunks; ++i4) {
					this.chunks[(i4 * this.yChunks + i3) * this.xChunks + i2] = new Chunk(this.level, i2 << 4, i3 << 4, i4 << 4, 16, this.glLists + i1);
					this.sortedChunks[(i4 * this.yChunks + i3) * this.xChunks + i2] = this.chunks[(i4 * this.yChunks + i3) * this.xChunks + i2];
					i1 += 2;
				}
			}
		}

		for(i2 = 0; i2 < this.allDirtyChunks.size(); ++i2) {
			((Chunk)this.allDirtyChunks.get(i2)).dirty = false;
		}

		this.allDirtyChunks.clear();
		GL11.glNewList(this.surroundLists, GL11.GL_COMPILE);
		LevelRenderer levelRenderer9 = this;
		float f10 = 0.5F;
		GL11.glColor4f(0.5F, f10, f10, 1.0F);
		Tesselator tesselator11 = Tesselator.instance;
		float f12 = this.level.getGroundLevel();
		int i5 = 128;
		if(128 > this.level.width) {
			i5 = this.level.width;
		}

		if(i5 > this.level.height) {
			i5 = this.level.height;
		}

		int i6 = 2048 / i5;
		tesselator11.begin();

		int i7;
		for(i7 = -i5 * i6; i7 < levelRenderer9.level.width + i5 * i6; i7 += i5) {
			for(int i8 = -i5 * i6; i8 < levelRenderer9.level.height + i5 * i6; i8 += i5) {
				f10 = f12;
				if(i7 >= 0 && i8 >= 0 && i7 < levelRenderer9.level.width && i8 < levelRenderer9.level.height) {
					f10 = 0.0F;
				}

				tesselator11.vertexUV((float)i7, f10, (float)(i8 + i5), 0.0F, (float)i5);
				tesselator11.vertexUV((float)(i7 + i5), f10, (float)(i8 + i5), (float)i5, (float)i5);
				tesselator11.vertexUV((float)(i7 + i5), f10, (float)i8, (float)i5, 0.0F);
				tesselator11.vertexUV((float)i7, f10, (float)i8, 0.0F, 0.0F);
			}
		}

		tesselator11.end();
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		tesselator11.begin();

		for(i7 = 0; i7 < levelRenderer9.level.width; i7 += i5) {
			tesselator11.vertexUV((float)i7, 0.0F, 0.0F, 0.0F, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, 0.0F, (float)i5, 0.0F);
			tesselator11.vertexUV((float)(i7 + i5), f12, 0.0F, (float)i5, f12);
			tesselator11.vertexUV((float)i7, f12, 0.0F, 0.0F, f12);
			tesselator11.vertexUV((float)i7, f12, (float)levelRenderer9.level.height, 0.0F, f12);
			tesselator11.vertexUV((float)(i7 + i5), f12, (float)levelRenderer9.level.height, (float)i5, f12);
			tesselator11.vertexUV((float)(i7 + i5), 0.0F, (float)levelRenderer9.level.height, (float)i5, 0.0F);
			tesselator11.vertexUV((float)i7, 0.0F, (float)levelRenderer9.level.height, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(i7 = 0; i7 < levelRenderer9.level.height; i7 += i5) {
			tesselator11.vertexUV(0.0F, f12, (float)i7, 0.0F, 0.0F);
			tesselator11.vertexUV(0.0F, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV(0.0F, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tesselator11.vertexUV(0.0F, 0.0F, (float)i7, 0.0F, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)i7, 0.0F, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tesselator11.vertexUV((float)levelRenderer9.level.width, f12, (float)i7, 0.0F, 0.0F);
		}

		tesselator11.end();
		GL11.glEndList();
		GL11.glNewList(this.surroundLists + 1, GL11.GL_COMPILE);
		levelRenderer9 = this;
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		f10 = this.level.getWaterLevel();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tesselator11 = Tesselator.instance;
		i4 = 128;
		if(128 > this.level.width) {
			i4 = this.level.width;
		}

		if(i4 > this.level.height) {
			i4 = this.level.height;
		}

		i5 = 2048 / i4;
		tesselator11.begin();

		for(i6 = -i4 * i5; i6 < levelRenderer9.level.width + i4 * i5; i6 += i4) {
			for(i7 = -i4 * i5; i7 < levelRenderer9.level.height + i4 * i5; i7 += i4) {
				float f13 = f10 - 0.1F;
				if(i6 < 0 || i7 < 0 || i6 >= levelRenderer9.level.width || i7 >= levelRenderer9.level.height) {
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tesselator11.vertexUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tesselator11.vertexUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
				}
			}
		}

		tesselator11.end();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEndList();
		this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
	}

	public final int render(Player player, int layer) {
		float f3 = player.x - this.lX;
		float f4 = player.y - this.lY;
		float f5 = player.z - this.lZ;
		if(f3 * f3 + f4 * f4 + f5 * f5 > 64.0F) {
			this.lX = player.x;
			this.lY = player.y;
			this.lZ = player.z;
			Arrays.sort(this.sortedChunks, new DistanceSorter(player));
		}

		int i6 = 0;

		for(int i7 = 0; i7 < this.sortedChunks.length; ++i7) {
			i6 = this.sortedChunks[i7].render(this.chunkBuffer, i6, layer);
		}

		this.ib.clear();
		this.ib.put(this.chunkBuffer, 0, i6);
		this.ib.flip();
		if(this.ib.remaining() > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
			GL11.glCallLists(this.ib);
		}

		return this.ib.remaining();
	}

	public final void setDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
		x0 /= 16;
		y0 /= 16;
		z0 /= 16;
		x1 /= 16;
		y1 /= 16;
		z1 /= 16;
		if(x0 < 0) {
			x0 = 0;
		}

		if(y0 < 0) {
			y0 = 0;
		}

		if(z0 < 0) {
			z0 = 0;
		}

		if(x1 > this.xChunks - 1) {
			x1 = this.xChunks - 1;
		}

		if(y1 > this.yChunks - 1) {
			y1 = this.yChunks - 1;
		}

		if(z1 > this.zChunks - 1) {
			z1 = this.zChunks - 1;
		}

		for(x0 = x0; x0 <= x1; ++x0) {
			for(int i7 = y0; i7 <= y1; ++i7) {
				for(int i8 = z0; i8 <= z1; ++i8) {
					Chunk chunk9;
					if(!(chunk9 = this.chunks[(i8 * this.yChunks + i7) * this.xChunks + x0]).dirty) {
						chunk9.dirty = true;
						this.allDirtyChunks.add(this.chunks[(i8 * this.yChunks + i7) * this.xChunks + x0]);
					}
				}
			}
		}

	}
}