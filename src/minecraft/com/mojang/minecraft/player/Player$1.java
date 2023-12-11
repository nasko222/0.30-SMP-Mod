package com.mojang.minecraft.player;

import com.mojang.minecraft.mob.ai.BasicAI;

final class Player$1 extends BasicAI {
	public static final long serialVersionUID = 0L;
	final Player this$0;

	Player$1(Player player1) {
		this.this$0 = player1;
	}

	public final void update() {
		this.jumping = this.this$0.input.jumping;
		this.xxa = this.this$0.input.ya;
		this.yya = this.this$0.input.xa;
	}
}
