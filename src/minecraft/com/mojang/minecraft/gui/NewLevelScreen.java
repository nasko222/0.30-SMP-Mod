package com.mojang.minecraft.gui;

public final class NewLevelScreen extends Screen {
	private Screen parent;

	public NewLevelScreen(Screen screen) {
		this.parent = screen;
	}

	public final void init() {
		this.buttons.clear();
		this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4, "Small"));
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 24, "Normal"));
		this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 48, "Huge"));
		this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 120, "Cancel"));
	}

	protected final void buttonClicked(Button button) {
		if(button.id == 3) {
			this.minecraft.setScreen(this.parent);
		} else {
			this.minecraft.generateLevel(button.id);
			this.minecraft.setScreen((Screen)null);
			this.minecraft.grabMouse();
		}
	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, "Generate new level", this.width / 2, 40, 0xFFFFFF);
		super.render(xMouse, yMouse);
	}
}