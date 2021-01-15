/*     */ package io.netty.handler.codec.serialization;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ abstract class ReferenceMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   private final Map<K, Reference<V>> delegate;
/*     */   
/*     */   protected ReferenceMap(Map<K, Reference<V>> delegate)
/*     */   {
/*  28 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   abstract Reference<V> fold(V paramV);
/*     */   
/*     */   private V unfold(Reference<V> ref) {
/*  34 */     if (ref == null) {
/*  35 */       return null;
/*     */     }
/*     */     
/*  38 */     return (V)ref.get();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  43 */     return this.delegate.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  48 */     return this.delegate.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key)
/*     */   {
/*  53 */     return this.delegate.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value)
/*     */   {
/*  58 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public V get(Object key)
/*     */   {
/*  63 */     return (V)unfold((Reference)this.delegate.get(key));
/*     */   }
/*     */   
/*     */   public V put(K key, V value)
/*     */   {
/*  68 */     return (V)unfold((Reference)this.delegate.put(key, fold(value)));
/*     */   }
/*     */   
/*     */   public V remove(Object key)
/*     */   {
/*  73 */     return (V)unfold((Reference)this.delegate.remove(key));
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m)
/*     */   {
/*  78 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  79 */       this.delegate.put(entry.getKey(), fold(entry.getValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  85 */     this.delegate.clear();
/*     */   }
/*     */   
/*     */   public Set<K> keySet()
/*     */   {
/*  90 */     return this.delegate.keySet();
/*     */   }
/*     */   
/*     */   public Collection<V> values()
/*     */   {
/*  95 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ReferenceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */