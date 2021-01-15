/*     */ package io.netty.handler.codec.stomp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.AppendableCharSequence;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.List;
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
/*     */ public class StompSubframeDecoder
/*     */   extends ReplayingDecoder<State>
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 8132;
/*     */   private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
/*     */   private final int maxLineLength;
/*     */   private final int maxChunkSize;
/*     */   private int alreadyReadChunkSize;
/*     */   private LastStompContentSubframe lastContent;
/*     */   private long contentLength;
/*     */   
/*     */   static enum State
/*     */   {
/*  64 */     SKIP_CONTROL_CHARACTERS, 
/*  65 */     READ_HEADERS, 
/*  66 */     READ_CONTENT, 
/*  67 */     FINALIZE_FRAME_READ, 
/*  68 */     BAD_FRAME, 
/*  69 */     INVALID_CHUNK;
/*     */     
/*     */ 
/*     */ 
/*     */     private State() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public StompSubframeDecoder()
/*     */   {
/*  79 */     this(1024, 8132);
/*     */   }
/*     */   
/*     */   public StompSubframeDecoder(int maxLineLength, int maxChunkSize) {
/*  83 */     super(State.SKIP_CONTROL_CHARACTERS);
/*  84 */     if (maxLineLength <= 0) {
/*  85 */       throw new IllegalArgumentException("maxLineLength must be a positive integer: " + maxLineLength);
/*     */     }
/*     */     
/*     */ 
/*  89 */     if (maxChunkSize <= 0) {
/*  90 */       throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
/*     */     }
/*     */     
/*     */ 
/*  94 */     this.maxChunkSize = maxChunkSize;
/*  95 */     this.maxLineLength = maxLineLength;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/* 100 */     switch ((State)state()) {
/*     */     case SKIP_CONTROL_CHARACTERS: 
/* 102 */       skipControlCharacters(in);
/* 103 */       checkpoint(State.READ_HEADERS);
/*     */     
/*     */     case READ_HEADERS: 
/* 106 */       StompCommand command = StompCommand.UNKNOWN;
/* 107 */       StompHeadersSubframe frame = null;
/*     */       try {
/* 109 */         command = readCommand(in);
/* 110 */         frame = new DefaultStompHeadersSubframe(command);
/* 111 */         checkpoint(readHeaders(in, frame.headers()));
/* 112 */         out.add(frame);
/*     */       } catch (Exception e) {
/* 114 */         if (frame == null) {
/* 115 */           frame = new DefaultStompHeadersSubframe(command);
/*     */         }
/* 117 */         frame.setDecoderResult(DecoderResult.failure(e));
/* 118 */         out.add(frame);
/* 119 */         checkpoint(State.BAD_FRAME);
/* 120 */         return;
/*     */       }
/*     */     
/*     */     case BAD_FRAME: 
/* 124 */       in.skipBytes(actualReadableBytes());
/* 125 */       return;
/*     */     }
/*     */     try {
/* 128 */       switch ((State)state()) {
/*     */       case READ_CONTENT: 
/* 130 */         int toRead = in.readableBytes();
/* 131 */         if (toRead == 0) {
/* 132 */           return;
/*     */         }
/* 134 */         if (toRead > this.maxChunkSize) {
/* 135 */           toRead = this.maxChunkSize;
/*     */         }
/* 137 */         int remainingLength = (int)(this.contentLength - this.alreadyReadChunkSize);
/* 138 */         if (toRead > remainingLength) {
/* 139 */           toRead = remainingLength;
/*     */         }
/* 141 */         ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
/* 142 */         if (this.alreadyReadChunkSize += toRead >= this.contentLength) {
/* 143 */           this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
/* 144 */           checkpoint(State.FINALIZE_FRAME_READ);
/*     */         }
/*     */         else {
/* 147 */           DefaultStompContentSubframe chunk = new DefaultStompContentSubframe(chunkBuffer);
/* 148 */           out.add(chunk);
/*     */         }
/* 150 */         if (this.alreadyReadChunkSize < this.contentLength) {
/* 151 */           return;
/*     */         }
/*     */       
/*     */       case FINALIZE_FRAME_READ: 
/* 155 */         skipNullCharacter(in);
/* 156 */         if (this.lastContent == null) {
/* 157 */           this.lastContent = LastStompContentSubframe.EMPTY_LAST_CONTENT;
/*     */         }
/* 159 */         out.add(this.lastContent);
/* 160 */         resetDecoder();
/*     */       }
/*     */     } catch (Exception e) {
/* 163 */       StompContentSubframe errorContent = new DefaultLastStompContentSubframe(Unpooled.EMPTY_BUFFER);
/* 164 */       errorContent.setDecoderResult(DecoderResult.failure(e));
/* 165 */       out.add(errorContent);
/* 166 */       checkpoint(State.BAD_FRAME);
/*     */     }
/*     */   }
/*     */   
/*     */   private StompCommand readCommand(ByteBuf in) {
/* 171 */     String commandStr = readLine(in, this.maxLineLength);
/* 172 */     StompCommand command = null;
/*     */     try {
/* 174 */       command = StompCommand.valueOf(commandStr);
/*     */     }
/*     */     catch (IllegalArgumentException iae) {}
/*     */     
/* 178 */     if (command == null) {
/* 179 */       commandStr = commandStr.toUpperCase(Locale.US);
/*     */       try {
/* 181 */         command = StompCommand.valueOf(commandStr);
/*     */       }
/*     */       catch (IllegalArgumentException iae) {}
/*     */     }
/*     */     
/* 186 */     if (command == null) {
/* 187 */       throw new DecoderException("failed to read command from channel");
/*     */     }
/* 189 */     return command;
/*     */   }
/*     */   
/*     */   private State readHeaders(ByteBuf buffer, StompHeaders headers) {
/*     */     for (;;) {
/* 194 */       String line = readLine(buffer, this.maxLineLength);
/* 195 */       if (!line.isEmpty()) {
/* 196 */         String[] split = StringUtil.split(line, ':');
/* 197 */         if (split.length == 2) {
/* 198 */           headers.add(split[0], split[1]);
/*     */         }
/*     */       } else {
/* 201 */         long contentLength = -1L;
/* 202 */         if (headers.contains(StompHeaders.CONTENT_LENGTH)) {
/* 203 */           contentLength = getContentLength(headers, 0L);
/*     */         } else {
/* 205 */           int globalIndex = ByteBufUtil.indexOf(buffer, buffer.readerIndex(), buffer.writerIndex(), (byte)0);
/*     */           
/* 207 */           if (globalIndex != -1) {
/* 208 */             contentLength = globalIndex - buffer.readerIndex();
/*     */           }
/*     */         }
/* 211 */         if (contentLength > 0L) {
/* 212 */           this.contentLength = contentLength;
/* 213 */           return State.READ_CONTENT;
/*     */         }
/* 215 */         return State.FINALIZE_FRAME_READ;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static long getContentLength(StompHeaders headers, long defaultValue)
/*     */   {
/* 222 */     return headers.getLong(StompHeaders.CONTENT_LENGTH, defaultValue);
/*     */   }
/*     */   
/*     */   private static void skipNullCharacter(ByteBuf buffer) {
/* 226 */     byte b = buffer.readByte();
/* 227 */     if (b != 0) {
/* 228 */       throw new IllegalStateException("unexpected byte in buffer " + b + " while expecting NULL byte");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void skipControlCharacters(ByteBuf buffer) {
/*     */     byte b;
/*     */     do {
/* 235 */       b = buffer.readByte();
/* 236 */     } while ((b == 13) || (b == 10));
/* 237 */     buffer.readerIndex(buffer.readerIndex() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String readLine(ByteBuf buffer, int maxLineLength)
/*     */   {
/* 244 */     AppendableCharSequence buf = new AppendableCharSequence(128);
/* 245 */     int lineLength = 0;
/*     */     for (;;) {
/* 247 */       byte nextByte = buffer.readByte();
/* 248 */       if (nextByte == 13) {
/* 249 */         nextByte = buffer.readByte();
/* 250 */         if (nextByte == 10)
/* 251 */           return buf.toString();
/*     */       } else {
/* 253 */         if (nextByte == 10) {
/* 254 */           return buf.toString();
/*     */         }
/* 256 */         if (lineLength >= maxLineLength) {
/* 257 */           throw new TooLongFrameException("An STOMP line is larger than " + maxLineLength + " bytes.");
/*     */         }
/* 259 */         lineLength++;
/* 260 */         buf.append((char)nextByte);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetDecoder() {
/* 266 */     checkpoint(State.SKIP_CONTROL_CHARACTERS);
/* 267 */     this.contentLength = 0L;
/* 268 */     this.alreadyReadChunkSize = 0;
/* 269 */     this.lastContent = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\StompSubframeDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */