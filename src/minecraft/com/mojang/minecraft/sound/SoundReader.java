package com.mojang.minecraft.sound;

import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.LogicalOggStreamImpl;
import de.jarnbjo.ogg.OnDemandUrlStream;
import de.jarnbjo.vorbis.IdentificationHeader;
import de.jarnbjo.vorbis.VorbisStream;

import java.io.IOException;
import java.net.URL;

public final class SoundReader {
	public static SoundData read(URL url) throws IOException {
		LogicalOggStreamImpl logicalOggStreamImpl11 = (LogicalOggStreamImpl)(new OnDemandUrlStream(url)).logicalStreams.values().iterator().next();
		VorbisStream vorbisStream12 = new VorbisStream(logicalOggStreamImpl11);
		byte[] b1 = new byte[4096];
		int i2 = 0;
		boolean z3 = false;
		IdentificationHeader identificationHeader14 = vorbisStream12.identificationHeader;
		int i4 = vorbisStream12.identificationHeader.channels;
		short[] s5 = new short[4096];
		int i6 = 0;

		while(true) {
			int i15;
			do {
				if(i2 < 0) {
					if(i6 != s5.length) {
						short[] s17 = s5;
						s5 = new short[i6];
						System.arraycopy(s17, 0, s5, 0, i6);
					}

					IdentificationHeader identificationHeader13;
					return new SoundData(s5, (float)(identificationHeader13 = vorbisStream12.identificationHeader).sampleRate);
				}

				i15 = 0;

				try {
					while(i15 < b1.length && (i2 = vorbisStream12.readPcm(b1, i15, b1.length - i15)) > 0) {
						i15 += i2;
					}
				} catch (EndOfOggStreamException endOfOggStreamException10) {
					i2 = -1;
				}
			} while(i15 <= 0);

			boolean z7 = false;

			int i8;
			for(int i16 = 0; i16 < i15; s5[i6++] = (short)(i8 / i4)) {
				i8 = 0;

				for(int i9 = 0; i9 < i4; ++i9) {
					i8 += b1[i16++] << 8 | b1[i16++] & 255;
				}

				if(i6 == s5.length) {
					short[] s18 = s5;
					s5 = new short[s5.length << 1];
					System.arraycopy(s18, 0, s5, 0, i6);
				}
			}
		}
	}
}