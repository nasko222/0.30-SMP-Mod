package com.mojang.minecraft.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import util.Mth;

public final class FrustumCuller extends Frustum {
	private static FrustumCuller frustum = new FrustumCuller();
	private FloatBuffer _proj = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _modl = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _clip = BufferUtils.createFloatBuffer(16);

	public static Frustum calculateFrustum() {
		FrustumCuller frustumCuller0 = frustum;
		frustum._proj.clear();
		frustumCuller0._modl.clear();
		frustumCuller0._clip.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, frustumCuller0._proj);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, frustumCuller0._modl);
		frustumCuller0._proj.flip().limit(16);
		frustumCuller0._proj.get(frustumCuller0.proj);
		frustumCuller0._modl.flip().limit(16);
		frustumCuller0._modl.get(frustumCuller0.modl);
		frustumCuller0.clip[0] = frustumCuller0.modl[0] * frustumCuller0.proj[0] + frustumCuller0.modl[1] * frustumCuller0.proj[4] + frustumCuller0.modl[2] * frustumCuller0.proj[8] + frustumCuller0.modl[3] * frustumCuller0.proj[12];
		frustumCuller0.clip[1] = frustumCuller0.modl[0] * frustumCuller0.proj[1] + frustumCuller0.modl[1] * frustumCuller0.proj[5] + frustumCuller0.modl[2] * frustumCuller0.proj[9] + frustumCuller0.modl[3] * frustumCuller0.proj[13];
		frustumCuller0.clip[2] = frustumCuller0.modl[0] * frustumCuller0.proj[2] + frustumCuller0.modl[1] * frustumCuller0.proj[6] + frustumCuller0.modl[2] * frustumCuller0.proj[10] + frustumCuller0.modl[3] * frustumCuller0.proj[14];
		frustumCuller0.clip[3] = frustumCuller0.modl[0] * frustumCuller0.proj[3] + frustumCuller0.modl[1] * frustumCuller0.proj[7] + frustumCuller0.modl[2] * frustumCuller0.proj[11] + frustumCuller0.modl[3] * frustumCuller0.proj[15];
		frustumCuller0.clip[4] = frustumCuller0.modl[4] * frustumCuller0.proj[0] + frustumCuller0.modl[5] * frustumCuller0.proj[4] + frustumCuller0.modl[6] * frustumCuller0.proj[8] + frustumCuller0.modl[7] * frustumCuller0.proj[12];
		frustumCuller0.clip[5] = frustumCuller0.modl[4] * frustumCuller0.proj[1] + frustumCuller0.modl[5] * frustumCuller0.proj[5] + frustumCuller0.modl[6] * frustumCuller0.proj[9] + frustumCuller0.modl[7] * frustumCuller0.proj[13];
		frustumCuller0.clip[6] = frustumCuller0.modl[4] * frustumCuller0.proj[2] + frustumCuller0.modl[5] * frustumCuller0.proj[6] + frustumCuller0.modl[6] * frustumCuller0.proj[10] + frustumCuller0.modl[7] * frustumCuller0.proj[14];
		frustumCuller0.clip[7] = frustumCuller0.modl[4] * frustumCuller0.proj[3] + frustumCuller0.modl[5] * frustumCuller0.proj[7] + frustumCuller0.modl[6] * frustumCuller0.proj[11] + frustumCuller0.modl[7] * frustumCuller0.proj[15];
		frustumCuller0.clip[8] = frustumCuller0.modl[8] * frustumCuller0.proj[0] + frustumCuller0.modl[9] * frustumCuller0.proj[4] + frustumCuller0.modl[10] * frustumCuller0.proj[8] + frustumCuller0.modl[11] * frustumCuller0.proj[12];
		frustumCuller0.clip[9] = frustumCuller0.modl[8] * frustumCuller0.proj[1] + frustumCuller0.modl[9] * frustumCuller0.proj[5] + frustumCuller0.modl[10] * frustumCuller0.proj[9] + frustumCuller0.modl[11] * frustumCuller0.proj[13];
		frustumCuller0.clip[10] = frustumCuller0.modl[8] * frustumCuller0.proj[2] + frustumCuller0.modl[9] * frustumCuller0.proj[6] + frustumCuller0.modl[10] * frustumCuller0.proj[10] + frustumCuller0.modl[11] * frustumCuller0.proj[14];
		frustumCuller0.clip[11] = frustumCuller0.modl[8] * frustumCuller0.proj[3] + frustumCuller0.modl[9] * frustumCuller0.proj[7] + frustumCuller0.modl[10] * frustumCuller0.proj[11] + frustumCuller0.modl[11] * frustumCuller0.proj[15];
		frustumCuller0.clip[12] = frustumCuller0.modl[12] * frustumCuller0.proj[0] + frustumCuller0.modl[13] * frustumCuller0.proj[4] + frustumCuller0.modl[14] * frustumCuller0.proj[8] + frustumCuller0.modl[15] * frustumCuller0.proj[12];
		frustumCuller0.clip[13] = frustumCuller0.modl[12] * frustumCuller0.proj[1] + frustumCuller0.modl[13] * frustumCuller0.proj[5] + frustumCuller0.modl[14] * frustumCuller0.proj[9] + frustumCuller0.modl[15] * frustumCuller0.proj[13];
		frustumCuller0.clip[14] = frustumCuller0.modl[12] * frustumCuller0.proj[2] + frustumCuller0.modl[13] * frustumCuller0.proj[6] + frustumCuller0.modl[14] * frustumCuller0.proj[10] + frustumCuller0.modl[15] * frustumCuller0.proj[14];
		frustumCuller0.clip[15] = frustumCuller0.modl[12] * frustumCuller0.proj[3] + frustumCuller0.modl[13] * frustumCuller0.proj[7] + frustumCuller0.modl[14] * frustumCuller0.proj[11] + frustumCuller0.modl[15] * frustumCuller0.proj[15];
		frustumCuller0.m_Frustum[0][0] = frustumCuller0.clip[3] - frustumCuller0.clip[0];
		frustumCuller0.m_Frustum[0][1] = frustumCuller0.clip[7] - frustumCuller0.clip[4];
		frustumCuller0.m_Frustum[0][2] = frustumCuller0.clip[11] - frustumCuller0.clip[8];
		frustumCuller0.m_Frustum[0][3] = frustumCuller0.clip[15] - frustumCuller0.clip[12];
		normalizePlane(frustumCuller0.m_Frustum, 0);
		frustumCuller0.m_Frustum[1][0] = frustumCuller0.clip[3] + frustumCuller0.clip[0];
		frustumCuller0.m_Frustum[1][1] = frustumCuller0.clip[7] + frustumCuller0.clip[4];
		frustumCuller0.m_Frustum[1][2] = frustumCuller0.clip[11] + frustumCuller0.clip[8];
		frustumCuller0.m_Frustum[1][3] = frustumCuller0.clip[15] + frustumCuller0.clip[12];
		normalizePlane(frustumCuller0.m_Frustum, 1);
		frustumCuller0.m_Frustum[2][0] = frustumCuller0.clip[3] + frustumCuller0.clip[1];
		frustumCuller0.m_Frustum[2][1] = frustumCuller0.clip[7] + frustumCuller0.clip[5];
		frustumCuller0.m_Frustum[2][2] = frustumCuller0.clip[11] + frustumCuller0.clip[9];
		frustumCuller0.m_Frustum[2][3] = frustumCuller0.clip[15] + frustumCuller0.clip[13];
		normalizePlane(frustumCuller0.m_Frustum, 2);
		frustumCuller0.m_Frustum[3][0] = frustumCuller0.clip[3] - frustumCuller0.clip[1];
		frustumCuller0.m_Frustum[3][1] = frustumCuller0.clip[7] - frustumCuller0.clip[5];
		frustumCuller0.m_Frustum[3][2] = frustumCuller0.clip[11] - frustumCuller0.clip[9];
		frustumCuller0.m_Frustum[3][3] = frustumCuller0.clip[15] - frustumCuller0.clip[13];
		normalizePlane(frustumCuller0.m_Frustum, 3);
		frustumCuller0.m_Frustum[4][0] = frustumCuller0.clip[3] - frustumCuller0.clip[2];
		frustumCuller0.m_Frustum[4][1] = frustumCuller0.clip[7] - frustumCuller0.clip[6];
		frustumCuller0.m_Frustum[4][2] = frustumCuller0.clip[11] - frustumCuller0.clip[10];
		frustumCuller0.m_Frustum[4][3] = frustumCuller0.clip[15] - frustumCuller0.clip[14];
		normalizePlane(frustumCuller0.m_Frustum, 4);
		frustumCuller0.m_Frustum[5][0] = frustumCuller0.clip[3] + frustumCuller0.clip[2];
		frustumCuller0.m_Frustum[5][1] = frustumCuller0.clip[7] + frustumCuller0.clip[6];
		frustumCuller0.m_Frustum[5][2] = frustumCuller0.clip[11] + frustumCuller0.clip[10];
		frustumCuller0.m_Frustum[5][3] = frustumCuller0.clip[15] + frustumCuller0.clip[14];
		normalizePlane(frustumCuller0.m_Frustum, 5);
		return frustum;
	}

	private static void normalizePlane(float[][] frustum, int side) {
		float f2 = Mth.sqrt(frustum[side][0] * frustum[side][0] + frustum[side][1] * frustum[side][1] + frustum[side][2] * frustum[side][2]);
		frustum[side][0] /= f2;
		frustum[side][1] /= f2;
		frustum[side][2] /= f2;
		frustum[side][3] /= f2;
	}
}