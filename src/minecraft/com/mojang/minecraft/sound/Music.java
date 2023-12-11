package com.mojang.minecraft.sound;

import de.jarnbjo.ogg.LogicalOggStreamImpl;
import de.jarnbjo.ogg.OnDemandUrlStream;
import de.jarnbjo.vorbis.VorbisStream;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public final class Music implements Audio {
	ByteBuffer playing = ByteBuffer.allocate(176400);
	ByteBuffer current = ByteBuffer.allocate(176400);
	private ByteBuffer processing = null;
	ByteBuffer previous = null;
	VorbisStream stream;
	SoundPlayer player;
	boolean finished = false;
	boolean stopped = false;

	public Music(SoundPlayer soundPlayer, URL url) throws IOException {
		this.player = soundPlayer;
		LogicalOggStreamImpl soundPlayer1 = (LogicalOggStreamImpl)(new OnDemandUrlStream(url)).logicalStreams.values().iterator().next();
		this.stream = new VorbisStream(soundPlayer1);
		(new MusicThread(this)).start();
	}

	public final boolean play(int[] leftChannel, int[] rightChannel, int sampleRate) {
		if(!this.player.options.music) {
			this.stopped = true;
			return false;
		} else {
			sampleRate = sampleRate;
			int i4 = 0;

			while(sampleRate > 0 && (this.processing != null || this.previous != null)) {
				if(this.processing == null && this.previous != null) {
					this.processing = this.previous;
					this.previous = null;
				}

				if(this.processing != null && this.processing.remaining() > 0) {
					int i5;
					if((i5 = this.processing.remaining() / 4) > sampleRate) {
						i5 = sampleRate;
					}

					for(int i6 = 0; i6 < i5; ++i6) {
						leftChannel[i4 + i6] += this.processing.getShort();
						rightChannel[i4 + i6] += this.processing.getShort();
					}

					i4 += i5;
					sampleRate -= i5;
				}

				if(this.current == null && this.processing != null && this.processing.remaining() == 0) {
					this.current = this.processing;
					this.processing = null;
				}
			}

			return this.processing != null || this.previous != null || !this.finished;
		}
	}
}