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
/*     */ public class EmptyBinaryHeaders
/*     */   extends EmptyHeaders<AsciiString>
/*     */   implements BinaryHeaders
/*     */ {
/*     */   public BinaryHeaders add(AsciiString name, AsciiString value)
/*     */   {
/*  25 */     super.add(name, value);
/*  26 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/*  31 */     super.add(name, values);
/*  32 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(AsciiString name, AsciiString... values)
/*     */   {
/*  37 */     super.add(name, values);
/*  38 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Object value)
/*     */   {
/*  43 */     super.addObject(name, value);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Iterable<?> values)
/*     */   {
/*  49 */     super.addObject(name, values);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Object... values)
/*     */   {
/*  55 */     super.addObject(name, values);
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addBoolean(AsciiString name, boolean value)
/*     */   {
/*  61 */     super.addBoolean(name, value);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addChar(AsciiString name, char value)
/*     */   {
/*  67 */     super.addChar(name, value);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addByte(AsciiString name, byte value)
/*     */   {
/*  73 */     super.addByte(name, value);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addShort(AsciiString name, short value)
/*     */   {
/*  79 */     super.addShort(name, value);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addInt(AsciiString name, int value)
/*     */   {
/*  85 */     super.addInt(name, value);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addLong(AsciiString name, long value)
/*     */   {
/*  91 */     super.addLong(name, value);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addFloat(AsciiString name, float value)
/*     */   {
/*  97 */     super.addFloat(name, value);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addDouble(AsciiString name, double value)
/*     */   {
/* 103 */     super.addDouble(name, value);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addTimeMillis(AsciiString name, long value)
/*     */   {
/* 109 */     super.addTimeMillis(name, value);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(BinaryHeaders headers)
/*     */   {
/* 115 */     super.add(headers);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, AsciiString value)
/*     */   {
/* 121 */     super.set(name, value);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/* 127 */     super.set(name, values);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, AsciiString... values)
/*     */   {
/* 133 */     super.set(name, values);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Object value)
/*     */   {
/* 139 */     super.setObject(name, value);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Iterable<?> values)
/*     */   {
/* 145 */     super.setObject(name, values);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Object... values)
/*     */   {
/* 151 */     super.setObject(name, values);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setBoolean(AsciiString name, boolean value)
/*     */   {
/* 157 */     super.setBoolean(name, value);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setChar(AsciiString name, char value)
/*     */   {
/* 163 */     super.setChar(name, value);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setByte(AsciiString name, byte value)
/*     */   {
/* 169 */     super.setByte(name, value);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setShort(AsciiString name, short value)
/*     */   {
/* 175 */     super.setShort(name, value);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setInt(AsciiString name, int value)
/*     */   {
/* 181 */     super.setInt(name, value);
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setLong(AsciiString name, long value)
/*     */   {
/* 187 */     super.setLong(name, value);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setFloat(AsciiString name, float value)
/*     */   {
/* 193 */     super.setFloat(name, value);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setDouble(AsciiString name, double value)
/*     */   {
/* 199 */     super.setDouble(name, value);
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setTimeMillis(AsciiString name, long value)
/*     */   {
/* 205 */     super.setTimeMillis(name, value);
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(BinaryHeaders headers)
/*     */   {
/* 211 */     super.set(headers);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setAll(BinaryHeaders headers)
/*     */   {
/* 217 */     super.setAll(headers);
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders clear()
/*     */   {
/* 223 */     super.clear();
/* 224 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\EmptyBinaryHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */