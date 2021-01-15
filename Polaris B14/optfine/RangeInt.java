/*    */ package optfine;
/*    */ 
/*    */ public class RangeInt
/*    */ {
/*    */   private int min;
/*    */   private int max;
/*    */   
/*    */   public RangeInt(int p_i54_1_, int p_i54_2_)
/*    */   {
/* 10 */     this.min = Math.min(p_i54_1_, p_i54_2_);
/* 11 */     this.max = Math.max(p_i54_1_, p_i54_2_);
/*    */   }
/*    */   
/*    */   public boolean isInRange(int p_isInRange_1_)
/*    */   {
/* 16 */     return p_isInRange_1_ >= this.min;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 21 */     return "min: " + this.min + ", max: " + this.max;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RangeInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */