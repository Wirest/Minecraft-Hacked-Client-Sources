/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReadWriteLock;
/*    */ 
/*    */ public class ThreadSafeBoundList<T>
/*    */ {
/*    */   private final T[] field_152759_a;
/*    */   private final Class<? extends T> field_152760_b;
/* 11 */   private final ReadWriteLock field_152761_c = new java.util.concurrent.locks.ReentrantReadWriteLock();
/*    */   private int field_152762_d;
/*    */   private int field_152763_e;
/*    */   
/*    */   public ThreadSafeBoundList(Class<? extends T> p_i1126_1_, int p_i1126_2_)
/*    */   {
/* 17 */     this.field_152760_b = p_i1126_1_;
/* 18 */     this.field_152759_a = ((Object[])Array.newInstance(p_i1126_1_, p_i1126_2_));
/*    */   }
/*    */   
/*    */   public T func_152757_a(T p_152757_1_)
/*    */   {
/* 23 */     this.field_152761_c.writeLock().lock();
/* 24 */     this.field_152759_a[this.field_152763_e] = p_152757_1_;
/* 25 */     this.field_152763_e = ((this.field_152763_e + 1) % func_152758_b());
/*    */     
/* 27 */     if (this.field_152762_d < func_152758_b())
/*    */     {
/* 29 */       this.field_152762_d += 1;
/*    */     }
/*    */     
/* 32 */     this.field_152761_c.writeLock().unlock();
/* 33 */     return p_152757_1_;
/*    */   }
/*    */   
/*    */   public int func_152758_b()
/*    */   {
/* 38 */     this.field_152761_c.readLock().lock();
/* 39 */     int i = this.field_152759_a.length;
/* 40 */     this.field_152761_c.readLock().unlock();
/* 41 */     return i;
/*    */   }
/*    */   
/*    */   public T[] func_152756_c()
/*    */   {
/* 46 */     Object[] at = (Object[])Array.newInstance(this.field_152760_b, this.field_152762_d);
/* 47 */     this.field_152761_c.readLock().lock();
/*    */     
/* 49 */     for (int i = 0; i < this.field_152762_d; i++)
/*    */     {
/* 51 */       int j = (this.field_152763_e - this.field_152762_d + i) % func_152758_b();
/*    */       
/* 53 */       if (j < 0)
/*    */       {
/* 55 */         j += func_152758_b();
/*    */       }
/*    */       
/* 58 */       at[i] = this.field_152759_a[j];
/*    */     }
/*    */     
/* 61 */     this.field_152761_c.readLock().unlock();
/* 62 */     return at;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ThreadSafeBoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */