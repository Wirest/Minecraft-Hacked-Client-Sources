/*    */ package rip.jutting.polaris.ui.protection;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.FontRenderer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringSystem
/*    */ {
/* 12 */   public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
/*    */   
/*    */   public static void drawStringWithShadow(String text, float x2, float y2, int color)
/*    */   {
/* 16 */     fr.drawString(text, x2, y2, color, true);
/*    */   }
/*    */   
/*    */   public static void drawSmallString(String s, int x2, int y2, int color) {
/* 20 */     GL11.glPushMatrix();
/* 21 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 22 */     Minecraft.getMinecraft().fontRendererObj.drawString(s, x2 * 2, y2 * 2, color);
/* 23 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */   public static void drawLargeString(String text, int x2, int y2, int color) {
/* 27 */     GL11.glPushMatrix();
/* 28 */     GL11.glScalef(1.5F, 1.5F, 1.5F);
/* 29 */     Minecraft.getMinecraft().fontRendererObj.drawString(text, x2 *= 2, y2, color);
/* 30 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */   public static String Capitalise(String text) {
/* 34 */     return String.valueOf(text.substring(0, 1).toUpperCase()) + text.substring(2, text.length()).toLowerCase();
/*    */   }
/*    */   
/*    */   public static void drawCentredStringWithShadow(String text, int x, int y, int colour) {
/* 38 */     x -= fr.getStringWidth(text) / 2;
/* 39 */     fr.drawString(text, x, y, colour, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\protection\StringSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */