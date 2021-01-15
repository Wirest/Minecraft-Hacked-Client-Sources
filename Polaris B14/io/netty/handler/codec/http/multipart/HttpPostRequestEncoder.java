/*      */ package io.netty.handler.codec.http.multipart;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.handler.codec.AsciiString;
/*      */ import io.netty.handler.codec.DecoderResult;
/*      */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*      */ import io.netty.handler.codec.http.DefaultHttpContent;
/*      */ import io.netty.handler.codec.http.EmptyHttpHeaders;
/*      */ import io.netty.handler.codec.http.FullHttpRequest;
/*      */ import io.netty.handler.codec.http.HttpConstants;
/*      */ import io.netty.handler.codec.http.HttpContent;
/*      */ import io.netty.handler.codec.http.HttpHeaderNames;
/*      */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*      */ import io.netty.handler.codec.http.HttpHeaderValues;
/*      */ import io.netty.handler.codec.http.HttpHeaders;
/*      */ import io.netty.handler.codec.http.HttpMethod;
/*      */ import io.netty.handler.codec.http.HttpRequest;
/*      */ import io.netty.handler.codec.http.HttpVersion;
/*      */ import io.netty.handler.codec.http.LastHttpContent;
/*      */ import io.netty.handler.stream.ChunkedInput;
/*      */ import io.netty.util.internal.ThreadLocalRandom;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URLEncoder;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
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
/*      */ public class HttpPostRequestEncoder
/*      */   implements ChunkedInput<HttpContent>
/*      */ {
/*      */   public static enum EncoderMode
/*      */   {
/*   65 */     RFC1738, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   70 */     RFC3986, 
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
/*   81 */     HTML5;
/*      */     
/*      */     private EncoderMode() {} }
/*   84 */   private static final Map<Pattern, String> percentEncodings = new HashMap();
/*      */   private final HttpDataFactory factory;
/*      */   
/*   87 */   static { percentEncodings.put(Pattern.compile("\\*"), "%2A");
/*   88 */     percentEncodings.put(Pattern.compile("\\+"), "%20");
/*   89 */     percentEncodings.put(Pattern.compile("%7E"), "~");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final HttpRequest request;
/*      */   
/*      */ 
/*      */   private final Charset charset;
/*      */   
/*      */ 
/*      */   private boolean isChunked;
/*      */   
/*      */ 
/*      */   private final List<InterfaceHttpData> bodyListDatas;
/*      */   
/*      */ 
/*      */   final List<InterfaceHttpData> multipartHttpDatas;
/*      */   
/*      */ 
/*      */   private final boolean isMultipart;
/*      */   
/*      */ 
/*      */   String multipartDataBoundary;
/*      */   
/*      */ 
/*      */   String multipartMixedBoundary;
/*      */   
/*      */ 
/*      */   private boolean headerFinalized;
/*      */   
/*      */ 
/*      */   private final EncoderMode encoderMode;
/*      */   
/*      */ 
/*      */   private boolean isLastChunk;
/*      */   
/*      */ 
/*      */   private boolean isLastChunkSent;
/*      */   
/*      */ 
/*      */   private FileUpload currentFileUpload;
/*      */   
/*      */ 
/*      */   private boolean duringMixedMode;
/*      */   
/*      */ 
/*      */   private long globalBodySize;
/*      */   
/*      */ 
/*      */   private long globalProgress;
/*      */   
/*      */ 
/*      */   private ListIterator<InterfaceHttpData> iterator;
/*      */   
/*      */ 
/*      */   private ByteBuf currentBuffer;
/*      */   
/*      */ 
/*      */   private InterfaceHttpData currentData;
/*      */   
/*      */ 
/*      */   public HttpPostRequestEncoder(HttpRequest request, boolean multipart)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  154 */     this(new DefaultHttpDataFactory(16384L), request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
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
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  173 */     this(factory, request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart, Charset charset, EncoderMode encoderMode)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  197 */     if (factory == null) {
/*  198 */       throw new NullPointerException("factory");
/*      */     }
/*  200 */     if (request == null) {
/*  201 */       throw new NullPointerException("request");
/*      */     }
/*  203 */     if (charset == null) {
/*  204 */       throw new NullPointerException("charset");
/*      */     }
/*  206 */     HttpMethod method = request.method();
/*  207 */     if ((!method.equals(HttpMethod.POST)) && (!method.equals(HttpMethod.PUT)) && (!method.equals(HttpMethod.PATCH)) && (!method.equals(HttpMethod.OPTIONS)))
/*      */     {
/*  209 */       throw new ErrorDataEncoderException("Cannot create a Encoder if not a POST");
/*      */     }
/*  211 */     this.request = request;
/*  212 */     this.charset = charset;
/*  213 */     this.factory = factory;
/*      */     
/*  215 */     this.bodyListDatas = new ArrayList();
/*      */     
/*  217 */     this.isLastChunk = false;
/*  218 */     this.isLastChunkSent = false;
/*  219 */     this.isMultipart = multipart;
/*  220 */     this.multipartHttpDatas = new ArrayList();
/*  221 */     this.encoderMode = encoderMode;
/*  222 */     if (this.isMultipart) {
/*  223 */       initDataMultipart();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void cleanFiles()
/*      */   {
/*  231 */     this.factory.cleanRequestHttpData(this.request);
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
/*      */   public boolean isMultipart()
/*      */   {
/*  265 */     return this.isMultipart;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initDataMultipart()
/*      */   {
/*  272 */     this.multipartDataBoundary = getNewMultipartDelimiter();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initMixedMultipart()
/*      */   {
/*  279 */     this.multipartMixedBoundary = getNewMultipartDelimiter();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String getNewMultipartDelimiter()
/*      */   {
/*  288 */     return Long.toHexString(ThreadLocalRandom.current().nextLong()).toLowerCase();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<InterfaceHttpData> getBodyListAttributes()
/*      */   {
/*  297 */     return this.bodyListDatas;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBodyHttpDatas(List<InterfaceHttpData> datas)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  309 */     if (datas == null) {
/*  310 */       throw new NullPointerException("datas");
/*      */     }
/*  312 */     this.globalBodySize = 0L;
/*  313 */     this.bodyListDatas.clear();
/*  314 */     this.currentFileUpload = null;
/*  315 */     this.duringMixedMode = false;
/*  316 */     this.multipartHttpDatas.clear();
/*  317 */     for (InterfaceHttpData data : datas) {
/*  318 */       addBodyHttpData(data);
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
/*      */   public void addBodyAttribute(String name, String value)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  335 */     if (name == null) {
/*  336 */       throw new NullPointerException("name");
/*      */     }
/*  338 */     String svalue = value;
/*  339 */     if (value == null) {
/*  340 */       svalue = "";
/*      */     }
/*  342 */     Attribute data = this.factory.createAttribute(this.request, name, svalue);
/*  343 */     addBodyHttpData(data);
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
/*      */   public void addBodyFileUpload(String name, File file, String contentType, boolean isText)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  364 */     if (name == null) {
/*  365 */       throw new NullPointerException("name");
/*      */     }
/*  367 */     if (file == null) {
/*  368 */       throw new NullPointerException("file");
/*      */     }
/*  370 */     String scontentType = contentType;
/*  371 */     String contentTransferEncoding = null;
/*  372 */     if (contentType == null) {
/*  373 */       if (isText) {
/*  374 */         scontentType = HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE;
/*      */       } else {
/*  376 */         scontentType = HttpPostBodyUtil.DEFAULT_BINARY_CONTENT_TYPE;
/*      */       }
/*      */     }
/*  379 */     if (!isText) {
/*  380 */       contentTransferEncoding = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();
/*      */     }
/*  382 */     FileUpload fileUpload = this.factory.createFileUpload(this.request, name, file.getName(), scontentType, contentTransferEncoding, null, file.length());
/*      */     try
/*      */     {
/*  385 */       fileUpload.setContent(file);
/*      */     } catch (IOException e) {
/*  387 */       throw new ErrorDataEncoderException(e);
/*      */     }
/*  389 */     addBodyHttpData(fileUpload);
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
/*      */   public void addBodyFileUploads(String name, File[] file, String[] contentType, boolean[] isText)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  410 */     if ((file.length != contentType.length) && (file.length != isText.length)) {
/*  411 */       throw new NullPointerException("Different array length");
/*      */     }
/*  413 */     for (int i = 0; i < file.length; i++) {
/*  414 */       addBodyFileUpload(name, file[i], contentType[i], isText[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBodyHttpData(InterfaceHttpData data)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  427 */     if (this.headerFinalized) {
/*  428 */       throw new ErrorDataEncoderException("Cannot add value once finalized");
/*      */     }
/*  430 */     if (data == null) {
/*  431 */       throw new NullPointerException("data");
/*      */     }
/*  433 */     this.bodyListDatas.add(data);
/*  434 */     if (!this.isMultipart) {
/*  435 */       if ((data instanceof Attribute)) {
/*  436 */         Attribute attribute = (Attribute)data;
/*      */         try
/*      */         {
/*  439 */           String key = encodeAttribute(attribute.getName(), this.charset);
/*  440 */           String value = encodeAttribute(attribute.getValue(), this.charset);
/*  441 */           Attribute newattribute = this.factory.createAttribute(this.request, key, value);
/*  442 */           this.multipartHttpDatas.add(newattribute);
/*  443 */           this.globalBodySize += newattribute.getName().length() + 1 + newattribute.length() + 1L;
/*      */         } catch (IOException e) {
/*  445 */           throw new ErrorDataEncoderException(e);
/*      */         }
/*  447 */       } else if ((data instanceof FileUpload))
/*      */       {
/*  449 */         FileUpload fileUpload = (FileUpload)data;
/*      */         
/*  451 */         String key = encodeAttribute(fileUpload.getName(), this.charset);
/*  452 */         String value = encodeAttribute(fileUpload.getFilename(), this.charset);
/*  453 */         Attribute newattribute = this.factory.createAttribute(this.request, key, value);
/*  454 */         this.multipartHttpDatas.add(newattribute);
/*  455 */         this.globalBodySize += newattribute.getName().length() + 1 + newattribute.length() + 1L;
/*      */       }
/*  457 */       return;
/*      */     }
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
/*  491 */     if ((data instanceof Attribute)) {
/*  492 */       if (this.duringMixedMode) {
/*  493 */         InternalAttribute internal = new InternalAttribute(this.charset);
/*  494 */         internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
/*  495 */         this.multipartHttpDatas.add(internal);
/*  496 */         this.multipartMixedBoundary = null;
/*  497 */         this.currentFileUpload = null;
/*  498 */         this.duringMixedMode = false;
/*      */       }
/*  500 */       InternalAttribute internal = new InternalAttribute(this.charset);
/*  501 */       if (!this.multipartHttpDatas.isEmpty())
/*      */       {
/*  503 */         internal.addValue("\r\n");
/*      */       }
/*  505 */       internal.addValue("--" + this.multipartDataBoundary + "\r\n");
/*      */       
/*  507 */       Attribute attribute = (Attribute)data;
/*  508 */       internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + attribute.getName() + "\"\r\n");
/*      */       
/*  510 */       Charset localcharset = attribute.getCharset();
/*  511 */       if (localcharset != null)
/*      */       {
/*  513 */         internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE + "; " + HttpHeaderValues.CHARSET + '=' + localcharset + "\r\n");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  519 */       internal.addValue("\r\n");
/*  520 */       this.multipartHttpDatas.add(internal);
/*  521 */       this.multipartHttpDatas.add(data);
/*  522 */       this.globalBodySize += attribute.length() + internal.size();
/*  523 */     } else if ((data instanceof FileUpload)) {
/*  524 */       FileUpload fileUpload = (FileUpload)data;
/*  525 */       InternalAttribute internal = new InternalAttribute(this.charset);
/*  526 */       if (!this.multipartHttpDatas.isEmpty())
/*      */       {
/*  528 */         internal.addValue("\r\n");
/*      */       }
/*      */       boolean localMixed;
/*  531 */       if (this.duringMixedMode) { boolean localMixed;
/*  532 */         if ((this.currentFileUpload != null) && (this.currentFileUpload.getName().equals(fileUpload.getName())))
/*      */         {
/*      */ 
/*  535 */           localMixed = true;
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  542 */           internal.addValue("--" + this.multipartMixedBoundary + "--");
/*  543 */           this.multipartHttpDatas.add(internal);
/*  544 */           this.multipartMixedBoundary = null;
/*      */           
/*      */ 
/*  547 */           internal = new InternalAttribute(this.charset);
/*  548 */           internal.addValue("\r\n");
/*  549 */           boolean localMixed = false;
/*      */           
/*  551 */           this.currentFileUpload = fileUpload;
/*  552 */           this.duringMixedMode = false;
/*      */         }
/*      */       }
/*  555 */       else if ((this.encoderMode != EncoderMode.HTML5) && (this.currentFileUpload != null) && (this.currentFileUpload.getName().equals(fileUpload.getName())))
/*      */       {
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
/*  577 */         initMixedMultipart();
/*  578 */         InternalAttribute pastAttribute = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2);
/*      */         
/*      */ 
/*  581 */         this.globalBodySize -= pastAttribute.size();
/*  582 */         StringBuilder replacement = new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + fileUpload.getFilename().length() + fileUpload.getName().length()).append("--").append(this.multipartDataBoundary).append("\r\n").append(HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append(HttpHeaderValues.FORM_DATA).append("; ").append(HttpHeaderValues.NAME).append("=\"").append(fileUpload.getName()).append("\"\r\n").append(HttpHeaderNames.CONTENT_TYPE).append(": ").append(HttpHeaderValues.MULTIPART_MIXED).append("; ").append(HttpHeaderValues.BOUNDARY).append('=').append(this.multipartMixedBoundary).append("\r\n\r\n").append("--").append(this.multipartMixedBoundary).append("\r\n").append(HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append(HttpHeaderValues.ATTACHMENT).append("; ").append(HttpHeaderValues.FILENAME).append("=\"").append(fileUpload.getFilename()).append("\"\r\n");
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
/*  621 */         pastAttribute.setValue(replacement.toString(), 1);
/*  622 */         pastAttribute.setValue("", 2);
/*      */         
/*      */ 
/*  625 */         this.globalBodySize += pastAttribute.size();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  631 */         boolean localMixed = true;
/*  632 */         this.duringMixedMode = true;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  637 */         localMixed = false;
/*  638 */         this.currentFileUpload = fileUpload;
/*  639 */         this.duringMixedMode = false;
/*      */       }
/*      */       
/*      */ 
/*  643 */       if (localMixed)
/*      */       {
/*      */ 
/*  646 */         internal.addValue("--" + this.multipartMixedBoundary + "\r\n");
/*      */         
/*  648 */         internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.ATTACHMENT + "; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload.getFilename() + "\"\r\n");
/*      */       }
/*      */       else {
/*  651 */         internal.addValue("--" + this.multipartDataBoundary + "\r\n");
/*      */         
/*      */ 
/*  654 */         internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + fileUpload.getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload.getFilename() + "\"\r\n");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  661 */       internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + fileUpload.getContentType());
/*  662 */       String contentTransferEncoding = fileUpload.getContentTransferEncoding();
/*  663 */       if ((contentTransferEncoding != null) && (contentTransferEncoding.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())))
/*      */       {
/*  665 */         internal.addValue("\r\n" + HttpHeaderNames.CONTENT_TRANSFER_ENCODING + ": " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + "\r\n\r\n");
/*      */       }
/*  667 */       else if (fileUpload.getCharset() != null) {
/*  668 */         internal.addValue("; " + HttpHeaderValues.CHARSET + '=' + fileUpload.getCharset() + "\r\n\r\n");
/*      */       } else {
/*  670 */         internal.addValue("\r\n\r\n");
/*      */       }
/*  672 */       this.multipartHttpDatas.add(internal);
/*  673 */       this.multipartHttpDatas.add(data);
/*  674 */       this.globalBodySize += fileUpload.length() + internal.size();
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
/*      */   public HttpRequest finalizeRequest()
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  695 */     if (!this.headerFinalized) {
/*  696 */       if (this.isMultipart) {
/*  697 */         InternalAttribute internal = new InternalAttribute(this.charset);
/*  698 */         if (this.duringMixedMode) {
/*  699 */           internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
/*      */         }
/*  701 */         internal.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
/*  702 */         this.multipartHttpDatas.add(internal);
/*  703 */         this.multipartMixedBoundary = null;
/*  704 */         this.currentFileUpload = null;
/*  705 */         this.duringMixedMode = false;
/*  706 */         this.globalBodySize += internal.size();
/*      */       }
/*  708 */       this.headerFinalized = true;
/*      */     } else {
/*  710 */       throw new ErrorDataEncoderException("Header already encoded");
/*      */     }
/*      */     
/*  713 */     HttpHeaders headers = this.request.headers();
/*  714 */     List<String> contentTypes = headers.getAllAndConvert(HttpHeaderNames.CONTENT_TYPE);
/*  715 */     List<CharSequence> transferEncoding = headers.getAll(HttpHeaderNames.TRANSFER_ENCODING);
/*  716 */     if (contentTypes != null) {
/*  717 */       headers.remove(HttpHeaderNames.CONTENT_TYPE);
/*  718 */       for (String contentType : contentTypes)
/*      */       {
/*  720 */         String lowercased = contentType.toLowerCase();
/*  721 */         if ((!lowercased.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())) && (!lowercased.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())))
/*      */         {
/*      */ 
/*      */ 
/*  725 */           headers.add(HttpHeaderNames.CONTENT_TYPE, contentType);
/*      */         }
/*      */       }
/*      */     }
/*  729 */     if (this.isMultipart) {
/*  730 */       String value = HttpHeaderValues.MULTIPART_FORM_DATA + "; " + HttpHeaderValues.BOUNDARY + '=' + this.multipartDataBoundary;
/*      */       
/*  732 */       headers.add(HttpHeaderNames.CONTENT_TYPE, value);
/*      */     }
/*      */     else {
/*  735 */       headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
/*      */     }
/*      */     
/*  738 */     long realSize = this.globalBodySize;
/*  739 */     if (this.isMultipart) {
/*  740 */       this.iterator = this.multipartHttpDatas.listIterator();
/*      */     } else {
/*  742 */       realSize -= 1L;
/*  743 */       this.iterator = this.multipartHttpDatas.listIterator();
/*      */     }
/*  745 */     headers.set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(realSize));
/*  746 */     if ((realSize > 8096L) || (this.isMultipart)) {
/*  747 */       this.isChunked = true;
/*  748 */       if (transferEncoding != null) {
/*  749 */         headers.remove(HttpHeaderNames.TRANSFER_ENCODING);
/*  750 */         for (CharSequence v : transferEncoding) {
/*  751 */           if (!HttpHeaderValues.CHUNKED.equalsIgnoreCase(v))
/*      */           {
/*      */ 
/*  754 */             headers.add(HttpHeaderNames.TRANSFER_ENCODING, v);
/*      */           }
/*      */         }
/*      */       }
/*  758 */       HttpHeaderUtil.setTransferEncodingChunked(this.request, true);
/*      */       
/*      */ 
/*  761 */       return new WrappedHttpRequest(this.request);
/*      */     }
/*      */     
/*  764 */     HttpContent chunk = nextChunk();
/*  765 */     if ((this.request instanceof FullHttpRequest)) {
/*  766 */       FullHttpRequest fullRequest = (FullHttpRequest)this.request;
/*  767 */       ByteBuf chunkContent = chunk.content();
/*  768 */       if (fullRequest.content() != chunkContent) {
/*  769 */         fullRequest.content().clear().writeBytes(chunkContent);
/*  770 */         chunkContent.release();
/*      */       }
/*  772 */       return fullRequest;
/*      */     }
/*  774 */     return new WrappedFullHttpRequest(this.request, chunk, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isChunked()
/*      */   {
/*  783 */     return this.isChunked;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String encodeAttribute(String s, Charset charset)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  794 */     if (s == null) {
/*  795 */       return "";
/*      */     }
/*      */     try {
/*  798 */       String encoded = URLEncoder.encode(s, charset.name());
/*  799 */       if (this.encoderMode == EncoderMode.RFC3986) {
/*  800 */         for (Map.Entry<Pattern, String> entry : percentEncodings.entrySet()) {
/*  801 */           String replacement = (String)entry.getValue();
/*  802 */           encoded = ((Pattern)entry.getKey()).matcher(encoded).replaceAll(replacement);
/*      */         }
/*      */       }
/*  805 */       return encoded;
/*      */     } catch (UnsupportedEncodingException e) {
/*  807 */       throw new ErrorDataEncoderException(charset.name(), e);
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
/*  822 */   private boolean isKey = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ByteBuf fillByteBuf()
/*      */   {
/*  829 */     int length = this.currentBuffer.readableBytes();
/*  830 */     if (length > 8096) {
/*  831 */       ByteBuf slice = this.currentBuffer.slice(this.currentBuffer.readerIndex(), 8096);
/*  832 */       this.currentBuffer.retain();
/*  833 */       this.currentBuffer.skipBytes(8096);
/*  834 */       return slice;
/*      */     }
/*      */     
/*  837 */     ByteBuf slice = this.currentBuffer;
/*  838 */     this.currentBuffer = null;
/*  839 */     return slice;
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
/*      */   private HttpContent encodeNextChunkMultipart(int sizeleft)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  854 */     if (this.currentData == null) {
/*  855 */       return null;
/*      */     }
/*      */     
/*  858 */     if ((this.currentData instanceof InternalAttribute)) {
/*  859 */       ByteBuf buffer = ((InternalAttribute)this.currentData).toByteBuf();
/*  860 */       this.currentData = null;
/*      */     } else {
/*  862 */       if ((this.currentData instanceof Attribute)) {
/*      */         try {
/*  864 */           buffer = ((Attribute)this.currentData).getChunk(sizeleft);
/*      */         } catch (IOException e) {
/*  866 */           throw new ErrorDataEncoderException(e);
/*      */         }
/*      */       } else {
/*      */         try {
/*  870 */           buffer = ((HttpData)this.currentData).getChunk(sizeleft);
/*      */         } catch (IOException e) {
/*  872 */           throw new ErrorDataEncoderException(e);
/*      */         }
/*      */       }
/*  875 */       if (buffer.capacity() == 0)
/*      */       {
/*  877 */         this.currentData = null;
/*  878 */         return null;
/*      */       }
/*      */     }
/*  881 */     if (this.currentBuffer == null) {
/*  882 */       this.currentBuffer = buffer;
/*      */     } else {
/*  884 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     }
/*  886 */     if (this.currentBuffer.readableBytes() < 8096) {
/*  887 */       this.currentData = null;
/*  888 */       return null;
/*      */     }
/*  890 */     ByteBuf buffer = fillByteBuf();
/*  891 */     return new DefaultHttpContent(buffer);
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
/*      */   private HttpContent encodeNextChunkUrlEncoded(int sizeleft)
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/*  905 */     if (this.currentData == null) {
/*  906 */       return null;
/*      */     }
/*  908 */     int size = sizeleft;
/*      */     
/*      */ 
/*      */ 
/*  912 */     if (this.isKey) {
/*  913 */       String key = this.currentData.getName();
/*  914 */       ByteBuf buffer = Unpooled.wrappedBuffer(key.getBytes());
/*  915 */       this.isKey = false;
/*  916 */       if (this.currentBuffer == null) {
/*  917 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */         
/*  919 */         size -= buffer.readableBytes() + 1;
/*      */       } else {
/*  921 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */         
/*  923 */         size -= buffer.readableBytes() + 1;
/*      */       }
/*  925 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  926 */         buffer = fillByteBuf();
/*  927 */         return new DefaultHttpContent(buffer);
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  933 */       buffer = ((HttpData)this.currentData).getChunk(size);
/*      */     } catch (IOException e) {
/*  935 */       throw new ErrorDataEncoderException(e);
/*      */     }
/*      */     
/*      */ 
/*  939 */     ByteBuf delimiter = null;
/*  940 */     if (buffer.readableBytes() < size) {
/*  941 */       this.isKey = true;
/*  942 */       delimiter = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
/*      */     }
/*      */     
/*      */ 
/*  946 */     if (buffer.capacity() == 0) {
/*  947 */       this.currentData = null;
/*  948 */       if (this.currentBuffer == null) {
/*  949 */         this.currentBuffer = delimiter;
/*      */       }
/*  951 */       else if (delimiter != null) {
/*  952 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, delimiter });
/*      */       }
/*      */       
/*  955 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  956 */         buffer = fillByteBuf();
/*  957 */         return new DefaultHttpContent(buffer);
/*      */       }
/*  959 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  963 */     if (this.currentBuffer == null) {
/*  964 */       if (delimiter != null) {
/*  965 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, delimiter });
/*      */       } else {
/*  967 */         this.currentBuffer = buffer;
/*      */       }
/*      */     }
/*  970 */     else if (delimiter != null) {
/*  971 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, delimiter });
/*      */     } else {
/*  973 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  978 */     if (this.currentBuffer.readableBytes() < 8096) {
/*  979 */       this.currentData = null;
/*  980 */       this.isKey = true;
/*  981 */       return null;
/*      */     }
/*      */     
/*  984 */     ByteBuf buffer = fillByteBuf();
/*  985 */     return new DefaultHttpContent(buffer);
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
/*      */   public HttpContent readChunk(ChannelHandlerContext ctx)
/*      */     throws Exception
/*      */   {
/* 1004 */     if (this.isLastChunkSent) {
/* 1005 */       return null;
/*      */     }
/* 1007 */     HttpContent nextChunk = nextChunk();
/* 1008 */     this.globalProgress += nextChunk.content().readableBytes();
/* 1009 */     return nextChunk;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private HttpContent nextChunk()
/*      */     throws HttpPostRequestEncoder.ErrorDataEncoderException
/*      */   {
/* 1022 */     if (this.isLastChunk) {
/* 1023 */       this.isLastChunkSent = true;
/* 1024 */       return LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     }
/*      */     
/* 1027 */     int size = 8096;
/*      */     
/* 1029 */     if (this.currentBuffer != null) {
/* 1030 */       size -= this.currentBuffer.readableBytes();
/*      */     }
/* 1032 */     if (size <= 0)
/*      */     {
/* 1034 */       ByteBuf buffer = fillByteBuf();
/* 1035 */       return new DefaultHttpContent(buffer);
/*      */     }
/*      */     
/* 1038 */     if (this.currentData != null)
/*      */     {
/* 1040 */       if (this.isMultipart) {
/* 1041 */         HttpContent chunk = encodeNextChunkMultipart(size);
/* 1042 */         if (chunk != null) {
/* 1043 */           return chunk;
/*      */         }
/*      */       } else {
/* 1046 */         HttpContent chunk = encodeNextChunkUrlEncoded(size);
/* 1047 */         if (chunk != null)
/*      */         {
/* 1049 */           return chunk;
/*      */         }
/*      */       }
/* 1052 */       size = 8096 - this.currentBuffer.readableBytes();
/*      */     }
/* 1054 */     if (!this.iterator.hasNext()) {
/* 1055 */       this.isLastChunk = true;
/*      */       
/* 1057 */       ByteBuf buffer = this.currentBuffer;
/* 1058 */       this.currentBuffer = null;
/* 1059 */       return new DefaultHttpContent(buffer);
/*      */     }
/* 1061 */     while ((size > 0) && (this.iterator.hasNext())) {
/* 1062 */       this.currentData = ((InterfaceHttpData)this.iterator.next());
/*      */       HttpContent chunk;
/* 1064 */       HttpContent chunk; if (this.isMultipart) {
/* 1065 */         chunk = encodeNextChunkMultipart(size);
/*      */       } else {
/* 1067 */         chunk = encodeNextChunkUrlEncoded(size);
/*      */       }
/* 1069 */       if (chunk == null)
/*      */       {
/* 1071 */         size = 8096 - this.currentBuffer.readableBytes();
/*      */       }
/*      */       else
/*      */       {
/* 1075 */         return chunk;
/*      */       }
/*      */     }
/* 1078 */     this.isLastChunk = true;
/* 1079 */     if (this.currentBuffer == null) {
/* 1080 */       this.isLastChunkSent = true;
/*      */       
/* 1082 */       return LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     }
/*      */     
/* 1085 */     ByteBuf buffer = this.currentBuffer;
/* 1086 */     this.currentBuffer = null;
/* 1087 */     return new DefaultHttpContent(buffer);
/*      */   }
/*      */   
/*      */   public boolean isEndOfInput() throws Exception
/*      */   {
/* 1092 */     return this.isLastChunkSent;
/*      */   }
/*      */   
/*      */   public long length()
/*      */   {
/* 1097 */     return this.isMultipart ? this.globalBodySize : this.globalBodySize - 1L;
/*      */   }
/*      */   
/*      */   public long progress()
/*      */   {
/* 1102 */     return this.globalProgress;
/*      */   }
/*      */   
/*      */   public void close() throws Exception
/*      */   {}
/*      */   
/*      */   public static class ErrorDataEncoderException extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 5020247425493164465L;
/*      */     
/*      */     public ErrorDataEncoderException() {}
/*      */     
/*      */     public ErrorDataEncoderException(String msg) {
/* 1115 */       super();
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(Throwable cause) {
/* 1119 */       super();
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(String msg, Throwable cause) {
/* 1123 */       super(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WrappedHttpRequest implements HttpRequest {
/*      */     private final HttpRequest request;
/*      */     
/* 1130 */     WrappedHttpRequest(HttpRequest request) { this.request = request; }
/*      */     
/*      */ 
/*      */     public HttpRequest setProtocolVersion(HttpVersion version)
/*      */     {
/* 1135 */       this.request.setProtocolVersion(version);
/* 1136 */       return this;
/*      */     }
/*      */     
/*      */     public HttpRequest setMethod(HttpMethod method)
/*      */     {
/* 1141 */       this.request.setMethod(method);
/* 1142 */       return this;
/*      */     }
/*      */     
/*      */     public HttpRequest setUri(String uri)
/*      */     {
/* 1147 */       this.request.setUri(uri);
/* 1148 */       return this;
/*      */     }
/*      */     
/*      */     public HttpMethod method()
/*      */     {
/* 1153 */       return this.request.method();
/*      */     }
/*      */     
/*      */     public String uri()
/*      */     {
/* 1158 */       return this.request.uri();
/*      */     }
/*      */     
/*      */     public HttpVersion protocolVersion()
/*      */     {
/* 1163 */       return this.request.protocolVersion();
/*      */     }
/*      */     
/*      */     public HttpHeaders headers()
/*      */     {
/* 1168 */       return this.request.headers();
/*      */     }
/*      */     
/*      */     public DecoderResult decoderResult()
/*      */     {
/* 1173 */       return this.request.decoderResult();
/*      */     }
/*      */     
/*      */     public void setDecoderResult(DecoderResult result)
/*      */     {
/* 1178 */       this.request.setDecoderResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class WrappedFullHttpRequest extends HttpPostRequestEncoder.WrappedHttpRequest implements FullHttpRequest {
/*      */     private final HttpContent content;
/*      */     
/*      */     private WrappedFullHttpRequest(HttpRequest request, HttpContent content) {
/* 1186 */       super();
/* 1187 */       this.content = content;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setProtocolVersion(HttpVersion version)
/*      */     {
/* 1192 */       super.setProtocolVersion(version);
/* 1193 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setMethod(HttpMethod method)
/*      */     {
/* 1198 */       super.setMethod(method);
/* 1199 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest setUri(String uri)
/*      */     {
/* 1204 */       super.setUri(uri);
/* 1205 */       return this;
/*      */     }
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
/*      */     private FullHttpRequest copy(boolean copyContent, ByteBuf newContent)
/*      */     {
/* 1224 */       DefaultFullHttpRequest copy = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), newContent == null ? Unpooled.buffer(0) : copyContent ? content().copy() : newContent);
/*      */       
/*      */ 
/*      */ 
/* 1228 */       copy.headers().set(headers());
/* 1229 */       copy.trailingHeaders().set(trailingHeaders());
/* 1230 */       return copy;
/*      */     }
/*      */     
/*      */     public FullHttpRequest copy(ByteBuf newContent)
/*      */     {
/* 1235 */       return copy(false, newContent);
/*      */     }
/*      */     
/*      */     public FullHttpRequest copy()
/*      */     {
/* 1240 */       return copy(true, null);
/*      */     }
/*      */     
/*      */     public FullHttpRequest duplicate()
/*      */     {
/* 1245 */       DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content().duplicate());
/*      */       
/* 1247 */       duplicate.headers().set(headers());
/* 1248 */       duplicate.trailingHeaders().set(trailingHeaders());
/* 1249 */       return duplicate;
/*      */     }
/*      */     
/*      */     public FullHttpRequest retain(int increment)
/*      */     {
/* 1254 */       this.content.retain(increment);
/* 1255 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest retain()
/*      */     {
/* 1260 */       this.content.retain();
/* 1261 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest touch()
/*      */     {
/* 1266 */       this.content.touch();
/* 1267 */       return this;
/*      */     }
/*      */     
/*      */     public FullHttpRequest touch(Object hint)
/*      */     {
/* 1272 */       this.content.touch(hint);
/* 1273 */       return this;
/*      */     }
/*      */     
/*      */     public ByteBuf content()
/*      */     {
/* 1278 */       return this.content.content();
/*      */     }
/*      */     
/*      */     public HttpHeaders trailingHeaders()
/*      */     {
/* 1283 */       if ((this.content instanceof LastHttpContent)) {
/* 1284 */         return ((LastHttpContent)this.content).trailingHeaders();
/*      */       }
/* 1286 */       return EmptyHttpHeaders.INSTANCE;
/*      */     }
/*      */     
/*      */ 
/*      */     public int refCnt()
/*      */     {
/* 1292 */       return this.content.refCnt();
/*      */     }
/*      */     
/*      */     public boolean release()
/*      */     {
/* 1297 */       return this.content.release();
/*      */     }
/*      */     
/*      */     public boolean release(int decrement)
/*      */     {
/* 1302 */       return this.content.release(decrement);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */