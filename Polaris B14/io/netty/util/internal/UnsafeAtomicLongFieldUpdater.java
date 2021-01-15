/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*    */ final class UnsafeAtomicLongFieldUpdater<T>
/*    */   extends AtomicLongFieldUpdater<T>
/*    */ {
/*    */   private final long offset;
/*    */   private final Unsafe unsafe;
/*    */   
/*    */   UnsafeAtomicLongFieldUpdater(Unsafe unsafe, Class<?> tClass, String fieldName)
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
/*    */   public boolean compareAndSet(T obj, long expect, long update)
/*    */   {
/* 39 */     return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public boolean weakCompareAndSet(T obj, long expect, long update)
/*    */   {
/* 44 */     return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public void set(T obj, long newValue)
/*    */   {
/* 49 */     this.unsafe.putLongVolatile(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */   public void lazySet(T obj, long newValue)
/*    */   {
/* 54 */     this.unsafe.putOrderedLong(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */   public long get(T obj)
/*    */   {
/* 59 */     return this.unsafe.getLongVolatile(obj, this.offset);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\UnsafeAtomicLongFieldUpdater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */