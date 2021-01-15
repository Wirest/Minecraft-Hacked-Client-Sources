/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ 
/*     */ public final class Http2CodecUtil
/*     */ {
/*  34 */   private static final byte[] CONNECTION_PREFACE = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(CharsetUtil.UTF_8);
/*  35 */   private static final byte[] EMPTY_PING = new byte[8];
/*     */   
/*     */   public static final int CONNECTION_STREAM_ID = 0;
/*     */   
/*     */   public static final int HTTP_UPGRADE_STREAM_ID = 1;
/*     */   
/*     */   public static final String HTTP_UPGRADE_SETTINGS_HEADER = "HTTP2-Settings";
/*     */   
/*     */   public static final String HTTP_UPGRADE_PROTOCOL_NAME = "h2c-16";
/*     */   
/*     */   public static final String TLS_UPGRADE_PROTOCOL_NAME = "h2-16";
/*     */   
/*     */   public static final int PING_FRAME_PAYLOAD_LENGTH = 8;
/*     */   
/*     */   public static final short MAX_UNSIGNED_BYTE = 255;
/*     */   
/*     */   public static final int MAX_UNSIGNED_SHORT = 65535;
/*     */   
/*     */   public static final long MAX_UNSIGNED_INT = 4294967295L;
/*     */   
/*     */   public static final int FRAME_HEADER_LENGTH = 9;
/*     */   
/*     */   public static final int SETTING_ENTRY_LENGTH = 6;
/*     */   public static final int PRIORITY_ENTRY_LENGTH = 5;
/*     */   public static final int INT_FIELD_LENGTH = 4;
/*     */   public static final short MAX_WEIGHT = 256;
/*     */   public static final short MIN_WEIGHT = 1;
/*     */   private static final int MAX_PADDING_LENGTH_LENGTH = 1;
/*     */   public static final int DATA_FRAME_HEADER_LENGTH = 10;
/*     */   public static final int HEADERS_FRAME_HEADER_LENGTH = 15;
/*     */   public static final int PRIORITY_FRAME_LENGTH = 14;
/*     */   public static final int RST_STREAM_FRAME_LENGTH = 13;
/*     */   public static final int PUSH_PROMISE_FRAME_HEADER_LENGTH = 14;
/*     */   public static final int GO_AWAY_FRAME_HEADER_LENGTH = 17;
/*     */   public static final int WINDOW_UPDATE_FRAME_LENGTH = 13;
/*     */   public static final int CONTINUATION_FRAME_HEADER_LENGTH = 10;
/*     */   public static final int SETTINGS_HEADER_TABLE_SIZE = 1;
/*     */   public static final int SETTINGS_ENABLE_PUSH = 2;
/*     */   public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 3;
/*     */   public static final int SETTINGS_INITIAL_WINDOW_SIZE = 4;
/*     */   public static final int SETTINGS_MAX_FRAME_SIZE = 5;
/*     */   public static final int SETTINGS_MAX_HEADER_LIST_SIZE = 6;
/*     */   public static final int MAX_HEADER_TABLE_SIZE = Integer.MAX_VALUE;
/*     */   public static final long MAX_CONCURRENT_STREAMS = 4294967295L;
/*     */   public static final int MAX_INITIAL_WINDOW_SIZE = Integer.MAX_VALUE;
/*     */   public static final int MAX_FRAME_SIZE_LOWER_BOUND = 16384;
/*     */   public static final int MAX_FRAME_SIZE_UPPER_BOUND = 16777215;
/*     */   public static final long MAX_HEADER_LIST_SIZE = Long.MAX_VALUE;
/*     */   public static final long MIN_HEADER_TABLE_SIZE = 0L;
/*     */   public static final long MIN_CONCURRENT_STREAMS = 0L;
/*     */   public static final int MIN_INITIAL_WINDOW_SIZE = 0;
/*     */   public static final long MIN_HEADER_LIST_SIZE = 0L;
/*     */   public static final int DEFAULT_WINDOW_SIZE = 65535;
/*     */   public static final boolean DEFAULT_ENABLE_PUSH = true;
/*     */   public static final short DEFAULT_PRIORITY_WEIGHT = 16;
/*     */   public static final int DEFAULT_HEADER_TABLE_SIZE = 4096;
/*     */   public static final int DEFAULT_MAX_HEADER_SIZE = 8192;
/*     */   public static final int DEFAULT_MAX_FRAME_SIZE = 16384;
/*     */   
/*     */   public static boolean isMaxFrameSizeValid(int maxFrameSize)
/*     */   {
/*  96 */     return (maxFrameSize >= 16384) && (maxFrameSize <= 16777215);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf connectionPrefaceBuf()
/*     */   {
/* 104 */     return Unpooled.wrappedBuffer(CONNECTION_PREFACE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf emptyPingBuf()
/*     */   {
/* 112 */     return Unpooled.wrappedBuffer(EMPTY_PING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Http2StreamRemovalPolicy immediateRemovalPolicy()
/*     */   {
/* 120 */     new Http2StreamRemovalPolicy()
/*     */     {
/*     */       private Http2StreamRemovalPolicy.Action action;
/*     */       
/*     */       public void setAction(Http2StreamRemovalPolicy.Action action) {
/* 125 */         this.action = ((Http2StreamRemovalPolicy.Action)ObjectUtil.checkNotNull(action, "action"));
/*     */       }
/*     */       
/*     */       public void markForRemoval(Http2Stream stream)
/*     */       {
/* 130 */         if (this.action == null) {
/* 131 */           throw new IllegalStateException("Action must be called before removing streams.");
/*     */         }
/*     */         
/* 134 */         this.action.removeStream(stream);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Http2Exception getEmbeddedHttp2Exception(Throwable cause)
/*     */   {
/* 144 */     while (cause != null) {
/* 145 */       if ((cause instanceof Http2Exception)) {
/* 146 */         return (Http2Exception)cause;
/*     */       }
/* 148 */       cause = cause.getCause();
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf toByteBuf(ChannelHandlerContext ctx, Throwable cause)
/*     */   {
/* 158 */     if ((cause == null) || (cause.getMessage() == null)) {
/* 159 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/*     */ 
/* 163 */     byte[] msg = cause.getMessage().getBytes(CharsetUtil.UTF_8);
/* 164 */     ByteBuf debugData = ctx.alloc().buffer(msg.length);
/* 165 */     debugData.writeBytes(msg);
/* 166 */     return debugData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int readUnsignedInt(ByteBuf buf)
/*     */   {
/* 173 */     return (buf.readByte() & 0x7F) << 24 | (buf.readByte() & 0xFF) << 16 | (buf.readByte() & 0xFF) << 8 | buf.readByte() & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedInt(long value, ByteBuf out)
/*     */   {
/* 181 */     out.writeByte((int)(value >> 24 & 0xFF));
/* 182 */     out.writeByte((int)(value >> 16 & 0xFF));
/* 183 */     out.writeByte((int)(value >> 8 & 0xFF));
/* 184 */     out.writeByte((int)(value & 0xFF));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void writeUnsignedShort(int value, ByteBuf out)
/*     */   {
/* 191 */     out.writeByte(value >> 8 & 0xFF);
/* 192 */     out.writeByte(value & 0xFF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void writeFrameHeader(ByteBuf out, int payloadLength, byte type, Http2Flags flags, int streamId)
/*     */   {
/* 200 */     out.ensureWritable(9 + payloadLength);
/* 201 */     writeFrameHeaderInternal(out, payloadLength, type, flags, streamId);
/*     */   }
/*     */   
/*     */   static void writeFrameHeaderInternal(ByteBuf out, int payloadLength, byte type, Http2Flags flags, int streamId)
/*     */   {
/* 206 */     out.writeMedium(payloadLength);
/* 207 */     out.writeByte(type);
/* 208 */     out.writeByte(flags.value());
/* 209 */     out.writeInt(streamId);
/*     */   }
/*     */   
/*     */ 
/*     */   static class SimpleChannelPromiseAggregator
/*     */     extends DefaultChannelPromise
/*     */   {
/*     */     private final ChannelPromise promise;
/*     */     private int expectedCount;
/*     */     private int successfulCount;
/*     */     private int failureCount;
/*     */     private boolean doneAllocating;
/*     */     
/*     */     SimpleChannelPromiseAggregator(ChannelPromise promise, Channel c, EventExecutor e)
/*     */     {
/* 224 */       super(e);
/* 225 */       assert (promise != null);
/* 226 */       this.promise = promise;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ChannelPromise newPromise()
/*     */     {
/* 235 */       if (this.doneAllocating) {
/* 236 */         throw new IllegalStateException("Done allocating. No more promises can be allocated.");
/*     */       }
/* 238 */       this.expectedCount += 1;
/* 239 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ChannelPromise doneAllocatingPromises()
/*     */     {
/* 248 */       if (!this.doneAllocating) {
/* 249 */         this.doneAllocating = true;
/* 250 */         if (this.successfulCount == this.expectedCount) {
/* 251 */           this.promise.setSuccess();
/* 252 */           return super.setSuccess();
/*     */         }
/*     */       }
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     public boolean tryFailure(Throwable cause)
/*     */     {
/* 260 */       if (allowNotificationEvent()) {
/* 261 */         this.failureCount += 1;
/* 262 */         if (this.failureCount == 1) {
/* 263 */           this.promise.tryFailure(cause);
/* 264 */           return super.tryFailure(cause);
/*     */         }
/*     */         
/*     */ 
/* 268 */         return true;
/*     */       }
/* 270 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ChannelPromise setFailure(Throwable cause)
/*     */     {
/* 281 */       if (allowNotificationEvent()) {
/* 282 */         this.failureCount += 1;
/* 283 */         if (this.failureCount == 1) {
/* 284 */           this.promise.setFailure(cause);
/* 285 */           return super.setFailure(cause);
/*     */         }
/*     */       }
/* 288 */       return this;
/*     */     }
/*     */     
/*     */     private boolean allowNotificationEvent() {
/* 292 */       return this.successfulCount + this.failureCount < this.expectedCount;
/*     */     }
/*     */     
/*     */     public ChannelPromise setSuccess(Void result)
/*     */     {
/* 297 */       if (allowNotificationEvent()) {
/* 298 */         this.successfulCount += 1;
/* 299 */         if ((this.successfulCount == this.expectedCount) && (this.doneAllocating)) {
/* 300 */           this.promise.setSuccess(result);
/* 301 */           return super.setSuccess(result);
/*     */         }
/*     */       }
/* 304 */       return this;
/*     */     }
/*     */     
/*     */     public boolean trySuccess(Void result)
/*     */     {
/* 309 */       if (allowNotificationEvent()) {
/* 310 */         this.successfulCount += 1;
/* 311 */         if ((this.successfulCount == this.expectedCount) && (this.doneAllocating)) {
/* 312 */           this.promise.trySuccess(result);
/* 313 */           return super.trySuccess(result);
/*     */         }
/*     */         
/*     */ 
/* 317 */         return true;
/*     */       }
/* 319 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T extends Throwable> T failAndThrow(ChannelPromise promise, T cause)
/*     */     throws Throwable
/*     */   {
/* 327 */     if (!promise.isDone()) {
/* 328 */       promise.setFailure(cause);
/*     */     }
/* 330 */     throw cause;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2CodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */