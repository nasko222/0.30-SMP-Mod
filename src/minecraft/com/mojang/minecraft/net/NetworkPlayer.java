package com.mojang.minecraft.net;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.mob.HumanoidMob;
import com.mojang.minecraft.renderer.Textures;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class NetworkPlayer extends HumanoidMob {
	public static final long serialVersionUID = 77479605454997290L;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private transient int texture = -1;
	public transient BufferedImage newTexture = null;
	public String name;
	public String displayName;
	int tickCount = 0;
	private Textures textures;

	public NetworkPlayer(Minecraft minecraft, int id, String username, int x, int y, int z, float yRot, float xRot) {
		super(minecraft.level, (float)x, (float)y, (float)z);
		this.minecraft = minecraft;
		this.displayName = username;
		username = Font.removeColorCodes(username);
		this.name = username;
		this.xp = x;
		this.yp = y;
		this.zp = z;
		this.heightOffset = 0.0F;
		this.pushthrough = 0.8F;
		this.setPos((float)x / 32.0F, (float)y / 32.0F, (float)z / 32.0F);
		this.xRot = xRot;
		this.yRot = yRot;
		this.armor = this.helmet = false;
		this.renderOffset = 0.6875F;
		(new NetworkPlayerTextureLoader(this)).start();
		this.allowAlpha = false;
	}
	
	public void hurt(Entity entity1, int i2) {
		return;
	}

	public void aiStep() {
		int i1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((EntityPos)this.moveQueue.remove(0));
			}
		} while(i1-- > 0 && this.moveQueue.size() > 10);

		this.onGround = true;
	}

	public void bindTexture(Textures textures) {
		this.textures = textures;
		if(this.newTexture != null) {
			BufferedImage bufferedImage2 = this.newTexture;
			int[] i3 = new int[512];
			bufferedImage2.getRGB(32, 0, 32, 16, i3, 0, 32);
			int i5 = 0;

			boolean z10001;
			while(true) {
				if(i5 >= i3.length) {
					z10001 = false;
					break;
				}

				if(i3[i5] >>> 24 < 128) {
					z10001 = true;
					break;
				}

				++i5;
			}

			this.hasHair = z10001;
			this.texture = textures.loadTexture(this.newTexture);
			this.newTexture = null;
		}

		if(this.texture < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.loadTexture("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);
		}
	}

	public void renderHover(Textures textures1, float f2) {
		Font font3 = this.minecraft.font;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xo + (this.x - this.xo) * f2, this.yo + (this.y - this.yo) * f2 + 0.8F + this.renderOffset, this.zo + (this.z - this.zo) * f2);
		GL11.glRotatef(-this.minecraft.player.yRot, 0.0F, 1.0F, 0.0F);
		f2 = 0.05F;
		GL11.glScalef(0.05F, -f2, f2);
		GL11.glTranslatef((float)(-font3.width(this.displayName)) / 2.0F, 0.0F, 0.0F);
		GL11.glNormal3f(1.0F, -1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		if(this.name.equalsIgnoreCase("nasko222")) {
			font3.draw(this.displayName, 0, 0, 16776960);
		} else {
			font3.draw(this.displayName, 0, 0, 0xFFFFFF);
		}

		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font3.draw(this.displayName, 0, 0, 0xFFFFFF);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		font3.draw(this.name, 0, 0, 5263440);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public void queue(byte xa, byte ya, byte za, float xr, float yr) {
		float f6 = xr - this.yRot;

		float f7;
		for(f7 = yr - this.xRot; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.yRot + f6 * 0.5F;
		f7 = this.xRot + f7 * 0.5F;
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)xa / 2.0F) / 32.0F, ((float)this.yp + (float)ya / 2.0F) / 32.0F, ((float)this.zp + (float)za / 2.0F) / 32.0F, f6, f7));
		this.xp += xa;
		this.yp += ya;
		this.zp += za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void teleport(short xa, short ya, short za, float xr, float yr) {
		float f6 = xr - this.yRot;

		float f7;
		for(f7 = yr - this.xRot; f6 >= 180.0F; f6 -= 360.0F) {
		}

		while(f6 < -180.0F) {
			f6 += 360.0F;
		}

		while(f7 >= 180.0F) {
			f7 -= 360.0F;
		}

		while(f7 < -180.0F) {
			f7 += 360.0F;
		}

		f6 = this.yRot + f6 * 0.5F;
		f7 = this.xRot + f7 * 0.5F;
		this.moveQueue.add(new EntityPos((float)(this.xp + xa) / 64.0F, (float)(this.yp + ya) / 64.0F, (float)(this.zp + za) / 64.0F, f6, f7));
		this.xp = xa;
		this.yp = ya;
		this.zp = za;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, xr, yr));
	}

	public void queue(byte x, byte y, byte z) {
		this.moveQueue.add(new EntityPos(((float)this.xp + (float)x / 2.0F) / 32.0F, ((float)this.yp + (float)y / 2.0F) / 32.0F, ((float)this.zp + (float)z / 2.0F) / 32.0F));
		this.xp += x;
		this.yp += y;
		this.zp += z;
		this.moveQueue.add(new EntityPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float xr, float yr) {
		float f3 = xr - this.yRot;

		float f4;
		for(f4 = yr - this.xRot; f3 >= 180.0F; f3 -= 360.0F) {
		}

		while(f3 < -180.0F) {
			f3 += 360.0F;
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		while(f4 < -180.0F) {
			f4 += 360.0F;
		}

		f3 = this.yRot + f3 * 0.5F;
		f4 = this.xRot + f4 * 0.5F;
		this.moveQueue.add(new EntityPos(f3, f4));
		this.moveQueue.add(new EntityPos(xr, yr));
	}

	public void clear() {
		if(this.texture >= 0 && this.textures != null) {
			Textures textures10000 = this.textures;
			int i1 = this.texture;
			Textures textures2 = this.textures;
			textures10000.pixelsMap.remove(i1);
			textures2.ib.clear();
			textures2.ib.put(i1);
			textures2.ib.flip();
			GL11.glDeleteTextures(textures2.ib);
		}

	}
}