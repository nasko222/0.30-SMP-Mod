package com.mojang.minecraft.mob.ai;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;

import java.util.List;
import java.util.Random;

public class BasicAI extends AI {
	public static final long serialVersionUID = 0L;
	public Random random = new Random();
	public float xxa;
	public float yya;
	protected float yRotA;
	public Level level;
	public Mob mob;
	public boolean jumping = false;
	protected int attackDelay = 0;
	public float runSpeed = 0.7F;
	protected int noActionTime = 0;
	public Entity attackTarget = null;

	public void tick(Level level1, Mob mob2) {
		++this.noActionTime;
		Entity entity3;
		if(this.noActionTime > 600 && this.random.nextInt(800) == 0 && (entity3 = level1.getPlayer()) != null) {
			float f4 = entity3.x - mob2.x;
			float f5 = entity3.y - mob2.y;
			float f6 = entity3.z - mob2.z;
			if(f4 * f4 + f5 * f5 + f6 * f6 < 1024.0F) {
				this.noActionTime = 0;
			} else {
				mob2.remove();
			}
		}

		this.level = level1;
		this.mob = mob2;
		if(this.attackDelay > 0) {
			--this.attackDelay;
		}

		if(mob2.health <= 0) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.yya = 0.0F;
			this.yRotA = 0.0F;
		} else {
			this.update();
		}

		boolean z7 = mob2.isInWater();
		boolean z9 = mob2.isInLava();
		if(this.jumping) {
			if(z7) {
				mob2.yd += 0.04F;
			} else if(z9) {
				mob2.yd += 0.04F;
			} else if(mob2.onGround) {
				this.jumpFromGround();
			}
		}

		this.xxa *= 0.98F;
		this.yya *= 0.98F;
		this.yRotA *= 0.9F;
		mob2.travel(this.xxa, this.yya);
		List list11;
		if((list11 = level1.findEntities(mob2, mob2.bb.grow(0.2F, 0.0F, 0.2F))) != null && list11.size() > 0) {
			for(int i8 = 0; i8 < list11.size(); ++i8) {
				Entity entity10;
				if((entity10 = (Entity)list11.get(i8)).isPushable()) {
					entity10.push(mob2);
				}
			}
		}

	}

	protected void jumpFromGround() {
		this.mob.yd = 0.42F;
	}

	public void update() {
		if(this.random.nextFloat() < 0.07F) {
			this.xxa = (this.random.nextFloat() - 0.5F) * this.runSpeed;
			this.yya = this.random.nextFloat() * this.runSpeed;
		}

		this.jumping = this.random.nextFloat() < 0.01F;
		if(this.random.nextFloat() < 0.04F) {
			this.yRotA = (this.random.nextFloat() - 0.5F) * 60.0F;
		}

		this.mob.yRot += this.yRotA;
		this.mob.xRot = (float)this.defaultLookAngle;
		if(this.attackTarget != null) {
			this.yya = this.runSpeed;
			this.jumping = this.random.nextFloat() < 0.04F;
		}

		boolean z1 = this.mob.isInWater();
		boolean z2 = this.mob.isInLava();
		if(z1 || z2) {
			this.jumping = this.random.nextFloat() < 0.8F;
		}

	}

	public void beforeRemove() {
	}

	public void hurt(Entity entity1, int i2) {
		super.hurt(entity1, i2);
		this.noActionTime = 0;
	}
}