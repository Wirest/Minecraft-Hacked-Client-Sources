/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ 
/*    */ public class DnsEntry
/*    */ {
/*    */   private final String name;
/*    */   private final DnsType type;
/*    */   private final DnsClass dnsClass;
/*    */   
/*    */   DnsEntry(String name, DnsType type, DnsClass dnsClass)
/*    */   {
/* 32 */     if (name == null) {
/* 33 */       throw new NullPointerException("name");
/*    */     }
/* 35 */     if (type == null) {
/* 36 */       throw new NullPointerException("type");
/*    */     }
/* 38 */     if (dnsClass == null) {
/* 39 */       throw new NullPointerException("dnsClass");
/*    */     }
/*    */     
/* 42 */     this.name = name;
/* 43 */     this.type = type;
/* 44 */     this.dnsClass = dnsClass;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String name()
/*    */   {
/* 51 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DnsType type()
/*    */   {
/* 58 */     return this.type;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public DnsClass dnsClass()
/*    */   {
/* 65 */     return this.dnsClass;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 70 */     return (this.name.hashCode() * 31 + this.type.hashCode()) * 31 + this.dnsClass.hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 75 */     return 128 + StringUtil.simpleClassName(this) + "(name: " + this.name + ", type: " + this.type + ", class: " + this.dnsClass + ')';
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 84 */     if (this == o) {
/* 85 */       return true;
/*    */     }
/* 87 */     if (!(o instanceof DnsEntry)) {
/* 88 */       return false;
/*    */     }
/*    */     
/* 91 */     DnsEntry that = (DnsEntry)o;
/* 92 */     return (type().intValue() == that.type().intValue()) && (dnsClass().intValue() == that.dnsClass().intValue()) && (name().equals(that.name()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */