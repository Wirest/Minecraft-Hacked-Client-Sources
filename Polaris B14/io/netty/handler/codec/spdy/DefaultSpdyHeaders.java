/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.DefaultHeaders.NameConverter;
/*     */ import io.netty.handler.codec.DefaultTextHeaders;
/*     */ import io.netty.handler.codec.DefaultTextHeaders.DefaultTextValueTypeConverter;
/*     */ import io.netty.handler.codec.Headers.ValueConverter;
/*     */ import io.netty.handler.codec.TextHeaders;
/*     */ import java.util.Locale;
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
/*     */ public class DefaultSpdyHeaders
/*     */   extends DefaultTextHeaders
/*     */   implements SpdyHeaders
/*     */ {
/*  26 */   private static final Headers.ValueConverter<CharSequence> SPDY_VALUE_CONVERTER = new DefaultTextHeaders.DefaultTextValueTypeConverter()
/*     */   {
/*     */     public CharSequence convertObject(Object value) {
/*     */       CharSequence seq;
/*     */       CharSequence seq;
/*  31 */       if ((value instanceof CharSequence)) {
/*  32 */         seq = (CharSequence)value;
/*     */       } else {
/*  34 */         seq = value.toString();
/*     */       }
/*     */       
/*  37 */       SpdyCodecUtil.validateHeaderValue(seq);
/*  38 */       return seq;
/*     */     }
/*     */   };
/*     */   
/*  42 */   private static final DefaultHeaders.NameConverter<CharSequence> SPDY_NAME_CONVERTER = new DefaultHeaders.NameConverter()
/*     */   {
/*     */     public CharSequence convertName(CharSequence name) {
/*  45 */       if ((name instanceof AsciiString)) {
/*  46 */         name = ((AsciiString)name).toLowerCase();
/*     */       } else {
/*  48 */         name = name.toString().toLowerCase(Locale.US);
/*     */       }
/*  50 */       SpdyCodecUtil.validateHeaderName(name);
/*  51 */       return name;
/*     */     }
/*     */   };
/*     */   
/*     */   public DefaultSpdyHeaders() {
/*  56 */     super(true, SPDY_VALUE_CONVERTER, SPDY_NAME_CONVERTER);
/*     */   }
/*     */   
/*     */   public SpdyHeaders add(CharSequence name, CharSequence value)
/*     */   {
/*  61 */     super.add(name, value);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders add(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/*  67 */     super.add(name, values);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders add(CharSequence name, CharSequence... values)
/*     */   {
/*  73 */     super.add(name, values);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addObject(CharSequence name, Object value)
/*     */   {
/*  79 */     super.addObject(name, value);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addObject(CharSequence name, Iterable<?> values)
/*     */   {
/*  85 */     super.addObject(name, values);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addObject(CharSequence name, Object... values)
/*     */   {
/*  91 */     super.addObject(name, values);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addBoolean(CharSequence name, boolean value)
/*     */   {
/*  97 */     super.addBoolean(name, value);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addChar(CharSequence name, char value)
/*     */   {
/* 103 */     super.addChar(name, value);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addByte(CharSequence name, byte value)
/*     */   {
/* 109 */     super.addByte(name, value);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addShort(CharSequence name, short value)
/*     */   {
/* 115 */     super.addShort(name, value);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addInt(CharSequence name, int value)
/*     */   {
/* 121 */     super.addInt(name, value);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addLong(CharSequence name, long value)
/*     */   {
/* 127 */     super.addLong(name, value);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addFloat(CharSequence name, float value)
/*     */   {
/* 133 */     super.addFloat(name, value);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addDouble(CharSequence name, double value)
/*     */   {
/* 139 */     super.addDouble(name, value);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders addTimeMillis(CharSequence name, long value)
/*     */   {
/* 145 */     super.addTimeMillis(name, value);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders add(TextHeaders headers)
/*     */   {
/* 151 */     super.add(headers);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders set(CharSequence name, CharSequence value)
/*     */   {
/* 157 */     super.set(name, value);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders set(CharSequence name, Iterable<? extends CharSequence> values)
/*     */   {
/* 163 */     super.set(name, values);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders set(CharSequence name, CharSequence... values)
/*     */   {
/* 169 */     super.set(name, values);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setObject(CharSequence name, Object value)
/*     */   {
/* 175 */     super.setObject(name, value);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setObject(CharSequence name, Iterable<?> values)
/*     */   {
/* 181 */     super.setObject(name, values);
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setObject(CharSequence name, Object... values)
/*     */   {
/* 187 */     super.setObject(name, values);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setBoolean(CharSequence name, boolean value)
/*     */   {
/* 193 */     super.setBoolean(name, value);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setChar(CharSequence name, char value)
/*     */   {
/* 199 */     super.setChar(name, value);
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setByte(CharSequence name, byte value)
/*     */   {
/* 205 */     super.setByte(name, value);
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setShort(CharSequence name, short value)
/*     */   {
/* 211 */     super.setShort(name, value);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setInt(CharSequence name, int value)
/*     */   {
/* 217 */     super.setInt(name, value);
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setLong(CharSequence name, long value)
/*     */   {
/* 223 */     super.setLong(name, value);
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setFloat(CharSequence name, float value)
/*     */   {
/* 229 */     super.setFloat(name, value);
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setDouble(CharSequence name, double value)
/*     */   {
/* 235 */     super.setDouble(name, value);
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setTimeMillis(CharSequence name, long value)
/*     */   {
/* 241 */     super.setTimeMillis(name, value);
/* 242 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders set(TextHeaders headers)
/*     */   {
/* 247 */     super.set(headers);
/* 248 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders setAll(TextHeaders headers)
/*     */   {
/* 253 */     super.setAll(headers);
/* 254 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders clear()
/*     */   {
/* 259 */     super.clear();
/* 260 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdyHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */