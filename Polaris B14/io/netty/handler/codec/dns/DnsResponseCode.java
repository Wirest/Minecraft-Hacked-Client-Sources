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
/*     */ 
/*     */ public final class DnsResponseCode
/*     */   implements Comparable<DnsResponseCode>
/*     */ {
/*  27 */   public static final DnsResponseCode NOERROR = new DnsResponseCode(0, "no error");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final DnsResponseCode FORMERROR = new DnsResponseCode(1, "format error");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  37 */   public static final DnsResponseCode SERVFAIL = new DnsResponseCode(2, "server failure");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   public static final DnsResponseCode NXDOMAIN = new DnsResponseCode(3, "name error");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   public static final DnsResponseCode NOTIMPL = new DnsResponseCode(4, "not implemented");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static final DnsResponseCode REFUSED = new DnsResponseCode(5, "operation refused");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   public static final DnsResponseCode YXDOMAIN = new DnsResponseCode(6, "domain name should not exist");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  62 */   public static final DnsResponseCode YXRRSET = new DnsResponseCode(7, "resource record set should not exist");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   public static final DnsResponseCode NXRRSET = new DnsResponseCode(8, "rrset does not exist");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public static final DnsResponseCode NOTAUTH = new DnsResponseCode(9, "not authoritative for zone");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  77 */   public static final DnsResponseCode NOTZONE = new DnsResponseCode(10, "name not in zone");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final DnsResponseCode BADVERS = new DnsResponseCode(11, "bad extension mechanism for version");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  87 */   public static final DnsResponseCode BADSIG = new DnsResponseCode(12, "bad signature");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   public static final DnsResponseCode BADKEY = new DnsResponseCode(13, "bad key");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  97 */   public static final DnsResponseCode BADTIME = new DnsResponseCode(14, "bad timestamp");
/*     */   
/*     */ 
/*     */ 
/*     */   private final int errorCode;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String message;
/*     */   
/*     */ 
/*     */ 
/*     */   public static DnsResponseCode valueOf(int responseCode)
/*     */   {
/* 111 */     switch (responseCode) {
/*     */     case 0: 
/* 113 */       return NOERROR;
/*     */     case 1: 
/* 115 */       return FORMERROR;
/*     */     case 2: 
/* 117 */       return SERVFAIL;
/*     */     case 3: 
/* 119 */       return NXDOMAIN;
/*     */     case 4: 
/* 121 */       return NOTIMPL;
/*     */     case 5: 
/* 123 */       return REFUSED;
/*     */     case 6: 
/* 125 */       return YXDOMAIN;
/*     */     case 7: 
/* 127 */       return YXRRSET;
/*     */     case 8: 
/* 129 */       return NXRRSET;
/*     */     case 9: 
/* 131 */       return NOTAUTH;
/*     */     case 10: 
/* 133 */       return NOTZONE;
/*     */     case 11: 
/* 135 */       return BADVERS;
/*     */     case 12: 
/* 137 */       return BADSIG;
/*     */     case 13: 
/* 139 */       return BADKEY;
/*     */     case 14: 
/* 141 */       return BADTIME;
/*     */     }
/* 143 */     return new DnsResponseCode(responseCode, null);
/*     */   }
/*     */   
/*     */   public DnsResponseCode(int errorCode, String message)
/*     */   {
/* 148 */     this.errorCode = errorCode;
/* 149 */     this.message = message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int code()
/*     */   {
/* 156 */     return this.errorCode;
/*     */   }
/*     */   
/*     */   public int compareTo(DnsResponseCode o)
/*     */   {
/* 161 */     return code() - o.code();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 166 */     return code();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 174 */     if (!(o instanceof DnsResponseCode)) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     return code() == ((DnsResponseCode)o).code();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 186 */     if (this.message == null) {
/* 187 */       return "DnsResponseCode(" + this.errorCode + ')';
/*     */     }
/* 189 */     return "DnsResponseCode(" + this.errorCode + ", " + this.message + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsResponseCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */