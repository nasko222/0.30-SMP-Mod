package util;

public final class Mth {
	private static float[] SINE_TABLE = new float[65536];

	public static final float sin(float f) {
		return SINE_TABLE[(int)(f * 10430.378F) & 65535];
	}

	public static final float cos(float f) {
		return SINE_TABLE[(int)(f * 10430.378F + 16384.0F) & 65535];
	}

	public static final float sqrt(float f) {
		return (float)Math.sqrt((double)f);
	}

	static {
		for(int i0 = 0; i0 < 65536; ++i0) {
			SINE_TABLE[i0] = (float)Math.sin((double)i0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}