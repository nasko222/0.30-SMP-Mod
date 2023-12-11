package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public final class Font {
	private int[] charWidths = new int[256];
	private int fontTexture = 0;
	private Options options;

	public Font(Options options, String name, Textures textures) {
		this.options = options;

		BufferedImage bufferedImage14;
		try {
			bufferedImage14 = ImageIO.read(Textures.class.getResourceAsStream(name));
		} catch (IOException iOException13) {
			throw new RuntimeException(iOException13);
		}

		int i4 = bufferedImage14.getWidth();
		int i5 = bufferedImage14.getHeight();
		int[] i6 = new int[i4 * i5];
		bufferedImage14.getRGB(0, 0, i4, i5, i6, 0, i4);

		for(int i15 = 0; i15 < 128; ++i15) {
			i5 = i15 % 16;
			int i7 = i15 / 16;
			int i8 = 0;

			for(boolean z9 = false; i8 < 8 && !z9; ++i8) {
				int i10 = (i5 << 3) + i8;
				z9 = true;

				for(int i11 = 0; i11 < 8 && z9; ++i11) {
					int i12 = ((i7 << 3) + i11) * i4;
					if((i6[i10 + i12] & 255) > 128) {
						z9 = false;
					}
				}
			}

			if(i15 == 32) {
				i8 = 4;
			}

			this.charWidths[i15] = i8;
		}

		this.fontTexture = textures.loadTexture(name);
	}

	public final void drawShadow(String str, int x, int y, int color) {
		this.draw(str, x + 1, y + 1, color, true);
		this.draw(str, x, y, color);
	}

	public final void draw(String str, int x, int y, int color) {
		this.draw(str, x, y, color, false);
	}

	private void draw(String str, int x, int y, int color, boolean darken) {
		if(str != null) {
			char[] c12 = str.toCharArray();
			if(darken) {
				color = (color & 16579836) >> 2;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTexture);
			Tesselator tesselator6 = Tesselator.instance;
			Tesselator.instance.begin();
			tesselator6.color(color);
			int i7 = 0;

			for(int i8 = 0; i8 < c12.length; ++i8) {
				int i9;
				if(c12[i8] == 38 && c12.length > i8 + 1) {
					if((color = "0123456789abcdef".indexOf(c12[i8 + 1])) < 0) {
						color = 15;
					}

					i9 = (color & 8) << 3;
					int i10 = (color & 1) * 191 + i9;
					int i11 = ((color & 2) >> 1) * 191 + i9;
					color = ((color & 4) >> 2) * 191 + i9;
					if(this.options.anaglyph3d) {
						i9 = (color * 30 + i11 * 59 + i10 * 11) / 100;
						i11 = (color * 30 + i11 * 70) / 100;
						i10 = (color * 30 + i10 * 70) / 100;
						color = i9;
						i11 = i11;
						i10 = i10;
					}

					color = color << 16 | i11 << 8 | i10;
					i8 += 2;
					if(darken) {
						color = (color & 16579836) >> 2;
					}

					tesselator6.color(color);
				}

				color = c12[i8] % 16 << 3;
				i9 = c12[i8] / 16 << 3;
				float f13 = 7.99F;
				tesselator6.vertexUV((float)(x + i7), (float)y + f13, 0.0F, (float)color / 128.0F, ((float)i9 + f13) / 128.0F);
				tesselator6.vertexUV((float)(x + i7) + f13, (float)y + f13, 0.0F, ((float)color + f13) / 128.0F, ((float)i9 + f13) / 128.0F);
				tesselator6.vertexUV((float)(x + i7) + f13, (float)y, 0.0F, ((float)color + f13) / 128.0F, (float)i9 / 128.0F);
				tesselator6.vertexUV((float)(x + i7), (float)y, 0.0F, (float)color / 128.0F, (float)i9 / 128.0F);
				i7 += this.charWidths[c12[i8]];
			}

			tesselator6.end();
		}
	}

	public final int width(String str) {
		if(str == null) {
			return 0;
		} else {
			char[] c4 = str.toCharArray();
			int i2 = 0;

			for(int i3 = 0; i3 < c4.length; ++i3) {
				if(c4[i3] == 38) {
					++i3;
				} else {
					i2 += this.charWidths[c4[i3]];
				}
			}

			return i2;
		}
	}

	public static String removeColorCodes(String str) {
		char[] c3 = str.toCharArray();
		String string1 = "";

		for(int i2 = 0; i2 < c3.length; ++i2) {
			if(c3[i2] == 38) {
				++i2;
			} else {
				string1 = string1 + c3[i2];
			}
		}

		return string1;
	}
}