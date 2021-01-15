/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Set;
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
/*     */ public class FastThreadLocal<V>
/*     */ {
/*  47 */   private static final int variablesToRemoveIndex = ;
/*     */   
/*     */ 
/*     */   private final int index;
/*     */   
/*     */ 
/*     */   public static void removeAll()
/*     */   {
/*  55 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  56 */     if (threadLocalMap == null) {
/*  57 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  61 */       Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*  62 */       if ((v != null) && (v != InternalThreadLocalMap.UNSET))
/*     */       {
/*  64 */         Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
/*  65 */         FastThreadLocal<?>[] variablesToRemoveArray = (FastThreadLocal[])variablesToRemove.toArray(new FastThreadLocal[variablesToRemove.size()]);
/*     */         
/*  67 */         for (FastThreadLocal<?> tlv : variablesToRemoveArray) {
/*  68 */           tlv.remove(threadLocalMap);
/*     */         }
/*     */       }
/*     */     } finally {
/*  72 */       InternalThreadLocalMap.remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int size()
/*     */   {
/*  80 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  81 */     if (threadLocalMap == null) {
/*  82 */       return 0;
/*     */     }
/*  84 */     return threadLocalMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void destroy() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable)
/*     */   {
/* 100 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     Set<FastThreadLocal<?>> variablesToRemove;
/* 102 */     if ((v == InternalThreadLocalMap.UNSET) || (v == null)) {
/* 103 */       Set<FastThreadLocal<?>> variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
/* 104 */       threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
/*     */     } else {
/* 106 */       variablesToRemove = (Set)v;
/*     */     }
/*     */     
/* 109 */     variablesToRemove.add(variable);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable)
/*     */   {
/* 115 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     
/* 117 */     if ((v == InternalThreadLocalMap.UNSET) || (v == null)) {
/* 118 */       return;
/*     */     }
/*     */     
/*     */ 
/* 122 */     Set<FastThreadLocal<?>> variablesToRemove = (Set)v;
/* 123 */     variablesToRemove.remove(variable);
/*     */   }
/*     */   
/*     */ 
/*     */   public FastThreadLocal()
/*     */   {
/* 129 */     this.index = InternalThreadLocalMap.nextVariableIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final V get()
/*     */   {
/* 136 */     return (V)get(InternalThreadLocalMap.get());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final V get(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 145 */     Object v = threadLocalMap.indexedVariable(this.index);
/* 146 */     if (v != InternalThreadLocalMap.UNSET) {
/* 147 */       return (V)v;
/*     */     }
/*     */     
/* 150 */     return (V)initialize(threadLocalMap);
/*     */   }
/*     */   
/*     */   private V initialize(InternalThreadLocalMap threadLocalMap) {
/* 154 */     V v = null;
/*     */     try {
/* 156 */       v = initialValue();
/*     */     } catch (Exception e) {
/* 158 */       PlatformDependent.throwException(e);
/*     */     }
/*     */     
/* 161 */     threadLocalMap.setIndexedVariable(this.index, v);
/* 162 */     addToVariablesToRemove(threadLocalMap, this);
/* 163 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void set(V value)
/*     */   {
/* 170 */     if (value != InternalThreadLocalMap.UNSET) {
/* 171 */       set(InternalThreadLocalMap.get(), value);
/*     */     } else {
/* 173 */       remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void set(InternalThreadLocalMap threadLocalMap, V value)
/*     */   {
/* 181 */     if (value != InternalThreadLocalMap.UNSET) {
/* 182 */       if (threadLocalMap.setIndexedVariable(this.index, value)) {
/* 183 */         addToVariablesToRemove(threadLocalMap, this);
/*     */       }
/*     */     } else {
/* 186 */       remove(threadLocalMap);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isSet()
/*     */   {
/* 194 */     return isSet(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isSet(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 202 */     return (threadLocalMap != null) && (threadLocalMap.isIndexedVariableSet(this.index));
/*     */   }
/*     */   
/*     */ 
/*     */   public final void remove()
/*     */   {
/* 208 */     remove(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void remove(InternalThreadLocalMap threadLocalMap)
/*     */   {
/* 218 */     if (threadLocalMap == null) {
/* 219 */       return;
/*     */     }
/*     */     
/* 222 */     Object v = threadLocalMap.removeIndexedVariable(this.index);
/* 223 */     removeFromVariablesToRemove(threadLocalMap, this);
/*     */     
/* 225 */     if (v != InternalThreadLocalMap.UNSET) {
/*     */       try {
/* 227 */         onRemoval(v);
/*     */       } catch (Exception e) {
/* 229 */         PlatformDependent.throwException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected V initialValue()
/*     */     throws Exception
/*     */   {
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   protected void onRemoval(V value)
/*     */     throws Exception
/*     */   {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\FastThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */