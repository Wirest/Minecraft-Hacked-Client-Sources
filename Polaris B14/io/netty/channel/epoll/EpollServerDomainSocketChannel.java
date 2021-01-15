/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.unix.DomainSocketAddress;
/*    */ import io.netty.channel.unix.FileDescriptor;
/*    */ import io.netty.channel.unix.ServerDomainSocketChannel;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.io.File;
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
/*    */ public final class EpollServerDomainSocketChannel
/*    */   extends AbstractEpollServerChannel
/*    */   implements ServerDomainSocketChannel
/*    */ {
/* 31 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EpollServerDomainSocketChannel.class);
/*    */   
/*    */ 
/* 34 */   private final EpollServerChannelConfig config = new EpollServerChannelConfig(this);
/*    */   private volatile DomainSocketAddress local;
/*    */   
/*    */   public EpollServerDomainSocketChannel() {
/* 38 */     super(Native.socketDomainFd());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EpollServerDomainSocketChannel(FileDescriptor fd)
/*    */   {
/* 45 */     super(fd);
/*    */   }
/*    */   
/*    */   protected Channel newChildChannel(int fd, byte[] addr, int offset, int len) throws Exception
/*    */   {
/* 50 */     return new EpollDomainSocketChannel(this, fd);
/*    */   }
/*    */   
/*    */   protected DomainSocketAddress localAddress0()
/*    */   {
/* 55 */     return this.local;
/*    */   }
/*    */   
/*    */   protected void doBind(SocketAddress localAddress) throws Exception
/*    */   {
/* 60 */     int fd = fd().intValue();
/* 61 */     Native.bind(fd, localAddress);
/* 62 */     Native.listen(fd, this.config.getBacklog());
/* 63 */     this.local = ((DomainSocketAddress)localAddress);
/*    */   }
/*    */   
/*    */   protected void doClose() throws Exception
/*    */   {
/*    */     try {
/* 69 */       super.doClose(); } finally { DomainSocketAddress local;
/*    */       File socketFile;
/* 71 */       boolean success; DomainSocketAddress local = this.local;
/* 72 */       if (local != null)
/*    */       {
/* 74 */         File socketFile = new File(local.path());
/* 75 */         boolean success = socketFile.delete();
/* 76 */         if ((!success) && (logger.isDebugEnabled())) {
/* 77 */           logger.debug("Failed to delete a domain socket file: {}", local.path());
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public EpollServerChannelConfig config()
/*    */   {
/* 85 */     return this.config;
/*    */   }
/*    */   
/*    */   public DomainSocketAddress remoteAddress()
/*    */   {
/* 90 */     return (DomainSocketAddress)super.remoteAddress();
/*    */   }
/*    */   
/*    */   public DomainSocketAddress localAddress()
/*    */   {
/* 95 */     return (DomainSocketAddress)super.localAddress();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollServerDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */