/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class FileDescriptor
/*    */ {
/*    */   private final int fd;
/* 27 */   private volatile boolean open = true;
/*    */   
/*    */   public FileDescriptor(int fd) {
/* 30 */     if (fd < 0) {
/* 31 */       throw new IllegalArgumentException("fd must be >= 0");
/*    */     }
/* 33 */     this.fd = fd;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int intValue()
/*    */   {
/* 40 */     return this.fd;
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 47 */     this.open = false;
/* 48 */     close(this.fd);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isOpen()
/*    */   {
/* 55 */     return this.open;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 60 */     return "FileDescriptor{fd=" + this.fd + '}';
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 67 */     if (this == o) {
/* 68 */       return true;
/*    */     }
/* 70 */     if (!(o instanceof FileDescriptor)) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     return this.fd == ((FileDescriptor)o).fd;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 79 */     return this.fd;
/*    */   }
/*    */   
/*    */   private static native int close(int paramInt);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\unix\FileDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */