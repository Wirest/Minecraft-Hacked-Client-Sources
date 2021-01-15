/*     */ package ch.qos.logback.core.encoder;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayoutWrappingEncoder<E>
/*     */   extends EncoderBase<E>
/*     */ {
/*     */   protected Layout<E> layout;
/*     */   private Charset charset;
/*  37 */   private boolean immediateFlush = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImmediateFlush(boolean immediateFlush)
/*     */   {
/*  48 */     this.immediateFlush = immediateFlush;
/*     */   }
/*     */   
/*     */   public boolean isImmediateFlush()
/*     */   {
/*  53 */     return this.immediateFlush;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout()
/*     */   {
/*  58 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/*  62 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/*  66 */     return this.charset;
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
/*     */   public void setCharset(Charset charset)
/*     */   {
/*  80 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public void init(OutputStream os) throws IOException {
/*  84 */     super.init(os);
/*  85 */     writeHeader();
/*     */   }
/*     */   
/*     */   void writeHeader() throws IOException {
/*  89 */     if ((this.layout != null) && (this.outputStream != null)) {
/*  90 */       StringBuilder sb = new StringBuilder();
/*  91 */       appendIfNotNull(sb, this.layout.getFileHeader());
/*  92 */       appendIfNotNull(sb, this.layout.getPresentationHeader());
/*  93 */       if (sb.length() > 0) {
/*  94 */         sb.append(CoreConstants.LINE_SEPARATOR);
/*     */         
/*     */ 
/*     */ 
/*  98 */         this.outputStream.write(convertToBytes(sb.toString()));
/*  99 */         this.outputStream.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 105 */     writeFooter();
/*     */   }
/*     */   
/*     */   void writeFooter() throws IOException {
/* 109 */     if ((this.layout != null) && (this.outputStream != null)) {
/* 110 */       StringBuilder sb = new StringBuilder();
/* 111 */       appendIfNotNull(sb, this.layout.getPresentationFooter());
/* 112 */       appendIfNotNull(sb, this.layout.getFileFooter());
/* 113 */       if (sb.length() > 0) {
/* 114 */         this.outputStream.write(convertToBytes(sb.toString()));
/* 115 */         this.outputStream.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private byte[] convertToBytes(String s) {
/* 121 */     if (this.charset == null) {
/* 122 */       return s.getBytes();
/*     */     }
/*     */     try {
/* 125 */       return s.getBytes(this.charset.name());
/*     */     } catch (UnsupportedEncodingException e) {
/* 127 */       throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doEncode(E event)
/*     */     throws IOException
/*     */   {
/* 134 */     String txt = this.layout.doLayout(event);
/* 135 */     this.outputStream.write(convertToBytes(txt));
/* 136 */     if (this.immediateFlush)
/* 137 */       this.outputStream.flush();
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 145 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 149 */     this.started = false;
/* 150 */     if (this.outputStream != null) {
/*     */       try {
/* 152 */         this.outputStream.flush();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendIfNotNull(StringBuilder sb, String s) {
/* 159 */     if (s != null) {
/* 160 */       sb.append(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\LayoutWrappingEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */