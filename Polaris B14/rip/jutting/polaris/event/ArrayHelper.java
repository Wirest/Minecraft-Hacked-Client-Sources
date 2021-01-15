/*     */ package rip.jutting.polaris.event;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ArrayHelper<T> implements Iterable<T>
/*     */ {
/*     */   private T[] elements;
/*     */   
/*     */   public ArrayHelper(T[] array)
/*     */   {
/*  11 */     this.elements = array;
/*     */   }
/*     */   
/*     */   public ArrayHelper()
/*     */   {
/*  16 */     this.elements = new Object[0];
/*     */   }
/*     */   
/*     */   public void add(T t)
/*     */   {
/*  21 */     if (t != null) {
/*  22 */       Object[] array = new Object[size() + 1];
/*     */       
/*  24 */       for (int i = 0; i < array.length; i++) {
/*  25 */         if (i < size()) {
/*  26 */           array[i] = get(i);
/*     */         } else {
/*  28 */           array[i] = t;
/*     */         }
/*     */       }
/*     */       
/*  32 */       set(array);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contains(T t)
/*     */   {
/*     */     Object[] array;
/*  40 */     int lenght = (array = array()).length; for (int i = 0; i < lenght; i++) {
/*  41 */       T entry = array[i];
/*  42 */       if (entry.equals(t)) {
/*  43 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public void remove(T t)
/*     */   {
/*  52 */     if (contains(t)) {
/*  53 */       Object[] array = new Object[size() - 1];
/*  54 */       boolean b = true;
/*     */       
/*  56 */       for (int i = 0; i < size(); i++) {
/*  57 */         if ((b) && (get(i).equals(t))) {
/*  58 */           b = false;
/*     */         } else {
/*  60 */           array[(b ? i : i - 1)] = get(i);
/*     */         }
/*     */       }
/*     */       
/*  64 */       set(array);
/*     */     }
/*     */   }
/*     */   
/*     */   public T[] array()
/*     */   {
/*  70 */     return this.elements;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  75 */     return array().length;
/*     */   }
/*     */   
/*     */   public void set(T[] array)
/*     */   {
/*  80 */     this.elements = array;
/*     */   }
/*     */   
/*     */   public T get(int index)
/*     */   {
/*  85 */     return (T)array()[index];
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  90 */     this.elements = new Object[0];
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  95 */     return size() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 101 */     new Iterator()
/*     */     {
/* 103 */       private int index = 0;
/*     */       
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 108 */         return (this.index < ArrayHelper.this.size()) && (ArrayHelper.this.get(this.index) != null);
/*     */       }
/*     */       
/*     */ 
/*     */       public T next()
/*     */       {
/* 114 */         return (T)ArrayHelper.this.get(this.index++);
/*     */       }
/*     */       
/*     */ 
/*     */       public void remove()
/*     */       {
/* 120 */         ArrayHelper.this.remove(ArrayHelper.this.get(this.index));
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\event\ArrayHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */