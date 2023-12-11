package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.Vec3;
import com.mojang.minecraft.player.Player;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import util.Mth;

public final class GameRenderer {
	public Minecraft minecraft;
	public float fogColorMultiplier = 1.0F;
	public boolean displayActive = false;
	public float renderDistance = 0.0F;
	public TileRenderer tileRenderer;
	public int rainTicks;
	public Entity entity = null;
	public Random random = new Random();
	private volatile int u1 = 0;
	private volatile int u2 = 0;
	private FloatBuffer lb = BufferUtils.createFloatBuffer(16);
	public float fogRed;
	public float fogGreen;
	public float fogBlue;

	public GameRenderer(Minecraft minecraft) {
		this.minecraft = minecraft;
		this.tileRenderer = new TileRenderer(minecraft);
	}

	public Vec3 getPlayerRotVec(float rot) {
		Player player4;
		float f2 = (player4 = this.minecraft.player).xo + (player4.x - player4.xo) * rot;
		float f3 = player4.yo + (player4.y - player4.yo) * rot;
		float f5 = player4.zo + (player4.z - player4.zo) * rot;
		return new Vec3(f2, f3, f5);
	}

	public void renderHurtFrames(float a) {
		Player player3;
		float f2 = (float)(player3 = this.minecraft.player).hurtTime - a;
		if(player3.health <= 0) {
			a += (float)player3.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (a + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f2 >= 0.0F) {
			f2 = Mth.sin((f2 /= (float)player3.hurtDuration) * f2 * f2 * f2 * (float)Math.PI);
			a = player3.hurtDir;
			GL11.glRotatef(-player3.hurtDir, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f2 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(a, 0.0F, 1.0F, 0.0F);
		}
	}

	public void cameraBob(float a) {
		Player player4;
		float f2 = (player4 = this.minecraft.player).walkDist - player4.walkDistO;
		f2 = player4.walkDist + f2 * a;
		float f3 = player4.oBob + (player4.bob - player4.oBob) * a;
		float f5 = player4.oTilt + (player4.tilt - player4.oTilt) * a;
		GL11.glTranslatef(Mth.sin(f2 * (float)Math.PI) * f3 * 0.5F, -Math.abs(Mth.cos(f2 * (float)Math.PI) * f3), 0.0F);
		GL11.glRotatef(Mth.sin(f2 * (float)Math.PI) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(Math.abs(Mth.cos(f2 * (float)Math.PI + 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f5, 1.0F, 0.0F, 0.0F);
	}

	public final void toggleLight(boolean light) {
		if(!light) {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LIGHT0);
		} else {
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_LIGHT0);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
			float light1 = 0.7F;
			float f2 = 0.3F;
			Vec3 vec33 = (new Vec3(0.0F, -1.0F, 0.5F)).normalize();
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, this.getBuffer(vec33.x, vec33.y, vec33.z, 0.0F));
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, this.getBuffer(f2, f2, f2, 1.0F));
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(light1, light1, light1, 1.0F));
		}
	}

	public final void render() {
		int i1 = this.minecraft.width * 240 / this.minecraft.height;
		int i2 = this.minecraft.height * 240 / this.minecraft.height;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)i1, (double)i2, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
	}

	public void setupFog() {
		Level level1 = this.minecraft.level;
		Player player2 = this.minecraft.player;
		GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogRed, this.fogGreen, this.fogBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tile tile5;
		if((tile5 = Tile.tiles[level1.getTile((int)player2.x, (int)(player2.y + 0.12F), (int)player2.z)]) != null && tile5.getLiquidType() != Liquid.none) {
			Liquid liquid6 = tile5.getLiquidType();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			float f3;
			float f4;
			float f7;
			float f8;
			if(liquid6 == Liquid.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				f7 = 0.4F;
				f8 = 0.4F;
				f3 = 0.9F;
				if(this.minecraft.options.anaglyph3d) {
					f4 = (f7 * 30.0F + f8 * 59.0F + f3 * 11.0F) / 100.0F;
					f8 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
					f3 = (f7 * 30.0F + f3 * 70.0F) / 100.0F;
					f7 = f4;
					f8 = f8;
					f3 = f3;
				}

				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f7, f8, f3, 1.0F));
			} else if(liquid6 == Liquid.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
				f7 = 0.4F;
				f8 = 0.3F;
				f3 = 0.3F;
				if(this.minecraft.options.anaglyph3d) {
					f4 = (f7 * 30.0F + f8 * 59.0F + f3 * 11.0F) / 100.0F;
					f8 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
					f3 = (f7 * 30.0F + f3 * 70.0F) / 100.0F;
					f7 = f4;
					f8 = f8;
					f3 = f3;
				}

				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(f7, f8, f3, 1.0F));
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}

	private FloatBuffer getBuffer(float r, float g, float b, float a) {
		this.lb.clear();
		this.lb.put(r).put(g).put(b).put(a);
		this.lb.flip();
		return this.lb;
	}
}