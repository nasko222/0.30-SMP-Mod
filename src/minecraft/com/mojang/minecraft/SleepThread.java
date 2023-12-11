package com.mojang.minecraft;

/**
 * probably some inner thread that didnt have a name to begin with
 */
final class SleepThread extends Thread {
	SleepThread(Minecraft minecraft) {
		this.setDaemon(true);
		this.start();
	}

	public final void run() {
		while(true) {
			try {
				Thread.sleep(2147483647L);
			} catch (InterruptedException interruptedException1) {
			}
		}
	}
}