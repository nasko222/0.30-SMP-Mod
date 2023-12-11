package com.mojang.minecraft.model;

import util.Mth;

public class QuadrupedModel extends BaseModel {
	public Cube head = new Cube(0, 0);
	public Cube body;
	public Cube leg1;
	public Cube leg2;
	public Cube leg3;
	public Cube leg4;

	public QuadrupedModel(int size, float scale) {
		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.head.setPos(0.0F, (float)(18 - size), -6.0F);
		this.body = new Cube(28, 8);
		this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, 0.0F);
		this.body.setPos(0.0F, (float)(17 - size), 2.0F);
		this.leg1 = new Cube(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
		this.leg1.setPos(-3.0F, (float)(24 - size), 7.0F);
		this.leg2 = new Cube(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
		this.leg2.setPos(3.0F, (float)(24 - size), 7.0F);
		this.leg3 = new Cube(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
		this.leg3.setPos(-3.0F, (float)(24 - size), -5.0F);
		this.leg4 = new Cube(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, size, 4, 0.0F);
		this.leg4.setPos(3.0F, (float)(24 - size), -5.0F);
	}

	public final void render(float x, float y, float z, float xRot, float yRot, float zRot) {
		this.head.yRot = xRot / 57.295776F;
		this.head.xRot = yRot / 57.295776F;
		this.body.xRot = (float)Math.PI / 2F;
		this.leg1.xRot = Mth.cos(x * 0.6662F) * 1.4F * y;
		this.leg2.xRot = Mth.cos(x * 0.6662F + (float)Math.PI) * 1.4F * y;
		this.leg3.xRot = Mth.cos(x * 0.6662F + (float)Math.PI) * 1.4F * y;
		this.leg4.xRot = Mth.cos(x * 0.6662F) * 1.4F * y;
		this.head.render(zRot);
		this.body.render(zRot);
		this.leg1.render(zRot);
		this.leg2.render(zRot);
		this.leg3.render(zRot);
		this.leg4.render(zRot);
	}
}