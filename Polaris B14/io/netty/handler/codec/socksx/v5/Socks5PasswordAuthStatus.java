/*    */ package io.netty.handler.codec.socksx.v5;
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
/*    */ 
/*    */ public class Socks5PasswordAuthStatus
/*    */   implements Comparable<Socks5PasswordAuthStatus>
/*    */ {
/* 24 */   public static final Socks5PasswordAuthStatus SUCCESS = new Socks5PasswordAuthStatus(0, "SUCCESS");
/* 25 */   public static final Socks5PasswordAuthStatus FAILURE = new Socks5PasswordAuthStatus(255, "FAILURE");
/*    */   private final byte byteValue;
/*    */   
/* 28 */   public static Socks5PasswordAuthStatus valueOf(byte b) { switch (b) {
/*    */     case 0: 
/* 30 */       return SUCCESS;
/*    */     case -1: 
/* 32 */       return FAILURE;
/*    */     }
/*    */     
/* 35 */     return new Socks5PasswordAuthStatus(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks5PasswordAuthStatus(int byteValue)
/*    */   {
/* 43 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks5PasswordAuthStatus(int byteValue, String name) {
/* 47 */     if (name == null) {
/* 48 */       throw new NullPointerException("name");
/*    */     }
/*    */     
/* 51 */     this.byteValue = ((byte)byteValue);
/* 52 */     this.name = name;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 56 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 60 */     return this.byteValue == 0;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 65 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 70 */     if (!(obj instanceof Socks5PasswordAuthStatus)) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     return this.byteValue == ((Socks5PasswordAuthStatus)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks5PasswordAuthStatus o)
/*    */   {
/* 79 */     return this.byteValue - o.byteValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 84 */     String text = this.text;
/* 85 */     if (text == null) {
/* 86 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*    */     }
/* 88 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5PasswordAuthStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */