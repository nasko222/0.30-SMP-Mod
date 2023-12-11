package com.mojang.minecraft.sound;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SoundEngine {
	private SoundReader reader = new SoundReader();
	public Map sounds = new HashMap();
	private Map music = new HashMap();
	public Random random = new Random();
	public long lastMusic = System.currentTimeMillis() + 60000L;

	public final AudioInfo getAudioInfo(String soundName, float volume, float pitch) {
		List list4 = null;
		Map map5 = this.sounds;
		synchronized(this.sounds) {
			list4 = (List)this.sounds.get(soundName);
		}

		if(list4 == null) {
			return null;
		} else {
			SoundData soundData7 = (SoundData)list4.get(this.random.nextInt(list4.size()));
			return new SoundInfo(soundData7, pitch, volume);
		}
	}

	public void registerSound(File soundFile, String soundName) {
		try {
			for(soundName = soundName.substring(0, soundName.length() - 4).replaceAll("/", "."); Character.isDigit(soundName.charAt(soundName.length() - 1)); soundName = soundName.substring(0, soundName.length() - 1)) {
			}

			SoundData soundFile1 = SoundReader.read(soundFile.toURI().toURL());
			Map map3 = this.sounds;
			synchronized(this.sounds) {
				Object object4;
				if((object4 = (List)this.sounds.get(soundName)) == null) {
					object4 = new ArrayList();
					this.sounds.put(soundName, object4);
				}

				((List)object4).add(soundFile1);
			}
		} catch (Exception exception6) {
			exception6.printStackTrace();
		}

	}

	public final void registerMusic(String musicName, File musicFile) {
		Map map3 = this.music;
		synchronized(this.music) {
			for(musicName = musicName.substring(0, musicName.length() - 4).replaceAll("/", "."); Character.isDigit(musicName.charAt(musicName.length() - 1)); musicName = musicName.substring(0, musicName.length() - 1)) {
			}

			Object object4;
			if((object4 = (List)this.music.get(musicName)) == null) {
				object4 = new ArrayList();
				this.music.put(musicName, object4);
			}

			((List)object4).add(musicFile);
		}
	}

	public boolean playMusic(SoundPlayer soundPlayer, String musicName) {
		List list3 = null;
		Map map4 = this.music;
		synchronized(this.music) {
			list3 = (List)this.music.get(musicName);
		}

		if(list3 == null) {
			return false;
		} else {
			File file8 = (File)list3.get(this.random.nextInt(list3.size()));

			try {
				soundPlayer.play(new Music(soundPlayer, file8.toURI().toURL()));
			} catch (MalformedURLException malformedURLException5) {
				malformedURLException5.printStackTrace();
			} catch (IOException iOException6) {
				iOException6.printStackTrace();
			}

			return true;
		}
	}
}