/*      */ package io.netty.util;
/*      */ 
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.NetworkInterface;
/*      */ import java.net.SocketException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class NetUtil
/*      */ {
/*      */   public static final Inet4Address LOCALHOST4;
/*      */   public static final Inet6Address LOCALHOST6;
/*      */   public static final InetAddress LOCALHOST;
/*      */   public static final NetworkInterface LOOPBACK_IF;
/*      */   public static final int SOMAXCONN;
/*      */   private static final int IPV6_WORD_COUNT = 8;
/*      */   private static final int IPV6_MAX_CHAR_COUNT = 39;
/*      */   private static final int IPV6_BYTE_COUNT = 16;
/*      */   private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;
/*      */   private static final int IPV6_MIN_SEPARATORS = 2;
/*      */   private static final int IPV6_MAX_SEPARATORS = 8;
/*      */   private static final int IPV4_BYTE_COUNT = 4;
/*      */   private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
/*      */   private static final int IPV4_SEPARATORS = 3;
/*  121 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetUtil.class);
/*      */   
/*      */   static {
/*  124 */     byte[] LOCALHOST4_BYTES = { Byte.MAX_VALUE, 0, 0, 1 };
/*  125 */     byte[] LOCALHOST6_BYTES = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
/*      */     
/*      */ 
/*  128 */     Inet4Address localhost4 = null;
/*      */     try {
/*  130 */       localhost4 = (Inet4Address)InetAddress.getByAddress(LOCALHOST4_BYTES);
/*      */     }
/*      */     catch (Exception e) {
/*  133 */       PlatformDependent.throwException(e);
/*      */     }
/*  135 */     LOCALHOST4 = localhost4;
/*      */     
/*      */ 
/*  138 */     Inet6Address localhost6 = null;
/*      */     try {
/*  140 */       localhost6 = (Inet6Address)InetAddress.getByAddress(LOCALHOST6_BYTES);
/*      */     }
/*      */     catch (Exception e) {
/*  143 */       PlatformDependent.throwException(e);
/*      */     }
/*  145 */     LOCALHOST6 = localhost6;
/*      */     
/*      */ 
/*  148 */     List<NetworkInterface> ifaces = new ArrayList();
/*      */     try {
/*  150 */       for (i = NetworkInterface.getNetworkInterfaces(); i.hasMoreElements();) {
/*  151 */         NetworkInterface iface = (NetworkInterface)i.nextElement();
/*      */         
/*  153 */         if (iface.getInetAddresses().hasMoreElements())
/*  154 */           ifaces.add(iface);
/*      */       }
/*      */     } catch (SocketException e) {
/*      */       Enumeration<NetworkInterface> i;
/*  158 */       logger.warn("Failed to retrieve the list of available network interfaces", e);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  164 */     NetworkInterface loopbackIface = null;
/*  165 */     InetAddress loopbackAddr = null;
/*  166 */     for (Iterator i$ = ifaces.iterator(); i$.hasNext();) { iface = (NetworkInterface)i$.next();
/*  167 */       for (i = iface.getInetAddresses(); i.hasMoreElements();) {
/*  168 */         InetAddress addr = (InetAddress)i.nextElement();
/*  169 */         if (addr.isLoopbackAddress())
/*      */         {
/*  171 */           loopbackIface = iface;
/*  172 */           loopbackAddr = addr;
/*      */           break label325;
/*      */         }
/*      */       } }
/*      */     NetworkInterface iface;
/*      */     Enumeration<InetAddress> i;
/*      */     label325:
/*  179 */     if (loopbackIface == null) {
/*      */       try {
/*  181 */         for (NetworkInterface iface : ifaces) {
/*  182 */           if (iface.isLoopback()) {
/*  183 */             Enumeration<InetAddress> i = iface.getInetAddresses();
/*  184 */             if (i.hasMoreElements())
/*      */             {
/*  186 */               loopbackIface = iface;
/*  187 */               loopbackAddr = (InetAddress)i.nextElement();
/*  188 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  193 */         if (loopbackIface == null) {
/*  194 */           logger.warn("Failed to find the loopback interface");
/*      */         }
/*      */       } catch (SocketException e) {
/*  197 */         logger.warn("Failed to find the loopback interface", e);
/*      */       }
/*      */     }
/*      */     
/*  201 */     if (loopbackIface != null)
/*      */     {
/*  203 */       logger.debug("Loopback interface: {} ({}, {})", new Object[] { loopbackIface.getName(), loopbackIface.getDisplayName(), loopbackAddr.getHostAddress() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  209 */     else if (loopbackAddr == null) {
/*      */       try {
/*  211 */         if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
/*  212 */           logger.debug("Using hard-coded IPv6 localhost address: {}", localhost6);
/*  213 */           loopbackAddr = localhost6;
/*      */         }
/*      */       }
/*      */       catch (Exception e) {}finally
/*      */       {
/*  218 */         if (loopbackAddr == null) {
/*  219 */           logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
/*  220 */           loopbackAddr = localhost4;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  226 */     LOOPBACK_IF = loopbackIface;
/*  227 */     LOCALHOST = loopbackAddr;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */     int somaxconn = PlatformDependent.isWindows() ? 200 : 128;
/*  234 */     File file = new File("/proc/sys/net/core/somaxconn");
/*  235 */     if (file.exists()) {
/*  236 */       BufferedReader in = null;
/*      */       try {
/*  238 */         in = new BufferedReader(new FileReader(file));
/*  239 */         somaxconn = Integer.parseInt(in.readLine());
/*  240 */         if (logger.isDebugEnabled()) {
/*  241 */           logger.debug("{}: {}", file, Integer.valueOf(somaxconn));
/*      */         }
/*      */       } catch (Exception e) {
/*  244 */         logger.debug("Failed to get SOMAXCONN from: {}", file, e);
/*      */       } finally {
/*  246 */         if (in != null) {
/*      */           try {
/*  248 */             in.close();
/*      */           }
/*      */           catch (Exception e) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  255 */     if (logger.isDebugEnabled()) {
/*  256 */       logger.debug("{}: {} (non-existent)", file, Integer.valueOf(somaxconn));
/*      */     }
/*      */     
/*      */ 
/*  260 */     SOMAXCONN = somaxconn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] createByteArrayFromIpAddressString(String ipAddressString)
/*      */   {
/*  269 */     if (isValidIpV4Address(ipAddressString)) {
/*  270 */       StringTokenizer tokenizer = new StringTokenizer(ipAddressString, ".");
/*      */       
/*      */ 
/*  273 */       byte[] byteAddress = new byte[4];
/*  274 */       for (int i = 0; i < 4; i++) {
/*  275 */         String token = tokenizer.nextToken();
/*  276 */         int tempInt = Integer.parseInt(token);
/*  277 */         byteAddress[i] = ((byte)tempInt);
/*      */       }
/*      */       
/*  280 */       return byteAddress;
/*      */     }
/*      */     
/*  283 */     if (isValidIpV6Address(ipAddressString)) {
/*  284 */       if (ipAddressString.charAt(0) == '[') {
/*  285 */         ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
/*      */       }
/*      */       
/*  288 */       int percentPos = ipAddressString.indexOf('%');
/*  289 */       if (percentPos >= 0) {
/*  290 */         ipAddressString = ipAddressString.substring(0, percentPos);
/*      */       }
/*      */       
/*  293 */       StringTokenizer tokenizer = new StringTokenizer(ipAddressString, ":.", true);
/*  294 */       ArrayList<String> hexStrings = new ArrayList();
/*  295 */       ArrayList<String> decStrings = new ArrayList();
/*  296 */       String token = "";
/*  297 */       String prevToken = "";
/*  298 */       int doubleColonIndex = -1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  306 */       while (tokenizer.hasMoreTokens()) {
/*  307 */         prevToken = token;
/*  308 */         token = tokenizer.nextToken();
/*      */         
/*  310 */         if (":".equals(token)) {
/*  311 */           if (":".equals(prevToken)) {
/*  312 */             doubleColonIndex = hexStrings.size();
/*  313 */           } else if (!prevToken.isEmpty()) {
/*  314 */             hexStrings.add(prevToken);
/*      */           }
/*  316 */         } else if (".".equals(token)) {
/*  317 */           decStrings.add(prevToken);
/*      */         }
/*      */       }
/*      */       
/*  321 */       if (":".equals(prevToken)) {
/*  322 */         if (":".equals(token)) {
/*  323 */           doubleColonIndex = hexStrings.size();
/*      */         } else {
/*  325 */           hexStrings.add(token);
/*      */         }
/*  327 */       } else if (".".equals(prevToken)) {
/*  328 */         decStrings.add(token);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  333 */       int hexStringsLength = 8;
/*      */       
/*      */ 
/*      */ 
/*  337 */       if (!decStrings.isEmpty()) {
/*  338 */         hexStringsLength -= 2;
/*      */       }
/*      */       
/*      */ 
/*  342 */       if (doubleColonIndex != -1) {
/*  343 */         int numberToInsert = hexStringsLength - hexStrings.size();
/*  344 */         for (int i = 0; i < numberToInsert; i++) {
/*  345 */           hexStrings.add(doubleColonIndex, "0");
/*      */         }
/*      */       }
/*      */       
/*  349 */       byte[] ipByteArray = new byte[16];
/*      */       
/*      */ 
/*  352 */       for (int i = 0; i < hexStrings.size(); i++) {
/*  353 */         convertToBytes((String)hexStrings.get(i), ipByteArray, i << 1);
/*      */       }
/*      */       
/*      */ 
/*  357 */       for (int i = 0; i < decStrings.size(); i++) {
/*  358 */         ipByteArray[(i + 12)] = ((byte)(Integer.parseInt((String)decStrings.get(i)) & 0xFF));
/*      */       }
/*  360 */       return ipByteArray;
/*      */     }
/*  362 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void convertToBytes(String hexWord, byte[] ipByteArray, int byteIndex)
/*      */   {
/*  370 */     int hexWordLength = hexWord.length();
/*  371 */     int hexWordIndex = 0;
/*  372 */     ipByteArray[byteIndex] = 0;
/*  373 */     ipByteArray[(byteIndex + 1)] = 0;
/*      */     
/*      */ 
/*      */ 
/*  377 */     if (hexWordLength > 3) {
/*  378 */       int charValue = getIntValue(hexWord.charAt(hexWordIndex++)); int 
/*  379 */         tmp39_38 = byteIndex; byte[] tmp39_37 = ipByteArray;tmp39_37[tmp39_38] = ((byte)(tmp39_37[tmp39_38] | charValue << 4));
/*      */     }
/*      */     
/*      */ 
/*  383 */     if (hexWordLength > 2) {
/*  384 */       int charValue = getIntValue(hexWord.charAt(hexWordIndex++)); int 
/*  385 */         tmp69_68 = byteIndex; byte[] tmp69_67 = ipByteArray;tmp69_67[tmp69_68] = ((byte)(tmp69_67[tmp69_68] | charValue));
/*      */     }
/*      */     
/*      */ 
/*  389 */     if (hexWordLength > 1) {
/*  390 */       int charValue = getIntValue(hexWord.charAt(hexWordIndex++)); int 
/*  391 */         tmp99_98 = (byteIndex + 1); byte[] tmp99_95 = ipByteArray;tmp99_95[tmp99_98] = ((byte)(tmp99_95[tmp99_98] | charValue << 4));
/*      */     }
/*      */     
/*      */ 
/*  395 */     int charValue = getIntValue(hexWord.charAt(hexWordIndex)); int 
/*  396 */       tmp123_122 = (byteIndex + 1); byte[] tmp123_119 = ipByteArray;tmp123_119[tmp123_122] = ((byte)(tmp123_119[tmp123_122] | charValue & 0xF));
/*      */   }
/*      */   
/*      */   private static int getIntValue(char c) {
/*  400 */     switch (c) {
/*      */     case '0': 
/*  402 */       return 0;
/*      */     case '1': 
/*  404 */       return 1;
/*      */     case '2': 
/*  406 */       return 2;
/*      */     case '3': 
/*  408 */       return 3;
/*      */     case '4': 
/*  410 */       return 4;
/*      */     case '5': 
/*  412 */       return 5;
/*      */     case '6': 
/*  414 */       return 6;
/*      */     case '7': 
/*  416 */       return 7;
/*      */     case '8': 
/*  418 */       return 8;
/*      */     case '9': 
/*  420 */       return 9;
/*      */     }
/*      */     
/*  423 */     c = Character.toLowerCase(c);
/*  424 */     switch (c) {
/*      */     case 'a': 
/*  426 */       return 10;
/*      */     case 'b': 
/*  428 */       return 11;
/*      */     case 'c': 
/*  430 */       return 12;
/*      */     case 'd': 
/*  432 */       return 13;
/*      */     case 'e': 
/*  434 */       return 14;
/*      */     case 'f': 
/*  436 */       return 15;
/*      */     }
/*  438 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String intToIpAddress(int i)
/*      */   {
/*  445 */     StringBuilder buf = new StringBuilder(15);
/*  446 */     buf.append(i >> 24 & 0xFF);
/*  447 */     buf.append('.');
/*  448 */     buf.append(i >> 16 & 0xFF);
/*  449 */     buf.append('.');
/*  450 */     buf.append(i >> 8 & 0xFF);
/*  451 */     buf.append('.');
/*  452 */     buf.append(i & 0xFF);
/*  453 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String bytesToIpAddress(byte[] bytes, int offset, int length)
/*      */   {
/*  463 */     if (length == 4) {
/*  464 */       StringBuilder buf = new StringBuilder(15);
/*      */       
/*  466 */       buf.append(bytes[(offset++)] >> 24 & 0xFF);
/*  467 */       buf.append('.');
/*  468 */       buf.append(bytes[(offset++)] >> 16 & 0xFF);
/*  469 */       buf.append('.');
/*  470 */       buf.append(bytes[(offset++)] >> 8 & 0xFF);
/*  471 */       buf.append('.');
/*  472 */       buf.append(bytes[offset] & 0xFF);
/*      */       
/*  474 */       return buf.toString();
/*      */     }
/*      */     
/*  477 */     if (length == 16) {
/*  478 */       StringBuilder sb = new StringBuilder(39);
/*  479 */       int endOffset = offset + 14;
/*  481 */       for (; 
/*  481 */           offset < endOffset; offset += 2) {
/*  482 */         StringUtil.toHexString(sb, bytes, offset, 2);
/*  483 */         sb.append(':');
/*      */       }
/*  485 */       StringUtil.toHexString(sb, bytes, offset, 2);
/*      */       
/*  487 */       return sb.toString();
/*      */     }
/*      */     
/*  490 */     throw new IllegalArgumentException("length: " + length + " (expected: 4 or 16)");
/*      */   }
/*      */   
/*      */   public static boolean isValidIpV6Address(String ipAddress) {
/*  494 */     int length = ipAddress.length();
/*  495 */     boolean doubleColon = false;
/*  496 */     int numberOfColons = 0;
/*  497 */     int numberOfPeriods = 0;
/*  498 */     StringBuilder word = new StringBuilder();
/*  499 */     char c = '\000';
/*      */     
/*  501 */     int startOffset = 0;
/*  502 */     int endOffset = ipAddress.length();
/*      */     
/*  504 */     if (endOffset < 2) {
/*  505 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  509 */     if (ipAddress.charAt(0) == '[') {
/*  510 */       if (ipAddress.charAt(endOffset - 1) != ']') {
/*  511 */         return false;
/*      */       }
/*      */       
/*  514 */       startOffset = 1;
/*  515 */       endOffset--;
/*      */     }
/*      */     
/*      */ 
/*  519 */     int percentIdx = ipAddress.indexOf('%', startOffset);
/*  520 */     if (percentIdx >= 0) {
/*  521 */       endOffset = percentIdx;
/*      */     }
/*      */     
/*  524 */     for (int i = startOffset; i < endOffset; i++) {
/*  525 */       char prevChar = c;
/*  526 */       c = ipAddress.charAt(i);
/*  527 */       switch (c)
/*      */       {
/*      */       case '.': 
/*  530 */         numberOfPeriods++;
/*  531 */         if (numberOfPeriods > 3) {
/*  532 */           return false;
/*      */         }
/*  534 */         if (!isValidIp4Word(word.toString())) {
/*  535 */           return false;
/*      */         }
/*  537 */         if ((numberOfColons != 6) && (!doubleColon)) {
/*  538 */           return false;
/*      */         }
/*      */         
/*      */ 
/*  542 */         if ((numberOfColons == 7) && (ipAddress.charAt(startOffset) != ':') && (ipAddress.charAt(1 + startOffset) != ':'))
/*      */         {
/*  544 */           return false;
/*      */         }
/*  546 */         word.delete(0, word.length());
/*  547 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       case ':': 
/*  553 */         if ((i == startOffset) && ((ipAddress.length() <= i) || (ipAddress.charAt(i + 1) != ':'))) {
/*  554 */           return false;
/*      */         }
/*      */         
/*  557 */         numberOfColons++;
/*  558 */         if (numberOfColons > 7) {
/*  559 */           return false;
/*      */         }
/*  561 */         if (numberOfPeriods > 0) {
/*  562 */           return false;
/*      */         }
/*  564 */         if (prevChar == ':') {
/*  565 */           if (doubleColon) {
/*  566 */             return false;
/*      */           }
/*  568 */           doubleColon = true;
/*      */         }
/*  570 */         word.delete(0, word.length());
/*  571 */         break;
/*      */       
/*      */       default: 
/*  574 */         if ((word != null) && (word.length() > 3)) {
/*  575 */           return false;
/*      */         }
/*  577 */         if (!isValidHexChar(c)) {
/*  578 */           return false;
/*      */         }
/*  580 */         word.append(c);
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  585 */     if (numberOfPeriods > 0)
/*      */     {
/*  587 */       if ((numberOfPeriods != 3) || (!isValidIp4Word(word.toString())) || (numberOfColons >= 7)) {
/*  588 */         return false;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  593 */       if ((numberOfColons != 7) && (!doubleColon)) {
/*  594 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  600 */       if ((word.length() == 0) && (ipAddress.charAt(length - 1 - startOffset) == ':') && (ipAddress.charAt(length - 2 - startOffset) != ':'))
/*      */       {
/*  602 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  606 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isValidIp4Word(String word)
/*      */   {
/*  611 */     if ((word.length() < 1) || (word.length() > 3)) {
/*  612 */       return false;
/*      */     }
/*  614 */     for (int i = 0; i < word.length(); i++) {
/*  615 */       char c = word.charAt(i);
/*  616 */       if ((c < '0') || (c > '9')) {
/*  617 */         return false;
/*      */       }
/*      */     }
/*  620 */     return Integer.parseInt(word) <= 255;
/*      */   }
/*      */   
/*      */   private static boolean isValidHexChar(char c) {
/*  624 */     return ((c >= '0') && (c <= '9')) || ((c >= 'A') && (c <= 'F')) || ((c >= 'a') && (c <= 'f'));
/*      */   }
/*      */   
/*      */   private static boolean isValidNumericChar(char c) {
/*  628 */     return (c >= '0') && (c <= '9');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isValidIpV4Address(String value)
/*      */   {
/*  639 */     int periods = 0;
/*      */     
/*  641 */     int length = value.length();
/*      */     
/*  643 */     if (length > 15) {
/*  644 */       return false;
/*      */     }
/*      */     
/*  647 */     StringBuilder word = new StringBuilder();
/*  648 */     for (int i = 0; i < length; i++) {
/*  649 */       char c = value.charAt(i);
/*  650 */       if (c == '.') {
/*  651 */         periods++;
/*  652 */         if (periods > 3) {
/*  653 */           return false;
/*      */         }
/*  655 */         if (word.length() == 0) {
/*  656 */           return false;
/*      */         }
/*  658 */         if (Integer.parseInt(word.toString()) > 255) {
/*  659 */           return false;
/*      */         }
/*  661 */         word.delete(0, word.length());
/*  662 */       } else { if (!Character.isDigit(c)) {
/*  663 */           return false;
/*      */         }
/*  665 */         if (word.length() > 2) {
/*  666 */           return false;
/*      */         }
/*  668 */         word.append(c);
/*      */       }
/*      */     }
/*      */     
/*  672 */     if ((word.length() == 0) || (Integer.parseInt(word.toString()) > 255)) {
/*  673 */       return false;
/*      */     }
/*      */     
/*  676 */     return periods == 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Inet6Address getByName(CharSequence ip)
/*      */   {
/*  687 */     return getByName(ip, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Inet6Address getByName(CharSequence ip, boolean ipv4Mapped)
/*      */   {
/*  705 */     byte[] bytes = new byte[16];
/*  706 */     int ipLength = ip.length();
/*  707 */     int compressBegin = 0;
/*  708 */     int compressLength = 0;
/*  709 */     int currentIndex = 0;
/*  710 */     int value = 0;
/*  711 */     int begin = -1;
/*  712 */     int i = 0;
/*  713 */     int ipv6Seperators = 0;
/*  714 */     int ipv4Seperators = 0;
/*      */     
/*  716 */     boolean needsShift = false;
/*  717 */     int tmp; for (; i < ipLength; i++) {
/*  718 */       char c = ip.charAt(i);
/*  719 */       switch (c) {
/*      */       case ':': 
/*  721 */         ipv6Seperators++;
/*  722 */         if ((i - begin > 4) || (ipv4Seperators > 0) || (ipv6Seperators > 8) || (currentIndex + 1 >= bytes.length))
/*      */         {
/*      */ 
/*  725 */           return null;
/*      */         }
/*  727 */         value <<= 4 - (i - begin) << 2;
/*      */         
/*  729 */         if (compressLength > 0) {
/*  730 */           compressLength -= 2;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  736 */         bytes[(currentIndex++)] = ((byte)((value & 0xF) << 4 | value >> 4 & 0xF));
/*  737 */         bytes[(currentIndex++)] = ((byte)((value >> 8 & 0xF) << 4 | value >> 12 & 0xF));
/*  738 */         tmp = i + 1;
/*  739 */         if ((tmp < ipLength) && (ip.charAt(tmp) == ':')) {
/*  740 */           tmp++;
/*  741 */           if ((compressBegin != 0) || ((tmp < ipLength) && (ip.charAt(tmp) == ':'))) {
/*  742 */             return null;
/*      */           }
/*  744 */           ipv6Seperators++;
/*  745 */           needsShift = (ipv6Seperators == 2) && (value == 0);
/*  746 */           compressBegin = currentIndex;
/*  747 */           compressLength = bytes.length - compressBegin - 2;
/*  748 */           i++;
/*      */         }
/*  750 */         value = 0;
/*  751 */         begin = -1;
/*  752 */         break;
/*      */       case '.': 
/*  754 */         ipv4Seperators++;
/*  755 */         if ((i - begin > 3) || (ipv4Seperators > 3) || ((ipv6Seperators > 0) && (currentIndex + compressLength < 12)) || (i + 1 >= ipLength) || (currentIndex >= bytes.length) || (begin < 0) || ((begin == 0) && (((i == 3) && ((!isValidNumericChar(ip.charAt(2))) || (!isValidNumericChar(ip.charAt(1))) || (!isValidNumericChar(ip.charAt(0))))) || ((i == 2) && ((!isValidNumericChar(ip.charAt(1))) || (!isValidNumericChar(ip.charAt(0))))) || ((i == 1) && (!isValidNumericChar(ip.charAt(0)))))))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  767 */           return null;
/*      */         }
/*  769 */         value <<= 3 - (i - begin) << 2;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  774 */         begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
/*  775 */         if ((begin < 0) || (begin > 255)) {
/*  776 */           return null;
/*      */         }
/*  778 */         bytes[(currentIndex++)] = ((byte)begin);
/*  779 */         value = 0;
/*  780 */         begin = -1;
/*  781 */         break;
/*      */       default: 
/*  783 */         if ((!isValidHexChar(c)) || ((ipv4Seperators > 0) && (!isValidNumericChar(c)))) {
/*  784 */           return null;
/*      */         }
/*  786 */         if (begin < 0) {
/*  787 */           begin = i;
/*  788 */         } else if (i - begin > 4) {
/*  789 */           return null;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  795 */         value += (getIntValue(c) << (i - begin << 2));
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  800 */     boolean isCompressed = compressBegin > 0;
/*      */     
/*  802 */     if (ipv4Seperators > 0) {
/*  803 */       if (((begin > 0) && (i - begin > 3)) || (ipv4Seperators != 3) || (currentIndex >= bytes.length))
/*      */       {
/*      */ 
/*  806 */         return null;
/*      */       }
/*  808 */       if (ipv6Seperators == 0) {
/*  809 */         compressLength = 12;
/*  810 */       } else if ((ipv6Seperators >= 2) && (ip.charAt(ipLength - 1) != ':') && (((!isCompressed) && (ipv6Seperators == 6) && (ip.charAt(0) != ':')) || ((isCompressed) && (ipv6Seperators + 1 < 8) && ((ip.charAt(0) != ':') || (compressBegin <= 2)))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  815 */         compressLength -= 2;
/*      */       } else {
/*  817 */         return null;
/*      */       }
/*  819 */       value <<= 3 - (i - begin) << 2;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  824 */       begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
/*  825 */       if ((begin < 0) || (begin > 255)) {
/*  826 */         return null;
/*      */       }
/*  828 */       bytes[(currentIndex++)] = ((byte)begin);
/*      */     } else {
/*  830 */       tmp = ipLength - 1;
/*  831 */       if (((begin > 0) && (i - begin > 4)) || (ipv6Seperators < 2) || ((!isCompressed) && ((ipv6Seperators + 1 != 8) || (ip.charAt(0) == ':') || (ip.charAt(tmp) == ':'))) || ((isCompressed) && ((ipv6Seperators > 8) || ((ipv6Seperators == 8) && (((compressBegin <= 2) && (ip.charAt(0) != ':')) || ((compressBegin >= 14) && (ip.charAt(tmp) != ':')))))) || (currentIndex + 1 >= bytes.length))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  840 */         return null;
/*      */       }
/*  842 */       if ((begin >= 0) && (i - begin <= 4)) {
/*  843 */         value <<= 4 - (i - begin) << 2;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  848 */       bytes[(currentIndex++)] = ((byte)((value & 0xF) << 4 | value >> 4 & 0xF));
/*  849 */       bytes[(currentIndex++)] = ((byte)((value >> 8 & 0xF) << 4 | value >> 12 & 0xF));
/*      */     }
/*      */     
/*  852 */     i = currentIndex + compressLength;
/*  853 */     if ((needsShift) || (i >= bytes.length))
/*      */     {
/*  855 */       if (i >= bytes.length) {
/*  856 */         compressBegin++;
/*      */       }
/*  858 */       for (i = currentIndex; i < bytes.length;) {
/*  859 */         for (begin = bytes.length - 1; begin >= compressBegin; begin--) {
/*  860 */           bytes[begin] = bytes[(begin - 1)];
/*      */         }
/*  862 */         bytes[begin] = 0;
/*  863 */         compressBegin++;i++; continue;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  867 */         for (i = 0; i < compressLength; i++) {
/*  868 */           begin = i + compressBegin;
/*  869 */           currentIndex = begin + compressLength;
/*  870 */           if (currentIndex >= bytes.length) break;
/*  871 */           bytes[currentIndex] = bytes[begin];
/*  872 */           bytes[begin] = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  879 */     if ((ipv4Mapped) && (ipv4Seperators > 0) && (bytes[0] == 0) && (bytes[1] == 0) && (bytes[2] == 0) && (bytes[3] == 0) && (bytes[4] == 0) && (bytes[5] == 0) && (bytes[6] == 0) && (bytes[7] == 0) && (bytes[8] == 0) && (bytes[9] == 0))
/*      */     {
/*      */ 
/*  882 */       bytes[10] = (bytes[11] = -1);
/*      */     }
/*      */     try
/*      */     {
/*  886 */       return Inet6Address.getByAddress(null, bytes, -1);
/*      */     } catch (UnknownHostException e) {
/*  888 */       throw new RuntimeException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toAddressString(InetAddress ip)
/*      */   {
/*  905 */     return toAddressString(ip, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toAddressString(InetAddress ip, boolean ipv4Mapped)
/*      */   {
/*  933 */     if ((ip instanceof Inet4Address)) {
/*  934 */       return ip.getHostAddress();
/*      */     }
/*  936 */     if (!(ip instanceof Inet6Address)) {
/*  937 */       throw new IllegalArgumentException("Unhandled type: " + ip.getClass());
/*      */     }
/*      */     
/*  940 */     byte[] bytes = ip.getAddress();
/*  941 */     int[] words = new int[8];
/*      */     
/*  943 */     for (int i = 0; i < words.length; i++) {
/*  944 */       words[i] = ((bytes[(i << 1)] & 0xFF) << 8 | bytes[((i << 1) + 1)] & 0xFF);
/*      */     }
/*      */     
/*      */ 
/*  948 */     int currentStart = -1;
/*  949 */     int currentLength = 0;
/*  950 */     int shortestStart = -1;
/*  951 */     int shortestLength = 0;
/*  952 */     for (i = 0; i < words.length; i++) {
/*  953 */       if (words[i] == 0) {
/*  954 */         if (currentStart < 0) {
/*  955 */           currentStart = i;
/*      */         }
/*  957 */       } else if (currentStart >= 0) {
/*  958 */         currentLength = i - currentStart;
/*  959 */         if (currentLength > shortestLength) {
/*  960 */           shortestStart = currentStart;
/*  961 */           shortestLength = currentLength;
/*      */         }
/*  963 */         currentStart = -1;
/*      */       }
/*      */     }
/*      */     
/*  967 */     if (currentStart >= 0) {
/*  968 */       currentLength = i - currentStart;
/*  969 */       if (currentLength > shortestLength) {
/*  970 */         shortestStart = currentStart;
/*  971 */         shortestLength = currentLength;
/*      */       }
/*      */     }
/*      */     
/*  975 */     if (shortestLength == 1) {
/*  976 */       shortestLength = 0;
/*  977 */       shortestStart = -1;
/*      */     }
/*      */     
/*      */ 
/*  981 */     int shortestEnd = shortestStart + shortestLength;
/*  982 */     StringBuilder b = new StringBuilder(39);
/*  983 */     if (shortestEnd < 0) {
/*  984 */       b.append(Integer.toHexString(words[0]));
/*  985 */       for (i = 1; i < words.length; i++) {
/*  986 */         b.append(':');
/*  987 */         b.append(Integer.toHexString(words[i]));
/*      */       }
/*      */     }
/*      */     boolean isIpv4Mapped;
/*      */     boolean isIpv4Mapped;
/*  992 */     if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
/*  993 */       b.append("::");
/*  994 */       isIpv4Mapped = (ipv4Mapped) && (shortestEnd == 5) && (words[5] == 65535);
/*      */     } else {
/*  996 */       b.append(Integer.toHexString(words[0]));
/*  997 */       isIpv4Mapped = false;
/*      */     }
/*  999 */     for (i = 1; i < words.length; i++) {
/* 1000 */       if (!inRangeEndExclusive(i, shortestStart, shortestEnd)) {
/* 1001 */         if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd))
/*      */         {
/* 1003 */           if ((!isIpv4Mapped) || (i == 6)) {
/* 1004 */             b.append(':');
/*      */           } else {
/* 1006 */             b.append('.');
/*      */           }
/*      */         }
/* 1009 */         if ((isIpv4Mapped) && (i > 5)) {
/* 1010 */           b.append(words[i] >> 8);
/* 1011 */           b.append('.');
/* 1012 */           b.append(words[i] & 0xFF);
/*      */         } else {
/* 1014 */           b.append(Integer.toHexString(words[i]));
/*      */         }
/* 1016 */       } else if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd))
/*      */       {
/* 1018 */         b.append("::");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1023 */     return b.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean inRangeEndExclusive(int value, int start, int end)
/*      */   {
/* 1038 */     return (value >= start) && (value < end);
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\NetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */