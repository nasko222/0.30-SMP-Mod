package com.mojang.minecraft.gui;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.ItemTile;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.tile.ItemTile.ItemMode;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import org.lwjgl.opengl.GL11;

public final class CraftMenu extends Screen {
	
	private int currentTile = 0;
	private Tile[] showcases = new Tile[] {Tile.wood, Tile.stoneBrick, Tile.iron, Tile.gold};
	private Tile showcaseTile = showcases[currentTile];
	
	private int thistick = 0;
	
	public CraftMenu() {
		this.allowUserInput = true;
	}

	private int getTiles(int x, int y) {
		for(int i3 = 0; i3 < User.tools.size(); ++i3) {
			int i4 = this.width / 2 + i3 % 3 * 24 + -108 - 3;
			int i5 = this.height / 2 + i3 / 3 * 24 + -60 + 3;
			if(x >= i4 && x <= i4 + 24 && y >= i5 - 12 && y <= i5 + 12) {
				return i3;
			}
		}

		return -1;
	}
	
	public void tick() {
		thistick++;
		if (thistick > 5) {
			thistick = 0;
			currentTile++;
			if (currentTile >= showcases.length) currentTile = 0;
			showcaseTile = showcases[currentTile];
		}
	}

	public final void render(int xMouse, int yMouse) {
		xMouse = this.getTiles(xMouse, yMouse);
		fillGradient(this.width / 2 - 120, 30, this.width / 2 + 120, 180, -1878719232, -1070583712);
		if(xMouse >= 0) {
			yMouse = this.width / 2 + xMouse % 3 * 24 + -108;
			int i3 = this.height / 2 + xMouse / 3 * 24 + -60;
			fillGradient(yMouse - 3, i3 - 8, yMouse + 23, i3 + 24 - 6, -1862270977, -1056964609);
		}

		drawCenteredString(this.font, "Craft tools", this.width / 2, 40, 0xFFFFFF);
		drawCenteredString(this.font, "  Pickaxe: 3x       2x", this.width / 2 + 25, 59, 0xFFFF00);
		drawCenteredString(this.font, "       Axe: 3x       2x", this.width / 2 + 25, 81, 0xFFFF00);
		drawCenteredString(this.font, "   Shovel: 1x       2x", this.width / 2 + 25, 103, 0xFFFF00);
		Textures textures7 = this.minecraft.textures;
		Tesselator tesselator8 = Tesselator.instance;
		yMouse = textures7.loadTexture("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, yMouse);
		
		for (int i = 0; i < 3; i++) {
			Tile tile41 = showcaseTile;
			GL11.glPushMatrix();
			int i51 = this.width / 2 + (6 + (i * 9)) % 9 * 24 + -105;
			int i61 = this.height / 2 + ((6 + (i * 9)) / 9 * 24 + -60 - (i+1));
			GL11.glTranslatef((float)i51, (float)i61, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			GL11.glScalef(1.2F, 1.2F, 1.2F);
			tesselator8.begin();
			tile41.render(tesselator8);
			tesselator8.end();
			GL11.glPopMatrix();
		}
		
		for (int i = 0; i < 3; i++) {
			Tile tile41 = Tile.wood;
			GL11.glPushMatrix();
			int i51 = this.width / 2 + (8 + (i * 9)) % 9 * 24 + -112;
			int i61 = this.height / 2 + ((8 + (i * 9)) / 9 * 24 + -60 - (i+1));
			GL11.glTranslatef((float)i51, (float)i61, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			GL11.glScalef(1.2F, 1.2F, 1.2F);
			tesselator8.begin();
			tile41.render(tesselator8);
			tesselator8.end();
			GL11.glPopMatrix();
		}

		for(yMouse = 0; yMouse < User.tools.size(); ++yMouse) {
			Tile tile4 = (Tile)User.tools.get(yMouse);
			GL11.glPushMatrix();
			int i5 = this.width / 2 + yMouse % 3 * 24 + -108;
			int i6 = this.height / 2 + yMouse / 3 * 24 + -60;
			GL11.glTranslatef((float)i5, (float)i6, 0.0F);
			GL11.glScalef(16.0F, 16.0F, 16.0F);
			GL11.glTranslatef(1.0F, 0.5F, 0.0F);
			GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
			GL11.glTranslatef(-0.4F, -0.2F, 0.0F);
			GL11.glScalef(-1.0F, -1.0F, -1.0F);
			if(xMouse == yMouse) {
				GL11.glTranslatef(1.0F, -0.2F, 0.0F);
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			tesselator8.begin();
			tile4.render(tesselator8);
			tesselator8.end();
			GL11.glPopMatrix();
		}
	}
	
	private void craftItem(int item) {
		if (item < 101) return;
		
		int pieces = 3;
		int pieceID = 5;
		
		if (ItemTile.getItemMode(item) == ItemMode.SHOVEL) pieces = 1;
		if (item >= 104) {
			pieceID = 4;
		}
		if (item >= 107) {
			pieceID = 42;
		}
		if (item >= 110) {
			pieceID = 41;
		}
		
		int test = 2;
		if (pieceID == 5) test += pieces;
		
		//System.out.println(test);
		
		//System.out.println(pieceID);
		
		//System.out.println("Crafting " + item);
		
		int num1 = -1;
		for (int i = 0; i < 9; i++) {
			if (this.minecraft.player.inventory.slots[i] == 5 && this.minecraft.player.inventory.count[i] >= test) {
				if (num1 == -1) num1 = i;
			}
		}
		if (num1 < 0) return;
		
		if (pieceID != 5) {
			int num2 = -1;
			for (int i = 0; i < 9; i++) {
				if (this.minecraft.player.inventory.slots[i] == pieceID && this.minecraft.player.inventory.count[i] >= pieces) {
					if (num2 == -1) num2 = i;
				}
			}
			if (num2 < 0) return;
			
			this.minecraft.player.inventory.count[num2]-=pieces;
			if (this.minecraft.player.inventory.count[num2] <= 0) this.minecraft.player.inventory.slots[num2] = -1;
		}
		
		this.minecraft.player.inventory.count[num1]-=test;
		if (this.minecraft.player.inventory.count[num1] <= 0) this.minecraft.player.inventory.slots[num1] = -1;
		
		this.minecraft.player.inventory.addTool(item);
	}

	protected final void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0) {
			craftItem(this.getTiles(x, y) + 101);
			this.minecraft.setScreen((Screen)null);
		}

	}
}