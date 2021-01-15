/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.BinaryHeaders;
/*     */ import io.netty.handler.codec.EmptyBinaryHeaders;
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
/*     */ public final class EmptyHttp2Headers
/*     */   extends EmptyBinaryHeaders
/*     */   implements Http2Headers
/*     */ {
/*  23 */   public static final EmptyHttp2Headers INSTANCE = new EmptyHttp2Headers();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Headers add(AsciiString name, AsciiString value)
/*     */   {
/*  30 */     super.add(name, value);
/*  31 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/*  36 */     super.add(name, values);
/*  37 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(AsciiString name, AsciiString... values)
/*     */   {
/*  42 */     super.add(name, values);
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Object value)
/*     */   {
/*  48 */     super.addObject(name, value);
/*  49 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Iterable<?> values)
/*     */   {
/*  54 */     super.addObject(name, values);
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addObject(AsciiString name, Object... values)
/*     */   {
/*  60 */     super.addObject(name, values);
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addBoolean(AsciiString name, boolean value)
/*     */   {
/*  66 */     super.addBoolean(name, value);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addChar(AsciiString name, char value)
/*     */   {
/*  72 */     super.addChar(name, value);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addByte(AsciiString name, byte value)
/*     */   {
/*  78 */     super.addByte(name, value);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addShort(AsciiString name, short value)
/*     */   {
/*  84 */     super.addShort(name, value);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addInt(AsciiString name, int value)
/*     */   {
/*  90 */     super.addInt(name, value);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addLong(AsciiString name, long value)
/*     */   {
/*  96 */     super.addLong(name, value);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addFloat(AsciiString name, float value)
/*     */   {
/* 102 */     super.addFloat(name, value);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addDouble(AsciiString name, double value)
/*     */   {
/* 108 */     super.addDouble(name, value);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers addTimeMillis(AsciiString name, long value)
/*     */   {
/* 114 */     super.addTimeMillis(name, value);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers add(BinaryHeaders headers)
/*     */   {
/* 120 */     super.add(headers);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, AsciiString value)
/*     */   {
/* 126 */     super.set(name, value);
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, Iterable<? extends AsciiString> values)
/*     */   {
/* 132 */     super.set(name, values);
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(AsciiString name, AsciiString... values)
/*     */   {
/* 138 */     super.set(name, values);
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Object value)
/*     */   {
/* 144 */     super.setObject(name, value);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Iterable<?> values)
/*     */   {
/* 150 */     super.setObject(name, values);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setObject(AsciiString name, Object... values)
/*     */   {
/* 156 */     super.setObject(name, values);
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setBoolean(AsciiString name, boolean value)
/*     */   {
/* 162 */     super.setBoolean(name, value);
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setChar(AsciiString name, char value)
/*     */   {
/* 168 */     super.setChar(name, value);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setByte(AsciiString name, byte value)
/*     */   {
/* 174 */     super.setByte(name, value);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setShort(AsciiString name, short value)
/*     */   {
/* 180 */     super.setShort(name, value);
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setInt(AsciiString name, int value)
/*     */   {
/* 186 */     super.setInt(name, value);
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setLong(AsciiString name, long value)
/*     */   {
/* 192 */     super.setLong(name, value);
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setFloat(AsciiString name, float value)
/*     */   {
/* 198 */     super.setFloat(name, value);
/* 199 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setDouble(AsciiString name, double value)
/*     */   {
/* 204 */     super.setDouble(name, value);
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setTimeMillis(AsciiString name, long value)
/*     */   {
/* 210 */     super.setTimeMillis(name, value);
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers set(BinaryHeaders headers)
/*     */   {
/* 216 */     super.set(headers);
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers setAll(BinaryHeaders headers)
/*     */   {
/* 222 */     super.setAll(headers);
/* 223 */     return this;
/*     */   }
/*     */   
/*     */   public Http2Headers clear()
/*     */   {
/* 228 */     super.clear();
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public EmptyHttp2Headers method(AsciiString method)
/*     */   {
/* 234 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public EmptyHttp2Headers scheme(AsciiString status)
/*     */   {
/* 239 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public EmptyHttp2Headers authority(AsciiString authority)
/*     */   {
/* 244 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public EmptyHttp2Headers path(AsciiString path)
/*     */   {
/* 249 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public EmptyHttp2Headers status(AsciiString status)
/*     */   {
/* 254 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public AsciiString method()
/*     */   {
/* 259 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.METHOD.value());
/*     */   }
/*     */   
/*     */   public AsciiString scheme()
/*     */   {
/* 264 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.SCHEME.value());
/*     */   }
/*     */   
/*     */   public AsciiString authority()
/*     */   {
/* 269 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.AUTHORITY.value());
/*     */   }
/*     */   
/*     */   public AsciiString path()
/*     */   {
/* 274 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.PATH.value());
/*     */   }
/*     */   
/*     */   public AsciiString status()
/*     */   {
/* 279 */     return (AsciiString)get(Http2Headers.PseudoHeaderName.STATUS.value());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\EmptyHttp2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */