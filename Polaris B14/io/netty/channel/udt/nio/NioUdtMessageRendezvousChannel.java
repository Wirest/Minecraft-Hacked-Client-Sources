/*    */ package io.netty.channel.udt.nio;
/*    */ 
/*    */ import com.barchart.udt.TypeUDT;
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
/*    */ public class NioUdtMessageRendezvousChannel
/*    */   extends NioUdtMessageConnectorChannel
/*    */ {
/*    */   public NioUdtMessageRendezvousChannel()
/*    */   {
/* 29 */     super(NioUdtProvider.newRendezvousChannelUDT(TypeUDT.DATAGRAM));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtMessageRendezvousChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */