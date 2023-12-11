package com.mojang.minecraft.model;

public final class Vertex {
	public Vec3 pos;
	public float u;
	public float v;

	public Vertex(float x, float y, float z, float u, float v) {
		this(new Vec3(x, y, z), u, v);
	}

	public final Vertex remap(float u, float v) {
		return new Vertex(this, u, v);
	}

	private Vertex(Vertex vertex, float u, float v) {
		this.pos = vertex.pos;
		this.u = u;
		this.v = v;
	}

	private Vertex(Vec3 pos, float u, float v) {
		this.pos = pos;
		this.u = u;
		this.v = v;
	}
}