/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.encoder.Encoder;
/*     */ import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
/*     */ import ch.qos.logback.core.spi.DeferredProcessingAware;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OutputStreamAppender<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */ {
/*     */   protected Encoder<E> encoder;
/*  48 */   protected final ReentrantLock lock = new ReentrantLock(true);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private OutputStream outputStream;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/*  61 */     return this.outputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  69 */     int errors = 0;
/*  70 */     if (this.encoder == null) {
/*  71 */       addStatus(new ErrorStatus("No encoder set for the appender named \"" + this.name + "\".", this));
/*     */       
/*  73 */       errors++;
/*     */     }
/*     */     
/*  76 */     if (this.outputStream == null) {
/*  77 */       addStatus(new ErrorStatus("No output stream set for the appender named \"" + this.name + "\".", this));
/*     */       
/*  79 */       errors++;
/*     */     }
/*     */     
/*  82 */     if (errors == 0) {
/*  83 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/*  88 */     addWarn("This appender no longer admits a layout as a sub-component, set an encoder instead.");
/*  89 */     addWarn("To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.");
/*  90 */     addWarn("See also http://logback.qos.ch/codes.html#layoutInsteadOfEncoder for details");
/*  91 */     LayoutWrappingEncoder<E> lwe = new LayoutWrappingEncoder();
/*  92 */     lwe.setLayout(layout);
/*  93 */     lwe.setContext(this.context);
/*  94 */     this.encoder = lwe;
/*     */   }
/*     */   
/*     */   protected void append(E eventObject)
/*     */   {
/*  99 */     if (!isStarted()) {
/* 100 */       return;
/*     */     }
/*     */     
/* 103 */     subAppend(eventObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 114 */     this.lock.lock();
/*     */     try {
/* 116 */       closeOutputStream();
/* 117 */       super.stop();
/*     */     } finally {
/* 119 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void closeOutputStream()
/*     */   {
/* 127 */     if (this.outputStream != null) {
/*     */       try
/*     */       {
/* 130 */         encoderClose();
/* 131 */         this.outputStream.close();
/* 132 */         this.outputStream = null;
/*     */       } catch (IOException e) {
/* 134 */         addStatus(new ErrorStatus("Could not close output stream for OutputStreamAppender.", this, e));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void encoderInit()
/*     */   {
/* 141 */     if ((this.encoder != null) && (this.outputStream != null)) {
/*     */       try {
/* 143 */         this.encoder.init(this.outputStream);
/*     */       } catch (IOException ioe) {
/* 145 */         this.started = false;
/* 146 */         addStatus(new ErrorStatus("Failed to initialize encoder for appender named [" + this.name + "].", this, ioe));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void encoderClose()
/*     */   {
/* 154 */     if ((this.encoder != null) && (this.outputStream != null)) {
/*     */       try {
/* 156 */         this.encoder.close();
/*     */       } catch (IOException ioe) {
/* 158 */         this.started = false;
/* 159 */         addStatus(new ErrorStatus("Failed to write footer for appender named [" + this.name + "].", this, ioe));
/*     */       }
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
/*     */   public void setOutputStream(OutputStream outputStream)
/*     */   {
/* 176 */     this.lock.lock();
/*     */     try
/*     */     {
/* 179 */       closeOutputStream();
/*     */       
/* 181 */       this.outputStream = outputStream;
/* 182 */       if (this.encoder == null) {
/* 183 */         addWarn("Encoder has not been set. Cannot invoke its init method.");
/*     */       }
/*     */       else
/*     */       {
/* 187 */         encoderInit(); }
/*     */     } finally {
/* 189 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeOut(E event) throws IOException {
/* 194 */     this.encoder.doEncode(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void subAppend(E event)
/*     */   {
/* 206 */     if (!isStarted()) {
/* 207 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 211 */       if ((event instanceof DeferredProcessingAware)) {
/* 212 */         ((DeferredProcessingAware)event).prepareForDeferredProcessing();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 217 */       this.lock.lock();
/*     */       try {
/* 219 */         writeOut(event);
/*     */       } finally {
/* 221 */         this.lock.unlock();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 226 */       this.started = false;
/* 227 */       addStatus(new ErrorStatus("IO failure in appender", this, ioe));
/*     */     }
/*     */   }
/*     */   
/*     */   public Encoder<E> getEncoder() {
/* 232 */     return this.encoder;
/*     */   }
/*     */   
/*     */   public void setEncoder(Encoder<E> encoder) {
/* 236 */     this.encoder = encoder;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\OutputStreamAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */