/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ 
/*    */ public abstract class BanEntry<T> extends UserListEntry<T>
/*    */ {
/* 10 */   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*    */   protected final Date banStartDate;
/*    */   protected final String bannedBy;
/*    */   protected final Date banEndDate;
/*    */   protected final String reason;
/*    */   
/*    */   public BanEntry(T valueIn, Date startDate, String banner, Date endDate, String banReason)
/*    */   {
/* 18 */     super(valueIn);
/* 19 */     this.banStartDate = (startDate == null ? new Date() : startDate);
/* 20 */     this.bannedBy = (banner == null ? "(Unknown)" : banner);
/* 21 */     this.banEndDate = endDate;
/* 22 */     this.reason = (banReason == null ? "Banned by an operator." : banReason);
/*    */   }
/*    */   
/*    */   protected BanEntry(T p_i1174_1_, JsonObject p_i1174_2_)
/*    */   {
/* 27 */     super(p_i1174_1_, p_i1174_2_);
/*    */     
/*    */     Date date;
/*    */     try
/*    */     {
/* 32 */       date = p_i1174_2_.has("created") ? dateFormat.parse(p_i1174_2_.get("created").getAsString()) : new Date();
/*    */     }
/*    */     catch (ParseException var7) {
/*    */       Date date;
/* 36 */       date = new Date();
/*    */     }
/*    */     
/* 39 */     this.banStartDate = date;
/* 40 */     this.bannedBy = (p_i1174_2_.has("source") ? p_i1174_2_.get("source").getAsString() : "(Unknown)");
/*    */     
/*    */     Date date1;
/*    */     try
/*    */     {
/* 45 */       date1 = p_i1174_2_.has("expires") ? dateFormat.parse(p_i1174_2_.get("expires").getAsString()) : null;
/*    */     }
/*    */     catch (ParseException var6) {
/*    */       Date date1;
/* 49 */       date1 = null;
/*    */     }
/*    */     
/* 52 */     this.banEndDate = date1;
/* 53 */     this.reason = (p_i1174_2_.has("reason") ? p_i1174_2_.get("reason").getAsString() : "Banned by an operator.");
/*    */   }
/*    */   
/*    */   public Date getBanEndDate()
/*    */   {
/* 58 */     return this.banEndDate;
/*    */   }
/*    */   
/*    */   public String getBanReason()
/*    */   {
/* 63 */     return this.reason;
/*    */   }
/*    */   
/*    */   boolean hasBanExpired()
/*    */   {
/* 68 */     return this.banEndDate == null ? false : this.banEndDate.before(new Date());
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data)
/*    */   {
/* 73 */     data.addProperty("created", dateFormat.format(this.banStartDate));
/* 74 */     data.addProperty("source", this.bannedBy);
/* 75 */     data.addProperty("expires", this.banEndDate == null ? "forever" : dateFormat.format(this.banEndDate));
/* 76 */     data.addProperty("reason", this.reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\BanEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */