/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import net.minecraft.util.JsonUtils;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class SoundListSerializer implements JsonDeserializer<SoundList>
/*    */ {
/*    */   public SoundList deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*    */   {
/* 17 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "entry");
/* 18 */     SoundList soundlist = new SoundList();
/* 19 */     soundlist.setReplaceExisting(JsonUtils.getBoolean(jsonobject, "replace", false));
/* 20 */     SoundCategory soundcategory = SoundCategory.getCategory(JsonUtils.getString(jsonobject, "category", SoundCategory.MASTER.getCategoryName()));
/* 21 */     soundlist.setSoundCategory(soundcategory);
/* 22 */     Validate.notNull(soundcategory, "Invalid category", new Object[0]);
/*    */     
/* 24 */     if (jsonobject.has("sounds"))
/*    */     {
/* 26 */       JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sounds");
/*    */       
/* 28 */       for (int i = 0; i < jsonarray.size(); i++)
/*    */       {
/* 30 */         JsonElement jsonelement = jsonarray.get(i);
/* 31 */         SoundList.SoundEntry soundlist$soundentry = new SoundList.SoundEntry();
/*    */         
/* 33 */         if (JsonUtils.isString(jsonelement))
/*    */         {
/* 35 */           soundlist$soundentry.setSoundEntryName(JsonUtils.getString(jsonelement, "sound"));
/*    */         }
/*    */         else
/*    */         {
/* 39 */           JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonelement, "sound");
/* 40 */           soundlist$soundentry.setSoundEntryName(JsonUtils.getString(jsonobject1, "name"));
/*    */           
/* 42 */           if (jsonobject1.has("type"))
/*    */           {
/* 44 */             SoundList.SoundEntry.Type soundlist$soundentry$type = SoundList.SoundEntry.Type.getType(JsonUtils.getString(jsonobject1, "type"));
/* 45 */             Validate.notNull(soundlist$soundentry$type, "Invalid type", new Object[0]);
/* 46 */             soundlist$soundentry.setSoundEntryType(soundlist$soundentry$type);
/*    */           }
/*    */           
/* 49 */           if (jsonobject1.has("volume"))
/*    */           {
/* 51 */             float f = JsonUtils.getFloat(jsonobject1, "volume");
/* 52 */             Validate.isTrue(f > 0.0F, "Invalid volume", new Object[0]);
/* 53 */             soundlist$soundentry.setSoundEntryVolume(f);
/*    */           }
/*    */           
/* 56 */           if (jsonobject1.has("pitch"))
/*    */           {
/* 58 */             float f1 = JsonUtils.getFloat(jsonobject1, "pitch");
/* 59 */             Validate.isTrue(f1 > 0.0F, "Invalid pitch", new Object[0]);
/* 60 */             soundlist$soundentry.setSoundEntryPitch(f1);
/*    */           }
/*    */           
/* 63 */           if (jsonobject1.has("weight"))
/*    */           {
/* 65 */             int j = JsonUtils.getInt(jsonobject1, "weight");
/* 66 */             Validate.isTrue(j > 0, "Invalid weight", new Object[0]);
/* 67 */             soundlist$soundentry.setSoundEntryWeight(j);
/*    */           }
/*    */           
/* 70 */           if (jsonobject1.has("stream"))
/*    */           {
/* 72 */             soundlist$soundentry.setStreaming(JsonUtils.getBoolean(jsonobject1, "stream"));
/*    */           }
/*    */         }
/*    */         
/* 76 */         soundlist.getSoundList().add(soundlist$soundentry);
/*    */       }
/*    */     }
/*    */     
/* 80 */     return soundlist;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundListSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */