/*     */ package net.minecraft.client.resources.data;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class AnimationMetadataSectionSerializer extends BaseMetadataSectionSerializer<AnimationMetadataSection> implements JsonSerializer<AnimationMetadataSection>
/*     */ {
/*     */   public AnimationMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */   {
/*  21 */     List<AnimationFrame> list = Lists.newArrayList();
/*  22 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "metadata section");
/*  23 */     int i = JsonUtils.getInt(jsonobject, "frametime", 1);
/*     */     
/*  25 */     if (i != 1)
/*     */     {
/*  27 */       Validate.inclusiveBetween(1L, 2147483647L, i, "Invalid default frame time");
/*     */     }
/*     */     
/*  30 */     if (jsonobject.has("frames"))
/*     */     {
/*     */       try
/*     */       {
/*  34 */         JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "frames");
/*     */         
/*  36 */         for (int j = 0; j < jsonarray.size(); j++)
/*     */         {
/*  38 */           JsonElement jsonelement = jsonarray.get(j);
/*  39 */           AnimationFrame animationframe = parseAnimationFrame(j, jsonelement);
/*     */           
/*  41 */           if (animationframe != null)
/*     */           {
/*  43 */             list.add(animationframe);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (ClassCastException classcastexception)
/*     */       {
/*  49 */         throw new JsonParseException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"), classcastexception);
/*     */       }
/*     */     }
/*     */     
/*  53 */     int k = JsonUtils.getInt(jsonobject, "width", -1);
/*  54 */     int l = JsonUtils.getInt(jsonobject, "height", -1);
/*     */     
/*  56 */     if (k != -1)
/*     */     {
/*  58 */       Validate.inclusiveBetween(1L, 2147483647L, k, "Invalid width");
/*     */     }
/*     */     
/*  61 */     if (l != -1)
/*     */     {
/*  63 */       Validate.inclusiveBetween(1L, 2147483647L, l, "Invalid height");
/*     */     }
/*     */     
/*  66 */     boolean flag = JsonUtils.getBoolean(jsonobject, "interpolate", false);
/*  67 */     return new AnimationMetadataSection(list, k, l, i, flag);
/*     */   }
/*     */   
/*     */   private AnimationFrame parseAnimationFrame(int p_110492_1_, JsonElement p_110492_2_)
/*     */   {
/*  72 */     if (p_110492_2_.isJsonPrimitive())
/*     */     {
/*  74 */       return new AnimationFrame(JsonUtils.getInt(p_110492_2_, "frames[" + p_110492_1_ + "]"));
/*     */     }
/*  76 */     if (p_110492_2_.isJsonObject())
/*     */     {
/*  78 */       JsonObject jsonobject = JsonUtils.getJsonObject(p_110492_2_, "frames[" + p_110492_1_ + "]");
/*  79 */       int i = JsonUtils.getInt(jsonobject, "time", -1);
/*     */       
/*  81 */       if (jsonobject.has("time"))
/*     */       {
/*  83 */         Validate.inclusiveBetween(1L, 2147483647L, i, "Invalid frame time");
/*     */       }
/*     */       
/*  86 */       int j = JsonUtils.getInt(jsonobject, "index");
/*  87 */       Validate.inclusiveBetween(0L, 2147483647L, j, "Invalid frame index");
/*  88 */       return new AnimationFrame(j, i);
/*     */     }
/*     */     
/*     */ 
/*  92 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonElement serialize(AnimationMetadataSection p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */   {
/*  98 */     JsonObject jsonobject = new JsonObject();
/*  99 */     jsonobject.addProperty("frametime", Integer.valueOf(p_serialize_1_.getFrameTime()));
/*     */     
/* 101 */     if (p_serialize_1_.getFrameWidth() != -1)
/*     */     {
/* 103 */       jsonobject.addProperty("width", Integer.valueOf(p_serialize_1_.getFrameWidth()));
/*     */     }
/*     */     
/* 106 */     if (p_serialize_1_.getFrameHeight() != -1)
/*     */     {
/* 108 */       jsonobject.addProperty("height", Integer.valueOf(p_serialize_1_.getFrameHeight()));
/*     */     }
/*     */     
/* 111 */     if (p_serialize_1_.getFrameCount() > 0)
/*     */     {
/* 113 */       JsonArray jsonarray = new JsonArray();
/*     */       
/* 115 */       for (int i = 0; i < p_serialize_1_.getFrameCount(); i++)
/*     */       {
/* 117 */         if (p_serialize_1_.frameHasTime(i))
/*     */         {
/* 119 */           JsonObject jsonobject1 = new JsonObject();
/* 120 */           jsonobject1.addProperty("index", Integer.valueOf(p_serialize_1_.getFrameIndex(i)));
/* 121 */           jsonobject1.addProperty("time", Integer.valueOf(p_serialize_1_.getFrameTimeSingle(i)));
/* 122 */           jsonarray.add(jsonobject1);
/*     */         }
/*     */         else
/*     */         {
/* 126 */           jsonarray.add(new JsonPrimitive(Integer.valueOf(p_serialize_1_.getFrameIndex(i))));
/*     */         }
/*     */       }
/*     */       
/* 130 */       jsonobject.add("frames", jsonarray);
/*     */     }
/*     */     
/* 133 */     return jsonobject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSectionName()
/*     */   {
/* 141 */     return "animation";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\AnimationMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */