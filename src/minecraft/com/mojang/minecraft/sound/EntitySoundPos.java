package com.mojang.minecraft.sound;

import com.mojang.minecraft.Entity;

public final class EntitySoundPos extends BaseSoundPos {
	private Entity source;

	public EntitySoundPos(Entity source, Entity listener) {
		super(listener);
		this.source = source;
	}

	public final float getRotationDiff() {
		return super.getRotationDiff(this.source.x, this.source.z);
	}

	public final float getDistanceSq() {
		return super.getDistanceSq(this.source.x, this.source.y, this.source.z);
	}
}