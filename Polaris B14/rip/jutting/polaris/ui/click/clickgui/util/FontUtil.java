/*    */ package rip.jutting.polaris.ui.click.clickgui.util;
/*    */ 
/*    */ import net.minecraft.util.StringUtils;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FontUtil
/*    */ {
/* 15 */   private static CFontRenderer font = FontLoaders.vardana12;
/*    */   
/*    */   public static void setupFontUtils()
/*    */   {
/* 19 */     font = FontLoaders.vardana12;
/*    */   }
/*    */   
/*    */   public static int getStringWidth(String text) {
/* 23 */     return font.getStringWidth(StringUtils.stripControlCodes(text));
/*    */   }
/*    */   
/*    */   public static int getFontHeight() {
/* 27 */     return font.getHeight();
/*    */   }
/*    */   
/*    */   public static void drawString(String text, double x, double y, int color, boolean shadow) {
/* 31 */     font.drawString(text, x, y, color, shadow);
/*    */   }
/*    */   
/*    */   public static void drawStringWithShadow(String text, double x, double y, int color) {
/* 35 */     font.drawStringWithShadow(text, (float)x, (float)y, color);
/*    */   }
/*    */   
/*    */   public static void drawCenteredString(String text, double x, double y, int color) {
/* 39 */     drawString(text, x - font.getStringWidth(text) / 2, y, color, true);
/*    */   }
/*    */   
/*    */   public static void drawCenteredStringWithShadow(String text, double x, double y, int color) {
/* 43 */     drawStringWithShadow(text, x - font.getStringWidth(text) / 2, y, color);
/*    */   }
/*    */   
/*    */   public static void drawTotalCenteredString(String text, double x, double y, int color) {
/* 47 */     drawString(text, x - font.getStringWidth(text) / 2, y - font.getHeight() / 2, color, true);
/*    */   }
/*    */   
/*    */   public static void drawTotalCenteredStringWithShadow(String text, double x, double y, int color) {
/* 51 */     drawStringWithShadow(text, x - font.getStringWidth(text) / 2, y - font.getHeight() / 2.0F, color);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\util\FontUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */