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
/*    */ public class Socks4CommandType
/*    */   implements Comparable<Socks4CommandType>
/*    */ {
/* 23 */   public static final Socks4CommandType CONNECT = new Socks4CommandType(1, "CONNECT");
/* 24 */   public static final Socks4CommandType BIND = new Socks4CommandType(2, "BIND");
/*    */   private final byte byteValue;
/*    */   
/* 27 */   public static Socks4CommandType valueOf(byte b) { switch (b) {
/*    */     case 1: 
/* 29 */       return CONNECT;
/*    */     case 2: 
/* 31 */       return BIND;
/*    */     }
/*    */     
/* 34 */     return new Socks4CommandType(b);
/*    */   }
/*    */   
/*    */ 
/*    */   private final String name;
/*    */   private String text;
/*    */   public Socks4CommandType(int byteValue)
/*    */   {
/* 42 */     this(byteValue, "UNKNOWN");
/*    */   }
/*    */   
/*    */   public Socks4CommandType(int byteValue, String name) {
/* 46 */     if (name == null) {
/* 47 */       throw new NullPointerException("name");
/*    */     }
/* 49 */     this.byteValue = ((byte)byteValue);
/* 50 */     this.name = name;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 54 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 59 */     return this.byteValue;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 64 */     if (!(obj instanceof Socks4CommandType)) {
/* 65 */       return false;
/*    */     }
/*    */     
/* 68 */     return this.byteValue == ((Socks4CommandType)obj).byteValue;
/*    */   }
/*    */   
/*    */   public int compareTo(Socks4CommandType o)
/*    */   {
/* 73 */     return this.byteValue - o.byteValue;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 78 */     String text = this.text;
/* 79 */     if (text == null) {
/* 80 */       this.text = (text = this.name + '(' + (this.byteValue & 0xFF) + ')');
/*    */     }
/* 82 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */