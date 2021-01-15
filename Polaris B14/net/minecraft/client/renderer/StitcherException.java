/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.Stitcher.Holder;
/*    */ 
/*    */ public class StitcherException extends RuntimeException
/*    */ {
/*    */   private final Stitcher.Holder holder;
/*    */   
/*    */   public StitcherException(Stitcher.Holder p_i2344_1_, String p_i2344_2_)
/*    */   {
/* 11 */     super(p_i2344_2_);
/* 12 */     this.holder = p_i2344_1_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\StitcherException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */