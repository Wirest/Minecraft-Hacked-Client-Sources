package me.existdev.exist.utils;

import java.awt.Color;
import me.existdev.exist.Exist;
import me.existdev.exist.module.modules.render.HUD;

public class ColorUtils {
   // $FF: synthetic field
   static int startA = 20;

   // $FF: synthetic method
   public static final Color getClientColor() {
      return new Color((int)Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Red").getCurrentValue(), (int)Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Green").getCurrentValue(), (int)Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Blue").getCurrentValue());
   }

   // $FF: synthetic method
   public static int SmoothDownAlpha(int speed, int oldA) {
      startA -= speed;
      if(startA < oldA) {
         startA = oldA;
      }

      return startA;
   }

   // $FF: synthetic method
   public static int SmoothUpAlpha(int speed, int finalA) {
      startA += speed;
      if(startA > finalA) {
         startA = finalA;
      }

      return startA;
   }

   // $FF: synthetic method
   public static int SmoothUpAlpha(int speed) {
      startA += speed;
      if(startA > 150) {
         startA = 150;
      }

      return startA;
   }

   // $FF: synthetic method
   public static int getRainbow(int speed, int offset) {
      float hue = (float)((System.currentTimeMillis() + (long)offset) % (long)speed);
      hue /= (float)speed;
      return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
   }

   // $FF: synthetic method
   public static int transparency(int color, double alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, (float)alpha)).getRGB();
   }

   // $FF: synthetic method
   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      Color color = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
      return color;
   }
}
