/*    */ package optfine;
/*    */ 
/*    */ public class CacheLocalByte
/*    */ {
/*  5 */   private int maxX = 18;
/*  6 */   private int maxY = 128;
/*  7 */   private int maxZ = 18;
/*  8 */   private int offsetX = 0;
/*  9 */   private int offsetY = 0;
/* 10 */   private int offsetZ = 0;
/* 11 */   private byte[][][] cache = null;
/* 12 */   private byte[] lastZs = null;
/* 13 */   private int lastDz = 0;
/*    */   
/*    */   public CacheLocalByte(int p_i24_1_, int p_i24_2_, int p_i24_3_)
/*    */   {
/* 17 */     this.maxX = p_i24_1_;
/* 18 */     this.maxY = p_i24_2_;
/* 19 */     this.maxZ = p_i24_3_;
/* 20 */     this.cache = new byte[p_i24_1_][p_i24_2_][p_i24_3_];
/* 21 */     resetCache();
/*    */   }
/*    */   
/*    */   public void resetCache()
/*    */   {
/* 26 */     for (int i = 0; i < this.maxX; i++)
/*    */     {
/* 28 */       byte[][] abyte = this.cache[i];
/*    */       
/* 30 */       for (int j = 0; j < this.maxY; j++)
/*    */       {
/* 32 */         byte[] abyte1 = abyte[j];
/*    */         
/* 34 */         for (int k = 0; k < this.maxZ; k++)
/*    */         {
/* 36 */           abyte1[k] = -1;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void setOffset(int p_setOffset_1_, int p_setOffset_2_, int p_setOffset_3_)
/*    */   {
/* 44 */     this.offsetX = p_setOffset_1_;
/* 45 */     this.offsetY = p_setOffset_2_;
/* 46 */     this.offsetZ = p_setOffset_3_;
/* 47 */     resetCache();
/*    */   }
/*    */   
/*    */   public byte get(int p_get_1_, int p_get_2_, int p_get_3_)
/*    */   {
/*    */     try
/*    */     {
/* 54 */       this.lastZs = this.cache[(p_get_1_ - this.offsetX)][(p_get_2_ - this.offsetY)];
/* 55 */       this.lastDz = (p_get_3_ - this.offsetZ);
/* 56 */       return this.lastZs[this.lastDz];
/*    */     }
/*    */     catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
/*    */     {
/* 60 */       arrayindexoutofboundsexception.printStackTrace(); }
/* 61 */     return -1;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setLast(byte p_setLast_1_)
/*    */   {
/*    */     try
/*    */     {
/* 69 */       this.lastZs[this.lastDz] = p_setLast_1_;
/*    */     }
/*    */     catch (Exception exception)
/*    */     {
/* 73 */       exception.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\CacheLocalByte.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */