/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ public class AnimationFrame
/*    */ {
/*    */   private final int frameIndex;
/*    */   private final int frameTime;
/*    */   
/*    */   public AnimationFrame(int p_i1307_1_)
/*    */   {
/* 10 */     this(p_i1307_1_, -1);
/*    */   }
/*    */   
/*    */   public AnimationFrame(int p_i1308_1_, int p_i1308_2_)
/*    */   {
/* 15 */     this.frameIndex = p_i1308_1_;
/* 16 */     this.frameTime = p_i1308_2_;
/*    */   }
/*    */   
/*    */   public boolean hasNoTime()
/*    */   {
/* 21 */     return this.frameTime == -1;
/*    */   }
/*    */   
/*    */   public int getFrameTime()
/*    */   {
/* 26 */     return this.frameTime;
/*    */   }
/*    */   
/*    */   public int getFrameIndex()
/*    */   {
/* 31 */     return this.frameIndex;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\AnimationFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */