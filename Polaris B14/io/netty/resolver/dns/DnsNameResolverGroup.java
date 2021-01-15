/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.ChannelFactory;
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.channel.ReflectiveChannelFactory;
/*    */ import io.netty.channel.socket.DatagramChannel;
/*    */ import io.netty.resolver.NameResolver;
/*    */ import io.netty.resolver.NameResolverGroup;
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.internal.StringUtil;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class DnsNameResolverGroup
/*    */   extends NameResolverGroup<InetSocketAddress>
/*    */ {
/*    */   private final ChannelFactory<? extends DatagramChannel> channelFactory;
/*    */   private final InetSocketAddress localAddress;
/*    */   private final Iterable<InetSocketAddress> nameServerAddresses;
/*    */   
/*    */   public DnsNameResolverGroup(Class<? extends DatagramChannel> channelType, InetSocketAddress nameServerAddress)
/*    */   {
/* 44 */     this(channelType, DnsNameResolver.ANY_LOCAL_ADDR, nameServerAddress);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(Class<? extends DatagramChannel> channelType, InetSocketAddress localAddress, InetSocketAddress nameServerAddress)
/*    */   {
/* 50 */     this(new ReflectiveChannelFactory(channelType), localAddress, nameServerAddress);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress nameServerAddress)
/*    */   {
/* 56 */     this(channelFactory, DnsNameResolver.ANY_LOCAL_ADDR, nameServerAddress);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress localAddress, InetSocketAddress nameServerAddress)
/*    */   {
/* 62 */     this(channelFactory, localAddress, DnsServerAddresses.singleton(nameServerAddress));
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(Class<? extends DatagramChannel> channelType, Iterable<InetSocketAddress> nameServerAddresses)
/*    */   {
/* 68 */     this(channelType, DnsNameResolver.ANY_LOCAL_ADDR, nameServerAddresses);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(Class<? extends DatagramChannel> channelType, InetSocketAddress localAddress, Iterable<InetSocketAddress> nameServerAddresses)
/*    */   {
/* 74 */     this(new ReflectiveChannelFactory(channelType), localAddress, nameServerAddresses);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, Iterable<InetSocketAddress> nameServerAddresses)
/*    */   {
/* 80 */     this(channelFactory, DnsNameResolver.ANY_LOCAL_ADDR, nameServerAddresses);
/*    */   }
/*    */   
/*    */ 
/*    */   public DnsNameResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress localAddress, Iterable<InetSocketAddress> nameServerAddresses)
/*    */   {
/* 86 */     this.channelFactory = channelFactory;
/* 87 */     this.localAddress = localAddress;
/* 88 */     this.nameServerAddresses = nameServerAddresses;
/*    */   }
/*    */   
/*    */   protected NameResolver<InetSocketAddress> newResolver(EventExecutor executor) throws Exception
/*    */   {
/* 93 */     if (!(executor instanceof EventLoop)) {
/* 94 */       throw new IllegalStateException("unsupported executor type: " + StringUtil.simpleClassName(executor) + " (expected: " + StringUtil.simpleClassName(EventLoop.class));
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 99 */     return new DnsNameResolver((EventLoop)executor, this.channelFactory, this.localAddress, this.nameServerAddresses);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\dns\DnsNameResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */