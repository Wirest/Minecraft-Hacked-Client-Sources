/*     */ package io.netty.handler.ipfilter;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
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
/*     */ public final class IpSubnetFilterRule
/*     */   implements IpFilterRule
/*     */ {
/*     */   private final IpFilterRule filterRule;
/*     */   
/*     */   public IpSubnetFilterRule(String ipAddress, int cidrPrefix, IpFilterRuleType ruleType)
/*     */   {
/*     */     try
/*     */     {
/*  35 */       this.filterRule = selectFilterRule(InetAddress.getByName(ipAddress), cidrPrefix, ruleType);
/*     */     } catch (UnknownHostException e) {
/*  37 */       throw new IllegalArgumentException("ipAddress", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public IpSubnetFilterRule(InetAddress ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  42 */     this.filterRule = selectFilterRule(ipAddress, cidrPrefix, ruleType);
/*     */   }
/*     */   
/*     */   private static IpFilterRule selectFilterRule(InetAddress ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  46 */     if (ipAddress == null) {
/*  47 */       throw new NullPointerException("ipAddress");
/*     */     }
/*     */     
/*  50 */     if (ruleType == null) {
/*  51 */       throw new NullPointerException("ruleType");
/*     */     }
/*     */     
/*  54 */     if ((ipAddress instanceof Inet4Address))
/*  55 */       return new Ip4SubnetFilterRule((Inet4Address)ipAddress, cidrPrefix, ruleType, null);
/*  56 */     if ((ipAddress instanceof Inet6Address)) {
/*  57 */       return new Ip6SubnetFilterRule((Inet6Address)ipAddress, cidrPrefix, ruleType, null);
/*     */     }
/*  59 */     throw new IllegalArgumentException("Only IPv4 and IPv6 addresses are supported");
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean matches(InetSocketAddress remoteAddress)
/*     */   {
/*  65 */     return this.filterRule.matches(remoteAddress);
/*     */   }
/*     */   
/*     */   public IpFilterRuleType ruleType()
/*     */   {
/*  70 */     return this.filterRule.ruleType();
/*     */   }
/*     */   
/*     */   private static final class Ip4SubnetFilterRule implements IpFilterRule
/*     */   {
/*     */     private final int networkAddress;
/*     */     private final int subnetMask;
/*     */     private final IpFilterRuleType ruleType;
/*     */     
/*     */     private Ip4SubnetFilterRule(Inet4Address ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  80 */       if ((cidrPrefix < 0) || (cidrPrefix > 32)) {
/*  81 */         throw new IllegalArgumentException(String.format("IPv4 requires the subnet prefix to be in range of [0,32]. The prefix was: %d", new Object[] { Integer.valueOf(cidrPrefix) }));
/*     */       }
/*     */       
/*     */ 
/*  85 */       this.subnetMask = prefixToSubnetMask(cidrPrefix);
/*  86 */       this.networkAddress = (ipToInt(ipAddress) & this.subnetMask);
/*  87 */       this.ruleType = ruleType;
/*     */     }
/*     */     
/*     */     public boolean matches(InetSocketAddress remoteAddress)
/*     */     {
/*  92 */       int ipAddress = ipToInt((Inet4Address)remoteAddress.getAddress());
/*     */       
/*  94 */       return (ipAddress & this.subnetMask) == this.networkAddress;
/*     */     }
/*     */     
/*     */     public IpFilterRuleType ruleType()
/*     */     {
/*  99 */       return this.ruleType;
/*     */     }
/*     */     
/*     */     private static int ipToInt(Inet4Address ipAddress) {
/* 103 */       byte[] octets = ipAddress.getAddress();
/* 104 */       assert (octets.length == 4);
/*     */       
/* 106 */       return (octets[0] & 0xFF) << 24 | (octets[1] & 0xFF) << 16 | (octets[2] & 0xFF) << 8 | octets[3] & 0xFF;
/*     */     }
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
/*     */     private static int prefixToSubnetMask(int cidrPrefix)
/*     */     {
/* 123 */       return (int)(-1L << 32 - cidrPrefix & 0xFFFFFFFFFFFFFFFF);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Ip6SubnetFilterRule implements IpFilterRule
/*     */   {
/* 129 */     private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1L);
/*     */     private final BigInteger networkAddress;
/*     */     private final BigInteger subnetMask;
/*     */     private final IpFilterRuleType ruleType;
/*     */     
/*     */     private Ip6SubnetFilterRule(Inet6Address ipAddress, int cidrPrefix, IpFilterRuleType ruleType)
/*     */     {
/* 136 */       if ((cidrPrefix < 0) || (cidrPrefix > 128)) {
/* 137 */         throw new IllegalArgumentException(String.format("IPv6 requires the subnet prefix to be in range of [0,128]. The prefix was: %d", new Object[] { Integer.valueOf(cidrPrefix) }));
/*     */       }
/*     */       
/*     */ 
/* 141 */       this.subnetMask = prefixToSubnetMask(cidrPrefix);
/* 142 */       this.networkAddress = ipToInt(ipAddress).and(this.subnetMask);
/* 143 */       this.ruleType = ruleType;
/*     */     }
/*     */     
/*     */     public boolean matches(InetSocketAddress remoteAddress)
/*     */     {
/* 148 */       BigInteger ipAddress = ipToInt((Inet6Address)remoteAddress.getAddress());
/*     */       
/* 150 */       return ipAddress.and(this.subnetMask).equals(this.networkAddress);
/*     */     }
/*     */     
/*     */     public IpFilterRuleType ruleType()
/*     */     {
/* 155 */       return this.ruleType;
/*     */     }
/*     */     
/*     */     private static BigInteger ipToInt(Inet6Address ipAddress) {
/* 159 */       byte[] octets = ipAddress.getAddress();
/* 160 */       assert (octets.length == 16);
/*     */       
/* 162 */       return new BigInteger(octets);
/*     */     }
/*     */     
/*     */     private static BigInteger prefixToSubnetMask(int cidrPrefix) {
/* 166 */       return MINUS_ONE.shiftLeft(128 - cidrPrefix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ipfilter\IpSubnetFilterRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */