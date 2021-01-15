/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.Recycler.Handle;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*     */ public final class ChannelOutboundBuffer
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS;
/*     */   private final Channel channel;
/*     */   private Entry flushedEntry;
/*     */   private Entry unflushedEntry;
/*     */   private Entry tailEntry;
/*     */   private int flushed;
/*     */   private int nioBufferCount;
/*     */   private long nioBufferSize;
/*     */   private boolean inFail;
/*     */   private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER;
/*     */   private volatile long totalPendingSize;
/*     */   private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> UNWRITABLE_UPDATER;
/*     */   private volatile int unwritable;
/*     */   private volatile Runnable fireChannelWritabilityChangedTask;
/*     */   
/*     */   static
/*     */   {
/*  50 */     logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
/*     */     
/*  52 */     NIO_BUFFERS = new FastThreadLocal()
/*     */     {
/*     */       protected ByteBuffer[] initialValue() throws Exception {
/*  55 */         return new ByteBuffer['Ѐ'];
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
/*     */       }
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
/*  89 */     };
/*  90 */     AtomicIntegerFieldUpdater<ChannelOutboundBuffer> unwritableUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(ChannelOutboundBuffer.class, "unwritable");
/*     */     
/*  92 */     if (unwritableUpdater == null) {
/*  93 */       unwritableUpdater = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "unwritable");
/*     */     }
/*  95 */     UNWRITABLE_UPDATER = unwritableUpdater;
/*     */     
/*  97 */     AtomicLongFieldUpdater<ChannelOutboundBuffer> pendingSizeUpdater = PlatformDependent.newAtomicLongFieldUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
/*     */     
/*  99 */     if (pendingSizeUpdater == null) {
/* 100 */       pendingSizeUpdater = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
/*     */     }
/* 102 */     TOTAL_PENDING_SIZE_UPDATER = pendingSizeUpdater;
/*     */   }
/*     */   
/*     */   ChannelOutboundBuffer(AbstractChannel channel) {
/* 106 */     this.channel = channel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addMessage(Object msg, int size, ChannelPromise promise)
/*     */   {
/* 114 */     Entry entry = Entry.newInstance(msg, size, total(msg), promise);
/* 115 */     if (this.tailEntry == null) {
/* 116 */       this.flushedEntry = null;
/* 117 */       this.tailEntry = entry;
/*     */     } else {
/* 119 */       Entry tail = this.tailEntry;
/* 120 */       tail.next = entry;
/* 121 */       this.tailEntry = entry;
/*     */     }
/* 123 */     if (this.unflushedEntry == null) {
/* 124 */       this.unflushedEntry = entry;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 129 */     incrementPendingOutboundBytes(size, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addFlush()
/*     */   {
/* 141 */     Entry entry = this.unflushedEntry;
/* 142 */     if (entry != null) {
/* 143 */       if (this.flushedEntry == null)
/*     */       {
/* 145 */         this.flushedEntry = entry;
/*     */       }
/*     */       do {
/* 148 */         this.flushed += 1;
/* 149 */         if (!entry.promise.setUncancellable())
/*     */         {
/* 151 */           int pending = entry.cancel();
/* 152 */           decrementPendingOutboundBytes(pending, false);
/*     */         }
/* 154 */         entry = entry.next;
/* 155 */       } while (entry != null);
/*     */       
/*     */ 
/* 158 */       this.unflushedEntry = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void incrementPendingOutboundBytes(long size)
/*     */   {
/* 167 */     incrementPendingOutboundBytes(size, true);
/*     */   }
/*     */   
/*     */   private void incrementPendingOutboundBytes(long size, boolean invokeLater) {
/* 171 */     if (size == 0L) {
/* 172 */       return;
/*     */     }
/*     */     
/* 175 */     long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, size);
/* 176 */     if (newWriteBufferSize >= this.channel.config().getWriteBufferHighWaterMark()) {
/* 177 */       setUnwritable(invokeLater);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void decrementPendingOutboundBytes(long size)
/*     */   {
/* 186 */     decrementPendingOutboundBytes(size, true);
/*     */   }
/*     */   
/*     */   private void decrementPendingOutboundBytes(long size, boolean invokeLater) {
/* 190 */     if (size == 0L) {
/* 191 */       return;
/*     */     }
/*     */     
/* 194 */     long newWriteBufferSize = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
/* 195 */     if ((newWriteBufferSize == 0L) || (newWriteBufferSize <= this.channel.config().getWriteBufferLowWaterMark())) {
/* 196 */       setWritable(invokeLater);
/*     */     }
/*     */   }
/*     */   
/*     */   private static long total(Object msg) {
/* 201 */     if ((msg instanceof ByteBuf)) {
/* 202 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/* 204 */     if ((msg instanceof FileRegion)) {
/* 205 */       return ((FileRegion)msg).count();
/*     */     }
/* 207 */     if ((msg instanceof ByteBufHolder)) {
/* 208 */       return ((ByteBufHolder)msg).content().readableBytes();
/*     */     }
/* 210 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object current()
/*     */   {
/* 217 */     Entry entry = this.flushedEntry;
/* 218 */     if (entry == null) {
/* 219 */       return null;
/*     */     }
/*     */     
/* 222 */     return entry.msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void progress(long amount)
/*     */   {
/* 229 */     Entry e = this.flushedEntry;
/* 230 */     assert (e != null);
/* 231 */     ChannelPromise p = e.promise;
/* 232 */     if ((p instanceof ChannelProgressivePromise)) {
/* 233 */       long progress = e.progress + amount;
/* 234 */       e.progress = progress;
/* 235 */       ((ChannelProgressivePromise)p).tryProgress(progress, e.total);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean remove()
/*     */   {
/* 245 */     Entry e = this.flushedEntry;
/* 246 */     if (e == null) {
/* 247 */       return false;
/*     */     }
/* 249 */     Object msg = e.msg;
/*     */     
/* 251 */     ChannelPromise promise = e.promise;
/* 252 */     int size = e.pendingSize;
/*     */     
/* 254 */     removeEntry(e);
/*     */     
/* 256 */     if (!e.cancelled)
/*     */     {
/* 258 */       ReferenceCountUtil.safeRelease(msg);
/* 259 */       safeSuccess(promise);
/* 260 */       decrementPendingOutboundBytes(size, false);
/*     */     }
/*     */     
/*     */ 
/* 264 */     e.recycle();
/*     */     
/* 266 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean remove(Throwable cause)
/*     */   {
/* 275 */     Entry e = this.flushedEntry;
/* 276 */     if (e == null) {
/* 277 */       return false;
/*     */     }
/* 279 */     Object msg = e.msg;
/*     */     
/* 281 */     ChannelPromise promise = e.promise;
/* 282 */     int size = e.pendingSize;
/*     */     
/* 284 */     removeEntry(e);
/*     */     
/* 286 */     if (!e.cancelled)
/*     */     {
/* 288 */       ReferenceCountUtil.safeRelease(msg);
/*     */       
/* 290 */       safeFail(promise, cause);
/* 291 */       decrementPendingOutboundBytes(size, false);
/*     */     }
/*     */     
/*     */ 
/* 295 */     e.recycle();
/*     */     
/* 297 */     return true;
/*     */   }
/*     */   
/*     */   private void removeEntry(Entry e) {
/* 301 */     if (--this.flushed == 0)
/*     */     {
/* 303 */       this.flushedEntry = null;
/* 304 */       if (e == this.tailEntry) {
/* 305 */         this.tailEntry = null;
/* 306 */         this.unflushedEntry = null;
/*     */       }
/*     */     } else {
/* 309 */       this.flushedEntry = e.next;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeBytes(long writtenBytes)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 319 */       Object msg = current();
/* 320 */       if (!(msg instanceof ByteBuf)) {
/* 321 */         if (($assertionsDisabled) || (writtenBytes == 0L)) break; throw new AssertionError();
/*     */       }
/*     */       
/*     */ 
/* 325 */       ByteBuf buf = (ByteBuf)msg;
/* 326 */       int readerIndex = buf.readerIndex();
/* 327 */       int readableBytes = buf.writerIndex() - readerIndex;
/*     */       
/* 329 */       if (readableBytes <= writtenBytes) {
/* 330 */         if (writtenBytes != 0L) {
/* 331 */           progress(readableBytes);
/* 332 */           writtenBytes -= readableBytes;
/*     */         }
/* 334 */         remove();
/*     */       } else {
/* 336 */         if (writtenBytes == 0L) break;
/* 337 */         buf.readerIndex(readerIndex + (int)writtenBytes);
/* 338 */         progress(writtenBytes); break;
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public ByteBuffer[] nioBuffers()
/*     */   {
/* 356 */     long nioBufferSize = 0L;
/* 357 */     int nioBufferCount = 0;
/* 358 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 359 */     ByteBuffer[] nioBuffers = (ByteBuffer[])NIO_BUFFERS.get(threadLocalMap);
/* 360 */     Entry entry = this.flushedEntry;
/* 361 */     while ((isFlushedEntry(entry)) && ((entry.msg instanceof ByteBuf))) {
/* 362 */       if (!entry.cancelled) {
/* 363 */         ByteBuf buf = (ByteBuf)entry.msg;
/* 364 */         int readerIndex = buf.readerIndex();
/* 365 */         int readableBytes = buf.writerIndex() - readerIndex;
/*     */         
/* 367 */         if (readableBytes > 0) {
/* 368 */           nioBufferSize += readableBytes;
/* 369 */           int count = entry.count;
/* 370 */           if (count == -1)
/*     */           {
/* 372 */             entry.count = (count = buf.nioBufferCount());
/*     */           }
/* 374 */           int neededSpace = nioBufferCount + count;
/* 375 */           if (neededSpace > nioBuffers.length) {
/* 376 */             nioBuffers = expandNioBufferArray(nioBuffers, neededSpace, nioBufferCount);
/* 377 */             NIO_BUFFERS.set(threadLocalMap, nioBuffers);
/*     */           }
/* 379 */           if (count == 1) {
/* 380 */             ByteBuffer nioBuf = entry.buf;
/* 381 */             if (nioBuf == null)
/*     */             {
/*     */ 
/* 384 */               entry.buf = (nioBuf = buf.internalNioBuffer(readerIndex, readableBytes));
/*     */             }
/* 386 */             nioBuffers[(nioBufferCount++)] = nioBuf;
/*     */           } else {
/* 388 */             ByteBuffer[] nioBufs = entry.bufs;
/* 389 */             if (nioBufs == null)
/*     */             {
/*     */ 
/* 392 */               entry.bufs = (nioBufs = buf.nioBuffers());
/*     */             }
/* 394 */             nioBufferCount = fillBufferArray(nioBufs, nioBuffers, nioBufferCount);
/*     */           }
/*     */         }
/*     */       }
/* 398 */       entry = entry.next;
/*     */     }
/* 400 */     this.nioBufferCount = nioBufferCount;
/* 401 */     this.nioBufferSize = nioBufferSize;
/*     */     
/* 403 */     return nioBuffers;
/*     */   }
/*     */   
/*     */   private static int fillBufferArray(ByteBuffer[] nioBufs, ByteBuffer[] nioBuffers, int nioBufferCount) {
/* 407 */     for (ByteBuffer nioBuf : nioBufs) {
/* 408 */       if (nioBuf == null) {
/*     */         break;
/*     */       }
/* 411 */       nioBuffers[(nioBufferCount++)] = nioBuf;
/*     */     }
/* 413 */     return nioBufferCount;
/*     */   }
/*     */   
/*     */   private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] array, int neededSpace, int size) {
/* 417 */     int newCapacity = array.length;
/*     */     
/*     */     do
/*     */     {
/* 421 */       newCapacity <<= 1;
/*     */       
/* 423 */       if (newCapacity < 0) {
/* 424 */         throw new IllegalStateException();
/*     */       }
/*     */       
/* 427 */     } while (neededSpace > newCapacity);
/*     */     
/* 429 */     ByteBuffer[] newArray = new ByteBuffer[newCapacity];
/* 430 */     System.arraycopy(array, 0, newArray, 0, size);
/*     */     
/* 432 */     return newArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int nioBufferCount()
/*     */   {
/* 441 */     return this.nioBufferCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long nioBufferSize()
/*     */   {
/* 450 */     return this.nioBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWritable()
/*     */   {
/* 460 */     return this.unwritable == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUserDefinedWritability(int index)
/*     */   {
/* 468 */     return (this.unwritable & writabilityMask(index)) == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUserDefinedWritability(int index, boolean writable)
/*     */   {
/* 475 */     if (writable) {
/* 476 */       setUserDefinedWritability(index);
/*     */     } else {
/* 478 */       clearUserDefinedWritability(index);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setUserDefinedWritability(int index) {
/* 483 */     int mask = writabilityMask(index) ^ 0xFFFFFFFF;
/*     */     for (;;) {
/* 485 */       int oldValue = this.unwritable;
/* 486 */       int newValue = oldValue & mask;
/* 487 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 488 */         if ((oldValue == 0) || (newValue != 0)) break;
/* 489 */         fireChannelWritabilityChanged(true); break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void clearUserDefinedWritability(int index)
/*     */   {
/* 497 */     int mask = writabilityMask(index);
/*     */     for (;;) {
/* 499 */       int oldValue = this.unwritable;
/* 500 */       int newValue = oldValue | mask;
/* 501 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 502 */         if ((oldValue != 0) || (newValue == 0)) break;
/* 503 */         fireChannelWritabilityChanged(true); break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static int writabilityMask(int index)
/*     */   {
/* 511 */     if ((index < 1) || (index > 31)) {
/* 512 */       throw new IllegalArgumentException("index: " + index + " (expected: 1~31)");
/*     */     }
/* 514 */     return 1 << index;
/*     */   }
/*     */   
/*     */   private void setWritable(boolean invokeLater) {
/*     */     for (;;) {
/* 519 */       int oldValue = this.unwritable;
/* 520 */       int newValue = oldValue & 0xFFFFFFFE;
/* 521 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 522 */         if ((oldValue == 0) || (newValue != 0)) break;
/* 523 */         fireChannelWritabilityChanged(invokeLater); break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setUnwritable(boolean invokeLater)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 532 */       int oldValue = this.unwritable;
/* 533 */       int newValue = oldValue | 0x1;
/* 534 */       if (UNWRITABLE_UPDATER.compareAndSet(this, oldValue, newValue)) {
/* 535 */         if ((oldValue != 0) || (newValue == 0)) break;
/* 536 */         fireChannelWritabilityChanged(invokeLater); break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void fireChannelWritabilityChanged(boolean invokeLater)
/*     */   {
/* 544 */     final ChannelPipeline pipeline = this.channel.pipeline();
/* 545 */     if (invokeLater) {
/* 546 */       Runnable task = this.fireChannelWritabilityChangedTask;
/* 547 */       if (task == null) {
/* 548 */         this.fireChannelWritabilityChangedTask = ( = new Runnable()
/*     */         {
/*     */           public void run() {
/* 551 */             pipeline.fireChannelWritabilityChanged();
/*     */           }
/*     */         });
/*     */       }
/* 555 */       this.channel.eventLoop().execute(task);
/*     */     } else {
/* 557 */       pipeline.fireChannelWritabilityChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 565 */     return this.flushed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 573 */     return this.flushed == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void failFlushed(Throwable cause)
/*     */   {
/* 582 */     if (this.inFail) {
/* 583 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 587 */       this.inFail = true;
/*     */       for (;;) {
/* 589 */         if (!remove(cause)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     } finally {
/* 594 */       this.inFail = false;
/*     */     }
/*     */   }
/*     */   
/*     */   void close(final ClosedChannelException cause) {
/* 599 */     if (this.inFail) {
/* 600 */       this.channel.eventLoop().execute(new Runnable()
/*     */       {
/*     */         public void run() {
/* 603 */           ChannelOutboundBuffer.this.close(cause);
/*     */         }
/* 605 */       });
/* 606 */       return;
/*     */     }
/*     */     
/* 609 */     this.inFail = true;
/*     */     
/* 611 */     if (this.channel.isOpen()) {
/* 612 */       throw new IllegalStateException("close() must be invoked after the channel is closed.");
/*     */     }
/*     */     
/* 615 */     if (!isEmpty()) {
/* 616 */       throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 621 */       Entry e = this.unflushedEntry;
/* 622 */       while (e != null)
/*     */       {
/* 624 */         int size = e.pendingSize;
/* 625 */         TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -size);
/*     */         
/* 627 */         if (!e.cancelled) {
/* 628 */           ReferenceCountUtil.safeRelease(e.msg);
/* 629 */           safeFail(e.promise, cause);
/*     */         }
/* 631 */         e = e.recycleAndGetNext();
/*     */       }
/*     */     } finally {
/* 634 */       this.inFail = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeSuccess(ChannelPromise promise) {
/* 639 */     if ((!(promise instanceof VoidChannelPromise)) && (!promise.trySuccess())) {
/* 640 */       logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeFail(ChannelPromise promise, Throwable cause) {
/* 645 */     if ((!(promise instanceof VoidChannelPromise)) && (!promise.tryFailure(cause))) {
/* 646 */       logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long totalPendingWriteBytes()
/*     */   {
/* 656 */     return this.totalPendingSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void forEachFlushedMessage(MessageProcessor processor)
/*     */     throws Exception
/*     */   {
/* 665 */     if (processor == null) {
/* 666 */       throw new NullPointerException("processor");
/*     */     }
/*     */     
/* 669 */     Entry entry = this.flushedEntry;
/* 670 */     if (entry == null) {
/*     */       return;
/*     */     }
/*     */     do
/*     */     {
/* 675 */       if ((!entry.cancelled) && 
/* 676 */         (!processor.processMessage(entry.msg))) {
/* 677 */         return;
/*     */       }
/*     */       
/* 680 */       entry = entry.next;
/* 681 */     } while (isFlushedEntry(entry));
/*     */   }
/*     */   
/*     */   private boolean isFlushedEntry(Entry e) {
/* 685 */     return (e != null) && (e != this.unflushedEntry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void recycle() {}
/*     */   
/*     */ 
/*     */ 
/*     */   static final class Entry
/*     */   {
/* 697 */     private static final Recycler<Entry> RECYCLER = new Recycler()
/*     */     {
/*     */       protected ChannelOutboundBuffer.Entry newObject(Recycler.Handle handle) {
/* 700 */         return new ChannelOutboundBuffer.Entry(handle, null);
/*     */       }
/*     */     };
/*     */     
/*     */     private final Recycler.Handle handle;
/*     */     Entry next;
/*     */     Object msg;
/*     */     ByteBuffer[] bufs;
/*     */     ByteBuffer buf;
/*     */     ChannelPromise promise;
/*     */     long progress;
/*     */     long total;
/*     */     int pendingSize;
/* 713 */     int count = -1;
/*     */     boolean cancelled;
/*     */     
/*     */     private Entry(Recycler.Handle handle) {
/* 717 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     static Entry newInstance(Object msg, int size, long total, ChannelPromise promise) {
/* 721 */       Entry entry = (Entry)RECYCLER.get();
/* 722 */       entry.msg = msg;
/* 723 */       entry.pendingSize = size;
/* 724 */       entry.total = total;
/* 725 */       entry.promise = promise;
/* 726 */       return entry;
/*     */     }
/*     */     
/*     */     int cancel() {
/* 730 */       if (!this.cancelled) {
/* 731 */         this.cancelled = true;
/* 732 */         int pSize = this.pendingSize;
/*     */         
/*     */ 
/* 735 */         ReferenceCountUtil.safeRelease(this.msg);
/* 736 */         this.msg = Unpooled.EMPTY_BUFFER;
/*     */         
/* 738 */         this.pendingSize = 0;
/* 739 */         this.total = 0L;
/* 740 */         this.progress = 0L;
/* 741 */         this.bufs = null;
/* 742 */         this.buf = null;
/* 743 */         return pSize;
/*     */       }
/* 745 */       return 0;
/*     */     }
/*     */     
/*     */     void recycle() {
/* 749 */       this.next = null;
/* 750 */       this.bufs = null;
/* 751 */       this.buf = null;
/* 752 */       this.msg = null;
/* 753 */       this.promise = null;
/* 754 */       this.progress = 0L;
/* 755 */       this.total = 0L;
/* 756 */       this.pendingSize = 0;
/* 757 */       this.count = -1;
/* 758 */       this.cancelled = false;
/* 759 */       RECYCLER.recycle(this, this.handle);
/*     */     }
/*     */     
/*     */     Entry recycleAndGetNext() {
/* 763 */       Entry next = this.next;
/* 764 */       recycle();
/* 765 */       return next;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface MessageProcessor
/*     */   {
/*     */     public abstract boolean processMessage(Object paramObject)
/*     */       throws Exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelOutboundBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */