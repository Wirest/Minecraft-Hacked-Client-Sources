/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*    */ final class UnsafeAtomicIntegerFieldUpdater<T>
/*    */   extends AtomicIntegerFieldUpdater<T>
/*    */ {
/*    */   private final long offset;
/*    */   private final Unsafe unsafe;
/*    */   
/*    */   UnsafeAtomicIntegerFieldUpdater(Unsafe unsafe, Class<?> tClass, String fieldName)
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
/*    */   public boolean compareAndSet(T obj, int expect, int update)
/*    */   {
/* 39 */     return this.unsafe.compareAndSwapInt(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public boolean weakCompareAndSet(T obj, int expect, int update)
/*    */   {
/* 44 */     return this.unsafe.compareAndSwapInt(obj, this.offset, expect, update);
/*    */   }
/*    */   
/*    */   public void set(T obj, int newValue)
/*    */   {
/* 49 */     this.unsafe.putIntVolatile(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */   public void lazySet(T obj, int newValue)
/*    */   {
/* 54 */     this.unsafe.putOrderedInt(obj, this.offset, newValue);
/*    */   }
/*    */   
/*    */   public int get(T obj)
/*    */   {
/* 59 */     return this.unsafe.getIntVolatile(obj, this.offset);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\UnsafeAtomicIntegerFieldUpdater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */