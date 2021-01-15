/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.util.FileSize;
/*    */ import ch.qos.logback.core.util.InvocationGate;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SizeBasedTriggeringPolicy<E>
/*    */   extends TriggeringPolicyBase<E>
/*    */ {
/*    */   public static final String SEE_SIZE_FORMAT = "http://logback.qos.ch/codes.html#sbtp_size_format";
/*    */   public static final long DEFAULT_MAX_FILE_SIZE = 10485760L;
/* 40 */   String maxFileSizeAsString = Long.toString(10485760L);
/*    */   FileSize maxFileSize;
/*    */   
/*    */   public SizeBasedTriggeringPolicy() {}
/*    */   
/*    */   public SizeBasedTriggeringPolicy(String maxFileSize)
/*    */   {
/* 47 */     setMaxFileSize(maxFileSize);
/*    */   }
/*    */   
/* 50 */   private InvocationGate invocationGate = new InvocationGate();
/*    */   
/*    */   public boolean isTriggeringEvent(File activeFile, E event) {
/* 53 */     if (this.invocationGate.skipFurtherWork()) {
/* 54 */       return false;
/*    */     }
/* 56 */     long now = System.currentTimeMillis();
/* 57 */     this.invocationGate.updateMaskIfNecessary(now);
/*    */     
/* 59 */     return activeFile.length() >= this.maxFileSize.getSize();
/*    */   }
/*    */   
/*    */   public String getMaxFileSize() {
/* 63 */     return this.maxFileSizeAsString;
/*    */   }
/*    */   
/*    */   public void setMaxFileSize(String maxFileSize) {
/* 67 */     this.maxFileSizeAsString = maxFileSize;
/* 68 */     this.maxFileSize = FileSize.valueOf(maxFileSize);
/*    */   }
/*    */   
/*    */   long toFileSize(String value) {
/* 72 */     if (value == null) {
/* 73 */       return 10485760L;
/*    */     }
/* 75 */     String s = value.trim().toUpperCase();
/* 76 */     long multiplier = 1L;
/*    */     
/*    */     int index;
/* 79 */     if ((index = s.indexOf("KB")) != -1) {
/* 80 */       multiplier = 1024L;
/* 81 */       s = s.substring(0, index);
/* 82 */     } else if ((index = s.indexOf("MB")) != -1) {
/* 83 */       multiplier = 1048576L;
/* 84 */       s = s.substring(0, index);
/* 85 */     } else if ((index = s.indexOf("GB")) != -1) {
/* 86 */       multiplier = 1073741824L;
/* 87 */       s = s.substring(0, index);
/*    */     }
/* 89 */     if (s != null) {
/*    */       try {
/* 91 */         return Long.valueOf(s).longValue() * multiplier;
/*    */       } catch (NumberFormatException e) {
/* 93 */         addError("[" + s + "] is not in proper int format. Please refer to " + "http://logback.qos.ch/codes.html#sbtp_size_format");
/*    */         
/* 95 */         addError("[" + value + "] not in expected format.", e);
/*    */       }
/*    */     }
/* 98 */     return 10485760L;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\SizeBasedTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */