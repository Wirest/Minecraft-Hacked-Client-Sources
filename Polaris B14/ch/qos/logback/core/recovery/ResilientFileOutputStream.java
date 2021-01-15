/*    */ package ch.qos.logback.core.recovery;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.channels.FileChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResilientFileOutputStream
/*    */   extends ResilientOutputStreamBase
/*    */ {
/*    */   private File file;
/*    */   private FileOutputStream fos;
/*    */   
/*    */   public ResilientFileOutputStream(File file, boolean append)
/*    */     throws FileNotFoundException
/*    */   {
/* 27 */     this.file = file;
/* 28 */     this.fos = new FileOutputStream(file, append);
/* 29 */     this.os = new BufferedOutputStream(this.fos);
/* 30 */     this.presumedClean = true;
/*    */   }
/*    */   
/*    */   public FileChannel getChannel() {
/* 34 */     if (this.os == null) {
/* 35 */       return null;
/*    */     }
/* 37 */     return this.fos.getChannel();
/*    */   }
/*    */   
/*    */   public File getFile() {
/* 41 */     return this.file;
/*    */   }
/*    */   
/*    */   String getDescription()
/*    */   {
/* 46 */     return "file [" + this.file + "]";
/*    */   }
/*    */   
/*    */   OutputStream openNewOutputStream()
/*    */     throws IOException
/*    */   {
/* 52 */     this.fos = new FileOutputStream(this.file, true);
/* 53 */     return new BufferedOutputStream(this.fos);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 58 */     return "c.q.l.c.recovery.ResilientFileOutputStream@" + System.identityHashCode(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\recovery\ResilientFileOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */