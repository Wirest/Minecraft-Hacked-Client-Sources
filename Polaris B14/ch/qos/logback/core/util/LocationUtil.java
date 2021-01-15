/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocationUtil
/*    */ {
/*    */   public static final String SCHEME_PATTERN = "^\\p{Alpha}[\\p{Alnum}+.-]*:.*$";
/*    */   public static final String CLASSPATH_SCHEME = "classpath:";
/*    */   
/*    */   public static URL urlForResource(String location)
/*    */     throws MalformedURLException, FileNotFoundException
/*    */   {
/* 49 */     if (location == null) {
/* 50 */       throw new NullPointerException("location is required");
/*    */     }
/* 52 */     URL url = null;
/* 53 */     if (!location.matches("^\\p{Alpha}[\\p{Alnum}+.-]*:.*$")) {
/* 54 */       url = Loader.getResourceBySelfClassLoader(location);
/*    */     }
/* 56 */     else if (location.startsWith("classpath:")) {
/* 57 */       String path = location.substring("classpath:".length());
/* 58 */       if (path.startsWith("/")) {
/* 59 */         path = path.substring(1);
/*    */       }
/* 61 */       if (path.length() == 0) {
/* 62 */         throw new MalformedURLException("path is required");
/*    */       }
/* 64 */       url = Loader.getResourceBySelfClassLoader(path);
/*    */     }
/*    */     else {
/* 67 */       url = new URL(location);
/*    */     }
/* 69 */     if (url == null) {
/* 70 */       throw new FileNotFoundException(location);
/*    */     }
/* 72 */     return url;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\LocationUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */