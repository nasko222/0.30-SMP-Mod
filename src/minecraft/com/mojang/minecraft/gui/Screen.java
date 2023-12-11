package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Screen extends GuiComponent {
	protected Minecraft minecraft;
	protected int width;
	protected int height;
	protected List buttons = new ArrayList();
	public boolean allowUserInput = false;
	protected Font font;

	public void render(int xMouse, int yMouse) {
		for(int i3 = 0; i3 < this.buttons.size(); ++i3) {
			Button button10000 = (Button)this.buttons.get(i3);
			Minecraft minecraft5 = this.minecraft;
			Button button4 = button10000;
			if(button10000.visible) {
				Font font8 = minecraft5.font;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft5.textures.loadTexture("/gui/gui.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				byte b9 = 1;
				boolean z6 = xMouse >= button4.x && yMouse >= button4.y && xMouse < button4.x + button4.w && yMouse < button4.y + button4.h;
				if(!button4.enabled) {
					b9 = 0;
				} else if(z6) {
					b9 = 2;
				}

				button4.blit(button4.x, button4.y, 0, 46 + b9 * 20, button4.w / 2, button4.h);
				button4.blit(button4.x + button4.w / 2, button4.y, 200 - button4.w / 2, 46 + b9 * 20, button4.w / 2, button4.h);
				if(!button4.enabled) {
					Button.drawCenteredString(font8, button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, -6250336);
				} else if(z6) {
					Button.drawCenteredString(font8, button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, 16777120);
				} else {
					Button.drawCenteredString(font8, button4.msg, button4.x + button4.w / 2, button4.y + (button4.h - 8) / 2, 14737632);
				}
			}
		}

	}

	protected void keyPressed(char eventCharacter, int eventKey) {
		if(eventKey == 1) {
			this.minecraft.setScreen((Screen)null);
			this.minecraft.grabMouse();
		}

	}

	protected void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0) {
			for(buttonNum = 0; buttonNum < this.buttons.size(); ++buttonNum) {
				Button button4;
				Button button5;
				if((button5 = button4 = (Button)this.buttons.get(buttonNum)).enabled && x >= button5.x && y >= button5.y && x < button5.x + button5.w && y < button5.y + button5.h) {
					this.buttonClicked(button4);
				}
			}
		}

	}

	protected void buttonClicked(Button button) {
	}

	public final void init(Minecraft minecraft, int width, int height) {
		this.minecraft = minecraft;
		this.font = minecraft.font;
		this.width = width;
		this.height = height;
		this.init();
	}

	public void init() {
	}

	public final void updateEvents() {
		while(Mouse.next()) {
			this.mouseEvent();
		}

		while(Keyboard.next()) {
			this.keyboardEvent();
		}

	}

	public final void mouseEvent() {
		if(Mouse.getEventButtonState()) {
			int i1 = Mouse.getEventX() * this.width / this.minecraft.width;
			int i2 = this.height - Mouse.getEventY() * this.height / this.minecraft.height - 1;
			this.mouseClicked(i1, i2, Mouse.getEventButton());
		}

	}

	public final void keyboardEvent() {
		if(Keyboard.getEventKeyState()) {
			this.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void tick() {
	}

	public void removed() {
	}
}