/*      */ package io.netty.handler.codec.http.multipart;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.handler.codec.AsciiString;
/*      */ import io.netty.handler.codec.http.HttpConstants;
/*      */ import io.netty.handler.codec.http.HttpContent;
/*      */ import io.netty.handler.codec.http.HttpHeaderNames;
/*      */ import io.netty.handler.codec.http.HttpHeaderValues;
/*      */ import io.netty.handler.codec.http.HttpHeaders;
/*      */ import io.netty.handler.codec.http.HttpRequest;
/*      */ import io.netty.handler.codec.http.LastHttpContent;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.IOException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpPostMultipartRequestDecoder
/*      */   implements InterfaceHttpPostRequestDecoder
/*      */ {
/*      */   private final HttpDataFactory factory;
/*      */   private final HttpRequest request;
/*      */   private Charset charset;
/*      */   private boolean isLastChunk;
/*   74 */   private final List<InterfaceHttpData> bodyListHttpData = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   79 */   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf undecodedChunk;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int bodyListHttpDataRank;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String multipartDataBoundary;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String multipartMixedBoundary;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  106 */   private HttpPostRequestDecoder.MultiPartStatus currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
/*      */   
/*      */ 
/*      */ 
/*      */   private Map<CharSequence, Attribute> currentFieldAttributes;
/*      */   
/*      */ 
/*      */ 
/*      */   private FileUpload currentFileUpload;
/*      */   
/*      */ 
/*      */ 
/*      */   private Attribute currentAttribute;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean destroyed;
/*      */   
/*      */ 
/*  125 */   private int discardThreshold = 10485760;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostMultipartRequestDecoder(HttpRequest request)
/*      */   {
/*  138 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request)
/*      */   {
/*  154 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset)
/*      */   {
/*  172 */     if (factory == null) {
/*  173 */       throw new NullPointerException("factory");
/*      */     }
/*  175 */     if (request == null) {
/*  176 */       throw new NullPointerException("request");
/*      */     }
/*  178 */     if (charset == null) {
/*  179 */       throw new NullPointerException("charset");
/*      */     }
/*  181 */     this.request = request;
/*  182 */     this.charset = charset;
/*  183 */     this.factory = factory;
/*      */     
/*      */ 
/*  186 */     setMultipart((String)this.request.headers().getAndConvert(HttpHeaderNames.CONTENT_TYPE));
/*  187 */     if ((request instanceof HttpContent))
/*      */     {
/*      */ 
/*  190 */       offer((HttpContent)request);
/*      */     } else {
/*  192 */       this.undecodedChunk = Unpooled.buffer();
/*  193 */       parseBody();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setMultipart(String contentType)
/*      */   {
/*  201 */     String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentType);
/*  202 */     if (dataBoundary != null) {
/*  203 */       this.multipartDataBoundary = dataBoundary[0];
/*  204 */       if ((dataBoundary.length > 1) && (dataBoundary[1] != null)) {
/*  205 */         this.charset = Charset.forName(dataBoundary[1]);
/*      */       }
/*      */     } else {
/*  208 */       this.multipartDataBoundary = null;
/*      */     }
/*  210 */     this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*      */   }
/*      */   
/*      */   private void checkDestroyed() {
/*  214 */     if (this.destroyed) {
/*  215 */       throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMultipart()
/*      */   {
/*  227 */     checkDestroyed();
/*  228 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDiscardThreshold(int discardThreshold)
/*      */   {
/*  238 */     if (discardThreshold < 0) {
/*  239 */       throw new IllegalArgumentException("discardThreshold must be >= 0");
/*      */     }
/*  241 */     this.discardThreshold = discardThreshold;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDiscardThreshold()
/*      */   {
/*  249 */     return this.discardThreshold;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<InterfaceHttpData> getBodyHttpDatas()
/*      */   {
/*  264 */     checkDestroyed();
/*      */     
/*  266 */     if (!this.isLastChunk) {
/*  267 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  269 */     return this.bodyListHttpData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<InterfaceHttpData> getBodyHttpDatas(String name)
/*      */   {
/*  285 */     checkDestroyed();
/*      */     
/*  287 */     if (!this.isLastChunk) {
/*  288 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  290 */     return (List)this.bodyMapHttpData.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public InterfaceHttpData getBodyHttpData(String name)
/*      */   {
/*  307 */     checkDestroyed();
/*      */     
/*  309 */     if (!this.isLastChunk) {
/*  310 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  312 */     List<InterfaceHttpData> list = (List)this.bodyMapHttpData.get(name);
/*  313 */     if (list != null) {
/*  314 */       return (InterfaceHttpData)list.get(0);
/*      */     }
/*  316 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostMultipartRequestDecoder offer(HttpContent content)
/*      */   {
/*  330 */     checkDestroyed();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  335 */     ByteBuf buf = content.content();
/*  336 */     if (this.undecodedChunk == null) {
/*  337 */       this.undecodedChunk = buf.copy();
/*      */     } else {
/*  339 */       this.undecodedChunk.writeBytes(buf);
/*      */     }
/*  341 */     if ((content instanceof LastHttpContent)) {
/*  342 */       this.isLastChunk = true;
/*      */     }
/*  344 */     parseBody();
/*  345 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.writerIndex() > this.discardThreshold)) {
/*  346 */       this.undecodedChunk.discardReadBytes();
/*      */     }
/*  348 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasNext()
/*      */   {
/*  363 */     checkDestroyed();
/*      */     
/*  365 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)
/*      */     {
/*  367 */       if (this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
/*  368 */         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
/*      */       }
/*      */     }
/*  371 */     return (!this.bodyListHttpData.isEmpty()) && (this.bodyListHttpDataRank < this.bodyListHttpData.size());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public InterfaceHttpData next()
/*      */   {
/*  388 */     checkDestroyed();
/*      */     
/*  390 */     if (hasNext()) {
/*  391 */       return (InterfaceHttpData)this.bodyListHttpData.get(this.bodyListHttpDataRank++);
/*      */     }
/*  393 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseBody()
/*      */   {
/*  404 */     if ((this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) || (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)) {
/*  405 */       if (this.isLastChunk) {
/*  406 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/*      */       }
/*  408 */       return;
/*      */     }
/*  410 */     parseBodyMultipart();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void addHttpData(InterfaceHttpData data)
/*      */   {
/*  417 */     if (data == null) {
/*  418 */       return;
/*      */     }
/*  420 */     List<InterfaceHttpData> datas = (List)this.bodyMapHttpData.get(data.getName());
/*  421 */     if (datas == null) {
/*  422 */       datas = new ArrayList(1);
/*  423 */       this.bodyMapHttpData.put(data.getName(), datas);
/*      */     }
/*  425 */     datas.add(data);
/*  426 */     this.bodyListHttpData.add(data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseBodyMultipart()
/*      */   {
/*  437 */     if ((this.undecodedChunk == null) || (this.undecodedChunk.readableBytes() == 0))
/*      */     {
/*  439 */       return;
/*      */     }
/*  441 */     InterfaceHttpData data = decodeMultipart(this.currentStatus);
/*  442 */     while (data != null) {
/*  443 */       addHttpData(data);
/*  444 */       if ((this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE) || (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)) {
/*      */         break;
/*      */       }
/*  447 */       data = decodeMultipart(this.currentStatus);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private InterfaceHttpData decodeMultipart(HttpPostRequestDecoder.MultiPartStatus state)
/*      */   {
/*  468 */     switch (state) {
/*      */     case NOTSTARTED: 
/*  470 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
/*      */     
/*      */     case PREAMBLE: 
/*  473 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
/*      */     
/*      */     case HEADERDELIMITER: 
/*  476 */       return findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case DISPOSITION: 
/*  489 */       return findMultipartDisposition();
/*      */     
/*      */ 
/*      */     case FIELD: 
/*  493 */       Charset localCharset = null;
/*  494 */       Attribute charsetAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
/*  495 */       if (charsetAttribute != null) {
/*      */         try {
/*  497 */           localCharset = Charset.forName(charsetAttribute.getValue());
/*      */         } catch (IOException e) {
/*  499 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/*      */       }
/*  502 */       Attribute nameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.NAME);
/*  503 */       if (this.currentAttribute == null) {
/*      */         try {
/*  505 */           this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
/*      */         }
/*      */         catch (NullPointerException e) {
/*  508 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } catch (IllegalArgumentException e) {
/*  510 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } catch (IOException e) {
/*  512 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/*  514 */         if (localCharset != null) {
/*  515 */           this.currentAttribute.setCharset(localCharset);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/*  520 */         loadFieldMultipart(this.multipartDataBoundary);
/*      */       } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
/*  522 */         return null;
/*      */       }
/*  524 */       Attribute finalAttribute = this.currentAttribute;
/*  525 */       this.currentAttribute = null;
/*  526 */       this.currentFieldAttributes = null;
/*      */       
/*  528 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*  529 */       return finalAttribute;
/*      */     
/*      */ 
/*      */     case FILEUPLOAD: 
/*  533 */       return getFileUpload(this.multipartDataBoundary);
/*      */     
/*      */ 
/*      */ 
/*      */     case MIXEDDELIMITER: 
/*  538 */       return findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
/*      */     
/*      */ 
/*      */     case MIXEDDISPOSITION: 
/*  542 */       return findMultipartDisposition();
/*      */     
/*      */ 
/*      */     case MIXEDFILEUPLOAD: 
/*  546 */       return getFileUpload(this.multipartMixedBoundary);
/*      */     
/*      */     case PREEPILOGUE: 
/*  549 */       return null;
/*      */     case EPILOGUE: 
/*  551 */       return null;
/*      */     }
/*  553 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void skipControlCharacters()
/*      */   {
/*      */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  565 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*      */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/*      */       try {
/*  568 */         skipControlCharactersStandard();
/*      */       } catch (IndexOutOfBoundsException e1) {
/*  570 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e1);
/*      */       }
/*  572 */       return;
/*      */     }
/*      */     
/*  575 */     while (sao.pos < sao.limit) {
/*  576 */       char c = (char)(sao.bytes[(sao.pos++)] & 0xFF);
/*  577 */       if ((!Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/*  578 */         sao.setReadPosition(1);
/*  579 */         return;
/*      */       }
/*      */     }
/*  582 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
/*      */   }
/*      */   
/*      */   void skipControlCharactersStandard() {
/*      */     for (;;) {
/*  587 */       char c = (char)this.undecodedChunk.readUnsignedByte();
/*  588 */       if ((!Character.isISOControl(c)) && (!Character.isWhitespace(c))) {
/*  589 */         this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/*  590 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private InterfaceHttpData findMultipartDelimiter(String delimiter, HttpPostRequestDecoder.MultiPartStatus dispositionStatus, HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus)
/*      */   {
/*  610 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try {
/*  612 */       skipControlCharacters();
/*      */     } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
/*  614 */       this.undecodedChunk.readerIndex(readerIndex);
/*  615 */       return null;
/*      */     }
/*  617 */     skipOneLine();
/*      */     String newline;
/*      */     try {
/*  620 */       newline = readDelimiter(delimiter);
/*      */     } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
/*  622 */       this.undecodedChunk.readerIndex(readerIndex);
/*  623 */       return null;
/*      */     }
/*  625 */     if (newline.equals(delimiter)) {
/*  626 */       this.currentStatus = dispositionStatus;
/*  627 */       return decodeMultipart(dispositionStatus);
/*      */     }
/*  629 */     if (newline.equals(delimiter + "--"))
/*      */     {
/*  631 */       this.currentStatus = closeDelimiterStatus;
/*  632 */       if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER)
/*      */       {
/*      */ 
/*  635 */         this.currentFieldAttributes = null;
/*  636 */         return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
/*      */       }
/*  638 */       return null;
/*      */     }
/*  640 */     this.undecodedChunk.readerIndex(readerIndex);
/*  641 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private InterfaceHttpData findMultipartDisposition()
/*      */   {
/*  651 */     int readerIndex = this.undecodedChunk.readerIndex();
/*  652 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  653 */       this.currentFieldAttributes = new TreeMap(CaseIgnoringComparator.INSTANCE);
/*      */     }
/*      */     
/*  656 */     while (!skipOneLine()) {
/*      */       String newline;
/*      */       try {
/*  659 */         skipControlCharacters();
/*  660 */         newline = readLine();
/*      */       } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException ignored) {
/*  662 */         this.undecodedChunk.readerIndex(readerIndex);
/*  663 */         return null;
/*      */       }
/*  665 */       String[] contents = splitMultipartHeader(newline);
/*  666 */       if (HttpHeaderNames.CONTENT_DISPOSITION.equalsIgnoreCase(contents[0])) { boolean checkSecondArg;
/*      */         boolean checkSecondArg;
/*  668 */         if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  669 */           checkSecondArg = HttpHeaderValues.FORM_DATA.equalsIgnoreCase(contents[1]);
/*      */         } else {
/*  671 */           checkSecondArg = (HttpHeaderValues.ATTACHMENT.equalsIgnoreCase(contents[1])) || (HttpHeaderValues.FILE.equalsIgnoreCase(contents[1]));
/*      */         }
/*      */         
/*  674 */         if (checkSecondArg)
/*      */         {
/*  676 */           for (int i = 2; i < contents.length; i++) {
/*  677 */             String[] values = StringUtil.split(contents[i], '=', 2);
/*      */             Attribute attribute;
/*      */             try {
/*  680 */               String name = cleanString(values[0]);
/*  681 */               String value = values[1];
/*      */               
/*      */ 
/*  684 */               if (HttpHeaderValues.FILENAME.contentEquals(name))
/*      */               {
/*  686 */                 value = value.substring(1, value.length() - 1);
/*      */               }
/*      */               else {
/*  689 */                 value = cleanString(value);
/*      */               }
/*  691 */               attribute = this.factory.createAttribute(this.request, name, value);
/*      */             } catch (NullPointerException e) {
/*  693 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             } catch (IllegalArgumentException e) {
/*  695 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             }
/*  697 */             this.currentFieldAttributes.put(attribute.getName(), attribute);
/*      */           }
/*      */         }
/*  700 */       } else if (HttpHeaderNames.CONTENT_TRANSFER_ENCODING.equalsIgnoreCase(contents[0])) {
/*      */         Attribute attribute;
/*      */         try {
/*  703 */           attribute = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), cleanString(contents[1]));
/*      */         }
/*      */         catch (NullPointerException e) {
/*  706 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } catch (IllegalArgumentException e) {
/*  708 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/*  710 */         this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), attribute);
/*  711 */       } else if (HttpHeaderNames.CONTENT_LENGTH.equalsIgnoreCase(contents[0])) {
/*      */         Attribute attribute;
/*      */         try {
/*  714 */           attribute = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_LENGTH.toString(), cleanString(contents[1]));
/*      */         }
/*      */         catch (NullPointerException e) {
/*  717 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } catch (IllegalArgumentException e) {
/*  719 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/*  721 */         this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_LENGTH.toString(), attribute);
/*  722 */       } else if (HttpHeaderNames.CONTENT_TYPE.equalsIgnoreCase(contents[0]))
/*      */       {
/*  724 */         if (HttpHeaderValues.MULTIPART_MIXED.equalsIgnoreCase(contents[1])) {
/*  725 */           if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  726 */             String values = StringUtil.substringAfter(contents[2], '=');
/*  727 */             this.multipartMixedBoundary = ("--" + values);
/*  728 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
/*  729 */             return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
/*      */           }
/*  731 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
/*      */         }
/*      */         
/*  734 */         for (int i = 1; i < contents.length; i++) {
/*  735 */           if (contents[i].toLowerCase().startsWith(HttpHeaderValues.CHARSET.toString())) {
/*  736 */             String values = StringUtil.substringAfter(contents[i], '=');
/*      */             Attribute attribute;
/*      */             try {
/*  739 */               attribute = this.factory.createAttribute(this.request, HttpHeaderValues.CHARSET.toString(), cleanString(values));
/*      */             }
/*      */             catch (NullPointerException e) {
/*  742 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             } catch (IllegalArgumentException e) {
/*  744 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             }
/*  746 */             this.currentFieldAttributes.put(HttpHeaderValues.CHARSET.toString(), attribute);
/*      */           } else {
/*      */             Attribute attribute;
/*      */             try {
/*  750 */               attribute = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[i]);
/*      */             }
/*      */             catch (NullPointerException e) {
/*  753 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             } catch (IllegalArgumentException e) {
/*  755 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             }
/*  757 */             this.currentFieldAttributes.put(attribute.getName(), attribute);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  762 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Unknown Params: " + newline);
/*      */       }
/*      */     }
/*      */     
/*  766 */     Attribute filenameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
/*  767 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  768 */       if (filenameAttribute != null)
/*      */       {
/*  770 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
/*      */         
/*  772 */         return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
/*      */       }
/*      */       
/*  775 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/*      */       
/*  777 */       return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
/*      */     }
/*      */     
/*  780 */     if (filenameAttribute != null)
/*      */     {
/*  782 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
/*      */       
/*  784 */       return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
/*      */     }
/*      */     
/*  787 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InterfaceHttpData getFileUpload(String delimiter)
/*      */   {
/*  803 */     Attribute encoding = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString());
/*  804 */     Charset localCharset = this.charset;
/*      */     
/*  806 */     HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
/*  807 */     if (encoding != null) {
/*      */       String code;
/*      */       try {
/*  810 */         code = encoding.getValue().toLowerCase();
/*      */       } catch (IOException e) {
/*  812 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*  814 */       if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
/*  815 */         localCharset = CharsetUtil.US_ASCII;
/*  816 */       } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
/*  817 */         localCharset = CharsetUtil.ISO_8859_1;
/*  818 */         mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
/*  819 */       } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value()))
/*      */       {
/*  821 */         mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
/*      */       } else {
/*  823 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
/*      */       }
/*      */     }
/*  826 */     Attribute charsetAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.CHARSET.toString());
/*  827 */     if (charsetAttribute != null) {
/*      */       try {
/*  829 */         localCharset = Charset.forName(charsetAttribute.getValue());
/*      */       } catch (IOException e) {
/*  831 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */     }
/*  834 */     if (this.currentFileUpload == null) {
/*  835 */       Attribute filenameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
/*  836 */       Attribute nameAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderValues.NAME);
/*  837 */       Attribute contentTypeAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
/*  838 */       if (contentTypeAttribute == null) {
/*  839 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Content-Type is absent but required");
/*      */       }
/*  841 */       Attribute lengthAttribute = (Attribute)this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
/*      */       long size;
/*      */       try {
/*  844 */         size = lengthAttribute != null ? Long.parseLong(lengthAttribute.getValue()) : 0L;
/*      */       } catch (IOException e) {
/*  846 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } catch (NumberFormatException ignored) {
/*  848 */         size = 0L;
/*      */       }
/*      */       try {
/*  851 */         this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentTypeAttribute.getValue(), mechanism.value(), localCharset, size);
/*      */ 
/*      */       }
/*      */       catch (NullPointerException e)
/*      */       {
/*  856 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } catch (IllegalArgumentException e) {
/*  858 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } catch (IOException e) {
/*  860 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  865 */       readFileUploadByteMultipart(delimiter);
/*      */ 
/*      */     }
/*      */     catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e)
/*      */     {
/*  870 */       return null;
/*      */     }
/*  872 */     if (this.currentFileUpload.isCompleted())
/*      */     {
/*  874 */       if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
/*  875 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*  876 */         this.currentFieldAttributes = null;
/*      */       } else {
/*  878 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
/*  879 */         cleanMixedAttributes();
/*      */       }
/*  881 */       FileUpload fileUpload = this.currentFileUpload;
/*  882 */       this.currentFileUpload = null;
/*  883 */       return fileUpload;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  888 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  897 */     checkDestroyed();
/*  898 */     cleanFiles();
/*  899 */     this.destroyed = true;
/*      */     
/*  901 */     if ((this.undecodedChunk != null) && (this.undecodedChunk.refCnt() > 0)) {
/*  902 */       this.undecodedChunk.release();
/*  903 */       this.undecodedChunk = null;
/*      */     }
/*      */     
/*      */ 
/*  907 */     for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); i++) {
/*  908 */       ((InterfaceHttpData)this.bodyListHttpData.get(i)).release();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cleanFiles()
/*      */   {
/*  917 */     checkDestroyed();
/*      */     
/*  919 */     this.factory.cleanRequestHttpData(this.request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeHttpDataFromClean(InterfaceHttpData data)
/*      */   {
/*  927 */     checkDestroyed();
/*      */     
/*  929 */     this.factory.removeHttpDataFromClean(this.request, data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void cleanMixedAttributes()
/*      */   {
/*  937 */     this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
/*  938 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
/*  939 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
/*  940 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
/*  941 */     this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String readLineStandard()
/*      */   {
/*  953 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try {
/*  955 */       ByteBuf line = Unpooled.buffer(64);
/*      */       
/*  957 */       while (this.undecodedChunk.isReadable()) {
/*  958 */         byte nextByte = this.undecodedChunk.readByte();
/*  959 */         if (nextByte == 13)
/*      */         {
/*  961 */           nextByte = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
/*  962 */           if (nextByte == 10)
/*      */           {
/*  964 */             this.undecodedChunk.readByte();
/*  965 */             return line.toString(this.charset);
/*      */           }
/*      */           
/*  968 */           line.writeByte(13);
/*      */         } else {
/*  970 */           if (nextByte == 10) {
/*  971 */             return line.toString(this.charset);
/*      */           }
/*  973 */           line.writeByte(nextByte);
/*      */         }
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/*  977 */       this.undecodedChunk.readerIndex(readerIndex);
/*  978 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/*  980 */     this.undecodedChunk.readerIndex(readerIndex);
/*  981 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String readLine()
/*      */   {
/*      */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  995 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*      */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/*  997 */       return readLineStandard();
/*      */     }
/*  999 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try {
/* 1001 */       ByteBuf line = Unpooled.buffer(64);
/*      */       
/* 1003 */       while (sao.pos < sao.limit) {
/* 1004 */         byte nextByte = sao.bytes[(sao.pos++)];
/* 1005 */         if (nextByte == 13) {
/* 1006 */           if (sao.pos < sao.limit) {
/* 1007 */             nextByte = sao.bytes[(sao.pos++)];
/* 1008 */             if (nextByte == 10) {
/* 1009 */               sao.setReadPosition(0);
/* 1010 */               return line.toString(this.charset);
/*      */             }
/*      */             
/* 1013 */             sao.pos -= 1;
/* 1014 */             line.writeByte(13);
/*      */           }
/*      */           else {
/* 1017 */             line.writeByte(nextByte);
/*      */           }
/* 1019 */         } else { if (nextByte == 10) {
/* 1020 */             sao.setReadPosition(0);
/* 1021 */             return line.toString(this.charset);
/*      */           }
/* 1023 */           line.writeByte(nextByte);
/*      */         }
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/* 1027 */       this.undecodedChunk.readerIndex(readerIndex);
/* 1028 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/* 1030 */     this.undecodedChunk.readerIndex(readerIndex);
/* 1031 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String readDelimiterStandard(String delimiter)
/*      */   {
/* 1050 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try {
/* 1052 */       StringBuilder sb = new StringBuilder(64);
/* 1053 */       int delimiterPos = 0;
/* 1054 */       int len = delimiter.length();
/* 1055 */       while ((this.undecodedChunk.isReadable()) && (delimiterPos < len)) {
/* 1056 */         byte nextByte = this.undecodedChunk.readByte();
/* 1057 */         if (nextByte == delimiter.charAt(delimiterPos)) {
/* 1058 */           delimiterPos++;
/* 1059 */           sb.append((char)nextByte);
/*      */         }
/*      */         else {
/* 1062 */           this.undecodedChunk.readerIndex(readerIndex);
/* 1063 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         }
/*      */       }
/*      */       
/* 1067 */       if (this.undecodedChunk.isReadable()) {
/* 1068 */         byte nextByte = this.undecodedChunk.readByte();
/*      */         
/* 1070 */         if (nextByte == 13) {
/* 1071 */           nextByte = this.undecodedChunk.readByte();
/* 1072 */           if (nextByte == 10) {
/* 1073 */             return sb.toString();
/*      */           }
/*      */           
/*      */ 
/* 1077 */           this.undecodedChunk.readerIndex(readerIndex);
/* 1078 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         }
/* 1080 */         if (nextByte == 10)
/* 1081 */           return sb.toString();
/* 1082 */         if (nextByte == 45) {
/* 1083 */           sb.append('-');
/*      */           
/* 1085 */           nextByte = this.undecodedChunk.readByte();
/* 1086 */           if (nextByte == 45) {
/* 1087 */             sb.append('-');
/*      */             
/* 1089 */             if (this.undecodedChunk.isReadable()) {
/* 1090 */               nextByte = this.undecodedChunk.readByte();
/* 1091 */               if (nextByte == 13) {
/* 1092 */                 nextByte = this.undecodedChunk.readByte();
/* 1093 */                 if (nextByte == 10) {
/* 1094 */                   return sb.toString();
/*      */                 }
/*      */                 
/*      */ 
/* 1098 */                 this.undecodedChunk.readerIndex(readerIndex);
/* 1099 */                 throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */               }
/* 1101 */               if (nextByte == 10) {
/* 1102 */                 return sb.toString();
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1107 */               this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 1108 */               return sb.toString();
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1115 */             return sb.toString();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IndexOutOfBoundsException e)
/*      */     {
/* 1122 */       this.undecodedChunk.readerIndex(readerIndex);
/* 1123 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/* 1125 */     this.undecodedChunk.readerIndex(readerIndex);
/* 1126 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String readDelimiter(String delimiter)
/*      */   {
/*      */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1146 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*      */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/* 1148 */       return readDelimiterStandard(delimiter);
/*      */     }
/* 1150 */     int readerIndex = this.undecodedChunk.readerIndex();
/* 1151 */     int delimiterPos = 0;
/* 1152 */     int len = delimiter.length();
/*      */     try {
/* 1154 */       StringBuilder sb = new StringBuilder(64);
/*      */       
/* 1156 */       while ((sao.pos < sao.limit) && (delimiterPos < len)) {
/* 1157 */         byte nextByte = sao.bytes[(sao.pos++)];
/* 1158 */         if (nextByte == delimiter.charAt(delimiterPos)) {
/* 1159 */           delimiterPos++;
/* 1160 */           sb.append((char)nextByte);
/*      */         }
/*      */         else {
/* 1163 */           this.undecodedChunk.readerIndex(readerIndex);
/* 1164 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         }
/*      */       }
/*      */       
/* 1168 */       if (sao.pos < sao.limit) {
/* 1169 */         byte nextByte = sao.bytes[(sao.pos++)];
/* 1170 */         if (nextByte == 13)
/*      */         {
/* 1172 */           if (sao.pos < sao.limit) {
/* 1173 */             nextByte = sao.bytes[(sao.pos++)];
/* 1174 */             if (nextByte == 10) {
/* 1175 */               sao.setReadPosition(0);
/* 1176 */               return sb.toString();
/*      */             }
/*      */             
/*      */ 
/* 1180 */             this.undecodedChunk.readerIndex(readerIndex);
/* 1181 */             throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1186 */           this.undecodedChunk.readerIndex(readerIndex);
/* 1187 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         }
/* 1189 */         if (nextByte == 10)
/*      */         {
/*      */ 
/* 1192 */           sao.setReadPosition(0);
/* 1193 */           return sb.toString(); }
/* 1194 */         if (nextByte == 45) {
/* 1195 */           sb.append('-');
/*      */           
/* 1197 */           if (sao.pos < sao.limit) {
/* 1198 */             nextByte = sao.bytes[(sao.pos++)];
/* 1199 */             if (nextByte == 45) {
/* 1200 */               sb.append('-');
/*      */               
/* 1202 */               if (sao.pos < sao.limit) {
/* 1203 */                 nextByte = sao.bytes[(sao.pos++)];
/* 1204 */                 if (nextByte == 13) {
/* 1205 */                   if (sao.pos < sao.limit) {
/* 1206 */                     nextByte = sao.bytes[(sao.pos++)];
/* 1207 */                     if (nextByte == 10) {
/* 1208 */                       sao.setReadPosition(0);
/* 1209 */                       return sb.toString();
/*      */                     }
/*      */                     
/*      */ 
/* 1213 */                     this.undecodedChunk.readerIndex(readerIndex);
/* 1214 */                     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/* 1219 */                   this.undecodedChunk.readerIndex(readerIndex);
/* 1220 */                   throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */                 }
/* 1222 */                 if (nextByte == 10) {
/* 1223 */                   sao.setReadPosition(0);
/* 1224 */                   return sb.toString();
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1230 */                 sao.setReadPosition(1);
/* 1231 */                 return sb.toString();
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1238 */               sao.setReadPosition(0);
/* 1239 */               return sb.toString();
/*      */             }
/*      */             
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IndexOutOfBoundsException e)
/*      */     {
/* 1248 */       this.undecodedChunk.readerIndex(readerIndex);
/* 1249 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/* 1251 */     this.undecodedChunk.readerIndex(readerIndex);
/* 1252 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readFileUploadByteMultipartStandard(String delimiter)
/*      */   {
/* 1266 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     
/* 1268 */     boolean newLine = true;
/* 1269 */     int index = 0;
/* 1270 */     int lastPosition = this.undecodedChunk.readerIndex();
/* 1271 */     boolean found = false;
/* 1272 */     while (this.undecodedChunk.isReadable()) {
/* 1273 */       byte nextByte = this.undecodedChunk.readByte();
/* 1274 */       if (newLine)
/*      */       {
/* 1276 */         if (nextByte == delimiter.codePointAt(index)) {
/* 1277 */           index++;
/* 1278 */           if (delimiter.length() == index) {
/* 1279 */             found = true;
/* 1280 */             break;
/*      */           }
/*      */         }
/*      */         else {
/* 1284 */           newLine = false;
/* 1285 */           index = 0;
/*      */           
/* 1287 */           if (nextByte == 13) {
/* 1288 */             if (this.undecodedChunk.isReadable()) {
/* 1289 */               nextByte = this.undecodedChunk.readByte();
/* 1290 */               if (nextByte == 10) {
/* 1291 */                 newLine = true;
/* 1292 */                 index = 0;
/* 1293 */                 lastPosition = this.undecodedChunk.readerIndex() - 2;
/*      */               }
/*      */               else {
/* 1296 */                 lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */                 
/*      */ 
/* 1299 */                 this.undecodedChunk.readerIndex(lastPosition);
/*      */               }
/*      */             }
/* 1302 */           } else if (nextByte == 10) {
/* 1303 */             newLine = true;
/* 1304 */             index = 0;
/* 1305 */             lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */           }
/*      */           else {
/* 1308 */             lastPosition = this.undecodedChunk.readerIndex();
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 1313 */       else if (nextByte == 13) {
/* 1314 */         if (this.undecodedChunk.isReadable()) {
/* 1315 */           nextByte = this.undecodedChunk.readByte();
/* 1316 */           if (nextByte == 10) {
/* 1317 */             newLine = true;
/* 1318 */             index = 0;
/* 1319 */             lastPosition = this.undecodedChunk.readerIndex() - 2;
/*      */           }
/*      */           else {
/* 1322 */             lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */             
/*      */ 
/* 1325 */             this.undecodedChunk.readerIndex(lastPosition);
/*      */           }
/*      */         }
/* 1328 */       } else if (nextByte == 10) {
/* 1329 */         newLine = true;
/* 1330 */         index = 0;
/* 1331 */         lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */       }
/*      */       else {
/* 1334 */         lastPosition = this.undecodedChunk.readerIndex();
/*      */       }
/*      */     }
/*      */     
/* 1338 */     ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
/* 1339 */     if (found) {
/*      */       try
/*      */       {
/* 1342 */         this.currentFileUpload.addContent(buffer, true);
/*      */         
/* 1344 */         this.undecodedChunk.readerIndex(lastPosition);
/*      */       } catch (IOException e) {
/* 1346 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */       
/*      */     } else {
/*      */       try
/*      */       {
/* 1352 */         this.currentFileUpload.addContent(buffer, false);
/*      */         
/* 1354 */         this.undecodedChunk.readerIndex(lastPosition);
/* 1355 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       } catch (IOException e) {
/* 1357 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void readFileUploadByteMultipart(String delimiter)
/*      */   {
/*      */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1375 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*      */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/* 1377 */       readFileUploadByteMultipartStandard(delimiter);
/* 1378 */       return;
/*      */     }
/* 1380 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     
/* 1382 */     boolean newLine = true;
/* 1383 */     int index = 0;
/* 1384 */     int lastrealpos = sao.pos;
/*      */     
/* 1386 */     boolean found = false;
/*      */     
/* 1388 */     while (sao.pos < sao.limit) {
/* 1389 */       byte nextByte = sao.bytes[(sao.pos++)];
/* 1390 */       if (newLine)
/*      */       {
/* 1392 */         if (nextByte == delimiter.codePointAt(index)) {
/* 1393 */           index++;
/* 1394 */           if (delimiter.length() == index) {
/* 1395 */             found = true;
/* 1396 */             break;
/*      */           }
/*      */         }
/*      */         else {
/* 1400 */           newLine = false;
/* 1401 */           index = 0;
/*      */           
/* 1403 */           if (nextByte == 13) {
/* 1404 */             if (sao.pos < sao.limit) {
/* 1405 */               nextByte = sao.bytes[(sao.pos++)];
/* 1406 */               if (nextByte == 10) {
/* 1407 */                 newLine = true;
/* 1408 */                 index = 0;
/* 1409 */                 lastrealpos = sao.pos - 2;
/*      */               }
/*      */               else {
/* 1412 */                 sao.pos -= 1;
/*      */                 
/*      */ 
/* 1415 */                 lastrealpos = sao.pos;
/*      */               }
/*      */             }
/* 1418 */           } else if (nextByte == 10) {
/* 1419 */             newLine = true;
/* 1420 */             index = 0;
/* 1421 */             lastrealpos = sao.pos - 1;
/*      */           }
/*      */           else {
/* 1424 */             lastrealpos = sao.pos;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 1429 */       else if (nextByte == 13) {
/* 1430 */         if (sao.pos < sao.limit) {
/* 1431 */           nextByte = sao.bytes[(sao.pos++)];
/* 1432 */           if (nextByte == 10) {
/* 1433 */             newLine = true;
/* 1434 */             index = 0;
/* 1435 */             lastrealpos = sao.pos - 2;
/*      */           }
/*      */           else {
/* 1438 */             sao.pos -= 1;
/*      */             
/*      */ 
/* 1441 */             lastrealpos = sao.pos;
/*      */           }
/*      */         }
/* 1444 */       } else if (nextByte == 10) {
/* 1445 */         newLine = true;
/* 1446 */         index = 0;
/* 1447 */         lastrealpos = sao.pos - 1;
/*      */       }
/*      */       else {
/* 1450 */         lastrealpos = sao.pos;
/*      */       }
/*      */     }
/*      */     
/* 1454 */     int lastPosition = sao.getReadPosition(lastrealpos);
/* 1455 */     ByteBuf buffer = this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex);
/* 1456 */     if (found) {
/*      */       try
/*      */       {
/* 1459 */         this.currentFileUpload.addContent(buffer, true);
/*      */         
/* 1461 */         this.undecodedChunk.readerIndex(lastPosition);
/*      */       } catch (IOException e) {
/* 1463 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */       
/*      */     } else {
/*      */       try
/*      */       {
/* 1469 */         this.currentFileUpload.addContent(buffer, false);
/*      */         
/* 1471 */         this.undecodedChunk.readerIndex(lastPosition);
/* 1472 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       } catch (IOException e) {
/* 1474 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadFieldMultipartStandard(String delimiter)
/*      */   {
/* 1487 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try
/*      */     {
/* 1490 */       boolean newLine = true;
/* 1491 */       int index = 0;
/* 1492 */       int lastPosition = this.undecodedChunk.readerIndex();
/* 1493 */       boolean found = false;
/* 1494 */       while (this.undecodedChunk.isReadable()) {
/* 1495 */         byte nextByte = this.undecodedChunk.readByte();
/* 1496 */         if (newLine)
/*      */         {
/* 1498 */           if (nextByte == delimiter.codePointAt(index)) {
/* 1499 */             index++;
/* 1500 */             if (delimiter.length() == index) {
/* 1501 */               found = true;
/* 1502 */               break;
/*      */             }
/*      */           }
/*      */           else {
/* 1506 */             newLine = false;
/* 1507 */             index = 0;
/*      */             
/* 1509 */             if (nextByte == 13) {
/* 1510 */               if (this.undecodedChunk.isReadable()) {
/* 1511 */                 nextByte = this.undecodedChunk.readByte();
/* 1512 */                 if (nextByte == 10) {
/* 1513 */                   newLine = true;
/* 1514 */                   index = 0;
/* 1515 */                   lastPosition = this.undecodedChunk.readerIndex() - 2;
/*      */                 }
/*      */                 else {
/* 1518 */                   lastPosition = this.undecodedChunk.readerIndex() - 1;
/* 1519 */                   this.undecodedChunk.readerIndex(lastPosition);
/*      */                 }
/*      */               } else {
/* 1522 */                 lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */               }
/* 1524 */             } else if (nextByte == 10) {
/* 1525 */               newLine = true;
/* 1526 */               index = 0;
/* 1527 */               lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */             } else {
/* 1529 */               lastPosition = this.undecodedChunk.readerIndex();
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 1534 */         else if (nextByte == 13) {
/* 1535 */           if (this.undecodedChunk.isReadable()) {
/* 1536 */             nextByte = this.undecodedChunk.readByte();
/* 1537 */             if (nextByte == 10) {
/* 1538 */               newLine = true;
/* 1539 */               index = 0;
/* 1540 */               lastPosition = this.undecodedChunk.readerIndex() - 2;
/*      */             }
/*      */             else {
/* 1543 */               lastPosition = this.undecodedChunk.readerIndex() - 1;
/* 1544 */               this.undecodedChunk.readerIndex(lastPosition);
/*      */             }
/*      */           } else {
/* 1547 */             lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */           }
/* 1549 */         } else if (nextByte == 10) {
/* 1550 */           newLine = true;
/* 1551 */           index = 0;
/* 1552 */           lastPosition = this.undecodedChunk.readerIndex() - 1;
/*      */         } else {
/* 1554 */           lastPosition = this.undecodedChunk.readerIndex();
/*      */         }
/*      */       }
/*      */       
/* 1558 */       if (found)
/*      */       {
/*      */ 
/*      */         try
/*      */         {
/*      */ 
/* 1564 */           this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
/*      */         }
/*      */         catch (IOException e) {
/* 1567 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/* 1569 */         this.undecodedChunk.readerIndex(lastPosition);
/*      */       } else {
/*      */         try {
/* 1572 */           this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
/*      */         }
/*      */         catch (IOException e) {
/* 1575 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/* 1577 */         this.undecodedChunk.readerIndex(lastPosition);
/* 1578 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/* 1581 */       this.undecodedChunk.readerIndex(readerIndex);
/* 1582 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadFieldMultipart(String delimiter)
/*      */   {
/*      */     HttpPostBodyUtil.SeekAheadOptimize sao;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1596 */       sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
/*      */     } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException ignored) {
/* 1598 */       loadFieldMultipartStandard(delimiter);
/* 1599 */       return;
/*      */     }
/* 1601 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try
/*      */     {
/* 1604 */       boolean newLine = true;
/* 1605 */       int index = 0;
/*      */       
/* 1607 */       int lastrealpos = sao.pos;
/* 1608 */       boolean found = false;
/*      */       
/* 1610 */       while (sao.pos < sao.limit) {
/* 1611 */         byte nextByte = sao.bytes[(sao.pos++)];
/* 1612 */         if (newLine)
/*      */         {
/* 1614 */           if (nextByte == delimiter.codePointAt(index)) {
/* 1615 */             index++;
/* 1616 */             if (delimiter.length() == index) {
/* 1617 */               found = true;
/* 1618 */               break;
/*      */             }
/*      */           }
/*      */           else {
/* 1622 */             newLine = false;
/* 1623 */             index = 0;
/*      */             
/* 1625 */             if (nextByte == 13) {
/* 1626 */               if (sao.pos < sao.limit) {
/* 1627 */                 nextByte = sao.bytes[(sao.pos++)];
/* 1628 */                 if (nextByte == 10) {
/* 1629 */                   newLine = true;
/* 1630 */                   index = 0;
/* 1631 */                   lastrealpos = sao.pos - 2;
/*      */                 }
/*      */                 else {
/* 1634 */                   sao.pos -= 1;
/* 1635 */                   lastrealpos = sao.pos;
/*      */                 }
/*      */               }
/* 1638 */             } else if (nextByte == 10) {
/* 1639 */               newLine = true;
/* 1640 */               index = 0;
/* 1641 */               lastrealpos = sao.pos - 1;
/*      */             } else {
/* 1643 */               lastrealpos = sao.pos;
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 1648 */         else if (nextByte == 13) {
/* 1649 */           if (sao.pos < sao.limit) {
/* 1650 */             nextByte = sao.bytes[(sao.pos++)];
/* 1651 */             if (nextByte == 10) {
/* 1652 */               newLine = true;
/* 1653 */               index = 0;
/* 1654 */               lastrealpos = sao.pos - 2;
/*      */             }
/*      */             else {
/* 1657 */               sao.pos -= 1;
/* 1658 */               lastrealpos = sao.pos;
/*      */             }
/*      */           }
/* 1661 */         } else if (nextByte == 10) {
/* 1662 */           newLine = true;
/* 1663 */           index = 0;
/* 1664 */           lastrealpos = sao.pos - 1;
/*      */         } else {
/* 1666 */           lastrealpos = sao.pos;
/*      */         }
/*      */       }
/*      */       
/* 1670 */       int lastPosition = sao.getReadPosition(lastrealpos);
/* 1671 */       if (found)
/*      */       {
/*      */ 
/*      */         try
/*      */         {
/*      */ 
/* 1677 */           this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), true);
/*      */         }
/*      */         catch (IOException e) {
/* 1680 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/* 1682 */         this.undecodedChunk.readerIndex(lastPosition);
/*      */       } else {
/*      */         try {
/* 1685 */           this.currentAttribute.addContent(this.undecodedChunk.copy(readerIndex, lastPosition - readerIndex), false);
/*      */         }
/*      */         catch (IOException e) {
/* 1688 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         }
/* 1690 */         this.undecodedChunk.readerIndex(lastPosition);
/* 1691 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/* 1694 */       this.undecodedChunk.readerIndex(readerIndex);
/* 1695 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String cleanString(String field)
/*      */   {
/* 1706 */     StringBuilder sb = new StringBuilder(field.length());
/* 1707 */     for (int i = 0; i < field.length(); i++) {
/* 1708 */       char nextChar = field.charAt(i);
/* 1709 */       if (nextChar == ':') {
/* 1710 */         sb.append(32);
/* 1711 */       } else if (nextChar == ',') {
/* 1712 */         sb.append(32);
/* 1713 */       } else if (nextChar == '=') {
/* 1714 */         sb.append(32);
/* 1715 */       } else if (nextChar == ';') {
/* 1716 */         sb.append(32);
/* 1717 */       } else if (nextChar == '\t') {
/* 1718 */         sb.append(32);
/* 1719 */       } else if (nextChar != '"')
/*      */       {
/*      */ 
/* 1722 */         sb.append(nextChar);
/*      */       }
/*      */     }
/* 1725 */     return sb.toString().trim();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean skipOneLine()
/*      */   {
/* 1734 */     if (!this.undecodedChunk.isReadable()) {
/* 1735 */       return false;
/*      */     }
/* 1737 */     byte nextByte = this.undecodedChunk.readByte();
/* 1738 */     if (nextByte == 13) {
/* 1739 */       if (!this.undecodedChunk.isReadable()) {
/* 1740 */         this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 1741 */         return false;
/*      */       }
/* 1743 */       nextByte = this.undecodedChunk.readByte();
/* 1744 */       if (nextByte == 10) {
/* 1745 */         return true;
/*      */       }
/* 1747 */       this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
/* 1748 */       return false;
/*      */     }
/* 1750 */     if (nextByte == 10) {
/* 1751 */       return true;
/*      */     }
/* 1753 */     this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 1754 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String[] splitMultipartHeader(String sb)
/*      */   {
/* 1764 */     ArrayList<String> headers = new ArrayList(1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1770 */     int nameStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
/* 1771 */     for (int nameEnd = nameStart; nameEnd < sb.length(); nameEnd++) {
/* 1772 */       char ch = sb.charAt(nameEnd);
/* 1773 */       if ((ch == ':') || (Character.isWhitespace(ch))) {
/*      */         break;
/*      */       }
/*      */     }
/* 1777 */     for (int colonEnd = nameEnd; colonEnd < sb.length(); colonEnd++) {
/* 1778 */       if (sb.charAt(colonEnd) == ':') {
/* 1779 */         colonEnd++;
/* 1780 */         break;
/*      */       }
/*      */     }
/* 1783 */     int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
/* 1784 */     int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 1785 */     headers.add(sb.substring(nameStart, nameEnd));
/* 1786 */     String svalue = sb.substring(valueStart, valueEnd);
/*      */     String[] values;
/* 1788 */     String[] values; if (svalue.indexOf(';') >= 0) {
/* 1789 */       values = splitMultipartHeaderValues(svalue);
/*      */     } else {
/* 1791 */       values = StringUtil.split(svalue, ',');
/*      */     }
/* 1793 */     for (String value : values) {
/* 1794 */       headers.add(value.trim());
/*      */     }
/* 1796 */     String[] array = new String[headers.size()];
/* 1797 */     for (int i = 0; i < headers.size(); i++) {
/* 1798 */       array[i] = ((String)headers.get(i));
/*      */     }
/* 1800 */     return array;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String[] splitMultipartHeaderValues(String svalue)
/*      */   {
/* 1808 */     List<String> values = new ArrayList(1);
/* 1809 */     boolean inQuote = false;
/* 1810 */     boolean escapeNext = false;
/* 1811 */     int start = 0;
/* 1812 */     for (int i = 0; i < svalue.length(); i++) {
/* 1813 */       char c = svalue.charAt(i);
/* 1814 */       if (inQuote) {
/* 1815 */         if (escapeNext) {
/* 1816 */           escapeNext = false;
/*      */         }
/* 1818 */         else if (c == '\\') {
/* 1819 */           escapeNext = true;
/* 1820 */         } else if (c == '"') {
/* 1821 */           inQuote = false;
/*      */         }
/*      */         
/*      */       }
/* 1825 */       else if (c == '"') {
/* 1826 */         inQuote = true;
/* 1827 */       } else if (c == ';') {
/* 1828 */         values.add(svalue.substring(start, i));
/* 1829 */         start = i + 1;
/*      */       }
/*      */     }
/*      */     
/* 1833 */     values.add(svalue.substring(start));
/* 1834 */     return (String[])values.toArray(new String[values.size()]);
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\HttpPostMultipartRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */