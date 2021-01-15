/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HttpMessageUtil
/*     */ {
/*     */   static StringBuilder appendRequest(StringBuilder buf, HttpRequest req)
/*     */   {
/*  29 */     appendCommon(buf, req);
/*  30 */     appendInitialLine(buf, req);
/*  31 */     appendHeaders(buf, req.headers());
/*  32 */     removeLastNewLine(buf);
/*  33 */     return buf;
/*     */   }
/*     */   
/*     */   static StringBuilder appendResponse(StringBuilder buf, HttpResponse res) {
/*  37 */     appendCommon(buf, res);
/*  38 */     appendInitialLine(buf, res);
/*  39 */     appendHeaders(buf, res.headers());
/*  40 */     removeLastNewLine(buf);
/*  41 */     return buf;
/*     */   }
/*     */   
/*     */   private static void appendCommon(StringBuilder buf, HttpMessage msg) {
/*  45 */     buf.append(StringUtil.simpleClassName(msg));
/*  46 */     buf.append("(decodeResult: ");
/*  47 */     buf.append(msg.decoderResult());
/*  48 */     buf.append(", version: ");
/*  49 */     buf.append(msg.protocolVersion());
/*  50 */     buf.append(')');
/*  51 */     buf.append(StringUtil.NEWLINE);
/*     */   }
/*     */   
/*     */   static StringBuilder appendFullRequest(StringBuilder buf, FullHttpRequest req) {
/*  55 */     appendFullCommon(buf, req);
/*  56 */     appendInitialLine(buf, req);
/*  57 */     appendHeaders(buf, req.headers());
/*  58 */     appendHeaders(buf, req.trailingHeaders());
/*  59 */     removeLastNewLine(buf);
/*  60 */     return buf;
/*     */   }
/*     */   
/*     */   static StringBuilder appendFullResponse(StringBuilder buf, FullHttpResponse res) {
/*  64 */     appendFullCommon(buf, res);
/*  65 */     appendInitialLine(buf, res);
/*  66 */     appendHeaders(buf, res.headers());
/*  67 */     appendHeaders(buf, res.trailingHeaders());
/*  68 */     removeLastNewLine(buf);
/*  69 */     return buf;
/*     */   }
/*     */   
/*     */   private static void appendFullCommon(StringBuilder buf, FullHttpMessage msg) {
/*  73 */     buf.append(StringUtil.simpleClassName(msg));
/*  74 */     buf.append("(decodeResult: ");
/*  75 */     buf.append(msg.decoderResult());
/*  76 */     buf.append(", version: ");
/*  77 */     buf.append(msg.protocolVersion());
/*  78 */     buf.append(", content: ");
/*  79 */     buf.append(msg.content());
/*  80 */     buf.append(')');
/*  81 */     buf.append(StringUtil.NEWLINE);
/*     */   }
/*     */   
/*     */   private static void appendInitialLine(StringBuilder buf, HttpRequest req) {
/*  85 */     buf.append(req.method());
/*  86 */     buf.append(' ');
/*  87 */     buf.append(req.uri());
/*  88 */     buf.append(' ');
/*  89 */     buf.append(req.protocolVersion());
/*  90 */     buf.append(StringUtil.NEWLINE);
/*     */   }
/*     */   
/*     */   private static void appendInitialLine(StringBuilder buf, HttpResponse res) {
/*  94 */     buf.append(res.protocolVersion());
/*  95 */     buf.append(' ');
/*  96 */     buf.append(res.status());
/*  97 */     buf.append(StringUtil.NEWLINE);
/*     */   }
/*     */   
/*     */   private static void appendHeaders(StringBuilder buf, HttpHeaders headers) {
/* 101 */     for (Map.Entry<CharSequence, CharSequence> e : headers) {
/* 102 */       buf.append((CharSequence)e.getKey());
/* 103 */       buf.append(": ");
/* 104 */       buf.append((CharSequence)e.getValue());
/* 105 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void removeLastNewLine(StringBuilder buf) {
/* 110 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpMessageUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */