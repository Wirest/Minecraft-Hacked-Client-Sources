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
/*    */ public class Socks5CommandType
/*    */   implements Comparable<Socks5CommandType>
/*    */ {
/* 24 */   public static final Socks5CommandType CONNECT = new Socks5CommandType(1, "CONNECT");
/* 25 */   public static final Socks5CommandType BIND = new Socks5CommandType(2, "BIND");
/* 26 */   public static final Socks5CommandType UDP_ASSOCIATE = new Socks5CommandType(3, "UDP_ASSOCIATE");
/*    */   private final byte byteValue;
/*    */   
/* 29 */   public static Socks5CommandType valueOf(byte b) { switch (b) {
/*    */     case 1: 
/* 31 */       return CONNECT;
/*    */     case 2: 
/* 33 */       return BIND;
/*    */     case 3: 
/* 35 */       return UDP_ASSOCIATE;
/*    */     }
/*    */     
/* 38 */     return new Socks5CommandType(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks5CommandType(int byteValue)
/*    */   {
/* 46 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks5CommandType(int byteValue, String name) {
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
/* 69 */     if (!(obj instanceof Socks5CommandType)) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     return this.byteValue == ((Socks5CommandType)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks5CommandType o)
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


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */