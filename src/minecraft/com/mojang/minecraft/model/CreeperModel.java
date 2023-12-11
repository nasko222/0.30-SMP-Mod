package com.mojang.minecraft.model;

import util.Mth;

public final class CreeperModel extends BaseModel {
	private Cube head = new Cube(0, 0);
	private Cube hair;
	private Cube body;
	private Cube leg1;
	private Cube leg2;
	private Cube leg3;
	private Cube leg4;

	public CreeperModel() {
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.hair = new Cube(32, 0);
		this.hair.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F + 0.5F);
		this.body = new Cube(16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.leg1 = new Cube(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg1.setPos(-2.0F, 12.0F, 4.0F);
		this.leg2 = new Cube(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2.setPos(2.0F, 12.0F, 4.0F);
		this.leg3 = new Cube(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3.setPos(-2.0F, 12.0F, -4.0F);
		this.leg4 = new Cube(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4.setPos(2.0F, 12.0F, -4.0F);
	}

	public final void render(float x, float y, float z, float xRot, float yRot, float zRot) {
		this.head.yRot = xRot / 57.295776F;
		this.head.xRot = yRot / 57.295776F;
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