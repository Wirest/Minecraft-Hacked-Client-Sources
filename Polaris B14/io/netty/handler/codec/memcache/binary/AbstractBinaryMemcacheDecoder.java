/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectDecoder;
/*     */ import io.netty.handler.codec.memcache.DefaultLastMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.DefaultMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.LastMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.MemcacheContent;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBinaryMemcacheDecoder<M extends BinaryMemcacheMessage>
/*     */   extends AbstractMemcacheObjectDecoder
/*     */ {
/*     */   public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
/*     */   private final int chunkSize;
/*     */   private M currentMessage;
/*     */   private int alreadyReadChunkSize;
/*  48 */   private State state = State.READ_HEADER;
/*     */   
/*     */ 
/*     */ 
/*     */   protected AbstractBinaryMemcacheDecoder()
/*     */   {
/*  54 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractBinaryMemcacheDecoder(int chunkSize)
/*     */   {
/*  63 */     if (chunkSize < 0) {
/*  64 */       throw new IllegalArgumentException("chunkSize must be a positive integer: " + chunkSize);
/*     */     }
/*     */     
/*  67 */     this.chunkSize = chunkSize;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  72 */     switch (this.state) {
/*     */     case READ_HEADER:  try {
/*  74 */         if (in.readableBytes() < 24) {
/*  75 */           return;
/*     */         }
/*  77 */         resetDecoder();
/*     */         
/*  79 */         this.currentMessage = decodeHeader(in);
/*  80 */         this.state = State.READ_EXTRAS;
/*     */       } catch (Exception e) {
/*  82 */         out.add(invalidMessage(e));
/*  83 */         return;
/*     */       }
/*     */     case READ_EXTRAS:  try {
/*  86 */         byte extrasLength = this.currentMessage.extrasLength();
/*  87 */         if (extrasLength > 0) {
/*  88 */           if (in.readableBytes() < extrasLength) {
/*  89 */             return;
/*     */           }
/*     */           
/*  92 */           this.currentMessage.setExtras(ByteBufUtil.readBytes(ctx.alloc(), in, extrasLength));
/*     */         }
/*     */         
/*  95 */         this.state = State.READ_KEY;
/*     */       } catch (Exception e) {
/*  97 */         out.add(invalidMessage(e));
/*  98 */         return;
/*     */       }
/*     */     case READ_KEY:  try {
/* 101 */         short keyLength = this.currentMessage.keyLength();
/* 102 */         if (keyLength > 0) {
/* 103 */           if (in.readableBytes() < keyLength) {
/* 104 */             return;
/*     */           }
/*     */           
/* 107 */           this.currentMessage.setKey(in.toString(in.readerIndex(), keyLength, CharsetUtil.UTF_8));
/* 108 */           in.skipBytes(keyLength);
/*     */         }
/*     */         
/* 111 */         out.add(this.currentMessage);
/* 112 */         this.state = State.READ_CONTENT;
/*     */       } catch (Exception e) {
/* 114 */         out.add(invalidMessage(e));
/* 115 */         return;
/*     */       }
/*     */     case READ_CONTENT:  try {
/* 118 */         int valueLength = this.currentMessage.totalBodyLength() - this.currentMessage.keyLength() - this.currentMessage.extrasLength();
/*     */         
/*     */ 
/* 121 */         int toRead = in.readableBytes();
/* 122 */         if (valueLength > 0) {
/* 123 */           if (toRead == 0) {
/* 124 */             return;
/*     */           }
/*     */           
/* 127 */           if (toRead > this.chunkSize) {
/* 128 */             toRead = this.chunkSize;
/*     */           }
/*     */           
/* 131 */           int remainingLength = valueLength - this.alreadyReadChunkSize;
/* 132 */           if (toRead > remainingLength) {
/* 133 */             toRead = remainingLength;
/*     */           }
/*     */           
/* 136 */           ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
/*     */           MemcacheContent chunk;
/*     */           MemcacheContent chunk;
/* 139 */           if (this.alreadyReadChunkSize += toRead >= valueLength) {
/* 140 */             chunk = new DefaultLastMemcacheContent(chunkBuffer);
/*     */           } else {
/* 142 */             chunk = new DefaultMemcacheContent(chunkBuffer);
/*     */           }
/*     */           
/* 145 */           out.add(chunk);
/* 146 */           if (this.alreadyReadChunkSize < valueLength) {
/* 147 */             return;
/*     */           }
/*     */         } else {
/* 150 */           out.add(LastMemcacheContent.EMPTY_LAST_CONTENT);
/*     */         }
/*     */         
/* 153 */         this.state = State.READ_HEADER;
/* 154 */         return;
/*     */       } catch (Exception e) {
/* 156 */         out.add(invalidChunk(e));
/* 157 */         return;
/*     */       }
/*     */     case BAD_MESSAGE: 
/* 160 */       in.skipBytes(actualReadableBytes());
/* 161 */       return;
/*     */     }
/* 163 */     throw new Error("Unknown state reached: " + this.state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private M invalidMessage(Exception cause)
/*     */   {
/* 174 */     this.state = State.BAD_MESSAGE;
/* 175 */     M message = buildInvalidMessage();
/* 176 */     message.setDecoderResult(DecoderResult.failure(cause));
/* 177 */     return message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MemcacheContent invalidChunk(Exception cause)
/*     */   {
/* 187 */     this.state = State.BAD_MESSAGE;
/* 188 */     MemcacheContent chunk = new DefaultLastMemcacheContent(Unpooled.EMPTY_BUFFER);
/* 189 */     chunk.setDecoderResult(DecoderResult.failure(cause));
/* 190 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 201 */     super.channelInactive(ctx);
/*     */     
/* 203 */     if (this.currentMessage != null) {
/* 204 */       this.currentMessage.release();
/*     */     }
/*     */     
/* 207 */     resetDecoder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void resetDecoder()
/*     */   {
/* 214 */     this.currentMessage = null;
/* 215 */     this.alreadyReadChunkSize = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract M decodeHeader(ByteBuf paramByteBuf);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract M buildInvalidMessage();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static enum State
/*     */   {
/* 244 */     READ_HEADER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 249 */     READ_EXTRAS, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 254 */     READ_KEY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 259 */     READ_CONTENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 264 */     BAD_MESSAGE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */