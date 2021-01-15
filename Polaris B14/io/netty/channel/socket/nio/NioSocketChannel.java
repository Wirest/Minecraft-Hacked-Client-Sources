/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel;
/*     */ import io.netty.channel.nio.AbstractNioByteChannel.NioByteUnsafe;
/*     */ import io.netty.channel.nio.AbstractNioChannel.AbstractNioUnsafe;
/*     */ import io.netty.channel.socket.DefaultSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.spi.SelectorProvider;
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
/*     */ public class NioSocketChannel
/*     */   extends AbstractNioByteChannel
/*     */   implements io.netty.channel.socket.SocketChannel
/*     */ {
/*  49 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
/*  50 */   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
/*     */   
/*     */ 
/*     */   private final SocketChannelConfig config;
/*     */   
/*     */ 
/*     */   private static java.nio.channels.SocketChannel newSocket(SelectorProvider provider)
/*     */   {
/*     */     try
/*     */     {
/*  60 */       return provider.openSocketChannel();
/*     */     } catch (IOException e) {
/*  62 */       throw new ChannelException("Failed to open a socket.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSocketChannel()
/*     */   {
/*  72 */     this(DEFAULT_SELECTOR_PROVIDER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(SelectorProvider provider)
/*     */   {
/*  79 */     this(newSocket(provider));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(java.nio.channels.SocketChannel socket)
/*     */   {
/*  86 */     this(null, socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NioSocketChannel(Channel parent, java.nio.channels.SocketChannel socket)
/*     */   {
/*  95 */     super(parent, socket);
/*  96 */     this.config = new NioSocketChannelConfig(this, socket.socket(), null);
/*     */   }
/*     */   
/*     */   public ServerSocketChannel parent()
/*     */   {
/* 101 */     return (ServerSocketChannel)super.parent();
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/* 106 */     return METADATA;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig config()
/*     */   {
/* 111 */     return this.config;
/*     */   }
/*     */   
/*     */   protected java.nio.channels.SocketChannel javaChannel()
/*     */   {
/* 116 */     return (java.nio.channels.SocketChannel)super.javaChannel();
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/* 121 */     java.nio.channels.SocketChannel ch = javaChannel();
/* 122 */     return (ch.isOpen()) && (ch.isConnected());
/*     */   }
/*     */   
/*     */   public boolean isInputShutdown()
/*     */   {
/* 127 */     return super.isInputShutdown();
/*     */   }
/*     */   
/*     */   public InetSocketAddress localAddress()
/*     */   {
/* 132 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public InetSocketAddress remoteAddress()
/*     */   {
/* 137 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   public boolean isOutputShutdown()
/*     */   {
/* 142 */     return (javaChannel().socket().isOutputShutdown()) || (!isActive());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput()
/*     */   {
/* 147 */     return shutdownOutput(newPromise());
/*     */   }
/*     */   
/*     */   public ChannelFuture shutdownOutput(final ChannelPromise promise)
/*     */   {
/* 152 */     Executor closeExecutor = ((NioSocketChannelUnsafe)unsafe()).closeExecutor();
/* 153 */     if (closeExecutor != null) {
/* 154 */       closeExecutor.execute(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/* 157 */           NioSocketChannel.this.shutdownOutput0(promise);
/*     */         }
/*     */       });
/*     */     } else {
/* 161 */       EventLoop loop = eventLoop();
/* 162 */       if (loop.inEventLoop()) {
/* 163 */         shutdownOutput0(promise);
/*     */       } else {
/* 165 */         loop.execute(new OneTimeTask()
/*     */         {
/*     */           public void run() {
/* 168 */             NioSocketChannel.this.shutdownOutput0(promise);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 173 */     return promise;
/*     */   }
/*     */   
/*     */   private void shutdownOutput0(ChannelPromise promise) {
/*     */     try {
/* 178 */       javaChannel().socket().shutdownOutput();
/* 179 */       promise.setSuccess();
/*     */     } catch (Throwable t) {
/* 181 */       promise.setFailure(t);
/*     */     }
/*     */   }
/*     */   
/*     */   protected SocketAddress localAddress0()
/*     */   {
/* 187 */     return javaChannel().socket().getLocalSocketAddress();
/*     */   }
/*     */   
/*     */   protected SocketAddress remoteAddress0()
/*     */   {
/* 192 */     return javaChannel().socket().getRemoteSocketAddress();
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 197 */     javaChannel().socket().bind(localAddress);
/*     */   }
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/* 202 */     if (localAddress != null) {
/* 203 */       javaChannel().socket().bind(localAddress);
/*     */     }
/*     */     
/* 206 */     boolean success = false;
/*     */     try {
/* 208 */       boolean connected = javaChannel().connect(remoteAddress);
/* 209 */       if (!connected) {
/* 210 */         selectionKey().interestOps(8);
/*     */       }
/* 212 */       success = true;
/* 213 */       return connected;
/*     */     } finally {
/* 215 */       if (!success) {
/* 216 */         doClose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doFinishConnect() throws Exception
/*     */   {
/* 223 */     if (!javaChannel().finishConnect()) {
/* 224 */       throw new Error();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 230 */     doClose();
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 235 */     javaChannel().close();
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf byteBuf) throws Exception
/*     */   {
/* 240 */     return byteBuf.writeBytes(javaChannel(), byteBuf.writableBytes());
/*     */   }
/*     */   
/*     */   protected int doWriteBytes(ByteBuf buf) throws Exception
/*     */   {
/* 245 */     int expectedWrittenBytes = buf.readableBytes();
/* 246 */     return buf.readBytes(javaChannel(), expectedWrittenBytes);
/*     */   }
/*     */   
/*     */   protected long doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 251 */     long position = region.transfered();
/* 252 */     return region.transferTo(javaChannel(), position);
/*     */   }
/*     */   
/*     */   protected void doWrite(ChannelOutboundBuffer in) throws Exception
/*     */   {
/*     */     for (;;) {
/* 258 */       int size = in.size();
/* 259 */       if (size == 0)
/*     */       {
/* 261 */         clearOpWrite();
/* 262 */         break;
/*     */       }
/* 264 */       long writtenBytes = 0L;
/* 265 */       boolean done = false;
/* 266 */       boolean setOpWrite = false;
/*     */       
/*     */ 
/* 269 */       ByteBuffer[] nioBuffers = in.nioBuffers();
/* 270 */       int nioBufferCnt = in.nioBufferCount();
/* 271 */       long expectedWrittenBytes = in.nioBufferSize();
/* 272 */       java.nio.channels.SocketChannel ch = javaChannel();
/*     */       
/*     */ 
/*     */ 
/* 276 */       switch (nioBufferCnt)
/*     */       {
/*     */       case 0: 
/* 279 */         super.doWrite(in);
/* 280 */         return;
/*     */       
/*     */       case 1: 
/* 283 */         ByteBuffer nioBuffer = nioBuffers[0];
/* 284 */         for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
/* 285 */           int localWrittenBytes = ch.write(nioBuffer);
/* 286 */           if (localWrittenBytes == 0) {
/* 287 */             setOpWrite = true;
/* 288 */             break;
/*     */           }
/* 290 */           expectedWrittenBytes -= localWrittenBytes;
/* 291 */           writtenBytes += localWrittenBytes;
/* 292 */           if (expectedWrittenBytes == 0L) {
/* 293 */             done = true;
/* 294 */             break;
/*     */           }
/*     */         }
/* 297 */         break;
/*     */       default: 
/* 299 */         for (int i = config().getWriteSpinCount() - 1; i >= 0; i--) {
/* 300 */           long localWrittenBytes = ch.write(nioBuffers, 0, nioBufferCnt);
/* 301 */           if (localWrittenBytes == 0L) {
/* 302 */             setOpWrite = true;
/* 303 */             break;
/*     */           }
/* 305 */           expectedWrittenBytes -= localWrittenBytes;
/* 306 */           writtenBytes += localWrittenBytes;
/* 307 */           if (expectedWrittenBytes == 0L) {
/* 308 */             done = true;
/* 309 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 316 */       in.removeBytes(writtenBytes);
/*     */       
/* 318 */       if (!done)
/*     */       {
/* 320 */         incompleteWrite(setOpWrite);
/* 321 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 328 */   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() { return new NioSocketChannelUnsafe(null); }
/*     */   
/*     */   private final class NioSocketChannelUnsafe extends AbstractNioByteChannel.NioByteUnsafe {
/* 331 */     private NioSocketChannelUnsafe() { super(); }
/*     */     
/*     */     protected Executor closeExecutor() {
/* 334 */       if (NioSocketChannel.this.config().getSoLinger() > 0) {
/* 335 */         return GlobalEventExecutor.INSTANCE;
/*     */       }
/* 337 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
/*     */     private NioSocketChannelConfig(NioSocketChannel channel, Socket javaSocket) {
/* 343 */       super(javaSocket);
/*     */     }
/*     */     
/*     */     protected void autoReadCleared()
/*     */     {
/* 348 */       NioSocketChannel.this.setReadPending(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\nio\NioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */