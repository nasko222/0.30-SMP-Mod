package com.mojang.minecraft.sound;

public final class SoundData {
	public final short[] data;
	public final float sampleRate;

	public SoundData(short[] data, float sampleRate) {
		this.data = data;
		this.sampleRate = sampleRate;
	}
}