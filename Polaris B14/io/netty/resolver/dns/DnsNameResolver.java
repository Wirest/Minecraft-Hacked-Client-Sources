/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.FixedRecvByteBufAllocator;
/*     */ import io.netty.channel.ReflectiveChannelFactory;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.handler.codec.dns.DnsClass;
/*     */ import io.netty.handler.codec.dns.DnsQueryEncoder;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsResource;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsResponseCode;
/*     */ import io.netty.handler.codec.dns.DnsResponseDecoder;
/*     */ import io.netty.handler.codec.dns.DnsResponseHeader;
/*     */ import io.netty.resolver.SimpleNameResolver;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.IDN;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*     */ public class DnsNameResolver
/*     */   extends SimpleNameResolver<InetSocketAddress>
/*     */ {
/*  67 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DnsNameResolver.class);
/*     */   
/*  69 */   static final InetSocketAddress ANY_LOCAL_ADDR = new InetSocketAddress(0);
/*     */   
/*  71 */   private static final InternetProtocolFamily[] DEFAULT_RESOLVE_ADDRESS_TYPES = new InternetProtocolFamily[2];
/*     */   
/*     */   static
/*     */   {
/*  75 */     if ("true".equalsIgnoreCase(SystemPropertyUtil.get("java.net.preferIPv6Addresses"))) {
/*  76 */       DEFAULT_RESOLVE_ADDRESS_TYPES[0] = InternetProtocolFamily.IPv6;
/*  77 */       DEFAULT_RESOLVE_ADDRESS_TYPES[1] = InternetProtocolFamily.IPv4;
/*  78 */       logger.debug("-Djava.net.preferIPv6Addresses: true");
/*     */     } else {
/*  80 */       DEFAULT_RESOLVE_ADDRESS_TYPES[0] = InternetProtocolFamily.IPv4;
/*  81 */       DEFAULT_RESOLVE_ADDRESS_TYPES[1] = InternetProtocolFamily.IPv6;
/*  82 */       logger.debug("-Djava.net.preferIPv6Addresses: false");
/*     */     }
/*     */   }
/*     */   
/*  86 */   private static final DnsResponseDecoder DECODER = new DnsResponseDecoder();
/*  87 */   private static final DnsQueryEncoder ENCODER = new DnsQueryEncoder();
/*     */   
/*     */ 
/*     */   final Iterable<InetSocketAddress> nameServerAddresses;
/*     */   
/*     */ 
/*     */   final ChannelFuture bindFuture;
/*     */   
/*     */ 
/*     */   final DatagramChannel ch;
/*     */   
/*  98 */   final AtomicReferenceArray<DnsQueryContext> promises = new AtomicReferenceArray(65536);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   final ConcurrentMap<DnsQuestion, DnsCacheEntry> queryCache = PlatformDependent.newConcurrentHashMap();
/*     */   
/* 105 */   private final DnsResponseHandler responseHandler = new DnsResponseHandler(null);
/*     */   
/* 107 */   private volatile long queryTimeoutMillis = 5000L;
/*     */   
/*     */   private volatile int minTtl;
/*     */   
/* 111 */   private volatile int maxTtl = Integer.MAX_VALUE;
/*     */   private volatile int negativeTtl;
/* 113 */   private volatile int maxTriesPerQuery = 2;
/*     */   
/* 115 */   private volatile InternetProtocolFamily[] resolveAddressTypes = DEFAULT_RESOLVE_ADDRESS_TYPES;
/* 116 */   private volatile boolean recursionDesired = true;
/* 117 */   private volatile int maxQueriesPerResolve = 8;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile int maxPayloadSize;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile DnsClass maxPayloadSizeClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver(EventLoop eventLoop, Class<? extends DatagramChannel> channelType, InetSocketAddress nameServerAddress)
/*     */   {
/* 132 */     this(eventLoop, channelType, ANY_LOCAL_ADDR, nameServerAddress);
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
/*     */   public DnsNameResolver(EventLoop eventLoop, Class<? extends DatagramChannel> channelType, InetSocketAddress localAddress, InetSocketAddress nameServerAddress)
/*     */   {
/* 146 */     this(eventLoop, new ReflectiveChannelFactory(channelType), localAddress, nameServerAddress);
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
/*     */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress nameServerAddress)
/*     */   {
/* 159 */     this(eventLoop, channelFactory, ANY_LOCAL_ADDR, nameServerAddress);
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
/*     */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress localAddress, InetSocketAddress nameServerAddress)
/*     */   {
/* 173 */     this(eventLoop, channelFactory, localAddress, DnsServerAddresses.singleton(nameServerAddress));
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
/*     */   public DnsNameResolver(EventLoop eventLoop, Class<? extends DatagramChannel> channelType, Iterable<InetSocketAddress> nameServerAddresses)
/*     */   {
/* 188 */     this(eventLoop, channelType, ANY_LOCAL_ADDR, nameServerAddresses);
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
/*     */   public DnsNameResolver(EventLoop eventLoop, Class<? extends DatagramChannel> channelType, InetSocketAddress localAddress, Iterable<InetSocketAddress> nameServerAddresses)
/*     */   {
/* 204 */     this(eventLoop, new ReflectiveChannelFactory(channelType), localAddress, nameServerAddresses);
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
/*     */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, Iterable<InetSocketAddress> nameServerAddresses)
/*     */   {
/* 219 */     this(eventLoop, channelFactory, ANY_LOCAL_ADDR, nameServerAddresses);
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
/*     */ 
/*     */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress localAddress, Iterable<InetSocketAddress> nameServerAddresses)
/*     */   {
/* 236 */     super(eventLoop);
/*     */     
/* 238 */     if (channelFactory == null) {
/* 239 */       throw new NullPointerException("channelFactory");
/*     */     }
/* 241 */     if (nameServerAddresses == null) {
/* 242 */       throw new NullPointerException("nameServerAddresses");
/*     */     }
/* 244 */     if (!nameServerAddresses.iterator().hasNext()) {
/* 245 */       throw new NullPointerException("nameServerAddresses is empty");
/*     */     }
/* 247 */     if (localAddress == null) {
/* 248 */       throw new NullPointerException("localAddress");
/*     */     }
/*     */     
/* 251 */     this.nameServerAddresses = nameServerAddresses;
/* 252 */     this.bindFuture = newChannel(channelFactory, localAddress);
/* 253 */     this.ch = ((DatagramChannel)this.bindFuture.channel());
/*     */     
/* 255 */     setMaxPayloadSize(4096);
/*     */   }
/*     */   
/*     */ 
/*     */   private ChannelFuture newChannel(ChannelFactory<? extends DatagramChannel> channelFactory, InetSocketAddress localAddress)
/*     */   {
/* 261 */     Bootstrap b = new Bootstrap();
/* 262 */     b.group(executor());
/* 263 */     b.channelFactory(channelFactory);
/* 264 */     b.handler(new ChannelInitializer()
/*     */     {
/*     */       protected void initChannel(DatagramChannel ch) throws Exception {
/* 267 */         ch.pipeline().addLast(new ChannelHandler[] { DnsNameResolver.DECODER, DnsNameResolver.ENCODER, DnsNameResolver.this.responseHandler });
/*     */       }
/*     */       
/* 270 */     });
/* 271 */     ChannelFuture bindFuture = b.bind(localAddress);
/* 272 */     bindFuture.channel().closeFuture().addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 275 */         DnsNameResolver.this.clearCache();
/*     */       }
/*     */       
/* 278 */     });
/* 279 */     return bindFuture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int minTtl()
/*     */   {
/* 289 */     return this.minTtl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxTtl()
/*     */   {
/* 299 */     return this.maxTtl;
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
/*     */   public DnsNameResolver setTtl(int minTtl, int maxTtl)
/*     */   {
/* 315 */     if (minTtl < 0) {
/* 316 */       throw new IllegalArgumentException("minTtl: " + minTtl + " (expected: >= 0)");
/*     */     }
/* 318 */     if (maxTtl < 0) {
/* 319 */       throw new IllegalArgumentException("maxTtl: " + maxTtl + " (expected: >= 0)");
/*     */     }
/* 321 */     if (minTtl > maxTtl) {
/* 322 */       throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
/*     */     }
/*     */     
/*     */ 
/* 326 */     this.maxTtl = maxTtl;
/* 327 */     this.minTtl = minTtl;
/*     */     
/* 329 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int negativeTtl()
/*     */   {
/* 339 */     return this.negativeTtl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setNegativeTtl(int negativeTtl)
/*     */   {
/* 350 */     if (negativeTtl < 0) {
/* 351 */       throw new IllegalArgumentException("negativeTtl: " + negativeTtl + " (expected: >= 0)");
/*     */     }
/*     */     
/* 354 */     this.negativeTtl = negativeTtl;
/*     */     
/* 356 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long queryTimeoutMillis()
/*     */   {
/* 366 */     return this.queryTimeoutMillis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setQueryTimeoutMillis(long queryTimeoutMillis)
/*     */   {
/* 377 */     if (queryTimeoutMillis < 0L) {
/* 378 */       throw new IllegalArgumentException("queryTimeoutMillis: " + queryTimeoutMillis + " (expected: >= 0)");
/*     */     }
/*     */     
/* 381 */     this.queryTimeoutMillis = queryTimeoutMillis;
/*     */     
/* 383 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxTriesPerQuery()
/*     */   {
/* 392 */     return this.maxTriesPerQuery;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setMaxTriesPerQuery(int maxTriesPerQuery)
/*     */   {
/* 403 */     if (maxTriesPerQuery < 1) {
/* 404 */       throw new IllegalArgumentException("maxTries: " + maxTriesPerQuery + " (expected: > 0)");
/*     */     }
/*     */     
/* 407 */     this.maxTriesPerQuery = maxTriesPerQuery;
/*     */     
/* 409 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<InternetProtocolFamily> resolveAddressTypes()
/*     */   {
/* 420 */     return Arrays.asList(this.resolveAddressTypes);
/*     */   }
/*     */   
/*     */   InternetProtocolFamily[] resolveAddressTypesUnsafe() {
/* 424 */     return this.resolveAddressTypes;
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
/*     */   public DnsNameResolver setResolveAddressTypes(InternetProtocolFamily... resolveAddressTypes)
/*     */   {
/* 438 */     if (resolveAddressTypes == null) {
/* 439 */       throw new NullPointerException("resolveAddressTypes");
/*     */     }
/*     */     
/* 442 */     List<InternetProtocolFamily> list = new ArrayList(InternetProtocolFamily.values().length);
/*     */     
/*     */ 
/* 445 */     for (InternetProtocolFamily f : resolveAddressTypes) {
/* 446 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 451 */       if (!list.contains(f))
/*     */       {
/*     */ 
/*     */ 
/* 455 */         list.add(f);
/*     */       }
/*     */     }
/* 458 */     if (list.isEmpty()) {
/* 459 */       throw new IllegalArgumentException("no protocol family specified");
/*     */     }
/*     */     
/* 462 */     this.resolveAddressTypes = ((InternetProtocolFamily[])list.toArray(new InternetProtocolFamily[list.size()]));
/*     */     
/* 464 */     return this;
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
/*     */   public DnsNameResolver setResolveAddressTypes(Iterable<InternetProtocolFamily> resolveAddressTypes)
/*     */   {
/* 478 */     if (resolveAddressTypes == null) {
/* 479 */       throw new NullPointerException("resolveAddressTypes");
/*     */     }
/*     */     
/* 482 */     List<InternetProtocolFamily> list = new ArrayList(InternetProtocolFamily.values().length);
/*     */     
/*     */ 
/* 485 */     for (InternetProtocolFamily f : resolveAddressTypes) {
/* 486 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 491 */       if (!list.contains(f))
/*     */       {
/*     */ 
/*     */ 
/* 495 */         list.add(f);
/*     */       }
/*     */     }
/* 498 */     if (list.isEmpty()) {
/* 499 */       throw new IllegalArgumentException("no protocol family specified");
/*     */     }
/*     */     
/* 502 */     this.resolveAddressTypes = ((InternetProtocolFamily[])list.toArray(new InternetProtocolFamily[list.size()]));
/*     */     
/* 504 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRecursionDesired()
/*     */   {
/* 514 */     return this.recursionDesired;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setRecursionDesired(boolean recursionDesired)
/*     */   {
/* 525 */     this.recursionDesired = recursionDesired;
/* 526 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxQueriesPerResolve()
/*     */   {
/* 536 */     return this.maxQueriesPerResolve;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setMaxQueriesPerResolve(int maxQueriesPerResolve)
/*     */   {
/* 547 */     if (maxQueriesPerResolve <= 0) {
/* 548 */       throw new IllegalArgumentException("maxQueriesPerResolve: " + maxQueriesPerResolve + " (expected: > 0)");
/*     */     }
/*     */     
/* 551 */     this.maxQueriesPerResolve = maxQueriesPerResolve;
/*     */     
/* 553 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxPayloadSize()
/*     */   {
/* 562 */     return this.maxPayloadSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver setMaxPayloadSize(int maxPayloadSize)
/*     */   {
/* 573 */     if (maxPayloadSize <= 0) {
/* 574 */       throw new IllegalArgumentException("maxPayloadSize: " + maxPayloadSize + " (expected: > 0)");
/*     */     }
/*     */     
/* 577 */     if (this.maxPayloadSize == maxPayloadSize)
/*     */     {
/* 579 */       return this;
/*     */     }
/*     */     
/* 582 */     this.maxPayloadSize = maxPayloadSize;
/* 583 */     this.maxPayloadSizeClass = DnsClass.valueOf(maxPayloadSize);
/* 584 */     this.ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(maxPayloadSize));
/*     */     
/* 586 */     return this;
/*     */   }
/*     */   
/*     */   DnsClass maxPayloadSizeClass() {
/* 590 */     return this.maxPayloadSizeClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsNameResolver clearCache()
/*     */   {
/* 601 */     for (Iterator<Map.Entry<DnsQuestion, DnsCacheEntry>> i = this.queryCache.entrySet().iterator(); i.hasNext();) {
/* 602 */       Map.Entry<DnsQuestion, DnsCacheEntry> e = (Map.Entry)i.next();
/* 603 */       i.remove();
/* 604 */       ((DnsCacheEntry)e.getValue()).release();
/*     */     }
/*     */     
/* 607 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean clearCache(DnsQuestion question)
/*     */   {
/* 614 */     DnsCacheEntry e = (DnsCacheEntry)this.queryCache.remove(question);
/* 615 */     if (e != null) {
/* 616 */       e.release();
/* 617 */       return true;
/*     */     }
/* 619 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 630 */     this.ch.close();
/*     */   }
/*     */   
/*     */   protected EventLoop executor()
/*     */   {
/* 635 */     return (EventLoop)super.executor();
/*     */   }
/*     */   
/*     */   protected boolean doIsResolved(InetSocketAddress address)
/*     */   {
/* 640 */     return !address.isUnresolved();
/*     */   }
/*     */   
/*     */   protected void doResolve(InetSocketAddress unresolvedAddress, Promise<InetSocketAddress> promise) throws Exception
/*     */   {
/* 645 */     String hostname = IDN.toASCII(hostname(unresolvedAddress));
/* 646 */     int port = unresolvedAddress.getPort();
/*     */     
/* 648 */     DnsNameResolverContext ctx = new DnsNameResolverContext(this, hostname, port, promise);
/*     */     
/* 650 */     ctx.resolve();
/*     */   }
/*     */   
/*     */   private static String hostname(InetSocketAddress addr)
/*     */   {
/* 655 */     if (PlatformDependent.javaVersion() < 7) {
/* 656 */       return addr.getHostName();
/*     */     }
/* 658 */     return addr.getHostString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Future<DnsResponse> query(DnsQuestion question)
/*     */   {
/* 666 */     return query(this.nameServerAddresses, question);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Future<DnsResponse> query(DnsQuestion question, Promise<DnsResponse> promise)
/*     */   {
/* 673 */     return query(this.nameServerAddresses, question, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Future<DnsResponse> query(Iterable<InetSocketAddress> nameServerAddresses, DnsQuestion question)
/*     */   {
/* 680 */     if (nameServerAddresses == null) {
/* 681 */       throw new NullPointerException("nameServerAddresses");
/*     */     }
/* 683 */     if (question == null) {
/* 684 */       throw new NullPointerException("question");
/*     */     }
/*     */     
/* 687 */     EventLoop eventLoop = this.ch.eventLoop();
/* 688 */     DnsCacheEntry cachedResult = (DnsCacheEntry)this.queryCache.get(question);
/* 689 */     if (cachedResult != null) {
/* 690 */       if (cachedResult.response != null) {
/* 691 */         return eventLoop.newSucceededFuture(cachedResult.response.retain());
/*     */       }
/* 693 */       return eventLoop.newFailedFuture(cachedResult.cause);
/*     */     }
/*     */     
/* 696 */     return query0(nameServerAddresses, question, eventLoop.newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Future<DnsResponse> query(Iterable<InetSocketAddress> nameServerAddresses, DnsQuestion question, Promise<DnsResponse> promise)
/*     */   {
/* 706 */     if (nameServerAddresses == null) {
/* 707 */       throw new NullPointerException("nameServerAddresses");
/*     */     }
/* 709 */     if (question == null) {
/* 710 */       throw new NullPointerException("question");
/*     */     }
/* 712 */     if (promise == null) {
/* 713 */       throw new NullPointerException("promise");
/*     */     }
/*     */     
/* 716 */     DnsCacheEntry cachedResult = (DnsCacheEntry)this.queryCache.get(question);
/* 717 */     if (cachedResult != null) {
/* 718 */       if (cachedResult.response != null) {
/* 719 */         return promise.setSuccess(cachedResult.response.retain());
/*     */       }
/* 721 */       return promise.setFailure(cachedResult.cause);
/*     */     }
/*     */     
/* 724 */     return query0(nameServerAddresses, question, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   private Future<DnsResponse> query0(Iterable<InetSocketAddress> nameServerAddresses, DnsQuestion question, Promise<DnsResponse> promise)
/*     */   {
/*     */     try
/*     */     {
/* 732 */       new DnsQueryContext(this, nameServerAddresses, question, promise).query();
/* 733 */       return promise;
/*     */     } catch (Exception e) {
/* 735 */       return promise.setFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   void cache(final DnsQuestion question, DnsCacheEntry entry, long delaySeconds) {
/* 740 */     DnsCacheEntry oldEntry = (DnsCacheEntry)this.queryCache.put(question, entry);
/* 741 */     if (oldEntry != null) {
/* 742 */       oldEntry.release();
/*     */     }
/*     */     
/* 745 */     boolean scheduled = false;
/*     */     try {
/* 747 */       entry.expirationFuture = this.ch.eventLoop().schedule(new OneTimeTask()
/*     */       {
/*     */ 
/* 750 */         public void run() { DnsNameResolver.this.clearCache(question); } }, delaySeconds, TimeUnit.SECONDS);
/*     */       
/*     */ 
/*     */ 
/* 754 */       scheduled = true;
/*     */     } finally {
/* 756 */       if (!scheduled)
/*     */       {
/*     */ 
/* 759 */         clearCache(question);
/* 760 */         entry.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final class DnsResponseHandler extends ChannelHandlerAdapter {
/*     */     private DnsResponseHandler() {}
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 769 */       try { DnsResponse res = (DnsResponse)msg;
/* 770 */         int queryId = res.header().id();
/*     */         
/* 772 */         if (DnsNameResolver.logger.isDebugEnabled()) {
/* 773 */           DnsNameResolver.logger.debug("{} RECEIVED: [{}: {}], {}", new Object[] { DnsNameResolver.this.ch, Integer.valueOf(queryId), res.sender(), res });
/*     */         }
/*     */         
/* 776 */         DnsQueryContext qCtx = (DnsQueryContext)DnsNameResolver.this.promises.get(queryId);
/*     */         
/* 778 */         if (qCtx == null) {
/* 779 */           if (DnsNameResolver.logger.isWarnEnabled()) {
/* 780 */             DnsNameResolver.logger.warn("Received a DNS response with an unknown ID: {}", Integer.valueOf(queryId));
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 785 */           List<DnsQuestion> questions = res.questions();
/* 786 */           if (questions.size() != 1) {
/* 787 */             DnsNameResolver.logger.warn("Received a DNS response with invalid number of questions: {}", res);
/*     */           }
/*     */           else
/*     */           {
/* 791 */             DnsQuestion q = qCtx.question();
/* 792 */             if (!q.equals(questions.get(0))) {
/* 793 */               DnsNameResolver.logger.warn("Received a mismatching DNS response: {}", res);
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 798 */               ScheduledFuture<?> timeoutFuture = qCtx.timeoutFuture();
/* 799 */               if (timeoutFuture != null) {
/* 800 */                 timeoutFuture.cancel(false);
/*     */               }
/*     */               
/* 803 */               if (res.header().responseCode() == DnsResponseCode.NOERROR) {
/* 804 */                 cache(q, res);
/* 805 */                 DnsNameResolver.this.promises.set(queryId, null);
/*     */                 
/* 807 */                 Promise<DnsResponse> qPromise = qCtx.promise();
/* 808 */                 if (qPromise.setUncancellable()) {
/* 809 */                   qPromise.setSuccess(res.retain());
/*     */                 }
/*     */               } else {
/* 812 */                 qCtx.retry(res.sender(), "response code: " + res.header().responseCode() + " with " + res.answers().size() + " answer(s) and " + res.authorityResources().size() + " authority resource(s)");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       } finally {
/* 818 */         ReferenceCountUtil.safeRelease(msg);
/*     */       }
/*     */     }
/*     */     
/*     */     private void cache(DnsQuestion question, DnsResponse res) {
/* 823 */       int maxTtl = DnsNameResolver.this.maxTtl();
/* 824 */       if (maxTtl == 0) {
/* 825 */         return;
/*     */       }
/*     */       
/* 828 */       long ttl = Long.MAX_VALUE;
/*     */       
/* 830 */       for (DnsResource r : res.answers()) {
/* 831 */         long rTtl = r.timeToLive();
/* 832 */         if (ttl > rTtl) {
/* 833 */           ttl = rTtl;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 838 */       ttl = Math.max(DnsNameResolver.this.minTtl(), Math.min(maxTtl, ttl));
/*     */       
/* 840 */       DnsNameResolver.this.cache(question, new DnsNameResolver.DnsCacheEntry(res), ttl);
/*     */     }
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */     {
/* 845 */       DnsNameResolver.logger.warn("Unexpected exception: ", cause);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DnsCacheEntry {
/*     */     final DnsResponse response;
/*     */     final Throwable cause;
/*     */     volatile ScheduledFuture<?> expirationFuture;
/*     */     
/*     */     DnsCacheEntry(DnsResponse response) {
/* 855 */       this.response = response.retain();
/* 856 */       this.cause = null;
/*     */     }
/*     */     
/*     */     DnsCacheEntry(Throwable cause) {
/* 860 */       this.cause = cause;
/* 861 */       this.response = null;
/*     */     }
/*     */     
/*     */     void release() {
/* 865 */       DnsResponse response = this.response;
/* 866 */       if (response != null) {
/* 867 */         ReferenceCountUtil.safeRelease(response);
/*     */       }
/*     */       
/* 870 */       ScheduledFuture<?> expirationFuture = this.expirationFuture;
/* 871 */       if (expirationFuture != null) {
/* 872 */         expirationFuture.cancel(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\dns\DnsNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */