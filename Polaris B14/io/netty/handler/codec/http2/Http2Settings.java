/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public final class Http2Settings
/*     */   extends IntObjectHashMap<Long>
/*     */ {
/*     */   public Http2Settings()
/*     */   {
/*  44 */     this(6);
/*     */   }
/*     */   
/*     */   public Http2Settings(int initialCapacity, float loadFactor) {
/*  48 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */   
/*     */   public Http2Settings(int initialCapacity) {
/*  52 */     super(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Long put(int key, Long value)
/*     */   {
/*  62 */     verifyStandardSetting(key, value);
/*  63 */     return (Long)super.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Long headerTableSize()
/*     */   {
/*  70 */     return (Long)get(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Settings headerTableSize(int value)
/*     */   {
/*  79 */     put(1, Long.valueOf(value));
/*  80 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Boolean pushEnabled()
/*     */   {
/*  87 */     Long value = (Long)get(2);
/*  88 */     if (value == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     return Boolean.valueOf(value.longValue() != 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Settings pushEnabled(boolean enabled)
/*     */   {
/*  98 */     put(2, Long.valueOf(enabled ? 1L : 0L));
/*  99 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Long maxConcurrentStreams()
/*     */   {
/* 106 */     return (Long)get(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Settings maxConcurrentStreams(long value)
/*     */   {
/* 115 */     put(3, Long.valueOf(value));
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer initialWindowSize()
/*     */   {
/* 123 */     return getIntValue(4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Settings initialWindowSize(int value)
/*     */   {
/* 132 */     put(4, Long.valueOf(value));
/* 133 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer maxFrameSize()
/*     */   {
/* 140 */     return getIntValue(5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Settings maxFrameSize(int value)
/*     */   {
/* 149 */     put(5, Long.valueOf(value));
/* 150 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer maxHeaderListSize()
/*     */   {
/* 157 */     return getIntValue(6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Settings maxHeaderListSize(int value)
/*     */   {
/* 166 */     put(6, Long.valueOf(value));
/* 167 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Settings copyFrom(Http2Settings settings)
/*     */   {
/* 174 */     clear();
/* 175 */     putAll(settings);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   Integer getIntValue(int key) {
/* 180 */     Long value = (Long)get(key);
/* 181 */     if (value == null) {
/* 182 */       return null;
/*     */     }
/* 184 */     return Integer.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */   private static void verifyStandardSetting(int key, Long value) {
/* 188 */     ObjectUtil.checkNotNull(value, "value");
/* 189 */     switch (key) {
/*     */     case 1: 
/* 191 */       if ((value.longValue() < 0L) || (value.longValue() > 2147483647L)) {
/* 192 */         throw new IllegalArgumentException("Setting HEADER_TABLE_SIZE is invalid: " + value);
/*     */       }
/*     */       break;
/*     */     case 2: 
/* 196 */       if ((value.longValue() != 0L) && (value.longValue() != 1L)) {
/* 197 */         throw new IllegalArgumentException("Setting ENABLE_PUSH is invalid: " + value);
/*     */       }
/*     */       break;
/*     */     case 3: 
/* 201 */       if ((value.longValue() < 0L) || (value.longValue() > 4294967295L)) {
/* 202 */         throw new IllegalArgumentException("Setting MAX_CONCURRENT_STREAMS is invalid: " + value);
/*     */       }
/*     */       
/*     */       break;
/*     */     case 4: 
/* 207 */       if ((value.longValue() < 0L) || (value.longValue() > 2147483647L)) {
/* 208 */         throw new IllegalArgumentException("Setting INITIAL_WINDOW_SIZE is invalid: " + value);
/*     */       }
/*     */       
/*     */       break;
/*     */     case 5: 
/* 213 */       if (!Http2CodecUtil.isMaxFrameSizeValid(value.intValue())) {
/* 214 */         throw new IllegalArgumentException("Setting MAX_FRAME_SIZE is invalid: " + value);
/*     */       }
/*     */       break;
/*     */     case 6: 
/* 218 */       if ((value.longValue() < 0L) || (value.longValue() > Long.MAX_VALUE)) {
/* 219 */         throw new IllegalArgumentException("Setting MAX_HEADER_LIST_SIZE is invalid: " + value);
/*     */       }
/*     */       break;
/*     */     default: 
/* 223 */       throw new IllegalArgumentException("key");
/*     */     }
/*     */   }
/*     */   
/*     */   protected String keyToString(int key)
/*     */   {
/* 229 */     switch (key) {
/*     */     case 1: 
/* 231 */       return "HEADER_TABLE_SIZE";
/*     */     case 2: 
/* 233 */       return "ENABLE_PUSH";
/*     */     case 3: 
/* 235 */       return "MAX_CONCURRENT_STREAMS";
/*     */     case 4: 
/* 237 */       return "INITIAL_WINDOW_SIZE";
/*     */     case 5: 
/* 239 */       return "MAX_FRAME_SIZE";
/*     */     case 6: 
/* 241 */       return "MAX_HEADER_LIST_SIZE";
/*     */     }
/*     */     
/* 244 */     return super.keyToString(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Settings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */