/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.rolling.RollingFileAppender;
/*     */ import ch.qos.logback.core.rolling.RolloverFailure;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.util.EnvUtil;
/*     */ import ch.qos.logback.core.util.FileUtil;
/*     */ import java.io.File;
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
/*     */ public class RenameUtil
/*     */   extends ContextAwareBase
/*     */ {
/*  39 */   static String RENAMING_ERROR_URL = "http://logback.qos.ch/codes.html#renamingError";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void rename(String src, String target)
/*     */     throws RolloverFailure
/*     */   {
/*  51 */     if (src.equals(target)) {
/*  52 */       addWarn("Source and target files are the same [" + src + "]. Skipping.");
/*  53 */       return;
/*     */     }
/*  55 */     File srcFile = new File(src);
/*     */     
/*  57 */     if (srcFile.exists()) {
/*  58 */       File targetFile = new File(target);
/*  59 */       createMissingTargetDirsIfNecessary(targetFile);
/*     */       
/*  61 */       addInfo("Renaming file [" + srcFile + "] to [" + targetFile + "]");
/*     */       
/*  63 */       boolean result = srcFile.renameTo(targetFile);
/*     */       
/*  65 */       if (!result) {
/*  66 */         addWarn("Failed to rename file [" + srcFile + "] as [" + targetFile + "].");
/*  67 */         if (areOnDifferentVolumes(srcFile, targetFile)) {
/*  68 */           addWarn("Detected different file systems for source [" + src + "] and target [" + target + "]. Attempting rename by copying.");
/*  69 */           renameByCopying(src, target);
/*  70 */           return;
/*     */         }
/*  72 */         addWarn("Please consider leaving the [file] option of " + RollingFileAppender.class.getSimpleName() + " empty.");
/*  73 */         addWarn("See also " + RENAMING_ERROR_URL);
/*     */       }
/*     */     }
/*     */     else {
/*  77 */       throw new RolloverFailure("File [" + src + "] does not exist.");
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
/*     */   boolean areOnDifferentVolumes(File srcFile, File targetFile)
/*     */     throws RolloverFailure
/*     */   {
/*  91 */     if (!EnvUtil.isJDK7OrHigher()) {
/*  92 */       return false;
/*     */     }
/*  94 */     File parentOfTarget = targetFile.getParentFile();
/*     */     try
/*     */     {
/*  97 */       boolean onSameFileStore = FileStoreUtil.areOnSameFileStore(srcFile, parentOfTarget);
/*  98 */       return !onSameFileStore;
/*     */     } catch (RolloverFailure rf) {
/* 100 */       addWarn("Error while checking file store equality", rf); }
/* 101 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renameByCopying(String src, String target)
/*     */     throws RolloverFailure
/*     */   {
/* 111 */     FileUtil fileUtil = new FileUtil(getContext());
/* 112 */     fileUtil.copy(src, target);
/*     */     
/* 114 */     File srcFile = new File(src);
/* 115 */     if (!srcFile.delete()) {
/* 116 */       addWarn("Could not delete " + src);
/*     */     }
/*     */   }
/*     */   
/*     */   void createMissingTargetDirsIfNecessary(File toFile) throws RolloverFailure
/*     */   {
/* 122 */     boolean result = FileUtil.createMissingParentDirectories(toFile);
/* 123 */     if (!result) {
/* 124 */       throw new RolloverFailure("Failed to create parent directories for [" + toFile.getAbsolutePath() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return "c.q.l.co.rolling.helper.RenameUtil";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\RenameUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */