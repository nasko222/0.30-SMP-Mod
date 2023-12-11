package com.mojang.minecraft.player;

import com.mojang.minecraft.Options;

public final class KeyboardInput extends Input {
	private boolean[] keys = new boolean[10];
	private Options options;

	public KeyboardInput(Options options) {
		this.options = options;
	}

	public final void setKey(int key, boolean state) {
		byte b3 = -1;
		if(key == this.options.forward.key) {
			b3 = 0;
		}

		if(key == this.options.back.key) {
			b3 = 1;
		}

		if(key == this.options.left.key) {
			b3 = 2;
		}

		if(key == this.options.right.key) {
			b3 = 3;
		}

		if(key == this.options.jump.key) {
			b3 = 4;
		}

		if(b3 >= 0) {
			this.keys[b3] = state;
		}

	}

	public final void releaseAllKeys() {
		for(int i1 = 0; i1 < 10; ++i1) {
			this.keys[i1] = false;
		}

	}

	public final void tick() {
		this.ya = 0.0F;
		this.xa = 0.0F;
		if(this.keys[0]) {
			--this.xa;
		}

		if(this.keys[1]) {
			++this.xa;
		}

		if(this.keys[2]) {
			--this.ya;
		}

		if(this.keys[3]) {
			++this.ya;
		}

		this.jumping = this.keys[4];
	}
}