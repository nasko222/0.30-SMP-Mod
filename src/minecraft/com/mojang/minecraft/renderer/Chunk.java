package com.mojang.minecraft.renderer;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

import org.lwjgl.opengl.GL11;

import util.Mth;

public final class Chunk {
	private Level level;
	private int lists = -1;
	private static Tesselator t = Tesselator.instance;
	public static int updates = 0;
	private int x0;
	private int y0;
	private int z0;
	private int x1;
	private int y1;
	private int z1;
	public boolean isInFrustum = false;
	private boolean[] skipRenderPass = new boolean[2];
	public boolean dirty;

	public Chunk(Level level, int x, int y, int z, int id1, int id2) {
		this.level = level;
		this.x0 = x;
		this.y0 = y;
		this.z0 = z;
		this.x1 = this.y1 = this.z1 = 16;
		Mth.sqrt((float)(this.x1 * this.x1 + this.y1 * this.y1 + this.z1 * this.z1));
		this.lists = id2;
		this.reset();
	}

	public final void rebuild() {
		++updates;
		int i1 = this.x0;
		int i2 = this.y0;
		int i3 = this.z0;
		int i4 = this.x0 + this.x1;
		int i5 = this.y0 + this.y1;
		int i6 = this.z0 + this.z1;

		int i7;
		for(i7 = 0; i7 < 2; ++i7) {
			this.skipRenderPass[i7] = true;
		}

		for(i7 = 0; i7 < 2; ++i7) {
			boolean z8 = false;
			boolean z9 = false;
			GL11.glNewList(this.lists + i7, GL11.GL_COMPILE);
			t.begin();

			for(int i10 = i1; i10 < i4; ++i10) {
				for(int i11 = i2; i11 < i5; ++i11) {
					for(int i12 = i3; i12 < i6; ++i12) {
						int i13;
						if((i13 = this.level.getTile(i10, i11, i12)) > 0) {
							Tile tile14;
							if((tile14 = Tile.tiles[i13]).getRenderLayer() != i7) {
								z8 = true;
							} else {
								z9 |= tile14.render(this.level, i10, i11, i12, t);
							}
						}
					}
				}
			}

			t.end();
			GL11.glEndList();
			if(z9) {
				this.skipRenderPass[i7] = false;
			}

			if(!z8) {
				break;
			}
		}

	}

	public final float compare(Player player) {
		float f2 = player.x - (float)this.x0;
		float f3 = player.y - (float)this.y0;
		float f4 = player.z - (float)this.z0;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void reset() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void clear() {
		this.reset();
		this.level = null;
	}

	public final int render(int[] chunkBuffer, int startingIndex, int renderPass) {
		if(!this.isInFrustum) {
			return startingIndex;
		} else {
			if(!this.skipRenderPass[renderPass]) {
				chunkBuffer[startingIndex++] = this.lists + renderPass;
			}

			return startingIndex;
		}
	}

	public final void isInFrustum(Frustum frustum) {
		this.isInFrustum = frustum.cubeInFrustum((float)this.x0, (float)this.y0, (float)this.z0, (float)(this.x0 + this.x1), (float)(this.y0 + this.y1), (float)(this.z0 + this.z1));
	}
}