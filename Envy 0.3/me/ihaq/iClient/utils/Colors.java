package me.ihaq.iClient.utils;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class Colors {

	public static float r = 0;
	public static float g = 1;
	public static float b = 0;
	public static boolean rainbow = false;

	public static Color getRainbow(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 5.0E9F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);

	}

	public static int getColor() {
		return rainbow ? Colors.getRainbow(0L, 1.0F).hashCode() : new Color(r, g, b).hashCode();
	}

}
