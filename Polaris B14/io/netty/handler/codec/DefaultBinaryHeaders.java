/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.text.ParseException;
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
/*     */ public class DefaultBinaryHeaders
/*     */   extends DefaultHeaders<AsciiString>
/*     */   implements BinaryHeaders
/*     */ {
/*  24 */   private static final DefaultHeaders.HashCodeGenerator<AsciiString> ASCII_HASH_CODE_GENERATOR = new DefaultHeaders.HashCodeGenerator()
/*     */   {
/*     */     public int generateHashCode(AsciiString name)
/*     */     {
/*  28 */       return AsciiString.caseInsensitiveHashCode(name);
/*     */     }
/*     */   };
/*     */   
/*  32 */   private static final Headers.ValueConverter<AsciiString> OBJECT_TO_ASCII = new Headers.ValueConverter()
/*     */   {
/*     */     public AsciiString convertObject(Object value) {
/*  35 */       if ((value instanceof AsciiString)) {
/*  36 */         return (AsciiString)value;
/*     */       }
/*  38 */       if ((value instanceof CharSequence)) {
/*  39 */         return new AsciiString((CharSequence)value);
/*     */       }
/*  41 */       return new AsciiString(value.toString());
/*     */     }
/*     */     
/*     */     public AsciiString convertInt(int value)
/*     */     {
/*  46 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public AsciiString convertLong(long value)
/*     */     {
/*  51 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public AsciiString convertDouble(double value)
/*     */     {
/*  56 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public AsciiString convertChar(char value)
/*     */     {
/*  61 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public AsciiString convertBoolean(boolean value)
/*     */     {
/*  66 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public AsciiString convertFloat(float value)
/*     */     {
/*  71 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public int convertToInt(AsciiString value)
/*     */     {
/*  76 */       return value.parseInt();
/*     */     }
/*     */     
/*     */     public long convertToLong(AsciiString value)
/*     */     {
/*  81 */       return value.parseLong();
/*     */     }
/*     */     
/*     */     public AsciiString convertTimeMillis(long value)
/*     */     {
/*  86 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public long convertToTimeMillis(AsciiString value)
/*     */     {
/*     */       try {
/*  92 */         return DefaultHeaders.HeaderDateFormat.get().parse(value.toString());
/*     */       } catch (ParseException e) {
/*  94 */         PlatformDependent.throwException(e);
/*     */       }
/*  96 */       return 0L;
/*     */     }
/*     */     
/*     */     public double convertToDouble(AsciiString value)
/*     */     {
/* 101 */       return value.parseDouble();
/*     */     }
/*     */     
/*     */     public char convertToChar(AsciiString value)
/*     */     {
/* 106 */       return value.charAt(0);
/*     */     }
/*     */     
/*     */     public boolean convertToBoolean(AsciiString value)
/*     */     {
/* 111 */       return value.byteAt(0) != 0;
/*     */     }
/*     */     
/*     */     public float convertToFloat(AsciiString value)
/*     */     {
/* 116 */       return value.parseFloat();
/*     */     }
/*     */     
/*     */     public AsciiString convertShort(short value)
/*     */     {
/* 121 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public short convertToShort(AsciiString value)
/*     */     {
/* 126 */       return value.parseShort();
/*     */     }
/*     */     
/*     */     public AsciiString convertByte(byte value)
/*     */     {
/* 131 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public byte convertToByte(AsciiString value)
/*     */     {
/* 136 */       return value.byteAt(0);
/*     */     }
/*     */   };
/*     */   
/* 140 */   private static final DefaultHeaders.NameConverter<AsciiString> ASCII_TO_LOWER_CONVERTER = new DefaultHeaders.NameConverter()
/*     */   {
/*     */     public AsciiString convertName(AsciiString name) {
/* 143 */       return name.toLowerCase();
/*     */     }
/*     */   };
/*     */   
/* 147 */   private static final DefaultHeaders.NameConverter<AsciiString> ASCII_IDENTITY_CONVERTER = new DefaultHeaders.NameConverter()
/*     */   {
/*     */     public AsciiString convertName(AsciiString name) {
/* 150 */       return name;
/*     */     }
/*     */   };
/*     */   
/*     */   public DefaultBinaryHeaders() {
/* 155 */     this(false);
/*     */   }
/*     */   
/*     */   public DefaultBinaryHeaders(boolean forceKeyToLower) {
/* 159 */     super(AsciiString.CASE_INSENSITIVE_ORDER, AsciiString.CASE_INSENSITIVE_ORDER, ASCII_HASH_CODE_GENERATOR, OBJECT_TO_ASCII, forceKeyToLower ? ASCII_TO_LOWER_CONVERTER : ASCII_IDENTITY_CONVERTER);
/*     */   }
/*     */   
/*     */ 
/*     */   public BinaryHeaders add(AsciiString name, AsciiString value)
/*     */   {
/* 165 */     super.add(name, value);
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/* 171 */     super.add(name, values);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(AsciiString name, AsciiString... values)
/*     */   {
/* 177 */     super.add(name, values);
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Object value)
/*     */   {
/* 183 */     super.addObject(name, value);
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Iterable<?> values)
/*     */   {
/* 189 */     super.addObject(name, values);
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addObject(AsciiString name, Object... values)
/*     */   {
/* 195 */     super.addObject(name, values);
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addBoolean(AsciiString name, boolean value)
/*     */   {
/* 201 */     super.addBoolean(name, value);
/* 202 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addChar(AsciiString name, char value)
/*     */   {
/* 207 */     super.addChar(name, value);
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addByte(AsciiString name, byte value)
/*     */   {
/* 213 */     super.addByte(name, value);
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addShort(AsciiString name, short value)
/*     */   {
/* 219 */     super.addShort(name, value);
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addInt(AsciiString name, int value)
/*     */   {
/* 225 */     super.addInt(name, value);
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addLong(AsciiString name, long value)
/*     */   {
/* 231 */     super.addLong(name, value);
/* 232 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addFloat(AsciiString name, float value)
/*     */   {
/* 237 */     super.addFloat(name, value);
/* 238 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addDouble(AsciiString name, double value)
/*     */   {
/* 243 */     super.addDouble(name, value);
/* 244 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders addTimeMillis(AsciiString name, long value)
/*     */   {
/* 249 */     super.addTimeMillis(name, value);
/* 250 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders add(BinaryHeaders headers)
/*     */   {
/* 255 */     super.add(headers);
/* 256 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, AsciiString value)
/*     */   {
/* 261 */     super.set(name, value);
/* 262 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/* 267 */     super.set(name, values);
/* 268 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(AsciiString name, AsciiString... values)
/*     */   {
/* 273 */     super.set(name, values);
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Object value)
/*     */   {
/* 279 */     super.setObject(name, value);
/* 280 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Iterable<?> values)
/*     */   {
/* 285 */     super.setObject(name, values);
/* 286 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setObject(AsciiString name, Object... values)
/*     */   {
/* 291 */     super.setObject(name, values);
/* 292 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setBoolean(AsciiString name, boolean value)
/*     */   {
/* 297 */     super.setBoolean(name, value);
/* 298 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setChar(AsciiString name, char value)
/*     */   {
/* 303 */     super.setChar(name, value);
/* 304 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setByte(AsciiString name, byte value)
/*     */   {
/* 309 */     super.setByte(name, value);
/* 310 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setShort(AsciiString name, short value)
/*     */   {
/* 315 */     super.setShort(name, value);
/* 316 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setInt(AsciiString name, int value)
/*     */   {
/* 321 */     super.setInt(name, value);
/* 322 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setLong(AsciiString name, long value)
/*     */   {
/* 327 */     super.setLong(name, value);
/* 328 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setFloat(AsciiString name, float value)
/*     */   {
/* 333 */     super.setFloat(name, value);
/* 334 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setDouble(AsciiString name, double value)
/*     */   {
/* 339 */     super.setDouble(name, value);
/* 340 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setTimeMillis(AsciiString name, long value)
/*     */   {
/* 345 */     super.setTimeMillis(name, value);
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders set(BinaryHeaders headers)
/*     */   {
/* 351 */     super.set(headers);
/* 352 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders setAll(BinaryHeaders headers)
/*     */   {
/* 357 */     super.setAll(headers);
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryHeaders clear()
/*     */   {
/* 363 */     super.clear();
/* 364 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\DefaultBinaryHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */