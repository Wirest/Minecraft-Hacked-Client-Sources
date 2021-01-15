/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import java.net.InetSocketAddress;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEpollServerChannel
/*     */   extends AbstractEpollChannel
/*     */   implements ServerChannel
/*     */ {
/*     */   protected AbstractEpollServerChannel(int fd)
/*     */   {
/*  34 */     super(fd, Native.EPOLLIN);
/*     */   }
/*     */   
/*     */   protected AbstractEpollServerChannel(FileDescriptor fd) {
/*  38 */     super(null, fd, Native.EPOLLIN, Native.getSoError(fd.intValue()) == 0);
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/*  43 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */   
/*     */   protected InetSocketAddress remoteAddress0()
/*     */   {
/*  48 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe()
/*     */   {
/*  53 */     return new EpollServerSocketUnsafe();
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*  58 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  63 */   protected Object filterOutboundMessage(Object msg) throws Exception { throw new UnsupportedOperationException(); }
/*     */   
/*     */   abstract Channel newChildChannel(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3) throws Exception;
/*     */   
/*     */   final class EpollServerSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
/*  68 */     EpollServerSocketUnsafe() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  72 */     private final byte[] acceptedAddress = new byte[26];
/*     */     
/*     */ 
/*     */     public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise)
/*     */     {
/*  77 */       channelPromise.setFailure(new UnsupportedOperationException());
/*     */     }
/*     */     
/*     */     void epollInReady()
/*     */     {
/*  82 */       assert (AbstractEpollServerChannel.this.eventLoop().inEventLoop());
/*  83 */       boolean edgeTriggered = AbstractEpollServerChannel.this.isFlagSet(Native.EPOLLET);
/*     */       
/*  85 */       ChannelConfig config = AbstractEpollServerChannel.this.config();
/*  86 */       if ((!this.readPending) && (!edgeTriggered) && (!config.isAutoRead()))
/*     */       {
/*  88 */         clearEpollIn0();
/*  89 */         return;
/*     */       }
/*     */       
/*  92 */       ChannelPipeline pipeline = AbstractEpollServerChannel.this.pipeline();
/*  93 */       Throwable exception = null;
/*     */       try
/*     */       {
/*     */         try {
/*  97 */           int maxMessagesPerRead = edgeTriggered ? Integer.MAX_VALUE : config.getMaxMessagesPerRead();
/*     */           
/*  99 */           int messages = 0;
/*     */           label104:
/* 101 */           int socketFd = Native.accept(AbstractEpollServerChannel.this.fd().intValue(), this.acceptedAddress);
/* 102 */           if (socketFd != -1)
/*     */           {
/*     */ 
/*     */ 
/* 106 */             this.readPending = false;
/*     */             try
/*     */             {
/* 109 */               int len = this.acceptedAddress[0];
/* 110 */               pipeline.fireChannelRead(AbstractEpollServerChannel.this.newChildChannel(socketFd, this.acceptedAddress, 1, len));
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */               if ((edgeTriggered) || (config.isAutoRead())) {}
/*     */             }
/*     */             catch (Throwable t)
/*     */             {
/* 113 */               pipeline.fireChannelReadComplete();
/* 114 */               pipeline.fireExceptionCaught(t);
/*     */               
/* 116 */               if ((edgeTriggered) || (config.isAutoRead())) {} } finally { if ((edgeTriggered) || (config.isAutoRead()))
/*     */               {
/*     */ 
/*     */ 
/* 120 */                 throw ((Throwable)localObject1);
/*     */                 
/*     */ 
/* 123 */                 messages++; if (messages < maxMessagesPerRead) break label104;
/*     */               }
/* 125 */             } } } catch (Throwable t) { exception = t;
/*     */         }
/* 127 */         pipeline.fireChannelReadComplete();
/*     */         
/* 129 */         if (exception != null) {
/* 130 */           pipeline.fireExceptionCaught(exception);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 139 */         if ((!this.readPending) && (!config.isAutoRead())) {
/* 140 */           clearEpollIn0();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\AbstractEpollServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */