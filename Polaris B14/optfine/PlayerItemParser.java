/*     */ package optfine;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.awt.Dimension;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class PlayerItemParser
/*     */ {
/*  20 */   private static JsonParser jsonParser = new JsonParser();
/*     */   public static final String ITEM_TYPE = "type";
/*     */   public static final String ITEM_TEXTURE_SIZE = "textureSize";
/*     */   public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
/*     */   public static final String ITEM_MODELS = "models";
/*     */   public static final String MODEL_ID = "id";
/*     */   public static final String MODEL_BASE_ID = "baseId";
/*     */   public static final String MODEL_TYPE = "type";
/*     */   public static final String MODEL_ATTACH_TO = "attachTo";
/*     */   public static final String MODEL_INVERT_AXIS = "invertAxis";
/*     */   public static final String MODEL_MIRROR_TEXTURE = "mirrorTexture";
/*     */   public static final String MODEL_TRANSLATE = "translate";
/*     */   public static final String MODEL_ROTATE = "rotate";
/*     */   public static final String MODEL_SCALE = "scale";
/*     */   public static final String MODEL_BOXES = "boxes";
/*     */   public static final String MODEL_SPRITES = "sprites";
/*     */   public static final String MODEL_SUBMODEL = "submodel";
/*     */   public static final String MODEL_SUBMODELS = "submodels";
/*     */   public static final String BOX_TEXTURE_OFFSET = "textureOffset";
/*     */   public static final String BOX_COORDINATES = "coordinates";
/*     */   public static final String BOX_SIZE_ADD = "sizeAdd";
/*     */   public static final String ITEM_TYPE_MODEL = "PlayerItem";
/*     */   public static final String MODEL_TYPE_BOX = "ModelBox";
/*     */   
/*     */   public static PlayerItemModel parseItemModel(JsonObject p_parseItemModel_0_)
/*     */   {
/*  46 */     String s = Json.getString(p_parseItemModel_0_, "type");
/*     */     
/*  48 */     if (!Config.equals(s, "PlayerItem"))
/*     */     {
/*  50 */       throw new JsonParseException("Unknown model type: " + s);
/*     */     }
/*     */     
/*     */ 
/*  54 */     int[] aint = Json.parseIntArray(p_parseItemModel_0_.get("textureSize"), 2);
/*  55 */     checkNull(aint, "Missing texture size");
/*  56 */     Dimension dimension = new Dimension(aint[0], aint[1]);
/*  57 */     boolean flag = Json.getBoolean(p_parseItemModel_0_, "usePlayerTexture", false);
/*  58 */     JsonArray jsonarray = (JsonArray)p_parseItemModel_0_.get("models");
/*  59 */     checkNull(jsonarray, "Missing elements");
/*  60 */     Map map = new HashMap();
/*  61 */     List list = new ArrayList();
/*  62 */     new ArrayList();
/*     */     
/*  64 */     for (int i = 0; i < jsonarray.size(); i++)
/*     */     {
/*  66 */       JsonObject jsonobject = (JsonObject)jsonarray.get(i);
/*  67 */       String s1 = Json.getString(jsonobject, "baseId");
/*     */       
/*  69 */       if (s1 != null)
/*     */       {
/*  71 */         JsonObject jsonobject1 = (JsonObject)map.get(s1);
/*     */         
/*  73 */         if (jsonobject1 == null)
/*     */         {
/*  75 */           Config.warn("BaseID not found: " + s1);
/*     */         }
/*     */         else
/*     */         {
/*  79 */           for (Map.Entry<String, JsonElement> entry : jsonobject1.entrySet())
/*     */           {
/*  81 */             if (!jsonobject.has((String)entry.getKey()))
/*     */             {
/*  83 */               jsonobject.add((String)entry.getKey(), (JsonElement)entry.getValue());
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/*  88 */         String s2 = Json.getString(jsonobject, "id");
/*     */         
/*  90 */         if (s2 != null)
/*     */         {
/*  92 */           if (!map.containsKey(s2))
/*     */           {
/*  94 */             map.put(s2, jsonobject);
/*     */           }
/*     */           else
/*     */           {
/*  98 */             Config.warn("Duplicate model ID: " + s2);
/*     */           }
/*     */         }
/*     */         
/* 102 */         PlayerItemRenderer playeritemrenderer = parseItemRenderer(jsonobject, dimension);
/*     */         
/* 104 */         if (playeritemrenderer != null)
/*     */         {
/* 106 */           list.add(playeritemrenderer);
/*     */         }
/*     */       }
/*     */     }
/* 110 */     PlayerItemRenderer[] aplayeritemrenderer = (PlayerItemRenderer[])list.toArray(new PlayerItemRenderer[list.size()]);
/* 111 */     return new PlayerItemModel(dimension, flag, aplayeritemrenderer);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void checkNull(Object p_checkNull_0_, String p_checkNull_1_)
/*     */   {
/* 117 */     if (p_checkNull_0_ == null)
/*     */     {
/* 119 */       throw new JsonParseException(p_checkNull_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   private static ResourceLocation makeResourceLocation(String p_makeResourceLocation_0_)
/*     */   {
/* 125 */     int i = p_makeResourceLocation_0_.indexOf(':');
/*     */     
/* 127 */     if (i < 0)
/*     */     {
/* 129 */       return new ResourceLocation(p_makeResourceLocation_0_);
/*     */     }
/*     */     
/*     */ 
/* 133 */     String s = p_makeResourceLocation_0_.substring(0, i);
/* 134 */     String s1 = p_makeResourceLocation_0_.substring(i + 1);
/* 135 */     return new ResourceLocation(s, s1);
/*     */   }
/*     */   
/*     */ 
/*     */   private static int parseAttachModel(String p_parseAttachModel_0_)
/*     */   {
/* 141 */     if (p_parseAttachModel_0_ == null)
/*     */     {
/* 143 */       return 0;
/*     */     }
/* 145 */     if (p_parseAttachModel_0_.equals("body"))
/*     */     {
/* 147 */       return 0;
/*     */     }
/* 149 */     if (p_parseAttachModel_0_.equals("head"))
/*     */     {
/* 151 */       return 1;
/*     */     }
/* 153 */     if (p_parseAttachModel_0_.equals("leftArm"))
/*     */     {
/* 155 */       return 2;
/*     */     }
/* 157 */     if (p_parseAttachModel_0_.equals("rightArm"))
/*     */     {
/* 159 */       return 3;
/*     */     }
/* 161 */     if (p_parseAttachModel_0_.equals("leftLeg"))
/*     */     {
/* 163 */       return 4;
/*     */     }
/* 165 */     if (p_parseAttachModel_0_.equals("rightLeg"))
/*     */     {
/* 167 */       return 5;
/*     */     }
/* 169 */     if (p_parseAttachModel_0_.equals("cape"))
/*     */     {
/* 171 */       return 6;
/*     */     }
/*     */     
/*     */ 
/* 175 */     Config.warn("Unknown attachModel: " + p_parseAttachModel_0_);
/* 176 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   private static PlayerItemRenderer parseItemRenderer(JsonObject p_parseItemRenderer_0_, Dimension p_parseItemRenderer_1_)
/*     */   {
/* 182 */     String s = Json.getString(p_parseItemRenderer_0_, "type");
/*     */     
/* 184 */     if (!Config.equals(s, "ModelBox"))
/*     */     {
/* 186 */       Config.warn("Unknown model type: " + s);
/* 187 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 191 */     String s1 = Json.getString(p_parseItemRenderer_0_, "attachTo");
/* 192 */     int i = parseAttachModel(s1);
/* 193 */     float f = Json.getFloat(p_parseItemRenderer_0_, "scale", 1.0F);
/* 194 */     ModelBase modelbase = new ModelPlayerItem();
/* 195 */     modelbase.textureWidth = p_parseItemRenderer_1_.width;
/* 196 */     modelbase.textureHeight = p_parseItemRenderer_1_.height;
/* 197 */     ModelRenderer modelrenderer = parseModelRenderer(p_parseItemRenderer_0_, modelbase);
/* 198 */     PlayerItemRenderer playeritemrenderer = new PlayerItemRenderer(i, f, modelrenderer);
/* 199 */     return playeritemrenderer;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ModelRenderer parseModelRenderer(JsonObject p_parseModelRenderer_0_, ModelBase p_parseModelRenderer_1_)
/*     */   {
/* 205 */     ModelRenderer modelrenderer = new ModelRenderer(p_parseModelRenderer_1_);
/* 206 */     String s = Json.getString(p_parseModelRenderer_0_, "invertAxis", "").toLowerCase();
/* 207 */     boolean flag = s.contains("x");
/* 208 */     boolean flag1 = s.contains("y");
/* 209 */     boolean flag2 = s.contains("z");
/* 210 */     float[] afloat = Json.parseFloatArray(p_parseModelRenderer_0_.get("translate"), 3, new float[3]);
/*     */     
/* 212 */     if (flag)
/*     */     {
/* 214 */       afloat[0] = (-afloat[0]);
/*     */     }
/*     */     
/* 217 */     if (flag1)
/*     */     {
/* 219 */       afloat[1] = (-afloat[1]);
/*     */     }
/*     */     
/* 222 */     if (flag2)
/*     */     {
/* 224 */       afloat[2] = (-afloat[2]);
/*     */     }
/*     */     
/* 227 */     float[] afloat1 = Json.parseFloatArray(p_parseModelRenderer_0_.get("rotate"), 3, new float[3]);
/*     */     
/* 229 */     for (int i = 0; i < afloat1.length; i++)
/*     */     {
/* 231 */       afloat1[i] = (afloat1[i] / 180.0F * 3.1415927F);
/*     */     }
/*     */     
/* 234 */     if (flag)
/*     */     {
/* 236 */       afloat1[0] = (-afloat1[0]);
/*     */     }
/*     */     
/* 239 */     if (flag1)
/*     */     {
/* 241 */       afloat1[1] = (-afloat1[1]);
/*     */     }
/*     */     
/* 244 */     if (flag2)
/*     */     {
/* 246 */       afloat1[2] = (-afloat1[2]);
/*     */     }
/*     */     
/* 249 */     modelrenderer.setRotationPoint(afloat[0], afloat[1], afloat[2]);
/* 250 */     modelrenderer.rotateAngleX = afloat1[0];
/* 251 */     modelrenderer.rotateAngleY = afloat1[1];
/* 252 */     modelrenderer.rotateAngleZ = afloat1[2];
/* 253 */     String s1 = Json.getString(p_parseModelRenderer_0_, "mirrorTexture", "").toLowerCase();
/* 254 */     boolean flag3 = s1.contains("u");
/* 255 */     boolean flag4 = s1.contains("v");
/*     */     
/* 257 */     if (flag3)
/*     */     {
/* 259 */       modelrenderer.mirror = true;
/*     */     }
/*     */     
/* 262 */     if (flag4)
/*     */     {
/* 264 */       modelrenderer.mirrorV = true;
/*     */     }
/*     */     
/* 267 */     JsonArray jsonarray = p_parseModelRenderer_0_.getAsJsonArray("boxes");
/*     */     
/* 269 */     if (jsonarray != null)
/*     */     {
/* 271 */       for (int j = 0; j < jsonarray.size(); j++)
/*     */       {
/* 273 */         JsonObject jsonobject = jsonarray.get(j).getAsJsonObject();
/* 274 */         int[] aint = Json.parseIntArray(jsonobject.get("textureOffset"), 2);
/*     */         
/* 276 */         if (aint == null)
/*     */         {
/* 278 */           throw new JsonParseException("Texture offset not specified");
/*     */         }
/*     */         
/* 281 */         float[] afloat2 = Json.parseFloatArray(jsonobject.get("coordinates"), 6);
/*     */         
/* 283 */         if (afloat2 == null)
/*     */         {
/* 285 */           throw new JsonParseException("Coordinates not specified");
/*     */         }
/*     */         
/* 288 */         if (flag)
/*     */         {
/* 290 */           afloat2[0] = (-afloat2[0] - afloat2[3]);
/*     */         }
/*     */         
/* 293 */         if (flag1)
/*     */         {
/* 295 */           afloat2[1] = (-afloat2[1] - afloat2[4]);
/*     */         }
/*     */         
/* 298 */         if (flag2)
/*     */         {
/* 300 */           afloat2[2] = (-afloat2[2] - afloat2[5]);
/*     */         }
/*     */         
/* 303 */         float f = Json.getFloat(jsonobject, "sizeAdd", 0.0F);
/* 304 */         modelrenderer.setTextureOffset(aint[0], aint[1]);
/* 305 */         modelrenderer.addBox(afloat2[0], afloat2[1], afloat2[2], (int)afloat2[3], (int)afloat2[4], (int)afloat2[5], f);
/*     */       }
/*     */     }
/*     */     
/* 309 */     JsonArray jsonarray1 = p_parseModelRenderer_0_.getAsJsonArray("sprites");
/*     */     
/* 311 */     if (jsonarray1 != null)
/*     */     {
/* 313 */       for (int k = 0; k < jsonarray1.size(); k++)
/*     */       {
/* 315 */         JsonObject jsonobject2 = jsonarray1.get(k).getAsJsonObject();
/* 316 */         int[] aint1 = Json.parseIntArray(jsonobject2.get("textureOffset"), 2);
/*     */         
/* 318 */         if (aint1 == null)
/*     */         {
/* 320 */           throw new JsonParseException("Texture offset not specified");
/*     */         }
/*     */         
/* 323 */         float[] afloat3 = Json.parseFloatArray(jsonobject2.get("coordinates"), 6);
/*     */         
/* 325 */         if (afloat3 == null)
/*     */         {
/* 327 */           throw new JsonParseException("Coordinates not specified");
/*     */         }
/*     */         
/* 330 */         if (flag)
/*     */         {
/* 332 */           afloat3[0] = (-afloat3[0] - afloat3[3]);
/*     */         }
/*     */         
/* 335 */         if (flag1)
/*     */         {
/* 337 */           afloat3[1] = (-afloat3[1] - afloat3[4]);
/*     */         }
/*     */         
/* 340 */         if (flag2)
/*     */         {
/* 342 */           afloat3[2] = (-afloat3[2] - afloat3[5]);
/*     */         }
/*     */         
/* 345 */         float f1 = Json.getFloat(jsonobject2, "sizeAdd", 0.0F);
/* 346 */         modelrenderer.setTextureOffset(aint1[0], aint1[1]);
/* 347 */         modelrenderer.addSprite(afloat3[0], afloat3[1], afloat3[2], (int)afloat3[3], (int)afloat3[4], (int)afloat3[5], f1);
/*     */       }
/*     */     }
/*     */     
/* 351 */     JsonObject jsonobject1 = (JsonObject)p_parseModelRenderer_0_.get("submodel");
/*     */     
/* 353 */     if (jsonobject1 != null)
/*     */     {
/* 355 */       ModelRenderer modelrenderer1 = parseModelRenderer(jsonobject1, p_parseModelRenderer_1_);
/* 356 */       modelrenderer.addChild(modelrenderer1);
/*     */     }
/*     */     
/* 359 */     JsonArray jsonarray2 = (JsonArray)p_parseModelRenderer_0_.get("submodels");
/*     */     
/* 361 */     if (jsonarray2 != null)
/*     */     {
/* 363 */       for (int l = 0; l < jsonarray2.size(); l++)
/*     */       {
/* 365 */         JsonObject jsonobject3 = (JsonObject)jsonarray2.get(l);
/* 366 */         ModelRenderer modelrenderer2 = parseModelRenderer(jsonobject3, p_parseModelRenderer_1_);
/* 367 */         modelrenderer.addChild(modelrenderer2);
/*     */       }
/*     */     }
/*     */     
/* 371 */     return modelrenderer;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\PlayerItemParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */