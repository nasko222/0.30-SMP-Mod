package com.mojang.minecraft.gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * probably didnt even have a name cause most likely inner class thread
 */
final class LoadLevelScreenThread extends Thread {
	private LoadLevelScreen screen;

	LoadLevelScreenThread(LoadLevelScreen screen) {
		this.screen = screen;
	}

	public final void run() {
		LoadLevelScreen loadLevelScreen2;
		JFileChooser jFileChooser3;
		try {
			LoadLevelScreen loadLevelScreen10000 = this.screen;
			jFileChooser3 = new JFileChooser();
			loadLevelScreen10000.fileChooser = jFileChooser3;
			FileNameExtensionFilter fileNameExtensionFilter1 = new FileNameExtensionFilter("Minecraft levels", new String[]{"mine"});
			loadLevelScreen2 = this.screen;
			this.screen.fileChooser.setFileFilter(fileNameExtensionFilter1);
			loadLevelScreen2 = this.screen;
			this.screen.fileChooser.setMultiSelectionEnabled(false);
			int i7;
			if(this.screen.isSaveScreen) {
				loadLevelScreen2 = this.screen;
				i7 = this.screen.fileChooser.showSaveDialog(this.screen.minecraft.parent);
			} else {
				loadLevelScreen2 = this.screen;
				i7 = this.screen.fileChooser.showOpenDialog(this.screen.minecraft.parent);
			}

			if(i7 == 0) {
				(loadLevelScreen2 = this.screen).levelFile = this.screen.fileChooser.getSelectedFile();
			}
		} finally {
			boolean z6 = false;
			loadLevelScreen2 = this.screen;
			this.screen.fileLoaded = false;
			jFileChooser3 = null;
			loadLevelScreen2 = this.screen;
			this.screen.fileChooser = jFileChooser3;
		}

	}
}