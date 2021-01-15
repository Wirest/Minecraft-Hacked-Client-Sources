/*    */ package io.netty.channel.socket.nio;
/*    */ 
/*    */ import io.netty.channel.socket.InternetProtocolFamily;
/*    */ import java.net.ProtocolFamily;
/*    */ import java.net.StandardProtocolFamily;
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
/*    */ final class ProtocolFamilyConverter
/*    */ {
/*    */   public static ProtocolFamily convert(InternetProtocolFamily family)
/*    */   {
/* 36 */     switch (family) {
/*    */     case IPv4: 
/* 38 */       return StandardProtocolFamily.INET;
/*    */     case IPv6: 
/* 40 */       return StandardProtocolFamily.INET6;
/*    */     }
/* 42 */     throw new IllegalArgumentException();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\nio\ProtocolFamilyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */