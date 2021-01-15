/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocalAccess;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class InternalThreadLocalMap
/*     */   extends UnpaddedInternalThreadLocalMap
/*     */ {
/*  37 */   public static final Object UNSET = new Object();
/*     */   public long rp1;
/*     */   
/*  40 */   public static InternalThreadLocalMap getIfSet() { Thread thread = Thread.currentThread();
/*     */     InternalThreadLocalMap threadLocalMap;
/*  42 */     InternalThreadLocalMap threadLocalMap; if ((thread instanceof FastThreadLocalAccess)) {
/*  43 */       threadLocalMap = ((FastThreadLocalAccess)thread).threadLocalMap();
/*     */     } else {
/*  45 */       ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
/*  46 */       InternalThreadLocalMap threadLocalMap; if (slowThreadLocalMap == null) {
/*  47 */         threadLocalMap = null;
/*     */       } else {
/*  49 */         threadLocalMap = (InternalThreadLocalMap)slowThreadLocalMap.get();
/*     */       }
/*     */     }
/*  52 */     return threadLocalMap; }
/*     */   
/*     */   public long rp2;
/*     */   
/*  56 */   public static InternalThreadLocalMap get() { Thread thread = Thread.currentThread();
/*  57 */     if ((thread instanceof FastThreadLocalAccess)) {
/*  58 */       return fastGet((FastThreadLocalAccess)thread);
/*     */     }
/*  60 */     return slowGet();
/*     */   }
/*     */   
/*     */   public long rp3;
/*     */   public long rp4;
/*  65 */   private static InternalThreadLocalMap fastGet(FastThreadLocalAccess thread) { InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
/*  66 */     if (threadLocalMap == null) {
/*  67 */       thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
/*     */     }
/*  69 */     return threadLocalMap;
/*     */   }
/*     */   
/*     */   private static InternalThreadLocalMap slowGet() {
/*  73 */     ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
/*  74 */     if (slowThreadLocalMap == null) {
/*  75 */       UnpaddedInternalThreadLocalMap.slowThreadLocalMap = slowThreadLocalMap = new ThreadLocal();
/*     */     }
/*     */     
/*     */ 
/*  79 */     InternalThreadLocalMap ret = (InternalThreadLocalMap)slowThreadLocalMap.get();
/*  80 */     if (ret == null) {
/*  81 */       ret = new InternalThreadLocalMap();
/*  82 */       slowThreadLocalMap.set(ret);
/*     */     }
/*  84 */     return ret;
/*     */   }
/*     */   
/*     */   public static void remove() {
/*  88 */     Thread thread = Thread.currentThread();
/*  89 */     if ((thread instanceof FastThreadLocalAccess)) {
/*  90 */       ((FastThreadLocalAccess)thread).setThreadLocalMap(null);
/*     */     } else {
/*  92 */       ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
/*  93 */       if (slowThreadLocalMap != null) {
/*  94 */         slowThreadLocalMap.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void destroy() {
/* 100 */     slowThreadLocalMap = null;
/*     */   }
/*     */   
/*     */   public static int nextVariableIndex() {
/* 104 */     int index = nextIndex.getAndIncrement();
/* 105 */     if (index < 0) {
/* 106 */       nextIndex.decrementAndGet();
/* 107 */       throw new IllegalStateException("too many thread-local indexed variables");
/*     */     }
/* 109 */     return index;
/*     */   }
/*     */   
/*     */   public static int lastVariableIndex() {
/* 113 */     return nextIndex.get() - 1;
/*     */   }
/*     */   
/*     */   public long rp5;
/*     */   public long rp6;
/*     */   public long rp7;
/*     */   public long rp8;
/*     */   public long rp9;
/* 121 */   private InternalThreadLocalMap() { super(newIndexedVariableTable()); }
/*     */   
/*     */ 
/*     */   private static Object[] newIndexedVariableTable() {
/* 125 */     Object[] array = new Object[32];
/* 126 */     Arrays.fill(array, UNSET);
/* 127 */     return array;
/*     */   }
/*     */   
/*     */   public int size() {
/* 131 */     int count = 0;
/*     */     
/* 133 */     if (this.futureListenerStackDepth != 0) {
/* 134 */       count++;
/*     */     }
/* 136 */     if (this.localChannelReaderStackDepth != 0) {
/* 137 */       count++;
/*     */     }
/* 139 */     if (this.handlerSharableCache != null) {
/* 140 */       count++;
/*     */     }
/* 142 */     if (this.counterHashCode != null) {
/* 143 */       count++;
/*     */     }
/* 145 */     if (this.random != null) {
/* 146 */       count++;
/*     */     }
/* 148 */     if (this.typeParameterMatcherGetCache != null) {
/* 149 */       count++;
/*     */     }
/* 151 */     if (this.typeParameterMatcherFindCache != null) {
/* 152 */       count++;
/*     */     }
/* 154 */     if (this.stringBuilder != null) {
/* 155 */       count++;
/*     */     }
/* 157 */     if (this.charsetEncoderCache != null) {
/* 158 */       count++;
/*     */     }
/* 160 */     if (this.charsetDecoderCache != null) {
/* 161 */       count++;
/*     */     }
/*     */     
/* 164 */     for (Object o : this.indexedVariables) {
/* 165 */       if (o != UNSET) {
/* 166 */         count++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 172 */     return count - 1;
/*     */   }
/*     */   
/*     */   public StringBuilder stringBuilder() {
/* 176 */     StringBuilder builder = this.stringBuilder;
/* 177 */     if (builder == null) {
/* 178 */       this.stringBuilder = (builder = new StringBuilder(512));
/*     */     } else {
/* 180 */       builder.setLength(0);
/*     */     }
/* 182 */     return builder;
/*     */   }
/*     */   
/*     */   public Map<Charset, CharsetEncoder> charsetEncoderCache() {
/* 186 */     Map<Charset, CharsetEncoder> cache = this.charsetEncoderCache;
/* 187 */     if (cache == null) {
/* 188 */       this.charsetEncoderCache = (cache = new IdentityHashMap());
/*     */     }
/* 190 */     return cache;
/*     */   }
/*     */   
/*     */   public Map<Charset, CharsetDecoder> charsetDecoderCache() {
/* 194 */     Map<Charset, CharsetDecoder> cache = this.charsetDecoderCache;
/* 195 */     if (cache == null) {
/* 196 */       this.charsetDecoderCache = (cache = new IdentityHashMap());
/*     */     }
/* 198 */     return cache;
/*     */   }
/*     */   
/*     */   public int futureListenerStackDepth() {
/* 202 */     return this.futureListenerStackDepth;
/*     */   }
/*     */   
/*     */   public void setFutureListenerStackDepth(int futureListenerStackDepth) {
/* 206 */     this.futureListenerStackDepth = futureListenerStackDepth;
/*     */   }
/*     */   
/*     */   public ThreadLocalRandom random() {
/* 210 */     ThreadLocalRandom r = this.random;
/* 211 */     if (r == null) {
/* 212 */       this.random = (r = new ThreadLocalRandom());
/*     */     }
/* 214 */     return r;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
/* 218 */     Map<Class<?>, TypeParameterMatcher> cache = this.typeParameterMatcherGetCache;
/* 219 */     if (cache == null) {
/* 220 */       this.typeParameterMatcherGetCache = (cache = new IdentityHashMap());
/*     */     }
/* 222 */     return cache;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
/* 226 */     Map<Class<?>, Map<String, TypeParameterMatcher>> cache = this.typeParameterMatcherFindCache;
/* 227 */     if (cache == null) {
/* 228 */       this.typeParameterMatcherFindCache = (cache = new IdentityHashMap());
/*     */     }
/* 230 */     return cache;
/*     */   }
/*     */   
/*     */   public IntegerHolder counterHashCode() {
/* 234 */     return this.counterHashCode;
/*     */   }
/*     */   
/*     */   public void setCounterHashCode(IntegerHolder counterHashCode) {
/* 238 */     this.counterHashCode = counterHashCode;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, Boolean> handlerSharableCache() {
/* 242 */     Map<Class<?>, Boolean> cache = this.handlerSharableCache;
/* 243 */     if (cache == null)
/*     */     {
/* 245 */       this.handlerSharableCache = (cache = new WeakHashMap(4));
/*     */     }
/* 247 */     return cache;
/*     */   }
/*     */   
/*     */   public int localChannelReaderStackDepth() {
/* 251 */     return this.localChannelReaderStackDepth;
/*     */   }
/*     */   
/*     */   public void setLocalChannelReaderStackDepth(int localChannelReaderStackDepth) {
/* 255 */     this.localChannelReaderStackDepth = localChannelReaderStackDepth;
/*     */   }
/*     */   
/*     */   public Object indexedVariable(int index) {
/* 259 */     Object[] lookup = this.indexedVariables;
/* 260 */     return index < lookup.length ? lookup[index] : UNSET;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean setIndexedVariable(int index, Object value)
/*     */   {
/* 267 */     Object[] lookup = this.indexedVariables;
/* 268 */     if (index < lookup.length) {
/* 269 */       Object oldValue = lookup[index];
/* 270 */       lookup[index] = value;
/* 271 */       return oldValue == UNSET;
/*     */     }
/* 273 */     expandIndexedVariableTableAndSet(index, value);
/* 274 */     return true;
/*     */   }
/*     */   
/*     */   private void expandIndexedVariableTableAndSet(int index, Object value)
/*     */   {
/* 279 */     Object[] oldArray = this.indexedVariables;
/* 280 */     int oldCapacity = oldArray.length;
/* 281 */     int newCapacity = index;
/* 282 */     newCapacity |= newCapacity >>> 1;
/* 283 */     newCapacity |= newCapacity >>> 2;
/* 284 */     newCapacity |= newCapacity >>> 4;
/* 285 */     newCapacity |= newCapacity >>> 8;
/* 286 */     newCapacity |= newCapacity >>> 16;
/* 287 */     newCapacity++;
/*     */     
/* 289 */     Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
/* 290 */     Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
/* 291 */     newArray[index] = value;
/* 292 */     this.indexedVariables = newArray;
/*     */   }
/*     */   
/*     */   public Object removeIndexedVariable(int index) {
/* 296 */     Object[] lookup = this.indexedVariables;
/* 297 */     if (index < lookup.length) {
/* 298 */       Object v = lookup[index];
/* 299 */       lookup[index] = UNSET;
/* 300 */       return v;
/*     */     }
/* 302 */     return UNSET;
/*     */   }
/*     */   
/*     */   public boolean isIndexedVariableSet(int index)
/*     */   {
/* 307 */     Object[] lookup = this.indexedVariables;
/* 308 */     return (index < lookup.length) && (lookup[index] != UNSET);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\InternalThreadLocalMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */