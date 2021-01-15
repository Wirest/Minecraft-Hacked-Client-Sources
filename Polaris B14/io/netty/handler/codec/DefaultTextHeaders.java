/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.text.ParseException;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ public class DefaultTextHeaders
/*     */   extends DefaultConvertibleHeaders<CharSequence, String>
/*     */   implements TextHeaders
/*     */ {
/*  30 */   private static final DefaultHeaders.HashCodeGenerator<CharSequence> CHARSEQUECE_CASE_INSENSITIVE_HASH_CODE_GENERATOR = new DefaultHeaders.HashCodeGenerator()
/*     */   {
/*     */     public int generateHashCode(CharSequence name)
/*     */     {
/*  34 */       return AsciiString.caseInsensitiveHashCode(name);
/*     */     }
/*     */   };
/*     */   
/*  38 */   private static final DefaultHeaders.HashCodeGenerator<CharSequence> CHARSEQUECE_CASE_SENSITIVE_HASH_CODE_GENERATOR = new DefaultHeaders.HashCodeGenerator()
/*     */   {
/*     */     public int generateHashCode(CharSequence name)
/*     */     {
/*  42 */       return name.hashCode();
/*     */     }
/*     */   };
/*     */   
/*     */   public static class DefaultTextValueTypeConverter implements Headers.ValueConverter<CharSequence>
/*     */   {
/*     */     public CharSequence convertObject(Object value) {
/*  49 */       if ((value instanceof CharSequence)) {
/*  50 */         return (CharSequence)value;
/*     */       }
/*  52 */       return value.toString();
/*     */     }
/*     */     
/*     */     public CharSequence convertInt(int value)
/*     */     {
/*  57 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public CharSequence convertLong(long value)
/*     */     {
/*  62 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public CharSequence convertDouble(double value)
/*     */     {
/*  67 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public CharSequence convertChar(char value)
/*     */     {
/*  72 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public CharSequence convertBoolean(boolean value)
/*     */     {
/*  77 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public CharSequence convertFloat(float value)
/*     */     {
/*  82 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public boolean convertToBoolean(CharSequence value)
/*     */     {
/*  87 */       return Boolean.parseBoolean(value.toString());
/*     */     }
/*     */     
/*     */     public CharSequence convertByte(byte value)
/*     */     {
/*  92 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public byte convertToByte(CharSequence value)
/*     */     {
/*  97 */       return Byte.valueOf(value.toString()).byteValue();
/*     */     }
/*     */     
/*     */     public char convertToChar(CharSequence value)
/*     */     {
/* 102 */       return value.charAt(0);
/*     */     }
/*     */     
/*     */     public CharSequence convertShort(short value)
/*     */     {
/* 107 */       return String.valueOf(value);
/*     */     }
/*     */     
/*     */     public short convertToShort(CharSequence value)
/*     */     {
/* 112 */       return Short.valueOf(value.toString()).shortValue();
/*     */     }
/*     */     
/*     */     public int convertToInt(CharSequence value)
/*     */     {
/* 117 */       return Integer.parseInt(value.toString());
/*     */     }
/*     */     
/*     */     public long convertToLong(CharSequence value)
/*     */     {
/* 122 */       return Long.parseLong(value.toString());
/*     */     }
/*     */     
/*     */     public AsciiString convertTimeMillis(long value)
/*     */     {
/* 127 */       return new AsciiString(String.valueOf(value));
/*     */     }
/*     */     
/*     */     public long convertToTimeMillis(CharSequence value)
/*     */     {
/*     */       try {
/* 133 */         return DefaultHeaders.HeaderDateFormat.get().parse(value.toString());
/*     */       } catch (ParseException e) {
/* 135 */         PlatformDependent.throwException(e);
/*     */       }
/* 137 */       return 0L;
/*     */     }
/*     */     
/*     */     public float convertToFloat(CharSequence value)
/*     */     {
/* 142 */       return Float.valueOf(value.toString()).floatValue();
/*     */     }
/*     */     
/*     */     public double convertToDouble(CharSequence value)
/*     */     {
/* 147 */       return Double.valueOf(value.toString()).doubleValue();
/*     */     }
/*     */   }
/*     */   
/* 151 */   private static final Headers.ValueConverter<CharSequence> CHARSEQUENCE_FROM_OBJECT_CONVERTER = new DefaultTextValueTypeConverter();
/*     */   
/* 153 */   private static final ConvertibleHeaders.TypeConverter<CharSequence, String> CHARSEQUENCE_TO_STRING_CONVERTER = new ConvertibleHeaders.TypeConverter()
/*     */   {
/*     */     public String toConvertedType(CharSequence value)
/*     */     {
/* 157 */       return value.toString();
/*     */     }
/*     */     
/*     */     public CharSequence toUnconvertedType(String value)
/*     */     {
/* 162 */       return value;
/*     */     }
/*     */   };
/*     */   
/* 166 */   private static final DefaultHeaders.NameConverter<CharSequence> CHARSEQUENCE_IDENTITY_CONVERTER = new DefaultHeaders.IdentityNameConverter();
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_VALUE_SIZE = 10;
/*     */   
/*     */   private final ValuesComposer valuesComposer;
/*     */   
/*     */ 
/*     */   public DefaultTextHeaders()
/*     */   {
/* 176 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultTextHeaders(boolean ignoreCase) {
/* 180 */     this(ignoreCase, CHARSEQUENCE_FROM_OBJECT_CONVERTER, CHARSEQUENCE_IDENTITY_CONVERTER, false);
/*     */   }
/*     */   
/*     */   public DefaultTextHeaders(boolean ignoreCase, boolean singleHeaderFields) {
/* 184 */     this(ignoreCase, CHARSEQUENCE_FROM_OBJECT_CONVERTER, CHARSEQUENCE_IDENTITY_CONVERTER, singleHeaderFields);
/*     */   }
/*     */   
/*     */   protected DefaultTextHeaders(boolean ignoreCase, Headers.ValueConverter<CharSequence> valueConverter, DefaultHeaders.NameConverter<CharSequence> nameConverter)
/*     */   {
/* 189 */     this(ignoreCase, valueConverter, nameConverter, false);
/*     */   }
/*     */   
/*     */   public DefaultTextHeaders(boolean ignoreCase, Headers.ValueConverter<CharSequence> valueConverter, DefaultHeaders.NameConverter<CharSequence> nameConverter, boolean singleHeaderFields)
/*     */   {
/* 194 */     super(comparator(ignoreCase), comparator(ignoreCase), ignoreCase ? CHARSEQUECE_CASE_INSENSITIVE_HASH_CODE_GENERATOR : CHARSEQUECE_CASE_SENSITIVE_HASH_CODE_GENERATOR, valueConverter, CHARSEQUENCE_TO_STRING_CONVERTER, nameConverter);
/*     */     
/*     */ 
/*     */ 
/* 198 */     this.valuesComposer = (singleHeaderFields ? new SingleHeaderValuesComposer(null) : new MultipleFieldsValueComposer(null));
/*     */   }
/*     */   
/*     */   public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase)
/*     */   {
/* 203 */     return contains(name, value, comparator(ignoreCase));
/*     */   }
/*     */   
/*     */   public boolean containsObject(CharSequence name, Object value, boolean ignoreCase)
/*     */   {
/* 208 */     return containsObject(name, value, comparator(ignoreCase));
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, CharSequence value)
/*     */   {
/* 213 */     return this.valuesComposer.add(name, value);
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 218 */     return this.valuesComposer.add(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/* 223 */     return this.valuesComposer.add(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Object value)
/*     */   {
/* 228 */     return this.valuesComposer.addObject(name, new Object[] { value });
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 233 */     return this.valuesComposer.addObject(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders addObject(CharSequence name, Object... values)
/*     */   {
/* 238 */     return this.valuesComposer.addObject(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/* 243 */     super.addBoolean(name, value);
/* 244 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addChar(CharSequence name, char value)
/*     */   {
/* 249 */     super.addChar(name, value);
/* 250 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addByte(CharSequence name, byte value)
/*     */   {
/* 255 */     super.addByte(name, value);
/* 256 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addShort(CharSequence name, short value)
/*     */   {
/* 261 */     super.addShort(name, value);
/* 262 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addInt(CharSequence name, int value)
/*     */   {
/* 267 */     super.addInt(name, value);
/* 268 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addLong(CharSequence name, long value)
/*     */   {
/* 273 */     super.addLong(name, value);
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addFloat(CharSequence name, float value)
/*     */   {
/* 279 */     super.addFloat(name, value);
/* 280 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 285 */     super.addDouble(name, value);
/* 286 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 291 */     super.addTimeMillis(name, value);
/* 292 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders add(TextHeaders headers)
/*     */   {
/* 297 */     super.add(headers);
/* 298 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 303 */     super.set(name, value);
/* 304 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 309 */     return this.valuesComposer.set(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 314 */     return this.valuesComposer.set(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 319 */     super.setObject(name, value);
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 325 */     return this.valuesComposer.setObject(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 330 */     return this.valuesComposer.setObject(name, values);
/*     */   }
/*     */   
/*     */   public TextHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 335 */     super.setBoolean(name, value);
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setChar(CharSequence name, char value)
/*     */   {
/* 341 */     super.setChar(name, value);
/* 342 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 347 */     super.setByte(name, value);
/* 348 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setShort(CharSequence name, short value)
/*     */   {
/* 353 */     super.setShort(name, value);
/* 354 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setInt(CharSequence name, int value)
/*     */   {
/* 359 */     super.setInt(name, value);
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setLong(CharSequence name, long value)
/*     */   {
/* 365 */     super.setLong(name, value);
/* 366 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 371 */     super.setFloat(name, value);
/* 372 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 377 */     super.setDouble(name, value);
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 383 */     super.setTimeMillis(name, value);
/* 384 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders set(TextHeaders headers)
/*     */   {
/* 389 */     super.set(headers);
/* 390 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders setAll(TextHeaders headers)
/*     */   {
/* 395 */     super.setAll(headers);
/* 396 */     return this;
/*     */   }
/*     */   
/*     */   public TextHeaders clear()
/*     */   {
/* 401 */     super.clear();
/* 402 */     return this;
/*     */   }
/*     */   
/*     */   private static Comparator<CharSequence> comparator(boolean ignoreCase) {
/* 406 */     return ignoreCase ? AsciiString.CHARSEQUENCE_CASE_INSENSITIVE_ORDER : AsciiString.CHARSEQUENCE_CASE_SENSITIVE_ORDER;
/*     */   }
/*     */   
/*     */   private static abstract interface ValuesComposer
/*     */   {
/*     */     public abstract TextHeaders add(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*     */     
/*     */     public abstract TextHeaders add(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*     */     
/*     */     public abstract TextHeaders add(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*     */     
/*     */     public abstract TextHeaders addObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*     */     
/*     */     public abstract TextHeaders addObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*     */     
/*     */     public abstract TextHeaders set(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*     */     
/*     */     public abstract TextHeaders set(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*     */     
/*     */     public abstract TextHeaders setObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*     */     
/*     */     public abstract TextHeaders setObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*     */   }
/*     */   
/*     */   private final class MultipleFieldsValueComposer
/*     */     implements DefaultTextHeaders.ValuesComposer
/*     */   {
/*     */     private MultipleFieldsValueComposer() {}
/*     */     
/*     */     public TextHeaders add(CharSequence name, CharSequence value)
/*     */     {
/* 437 */       DefaultTextHeaders.this.add(name, value);
/* 438 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders add(CharSequence name, CharSequence... values)
/*     */     {
/* 443 */       DefaultTextHeaders.this.add(name, values);
/* 444 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */     {
/* 449 */       DefaultTextHeaders.this.add(name, values);
/* 450 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders addObject(CharSequence name, Iterable<?> values)
/*     */     {
/* 455 */       DefaultTextHeaders.this.addObject(name, values);
/* 456 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders addObject(CharSequence name, Object... values)
/*     */     {
/* 461 */       DefaultTextHeaders.this.addObject(name, values);
/* 462 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders set(CharSequence name, CharSequence... values)
/*     */     {
/* 467 */       DefaultTextHeaders.this.set(name, values);
/* 468 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */     {
/* 473 */       DefaultTextHeaders.this.set(name, values);
/* 474 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders setObject(CharSequence name, Object... values)
/*     */     {
/* 479 */       DefaultTextHeaders.this.setObject(name, values);
/* 480 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders setObject(CharSequence name, Iterable<?> values)
/*     */     {
/* 485 */       DefaultTextHeaders.this.setObject(name, values);
/* 486 */       return DefaultTextHeaders.this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class SingleHeaderValuesComposer
/*     */     implements DefaultTextHeaders.ValuesComposer
/*     */   {
/* 498 */     private final Headers.ValueConverter<CharSequence> valueConverter = DefaultTextHeaders.this.valueConverter();
/*     */     private DefaultTextHeaders.CsvValueEscaper<Object> objectEscaper;
/*     */     
/*     */     private SingleHeaderValuesComposer() {}
/*     */     
/* 503 */     private DefaultTextHeaders.CsvValueEscaper<Object> objectEscaper() { if (this.objectEscaper == null) {
/* 504 */         this.objectEscaper = new DefaultTextHeaders.CsvValueEscaper()
/*     */         {
/*     */           public CharSequence escape(Object value) {
/* 507 */             return StringUtil.escapeCsv((CharSequence)DefaultTextHeaders.SingleHeaderValuesComposer.this.valueConverter.convertObject(value));
/*     */           }
/*     */         };
/*     */       }
/* 511 */       return this.objectEscaper;
/*     */     }
/*     */     
/*     */     private DefaultTextHeaders.CsvValueEscaper<CharSequence> charSequenceEscaper;
/* 515 */     private DefaultTextHeaders.CsvValueEscaper<CharSequence> charSequenceEscaper() { if (this.charSequenceEscaper == null) {
/* 516 */         this.charSequenceEscaper = new DefaultTextHeaders.CsvValueEscaper()
/*     */         {
/*     */           public CharSequence escape(CharSequence value) {
/* 519 */             return StringUtil.escapeCsv(value);
/*     */           }
/*     */         };
/*     */       }
/* 523 */       return this.charSequenceEscaper;
/*     */     }
/*     */     
/*     */     public TextHeaders add(CharSequence name, CharSequence value)
/*     */     {
/* 528 */       return addEscapedValue(name, StringUtil.escapeCsv(value));
/*     */     }
/*     */     
/*     */     public TextHeaders add(CharSequence name, CharSequence... values)
/*     */     {
/* 533 */       return addEscapedValue(name, commaSeparate(charSequenceEscaper(), values));
/*     */     }
/*     */     
/*     */     public TextHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */     {
/* 538 */       return addEscapedValue(name, commaSeparate(charSequenceEscaper(), values));
/*     */     }
/*     */     
/*     */     public TextHeaders addObject(CharSequence name, Iterable<?> values)
/*     */     {
/* 543 */       return addEscapedValue(name, commaSeparate(objectEscaper(), values));
/*     */     }
/*     */     
/*     */     public TextHeaders addObject(CharSequence name, Object... values)
/*     */     {
/* 548 */       return addEscapedValue(name, commaSeparate(objectEscaper(), values));
/*     */     }
/*     */     
/*     */     public TextHeaders set(CharSequence name, CharSequence... values)
/*     */     {
/* 553 */       DefaultTextHeaders.this.set(name, commaSeparate(charSequenceEscaper(), values));
/* 554 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */     {
/* 559 */       DefaultTextHeaders.this.set(name, commaSeparate(charSequenceEscaper(), values));
/* 560 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders setObject(CharSequence name, Object... values)
/*     */     {
/* 565 */       DefaultTextHeaders.this.set(name, commaSeparate(objectEscaper(), values));
/* 566 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     public TextHeaders setObject(CharSequence name, Iterable<?> values)
/*     */     {
/* 571 */       DefaultTextHeaders.this.set(name, commaSeparate(objectEscaper(), values));
/* 572 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     private TextHeaders addEscapedValue(CharSequence name, CharSequence escapedValue) {
/* 576 */       CharSequence currentValue = (CharSequence)DefaultTextHeaders.this.get(name);
/* 577 */       if (currentValue == null) {
/* 578 */         DefaultTextHeaders.this.add(name, escapedValue);
/*     */       } else {
/* 580 */         DefaultTextHeaders.this.set(name, commaSeparateEscapedValues(currentValue, escapedValue));
/*     */       }
/* 582 */       return DefaultTextHeaders.this;
/*     */     }
/*     */     
/*     */     private <T> CharSequence commaSeparate(DefaultTextHeaders.CsvValueEscaper<T> escaper, T... values) {
/* 586 */       StringBuilder sb = new StringBuilder(values.length * 10);
/* 587 */       if (values.length > 0) {
/* 588 */         int end = values.length - 1;
/* 589 */         for (int i = 0; i < end; i++) {
/* 590 */           sb.append(escaper.escape(values[i])).append(',');
/*     */         }
/* 592 */         sb.append(escaper.escape(values[end]));
/*     */       }
/* 594 */       return sb;
/*     */     }
/*     */     
/*     */     private <T> CharSequence commaSeparate(DefaultTextHeaders.CsvValueEscaper<T> escaper, Iterable<? extends T> values) {
/* 598 */       StringBuilder sb = new StringBuilder();
/* 599 */       Iterator<? extends T> iterator = values.iterator();
/* 600 */       if (iterator.hasNext()) {
/* 601 */         T next = iterator.next();
/* 602 */         while (iterator.hasNext()) {
/* 603 */           sb.append(escaper.escape(next)).append(',');
/* 604 */           next = iterator.next();
/*     */         }
/* 606 */         sb.append(escaper.escape(next));
/*     */       }
/* 608 */       return sb;
/*     */     }
/*     */     
/*     */     private CharSequence commaSeparateEscapedValues(CharSequence currentValue, CharSequence value) {
/* 612 */       return new StringBuilder(currentValue.length() + 1 + value.length()).append(currentValue).append(',').append(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract interface CsvValueEscaper<T>
/*     */   {
/*     */     public abstract CharSequence escape(T paramT);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\DefaultTextHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */