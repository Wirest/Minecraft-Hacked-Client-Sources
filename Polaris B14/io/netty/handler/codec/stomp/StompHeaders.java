/*    */ package io.netty.handler.codec.stomp;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface StompHeaders
/*    */   extends TextHeaders
/*    */ {
/* 27 */   public static final AsciiString ACCEPT_VERSION = new AsciiString("accept-version");
/* 28 */   public static final AsciiString HOST = new AsciiString("host");
/* 29 */   public static final AsciiString LOGIN = new AsciiString("login");
/* 30 */   public static final AsciiString PASSCODE = new AsciiString("passcode");
/* 31 */   public static final AsciiString HEART_BEAT = new AsciiString("heart-beat");
/* 32 */   public static final AsciiString VERSION = new AsciiString("version");
/* 33 */   public static final AsciiString SESSION = new AsciiString("session");
/* 34 */   public static final AsciiString SERVER = new AsciiString("server");
/* 35 */   public static final AsciiString DESTINATION = new AsciiString("destination");
/* 36 */   public static final AsciiString ID = new AsciiString("id");
/* 37 */   public static final AsciiString ACK = new AsciiString("ack");
/* 38 */   public static final AsciiString TRANSACTION = new AsciiString("transaction");
/* 39 */   public static final AsciiString RECEIPT = new AsciiString("receipt");
/* 40 */   public static final AsciiString MESSAGE_ID = new AsciiString("message-id");
/* 41 */   public static final AsciiString SUBSCRIPTION = new AsciiString("subscription");
/* 42 */   public static final AsciiString RECEIPT_ID = new AsciiString("receipt-id");
/* 43 */   public static final AsciiString MESSAGE = new AsciiString("message");
/* 44 */   public static final AsciiString CONTENT_LENGTH = new AsciiString("content-length");
/* 45 */   public static final AsciiString CONTENT_TYPE = new AsciiString("content-type");
/*    */   
/*    */   public abstract StompHeaders add(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*    */   
/*    */   public abstract StompHeaders add(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*    */   
/*    */   public abstract StompHeaders add(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*    */   
/*    */   public abstract StompHeaders addObject(CharSequence paramCharSequence, Object paramObject);
/*    */   
/*    */   public abstract StompHeaders addObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract StompHeaders addObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*    */   
/*    */   public abstract StompHeaders addBoolean(CharSequence paramCharSequence, boolean paramBoolean);
/*    */   
/*    */   public abstract StompHeaders addByte(CharSequence paramCharSequence, byte paramByte);
/*    */   
/*    */   public abstract StompHeaders addChar(CharSequence paramCharSequence, char paramChar);
/*    */   
/*    */   public abstract StompHeaders addShort(CharSequence paramCharSequence, short paramShort);
/*    */   
/*    */   public abstract StompHeaders addInt(CharSequence paramCharSequence, int paramInt);
/*    */   
/*    */   public abstract StompHeaders addLong(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract StompHeaders addFloat(CharSequence paramCharSequence, float paramFloat);
/*    */   
/*    */   public abstract StompHeaders addDouble(CharSequence paramCharSequence, double paramDouble);
/*    */   
/*    */   public abstract StompHeaders addTimeMillis(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract StompHeaders add(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract StompHeaders set(CharSequence paramCharSequence1, CharSequence paramCharSequence2);
/*    */   
/*    */   public abstract StompHeaders set(CharSequence paramCharSequence, Iterable<? extends CharSequence> paramIterable);
/*    */   
/*    */   public abstract StompHeaders set(CharSequence paramCharSequence, CharSequence... paramVarArgs);
/*    */   
/*    */   public abstract StompHeaders setObject(CharSequence paramCharSequence, Object paramObject);
/*    */   
/*    */   public abstract StompHeaders setObject(CharSequence paramCharSequence, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract StompHeaders setObject(CharSequence paramCharSequence, Object... paramVarArgs);
/*    */   
/*    */   public abstract StompHeaders setBoolean(CharSequence paramCharSequence, boolean paramBoolean);
/*    */   
/*    */   public abstract StompHeaders setByte(CharSequence paramCharSequence, byte paramByte);
/*    */   
/*    */   public abstract StompHeaders setChar(CharSequence paramCharSequence, char paramChar);
/*    */   
/*    */   public abstract StompHeaders setShort(CharSequence paramCharSequence, short paramShort);
/*    */   
/*    */   public abstract StompHeaders setInt(CharSequence paramCharSequence, int paramInt);
/*    */   
/*    */   public abstract StompHeaders setLong(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract StompHeaders setFloat(CharSequence paramCharSequence, float paramFloat);
/*    */   
/*    */   public abstract StompHeaders setDouble(CharSequence paramCharSequence, double paramDouble);
/*    */   
/*    */   public abstract StompHeaders setTimeMillis(CharSequence paramCharSequence, long paramLong);
/*    */   
/*    */   public abstract StompHeaders set(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract StompHeaders setAll(TextHeaders paramTextHeaders);
/*    */   
/*    */   public abstract StompHeaders clear();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\StompHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */