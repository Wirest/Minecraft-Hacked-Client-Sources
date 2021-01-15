/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class DomainSocketAddress
/*    */   extends SocketAddress
/*    */ {
/*    */   private final String socketPath;
/*    */   
/*    */   public DomainSocketAddress(String socketPath)
/*    */   {
/* 29 */     if (socketPath == null) {
/* 30 */       throw new NullPointerException("socketPath");
/*    */     }
/* 32 */     this.socketPath = socketPath;
/*    */   }
/*    */   
/*    */   public DomainSocketAddress(File file) {
/* 36 */     this(file.getPath());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String path()
/*    */   {
/* 43 */     return this.socketPath;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return path();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 53 */     if (this == o) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (!(o instanceof DomainSocketAddress)) {
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     return ((DomainSocketAddress)o).socketPath.equals(this.socketPath);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 65 */     return this.socketPath.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\unix\DomainSocketAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */