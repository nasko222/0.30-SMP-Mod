package com.mojang.minecraft.model;

public final class Polygon {
	public Vertex[] vertices;

	private Polygon(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public Polygon(Vertex[] vertices, int u0, int v0, int u1, int v1) {
		this(vertices);
		float f7 = 0.0015625F;
		float f6 = 0.003125F;
		vertices[0] = vertices[0].remap((float)u1 / 64.0F - f7, (float)v0 / 32.0F + f6);
		vertices[1] = vertices[1].remap((float)u0 / 64.0F + f7, (float)v0 / 32.0F + f6);
		vertices[2] = vertices[2].remap((float)u0 / 64.0F + f7, (float)v1 / 32.0F - f6);
		vertices[3] = vertices[3].remap((float)u1 / 64.0F - f7, (float)v1 / 32.0F - f6);
	}

	public Polygon(Vertex[] vertices, float u0, float v0, float u1, float v1) {
		this(vertices);
		vertices[0] = vertices[0].remap(u1, v0);
		vertices[1] = vertices[1].remap(u0, v0);
		vertices[2] = vertices[2].remap(u0, v1);
		vertices[3] = vertices[3].remap(u1, v1);
	}
}