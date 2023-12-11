package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Textures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockMap implements Serializable {
	public static final long serialVersionUID = 0L;
	private int width;
	private int depth;
	private int height;
	private BlockMap$Slot slot = new BlockMap$Slot(this, (UnusedSyntheticClass)null);
	private BlockMap$Slot slot2 = new BlockMap$Slot(this, (UnusedSyntheticClass)null);
	public List[] entityGrid;
	public List all = new ArrayList();
	private List tmp = new ArrayList();

	public BlockMap(int w, int d, int h) {
		this.width = w / 16;
		this.depth = d / 16;
		this.height = h / 16;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.depth * this.height];

		for(w = 0; w < this.width; ++w) {
			for(d = 0; d < this.depth; ++d) {
				for(h = 0; h < this.height; ++h) {
					this.entityGrid[(h * this.depth + d) * this.width + w] = new ArrayList();
				}
			}
		}

	}

	public void insert(Entity entity) {
		this.all.add(entity);
		this.slot.init(entity.x, entity.y, entity.z).add(entity);
		entity.xOld = entity.x;
		entity.yOld = entity.y;
		entity.zOld = entity.z;
		entity.blockMap = this;
	}

	public void remove(Entity entity) {
		this.slot.init(entity.xOld, entity.yOld, entity.zOld).remove(entity);
		this.all.remove(entity);
	}

	public void moved(Entity entity) {
		BlockMap$Slot blockMap$Slot2 = this.slot.init(entity.xOld, entity.yOld, entity.zOld);
		BlockMap$Slot blockMap$Slot3 = this.slot2.init(entity.x, entity.y, entity.z);
		if(!blockMap$Slot2.equals(blockMap$Slot3)) {
			blockMap$Slot2.remove(entity);
			blockMap$Slot3.add(entity);
			entity.xOld = entity.x;
			entity.yOld = entity.y;
			entity.zOld = entity.z;
		}
	}

	public List getEntities(Entity entity, float x0, float y0, float z0, float x1, float y1, float z1) {
		this.tmp.clear();
		return this.getEntities(entity, x0, y0, z0, x1, y1, z1, this.tmp);
	}

	public List getEntities(Entity entity, float x0, float y0, float z0, float x1, float y1, float z1, List entities) {
		BlockMap$Slot blockMap$Slot9 = this.slot.init(x0, y0, z0);
		BlockMap$Slot blockMap$Slot10 = this.slot2.init(x1, y1, z1);

		for(int i11 = BlockMap$Slot.access$400(blockMap$Slot9) - 1; i11 <= BlockMap$Slot.access$400(blockMap$Slot10) + 1; ++i11) {
			for(int i12 = BlockMap$Slot.access$500(blockMap$Slot9) - 1; i12 <= BlockMap$Slot.access$500(blockMap$Slot10) + 1; ++i12) {
				for(int i13 = BlockMap$Slot.access$600(blockMap$Slot9) - 1; i13 <= BlockMap$Slot.access$600(blockMap$Slot10) + 1; ++i13) {
					if(i11 >= 0 && i12 >= 0 && i13 >= 0 && i11 < this.width && i12 < this.depth && i13 < this.height) {
						List list14 = this.entityGrid[(i13 * this.depth + i12) * this.width + i11];

						for(int i15 = 0; i15 < list14.size(); ++i15) {
							Entity entity16;
							if((entity16 = (Entity)list14.get(i15)) != entity && entity16.intersects(x0, y0, z0, x1, y1, z1)) {
								entities.add(entity16);
							}
						}
					}
				}
			}
		}

		return entities;
	}

	public void removeAllNonCreativeModeEntities() {
		for(int i1 = 0; i1 < this.width; ++i1) {
			for(int i2 = 0; i2 < this.depth; ++i2) {
				for(int i3 = 0; i3 < this.height; ++i3) {
					List list4 = this.entityGrid[(i3 * this.depth + i2) * this.width + i1];

					for(int i5 = 0; i5 < list4.size(); ++i5) {
						if(!((Entity)list4.get(i5)).isCreativeModeAllowed()) {
							list4.remove(i5--);
						}
					}
				}
			}
		}

	}

	public void clear() {
		for(int i1 = 0; i1 < this.width; ++i1) {
			for(int i2 = 0; i2 < this.depth; ++i2) {
				for(int i3 = 0; i3 < this.height; ++i3) {
					this.entityGrid[(i3 * this.depth + i2) * this.width + i1].clear();
				}
			}
		}

	}

	public List getEntities(Entity entity, AABB box) {
		this.tmp.clear();
		return this.getEntities(entity, box.x0, box.y0, box.z0, box.x1, box.y1, box.z1, this.tmp);
	}

	public List getEntities(Entity entity, AABB box, List entities) {
		return this.getEntities(entity, box.x0, box.y0, box.z0, box.x1, box.y1, box.z1, entities);
	}

	public void tickAll() {
		for(int i1 = 0; i1 < this.all.size(); ++i1) {
			Entity entity2;
			(entity2 = (Entity)this.all.get(i1)).tick();
			if(entity2.removed) {
				this.all.remove(i1--);
				this.slot.init(entity2.xOld, entity2.yOld, entity2.zOld).remove(entity2);
			} else {
				int i3 = (int)(entity2.xOld / 16.0F);
				int i4 = (int)(entity2.yOld / 16.0F);
				int i5 = (int)(entity2.zOld / 16.0F);
				int i6 = (int)(entity2.x / 16.0F);
				int i7 = (int)(entity2.y / 16.0F);
				int i8 = (int)(entity2.z / 16.0F);
				if(i3 != i6 || i4 != i7 || i5 != i8) {
					this.moved(entity2);
				}
			}
		}

	}

	public void render(Vec3 v, Frustum frustum, Textures textures, float a_) {
		for(int i5 = 0; i5 < this.width; ++i5) {
			float f6 = (float)((i5 << 4) - 2);
			float f7 = (float)((i5 + 1 << 4) + 2);

			for(int i8 = 0; i8 < this.depth; ++i8) {
				float f9 = (float)((i8 << 4) - 2);
				float f10 = (float)((i8 + 1 << 4) + 2);

				for(int i11 = 0; i11 < this.height; ++i11) {
					List list12;
					if((list12 = this.entityGrid[(i11 * this.depth + i8) * this.width + i5]).size() != 0) {
						float f13 = (float)((i11 << 4) - 2);
						float f14 = (float)((i11 + 1 << 4) + 2);
						if(frustum.cubeInFrustum(f6, f9, f13, f7, f10, f14)) {
							float f19 = f14;
							float f18 = f10;
							float f15 = f7;
							f14 = f13;
							f13 = f9;
							float f17 = f6;
							Frustum frustum16 = frustum;
							int i20 = 0;

							boolean z10000;
							while(true) {
								if(i20 >= 6) {
									z10000 = true;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f17 + frustum16.m_Frustum[i20][1] * f13 + frustum16.m_Frustum[i20][2] * f14 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f15 + frustum16.m_Frustum[i20][1] * f13 + frustum16.m_Frustum[i20][2] * f14 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f17 + frustum16.m_Frustum[i20][1] * f18 + frustum16.m_Frustum[i20][2] * f14 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f15 + frustum16.m_Frustum[i20][1] * f18 + frustum16.m_Frustum[i20][2] * f14 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f17 + frustum16.m_Frustum[i20][1] * f13 + frustum16.m_Frustum[i20][2] * f19 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f15 + frustum16.m_Frustum[i20][1] * f13 + frustum16.m_Frustum[i20][2] * f19 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f17 + frustum16.m_Frustum[i20][1] * f18 + frustum16.m_Frustum[i20][2] * f19 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								if(frustum16.m_Frustum[i20][0] * f15 + frustum16.m_Frustum[i20][1] * f18 + frustum16.m_Frustum[i20][2] * f19 + frustum16.m_Frustum[i20][3] <= 0.0F) {
									z10000 = false;
									break;
								}

								++i20;
							}

							boolean z21 = z10000;

							for(int i22 = 0; i22 < list12.size(); ++i22) {
								Entity entity23;
								if((entity23 = (Entity)list12.get(i22)).shouldRender(v)) {
									if(!z21) {
										AABB aABB24 = entity23.bb;
										if(!frustum.cubeInFrustum(aABB24.x0, aABB24.y0, aABB24.z0, aABB24.x1, aABB24.y1, aABB24.z1)) {
											continue;
										}
									}

									entity23.render(textures, a_);
								}
							}
						}
					}
				}
			}
		}

	}

	static int access$000(BlockMap blockMap0) {
		return blockMap0.width;
	}

	static int access$100(BlockMap blockMap0) {
		return blockMap0.depth;
	}

	static int access$200(BlockMap blockMap0) {
		return blockMap0.height;
	}
}