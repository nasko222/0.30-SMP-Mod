package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;

public final class ControlsScreen extends Screen {
	private Screen parent;
	private String title = "Controls";
	private Options options;
	private int selectedKey = -1;

	public ControlsScreen(Screen screen, Options options) {
		this.parent = screen;
		this.options = options;
	}

	public final void init() {
		for(int i1 = 0; i1 < this.options.keys.length; ++i1) {
			this.buttons.add(new SmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getKeyMessage(i1)));
		}

		this.buttons.add(new Button(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void buttonClicked(Button button) {
		for(int i2 = 0; i2 < this.options.keys.length; ++i2) {
			((Button)this.buttons.get(i2)).msg = this.options.getKeyMessage(i2);
		}

		if(button.id == 200) {
			this.minecraft.setScreen(this.parent);
		} else {
			this.selectedKey = button.id;
			button.msg = "> " + this.options.getKeyMessage(button.id) + " <";
		}
	}

	protected final void keyPressed(char eventCharacter, int eventKey) {
		if(this.selectedKey >= 0) {
			this.options.setKey(this.selectedKey, eventKey);
			((Button)this.buttons.get(this.selectedKey)).msg = this.options.getKeyMessage(this.selectedKey);
			this.selectedKey = -1;
		} else {
			super.keyPressed(eventCharacter, eventKey);
		}
	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
		super.render(xMouse, yMouse);
	}
}