/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.channel.socket.ServerSocketChannel;
/*    */ import io.netty.channel.unix.FileDescriptor;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class EpollServerSocketChannel
/*    */   extends AbstractEpollServerChannel
/*    */   implements ServerSocketChannel
/*    */ {
/*    */   private final EpollServerSocketChannelConfig config;
/*    */   private volatile InetSocketAddress local;
/*    */   
/*    */   public EpollServerSocketChannel()
/*    */   {
/* 36 */     super(Native.socketStreamFd());
/* 37 */     this.config = new EpollServerSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EpollServerSocketChannel(FileDescriptor fd)
/*    */   {
/* 44 */     super(fd);
/* 45 */     this.config = new EpollServerSocketChannelConfig(this);
/*    */     
/*    */ 
/*    */ 
/* 49 */     this.local = Native.localAddress(fd.intValue());
/*    */   }
/*    */   
/*    */   protected boolean isCompatible(EventLoop loop)
/*    */   {
/* 54 */     return loop instanceof EpollEventLoop;
/*    */   }
/*    */   
/*    */   protected void doBind(SocketAddress localAddress) throws Exception
/*    */   {
/* 59 */     InetSocketAddress addr = (InetSocketAddress)localAddress;
/* 60 */     checkResolvable(addr);
/* 61 */     int fd = fd().intValue();
/* 62 */     Native.bind(fd, addr);
/* 63 */     this.local = Native.localAddress(fd);
/* 64 */     Native.listen(fd, this.config.getBacklog());
/* 65 */     this.active = true;
/*    */   }
/*    */   
/*    */   public InetSocketAddress remoteAddress()
/*    */   {
/* 70 */     return (InetSocketAddress)super.remoteAddress();
/*    */   }
/*    */   
/*    */   public InetSocketAddress localAddress()
/*    */   {
/* 75 */     return (InetSocketAddress)super.localAddress();
/*    */   }
/*    */   
/*    */   public EpollServerSocketChannelConfig config()
/*    */   {
/* 80 */     return this.config;
/*    */   }
/*    */   
/*    */   protected InetSocketAddress localAddress0()
/*    */   {
/* 85 */     return this.local;
/*    */   }
/*    */   
/*    */   protected Channel newChildChannel(int fd, byte[] address, int offset, int len) throws Exception
/*    */   {
/* 90 */     return new EpollSocketChannel(this, fd, Native.address(address, offset, len));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */