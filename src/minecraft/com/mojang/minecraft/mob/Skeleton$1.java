package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.BasicAttackAI;

final class Skeleton$1 extends BasicAttackAI {
	public static final long serialVersionUID = 0L;
	final Skeleton this$0;

	Skeleton$1(Skeleton skeleton1) {
		this.this$0 = skeleton1;
	}

	public final void tick(Level level1, Mob mob2) {
		super.tick(level1, mob2);
		if(mob2.health > 0 && this.random.nextInt(30) == 0 && this.attackTarget != null) {
			this.this$0.shootArrow(level1);
		}

	}

	public final void beforeRemove() {
		Skeleton.access$000(this.this$0);
	}
}