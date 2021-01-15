/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.client.resources.model.ModelRotation;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class ModelBlockDefinition
/*     */ {
/*  24 */   static final Gson GSON = new GsonBuilder().registerTypeAdapter(ModelBlockDefinition.class, new Deserializer()).registerTypeAdapter(Variant.class, new ModelBlockDefinition.Variant.Deserializer()).create();
/*  25 */   private final Map<String, Variants> mapVariants = Maps.newHashMap();
/*     */   
/*     */   public static ModelBlockDefinition parseFromReader(Reader p_178331_0_)
/*     */   {
/*  29 */     return (ModelBlockDefinition)GSON.fromJson(p_178331_0_, ModelBlockDefinition.class);
/*     */   }
/*     */   
/*     */   public ModelBlockDefinition(Collection<Variants> p_i46221_1_)
/*     */   {
/*  34 */     for (Variants modelblockdefinition$variants : p_i46221_1_)
/*     */     {
/*  36 */       this.mapVariants.put(modelblockdefinition$variants.name, modelblockdefinition$variants);
/*     */     }
/*     */   }
/*     */   
/*     */   public ModelBlockDefinition(List<ModelBlockDefinition> p_i46222_1_)
/*     */   {
/*  42 */     for (ModelBlockDefinition modelblockdefinition : p_i46222_1_)
/*     */     {
/*  44 */       this.mapVariants.putAll(modelblockdefinition.mapVariants);
/*     */     }
/*     */   }
/*     */   
/*     */   public Variants getVariants(String p_178330_1_)
/*     */   {
/*  50 */     Variants modelblockdefinition$variants = (Variants)this.mapVariants.get(p_178330_1_);
/*     */     
/*  52 */     if (modelblockdefinition$variants == null)
/*     */     {
/*  54 */       throw new MissingVariantException();
/*     */     }
/*     */     
/*     */ 
/*  58 */     return modelblockdefinition$variants;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  64 */     if (this == p_equals_1_)
/*     */     {
/*  66 */       return true;
/*     */     }
/*  68 */     if ((p_equals_1_ instanceof ModelBlockDefinition))
/*     */     {
/*  70 */       ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
/*  71 */       return this.mapVariants.equals(modelblockdefinition.mapVariants);
/*     */     }
/*     */     
/*     */ 
/*  75 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  81 */     return this.mapVariants.hashCode();
/*     */   }
/*     */   
/*     */   public static class Deserializer implements JsonDeserializer<ModelBlockDefinition>
/*     */   {
/*     */     public ModelBlockDefinition deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/*  88 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*  89 */       List<ModelBlockDefinition.Variants> list = parseVariantsList(p_deserialize_3_, jsonobject);
/*  90 */       return new ModelBlockDefinition(list);
/*     */     }
/*     */     
/*     */     protected List<ModelBlockDefinition.Variants> parseVariantsList(JsonDeserializationContext p_178334_1_, JsonObject p_178334_2_)
/*     */     {
/*  95 */       JsonObject jsonobject = JsonUtils.getJsonObject(p_178334_2_, "variants");
/*  96 */       List<ModelBlockDefinition.Variants> list = Lists.newArrayList();
/*     */       
/*  98 */       for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*     */       {
/* 100 */         list.add(parseVariants(p_178334_1_, entry));
/*     */       }
/*     */       
/* 103 */       return list;
/*     */     }
/*     */     
/*     */     protected ModelBlockDefinition.Variants parseVariants(JsonDeserializationContext p_178335_1_, Map.Entry<String, JsonElement> p_178335_2_)
/*     */     {
/* 108 */       String s = (String)p_178335_2_.getKey();
/* 109 */       List<ModelBlockDefinition.Variant> list = Lists.newArrayList();
/* 110 */       JsonElement jsonelement = (JsonElement)p_178335_2_.getValue();
/*     */       
/* 112 */       if (jsonelement.isJsonArray())
/*     */       {
/* 114 */         for (JsonElement jsonelement1 : jsonelement.getAsJsonArray())
/*     */         {
/* 116 */           list.add((ModelBlockDefinition.Variant)p_178335_1_.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 121 */         list.add((ModelBlockDefinition.Variant)p_178335_1_.deserialize(jsonelement, ModelBlockDefinition.Variant.class));
/*     */       }
/*     */       
/* 124 */       return new ModelBlockDefinition.Variants(s, list);
/*     */     }
/*     */   }
/*     */   
/*     */   public class MissingVariantException extends RuntimeException
/*     */   {
/*     */     public MissingVariantException() {}
/*     */   }
/*     */   
/*     */   public static class Variant
/*     */   {
/*     */     private final ResourceLocation modelLocation;
/*     */     private final ModelRotation modelRotation;
/*     */     private final boolean uvLock;
/*     */     private final int weight;
/*     */     
/*     */     public Variant(ResourceLocation modelLocationIn, ModelRotation modelRotationIn, boolean uvLockIn, int weightIn) {
/* 141 */       this.modelLocation = modelLocationIn;
/* 142 */       this.modelRotation = modelRotationIn;
/* 143 */       this.uvLock = uvLockIn;
/* 144 */       this.weight = weightIn;
/*     */     }
/*     */     
/*     */     public ResourceLocation getModelLocation()
/*     */     {
/* 149 */       return this.modelLocation;
/*     */     }
/*     */     
/*     */     public ModelRotation getRotation()
/*     */     {
/* 154 */       return this.modelRotation;
/*     */     }
/*     */     
/*     */     public boolean isUvLocked()
/*     */     {
/* 159 */       return this.uvLock;
/*     */     }
/*     */     
/*     */     public int getWeight()
/*     */     {
/* 164 */       return this.weight;
/*     */     }
/*     */     
/*     */     public boolean equals(Object p_equals_1_)
/*     */     {
/* 169 */       if (this == p_equals_1_)
/*     */       {
/* 171 */         return true;
/*     */       }
/* 173 */       if (!(p_equals_1_ instanceof Variant))
/*     */       {
/* 175 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 179 */       Variant modelblockdefinition$variant = (Variant)p_equals_1_;
/* 180 */       return (this.modelLocation.equals(modelblockdefinition$variant.modelLocation)) && (this.modelRotation == modelblockdefinition$variant.modelRotation) && (this.uvLock == modelblockdefinition$variant.uvLock);
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 186 */       int i = this.modelLocation.hashCode();
/* 187 */       i = 31 * i + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
/* 188 */       i = 31 * i + (this.uvLock ? 1 : 0);
/* 189 */       return i;
/*     */     }
/*     */     
/*     */     public static class Deserializer implements JsonDeserializer<ModelBlockDefinition.Variant>
/*     */     {
/*     */       public ModelBlockDefinition.Variant deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */       {
/* 196 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 197 */         String s = parseModel(jsonobject);
/* 198 */         ModelRotation modelrotation = parseRotation(jsonobject);
/* 199 */         boolean flag = parseUvLock(jsonobject);
/* 200 */         int i = parseWeight(jsonobject);
/* 201 */         return new ModelBlockDefinition.Variant(makeModelLocation(s), modelrotation, flag, i);
/*     */       }
/*     */       
/*     */       private ResourceLocation makeModelLocation(String p_178426_1_)
/*     */       {
/* 206 */         ResourceLocation resourcelocation = new ResourceLocation(p_178426_1_);
/* 207 */         resourcelocation = new ResourceLocation(resourcelocation.getResourceDomain(), "block/" + resourcelocation.getResourcePath());
/* 208 */         return resourcelocation;
/*     */       }
/*     */       
/*     */       private boolean parseUvLock(JsonObject p_178429_1_)
/*     */       {
/* 213 */         return JsonUtils.getBoolean(p_178429_1_, "uvlock", false);
/*     */       }
/*     */       
/*     */       protected ModelRotation parseRotation(JsonObject p_178428_1_)
/*     */       {
/* 218 */         int i = JsonUtils.getInt(p_178428_1_, "x", 0);
/* 219 */         int j = JsonUtils.getInt(p_178428_1_, "y", 0);
/* 220 */         ModelRotation modelrotation = ModelRotation.getModelRotation(i, j);
/*     */         
/* 222 */         if (modelrotation == null)
/*     */         {
/* 224 */           throw new JsonParseException("Invalid BlockModelRotation x: " + i + ", y: " + j);
/*     */         }
/*     */         
/*     */ 
/* 228 */         return modelrotation;
/*     */       }
/*     */       
/*     */ 
/*     */       protected String parseModel(JsonObject p_178424_1_)
/*     */       {
/* 234 */         return JsonUtils.getString(p_178424_1_, "model");
/*     */       }
/*     */       
/*     */       protected int parseWeight(JsonObject p_178427_1_)
/*     */       {
/* 239 */         return JsonUtils.getInt(p_178427_1_, "weight", 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Variants
/*     */   {
/*     */     private final String name;
/*     */     private final List<ModelBlockDefinition.Variant> listVariants;
/*     */     
/*     */     public Variants(String nameIn, List<ModelBlockDefinition.Variant> listVariantsIn)
/*     */     {
/* 251 */       this.name = nameIn;
/* 252 */       this.listVariants = listVariantsIn;
/*     */     }
/*     */     
/*     */     public List<ModelBlockDefinition.Variant> getVariants()
/*     */     {
/* 257 */       return this.listVariants;
/*     */     }
/*     */     
/*     */     public boolean equals(Object p_equals_1_)
/*     */     {
/* 262 */       if (this == p_equals_1_)
/*     */       {
/* 264 */         return true;
/*     */       }
/* 266 */       if (!(p_equals_1_ instanceof Variants))
/*     */       {
/* 268 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 272 */       Variants modelblockdefinition$variants = (Variants)p_equals_1_;
/* 273 */       return !this.name.equals(modelblockdefinition$variants.name) ? false : this.listVariants.equals(modelblockdefinition$variants.listVariants);
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 279 */       int i = this.name.hashCode();
/* 280 */       i = 31 * i + this.listVariants.hashCode();
/* 281 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\ModelBlockDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */