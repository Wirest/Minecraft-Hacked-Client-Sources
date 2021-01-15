/*    */ package net.minecraft.util;
/*    */ 
/*    */ public abstract class LazyLoadBase<T>
/*    */ {
/*    */   private T value;
/*  6 */   private boolean isLoaded = false;
/*    */   
/*    */   public T getValue()
/*    */   {
/* 10 */     if (!this.isLoaded)
/*    */     {
/* 12 */       this.isLoaded = true;
/* 13 */       this.value = load();
/*    */     }
/*    */     
/* 16 */     return (T)this.value;
/*    */   }
/*    */   
/*    */   protected abstract T load();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\LazyLoadBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */