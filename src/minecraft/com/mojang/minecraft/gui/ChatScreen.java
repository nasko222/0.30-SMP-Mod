package com.mojang.minecraft.gui;

import com.mojang.minecraft.net.Client;
import com.mojang.minecraft.net.Packet;

import org.lwjgl.input.Keyboard;

public final class ChatScreen extends Screen {
	private String typedMsg = "";
	private int counter = 0;

	public final void init() {
		Keyboard.enableRepeatEvents(true);
	}

	public final void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void keyPressed(char eventCharacter, int eventKey) {
		if(eventKey == 1) {
			this.minecraft.setScreen((Screen)null);
		} else if(eventKey == 28) {
			Client client10000 = this.minecraft.networkClient;
			String eventKey1 = this.typedMsg.trim();
			Client eventCharacter1 = client10000;
			if((eventKey1 = eventKey1.trim()).length() > 0) {
				eventCharacter1.serverConnection.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, eventKey1});
			}

			this.minecraft.setScreen((Screen)null);
		} else {
			if(eventKey == 14 && this.typedMsg.length() > 0) {
				this.typedMsg = this.typedMsg.substring(0, this.typedMsg.length() - 1);
			}

			if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\\\"#%/()=+?[]{}<>@|$;".indexOf(eventCharacter) >= 0 && this.typedMsg.length() < 64 - (this.minecraft.user.name.length() + 2)) {
				this.typedMsg = this.typedMsg + eventCharacter;
			}

		}
	}

	public final void render(int xMouse, int yMouse) {
		fill(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		drawString(this.font, "> " + this.typedMsg + (this.counter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
	}

	protected final void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0 && this.minecraft.gui.hoveredUsername != null) {
			if(this.typedMsg.length() > 0 && !this.typedMsg.endsWith(" ")) {
				this.typedMsg = this.typedMsg + " ";
			}

			this.typedMsg = this.typedMsg + this.minecraft.gui.hoveredUsername;
			x = 64 - (this.minecraft.user.name.length() + 2);
			if(this.typedMsg.length() > x) {
				this.typedMsg = this.typedMsg.substring(0, x);
			}
		}

	}
}