package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import java.util.List;

import org.lwjgl.opengl.GL11;

import util.Mth;

public class Arrow extends Entity {
	public static final long serialVersionUID = 0L;
	private float xd;
	private float yd;
	private float zd;
	private float yRot;
	private float xRot;
	private float yRotO;
	private float xRotO;
	private boolean hasHit = false;
	private int stickTime = 0;
	private Entity owner;
	private int time = 0;
	private int type = 0;
	private float gravity = 0.0F;
	private int damage;

	public Arrow(Level level, Entity entity, float x, float y, float z, float xr, float yr, float zr) {
		super(level);
		this.owner = entity;
		this.setSize(0.3F, 0.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.damage = 3;
		if(!(entity instanceof Player)) {
			this.type = 1;
		} else {
			this.damage = 7;
		}

		this.heightOffset = 0.25F;
		float level1 = Mth.cos(-xr * 0.017453292F - (float)Math.PI);
		float entity1 = Mth.sin(-xr * 0.017453292F - (float)Math.PI);
		xr = Mth.cos(-yr * 0.017453292F);
		yr = Mth.sin(-yr * 0.017453292F);
		this.slide = false;
		this.gravity = 1.0F / zr;
		this.xo -= level1 * 0.2F;
		this.zo += entity1 * 0.2F;
		x -= level1 * 0.2F;
		z += entity1 * 0.2F;
		this.xd = entity1 * xr * zr;
		this.yd = yr * zr;
		this.zd = level1 * xr * zr;
		this.setPos(x, y, z);
		level1 = Mth.sqrt(this.xd * this.xd + this.zd * this.zd);
		this.yRotO = this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / (double)(float)Math.PI);
		this.xRotO = this.xRot = (float)(Math.atan2((double)this.yd, (double)level1) * 180.0D / (double)(float)Math.PI);
		this.makeStepSound = false;
	}

	public void tick() {
		++this.time;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.hasHit) {
			++this.stickTime;
			if(this.type == 0) {
				if(this.stickTime >= 300 && Math.random() < (double)0.01F) {
					this.remove();
					return;
				}
			} else if(this.type == 1 && this.stickTime >= 20) {
				this.remove();
			}

		} else {
			this.xd *= 0.998F;
			this.yd *= 0.998F;
			this.zd *= 0.998F;
			this.yd -= 0.02F * this.gravity;
			int i1 = (int)(Mth.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd) / 0.2F + 1.0F);
			float f2 = this.xd / (float)i1;
			float f3 = this.yd / (float)i1;
			float f4 = this.zd / (float)i1;

			for(int i5 = 0; i5 < i1 && !this.collision; ++i5) {
				AABB aABB6 = this.bb.expand(f2, f3, f4);
				if(this.level.getCubes(aABB6).size() > 0) {
					this.collision = true;
				}

				List list10 = this.level.blockMap.getEntities(this, aABB6);

				for(int i7 = 0; i7 < list10.size(); ++i7) {
					Entity entity8;
					if((entity8 = (Entity)list10.get(i7)).isShootable() && (entity8 != this.owner || this.time > 5)) {
						entity8.hurt(this, this.damage);
						this.collision = true;
						this.remove();
						return;
					}
				}

				if(!this.collision) {
					this.bb.move(f2, f3, f4);
					this.x += f2;
					this.y += f3;
					this.z += f4;
					this.blockMap.moved(this);
				}
			}

			if(this.collision) {
				this.hasHit = true;
				this.xd = this.yd = this.zd = 0.0F;
			}

			if(!this.hasHit) {
				float f9 = Mth.sqrt(this.xd * this.xd + this.zd * this.zd);
				this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / (double)(float)Math.PI);

				for(this.xRot = (float)(Math.atan2((double)this.yd, (double)f9) * 180.0D / (double)(float)Math.PI); this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F) {
				}

				while(this.xRot - this.xRotO >= 180.0F) {
					this.xRotO += 360.0F;
				}

				while(this.yRot - this.yRotO < -180.0F) {
					this.yRotO -= 360.0F;
				}

				while(this.yRot - this.yRotO >= 180.0F) {
					this.yRotO += 360.0F;
				}
			}

		}
	}

	public void render(Textures textures, float translation) {
		this.textureId = textures.loadTexture("/item/arrows.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
		float f10 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glColor4f(f10, f10, f10, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * translation, this.yo + (this.y - this.yo) * translation - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * translation);
		GL11.glRotatef(this.yRotO + (this.yRot - this.yRotO) * translation - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.xRotO + (this.xRot - this.xRotO) * translation, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		Tesselator tesselator11 = Tesselator.instance;
		translation = 0.5F;
		float f3 = (float)(0 + this.type * 10) / 32.0F;
		float f4 = (float)(5 + this.type * 10) / 32.0F;
		float f5 = 0.15625F;
		float f6 = (float)(5 + this.type * 10) / 32.0F;
		float f8 = (float)(10 + this.type * 10) / 32.0F;
		float f7 = 0.05625F;
		GL11.glScalef(0.05625F, f7, f7);
		GL11.glNormal3f(f7, 0.0F, 0.0F);
		tesselator11.begin();
		tesselator11.vertexUV(-7.0F, -2.0F, -2.0F, 0.0F, f6);
		tesselator11.vertexUV(-7.0F, -2.0F, 2.0F, f5, f6);
		tesselator11.vertexUV(-7.0F, 2.0F, 2.0F, f5, f8);
		tesselator11.vertexUV(-7.0F, 2.0F, -2.0F, 0.0F, f8);
		tesselator11.end();
		GL11.glNormal3f(-f7, 0.0F, 0.0F);
		tesselator11.begin();
		tesselator11.vertexUV(-7.0F, 2.0F, -2.0F, 0.0F, f6);
		tesselator11.vertexUV(-7.0F, 2.0F, 2.0F, f5, f6);
		tesselator11.vertexUV(-7.0F, -2.0F, 2.0F, f5, f8);
		tesselator11.vertexUV(-7.0F, -2.0F, -2.0F, 0.0F, f8);
		tesselator11.end();

		for(int i9 = 0; i9 < 4; ++i9) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, -f7, 0.0F);
			tesselator11.vertexUV(-8.0F, -2.0F, 0.0F, 0.0F, f3);
			tesselator11.vertexUV(8.0F, -2.0F, 0.0F, translation, f3);
			tesselator11.vertexUV(8.0F, 2.0F, 0.0F, translation, f4);
			tesselator11.vertexUV(-8.0F, 2.0F, 0.0F, 0.0F, f4);
			tesselator11.end();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void awardKillScore(Entity entity1, int i2) {
		this.owner.awardKillScore(entity1, i2);
	}

	public Entity getOwner() {
		return this.owner;
	}

	public void playerTouch(Entity entity1) {
		Player player2 = (Player)entity1;
		if(this.hasHit && this.owner == player2 && player2.arrows < 99) {
			this.level.addEntity(new TakeEntityAnim(this.level, this, player2));
			++player2.arrows;
			this.remove();
		}

	}
}