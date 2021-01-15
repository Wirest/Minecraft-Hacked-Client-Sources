/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.BinaryHeaders;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public abstract interface Http2Headers
/*    */   extends BinaryHeaders
/*    */ {
/*    */   public abstract Http2Headers add(AsciiString paramAsciiString1, AsciiString paramAsciiString2);
/*    */   
/*    */   public abstract Http2Headers add(AsciiString paramAsciiString, Iterable<? extends AsciiString> paramIterable);
/*    */   
/*    */   public abstract Http2Headers add(AsciiString paramAsciiString, AsciiString... paramVarArgs);
/*    */   
/*    */   public abstract Http2Headers addObject(AsciiString paramAsciiString, Object paramObject);
/*    */   
/*    */   public abstract Http2Headers addObject(AsciiString paramAsciiString, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract Http2Headers addObject(AsciiString paramAsciiString, Object... paramVarArgs);
/*    */   
/*    */   public abstract Http2Headers addBoolean(AsciiString paramAsciiString, boolean paramBoolean);
/*    */   
/*    */   public abstract Http2Headers addByte(AsciiString paramAsciiString, byte paramByte);
/*    */   
/*    */   public abstract Http2Headers addChar(AsciiString paramAsciiString, char paramChar);
/*    */   
/*    */   public abstract Http2Headers addShort(AsciiString paramAsciiString, short paramShort);
/*    */   
/*    */   public abstract Http2Headers addInt(AsciiString paramAsciiString, int paramInt);
/*    */   
/*    */   public static enum PseudoHeaderName
/*    */   {
/* 36 */     METHOD(":method"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 41 */     SCHEME(":scheme"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 46 */     AUTHORITY(":authority"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 51 */     PATH(":path"), 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 56 */     STATUS(":status");
/*    */     
/*    */     static {
/* 59 */       PSEUDO_HEADERS = new HashSet();
/*    */       
/* 61 */       for (PseudoHeaderName pseudoHeader : values()) {
/* 62 */         PSEUDO_HEADERS.add(pseudoHeader.value());
/*    */       }
/*    */     }
/*    */     
/*    */     private PseudoHeaderName(String value) {
/* 67 */       this.value = new AsciiString(value);
/*    */     }
/*    */     
/*    */     public AsciiString value()
/*    */     {
/* 72 */       return this.value;
/*    */     }
/*    */     
/*    */     private final AsciiString value;
/*    */     private static final Set<AsciiString> PSEUDO_HEADERS;
/*    */     public static boolean isPseudoHeader(AsciiString header)
/*    */     {
/* 79 */       return PSEUDO_HEADERS.contains(header);
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract Http2Headers addLong(AsciiString paramAsciiString, long paramLong);
/*    */   
/*    */   public abstract Http2Headers addFloat(AsciiString paramAsciiString, float paramFloat);
/*    */   
/*    */   public abstract Http2Headers addDouble(AsciiString paramAsciiString, double paramDouble);
/*    */   
/*    */   public abstract Http2Headers addTimeMillis(AsciiString paramAsciiString, long paramLong);
/*    */   
/*    */   public abstract Http2Headers add(BinaryHeaders paramBinaryHeaders);
/*    */   
/*    */   public abstract Http2Headers set(AsciiString paramAsciiString1, AsciiString paramAsciiString2);
/*    */   
/*    */   public abstract Http2Headers set(AsciiString paramAsciiString, Iterable<? extends AsciiString> paramIterable);
/*    */   
/*    */   public abstract Http2Headers set(AsciiString paramAsciiString, AsciiString... paramVarArgs);
/*    */   
/*    */   public abstract Http2Headers setObject(AsciiString paramAsciiString, Object paramObject);
/*    */   
/*    */   public abstract Http2Headers setObject(AsciiString paramAsciiString, Iterable<?> paramIterable);
/*    */   
/*    */   public abstract Http2Headers setObject(AsciiString paramAsciiString, Object... paramVarArgs);
/*    */   
/*    */   public abstract Http2Headers setBoolean(AsciiString paramAsciiString, boolean paramBoolean);
/*    */   
/*    */   public abstract Http2Headers setByte(AsciiString paramAsciiString, byte paramByte);
/*    */   
/*    */   public abstract Http2Headers setChar(AsciiString paramAsciiString, char paramChar);
/*    */   
/*    */   public abstract Http2Headers setShort(AsciiString paramAsciiString, short paramShort);
/*    */   
/*    */   public abstract Http2Headers setInt(AsciiString paramAsciiString, int paramInt);
/*    */   
/*    */   public abstract Http2Headers setLong(AsciiString paramAsciiString, long paramLong);
/*    */   
/*    */   public abstract Http2Headers setFloat(AsciiString paramAsciiString, float paramFloat);
/*    */   
/*    */   public abstract Http2Headers setDouble(AsciiString paramAsciiString, double paramDouble);
/*    */   
/*    */   public abstract Http2Headers setTimeMillis(AsciiString paramAsciiString, long paramLong);
/*    */   
/*    */   public abstract Http2Headers set(BinaryHeaders paramBinaryHeaders);
/*    */   
/*    */   public abstract Http2Headers setAll(BinaryHeaders paramBinaryHeaders);
/*    */   
/*    */   public abstract Http2Headers clear();
/*    */   
/*    */   public abstract Http2Headers method(AsciiString paramAsciiString);
/*    */   
/*    */   public abstract Http2Headers scheme(AsciiString paramAsciiString);
/*    */   
/*    */   public abstract Http2Headers authority(AsciiString paramAsciiString);
/*    */   
/*    */   public abstract Http2Headers path(AsciiString paramAsciiString);
/*    */   
/*    */   public abstract Http2Headers status(AsciiString paramAsciiString);
/*    */   
/*    */   public abstract AsciiString method();
/*    */   
/*    */   public abstract AsciiString scheme();
/*    */   
/*    */   public abstract AsciiString authority();
/*    */   
/*    */   public abstract AsciiString path();
/*    */   
/*    */   public abstract AsciiString status();
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */