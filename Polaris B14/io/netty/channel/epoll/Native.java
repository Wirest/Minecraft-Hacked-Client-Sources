/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.unix.DomainSocketAddress;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ 
/*     */ final class Native
/*     */ {
/*     */   public static final int EPOLLIN;
/*     */   public static final int EPOLLOUT;
/*     */   public static final int EPOLLRDHUP;
/*     */   public static final int EPOLLET;
/*     */   public static final int IOV_MAX;
/*     */   public static final int UIO_MAX_IOV;
/*     */   public static final boolean IS_SUPPORTING_SENDMMSG;
/*     */   private static final byte[] IPV4_MAPPED_IPV6_PREFIX;
/*     */   private static final int ERRNO_EBADF_NEGATIVE;
/*     */   private static final int ERRNO_EPIPE_NEGATIVE;
/*     */   private static final int ERRNO_ECONNRESET_NEGATIVE;
/*     */   private static final int ERRNO_EAGAIN_NEGATIVE;
/*     */   private static final int ERRNO_EWOULDBLOCK_NEGATIVE;
/*     */   private static final int ERRNO_EINPROGRESS_NEGATIVE;
/*     */   private static final String[] ERRORS;
/*     */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_WRITE;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_WRITEV;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_READ;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_SENDFILE;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_SENDTO;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_SENDMSG;
/*     */   private static final IOException CONNECTION_RESET_EXCEPTION_SENDMMSG;
/*     */   
/*     */   static
/*     */   {
/*  45 */     String name = SystemPropertyUtil.get("os.name").toLowerCase(java.util.Locale.UK).trim();
/*  46 */     if (!name.startsWith("linux")) {
/*  47 */       throw new IllegalStateException("Only supported on Linux");
/*     */     }
/*  49 */     io.netty.util.internal.NativeLibraryLoader.load("netty-transport-native-epoll", io.netty.util.internal.PlatformDependent.getClassLoader(Native.class));
/*     */     
/*     */ 
/*     */ 
/*  53 */     EPOLLIN = epollin();
/*  54 */     EPOLLOUT = epollout();
/*  55 */     EPOLLRDHUP = epollrdhup();
/*  56 */     EPOLLET = epollet();
/*     */     
/*  58 */     IOV_MAX = iovMax();
/*  59 */     UIO_MAX_IOV = uioMaxIov();
/*  60 */     IS_SUPPORTING_SENDMMSG = isSupportingSendmmsg();
/*     */     
/*  62 */     IPV4_MAPPED_IPV6_PREFIX = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 };
/*     */     
/*     */ 
/*     */ 
/*  66 */     ERRNO_EBADF_NEGATIVE = -errnoEBADF();
/*  67 */     ERRNO_EPIPE_NEGATIVE = -errnoEPIPE();
/*  68 */     ERRNO_ECONNRESET_NEGATIVE = -errnoECONNRESET();
/*  69 */     ERRNO_EAGAIN_NEGATIVE = -errnoEAGAIN();
/*  70 */     ERRNO_EWOULDBLOCK_NEGATIVE = -errnoEWOULDBLOCK();
/*  71 */     ERRNO_EINPROGRESS_NEGATIVE = -errnoEINPROGRESS();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */     ERRORS = new String['Ѐ'];
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
/*  94 */     for (int i = 0; i < ERRORS.length; i++)
/*     */     {
/*  96 */       ERRORS[i] = strError(i);
/*     */     }
/*     */     
/*  99 */     CONNECTION_RESET_EXCEPTION_READ = newConnectionResetException("syscall:read(...)", ERRNO_ECONNRESET_NEGATIVE);
/*     */     
/* 101 */     CONNECTION_RESET_EXCEPTION_WRITE = newConnectionResetException("syscall:write(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 103 */     CONNECTION_RESET_EXCEPTION_WRITEV = newConnectionResetException("syscall:writev(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 105 */     CONNECTION_RESET_EXCEPTION_SENDFILE = newConnectionResetException("syscall:sendfile(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 107 */     CONNECTION_RESET_EXCEPTION_SENDTO = newConnectionResetException("syscall:sendto(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 109 */     CONNECTION_RESET_EXCEPTION_SENDMSG = newConnectionResetException("syscall:sendmsg(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 111 */     CONNECTION_RESET_EXCEPTION_SENDMMSG = newConnectionResetException("syscall:sendmmsg(...)", ERRNO_EPIPE_NEGATIVE);
/*     */     
/* 113 */     CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
/* 114 */     CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*     */   }
/*     */   
/*     */   private static IOException newConnectionResetException(String method, int errnoNegative) {
/* 118 */     IOException exception = newIOException(method, errnoNegative);
/* 119 */     exception.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/* 120 */     return exception;
/*     */   }
/*     */   
/*     */   private static IOException newIOException(String method, int err) {
/* 124 */     return new IOException(method + "() failed: " + ERRORS[(-err)]);
/*     */   }
/*     */   
/*     */   private static int ioResult(String method, int err, IOException resetCause) throws IOException
/*     */   {
/* 129 */     if ((err == ERRNO_EAGAIN_NEGATIVE) || (err == ERRNO_EWOULDBLOCK_NEGATIVE)) {
/* 130 */       return 0;
/*     */     }
/* 132 */     if ((err == ERRNO_EPIPE_NEGATIVE) || (err == ERRNO_ECONNRESET_NEGATIVE)) {
/* 133 */       throw resetCause;
/*     */     }
/* 135 */     if (err == ERRNO_EBADF_NEGATIVE) {
/* 136 */       throw CLOSED_CHANNEL_EXCEPTION;
/*     */     }
/*     */     
/*     */ 
/* 140 */     throw newIOException(method, err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int epollWait(int efd, EpollEventArray events, int timeout)
/*     */     throws IOException
/*     */   {
/* 148 */     int ready = epollWait0(efd, events.memoryAddress(), events.length(), timeout);
/* 149 */     if (ready < 0) {
/* 150 */       throw newIOException("epoll_wait", ready);
/*     */     }
/* 152 */     return ready;
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
/*     */ 
/*     */ 
/*     */   public static void close(int fd)
/*     */     throws IOException
/*     */   {
/* 172 */     int res = close0(fd);
/* 173 */     if (res < 0) {
/* 174 */       throw newIOException("close", res);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int write(int fd, ByteBuffer buf, int pos, int limit)
/*     */     throws IOException
/*     */   {
/* 181 */     int res = write0(fd, buf, pos, limit);
/* 182 */     if (res >= 0) {
/* 183 */       return res;
/*     */     }
/* 185 */     return ioResult("write", res, CONNECTION_RESET_EXCEPTION_WRITE);
/*     */   }
/*     */   
/*     */   public static int writeAddress(int fd, long address, int pos, int limit)
/*     */     throws IOException
/*     */   {
/* 191 */     int res = writeAddress0(fd, address, pos, limit);
/* 192 */     if (res >= 0) {
/* 193 */       return res;
/*     */     }
/* 195 */     return ioResult("writeAddress", res, CONNECTION_RESET_EXCEPTION_WRITE);
/*     */   }
/*     */   
/*     */   public static long writev(int fd, ByteBuffer[] buffers, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 201 */     long res = writev0(fd, buffers, offset, length);
/* 202 */     if (res >= 0L) {
/* 203 */       return res;
/*     */     }
/* 205 */     return ioResult("writev", (int)res, CONNECTION_RESET_EXCEPTION_WRITEV);
/*     */   }
/*     */   
/*     */ 
/*     */   public static long writevAddresses(int fd, long memoryAddress, int length)
/*     */     throws IOException
/*     */   {
/* 212 */     long res = writevAddresses0(fd, memoryAddress, length);
/* 213 */     if (res >= 0L) {
/* 214 */       return res;
/*     */     }
/* 216 */     return ioResult("writevAddresses", (int)res, CONNECTION_RESET_EXCEPTION_WRITEV);
/*     */   }
/*     */   
/*     */   public static int read(int fd, ByteBuffer buf, int pos, int limit)
/*     */     throws IOException
/*     */   {
/* 222 */     int res = read0(fd, buf, pos, limit);
/* 223 */     if (res > 0) {
/* 224 */       return res;
/*     */     }
/* 226 */     if (res == 0) {
/* 227 */       return -1;
/*     */     }
/* 229 */     return ioResult("read", res, CONNECTION_RESET_EXCEPTION_READ);
/*     */   }
/*     */   
/*     */   public static int readAddress(int fd, long address, int pos, int limit)
/*     */     throws IOException
/*     */   {
/* 235 */     int res = readAddress0(fd, address, pos, limit);
/* 236 */     if (res > 0) {
/* 237 */       return res;
/*     */     }
/* 239 */     if (res == 0) {
/* 240 */       return -1;
/*     */     }
/* 242 */     return ioResult("readAddress", res, CONNECTION_RESET_EXCEPTION_READ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long sendfile(int dest, DefaultFileRegion src, long baseOffset, long offset, long length)
/*     */     throws IOException
/*     */   {
/* 251 */     src.open();
/*     */     
/* 253 */     long res = sendfile0(dest, src, baseOffset, offset, length);
/* 254 */     if (res >= 0L) {
/* 255 */       return res;
/*     */     }
/* 257 */     return ioResult("sendfile", (int)res, CONNECTION_RESET_EXCEPTION_SENDFILE);
/*     */   }
/*     */   
/*     */ 
/*     */   public static int sendTo(int fd, ByteBuffer buf, int pos, int limit, InetAddress addr, int port)
/*     */     throws IOException
/*     */   {
/*     */     int scopeId;
/*     */     
/*     */     int scopeId;
/*     */     
/*     */     byte[] address;
/* 269 */     if ((addr instanceof Inet6Address)) {
/* 270 */       byte[] address = addr.getAddress();
/* 271 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     }
/*     */     else {
/* 274 */       scopeId = 0;
/* 275 */       address = ipv4MappedIpv6Address(addr.getAddress());
/*     */     }
/* 277 */     int res = sendTo0(fd, buf, pos, limit, address, scopeId, port);
/* 278 */     if (res >= 0) {
/* 279 */       return res;
/*     */     }
/* 281 */     return ioResult("sendTo", res, CONNECTION_RESET_EXCEPTION_SENDTO);
/*     */   }
/*     */   
/*     */ 
/*     */   public static int sendToAddress(int fd, long memoryAddress, int pos, int limit, InetAddress addr, int port)
/*     */     throws IOException
/*     */   {
/*     */     int scopeId;
/*     */     
/*     */     int scopeId;
/*     */     
/*     */     byte[] address;
/* 293 */     if ((addr instanceof Inet6Address)) {
/* 294 */       byte[] address = addr.getAddress();
/* 295 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     }
/*     */     else {
/* 298 */       scopeId = 0;
/* 299 */       address = ipv4MappedIpv6Address(addr.getAddress());
/*     */     }
/* 301 */     int res = sendToAddress0(fd, memoryAddress, pos, limit, address, scopeId, port);
/* 302 */     if (res >= 0) {
/* 303 */       return res;
/*     */     }
/* 305 */     return ioResult("sendToAddress", res, CONNECTION_RESET_EXCEPTION_SENDTO);
/*     */   }
/*     */   
/*     */ 
/*     */   public static int sendToAddresses(int fd, long memoryAddress, int length, InetAddress addr, int port)
/*     */     throws IOException
/*     */   {
/*     */     int scopeId;
/*     */     
/*     */     int scopeId;
/*     */     
/*     */     byte[] address;
/* 317 */     if ((addr instanceof Inet6Address)) {
/* 318 */       byte[] address = addr.getAddress();
/* 319 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     }
/*     */     else {
/* 322 */       scopeId = 0;
/* 323 */       address = ipv4MappedIpv6Address(addr.getAddress());
/*     */     }
/* 325 */     int res = sendToAddresses(fd, memoryAddress, length, address, scopeId, port);
/* 326 */     if (res >= 0) {
/* 327 */       return res;
/*     */     }
/* 329 */     return ioResult("sendToAddresses", res, CONNECTION_RESET_EXCEPTION_SENDMSG);
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
/*     */   public static int sendmmsg(int fd, NativeDatagramPacketArray.NativeDatagramPacket[] msgs, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 343 */     int res = sendmmsg0(fd, msgs, offset, len);
/* 344 */     if (res >= 0) {
/* 345 */       return res;
/*     */     }
/* 347 */     return ioResult("sendmmsg", res, CONNECTION_RESET_EXCEPTION_SENDMMSG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int socketStreamFd()
/*     */   {
/* 357 */     int res = socketStream();
/* 358 */     if (res < 0) {
/* 359 */       throw new ChannelException(newIOException("socketStreamFd", res));
/*     */     }
/* 361 */     return res;
/*     */   }
/*     */   
/*     */   public static int socketDgramFd() {
/* 365 */     int res = socketDgram();
/* 366 */     if (res < 0) {
/* 367 */       throw new ChannelException(newIOException("socketDgramFd", res));
/*     */     }
/* 369 */     return res;
/*     */   }
/*     */   
/*     */   public static int socketDomainFd() {
/* 373 */     int res = socketDomain();
/* 374 */     if (res < 0) {
/* 375 */       throw new ChannelException(newIOException("socketDomain", res));
/*     */     }
/* 377 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void bind(int fd, SocketAddress socketAddress)
/*     */     throws IOException
/*     */   {
/* 385 */     if ((socketAddress instanceof InetSocketAddress)) {
/* 386 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 387 */       NativeInetAddress address = toNativeInetAddress(addr.getAddress());
/* 388 */       int res = bind(fd, address.address, address.scopeId, addr.getPort());
/* 389 */       if (res < 0) {
/* 390 */         throw newIOException("bind", res);
/*     */       }
/* 392 */     } else if ((socketAddress instanceof DomainSocketAddress)) {
/* 393 */       DomainSocketAddress addr = (DomainSocketAddress)socketAddress;
/* 394 */       int res = bindDomainSocket(fd, addr.path());
/* 395 */       if (res < 0) {
/* 396 */         throw newIOException("bind", res);
/*     */       }
/*     */     } else {
/* 399 */       throw new Error("Unexpected SocketAddress implementation " + socketAddress);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void listen(int fd, int backlog)
/*     */     throws IOException
/*     */   {
/* 407 */     int res = listen0(fd, backlog);
/* 408 */     if (res < 0) {
/* 409 */       throw newIOException("listen", res);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean connect(int fd, SocketAddress socketAddress)
/*     */     throws IOException
/*     */   {
/*     */     int res;
/* 417 */     if ((socketAddress instanceof InetSocketAddress)) {
/* 418 */       InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
/* 419 */       NativeInetAddress address = toNativeInetAddress(inetSocketAddress.getAddress());
/* 420 */       res = connect(fd, address.address, address.scopeId, inetSocketAddress.getPort()); } else { int res;
/* 421 */       if ((socketAddress instanceof DomainSocketAddress)) {
/* 422 */         DomainSocketAddress unixDomainSocketAddress = (DomainSocketAddress)socketAddress;
/* 423 */         res = connectDomainSocket(fd, unixDomainSocketAddress.path());
/*     */       } else {
/* 425 */         throw new Error("Unexpected SocketAddress implementation " + socketAddress); } }
/*     */     int res;
/* 427 */     if (res < 0) {
/* 428 */       if (res == ERRNO_EINPROGRESS_NEGATIVE)
/*     */       {
/* 430 */         return false;
/*     */       }
/* 432 */       throw newIOException("connect", res);
/*     */     }
/* 434 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean finishConnect(int fd)
/*     */     throws IOException
/*     */   {
/* 441 */     int res = finishConnect0(fd);
/* 442 */     if (res < 0) {
/* 443 */       if (res == ERRNO_EINPROGRESS_NEGATIVE)
/*     */       {
/* 445 */         return false;
/*     */       }
/* 447 */       throw newIOException("finishConnect", res);
/*     */     }
/* 449 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static InetSocketAddress remoteAddress(int fd)
/*     */   {
/* 455 */     byte[] addr = remoteAddress0(fd);
/*     */     
/*     */ 
/* 458 */     if (addr == null) {
/* 459 */       return null;
/*     */     }
/* 461 */     return address(addr, 0, addr.length);
/*     */   }
/*     */   
/*     */   public static InetSocketAddress localAddress(int fd) {
/* 465 */     byte[] addr = localAddress0(fd);
/*     */     
/*     */ 
/* 468 */     if (addr == null) {
/* 469 */       return null;
/*     */     }
/* 471 */     return address(addr, 0, addr.length);
/*     */   }
/*     */   
/*     */   static InetSocketAddress address(byte[] addr, int offset, int len)
/*     */   {
/* 476 */     int port = decodeInt(addr, offset + len - 4);
/*     */     try
/*     */     {
/*     */       InetAddress address;
/* 480 */       switch (len)
/*     */       {
/*     */ 
/*     */ 
/*     */       case 8: 
/* 485 */         byte[] ipv4 = new byte[4];
/* 486 */         System.arraycopy(addr, offset, ipv4, 0, 4);
/* 487 */         address = InetAddress.getByAddress(ipv4);
/* 488 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case 24: 
/* 495 */         byte[] ipv6 = new byte[16];
/* 496 */         System.arraycopy(addr, offset, ipv6, 0, 16);
/* 497 */         int scopeId = decodeInt(addr, offset + len - 8);
/* 498 */         address = Inet6Address.getByAddress(null, ipv6, scopeId);
/* 499 */         break;
/*     */       default: 
/* 501 */         throw new Error(); }
/*     */       
/* 503 */       return new InetSocketAddress(address, port);
/*     */     } catch (UnknownHostException e) {
/* 505 */       throw new Error("Should never happen", e);
/*     */     }
/*     */   }
/*     */   
/*     */   static int decodeInt(byte[] addr, int index) {
/* 510 */     return (addr[index] & 0xFF) << 24 | (addr[(index + 1)] & 0xFF) << 16 | (addr[(index + 2)] & 0xFF) << 8 | addr[(index + 3)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int accept(int fd, byte[] addr)
/*     */     throws IOException
/*     */   {
/* 520 */     int res = accept0(fd, addr);
/* 521 */     if (res >= 0) {
/* 522 */       return res;
/*     */     }
/* 524 */     if ((res == ERRNO_EAGAIN_NEGATIVE) || (res == ERRNO_EWOULDBLOCK_NEGATIVE))
/*     */     {
/* 526 */       return -1;
/*     */     }
/* 528 */     throw newIOException("accept", res);
/*     */   }
/*     */   
/*     */   public static int recvFd(int fd)
/*     */     throws IOException
/*     */   {
/* 534 */     int res = recvFd0(fd);
/* 535 */     if (res > 0) {
/* 536 */       return res;
/*     */     }
/* 538 */     if (res == 0) {
/* 539 */       return -1;
/*     */     }
/*     */     
/* 542 */     if ((res == ERRNO_EAGAIN_NEGATIVE) || (res == ERRNO_EWOULDBLOCK_NEGATIVE))
/*     */     {
/* 544 */       return 0;
/*     */     }
/* 546 */     throw newIOException("recvFd", res);
/*     */   }
/*     */   
/*     */   public static int sendFd(int socketFd, int fd)
/*     */     throws IOException
/*     */   {
/* 552 */     int res = sendFd0(socketFd, fd);
/* 553 */     if (res >= 0) {
/* 554 */       return res;
/*     */     }
/* 556 */     if ((res == ERRNO_EAGAIN_NEGATIVE) || (res == ERRNO_EWOULDBLOCK_NEGATIVE))
/*     */     {
/* 558 */       return -1;
/*     */     }
/* 560 */     throw newIOException("sendFd", res);
/*     */   }
/*     */   
/*     */   public static void shutdown(int fd, boolean read, boolean write)
/*     */     throws IOException
/*     */   {
/* 566 */     int res = shutdown0(fd, read, write);
/* 567 */     if (res < 0) {
/* 568 */       throw newIOException("shutdown", res);
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
/*     */   public static void tcpInfo(int fd, EpollTcpInfo info)
/*     */   {
/* 605 */     tcpInfo0(fd, info.info);
/*     */   }
/*     */   
/*     */ 
/*     */   private static NativeInetAddress toNativeInetAddress(InetAddress addr)
/*     */   {
/* 611 */     byte[] bytes = addr.getAddress();
/* 612 */     if ((addr instanceof Inet6Address)) {
/* 613 */       return new NativeInetAddress(bytes, ((Inet6Address)addr).getScopeId());
/*     */     }
/*     */     
/* 616 */     return new NativeInetAddress(ipv4MappedIpv6Address(bytes));
/*     */   }
/*     */   
/*     */   static byte[] ipv4MappedIpv6Address(byte[] ipv4)
/*     */   {
/* 621 */     byte[] address = new byte[16];
/* 622 */     System.arraycopy(IPV4_MAPPED_IPV6_PREFIX, 0, address, 0, IPV4_MAPPED_IPV6_PREFIX.length);
/* 623 */     System.arraycopy(ipv4, 0, address, 12, ipv4.length);
/* 624 */     return address;
/*     */   }
/*     */   
/*     */   public static native int eventFd();
/*     */   
/*     */   public static native void eventFdWrite(int paramInt, long paramLong);
/*     */   
/*     */   private static class NativeInetAddress {
/* 632 */     NativeInetAddress(byte[] address, int scopeId) { this.address = address;
/* 633 */       this.scopeId = scopeId; }
/*     */     
/*     */     final byte[] address;
/*     */     final int scopeId;
/* 637 */     NativeInetAddress(byte[] address) { this(address, 0); }
/*     */   }
/*     */   
/*     */   public static native void eventFdRead(int paramInt);
/*     */   
/*     */   public static native int epollCreate();
/*     */   
/*     */   private static native int epollWait0(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */   
/*     */   public static native void epollCtlAdd(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public static native void epollCtlMod(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public static native void epollCtlDel(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int errnoEBADF();
/*     */   
/*     */   private static native int errnoEPIPE();
/*     */   
/*     */   private static native int errnoECONNRESET();
/*     */   
/*     */   private static native int errnoEAGAIN();
/*     */   
/*     */   private static native int errnoEWOULDBLOCK();
/*     */   
/*     */   private static native int errnoEINPROGRESS();
/*     */   
/*     */   private static native String strError(int paramInt);
/*     */   
/*     */   private static native int close0(int paramInt);
/*     */   
/*     */   private static native int write0(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int writeAddress0(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native long writev0(int paramInt1, ByteBuffer[] paramArrayOfByteBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native long writevAddresses0(int paramInt1, long paramLong, int paramInt2);
/*     */   
/*     */   private static native int read0(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int readAddress0(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native long sendfile0(int paramInt, DefaultFileRegion paramDefaultFileRegion, long paramLong1, long paramLong2, long paramLong3)
/*     */     throws IOException;
/*     */   
/*     */   private static native int sendTo0(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native int sendToAddress0(int paramInt1, long paramLong, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native int sendToAddresses(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4);
/*     */   
/*     */   public static native EpollDatagramChannel.DatagramSocketAddress recvFrom(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */   
/*     */   public static native EpollDatagramChannel.DatagramSocketAddress recvFromAddress(int paramInt1, long paramLong, int paramInt2, int paramInt3)
/*     */     throws IOException;
/*     */   
/*     */   private static native int sendmmsg0(int paramInt1, NativeDatagramPacketArray.NativeDatagramPacket[] paramArrayOfNativeDatagramPacket, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native boolean isSupportingSendmmsg();
/*     */   
/*     */   private static native int socketStream();
/*     */   
/*     */   private static native int socketDgram();
/*     */   
/*     */   private static native int socketDomain();
/*     */   
/*     */   private static native int bind(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int bindDomainSocket(int paramInt, String paramString);
/*     */   
/*     */   private static native int listen0(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int connect(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int connectDomainSocket(int paramInt, String paramString);
/*     */   
/*     */   private static native int finishConnect0(int paramInt);
/*     */   
/*     */   private static native byte[] remoteAddress0(int paramInt);
/*     */   
/*     */   private static native byte[] localAddress0(int paramInt);
/*     */   
/*     */   private static native int accept0(int paramInt, byte[] paramArrayOfByte);
/*     */   
/*     */   private static native int recvFd0(int paramInt);
/*     */   
/*     */   private static native int sendFd0(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int shutdown0(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public static native int getReceiveBufferSize(int paramInt);
/*     */   
/*     */   public static native int getSendBufferSize(int paramInt);
/*     */   
/*     */   public static native int isKeepAlive(int paramInt);
/*     */   
/*     */   public static native int isReuseAddress(int paramInt);
/*     */   
/*     */   public static native int isReusePort(int paramInt);
/*     */   
/*     */   public static native int isTcpNoDelay(int paramInt);
/*     */   
/*     */   public static native int isTcpCork(int paramInt);
/*     */   
/*     */   public static native int getSoLinger(int paramInt);
/*     */   
/*     */   public static native int getTrafficClass(int paramInt);
/*     */   
/*     */   public static native int isBroadcast(int paramInt);
/*     */   
/*     */   public static native int getTcpKeepIdle(int paramInt);
/*     */   
/*     */   public static native int getTcpKeepIntvl(int paramInt);
/*     */   
/*     */   public static native int getTcpKeepCnt(int paramInt);
/*     */   
/*     */   public static native int getSoError(int paramInt);
/*     */   
/*     */   public static native void setKeepAlive(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setReceiveBufferSize(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setReuseAddress(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setReusePort(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setSendBufferSize(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTcpNoDelay(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTcpCork(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setSoLinger(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTrafficClass(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setBroadcast(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTcpKeepIdle(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTcpKeepIntvl(int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setTcpKeepCnt(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native void tcpInfo0(int paramInt, int[] paramArrayOfInt);
/*     */   
/*     */   public static native String kernelVersion();
/*     */   
/*     */   private static native int iovMax();
/*     */   
/*     */   private static native int uioMaxIov();
/*     */   
/*     */   public static native int sizeofEpollEvent();
/*     */   
/*     */   public static native int offsetofEpollData();
/*     */   
/*     */   private static native int epollin();
/*     */   
/*     */   private static native int epollout();
/*     */   
/*     */   private static native int epollrdhup();
/*     */   
/*     */   private static native int epollet();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */