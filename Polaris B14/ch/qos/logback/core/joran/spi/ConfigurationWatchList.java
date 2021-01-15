/*    */ package ch.qos.logback.core.joran.spi;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import java.net.URLDecoder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ConfigurationWatchList
/*    */   extends ContextAwareBase
/*    */ {
/*    */   URL mainURL;
/* 31 */   List<File> fileWatchList = new ArrayList();
/* 32 */   List<Long> lastModifiedList = new ArrayList();
/*    */   
/*    */   public void clear() {
/* 35 */     this.mainURL = null;
/* 36 */     this.lastModifiedList.clear();
/* 37 */     this.fileWatchList.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMainURL(URL mainURL)
/*    */   {
/* 46 */     this.mainURL = mainURL;
/* 47 */     if (mainURL != null)
/* 48 */       addAsFileToWatch(mainURL);
/*    */   }
/*    */   
/*    */   private void addAsFileToWatch(URL url) {
/* 52 */     File file = convertToFile(url);
/* 53 */     if (file != null) {
/* 54 */       this.fileWatchList.add(file);
/* 55 */       this.lastModifiedList.add(Long.valueOf(file.lastModified()));
/*    */     }
/*    */   }
/*    */   
/*    */   public void addToWatchList(URL url) {
/* 60 */     addAsFileToWatch(url);
/*    */   }
/*    */   
/*    */   public URL getMainURL() {
/* 64 */     return this.mainURL;
/*    */   }
/*    */   
/*    */   public List<File> getCopyOfFileWatchList() {
/* 68 */     return new ArrayList(this.fileWatchList);
/*    */   }
/*    */   
/*    */   public boolean changeDetected() {
/* 72 */     int len = this.fileWatchList.size();
/* 73 */     for (int i = 0; i < len; i++) {
/* 74 */       long lastModified = ((Long)this.lastModifiedList.get(i)).longValue();
/* 75 */       File file = (File)this.fileWatchList.get(i);
/* 76 */       if (lastModified != file.lastModified()) {
/* 77 */         return true;
/*    */       }
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   File convertToFile(URL url)
/*    */   {
/* 86 */     String protocol = url.getProtocol();
/* 87 */     if ("file".equals(protocol)) {
/* 88 */       return new File(URLDecoder.decode(url.getFile()));
/*    */     }
/* 90 */     addInfo("URL [" + url + "] is not of type file");
/* 91 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\ConfigurationWatchList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */