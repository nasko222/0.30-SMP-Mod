package com.mojang.minecraft.model;

import org.lwjgl.opengl.GL11;

public final class Cube {
	public Vertex[] vertices;
	public Polygon[] polygons;
	private int xTexOffs;
	private int yTexOffs;
	public float x;
	public float y;
	public float z;
	public float xRot;
	public float yRot;
	public float zRot;
	public boolean compiled = false;
	public int list = 0;
	public boolean mirror = false;
	public boolean showModel = true;
	private boolean isHidden = false;

	public Cube(int xTexOffs, int yTexOffs) {
		this.xTexOffs = xTexOffs;
		this.yTexOffs = yTexOffs;
	}

	public final void addBox(float x0, float y0, float z0, int w, int h, int d, float translation) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
		float f8 = x0 + (float)w;
		float f9 = y0 + (float)h;
		float f10 = z0 + (float)d;
		x0 -= translation;
		y0 -= translation;
		z0 -= translation;
		f8 += translation;
		f9 += translation;
		f10 += translation;
		if(this.mirror) {
			translation = f8;
			f8 = x0;
			x0 = translation;
		}

		Vertex vertex20 = new Vertex(x0, y0, z0, 0.0F, 0.0F);
		Vertex vertex11 = new Vertex(f8, y0, z0, 0.0F, 8.0F);
		Vertex vertex12 = new Vertex(f8, f9, z0, 8.0F, 8.0F);
		Vertex vertex18 = new Vertex(x0, f9, z0, 8.0F, 0.0F);
		Vertex vertex13 = new Vertex(x0, y0, f10, 0.0F, 0.0F);
		Vertex vertex15 = new Vertex(f8, y0, f10, 0.0F, 8.0F);
		Vertex vertex21 = new Vertex(f8, f9, f10, 8.0F, 8.0F);
		Vertex vertex14 = new Vertex(x0, f9, f10, 8.0F, 0.0F);
		this.vertices[0] = vertex20;
		this.vertices[1] = vertex11;
		this.vertices[2] = vertex12;
		this.vertices[3] = vertex18;
		this.vertices[4] = vertex13;
		this.vertices[5] = vertex15;
		this.vertices[6] = vertex21;
		this.vertices[7] = vertex14;
		this.polygons[0] = new Polygon(new Vertex[]{vertex15, vertex11, vertex12, vertex21}, this.xTexOffs + d + w, this.yTexOffs + d, this.xTexOffs + d + w + d, this.yTexOffs + d + h);
		this.polygons[1] = new Polygon(new Vertex[]{vertex20, vertex13, vertex14, vertex18}, this.xTexOffs, this.yTexOffs + d, this.xTexOffs + d, this.yTexOffs + d + h);
		this.polygons[2] = new Polygon(new Vertex[]{vertex15, vertex13, vertex20, vertex11}, this.xTexOffs + d, this.yTexOffs, this.xTexOffs + d + w, this.yTexOffs + d);
		this.polygons[3] = new Polygon(new Vertex[]{vertex12, vertex18, vertex14, vertex21}, this.xTexOffs + d + w, this.yTexOffs, this.xTexOffs + d + w + w, this.yTexOffs + d);
		this.polygons[4] = new Polygon(new Vertex[]{vertex11, vertex20, vertex18, vertex12}, this.xTexOffs + d, this.yTexOffs + d, this.xTexOffs + d + w, this.yTexOffs + d + h);
		this.polygons[5] = new Polygon(new Vertex[]{vertex13, vertex15, vertex21, vertex14}, this.xTexOffs + d + w + d, this.yTexOffs + d, this.xTexOffs + d + w + d + w, this.yTexOffs + d + h);
		if(this.mirror) {
			for(int i16 = 0; i16 < this.polygons.length; ++i16) {
				Polygon polygon17;
				Vertex[] vertex19 = new Vertex[(polygon17 = this.polygons[i16]).vertices.length];

				for(w = 0; w < polygon17.vertices.length; ++w) {
					vertex19[w] = polygon17.vertices[polygon17.vertices.length - w - 1];
				}

				polygon17.vertices = vertex19;
			}
		}

	}

	public final void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final void render(float translation) {
		if(this.showModel) {
			if(!this.compiled) {
				this.translateTo(translation);
			}

			if(this.xRot == 0.0F && this.yRot == 0.0F && this.zRot == 0.0F) {
				if(this.x == 0.0F && this.y == 0.0F && this.z == 0.0F) {
					GL11.glCallList(this.list);
				} else {
					GL11.glTranslatef(this.x * translation, this.y * translation, this.z * translation);
					GL11.glCallList(this.list);
					GL11.glTranslatef(-this.x * translation, -this.y * translation, -this.z * translation);
				}
			} else {
				GL11.glPushMatrix();
				GL11.glTranslatef(this.x * translation, this.y * translation, this.z * translation);
				if(this.zRot != 0.0F) {
					GL11.glRotatef(this.zRot * 57.295776F, 0.0F, 0.0F, 1.0F);
				}

				if(this.yRot != 0.0F) {
					GL11.glRotatef(this.yRot * 57.295776F, 0.0F, 1.0F, 0.0F);
				}

				if(this.xRot != 0.0F) {
					GL11.glRotatef(this.xRot * 57.295776F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glCallList(this.list);
				GL11.glPopMatrix();
			}
		}
	}

	public void translateTo(float translation) {
		this.list = GL11.glGenLists(1);
		GL11.glNewList(this.list, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);

		for(int i2 = 0; i2 < this.polygons.length; ++i2) {
			Polygon polygon10000 = this.polygons[i2];
			float f4 = translation;
			Polygon polygon3 = polygon10000;
			Vec3 vec35 = polygon10000.vertices[1].pos.subtract(polygon3.vertices[0].pos).normalize();
			Vec3 vec36 = polygon3.vertices[1].pos.subtract(polygon3.vertices[2].pos).normalize();
			GL11.glNormal3f((vec35 = (new Vec3(vec35.y * vec36.z - vec35.z * vec36.y, vec35.z * vec36.x - vec35.x * vec36.z, vec35.x * vec36.y - vec35.y * vec36.x)).normalize()).x, vec35.y, vec35.z);

			for(int i7 = 0; i7 < 4; ++i7) {
				Vertex vertex8;
				GL11.glTexCoord2f((vertex8 = polygon3.vertices[i7]).u, vertex8.v);
				GL11.glVertex3f(vertex8.pos.x * f4, vertex8.pos.y * f4, vertex8.pos.z * f4);
			}
		}

		GL11.glEnd();
		GL11.glEndList();
		this.compiled = true;
	}
}