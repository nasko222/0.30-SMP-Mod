package com.mojang.minecraft;

import com.mojang.minecraft.renderer.Textures;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

public final class Options {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	public boolean music = true;
	public boolean sound = true;
	public boolean invertYMouse = false;
	public boolean showFramerate = false;
	public int viewDistance = 0;
	public boolean bobView = true;
	public boolean anaglyph3d = false;
	public boolean limitFramerate = false;
	public KeyMapping forward = new KeyMapping("Forward", 17);
	public KeyMapping left = new KeyMapping("Left", 30);
	public KeyMapping back = new KeyMapping("Back", 31);
	public KeyMapping right = new KeyMapping("Right", 32);
	public KeyMapping jump = new KeyMapping("Jump", 57);
	public KeyMapping build = new KeyMapping("Build", 48);
	public KeyMapping chat = new KeyMapping("Chat", 20);
	public KeyMapping toggleFog = new KeyMapping("Toggle fog", 33);
	public KeyMapping save = new KeyMapping("Save location", 28);
	public KeyMapping load = new KeyMapping("Load location", 19);
	public KeyMapping[] keys = new KeyMapping[]{this.forward, this.left, this.back, this.right, this.jump, this.build, this.chat, this.toggleFog, this.save, this.load};
	private Minecraft minecraft;
	private File optionsFile;
	public int optionCount = 8;

	public Options(Minecraft minecraft, File savedFile) {
		this.minecraft = minecraft;
		this.optionsFile = new File(savedFile, "options.txt");
		this.load();
	}

	public final String getKeyMessage(int key) {
		return this.keys[key].name + ": " + Keyboard.getKeyName(this.keys[key].key);
	}

	public final void setKey(int keyNum, int key) {
		this.keys[keyNum].key = key;
		this.save();
	}

	public final void setOption(int option, int tex) {
		if(option == 0) {
			this.music = !this.music;
		}

		if(option == 1) {
			this.sound = !this.sound;
		}

		if(option == 2) {
			this.invertYMouse = !this.invertYMouse;
		}

		if(option == 3) {
			this.showFramerate = !this.showFramerate;
		}

		if(option == 4) {
			this.viewDistance = this.viewDistance + tex & 3;
		}

		if(option == 5) {
			this.bobView = !this.bobView;
		}

		if(option == 6) {
			this.anaglyph3d = !this.anaglyph3d;
			Textures tex1 = this.minecraft.textures;
			Iterator iterator3 = this.minecraft.textures.pixelsMap.keySet().iterator();

			int i4;
			BufferedImage bufferedImage5;
			while(iterator3.hasNext()) {
				i4 = ((Integer)iterator3.next()).intValue();
				bufferedImage5 = (BufferedImage)tex1.pixelsMap.get(i4);
				tex1.addTexture(bufferedImage5, i4);
			}

			iterator3 = tex1.idMap.keySet().iterator();

			while(iterator3.hasNext()) {
				String string8 = (String)iterator3.next();

				try {
					if(string8.startsWith("##")) {
						bufferedImage5 = Textures.addTexture(ImageIO.read(Textures.class.getResourceAsStream(string8.substring(2))));
					} else {
						bufferedImage5 = ImageIO.read(Textures.class.getResourceAsStream(string8));
					}

					i4 = ((Integer)tex1.idMap.get(string8)).intValue();
					tex1.addTexture(bufferedImage5, i4);
				} catch (IOException iOException6) {
					iOException6.printStackTrace();
				}
			}
		}

		if(option == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		this.save();
	}

	public final String getMessage(int option) {
		return option == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (option == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (option == 2 ? "Invert mouse: " + (this.invertYMouse ? "ON" : "OFF") : (option == 3 ? "Show FPS: " + (this.showFramerate ? "ON" : "OFF") : (option == 4 ? "Render distance: " + RENDER_DISTANCES[this.viewDistance] : (option == 5 ? "View bobbing: " + (this.bobView ? "ON" : "OFF") : (option == 6 ? "3d anaglyph: " + (this.anaglyph3d ? "ON" : "OFF") : (option == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : "")))))));
	}

	private void load() {
		try {
			if(this.optionsFile.exists()) {
				BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));
				String string2 = null;

				while((string2 = bufferedReader1.readLine()) != null) {
					String[] string5;
					if((string5 = string2.split(":"))[0].equals("music")) {
						this.music = string5[1].equals("true");
					}

					if(string5[0].equals("sound")) {
						this.sound = string5[1].equals("true");
					}

					if(string5[0].equals("invertYMouse")) {
						this.invertYMouse = string5[1].equals("true");
					}

					if(string5[0].equals("showFrameRate")) {
						this.showFramerate = string5[1].equals("true");
					}

					if(string5[0].equals("viewDistance")) {
						this.viewDistance = Integer.parseInt(string5[1]);
					}

					if(string5[0].equals("bobView")) {
						this.bobView = string5[1].equals("true");
					}

					if(string5[0].equals("anaglyph3d")) {
						this.anaglyph3d = string5[1].equals("true");
					}

					if(string5[0].equals("limitFramerate")) {
						this.limitFramerate = string5[1].equals("true");
					}

					for(int i3 = 0; i3 < this.keys.length; ++i3) {
						if(string5[0].equals("key_" + this.keys[i3].name)) {
							this.keys[i3].key = Integer.parseInt(string5[1]);
						}
					}
				}

				bufferedReader1.close();
			}
		} catch (Exception exception4) {
			System.out.println("Failed to load options");
			exception4.printStackTrace();
		}
	}

	private void save() {
		try {
			PrintWriter printWriter1;
			(printWriter1 = new PrintWriter(new FileWriter(this.optionsFile))).println("music:" + this.music);
			printWriter1.println("sound:" + this.sound);
			printWriter1.println("invertYMouse:" + this.invertYMouse);
			printWriter1.println("showFrameRate:" + this.showFramerate);
			printWriter1.println("viewDistance:" + this.viewDistance);
			printWriter1.println("bobView:" + this.bobView);
			printWriter1.println("anaglyph3d:" + this.anaglyph3d);
			printWriter1.println("limitFramerate:" + this.limitFramerate);

			for(int i2 = 0; i2 < this.keys.length; ++i2) {
				printWriter1.println("key_" + this.keys[i2].name + ":" + this.keys[i2].key);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}
	}
}