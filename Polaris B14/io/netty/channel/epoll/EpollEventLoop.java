/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.SingleThreadEventLoop;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.collection.IntObjectMap.Entry;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ final class EpollEventLoop
/*     */   extends SingleThreadEventLoop
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
/*     */   private final int epollFd;
/*     */   private final int eventFd;
/*     */   
/*     */   static
/*     */   {
/*  40 */     logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
/*     */     
/*     */ 
/*     */ 
/*  44 */     AtomicIntegerFieldUpdater<EpollEventLoop> updater = PlatformDependent.newAtomicIntegerFieldUpdater(EpollEventLoop.class, "wakenUp");
/*     */     
/*  46 */     if (updater == null) {
/*  47 */       updater = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
/*     */     }
/*  49 */     WAKEN_UP_UPDATER = updater;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  54 */   private final IntObjectMap<AbstractEpollChannel> channels = new IntObjectHashMap(4096);
/*     */   
/*     */   private final boolean allowGrowing;
/*     */   
/*     */   private final EpollEventArray events;
/*     */   private volatile int wakenUp;
/*  60 */   private volatile int ioRatio = 50;
/*     */   
/*     */   EpollEventLoop(EventLoopGroup parent, Executor executor, int maxEvents) {
/*  63 */     super(parent, executor, false);
/*  64 */     if (maxEvents == 0) {
/*  65 */       this.allowGrowing = true;
/*  66 */       this.events = new EpollEventArray(4096);
/*     */     } else {
/*  68 */       this.allowGrowing = false;
/*  69 */       this.events = new EpollEventArray(maxEvents);
/*     */     }
/*  71 */     boolean success = false;
/*  72 */     int epollFd = -1;
/*  73 */     int eventFd = -1;
/*     */     try {
/*  75 */       this.epollFd = (epollFd = Native.epollCreate());
/*  76 */       this.eventFd = (eventFd = Native.eventFd());
/*  77 */       Native.epollCtlAdd(epollFd, eventFd, Native.EPOLLIN);
/*  78 */       success = true; return;
/*     */     } finally {
/*  80 */       if (!success) {
/*  81 */         if (epollFd != -1) {
/*     */           try {
/*  83 */             Native.close(epollFd);
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */         
/*  88 */         if (eventFd != -1) {
/*     */           try {
/*  90 */             Native.close(eventFd);
/*     */           }
/*     */           catch (Exception e) {}
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void wakeup(boolean inEventLoop)
/*     */   {
/* 101 */     if ((!inEventLoop) && (WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)))
/*     */     {
/* 103 */       Native.eventFdWrite(this.eventFd, 1L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void add(AbstractEpollChannel ch)
/*     */   {
/* 111 */     assert (inEventLoop());
/* 112 */     int fd = ch.fd().intValue();
/* 113 */     Native.epollCtlAdd(this.epollFd, fd, ch.flags);
/* 114 */     this.channels.put(fd, ch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void modify(AbstractEpollChannel ch)
/*     */   {
/* 121 */     assert (inEventLoop());
/* 122 */     Native.epollCtlMod(this.epollFd, ch.fd().intValue(), ch.flags);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void remove(AbstractEpollChannel ch)
/*     */   {
/* 129 */     assert (inEventLoop());
/*     */     
/* 131 */     if (ch.isOpen()) {
/* 132 */       int fd = ch.fd().intValue();
/* 133 */       if (this.channels.remove(fd) != null)
/*     */       {
/*     */ 
/* 136 */         Native.epollCtlDel(this.epollFd, ch.fd().intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Queue<Runnable> newTaskQueue()
/*     */   {
/* 144 */     return PlatformDependent.newMpscQueue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIoRatio()
/*     */   {
/* 151 */     return this.ioRatio;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIoRatio(int ioRatio)
/*     */   {
/* 159 */     if ((ioRatio <= 0) || (ioRatio > 100)) {
/* 160 */       throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
/*     */     }
/* 162 */     this.ioRatio = ioRatio;
/*     */   }
/*     */   
/*     */   private int epollWait(boolean oldWakenUp) throws IOException {
/* 166 */     int selectCnt = 0;
/* 167 */     long currentTimeNanos = System.nanoTime();
/* 168 */     long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
/*     */     for (;;) {
/* 170 */       long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
/* 171 */       if (timeoutMillis <= 0L) {
/* 172 */         if (selectCnt != 0) break;
/* 173 */         int ready = Native.epollWait(this.epollFd, this.events, 0);
/* 174 */         if (ready > 0) {
/* 175 */           return ready;
/*     */         }
/* 177 */         break;
/*     */       }
/*     */       
/*     */ 
/* 181 */       int selectedKeys = Native.epollWait(this.epollFd, this.events, (int)timeoutMillis);
/* 182 */       selectCnt++;
/*     */       
/* 184 */       if ((selectedKeys != 0) || (oldWakenUp) || (this.wakenUp == 1) || (hasTasks()) || (hasScheduledTasks()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 189 */         return selectedKeys;
/*     */       }
/* 191 */       currentTimeNanos = System.nanoTime();
/*     */     }
/* 193 */     return 0;
/*     */   }
/*     */   
/*     */   protected void run()
/*     */   {
/* 198 */     boolean oldWakenUp = WAKEN_UP_UPDATER.getAndSet(this, 0) == 1;
/*     */     try { int ready;
/*     */       int ready;
/* 201 */       if (hasTasks())
/*     */       {
/* 203 */         ready = Native.epollWait(this.epollFd, this.events, 0);
/*     */       } else {
/* 205 */         ready = epollWait(oldWakenUp);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */         if (this.wakenUp == 1) {
/* 236 */           Native.eventFdWrite(this.eventFd, 1L);
/*     */         }
/*     */       }
/*     */       
/* 240 */       int ioRatio = this.ioRatio;
/* 241 */       if (ioRatio == 100) {
/* 242 */         if (ready > 0) {
/* 243 */           processReady(this.events, ready);
/*     */         }
/* 245 */         runAllTasks();
/*     */       } else {
/* 247 */         long ioStartTime = System.nanoTime();
/*     */         
/* 249 */         if (ready > 0) {
/* 250 */           processReady(this.events, ready);
/*     */         }
/*     */         
/* 253 */         long ioTime = System.nanoTime() - ioStartTime;
/* 254 */         runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
/*     */       }
/* 256 */       if ((this.allowGrowing) && (ready == this.events.length()))
/*     */       {
/* 258 */         this.events.increase();
/*     */       }
/* 260 */       if (isShuttingDown()) {
/* 261 */         closeAll();
/* 262 */         if (confirmShutdown()) {
/* 263 */           cleanupAndTerminate(true);
/* 264 */           return;
/*     */         }
/*     */       }
/*     */     } catch (Throwable t) {
/* 268 */       logger.warn("Unexpected exception in the selector loop.", t);
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 273 */         Thread.sleep(1000L);
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */     }
/*     */     
/*     */ 
/* 279 */     scheduleExecution();
/*     */   }
/*     */   
/*     */   private void closeAll() {
/*     */     try {
/* 284 */       Native.epollWait(this.epollFd, this.events, 0);
/*     */     }
/*     */     catch (IOException ignore) {}
/*     */     
/* 288 */     Collection<AbstractEpollChannel> array = new ArrayList(this.channels.size());
/*     */     
/* 290 */     for (IntObjectMap.Entry<AbstractEpollChannel> entry : this.channels.entries()) {
/* 291 */       array.add(entry.value());
/*     */     }
/*     */     
/* 294 */     for (AbstractEpollChannel ch : array) {
/* 295 */       ch.unsafe().close(ch.unsafe().voidPromise());
/*     */     }
/*     */   }
/*     */   
/*     */   private void processReady(EpollEventArray events, int ready) {
/* 300 */     for (int i = 0; i < ready; i++) {
/* 301 */       int fd = events.fd(i);
/* 302 */       if (fd == this.eventFd)
/*     */       {
/* 304 */         Native.eventFdRead(this.eventFd);
/*     */       } else {
/* 306 */         long ev = events.events(i);
/*     */         
/* 308 */         AbstractEpollChannel ch = (AbstractEpollChannel)this.channels.get(fd);
/* 309 */         if ((ch != null) && (ch.isOpen())) {
/* 310 */           boolean close = (ev & Native.EPOLLRDHUP) != 0L;
/* 311 */           boolean read = (ev & Native.EPOLLIN) != 0L;
/* 312 */           boolean write = (ev & Native.EPOLLOUT) != 0L;
/*     */           
/* 314 */           AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe)ch.unsafe();
/*     */           
/* 316 */           if (close) {
/* 317 */             unsafe.epollRdHupReady();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 325 */           if ((write) && (ch.isOpen()))
/*     */           {
/* 327 */             unsafe.epollOutReady();
/*     */           }
/* 329 */           if ((read) && (ch.isOpen()))
/*     */           {
/* 331 */             unsafe.epollInReady();
/*     */           }
/*     */         }
/*     */         else {
/* 335 */           Native.epollCtlDel(this.epollFd, fd);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void cleanup()
/*     */   {
/*     */     try {
/*     */       try {
/* 345 */         Native.close(this.epollFd);
/*     */       } catch (IOException e) {
/* 347 */         logger.warn("Failed to close the epoll fd.", e);
/*     */       }
/*     */       try {
/* 350 */         Native.close(this.eventFd);
/*     */       } catch (IOException e) {
/* 352 */         logger.warn("Failed to close the event fd.", e);
/*     */       }
/*     */     }
/*     */     finally {
/* 356 */       this.events.free();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */