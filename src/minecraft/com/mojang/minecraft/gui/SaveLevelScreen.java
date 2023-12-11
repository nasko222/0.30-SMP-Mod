package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;

import java.io.File;

public final class SaveLevelScreen extends LoadLevelScreen {
	public SaveLevelScreen(Screen screen1) {
		super(screen1);
		this.title = "Save level";
		this.isSaveScreen = true;
	}

	public final void init() {
		super.init();
		((Button)this.buttons.get(5)).msg = "Save file...";
	}

	protected final void setLevels(String[] levels) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((Button)this.buttons.get(i2)).msg = levels[i2];
			((Button)this.buttons.get(i2)).visible = true;
			((Button)this.buttons.get(i2)).enabled = this.minecraft.user.hasPaid;
		}

	}

	public final void render(int xMouse, int yMouse) {
		super.render(xMouse, yMouse);
		if(!this.minecraft.user.hasPaid) {
			fillGradient(this.width / 2 - 80, 72, this.width / 2 + 80, 120, -536870912, -536870912);
			drawCenteredString(this.font, "Premium only!", this.width / 2, 80, 16748688);
			drawCenteredString(this.font, "Purchase the game to be able", this.width / 2, 96, 14712960);
			drawCenteredString(this.font, "to save your levels online.", this.width / 2, 104, 14712960);
		}
	}

	protected final void loadLevel(File file) {
		if(!file.getName().endsWith(".mine")) {
			file = new File(file.getParentFile(), file.getName() + ".mine");
		}

		File file2 = file;
		Minecraft file1 = this.minecraft;
		this.minecraft.levelIo.save(file1.level, file2);
		this.minecraft.setScreen(this.parent);
	}

	protected final void loadLevel(int id) {
		this.minecraft.setScreen(new NameLevelScreen(this, ((Button)this.buttons.get(id)).msg, id));
	}
}