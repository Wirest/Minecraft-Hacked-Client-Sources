/*    */ package io.netty.channel.udt.nio;
/*    */ 
/*    */ import com.barchart.udt.TypeUDT;
/*    */ import com.barchart.udt.nio.ServerSocketChannelUDT;
/*    */ import com.barchart.udt.nio.SocketChannelUDT;
/*    */ import io.netty.channel.ChannelMetadata;
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
/*    */ public class NioUdtMessageAcceptorChannel
/*    */   extends NioUdtAcceptorChannel
/*    */ {
/* 29 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*    */   
/*    */   public NioUdtMessageAcceptorChannel() {
/* 32 */     super(TypeUDT.DATAGRAM);
/*    */   }
/*    */   
/*    */   protected int doReadMessages(List<Object> buf) throws Exception
/*    */   {
/* 37 */     SocketChannelUDT channelUDT = javaChannel().accept();
/* 38 */     if (channelUDT == null) {
/* 39 */       return 0;
/*    */     }
/* 41 */     buf.add(new NioUdtMessageConnectorChannel(this, channelUDT));
/* 42 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */   public ChannelMetadata metadata()
/*    */   {
/* 48 */     return METADATA;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\nio\NioUdtMessageAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */