/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.ConsoleTarget;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import ch.qos.logback.core.util.EnvUtil;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConsoleAppender<E>
/*     */   extends OutputStreamAppender<E>
/*     */ {
/*  44 */   protected ConsoleTarget target = ConsoleTarget.SystemOut;
/*  45 */   protected boolean withJansi = false;
/*     */   
/*     */ 
/*     */   private static final String WindowsAnsiOutputStream_CLASS_NAME = "org.fusesource.jansi.WindowsAnsiOutputStream";
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTarget(String value)
/*     */   {
/*  54 */     ConsoleTarget t = ConsoleTarget.findByName(value.trim());
/*  55 */     if (t == null) {
/*  56 */       targetWarn(value);
/*     */     } else {
/*  58 */       this.target = t;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTarget()
/*     */   {
/*  69 */     return this.target.getName();
/*     */   }
/*     */   
/*     */   private void targetWarn(String val) {
/*  73 */     Status status = new WarnStatus("[" + val + "] should be one of " + Arrays.toString(ConsoleTarget.values()), this);
/*     */     
/*  75 */     status.add(new WarnStatus("Using previously set target, System.out by default.", this));
/*     */     
/*  77 */     addStatus(status);
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  82 */     OutputStream targetStream = this.target.getStream();
/*     */     
/*  84 */     if ((EnvUtil.isWindows()) && (this.withJansi)) {
/*  85 */       targetStream = getTargetStreamForWindows(targetStream);
/*     */     }
/*  87 */     setOutputStream(targetStream);
/*  88 */     super.start();
/*     */   }
/*     */   
/*     */   private OutputStream getTargetStreamForWindows(OutputStream targetStream) {
/*     */     try {
/*  93 */       addInfo("Enabling JANSI WindowsAnsiOutputStream for the console.");
/*  94 */       Object windowsAnsiOutputStream = OptionHelper.instantiateByClassNameAndParameter("org.fusesource.jansi.WindowsAnsiOutputStream", Object.class, this.context, OutputStream.class, targetStream);
/*     */       
/*  96 */       return (OutputStream)windowsAnsiOutputStream;
/*     */     } catch (Exception e) {
/*  98 */       addWarn("Failed to create WindowsAnsiOutputStream. Falling back on the default stream.", e);
/*     */     }
/* 100 */     return targetStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isWithJansi()
/*     */   {
/* 107 */     return this.withJansi;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWithJansi(boolean withJansi)
/*     */   {
/* 117 */     this.withJansi = withJansi;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\ConsoleAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */