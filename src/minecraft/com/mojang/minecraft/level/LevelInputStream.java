package com.mojang.minecraft.level;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;

public final class LevelInputStream extends ObjectInputStream {
	private Set classDescriptors = new HashSet();

	/**
	 * i don't know how well this is going to work with the inner classes being renamed... so i didnt rename them in these mappings
	 * @throws IOException 
	 */
	public LevelInputStream(InputStream in) throws IOException {
		super(in);
		this.classDescriptors.add("com.mojang.minecraft.player.Player$1");
		this.classDescriptors.add("com.mojang.minecraft.mob.Creeper$1");
		this.classDescriptors.add("com.mojang.minecraft.mob.Skeleton$1");
	}

	protected final ObjectStreamClass readClassDescriptor() throws ClassNotFoundException, IOException {
		ObjectStreamClass objectStreamClass1 = super.readClassDescriptor();
		return this.classDescriptors.contains(objectStreamClass1.getName()) ? ObjectStreamClass.lookup(Class.forName(objectStreamClass1.getName())) : objectStreamClass1;
	}
}
