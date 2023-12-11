package com.mojang.minecraft.level.levelgen;

import com.mojang.minecraft.LevelLoaderListener;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.levelgen.synth.Distort;
import com.mojang.minecraft.level.levelgen.synth.PerlinNoise;
import com.mojang.minecraft.level.tile.Tile;

import java.util.ArrayList;
import java.util.Random;

import util.Mth;

public final class LevelGen {
	private LevelLoaderListener levelLoaderListener;
	private int width;
	private int height;
	private int depth;
	private Random random = new Random();
	private byte[] blocks;
	private int waterLevel;
	private int[] coords = new int[1048576];

	public LevelGen(LevelLoaderListener levelLoaderListener) {
		this.levelLoaderListener = levelLoaderListener;
	}

	public final Level generateLevel(String creator, int width, int height, int depth) {
		this.levelLoaderListener.beginLevelLoading("Generating level");
		this.width = width;
		this.height = height;
		this.depth = 64;
		this.waterLevel = 32;
		this.blocks = new byte[width * height << 6];
		this.levelLoaderListener.levelLoadUpdate("Raising..");
		LevelGen levelGen6 = this;
		Distort distort7 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		PerlinNoise perlinNoise9 = new PerlinNoise(this.random, 6);
		int[] i10 = new int[this.width * this.height];
		float f11 = 1.3F;

		int i12;
		int i14;
		for(i14 = 0; i14 < levelGen6.width; ++i14) {
			levelGen6.setNextPhase(i14 * 100 / (levelGen6.width - 1));

			for(i12 = 0; i12 < levelGen6.height; ++i12) {
				double d16 = distort7.getValue((double)((float)i14 * f11), (double)((float)i12 * f11)) / 6.0D + (double)-4;
				double d18 = distort8.getValue((double)((float)i14 * f11), (double)((float)i12 * f11)) / 5.0D + 10.0D + (double)-4;
				if(perlinNoise9.getValue((double)i14, (double)i12) / 8.0D > 0.0D) {
					d18 = d16;
				}

				double d22;
				if((d22 = Math.max(d16, d18) / 2.0D) < 0.0D) {
					d22 *= 0.8D;
				}

				i10[i14 + i12 * levelGen6.width] = (int)d22;
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Eroding..");
		int[] i33 = i10;
		levelGen6 = this;
		distort8 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));
		Distort distort38 = new Distort(new PerlinNoise(this.random, 8), new PerlinNoise(this.random, 8));

		int i42;
		int i44;
		int i49;
		for(i42 = 0; i42 < levelGen6.width; ++i42) {
			levelGen6.setNextPhase(i42 * 100 / (levelGen6.width - 1));

			for(i44 = 0; i44 < levelGen6.height; ++i44) {
				double d13 = distort8.getValue((double)(i42 << 1), (double)(i44 << 1)) / 8.0D;
				i12 = distort38.getValue((double)(i42 << 1), (double)(i44 << 1)) > 0.0D ? 1 : 0;
				if(d13 > 2.0D) {
					i49 = ((i33[i42 + i44 * levelGen6.width] - i12) / 2 << 1) + i12;
					i33[i42 + i44 * levelGen6.width] = i49;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Soiling..");
		i33 = i10;
		levelGen6 = this;
		int i36 = this.width;
		int i40 = this.height;
		i42 = this.depth;
		PerlinNoise perlinNoise45 = new PerlinNoise(this.random, 8);

		int i17;
		int i19;
		int i20;
		int i46;
		int i54;
		for(i46 = 0; i46 < i36; ++i46) {
			levelGen6.setNextPhase(i46 * 100 / (levelGen6.width - 1));

			for(i14 = 0; i14 < i40; ++i14) {
				i12 = (int)(perlinNoise45.getValue((double)i46, (double)i14) / 24.0D) - 4;
				i17 = (i49 = i33[i46 + i14 * i36] + levelGen6.waterLevel) + i12;
				i33[i46 + i14 * i36] = Math.max(i49, i17);
				if(i33[i46 + i14 * i36] > i42 - 2) {
					i33[i46 + i14 * i36] = i42 - 2;
				}

				if(i33[i46 + i14 * i36] < 1) {
					i33[i46 + i14 * i36] = 1;
				}

				for(i54 = 0; i54 < i42; ++i54) {
					i19 = (i54 * levelGen6.height + i14) * levelGen6.width + i46;
					i20 = 0;
					if(i54 <= i49) {
						i20 = Tile.dirt.id;
					}

					if(i54 <= i17) {
						i20 = Tile.rock.id;
					}

					if(i54 == 0) {
						i20 = Tile.lava.id;
					}

					levelGen6.blocks[i19] = (byte)i20;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Carving..");
		boolean z37 = true;
		boolean z34 = false;
		levelGen6 = this;
		i40 = this.width;
		i42 = this.height;
		i44 = this.depth;
		i46 = i40 * i42 * i44 / 256 / 64 << 1;

		for(i14 = 0; i14 < i46; ++i14) {
			levelGen6.setNextPhase(i14 * 100 / (i46 - 1) / 4);
			float f47 = levelGen6.random.nextFloat() * (float)i40;
			float f50 = levelGen6.random.nextFloat() * (float)i44;
			float f51 = levelGen6.random.nextFloat() * (float)i42;
			i54 = (int)((levelGen6.random.nextFloat() + levelGen6.random.nextFloat()) * 200.0F);
			float f55 = levelGen6.random.nextFloat() * (float)Math.PI * 2.0F;
			float f56 = 0.0F;
			float f21 = levelGen6.random.nextFloat() * (float)Math.PI * 2.0F;
			float f58 = 0.0F;
			float f23 = levelGen6.random.nextFloat() * levelGen6.random.nextFloat();

			for(int i5 = 0; i5 < i54; ++i5) {
				f47 += Mth.sin(f55) * Mth.cos(f21);
				f51 += Mth.cos(f55) * Mth.cos(f21);
				f50 += Mth.sin(f21);
				f55 += f56 * 0.2F;
				f56 = (f56 *= 0.9F) + (levelGen6.random.nextFloat() - levelGen6.random.nextFloat());
				f21 = (f21 + f58 * 0.5F) * 0.5F;
				f58 = (f58 *= 0.75F) + (levelGen6.random.nextFloat() - levelGen6.random.nextFloat());
				if(levelGen6.random.nextFloat() >= 0.25F) {
					float f35 = f47 + (levelGen6.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f39 = f50 + (levelGen6.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f15 = f51 + (levelGen6.random.nextFloat() * 4.0F - 2.0F) * 0.2F;
					float f24 = ((float)levelGen6.depth - f39) / (float)levelGen6.depth;
					f24 = 1.2F + (f24 * 3.5F + 1.0F) * f23;
					f24 = Mth.sin((float)i5 * (float)Math.PI / (float)i54) * f24;

					for(int i25 = (int)(f35 - f24); i25 <= (int)(f35 + f24); ++i25) {
						for(int i26 = (int)(f39 - f24); i26 <= (int)(f39 + f24); ++i26) {
							for(int i27 = (int)(f15 - f24); i27 <= (int)(f15 + f24); ++i27) {
								float f28 = (float)i25 - f35;
								float f29 = (float)i26 - f39;
								float f30 = (float)i27 - f15;
								if(f28 * f28 + f29 * f29 * 2.0F + f30 * f30 < f24 * f24 && i25 >= 1 && i26 >= 1 && i27 >= 1 && i25 < levelGen6.width - 1 && i26 < levelGen6.depth - 1 && i27 < levelGen6.height - 1) {
									int i60 = (i26 * levelGen6.height + i27) * levelGen6.width + i25;
									if(levelGen6.blocks[i60] == Tile.rock.id) {
										levelGen6.blocks[i60] = 0;
									}
								}
							}
						}
					}
				}
			}
		}

		this.addOre(Tile.coalOre.id, 90, 1, 4);
		this.addOre(Tile.ironOre.id, 70, 2, 4);
		this.addOre(Tile.goldOre.id, 50, 3, 4);
		this.levelLoaderListener.levelLoadUpdate("Watering..");
		levelGen6 = this;
		i42 = Tile.calmWater.id;
		this.setNextPhase(0);

		for(i44 = 0; i44 < levelGen6.width; ++i44) {
			levelGen6.floodFillWithLiquid(i44, levelGen6.depth / 2 - 1, 0, 0, i42);
			levelGen6.floodFillWithLiquid(i44, levelGen6.depth / 2 - 1, levelGen6.height - 1, 0, i42);
		}

		for(i44 = 0; i44 < levelGen6.height; ++i44) {
			levelGen6.floodFillWithLiquid(0, levelGen6.depth / 2 - 1, i44, 0, i42);
			levelGen6.floodFillWithLiquid(levelGen6.width - 1, levelGen6.depth / 2 - 1, i44, 0, i42);
		}

		i44 = levelGen6.width * levelGen6.height / 8000;

		for(i46 = 0; i46 < i44; ++i46) {
			if(i46 % 100 == 0) {
				levelGen6.setNextPhase(i46 * 100 / (i44 - 1));
			}

			i14 = levelGen6.random.nextInt(levelGen6.width);
			i12 = levelGen6.waterLevel - 1 - levelGen6.random.nextInt(2);
			i49 = levelGen6.random.nextInt(levelGen6.height);
			if(levelGen6.blocks[(i12 * levelGen6.height + i49) * levelGen6.width + i14] == 0) {
				levelGen6.floodFillWithLiquid(i14, i12, i49, 0, i42);
			}
		}

		levelGen6.setNextPhase(100);
		this.levelLoaderListener.levelLoadUpdate("Melting..");
		levelGen6 = this;
		i36 = this.width * this.height * this.depth / 20000;

		for(i40 = 0; i40 < i36; ++i40) {
			if(i40 % 100 == 0) {
				levelGen6.setNextPhase(i40 * 100 / (i36 - 1));
			}

			i42 = levelGen6.random.nextInt(levelGen6.width);
			i44 = (int)(levelGen6.random.nextFloat() * levelGen6.random.nextFloat() * (float)(levelGen6.waterLevel - 3));
			i46 = levelGen6.random.nextInt(levelGen6.height);
			if(levelGen6.blocks[(i44 * levelGen6.height + i46) * levelGen6.width + i42] == 0) {
				levelGen6.floodFillWithLiquid(i42, i44, i46, 0, Tile.calmLava.id);
			}
		}

		levelGen6.setNextPhase(100);
		this.levelLoaderListener.levelLoadUpdate("Growing..");
		i33 = i10;
		levelGen6 = this;
		i36 = this.width;
		i40 = this.height;
		i42 = this.depth;
		perlinNoise45 = new PerlinNoise(this.random, 8);
		PerlinNoise perlinNoise48 = new PerlinNoise(this.random, 8);

		int i57;
		for(i14 = 0; i14 < i36; ++i14) {
			levelGen6.setNextPhase(i14 * 100 / (levelGen6.width - 1));

			for(i12 = 0; i12 < i40; ++i12) {
				boolean z52 = perlinNoise45.getValue((double)i14, (double)i12) > 8.0D;
				boolean z53 = perlinNoise48.getValue((double)i14, (double)i12) > 12.0D;
				i19 = ((i54 = i33[i14 + i12 * i36]) * levelGen6.height + i12) * levelGen6.width + i14;
				if(((i20 = levelGen6.blocks[((i54 + 1) * levelGen6.height + i12) * levelGen6.width + i14] & 255) == Tile.water.id || i20 == Tile.calmWater.id) && i54 <= i42 / 2 - 1 && z53) {
					levelGen6.blocks[i19] = (byte)Tile.gravel.id;
				}

				if(i20 == 0) {
					i57 = Tile.grass.id;
					if(i54 <= i42 / 2 - 1 && z52) {
						i57 = Tile.sand.id;
					}

					levelGen6.blocks[i19] = (byte)i57;
				}
			}
		}

		this.levelLoaderListener.levelLoadUpdate("Planting..");
		i33 = i10;
		levelGen6 = this;
		i36 = this.width;
		i40 = this.width * this.height / 3000;

		for(i42 = 0; i42 < i40; ++i42) {
			i44 = levelGen6.random.nextInt(2);
			levelGen6.setNextPhase(i42 * 50 / (i40 - 1));
			i46 = levelGen6.random.nextInt(levelGen6.width);
			i14 = levelGen6.random.nextInt(levelGen6.height);

			for(i12 = 0; i12 < 10; ++i12) {
				i49 = i46;
				i17 = i14;

				for(i54 = 0; i54 < 5; ++i54) {
					i49 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					i17 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					if((i44 < 2 || levelGen6.random.nextInt(4) == 0) && i49 >= 0 && i17 >= 0 && i49 < levelGen6.width && i17 < levelGen6.height) {
						i19 = i33[i49 + i17 * i36] + 1;
						if((levelGen6.blocks[(i19 * levelGen6.height + i17) * levelGen6.width + i49] & 255) == 0) {
							i57 = (i19 * levelGen6.height + i17) * levelGen6.width + i49;
							if((levelGen6.blocks[((i19 - 1) * levelGen6.height + i17) * levelGen6.width + i49] & 255) == Tile.grass.id) {
								if(i44 == 0) {
									levelGen6.blocks[i57] = (byte)Tile.flower.id;
								} else if(i44 == 1) {
									levelGen6.blocks[i57] = (byte)Tile.rose.id;
								}
							}
						}
					}
				}
			}
		}

		i33 = i10;
		levelGen6 = this;
		i36 = this.width;
		i42 = this.width * this.height * this.depth / 2000;

		for(i44 = 0; i44 < i42; ++i44) {
			i46 = levelGen6.random.nextInt(2);
			levelGen6.setNextPhase(i44 * 50 / (i42 - 1) + 50);
			i14 = levelGen6.random.nextInt(levelGen6.width);
			i12 = levelGen6.random.nextInt(levelGen6.depth);
			i49 = levelGen6.random.nextInt(levelGen6.height);

			for(i17 = 0; i17 < 20; ++i17) {
				i54 = i14;
				i19 = i12;
				i20 = i49;

				for(i57 = 0; i57 < 5; ++i57) {
					i54 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					i19 += levelGen6.random.nextInt(2) - levelGen6.random.nextInt(2);
					i20 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					if((i46 < 2 || levelGen6.random.nextInt(4) == 0) && i54 >= 0 && i20 >= 0 && i19 >= 1 && i54 < levelGen6.width && i20 < levelGen6.height && i19 < i33[i54 + i20 * i36] - 1 && (levelGen6.blocks[(i19 * levelGen6.height + i20) * levelGen6.width + i54] & 255) == 0) {
						int i59 = (i19 * levelGen6.height + i20) * levelGen6.width + i54;
						if((levelGen6.blocks[((i19 - 1) * levelGen6.height + i20) * levelGen6.width + i54] & 255) == Tile.rock.id) {
							if(i46 == 0) {
								levelGen6.blocks[i59] = (byte)Tile.mushroom1.id;
							} else if(i46 == 1) {
								levelGen6.blocks[i59] = (byte)Tile.mushroom2.id;
							}
						}
					}
				}
			}
		}

		Level level32;
		(level32 = new Level()).waterLevel = this.waterLevel;
		level32.setData(width, 64, height, this.blocks);
		level32.createTime = System.currentTimeMillis();
		level32.creator = creator;
		level32.name = "A Nice World";
		int[] i43 = i10;
		Level level41 = level32;
		levelGen6 = this;
		i40 = this.width;
		i42 = this.width * this.height / 4000;

		for(i44 = 0; i44 < i42; ++i44) {
			levelGen6.setNextPhase(i44 * 50 / (i42 - 1) + 50);
			i46 = levelGen6.random.nextInt(levelGen6.width);
			i14 = levelGen6.random.nextInt(levelGen6.height);

			for(i12 = 0; i12 < 20; ++i12) {
				i49 = i46;
				i17 = i14;

				for(i54 = 0; i54 < 20; ++i54) {
					i49 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					i17 += levelGen6.random.nextInt(6) - levelGen6.random.nextInt(6);
					if(i49 >= 0 && i17 >= 0 && i49 < levelGen6.width && i17 < levelGen6.height) {
						i19 = i43[i49 + i17 * i40] + 1;
						if(levelGen6.random.nextInt(4) == 0) {
							level41.maybeGrowTree(i49, i19, i17);
						}
					}
				}
			}
		}

		return level32;
	}

	private void addOre(int tile, int rarity, int min, int max) {
		byte b25 = (byte)tile;
		max = this.width;
		int i5 = this.height;
		int i6 = this.depth;
		int i7 = max * i5 * i6 / 256 / 64 * rarity / 100;

		for(int i8 = 0; i8 < i7; ++i8) {
			this.setNextPhase(i8 * 100 / (i7 - 1) / 4 + min * 100 / 4);
			float f9 = this.random.nextFloat() * (float)max;
			float f10 = this.random.nextFloat() * (float)i6;
			float f11 = this.random.nextFloat() * (float)i5;
			int i12 = (int)((this.random.nextFloat() + this.random.nextFloat()) * 75.0F * (float)rarity / 100.0F);
			float f13 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float f14 = 0.0F;
			float f15 = this.random.nextFloat() * (float)Math.PI * 2.0F;
			float f16 = 0.0F;

			for(int i17 = 0; i17 < i12; ++i17) {
				f9 += Mth.sin(f13) * Mth.cos(f15);
				f11 += Mth.cos(f13) * Mth.cos(f15);
				f10 += Mth.sin(f15);
				f13 += f14 * 0.2F;
				f14 = (f14 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				f15 = (f15 + f16 * 0.5F) * 0.5F;
				f16 = (f16 *= 0.9F) + (this.random.nextFloat() - this.random.nextFloat());
				float f18 = Mth.sin((float)i17 * (float)Math.PI / (float)i12) * (float)rarity / 100.0F + 1.0F;

				for(int i19 = (int)(f9 - f18); i19 <= (int)(f9 + f18); ++i19) {
					for(int i20 = (int)(f10 - f18); i20 <= (int)(f10 + f18); ++i20) {
						for(int i21 = (int)(f11 - f18); i21 <= (int)(f11 + f18); ++i21) {
							float f22 = (float)i19 - f9;
							float f23 = (float)i20 - f10;
							float f24 = (float)i21 - f11;
							if(f22 * f22 + f23 * f23 * 2.0F + f24 * f24 < f18 * f18 && i19 >= 1 && i20 >= 1 && i21 >= 1 && i19 < this.width - 1 && i20 < this.depth - 1 && i21 < this.height - 1) {
								int i26 = (i20 * this.height + i21) * this.width + i19;
								if(this.blocks[i26] == Tile.rock.id) {
									this.blocks[i26] = b25;
								}
							}
						}
					}
				}
			}
		}

	}

	private void setNextPhase(int phase) {
		this.levelLoaderListener.setLoadingProgress(phase);
	}

	private long floodFillWithLiquid(int x, int y, int z, int source, int tt) {
		byte b20 = (byte)tt;
		ArrayList arrayList21 = new ArrayList();
		byte b6 = 0;
		int i7 = 1;

		int i8;
		for(i8 = 1; 1 << i7 < this.width; ++i7) {
		}

		while(1 << i8 < this.height) {
			++i8;
		}

		int i9 = this.height - 1;
		int i10 = this.width - 1;
		int i22 = b6 + 1;
		this.coords[0] = ((y << i8) + z << i7) + x;
		long j13 = 0L;
		x = this.width * this.height;

		while(i22 > 0) {
			--i22;
			y = this.coords[i22];
			if(i22 == 0 && arrayList21.size() > 0) {
				this.coords = (int[])arrayList21.remove(arrayList21.size() - 1);
				i22 = this.coords.length;
			}

			z = y >> i7 & i9;
			int i11 = y >> i7 + i8;

			int i12;
			int i15;
			for(i15 = i12 = y & i10; i12 > 0 && this.blocks[y - 1] == 0; --y) {
				--i12;
			}

			while(i15 < this.width && this.blocks[y + i15 - i12] == 0) {
				++i15;
			}

			int i16 = y >> i7 & i9;
			int i17 = y >> i7 + i8;
			if(i16 != z || i17 != i11) {
				System.out.println("Diagonal flood!?");
			}

			boolean z23 = false;
			boolean z24 = false;
			boolean z18 = false;
			j13 += (long)(i15 - i12);

			for(i12 = i12; i12 < i15; ++i12) {
				this.blocks[y] = b20;
				boolean z19;
				if(z > 0) {
					if((z19 = this.blocks[y - this.width] == 0) && !z23) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - this.width;
					}

					z23 = z19;
				}

				if(z < this.height - 1) {
					if((z19 = this.blocks[y + this.width] == 0) && !z24) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y + this.width;
					}

					z24 = z19;
				}

				if(i11 > 0) {
					byte b25 = this.blocks[y - x];
					if((b20 == Tile.lava.id || b20 == Tile.calmLava.id) && (b25 == Tile.water.id || b25 == Tile.calmWater.id)) {
						this.blocks[y - x] = (byte)Tile.rock.id;
					}

					if((z19 = b25 == 0) && !z18) {
						if(i22 == this.coords.length) {
							arrayList21.add(this.coords);
							this.coords = new int[1048576];
							i22 = 0;
						}

						this.coords[i22++] = y - x;
					}

					z18 = z19;
				}

				++y;
			}
		}

		return j13;
	}
}