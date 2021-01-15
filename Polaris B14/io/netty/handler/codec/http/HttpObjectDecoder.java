/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufProcessor;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.AppendableCharSequence;
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
/*     */ public abstract class HttpObjectDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final String EMPTY_VALUE = "";
/*     */   private final int maxChunkSize;
/*     */   private final boolean chunkedSupported;
/*     */   protected final boolean validateHeaders;
/*     */   private final HeaderParser headerParser;
/*     */   private final LineParser lineParser;
/*     */   private HttpMessage message;
/*     */   private long chunkSize;
/* 112 */   private long contentLength = Long.MIN_VALUE;
/*     */   
/*     */ 
/*     */   private volatile boolean resetRequested;
/*     */   
/*     */   private CharSequence name;
/*     */   
/*     */   private CharSequence value;
/*     */   
/*     */   private LastHttpContent trailer;
/*     */   
/*     */ 
/*     */   private static enum State
/*     */   {
/* 126 */     SKIP_CONTROL_CHARS, 
/* 127 */     READ_INITIAL, 
/* 128 */     READ_HEADER, 
/* 129 */     READ_VARIABLE_LENGTH_CONTENT, 
/* 130 */     READ_FIXED_LENGTH_CONTENT, 
/* 131 */     READ_CHUNK_SIZE, 
/* 132 */     READ_CHUNKED_CONTENT, 
/* 133 */     READ_CHUNK_DELIMITER, 
/* 134 */     READ_CHUNK_FOOTER, 
/* 135 */     BAD_MESSAGE, 
/* 136 */     UPGRADED;
/*     */     
/*     */     private State() {} }
/* 139 */   private State currentState = State.SKIP_CONTROL_CHARS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpObjectDecoder()
/*     */   {
/* 147 */     this(4096, 8192, 8192, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported)
/*     */   {
/* 155 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders)
/*     */   {
/* 165 */     if (maxInitialLineLength <= 0) {
/* 166 */       throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength);
/*     */     }
/*     */     
/*     */ 
/* 170 */     if (maxHeaderSize <= 0) {
/* 171 */       throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + maxHeaderSize);
/*     */     }
/*     */     
/*     */ 
/* 175 */     if (maxChunkSize <= 0) {
/* 176 */       throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
/*     */     }
/*     */     
/*     */ 
/* 180 */     this.maxChunkSize = maxChunkSize;
/* 181 */     this.chunkedSupported = chunkedSupported;
/* 182 */     this.validateHeaders = validateHeaders;
/* 183 */     AppendableCharSequence seq = new AppendableCharSequence(128);
/* 184 */     this.lineParser = new LineParser(seq, maxInitialLineLength);
/* 185 */     this.headerParser = new HeaderParser(seq, maxHeaderSize);
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
/*     */   {
/* 190 */     if (this.resetRequested) {
/* 191 */       resetNow();
/*     */     }
/*     */     
/* 194 */     switch (this.currentState) {
/*     */     case SKIP_CONTROL_CHARS: 
/* 196 */       if (!skipControlCharacters(buffer)) {
/* 197 */         return;
/*     */       }
/* 199 */       this.currentState = State.READ_INITIAL;
/*     */     case READ_INITIAL: 
/*     */       try {
/* 202 */         AppendableCharSequence line = this.lineParser.parse(buffer);
/* 203 */         if (line == null) {
/* 204 */           return;
/*     */         }
/* 206 */         String[] initialLine = splitInitialLine(line);
/* 207 */         if (initialLine.length < 3)
/*     */         {
/* 209 */           this.currentState = State.SKIP_CONTROL_CHARS;
/* 210 */           return;
/*     */         }
/*     */         
/* 213 */         this.message = createMessage(initialLine);
/* 214 */         this.currentState = State.READ_HEADER;
/*     */       }
/*     */       catch (Exception e) {
/* 217 */         out.add(invalidMessage(e));
/* 218 */         return;
/*     */       }
/*     */     case READ_HEADER:  try {
/* 221 */         State nextState = readHeaders(buffer);
/* 222 */         if (nextState == null) {
/* 223 */           return;
/*     */         }
/* 225 */         this.currentState = nextState;
/* 226 */         switch (nextState)
/*     */         {
/*     */ 
/*     */         case SKIP_CONTROL_CHARS: 
/* 230 */           out.add(this.message);
/* 231 */           out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 232 */           resetNow();
/* 233 */           return;
/*     */         case READ_CHUNK_SIZE: 
/* 235 */           if (!this.chunkedSupported) {
/* 236 */             throw new IllegalArgumentException("Chunked messages not supported");
/*     */           }
/*     */           
/* 239 */           out.add(this.message);
/* 240 */           return; }
/*     */         
/* 242 */         long contentLength = contentLength();
/* 243 */         if ((contentLength == 0L) || ((contentLength == -1L) && (isDecodingRequest()))) {
/* 244 */           out.add(this.message);
/* 245 */           out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 246 */           resetNow();
/* 247 */           return;
/*     */         }
/*     */         
/* 250 */         assert ((nextState == State.READ_FIXED_LENGTH_CONTENT) || (nextState == State.READ_VARIABLE_LENGTH_CONTENT));
/*     */         
/*     */ 
/* 253 */         out.add(this.message);
/*     */         
/* 255 */         if (nextState == State.READ_FIXED_LENGTH_CONTENT)
/*     */         {
/* 257 */           this.chunkSize = contentLength;
/*     */         }
/*     */         
/*     */ 
/* 261 */         return;
/*     */       }
/*     */       catch (Exception e) {
/* 264 */         out.add(invalidMessage(e));
/* 265 */         return;
/*     */       }
/*     */     
/*     */     case READ_VARIABLE_LENGTH_CONTENT: 
/* 269 */       int toRead = Math.min(buffer.readableBytes(), this.maxChunkSize);
/* 270 */       if (toRead > 0) {
/* 271 */         ByteBuf content = buffer.readSlice(toRead).retain();
/* 272 */         out.add(new DefaultHttpContent(content));
/*     */       }
/* 274 */       return;
/*     */     
/*     */     case READ_FIXED_LENGTH_CONTENT: 
/* 277 */       int readLimit = buffer.readableBytes();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */       if (readLimit == 0) {
/* 286 */         return;
/*     */       }
/*     */       
/* 289 */       int toRead = Math.min(readLimit, this.maxChunkSize);
/* 290 */       if (toRead > this.chunkSize) {
/* 291 */         toRead = (int)this.chunkSize;
/*     */       }
/* 293 */       ByteBuf content = buffer.readSlice(toRead).retain();
/* 294 */       this.chunkSize -= toRead;
/*     */       
/* 296 */       if (this.chunkSize == 0L)
/*     */       {
/* 298 */         out.add(new DefaultLastHttpContent(content, this.validateHeaders));
/* 299 */         resetNow();
/*     */       } else {
/* 301 */         out.add(new DefaultHttpContent(content));
/*     */       }
/* 303 */       return;
/*     */     
/*     */ 
/*     */ 
/*     */     case READ_CHUNK_SIZE: 
/*     */       try
/*     */       {
/* 310 */         AppendableCharSequence line = this.lineParser.parse(buffer);
/* 311 */         if (line == null) {
/* 312 */           return;
/*     */         }
/* 314 */         int chunkSize = getChunkSize(line.toString());
/* 315 */         this.chunkSize = chunkSize;
/* 316 */         if (chunkSize == 0) {
/* 317 */           this.currentState = State.READ_CHUNK_FOOTER;
/* 318 */           return;
/*     */         }
/* 320 */         this.currentState = State.READ_CHUNKED_CONTENT;
/*     */       }
/*     */       catch (Exception e) {
/* 323 */         out.add(invalidChunk(e));
/* 324 */         return;
/*     */       }
/*     */     case READ_CHUNKED_CONTENT: 
/* 327 */       assert (this.chunkSize <= 2147483647L);
/* 328 */       int toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
/* 329 */       toRead = Math.min(toRead, buffer.readableBytes());
/* 330 */       if (toRead == 0) {
/* 331 */         return;
/*     */       }
/* 333 */       HttpContent chunk = new DefaultHttpContent(buffer.readSlice(toRead).retain());
/* 334 */       this.chunkSize -= toRead;
/*     */       
/* 336 */       out.add(chunk);
/*     */       
/* 338 */       if (this.chunkSize != 0L) {
/* 339 */         return;
/*     */       }
/* 341 */       this.currentState = State.READ_CHUNK_DELIMITER;
/*     */     
/*     */ 
/*     */     case READ_CHUNK_DELIMITER: 
/* 345 */       int wIdx = buffer.writerIndex();
/* 346 */       int rIdx = buffer.readerIndex();
/* 347 */       while (wIdx > rIdx) {
/* 348 */         byte next = buffer.getByte(rIdx++);
/* 349 */         if (next == 10) {
/* 350 */           this.currentState = State.READ_CHUNK_SIZE;
/* 351 */           break;
/*     */         }
/*     */       }
/* 354 */       buffer.readerIndex(rIdx);
/* 355 */       return;
/*     */     case READ_CHUNK_FOOTER: 
/*     */       try {
/* 358 */         LastHttpContent trailer = readTrailingHeaders(buffer);
/* 359 */         if (trailer == null) {
/* 360 */           return;
/*     */         }
/* 362 */         out.add(trailer);
/* 363 */         resetNow();
/* 364 */         return;
/*     */       } catch (Exception e) {
/* 366 */         out.add(invalidChunk(e));
/* 367 */         return;
/*     */       }
/*     */     
/*     */     case BAD_MESSAGE: 
/* 371 */       buffer.skipBytes(buffer.readableBytes());
/* 372 */       break;
/*     */     
/*     */     case UPGRADED: 
/* 375 */       int readableBytes = buffer.readableBytes();
/* 376 */       if (readableBytes > 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 381 */         out.add(buffer.readBytes(readableBytes));
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 390 */     decode(ctx, in, out);
/*     */     
/*     */ 
/* 393 */     if (this.message != null) {
/* 394 */       boolean chunked = HttpHeaderUtil.isTransferEncodingChunked(this.message);
/* 395 */       if ((this.currentState == State.READ_VARIABLE_LENGTH_CONTENT) && (!in.isReadable()) && (!chunked))
/*     */       {
/* 397 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 398 */         reset(); return;
/*     */       }
/*     */       
/*     */       boolean prematureClosure;
/*     */       boolean prematureClosure;
/* 403 */       if ((isDecodingRequest()) || (chunked))
/*     */       {
/* 405 */         prematureClosure = true;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 410 */         prematureClosure = contentLength() > 0L;
/*     */       }
/* 412 */       resetNow();
/*     */       
/* 414 */       if (!prematureClosure) {
/* 415 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isContentAlwaysEmpty(HttpMessage msg) {
/* 421 */     if ((msg instanceof HttpResponse)) {
/* 422 */       HttpResponse res = (HttpResponse)msg;
/* 423 */       int code = res.status().code();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */       if ((code >= 100) && (code < 200))
/*     */       {
/* 432 */         return (code != 101) || (res.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT));
/*     */       }
/*     */       
/* 435 */       switch (code) {
/*     */       case 204: case 205: case 304: 
/* 437 */         return true;
/*     */       }
/*     */     }
/* 440 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 448 */     this.resetRequested = true;
/*     */   }
/*     */   
/*     */   private void resetNow() {
/* 452 */     HttpMessage message = this.message;
/* 453 */     this.message = null;
/* 454 */     this.name = null;
/* 455 */     this.value = null;
/* 456 */     this.contentLength = Long.MIN_VALUE;
/* 457 */     this.lineParser.reset();
/* 458 */     this.headerParser.reset();
/* 459 */     this.trailer = null;
/* 460 */     if (!isDecodingRequest()) {
/* 461 */       HttpResponse res = (HttpResponse)message;
/* 462 */       if ((res != null) && (res.status().code() == 101)) {
/* 463 */         this.currentState = State.UPGRADED;
/* 464 */         return;
/*     */       }
/*     */     }
/*     */     
/* 468 */     this.currentState = State.SKIP_CONTROL_CHARS;
/*     */   }
/*     */   
/*     */   private HttpMessage invalidMessage(Exception cause) {
/* 472 */     this.currentState = State.BAD_MESSAGE;
/* 473 */     if (this.message != null) {
/* 474 */       this.message.setDecoderResult(DecoderResult.failure(cause));
/*     */     } else {
/* 476 */       this.message = createInvalidMessage();
/* 477 */       this.message.setDecoderResult(DecoderResult.failure(cause));
/*     */     }
/*     */     
/* 480 */     HttpMessage ret = this.message;
/* 481 */     this.message = null;
/* 482 */     return ret;
/*     */   }
/*     */   
/*     */   private HttpContent invalidChunk(Exception cause) {
/* 486 */     this.currentState = State.BAD_MESSAGE;
/* 487 */     HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
/* 488 */     chunk.setDecoderResult(DecoderResult.failure(cause));
/* 489 */     this.message = null;
/* 490 */     this.trailer = null;
/* 491 */     return chunk;
/*     */   }
/*     */   
/*     */   private static boolean skipControlCharacters(ByteBuf buffer) {
/* 495 */     boolean skiped = false;
/* 496 */     int wIdx = buffer.writerIndex();
/* 497 */     int rIdx = buffer.readerIndex();
/* 498 */     while (wIdx > rIdx) {
/* 499 */       int c = buffer.getUnsignedByte(rIdx++);
/* 500 */       if ((!Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/* 501 */         rIdx--;
/* 502 */         skiped = true;
/* 503 */         break;
/*     */       }
/*     */     }
/* 506 */     buffer.readerIndex(rIdx);
/* 507 */     return skiped;
/*     */   }
/*     */   
/*     */   private State readHeaders(ByteBuf buffer) {
/* 511 */     HttpMessage message = this.message;
/* 512 */     HttpHeaders headers = message.headers();
/*     */     
/* 514 */     AppendableCharSequence line = this.headerParser.parse(buffer);
/* 515 */     if (line == null) {
/* 516 */       return null;
/*     */     }
/* 518 */     if (line.length() > 0) {
/*     */       do {
/* 520 */         char firstChar = line.charAt(0);
/* 521 */         if ((this.name != null) && ((firstChar == ' ') || (firstChar == '\t'))) {
/* 522 */           StringBuilder buf = new StringBuilder(this.value.length() + line.length() + 1);
/* 523 */           buf.append(this.value).append(' ').append(line.toString().trim());
/*     */           
/*     */ 
/* 526 */           this.value = buf.toString();
/*     */         } else {
/* 528 */           if (this.name != null) {
/* 529 */             headers.add(this.name, this.value);
/*     */           }
/* 531 */           splitHeader(line);
/*     */         }
/*     */         
/* 534 */         line = this.headerParser.parse(buffer);
/* 535 */         if (line == null) {
/* 536 */           return null;
/*     */         }
/* 538 */       } while (line.length() > 0);
/*     */     }
/*     */     
/*     */ 
/* 542 */     if (this.name != null) {
/* 543 */       headers.add(this.name, this.value);
/*     */     }
/*     */     
/* 546 */     this.name = null;
/* 547 */     this.value = null;
/*     */     
/*     */     State nextState;
/*     */     State nextState;
/* 551 */     if (isContentAlwaysEmpty(message)) {
/* 552 */       HttpHeaderUtil.setTransferEncodingChunked(message, false);
/* 553 */       nextState = State.SKIP_CONTROL_CHARS; } else { State nextState;
/* 554 */       if (HttpHeaderUtil.isTransferEncodingChunked(message)) {
/* 555 */         nextState = State.READ_CHUNK_SIZE; } else { State nextState;
/* 556 */         if (contentLength() >= 0L) {
/* 557 */           nextState = State.READ_FIXED_LENGTH_CONTENT;
/*     */         } else
/* 559 */           nextState = State.READ_VARIABLE_LENGTH_CONTENT;
/*     */       } }
/* 561 */     return nextState;
/*     */   }
/*     */   
/*     */   private long contentLength() {
/* 565 */     if (this.contentLength == Long.MIN_VALUE) {
/* 566 */       this.contentLength = HttpHeaderUtil.getContentLength(this.message, -1L);
/*     */     }
/* 568 */     return this.contentLength;
/*     */   }
/*     */   
/*     */   private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
/* 572 */     AppendableCharSequence line = this.headerParser.parse(buffer);
/* 573 */     if (line == null) {
/* 574 */       return null;
/*     */     }
/* 576 */     CharSequence lastHeader = null;
/* 577 */     if (line.length() > 0) {
/* 578 */       LastHttpContent trailer = this.trailer;
/* 579 */       if (trailer == null) {
/* 580 */         trailer = this.trailer = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
/*     */       }
/*     */       do {
/* 583 */         char firstChar = line.charAt(0);
/* 584 */         if ((lastHeader != null) && ((firstChar == ' ') || (firstChar == '\t'))) {
/* 585 */           List<CharSequence> current = trailer.trailingHeaders().getAll(lastHeader);
/* 586 */           if (!current.isEmpty()) {
/* 587 */             int lastPos = current.size() - 1;
/* 588 */             String lineTrimmed = line.toString().trim();
/* 589 */             CharSequence currentLastPos = (CharSequence)current.get(lastPos);
/* 590 */             StringBuilder b = new StringBuilder(currentLastPos.length() + lineTrimmed.length());
/* 591 */             b.append(currentLastPos).append(lineTrimmed);
/*     */             
/* 593 */             current.set(lastPos, b.toString());
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 598 */           splitHeader(line);
/* 599 */           CharSequence headerName = this.name;
/* 600 */           if ((!HttpHeaderNames.CONTENT_LENGTH.equalsIgnoreCase(headerName)) && (!HttpHeaderNames.TRANSFER_ENCODING.equalsIgnoreCase(headerName)) && (!HttpHeaderNames.TRAILER.equalsIgnoreCase(headerName)))
/*     */           {
/*     */ 
/* 603 */             trailer.trailingHeaders().add(headerName, this.value);
/*     */           }
/* 605 */           lastHeader = this.name;
/*     */           
/* 607 */           this.name = null;
/* 608 */           this.value = null;
/*     */         }
/*     */         
/* 611 */         line = this.headerParser.parse(buffer);
/* 612 */         if (line == null) {
/* 613 */           return null;
/*     */         }
/* 615 */       } while (line.length() > 0);
/*     */       
/* 617 */       this.trailer = null;
/* 618 */       return trailer;
/*     */     }
/*     */     
/* 621 */     return LastHttpContent.EMPTY_LAST_CONTENT; }
/*     */   
/*     */   protected abstract boolean isDecodingRequest();
/*     */   
/*     */   protected abstract HttpMessage createMessage(String[] paramArrayOfString) throws Exception;
/*     */   
/*     */   protected abstract HttpMessage createInvalidMessage();
/*     */   
/* 629 */   private static int getChunkSize(String hex) { hex = hex.trim();
/* 630 */     for (int i = 0; i < hex.length(); i++) {
/* 631 */       char c = hex.charAt(i);
/* 632 */       if ((c == ';') || (Character.isWhitespace(c)) || (Character.isISOControl(c))) {
/* 633 */         hex = hex.substring(0, i);
/* 634 */         break;
/*     */       }
/*     */     }
/*     */     
/* 638 */     return Integer.parseInt(hex, 16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String[] splitInitialLine(AppendableCharSequence sb)
/*     */   {
/* 649 */     int aStart = findNonWhitespace(sb, 0);
/* 650 */     int aEnd = findWhitespace(sb, aStart);
/*     */     
/* 652 */     int bStart = findNonWhitespace(sb, aEnd);
/* 653 */     int bEnd = findWhitespace(sb, bStart);
/*     */     
/* 655 */     int cStart = findNonWhitespace(sb, bEnd);
/* 656 */     int cEnd = findEndOfString(sb);
/*     */     
/* 658 */     return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), cStart < cEnd ? sb.substring(cStart, cEnd) : "" };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void splitHeader(AppendableCharSequence sb)
/*     */   {
/* 665 */     int length = sb.length();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 672 */     int nameStart = findNonWhitespace(sb, 0);
/* 673 */     for (int nameEnd = nameStart; nameEnd < length; nameEnd++) {
/* 674 */       char ch = sb.charAt(nameEnd);
/* 675 */       if ((ch == ':') || (Character.isWhitespace(ch))) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 680 */     for (int colonEnd = nameEnd; colonEnd < length; colonEnd++) {
/* 681 */       if (sb.charAt(colonEnd) == ':') {
/* 682 */         colonEnd++;
/* 683 */         break;
/*     */       }
/*     */     }
/*     */     
/* 687 */     this.name = sb.substring(nameStart, nameEnd);
/* 688 */     int valueStart = findNonWhitespace(sb, colonEnd);
/* 689 */     if (valueStart == length) {
/* 690 */       this.value = "";
/*     */     } else {
/* 692 */       int valueEnd = findEndOfString(sb);
/* 693 */       this.value = sb.substring(valueStart, valueEnd);
/*     */     }
/*     */   }
/*     */   
/*     */   private static int findNonWhitespace(CharSequence sb, int offset)
/*     */   {
/* 699 */     for (int result = offset; result < sb.length(); result++) {
/* 700 */       if (!Character.isWhitespace(sb.charAt(result))) {
/*     */         break;
/*     */       }
/*     */     }
/* 704 */     return result;
/*     */   }
/*     */   
/*     */   private static int findWhitespace(CharSequence sb, int offset)
/*     */   {
/* 709 */     for (int result = offset; result < sb.length(); result++) {
/* 710 */       if (Character.isWhitespace(sb.charAt(result))) {
/*     */         break;
/*     */       }
/*     */     }
/* 714 */     return result;
/*     */   }
/*     */   
/*     */   private static int findEndOfString(CharSequence sb)
/*     */   {
/* 719 */     for (int result = sb.length(); result > 0; result--) {
/* 720 */       if (!Character.isWhitespace(sb.charAt(result - 1))) {
/*     */         break;
/*     */       }
/*     */     }
/* 724 */     return result;
/*     */   }
/*     */   
/*     */   private static class HeaderParser implements ByteBufProcessor {
/*     */     private final AppendableCharSequence seq;
/*     */     private final int maxLength;
/*     */     private int size;
/*     */     
/*     */     HeaderParser(AppendableCharSequence seq, int maxLength) {
/* 733 */       this.seq = seq;
/* 734 */       this.maxLength = maxLength;
/*     */     }
/*     */     
/*     */     public AppendableCharSequence parse(ByteBuf buffer) {
/* 738 */       this.seq.reset();
/* 739 */       int i = buffer.forEachByte(this);
/* 740 */       if (i == -1) {
/* 741 */         return null;
/*     */       }
/* 743 */       buffer.readerIndex(i + 1);
/* 744 */       return this.seq;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 748 */       this.size = 0;
/*     */     }
/*     */     
/*     */     public boolean process(byte value) throws Exception
/*     */     {
/* 753 */       char nextByte = (char)value;
/* 754 */       if (nextByte == '\r') {
/* 755 */         return true;
/*     */       }
/* 757 */       if (nextByte == '\n') {
/* 758 */         return false;
/*     */       }
/* 760 */       if (this.size >= this.maxLength)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 765 */         throw newException(this.maxLength);
/*     */       }
/* 767 */       this.size += 1;
/* 768 */       this.seq.append(nextByte);
/* 769 */       return true;
/*     */     }
/*     */     
/*     */     protected TooLongFrameException newException(int maxLength) {
/* 773 */       return new TooLongFrameException("HTTP header is larger than " + maxLength + " bytes.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LineParser extends HttpObjectDecoder.HeaderParser
/*     */   {
/*     */     LineParser(AppendableCharSequence seq, int maxLength) {
/* 780 */       super(maxLength);
/*     */     }
/*     */     
/*     */     public AppendableCharSequence parse(ByteBuf buffer)
/*     */     {
/* 785 */       reset();
/* 786 */       return super.parse(buffer);
/*     */     }
/*     */     
/*     */     protected TooLongFrameException newException(int maxLength)
/*     */     {
/* 791 */       return new TooLongFrameException("An HTTP line is larger than " + maxLength + " bytes.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */