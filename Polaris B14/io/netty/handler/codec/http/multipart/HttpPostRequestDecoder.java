/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class HttpPostRequestDecoder
/*     */   implements InterfaceHttpPostRequestDecoder
/*     */ {
/*     */   static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
/*     */   private final InterfaceHttpPostRequestDecoder decoder;
/*     */   
/*     */   public HttpPostRequestDecoder(HttpRequest request)
/*     */   {
/*  52 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request)
/*     */   {
/*  68 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset)
/*     */   {
/*  86 */     if (factory == null) {
/*  87 */       throw new NullPointerException("factory");
/*     */     }
/*  89 */     if (request == null) {
/*  90 */       throw new NullPointerException("request");
/*     */     }
/*  92 */     if (charset == null) {
/*  93 */       throw new NullPointerException("charset");
/*     */     }
/*     */     
/*  96 */     if (isMultipart(request)) {
/*  97 */       this.decoder = new HttpPostMultipartRequestDecoder(factory, request, charset);
/*     */     } else {
/*  99 */       this.decoder = new HttpPostStandardRequestDecoder(factory, request, charset);
/*     */     }
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
/*     */   protected static enum MultiPartStatus
/*     */   {
/* 134 */     NOTSTARTED,  PREAMBLE,  HEADERDELIMITER,  DISPOSITION,  FIELD,  FILEUPLOAD,  MIXEDPREAMBLE,  MIXEDDELIMITER, 
/* 135 */     MIXEDDISPOSITION,  MIXEDFILEUPLOAD,  MIXEDCLOSEDELIMITER,  CLOSEDELIMITER,  PREEPILOGUE,  EPILOGUE;
/*     */     
/*     */ 
/*     */     private MultiPartStatus() {}
/*     */   }
/*     */   
/*     */   public static boolean isMultipart(HttpRequest request)
/*     */   {
/* 143 */     if (request.headers().contains(HttpHeaderNames.CONTENT_TYPE)) {
/* 144 */       return getMultipartDataBoundary((String)request.headers().getAndConvert(HttpHeaderNames.CONTENT_TYPE)) != null;
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String[] getMultipartDataBoundary(String contentType)
/*     */   {
/* 157 */     String[] headerContentType = splitHeaderContentType(contentType);
/* 158 */     if (headerContentType[0].toLowerCase().startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString()))
/*     */     {
/*     */       int crank;
/*     */       
/* 162 */       if (headerContentType[1].toLowerCase().startsWith(HttpHeaderValues.BOUNDARY.toString()))
/*     */       {
/* 164 */         int mrank = 1;
/* 165 */         crank = 2; } else { int crank;
/* 166 */         if (headerContentType[2].toLowerCase().startsWith(HttpHeaderValues.BOUNDARY.toString()))
/*     */         {
/* 168 */           int mrank = 2;
/* 169 */           crank = 1;
/*     */         } else {
/* 171 */           return null; } }
/*     */       int crank;
/* 173 */       int mrank; String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
/* 174 */       if (boundary == null) {
/* 175 */         throw new ErrorDataDecoderException("Needs a boundary value");
/*     */       }
/* 177 */       if (boundary.charAt(0) == '"') {
/* 178 */         String bound = boundary.trim();
/* 179 */         int index = bound.length() - 1;
/* 180 */         if (bound.charAt(index) == '"') {
/* 181 */           boundary = bound.substring(1, index);
/*     */         }
/*     */       }
/* 184 */       if (headerContentType[crank].toLowerCase().startsWith(HttpHeaderValues.CHARSET.toString()))
/*     */       {
/* 186 */         String charset = StringUtil.substringAfter(headerContentType[crank], '=');
/* 187 */         if (charset != null) {
/* 188 */           return new String[] { "--" + boundary, charset };
/*     */         }
/*     */       }
/* 191 */       return new String[] { "--" + boundary };
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isMultipart()
/*     */   {
/* 198 */     return this.decoder.isMultipart();
/*     */   }
/*     */   
/*     */   public void setDiscardThreshold(int discardThreshold)
/*     */   {
/* 203 */     this.decoder.setDiscardThreshold(discardThreshold);
/*     */   }
/*     */   
/*     */   public int getDiscardThreshold()
/*     */   {
/* 208 */     return this.decoder.getDiscardThreshold();
/*     */   }
/*     */   
/*     */   public List<InterfaceHttpData> getBodyHttpDatas()
/*     */   {
/* 213 */     return this.decoder.getBodyHttpDatas();
/*     */   }
/*     */   
/*     */   public List<InterfaceHttpData> getBodyHttpDatas(String name)
/*     */   {
/* 218 */     return this.decoder.getBodyHttpDatas(name);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData getBodyHttpData(String name)
/*     */   {
/* 223 */     return this.decoder.getBodyHttpData(name);
/*     */   }
/*     */   
/*     */   public InterfaceHttpPostRequestDecoder offer(HttpContent content)
/*     */   {
/* 228 */     return this.decoder.offer(content);
/*     */   }
/*     */   
/*     */   public boolean hasNext()
/*     */   {
/* 233 */     return this.decoder.hasNext();
/*     */   }
/*     */   
/*     */   public InterfaceHttpData next()
/*     */   {
/* 238 */     return this.decoder.next();
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */   {
/* 243 */     this.decoder.destroy();
/*     */   }
/*     */   
/*     */   public void cleanFiles()
/*     */   {
/* 248 */     this.decoder.cleanFiles();
/*     */   }
/*     */   
/*     */   public void removeHttpDataFromClean(InterfaceHttpData data)
/*     */   {
/* 253 */     this.decoder.removeHttpDataFromClean(data);
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
/*     */   private static String[] splitHeaderContentType(String sb)
/*     */   {
/* 268 */     int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
/* 269 */     int aEnd = sb.indexOf(';');
/* 270 */     if (aEnd == -1) {
/* 271 */       return new String[] { sb, "", "" };
/*     */     }
/* 273 */     int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
/* 274 */     if (sb.charAt(aEnd - 1) == ' ') {
/* 275 */       aEnd--;
/*     */     }
/* 277 */     int bEnd = sb.indexOf(';', bStart);
/* 278 */     if (bEnd == -1) {
/* 279 */       bEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 280 */       return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), "" };
/*     */     }
/* 282 */     int cStart = HttpPostBodyUtil.findNonWhitespace(sb, bEnd + 1);
/* 283 */     if (sb.charAt(bEnd - 1) == ' ') {
/* 284 */       bEnd--;
/*     */     }
/* 286 */     int cEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 287 */     return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), sb.substring(cStart, cEnd) };
/*     */   }
/*     */   
/*     */ 
/*     */   public static class NotEnoughDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = -7846841864603865638L;
/*     */     
/*     */ 
/*     */     public NotEnoughDataDecoderException() {}
/*     */     
/*     */     public NotEnoughDataDecoderException(String msg)
/*     */     {
/* 301 */       super();
/*     */     }
/*     */     
/*     */     public NotEnoughDataDecoderException(Throwable cause) {
/* 305 */       super();
/*     */     }
/*     */     
/*     */     public NotEnoughDataDecoderException(String msg, Throwable cause) {
/* 309 */       super(cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class EndOfDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = 1336267941020800769L;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ErrorDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = 5020247425493164465L;
/*     */     
/*     */     public ErrorDataDecoderException() {}
/*     */     
/*     */     public ErrorDataDecoderException(String msg)
/*     */     {
/* 330 */       super();
/*     */     }
/*     */     
/*     */     public ErrorDataDecoderException(Throwable cause) {
/* 334 */       super();
/*     */     }
/*     */     
/*     */     public ErrorDataDecoderException(String msg, Throwable cause) {
/* 338 */       super(cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */