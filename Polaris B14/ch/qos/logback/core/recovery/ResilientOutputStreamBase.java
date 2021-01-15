/*     */ package ch.qos.logback.core.recovery;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
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
/*     */ public abstract class ResilientOutputStreamBase
/*     */   extends OutputStream
/*     */ {
/*     */   static final int STATUS_COUNT_LIMIT = 8;
/*  29 */   private int noContextWarning = 0;
/*  30 */   private int statusCount = 0;
/*     */   
/*     */   private Context context;
/*     */   
/*     */   private RecoveryCoordinator recoveryCoordinator;
/*     */   protected OutputStream os;
/*  36 */   protected boolean presumedClean = true;
/*     */   
/*     */   private boolean isPresumedInError()
/*     */   {
/*  40 */     return (this.recoveryCoordinator != null) && (!this.presumedClean);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) {
/*  44 */     if (isPresumedInError()) {
/*  45 */       if (!this.recoveryCoordinator.isTooSoon()) {
/*  46 */         attemptRecovery();
/*     */       }
/*  48 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  52 */       this.os.write(b, off, len);
/*  53 */       postSuccessfulWrite();
/*     */     } catch (IOException e) {
/*  55 */       postIOFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b)
/*     */   {
/*  61 */     if (isPresumedInError()) {
/*  62 */       if (!this.recoveryCoordinator.isTooSoon()) {
/*  63 */         attemptRecovery();
/*     */       }
/*  65 */       return;
/*     */     }
/*     */     try {
/*  68 */       this.os.write(b);
/*  69 */       postSuccessfulWrite();
/*     */     } catch (IOException e) {
/*  71 */       postIOFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush()
/*     */   {
/*  77 */     if (this.os != null) {
/*     */       try {
/*  79 */         this.os.flush();
/*  80 */         postSuccessfulWrite();
/*     */       } catch (IOException e) {
/*  82 */         postIOFailure(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   abstract String getDescription();
/*     */   
/*     */   abstract OutputStream openNewOutputStream() throws IOException;
/*     */   
/*     */   private void postSuccessfulWrite() {
/*  92 */     if (this.recoveryCoordinator != null) {
/*  93 */       this.recoveryCoordinator = null;
/*  94 */       this.statusCount = 0;
/*  95 */       addStatus(new InfoStatus("Recovered from IO failure on " + getDescription(), this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void postIOFailure(IOException e)
/*     */   {
/* 101 */     addStatusIfCountNotOverLimit(new ErrorStatus("IO failure while writing to " + getDescription(), this, e));
/*     */     
/* 103 */     this.presumedClean = false;
/* 104 */     if (this.recoveryCoordinator == null) {
/* 105 */       this.recoveryCoordinator = new RecoveryCoordinator();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 111 */     if (this.os != null) {
/* 112 */       this.os.close();
/*     */     }
/*     */   }
/*     */   
/*     */   void attemptRecovery() {
/*     */     try {
/* 118 */       close();
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 122 */     addStatusIfCountNotOverLimit(new InfoStatus("Attempting to recover from IO failure on " + getDescription(), this));
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 127 */       this.os = openNewOutputStream();
/* 128 */       this.presumedClean = true;
/*     */     } catch (IOException e) {
/* 130 */       addStatusIfCountNotOverLimit(new ErrorStatus("Failed to open " + getDescription(), this, e));
/*     */     }
/*     */   }
/*     */   
/*     */   void addStatusIfCountNotOverLimit(Status s)
/*     */   {
/* 136 */     this.statusCount += 1;
/* 137 */     if (this.statusCount < 8) {
/* 138 */       addStatus(s);
/*     */     }
/*     */     
/* 141 */     if (this.statusCount == 8) {
/* 142 */       addStatus(s);
/* 143 */       addStatus(new InfoStatus("Will supress future messages regarding " + getDescription(), this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addStatus(Status status)
/*     */   {
/* 149 */     if (this.context == null) {
/* 150 */       if (this.noContextWarning++ == 0) {
/* 151 */         System.out.println("LOGBACK: No context given for " + this);
/*     */       }
/* 153 */       return;
/*     */     }
/* 155 */     StatusManager sm = this.context.getStatusManager();
/* 156 */     if (sm != null) {
/* 157 */       sm.add(status);
/*     */     }
/*     */   }
/*     */   
/*     */   public Context getContext() {
/* 162 */     return this.context;
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/* 166 */     this.context = context;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\recovery\ResilientOutputStreamBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */