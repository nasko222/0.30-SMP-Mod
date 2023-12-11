package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;

import java.io.Serializable;

class BlockMap$Slot implements Serializable {
	public static final long serialVersionUID = 0L;
	private int xSlot;
	private int ySlot;
	private int zSlot;
	final BlockMap this$0;

	private BlockMap$Slot(BlockMap blockMap1) {
		this.this$0 = blockMap1;
	}

	public BlockMap$Slot init(float f1, float f2, float f3) {
		this.xSlot = (int)(f1 / 16.0F);
		this.ySlot = (int)(f2 / 16.0F);
		this.zSlot = (int)(f3 / 16.0F);
		if(this.xSlot < 0) {
			this.xSlot = 0;
		}

		if(this.ySlot < 0) {
			this.ySlot = 0;
		}

		if(this.zSlot < 0) {
			this.zSlot = 0;
		}

		if(this.xSlot >= BlockMap.access$000(this.this$0)) {
			this.xSlot = BlockMap.access$000(this.this$0) - 1;
		}

		if(this.ySlot >= BlockMap.access$100(this.this$0)) {
			this.ySlot = BlockMap.access$100(this.this$0) - 1;
		}

		if(this.zSlot >= BlockMap.access$200(this.this$0)) {
			this.zSlot = BlockMap.access$200(this.this$0) - 1;
		}

		return this;
	}

	public void add(Entity entity1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.this$0.entityGrid[(this.zSlot * BlockMap.access$100(this.this$0) + this.ySlot) * BlockMap.access$000(this.this$0) + this.xSlot].add(entity1);
		}

	}

	public void remove(Entity entity1) {
		if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
			this.this$0.entityGrid[(this.zSlot * BlockMap.access$100(this.this$0) + this.ySlot) * BlockMap.access$000(this.this$0) + this.xSlot].remove(entity1);
		}

	}

	BlockMap$Slot(BlockMap blockMap1, UnusedSyntheticClass unusedSyntheticClass2) {
		this(blockMap1);
	}

	static int access$400(BlockMap$Slot blockMap$Slot0) {
		return blockMap$Slot0.xSlot;
	}

	static int access$500(BlockMap$Slot blockMap$Slot0) {
		return blockMap$Slot0.ySlot;
	}

	static int access$600(BlockMap$Slot blockMap$Slot0) {
		return blockMap$Slot0.zSlot;
	}
}