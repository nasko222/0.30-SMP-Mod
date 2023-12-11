package com.mojang.minecraft.level.tile;

public enum Tile$SoundType {
	none("-", 0.0F, 0.0F),
	grass("grass", 0.6F, 1.0F),
	cloth("grass", 0.7F, 1.2F),
	gravel("gravel", 1.0F, 1.0F),
	stone("stone", 1.0F, 1.0F),
	metal("stone", 1.0F, 2.0F),
	wood("wood", 1.0F, 1.0F);

	public final String name;
	private final float volume;
	private final float pitch;

	private Tile$SoundType(String string3, float f4, float f5) {
		this.name = string3;
		this.volume = f4;
		this.pitch = f5;
	}

	public final float getVolume() {
		return this.volume / (Tile.random.nextFloat() * 0.4F + 1.0F) * 0.5F;
	}

	public final float getPitch() {
		return this.pitch / (Tile.random.nextFloat() * 0.2F + 0.9F);
	}
}