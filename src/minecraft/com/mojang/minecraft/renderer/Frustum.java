package com.mojang.minecraft.renderer;

/**
 * frustumCuller?
 */
public class Frustum {
	public float[][] m_Frustum = new float[16][16];
	public float[] proj = new float[16];
	public float[] modl = new float[16];
	public float[] clip = new float[16];

	public final boolean cubeInFrustum(float x1, float y1, float z1, float x2, float y2, float z2) {
		for(int i7 = 0; i7 < 6; ++i7) {
			if(this.m_Frustum[i7][0] * x1 + this.m_Frustum[i7][1] * y1 + this.m_Frustum[i7][2] * z1 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x2 + this.m_Frustum[i7][1] * y1 + this.m_Frustum[i7][2] * z1 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x1 + this.m_Frustum[i7][1] * y2 + this.m_Frustum[i7][2] * z1 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x2 + this.m_Frustum[i7][1] * y2 + this.m_Frustum[i7][2] * z1 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x1 + this.m_Frustum[i7][1] * y1 + this.m_Frustum[i7][2] * z2 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x2 + this.m_Frustum[i7][1] * y1 + this.m_Frustum[i7][2] * z2 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x1 + this.m_Frustum[i7][1] * y2 + this.m_Frustum[i7][2] * z2 + this.m_Frustum[i7][3] <= 0.0F && this.m_Frustum[i7][0] * x2 + this.m_Frustum[i7][1] * y2 + this.m_Frustum[i7][2] * z2 + this.m_Frustum[i7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
}