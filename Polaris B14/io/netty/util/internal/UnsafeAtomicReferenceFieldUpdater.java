/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*    */ import sun.misc.Unsafe;
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
/*    */ final class UnsafeAtomicReferenceFieldUpdater<U, M>
/*    */   extends AtomicReferenceFieldUpdater<U, M>
/*    */ {
/*    */   private final long offset;
/*    */   private final Unsafe unsafe;
/*    */   
/*    */   UnsafeAtomicReferenceFieldUpdater(Unsafe unsafe, Class<U> tClass, String fieldName)
/*    */     throws NoSuchFieldException
/*    */   {
/* 29 */     Field field = tClass.getDeclaredField(fieldName);
/* 30 */     if (!Modifier.isVolatile(field.getModifiers())) {
/* 31 */       throw new IllegalArgumentException("Must be volatile");
/*    */     }
/* 33 */     this.unsafe = unsafe;
/* 34 */     this.offset = unsafe.objectFieldOffset(field);
/*    */   }
/*    */   
/*    */   public boolean compareAndSet(U obj, M expect, M update)
/*    */   {
/* 39 */     return this.unsafe.compareAndSwapObject(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public boolean weakCompareAndSet(U obj, M expect, M update)
/*    */   {
/* 44 */     return this.unsafe.compareAndSwapObject(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public void set(U obj, M newValue)
/*    */   {
/* 49 */     this.unsafe.putObjectVolatile(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */   public void lazySet(U obj, M newValue)
/*    */   {
/* 54 */     this.unsafe.putOrderedObject(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */ 
/*    */   public M get(U obj)
/*    */   {
/* 60 */     return (M)this.unsafe.getObjectVolatile(obj, this.offset);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\UnsafeAtomicReferenceFieldUpdater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */