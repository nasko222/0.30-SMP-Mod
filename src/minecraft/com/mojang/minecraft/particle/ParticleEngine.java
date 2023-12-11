package com.mojang.minecraft.particle;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Textures;

import java.util.ArrayList;
import java.util.List;

public final class ParticleEngine {
	public List[] particles = new List[2];
	public Textures textures;

	public ParticleEngine(Level level, Textures textures) {
		if(level != null) {
			level.particleEngine = this;
		}

		this.textures = textures;

		for(int i3 = 0; i3 < 2; ++i3) {
			this.particles[i3] = new ArrayList();
		}

	}

	public final void addParticle(Entity particle) {
		Particle particle1;
		int i2 = (particle1 = (Particle)particle).getParticleTexture();
		this.particles[i2].add(particle1);
	}

	public final void tick() {
		for(int i1 = 0; i1 < 2; ++i1) {
			for(int i2 = 0; i2 < this.particles[i1].size(); ++i2) {
				Particle particle3;
				(particle3 = (Particle)this.particles[i1].get(i2)).tick();
				if(particle3.removed) {
					this.particles[i1].remove(i2--);
				}
			}
		}

	}
}