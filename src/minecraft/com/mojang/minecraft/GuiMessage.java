package com.mojang.minecraft;

public final class GuiMessage {
	public String message;
	public int counter;

	public GuiMessage(String message) {
		this.message = message;
		this.counter = 0;
	}
}