/*    */ package io.netty.channel.udt;
/*    */ 
/*    */ import io.netty.channel.ChannelOption;
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
/*    */ public final class UdtChannelOption
/*    */ {
/* 28 */   private static final Class<UdtChannelOption> T = UdtChannelOption.class;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 33 */   public static final ChannelOption<Integer> PROTOCOL_RECEIVE_BUFFER_SIZE = ChannelOption.valueOf(T, "PROTOCOL_RECEIVE_BUFFER_SIZE");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */   public static final ChannelOption<Integer> PROTOCOL_SEND_BUFFER_SIZE = ChannelOption.valueOf(T, "PROTOCOL_SEND_BUFFER_SIZE");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 44 */   public static final ChannelOption<Integer> SYSTEM_RECEIVE_BUFFER_SIZE = ChannelOption.valueOf(T, "SYSTEM_RECEIVE_BUFFER_SIZE");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 49 */   public static final ChannelOption<Integer> SYSTEM_SEND_BUFFER_SIZE = ChannelOption.valueOf(T, "SYSTEM_SEND_BUFFER_SIZE");
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\UdtChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */