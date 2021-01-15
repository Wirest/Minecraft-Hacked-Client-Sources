/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.TextHeaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface SpdyHeaders
/*    */   extends TextHeaders
/*    */ {
/*    */   public abstract SpdyHeaders add(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*    */   
/*    */   public abstract SpdyHeaders add(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*    */   
/*    */   public abstract SpdyHeaders add(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*    */   
/*    */   public abstract SpdyHeaders addObject(CharSequence paramCharSequence, Object paramObject);
/*    */   
/*    */   public abstract SpdyHeaders addObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract SpdyHeaders addObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*    */   
/*    */   public abstract SpdyHeaders addBoolean(CharSequence paramCharSequence, boolean paramBoolean);
/*    */   
/*    */   public abstract SpdyHeaders addByte(CharSequence paramCharSequence, byte paramByte);
/*    */   
/*    */   public static final class HttpNames
/*    */   {
/* 34 */     public static final AsciiString HOST = new AsciiString(":host");
/*    */     
/*    */ 
/*    */ 
/* 38 */     public static final AsciiString METHOD = new AsciiString(":method");
/*    */     
/*    */ 
/*    */ 
/* 42 */     public static final AsciiString PATH = new AsciiString(":path");
/*    */     
/*    */ 
/*    */ 
/* 46 */     public static final AsciiString SCHEME = new AsciiString(":scheme");
/*    */     
/*    */ 
/*    */ 
/* 50 */     public static final AsciiString STATUS = new AsciiString(":status");
/*    */     
/*    */ 
/*    */ 
/* 54 */     public static final AsciiString VERSION = new AsciiString(":version");
/*    */   }
/*    */   
/*    */   public abstract SpdyHeaders addChar(CharSequence paramCharSequence, char paramChar);
/*    */   
/*    */   public abstract SpdyHeaders addShort(CharSequence paramCharSequence, short paramShort);
/*    */   
/*    */   public abstract SpdyHeaders addInt(CharSequence paramCharSequence, int paramInt);
/*    */   
/*    */   public abstract SpdyHeaders addLong(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract SpdyHeaders addFloat(CharSequence paramCharSequence, float paramFloat);
/*    */   
/*    */   public abstract SpdyHeaders addDouble(CharSequence paramCharSequence, double paramDouble);
/*    */   
/*    */   public abstract SpdyHeaders addTimeMillis(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract SpdyHeaders add(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract SpdyHeaders set(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*    */   
/*    */   public abstract SpdyHeaders set(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*    */   
/*    */   public abstract SpdyHeaders set(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*    */   
/*    */   public abstract SpdyHeaders setBoolean(CharSequence paramCharSequence, boolean paramBoolean);
/*    */   
/*    */   public abstract SpdyHeaders setByte(CharSequence paramCharSequence, byte paramByte);
/*    */   
/*    */   public abstract SpdyHeaders setChar(CharSequence paramCharSequence, char paramChar);
/*    */   
/*    */   public abstract SpdyHeaders setShort(CharSequence paramCharSequence, short paramShort);
/*    */   
/*    */   public abstract SpdyHeaders setInt(CharSequence paramCharSequence, int paramInt);
/*    */   
/*    */   public abstract SpdyHeaders setLong(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract SpdyHeaders setFloat(CharSequence paramCharSequence, float paramFloat);
/*    */   
/*    */   public abstract SpdyHeaders setDouble(CharSequence paramCharSequence, double paramDouble);
/*    */   
/*    */   public abstract SpdyHeaders setTimeMillis(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract SpdyHeaders setObject(CharSequence paramCharSequence, Object paramObject);
/*    */   
/*    */   public abstract SpdyHeaders setObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract SpdyHeaders setObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*    */   
/*    */   public abstract SpdyHeaders set(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract SpdyHeaders setAll(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract SpdyHeaders clear();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */