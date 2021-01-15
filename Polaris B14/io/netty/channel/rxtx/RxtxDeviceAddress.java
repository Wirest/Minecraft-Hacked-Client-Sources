/*    */ package io.netty.channel.rxtx;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RxtxDeviceAddress
/*    */   extends SocketAddress
/*    */ {
/*    */   private static final long serialVersionUID = -2907820090993709523L;
/*    */   private final String value;
/*    */   
/*    */   public RxtxDeviceAddress(String value)
/*    */   {
/* 36 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String value()
/*    */   {
/* 43 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\rxtx\RxtxDeviceAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */