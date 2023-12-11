package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;

import java.awt.image.BufferedImage;
import java.util.List;

import org.lwjgl.opengl.GL11;

import util.Mth;

public class Player extends Mob {
	public static final long serialVersionUID = 0L;
	public static final int MAX_HEALTH = 20;
	public static final int MAX_ARROWS = 99;
	public transient Input input;
	public Inventory inventory = new Inventory();
	public byte userType = 0;
	public float oBob;
	public float bob;
	public int score = 0;
	public int arrows = 0;
	private static int texture = -1;
	public static BufferedImage newTexture;

	public Player(Level level1) {
		super(level1);
		if(level1 != null) {
			level1.player = this;
			level1.removeEntity(this);
			level1.addEntity(this);
		}

		this.heightOffset = 1.62F;
		this.health = 20;
		this.modelName = "humanoid";
		this.rotOffs = 180.0F;
		this.ai = new Player$1(this);
	}

	public void resetPos() {
		this.heightOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.resetPos();
		if(this.level != null) {
			this.level.player = this;
		}

		if (!Minecraft.DontLoadToolbar) this.health = 20;
		this.deathTime = 0;
	}

	public void aiStep() {
		this.inventory.tick();
		this.oBob = this.bob;
		this.input.tick();
		super.aiStep();
		float f1 = Mth.sqrt(this.xd * this.xd + this.zd * this.zd);
		float f2 = (float)Math.atan((double)(-this.yd * 0.2F)) * 15.0F;
		if(f1 > 0.1F) {
			f1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f2 = 0.0F;
		}

		this.bob += (f1 - this.bob) * 0.4F;
		this.tilt += (f2 - this.tilt) * 0.8F;
		List list3;
		if(this.health > 0 && (list3 = this.level.findEntities(this, this.bb.grow(1.0F, 0.0F, 1.0F))) != null) {
			for(int i4 = 0; i4 < list3.size(); ++i4) {
				((Entity)list3.get(i4)).playerTouch(this);
			}
		}

	}

	public void render(Textures textures, float translation) {
	}

	public void releaseAllKeys() {
		this.input.releaseAllKeys();
	}

	public void setKey(int key, boolean state) {
		this.input.setKey(key, state);
	}

	public boolean addResource(int index) {
		return this.inventory.addResource(index);
	}

	public int getScore() {
		return this.score;
	}

	public HumanoidModel getModel() {
		return (HumanoidModel)modelCache.getModel(this.modelName);
	}

	public void die(Entity entity1) {
		this.setSize(0.2F, 0.2F);
		this.setPos(this.x, this.y, this.z);
		this.yd = 0.1F;
		if(entity1 != null) {
			this.xd = -Mth.cos((this.hurtDir + this.yRot) * (float)Math.PI / 180.0F) * 0.1F;
			this.zd = -Mth.sin((this.hurtDir + this.yRot) * (float)Math.PI / 180.0F) * 0.1F;
		} else {
			this.xd = this.zd = 0.0F;
		}

		this.heightOffset = 0.1F;
	}

	public void remove() {
	}

	public void awardKillScore(Entity entity1, int i2) {
		this.score += i2;
	}

	public boolean isShootable() {
		return true;
	}

	public void bindTexture(Textures textures) {
		if(newTexture != null) {
			texture = textures.loadTexture(newTexture);
			newTexture = null;
		}

		int textures1;
		if(texture < 0) {
			textures1 = textures.loadTexture("/char.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures1);
		} else {
			textures1 = texture;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures1);
		}
	}

	public void hurt(Entity entity1, int i2) {
		if(!this.level.creativeMode) {
			super.hurt(entity1, i2);
		}

	}

	public boolean isCreativeModeAllowed() {
		return true;
	}
}