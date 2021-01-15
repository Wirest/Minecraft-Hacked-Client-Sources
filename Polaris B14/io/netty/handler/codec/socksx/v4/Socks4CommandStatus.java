/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Socks4CommandStatus
/*    */   implements Comparable<Socks4CommandStatus>
/*    */ {
/* 23 */   public static final Socks4CommandStatus SUCCESS = new Socks4CommandStatus(90, "SUCCESS");
/* 24 */   public static final Socks4CommandStatus REJECTED_OR_FAILED = new Socks4CommandStatus(91, "REJECTED_OR_FAILED");
/* 25 */   public static final Socks4CommandStatus IDENTD_UNREACHABLE = new Socks4CommandStatus(92, "IDENTD_UNREACHABLE");
/* 26 */   public static final Socks4CommandStatus IDENTD_AUTH_FAILURE = new Socks4CommandStatus(93, "IDENTD_AUTH_FAILURE");
/*    */   private final byte byteValue;
/*    */   
/* 29 */   public static Socks4CommandStatus valueOf(byte b) { switch (b) {
/*    */     case 90: 
/* 31 */       return SUCCESS;
/*    */     case 91: 
/* 33 */       return REJECTED_OR_FAILED;
/*    */     case 92: 
/* 35 */       return IDENTD_UNREACHABLE;
/*    */     case 93: 
/* 37 */       return IDENTD_AUTH_FAILURE;
/*    */     }
/*    */     
/* 40 */     return new Socks4CommandStatus(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks4CommandStatus(int byteValue)
/*    */   {
/* 48 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks4CommandStatus(int byteValue, String name) {
/* 52 */     if (name == null) {
/* 53 */       throw new NullPointerException("name");
/*    */     }
/*    */     
/* 56 */     this.byteValue = ((byte)byteValue);
/* 57 */     this.name = name;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 61 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 65 */     return this.byteValue == 90;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 70 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 75 */     if (!(obj instanceof Socks4CommandStatus)) {
/* 76 */       return false;
/*    */     }
/*    */     
/* 79 */     return this.byteValue == ((Socks4CommandStatus)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks4CommandStatus o)
/*    */   {
/* 84 */     return this.byteValue - o.byteValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 89 */     String text = this.text;
/* 90 */     if (text == null) {
/* 91 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*    */     }
/* 93 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */