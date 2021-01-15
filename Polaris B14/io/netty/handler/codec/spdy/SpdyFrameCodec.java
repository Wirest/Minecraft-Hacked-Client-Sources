/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*     */ import java.util.List;
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
/*     */ public class SpdyFrameCodec
/*     */   extends ByteToMessageDecoder
/*     */   implements SpdyFrameDecoderDelegate
/*     */ {
/*  34 */   private static final SpdyProtocolException INVALID_FRAME = new SpdyProtocolException("Received invalid frame");
/*     */   
/*     */ 
/*     */   private final SpdyFrameDecoder spdyFrameDecoder;
/*     */   
/*     */ 
/*     */   private final SpdyFrameEncoder spdyFrameEncoder;
/*     */   
/*     */   private final SpdyHeaderBlockDecoder spdyHeaderBlockDecoder;
/*     */   
/*     */   private final SpdyHeaderBlockEncoder spdyHeaderBlockEncoder;
/*     */   
/*     */   private SpdyHeadersFrame spdyHeadersFrame;
/*     */   
/*     */   private SpdySettingsFrame spdySettingsFrame;
/*     */   
/*     */   private ChannelHandlerContext ctx;
/*     */   
/*     */ 
/*     */   public SpdyFrameCodec(SpdyVersion version)
/*     */   {
/*  55 */     this(version, 8192, 16384, 6, 15, 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpdyFrameCodec(SpdyVersion version, int maxChunkSize, int maxHeaderSize, int compressionLevel, int windowBits, int memLevel)
/*     */   {
/*  64 */     this(version, maxChunkSize, SpdyHeaderBlockDecoder.newInstance(version, maxHeaderSize), SpdyHeaderBlockEncoder.newInstance(version, compressionLevel, windowBits, memLevel));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SpdyFrameCodec(SpdyVersion version, int maxChunkSize, SpdyHeaderBlockDecoder spdyHeaderBlockDecoder, SpdyHeaderBlockEncoder spdyHeaderBlockEncoder)
/*     */   {
/*  71 */     this.spdyFrameDecoder = new SpdyFrameDecoder(version, this, maxChunkSize);
/*  72 */     this.spdyFrameEncoder = new SpdyFrameEncoder(version);
/*  73 */     this.spdyHeaderBlockDecoder = spdyHeaderBlockDecoder;
/*  74 */     this.spdyHeaderBlockEncoder = spdyHeaderBlockEncoder;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  79 */     super.handlerAdded(ctx);
/*  80 */     this.ctx = ctx;
/*  81 */     ctx.channel().closeFuture().addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/*  84 */         SpdyFrameCodec.this.spdyHeaderBlockDecoder.end();
/*  85 */         SpdyFrameCodec.this.spdyHeaderBlockEncoder.end();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  92 */     this.spdyFrameDecoder.decode(in);
/*     */   }
/*     */   
/*     */ 
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/*  99 */     if ((msg instanceof SpdyDataFrame))
/*     */     {
/* 101 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 102 */       ByteBuf frame = this.spdyFrameEncoder.encodeDataFrame(ctx.alloc(), spdyDataFrame.streamId(), spdyDataFrame.isLast(), spdyDataFrame.content());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */       spdyDataFrame.release();
/* 109 */       ctx.write(frame, promise);
/*     */     }
/* 111 */     else if ((msg instanceof SpdySynStreamFrame))
/*     */     {
/* 113 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 114 */       ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynStreamFrame);
/*     */       ByteBuf frame;
/* 116 */       try { frame = this.spdyFrameEncoder.encodeSynStreamFrame(ctx.alloc(), spdySynStreamFrame.streamId(), spdySynStreamFrame.associatedStreamId(), spdySynStreamFrame.priority(), spdySynStreamFrame.isLast(), spdySynStreamFrame.isUnidirectional(), headerBlock);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 126 */         headerBlock.release();
/*     */       }
/* 128 */       ctx.write(frame, promise);
/*     */     }
/* 130 */     else if ((msg instanceof SpdySynReplyFrame))
/*     */     {
/* 132 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 133 */       ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynReplyFrame);
/*     */       ByteBuf frame;
/* 135 */       try { frame = this.spdyFrameEncoder.encodeSynReplyFrame(ctx.alloc(), spdySynReplyFrame.streamId(), spdySynReplyFrame.isLast(), headerBlock);
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 142 */         headerBlock.release();
/*     */       }
/* 144 */       ctx.write(frame, promise);
/*     */     }
/* 146 */     else if ((msg instanceof SpdyRstStreamFrame))
/*     */     {
/* 148 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 149 */       ByteBuf frame = this.spdyFrameEncoder.encodeRstStreamFrame(ctx.alloc(), spdyRstStreamFrame.streamId(), spdyRstStreamFrame.status().code());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 154 */       ctx.write(frame, promise);
/*     */     }
/* 156 */     else if ((msg instanceof SpdySettingsFrame))
/*     */     {
/* 158 */       SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
/* 159 */       ByteBuf frame = this.spdyFrameEncoder.encodeSettingsFrame(ctx.alloc(), spdySettingsFrame);
/*     */       
/*     */ 
/*     */ 
/* 163 */       ctx.write(frame, promise);
/*     */     }
/* 165 */     else if ((msg instanceof SpdyPingFrame))
/*     */     {
/* 167 */       SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
/* 168 */       ByteBuf frame = this.spdyFrameEncoder.encodePingFrame(ctx.alloc(), spdyPingFrame.id());
/*     */       
/*     */ 
/*     */ 
/* 172 */       ctx.write(frame, promise);
/*     */     }
/* 174 */     else if ((msg instanceof SpdyGoAwayFrame))
/*     */     {
/* 176 */       SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame)msg;
/* 177 */       ByteBuf frame = this.spdyFrameEncoder.encodeGoAwayFrame(ctx.alloc(), spdyGoAwayFrame.lastGoodStreamId(), spdyGoAwayFrame.status().code());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 182 */       ctx.write(frame, promise);
/*     */     }
/* 184 */     else if ((msg instanceof SpdyHeadersFrame))
/*     */     {
/* 186 */       SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 187 */       ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdyHeadersFrame);
/*     */       ByteBuf frame;
/* 189 */       try { frame = this.spdyFrameEncoder.encodeHeadersFrame(ctx.alloc(), spdyHeadersFrame.streamId(), spdyHeadersFrame.isLast(), headerBlock);
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 196 */         headerBlock.release();
/*     */       }
/* 198 */       ctx.write(frame, promise);
/*     */     }
/* 200 */     else if ((msg instanceof SpdyWindowUpdateFrame))
/*     */     {
/* 202 */       SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)msg;
/* 203 */       ByteBuf frame = this.spdyFrameEncoder.encodeWindowUpdateFrame(ctx.alloc(), spdyWindowUpdateFrame.streamId(), spdyWindowUpdateFrame.deltaWindowSize());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 208 */       ctx.write(frame, promise);
/*     */     } else {
/* 210 */       throw new UnsupportedMessageTypeException(msg, new Class[0]);
/*     */     }
/*     */     ByteBuf frame;
/*     */   }
/*     */   
/*     */   public void readDataFrame(int streamId, boolean last, ByteBuf data) {
/* 216 */     SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamId, data);
/* 217 */     spdyDataFrame.setLast(last);
/* 218 */     this.ctx.fireChannelRead(spdyDataFrame);
/*     */   }
/*     */   
/*     */ 
/*     */   public void readSynStreamFrame(int streamId, int associatedToStreamId, byte priority, boolean last, boolean unidirectional)
/*     */   {
/* 224 */     SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority);
/* 225 */     spdySynStreamFrame.setLast(last);
/* 226 */     spdySynStreamFrame.setUnidirectional(unidirectional);
/* 227 */     this.spdyHeadersFrame = spdySynStreamFrame;
/*     */   }
/*     */   
/*     */   public void readSynReplyFrame(int streamId, boolean last)
/*     */   {
/* 232 */     SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
/* 233 */     spdySynReplyFrame.setLast(last);
/* 234 */     this.spdyHeadersFrame = spdySynReplyFrame;
/*     */   }
/*     */   
/*     */   public void readRstStreamFrame(int streamId, int statusCode)
/*     */   {
/* 239 */     SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, statusCode);
/* 240 */     this.ctx.fireChannelRead(spdyRstStreamFrame);
/*     */   }
/*     */   
/*     */   public void readSettingsFrame(boolean clearPersisted)
/*     */   {
/* 245 */     this.spdySettingsFrame = new DefaultSpdySettingsFrame();
/* 246 */     this.spdySettingsFrame.setClearPreviouslyPersistedSettings(clearPersisted);
/*     */   }
/*     */   
/*     */   public void readSetting(int id, int value, boolean persistValue, boolean persisted)
/*     */   {
/* 251 */     this.spdySettingsFrame.setValue(id, value, persistValue, persisted);
/*     */   }
/*     */   
/*     */   public void readSettingsEnd()
/*     */   {
/* 256 */     Object frame = this.spdySettingsFrame;
/* 257 */     this.spdySettingsFrame = null;
/* 258 */     this.ctx.fireChannelRead(frame);
/*     */   }
/*     */   
/*     */   public void readPingFrame(int id)
/*     */   {
/* 263 */     SpdyPingFrame spdyPingFrame = new DefaultSpdyPingFrame(id);
/* 264 */     this.ctx.fireChannelRead(spdyPingFrame);
/*     */   }
/*     */   
/*     */   public void readGoAwayFrame(int lastGoodStreamId, int statusCode)
/*     */   {
/* 269 */     SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(lastGoodStreamId, statusCode);
/* 270 */     this.ctx.fireChannelRead(spdyGoAwayFrame);
/*     */   }
/*     */   
/*     */   public void readHeadersFrame(int streamId, boolean last)
/*     */   {
/* 275 */     this.spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId);
/* 276 */     this.spdyHeadersFrame.setLast(last);
/*     */   }
/*     */   
/*     */   public void readWindowUpdateFrame(int streamId, int deltaWindowSize)
/*     */   {
/* 281 */     SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(streamId, deltaWindowSize);
/* 282 */     this.ctx.fireChannelRead(spdyWindowUpdateFrame);
/*     */   }
/*     */   
/*     */   public void readHeaderBlock(ByteBuf headerBlock)
/*     */   {
/*     */     try {
/* 288 */       this.spdyHeaderBlockDecoder.decode(this.ctx.alloc(), headerBlock, this.spdyHeadersFrame);
/*     */     } catch (Exception e) {
/* 290 */       this.ctx.fireExceptionCaught(e);
/*     */     } finally {
/* 292 */       headerBlock.release();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readHeaderBlockEnd()
/*     */   {
/* 298 */     Object frame = null;
/*     */     try {
/* 300 */       this.spdyHeaderBlockDecoder.endHeaderBlock(this.spdyHeadersFrame);
/* 301 */       frame = this.spdyHeadersFrame;
/* 302 */       this.spdyHeadersFrame = null;
/*     */     } catch (Exception e) {
/* 304 */       this.ctx.fireExceptionCaught(e);
/*     */     }
/* 306 */     if (frame != null) {
/* 307 */       this.ctx.fireChannelRead(frame);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readFrameError(String message)
/*     */   {
/* 313 */     this.ctx.fireExceptionCaught(INVALID_FRAME);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyFrameCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */