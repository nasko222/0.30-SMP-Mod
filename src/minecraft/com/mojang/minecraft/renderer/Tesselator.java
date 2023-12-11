package com.mojang.minecraft.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class Tesselator {
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(524288);
	private float[] array = new float[524288];
	private int vertices = 0;
	private float u;
	private float v;
	/**
	 * enigma wouldnt let me rename these so have an underscore
	 * <p>thank you for your time.
	 */
	private float _r;
	private float _g;
	private float _b;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int len = 3;
	private int p = 0;
	private boolean noColor = false;
	public static Tesselator instance = new Tesselator();

	public final void end() {
		if(this.vertices > 0) {
			this.buffer.clear();
			this.buffer.put(this.array, 0, this.p);
			this.buffer.flip();
			if(this.hasTexture && this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_T2F_C3F_V3F, 0, this.buffer);
			} else if(this.hasTexture) {
				GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, this.buffer);
			} else if(this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_C3F_V3F, 0, this.buffer);
			} else {
				GL11.glInterleavedArrays(GL11.GL_V3F, 0, this.buffer);
			}

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			}

			GL11.glDrawArrays(GL11.GL_QUADS, GL11.GL_POINTS, this.vertices);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}
		}

		this.clear();
	}

	private void clear() {
		this.vertices = 0;
		this.buffer.clear();
		this.p = 0;
	}

	public final void begin() {
		this.clear();
		this.hasColor = false;
		this.hasTexture = false;
		this.noColor = false;
	}

	public final void color(float r, float g, float b) {
		if(!this.noColor) {
			if(!this.hasColor) {
				this.len += 3;
			}

			this.hasColor = true;
			this._r = r;
			this._g = g;
			this._b = b;
		}
	}

	public final void vertexUV(float x, float y, float z, float u, float v) {
		if(!this.hasTexture) {
			this.len += 2;
		}

		this.hasTexture = true;
		this.u = u;
		this.v = v;
		this.vertex(x, y, z);
	}

	public final void vertex(float x, float y, float z) {
		if(this.hasTexture) {
			this.array[this.p++] = this.u;
			this.array[this.p++] = this.v;
		}

		if(this.hasColor) {
			this.array[this.p++] = this._r;
			this.array[this.p++] = this._g;
			this.array[this.p++] = this._b;
		}

		this.array[this.p++] = x;
		this.array[this.p++] = y;
		this.array[this.p++] = z;
		++this.vertices;
		if(this.vertices % 4 == 0 && this.p >= 524288 - (this.len << 2)) {
			this.end();
		}

	}

	public final void color(int color) {
		int i2 = color >> 16 & 255;
		int i3 = color >> 8 & 255;
		color &= 255;
		int i10001 = i2;
		int i10002 = i3;
		i3 = color;
		i2 = i10002;
		color = i10001;
		byte b7 = (byte)color;
		byte b8 = (byte)i2;
		byte b6 = (byte)i3;
		byte b5 = b8;
		byte color1 = b7;
		if(!this.noColor) {
			if(!this.hasColor) {
				this.len += 3;
			}

			this.hasColor = true;
			this._r = (float)(color1 & 255) / 255.0F;
			this._g = (float)(b5 & 255) / 255.0F;
			this._b = (float)(b6 & 255) / 255.0F;
		}

	}

	public final void noColor() {
		this.noColor = true;
	}

	public final void normal(float x, float y, float z) {
		GL11.glNormal3f(x, y, z);
	}
}