package cheatware.utils;

import java.awt.Color;

public class RainbowUtil {
	
	public static int rainbow() {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + 0) / 12);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 370.0f), 0.8f, 1f).getRGB();
	}
}
