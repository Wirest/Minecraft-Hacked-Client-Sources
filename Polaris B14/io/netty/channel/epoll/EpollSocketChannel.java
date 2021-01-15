/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public final class EpollSocketChannel
/*     */   extends AbstractEpollStreamChannel
/*     */   implements SocketChannel
/*     */ {
/*     */   private final EpollSocketChannelConfig config;
/*     */   private volatile InetSocketAddress local;
/*     */   private volatile InetSocketAddress remote;
/*     */   
/*     */   EpollSocketChannel(Channel parent, int fd, InetSocketAddress remote)
/*     */   {
/*  44 */     super(parent, fd);
/*  45 */     this.config = new EpollSocketChannelConfig(this);
/*     */     
/*     */ 
/*  48 */     this.remote = remote;
/*  49 */     this.local = Native.localAddress(fd);
/*     */   }
/*     */   
/*     */   public EpollSocketChannel() {
/*  53 */     super(Native.socketStreamFd());
/*  54 */     this.config = new EpollSocketChannelConfig(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannel(FileDescriptor fd)
/*     */   {
/*  61 */     super(fd);
/*  62 */     this.config = new EpollSocketChannelConfig(this);
/*     */     
/*     */ 
/*     */ 
/*  66 */     this.remote = Native.remoteAddress(fd.intValue());
/*  67 */     this.local = Native.localAddress(fd.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollTcpInfo tcpInfo()
/*     */   {
/*  74 */     return tcpInfo(new EpollTcpInfo());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollTcpInfo tcpInfo(EpollTcpInfo info)
/*     */   {
/*  82 */     Native.tcpInfo(fd().intValue(), info);
/*  83 */     return info;
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/*  88 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/*  93 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/*  98 */     return this.local;
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 103 */     if (this.remote == null)
/*     */     {
/* 105 */       InetSocketAddress address = Native.remoteAddress(fd().intValue());
/* 106 */       if (address != null) {
/* 107 */         this.remote = address;
/*     */       }
/* 109 */       return address;
/*     */     }
/* 111 */     return this.remote;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress local) throws Exception
/*     */   {
/* 116 */     InetSocketAddress localAddress = (InetSocketAddress)local;
/* 117 */     int fd = fd().intValue();
/* 118 */     Native.bind(fd, localAddress);
/* 119 */     this.local = Native.localAddress(fd);
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig config()
/*     */   {
/* 124 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isInputShutdown()
/*     */   {
/* 129 */     return isInputShutdown0();
/*     */   }
/*     */   
/*     */   public boolean isOutputShutdown()
/*     */   {
/* 134 */     return isOutputShutdown0();
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput()
/*     */   {
/* 139 */     return shutdownOutput(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise)
/*     */   {
/* 144 */     Executor closeExecutor = ((EpollSocketChannelUnsafe)unsafe()).closeExecutor();
/* 145 */     if (closeExecutor != null) {
/* 146 */       closeExecutor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/* 149 */           EpollSocketChannel.this.shutdownOutput0(promise);
/*     */         }
/*     */       });
/*     */     } else {
/* 153 */       EventLoop loop = eventLoop();
/* 154 */       if (loop.inEventLoop()) {
/* 155 */         shutdownOutput0(promise);
/*     */       } else {
/* 157 */         loop.execute(new OneTimeTask()
/*     */         {
/*     */           public void run() {
/* 160 */             EpollSocketChannel.this.shutdownOutput0(promise);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 165 */     return promise;
/*     */   }
/*     */   
/*     */   public ServerSocketChannel parent()
/*     */   {
/* 170 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/* 175 */     return new EpollSocketChannelUnsafe(null);
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/* 180 */     if (localAddress != null) {
/* 181 */       checkResolvable((InetSocketAddress)localAddress);
/*     */     }
/* 183 */     checkResolvable((InetSocketAddress)remoteAddress);
/* 184 */     if (super.doConnect(remoteAddress, localAddress)) {
/* 185 */       int fd = fd().intValue();
/* 186 */       this.local = Native.localAddress(fd);
/* 187 */       this.remote = ((InetSocketAddress)remoteAddress);
/* 188 */       return true;
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */   
/* 193 */   private final class EpollSocketChannelUnsafe extends AbstractEpollStreamChannel.EpollStreamUnsafe { private EpollSocketChannelUnsafe() { super(); }
/*     */     
/*     */     protected Executor closeExecutor() {
/* 196 */       if (EpollSocketChannel.this.config().getSoLinger() > 0) {
/* 197 */         return GlobalEventExecutor.INSTANCE;
/*     */       }
/* 199 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */