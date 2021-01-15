/*     */ package io.netty.handler.codec.haproxy;
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
/*     */ public enum HAProxyProxiedProtocol
/*     */ {
/*  27 */   UNKNOWN((byte)0, AddressFamily.AF_UNSPEC, TransportProtocol.UNSPEC), 
/*     */   
/*     */ 
/*     */ 
/*  31 */   TCP4((byte)17, AddressFamily.AF_IPv4, TransportProtocol.STREAM), 
/*     */   
/*     */ 
/*     */ 
/*  35 */   TCP6((byte)33, AddressFamily.AF_IPv6, TransportProtocol.STREAM), 
/*     */   
/*     */ 
/*     */ 
/*  39 */   UDP4((byte)18, AddressFamily.AF_IPv4, TransportProtocol.DGRAM), 
/*     */   
/*     */ 
/*     */ 
/*  43 */   UDP6((byte)34, AddressFamily.AF_IPv6, TransportProtocol.DGRAM), 
/*     */   
/*     */ 
/*     */ 
/*  47 */   UNIX_STREAM((byte)49, AddressFamily.AF_UNIX, TransportProtocol.STREAM), 
/*     */   
/*     */ 
/*     */ 
/*  51 */   UNIX_DGRAM((byte)50, AddressFamily.AF_UNIX, TransportProtocol.DGRAM);
/*     */   
/*     */ 
/*     */ 
/*     */   private final byte byteValue;
/*     */   
/*     */ 
/*     */   private final AddressFamily addressFamily;
/*     */   
/*     */   private final TransportProtocol transportProtocol;
/*     */   
/*     */ 
/*     */   private HAProxyProxiedProtocol(byte byteValue, AddressFamily addressFamily, TransportProtocol transportProtocol)
/*     */   {
/*  65 */     this.byteValue = byteValue;
/*  66 */     this.addressFamily = addressFamily;
/*  67 */     this.transportProtocol = transportProtocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HAProxyProxiedProtocol valueOf(byte tpafByte)
/*     */   {
/*  76 */     switch (tpafByte) {
/*     */     case 17: 
/*  78 */       return TCP4;
/*     */     case 33: 
/*  80 */       return TCP6;
/*     */     case 0: 
/*  82 */       return UNKNOWN;
/*     */     case 18: 
/*  84 */       return UDP4;
/*     */     case 34: 
/*  86 */       return UDP6;
/*     */     case 49: 
/*  88 */       return UNIX_STREAM;
/*     */     case 50: 
/*  90 */       return UNIX_DGRAM;
/*     */     }
/*  92 */     throw new IllegalArgumentException("unknown transport protocol + address family: " + (tpafByte & 0xFF));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte byteValue()
/*     */   {
/* 101 */     return this.byteValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AddressFamily addressFamily()
/*     */   {
/* 108 */     return this.addressFamily;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TransportProtocol transportProtocol()
/*     */   {
/* 115 */     return this.transportProtocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum AddressFamily
/*     */   {
/* 125 */     AF_UNSPEC((byte)0), 
/*     */     
/*     */ 
/*     */ 
/* 129 */     AF_IPv4((byte)16), 
/*     */     
/*     */ 
/*     */ 
/* 133 */     AF_IPv6((byte)32), 
/*     */     
/*     */ 
/*     */ 
/* 137 */     AF_UNIX((byte)48);
/*     */     
/*     */ 
/*     */ 
/*     */     private static final byte FAMILY_MASK = -16;
/*     */     
/*     */ 
/*     */     private final byte byteValue;
/*     */     
/*     */ 
/*     */ 
/*     */     private AddressFamily(byte byteValue)
/*     */     {
/* 150 */       this.byteValue = byteValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static AddressFamily valueOf(byte tpafByte)
/*     */     {
/* 159 */       int addressFamily = tpafByte & 0xFFFFFFF0;
/* 160 */       switch ((byte)addressFamily) {
/*     */       case 16: 
/* 162 */         return AF_IPv4;
/*     */       case 32: 
/* 164 */         return AF_IPv6;
/*     */       case 0: 
/* 166 */         return AF_UNSPEC;
/*     */       case 48: 
/* 168 */         return AF_UNIX;
/*     */       }
/* 170 */       throw new IllegalArgumentException("unknown address family: " + addressFamily);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public byte byteValue()
/*     */     {
/* 178 */       return this.byteValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum TransportProtocol
/*     */   {
/* 189 */     UNSPEC((byte)0), 
/*     */     
/*     */ 
/*     */ 
/* 193 */     STREAM((byte)1), 
/*     */     
/*     */ 
/*     */ 
/* 197 */     DGRAM((byte)2);
/*     */     
/*     */ 
/*     */ 
/*     */     private static final byte TRANSPORT_MASK = 15;
/*     */     
/*     */ 
/*     */     private final byte transportByte;
/*     */     
/*     */ 
/*     */ 
/*     */     private TransportProtocol(byte transportByte)
/*     */     {
/* 210 */       this.transportByte = transportByte;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static TransportProtocol valueOf(byte tpafByte)
/*     */     {
/* 219 */       int transportProtocol = tpafByte & 0xF;
/* 220 */       switch ((byte)transportProtocol) {
/*     */       case 1: 
/* 222 */         return STREAM;
/*     */       case 0: 
/* 224 */         return UNSPEC;
/*     */       case 2: 
/* 226 */         return DGRAM;
/*     */       }
/* 228 */       throw new IllegalArgumentException("unknown transport protocol: " + transportProtocol);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public byte byteValue()
/*     */     {
/* 236 */       return this.transportByte;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyProxiedProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */