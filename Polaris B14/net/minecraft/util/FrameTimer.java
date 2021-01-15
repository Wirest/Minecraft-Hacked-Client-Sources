/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class FrameTimer
/*    */ {
/*  5 */   private final long[] field_181752_a = new long['ð'];
/*    */   private int field_181753_b;
/*    */   private int field_181754_c;
/*    */   private int field_181755_d;
/*    */   
/*    */   public void func_181747_a(long p_181747_1_)
/*    */   {
/* 12 */     this.field_181752_a[this.field_181755_d] = p_181747_1_;
/* 13 */     this.field_181755_d += 1;
/*    */     
/* 15 */     if (this.field_181755_d == 240)
/*    */     {
/* 17 */       this.field_181755_d = 0;
/*    */     }
/*    */     
/* 20 */     if (this.field_181754_c < 240)
/*    */     {
/* 22 */       this.field_181753_b = 0;
/* 23 */       this.field_181754_c += 1;
/*    */     }
/*    */     else
/*    */     {
/* 27 */       this.field_181753_b = func_181751_b(this.field_181755_d + 1);
/*    */     }
/*    */   }
/*    */   
/*    */   public int func_181748_a(long p_181748_1_, int p_181748_3_)
/*    */   {
/* 33 */     double d0 = p_181748_1_ / 1.6666666E7D;
/* 34 */     return (int)(d0 * p_181748_3_);
/*    */   }
/*    */   
/*    */   public int func_181749_a()
/*    */   {
/* 39 */     return this.field_181753_b;
/*    */   }
/*    */   
/*    */   public int func_181750_b()
/*    */   {
/* 44 */     return this.field_181755_d;
/*    */   }
/*    */   
/*    */   public int func_181751_b(int p_181751_1_)
/*    */   {
/* 49 */     return p_181751_1_ % 240;
/*    */   }
/*    */   
/*    */   public long[] func_181746_c()
/*    */   {
/* 54 */     return this.field_181752_a;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\FrameTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */