package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;

public final class OptionsScreen extends Screen {
	private Screen parent;
	private String title = "Options";
	private Options options;

	public OptionsScreen(Screen screen, Options options) {
		this.parent = screen;
		this.options = options;
	}

	public final void init() {
		for(int i1 = 0; i1 < this.options.optionCount; ++i1) {
			this.buttons.add(new SmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getMessage(i1)));
		}

		this.buttons.add(new Button(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls..."));
		this.buttons.add(new Button(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void buttonClicked(Button button) {
		if(button.enabled) {
			if(button.id < 100) {
				this.options.setOption(button.id, 1);
				button.msg = this.options.getMessage(button.id);
			}

			if(button.id == 100) {
				this.minecraft.setScreen(new ControlsScreen(this, this.options));
			}

			if(button.id == 200) {
				this.minecraft.setScreen(this.parent);
			}

		}
	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
		super.render(xMouse, yMouse);
	}
}