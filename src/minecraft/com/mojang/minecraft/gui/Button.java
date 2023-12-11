package com.mojang.minecraft.gui;

public class Button extends GuiComponent {
	int w;
	int h;
	public int x;
	public int y;
	public String msg;
	public int id;
	public boolean enabled;
	public boolean visible;

	public Button(int id, int x, int y, String msg) {
		this(id, x, y, 200, 20, msg);
	}

	protected Button(int id, int x, int y, int w, int h, String msg) {
		this.w = 200;
		this.h = 20;
		this.enabled = true;
		this.visible = true;
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = 20;
		this.msg = msg;
	}
}