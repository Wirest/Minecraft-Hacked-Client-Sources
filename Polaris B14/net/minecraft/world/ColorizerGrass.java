/*    */ package net.minecraft.world;
/*    */ 
/*    */ 
/*    */ public class ColorizerGrass
/*    */ {
/*  6 */   private static int[] grassBuffer = new int[65536];
/*    */   
/*    */   public static void setGrassBiomeColorizer(int[] p_77479_0_)
/*    */   {
/* 10 */     grassBuffer = p_77479_0_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int getGrassColor(double p_77480_0_, double p_77480_2_)
/*    */   {
/* 18 */     p_77480_2_ *= p_77480_0_;
/* 19 */     int i = (int)((1.0D - p_77480_0_) * 255.0D);
/* 20 */     int j = (int)((1.0D - p_77480_2_) * 255.0D);
/* 21 */     int k = j << 8 | i;
/* 22 */     return k > grassBuffer.length ? -65281 : grassBuffer[k];
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\ColorizerGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */