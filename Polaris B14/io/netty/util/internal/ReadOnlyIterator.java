/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReadOnlyIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   private final Iterator<? extends T> iterator;
/*    */   
/*    */   public ReadOnlyIterator(Iterator<? extends T> iterator)
/*    */   {
/* 25 */     if (iterator == null) {
/* 26 */       throw new NullPointerException("iterator");
/*    */     }
/* 28 */     this.iterator = iterator;
/*    */   }
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 33 */     return this.iterator.hasNext();
/*    */   }
/*    */   
/*    */   public T next()
/*    */   {
/* 38 */     return (T)this.iterator.next();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 43 */     throw new UnsupportedOperationException("read-only");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\ReadOnlyIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */