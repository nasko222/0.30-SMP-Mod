package com.mojang.minecraft.item;

import com.mojang.minecraft.model.Cube;
import com.mojang.minecraft.model.Polygon;
import com.mojang.minecraft.model.Vertex;

public final class ItemModel {
	private Cube cube = new Cube(0, 0);

	public ItemModel(int tex) {
		Cube cube10000 = this.cube;
		int i4 = tex;
		boolean z14 = true;
		z14 = true;
		z14 = true;
		float f3 = -2.0F;
		float f2 = -2.0F;
		float tex1 = -2.0F;
		Cube cube16 = cube10000;
		cube10000.vertices = new Vertex[8];
		cube16.polygons = new Polygon[6];
		Vertex vertex5 = new Vertex(tex1, f2, f3, 0.0F, 0.0F);
		Vertex vertex6 = new Vertex(2.0F, f2, f3, 0.0F, 8.0F);
		Vertex vertex7 = new Vertex(2.0F, 2.0F, f3, 8.0F, 8.0F);
		Vertex vertex19 = new Vertex(tex1, 2.0F, f3, 8.0F, 0.0F);
		Vertex vertex8 = new Vertex(tex1, f2, 2.0F, 0.0F, 0.0F);
		Vertex vertex18 = new Vertex(2.0F, f2, 2.0F, 0.0F, 8.0F);
		Vertex vertex9 = new Vertex(2.0F, 2.0F, 2.0F, 8.0F, 8.0F);
		Vertex tex2 = new Vertex(tex1, 2.0F, 2.0F, 8.0F, 0.0F);
		cube16.vertices[0] = vertex5;
		cube16.vertices[1] = vertex6;
		cube16.vertices[2] = vertex7;
		cube16.vertices[3] = vertex19;
		cube16.vertices[4] = vertex8;
		cube16.vertices[5] = vertex18;
		cube16.vertices[6] = vertex9;
		cube16.vertices[7] = tex2;
		float f10 = 0.25F;
		float f11 = 0.25F;
		float f12 = ((float)(i4 % 16) + (1.0F - f10)) / 16.0F;
		float f13 = ((float)(i4 / 16) + (1.0F - f11)) / 16.0F;
		f10 = ((float)(i4 % 16) + f10) / 16.0F;
		float f20 = ((float)(i4 / 16) + f11) / 16.0F;
		cube16.polygons[0] = new Polygon(new Vertex[]{vertex18, vertex6, vertex7, vertex9}, f12, f13, f10, f20);
		cube16.polygons[1] = new Polygon(new Vertex[]{vertex5, vertex8, tex2, vertex19}, f12, f13, f10, f20);
		cube16.polygons[2] = new Polygon(new Vertex[]{vertex18, vertex8, vertex5, vertex6}, f12, f13, f10, f20);
		cube16.polygons[3] = new Polygon(new Vertex[]{vertex7, vertex19, tex2, vertex9}, f12, f13, f10, f20);
		cube16.polygons[4] = new Polygon(new Vertex[]{vertex6, vertex5, vertex19, vertex7}, f12, f13, f10, f20);
		cube16.polygons[5] = new Polygon(new Vertex[]{vertex8, vertex18, vertex9, tex2}, f12, f13, f10, f20);
	}

	public final void render() {
		this.cube.render(0.0625F);
	}
}