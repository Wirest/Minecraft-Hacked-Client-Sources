/*    */ package io.netty.channel.sctp;
/*    */ 
/*    */ import com.sun.nio.sctp.SctpStandardSocketOptions.InitMaxStreams;
/*    */ import io.netty.channel.ChannelOption;
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
/*    */ public final class SctpChannelOption
/*    */ {
/* 30 */   private static final Class<SctpChannelOption> T = SctpChannelOption.class;
/*    */   
/* 32 */   public static final ChannelOption<Boolean> SCTP_DISABLE_FRAGMENTS = ChannelOption.valueOf(T, "SCTP_DISABLE_FRAGMENTS");
/* 33 */   public static final ChannelOption<Boolean> SCTP_EXPLICIT_COMPLETE = ChannelOption.valueOf(T, "SCTP_EXPLICIT_COMPLETE");
/* 34 */   public static final ChannelOption<Integer> SCTP_FRAGMENT_INTERLEAVE = ChannelOption.valueOf(T, "SCTP_FRAGMENT_INTERLEAVE");
/* 35 */   public static final ChannelOption<SctpStandardSocketOptions.InitMaxStreams> SCTP_INIT_MAXSTREAMS = ChannelOption.valueOf(T, "SCTP_INIT_MAXSTREAMS");
/*    */   
/* 37 */   public static final ChannelOption<Boolean> SCTP_NODELAY = ChannelOption.valueOf(T, "SCTP_NODELAY");
/* 38 */   public static final ChannelOption<SocketAddress> SCTP_PRIMARY_ADDR = ChannelOption.valueOf(T, "SCTP_PRIMARY_ADDR");
/* 39 */   public static final ChannelOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR = ChannelOption.valueOf(T, "SCTP_SET_PEER_PRIMARY_ADDR");
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\SctpChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */