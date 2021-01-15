/*     */ package io.netty.handler.codec.stomp;
/*     */ 
/*     */ import io.netty.handler.codec.DefaultTextHeaders;
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
/*     */ public class DefaultStompHeaders
/*     */   extends DefaultTextHeaders
/*     */   implements StompHeaders
/*     */ {
/*     */   public StompHeaders add(CharSequence name, CharSequence value)
/*     */   {
/*  26 */     super.add(name, value);
/*  27 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/*  32 */     super.add(name, values);
/*  33 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/*  38 */     super.add(name, values);
/*  39 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addObject(CharSequence name, Object value)
/*     */   {
/*  44 */     super.addObject(name, value);
/*  45 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/*  50 */     super.addObject(name, values);
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addObject(CharSequence name, Object... values)
/*     */   {
/*  56 */     super.addObject(name, values);
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/*  62 */     super.addBoolean(name, value);
/*  63 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addChar(CharSequence name, char value)
/*     */   {
/*  68 */     super.addChar(name, value);
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addByte(CharSequence name, byte value)
/*     */   {
/*  74 */     super.addByte(name, value);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addShort(CharSequence name, short value)
/*     */   {
/*  80 */     super.addShort(name, value);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addInt(CharSequence name, int value)
/*     */   {
/*  86 */     super.addInt(name, value);
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addLong(CharSequence name, long value)
/*     */   {
/*  92 */     super.addLong(name, value);
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addFloat(CharSequence name, float value)
/*     */   {
/*  98 */     super.addFloat(name, value);
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 104 */     super.addDouble(name, value);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 110 */     super.addTimeMillis(name, value);
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders add(TextHeaders headers)
/*     */   {
/* 116 */     super.add(headers);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 122 */     super.set(name, value);
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 128 */     super.set(name, values);
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 134 */     super.set(name, values);
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 140 */     super.setObject(name, value);
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 146 */     super.setObject(name, values);
/* 147 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 152 */     super.setObject(name, values);
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 158 */     super.setBoolean(name, value);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setChar(CharSequence name, char value)
/*     */   {
/* 164 */     super.setChar(name, value);
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 170 */     super.setByte(name, value);
/* 171 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setShort(CharSequence name, short value)
/*     */   {
/* 176 */     super.setShort(name, value);
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setInt(CharSequence name, int value)
/*     */   {
/* 182 */     super.setInt(name, value);
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setLong(CharSequence name, long value)
/*     */   {
/* 188 */     super.setLong(name, value);
/* 189 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 194 */     super.setFloat(name, value);
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 200 */     super.setDouble(name, value);
/* 201 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 206 */     super.setTimeMillis(name, value);
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders set(TextHeaders headers)
/*     */   {
/* 212 */     super.set(headers);
/* 213 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders setAll(TextHeaders headers)
/*     */   {
/* 218 */     super.setAll(headers);
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public StompHeaders clear()
/*     */   {
/* 224 */     super.clear();
/* 225 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\DefaultStompHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */