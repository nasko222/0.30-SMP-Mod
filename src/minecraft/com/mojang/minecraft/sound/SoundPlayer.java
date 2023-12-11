package com.mojang.minecraft.sound;

import com.mojang.minecraft.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.SourceDataLine;

public final class SoundPlayer implements Runnable {
	public boolean running = false;
	public SourceDataLine dataLine;
	private List audioQueue = new ArrayList();
	public Options options;

	public SoundPlayer(Options options) {
		this.options = options;
	}

	public final void play(Audio audio) {
		if(this.running) {
			List list2 = this.audioQueue;
			synchronized(this.audioQueue) {
				this.audioQueue.add(audio);
			}
		}
	}

	public final void play(AudioInfo audioInfo, SoundPos soundPos) {
		this.play(new Sound(audioInfo, soundPos));
	}

	public final void run() {
		int[] i1 = new int[4410];
		int[] i2 = new int[4410];

		for(byte[] b3 = new byte[17640]; this.running; this.dataLine.write(b3, 0, 17640)) {
			try {
				Thread.sleep(1L);
			} catch (InterruptedException interruptedException10) {
				interruptedException10.printStackTrace();
			}

			Arrays.fill(i1, 0, 4410, 0);
			Arrays.fill(i2, 0, 4410, 0);
			boolean z4 = true;
			int[] i6 = i2;
			int[] i5 = i1;
			List list12 = this.audioQueue;
			List list7 = this.audioQueue;
			synchronized(this.audioQueue) {
				int i8 = 0;

				while(true) {
					if(i8 >= list12.size()) {
						break;
					}

					if(!((Audio)list12.get(i8)).play(i5, i6, 4410)) {
						list12.remove(i8--);
					}

					++i8;
				}
			}

			int i13;
			if(!this.options.music && !this.options.sound) {
				for(i13 = 0; i13 < 4410; ++i13) {
					b3[i13 << 2] = 0;
					b3[(i13 << 2) + 1] = 0;
					b3[(i13 << 2) + 2] = 0;
					b3[(i13 << 2) + 3] = 0;
				}
			} else {
				for(i13 = 0; i13 < 4410; ++i13) {
					int i14 = i1[i13];
					int i15 = i2[i13];
					if(i14 < -32000) {
						i14 = -32000;
					}

					if(i15 < -32000) {
						i15 = -32000;
					}

					if(i14 >= 32000) {
						i14 = 32000;
					}

					if(i15 >= 32000) {
						i15 = 32000;
					}

					b3[i13 << 2] = (byte)(i14 >> 8);
					b3[(i13 << 2) + 1] = (byte)i14;
					b3[(i13 << 2) + 2] = (byte)(i15 >> 8);
					b3[(i13 << 2) + 3] = (byte)i15;
				}
			}
		}

	}
}