/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.resources.Language;
/*    */ import net.minecraft.util.JsonUtils;
/*    */ 
/*    */ public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer<LanguageMetadataSection>
/*    */ {
/*    */   public LanguageMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*    */   {
/* 18 */     JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 19 */     Set<Language> set = Sets.newHashSet();
/*    */     
/* 21 */     for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*    */     {
/* 23 */       String s = (String)entry.getKey();
/* 24 */       JsonObject jsonobject1 = JsonUtils.getJsonObject((JsonElement)entry.getValue(), "language");
/* 25 */       String s1 = JsonUtils.getString(jsonobject1, "region");
/* 26 */       String s2 = JsonUtils.getString(jsonobject1, "name");
/* 27 */       boolean flag = JsonUtils.getBoolean(jsonobject1, "bidirectional", false);
/*    */       
/* 29 */       if (s1.isEmpty())
/*    */       {
/* 31 */         throw new JsonParseException("Invalid language->'" + s + "'->region: empty value");
/*    */       }
/*    */       
/* 34 */       if (s2.isEmpty())
/*    */       {
/* 36 */         throw new JsonParseException("Invalid language->'" + s + "'->name: empty value");
/*    */       }
/*    */       
/* 39 */       if (!set.add(new Language(s, s1, s2, flag)))
/*    */       {
/* 41 */         throw new JsonParseException("Duplicate language->'" + s + "' defined");
/*    */       }
/*    */     }
/*    */     
/* 45 */     return new LanguageMetadataSection(set);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSectionName()
/*    */   {
/* 53 */     return "language";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\LanguageMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */