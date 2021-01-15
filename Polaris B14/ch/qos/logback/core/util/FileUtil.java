/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.rolling.RolloverFailure;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */   extends ContextAwareBase
/*     */ {
/*     */   static final int BUF_SIZE = 32768;
/*     */   
/*     */   public FileUtil(Context context)
/*     */   {
/*  28 */     setContext(context);
/*     */   }
/*     */   
/*     */   public static URL fileToURL(File file) {
/*     */     try {
/*  33 */       return file.toURI().toURL();
/*     */     } catch (MalformedURLException e) {
/*  35 */       throw new RuntimeException("Unexpected exception on file [" + file + "]", e);
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
/*     */   public static boolean createMissingParentDirectories(File file)
/*     */   {
/*  49 */     File parent = file.getParentFile();
/*  50 */     if (parent == null)
/*     */     {
/*     */ 
/*  53 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  58 */     parent.mkdirs();
/*  59 */     return parent.exists();
/*     */   }
/*     */   
/*     */   public String resourceAsString(ClassLoader classLoader, String resourceName)
/*     */   {
/*  64 */     URL url = classLoader.getResource(resourceName);
/*  65 */     if (url == null) {
/*  66 */       addError("Failed to find resource [" + resourceName + "]");
/*  67 */       return null;
/*     */     }
/*     */     
/*  70 */     InputStreamReader isr = null;
/*     */     try {
/*  72 */       URLConnection urlConnection = url.openConnection();
/*  73 */       urlConnection.setUseCaches(false);
/*  74 */       isr = new InputStreamReader(urlConnection.getInputStream());
/*  75 */       char[] buf = new char[''];
/*  76 */       StringBuilder builder = new StringBuilder();
/*  77 */       int count = -1;
/*  78 */       while ((count = isr.read(buf, 0, buf.length)) != -1) {
/*  79 */         builder.append(buf, 0, count);
/*     */       }
/*  81 */       return builder.toString();
/*     */     } catch (IOException e) {
/*  83 */       addError("Failed to open " + resourceName, e);
/*     */     } finally {
/*  85 */       if (isr != null) {
/*     */         try {
/*  87 */           isr.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */     }
/*     */     
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public void copy(String src, String destination)
/*     */     throws RolloverFailure
/*     */   {
/*  99 */     BufferedInputStream bis = null;
/* 100 */     BufferedOutputStream bos = null;
/*     */     try {
/* 102 */       bis = new BufferedInputStream(new FileInputStream(src));
/* 103 */       bos = new BufferedOutputStream(new FileOutputStream(destination));
/* 104 */       byte[] inbuf = new byte[32768];
/*     */       
/*     */       int n;
/* 107 */       while ((n = bis.read(inbuf)) != -1) {
/* 108 */         bos.write(inbuf, 0, n);
/*     */       }
/*     */       
/* 111 */       bis.close();
/* 112 */       bis = null;
/* 113 */       bos.close();
/* 114 */       bos = null;
/*     */       String msg;
/*     */       return; } catch (IOException ioe) { msg = "Failed to copy [" + src + "] to [" + destination + "]";
/* 117 */       addError(msg, ioe);
/* 118 */       throw new RolloverFailure(msg);
/*     */     } finally {
/* 120 */       if (bis != null) {
/*     */         try {
/* 122 */           bis.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */       
/* 127 */       if (bos != null) {
/*     */         try {
/* 129 */           bos.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\FileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */