/*     */ package io.netty.handler.codec.socksx.v5;
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
/*     */ public class Socks5CommandStatus
/*     */   implements Comparable<Socks5CommandStatus>
/*     */ {
/*  24 */   public static final Socks5CommandStatus SUCCESS = new Socks5CommandStatus(0, "SUCCESS");
/*  25 */   public static final Socks5CommandStatus FAILURE = new Socks5CommandStatus(1, "FAILURE");
/*  26 */   public static final Socks5CommandStatus FORBIDDEN = new Socks5CommandStatus(2, "FORBIDDEN");
/*  27 */   public static final Socks5CommandStatus NETWORK_UNREACHABLE = new Socks5CommandStatus(3, "NETWORK_UNREACHABLE");
/*  28 */   public static final Socks5CommandStatus HOST_UNREACHABLE = new Socks5CommandStatus(4, "HOST_UNREACHABLE");
/*  29 */   public static final Socks5CommandStatus CONNECTION_REFUSED = new Socks5CommandStatus(5, "CONNECTION_REFUSED");
/*  30 */   public static final Socks5CommandStatus TTL_EXPIRED = new Socks5CommandStatus(6, "TTL_EXPIRED");
/*  31 */   public static final Socks5CommandStatus COMMAND_UNSUPPORTED = new Socks5CommandStatus(7, "COMMAND_UNSUPPORTED");
/*  32 */   public static final Socks5CommandStatus ADDRESS_UNSUPPORTED = new Socks5CommandStatus(8, "ADDRESS_UNSUPPORTED");
/*     */   private final byte byteValue;
/*     */   
/*  35 */   public static Socks5CommandStatus valueOf(byte b) { switch (b) {
/*     */     case 0: 
/*  37 */       return SUCCESS;
/*     */     case 1: 
/*  39 */       return FAILURE;
/*     */     case 2: 
/*  41 */       return FORBIDDEN;
/*     */     case 3: 
/*  43 */       return NETWORK_UNREACHABLE;
/*     */     case 4: 
/*  45 */       return HOST_UNREACHABLE;
/*     */     case 5: 
/*  47 */       return CONNECTION_REFUSED;
/*     */     case 6: 
/*  49 */       return TTL_EXPIRED;
/*     */     case 7: 
/*  51 */       return COMMAND_UNSUPPORTED;
/*     */     case 8: 
/*  53 */       return ADDRESS_UNSUPPORTED;
/*     */     }
/*     */     
/*  56 */     return new Socks5CommandStatus(b);
/*     */   }
/*     */   
/*     */ 
/*     */   private final String name;
/*     */   private String text;
/*     */   public Socks5CommandStatus(int byteValue)
/*     */   {
/*  64 */     this(byteValue, "UNKNOWN");
/*     */   }
/*     */   
/*     */   public Socks5CommandStatus(int byteValue, String name) {
/*  68 */     if (name == null) {
/*  69 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/*  72 */     this.byteValue = ((byte)byteValue);
/*  73 */     this.name = name;
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/*  77 */     return this.byteValue;
/*     */   }
/*     */   
/*     */   public boolean isSuccess() {
/*  81 */     return this.byteValue == 0;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  86 */     return this.byteValue;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  91 */     if (!(obj instanceof Socks5CommandStatus)) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     return this.byteValue == ((Socks5CommandStatus)obj).byteValue;
/*     */   }
/*     */   
/*     */   public int compareTo(Socks5CommandStatus o)
/*     */   {
/* 100 */     return this.byteValue - o.byteValue;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 105 */     String text = this.text;
/* 106 */     if (text == null) {
/* 107 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*     */     }
/* 109 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */