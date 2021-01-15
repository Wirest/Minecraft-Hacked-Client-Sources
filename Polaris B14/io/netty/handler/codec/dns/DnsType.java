/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class DnsType
/*     */   implements Comparable<DnsType>
/*     */ {
/*  32 */   public static final DnsType A = new DnsType(1, "A");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final DnsType NS = new DnsType(2, "NS");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final DnsType CNAME = new DnsType(5, "CNAME");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static final DnsType SOA = new DnsType(6, "SOA");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final DnsType PTR = new DnsType(12, "PTR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   public static final DnsType MX = new DnsType(15, "MX");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   public static final DnsType TXT = new DnsType(16, "TXT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final DnsType RP = new DnsType(17, "RP");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   public static final DnsType AFSDB = new DnsType(18, "AFSDB");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   public static final DnsType SIG = new DnsType(24, "SIG");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   public static final DnsType KEY = new DnsType(25, "KEY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   public static final DnsType AAAA = new DnsType(28, "AAAA");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   public static final DnsType LOC = new DnsType(29, "LOC");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   public static final DnsType SRV = new DnsType(33, "SRV");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */   public static final DnsType NAPTR = new DnsType(35, "NAPTR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */   public static final DnsType KX = new DnsType(36, "KX");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 145 */   public static final DnsType CERT = new DnsType(37, "CERT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */   public static final DnsType DNAME = new DnsType(39, "DNAME");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   public static final DnsType OPT = new DnsType(41, "OPT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */   public static final DnsType APL = new DnsType(42, "APL");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */   public static final DnsType DS = new DnsType(43, "DS");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */   public static final DnsType SSHFP = new DnsType(44, "SSHFP");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 184 */   public static final DnsType IPSECKEY = new DnsType(45, "IPSECKEY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public static final DnsType RRSIG = new DnsType(46, "RRSIG");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 196 */   public static final DnsType NSEC = new DnsType(47, "NSEC");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */   public static final DnsType DNSKEY = new DnsType(48, "DNSKEY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */   public static final DnsType DHCID = new DnsType(49, "DHCID");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */   public static final DnsType NSEC3 = new DnsType(50, "NSEC3");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 219 */   public static final DnsType NSEC3PARAM = new DnsType(51, "NSEC3PARAM");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */   public static final DnsType TLSA = new DnsType(52, "TLSA");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 234 */   public static final DnsType HIP = new DnsType(55, "HIP");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */   public static final DnsType SPF = new DnsType(99, "SPF");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 248 */   public static final DnsType TKEY = new DnsType(249, "TKEY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */   public static final DnsType TSIG = new DnsType(250, "TSIG");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 264 */   public static final DnsType IXFR = new DnsType(251, "IXFR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 270 */   public static final DnsType AXFR = new DnsType(252, "AXFR");
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
/* 281 */   public static final DnsType ANY = new DnsType(255, "ANY");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 287 */   public static final DnsType CAA = new DnsType(257, "CAA");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 294 */   public static final DnsType TA = new DnsType(32768, "TA");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 301 */   public static final DnsType DLV = new DnsType(32769, "DLV");
/*     */   
/* 303 */   private static final Map<String, DnsType> BY_NAME = new HashMap();
/* 304 */   private static final IntObjectHashMap<DnsType> BY_TYPE = new IntObjectHashMap();
/*     */   private static final String EXPECTED;
/*     */   
/*     */   static {
/* 308 */     DnsType[] all = { A, NS, CNAME, SOA, PTR, MX, TXT, RP, AFSDB, SIG, KEY, AAAA, LOC, SRV, NAPTR, KX, CERT, DNAME, OPT, APL, DS, SSHFP, IPSECKEY, RRSIG, NSEC, DNSKEY, DHCID, NSEC3, NSEC3PARAM, TLSA, HIP, SPF, TKEY, TSIG, IXFR, AXFR, ANY, CAA, TA, DLV };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 314 */     StringBuilder expected = new StringBuilder(512);
/* 315 */     expected.append(" (expected: ");
/*     */     
/* 317 */     for (DnsType type : all) {
/* 318 */       BY_NAME.put(type.name(), type);
/* 319 */       BY_TYPE.put(type.intValue(), type);
/* 320 */       expected.append(type.name());
/* 321 */       expected.append('(');
/* 322 */       expected.append(type.intValue());
/* 323 */       expected.append("), ");
/*     */     }
/*     */     
/* 326 */     expected.setLength(expected.length() - 2);
/* 327 */     expected.append(')');
/* 328 */     EXPECTED = expected.toString();
/*     */   }
/*     */   
/*     */   public static DnsType valueOf(int intValue) {
/* 332 */     DnsType result = (DnsType)BY_TYPE.get(intValue);
/* 333 */     if (result == null) {
/* 334 */       return new DnsType(intValue, "UNKNOWN");
/*     */     }
/* 336 */     return result;
/*     */   }
/*     */   
/*     */   public static DnsType valueOf(String name) {
/* 340 */     DnsType result = (DnsType)BY_NAME.get(name);
/* 341 */     if (result == null) {
/* 342 */       throw new IllegalArgumentException("name: " + name + EXPECTED);
/*     */     }
/* 344 */     return result;
/*     */   }
/*     */   
/*     */   private final int intValue;
/*     */   private final String name;
/*     */   public static DnsType valueOf(int intValue, String name)
/*     */   {
/* 351 */     return new DnsType(intValue, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private DnsType(int intValue, String name)
/*     */   {
/* 358 */     if ((intValue & 0xFFFF) != intValue) {
/* 359 */       throw new IllegalArgumentException("intValue: " + intValue + " (expected: 0 ~ 65535)");
/*     */     }
/* 361 */     this.intValue = intValue;
/* 362 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String name()
/*     */   {
/* 369 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 376 */     return this.intValue;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 381 */     return this.intValue;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 386 */     return ((o instanceof DnsType)) && (((DnsType)o).intValue == this.intValue);
/*     */   }
/*     */   
/*     */   public int compareTo(DnsType o)
/*     */   {
/* 391 */     return intValue() - o.intValue();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 396 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */