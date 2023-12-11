package com.mojang.minecraft.gamemode;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.User;
import com.mojang.minecraft.gui.BlockSelectionScreen;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;

public final class CreativeGameMode extends GameMode {
	public CreativeGameMode(Minecraft minecraft1) {
		super(minecraft1);
		this.mode = true;
	}

	public final void handleOpenInventory() {
		this.minecraft.setScreen(new BlockSelectionScreen());
	}

	public final void initLevel(Level level) {
		super.initLevel(level);
		level.removeAllNonCreativeModeEntities();
		level.creativeMode = true;
		level.growTrees = false;
	}

	public final void adjustPlayer(Player player) {
		for(int i2 = 0; i2 < 9; ++i2) {
			player.inventory.count[i2] = 1;
			if(player.inventory.slots[i2] <= 0) {
				player.inventory.slots[i2] = ((Tile)User.creativeTiles.get(i2)).id;
			}
		}

	}

	public final boolean canHurtPlayer() {
		return false;
	}
}