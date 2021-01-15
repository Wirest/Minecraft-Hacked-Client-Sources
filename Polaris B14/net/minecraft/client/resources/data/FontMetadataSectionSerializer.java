/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.util.JsonUtils;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class FontMetadataSectionSerializer extends BaseMetadataSectionSerializer<FontMetadataSection>
/*    */ {
/*    */   public FontMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*    */   {
/* 15 */     JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 16 */     float[] afloat = new float['Ā'];
/* 17 */     float[] afloat1 = new float['Ā'];
/* 18 */     float[] afloat2 = new float['Ā'];
/* 19 */     float f = 1.0F;
/* 20 */     float f1 = 0.0F;
/* 21 */     float f2 = 0.0F;
/*    */     
/* 23 */     if (jsonobject.has("characters"))
/*    */     {
/* 25 */       if (!jsonobject.get("characters").isJsonObject())
/*    */       {
/* 27 */         throw new JsonParseException("Invalid font->characters: expected object, was " + jsonobject.get("characters"));
/*    */       }
/*    */       
/* 30 */       JsonObject jsonobject1 = jsonobject.getAsJsonObject("characters");
/*    */       
/* 32 */       if (jsonobject1.has("default"))
/*    */       {
/* 34 */         if (!jsonobject1.get("default").isJsonObject())
/*    */         {
/* 36 */           throw new JsonParseException("Invalid font->characters->default: expected object, was " + jsonobject1.get("default"));
/*    */         }
/*    */         
/* 39 */         JsonObject jsonobject2 = jsonobject1.getAsJsonObject("default");
/* 40 */         f = JsonUtils.getFloat(jsonobject2, "width", f);
/* 41 */         Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f, "Invalid default width");
/* 42 */         f1 = JsonUtils.getFloat(jsonobject2, "spacing", f1);
/* 43 */         Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f1, "Invalid default spacing");
/* 44 */         f2 = JsonUtils.getFloat(jsonobject2, "left", f1);
/* 45 */         Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f2, "Invalid default left");
/*    */       }
/*    */       
/* 48 */       for (int i = 0; i < 256; i++)
/*    */       {
/* 50 */         JsonElement jsonelement = jsonobject1.get(Integer.toString(i));
/* 51 */         float f3 = f;
/* 52 */         float f4 = f1;
/* 53 */         float f5 = f2;
/*    */         
/* 55 */         if (jsonelement != null)
/*    */         {
/* 57 */           JsonObject jsonobject3 = JsonUtils.getJsonObject(jsonelement, "characters[" + i + "]");
/* 58 */           f3 = JsonUtils.getFloat(jsonobject3, "width", f);
/* 59 */           Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f3, "Invalid width");
/* 60 */           f4 = JsonUtils.getFloat(jsonobject3, "spacing", f1);
/* 61 */           Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f4, "Invalid spacing");
/* 62 */           f5 = JsonUtils.getFloat(jsonobject3, "left", f2);
/* 63 */           Validate.inclusiveBetween(0.0D, 3.4028234663852886E38D, f5, "Invalid left");
/*    */         }
/*    */         
/* 66 */         afloat[i] = f3;
/* 67 */         afloat1[i] = f4;
/* 68 */         afloat2[i] = f5;
/*    */       }
/*    */     }
/*    */     
/* 72 */     return new FontMetadataSection(afloat, afloat2, afloat1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSectionName()
/*    */   {
/* 80 */     return "font";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\FontMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */