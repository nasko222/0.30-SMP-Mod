package com.mojang.minecraft.player;

import com.mojang.minecraft.User;
import com.mojang.minecraft.level.tile.Tile;

import java.io.Serializable;

public class Inventory implements Serializable {
	public static final long serialVersionUID = 0L;
	public static final int POP_TIME_DURATION = 5;
	public int[] slots = new int[9];
	public int[] count = new int[9];
	public int[] popTime = new int[9];
	public int selected = 0;

	public Inventory() {
		for(int i1 = 0; i1 < 9; ++i1) {
			this.slots[i1] = -1;
			this.count[i1] = 0;
		}

	}

	public int getSelected() {
		return this.slots[this.selected];
	}

	private int containsTileAt(int index) {
		for(int i2 = 0; i2 < this.slots.length; ++i2) {
			if(index == this.slots[i2]) {
				return i2;
			}
		}

		return -1;
	}

	public void grabTexture(int id, boolean replace) {
		int i3;
		if((i3 = this.containsTileAt(id)) >= 0) {
			this.selected = i3;
		} else {
			if(replace && id > 0 && User.creativeTiles.contains(Tile.tiles[id])) {
				this.replaceSlot(Tile.tiles[id]);
			}

		}
	}

	public void swapPaint(int index) {
		if(index > 0) {
			index = 1;
		}

		if(index < 0) {
			index = -1;
		}

		for(this.selected -= index; this.selected < 0; this.selected += this.slots.length) {
		}

		while(this.selected >= this.slots.length) {
			this.selected -= this.slots.length;
		}

	}

	public void replaceSlot(int id) {
		if(id >= 0) {
			this.replaceSlot((Tile)User.creativeTiles.get(id));
		}

	}

	public void replaceSlot(Tile tile) {
		if(tile != null) {
			int i2;
			if((i2 = this.containsTileAt(tile.id)) >= 0) {
				this.slots[i2] = this.slots[this.selected];
			}

			this.slots[this.selected] = tile.id;
		}

	}
	
	public void addResource(int intValue1, int intValue2) {
		for (int i = 0; i < intValue2; i++) {
			addResource(intValue1);
		}
	}
	
	public boolean addTool(int id) {
		int i2 = this.containsTileAt(-1);
		if (i2 < 0) return false;
			this.slots[i2] = id;
			++this.count[i2];
			this.popTime[i2] = 5;
			return true;
		
	}

	public boolean addResource(int id) {
		int i2;
		if((i2 = this.containsTileAt(id)) < 0) {
			i2 = this.containsTileAt(-1);
		}

		if(i2 < 0) {
			return false;
		} else if(this.count[i2] >= 999) {
			return false;
		} else {
			this.slots[i2] = id;
			++this.count[i2];
			this.popTime[i2] = 5;
			return true;
		}
	}

	public void tick() {
		for(int i1 = 0; i1 < this.popTime.length; ++i1) {
			if(this.popTime[i1] > 0) {
				--this.popTime[i1];
			}
		}

	}

	public boolean removeResource(int index) {
		if((index = this.containsTileAt(index)) < 0) {
			return false;
		} else {
			if(--this.count[index] <= 0) {
				this.slots[index] = -1;
			}

			return true;
		}
	}

	public void clearInv() {
		for (int i = 0; i < 9; i++) {
			slots[i] = -1;
			count[i] = 0;
		}
	}

	
}