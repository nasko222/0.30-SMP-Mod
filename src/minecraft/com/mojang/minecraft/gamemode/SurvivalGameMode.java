package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.MobSpwaner;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.player.Player;

public final class SurvivalGameMode extends GameMode {
	public SurvivalGameMode(Minecraft minecraft) {
		super(minecraft);
	}

	private int x;
	private int y;
	private int z;
	private int oDestroyProgress;
	private int destroyProgress;
	private int delay;
	//private MobSpwaner mobSpawner;

	public final void initPlayer(Player player) {
		if (Minecraft.DontLoadToolbar) {
			Minecraft.DontLoadToolbar = false;
			return;
		}
		player.inventory.slots[0] = Tile.clothWhite.id;
		player.inventory.count[0] = 99;
		
		player.inventory.slots[2] = Tile.brick.id;
		player.inventory.count[2] = 99;
		player.inventory.slots[3] = Tile.glass.id;
		player.inventory.count[3] = 99;
		player.inventory.slots[4] = Tile.mossStone.id;
		player.inventory.count[4] = 99;
		player.inventory.slots[5] = Tile.sponge.id;
		player.inventory.count[5] = 99;
		player.inventory.slots[6] = Tile.bookshelf.id;
		player.inventory.count[6] = 99;
		player.inventory.slots[7] = Tile.tnt.id;
		player.inventory.count[7] = 99;
		player.inventory.slots[8] = Tile.obsidian.id;
		player.inventory.count[8] = 99;
	}

	public final void destroyBlock(int x, int y, int z) {
		int i4 = this.minecraft.level.getTile(x, y, z);
		Tile.tiles[i4].spawnResources(this.minecraft.level, x, y, z);
		super.destroyBlock(x, y, z);
	}

	public final boolean removeResource(int quantity) {
		return this.minecraft.player.inventory.removeResource(quantity);
	}

	public final void startDestroyBlock(int x, int y, int z) {
		int i4;
		if((i4 = this.minecraft.level.getTile(x, y, z)) > 0 && Tile.tiles[i4].getDestroyProgress() == 0) {
			this.destroyBlock(x, y, z);
		}

	}

	public final void stopDestroyBlock() {
		this.oDestroyProgress = 0;
		this.delay = 0;
	}

	public final void continueDestroyBlock(int x, int y, int z, int id) {
		if(this.delay > 0) {
			--this.delay;
		} else if(x == this.x && y == this.y && z == this.z) {
			int i5;
			if((i5 = this.minecraft.level.getTile(x, y, z)) != 0) {
				Tile tile6 = Tile.tiles[i5];
				this.destroyProgress = tile6.getDestroyProgress();
				tile6.destroy(this.minecraft.level, x, y, z, id, this.minecraft.particleEngine);
				++this.oDestroyProgress;
				if(this.oDestroyProgress == this.destroyProgress + 1) {
					this.destroyBlock(x, y, z);
					this.oDestroyProgress = 0;
					this.delay = 5;
				}

			}
		} else {
			this.oDestroyProgress = 0;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public final void render(float damageTime) {
		if(this.oDestroyProgress <= 0) {
			this.minecraft.levelRenderer.hurtTime = 0.0F;
		} else {
			this.minecraft.levelRenderer.hurtTime = ((float)this.oDestroyProgress + damageTime - 1.0F) / (float)this.destroyProgress;
		}
	}

	public final float getPickRange() {
		return 4.0F;
	}

	public final boolean removeResource(Player player, int quantity) {
		Tile tile3;
		if((tile3 = Tile.tiles[quantity]) == Tile.mushroom2 && this.minecraft.player.inventory.removeResource(quantity)) {
			player.hurt((Entity)null, 3);
			return true;
		} else if(tile3 == Tile.mushroom1 && this.minecraft.player.inventory.removeResource(quantity)) {
			player.heal(5);
			return true;
		} else {
			return false;
		}
	}

	public final void initLevel(Level level) {
		super.initLevel(level);
	}

	public final void tick() {

	}

	public final void createPlayer(Level level) {
		//this.mobSpawner = new MobSpwaner(level);
		SurvivalGameMode survivalGameMode2;
		(survivalGameMode2 = this).minecraft.loadingScreen.levelLoadUpdate("Spawning..");
		int level1 = level.width * level.height * level.depth / 800;
		//survivalGameMode2.mobSpawner.spawnMobs(level1, (Entity)null, survivalGameMode2.minecraft.loadingScreen);
	}
}
