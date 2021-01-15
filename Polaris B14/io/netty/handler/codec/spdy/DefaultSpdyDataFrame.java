/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ 
/*     */ public class DefaultSpdyDataFrame
/*     */   extends DefaultSpdyStreamFrame
/*     */   implements SpdyDataFrame
/*     */ {
/*     */   private final ByteBuf data;
/*     */   
/*     */   public DefaultSpdyDataFrame(int streamId)
/*     */   {
/*  36 */     this(streamId, Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultSpdyDataFrame(int streamId, ByteBuf data)
/*     */   {
/*  46 */     super(streamId);
/*  47 */     if (data == null) {
/*  48 */       throw new NullPointerException("data");
/*     */     }
/*  50 */     this.data = validate(data);
/*     */   }
/*     */   
/*     */   private static ByteBuf validate(ByteBuf data) {
/*  54 */     if (data.readableBytes() > 16777215) {
/*  55 */       throw new IllegalArgumentException("data payload cannot exceed 16777215 bytes");
/*     */     }
/*     */     
/*  58 */     return data;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame setStreamId(int streamId)
/*     */   {
/*  63 */     super.setStreamId(streamId);
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame setLast(boolean last)
/*     */   {
/*  69 */     super.setLast(last);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  75 */     if (this.data.refCnt() <= 0) {
/*  76 */       throw new IllegalReferenceCountException(this.data.refCnt());
/*     */     }
/*  78 */     return this.data;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame copy()
/*     */   {
/*  83 */     SpdyDataFrame frame = new DefaultSpdyDataFrame(streamId(), content().copy());
/*  84 */     frame.setLast(isLast());
/*  85 */     return frame;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame duplicate()
/*     */   {
/*  90 */     SpdyDataFrame frame = new DefaultSpdyDataFrame(streamId(), content().duplicate());
/*  91 */     frame.setLast(isLast());
/*  92 */     return frame;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  97 */     return this.data.refCnt();
/*     */   }
/*     */   
/*     */   public SpdyDataFrame retain()
/*     */   {
/* 102 */     this.data.retain();
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame retain(int increment)
/*     */   {
/* 108 */     this.data.retain(increment);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame touch()
/*     */   {
/* 114 */     this.data.touch();
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyDataFrame touch(Object hint)
/*     */   {
/* 120 */     this.data.touch(hint);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 126 */     return this.data.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 131 */     return this.data.release(decrement);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 136 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Size = ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     if (refCnt() == 0) {
/* 147 */       buf.append("(freed)");
/*     */     } else {
/* 149 */       buf.append(content().readableBytes());
/*     */     }
/* 151 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdyDataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */