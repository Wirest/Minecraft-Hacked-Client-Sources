/*    */ package io.netty.channel.rxtx;
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
/*    */ 
/*    */ 
/*    */ public final class RxtxChannelOption
/*    */ {
/* 30 */   private static final Class<RxtxChannelOption> T = RxtxChannelOption.class;
/*    */   
/* 32 */   public static final ChannelOption<Integer> BAUD_RATE = ChannelOption.valueOf(T, "BAUD_RATE");
/* 33 */   public static final ChannelOption<Boolean> DTR = ChannelOption.valueOf(T, "DTR");
/* 34 */   public static final ChannelOption<Boolean> RTS = ChannelOption.valueOf(T, "RTS");
/* 35 */   public static final ChannelOption<RxtxChannelConfig.Stopbits> STOP_BITS = ChannelOption.valueOf(T, "STOP_BITS");
/* 36 */   public static final ChannelOption<RxtxChannelConfig.Databits> DATA_BITS = ChannelOption.valueOf(T, "DATA_BITS");
/* 37 */   public static final ChannelOption<RxtxChannelConfig.Paritybit> PARITY_BIT = ChannelOption.valueOf(T, "PARITY_BIT");
/* 38 */   public static final ChannelOption<Integer> WAIT_TIME = ChannelOption.valueOf(T, "WAIT_TIME");
/* 39 */   public static final ChannelOption<Integer> READ_TIMEOUT = ChannelOption.valueOf(T, "READ_TIMEOUT");
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\rxtx\RxtxChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */