/*     */ package ch.qos.logback.core.rolling;
/*     */ 
/*     */ import ch.qos.logback.core.rolling.helper.CompressionMode;
/*     */ import ch.qos.logback.core.rolling.helper.Compressor;
/*     */ import ch.qos.logback.core.rolling.helper.FileFilterUtil;
/*     */ import ch.qos.logback.core.rolling.helper.FileNamePattern;
/*     */ import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
/*     */ import ch.qos.logback.core.rolling.helper.RenameUtil;
/*     */ import java.io.File;
/*     */ import java.util.Date;
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
/*     */ public class FixedWindowRollingPolicy
/*     */   extends RollingPolicyBase
/*     */ {
/*     */   static final String FNP_NOT_SET = "The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ";
/*     */   static final String PRUDENT_MODE_UNSUPPORTED = "See also http://logback.qos.ch/codes.html#tbr_fnp_prudent_unsupported";
/*     */   static final String SEE_PARENT_FN_NOT_SET = "Please refer to http://logback.qos.ch/codes.html#fwrp_parentFileName_not_set";
/*     */   int maxIndex;
/*     */   int minIndex;
/*  37 */   RenameUtil util = new RenameUtil();
/*     */   
/*     */ 
/*     */   Compressor compressor;
/*     */   
/*     */ 
/*     */   public static final String ZIP_ENTRY_DATE_PATTERN = "yyyy-MM-dd_HHmm";
/*     */   
/*  45 */   private static int MAX_WINDOW_SIZE = 20;
/*     */   
/*     */   public FixedWindowRollingPolicy() {
/*  48 */     this.minIndex = 1;
/*  49 */     this.maxIndex = 7;
/*     */   }
/*     */   
/*     */   public void start() {
/*  53 */     this.util.setContext(this.context);
/*     */     
/*  55 */     if (this.fileNamePatternStr != null) {
/*  56 */       this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
/*  57 */       determineCompressionMode();
/*     */     } else {
/*  59 */       addError("The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. ");
/*  60 */       addError("See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
/*  61 */       throw new IllegalStateException("The \"FileNamePattern\" property must be set before using FixedWindowRollingPolicy. See also http://logback.qos.ch/codes.html#tbr_fnp_not_set");
/*     */     }
/*     */     
/*  64 */     if (isParentPrudent()) {
/*  65 */       addError("Prudent mode is not supported with FixedWindowRollingPolicy.");
/*  66 */       addError("See also http://logback.qos.ch/codes.html#tbr_fnp_prudent_unsupported");
/*  67 */       throw new IllegalStateException("Prudent mode is not supported.");
/*     */     }
/*     */     
/*  70 */     if (getParentsRawFileProperty() == null) {
/*  71 */       addError("The File name property must be set before using this rolling policy.");
/*  72 */       addError("Please refer to http://logback.qos.ch/codes.html#fwrp_parentFileName_not_set");
/*  73 */       throw new IllegalStateException("The \"File\" option must be set.");
/*     */     }
/*     */     
/*  76 */     if (this.maxIndex < this.minIndex) {
/*  77 */       addWarn("MaxIndex (" + this.maxIndex + ") cannot be smaller than MinIndex (" + this.minIndex + ").");
/*     */       
/*  79 */       addWarn("Setting maxIndex to equal minIndex.");
/*  80 */       this.maxIndex = this.minIndex;
/*     */     }
/*     */     
/*  83 */     int maxWindowSize = getMaxWindowSize();
/*  84 */     if (this.maxIndex - this.minIndex > maxWindowSize) {
/*  85 */       addWarn("Large window sizes are not allowed.");
/*  86 */       this.maxIndex = (this.minIndex + maxWindowSize);
/*  87 */       addWarn("MaxIndex reduced to " + this.maxIndex);
/*     */     }
/*     */     
/*  90 */     IntegerTokenConverter itc = this.fileNamePattern.getIntegerTokenConverter();
/*     */     
/*  92 */     if (itc == null) {
/*  93 */       throw new IllegalStateException("FileNamePattern [" + this.fileNamePattern.getPattern() + "] does not contain a valid IntegerToken");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  98 */     if (this.compressionMode == CompressionMode.ZIP) {
/*  99 */       String zipEntryFileNamePatternStr = transformFileNamePatternFromInt2Date(this.fileNamePatternStr);
/* 100 */       this.zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, this.context);
/*     */     }
/* 102 */     this.compressor = new Compressor(this.compressionMode);
/* 103 */     this.compressor.setContext(this.context);
/* 104 */     super.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getMaxWindowSize()
/*     */   {
/* 113 */     return MAX_WINDOW_SIZE;
/*     */   }
/*     */   
/*     */   private String transformFileNamePatternFromInt2Date(String fileNamePatternStr) {
/* 117 */     String slashified = FileFilterUtil.slashify(fileNamePatternStr);
/* 118 */     String stemOfFileNamePattern = FileFilterUtil.afterLastSlash(slashified);
/* 119 */     return stemOfFileNamePattern.replace("%i", "%d{yyyy-MM-dd_HHmm}");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void rollover()
/*     */     throws RolloverFailure
/*     */   {
/* 127 */     if (this.maxIndex >= 0)
/*     */     {
/* 129 */       File file = new File(this.fileNamePattern.convertInt(this.maxIndex));
/*     */       
/* 131 */       if (file.exists()) {
/* 132 */         file.delete();
/*     */       }
/*     */       
/*     */ 
/* 136 */       for (int i = this.maxIndex - 1; i >= this.minIndex; i--) {
/* 137 */         String toRenameStr = this.fileNamePattern.convertInt(i);
/* 138 */         File toRename = new File(toRenameStr);
/*     */         
/* 140 */         if (toRename.exists()) {
/* 141 */           this.util.rename(toRenameStr, this.fileNamePattern.convertInt(i + 1));
/*     */         } else {
/* 143 */           addInfo("Skipping roll-over for inexistent file " + toRenameStr);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 148 */       switch (this.compressionMode) {
/*     */       case NONE: 
/* 150 */         this.util.rename(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex));
/*     */         
/* 152 */         break;
/*     */       case GZ: 
/* 154 */         this.compressor.compress(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex), null);
/* 155 */         break;
/*     */       case ZIP: 
/* 157 */         this.compressor.compress(getActiveFileName(), this.fileNamePattern.convertInt(this.minIndex), this.zipEntryFileNamePattern.convert(new Date()));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getActiveFileName()
/*     */   {
/* 167 */     return getParentsRawFileProperty();
/*     */   }
/*     */   
/*     */   public int getMaxIndex() {
/* 171 */     return this.maxIndex;
/*     */   }
/*     */   
/*     */   public int getMinIndex() {
/* 175 */     return this.minIndex;
/*     */   }
/*     */   
/*     */   public void setMaxIndex(int maxIndex) {
/* 179 */     this.maxIndex = maxIndex;
/*     */   }
/*     */   
/*     */   public void setMinIndex(int minIndex) {
/* 183 */     this.minIndex = minIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\FixedWindowRollingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */