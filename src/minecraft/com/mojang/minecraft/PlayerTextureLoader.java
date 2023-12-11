package com.mojang.minecraft;

import com.mojang.minecraft.player.Player;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * probably some inner thread that didnt have a name to begin with
 */
final class PlayerTextureLoader extends Thread {
	private Minecraft minecraft;

	PlayerTextureLoader(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	public final void run() {
		if(this.minecraft.user != null) {
			HttpURLConnection httpURLConnection1 = null;

			try {
				(httpURLConnection1 = (HttpURLConnection)(new URL("http://www.minecraft.net/skin/" + this.minecraft.user.name + ".png")).openConnection()).setDoInput(true);
				httpURLConnection1.setDoOutput(false);
				httpURLConnection1.connect();
				if(httpURLConnection1.getResponseCode() != 404) {
					Player.newTexture = ImageIO.read(httpURLConnection1.getInputStream());
					return;
				}
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return;
			} finally {
				httpURLConnection1.disconnect();
			}

		}
	}
}