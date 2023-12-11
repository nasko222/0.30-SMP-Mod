package com.mojang.minecraft.mob;

import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.ai.BasicAI;

import util.Mth;

final class Sheep$1 extends BasicAI {
	private static final long serialVersionUID = 1L;
	final Sheep this$0;

	Sheep$1(Sheep sheep1) {
		this.this$0 = sheep1;
	}

	public final void update() {
		float f1 = Mth.sin(this.this$0.yRot * (float)Math.PI / 180.0F);
		float f2 = Mth.cos(this.this$0.yRot * (float)Math.PI / 180.0F);
		f1 = -0.7F * f1;
		f2 = 0.7F * f2;
		int i4 = (int)(this.mob.x + f1);
		int i3 = (int)(this.mob.y - 2.0F);
		int i5 = (int)(this.mob.z + f2);
		if(this.this$0.grazing) {
			if(this.level.getTile(i4, i3, i5) != Tile.grass.id) {
				this.this$0.grazing = false;
			} else {
				if(++this.this$0.grazingTime == 60) {
					this.level.setTile(i4, i3, i5, Tile.dirt.id);
					if(this.random.nextInt(5) == 0) {
						this.this$0.hasFur = true;
					}
				}

				this.xxa = 0.0F;
				this.yya = 0.0F;
				this.mob.xRot = (float)(40 + this.this$0.grazingTime / 2 % 2 * 10);
			}
		} else {
			if(this.level.getTile(i4, i3, i5) == Tile.grass.id) {
				this.this$0.grazing = true;
				this.this$0.grazingTime = 0;
			}

			super.update();
		}
	}
}
