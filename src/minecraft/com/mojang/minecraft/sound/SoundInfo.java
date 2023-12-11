package com.mojang.minecraft.sound;

public final class SoundInfo extends AudioInfo {
	private SoundData data;
	private float seek = 0.0F;
	private float pitch;

	public SoundInfo(SoundData data, float pitch, float volume) {
		this.data = data;
		this.pitch = pitch * 44100.0F / data.sampleRate;
		this.volume = volume;
	}

	public final int update(short[] data, int sampleRate) {
		if(this.seek >= (float)this.data.data.length) {
			return 0;
		} else {
			for(int i3 = 0; i3 < sampleRate; ++i3) {
				int i4 = (int)this.seek;
				short s5 = this.data.data[i4];
				short s6 = i4 < this.data.data.length - 1 ? this.data.data[i4 + 1] : 0;
				data[i3] = (short)((int)((float)s5 + (float)(s6 - s5) * (this.seek - (float)i4)));
				this.seek += this.pitch;
				if(this.seek >= (float)this.data.data.length) {
					return i3;
				}
			}

			return sampleRate;
		}
	}
}