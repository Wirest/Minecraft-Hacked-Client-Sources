/*     */ package io.netty.util.collection;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class PrimitiveCollections
/*     */ {
/*  29 */   private static final IntObjectMap<Object> EMPTY_INT_OBJECT_MAP = new EmptyIntObjectMap(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <V> IntObjectMap<V> emptyIntObjectMap()
/*     */   {
/*  39 */     return EMPTY_INT_OBJECT_MAP;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <V> IntObjectMap<V> unmodifiableIntObjectMap(IntObjectMap<V> map)
/*     */   {
/*  46 */     return new UnmodifiableIntObjectMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class EmptyIntObjectMap
/*     */     implements IntObjectMap<Object>
/*     */   {
/*     */     public Object get(int key)
/*     */     {
/*  56 */       return null;
/*     */     }
/*     */     
/*     */     public Object put(int key, Object value)
/*     */     {
/*  61 */       throw new UnsupportedOperationException("put");
/*     */     }
/*     */     
/*     */     public void putAll(IntObjectMap<Object> sourceMap)
/*     */     {
/*  66 */       throw new UnsupportedOperationException("putAll");
/*     */     }
/*     */     
/*     */     public Object remove(int key)
/*     */     {
/*  71 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/*  76 */       return 0;
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/*  81 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void clear() {}
/*     */     
/*     */ 
/*     */     public boolean containsKey(int key)
/*     */     {
/*  91 */       return false;
/*     */     }
/*     */     
/*     */     public boolean containsValue(Object value)
/*     */     {
/*  96 */       return false;
/*     */     }
/*     */     
/*     */     public Iterable<IntObjectMap.Entry<Object>> entries()
/*     */     {
/* 101 */       return Collections.emptySet();
/*     */     }
/*     */     
/*     */     public int[] keys()
/*     */     {
/* 106 */       return EmptyArrays.EMPTY_INTS;
/*     */     }
/*     */     
/*     */     public Object[] values(Class<Object> clazz)
/*     */     {
/* 111 */       return EmptyArrays.EMPTY_OBJECTS;
/*     */     }
/*     */     
/*     */     public Collection<Object> values()
/*     */     {
/* 116 */       return Collections.emptyList();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class UnmodifiableIntObjectMap<V>
/*     */     implements IntObjectMap<V>, Iterable<IntObjectMap.Entry<V>>
/*     */   {
/*     */     final IntObjectMap<V> map;
/*     */     
/*     */ 
/*     */     UnmodifiableIntObjectMap(IntObjectMap<V> map)
/*     */     {
/* 130 */       this.map = map;
/*     */     }
/*     */     
/*     */     public V get(int key)
/*     */     {
/* 135 */       return (V)this.map.get(key);
/*     */     }
/*     */     
/*     */     public V put(int key, V value)
/*     */     {
/* 140 */       throw new UnsupportedOperationException("put");
/*     */     }
/*     */     
/*     */     public void putAll(IntObjectMap<V> sourceMap)
/*     */     {
/* 145 */       throw new UnsupportedOperationException("putAll");
/*     */     }
/*     */     
/*     */     public V remove(int key)
/*     */     {
/* 150 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 155 */       return this.map.size();
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 160 */       return this.map.isEmpty();
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 165 */       throw new UnsupportedOperationException("clear");
/*     */     }
/*     */     
/*     */     public boolean containsKey(int key)
/*     */     {
/* 170 */       return this.map.containsKey(key);
/*     */     }
/*     */     
/*     */     public boolean containsValue(V value)
/*     */     {
/* 175 */       return this.map.containsValue(value);
/*     */     }
/*     */     
/*     */     public Iterable<IntObjectMap.Entry<V>> entries()
/*     */     {
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Iterator<IntObjectMap.Entry<V>> iterator()
/*     */     {
/* 185 */       return new IteratorImpl(this.map.entries().iterator());
/*     */     }
/*     */     
/*     */     public int[] keys()
/*     */     {
/* 190 */       return this.map.keys();
/*     */     }
/*     */     
/*     */     public V[] values(Class<V> clazz)
/*     */     {
/* 195 */       return this.map.values(clazz);
/*     */     }
/*     */     
/*     */     public Collection<V> values()
/*     */     {
/* 200 */       return this.map.values();
/*     */     }
/*     */     
/*     */     private class IteratorImpl
/*     */       implements Iterator<IntObjectMap.Entry<V>>
/*     */     {
/*     */       final Iterator<IntObjectMap.Entry<V>> iter;
/*     */       
/*     */       IteratorImpl()
/*     */       {
/* 210 */         this.iter = iter;
/*     */       }
/*     */       
/*     */       public boolean hasNext()
/*     */       {
/* 215 */         return this.iter.hasNext();
/*     */       }
/*     */       
/*     */       public IntObjectMap.Entry<V> next()
/*     */       {
/* 220 */         if (!hasNext()) {
/* 221 */           throw new NoSuchElementException();
/*     */         }
/* 223 */         return new PrimitiveCollections.UnmodifiableIntObjectMap.EntryImpl(PrimitiveCollections.UnmodifiableIntObjectMap.this, (IntObjectMap.Entry)this.iter.next());
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 228 */         throw new UnsupportedOperationException("remove");
/*     */       }
/*     */     }
/*     */     
/*     */     private class EntryImpl
/*     */       implements IntObjectMap.Entry<V>
/*     */     {
/*     */       final IntObjectMap.Entry<V> entry;
/*     */       
/*     */       EntryImpl()
/*     */       {
/* 239 */         this.entry = entry;
/*     */       }
/*     */       
/*     */       public int key()
/*     */       {
/* 244 */         return this.entry.key();
/*     */       }
/*     */       
/*     */       public V value()
/*     */       {
/* 249 */         return (V)this.entry.value();
/*     */       }
/*     */       
/*     */       public void setValue(V value)
/*     */       {
/* 254 */         throw new UnsupportedOperationException("setValue");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\collection\PrimitiveCollections.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */