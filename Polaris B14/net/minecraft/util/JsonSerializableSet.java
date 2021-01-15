/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ForwardingSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class JsonSerializableSet extends ForwardingSet<String> implements IJsonSerializable
/*    */ {
/* 12 */   private final Set<String> underlyingSet = Sets.newHashSet();
/*    */   
/*    */   public void fromJson(JsonElement json)
/*    */   {
/* 16 */     if (json.isJsonArray())
/*    */     {
/* 18 */       for (JsonElement jsonelement : json.getAsJsonArray())
/*    */       {
/* 20 */         add(jsonelement.getAsString());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonElement getSerializableElement()
/*    */   {
/* 30 */     JsonArray jsonarray = new JsonArray();
/*    */     
/* 32 */     for (String s : this)
/*    */     {
/* 34 */       jsonarray.add(new JsonPrimitive(s));
/*    */     }
/*    */     
/* 37 */     return jsonarray;
/*    */   }
/*    */   
/*    */   protected Set<String> delegate()
/*    */   {
/* 42 */     return this.underlyingSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\JsonSerializableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */