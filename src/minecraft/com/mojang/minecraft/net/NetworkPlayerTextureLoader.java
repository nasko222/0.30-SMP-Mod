package com.mojang.minecraft.net;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * this is probably some inner thread that didnt even have a name to begin with
 */
final class NetworkPlayerTextureLoader extends Thread {
	private NetworkPlayer player;

	NetworkPlayerTextureLoader(NetworkPlayer player) {
		this.player = player;
	}

	public final void run() {
		HttpURLConnection httpURLConnection1 = null;

		try {
			(httpURLConnection1 = (HttpURLConnection)(new URL("http://www.minecraft.net/skin/" + this.player.name + ".png")).openConnection()).setDoInput(true);
			httpURLConnection1.setDoOutput(false);
			httpURLConnection1.connect();
			if(httpURLConnection1.getResponseCode() != 404) {
				this.player.newTexture = ImageIO.read(httpURLConnection1.getInputStream());
				return;
			}

			return;
		} catch (Exception exception5) {
			exception5.printStackTrace();
		} finally {
			httpURLConnection1.disconnect();
		}

	}
}