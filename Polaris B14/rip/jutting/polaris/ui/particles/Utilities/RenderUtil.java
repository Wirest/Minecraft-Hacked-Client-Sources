/*    */ package rip.jutting.polaris.ui.particles.Utilities;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderUtil
/*    */ {
/*    */   public static void drawCircle(int x, int y, float radius, int color)
/*    */   {
/* 15 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/* 16 */     float red = (color >> 16 & 0xFF) / 255.0F;
/* 17 */     float green = (color >> 8 & 0xFF) / 255.0F;
/* 18 */     float blue = (color & 0xFF) / 255.0F;
/*    */     
/* 20 */     GL11.glColor4f(red, green, blue, alpha);
/* 21 */     GL11.glEnable(3042);
/* 22 */     GL11.glDisable(3553);
/* 23 */     GL11.glBlendFunc(770, 771);
/* 24 */     GL11.glEnable(2848);
/* 25 */     GL11.glPushMatrix();
/* 26 */     GL11.glLineWidth(1.0F);
/* 27 */     GL11.glBegin(9);
/* 28 */     for (int i = 0; i <= 360; i++)
/* 29 */       GL11.glVertex2d(x + Math.sin(i * 3.141592653589793D / 180.0D) * radius, y + Math.cos(i * 3.141592653589793D / 180.0D) * radius);
/* 30 */     GL11.glEnd();
/* 31 */     GL11.glPopMatrix();
/* 32 */     GL11.glEnable(3553);
/* 33 */     GL11.glDisable(3042);
/* 34 */     GL11.glDisable(2848);
/* 35 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static int getRainbow(int speed, int offset) {
/* 39 */     float hue = (float)((System.currentTimeMillis() + offset) % speed);
/* 40 */     return Color.getHSBColor(hue / speed, 1.0F, 1.0F).getRGB();
/*    */   }
/*    */   
/*    */   public static void connectPoints(int xOne, int yOne, int xTwo, int yTwo) {
/* 44 */     GL11.glPushMatrix();
/* 45 */     GL11.glEnable(2848);
/* 46 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
/* 47 */     GL11.glDisable(3553);
/* 48 */     GL11.glBlendFunc(770, 771);
/* 49 */     GL11.glEnable(3042);
/* 50 */     GL11.glLineWidth(0.5F);
/* 51 */     GL11.glBegin(1);
/* 52 */     GL11.glVertex2i(xOne, yOne);
/* 53 */     GL11.glVertex2i(xTwo, yTwo);
/* 54 */     GL11.glEnd();
/* 55 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 56 */     GL11.glDisable(3042);
/* 57 */     GL11.glDisable(2848);
/* 58 */     GL11.glEnable(3553);
/* 59 */     GL11.glPopMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\particles\Utilities\RenderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */