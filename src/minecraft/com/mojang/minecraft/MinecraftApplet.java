package com.mojang.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private Canvas canvas;
	private Minecraft minecraft;
	private Thread thread = null;

	public void init() {
		this.canvas = new MinecraftApplet$1(this);
		boolean z1 = false;
		if(this.getParameter("fullscreen") != null) {
			z1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
		}

		this.minecraft = new Minecraft(this.canvas, this, this.getWidth(), this.getHeight(), z1);
		this.minecraft.host = this.getDocumentBase().getHost();
		if(this.getDocumentBase().getPort() > 0) {
			this.minecraft.host = this.minecraft.host + ":" + this.getDocumentBase().getPort();
		}

		if(this.getParameter("username") != null && this.getParameter("sessionid") != null) {
			this.minecraft.user = new User(this.getParameter("username"), this.getParameter("sessionid"));
			if(this.getParameter("mppass") != null) {
				this.minecraft.user.mpPass = this.getParameter("mppass");
			}

			this.minecraft.user.hasPaid = "true".equals(this.getParameter("haspaid"));
		}

		if(this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
			this.minecraft.loadMapUser = this.getParameter("loadmap_user");
			this.minecraft.loadMapId = Integer.parseInt(this.getParameter("loadmap_id"));
		} else if(this.getParameter("server") != null && this.getParameter("port") != null) {
			Minecraft minecraft10000 = this.minecraft;
			String string10001 = this.getParameter("server");
			int i3 = Integer.parseInt(this.getParameter("port"));
			String string2 = string10001;
			Minecraft minecraft4 = minecraft10000;
			minecraft10000.server = string2;
			minecraft4.port = i3;
		}

		this.minecraft.appletMode = true;
		this.setLayout(new BorderLayout());
		this.add(this.canvas, "Center");
		this.canvas.setFocusable(true);
		this.validate();
	}

	public void startGameThread() {
		if(this.thread == null) {
			this.thread = new Thread(this.minecraft);
			this.thread.start();
		}
	}

	public void start() {
		this.minecraft.pause = false;
	}

	public void stop() {
		this.minecraft.pause = true;
	}

	public void destroy() {
		this.stopGameThread();
	}

	public void stopGameThread() {
		if(this.thread != null) {
			Minecraft minecraft1 = this.minecraft;
			this.minecraft.running = false;

			try {
				this.thread.join(1000L);
			} catch (InterruptedException interruptedException3) {
				try {
					this.minecraft.destroy();
				} catch (Exception exception2) {
					exception2.printStackTrace();
				}
			}

			this.thread = null;
		}
	}
}