/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class IPBanEntry extends BanEntry<String>
/*    */ {
/*    */   public IPBanEntry(String p_i46330_1_)
/*    */   {
/* 10 */     this(p_i46330_1_, null, null, null, null);
/*    */   }
/*    */   
/*    */   public IPBanEntry(String p_i1159_1_, Date startDate, String banner, Date endDate, String p_i1159_5_)
/*    */   {
/* 15 */     super(p_i1159_1_, startDate, banner, endDate, p_i1159_5_);
/*    */   }
/*    */   
/*    */   public IPBanEntry(JsonObject p_i46331_1_)
/*    */   {
/* 20 */     super(getIPFromJson(p_i46331_1_), p_i46331_1_);
/*    */   }
/*    */   
/*    */   private static String getIPFromJson(JsonObject json)
/*    */   {
/* 25 */     return json.has("ip") ? json.get("ip").getAsString() : null;
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data)
/*    */   {
/* 30 */     if (getValue() != null)
/*    */     {
/* 32 */       data.addProperty("ip", (String)getValue());
/* 33 */       super.onSerialization(data);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\IPBanEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */