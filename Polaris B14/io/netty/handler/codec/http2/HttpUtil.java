/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.BinaryHeaders.EntryVisitor;
/*     */ import io.netty.handler.codec.TextHeaders.EntryVisitor;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderUtil;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpUtil
/*     */ {
/*  55 */   private static final Set<CharSequence> HTTP_TO_HTTP2_HEADER_BLACKLIST = new HashSet()
/*     */   {
/*     */     private static final long serialVersionUID = -5678614530214167043L;
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   public static final HttpMethod OUT_OF_MESSAGE_SEQUENCE_METHOD = HttpMethod.OPTIONS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String OUT_OF_MESSAGE_SEQUENCE_PATH = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   public static final HttpResponseStatus OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = HttpResponseStatus.OK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private static final Pattern AUTHORITY_REPLACEMENT_PATTERN = Pattern.compile("^.*@");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ExtensionHeaderNames
/*     */   {
/* 108 */     STREAM_ID("x-http2-stream-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     AUTHORITY("x-http2-authority"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     SCHEME("x-http2-scheme"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     PATH("x-http2-path"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     STREAM_PROMISE_ID("x-http2-stream-promise-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     STREAM_DEPENDENCY_ID("x-http2-stream-dependency-id"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */     STREAM_WEIGHT("x-http2-stream-weight");
/*     */     
/*     */     private final AsciiString text;
/*     */     
/*     */     private ExtensionHeaderNames(String text) {
/* 156 */       this.text = new AsciiString(text);
/*     */     }
/*     */     
/*     */     public AsciiString text() {
/* 160 */       return this.text;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static HttpResponseStatus parseStatus(AsciiString status)
/*     */     throws Http2Exception
/*     */   {
/*     */     HttpResponseStatus result;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 174 */       result = HttpResponseStatus.parseLine(status);
/* 175 */       if (result == HttpResponseStatus.SWITCHING_PROTOCOLS) {
/* 176 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 status code '%d'", new Object[] { Integer.valueOf(result.code()) });
/*     */       }
/*     */     } catch (Http2Exception e) {
/* 179 */       throw e;
/*     */     } catch (Throwable t) {
/* 181 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "Unrecognized HTTP status code '%s' encountered in translation to HTTP/1.x", new Object[] { status });
/*     */     }
/*     */     
/* 184 */     return result;
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
/*     */   public static FullHttpResponse toHttpResponse(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 201 */     HttpResponseStatus status = parseStatus(http2Headers.status());
/*     */     
/*     */ 
/* 204 */     FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, validateHttpHeaders);
/* 205 */     addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
/* 206 */     return msg;
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
/*     */   public static FullHttpRequest toHttpRequest(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders)
/*     */     throws Http2Exception
/*     */   {
/* 225 */     AsciiString method = (AsciiString)ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
/*     */     
/* 227 */     AsciiString path = (AsciiString)ObjectUtil.checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
/*     */     
/* 229 */     FullHttpRequest msg = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), validateHttpHeaders);
/*     */     
/* 231 */     addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
/* 232 */     return msg;
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
/*     */   public static void addHttp2ToHttpHeaders(int streamId, Http2Headers sourceHeaders, FullHttpMessage destinationMessage, boolean addToTrailer)
/*     */     throws Http2Exception
/*     */   {
/* 246 */     HttpHeaders headers = addToTrailer ? destinationMessage.trailingHeaders() : destinationMessage.headers();
/* 247 */     boolean request = destinationMessage instanceof HttpRequest;
/* 248 */     Http2ToHttpHeaderTranslator visitor = new Http2ToHttpHeaderTranslator(streamId, headers, request);
/*     */     try {
/* 250 */       sourceHeaders.forEachEntry(visitor);
/*     */     } catch (Http2Exception ex) {
/* 252 */       throw ex;
/*     */     } catch (Throwable t) {
/* 254 */       throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
/*     */     }
/*     */     
/* 257 */     headers.remove(HttpHeaderNames.TRANSFER_ENCODING);
/* 258 */     headers.remove(HttpHeaderNames.TRAILER);
/* 259 */     if (!addToTrailer) {
/* 260 */       headers.setInt(ExtensionHeaderNames.STREAM_ID.text(), streamId);
/* 261 */       HttpHeaderUtil.setKeepAlive(destinationMessage, true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static Http2Headers toHttp2Headers(FullHttpMessage in)
/*     */     throws Exception
/*     */   {
/* 269 */     Http2Headers out = new DefaultHttp2Headers();
/* 270 */     HttpHeaders inHeaders = in.headers();
/* 271 */     if ((in instanceof HttpRequest)) {
/* 272 */       HttpRequest request = (HttpRequest)in;
/* 273 */       out.path(new AsciiString(request.uri()));
/* 274 */       out.method(new AsciiString(request.method().toString()));
/*     */       
/* 276 */       String value = (String)inHeaders.getAndConvert(HttpHeaderNames.HOST);
/* 277 */       if (value != null) {
/* 278 */         URI hostUri = URI.create(value);
/*     */         
/* 280 */         value = hostUri.getAuthority();
/* 281 */         if (value != null) {
/* 282 */           out.authority(new AsciiString(AUTHORITY_REPLACEMENT_PATTERN.matcher(value).replaceFirst("")));
/*     */         }
/* 284 */         value = hostUri.getScheme();
/* 285 */         if (value != null) {
/* 286 */           out.scheme(new AsciiString(value));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 291 */       CharSequence cValue = (CharSequence)inHeaders.get(ExtensionHeaderNames.AUTHORITY.text());
/* 292 */       if (cValue != null) {
/* 293 */         out.authority(AsciiString.of(cValue));
/*     */       }
/*     */       
/*     */ 
/* 297 */       cValue = (CharSequence)inHeaders.get(ExtensionHeaderNames.SCHEME.text());
/* 298 */       if (cValue != null) {
/* 299 */         out.scheme(AsciiString.of(cValue));
/*     */       }
/* 301 */     } else if ((in instanceof HttpResponse)) {
/* 302 */       HttpResponse response = (HttpResponse)in;
/* 303 */       out.status(new AsciiString(Integer.toString(response.status().code())));
/*     */     }
/*     */     
/*     */ 
/* 307 */     inHeaders.forEachEntry(new TextHeaders.EntryVisitor()
/*     */     {
/*     */       public boolean visit(Map.Entry<CharSequence, CharSequence> entry) throws Exception {
/* 310 */         AsciiString aName = AsciiString.of((CharSequence)entry.getKey()).toLowerCase();
/* 311 */         if (!HttpUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.contains(aName)) {
/* 312 */           AsciiString aValue = AsciiString.of((CharSequence)entry.getValue());
/*     */           
/*     */ 
/* 315 */           if ((!aName.equalsIgnoreCase(HttpHeaderNames.TE)) || (aValue.equalsIgnoreCase(HttpHeaderValues.TRAILERS)))
/*     */           {
/* 317 */             this.val$out.add(aName, aValue);
/*     */           }
/*     */         }
/* 320 */         return true;
/*     */       }
/* 322 */     });
/* 323 */     return out;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class Http2ToHttpHeaderTranslator
/*     */     implements BinaryHeaders.EntryVisitor
/*     */   {
/* 334 */     private static final Map<AsciiString, AsciiString> REQUEST_HEADER_TRANSLATIONS = new HashMap();
/*     */     
/* 336 */     private static final Map<AsciiString, AsciiString> RESPONSE_HEADER_TRANSLATIONS = new HashMap();
/*     */     
/* 338 */     static { RESPONSE_HEADER_TRANSLATIONS.put(Http2Headers.PseudoHeaderName.AUTHORITY.value(), HttpUtil.ExtensionHeaderNames.AUTHORITY.text());
/*     */       
/* 340 */       RESPONSE_HEADER_TRANSLATIONS.put(Http2Headers.PseudoHeaderName.SCHEME.value(), HttpUtil.ExtensionHeaderNames.SCHEME.text());
/*     */       
/* 342 */       REQUEST_HEADER_TRANSLATIONS.putAll(RESPONSE_HEADER_TRANSLATIONS);
/* 343 */       RESPONSE_HEADER_TRANSLATIONS.put(Http2Headers.PseudoHeaderName.PATH.value(), HttpUtil.ExtensionHeaderNames.PATH.text());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private final int streamId;
/*     */     
/*     */ 
/*     */     private final HttpHeaders output;
/*     */     
/*     */ 
/*     */     private final Map<AsciiString, AsciiString> translations;
/*     */     
/*     */ 
/*     */     Http2ToHttpHeaderTranslator(int streamId, HttpHeaders output, boolean request)
/*     */     {
/* 359 */       this.streamId = streamId;
/* 360 */       this.output = output;
/* 361 */       this.translations = (request ? REQUEST_HEADER_TRANSLATIONS : RESPONSE_HEADER_TRANSLATIONS);
/*     */     }
/*     */     
/*     */     public boolean visit(Map.Entry<AsciiString, AsciiString> entry) throws Http2Exception
/*     */     {
/* 366 */       AsciiString name = (AsciiString)entry.getKey();
/* 367 */       AsciiString value = (AsciiString)entry.getValue();
/* 368 */       AsciiString translatedName = (AsciiString)this.translations.get(name);
/* 369 */       if ((translatedName != null) || (!Http2Headers.PseudoHeaderName.isPseudoHeader(name))) {
/* 370 */         if (translatedName == null) {
/* 371 */           translatedName = name;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 376 */         if ((translatedName.isEmpty()) || (translatedName.charAt(0) == ':')) {
/* 377 */           throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 header '%s' encountered in translation to HTTP/1.x", new Object[] { translatedName });
/*     */         }
/*     */         
/* 380 */         this.output.add(translatedName, value);
/*     */       }
/*     */       
/* 383 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\HttpUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */