/*     */ package io.netty.handler.codec.dns;
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
/*     */ public final class DnsClass
/*     */   implements Comparable<DnsClass>
/*     */ {
/*  26 */   public static final DnsClass IN = new DnsClass(1, "IN");
/*  27 */   public static final DnsClass CSNET = new DnsClass(2, "CSNET");
/*  28 */   public static final DnsClass CHAOS = new DnsClass(3, "CHAOS");
/*  29 */   public static final DnsClass HESIOD = new DnsClass(4, "HESIOD");
/*  30 */   public static final DnsClass NONE = new DnsClass(254, "NONE");
/*  31 */   public static final DnsClass ANY = new DnsClass(255, "ANY");
/*     */   
/*  33 */   private static final String EXPECTED = " (expected: " + IN + '(' + IN.intValue() + "), " + CSNET + '(' + CSNET.intValue() + "), " + CHAOS + '(' + CHAOS.intValue() + "), " + HESIOD + '(' + HESIOD.intValue() + "), " + NONE + '(' + NONE.intValue() + "), " + ANY + '(' + ANY.intValue() + "))";
/*     */   
/*     */ 
/*     */   private final int intValue;
/*     */   
/*     */   private final String name;
/*     */   
/*     */ 
/*     */   public static DnsClass valueOf(String name)
/*     */   {
/*  43 */     if (IN.name().equals(name)) {
/*  44 */       return IN;
/*     */     }
/*  46 */     if (NONE.name().equals(name)) {
/*  47 */       return NONE;
/*     */     }
/*  49 */     if (ANY.name().equals(name)) {
/*  50 */       return ANY;
/*     */     }
/*  52 */     if (CSNET.name().equals(name)) {
/*  53 */       return CSNET;
/*     */     }
/*  55 */     if (CHAOS.name().equals(name)) {
/*  56 */       return CHAOS;
/*     */     }
/*  58 */     if (HESIOD.name().equals(name)) {
/*  59 */       return HESIOD;
/*     */     }
/*     */     
/*  62 */     throw new IllegalArgumentException("name: " + name + EXPECTED);
/*     */   }
/*     */   
/*     */   public static DnsClass valueOf(int intValue) {
/*  66 */     switch (intValue) {
/*     */     case 1: 
/*  68 */       return IN;
/*     */     case 2: 
/*  70 */       return CSNET;
/*     */     case 3: 
/*  72 */       return CHAOS;
/*     */     case 4: 
/*  74 */       return HESIOD;
/*     */     case 254: 
/*  76 */       return NONE;
/*     */     case 255: 
/*  78 */       return ANY;
/*     */     }
/*  80 */     return new DnsClass(intValue, "UNKNOWN");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DnsClass valueOf(int clazz, String name)
/*     */   {
/*  91 */     return new DnsClass(clazz, name);
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
/*     */   private DnsClass(int intValue, String name)
/*     */   {
/* 105 */     if ((intValue & 0xFFFF) != intValue) {
/* 106 */       throw new IllegalArgumentException("intValue: " + intValue + " (expected: 0 ~ 65535)");
/*     */     }
/*     */     
/* 109 */     this.intValue = intValue;
/* 110 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String name()
/*     */   {
/* 117 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 124 */     return this.intValue;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 129 */     return this.intValue;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 134 */     return ((o instanceof DnsClass)) && (((DnsClass)o).intValue == this.intValue);
/*     */   }
/*     */   
/*     */   public int compareTo(DnsClass o)
/*     */   {
/* 139 */     return intValue() - o.intValue();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 144 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */