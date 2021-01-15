/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class Vec4b
/*    */ {
/*    */   private byte field_176117_a;
/*    */   private byte field_176115_b;
/*    */   private byte field_176116_c;
/*    */   private byte field_176114_d;
/*    */   
/*    */   public Vec4b(byte p_i45555_1_, byte p_i45555_2_, byte p_i45555_3_, byte p_i45555_4_)
/*    */   {
/* 12 */     this.field_176117_a = p_i45555_1_;
/* 13 */     this.field_176115_b = p_i45555_2_;
/* 14 */     this.field_176116_c = p_i45555_3_;
/* 15 */     this.field_176114_d = p_i45555_4_;
/*    */   }
/*    */   
/*    */   public Vec4b(Vec4b p_i45556_1_)
/*    */   {
/* 20 */     this.field_176117_a = p_i45556_1_.field_176117_a;
/* 21 */     this.field_176115_b = p_i45556_1_.field_176115_b;
/* 22 */     this.field_176116_c = p_i45556_1_.field_176116_c;
/* 23 */     this.field_176114_d = p_i45556_1_.field_176114_d;
/*    */   }
/*    */   
/*    */   public byte func_176110_a()
/*    */   {
/* 28 */     return this.field_176117_a;
/*    */   }
/*    */   
/*    */   public byte func_176112_b()
/*    */   {
/* 33 */     return this.field_176115_b;
/*    */   }
/*    */   
/*    */   public byte func_176113_c()
/*    */   {
/* 38 */     return this.field_176116_c;
/*    */   }
/*    */   
/*    */   public byte func_176111_d()
/*    */   {
/* 43 */     return this.field_176114_d;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 48 */     if (this == p_equals_1_)
/*    */     {
/* 50 */       return true;
/*    */     }
/* 52 */     if (!(p_equals_1_ instanceof Vec4b))
/*    */     {
/* 54 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 58 */     Vec4b vec4b = (Vec4b)p_equals_1_;
/* 59 */     return this.field_176117_a == vec4b.field_176117_a;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 65 */     int i = this.field_176117_a;
/* 66 */     i = 31 * i + this.field_176115_b;
/* 67 */     i = 31 * i + this.field_176116_c;
/* 68 */     i = 31 * i + this.field_176114_d;
/* 69 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Vec4b.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */