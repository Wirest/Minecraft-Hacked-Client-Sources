package org.m0jang.crystal.Utils;

import java.awt.Color;

public class HudColorUtils {
   public static int getRainbow(int speed, int offset) {
      float hue = (float)((System.currentTimeMillis() + (long)offset) % (long)speed);
      hue /= (float)speed;
      return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
   }
}
