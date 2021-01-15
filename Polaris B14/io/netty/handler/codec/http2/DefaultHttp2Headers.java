/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.BinaryHeaders;
/*     */ import io.netty.handler.codec.DefaultBinaryHeaders;
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
/*     */ public class DefaultHttp2Headers
/*     */   extends DefaultBinaryHeaders
/*     */   implements Http2Headers
/*     */ {
/*     */   public DefaultHttp2Headers()
/*     */   {
/*  27 */     this(true);
/*     */   }
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
/*     */   public DefaultHttp2Headers(boolean forceKeyToLower)
/*     */   {
/*  44 */     super(forceKeyToLower);
/*     */   }
/*     */   
/*     */   public Http2Headers add(AsciiString name, AsciiString value)
/*     */   {
/*  49 */     super.add(name, value);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/*  55 */     super.add(name, values);
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(AsciiString name, AsciiString... values)
/*     */   {
/*  61 */     super.add(name, values);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Object value)
/*     */   {
/*  67 */     super.addObject(name, value);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Iterable<?> values)
/*     */   {
/*  73 */     super.addObject(name, values);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Object... values)
/*     */   {
/*  79 */     super.addObject(name, values);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addBoolean(AsciiString name, boolean value)
/*     */   {
/*  85 */     super.addBoolean(name, value);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addChar(AsciiString name, char value)
/*     */   {
/*  91 */     super.addChar(name, value);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addByte(AsciiString name, byte value)
/*     */   {
/*  97 */     super.addByte(name, value);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addShort(AsciiString name, short value)
/*     */   {
/* 103 */     super.addShort(name, value);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addInt(AsciiString name, int value)
/*     */   {
/* 109 */     super.addInt(name, value);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addLong(AsciiString name, long value)
/*     */   {
/* 115 */     super.addLong(name, value);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addFloat(AsciiString name, float value)
/*     */   {
/* 121 */     super.addFloat(name, value);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addDouble(AsciiString name, double value)
/*     */   {
/* 127 */     super.addDouble(name, value);
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addTimeMillis(AsciiString name, long value)
/*     */   {
/* 133 */     super.addTimeMillis(name, value);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(BinaryHeaders headers)
/*     */   {
/* 139 */     super.add(headers);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, AsciiString value)
/*     */   {
/* 145 */     super.set(name, value);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/* 151 */     super.set(name, values);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, AsciiString... values)
/*     */   {
/* 157 */     super.set(name, values);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Object value)
/*     */   {
/* 163 */     super.setObject(name, value);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Iterable<?> values)
/*     */   {
/* 169 */     super.setObject(name, values);
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Object... values)
/*     */   {
/* 175 */     super.setObject(name, values);
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setBoolean(AsciiString name, boolean value)
/*     */   {
/* 181 */     super.setBoolean(name, value);
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setChar(AsciiString name, char value)
/*     */   {
/* 187 */     super.setChar(name, value);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setByte(AsciiString name, byte value)
/*     */   {
/* 193 */     super.setByte(name, value);
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setShort(AsciiString name, short value)
/*     */   {
/* 199 */     super.setShort(name, value);
/* 200 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setInt(AsciiString name, int value)
/*     */   {
/* 205 */     super.setInt(name, value);
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setLong(AsciiString name, long value)
/*     */   {
/* 211 */     super.setLong(name, value);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setFloat(AsciiString name, float value)
/*     */   {
/* 217 */     super.setFloat(name, value);
/* 218 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setDouble(AsciiString name, double value)
/*     */   {
/* 223 */     super.setDouble(name, value);
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setTimeMillis(AsciiString name, long value)
/*     */   {
/* 229 */     super.setTimeMillis(name, value);
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(BinaryHeaders headers)
/*     */   {
/* 235 */     super.set(headers);
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setAll(BinaryHeaders headers)
/*     */   {
/* 241 */     super.setAll(headers);
/* 242 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers clear()
/*     */   {
/* 247 */     super.clear();
/* 248 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers method(AsciiString value)
/*     */   {
/* 253 */     set(Http2Headers.PseudoHeaderName.METHOD.value(), value);
/* 254 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers scheme(AsciiString value)
/*     */   {
/* 259 */     set(Http2Headers.PseudoHeaderName.SCHEME.value(), value);
/* 260 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers authority(AsciiString value)
/*     */   {
/* 265 */     set(Http2Headers.PseudoHeaderName.AUTHORITY.value(), value);
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers path(AsciiString value)
/*     */   {
/* 271 */     set(Http2Headers.PseudoHeaderName.PATH.value(), value);
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers status(AsciiString value)
/*     */   {
/* 277 */     set(Http2Headers.PseudoHeaderName.STATUS.value(), value);
/* 278 */     return this;
/*     */   }
/*     */   
/*     */   public AsciiString method()
/*     */   {
/* 283 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.METHOD.value());
/*     */   }
/*     */   
/*     */   public AsciiString scheme()
/*     */   {
/* 288 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.SCHEME.value());
/*     */   }
/*     */   
/*     */   public AsciiString authority()
/*     */   {
/* 293 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.AUTHORITY.value());
/*     */   }
/*     */   
/*     */   public AsciiString path()
/*     */   {
/* 298 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.PATH.value());
/*     */   }
/*     */   
/*     */   public AsciiString status()
/*     */   {
/* 303 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.STATUS.value());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */