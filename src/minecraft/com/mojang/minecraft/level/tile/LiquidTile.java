package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Tesselator;

import java.util.Random;

public class LiquidTile extends Tile {
	protected Liquid liquid;
	protected int calmTileID;
	protected int tileID;

	protected LiquidTile(int id, Liquid liquid) {
		super(id);
		this.liquid = liquid;
		this.tex = 14;
		if(liquid == Liquid.lava) {
			this.tex = 30;
		}

		Tile.isLiquid[id] = true;
		this.tileID = id;
		this.calmTileID = id + 1;
		float id1 = 0.01F;
		float f3 = 0.1F;
		this.setShape(id1 + 0.0F, 0.0F - f3 + id1, id1 + 0.0F, id1 + 1.0F, 1.0F - f3 + id1, id1 + 1.0F);
		this.setTicking(true);
		if(liquid == Liquid.lava) {
			this.setTickSpeed(16);
		}

	}

	public final boolean isOpaque() {
		return false;
	}

	public final void onPlace(Level level, int x, int y, int z) {
		level.addToTickNextTick(x, y, z, this.tileID);
	}

	public void tick(Level level, int x, int y, int z, Random random) {
		boolean z7 = false;
		z = z;
		y = y;
		x = x;
		level = level;
		LiquidTile liquidTile8 = this;
		boolean z9 = false;

		boolean z6;
		do {
			--y;
			if(level.getTile(x, y, z) != 0 || !liquidTile8.checkSponge(level, x, y, z)) {
				break;
			}

			if(z6 = level.setTile(x, y, z, liquidTile8.tileID)) {
				z9 = true;
			}
		} while(z6 && liquidTile8.liquid != Liquid.lava);

		++y;
		if(liquidTile8.liquid == Liquid.water || !z9) {
			z9 = z9 | liquidTile8.checkWater(level, x - 1, y, z) | liquidTile8.checkWater(level, x + 1, y, z) | liquidTile8.checkWater(level, x, y, z - 1) | liquidTile8.checkWater(level, x, y, z + 1);
		}

		if(!z9) {
			level.setTileNoUpdate(x, y, z, liquidTile8.calmTileID);
		} else {
			level.addToTickNextTick(x, y, z, liquidTile8.tileID);
		}

	}

	private boolean checkSponge(Level level, int x, int y, int z) {
		if(this.liquid == Liquid.water) {
			for(int i7 = x - 2; i7 <= x + 2; ++i7) {
				for(int i5 = y - 2; i5 <= y + 2; ++i5) {
					for(int i6 = z - 2; i6 <= z + 2; ++i6) {
						if(level.getTile(i7, i5, i6) == Tile.sponge.id) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean checkWater(Level level, int x, int y, int z) {
		if(level.getTile(x, y, z) == 0) {
			if(!this.checkSponge(level, x, y, z)) {
				return false;
			}

			if(level.setTile(x, y, z, this.tileID)) {
				level.addToTickNextTick(x, y, z, this.tileID);
			}
		}

		return false;
	}

	protected final float getBrightness(Level level, int x, int y, int z) {
		return this.liquid == Liquid.lava ? 100.0F : level.getBrightness(x, y, z);
	}

	public final boolean shouldRenderFace(Level level, int x, int y, int z, int layer) {
		int i6;
		return x >= 0 && y >= 0 && z >= 0 && x < level.width && z < level.height ? ((i6 = level.getTile(x, y, z)) != this.tileID && i6 != this.calmTileID ? (layer == 1 && (level.getTile(x - 1, y, z) == 0 || level.getTile(x + 1, y, z) == 0 || level.getTile(x, y, z - 1) == 0 || level.getTile(x, y, z + 1) == 0) ? true : super.shouldRenderFace(level, x, y, z, layer)) : false) : false;
	}

	public final void renderFace(Tesselator t, int x, int y, int z, int face) {
		super.renderFace(t, x, y, z, face);
		super.renderBackFace(t, x, y, z, face);
	}

	public final AABB getTileAABB(int x, int y, int z) {
		return null;
	}

	public final boolean blocksLight() {
		return true;
	}

	public final boolean isSolid() {
		return false;
	}

	public final Liquid getLiquidType() {
		return this.liquid;
	}

	public void neighborChanged(Level level, int x, int y, int z, int type) {
		if(type != 0) {
			Liquid liquid6 = Tile.tiles[type].getLiquidType();
			if(this.liquid == Liquid.water && liquid6 == Liquid.lava || liquid6 == Liquid.water && this.liquid == Liquid.lava) {
				level.setTile(x, y, z, Tile.rock.id);
				return;
			}
		}

		level.addToTickNextTick(x, y, z, type);
	}

	public final int getTickDelay() {
		return this.liquid == Liquid.lava ? 5 : 0;
	}

	public final void spawnResources(Level level, int x, int y, int z, float chance) {
	}

	public final void spawnResources(Level level, int x, int y, int z) {
	}

	public final int resourceCount() {
		return 0;
	}

	public final int getRenderLayer() {
		return this.liquid == Liquid.water ? 1 : 0;
	}
}