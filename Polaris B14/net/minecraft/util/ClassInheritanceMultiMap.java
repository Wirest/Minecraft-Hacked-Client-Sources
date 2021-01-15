/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ClassInheritanceMultiMap<T> extends AbstractSet<T>
/*     */ {
/*  15 */   private static final Set<Class<?>> field_181158_a = ;
/*  16 */   private final Map<Class<?>, List<T>> map = Maps.newHashMap();
/*  17 */   private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
/*     */   private final Class<T> baseClass;
/*  19 */   private final List<T> field_181745_e = Lists.newArrayList();
/*     */   
/*     */   public ClassInheritanceMultiMap(Class<T> baseClassIn)
/*     */   {
/*  23 */     this.baseClass = baseClassIn;
/*  24 */     this.knownKeys.add(baseClassIn);
/*  25 */     this.map.put(baseClassIn, this.field_181745_e);
/*     */     
/*  27 */     for (Class<?> oclass : field_181158_a)
/*     */     {
/*  29 */       createLookup(oclass);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void createLookup(Class<?> clazz)
/*     */   {
/*  35 */     field_181158_a.add(clazz);
/*     */     
/*  37 */     for (T t : this.field_181745_e)
/*     */     {
/*  39 */       if (clazz.isAssignableFrom(t.getClass()))
/*     */       {
/*  41 */         func_181743_a(t, clazz);
/*     */       }
/*     */     }
/*     */     
/*  45 */     this.knownKeys.add(clazz);
/*     */   }
/*     */   
/*     */   protected Class<?> func_181157_b(Class<?> p_181157_1_)
/*     */   {
/*  50 */     if (this.baseClass.isAssignableFrom(p_181157_1_))
/*     */     {
/*  52 */       if (!this.knownKeys.contains(p_181157_1_))
/*     */       {
/*  54 */         createLookup(p_181157_1_);
/*     */       }
/*     */       
/*  57 */       return p_181157_1_;
/*     */     }
/*     */     
/*     */ 
/*  61 */     throw new IllegalArgumentException("Don't know how to search for " + p_181157_1_);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean add(T p_add_1_)
/*     */   {
/*  67 */     for (Class<?> oclass : this.knownKeys)
/*     */     {
/*  69 */       if (oclass.isAssignableFrom(p_add_1_.getClass()))
/*     */       {
/*  71 */         func_181743_a(p_add_1_, oclass);
/*     */       }
/*     */     }
/*     */     
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   private void func_181743_a(T p_181743_1_, Class<?> p_181743_2_)
/*     */   {
/*  80 */     List<T> list = (List)this.map.get(p_181743_2_);
/*     */     
/*  82 */     if (list == null)
/*     */     {
/*  84 */       this.map.put(p_181743_2_, Lists.newArrayList(new Object[] { p_181743_1_ }));
/*     */     }
/*     */     else
/*     */     {
/*  88 */       list.add(p_181743_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean remove(Object p_remove_1_)
/*     */   {
/*  94 */     T t = (T)p_remove_1_;
/*  95 */     boolean flag = false;
/*     */     
/*  97 */     for (Class<?> oclass : this.knownKeys)
/*     */     {
/*  99 */       if (oclass.isAssignableFrom(t.getClass()))
/*     */       {
/* 101 */         List<T> list = (List)this.map.get(oclass);
/*     */         
/* 103 */         if ((list != null) && (list.remove(t)))
/*     */         {
/* 105 */           flag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 110 */     return flag;
/*     */   }
/*     */   
/*     */   public boolean contains(Object p_contains_1_)
/*     */   {
/* 115 */     return Iterators.contains(getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
/*     */   }
/*     */   
/*     */   public <S> Iterable<S> getByClass(final Class<S> clazz)
/*     */   {
/* 120 */     new Iterable()
/*     */     {
/*     */       public Iterator<S> iterator()
/*     */       {
/* 124 */         List<T> list = (List)ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.func_181157_b(clazz));
/*     */         
/* 126 */         if (list == null)
/*     */         {
/* 128 */           return Iterators.emptyIterator();
/*     */         }
/*     */         
/*     */ 
/* 132 */         Iterator<T> iterator = list.iterator();
/* 133 */         return Iterators.filter(iterator, clazz);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 141 */     return this.field_181745_e.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator());
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 146 */     return this.field_181745_e.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ClassInheritanceMultiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */