/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class Tuple<A, B>
/*    */ {
/*    */   private A a;
/*    */   private B b;
/*    */   
/*    */   public Tuple(A aIn, B bIn)
/*    */   {
/* 10 */     this.a = aIn;
/* 11 */     this.b = bIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public A getFirst()
/*    */   {
/* 19 */     return (A)this.a;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public B getSecond()
/*    */   {
/* 27 */     return (B)this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Tuple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */