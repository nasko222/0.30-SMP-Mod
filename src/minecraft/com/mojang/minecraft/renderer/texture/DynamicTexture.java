package com.mojang.minecraft.renderer.texture;

public class DynamicTexture {
	public byte[] pixels = new byte[1024];
	public int tex;
	public boolean anaglyph = false;

	public DynamicTexture(int tex) {
		this.tex = tex;
	}

	public void tick() {
	}
}