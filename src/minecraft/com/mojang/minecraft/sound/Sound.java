package com.mojang.minecraft.sound;

public final class Sound implements Audio {
	private AudioInfo info;
	private SoundPos pos;
	private float pitch = 0.0F;
	private float volume = 1.0F;
	private static short[] data = new short[1];

	public Sound(AudioInfo audioInfo, SoundPos pos) {
		this.info = audioInfo;
		this.pos = pos;
		this.pitch = pos.getRotationDiff();
		this.volume = pos.getDistanceSq() * audioInfo.volume;
	}

	public final boolean play(int[] leftChannel, int[] rightChannel, int sampleRate) {
		if(data.length < sampleRate) {
			data = new short[sampleRate];
		}

		int i4;
		boolean z5 = (i4 = this.info.update(data, sampleRate)) > 0;
		float f6 = this.pos.getRotationDiff();
		float f7 = this.pos.getDistanceSq() * this.info.volume;
		int i8 = (int)((this.pitch > 0.0F ? 1.0F - this.pitch : 1.0F) * this.volume * 65536.0F);
		int i9 = (int)((this.pitch < 0.0F ? 1.0F + this.pitch : 1.0F) * this.volume * 65536.0F);
		int i10 = (int)((f6 > 0.0F ? 1.0F - f6 : 1.0F) * f7 * 65536.0F);
		int i11 = (int)((f6 < 0.0F ? f6 + 1.0F : 1.0F) * f7 * 65536.0F);
		i10 -= i8;
		i11 -= i9;
		int i12;
		int i13;
		int i14;
		if(i10 == 0 && i11 == 0) {
			if(i8 >= 0 || i9 != 0) {
				i12 = i8;
				i13 = i9;

				for(i14 = 0; i14 < i4; ++i14) {
					leftChannel[i14] += data[i14] * i12 >> 16;
					rightChannel[i14] += data[i14] * i13 >> 16;
				}
			}
		} else {
			for(i12 = 0; i12 < i4; ++i12) {
				i13 = i8 + i10 * i12 / sampleRate;
				i14 = i9 + i11 * i12 / sampleRate;
				leftChannel[i12] += data[i12] * i13 >> 16;
				rightChannel[i12] += data[i12] * i14 >> 16;
			}
		}

		this.pitch = f6;
		this.volume = f7;
		return z5;
	}
}