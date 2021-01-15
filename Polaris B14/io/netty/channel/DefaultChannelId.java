/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ final class DefaultChannelId
/*     */   implements ChannelId
/*     */ {
/*     */   private static final long serialVersionUID = 3884076183504074063L;
/*     */   private static final InternalLogger logger;
/*     */   private static final Pattern MACHINE_ID_PATTERN;
/*     */   private static final int MACHINE_ID_LEN = 8;
/*     */   private static final byte[] MACHINE_ID;
/*     */   private static final int PROCESS_ID_LEN = 4;
/*     */   private static final int MAX_PROCESS_ID = 4194304;
/*     */   private static final int PROCESS_ID;
/*     */   private static final int SEQUENCE_LEN = 4;
/*     */   private static final int TIMESTAMP_LEN = 8;
/*     */   private static final int RANDOM_LEN = 4;
/*     */   private static final AtomicInteger nextSequence;
/*     */   
/*     */   static ChannelId newInstance()
/*     */   {
/*  64 */     DefaultChannelId id = new DefaultChannelId();
/*  65 */     id.init();
/*  66 */     return id;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  47 */     logger = InternalLoggerFactory.getInstance(DefaultChannelId.class);
/*     */     
/*  49 */     MACHINE_ID_PATTERN = Pattern.compile("^(?:[0-9a-fA-F][:-]?){6,8}$");
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
/*  61 */     nextSequence = new AtomicInteger();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */     int processId = -1;
/*  71 */     String customProcessId = SystemPropertyUtil.get("io.netty.processId");
/*  72 */     if (customProcessId != null) {
/*     */       try {
/*  74 */         processId = Integer.parseInt(customProcessId);
/*     */       }
/*     */       catch (NumberFormatException e) {}
/*     */       
/*     */ 
/*  79 */       if ((processId < 0) || (processId > 4194304)) {
/*  80 */         processId = -1;
/*  81 */         logger.warn("-Dio.netty.processId: {} (malformed)", customProcessId);
/*  82 */       } else if (logger.isDebugEnabled()) {
/*  83 */         logger.debug("-Dio.netty.processId: {} (user-set)", Integer.valueOf(processId));
/*     */       }
/*     */     }
/*     */     
/*  87 */     if (processId < 0) {
/*  88 */       processId = defaultProcessId();
/*  89 */       if (logger.isDebugEnabled()) {
/*  90 */         logger.debug("-Dio.netty.processId: {} (auto-detected)", Integer.valueOf(processId));
/*     */       }
/*     */     }
/*     */     
/*  94 */     PROCESS_ID = processId;
/*     */     
/*  96 */     byte[] machineId = null;
/*  97 */     String customMachineId = SystemPropertyUtil.get("io.netty.machineId");
/*  98 */     if (customMachineId != null) {
/*  99 */       if (MACHINE_ID_PATTERN.matcher(customMachineId).matches()) {
/* 100 */         machineId = parseMachineId(customMachineId);
/* 101 */         logger.debug("-Dio.netty.machineId: {} (user-set)", customMachineId);
/*     */       } else {
/* 103 */         logger.warn("-Dio.netty.machineId: {} (malformed)", customMachineId);
/*     */       }
/*     */     }
/*     */     
/* 107 */     if (machineId == null) {
/* 108 */       machineId = defaultMachineId();
/* 109 */       if (logger.isDebugEnabled()) {
/* 110 */         logger.debug("-Dio.netty.machineId: {} (auto-detected)", formatAddress(machineId));
/*     */       }
/*     */     }
/*     */     
/* 114 */     MACHINE_ID = machineId;
/*     */   }
/*     */   
/*     */ 
/*     */   private static byte[] parseMachineId(String value)
/*     */   {
/* 120 */     value = value.replaceAll("[:-]", "");
/*     */     
/* 122 */     byte[] machineId = new byte[8];
/* 123 */     for (int i = 0; i < value.length(); i += 2) {
/* 124 */       machineId[i] = ((byte)Integer.parseInt(value.substring(i, i + 2), 16));
/*     */     }
/*     */     
/* 127 */     return machineId;
/*     */   }
/*     */   
/*     */   private static byte[] defaultMachineId()
/*     */   {
/* 132 */     byte[] NOT_FOUND = { -1 };
/* 133 */     byte[] bestMacAddr = NOT_FOUND;
/* 134 */     InetAddress bestInetAddr = null;
/*     */     try {
/* 136 */       bestInetAddr = InetAddress.getByAddress(new byte[] { Byte.MAX_VALUE, 0, 0, 1 });
/*     */     }
/*     */     catch (UnknownHostException e) {
/* 139 */       PlatformDependent.throwException(e);
/*     */     }
/*     */     
/*     */ 
/* 143 */     Map<NetworkInterface, InetAddress> ifaces = new LinkedHashMap();
/*     */     try {
/* 145 */       for (i = NetworkInterface.getNetworkInterfaces(); i.hasMoreElements();) {
/* 146 */         NetworkInterface iface = (NetworkInterface)i.nextElement();
/*     */         
/* 148 */         Enumeration<InetAddress> addrs = iface.getInetAddresses();
/* 149 */         if (addrs.hasMoreElements()) {
/* 150 */           InetAddress a = (InetAddress)addrs.nextElement();
/* 151 */           if (!a.isLoopbackAddress())
/* 152 */             ifaces.put(iface, a);
/*     */         }
/*     */       }
/*     */     } catch (SocketException e) {
/*     */       Enumeration<NetworkInterface> i;
/* 157 */       logger.warn("Failed to retrieve the list of available network interfaces", e);
/*     */     }
/*     */     
/* 160 */     for (Map.Entry<NetworkInterface, InetAddress> entry : ifaces.entrySet()) {
/* 161 */       NetworkInterface iface = (NetworkInterface)entry.getKey();
/* 162 */       InetAddress inetAddr = (InetAddress)entry.getValue();
/* 163 */       if (!iface.isVirtual())
/*     */       {
/*     */         byte[] macAddr;
/*     */         
/*     */         try
/*     */         {
/* 169 */           macAddr = iface.getHardwareAddress();
/*     */         } catch (SocketException e) {
/* 171 */           logger.debug("Failed to get the hardware address of a network interface: {}", iface, e); }
/* 172 */         continue;
/*     */         
/*     */ 
/* 175 */         boolean replace = false;
/* 176 */         int res = compareAddresses(bestMacAddr, macAddr);
/* 177 */         if (res < 0)
/*     */         {
/* 179 */           replace = true;
/* 180 */         } else if (res == 0)
/*     */         {
/* 182 */           res = compareAddresses(bestInetAddr, inetAddr);
/* 183 */           if (res < 0)
/*     */           {
/* 185 */             replace = true;
/* 186 */           } else if (res == 0)
/*     */           {
/* 188 */             if (bestMacAddr.length < macAddr.length) {
/* 189 */               replace = true;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 194 */         if (replace) {
/* 195 */           bestMacAddr = macAddr;
/* 196 */           bestInetAddr = inetAddr;
/*     */         }
/*     */       }
/*     */     }
/* 200 */     if (bestMacAddr == NOT_FOUND) {
/* 201 */       bestMacAddr = new byte[8];
/* 202 */       ThreadLocalRandom.current().nextBytes(bestMacAddr);
/* 203 */       logger.warn("Failed to find a usable hardware address from the network interfaces; using random bytes: {}", formatAddress(bestMacAddr));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 208 */     switch (bestMacAddr.length) {
/*     */     case 6: 
/* 210 */       byte[] newAddr = new byte[8];
/* 211 */       System.arraycopy(bestMacAddr, 0, newAddr, 0, 3);
/* 212 */       newAddr[3] = -1;
/* 213 */       newAddr[4] = -2;
/* 214 */       System.arraycopy(bestMacAddr, 3, newAddr, 5, 3);
/* 215 */       bestMacAddr = newAddr;
/* 216 */       break;
/*     */     default: 
/* 218 */       bestMacAddr = Arrays.copyOf(bestMacAddr, 8);
/*     */     }
/*     */     
/* 221 */     return bestMacAddr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int compareAddresses(byte[] current, byte[] candidate)
/*     */   {
/* 228 */     if (candidate == null) {
/* 229 */       return 1;
/*     */     }
/*     */     
/*     */ 
/* 233 */     if (candidate.length < 6) {
/* 234 */       return 1;
/*     */     }
/*     */     
/*     */ 
/* 238 */     boolean onlyZeroAndOne = true;
/* 239 */     for (byte b : candidate) {
/* 240 */       if ((b != 0) && (b != 1)) {
/* 241 */         onlyZeroAndOne = false;
/* 242 */         break;
/*     */       }
/*     */     }
/*     */     
/* 246 */     if (onlyZeroAndOne) {
/* 247 */       return 1;
/*     */     }
/*     */     
/*     */ 
/* 251 */     if ((candidate[0] & 0x1) != 0) {
/* 252 */       return 1;
/*     */     }
/*     */     
/*     */ 
/* 256 */     if ((current[0] & 0x2) == 0) {
/* 257 */       if ((candidate[0] & 0x2) == 0)
/*     */       {
/* 259 */         return 0;
/*     */       }
/*     */       
/* 262 */       return 1;
/*     */     }
/*     */     
/* 265 */     if ((candidate[0] & 0x2) == 0)
/*     */     {
/* 267 */       return -1;
/*     */     }
/*     */     
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int compareAddresses(InetAddress current, InetAddress candidate)
/*     */   {
/* 279 */     return scoreAddress(current) - scoreAddress(candidate);
/*     */   }
/*     */   
/*     */   private static int scoreAddress(InetAddress addr) {
/* 283 */     if (addr.isAnyLocalAddress()) {
/* 284 */       return 0;
/*     */     }
/* 286 */     if (addr.isMulticastAddress()) {
/* 287 */       return 1;
/*     */     }
/* 289 */     if (addr.isLinkLocalAddress()) {
/* 290 */       return 2;
/*     */     }
/* 292 */     if (addr.isSiteLocalAddress()) {
/* 293 */       return 3;
/*     */     }
/*     */     
/* 296 */     return 4;
/*     */   }
/*     */   
/*     */   private static String formatAddress(byte[] addr) {
/* 300 */     StringBuilder buf = new StringBuilder(24);
/* 301 */     for (byte b : addr) {
/* 302 */       buf.append(String.format("%02x:", new Object[] { Integer.valueOf(b & 0xFF) }));
/*     */     }
/* 304 */     return buf.substring(0, buf.length() - 1);
/*     */   }
/*     */   
/*     */   private static int defaultProcessId() {
/* 308 */     ClassLoader loader = PlatformDependent.getSystemClassLoader();
/*     */     String value;
/*     */     try
/*     */     {
/* 312 */       Class<?> mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
/* 313 */       Class<?> runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);
/*     */       
/* 315 */       Method getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", EmptyArrays.EMPTY_CLASSES);
/* 316 */       Object bean = getRuntimeMXBean.invoke(null, EmptyArrays.EMPTY_OBJECTS);
/* 317 */       Method getName = runtimeMxBeanType.getDeclaredMethod("getName", EmptyArrays.EMPTY_CLASSES);
/* 318 */       value = (String)getName.invoke(bean, EmptyArrays.EMPTY_OBJECTS);
/*     */     } catch (Exception e) {
/* 320 */       logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", e);
/*     */       try
/*     */       {
/* 323 */         Class<?> processType = Class.forName("android.os.Process", true, loader);
/* 324 */         Method myPid = processType.getMethod("myPid", EmptyArrays.EMPTY_CLASSES);
/* 325 */         value = myPid.invoke(null, EmptyArrays.EMPTY_OBJECTS).toString();
/*     */       } catch (Exception e2) {
/* 327 */         logger.debug("Could not invoke Process.myPid(); not Android?", e2);
/* 328 */         value = "";
/*     */       }
/*     */     }
/*     */     
/* 332 */     int atIndex = value.indexOf('@');
/* 333 */     if (atIndex >= 0) {
/* 334 */       value = value.substring(0, atIndex);
/*     */     }
/*     */     int pid;
/*     */     try
/*     */     {
/* 339 */       pid = Integer.parseInt(value);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 342 */       pid = -1;
/*     */     }
/*     */     
/* 345 */     if ((pid < 0) || (pid > 4194304)) {
/* 346 */       pid = ThreadLocalRandom.current().nextInt(4194305);
/* 347 */       logger.warn("Failed to find the current process ID from '{}'; using a random value: {}", value, Integer.valueOf(pid));
/*     */     }
/*     */     
/* 350 */     return pid;
/*     */   }
/*     */   
/* 353 */   private final byte[] data = new byte[28];
/*     */   private int hashCode;
/*     */   private transient String shortValue;
/*     */   private transient String longValue;
/*     */   
/*     */   private void init()
/*     */   {
/* 360 */     int i = 0;
/*     */     
/*     */ 
/* 363 */     System.arraycopy(MACHINE_ID, 0, this.data, i, 8);
/* 364 */     i += 8;
/*     */     
/*     */ 
/* 367 */     i = writeInt(i, PROCESS_ID);
/*     */     
/*     */ 
/* 370 */     i = writeInt(i, nextSequence.getAndIncrement());
/*     */     
/*     */ 
/* 373 */     i = writeLong(i, Long.reverse(System.nanoTime()) ^ System.currentTimeMillis());
/*     */     
/*     */ 
/* 376 */     int random = ThreadLocalRandom.current().nextInt();
/* 377 */     this.hashCode = random;
/* 378 */     i = writeInt(i, random);
/*     */     
/* 380 */     assert (i == this.data.length);
/*     */   }
/*     */   
/*     */   private int writeInt(int i, int value) {
/* 384 */     this.data[(i++)] = ((byte)(value >>> 24));
/* 385 */     this.data[(i++)] = ((byte)(value >>> 16));
/* 386 */     this.data[(i++)] = ((byte)(value >>> 8));
/* 387 */     this.data[(i++)] = ((byte)value);
/* 388 */     return i;
/*     */   }
/*     */   
/*     */   private int writeLong(int i, long value) {
/* 392 */     this.data[(i++)] = ((byte)(int)(value >>> 56));
/* 393 */     this.data[(i++)] = ((byte)(int)(value >>> 48));
/* 394 */     this.data[(i++)] = ((byte)(int)(value >>> 40));
/* 395 */     this.data[(i++)] = ((byte)(int)(value >>> 32));
/* 396 */     this.data[(i++)] = ((byte)(int)(value >>> 24));
/* 397 */     this.data[(i++)] = ((byte)(int)(value >>> 16));
/* 398 */     this.data[(i++)] = ((byte)(int)(value >>> 8));
/* 399 */     this.data[(i++)] = ((byte)(int)value);
/* 400 */     return i;
/*     */   }
/*     */   
/*     */   public String asShortText()
/*     */   {
/* 405 */     String shortValue = this.shortValue;
/* 406 */     if (shortValue == null) {
/* 407 */       this.shortValue = (shortValue = ByteBufUtil.hexDump(this.data, 24, 4));
/*     */     }
/*     */     
/* 410 */     return shortValue;
/*     */   }
/*     */   
/*     */   public String asLongText()
/*     */   {
/* 415 */     String longValue = this.longValue;
/* 416 */     if (longValue == null) {
/* 417 */       this.longValue = (longValue = newLongValue());
/*     */     }
/* 419 */     return longValue;
/*     */   }
/*     */   
/*     */   private String newLongValue() {
/* 423 */     StringBuilder buf = new StringBuilder(2 * this.data.length + 5);
/* 424 */     int i = 0;
/* 425 */     i = appendHexDumpField(buf, i, 8);
/* 426 */     i = appendHexDumpField(buf, i, 4);
/* 427 */     i = appendHexDumpField(buf, i, 4);
/* 428 */     i = appendHexDumpField(buf, i, 8);
/* 429 */     i = appendHexDumpField(buf, i, 4);
/* 430 */     assert (i == this.data.length);
/* 431 */     return buf.substring(0, buf.length() - 1);
/*     */   }
/*     */   
/*     */   private int appendHexDumpField(StringBuilder buf, int i, int length) {
/* 435 */     buf.append(ByteBufUtil.hexDump(this.data, i, length));
/* 436 */     buf.append('-');
/* 437 */     i += length;
/* 438 */     return i;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 443 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public int compareTo(ChannelId o)
/*     */   {
/* 448 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 453 */     if (obj == this) {
/* 454 */       return true;
/*     */     }
/*     */     
/* 457 */     if (!(obj instanceof DefaultChannelId)) {
/* 458 */       return false;
/*     */     }
/*     */     
/* 461 */     return Arrays.equals(this.data, ((DefaultChannelId)obj).data);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 466 */     return asShortText();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultChannelId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */