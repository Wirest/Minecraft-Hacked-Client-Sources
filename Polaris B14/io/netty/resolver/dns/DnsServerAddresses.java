/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ public final class DnsServerAddresses
/*     */ {
/*  44 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DnsServerAddresses.class);
/*     */   private static final List<InetSocketAddress> DEFAULT_NAME_SERVER_LIST;
/*     */   private static final InetSocketAddress[] DEFAULT_NAME_SERVER_ARRAY;
/*     */   
/*     */   static
/*     */   {
/*  50 */     int DNS_PORT = 53;
/*  51 */     List<InetSocketAddress> defaultNameServers = new ArrayList(2);
/*     */     try {
/*  53 */       Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
/*  54 */       Method open = configClass.getMethod("open", new Class[0]);
/*  55 */       Method nameservers = configClass.getMethod("nameservers", new Class[0]);
/*  56 */       Object instance = open.invoke(null, new Object[0]);
/*     */       
/*     */ 
/*  59 */       List<String> list = (List)nameservers.invoke(instance, new Object[0]);
/*  60 */       int size = list.size();
/*  61 */       for (int i = 0; i < size; i++) {
/*  62 */         String dnsAddr = (String)list.get(i);
/*  63 */         if (dnsAddr != null) {
/*  64 */           defaultNameServers.add(new InetSocketAddress(InetAddress.getByName(dnsAddr), 53));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ignore) {}
/*     */     
/*     */ 
/*     */ 
/*  72 */     if (!defaultNameServers.isEmpty()) {
/*  73 */       if (logger.isDebugEnabled()) {
/*  74 */         logger.debug("Default DNS servers: {} (sun.net.dns.ResolverConfiguration)", defaultNameServers);
/*     */       }
/*     */     }
/*     */     else {
/*  78 */       Collections.addAll(defaultNameServers, new InetSocketAddress[] { new InetSocketAddress("8.8.8.8", 53), new InetSocketAddress("8.8.4.4", 53) });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  83 */       if (logger.isWarnEnabled()) {
/*  84 */         logger.warn("Default DNS servers: {} (Google Public DNS as a fallback)", defaultNameServers);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  89 */     DEFAULT_NAME_SERVER_LIST = Collections.unmodifiableList(defaultNameServers);
/*  90 */     DEFAULT_NAME_SERVER_ARRAY = (InetSocketAddress[])defaultNameServers.toArray(new InetSocketAddress[defaultNameServers.size()]);
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
/*     */   public static List<InetSocketAddress> defaultAddresses()
/*     */   {
/* 103 */     return DEFAULT_NAME_SERVER_LIST;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> sequential(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/* 111 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> sequential(InetSocketAddress... addresses)
/*     */   {
/* 119 */     return sequential0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static Iterable<InetSocketAddress> sequential0(InetSocketAddress[] addresses) {
/* 123 */     new Iterable()
/*     */     {
/*     */       public Iterator<InetSocketAddress> iterator() {
/* 126 */         return new DnsServerAddresses.SequentialAddressIterator(this.val$addresses, 0);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> shuffled(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/* 136 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> shuffled(InetSocketAddress... addresses)
/*     */   {
/* 144 */     return shuffled0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static Iterable<InetSocketAddress> shuffled0(InetSocketAddress[] addresses) {
/* 148 */     if (addresses.length == 1) {
/* 149 */       return singleton(addresses[0]);
/*     */     }
/*     */     
/* 152 */     new Iterable()
/*     */     {
/*     */       public Iterator<InetSocketAddress> iterator() {
/* 155 */         return new DnsServerAddresses.ShuffledAddressIterator(this.val$addresses);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> rotational(Iterable<? extends InetSocketAddress> addresses)
/*     */   {
/* 168 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> rotational(InetSocketAddress... addresses)
/*     */   {
/* 179 */     return rotational0(sanitize(addresses));
/*     */   }
/*     */   
/*     */   private static Iterable<InetSocketAddress> rotational0(InetSocketAddress[] addresses) {
/* 183 */     return new RotationalAddresses(addresses);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Iterable<InetSocketAddress> singleton(InetSocketAddress address)
/*     */   {
/* 191 */     if (address == null) {
/* 192 */       throw new NullPointerException("address");
/*     */     }
/* 194 */     if (address.isUnresolved()) {
/* 195 */       throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + address);
/*     */     }
/*     */     
/* 198 */     new Iterable()
/*     */     {
/* 200 */       private final Iterator<InetSocketAddress> iterator = new Iterator()
/*     */       {
/*     */         public boolean hasNext() {
/* 203 */           return true;
/*     */         }
/*     */         
/*     */         public InetSocketAddress next()
/*     */         {
/* 208 */           return DnsServerAddresses.3.this.val$address;
/*     */         }
/*     */         
/*     */         public void remove()
/*     */         {
/* 213 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/*     */       
/*     */       public Iterator<InetSocketAddress> iterator()
/*     */       {
/* 219 */         return this.iterator;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static InetSocketAddress[] sanitize(Iterable<? extends InetSocketAddress> addresses) {
/* 225 */     if (addresses == null) {
/* 226 */       throw new NullPointerException("addresses");
/*     */     }
/*     */     List<InetSocketAddress> list;
/*     */     List<InetSocketAddress> list;
/* 230 */     if ((addresses instanceof Collection)) {
/* 231 */       list = new ArrayList(((Collection)addresses).size());
/*     */     } else {
/* 233 */       list = new ArrayList(4);
/*     */     }
/*     */     
/* 236 */     for (InetSocketAddress a : addresses) {
/* 237 */       if (a == null) {
/*     */         break;
/*     */       }
/* 240 */       if (a.isUnresolved()) {
/* 241 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 243 */       list.add(a);
/*     */     }
/*     */     
/* 246 */     if (list.isEmpty()) {
/* 247 */       return DEFAULT_NAME_SERVER_ARRAY;
/*     */     }
/*     */     
/* 250 */     return (InetSocketAddress[])list.toArray(new InetSocketAddress[list.size()]);
/*     */   }
/*     */   
/*     */   private static InetSocketAddress[] sanitize(InetSocketAddress[] addresses) {
/* 254 */     if (addresses == null) {
/* 255 */       throw new NullPointerException("addresses");
/*     */     }
/*     */     
/* 258 */     List<InetSocketAddress> list = new ArrayList(addresses.length);
/* 259 */     for (InetSocketAddress a : addresses) {
/* 260 */       if (a == null) {
/*     */         break;
/*     */       }
/* 263 */       if (a.isUnresolved()) {
/* 264 */         throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
/*     */       }
/* 266 */       list.add(a);
/*     */     }
/*     */     
/* 269 */     if (list.isEmpty()) {
/* 270 */       return DEFAULT_NAME_SERVER_ARRAY;
/*     */     }
/*     */     
/* 273 */     return (InetSocketAddress[])list.toArray(new InetSocketAddress[list.size()]);
/*     */   }
/*     */   
/*     */   private static final class SequentialAddressIterator
/*     */     implements Iterator<InetSocketAddress>
/*     */   {
/*     */     private final InetSocketAddress[] addresses;
/*     */     private int i;
/*     */     
/*     */     SequentialAddressIterator(InetSocketAddress[] addresses, int startIdx)
/*     */     {
/* 284 */       this.addresses = addresses;
/* 285 */       this.i = startIdx;
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 290 */       return true;
/*     */     }
/*     */     
/*     */     public InetSocketAddress next()
/*     */     {
/* 295 */       int i = this.i;
/* 296 */       InetSocketAddress next = this.addresses[i];
/* 297 */       i++; if (i < this.addresses.length) {
/* 298 */         this.i = i;
/*     */       } else {
/* 300 */         this.i = 0;
/*     */       }
/* 302 */       return next;
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 307 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ShuffledAddressIterator implements Iterator<InetSocketAddress>
/*     */   {
/*     */     private final InetSocketAddress[] addresses;
/*     */     private int i;
/*     */     
/*     */     ShuffledAddressIterator(InetSocketAddress[] addresses) {
/* 317 */       this.addresses = ((InetSocketAddress[])addresses.clone());
/*     */       
/* 319 */       shuffle();
/*     */     }
/*     */     
/*     */     private void shuffle() {
/* 323 */       InetSocketAddress[] addresses = this.addresses;
/* 324 */       Random r = ThreadLocalRandom.current();
/*     */       
/* 326 */       for (int i = addresses.length - 1; i >= 0; i--) {
/* 327 */         InetSocketAddress tmp = addresses[i];
/* 328 */         int j = r.nextInt(i + 1);
/* 329 */         addresses[i] = addresses[j];
/* 330 */         addresses[j] = tmp;
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 336 */       return true;
/*     */     }
/*     */     
/*     */     public InetSocketAddress next()
/*     */     {
/* 341 */       int i = this.i;
/* 342 */       InetSocketAddress next = this.addresses[i];
/* 343 */       i++; if (i < this.addresses.length) {
/* 344 */         this.i = i;
/*     */       } else {
/* 346 */         this.i = 0;
/* 347 */         shuffle();
/*     */       }
/* 349 */       return next;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 354 */     public void remove() { throw new UnsupportedOperationException(); }
/*     */   }
/*     */   
/*     */   private static final class RotationalAddresses implements Iterable<InetSocketAddress> {
/*     */     private static final AtomicIntegerFieldUpdater<RotationalAddresses> startIdxUpdater;
/*     */     private final InetSocketAddress[] addresses;
/*     */     private volatile int startIdx;
/*     */     
/*     */     static {
/* 363 */       AtomicIntegerFieldUpdater<RotationalAddresses> updater = PlatformDependent.newAtomicIntegerFieldUpdater(RotationalAddresses.class, "startIdx");
/*     */       
/*     */ 
/* 366 */       if (updater == null) {
/* 367 */         updater = AtomicIntegerFieldUpdater.newUpdater(RotationalAddresses.class, "startIdx");
/*     */       }
/*     */       
/* 370 */       startIdxUpdater = updater;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     RotationalAddresses(InetSocketAddress[] addresses)
/*     */     {
/* 378 */       this.addresses = addresses;
/*     */     }
/*     */     
/*     */     public Iterator<InetSocketAddress> iterator()
/*     */     {
/*     */       for (;;) {
/* 384 */         int curStartIdx = this.startIdx;
/* 385 */         int nextStartIdx = curStartIdx + 1;
/* 386 */         if (nextStartIdx >= this.addresses.length) {
/* 387 */           nextStartIdx = 0;
/*     */         }
/* 389 */         if (startIdxUpdater.compareAndSet(this, curStartIdx, nextStartIdx)) {
/* 390 */           return new DnsServerAddresses.SequentialAddressIterator(this.addresses, curStartIdx);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\dns\DnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */