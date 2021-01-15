/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import ch.qos.logback.core.util.FileUtil;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
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
/*     */ public class Compressor
/*     */   extends ContextAwareBase
/*     */ {
/*     */   final CompressionMode compressionMode;
/*     */   static final int BUFFER_SIZE = 8192;
/*     */   
/*     */   public Compressor(CompressionMode compressionMode)
/*     */   {
/*  44 */     this.compressionMode = compressionMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void compress(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName)
/*     */   {
/*  53 */     switch (this.compressionMode) {
/*     */     case GZ: 
/*  55 */       gzCompress(nameOfFile2Compress, nameOfCompressedFile);
/*  56 */       break;
/*     */     case ZIP: 
/*  58 */       zipCompress(nameOfFile2Compress, nameOfCompressedFile, innerEntryName);
/*  59 */       break;
/*     */     case NONE: 
/*  61 */       throw new UnsupportedOperationException("compress method called in NONE compression mode");
/*     */     }
/*     */   }
/*     */   
/*     */   private void zipCompress(String nameOfFile2zip, String nameOfZippedFile, String innerEntryName)
/*     */   {
/*  67 */     File file2zip = new File(nameOfFile2zip);
/*     */     
/*  69 */     if (!file2zip.exists()) {
/*  70 */       addStatus(new WarnStatus("The file to compress named [" + nameOfFile2zip + "] does not exist.", this));
/*     */       
/*     */ 
/*  73 */       return;
/*     */     }
/*     */     
/*  76 */     if (innerEntryName == null) {
/*  77 */       addStatus(new WarnStatus("The innerEntryName parameter cannot be null", this));
/*  78 */       return;
/*     */     }
/*     */     
/*  81 */     if (!nameOfZippedFile.endsWith(".zip")) {
/*  82 */       nameOfZippedFile = nameOfZippedFile + ".zip";
/*     */     }
/*     */     
/*  85 */     File zippedFile = new File(nameOfZippedFile);
/*     */     
/*  87 */     if (zippedFile.exists()) {
/*  88 */       addStatus(new WarnStatus("The target compressed file named [" + nameOfZippedFile + "] exist already.", this));
/*     */       
/*     */ 
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     addInfo("ZIP compressing [" + file2zip + "] as [" + zippedFile + "]");
/*  95 */     createMissingTargetDirsIfNecessary(zippedFile);
/*     */     
/*  97 */     BufferedInputStream bis = null;
/*  98 */     ZipOutputStream zos = null;
/*     */     try {
/* 100 */       bis = new BufferedInputStream(new FileInputStream(nameOfFile2zip));
/* 101 */       zos = new ZipOutputStream(new FileOutputStream(nameOfZippedFile));
/*     */       
/* 103 */       ZipEntry zipEntry = computeZipEntry(innerEntryName);
/* 104 */       zos.putNextEntry(zipEntry);
/*     */       
/* 106 */       byte[] inbuf = new byte[' '];
/*     */       
/*     */       int n;
/* 109 */       while ((n = bis.read(inbuf)) != -1) {
/* 110 */         zos.write(inbuf, 0, n);
/*     */       }
/*     */       
/* 113 */       bis.close();
/* 114 */       bis = null;
/* 115 */       zos.close();
/* 116 */       zos = null;
/*     */       
/* 118 */       if (!file2zip.delete()) {
/* 119 */         addStatus(new WarnStatus("Could not delete [" + nameOfFile2zip + "].", this));
/*     */       }
/*     */       return;
/*     */     } catch (Exception e) {
/* 123 */       addStatus(new ErrorStatus("Error occurred while compressing [" + nameOfFile2zip + "] into [" + nameOfZippedFile + "].", this, e));
/*     */     }
/*     */     finally {
/* 126 */       if (bis != null) {
/*     */         try {
/* 128 */           bis.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */       
/* 133 */       if (zos != null) {
/*     */         try {
/* 135 */           zos.close();
/*     */         }
/*     */         catch (IOException e) {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ZipEntry computeZipEntry(File zippedFile)
/*     */   {
/* 161 */     return computeZipEntry(zippedFile.getName());
/*     */   }
/*     */   
/*     */   ZipEntry computeZipEntry(String filename) {
/* 165 */     String nameOfFileNestedWithinArchive = computeFileNameStr_WCS(filename, this.compressionMode);
/* 166 */     return new ZipEntry(nameOfFileNestedWithinArchive);
/*     */   }
/*     */   
/*     */   private void gzCompress(String nameOfFile2gz, String nameOfgzedFile)
/*     */   {
/* 171 */     File file2gz = new File(nameOfFile2gz);
/*     */     
/* 173 */     if (!file2gz.exists()) {
/* 174 */       addStatus(new WarnStatus("The file to compress named [" + nameOfFile2gz + "] does not exist.", this));
/*     */       
/*     */ 
/* 177 */       return;
/*     */     }
/*     */     
/*     */ 
/* 181 */     if (!nameOfgzedFile.endsWith(".gz")) {
/* 182 */       nameOfgzedFile = nameOfgzedFile + ".gz";
/*     */     }
/*     */     
/* 185 */     File gzedFile = new File(nameOfgzedFile);
/*     */     
/* 187 */     if (gzedFile.exists()) {
/* 188 */       addWarn("The target compressed file named [" + nameOfgzedFile + "] exist already. Aborting file compression.");
/*     */       
/* 190 */       return;
/*     */     }
/*     */     
/* 193 */     addInfo("GZ compressing [" + file2gz + "] as [" + gzedFile + "]");
/* 194 */     createMissingTargetDirsIfNecessary(gzedFile);
/*     */     
/* 196 */     BufferedInputStream bis = null;
/* 197 */     GZIPOutputStream gzos = null;
/*     */     try {
/* 199 */       bis = new BufferedInputStream(new FileInputStream(nameOfFile2gz));
/* 200 */       gzos = new GZIPOutputStream(new FileOutputStream(nameOfgzedFile));
/* 201 */       byte[] inbuf = new byte[' '];
/*     */       
/*     */       int n;
/* 204 */       while ((n = bis.read(inbuf)) != -1) {
/* 205 */         gzos.write(inbuf, 0, n);
/*     */       }
/*     */       
/* 208 */       bis.close();
/* 209 */       bis = null;
/* 210 */       gzos.close();
/* 211 */       gzos = null;
/*     */       
/* 213 */       if (!file2gz.delete()) {
/* 214 */         addStatus(new WarnStatus("Could not delete [" + nameOfFile2gz + "].", this));
/*     */       }
/*     */       return;
/*     */     } catch (Exception e) {
/* 218 */       addStatus(new ErrorStatus("Error occurred while compressing [" + nameOfFile2gz + "] into [" + nameOfgzedFile + "].", this, e));
/*     */     }
/*     */     finally {
/* 221 */       if (bis != null) {
/*     */         try {
/* 223 */           bis.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */       
/* 228 */       if (gzos != null) {
/*     */         try {
/* 230 */           gzos.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static String computeFileNameStr_WCS(String fileNamePatternStr, CompressionMode compressionMode)
/*     */   {
/* 240 */     int len = fileNamePatternStr.length();
/* 241 */     switch (compressionMode) {
/*     */     case GZ: 
/* 243 */       if (fileNamePatternStr.endsWith(".gz")) {
/* 244 */         return fileNamePatternStr.substring(0, len - 3);
/*     */       }
/* 246 */       return fileNamePatternStr;
/*     */     case ZIP: 
/* 248 */       if (fileNamePatternStr.endsWith(".zip")) {
/* 249 */         return fileNamePatternStr.substring(0, len - 4);
/*     */       }
/* 251 */       return fileNamePatternStr;
/*     */     case NONE: 
/* 253 */       return fileNamePatternStr;
/*     */     }
/* 255 */     throw new IllegalStateException("Execution should not reach this point");
/*     */   }
/*     */   
/*     */   void createMissingTargetDirsIfNecessary(File file)
/*     */   {
/* 260 */     boolean result = FileUtil.createMissingParentDirectories(file);
/* 261 */     if (!result) {
/* 262 */       addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 269 */     return getClass().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\Compressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */