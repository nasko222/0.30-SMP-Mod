package com.mojang.minecraft.particle;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Tesselator;

public class SmokeParticle extends Particle {
	private static final long serialVersionUID = 1L;

	public SmokeParticle(Level level, float x, float y, float z) {
		super(level, x, y, z, 0.0F, 0.0F, 0.0F);
		this.xd *= 0.1F;
		this.yd *= 0.1F;
		this.zd *= 0.1F;
		this.rCol = this.gCol = this.bCol = (float)(Math.random() * (double)0.3F);
		this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.noPhysics = true;
	}

	public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float ya2) {
		super.render(t, a, xa, ya, za, xa2, ya2);
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.age++ >= this.lifetime) {
			this.remove();
		}

		this.tex = 7 - (this.age << 3) / this.lifetime;
		this.yd = (float)((double)this.yd + 0.004D);
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.96F;
		this.yd *= 0.96F;
		this.zd *= 0.96F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
		}

	}
}