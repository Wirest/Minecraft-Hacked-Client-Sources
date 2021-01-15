/*     */ package io.netty.util.collection;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ public class IntObjectHashMap<V>
/*     */   implements IntObjectMap<V>, Iterable<IntObjectMap.Entry<V>>
/*     */ {
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   private static final float DEFAULT_LOAD_FACTOR = 0.5F;
/*  45 */   private static final Object NULL_VALUE = new Object();
/*     */   
/*     */   private int maxSize;
/*     */   
/*     */   private final float loadFactor;
/*     */   
/*     */   private int[] keys;
/*     */   
/*     */   private V[] values;
/*     */   private Collection<V> valueCollection;
/*     */   private int size;
/*     */   
/*     */   public IntObjectHashMap()
/*     */   {
/*  59 */     this(11, 0.5F);
/*     */   }
/*     */   
/*     */   public IntObjectHashMap(int initialCapacity) {
/*  63 */     this(initialCapacity, 0.5F);
/*     */   }
/*     */   
/*     */   public IntObjectHashMap(int initialCapacity, float loadFactor) {
/*  67 */     if (initialCapacity < 1) {
/*  68 */       throw new IllegalArgumentException("initialCapacity must be >= 1");
/*     */     }
/*  70 */     if ((loadFactor <= 0.0F) || (loadFactor > 1.0F))
/*     */     {
/*     */ 
/*  73 */       throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
/*     */     }
/*     */     
/*  76 */     this.loadFactor = loadFactor;
/*     */     
/*     */ 
/*  79 */     int capacity = adjustCapacity(initialCapacity);
/*     */     
/*     */ 
/*  82 */     this.keys = new int[capacity];
/*     */     
/*  84 */     V[] temp = (Object[])new Object[capacity];
/*  85 */     this.values = temp;
/*     */     
/*     */ 
/*  88 */     this.maxSize = calcMaxSize(capacity);
/*     */   }
/*     */   
/*     */   private static <T> T toExternal(T value) {
/*  92 */     return value == NULL_VALUE ? null : value;
/*     */   }
/*     */   
/*     */   private static <T> T toInternal(T value)
/*     */   {
/*  97 */     return (T)(value == null ? NULL_VALUE : value);
/*     */   }
/*     */   
/*     */   public V get(int key)
/*     */   {
/* 102 */     int index = indexOf(key);
/* 103 */     return index == -1 ? null : toExternal(this.values[index]);
/*     */   }
/*     */   
/*     */   public V put(int key, V value)
/*     */   {
/* 108 */     int startIndex = hashIndex(key);
/* 109 */     int index = startIndex;
/*     */     do
/*     */     {
/* 112 */       if (this.values[index] == null)
/*     */       {
/* 114 */         this.keys[index] = key;
/* 115 */         this.values[index] = toInternal(value);
/* 116 */         growSize();
/* 117 */         return null;
/*     */       }
/* 119 */       if (this.keys[index] == key)
/*     */       {
/* 121 */         V previousValue = this.values[index];
/* 122 */         this.values[index] = toInternal(value);
/* 123 */         return (V)toExternal(previousValue);
/*     */       }
/*     */       
/*     */     }
/* 127 */     while ((index = probeNext(index)) != startIndex);
/*     */     
/* 129 */     throw new IllegalStateException("Unable to insert");
/*     */   }
/*     */   
/*     */ 
/*     */   private int probeNext(int index)
/*     */   {
/* 135 */     return index == this.values.length - 1 ? 0 : index + 1;
/*     */   }
/*     */   
/*     */   public void putAll(IntObjectMap<V> sourceMap)
/*     */   {
/* 140 */     if ((sourceMap instanceof IntObjectHashMap))
/*     */     {
/* 142 */       IntObjectHashMap<V> source = (IntObjectHashMap)sourceMap;
/* 143 */       for (int i = 0; i < source.values.length; i++) {
/* 144 */         V sourceValue = source.values[i];
/* 145 */         if (sourceValue != null) {
/* 146 */           put(source.keys[i], sourceValue);
/*     */         }
/*     */       }
/* 149 */       return;
/*     */     }
/*     */     
/*     */ 
/* 153 */     for (IntObjectMap.Entry<V> entry : sourceMap.entries()) {
/* 154 */       put(entry.key(), entry.value());
/*     */     }
/*     */   }
/*     */   
/*     */   public V remove(int key)
/*     */   {
/* 160 */     int index = indexOf(key);
/* 161 */     if (index == -1) {
/* 162 */       return null;
/*     */     }
/*     */     
/* 165 */     V prev = this.values[index];
/* 166 */     removeAt(index);
/* 167 */     return (V)toExternal(prev);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 172 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 177 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 182 */     Arrays.fill(this.keys, 0);
/* 183 */     Arrays.fill(this.values, null);
/* 184 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public boolean containsKey(int key)
/*     */   {
/* 189 */     return indexOf(key) >= 0;
/*     */   }
/*     */   
/*     */   public boolean containsValue(V value)
/*     */   {
/* 194 */     V v1 = toInternal(value);
/* 195 */     for (V v2 : this.values)
/*     */     {
/* 197 */       if ((v2 != null) && (v2.equals(v1))) {
/* 198 */         return true;
/*     */       }
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   public Iterable<IntObjectMap.Entry<V>> entries()
/*     */   {
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public Iterator<IntObjectMap.Entry<V>> iterator()
/*     */   {
/* 211 */     return new IteratorImpl(null);
/*     */   }
/*     */   
/*     */   public int[] keys()
/*     */   {
/* 216 */     int[] outKeys = new int[size()];
/* 217 */     int targetIx = 0;
/* 218 */     for (int i = 0; i < this.values.length; i++) {
/* 219 */       if (this.values[i] != null) {
/* 220 */         outKeys[(targetIx++)] = this.keys[i];
/*     */       }
/*     */     }
/* 223 */     return outKeys;
/*     */   }
/*     */   
/*     */ 
/*     */   public V[] values(Class<V> clazz)
/*     */   {
/* 229 */     V[] outValues = (Object[])Array.newInstance(clazz, size());
/* 230 */     int targetIx = 0;
/* 231 */     for (V value : this.values) {
/* 232 */       if (value != null) {
/* 233 */         outValues[(targetIx++)] = value;
/*     */       }
/*     */     }
/* 236 */     return outValues;
/*     */   }
/*     */   
/*     */   public Collection<V> values()
/*     */   {
/* 241 */     Collection<V> valueCollection = this.valueCollection;
/* 242 */     if (valueCollection == null) {
/* 243 */       this.valueCollection = ( = new AbstractCollection()
/*     */       {
/*     */         public Iterator<V> iterator() {
/* 246 */           new Iterator() {
/* 247 */             final Iterator<IntObjectMap.Entry<V>> iter = IntObjectHashMap.this.iterator();
/*     */             
/*     */             public boolean hasNext() {
/* 250 */               return this.iter.hasNext();
/*     */             }
/*     */             
/*     */             public V next()
/*     */             {
/* 255 */               return (V)((IntObjectMap.Entry)this.iter.next()).value();
/*     */             }
/*     */             
/*     */             public void remove()
/*     */             {
/* 260 */               throw new UnsupportedOperationException();
/*     */             }
/*     */           };
/*     */         }
/*     */         
/*     */         public int size()
/*     */         {
/* 267 */           return IntObjectHashMap.this.size;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 272 */     return valueCollection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 280 */     int hash = this.size;
/* 281 */     for (int key : this.keys)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 289 */       hash ^= key;
/*     */     }
/* 291 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 296 */     if (this == obj) {
/* 297 */       return true;
/*     */     }
/* 299 */     if (!(obj instanceof IntObjectMap)) {
/* 300 */       return false;
/*     */     }
/*     */     
/* 303 */     IntObjectMap other = (IntObjectMap)obj;
/* 304 */     if (this.size != other.size()) {
/* 305 */       return false;
/*     */     }
/* 307 */     for (int i = 0; i < this.values.length; i++) {
/* 308 */       V value = this.values[i];
/* 309 */       if (value != null) {
/* 310 */         int key = this.keys[i];
/* 311 */         Object otherValue = other.get(key);
/* 312 */         if (value == NULL_VALUE) {
/* 313 */           if (otherValue != null) {
/* 314 */             return false;
/*     */           }
/* 316 */         } else if (!value.equals(otherValue)) {
/* 317 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 321 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int indexOf(int key)
/*     */   {
/* 331 */     int startIndex = hashIndex(key);
/* 332 */     int index = startIndex;
/*     */     do
/*     */     {
/* 335 */       if (this.values[index] == null)
/*     */       {
/* 337 */         return -1;
/*     */       }
/* 339 */       if (key == this.keys[index]) {
/* 340 */         return index;
/*     */       }
/*     */       
/*     */     }
/* 344 */     while ((index = probeNext(index)) != startIndex);
/* 345 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int hashIndex(int key)
/*     */   {
/* 355 */     return (key % this.keys.length + this.keys.length) % this.keys.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void growSize()
/*     */   {
/* 362 */     this.size += 1;
/*     */     
/* 364 */     if (this.size > this.maxSize)
/*     */     {
/*     */ 
/* 367 */       rehash(adjustCapacity((int)Math.min(this.keys.length * 2.0D, 2.147483639E9D)));
/* 368 */     } else if (this.size == this.keys.length)
/*     */     {
/*     */ 
/* 371 */       rehash(this.keys.length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int adjustCapacity(int capacity)
/*     */   {
/* 379 */     return capacity | 0x1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeAt(int index)
/*     */   {
/* 389 */     this.size -= 1;
/*     */     
/*     */ 
/* 392 */     this.keys[index] = 0;
/* 393 */     this.values[index] = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 400 */     int nextFree = index;
/* 401 */     for (int i = probeNext(index); this.values[i] != null; i = probeNext(i)) {
/* 402 */       int bucket = hashIndex(this.keys[i]);
/* 403 */       if (((i < bucket) && ((bucket <= nextFree) || (nextFree <= i))) || ((bucket <= nextFree) && (nextFree <= i)))
/*     */       {
/*     */ 
/* 406 */         this.keys[nextFree] = this.keys[i];
/* 407 */         this.values[nextFree] = this.values[i];
/*     */         
/* 409 */         this.keys[i] = 0;
/* 410 */         this.values[i] = null;
/* 411 */         nextFree = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int calcMaxSize(int capacity)
/*     */   {
/* 421 */     int upperBound = capacity - 1;
/* 422 */     return Math.min(upperBound, (int)(capacity * this.loadFactor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void rehash(int newCapacity)
/*     */   {
/* 431 */     int[] oldKeys = this.keys;
/* 432 */     V[] oldVals = this.values;
/*     */     
/* 434 */     this.keys = new int[newCapacity];
/*     */     
/* 436 */     V[] temp = (Object[])new Object[newCapacity];
/* 437 */     this.values = temp;
/*     */     
/* 439 */     this.maxSize = calcMaxSize(newCapacity);
/*     */     
/*     */ 
/* 442 */     for (int i = 0; i < oldVals.length; i++) {
/* 443 */       V oldVal = oldVals[i];
/* 444 */       if (oldVal != null)
/*     */       {
/*     */ 
/* 447 */         int oldKey = oldKeys[i];
/* 448 */         int index = hashIndex(oldKey);
/*     */         for (;;)
/*     */         {
/* 451 */           if (this.values[index] == null) {
/* 452 */             this.keys[index] = oldKey;
/* 453 */             this.values[index] = toInternal(oldVal);
/* 454 */             break;
/*     */           }
/*     */           
/*     */ 
/* 458 */           index = probeNext(index);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class IteratorImpl
/*     */     implements Iterator<IntObjectMap.Entry<V>>, IntObjectMap.Entry<V>
/*     */   {
/* 468 */     private int prevIndex = -1;
/* 469 */     private int nextIndex = -1;
/* 470 */     private int entryIndex = -1;
/*     */     
/*     */     private IteratorImpl() {}
/*     */     
/* 474 */     private void scanNext() { while (++this.nextIndex != IntObjectHashMap.this.values.length) { if (IntObjectHashMap.this.values[this.nextIndex] != null) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 482 */       if (this.nextIndex == -1) {
/* 483 */         scanNext();
/*     */       }
/* 485 */       return this.nextIndex < IntObjectHashMap.this.keys.length;
/*     */     }
/*     */     
/*     */     public IntObjectMap.Entry<V> next()
/*     */     {
/* 490 */       if (!hasNext()) {
/* 491 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 494 */       this.prevIndex = this.nextIndex;
/* 495 */       scanNext();
/*     */       
/*     */ 
/* 498 */       this.entryIndex = this.prevIndex;
/* 499 */       return this;
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 504 */       if (this.prevIndex < 0) {
/* 505 */         throw new IllegalStateException("next must be called before each remove.");
/*     */       }
/* 507 */       IntObjectHashMap.this.removeAt(this.prevIndex);
/* 508 */       this.prevIndex = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int key()
/*     */     {
/* 516 */       return IntObjectHashMap.this.keys[this.entryIndex];
/*     */     }
/*     */     
/*     */     public V value()
/*     */     {
/* 521 */       return (V)IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
/*     */     }
/*     */     
/*     */     public void setValue(V value)
/*     */     {
/* 526 */       IntObjectHashMap.this.values[this.entryIndex] = IntObjectHashMap.toInternal(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 532 */     if (this.size == 0) {
/* 533 */       return "{}";
/*     */     }
/* 535 */     StringBuilder sb = new StringBuilder(4 * this.size);
/* 536 */     for (int i = 0; i < this.values.length; i++) {
/* 537 */       V value = this.values[i];
/* 538 */       if (value != null) {
/* 539 */         sb.append(sb.length() == 0 ? "{" : ", ");
/* 540 */         sb.append(keyToString(this.keys[i])).append('=').append(value == this ? "(this Map)" : value);
/*     */       }
/*     */     }
/* 543 */     return '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String keyToString(int key)
/*     */   {
/* 550 */     return Integer.toString(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\collection\IntObjectHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */