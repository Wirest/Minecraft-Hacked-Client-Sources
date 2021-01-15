/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RColorUtil
/*    */ {
/* 12 */   public static final int[] RAINBOW_COLORS = { 16711680, 16728064, 16744192, 16776960, 8453888, 65280, 65535, 33023, 255 };
/* 13 */   private static Random random = new Random();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int generateColor()
/*    */   {
/* 21 */     float hue = random.nextFloat();
/* 22 */     float saturation = random.nextInt(5000) / 10000.0F + 0.5F;
/* 23 */     float brightness = random.nextInt(5000) / 10000.0F + 0.5F;
/* 24 */     return Color.HSBtoRGB(hue, saturation, brightness);
/*    */   }
/*    */   
/*    */   public static Color blend(Color color1, Color color2, float ratio) {
/* 28 */     if (ratio < 0.0F) {
/* 29 */       return color2;
/*    */     }
/* 31 */     if (ratio > 1.0F) {
/* 32 */       return color1;
/*    */     }
/* 34 */     float ratio2 = 1.0F - ratio;
/* 35 */     float[] rgb1 = new float[3];
/* 36 */     float[] rgb2 = new float[3];
/* 37 */     color1.getColorComponents(rgb1);
/* 38 */     color2.getColorComponents(rgb2);
/* 39 */     Color color3 = new Color(rgb1[0] * ratio + rgb2[0] * ratio2, rgb1[1] * ratio + rgb2[1] * ratio2, rgb1[2] * ratio + rgb2[2] * ratio2);
/* 40 */     return color3;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\RColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */