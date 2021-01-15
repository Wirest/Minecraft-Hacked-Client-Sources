/*    */ package io.netty.channel.local;
/*    */ 
/*    */ import io.netty.channel.Channel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LocalAddress
/*    */   extends SocketAddress
/*    */   implements Comparable<LocalAddress>
/*    */ {
/*    */   private static final long serialVersionUID = 4644331421130916435L;
/* 30 */   public static final LocalAddress ANY = new LocalAddress("ANY");
/*    */   
/*    */ 
/*    */   private final String id;
/*    */   
/*    */ 
/*    */   private final String strVal;
/*    */   
/*    */ 
/*    */   LocalAddress(Channel channel)
/*    */   {
/* 41 */     StringBuilder buf = new StringBuilder(16);
/* 42 */     buf.append("local:E");
/* 43 */     buf.append(Long.toHexString(channel.hashCode() & 0xFFFFFFFF | 0x100000000));
/* 44 */     buf.setCharAt(7, ':');
/* 45 */     this.id = buf.substring(6);
/* 46 */     this.strVal = buf.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public LocalAddress(String id)
/*    */   {
/* 53 */     if (id == null) {
/* 54 */       throw new NullPointerException("id");
/*    */     }
/* 56 */     id = id.trim().toLowerCase();
/* 57 */     if (id.isEmpty()) {
/* 58 */       throw new IllegalArgumentException("empty id");
/*    */     }
/* 60 */     this.id = id;
/* 61 */     this.strVal = ("local:" + id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String id()
/*    */   {
/* 68 */     return this.id;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 73 */     return this.id.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 78 */     if (!(o instanceof LocalAddress)) {
/* 79 */       return false;
/*    */     }
/*    */     
/* 82 */     return this.id.equals(((LocalAddress)o).id);
/*    */   }
/*    */   
/*    */   public int compareTo(LocalAddress o)
/*    */   {
/* 87 */     return this.id.compareTo(o.id);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 92 */     return this.strVal;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\local\LocalAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */