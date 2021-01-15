/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public class BanList extends UserList<String, IPBanEntry>
/*    */ {
/*    */   public BanList(File bansFile)
/*    */   {
/* 11 */     super(bansFile);
/*    */   }
/*    */   
/*    */   protected UserListEntry<String> createEntry(JsonObject entryData)
/*    */   {
/* 16 */     return new IPBanEntry(entryData);
/*    */   }
/*    */   
/*    */   public boolean isBanned(SocketAddress address)
/*    */   {
/* 21 */     String s = addressToString(address);
/* 22 */     return hasEntry(s);
/*    */   }
/*    */   
/*    */   public IPBanEntry getBanEntry(SocketAddress address)
/*    */   {
/* 27 */     String s = addressToString(address);
/* 28 */     return (IPBanEntry)getEntry(s);
/*    */   }
/*    */   
/*    */   private String addressToString(SocketAddress address)
/*    */   {
/* 33 */     String s = address.toString();
/*    */     
/* 35 */     if (s.contains("/"))
/*    */     {
/* 37 */       s = s.substring(s.indexOf('/') + 1);
/*    */     }
/*    */     
/* 40 */     if (s.contains(":"))
/*    */     {
/* 42 */       s = s.substring(0, s.indexOf(':'));
/*    */     }
/*    */     
/* 45 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\BanList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */