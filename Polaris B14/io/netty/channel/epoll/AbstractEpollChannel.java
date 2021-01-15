/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelMetadata;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.UnixChannel;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.UnresolvedAddressException;
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
/*     */ abstract class AbstractEpollChannel
/*     */   extends AbstractChannel
/*     */   implements UnixChannel
/*     */ {
/*  36 */   private static final ChannelMetadata DATA = new ChannelMetadata(false);
/*     */   private final int readFlag;
/*     */   private final FileDescriptor fileDescriptor;
/*  39 */   protected int flags = Native.EPOLLET;
/*     */   protected volatile boolean active;
/*     */   
/*     */   AbstractEpollChannel(int fd, int flag)
/*     */   {
/*  44 */     this(null, fd, flag, false);
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, int fd, int flag, boolean active) {
/*  48 */     this(parent, new FileDescriptor(fd), flag, active);
/*     */   }
/*     */   
/*     */   AbstractEpollChannel(Channel parent, FileDescriptor fd, int flag, boolean active) {
/*  52 */     super(parent);
/*  53 */     if (fd == null) {
/*  54 */       throw new NullPointerException("fd");
/*     */     }
/*  56 */     this.readFlag = flag;
/*  57 */     this.flags |= flag;
/*  58 */     this.active = active;
/*  59 */     this.fileDescriptor = fd;
/*     */   }
/*     */   
/*     */   void setFlag(int flag) {
/*  63 */     if (!isFlagSet(flag)) {
/*  64 */       this.flags |= flag;
/*  65 */       modifyEvents();
/*     */     }
/*     */   }
/*     */   
/*     */   void clearFlag(int flag) {
/*  70 */     if (isFlagSet(flag)) {
/*  71 */       this.flags &= (flag ^ 0xFFFFFFFF);
/*  72 */       modifyEvents();
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isFlagSet(int flag) {
/*  77 */     return (this.flags & flag) != 0;
/*     */   }
/*     */   
/*     */   public final FileDescriptor fd()
/*     */   {
/*  82 */     return this.fileDescriptor;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract EpollChannelConfig config();
/*     */   
/*     */   public boolean isActive()
/*     */   {
/*  90 */     return this.active;
/*     */   }
/*     */   
/*     */   public ChannelMetadata metadata()
/*     */   {
/*  95 */     return DATA;
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception
/*     */   {
/* 100 */     this.active = false;
/*     */     
/*     */ 
/* 103 */     doDeregister();
/*     */     
/* 105 */     FileDescriptor fd = this.fileDescriptor;
/* 106 */     fd.close();
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 111 */     doClose();
/*     */   }
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop)
/*     */   {
/* 116 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 121 */     return this.fileDescriptor.isOpen();
/*     */   }
/*     */   
/*     */   protected void doDeregister() throws Exception
/*     */   {
/* 126 */     ((EpollEventLoop)eventLoop().unwrap()).remove(this);
/*     */   }
/*     */   
/*     */   protected void doBeginRead()
/*     */     throws Exception
/*     */   {
/* 132 */     ((AbstractEpollUnsafe)unsafe()).readPending = true;
/*     */     
/* 134 */     setFlag(this.readFlag);
/*     */   }
/*     */   
/*     */   final void clearEpollIn()
/*     */   {
/* 139 */     if (isRegistered()) {
/* 140 */       EventLoop loop = eventLoop();
/* 141 */       final AbstractEpollUnsafe unsafe = (AbstractEpollUnsafe)unsafe();
/* 142 */       if (loop.inEventLoop()) {
/* 143 */         unsafe.clearEpollIn0();
/*     */       }
/*     */       else {
/* 146 */         loop.execute(new OneTimeTask()
/*     */         {
/*     */           public void run() {
/* 149 */             if ((!AbstractEpollChannel.this.config().isAutoRead()) && (!unsafe.readPending))
/*     */             {
/* 151 */               unsafe.clearEpollIn0();
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 159 */       this.flags &= (this.readFlag ^ 0xFFFFFFFF);
/*     */     }
/*     */   }
/*     */   
/*     */   private void modifyEvents() {
/* 164 */     if ((isOpen()) && (isRegistered())) {
/* 165 */       ((EpollEventLoop)eventLoop().unwrap()).modify(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doRegister() throws Exception
/*     */   {
/* 171 */     ((EpollEventLoop)eventLoop().unwrap()).add(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract AbstractEpollUnsafe newUnsafe();
/*     */   
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(ByteBuf buf)
/*     */   {
/* 181 */     return newDirectBuffer(buf, buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ByteBuf newDirectBuffer(Object holder, ByteBuf buf)
/*     */   {
/* 190 */     int readableBytes = buf.readableBytes();
/* 191 */     if (readableBytes == 0) {
/* 192 */       ReferenceCountUtil.safeRelease(holder);
/* 193 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 196 */     ByteBufAllocator alloc = alloc();
/* 197 */     if (alloc.isDirectBufferPooled()) {
/* 198 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 201 */     ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
/* 202 */     if (directBuf == null) {
/* 203 */       return newDirectBuffer0(holder, buf, alloc, readableBytes);
/*     */     }
/*     */     
/* 206 */     directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
/* 207 */     ReferenceCountUtil.safeRelease(holder);
/* 208 */     return directBuf;
/*     */   }
/*     */   
/*     */   private static ByteBuf newDirectBuffer0(Object holder, ByteBuf buf, ByteBufAllocator alloc, int capacity) {
/* 212 */     ByteBuf directBuf = alloc.directBuffer(capacity);
/* 213 */     directBuf.writeBytes(buf, buf.readerIndex(), capacity);
/* 214 */     ReferenceCountUtil.safeRelease(holder);
/* 215 */     return directBuf;
/*     */   }
/*     */   
/*     */   protected static void checkResolvable(InetSocketAddress addr) {
/* 219 */     if (addr.isUnresolved()) {
/* 220 */       throw new UnresolvedAddressException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected final int doReadBytes(ByteBuf byteBuf)
/*     */     throws Exception
/*     */   {
/* 228 */     int writerIndex = byteBuf.writerIndex();
/*     */     int localReadAmount;
/* 230 */     int localReadAmount; if (byteBuf.hasMemoryAddress()) {
/* 231 */       localReadAmount = Native.readAddress(this.fileDescriptor.intValue(), byteBuf.memoryAddress(), writerIndex, byteBuf.capacity());
/*     */     }
/*     */     else {
/* 234 */       ByteBuffer buf = byteBuf.internalNioBuffer(writerIndex, byteBuf.writableBytes());
/* 235 */       localReadAmount = Native.read(this.fileDescriptor.intValue(), buf, buf.position(), buf.limit());
/*     */     }
/* 237 */     if (localReadAmount > 0) {
/* 238 */       byteBuf.writerIndex(writerIndex + localReadAmount);
/*     */     }
/* 240 */     return localReadAmount;
/*     */   }
/*     */   
/*     */   protected final int doWriteBytes(ByteBuf buf, int writeSpinCount) throws Exception {
/* 244 */     int readableBytes = buf.readableBytes();
/* 245 */     int writtenBytes = 0;
/* 246 */     if (buf.hasMemoryAddress()) {
/* 247 */       long memoryAddress = buf.memoryAddress();
/* 248 */       int readerIndex = buf.readerIndex();
/* 249 */       int writerIndex = buf.writerIndex();
/* 250 */       for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 251 */         int localFlushedAmount = Native.writeAddress(this.fileDescriptor.intValue(), memoryAddress, readerIndex, writerIndex);
/*     */         
/* 253 */         if (localFlushedAmount <= 0) break;
/* 254 */         writtenBytes += localFlushedAmount;
/* 255 */         if (writtenBytes == readableBytes) {
/* 256 */           return writtenBytes;
/*     */         }
/* 258 */         readerIndex += localFlushedAmount;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       ByteBuffer nioBuf;
/*     */       ByteBuffer nioBuf;
/* 265 */       if (buf.nioBufferCount() == 1) {
/* 266 */         nioBuf = buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes());
/*     */       } else {
/* 268 */         nioBuf = buf.nioBuffer();
/*     */       }
/* 270 */       for (int i = writeSpinCount - 1; i >= 0; i--) {
/* 271 */         int pos = nioBuf.position();
/* 272 */         int limit = nioBuf.limit();
/* 273 */         int localFlushedAmount = Native.write(this.fileDescriptor.intValue(), nioBuf, pos, limit);
/* 274 */         if (localFlushedAmount <= 0) break;
/* 275 */         nioBuf.position(pos + localFlushedAmount);
/* 276 */         writtenBytes += localFlushedAmount;
/* 277 */         if (writtenBytes == readableBytes) {
/* 278 */           return writtenBytes;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 285 */     if (writtenBytes < readableBytes)
/*     */     {
/* 287 */       setFlag(Native.EPOLLOUT);
/*     */     }
/* 289 */     return writtenBytes;
/*     */   }
/*     */   
/* 292 */   protected abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe { protected AbstractEpollUnsafe() { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean readPending;
/*     */     
/*     */ 
/*     */ 
/*     */     abstract void epollInReady();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void epollRdHupReady() {}
/*     */     
/*     */ 
/*     */ 
/*     */     protected void flush0()
/*     */     {
/* 312 */       if (AbstractEpollChannel.this.isFlagSet(Native.EPOLLOUT)) {
/* 313 */         return;
/*     */       }
/* 315 */       super.flush0();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     void epollOutReady()
/*     */     {
/* 323 */       super.flush0();
/*     */     }
/*     */     
/*     */     protected final void clearEpollIn0() {
/* 327 */       AbstractEpollChannel.this.clearFlag(AbstractEpollChannel.this.readFlag);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\AbstractEpollChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */