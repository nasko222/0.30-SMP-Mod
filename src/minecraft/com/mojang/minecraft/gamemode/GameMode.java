package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.BlockSelectionScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.tile.Tile$SoundType;
import com.mojang.minecraft.player.Player;

public class GameMode {
	protected final Minecraft minecraft;
	public boolean mode = false;

	public GameMode(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	public void initLevel(Level level) {
		level.creativeMode = false;
		level.growTrees = true;
	}

	public void handleOpenInventory() {
		this.minecraft.setScreen(new BlockSelectionScreen());
	}

	public void startDestroyBlock(int x, int y, int z) {
		this.destroyBlock(x, y, z);
	}

	public boolean removeResource(int quantity) {
		return true;
	}

	public void destroyBlock(int x, int y, int z) {
		Level level4 = this.minecraft.level;
		Tile tile5 = Tile.tiles[level4.getTile(x, y, z)];
		boolean z6 = level4.netSetTile(x, y, z, 0);
		if(tile5 != null && z6) {
			if(this.minecraft.isOnlineClient()) {
				this.minecraft.networkClient.sendTileUpdated(x, y, z, 0, 1);
			}

			if(tile5.soundType != Tile$SoundType.none) {
				level4.playSound("step." + tile5.soundType.name, (float)x, (float)y, (float)z, (tile5.soundType.getVolume() + 1.0F) / 2.0F, tile5.soundType.getPitch() * 0.8F);
			}

			tile5.destroy(level4, x, y, z, this.minecraft.particleEngine);
		}

	}

	public void continueDestroyBlock(int x, int y, int z, int id) {
	}

	public void stopDestroyBlock() {
	}

	public void render(float damageTime) {
	}

	public float getPickRange() {
		return 5.0F;
	}

	public boolean removeResource(Player player, int quantity) {
		return false;
	}

	public void initPlayer(Player player) {
	}

	public void tick() {
	}

	public void createPlayer(Level level) {
	}

	public boolean canHurtPlayer() {
		return true;
	}

	public void adjustPlayer(Player player) {
	}
}