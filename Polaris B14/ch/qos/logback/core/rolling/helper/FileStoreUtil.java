/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
/*    */ import ch.qos.logback.core.rolling.RolloverFailure;
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class FileStoreUtil
/*    */ {
/*    */   static final String PATH_CLASS_STR = "java.nio.file.Path";
/*    */   static final String FILES_CLASS_STR = "java.nio.file.Files";
/*    */   
/*    */   public static boolean areOnSameFileStore(File a, File b)
/*    */     throws RolloverFailure
/*    */   {
/* 41 */     if (!a.exists()) {
/* 42 */       throw new IllegalArgumentException("File [" + a + "] does not exist.");
/*    */     }
/* 44 */     if (!b.exists()) {
/* 45 */       throw new IllegalArgumentException("File [" + b + "] does not exist.");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 58 */       Class<?> pathClass = Class.forName("java.nio.file.Path");
/* 59 */       Class<?> filesClass = Class.forName("java.nio.file.Files");
/*    */       
/* 61 */       Method toPath = File.class.getMethod("toPath", new Class[0]);
/* 62 */       Method getFileStoreMethod = filesClass.getMethod("getFileStore", new Class[] { pathClass });
/*    */       
/*    */ 
/* 65 */       Object pathA = toPath.invoke(a, new Object[0]);
/* 66 */       Object pathB = toPath.invoke(b, new Object[0]);
/*    */       
/* 68 */       Object fileStoreA = getFileStoreMethod.invoke(null, new Object[] { pathA });
/* 69 */       Object fileStoreB = getFileStoreMethod.invoke(null, new Object[] { pathB });
/* 70 */       return fileStoreA.equals(fileStoreB);
/*    */     } catch (Exception e) {
/* 72 */       throw new RolloverFailure("Failed to check file store equality for [" + a + "] and [" + b + "]", e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\FileStoreUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */