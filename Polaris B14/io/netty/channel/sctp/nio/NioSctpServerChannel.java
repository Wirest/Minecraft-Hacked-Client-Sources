/*     */ package io.netty.channel.sctp.nio;
/*     */ 
/*     */ import com.sun.nio.sctp.SctpChannel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.nio.AbstractNioMessageChannel;
/*     */ import io.netty.channel.sctp.DefaultSctpServerChannelConfig;
/*     */ import io.netty.channel.sctp.SctpServerChannelConfig;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class NioSctpServerChannel
/*     */   extends AbstractNioMessageChannel
/*     */   implements io.netty.channel.sctp.SctpServerChannel
/*     */ {
/*  49 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*     */   private final SctpServerChannelConfig config;
/*     */   
/*     */   private static com.sun.nio.sctp.SctpServerChannel newSocket() {
/*  53 */     try { return com.sun.nio.sctp.SctpServerChannel.open();
/*     */     } catch (IOException e) {
/*  55 */       throw new ChannelException("Failed to open a server socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSctpServerChannel()
/*     */   {
/*  66 */     super(null, newSocket(), 16);
/*  67 */     this.config = new NioSctpServerChannelConfig(this, javaChannel(), null);
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  72 */     return METADATA;
/*     */   }
/*     */   
/*     */   public Set<InetSocketAddress> allLocalAddresses()
/*     */   {
/*     */     try {
/*  78 */       Set<SocketAddress> allLocalAddresses = javaChannel().getAllLocalAddresses();
/*  79 */       Set<InetSocketAddress> addresses = new LinkedHashSet(allLocalAddresses.size());
/*  80 */       for (SocketAddress socketAddress : allLocalAddresses) {
/*  81 */         addresses.add((InetSocketAddress)socketAddress);
/*     */       }
/*  83 */       return addresses;
/*     */     } catch (Throwable ignored) {}
/*  85 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */   public SctpServerChannelConfig config()
/*     */   {
/*  91 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/*  96 */     return (isOpen()) && (!allLocalAddresses().isEmpty());
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 106 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   protected com.sun.nio.sctp.SctpServerChannel javaChannel()
/*     */   {
/* 111 */     return (com.sun.nio.sctp.SctpServerChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/*     */     try {
/* 117 */       Iterator<SocketAddress> i = javaChannel().getAllLocalAddresses().iterator();
/* 118 */       if (i.hasNext()) {
/* 119 */         return (SocketAddress)i.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 124 */     return null;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 129 */     javaChannel().bind(localAddress, this.config.getBacklog());
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 134 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadMessages(List<Object> buf) throws Exception
/*     */   {
/* 139 */     SctpChannel ch = javaChannel().accept();
/* 140 */     if (ch == null) {
/* 141 */       return 0;
/*     */     }
/* 143 */     buf.add(new NioSctpChannel(this, ch));
/* 144 */     return 1;
/*     */   }
/*     */   
/*     */   public ChannelFuture bindAddress(InetAddress localAddress)
/*     */   {
/* 149 */     return bindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 154 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 156 */         javaChannel().bindAddress(localAddress);
/* 157 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 159 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 162 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 165 */           NioSctpServerChannel.this.bindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 169 */     return promise;
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(InetAddress localAddress)
/*     */   {
/* 174 */     return unbindAddress(localAddress, newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise)
/*     */   {
/* 179 */     if (eventLoop().inEventLoop()) {
/*     */       try {
/* 181 */         javaChannel().unbindAddress(localAddress);
/* 182 */         promise.setSuccess();
/*     */       } catch (Throwable t) {
/* 184 */         promise.setFailure(t);
/*     */       }
/*     */     } else {
/* 187 */       eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 190 */           NioSctpServerChannel.this.unbindAddress(localAddress, promise);
/*     */         }
/*     */       });
/*     */     }
/* 194 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress)
/*     */     throws Exception
/*     */   {
/* 201 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 206 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 211 */     return null;
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 216 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected boolean doWriteMessage(Object msg, ChannelOutboundBuffer in) throws Exception
/*     */   {
/* 221 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) throws Exception
/*     */   {
/* 226 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private final class NioSctpServerChannelConfig extends DefaultSctpServerChannelConfig {
/*     */     private NioSctpServerChannelConfig(NioSctpServerChannel channel, com.sun.nio.sctp.SctpServerChannel javaChannel) {
/* 231 */       super(javaChannel);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 236 */       NioSctpServerChannel.this.setReadPending(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\nio\NioSctpServerChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */