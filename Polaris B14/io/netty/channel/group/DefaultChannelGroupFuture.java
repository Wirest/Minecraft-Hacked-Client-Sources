/*     */ package io.netty.channel.group;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.util.concurrent.BlockingOperationException;
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.ImmediateEventExecutor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ final class DefaultChannelGroupFuture
/*     */   extends DefaultPromise<Void>
/*     */   implements ChannelGroupFuture
/*     */ {
/*     */   private final ChannelGroup group;
/*     */   private final Map<Channel, ChannelFuture> futures;
/*     */   private int successCount;
/*     */   private int failureCount;
/*  47 */   private final ChannelFutureListener childListener = new ChannelFutureListener()
/*     */   {
/*     */     public void operationComplete(ChannelFuture future) throws Exception {
/*  50 */       boolean success = future.isSuccess();
/*     */       boolean callSetDone;
/*  52 */       synchronized (DefaultChannelGroupFuture.this) {
/*  53 */         if (success) {
/*  54 */           DefaultChannelGroupFuture.access$008(DefaultChannelGroupFuture.this);
/*     */         } else {
/*  56 */           DefaultChannelGroupFuture.access$108(DefaultChannelGroupFuture.this);
/*     */         }
/*     */         
/*  59 */         callSetDone = DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size();
/*  60 */         if ((!$assertionsDisabled) && (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount > DefaultChannelGroupFuture.this.futures.size())) { throw new AssertionError();
/*     */         }
/*     */       }
/*  63 */       if (callSetDone) {
/*  64 */         if (DefaultChannelGroupFuture.this.failureCount > 0) {
/*  65 */           List<Map.Entry<Channel, Throwable>> failed = new ArrayList(DefaultChannelGroupFuture.this.failureCount);
/*     */           
/*  67 */           for (ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
/*  68 */             if (!f.isSuccess()) {
/*  69 */               failed.add(new DefaultChannelGroupFuture.DefaultEntry(f.channel(), f.cause()));
/*     */             }
/*     */           }
/*  72 */           DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
/*     */         } else {
/*  74 */           DefaultChannelGroupFuture.this.setSuccess0();
/*     */         }
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   DefaultChannelGroupFuture(ChannelGroup group, Collection<ChannelFuture> futures, EventExecutor executor)
/*     */   {
/*  84 */     super(executor);
/*  85 */     if (group == null) {
/*  86 */       throw new NullPointerException("group");
/*     */     }
/*  88 */     if (futures == null) {
/*  89 */       throw new NullPointerException("futures");
/*     */     }
/*     */     
/*  92 */     this.group = group;
/*     */     
/*  94 */     Map<Channel, ChannelFuture> futureMap = new LinkedHashMap();
/*  95 */     for (ChannelFuture f : futures) {
/*  96 */       futureMap.put(f.channel(), f);
/*     */     }
/*     */     
/*  99 */     this.futures = Collections.unmodifiableMap(futureMap);
/*     */     
/* 101 */     for (ChannelFuture f : this.futures.values()) {
/* 102 */       f.addListener(this.childListener);
/*     */     }
/*     */     
/*     */ 
/* 106 */     if (this.futures.isEmpty()) {
/* 107 */       setSuccess0();
/*     */     }
/*     */   }
/*     */   
/*     */   DefaultChannelGroupFuture(ChannelGroup group, Map<Channel, ChannelFuture> futures, EventExecutor executor) {
/* 112 */     super(executor);
/* 113 */     this.group = group;
/* 114 */     this.futures = Collections.unmodifiableMap(futures);
/* 115 */     for (ChannelFuture f : this.futures.values()) {
/* 116 */       f.addListener(this.childListener);
/*     */     }
/*     */     
/*     */ 
/* 120 */     if (this.futures.isEmpty()) {
/* 121 */       setSuccess0();
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelGroup group()
/*     */   {
/* 127 */     return this.group;
/*     */   }
/*     */   
/*     */   public ChannelFuture find(Channel channel)
/*     */   {
/* 132 */     return (ChannelFuture)this.futures.get(channel);
/*     */   }
/*     */   
/*     */   public Iterator<ChannelFuture> iterator()
/*     */   {
/* 137 */     return this.futures.values().iterator();
/*     */   }
/*     */   
/*     */   public synchronized boolean isPartialSuccess()
/*     */   {
/* 142 */     return (this.successCount != 0) && (this.successCount != this.futures.size());
/*     */   }
/*     */   
/*     */   public synchronized boolean isPartialFailure()
/*     */   {
/* 147 */     return (this.failureCount != 0) && (this.failureCount != this.futures.size());
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/* 152 */     super.addListener(listener);
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/* 158 */     super.addListeners(listeners);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener)
/*     */   {
/* 164 */     super.removeListener(listener);
/* 165 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public DefaultChannelGroupFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners)
/*     */   {
/* 171 */     super.removeListeners(listeners);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture await() throws InterruptedException
/*     */   {
/* 177 */     super.await();
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture awaitUninterruptibly()
/*     */   {
/* 183 */     super.awaitUninterruptibly();
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture syncUninterruptibly()
/*     */   {
/* 189 */     super.syncUninterruptibly();
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture sync() throws InterruptedException
/*     */   {
/* 195 */     super.sync();
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelGroupException cause()
/*     */   {
/* 201 */     return (ChannelGroupException)super.cause();
/*     */   }
/*     */   
/*     */   private void setSuccess0() {
/* 205 */     super.setSuccess(null);
/*     */   }
/*     */   
/*     */   private void setFailure0(ChannelGroupException cause) {
/* 209 */     super.setFailure(cause);
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture setSuccess(Void result)
/*     */   {
/* 214 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean trySuccess(Void result)
/*     */   {
/* 219 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public DefaultChannelGroupFuture setFailure(Throwable cause)
/*     */   {
/* 224 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean tryFailure(Throwable cause)
/*     */   {
/* 229 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   protected void checkDeadLock()
/*     */   {
/* 234 */     EventExecutor e = executor();
/* 235 */     if ((e != null) && (e != ImmediateEventExecutor.INSTANCE) && (e.inEventLoop())) {
/* 236 */       throw new BlockingOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DefaultEntry<K, V> implements Map.Entry<K, V> {
/*     */     private final K key;
/*     */     private final V value;
/*     */     
/*     */     DefaultEntry(K key, V value) {
/* 245 */       this.key = key;
/* 246 */       this.value = value;
/*     */     }
/*     */     
/*     */     public K getKey()
/*     */     {
/* 251 */       return (K)this.key;
/*     */     }
/*     */     
/*     */     public V getValue()
/*     */     {
/* 256 */       return (V)this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V value)
/*     */     {
/* 261 */       throw new UnsupportedOperationException("read-only");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\DefaultChannelGroupFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */