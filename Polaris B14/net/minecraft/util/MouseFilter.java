/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class MouseFilter
/*    */ {
/*    */   private float field_76336_a;
/*    */   
/*    */   private float field_76334_b;
/*    */   
/*    */   private float field_76335_c;
/*    */   
/*    */   public float smooth(float p_76333_1_, float p_76333_2_)
/*    */   {
/* 14 */     this.field_76336_a += p_76333_1_;
/* 15 */     p_76333_1_ = (this.field_76336_a - this.field_76334_b) * p_76333_2_;
/* 16 */     this.field_76335_c += (p_76333_1_ - this.field_76335_c) * 0.5F;
/*    */     
/* 18 */     if (((p_76333_1_ > 0.0F) && (p_76333_1_ > this.field_76335_c)) || ((p_76333_1_ < 0.0F) && (p_76333_1_ < this.field_76335_c)))
/*    */     {
/* 20 */       p_76333_1_ = this.field_76335_c;
/*    */     }
/*    */     
/* 23 */     this.field_76334_b += p_76333_1_;
/* 24 */     return p_76333_1_;
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 29 */     this.field_76336_a = 0.0F;
/* 30 */     this.field_76334_b = 0.0F;
/* 31 */     this.field_76335_c = 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MouseFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */