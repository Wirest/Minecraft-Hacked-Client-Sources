/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RecyclableArrayList
/*     */   extends ArrayList<Object>
/*     */ {
/*     */   private static final long serialVersionUID = -8605125654176467947L;
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 8;
/*  36 */   private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler()
/*     */   {
/*     */     protected RecyclableArrayList newObject(Recycler.Handle<RecyclableArrayList> handle) {
/*  39 */       return new RecyclableArrayList(handle, null);
/*     */     }
/*     */   };
/*     */   
/*     */   private final Recycler.Handle<RecyclableArrayList> handle;
/*     */   
/*     */   public static RecyclableArrayList newInstance()
/*     */   {
/*  47 */     return newInstance(8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static RecyclableArrayList newInstance(int minCapacity)
/*     */   {
/*  54 */     RecyclableArrayList ret = (RecyclableArrayList)RECYCLER.get();
/*  55 */     ret.ensureCapacity(minCapacity);
/*  56 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */   private RecyclableArrayList(Recycler.Handle<RecyclableArrayList> handle)
/*     */   {
/*  62 */     this(handle, 8);
/*     */   }
/*     */   
/*     */   private RecyclableArrayList(Recycler.Handle<RecyclableArrayList> handle, int initialCapacity) {
/*  66 */     super(initialCapacity);
/*  67 */     this.handle = handle;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<?> c)
/*     */   {
/*  72 */     checkNullElements(c);
/*  73 */     return super.addAll(c);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<?> c)
/*     */   {
/*  78 */     checkNullElements(c);
/*  79 */     return super.addAll(index, c);
/*     */   }
/*     */   
/*     */   private static void checkNullElements(Collection<?> c) {
/*  83 */     if (((c instanceof RandomAccess)) && ((c instanceof List)))
/*     */     {
/*  85 */       List<?> list = (List)c;
/*  86 */       int size = list.size();
/*  87 */       for (int i = 0; i < size; i++) {
/*  88 */         if (list.get(i) == null) {
/*  89 */           throw new IllegalArgumentException("c contains null values");
/*     */         }
/*     */       }
/*     */     } else {
/*  93 */       for (Object element : c) {
/*  94 */         if (element == null) {
/*  95 */           throw new IllegalArgumentException("c contains null values");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean add(Object element)
/*     */   {
/* 103 */     if (element == null) {
/* 104 */       throw new NullPointerException("element");
/*     */     }
/* 106 */     return super.add(element);
/*     */   }
/*     */   
/*     */   public void add(int index, Object element)
/*     */   {
/* 111 */     if (element == null) {
/* 112 */       throw new NullPointerException("element");
/*     */     }
/* 114 */     super.add(index, element);
/*     */   }
/*     */   
/*     */   public Object set(int index, Object element)
/*     */   {
/* 119 */     if (element == null) {
/* 120 */       throw new NullPointerException("element");
/*     */     }
/* 122 */     return super.set(index, element);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean recycle()
/*     */   {
/* 129 */     clear();
/* 130 */     return RECYCLER.recycle(this, this.handle);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\RecyclableArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */