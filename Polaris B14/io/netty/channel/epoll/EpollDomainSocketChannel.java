/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.unix.DomainSocketAddress;
/*     */ import io.netty.channel.unix.DomainSocketChannel;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import java.net.SocketAddress;
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
/*     */ public final class EpollDomainSocketChannel
/*     */   extends AbstractEpollStreamChannel
/*     */   implements DomainSocketChannel
/*     */ {
/*  29 */   private final EpollDomainSocketChannelConfig config = new EpollDomainSocketChannelConfig(this);
/*     */   private volatile DomainSocketAddress local;
/*     */   private volatile DomainSocketAddress remote;
/*     */   
/*     */   public EpollDomainSocketChannel()
/*     */   {
/*  35 */     super(Native.socketDomainFd());
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannel(Channel parent, FileDescriptor fd) {
/*  39 */     super(parent, fd.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollDomainSocketChannel(FileDescriptor fd)
/*     */   {
/*  46 */     super(fd);
/*     */   }
/*     */   
/*     */   EpollDomainSocketChannel(Channel parent, int fd) {
/*  50 */     super(parent, fd);
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/*  55 */     return new EpollDomainUnsafe(null);
/*     */   }
/*     */   
/*     */   protected DomainSocketAddress localAddress0()
/*     */   {
/*  60 */     return this.local;
/*     */   }
/*     */   
/*     */   protected DomainSocketAddress remoteAddress0()
/*     */   {
/*  65 */     return this.remote;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/*  70 */     Native.bind(fd().intValue(), localAddress);
/*  71 */     this.local = ((DomainSocketAddress)localAddress);
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig config()
/*     */   {
/*  76 */     return this.config;
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/*  81 */     if (super.doConnect(remoteAddress, localAddress)) {
/*  82 */       this.local = ((DomainSocketAddress)localAddress);
/*  83 */       this.remote = ((DomainSocketAddress)remoteAddress);
/*  84 */       return true;
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public DomainSocketAddress remoteAddress()
/*     */   {
/*  91 */     return (DomainSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public DomainSocketAddress localAddress()
/*     */   {
/*  96 */     return (DomainSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   protected boolean doWriteSingle(ChannelOutboundBuffer in, int writeSpinCount) throws Exception
/*     */   {
/* 101 */     Object msg = in.current();
/* 102 */     if (((msg instanceof FileDescriptor)) && (Native.sendFd(fd().intValue(), ((FileDescriptor)msg).intValue()) > 0))
/*     */     {
/* 104 */       in.remove();
/* 105 */       return true;
/*     */     }
/* 107 */     return super.doWriteSingle(in, writeSpinCount);
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg)
/*     */   {
/* 112 */     if ((msg instanceof FileDescriptor)) {
/* 113 */       return msg;
/*     */     }
/* 115 */     return super.filterOutboundMessage(msg);
/*     */   }
/*     */   
/* 118 */   private final class EpollDomainUnsafe extends AbstractEpollStreamChannel.EpollStreamUnsafe { private EpollDomainUnsafe() { super(); }
/*     */     
/*     */     void epollInReady() {
/* 121 */       switch (EpollDomainSocketChannel.1.$SwitchMap$io$netty$channel$unix$DomainSocketReadMode[EpollDomainSocketChannel.this.config().getReadMode().ordinal()]) {
/*     */       case 1: 
/* 123 */         super.epollInReady();
/* 124 */         break;
/*     */       case 2: 
/* 126 */         epollInReadFd();
/* 127 */         break;
/*     */       default: 
/* 129 */         throw new Error();
/*     */       }
/*     */     }
/*     */     
/*     */     private void epollInReadFd() {
/* 134 */       boolean edgeTriggered = EpollDomainSocketChannel.this.isFlagSet(Native.EPOLLET);
/* 135 */       ChannelConfig config = EpollDomainSocketChannel.this.config();
/* 136 */       if ((!this.readPending) && (!edgeTriggered) && (!config.isAutoRead()))
/*     */       {
/* 138 */         clearEpollIn0();
/* 139 */         return;
/*     */       }
/*     */       
/* 142 */       ChannelPipeline pipeline = EpollDomainSocketChannel.this.pipeline();
/*     */       
/*     */       try
/*     */       {
/* 146 */         int maxMessagesPerRead = edgeTriggered ? Integer.MAX_VALUE : config.getMaxMessagesPerRead();
/*     */         
/* 148 */         int messages = 0;
/*     */         label72:
/* 150 */         int socketFd = Native.recvFd(EpollDomainSocketChannel.this.fd().intValue());
/* 151 */         if (socketFd != 0)
/*     */         {
/*     */ 
/* 154 */           if (socketFd == -1) {
/* 155 */             close(voidPromise()); return;
/*     */           }
/*     */           
/* 158 */           this.readPending = false;
/*     */           try
/*     */           {
/* 161 */             pipeline.fireChannelRead(new FileDescriptor(socketFd));
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */             if ((edgeTriggered) || (config.isAutoRead())) {}
/*     */           }
/*     */           catch (Throwable t)
/*     */           {
/* 164 */             pipeline.fireChannelReadComplete();
/* 165 */             pipeline.fireExceptionCaught(t);
/*     */             
/* 167 */             if ((edgeTriggered) || (config.isAutoRead())) {} } finally { if ((edgeTriggered) || (config.isAutoRead()))
/*     */             {
/*     */ 
/*     */ 
/* 171 */               throw ((Throwable)localObject1);
/*     */               
/*     */ 
/* 174 */               messages++; if (messages < maxMessagesPerRead) break label72;
/*     */             } } }
/* 176 */         pipeline.fireChannelReadComplete();
/*     */       }
/*     */       catch (Throwable t) {
/* 179 */         pipeline.fireChannelReadComplete();
/* 180 */         pipeline.fireExceptionCaught(t);
/*     */         
/*     */ 
/* 183 */         EpollDomainSocketChannel.this.eventLoop().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 186 */             EpollDomainSocketChannel.EpollDomainUnsafe.this.epollInReady();
/*     */           }
/*     */           
/*     */ 
/*     */         });
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 196 */         if ((!this.readPending) && (!config.isAutoRead())) {
/* 197 */           clearEpollIn0();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */