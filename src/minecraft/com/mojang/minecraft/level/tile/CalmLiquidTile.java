package com.mojang.minecraft.level.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;

import java.util.Random;

public final class CalmLiquidTile extends LiquidTile {
	protected CalmLiquidTile(int i1, Liquid liquid2) {
		super(i1, liquid2);
		this.tileID = i1 - 1;
		this.calmTileID = i1;
		this.setTicking(false);
	}

	public final void tick(Level level, int x, int y, int z, Random random) {
	}

	public final void neighborChanged(Level level, int x, int y, int z, int type) {
		boolean z6 = false;
		if(level.getTile(x - 1, y, z) == 0) {
			z6 = true;
		}

		if(level.getTile(x + 1, y, z) == 0) {
			z6 = true;
		}

		if(level.getTile(x, y, z - 1) == 0) {
			z6 = true;
		}

		if(level.getTile(x, y, z + 1) == 0) {
			z6 = true;
		}

		if(level.getTile(x, y - 1, z) == 0) {
			z6 = true;
		}

		if(type != 0) {
			Liquid type1 = Tile.tiles[type].getLiquidType();
			if(this.liquid == Liquid.water && type1 == Liquid.lava || type1 == Liquid.water && this.liquid == Liquid.lava) {
				level.setTile(x, y, z, Tile.rock.id);
				return;
			}
		}

		if(z6) {
			level.setTileNoUpdate(x, y, z, this.tileID);
			level.addToTickNextTick(x, y, z, this.tileID);
		}

	}
}