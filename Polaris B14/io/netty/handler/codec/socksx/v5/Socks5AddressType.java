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
/*    */ public class Socks5AddressType
/*    */   implements Comparable<Socks5AddressType>
/*    */ {
/* 24 */   public static final Socks5AddressType IPv4 = new Socks5AddressType(1, "IPv4");
/* 25 */   public static final Socks5AddressType DOMAIN = new Socks5AddressType(3, "DOMAIN");
/* 26 */   public static final Socks5AddressType IPv6 = new Socks5AddressType(4, "IPv6");
/*    */   private final byte byteValue;
/*    */   
/* 29 */   public static Socks5AddressType valueOf(byte b) { switch (b) {
/*    */     case 1: 
/* 31 */       return IPv4;
/*    */     case 3: 
/* 33 */       return DOMAIN;
/*    */     case 4: 
/* 35 */       return IPv6;
/*    */     }
/*    */     
/* 38 */     return new Socks5AddressType(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks5AddressType(int byteValue)
/*    */   {
/* 46 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks5AddressType(int byteValue, String name) {
/* 50 */     if (name == null) {
/* 51 */       throw new NullPointerException("name");
/*    */     }
/*    */     
/* 54 */     this.byteValue = ((byte)byteValue);
/* 55 */     this.name = name;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 59 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 64 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 69 */     if (!(obj instanceof Socks5AddressType)) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     return this.byteValue == ((Socks5AddressType)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks5AddressType o)
/*    */   {
/* 78 */     return this.byteValue - o.byteValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 83 */     String text = this.text;
/* 84 */     if (text == null) {
/* 85 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*    */     }
/* 87 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5AddressType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */