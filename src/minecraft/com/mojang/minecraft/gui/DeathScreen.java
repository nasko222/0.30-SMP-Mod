package com.mojang.minecraft.gui;

import org.lwjgl.opengl.GL11;

public final class DeathScreen extends Screen {
	public final void init() {
		this.buttons.clear();
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 72, "Respawn"));

	}

	protected final void buttonClicked(Button button) {

		if(button.id == 1) {
			this.minecraft.player.resetPos();
			this.minecraft.gamemode.initPlayer(this.minecraft.player);
			this.minecraft.setScreen(null);
		}

	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.font, "You died!", this.width / 2 / 2, 30, 0xFFFFFF);
		GL11.glPopMatrix();
		//drawCenteredString(this.font, "Score: &e" + this.minecraft.player.getScore(), this.width / 2, 100, 0xFFFFFF);
		super.render(xMouse, yMouse);
	}
}