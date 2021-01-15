/*     */ package io.netty.channel.group;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelId;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class DefaultChannelGroup
/*     */   extends AbstractSet<Channel>
/*     */   implements ChannelGroup
/*     */ {
/*  44 */   private static final AtomicInteger nextId = new AtomicInteger();
/*     */   private final String name;
/*     */   private final EventExecutor executor;
/*  47 */   private final ConcurrentMap<ChannelId, Channel> serverChannels = PlatformDependent.newConcurrentHashMap();
/*  48 */   private final ConcurrentMap<ChannelId, Channel> nonServerChannels = PlatformDependent.newConcurrentHashMap();
/*  49 */   private final ChannelFutureListener remover = new ChannelFutureListener()
/*     */   {
/*     */     public void operationComplete(ChannelFuture future) throws Exception {
/*  52 */       DefaultChannelGroup.this.remove(future.channel());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultChannelGroup(EventExecutor executor)
/*     */   {
/*  61 */     this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultChannelGroup(String name, EventExecutor executor)
/*     */   {
/*  70 */     if (name == null) {
/*  71 */       throw new NullPointerException("name");
/*     */     }
/*  73 */     this.name = name;
/*  74 */     this.executor = executor;
/*     */   }
/*     */   
/*     */   public String name()
/*     */   {
/*  79 */     return this.name;
/*     */   }
/*     */   
/*     */   public Channel find(ChannelId id)
/*     */   {
/*  84 */     Channel c = (Channel)this.nonServerChannels.get(id);
/*  85 */     if (c != null) {
/*  86 */       return c;
/*     */     }
/*  88 */     return (Channel)this.serverChannels.get(id);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  94 */     return (this.nonServerChannels.isEmpty()) && (this.serverChannels.isEmpty());
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  99 */     return this.nonServerChannels.size() + this.serverChannels.size();
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/* 104 */     if ((o instanceof Channel)) {
/* 105 */       Channel c = (Channel)o;
/* 106 */       if ((o instanceof ServerChannel)) {
/* 107 */         return this.serverChannels.containsValue(c);
/*     */       }
/* 109 */       return this.nonServerChannels.containsValue(c);
/*     */     }
/*     */     
/* 112 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean add(Channel channel)
/*     */   {
/* 118 */     ConcurrentMap<ChannelId, Channel> map = (channel instanceof ServerChannel) ? this.serverChannels : this.nonServerChannels;
/*     */     
/*     */ 
/* 121 */     boolean added = map.putIfAbsent(channel.id(), channel) == null;
/* 122 */     if (added) {
/* 123 */       channel.closeFuture().addListener(this.remover);
/*     */     }
/* 125 */     return added;
/*     */   }
/*     */   
/*     */   public boolean remove(Object o)
/*     */   {
/* 130 */     Channel c = null;
/* 131 */     if ((o instanceof ChannelId)) {
/* 132 */       c = (Channel)this.nonServerChannels.remove(o);
/* 133 */       if (c == null) {
/* 134 */         c = (Channel)this.serverChannels.remove(o);
/*     */       }
/* 136 */     } else if ((o instanceof Channel)) {
/* 137 */       c = (Channel)o;
/* 138 */       if ((c instanceof ServerChannel)) {
/* 139 */         c = (Channel)this.serverChannels.remove(c.id());
/*     */       } else {
/* 141 */         c = (Channel)this.nonServerChannels.remove(c.id());
/*     */       }
/*     */     }
/*     */     
/* 145 */     if (c == null) {
/* 146 */       return false;
/*     */     }
/*     */     
/* 149 */     c.closeFuture().removeListener(this.remover);
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 155 */     this.nonServerChannels.clear();
/* 156 */     this.serverChannels.clear();
/*     */   }
/*     */   
/*     */   public Iterator<Channel> iterator()
/*     */   {
/* 161 */     return new CombinedIterator(this.serverChannels.values().iterator(), this.nonServerChannels.values().iterator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 168 */     Collection<Channel> channels = new ArrayList(size());
/* 169 */     channels.addAll(this.serverChannels.values());
/* 170 */     channels.addAll(this.nonServerChannels.values());
/* 171 */     return channels.toArray();
/*     */   }
/*     */   
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/* 176 */     Collection<Channel> channels = new ArrayList(size());
/* 177 */     channels.addAll(this.serverChannels.values());
/* 178 */     channels.addAll(this.nonServerChannels.values());
/* 179 */     return channels.toArray(a);
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture close()
/*     */   {
/* 184 */     return close(ChannelMatchers.all());
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture disconnect()
/*     */   {
/* 189 */     return disconnect(ChannelMatchers.all());
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture deregister()
/*     */   {
/* 194 */     return deregister(ChannelMatchers.all());
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture write(Object message)
/*     */   {
/* 199 */     return write(message, ChannelMatchers.all());
/*     */   }
/*     */   
/*     */ 
/*     */   private static Object safeDuplicate(Object message)
/*     */   {
/* 205 */     if ((message instanceof ByteBuf))
/* 206 */       return ((ByteBuf)message).duplicate().retain();
/* 207 */     if ((message instanceof ByteBufHolder)) {
/* 208 */       return ((ByteBufHolder)message).duplicate().retain();
/*     */     }
/* 210 */     return ReferenceCountUtil.retain(message);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelGroupFuture write(Object message, ChannelMatcher matcher)
/*     */   {
/* 216 */     if (message == null) {
/* 217 */       throw new NullPointerException("message");
/*     */     }
/* 219 */     if (matcher == null) {
/* 220 */       throw new NullPointerException("matcher");
/*     */     }
/*     */     
/* 223 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap(size());
/* 224 */     for (Channel c : this.nonServerChannels.values()) {
/* 225 */       if (matcher.matches(c)) {
/* 226 */         futures.put(c, c.write(safeDuplicate(message)));
/*     */       }
/*     */     }
/*     */     
/* 230 */     ReferenceCountUtil.release(message);
/* 231 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */   
/*     */   public ChannelGroup flush()
/*     */   {
/* 236 */     return flush(ChannelMatchers.all());
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture writeAndFlush(Object message)
/*     */   {
/* 241 */     return writeAndFlush(message, ChannelMatchers.all());
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture disconnect(ChannelMatcher matcher)
/*     */   {
/* 246 */     if (matcher == null) {
/* 247 */       throw new NullPointerException("matcher");
/*     */     }
/*     */     
/* 250 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap(size());
/*     */     
/*     */ 
/* 253 */     for (Channel c : this.serverChannels.values()) {
/* 254 */       if (matcher.matches(c)) {
/* 255 */         futures.put(c, c.disconnect());
/*     */       }
/*     */     }
/* 258 */     for (Channel c : this.nonServerChannels.values()) {
/* 259 */       if (matcher.matches(c)) {
/* 260 */         futures.put(c, c.disconnect());
/*     */       }
/*     */     }
/*     */     
/* 264 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture close(ChannelMatcher matcher)
/*     */   {
/* 269 */     if (matcher == null) {
/* 270 */       throw new NullPointerException("matcher");
/*     */     }
/*     */     
/* 273 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap(size());
/*     */     
/*     */ 
/* 276 */     for (Channel c : this.serverChannels.values()) {
/* 277 */       if (matcher.matches(c)) {
/* 278 */         futures.put(c, c.close());
/*     */       }
/*     */     }
/* 281 */     for (Channel c : this.nonServerChannels.values()) {
/* 282 */       if (matcher.matches(c)) {
/* 283 */         futures.put(c, c.close());
/*     */       }
/*     */     }
/*     */     
/* 287 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture deregister(ChannelMatcher matcher)
/*     */   {
/* 292 */     if (matcher == null) {
/* 293 */       throw new NullPointerException("matcher");
/*     */     }
/*     */     
/* 296 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap(size());
/*     */     
/*     */ 
/* 299 */     for (Channel c : this.serverChannels.values()) {
/* 300 */       if (matcher.matches(c)) {
/* 301 */         futures.put(c, c.deregister());
/*     */       }
/*     */     }
/* 304 */     for (Channel c : this.nonServerChannels.values()) {
/* 305 */       if (matcher.matches(c)) {
/* 306 */         futures.put(c, c.deregister());
/*     */       }
/*     */     }
/*     */     
/* 310 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */   
/*     */   public ChannelGroup flush(ChannelMatcher matcher)
/*     */   {
/* 315 */     for (Channel c : this.nonServerChannels.values()) {
/* 316 */       if (matcher.matches(c)) {
/* 317 */         c.flush();
/*     */       }
/*     */     }
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher)
/*     */   {
/* 325 */     if (message == null) {
/* 326 */       throw new NullPointerException("message");
/*     */     }
/*     */     
/* 329 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap(size());
/*     */     
/* 331 */     for (Channel c : this.nonServerChannels.values()) {
/* 332 */       if (matcher.matches(c)) {
/* 333 */         futures.put(c, c.writeAndFlush(safeDuplicate(message)));
/*     */       }
/*     */     }
/*     */     
/* 337 */     ReferenceCountUtil.release(message);
/*     */     
/* 339 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 344 */     return System.identityHashCode(this);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 349 */     return this == o;
/*     */   }
/*     */   
/*     */   public int compareTo(ChannelGroup o)
/*     */   {
/* 354 */     int v = name().compareTo(o.name());
/* 355 */     if (v != 0) {
/* 356 */       return v;
/*     */     }
/*     */     
/* 359 */     return System.identityHashCode(this) - System.identityHashCode(o);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 364 */     return StringUtil.simpleClassName(this) + "(name: " + name() + ", size: " + size() + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\DefaultChannelGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */