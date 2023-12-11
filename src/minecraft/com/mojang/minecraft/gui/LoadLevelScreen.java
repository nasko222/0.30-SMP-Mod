package com.mojang.minecraft.gui;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class LoadLevelScreen extends Screen implements Runnable {
	protected Screen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";
	boolean fileLoaded = false;
	JFileChooser fileChooser;
	protected boolean isSaveScreen = false;
	protected File levelFile;

	public LoadLevelScreen(Screen screen) {
		this.parent = screen;
	}

	public void run() {
		try {
			if(this.fileLoaded) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException interruptedException2) {
					interruptedException2.printStackTrace();
				}
			}

			this.status = "Getting level list..";
			URL uRL1 = new URL("http://" + this.minecraft.host + "/listmaps.jsp?user=" + this.minecraft.user.name);
			BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(uRL1.openConnection().getInputStream()));
			this.levels = bufferedReader4.readLine().split(";");
			if(this.levels.length >= 5) {
				this.setLevels(this.levels);
				this.loaded = true;
				return;
			}

			this.status = this.levels[0];
			this.finished = true;
		} catch (Exception exception3) {
			exception3.printStackTrace();
			this.status = "Failed to load levels";
			this.finished = true;
		}

	}

	protected void setLevels(String[] levels) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((Button)this.buttons.get(i2)).enabled = !levels[i2].equals("-");
			((Button)this.buttons.get(i2)).msg = levels[i2];
			((Button)this.buttons.get(i2)).visible = true;
		}

	}

	public void init() {
		(new Thread(this)).start();

		for(int i1 = 0; i1 < 5; ++i1) {
			this.buttons.add(new Button(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, "---"));
			((Button)this.buttons.get(i1)).visible = false;
			((Button)this.buttons.get(i1)).enabled = false;
		}

		this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
		this.buttons.add(new Button(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
	}

	protected final void buttonClicked(Button button) {
		if(!this.fileLoaded) {
			if(button.enabled) {
				if(this.loaded && button.id < 5) {
					this.loadLevel(button.id);
				}

				if(this.finished || this.loaded && button.id == 5) {
					this.fileLoaded = true;
					LoadLevelScreenThread loadLevelScreenThread2;
					(loadLevelScreenThread2 = new LoadLevelScreenThread(this)).setDaemon(true);
					SwingUtilities.invokeLater(loadLevelScreenThread2);
				}

				if(this.finished || this.loaded && button.id == 6) {
					this.minecraft.setScreen(this.parent);
				}

			}
		}
	}

	protected void loadLevel(File file) {
		File file2 = file;
		Minecraft file1 = this.minecraft;
		boolean z10000;
		Level level5;
		if((level5 = this.minecraft.levelIo.load(file2)) == null) {
			z10000 = false;
		} else {
			file1.loadLegacy(level5);
			z10000 = true;
		}

		this.minecraft.setScreen(this.parent);
	}

	protected void loadLevel(int id) {
		this.minecraft.loadLevel(this.minecraft.user.name, id);
		this.minecraft.setScreen((Screen)null);
		this.minecraft.grabMouse();
	}

	public void render(int xMouse, int yMouse) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
		if(this.fileLoaded) {
			drawCenteredString(this.font, "Selecting file..", this.width / 2, this.height / 2 - 4, 0xFFFFFF);

			try {
				Thread.sleep(20L);
			} catch (InterruptedException interruptedException3) {
				interruptedException3.printStackTrace();
			}
		} else {
			if(!this.loaded) {
				drawCenteredString(this.font, this.status, this.width / 2, this.height / 2 - 4, 0xFFFFFF);
			}

			super.render(xMouse, yMouse);
		}
	}

	public final void removed() {
		super.removed();
		if(this.fileChooser != null) {
			this.fileChooser.cancelSelection();
		}

	}

	public final void tick() {
		super.tick();
		if(this.levelFile != null) {
			this.loadLevel(this.levelFile);
			this.levelFile = null;
		}

	}
}