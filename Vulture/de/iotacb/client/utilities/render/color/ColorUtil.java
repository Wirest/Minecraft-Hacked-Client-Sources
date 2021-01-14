package de.iotacb.client.utilities.render.color;

import java.awt.Color;

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
	
}
