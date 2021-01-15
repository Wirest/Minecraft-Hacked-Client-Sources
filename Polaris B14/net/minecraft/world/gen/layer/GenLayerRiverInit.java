/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerRiverInit extends GenLayer
/*    */ {
/*    */   public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_)
/*    */   {
/*  7 */     super(p_i2127_1_);
/*  8 */     this.parent = p_i2127_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 17 */     int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/* 18 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 20 */     for (int i = 0; i < areaHeight; i++)
/*    */     {
/* 22 */       for (int j = 0; j < areaWidth; j++)
/*    */       {
/* 24 */         initChunkSeed(j + areaX, i + areaY);
/* 25 */         aint1[(j + i * areaWidth)] = (aint[(j + i * areaWidth)] > 0 ? nextInt(299999) + 2 : 0);
/*    */       }
/*    */     }
/*    */     
/* 29 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerRiverInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */