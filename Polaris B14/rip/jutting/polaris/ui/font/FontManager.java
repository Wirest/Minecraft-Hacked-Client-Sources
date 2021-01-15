/*    */ package rip.jutting.polaris.ui.font;
/*    */ 
/*    */ import java.awt.Font;
/*    */ 
/*    */ public class FontManager
/*    */ {
/*  7 */   public MinecraftFontRenderer hud = null;
/*    */   
/*  9 */   public MinecraftFontRenderer arraylist = null;
/*    */   
/* 11 */   public MinecraftFontRenderer mainMenu = null;
/*    */   
/* 13 */   public MinecraftFontRenderer Cyperus = null;
/*    */   
/* 15 */   public MinecraftFontRenderer chat = null;
/*    */   
/* 17 */   public MinecraftFontRenderer time = null;
/*    */   
/* 19 */   public MinecraftFontRenderer scaffold = null;
/*    */   
/*    */ 
/* 22 */   public MinecraftFontRenderer chat1 = null;
/*    */   
/* 24 */   public MinecraftFontRenderer buttons = null;
/*    */   
/*    */ 
/* 27 */   private static String fontName = "Comfortaa";
/*    */   
/*    */   public void loadFonts()
/*    */   {
/* 31 */     this.hud = new MinecraftFontRenderer(new Font("Arial", 0, 25), true, true);
/* 32 */     this.Cyperus = new MinecraftFontRenderer(new Font("Arial", 0, 50), true, true);
/* 33 */     this.mainMenu = new MinecraftFontRenderer(new Font("Arial", 0, 45), true, true);
/* 34 */     this.chat = new MinecraftFontRenderer(new Font("Arial", 0, 17), true, true);
/* 35 */     this.time = new MinecraftFontRenderer(new Font("Arial", 0, 23), true, true);
/* 36 */     this.buttons = new MinecraftFontRenderer(new Font("Arial", 0, 15), true, true);
/* 37 */     this.chat1 = new MinecraftFontRenderer(new Font("Arial", 0, 20), true, true);
/* 38 */     this.arraylist = new MinecraftFontRenderer(new Font("Verdana", 0, 17), true, true);
/*    */   }
/*    */   
/*    */   public static String getFontName()
/*    */   {
/* 43 */     return fontName;
/*    */   }
/*    */   
/*    */   public static void setFontName(String fontName)
/*    */   {
/* 48 */     fontName = fontName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\font\FontManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */