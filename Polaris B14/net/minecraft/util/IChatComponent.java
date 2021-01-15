/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface IChatComponent
/*     */   extends Iterable<IChatComponent>
/*     */ {
/*     */   public abstract IChatComponent setChatStyle(ChatStyle paramChatStyle);
/*     */   
/*     */   public abstract ChatStyle getChatStyle();
/*     */   
/*     */   public abstract IChatComponent appendText(String paramString);
/*     */   
/*     */   public abstract IChatComponent appendSibling(IChatComponent paramIChatComponent);
/*     */   
/*     */   public abstract String getUnformattedTextForChat();
/*     */   
/*     */   public abstract String getUnformattedText();
/*     */   
/*     */   public abstract String getFormattedText();
/*     */   
/*     */   public abstract List<IChatComponent> getSiblings();
/*     */   
/*     */   public abstract IChatComponent createCopy();
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<IChatComponent>, JsonSerializer<IChatComponent>
/*     */   {
/*     */     private static final Gson GSON;
/*     */     
/*     */     public IChatComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
/*     */       throws JsonParseException
/*     */     {
/*  63 */       if (p_deserialize_1_.isJsonPrimitive())
/*     */       {
/*  65 */         return new ChatComponentText(p_deserialize_1_.getAsString());
/*     */       }
/*  67 */       if (!p_deserialize_1_.isJsonObject())
/*     */       {
/*  69 */         if (p_deserialize_1_.isJsonArray())
/*     */         {
/*  71 */           JsonArray jsonarray1 = p_deserialize_1_.getAsJsonArray();
/*  72 */           IChatComponent ichatcomponent1 = null;
/*     */           
/*  74 */           for (JsonElement jsonelement : jsonarray1)
/*     */           {
/*  76 */             IChatComponent ichatcomponent2 = deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
/*     */             
/*  78 */             if (ichatcomponent1 == null)
/*     */             {
/*  80 */               ichatcomponent1 = ichatcomponent2;
/*     */             }
/*     */             else
/*     */             {
/*  84 */               ichatcomponent1.appendSibling(ichatcomponent2);
/*     */             }
/*     */           }
/*     */           
/*  88 */           return ichatcomponent1;
/*     */         }
/*     */         
/*     */ 
/*  92 */         throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  97 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*     */       IChatComponent ichatcomponent;
/*     */       IChatComponent ichatcomponent;
/* 100 */       if (jsonobject.has("text"))
/*     */       {
/* 102 */         ichatcomponent = new ChatComponentText(jsonobject.get("text").getAsString());
/*     */       } else { IChatComponent ichatcomponent;
/* 104 */         if (jsonobject.has("translate"))
/*     */         {
/* 106 */           String s = jsonobject.get("translate").getAsString();
/*     */           IChatComponent ichatcomponent;
/* 108 */           if (jsonobject.has("with"))
/*     */           {
/* 110 */             JsonArray jsonarray = jsonobject.getAsJsonArray("with");
/* 111 */             Object[] aobject = new Object[jsonarray.size()];
/*     */             
/* 113 */             for (int i = 0; i < aobject.length; i++)
/*     */             {
/* 115 */               aobject[i] = deserialize(jsonarray.get(i), p_deserialize_2_, p_deserialize_3_);
/*     */               
/* 117 */               if ((aobject[i] instanceof ChatComponentText))
/*     */               {
/* 119 */                 ChatComponentText chatcomponenttext = (ChatComponentText)aobject[i];
/*     */                 
/* 121 */                 if ((chatcomponenttext.getChatStyle().isEmpty()) && (chatcomponenttext.getSiblings().isEmpty()))
/*     */                 {
/* 123 */                   aobject[i] = chatcomponenttext.getChatComponentText_TextValue();
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/* 128 */             ichatcomponent = new ChatComponentTranslation(s, aobject);
/*     */           }
/*     */           else
/*     */           {
/* 132 */             ichatcomponent = new ChatComponentTranslation(s, new Object[0]);
/*     */           }
/*     */         }
/* 135 */         else if (jsonobject.has("score"))
/*     */         {
/* 137 */           JsonObject jsonobject1 = jsonobject.getAsJsonObject("score");
/*     */           
/* 139 */           if ((!jsonobject1.has("name")) || (!jsonobject1.has("objective")))
/*     */           {
/* 141 */             throw new JsonParseException("A score component needs a least a name and an objective");
/*     */           }
/*     */           
/* 144 */           IChatComponent ichatcomponent = new ChatComponentScore(JsonUtils.getString(jsonobject1, "name"), JsonUtils.getString(jsonobject1, "objective"));
/*     */           
/* 146 */           if (jsonobject1.has("value"))
/*     */           {
/* 148 */             ((ChatComponentScore)ichatcomponent).setValue(JsonUtils.getString(jsonobject1, "value"));
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 153 */           if (!jsonobject.has("selector"))
/*     */           {
/* 155 */             throw new JsonParseException("Don't know how to turn " + p_deserialize_1_.toString() + " into a Component");
/*     */           }
/*     */           
/* 158 */           ichatcomponent = new ChatComponentSelector(JsonUtils.getString(jsonobject, "selector"));
/*     */         }
/*     */       }
/* 161 */       if (jsonobject.has("extra"))
/*     */       {
/* 163 */         JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");
/*     */         
/* 165 */         if (jsonarray2.size() <= 0)
/*     */         {
/* 167 */           throw new JsonParseException("Unexpected empty array of components");
/*     */         }
/*     */         
/* 170 */         for (int j = 0; j < jsonarray2.size(); j++)
/*     */         {
/* 172 */           ichatcomponent.appendSibling(deserialize(jsonarray2.get(j), p_deserialize_2_, p_deserialize_3_));
/*     */         }
/*     */       }
/*     */       
/* 176 */       ichatcomponent.setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, ChatStyle.class));
/* 177 */       return ichatcomponent;
/*     */     }
/*     */     
/*     */ 
/*     */     private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx)
/*     */     {
/* 183 */       JsonElement jsonelement = ctx.serialize(style);
/*     */       
/* 185 */       if (jsonelement.isJsonObject())
/*     */       {
/* 187 */         JsonObject jsonobject = (JsonObject)jsonelement;
/*     */         
/* 189 */         for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*     */         {
/* 191 */           object.add((String)entry.getKey(), (JsonElement)entry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonElement serialize(IChatComponent p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 198 */       if (((p_serialize_1_ instanceof ChatComponentText)) && (p_serialize_1_.getChatStyle().isEmpty()) && (p_serialize_1_.getSiblings().isEmpty()))
/*     */       {
/* 200 */         return new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
/*     */       }
/*     */       
/*     */ 
/* 204 */       JsonObject jsonobject = new JsonObject();
/*     */       
/* 206 */       if (!p_serialize_1_.getChatStyle().isEmpty())
/*     */       {
/* 208 */         serializeChatStyle(p_serialize_1_.getChatStyle(), jsonobject, p_serialize_3_);
/*     */       }
/*     */       
/* 211 */       if (!p_serialize_1_.getSiblings().isEmpty())
/*     */       {
/* 213 */         JsonArray jsonarray = new JsonArray();
/*     */         
/* 215 */         for (IChatComponent ichatcomponent : p_serialize_1_.getSiblings())
/*     */         {
/* 217 */           jsonarray.add(serialize(ichatcomponent, ichatcomponent.getClass(), p_serialize_3_));
/*     */         }
/*     */         
/* 220 */         jsonobject.add("extra", jsonarray);
/*     */       }
/*     */       
/* 223 */       if ((p_serialize_1_ instanceof ChatComponentText))
/*     */       {
/* 225 */         jsonobject.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
/*     */       }
/* 227 */       else if ((p_serialize_1_ instanceof ChatComponentTranslation))
/*     */       {
/* 229 */         ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation)p_serialize_1_;
/* 230 */         jsonobject.addProperty("translate", chatcomponenttranslation.getKey());
/*     */         
/* 232 */         if ((chatcomponenttranslation.getFormatArgs() != null) && (chatcomponenttranslation.getFormatArgs().length > 0))
/*     */         {
/* 234 */           JsonArray jsonarray1 = new JsonArray();
/*     */           Object[] arrayOfObject;
/* 236 */           int j = (arrayOfObject = chatcomponenttranslation.getFormatArgs()).length; for (int i = 0; i < j; i++) { Object object = arrayOfObject[i];
/*     */             
/* 238 */             if ((object instanceof IChatComponent))
/*     */             {
/* 240 */               jsonarray1.add(serialize((IChatComponent)object, object.getClass(), p_serialize_3_));
/*     */             }
/*     */             else
/*     */             {
/* 244 */               jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
/*     */             }
/*     */           }
/*     */           
/* 248 */           jsonobject.add("with", jsonarray1);
/*     */         }
/*     */       }
/* 251 */       else if ((p_serialize_1_ instanceof ChatComponentScore))
/*     */       {
/* 253 */         ChatComponentScore chatcomponentscore = (ChatComponentScore)p_serialize_1_;
/* 254 */         JsonObject jsonobject1 = new JsonObject();
/* 255 */         jsonobject1.addProperty("name", chatcomponentscore.getName());
/* 256 */         jsonobject1.addProperty("objective", chatcomponentscore.getObjective());
/* 257 */         jsonobject1.addProperty("value", chatcomponentscore.getUnformattedTextForChat());
/* 258 */         jsonobject.add("score", jsonobject1);
/*     */       }
/*     */       else
/*     */       {
/* 262 */         if (!(p_serialize_1_ instanceof ChatComponentSelector))
/*     */         {
/* 264 */           throw new IllegalArgumentException("Don't know how to serialize " + p_serialize_1_ + " as a Component");
/*     */         }
/*     */         
/* 267 */         ChatComponentSelector chatcomponentselector = (ChatComponentSelector)p_serialize_1_;
/* 268 */         jsonobject.addProperty("selector", chatcomponentselector.getSelector());
/*     */       }
/*     */       
/* 271 */       return jsonobject;
/*     */     }
/*     */     
/*     */ 
/*     */     public static String componentToJson(IChatComponent component)
/*     */     {
/* 277 */       return GSON.toJson(component);
/*     */     }
/*     */     
/*     */     public static IChatComponent jsonToComponent(String json)
/*     */     {
/* 282 */       return (IChatComponent)GSON.fromJson(json, IChatComponent.class);
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 287 */       GsonBuilder gsonbuilder = new GsonBuilder();
/* 288 */       gsonbuilder.registerTypeHierarchyAdapter(IChatComponent.class, new Serializer());
/* 289 */       gsonbuilder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
/* 290 */       gsonbuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
/* 291 */       GSON = gsonbuilder.create();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IChatComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */