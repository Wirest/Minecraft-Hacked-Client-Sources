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
/*    */ public class NioUdtByteRendezvousChannel
/*    */   extends NioUdtByteConnectorChannel
/*    */ {
/*    */   public NioUdtByteRendezvousChannel()
/*    */   {
/* 26 */     super(NioUdtProvider.newRendezvousChannelUDT(TypeUDT.STREAM));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtByteRendezvousChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */