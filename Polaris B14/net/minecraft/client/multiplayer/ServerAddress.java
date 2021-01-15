/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ 
/*     */ public class ServerAddress
/*     */ {
/*     */   private final String ipAddress;
/*     */   private final int serverPort;
/*     */   
/*     */   private ServerAddress(String p_i1192_1_, int p_i1192_2_)
/*     */   {
/*  16 */     this.ipAddress = p_i1192_1_;
/*  17 */     this.serverPort = p_i1192_2_;
/*     */   }
/*     */   
/*     */   public String getIP()
/*     */   {
/*  22 */     return IDN.toASCII(this.ipAddress);
/*     */   }
/*     */   
/*     */   public int getPort()
/*     */   {
/*  27 */     return this.serverPort;
/*     */   }
/*     */   
/*     */   public static ServerAddress func_78860_a(String p_78860_0_)
/*     */   {
/*  32 */     if (p_78860_0_ == null)
/*     */     {
/*  34 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  38 */     String[] astring = p_78860_0_.split(":");
/*     */     
/*  40 */     if (p_78860_0_.startsWith("["))
/*     */     {
/*  42 */       int i = p_78860_0_.indexOf("]");
/*     */       
/*  44 */       if (i > 0)
/*     */       {
/*  46 */         String s = p_78860_0_.substring(1, i);
/*  47 */         String s1 = p_78860_0_.substring(i + 1).trim();
/*     */         
/*  49 */         if ((s1.startsWith(":")) && (s1.length() > 0))
/*     */         {
/*  51 */           s1 = s1.substring(1);
/*  52 */           astring = new String[] { s, s1 };
/*     */         }
/*     */         else
/*     */         {
/*  56 */           astring = new String[] { s };
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  61 */     if (astring.length > 2)
/*     */     {
/*  63 */       astring = new String[] { p_78860_0_ };
/*     */     }
/*     */     
/*  66 */     String s2 = astring[0];
/*  67 */     int j = astring.length > 1 ? parseIntWithDefault(astring[1], 25565) : 25565;
/*     */     
/*  69 */     if (j == 25565)
/*     */     {
/*  71 */       String[] astring1 = getServerAddress(s2);
/*  72 */       s2 = astring1[0];
/*  73 */       j = parseIntWithDefault(astring1[1], 25565);
/*     */     }
/*     */     
/*  76 */     return new ServerAddress(s2, j);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String[] getServerAddress(String p_78863_0_)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       String s = "com.sun.jndi.dns.DnsContextFactory";
/*  88 */       Class.forName("com.sun.jndi.dns.DnsContextFactory");
/*  89 */       Hashtable hashtable = new Hashtable();
/*  90 */       hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
/*  91 */       hashtable.put("java.naming.provider.url", "dns:");
/*  92 */       hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
/*  93 */       DirContext dircontext = new InitialDirContext(hashtable);
/*  94 */       Attributes attributes = dircontext.getAttributes("_minecraft._tcp." + p_78863_0_, new String[] { "SRV" });
/*  95 */       String[] astring = attributes.get("srv").get().toString().split(" ", 4);
/*  96 */       return new String[] { astring[3], astring[2] };
/*     */     }
/*     */     catch (Throwable var6) {}
/*     */     
/* 100 */     return tmp139_135;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int parseIntWithDefault(String p_78862_0_, int p_78862_1_)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       return Integer.parseInt(p_78862_0_.trim());
/*     */     }
/*     */     catch (Exception var3) {}
/*     */     
/* 112 */     return p_78862_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\ServerAddress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */