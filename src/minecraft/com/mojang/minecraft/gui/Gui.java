package com.mojang.minecraft.gui;

import com.mojang.minecraft.GuiMessage;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import util.Mth;

public final class Gui extends GuiComponent {
	public List messages = new ArrayList();
	private Random random = new Random();
	private Minecraft minecraft;
	private int scaledWidth;
	private int scaledHeight;
	public String hoveredUsername = null;
	public int tickCounter = 0;

	public Gui(Minecraft minecraft, int width, int height) {
		this.minecraft = minecraft;
		this.scaledWidth = width * 240 / height;
		this.scaledHeight = height * 240 / height;
	}

	public final void render(float scale, boolean playerAlive, int w, int h) {
		Font font5 = this.minecraft.font;
		this.minecraft.gameRenderer.render();
		Textures textures6 = this.minecraft.textures;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/gui.png"));
		Tesselator tesselator7 = Tesselator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		Inventory inventory8 = this.minecraft.player.inventory;
		this.blitOffset = -90.0F;
		this.blit(this.scaledWidth / 2 - 91, this.scaledHeight - 22, 0, 0, 182, 22);
		this.blit(this.scaledWidth / 2 - 91 - 1 + inventory8.selected * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/icons.png"));
		this.blit(this.scaledWidth / 2 - 7, this.scaledHeight / 2 - 7, 0, 0, 16, 16);
		boolean z9 = this.minecraft.player.invulnerableTime / 3 % 2 == 1;
		if(this.minecraft.player.invulnerableTime < 10) {
			z9 = false;
		}

		int i10 = this.minecraft.player.health;
		int i11 = this.minecraft.player.lastHealth;
		this.random.setSeed((long)(this.tickCounter * 312871));
		int i12;
		int i14;
		int i15;
		int i25;
		if(this.minecraft.gamemode.canHurtPlayer()) {
			for(i12 = 0; i12 < 10; ++i12) {
				byte b13 = 0;
				if(z9) {
					b13 = 1;
				}

				i14 = this.scaledWidth / 2 - 91 + (i12 << 3);
				i15 = this.scaledHeight - 32;
				if(i10 <= 4) {
					i15 += this.random.nextInt(2);
				}

				this.blit(i14, i15, 16 + b13 * 9, 0, 9, 9);
				if(z9) {
					if((i12 << 1) + 1 < i11) {
						this.blit(i14, i15, 70, 0, 9, 9);
					}

					if((i12 << 1) + 1 == i11) {
						this.blit(i14, i15, 79, 0, 9, 9);
					}
				}

				if((i12 << 1) + 1 < i10) {
					this.blit(i14, i15, 52, 0, 9, 9);
				}

				if((i12 << 1) + 1 == i10) {
					this.blit(i14, i15, 61, 0, 9, 9);
				}
			}

			if(this.minecraft.player.isUnderWater()) {
				i12 = (int)Math.ceil((double)(this.minecraft.player.airSupply - 2) * 10.0D / 300.0D);
				i25 = (int)Math.ceil((double)this.minecraft.player.airSupply * 10.0D / 300.0D) - i12;

				for(i14 = 0; i14 < i12 + i25; ++i14) {
					if(i14 < i12) {
						this.blit(this.scaledWidth / 2 - 91 + (i14 << 3), this.scaledHeight - 32 - 9, 16, 18, 9, 9);
					} else {
						this.blit(this.scaledWidth / 2 - 91 + (i14 << 3), this.scaledHeight - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);

		String string21;
		for(i12 = 0; i12 < inventory8.slots.length; ++i12) {
			i25 = this.scaledWidth / 2 - 90 + i12 * 20;
			i14 = this.scaledHeight - 16;
			if((i15 = inventory8.slots[i12]) > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)i25, (float)i14, -50.0F);
				if(inventory8.popTime[i12] > 0) {
					float f18;
					float f19 = -Mth.sin((f18 = ((float)inventory8.popTime[i12] - scale) / 5.0F) * f18 * (float)Math.PI) * 8.0F;
					float f23 = Mth.sin(f18 * f18 * (float)Math.PI) + 1.0F;
					float f16 = Mth.sin(f18 * (float)Math.PI) + 1.0F;
					GL11.glTranslatef(10.0F, f19 + 10.0F, 0.0F);
					GL11.glScalef(f23, f16, 1.0F);
					GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
				}

				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
				GL11.glScalef(-1.0F, -1.0F, -1.0F);
				int i20 = textures6.loadTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i20);
				tesselator7.begin();
				Tile.tiles[i15].render(tesselator7);
				tesselator7.end();
				GL11.glPopMatrix();
				if(inventory8.count[i12] > 1) {
					string21 = "" + inventory8.count[i12];
					font5.drawShadow(string21, i25 + 19 - font5.width(string21), i14 + 6, 0xFFFFFF);
				}
			}
		}

		font5.drawShadow("0.30 SMP MOD v0.12", 2, 2, 0xFFFFFF);
		if(this.minecraft.options.showFramerate) {
			font5.drawShadow(this.minecraft.fpsString, 2, 12, 0xFFFFFF);
		}

	

		byte b26 = 10;
		boolean z27 = false;
		if(this.minecraft.screen instanceof ChatScreen) {
			b26 = 20;
			z27 = true;
		}

		for(i14 = 0; i14 < this.messages.size() && i14 < b26; ++i14) {
			if(((GuiMessage)this.messages.get(i14)).counter < 200 || z27) {
				font5.drawShadow(((GuiMessage)this.messages.get(i14)).message, 2, this.scaledHeight - 8 - i14 * 9 - 20, 0xFFFFFF);
			}
		}

		i14 = this.scaledWidth / 2;
		i15 = this.scaledHeight / 2;
		this.hoveredUsername = null;
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.minecraft.networkClient != null && this.minecraft.networkClient.isConnected()) {
			List list22 = this.minecraft.networkClient.getUsernames();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
			GL11.glVertex2f((float)(i14 + 128), (float)(i15 - 68 - 12));
			GL11.glVertex2f((float)(i14 - 128), (float)(i15 - 68 - 12));
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
			GL11.glVertex2f((float)(i14 - 128), (float)(i15 + 68));
			GL11.glVertex2f((float)(i14 + 128), (float)(i15 + 68));
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			string21 = "Connected players:";
			font5.drawShadow(string21, i14 - font5.width(string21) / 2, i15 - 64 - 12, 0xFFFFFF);

			for(i11 = 0; i11 < list22.size(); ++i11) {
				int i28 = i14 + i11 % 2 * 120 - 120;
				int i17 = i15 - 64 + (i11 / 2 << 3);
				if(playerAlive && w >= i28 && h >= i17 && w < i28 + 120 && h < i17 + 8) {
					this.hoveredUsername = (String)list22.get(i11);
					font5.draw((String)list22.get(i11), i28 + 2, i17, 0xFFFFFF);
				} else {
					font5.draw((String)list22.get(i11), i28, i17, 15658734);
				}
			}
		}

	}

	public final void addMessage(String message) {
		this.messages.add(0, new GuiMessage(message));

		while(this.messages.size() > 50) {
			this.messages.remove(this.messages.size() - 1);
		}

	}
}