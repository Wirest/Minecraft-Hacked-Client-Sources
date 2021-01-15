/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class UserListEntry<T>
/*    */ {
/*    */   private final T value;
/*    */   
/*    */   public UserListEntry(T p_i1146_1_)
/*    */   {
/* 11 */     this.value = p_i1146_1_;
/*    */   }
/*    */   
/*    */   protected UserListEntry(T p_i1147_1_, JsonObject p_i1147_2_)
/*    */   {
/* 16 */     this.value = p_i1147_1_;
/*    */   }
/*    */   
/*    */   T getValue()
/*    */   {
/* 21 */     return (T)this.value;
/*    */   }
/*    */   
/*    */   boolean hasBanExpired()
/*    */   {
/* 26 */     return false;
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */