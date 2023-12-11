package com.mojang.minecraft.gui;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import org.lwjgl.opengl.GL11;

public final class BlockSelectionScreen extends Screen {
	public BlockSelectionScreen() {
		this.allowUserInput = true;
	}

	private int getTiles(int x, int y) {
		for(int i3 = 0; i3 < User.creativeTiles.size(); ++i3) {
			int i4 = this.width / 2 + i3 % 9 * 24 + -108 - 3;
			int i5 = this.height / 2 + i3 / 9 * 24 + -60 + 3;
			if(x >= i4 && x <= i4 + 24 && y >= i5 - 12 && y <= i5 + 12) {
				return i3;
			}
		}

		return -1;
	}

	public final void render(int xMouse, int yMouse) {
		xMouse = this.getTiles(xMouse, yMouse);
		fillGradient(this.width / 2 - 120, 30, this.width / 2 + 120, 180, -1878719232, -1070583712);
		if(xMouse >= 0) {
			yMouse = this.width / 2 + xMouse % 9 * 24 + -108;
			int i3 = this.height / 2 + xMouse / 9 * 24 + -60;
			fillGradient(yMouse - 3, i3 - 8, yMouse + 23, i3 + 24 - 6, -1862270977, -1056964609);
		}

		drawCenteredString(this.font, "Select cloth", this.width / 2, 40, 0xFFFFFF);
		Textures textures7 = this.minecraft.textures;
		Tesselator tesselator8 = Tesselator.instance;
		yMouse = textures7.loadTexture("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, yMouse);

		for(yMouse = 0; yMouse < User.creativeTiles.size(); ++yMouse) {
			Tile tile4 = (Tile)User.creativeTiles.get(yMouse);
			GL11.glPushMatrix();
			int i5 = this.width / 2 + yMouse % 9 * 24 + -108;
			int i6 = this.height / 2 + yMouse / 9 * 24 + -60;
			GL11.glTranslatef((float)i5, (float)i6, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			if(xMouse == yMouse) {
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			tesselator8.begin();
			tile4.render(tesselator8);
			tesselator8.end();
			GL11.glPopMatrix();
		}
	}

	protected final void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0) {
			if (this.minecraft.player.inventory.slots[this.minecraft.player.inventory.selected] >= 21 && this.minecraft.player.inventory.slots[this.minecraft.player.inventory.selected] <= 36)
			{
				if (this.minecraft.player.inventory.count[this.minecraft.player.inventory.selected] > 0)
				this.minecraft.player.inventory.replaceSlot(this.getTiles(x, y));
			}
			this.minecraft.setScreen((Screen)null);
		}

	}
}