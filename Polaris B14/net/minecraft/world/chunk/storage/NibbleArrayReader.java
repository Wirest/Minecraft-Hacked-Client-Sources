/*    */ package net.minecraft.world.chunk.storage;
/*    */ 
/*    */ public class NibbleArrayReader
/*    */ {
/*    */   public final byte[] data;
/*    */   private final int depthBits;
/*    */   private final int depthBitsPlusFour;
/*    */   
/*    */   public NibbleArrayReader(byte[] dataIn, int depthBitsIn)
/*    */   {
/* 11 */     this.data = dataIn;
/* 12 */     this.depthBits = depthBitsIn;
/* 13 */     this.depthBitsPlusFour = (depthBitsIn + 4);
/*    */   }
/*    */   
/*    */   public int get(int p_76686_1_, int p_76686_2_, int p_76686_3_)
/*    */   {
/* 18 */     int i = p_76686_1_ << this.depthBitsPlusFour | p_76686_3_ << this.depthBits | p_76686_2_;
/* 19 */     int j = i >> 1;
/* 20 */     int k = i & 0x1;
/* 21 */     return k == 0 ? this.data[j] & 0xF : this.data[j] >> 4 & 0xF;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\storage\NibbleArrayReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */