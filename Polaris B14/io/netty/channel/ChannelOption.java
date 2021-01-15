/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.AbstractConstant;
/*     */ import io.netty.util.ConstantPool;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ChannelOption<T>
/*     */   extends AbstractConstant<ChannelOption<T>>
/*     */ {
/*  35 */   private static final ConstantPool<ChannelOption<Object>> pool = new ConstantPool()
/*     */   {
/*     */     protected ChannelOption<Object> newConstant(int id, String name) {
/*  38 */       return new ChannelOption(id, name, null);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> ChannelOption<T> valueOf(String name)
/*     */   {
/*  47 */     return (ChannelOption)pool.valueOf(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent)
/*     */   {
/*  55 */     return (ChannelOption)pool.valueOf(firstNameComponent, secondNameComponent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean exists(String name)
/*     */   {
/*  62 */     return pool.exists(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> ChannelOption<T> newInstance(String name)
/*     */   {
/*  71 */     return (ChannelOption)pool.newInstance(name);
/*     */   }
/*     */   
/*  74 */   public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
/*  75 */   public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
/*  76 */   public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");
/*     */   
/*  78 */   public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
/*  79 */   public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
/*  80 */   public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
/*  81 */   public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
/*  82 */   public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
/*     */   
/*  84 */   public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
/*  85 */   public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");
/*     */   
/*  87 */   public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
/*  88 */   public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
/*  89 */   public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
/*  90 */   public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
/*  91 */   public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
/*  92 */   public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
/*  93 */   public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
/*  94 */   public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");
/*     */   
/*  96 */   public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
/*  97 */   public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
/*  98 */   public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
/*  99 */   public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
/* 100 */   public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");
/*     */   
/* 102 */   public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
/*     */   
/*     */   @Deprecated
/* 105 */   public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChannelOption(int id, String name)
/*     */   {
/* 112 */     super(id, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate(T value)
/*     */   {
/* 120 */     if (value == null) {
/* 121 */       throw new NullPointerException("value");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */