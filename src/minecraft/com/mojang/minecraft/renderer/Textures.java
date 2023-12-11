package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Options;
import com.mojang.minecraft.renderer.texture.DynamicTexture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Textures {
	public HashMap idMap = new HashMap();
	public HashMap pixelsMap = new HashMap();
	public IntBuffer ib = BufferUtils.createIntBuffer(1);
	public ByteBuffer pixels = BufferUtils.createByteBuffer(262144);
	public List textureList = new ArrayList();
	public Options options;

	public Textures(Options options) {
		this.options = options;
	}

	public final int loadTexture(String resourceName) {
		Integer integer2;
		if((integer2 = (Integer)this.idMap.get(resourceName)) != null) {
			return integer2.intValue();
		} else {
			try {
				this.ib.clear();
				GL11.glGenTextures(this.ib);
				int i4 = this.ib.get(0);
				if(resourceName.startsWith("##")) {
					this.addTexture(addTexture(ImageIO.read(Textures.class.getResourceAsStream(resourceName.substring(2)))), i4);
				} else {
					this.addTexture(ImageIO.read(Textures.class.getResourceAsStream(resourceName)), i4);
				}

				this.idMap.put(resourceName, i4);
				return i4;
			} catch (IOException iOException3) {
				throw new RuntimeException("!!");
			}
		}
	}

	public static BufferedImage addTexture(BufferedImage bufferedImage) {
		int i1 = bufferedImage.getWidth() / 16;
		BufferedImage bufferedImage2;
		Graphics graphics3 = (bufferedImage2 = new BufferedImage(16, bufferedImage.getHeight() * i1, 2)).getGraphics();

		for(int i4 = 0; i4 < i1; ++i4) {
			graphics3.drawImage(bufferedImage, -i4 << 4, i4 * bufferedImage.getHeight(), (ImageObserver)null);
		}

		graphics3.dispose();
		return bufferedImage2;
	}

	public final int loadTexture(BufferedImage bufferedImage) {
		this.ib.clear();
		GL11.glGenTextures(this.ib);
		int i2 = this.ib.get(0);
		this.addTexture(bufferedImage, i2);
		this.pixelsMap.put(i2, bufferedImage);
		return i2;
	}

	public void addTexture(BufferedImage bufferedImage, int textureId) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		textureId = bufferedImage.getWidth();
		int i3 = bufferedImage.getHeight();
		int[] i4 = new int[textureId * i3];
		byte[] b5 = new byte[textureId * i3 << 2];
		bufferedImage.getRGB(0, 0, textureId, i3, i4, 0, textureId);

		for(int i11 = 0; i11 < i4.length; ++i11) {
			int i6 = i4[i11] >>> 24;
			int i7 = i4[i11] >> 16 & 255;
			int i8 = i4[i11] >> 8 & 255;
			int i9 = i4[i11] & 255;
			if(this.options.anaglyph3d) {
				int i10 = (i7 * 30 + i8 * 59 + i9 * 11) / 100;
				i8 = (i7 * 30 + i8 * 70) / 100;
				i9 = (i7 * 30 + i9 * 70) / 100;
				i7 = i10;
				i8 = i8;
				i9 = i9;
			}

			b5[i11 << 2] = (byte)i7;
			b5[(i11 << 2) + 1] = (byte)i8;
			b5[(i11 << 2) + 2] = (byte)i9;
			b5[(i11 << 2) + 3] = (byte)i6;
		}

		this.pixels.clear();
		this.pixels.put(b5);
		this.pixels.position(0).limit(b5.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureId, i3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.pixels);
	}

	public final void addDynamicTexture(DynamicTexture dynamicTexture) {
		this.textureList.add(dynamicTexture);
		dynamicTexture.tick();
	}
}