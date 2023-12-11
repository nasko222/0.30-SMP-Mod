package com.mojang.minecraft.gui;

public final class ErrorScreen extends Screen {
	private String title;
	private String desc;

	public ErrorScreen(String msg2, String msg21) {
		this.title = msg2;
		this.desc = msg21;
	}

	public final void init() {
	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
		drawCenteredString(this.font, this.title, this.width / 2, 90, 0xFFFFFF);
		drawCenteredString(this.font, this.desc, this.width / 2, 110, 0xFFFFFF);
		super.render(xMouse, yMouse);
	}

	protected final void keyPressed(char eventCharacter, int eventKey) {
	}
}