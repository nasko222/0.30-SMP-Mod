package com.mojang.minecraft.sound;

import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.vorbis.VorbisStream;

import java.nio.ByteBuffer;

final class MusicThread extends Thread {
	private Music music;

	public MusicThread(Music music) {
		this.music = music;
		this.setPriority(10);
		this.setDaemon(true);
	}

	public final void run() {
		try {
			do {
				if(this.music.stopped) {
					return;
				}

				Music music1 = this.music;
				Music music10001;
				ByteBuffer byteBuffer2;
				if(this.music.playing == null) {
					music1 = this.music;
					if(this.music.current != null) {
						music1 = this.music;
						byteBuffer2 = this.music.current;
						music10001 = this.music;
						this.music.playing = byteBuffer2;
						byteBuffer2 = null;
						music1 = this.music;
						this.music.current = null;
						music1 = this.music;
						this.music.playing.clear();
					}
				}

				music1 = this.music;
				if(this.music.playing != null) {
					music1 = this.music;
					if(this.music.playing.remaining() != 0) {
						while(true) {
							music1 = this.music;
							if(this.music.playing.remaining() == 0) {
								break;
							}

							music1 = this.music;
							music1 = this.music;
							byteBuffer2 = this.music.playing;
							VorbisStream vorbisStream11 = this.music.stream;
							int i9 = this.music.stream.readPcm(byteBuffer2.array(), byteBuffer2.position(), byteBuffer2.remaining());
							byteBuffer2.position(byteBuffer2.position() + i9);
							boolean z10;
							if(z10 = i9 <= 0) {
								this.music.finished = true;
								this.music.stopped = true;
								break;
							}
						}
					}
				}

				music1 = this.music;
				if(this.music.playing != null) {
					music1 = this.music;
					if(this.music.previous == null) {
						music1 = this.music;
						this.music.playing.flip();
						music1 = this.music;
						byteBuffer2 = this.music.playing;
						music10001 = this.music;
						this.music.previous = byteBuffer2;
						byteBuffer2 = null;
						music1 = this.music;
						this.music.playing = byteBuffer2;
					}
				}

				Thread.sleep(10L);
				music1 = this.music;
			} while(this.music.player.running);

			return;
		} catch (EndOfOggStreamException endOfOggStreamException6) {
			return;
		} catch (Exception exception7) {
			exception7.printStackTrace();
		} finally {
			this.music.finished = true;
		}

	}
}