/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.EmptyTextHeaders;
/*     */ import io.netty.handler.codec.TextHeaders;
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
/*     */ public class EmptyHttpHeaders
/*     */   extends EmptyTextHeaders
/*     */   implements HttpHeaders
/*     */ {
/*  24 */   public static final EmptyHttpHeaders INSTANCE = new EmptyHttpHeaders();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpHeaders add(CharSequence name, CharSequence value)
/*     */   {
/*  31 */     super.add(name, value);
/*  32 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/*  37 */     super.add(name, values);
/*  38 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/*  43 */     super.add(name, values);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Object value)
/*     */   {
/*  49 */     super.addObject(name, value);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/*  55 */     super.addObject(name, values);
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Object... values)
/*     */   {
/*  61 */     super.addObject(name, values);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/*  67 */     super.addBoolean(name, value);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addChar(CharSequence name, char value)
/*     */   {
/*  73 */     super.addChar(name, value);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addByte(CharSequence name, byte value)
/*     */   {
/*  79 */     super.addByte(name, value);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addShort(CharSequence name, short value)
/*     */   {
/*  85 */     super.addShort(name, value);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addInt(CharSequence name, int value)
/*     */   {
/*  91 */     super.addInt(name, value);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addLong(CharSequence name, long value)
/*     */   {
/*  97 */     super.addLong(name, value);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addFloat(CharSequence name, float value)
/*     */   {
/* 103 */     super.addFloat(name, value);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 109 */     super.addDouble(name, value);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 115 */     super.addTimeMillis(name, value);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(TextHeaders headers)
/*     */   {
/* 121 */     super.add(headers);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 127 */     super.set(name, value);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 133 */     super.set(name, values);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 139 */     super.set(name, values);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 145 */     super.setObject(name, value);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 151 */     super.setObject(name, values);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 157 */     super.setObject(name, values);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 163 */     super.setBoolean(name, value);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setChar(CharSequence name, char value)
/*     */   {
/* 169 */     super.setChar(name, value);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 175 */     super.setByte(name, value);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setShort(CharSequence name, short value)
/*     */   {
/* 181 */     super.setShort(name, value);
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setInt(CharSequence name, int value)
/*     */   {
/* 187 */     super.setInt(name, value);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setLong(CharSequence name, long value)
/*     */   {
/* 193 */     super.setLong(name, value);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 199 */     super.setFloat(name, value);
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 205 */     super.setDouble(name, value);
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 211 */     super.setTimeMillis(name, value);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(TextHeaders headers)
/*     */   {
/* 217 */     super.set(headers);
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setAll(TextHeaders headers)
/*     */   {
/* 223 */     super.setAll(headers);
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders clear()
/*     */   {
/* 229 */     super.clear();
/* 230 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\EmptyHttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */