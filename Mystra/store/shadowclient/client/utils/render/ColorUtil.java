package store.shadowclient.client.utils.render;

import java.awt.Color;

import store.shadowclient.client.utils.player.MathUtil;

public class ColorUtil {

    public Color fade(Color color) {
        return fade(color, 2, 100);
    }
    
    public Color fade(Color color, int index, int count) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }
    
    public final static Color setAlpha(final int alpha, final Color color) {
		final int newAlpha = MathUtil.clamp(alpha, 0, 255);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
	}
	
}
