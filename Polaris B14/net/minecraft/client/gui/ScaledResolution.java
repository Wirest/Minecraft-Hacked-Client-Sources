/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ScaledResolution
/*    */ {
/*    */   private final double scaledWidthD;
/*    */   private final double scaledHeightD;
/*    */   private static int scaledWidth;
/*    */   private static int scaledHeight;
/*    */   private int scaleFactor;
/*    */   
/*    */   public ScaledResolution(Minecraft mcIn, int p_i46324_2_, int p_i46324_3_)
/*    */   {
/* 16 */     scaledWidth = p_i46324_2_;
/* 17 */     scaledHeight = p_i46324_3_;
/* 18 */     this.scaleFactor = 1;
/* 19 */     boolean var4 = mcIn.isUnicode();
/* 20 */     int var5 = mcIn.gameSettings.guiScale;
/*    */     
/* 22 */     if (var5 == 0)
/*    */     {
/* 24 */       var5 = 1000;
/*    */     }
/*    */     
/* 27 */     while ((this.scaleFactor < var5) && (scaledWidth / (this.scaleFactor + 1) >= 320) && (scaledHeight / (this.scaleFactor + 1) >= 240))
/*    */     {
/* 29 */       this.scaleFactor += 1;
/*    */     }
/*    */     
/* 32 */     if ((var4) && (this.scaleFactor % 2 != 0) && (this.scaleFactor != 1))
/*    */     {
/* 34 */       this.scaleFactor -= 1;
/*    */     }
/*    */     
/* 37 */     this.scaledWidthD = (scaledWidth / this.scaleFactor);
/* 38 */     this.scaledHeightD = (scaledHeight / this.scaleFactor);
/* 39 */     scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
/* 40 */     scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
/*    */   }
/*    */   
/*    */   public static int getScaledWidth()
/*    */   {
/* 45 */     return scaledWidth;
/*    */   }
/*    */   
/*    */   public static int getScaledHeight()
/*    */   {
/* 50 */     return scaledHeight;
/*    */   }
/*    */   
/*    */   public double getScaledWidth_double()
/*    */   {
/* 55 */     return this.scaledWidthD;
/*    */   }
/*    */   
/*    */   public double getScaledHeight_double()
/*    */   {
/* 60 */     return this.scaledHeightD;
/*    */   }
/*    */   
/*    */   public int getScaleFactor()
/*    */   {
/* 65 */     return this.scaleFactor;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\ScaledResolution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */