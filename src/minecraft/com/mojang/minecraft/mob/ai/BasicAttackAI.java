package com.mojang.minecraft.mob.ai;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.Arrow;
import com.mojang.minecraft.model.Vec3;

import util.Mth;

public class BasicAttackAI extends BasicAI {
	public static final long serialVersionUID = 0L;
	public int damage = 6;

	public void update() {
		super.update();
		if(this.mob.health > 0) {
			this.doAttack();
		}

	}

	protected void doAttack() {
		Entity entity1 = this.level.getPlayer();
		float f2 = 16.0F;
		if(this.attackTarget != null && this.attackTarget.removed) {
			this.attackTarget = null;
		}

		float f3;
		float f4;
		float f5;
		if(entity1 != null && this.attackTarget == null) {
			f3 = entity1.x - this.mob.x;
			f4 = entity1.y - this.mob.y;
			f5 = entity1.z - this.mob.z;
			if(f3 * f3 + f4 * f4 + f5 * f5 < f2 * f2) {
				this.attackTarget = entity1;
			}
		}

		if(this.attackTarget != null) {
			f3 = this.attackTarget.x - this.mob.x;
			f4 = this.attackTarget.y - this.mob.y;
			f5 = this.attackTarget.z - this.mob.z;
			float f6;
			if((f6 = f3 * f3 + f4 * f4 + f5 * f5) > f2 * f2 * 2.0F * 2.0F && this.random.nextInt(100) == 0) {
				this.attackTarget = null;
			}

			if(this.attackTarget != null) {
				f6 = Mth.sqrt(f6);
				this.mob.yRot = (float)(Math.atan2((double)f5, (double)f3) * 180.0D / (double)(float)Math.PI) - 90.0F;
				this.mob.xRot = -((float)(Math.atan2((double)f4, (double)f6) * 180.0D / (double)(float)Math.PI));
				if(Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5) < 2.0F && this.attackDelay == 0) {
					this.attack(this.attackTarget);
				}
			}

		}
	}

	public boolean attack(Entity entity) {
		if(this.level.clip(new Vec3(this.mob.x, this.mob.y, this.mob.z), new Vec3(entity.x, entity.y, entity.z)) != null) {
			return false;
		} else {
			this.mob.attackTime = 5;
			this.attackDelay = this.random.nextInt(20) + 10;
			int i2 = (int)((this.random.nextFloat() + this.random.nextFloat()) / 2.0F * (float)this.damage + 1.0F);
			entity.hurt(this.mob, i2);
			this.noActionTime = 0;
			return true;
		}
	}

	public void hurt(Entity entity1, int i2) {
		super.hurt(entity1, i2);
		if(entity1 instanceof Arrow) {
			entity1 = ((Arrow)entity1).getOwner();
		}

		if(entity1 != null && !entity1.getClass().equals(this.mob.getClass())) {
			this.attackTarget = entity1;
		}

	}
}
