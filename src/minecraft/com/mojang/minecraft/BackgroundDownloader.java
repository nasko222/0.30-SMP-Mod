package com.mojang.minecraft;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public final class BackgroundDownloader extends Thread {
	private File resourcesFolder;
	private Minecraft minecraft;
	boolean closing = false;

	public BackgroundDownloader(File savedFile, Minecraft minecraft) {
		this.minecraft = minecraft;
		this.setName("Resource download thread");
		this.setDaemon(true);
		this.resourcesFolder = new File(savedFile, "resources/");
		if(!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
		}
	}

	public final void run() {
		// $FF: Couldn't be decompiled
	}

	private void downloadResource(URL url, File savedFile) throws IOException {
		byte[] b3 = new byte[4096];
		DataInputStream url1 = new DataInputStream(url.openStream());
		DataOutputStream savedFile1 = new DataOutputStream(new FileOutputStream(savedFile));
		boolean z4 = false;

		do {
			int i7;
			if((i7 = url1.read(b3)) < 0) {
				url1.close();
				savedFile1.close();
				return;
			}

			savedFile1.write(b3, 0, i7);
		} while(!this.closing);

	}
}