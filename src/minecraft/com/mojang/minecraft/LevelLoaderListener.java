package com.mojang.minecraft;

import com.mojang.minecraft.renderer.Tesselator;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class LevelLoaderListener {
	private String title = "";
	private Minecraft minecraft;
	private String text = "";
	private long time = System.currentTimeMillis();

	public LevelLoaderListener(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	public final void beginLevelLoading(String text) {
		if(!this.minecraft.running) {
			throw new StopGameException();
		} else {
			this.text = text;
			int text1 = this.minecraft.width * 240 / this.minecraft.height;
			int i2 = this.minecraft.height * 240 / this.minecraft.height;
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, (double)text1, (double)i2, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	public final void levelLoadUpdate(String text) {
		if(!this.minecraft.running) {
			throw new StopGameException();
		} else {
			this.title = text;
			this.setLoadingProgress(-1);
		}
	}

	public final void setLoadingProgress(int prog) {
		if(!this.minecraft.running) {
			throw new StopGameException();
		} else {
			long j2;
			if((j2 = System.currentTimeMillis()) - this.time < 0L || j2 - this.time >= 20L) {
				this.time = j2;
				int i8 = this.minecraft.width * 240 / this.minecraft.height;
				int i3 = this.minecraft.height * 240 / this.minecraft.height;
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				Tesselator tesselator4 = Tesselator.instance;
				int i5 = this.minecraft.textures.loadTexture("/dirt.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
				float f9 = 32.0F;
				tesselator4.begin();
				tesselator4.color(4210752);
				tesselator4.vertexUV(0.0F, (float)i3, 0.0F, 0.0F, (float)i3 / f9);
				tesselator4.vertexUV((float)i8, (float)i3, 0.0F, (float)i8 / f9, (float)i3 / f9);
				tesselator4.vertexUV((float)i8, 0.0F, 0.0F, (float)i8 / f9, 0.0F);
				tesselator4.vertexUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				tesselator4.end();
				if(prog >= 0) {
					i5 = i8 / 2 - 50;
					int i6 = i3 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tesselator4.begin();
					tesselator4.color(8421504);
					tesselator4.vertex((float)i5, (float)i6, 0.0F);
					tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
					tesselator4.vertex((float)(i5 + 100), (float)(i6 + 2), 0.0F);
					tesselator4.vertex((float)(i5 + 100), (float)i6, 0.0F);
					tesselator4.color(8454016);
					tesselator4.vertex((float)i5, (float)i6, 0.0F);
					tesselator4.vertex((float)i5, (float)(i6 + 2), 0.0F);
					tesselator4.vertex((float)(i5 + prog), (float)(i6 + 2), 0.0F);
					tesselator4.vertex((float)(i5 + prog), (float)i6, 0.0F);
					tesselator4.end();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.minecraft.font.drawShadow(this.text, (i8 - this.minecraft.font.width(this.text)) / 2, i3 / 2 - 4 - 16, 0xFFFFFF);
				this.minecraft.font.drawShadow(this.title, (i8 - this.minecraft.font.width(this.title)) / 2, i3 / 2 - 4 + 8, 0xFFFFFF);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception exception7) {
				}
			}
		}
	}
}