/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufProcessor;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public final class HAProxyMessage
/*     */ {
/*  34 */   private static final HAProxyMessage V1_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private static final HAProxyMessage V2_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private static final HAProxyMessage V2_LOCAL_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.LOCAL, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */   private final HAProxyProtocolVersion protocolVersion;
/*     */   
/*     */   private final HAProxyCommand command;
/*     */   
/*     */   private final HAProxyProxiedProtocol proxiedProtocol;
/*     */   
/*     */   private final String sourceAddress;
/*     */   
/*     */   private final String destinationAddress;
/*     */   
/*     */   private final int sourcePort;
/*     */   private final int destinationPort;
/*     */   
/*     */   private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, String sourcePort, String destinationPort)
/*     */   {
/*  65 */     this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, portStringToInt(sourcePort), portStringToInt(destinationPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, int sourcePort, int destinationPort)
/*     */   {
/*  77 */     if (proxiedProtocol == null) {
/*  78 */       throw new NullPointerException("proxiedProtocol");
/*     */     }
/*  80 */     HAProxyProxiedProtocol.AddressFamily addrFamily = proxiedProtocol.addressFamily();
/*     */     
/*  82 */     checkAddress(sourceAddress, addrFamily);
/*  83 */     checkAddress(destinationAddress, addrFamily);
/*  84 */     checkPort(sourcePort);
/*  85 */     checkPort(destinationPort);
/*     */     
/*  87 */     this.protocolVersion = protocolVersion;
/*  88 */     this.command = command;
/*  89 */     this.proxiedProtocol = proxiedProtocol;
/*  90 */     this.sourceAddress = sourceAddress;
/*  91 */     this.destinationAddress = destinationAddress;
/*  92 */     this.sourcePort = sourcePort;
/*  93 */     this.destinationPort = destinationPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static HAProxyMessage decodeHeader(ByteBuf header)
/*     */   {
/* 104 */     if (header == null) {
/* 105 */       throw new NullPointerException("header");
/*     */     }
/*     */     
/* 108 */     if (header.readableBytes() < 16) {
/* 109 */       throw new HAProxyProtocolException("incomplete header: " + header.readableBytes() + " bytes (expected: 16+ bytes)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 114 */     header.skipBytes(12);
/* 115 */     byte verCmdByte = header.readByte();
/*     */     HAProxyProtocolVersion ver;
/*     */     try
/*     */     {
/* 119 */       ver = HAProxyProtocolVersion.valueOf(verCmdByte);
/*     */     } catch (IllegalArgumentException e) {
/* 121 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     
/* 124 */     if (ver != HAProxyProtocolVersion.V2) {
/* 125 */       throw new HAProxyProtocolException("version 1 unsupported: 0x" + Integer.toHexString(verCmdByte));
/*     */     }
/*     */     HAProxyCommand cmd;
/*     */     try
/*     */     {
/* 130 */       cmd = HAProxyCommand.valueOf(verCmdByte);
/*     */     } catch (IllegalArgumentException e) {
/* 132 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     
/* 135 */     if (cmd == HAProxyCommand.LOCAL) {
/* 136 */       return V2_LOCAL_MSG;
/*     */     }
/*     */     
/*     */     HAProxyProxiedProtocol protAndFam;
/*     */     try
/*     */     {
/* 142 */       protAndFam = HAProxyProxiedProtocol.valueOf(header.readByte());
/*     */     } catch (IllegalArgumentException e) {
/* 144 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     
/* 147 */     if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
/* 148 */       return V2_UNKNOWN_MSG;
/*     */     }
/*     */     
/* 151 */     int addressInfoLen = header.readUnsignedShort();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 156 */     int srcPort = 0;
/* 157 */     int dstPort = 0;
/*     */     
/* 159 */     HAProxyProxiedProtocol.AddressFamily addressFamily = protAndFam.addressFamily();
/*     */     String dstAddress;
/* 161 */     String srcAddress; String dstAddress; if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_UNIX)
/*     */     {
/* 163 */       if ((addressInfoLen < 216) || (header.readableBytes() < 216)) {
/* 164 */         throw new HAProxyProtocolException("incomplete UNIX socket address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 216+ bytes)");
/*     */       }
/*     */       
/*     */ 
/* 168 */       int startIdx = header.readerIndex();
/* 169 */       int addressEnd = header.forEachByte(startIdx, 108, ByteBufProcessor.FIND_NUL);
/* 170 */       int addressLen; int addressLen; if (addressEnd == -1) {
/* 171 */         addressLen = 108;
/*     */       } else {
/* 173 */         addressLen = addressEnd - startIdx;
/*     */       }
/* 175 */       String srcAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
/*     */       
/* 177 */       startIdx += 108;
/*     */       
/* 179 */       addressEnd = header.forEachByte(startIdx, 108, ByteBufProcessor.FIND_NUL);
/* 180 */       if (addressEnd == -1) {
/* 181 */         addressLen = 108;
/*     */       } else {
/* 183 */         addressLen = addressEnd - startIdx;
/*     */       }
/* 185 */       dstAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
/*     */     } else { int addressLen;
/* 187 */       if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv4)
/*     */       {
/* 189 */         if ((addressInfoLen < 12) || (header.readableBytes() < 12)) {
/* 190 */           throw new HAProxyProtocolException("incomplete IPv4 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 12+ bytes)");
/*     */         }
/*     */         
/*     */ 
/* 194 */         addressLen = 4; } else { int addressLen;
/* 195 */         if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv6)
/*     */         {
/* 197 */           if ((addressInfoLen < 36) || (header.readableBytes() < 36)) {
/* 198 */             throw new HAProxyProtocolException("incomplete IPv6 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 36+ bytes)");
/*     */           }
/*     */           
/*     */ 
/* 202 */           addressLen = 16;
/*     */         } else {
/* 204 */           throw new HAProxyProtocolException("unable to parse address information (unkown address family: " + addressFamily + ')');
/*     */         }
/*     */       }
/*     */       
/*     */       int addressLen;
/* 209 */       srcAddress = ipBytestoString(header, addressLen);
/* 210 */       dstAddress = ipBytestoString(header, addressLen);
/* 211 */       srcPort = header.readUnsignedShort();
/* 212 */       dstPort = header.readUnsignedShort();
/*     */     }
/*     */     
/* 215 */     return new HAProxyMessage(ver, cmd, protAndFam, srcAddress, dstAddress, srcPort, dstPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static HAProxyMessage decodeHeader(String header)
/*     */   {
/* 226 */     if (header == null) {
/* 227 */       throw new HAProxyProtocolException("header");
/*     */     }
/*     */     
/* 230 */     String[] parts = StringUtil.split(header, ' ');
/* 231 */     int numParts = parts.length;
/*     */     
/* 233 */     if (numParts < 2) {
/* 234 */       throw new HAProxyProtocolException("invalid header: " + header + " (expected: 'PROXY' and proxied protocol values)");
/*     */     }
/*     */     
/*     */ 
/* 238 */     if (!"PROXY".equals(parts[0])) {
/* 239 */       throw new HAProxyProtocolException("unknown identifier: " + parts[0]);
/*     */     }
/*     */     HAProxyProxiedProtocol protAndFam;
/*     */     try
/*     */     {
/* 244 */       protAndFam = HAProxyProxiedProtocol.valueOf(parts[1]);
/*     */     } catch (IllegalArgumentException e) {
/* 246 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     
/* 249 */     if ((protAndFam != HAProxyProxiedProtocol.TCP4) && (protAndFam != HAProxyProxiedProtocol.TCP6) && (protAndFam != HAProxyProxiedProtocol.UNKNOWN))
/*     */     {
/*     */ 
/* 252 */       throw new HAProxyProtocolException("unsupported v1 proxied protocol: " + parts[1]);
/*     */     }
/*     */     
/* 255 */     if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
/* 256 */       return V1_UNKNOWN_MSG;
/*     */     }
/*     */     
/* 259 */     if (numParts != 6) {
/* 260 */       throw new HAProxyProtocolException("invalid TCP4/6 header: " + header + " (expected: 6 parts)");
/*     */     }
/*     */     
/* 263 */     return new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, protAndFam, parts[2], parts[3], parts[4], parts[5]);
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
/*     */   private static String ipBytestoString(ByteBuf header, int addressLen)
/*     */   {
/* 276 */     StringBuilder sb = new StringBuilder();
/* 277 */     if (addressLen == 4) {
/* 278 */       sb.append(header.readByte() & 0xFF);
/* 279 */       sb.append('.');
/* 280 */       sb.append(header.readByte() & 0xFF);
/* 281 */       sb.append('.');
/* 282 */       sb.append(header.readByte() & 0xFF);
/* 283 */       sb.append('.');
/* 284 */       sb.append(header.readByte() & 0xFF);
/*     */     } else {
/* 286 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 287 */       sb.append(':');
/* 288 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 289 */       sb.append(':');
/* 290 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 291 */       sb.append(':');
/* 292 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 293 */       sb.append(':');
/* 294 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 295 */       sb.append(':');
/* 296 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 297 */       sb.append(':');
/* 298 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 299 */       sb.append(':');
/* 300 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/*     */     }
/* 302 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int portStringToInt(String value)
/*     */   {
/*     */     int port;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 315 */       port = Integer.parseInt(value);
/*     */     } catch (NumberFormatException e) {
/* 317 */       throw new HAProxyProtocolException("invalid port: " + value, e);
/*     */     }
/*     */     
/* 320 */     if ((port <= 0) || (port > 65535)) {
/* 321 */       throw new HAProxyProtocolException("invalid port: " + value + " (expected: 1 ~ 65535)");
/*     */     }
/*     */     
/* 324 */     return port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void checkAddress(String address, HAProxyProxiedProtocol.AddressFamily addrFamily)
/*     */   {
/* 335 */     if (addrFamily == null) {
/* 336 */       throw new NullPointerException("addrFamily");
/*     */     }
/*     */     
/* 339 */     switch (addrFamily) {
/*     */     case AF_UNSPEC: 
/* 341 */       if (address != null) {
/* 342 */         throw new HAProxyProtocolException("unable to validate an AF_UNSPEC address: " + address);
/*     */       }
/* 344 */       return;
/*     */     case AF_UNIX: 
/* 346 */       return;
/*     */     }
/*     */     
/* 349 */     if (address == null) {
/* 350 */       throw new NullPointerException("address");
/*     */     }
/*     */     
/* 353 */     switch (addrFamily) {
/*     */     case AF_IPv4: 
/* 355 */       if (!NetUtil.isValidIpV4Address(address)) {
/* 356 */         throw new HAProxyProtocolException("invalid IPv4 address: " + address);
/*     */       }
/*     */       break;
/*     */     case AF_IPv6: 
/* 360 */       if (!NetUtil.isValidIpV6Address(address)) {
/* 361 */         throw new HAProxyProtocolException("invalid IPv6 address: " + address);
/*     */       }
/*     */       break;
/*     */     default: 
/* 365 */       throw new Error();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void checkPort(int port)
/*     */   {
/* 376 */     if ((port < 0) || (port > 65535)) {
/* 377 */       throw new HAProxyProtocolException("invalid port: " + port + " (expected: 1 ~ 65535)");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyProtocolVersion protocolVersion()
/*     */   {
/* 385 */     return this.protocolVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyCommand command()
/*     */   {
/* 392 */     return this.command;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyProxiedProtocol proxiedProtocol()
/*     */   {
/* 399 */     return this.proxiedProtocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String sourceAddress()
/*     */   {
/* 406 */     return this.sourceAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String destinationAddress()
/*     */   {
/* 413 */     return this.destinationAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int sourcePort()
/*     */   {
/* 420 */     return this.sourcePort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int destinationPort()
/*     */   {
/* 427 */     return this.destinationPort;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */