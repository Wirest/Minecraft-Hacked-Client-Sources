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
/*    */ public class Socks5AuthMethod
/*    */   implements Comparable<Socks5AuthMethod>
/*    */ {
/* 24 */   public static final Socks5AuthMethod NO_AUTH = new Socks5AuthMethod(0, "NO_AUTH");
/* 25 */   public static final Socks5AuthMethod GSSAPI = new Socks5AuthMethod(1, "GSSAPI");
/* 26 */   public static final Socks5AuthMethod PASSWORD = new Socks5AuthMethod(2, "PASSWORD");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 31 */   public static final Socks5AuthMethod UNACCEPTED = new Socks5AuthMethod(255, "UNACCEPTED");
/*    */   private final byte byteValue;
/*    */   
/* 34 */   public static Socks5AuthMethod valueOf(byte b) { switch (b) {
/*    */     case 0: 
/* 36 */       return NO_AUTH;
/*    */     case 1: 
/* 38 */       return GSSAPI;
/*    */     case 2: 
/* 40 */       return PASSWORD;
/*    */     case -1: 
/* 42 */       return UNACCEPTED;
/*    */     }
/*    */     
/* 45 */     return new Socks5AuthMethod(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks5AuthMethod(int byteValue)
/*    */   {
/* 53 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks5AuthMethod(int byteValue, String name) {
/* 57 */     if (name == null) {
/* 58 */       throw new NullPointerException("name");
/*    */     }
/*    */     
/* 61 */     this.byteValue = ((byte)byteValue);
/* 62 */     this.name = name;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 66 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 71 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 76 */     if (!(obj instanceof Socks5AuthMethod)) {
/* 77 */       return false;
/*    */     }
/*    */     
/* 80 */     return this.byteValue == ((Socks5AuthMethod)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks5AuthMethod o)
/*    */   {
/* 85 */     return this.byteValue - o.byteValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 90 */     String text = this.text;
/* 91 */     if (text == null) {
/* 92 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*    */     }
/* 94 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5AuthMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */