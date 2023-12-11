package com.mojang.minecraft.model;

import util.Mth;

public final class SpiderModel extends BaseModel {
	private Cube head = new Cube(32, 4);
	private Cube neck;
	private Cube body;
	private Cube leg1;
	private Cube leg2;
	private Cube leg3;
	private Cube leg4;
	private Cube leg5;
	private Cube leg6;
	private Cube leg7;
	private Cube leg8;

	public SpiderModel() {
		this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.head.setPos(0.0F, 0.0F, -3.0F);
		this.neck = new Cube(0, 0);
		this.neck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
		this.body = new Cube(0, 12);
		this.body.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, 0.0F);
		this.body.setPos(0.0F, 0.0F, 9.0F);
		this.leg1 = new Cube(18, 0);
		this.leg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg1.setPos(-4.0F, 0.0F, 2.0F);
		this.leg2 = new Cube(18, 0);
		this.leg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg2.setPos(4.0F, 0.0F, 2.0F);
		this.leg3 = new Cube(18, 0);
		this.leg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg3.setPos(-4.0F, 0.0F, 1.0F);
		this.leg4 = new Cube(18, 0);
		this.leg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg4.setPos(4.0F, 0.0F, 1.0F);
		this.leg5 = new Cube(18, 0);
		this.leg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg5.setPos(-4.0F, 0.0F, 0.0F);
		this.leg6 = new Cube(18, 0);
		this.leg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg6.setPos(4.0F, 0.0F, 0.0F);
		this.leg7 = new Cube(18, 0);
		this.leg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg7.setPos(-4.0F, 0.0F, -1.0F);
		this.leg8 = new Cube(18, 0);
		this.leg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
		this.leg8.setPos(4.0F, 0.0F, -1.0F);
	}

	public final void render(float x, float y, float z, float xRot, float yRot, float zRot) {
		this.head.yRot = xRot / 57.295776F;
		this.head.xRot = yRot / 57.295776F;
		xRot = 0.7853982F;
		this.leg1.zRot = -xRot;
		this.leg2.zRot = xRot;
		this.leg3.zRot = -xRot * 0.74F;
		this.leg4.zRot = xRot * 0.74F;
		this.leg5.zRot = -xRot * 0.74F;
		this.leg6.zRot = xRot * 0.74F;
		this.leg7.zRot = -xRot;
		this.leg8.zRot = xRot;
		xRot = 0.3926991F;
		this.leg1.yRot = xRot * 2.0F;
		this.leg2.yRot = -xRot * 2.0F;
		this.leg3.yRot = xRot;
		this.leg4.yRot = -xRot;
		this.leg5.yRot = -xRot;
		this.leg6.yRot = xRot;
		this.leg7.yRot = -xRot * 2.0F;
		this.leg8.yRot = xRot * 2.0F;
		xRot = -(Mth.cos(x * 0.6662F * 2.0F) * 0.4F) * y;
		yRot = -(Mth.cos(x * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * y;
		float f7 = -(Mth.cos(x * 0.6662F * 2.0F + (float)Math.PI / 2F) * 0.4F) * y;
		float f8 = -(Mth.cos(x * 0.6662F * 2.0F + 4.712389F) * 0.4F) * y;
		float f9 = Math.abs(Mth.sin(x * 0.6662F) * 0.4F) * y;
		float f10 = Math.abs(Mth.sin(x * 0.6662F + (float)Math.PI) * 0.4F) * y;
		float f11 = Math.abs(Mth.sin(x * 0.6662F + (float)Math.PI / 2F) * 0.4F) * y;
		y = Math.abs(Mth.sin(x * 0.6662F + 4.712389F) * 0.4F) * y;
		this.leg1.yRot += xRot;
		this.leg2.yRot -= xRot;
		this.leg3.yRot += yRot;
		this.leg4.yRot -= yRot;
		this.leg5.yRot += f7;
		this.leg6.yRot -= f7;
		this.leg7.yRot += f8;
		this.leg8.yRot -= f8;
		this.leg1.zRot += f9;
		this.leg2.zRot -= f9;
		this.leg3.zRot += f10;
		this.leg4.zRot -= f10;
		this.leg5.zRot += f11;
		this.leg6.zRot -= f11;
		this.leg7.zRot += y;
		this.leg8.zRot -= y;
		this.head.render(zRot);
		this.neck.render(zRot);
		this.body.render(zRot);
		this.leg1.render(zRot);
		this.leg2.render(zRot);
		this.leg3.render(zRot);
		this.leg4.render(zRot);
		this.leg5.render(zRot);
		this.leg6.render(zRot);
		this.leg7.render(zRot);
		this.leg8.render(zRot);
	}
}