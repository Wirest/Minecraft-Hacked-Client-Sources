/*     */ package io.netty.handler.codec;
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
/*     */ public class EmptyTextHeaders
/*     */   extends EmptyConvertibleHeaders<CharSequence, String>
/*     */   implements TextHeaders
/*     */ {
/*     */   public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase)
/*     */   {
/*  25 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsObject(CharSequence name, Object value, boolean ignoreCase)
/*     */   {
/*  30 */     return false;
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, CharSequence value)
/*     */   {
/*  35 */     super.add(name, value);
/*  36 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/*  41 */     super.add(name, values);
/*  42 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/*  47 */     super.add(name, values);
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Object value)
/*     */   {
/*  53 */     super.addObject(name, value);
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/*  59 */     super.addObject(name, values);
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Object... values)
/*     */   {
/*  65 */     super.addObject(name, values);
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/*  71 */     super.addBoolean(name, value);
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addChar(CharSequence name, char value)
/*     */   {
/*  77 */     super.addChar(name, value);
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addByte(CharSequence name, byte value)
/*     */   {
/*  83 */     super.addByte(name, value);
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addShort(CharSequence name, short value)
/*     */   {
/*  89 */     super.addShort(name, value);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addInt(CharSequence name, int value)
/*     */   {
/*  95 */     super.addInt(name, value);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addLong(CharSequence name, long value)
/*     */   {
/* 101 */     super.addLong(name, value);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addFloat(CharSequence name, float value)
/*     */   {
/* 107 */     super.addFloat(name, value);
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 113 */     super.addDouble(name, value);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 119 */     super.addTimeMillis(name, value);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders add(TextHeaders headers)
/*     */   {
/* 125 */     super.add(headers);
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 131 */     super.set(name, value);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 137 */     super.set(name, values);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 143 */     super.set(name, values);
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 149 */     super.setObject(name, value);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 155 */     super.setObject(name, values);
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 161 */     super.setObject(name, values);
/* 162 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 167 */     super.setBoolean(name, value);
/* 168 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setChar(CharSequence name, char value)
/*     */   {
/* 173 */     super.setChar(name, value);
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 179 */     super.setByte(name, value);
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setShort(CharSequence name, short value)
/*     */   {
/* 185 */     super.setShort(name, value);
/* 186 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setInt(CharSequence name, int value)
/*     */   {
/* 191 */     super.setInt(name, value);
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setLong(CharSequence name, long value)
/*     */   {
/* 197 */     super.setLong(name, value);
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 203 */     super.setFloat(name, value);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 209 */     super.setDouble(name, value);
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 215 */     super.setTimeMillis(name, value);
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(TextHeaders headers)
/*     */   {
/* 221 */     super.set(headers);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setAll(TextHeaders headers)
/*     */   {
/* 227 */     super.setAll(headers);
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders clear()
/*     */   {
/* 233 */     super.clear();
/* 234 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\EmptyTextHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */