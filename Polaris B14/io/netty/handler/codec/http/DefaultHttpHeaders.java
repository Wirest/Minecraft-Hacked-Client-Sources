/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.DefaultHeaders.NameConverter;
/*     */ import io.netty.handler.codec.DefaultTextHeaders;
/*     */ import io.netty.handler.codec.DefaultTextHeaders.DefaultTextValueTypeConverter;
/*     */ import io.netty.handler.codec.TextHeaders;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
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
/*     */ public class DefaultHttpHeaders
/*     */   extends DefaultTextHeaders
/*     */   implements HttpHeaders
/*     */ {
/*     */   private static final int HIGHEST_INVALID_NAME_CHAR_MASK = -64;
/*     */   private static final int HIGHEST_INVALID_VALUE_CHAR_MASK = -16;
/*  33 */   private static final byte[] LOOKUP_TABLE = new byte[64];
/*     */   
/*     */   static {
/*  36 */     LOOKUP_TABLE[9] = -1;
/*  37 */     LOOKUP_TABLE[10] = -1;
/*  38 */     LOOKUP_TABLE[11] = -1;
/*  39 */     LOOKUP_TABLE[12] = -1;
/*  40 */     LOOKUP_TABLE[32] = -1;
/*  41 */     LOOKUP_TABLE[44] = -1;
/*  42 */     LOOKUP_TABLE[58] = -1;
/*  43 */     LOOKUP_TABLE[59] = -1;
/*  44 */     LOOKUP_TABLE[61] = -1;
/*     */   }
/*     */   
/*     */   private static final class HttpHeadersValidationConverter extends DefaultTextHeaders.DefaultTextValueTypeConverter {
/*     */     private final boolean validate;
/*     */     
/*     */     HttpHeadersValidationConverter(boolean validate) {
/*  51 */       this.validate = validate;
/*     */     }
/*     */     
/*     */     public CharSequence convertObject(Object value)
/*     */     {
/*  56 */       if (value == null) {
/*  57 */         throw new NullPointerException("value");
/*     */       }
/*     */       CharSequence seq;
/*     */       CharSequence seq;
/*  61 */       if ((value instanceof CharSequence)) {
/*  62 */         seq = (CharSequence)value; } else { CharSequence seq;
/*  63 */         if ((value instanceof Number)) {
/*  64 */           seq = value.toString(); } else { CharSequence seq;
/*  65 */           if ((value instanceof Date)) {
/*  66 */             seq = HttpHeaderDateFormat.get().format((Date)value); } else { CharSequence seq;
/*  67 */             if ((value instanceof Calendar)) {
/*  68 */               seq = HttpHeaderDateFormat.get().format(((Calendar)value).getTime());
/*     */             } else
/*  70 */               seq = value.toString();
/*     */           }
/*     */         } }
/*  73 */       if (this.validate) {
/*  74 */         if ((value instanceof AsciiString)) {
/*  75 */           validateValue((AsciiString)seq);
/*     */         } else {
/*  77 */           validateValue(seq);
/*     */         }
/*     */       }
/*     */       
/*  81 */       return seq;
/*     */     }
/*     */     
/*     */     private static void validateValue(AsciiString seq) {
/*  85 */       int state = 0;
/*     */       
/*  87 */       int start = seq.arrayOffset();
/*  88 */       int end = start + seq.length();
/*  89 */       byte[] array = seq.array();
/*  90 */       for (int index = start; index < end; index++) {
/*  91 */         state = validateValueChar(seq, state, (char)(array[index] & 0xFF));
/*     */       }
/*     */       
/*  94 */       if (state != 0) {
/*  95 */         throw new IllegalArgumentException("a header value must not end with '\\r' or '\\n':" + seq);
/*     */       }
/*     */     }
/*     */     
/*     */     private static void validateValue(CharSequence seq) {
/* 100 */       int state = 0;
/*     */       
/* 102 */       for (int index = 0; index < seq.length(); index++) {
/* 103 */         state = validateValueChar(seq, state, seq.charAt(index));
/*     */       }
/*     */       
/* 106 */       if (state != 0) {
/* 107 */         throw new IllegalArgumentException("a header value must not end with '\\r' or '\\n':" + seq);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static int validateValueChar(CharSequence seq, int state, char character)
/*     */     {
/* 118 */       if ((character & 0xFFFFFFF0) == 0)
/*     */       {
/* 120 */         switch (character) {
/*     */         case '\000': 
/* 122 */           throw new IllegalArgumentException("a header value contains a prohibited character '\000': " + seq);
/*     */         case '\013': 
/* 124 */           throw new IllegalArgumentException("a header value contains a prohibited character '\\v': " + seq);
/*     */         case '\f': 
/* 126 */           throw new IllegalArgumentException("a header value contains a prohibited character '\\f': " + seq);
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 131 */       switch (state) {
/*     */       case 0: 
/* 133 */         switch (character) {
/*     */         case '\r': 
/* 135 */           state = 1;
/* 136 */           break;
/*     */         case '\n': 
/* 138 */           state = 2;
/*     */         }
/*     */         
/* 141 */         break;
/*     */       case 1: 
/* 143 */         switch (character) {
/*     */         case '\n': 
/* 145 */           state = 2;
/* 146 */           break;
/*     */         default: 
/* 148 */           throw new IllegalArgumentException("only '\\n' is allowed after '\\r': " + seq);
/*     */         }
/*     */         break;
/*     */       case 2: 
/* 152 */         switch (character) {
/*     */         case '\t': 
/*     */         case ' ': 
/* 155 */           state = 0;
/* 156 */           break;
/*     */         default: 
/* 158 */           throw new IllegalArgumentException("only ' ' and '\\t' are allowed after '\\n': " + seq);
/*     */         }
/*     */         break; }
/* 161 */       return state;
/*     */     }
/*     */   }
/*     */   
/*     */   static class HttpHeadersNameConverter implements DefaultHeaders.NameConverter<CharSequence> {
/*     */     protected final boolean validate;
/*     */     
/*     */     HttpHeadersNameConverter(boolean validate) {
/* 169 */       this.validate = validate;
/*     */     }
/*     */     
/*     */     public CharSequence convertName(CharSequence name)
/*     */     {
/* 174 */       if (this.validate) {
/* 175 */         if ((name instanceof AsciiString)) {
/* 176 */           validateName((AsciiString)name);
/*     */         } else {
/* 178 */           validateName(name);
/*     */         }
/*     */       }
/*     */       
/* 182 */       return name;
/*     */     }
/*     */     
/*     */     private static void validateName(AsciiString name)
/*     */     {
/* 187 */       int start = name.arrayOffset();
/* 188 */       int end = start + name.length();
/* 189 */       byte[] array = name.array();
/* 190 */       for (int index = start; index < end; index++) {
/* 191 */         byte b = array[index];
/*     */         
/*     */ 
/* 194 */         if (b < 0) {
/* 195 */           throw new IllegalArgumentException("a header name cannot contain non-ASCII characters: " + name);
/*     */         }
/*     */         
/*     */ 
/* 199 */         validateNameChar(name, b);
/*     */       }
/*     */     }
/*     */     
/*     */     private static void validateName(CharSequence name)
/*     */     {
/* 205 */       for (int index = 0; index < name.length(); index++) {
/* 206 */         char character = name.charAt(index);
/*     */         
/*     */ 
/* 209 */         if (character > '') {
/* 210 */           throw new IllegalArgumentException("a header name cannot contain non-ASCII characters: " + name);
/*     */         }
/*     */         
/*     */ 
/* 214 */         validateNameChar(name, character);
/*     */       }
/*     */     }
/*     */     
/*     */     private static void validateNameChar(CharSequence name, int character) {
/* 219 */       if (((character & 0xFFFFFFC0) == 0) && (DefaultHttpHeaders.LOOKUP_TABLE[character] != 0)) {
/* 220 */         throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 228 */   private static final HttpHeadersValidationConverter VALIDATE_OBJECT_CONVERTER = new HttpHeadersValidationConverter(true);
/*     */   
/* 230 */   private static final HttpHeadersValidationConverter NO_VALIDATE_OBJECT_CONVERTER = new HttpHeadersValidationConverter(false);
/* 231 */   private static final HttpHeadersNameConverter VALIDATE_NAME_CONVERTER = new HttpHeadersNameConverter(true);
/* 232 */   private static final HttpHeadersNameConverter NO_VALIDATE_NAME_CONVERTER = new HttpHeadersNameConverter(false);
/*     */   
/*     */   public DefaultHttpHeaders() {
/* 235 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultHttpHeaders(boolean validate) {
/* 239 */     this(true, validate ? VALIDATE_NAME_CONVERTER : NO_VALIDATE_NAME_CONVERTER, false);
/*     */   }
/*     */   
/*     */   protected DefaultHttpHeaders(boolean validate, boolean singleHeaderFields) {
/* 243 */     this(true, validate ? VALIDATE_NAME_CONVERTER : NO_VALIDATE_NAME_CONVERTER, singleHeaderFields);
/*     */   }
/*     */   
/*     */   protected DefaultHttpHeaders(boolean validate, DefaultHeaders.NameConverter<CharSequence> nameConverter, boolean singleHeaderFields)
/*     */   {
/* 248 */     super(true, validate ? VALIDATE_OBJECT_CONVERTER : NO_VALIDATE_OBJECT_CONVERTER, nameConverter, singleHeaderFields);
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpHeaders add(CharSequence name, CharSequence value)
/*     */   {
/* 254 */     super.add(name, value);
/* 255 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 260 */     super.add(name, values);
/* 261 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/* 266 */     super.add(name, values);
/* 267 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Object value)
/*     */   {
/* 272 */     super.addObject(name, value);
/* 273 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 278 */     super.addObject(name, values);
/* 279 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addObject(CharSequence name, Object... values)
/*     */   {
/* 284 */     super.addObject(name, values);
/* 285 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/* 290 */     super.addBoolean(name, value);
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addChar(CharSequence name, char value)
/*     */   {
/* 296 */     super.addChar(name, value);
/* 297 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addByte(CharSequence name, byte value)
/*     */   {
/* 302 */     super.addByte(name, value);
/* 303 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addShort(CharSequence name, short value)
/*     */   {
/* 308 */     super.addShort(name, value);
/* 309 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addInt(CharSequence name, int value)
/*     */   {
/* 314 */     super.addInt(name, value);
/* 315 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addLong(CharSequence name, long value)
/*     */   {
/* 320 */     super.addLong(name, value);
/* 321 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addFloat(CharSequence name, float value)
/*     */   {
/* 326 */     super.addFloat(name, value);
/* 327 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 332 */     super.addDouble(name, value);
/* 333 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 338 */     super.addTimeMillis(name, value);
/* 339 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders add(TextHeaders headers)
/*     */   {
/* 344 */     super.add(headers);
/* 345 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 350 */     super.set(name, value);
/* 351 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 356 */     super.set(name, values);
/* 357 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 362 */     super.set(name, values);
/* 363 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 368 */     super.setObject(name, value);
/* 369 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 374 */     super.setObject(name, values);
/* 375 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 380 */     super.setObject(name, values);
/* 381 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 386 */     super.setBoolean(name, value);
/* 387 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setChar(CharSequence name, char value)
/*     */   {
/* 392 */     super.setChar(name, value);
/* 393 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 398 */     super.setByte(name, value);
/* 399 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setShort(CharSequence name, short value)
/*     */   {
/* 404 */     super.setShort(name, value);
/* 405 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setInt(CharSequence name, int value)
/*     */   {
/* 410 */     super.setInt(name, value);
/* 411 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setLong(CharSequence name, long value)
/*     */   {
/* 416 */     super.setLong(name, value);
/* 417 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 422 */     super.setFloat(name, value);
/* 423 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 428 */     super.setDouble(name, value);
/* 429 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 434 */     super.setTimeMillis(name, value);
/* 435 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders set(TextHeaders headers)
/*     */   {
/* 440 */     super.set(headers);
/* 441 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders setAll(TextHeaders headers)
/*     */   {
/* 446 */     super.setAll(headers);
/* 447 */     return this;
/*     */   }
/*     */   
/*     */   public HttpHeaders clear()
/*     */   {
/* 452 */     super.clear();
/* 453 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultHttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */