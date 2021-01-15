/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.JsonUtils;
/*    */ 
/*    */ public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<TextureMetadataSection>
/*    */ {
/*    */   public TextureMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*    */   {
/* 17 */     JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 18 */     boolean flag = JsonUtils.getBoolean(jsonobject, "blur", false);
/* 19 */     boolean flag1 = JsonUtils.getBoolean(jsonobject, "clamp", false);
/* 20 */     List<Integer> list = Lists.newArrayList();
/*    */     
/* 22 */     if (jsonobject.has("mipmaps"))
/*    */     {
/*    */       try
/*    */       {
/* 26 */         JsonArray jsonarray = jsonobject.getAsJsonArray("mipmaps");
/*    */         
/* 28 */         for (int i = 0; i < jsonarray.size(); i++)
/*    */         {
/* 30 */           JsonElement jsonelement = jsonarray.get(i);
/*    */           
/* 32 */           if (jsonelement.isJsonPrimitive())
/*    */           {
/*    */             try
/*    */             {
/* 36 */               list.add(Integer.valueOf(jsonelement.getAsInt()));
/*    */             }
/*    */             catch (NumberFormatException numberformatexception)
/*    */             {
/* 40 */               throw new JsonParseException("Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement, numberformatexception);
/*    */             }
/*    */             
/* 43 */           } else if (jsonelement.isJsonObject())
/*    */           {
/* 45 */             throw new JsonParseException("Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement);
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (ClassCastException classcastexception)
/*    */       {
/* 51 */         throw new JsonParseException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"), classcastexception);
/*    */       }
/*    */     }
/*    */     
/* 55 */     return new TextureMetadataSection(flag, flag1, list);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSectionName()
/*    */   {
/* 63 */     return "texture";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\TextureMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */