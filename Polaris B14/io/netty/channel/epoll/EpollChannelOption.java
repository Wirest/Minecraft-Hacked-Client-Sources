/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.channel.ChannelOption;
/*    */ import io.netty.channel.unix.DomainSocketReadMode;
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
/*    */ public final class EpollChannelOption
/*    */ {
/* 22 */   private static final Class<EpollChannelOption> T = EpollChannelOption.class;
/*    */   
/* 24 */   public static final ChannelOption<Boolean> TCP_CORK = ChannelOption.valueOf(T, "TCP_CORK");
/* 25 */   public static final ChannelOption<Boolean> SO_REUSEPORT = ChannelOption.valueOf(T, "SO_REUSEPORT");
/* 26 */   public static final ChannelOption<Integer> TCP_KEEPIDLE = ChannelOption.valueOf(T, "TCP_KEEPIDLE");
/* 27 */   public static final ChannelOption<Integer> TCP_KEEPINTVL = ChannelOption.valueOf(T, "TCP_KEEPINTVL");
/* 28 */   public static final ChannelOption<Integer> TCP_KEEPCNT = ChannelOption.valueOf(T, "TCP_KEEPCNT");
/* 29 */   public static final ChannelOption<DomainSocketReadMode> DOMAIN_SOCKET_READ_MODE = ChannelOption.valueOf(T, "DOMAIN_SOCKET_READ_MODE");
/*    */   
/* 31 */   public static final ChannelOption<EpollMode> EPOLL_MODE = ChannelOption.valueOf(T, "EPOLL_MODE");
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */