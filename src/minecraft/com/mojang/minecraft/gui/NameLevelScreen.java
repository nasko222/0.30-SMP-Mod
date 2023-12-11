package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;

import org.lwjgl.input.Keyboard;

public final class NameLevelScreen extends Screen {
	private Screen parent;
	private String title = "Enter level name:";
	private int id;
	private String name;
	private int counter = 0;

	public NameLevelScreen(Screen screen, String name, int id) {
		this.parent = screen;
		this.id = id;
		this.name = name;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void init() {
		this.buttons.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
		((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void buttonClicked(Button button) {
		if(button.enabled) {
			if(button.id == 0 && this.name.trim().length() > 1) {
				Minecraft minecraft10000 = this.minecraft;
				int i10001 = this.id;
				String string4 = this.name.trim();
				int i3 = i10001;
				Minecraft minecraft2 = minecraft10000;
				minecraft10000.levelIo.save(minecraft2.level, minecraft2.host, minecraft2.user.name, minecraft2.user.sessionid, string4, i3);
				this.minecraft.setScreen((Screen)null);
				this.minecraft.grabMouse();
			}

			if(button.id == 1) {
				this.minecraft.setScreen(this.parent);
			}

		}
	}

	protected final void keyPressed(char eventCharacter, int eventKey) {
		if(eventKey == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(eventCharacter) >= 0 && this.name.length() < 64) {
			this.name = this.name + eventCharacter;
		}

		((Button)this.buttons.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, this.title, this.width / 2, 40, 0xFFFFFF);
		int i3 = this.width / 2 - 100;
		int i4 = this.height / 2 - 10;
		fill(i3 - 1, i4 - 1, i3 + 200 + 1, i4 + 20 + 1, -6250336);
		fill(i3, i4, i3 + 200, i4 + 20, 0xFF000000);
		drawString(this.font, this.name + (this.counter / 6 % 2 == 0 ? "_" : ""), i3 + 4, i4 + 6, 14737632);
		super.render(xMouse, yMouse);
	}
}