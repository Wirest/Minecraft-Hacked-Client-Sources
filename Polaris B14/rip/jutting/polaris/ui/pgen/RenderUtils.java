/*    */ package rip.jutting.polaris.ui.pgen;
/*    */ 
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderUtils
/*    */ {
/*    */   public static void drawBorderedCircle(int x, int y, float radius, int outsideC, int insideC)
/*    */   {
/* 13 */     GL11.glEnable(3042);
/* 14 */     GL11.glDisable(3553);
/* 15 */     GL11.glBlendFunc(770, 771);
/* 16 */     GL11.glEnable(2848);
/* 17 */     GL11.glPushMatrix();
/* 18 */     float scale = 0.1F;
/* 19 */     GL11.glScalef(scale, scale, scale);
/* 20 */     x = (int)(x * (1.0F / scale));
/* 21 */     y = (int)(y * (1.0F / scale));
/* 22 */     radius *= 1.0F / scale;
/* 23 */     drawCircle(x, y, radius, insideC);
/* 24 */     drawUnfilledCircle(x, y, radius, 1.0F, outsideC);
/* 25 */     GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
/* 26 */     GL11.glPopMatrix();
/* 27 */     GL11.glEnable(3553);
/* 28 */     GL11.glDisable(3042);
/* 29 */     GL11.glDisable(2848);
/*    */   }
/*    */   
/*    */   public static void drawUnfilledCircle(int x, int y, float radius, float lineWidth, int color)
/*    */   {
/* 34 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 35 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 36 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 37 */     float blue = (color & 0xFF) / 255.0F;
/* 38 */     GL11.glColor4f(red, green, blue, alpha);
/* 39 */     GL11.glLineWidth(lineWidth);
/* 40 */     GL11.glEnable(2848);
/* 41 */     GL11.glBegin(2);
/* 42 */     for (int i = 0; i <= 360; i++) {
/* 43 */       GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
/*    */     }
/* 45 */     GL11.glEnd();
/* 46 */     GL11.glDisable(2848);
/*    */   }
/*    */   
/*    */   public static void drawCircle(int x, int y, float radius, int color)
/*    */   {
/* 51 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 52 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 53 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 54 */     float blue = (color & 0xFF) / 255.0F;
/* 55 */     GL11.glColor4f(red, green, blue, alpha);
/* 56 */     GL11.glBegin(9);
/* 57 */     for (int i = 0; i <= 360; i++) {
/* 58 */       GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
/*    */     }
/* 60 */     GL11.glEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\pgen\RenderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */