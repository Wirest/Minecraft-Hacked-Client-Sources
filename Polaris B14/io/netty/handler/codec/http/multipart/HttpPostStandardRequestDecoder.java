/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.LastHttpContent;
/*     */ import io.netty.handler.codec.http.QueryStringDecoder;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ public class HttpPostStandardRequestDecoder
/*     */   implements InterfaceHttpPostRequestDecoder
/*     */ {
/*     */   private final HttpDataFactory factory;
/*     */   private final HttpRequest request;
/*     */   private final Charset charset;
/*     */   private boolean isLastChunk;
/*  71 */   private final List<InterfaceHttpData> bodyListHttpData = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBuf undecodedChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bodyListHttpDataRank;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   private HttpPostRequestDecoder.MultiPartStatus currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
/*     */   
/*     */ 
/*     */   private Attribute currentAttribute;
/*     */   
/*     */ 
/*     */   private boolean destroyed;
/*     */   
/*     */ 
/* 101 */   private int discardThreshold = 10485760;
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
/*     */   public HttpPostStandardRequestDecoder(HttpRequest request)
/*     */   {
/* 114 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
/*     */   }
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
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request)
/*     */   {
/* 130 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
/*     */   }
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
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset)
/*     */   {
/* 148 */     if (factory == null) {
/* 149 */       throw new NullPointerException("factory");
/*     */     }
/* 151 */     if (request == null) {
/* 152 */       throw new NullPointerException("request");
/*     */     }
/* 154 */     if (charset == null) {
/* 155 */       throw new NullPointerException("charset");
/*     */     }
/* 157 */     this.request = request;
/* 158 */     this.charset = charset;
/* 159 */     this.factory = factory;
/* 160 */     if ((request instanceof HttpContent))
/*     */     {
/*     */ 
/* 163 */       offer((HttpContent)request);
/*     */     } else {
/* 165 */       this.undecodedChunk = Unpooled.buffer();
/* 166 */       parseBody();
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkDestroyed() {
/* 171 */     if (this.destroyed) {
/* 172 */       throw new IllegalStateException(HttpPostStandardRequestDecoder.class.getSimpleName() + " was destroyed already");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMultipart()
/*     */   {
/* 184 */     checkDestroyed();
/* 185 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDiscardThreshold(int discardThreshold)
/*     */   {
/* 195 */     if (discardThreshold < 0) {
/* 196 */       throw new IllegalArgumentException("discardThreshold must be >= 0");
/*     */     }
/* 198 */     this.discardThreshold = discardThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDiscardThreshold()
/*     */   {
/* 206 */     return this.discardThreshold;
/*     */   }
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
/*     */   public List<InterfaceHttpData> getBodyHttpDatas()
/*     */   {
/* 221 */     checkDestroyed();
/*     */     
/* 223 */     if (!this.isLastChunk) {
/* 224 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 226 */     return this.bodyListHttpData;
/*     */   }
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
/*     */   public List<InterfaceHttpData> getBodyHttpDatas(String name)
/*     */   {
/* 242 */     checkDestroyed();
/*     */     
/* 244 */     if (!this.isLastChunk) {
/* 245 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 247 */     return (List)this.bodyMapHttpData.get(name);
/*     */   }
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
/*     */   public InterfaceHttpData getBodyHttpData(String name)
/*     */   {
/* 264 */     checkDestroyed();
/*     */     
/* 266 */     if (!this.isLastChunk) {
/* 267 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 269 */     List<InterfaceHttpData> list = (List)this.bodyMapHttpData.get(name);
/* 270 */     if (list != null) {
/* 271 */       return (InterfaceHttpData)list.get(0);
/*     */     }
/* 273 */     return null;
/*     */   }
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
/*     */   public HttpPostStandardRequestDecoder offer(HttpContent content)
/*     */   {
/* 287 */     checkDestroyed();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 292 */     ByteBuf buf = content.content();
/* 293 */     if (this.undecodedChunk == null) {
/* 294 */       this.undecodedChunk = buf.copy();
/*     */     } else {
/* 296 */       this.undecodedChunk.writeBytes(buf);
/*     */     }
/* 298 */     if ((content instanceof LastHttpContent)) {
/* 299 */       this.isLastChunk = true;
/*     */     }
/* 301 */     parseBody();
/* 302 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.writerIndex() > this.discardThreshold)) {
/* 303 */       this.undecodedChunk.discardReadBytes();
/*     */     }
/* 305 */     return this;
/*     */   }
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
/*     */   public boolean hasNext()
/*     */   {
/* 320 */     checkDestroyed();
/*     */     
/* 322 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)
/*     */     {
/* 324 */       if (this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
/* 325 */         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
/*     */       }
/*     */     }
/* 328 */     return (!this.bodyListHttpData.isEmpty()) && (this.bodyListHttpDataRank < this.bodyListHttpData.size());
/*     */   }
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
/*     */   public InterfaceHttpData next()
/*     */   {
/* 345 */     checkDestroyed();
/*     */     
/* 347 */     if (hasNext()) {
/* 348 */       return (InterfaceHttpData)this.bodyListHttpData.get(this.bodyListHttpDataRank++);
/*     */     }
/* 350 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseBody()
/*     */   {
/* 361 */     if ((this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) || (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)) {
/* 362 */       if (this.isLastChunk) {
/* 363 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/*     */       }
/* 365 */       return;
/*     */     }
/* 367 */     parseBodyAttributes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addHttpData(InterfaceHttpData data)
/*     */   {
/* 374 */     if (data == null) {
/* 375 */       return;
/*     */     }
/* 377 */     List<InterfaceHttpData> datas = (List)this.bodyMapHttpData.get(data.getName());
/* 378 */     if (datas == null) {
/* 379 */       datas = new ArrayList(1);
/* 380 */       this.bodyMapHttpData.put(data.getName(), datas);
/*     */     }
/* 382 */     datas.add(data);
/* 383 */     this.bodyListHttpData.add(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseBodyAttributesStandard()
/*     */   {
/* 395 */     int firstpos = this.undecodedChunk.readerIndex();
/* 396 */     int currentpos = firstpos;
/*     */     
/*     */ 
/* 399 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
/* 400 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/*     */     }
/* 402 */     boolean contRead = true;
/*     */     try { int ampersandpos;
/* 404 */       while ((this.undecodedChunk.isReadable()) && (contRead)) {
/* 405 */         char read = (char)this.undecodedChunk.readUnsignedByte();
/* 406 */         currentpos++;
/* 407 */         int ampersandpos; switch (this.currentStatus) {
/*     */         case DISPOSITION: 
/* 409 */           if (read == '=') {
/* 410 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/* 411 */             int equalpos = currentpos - 1;
/* 412 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
/*     */             
/* 414 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 415 */             firstpos = currentpos;
/* 416 */           } else if (read == '&') {
/* 417 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 418 */             ampersandpos = currentpos - 1;
/* 419 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
/*     */             
/* 421 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 422 */             this.currentAttribute.setValue("");
/* 423 */             addHttpData(this.currentAttribute);
/* 424 */             this.currentAttribute = null;
/* 425 */             firstpos = currentpos;
/* 426 */             contRead = true; }
/* 427 */           break;
/*     */         
/*     */         case FIELD: 
/* 430 */           if (read == '&') {
/* 431 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 432 */             ampersandpos = currentpos - 1;
/* 433 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 434 */             firstpos = currentpos;
/* 435 */             contRead = true;
/* 436 */           } else if (read == '\r') {
/* 437 */             if (this.undecodedChunk.isReadable()) {
/* 438 */               read = (char)this.undecodedChunk.readUnsignedByte();
/* 439 */               currentpos++;
/* 440 */               if (read == '\n') {
/* 441 */                 this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 442 */                 int ampersandpos = currentpos - 2;
/* 443 */                 setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 444 */                 firstpos = currentpos;
/* 445 */                 contRead = false;
/*     */               }
/*     */               else {
/* 448 */                 throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
/*     */               }
/*     */             } else {
/* 451 */               currentpos--;
/*     */             }
/* 453 */           } else if (read == '\n') {
/* 454 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 455 */             ampersandpos = currentpos - 1;
/* 456 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 457 */             firstpos = currentpos;
/* 458 */             contRead = false;
/*     */           }
/*     */           
/*     */           break;
/*     */         default: 
/* 463 */           contRead = false;
/*     */         }
/*     */       }
/* 466 */       if ((this.isLastChunk) && (this.currentAttribute != null))
/*     */       {
/* 468 */         ampersandpos = currentpos;
/* 469 */         if (ampersandpos > firstpos) {
/* 470 */           setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 471 */         } else if (!this.currentAttribute.isCompleted()) {
/* 472 */           setFinalBuffer(Unpooled.EMPTY_BUFFER);
/*     */         }
/* 474 */         firstpos = currentpos;
/* 475 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/* 476 */         this.undecodedChunk.readerIndex(firstpos);
/* 477 */         return;
/*     */       }
/* 479 */       if ((contRead) && (this.currentAttribute != null))
/*     */       {
/* 481 */         if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
/* 482 */           this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
/*     */           
/* 484 */           firstpos = currentpos;
/*     */         }
/* 486 */         this.undecodedChunk.readerIndex(firstpos);
/*     */       }
/*     */       else {
/* 489 */         this.undecodedChunk.readerIndex(firstpos);
/*     */       }
/*     */     }
/*     */     catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
/* 493 */       this.undecodedChunk.readerIndex(firstpos);
/* 494 */       throw e;
/*     */     }
/*     */     catch (IOException e) {
/* 497 */       this.undecodedChunk.readerIndex(firstpos);
/* 498 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void parseBodyAttributes()
/*     */   {
/*     */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 513 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*     */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/* 515 */       parseBodyAttributesStandard();
/* 516 */       return;
/*     */     }
/* 518 */     int firstpos = this.undecodedChunk.readerIndex();
/* 519 */     int currentpos = firstpos;
/*     */     
/*     */ 
/* 522 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
/* 523 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/*     */     }
/* 525 */     boolean contRead = true;
/*     */     try { int ampersandpos;
/* 527 */       while (sao.pos < sao.limit) {
/* 528 */         char read = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/* 529 */         currentpos++;
/* 530 */         int ampersandpos; switch (this.currentStatus) {
/*     */         case DISPOSITION: 
/* 532 */           if (read == '=') {
/* 533 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/* 534 */             int equalpos = currentpos - 1;
/* 535 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
/*     */             
/* 537 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 538 */             firstpos = currentpos;
/* 539 */           } else if (read == '&') {
/* 540 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 541 */             ampersandpos = currentpos - 1;
/* 542 */             String key = decodeAttribute(this.undecodedChunk.toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
/*     */             
/* 544 */             this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 545 */             this.currentAttribute.setValue("");
/* 546 */             addHttpData(this.currentAttribute);
/* 547 */             this.currentAttribute = null;
/* 548 */             firstpos = currentpos;
/* 549 */             contRead = true; }
/* 550 */           break;
/*     */         
/*     */         case FIELD: 
/* 553 */           if (read == '&') {
/* 554 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 555 */             ampersandpos = currentpos - 1;
/* 556 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 557 */             firstpos = currentpos;
/* 558 */             contRead = true;
/* 559 */           } else if (read == '\r') {
/* 560 */             if (sao.pos < sao.limit) {
/* 561 */               read = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/* 562 */               currentpos++;
/* 563 */               if (read == '\n') {
/* 564 */                 this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 565 */                 int ampersandpos = currentpos - 2;
/* 566 */                 sao.setReadPosition(0);
/* 567 */                 setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 568 */                 firstpos = currentpos;
/* 569 */                 contRead = false;
/*     */                 
/*     */                 break label514;
/*     */               }
/* 573 */               sao.setReadPosition(0);
/* 574 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
/*     */             }
/*     */             
/* 577 */             if (sao.limit > 0) {
/* 578 */               currentpos--;
/*     */             }
/*     */           }
/* 581 */           else if (read == '\n') {
/* 582 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 583 */             ampersandpos = currentpos - 1;
/* 584 */             sao.setReadPosition(0);
/* 585 */             setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 586 */             firstpos = currentpos;
/* 587 */             contRead = false; }
/* 588 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         default: 
/* 593 */           sao.setReadPosition(0);
/* 594 */           contRead = false;
/*     */           break label514; }
/*     */       }
/*     */       label514:
/* 598 */       if ((this.isLastChunk) && (this.currentAttribute != null))
/*     */       {
/* 600 */         ampersandpos = currentpos;
/* 601 */         if (ampersandpos > firstpos) {
/* 602 */           setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 603 */         } else if (!this.currentAttribute.isCompleted()) {
/* 604 */           setFinalBuffer(Unpooled.EMPTY_BUFFER);
/*     */         }
/* 606 */         firstpos = currentpos;
/* 607 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/* 608 */         this.undecodedChunk.readerIndex(firstpos);
/* 609 */         return;
/*     */       }
/* 611 */       if ((contRead) && (this.currentAttribute != null))
/*     */       {
/* 613 */         if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
/* 614 */           this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
/*     */           
/* 616 */           firstpos = currentpos;
/*     */         }
/* 618 */         this.undecodedChunk.readerIndex(firstpos);
/*     */       }
/*     */       else {
/* 621 */         this.undecodedChunk.readerIndex(firstpos);
/*     */       }
/*     */     }
/*     */     catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
/* 625 */       this.undecodedChunk.readerIndex(firstpos);
/* 626 */       throw e;
/*     */     }
/*     */     catch (IOException e) {
/* 629 */       this.undecodedChunk.readerIndex(firstpos);
/* 630 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setFinalBuffer(ByteBuf buffer) throws IOException {
/* 635 */     this.currentAttribute.addContent(buffer, true);
/* 636 */     String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
/* 637 */     this.currentAttribute.setValue(value);
/* 638 */     addHttpData(this.currentAttribute);
/* 639 */     this.currentAttribute = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String decodeAttribute(String s, Charset charset)
/*     */   {
/*     */     try
/*     */     {
/* 649 */       return QueryStringDecoder.decodeComponent(s, charset);
/*     */     } catch (IllegalArgumentException e) {
/* 651 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: '" + s + '\'', e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void skipControlCharacters()
/*     */   {
/*     */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*     */     try
/*     */     {
/* 661 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*     */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/*     */       try {
/* 664 */         skipControlCharactersStandard();
/*     */       } catch (IndexOutOfBoundsException e) {
/* 666 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*     */       }
/* 668 */       return;
/*     */     }
/*     */     
/* 671 */     while (sao.pos < sao.limit) {
/* 672 */       char c = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/* 673 */       if ((!Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/* 674 */         sao.setReadPosition(1);
/* 675 */         return;
/*     */       }
/*     */     }
/* 678 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
/*     */   }
/*     */   
/*     */   void skipControlCharactersStandard() {
/*     */     for (;;) {
/* 683 */       char c = (char)this.undecodedChunk.readUnsignedByte();
/* 684 */       if ((!Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/* 685 */         this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 686 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 697 */     checkDestroyed();
/* 698 */     cleanFiles();
/* 699 */     this.destroyed = true;
/*     */     
/* 701 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.refCnt() > 0)) {
/* 702 */       this.undecodedChunk.release();
/* 703 */       this.undecodedChunk = null;
/*     */     }
/*     */     
/*     */ 
/* 707 */     for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); i++) {
/* 708 */       ((InterfaceHttpData)this.bodyListHttpData.get(i)).release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void cleanFiles()
/*     */   {
/* 717 */     checkDestroyed();
/*     */     
/* 719 */     this.factory.cleanRequestHttpData(this.request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeHttpDataFromClean(InterfaceHttpData data)
/*     */   {
/* 727 */     checkDestroyed();
/*     */     
/* 729 */     this.factory.removeHttpDataFromClean(this.request, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\HttpPostStandardRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */