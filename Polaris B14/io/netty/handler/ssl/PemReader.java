/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.KeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ final class PemReader
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PemReader.class);
/*     */   
/*  47 */   private static final Pattern CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private static final Pattern KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);
/*     */   
/*     */ 
/*     */   static ByteBuf[] readCertificates(File file)
/*     */     throws CertificateException
/*     */   {
/*     */     String content;
/*     */     try
/*     */     {
/*  61 */       content = readContent(file);
/*     */     } catch (IOException e) {
/*  63 */       throw new CertificateException("failed to read a file: " + file, e);
/*     */     }
/*     */     
/*  66 */     List<ByteBuf> certs = new ArrayList();
/*  67 */     Matcher m = CERT_PATTERN.matcher(content);
/*  68 */     int start = 0;
/*     */     
/*  70 */     while (m.find(start))
/*     */     {
/*     */ 
/*     */ 
/*  74 */       ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
/*  75 */       ByteBuf der = Base64.decode(base64);
/*  76 */       base64.release();
/*  77 */       certs.add(der);
/*     */       
/*  79 */       start = m.end();
/*     */     }
/*     */     
/*  82 */     if (certs.isEmpty()) {
/*  83 */       throw new CertificateException("found no certificates: " + file);
/*     */     }
/*     */     
/*  86 */     return (ByteBuf[])certs.toArray(new ByteBuf[certs.size()]);
/*     */   }
/*     */   
/*     */   static ByteBuf readPrivateKey(File file) throws KeyException {
/*     */     String content;
/*     */     try {
/*  92 */       content = readContent(file);
/*     */     } catch (IOException e) {
/*  94 */       throw new KeyException("failed to read a file: " + file, e);
/*     */     }
/*     */     
/*  97 */     Matcher m = KEY_PATTERN.matcher(content);
/*  98 */     if (!m.find()) {
/*  99 */       throw new KeyException("found no private key: " + file);
/*     */     }
/*     */     
/* 102 */     ByteBuf base64 = Unpooled.copiedBuffer(m.group(1), CharsetUtil.US_ASCII);
/* 103 */     ByteBuf der = Base64.decode(base64);
/* 104 */     base64.release();
/* 105 */     return der;
/*     */   }
/*     */   
/*     */   private static String readContent(File file) throws IOException {
/* 109 */     InputStream in = new FileInputStream(file);
/* 110 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 112 */       byte[] buf = new byte[' '];
/*     */       int ret;
/* 114 */       for (;;) { ret = in.read(buf);
/* 115 */         if (ret < 0) {
/*     */           break;
/*     */         }
/* 118 */         out.write(buf, 0, ret);
/*     */       }
/* 120 */       return out.toString(CharsetUtil.US_ASCII.name());
/*     */     } finally {
/* 122 */       safeClose(in);
/* 123 */       safeClose(out);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(InputStream in) {
/*     */     try {
/* 129 */       in.close();
/*     */     } catch (IOException e) {
/* 131 */       logger.warn("Failed to close a stream.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(OutputStream out) {
/*     */     try {
/* 137 */       out.close();
/*     */     } catch (IOException e) {
/* 139 */       logger.warn("Failed to close a stream.", e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\PemReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */