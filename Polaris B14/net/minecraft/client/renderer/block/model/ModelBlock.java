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
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ModelBlock
/*     */ {
/*  27 */   private static final Logger LOGGER = ;
/*  28 */   static final Gson SERIALIZER = new GsonBuilder().registerTypeAdapter(ModelBlock.class, new Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).create();
/*     */   private final List<BlockPart> elements;
/*     */   private final boolean gui3d;
/*     */   private final boolean ambientOcclusion;
/*     */   private ItemCameraTransforms cameraTransforms;
/*     */   public String name;
/*     */   protected final Map<String, String> textures;
/*     */   protected ModelBlock parent;
/*     */   protected ResourceLocation parentLocation;
/*     */   
/*     */   public static ModelBlock deserialize(Reader p_178307_0_)
/*     */   {
/*  40 */     return (ModelBlock)SERIALIZER.fromJson(p_178307_0_, ModelBlock.class);
/*     */   }
/*     */   
/*     */   public static ModelBlock deserialize(String p_178294_0_)
/*     */   {
/*  45 */     return deserialize(new StringReader(p_178294_0_));
/*     */   }
/*     */   
/*     */   protected ModelBlock(List<BlockPart> p_i46225_1_, Map<String, String> p_i46225_2_, boolean p_i46225_3_, boolean p_i46225_4_, ItemCameraTransforms p_i46225_5_)
/*     */   {
/*  50 */     this(null, p_i46225_1_, p_i46225_2_, p_i46225_3_, p_i46225_4_, p_i46225_5_);
/*     */   }
/*     */   
/*     */   protected ModelBlock(ResourceLocation p_i46226_1_, Map<String, String> p_i46226_2_, boolean p_i46226_3_, boolean p_i46226_4_, ItemCameraTransforms p_i46226_5_)
/*     */   {
/*  55 */     this(p_i46226_1_, Collections.emptyList(), p_i46226_2_, p_i46226_3_, p_i46226_4_, p_i46226_5_);
/*     */   }
/*     */   
/*     */   private ModelBlock(ResourceLocation parentLocationIn, List<BlockPart> elementsIn, Map<String, String> texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn)
/*     */   {
/*  60 */     this.name = "";
/*  61 */     this.elements = elementsIn;
/*  62 */     this.ambientOcclusion = ambientOcclusionIn;
/*  63 */     this.gui3d = gui3dIn;
/*  64 */     this.textures = texturesIn;
/*  65 */     this.parentLocation = parentLocationIn;
/*  66 */     this.cameraTransforms = cameraTransformsIn;
/*     */   }
/*     */   
/*     */   public List<BlockPart> getElements()
/*     */   {
/*  71 */     return hasParent() ? this.parent.getElements() : this.elements;
/*     */   }
/*     */   
/*     */   private boolean hasParent()
/*     */   {
/*  76 */     return this.parent != null;
/*     */   }
/*     */   
/*     */   public boolean isAmbientOcclusion()
/*     */   {
/*  81 */     return hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
/*     */   }
/*     */   
/*     */   public boolean isGui3d()
/*     */   {
/*  86 */     return this.gui3d;
/*     */   }
/*     */   
/*     */   public boolean isResolved()
/*     */   {
/*  91 */     return (this.parentLocation == null) || ((this.parent != null) && (this.parent.isResolved()));
/*     */   }
/*     */   
/*     */   public void getParentFromMap(Map<ResourceLocation, ModelBlock> p_178299_1_)
/*     */   {
/*  96 */     if (this.parentLocation != null)
/*     */     {
/*  98 */       this.parent = ((ModelBlock)p_178299_1_.get(this.parentLocation));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isTexturePresent(String textureName)
/*     */   {
/* 104 */     return !"missingno".equals(resolveTextureName(textureName));
/*     */   }
/*     */   
/*     */   public String resolveTextureName(String textureName)
/*     */   {
/* 109 */     if (!startsWithHash(textureName))
/*     */     {
/* 111 */       textureName = '#' + textureName;
/*     */     }
/*     */     
/* 114 */     return resolveTextureName(textureName, new Bookkeep(this, null));
/*     */   }
/*     */   
/*     */   private String resolveTextureName(String textureName, Bookkeep p_178302_2_)
/*     */   {
/* 119 */     if (startsWithHash(textureName))
/*     */     {
/* 121 */       if (this == p_178302_2_.modelExt)
/*     */       {
/* 123 */         LOGGER.warn("Unable to resolve texture due to upward reference: " + textureName + " in " + this.name);
/* 124 */         return "missingno";
/*     */       }
/*     */       
/*     */ 
/* 128 */       String s = (String)this.textures.get(textureName.substring(1));
/*     */       
/* 130 */       if ((s == null) && (hasParent()))
/*     */       {
/* 132 */         s = this.parent.resolveTextureName(textureName, p_178302_2_);
/*     */       }
/*     */       
/* 135 */       p_178302_2_.modelExt = this;
/*     */       
/* 137 */       if ((s != null) && (startsWithHash(s)))
/*     */       {
/* 139 */         s = p_178302_2_.model.resolveTextureName(s, p_178302_2_);
/*     */       }
/*     */       
/* 142 */       return (s != null) && (!startsWithHash(s)) ? s : "missingno";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     return textureName;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean startsWithHash(String hash)
/*     */   {
/* 153 */     return hash.charAt(0) == '#';
/*     */   }
/*     */   
/*     */   public ResourceLocation getParentLocation()
/*     */   {
/* 158 */     return this.parentLocation;
/*     */   }
/*     */   
/*     */   public ModelBlock getRootModel()
/*     */   {
/* 163 */     return hasParent() ? this.parent.getRootModel() : this;
/*     */   }
/*     */   
/*     */   public ItemCameraTransforms func_181682_g()
/*     */   {
/* 168 */     ItemTransformVec3f itemtransformvec3f = func_181681_a(ItemCameraTransforms.TransformType.THIRD_PERSON);
/* 169 */     ItemTransformVec3f itemtransformvec3f1 = func_181681_a(ItemCameraTransforms.TransformType.FIRST_PERSON);
/* 170 */     ItemTransformVec3f itemtransformvec3f2 = func_181681_a(ItemCameraTransforms.TransformType.HEAD);
/* 171 */     ItemTransformVec3f itemtransformvec3f3 = func_181681_a(ItemCameraTransforms.TransformType.GUI);
/* 172 */     ItemTransformVec3f itemtransformvec3f4 = func_181681_a(ItemCameraTransforms.TransformType.GROUND);
/* 173 */     ItemTransformVec3f itemtransformvec3f5 = func_181681_a(ItemCameraTransforms.TransformType.FIXED);
/* 174 */     return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
/*     */   }
/*     */   
/*     */   private ItemTransformVec3f func_181681_a(ItemCameraTransforms.TransformType p_181681_1_)
/*     */   {
/* 179 */     return (this.parent != null) && (!this.cameraTransforms.func_181687_c(p_181681_1_)) ? this.parent.func_181681_a(p_181681_1_) : this.cameraTransforms.getTransform(p_181681_1_);
/*     */   }
/*     */   
/*     */   public static void checkModelHierarchy(Map<ResourceLocation, ModelBlock> p_178312_0_)
/*     */   {
/* 184 */     for (ModelBlock modelblock : p_178312_0_.values())
/*     */     {
/*     */       try
/*     */       {
/* 188 */         ModelBlock modelblock1 = modelblock.parent;
/*     */         
/* 190 */         for (ModelBlock modelblock2 = modelblock1.parent; modelblock1 != modelblock2; modelblock2 = modelblock2.parent.parent)
/*     */         {
/* 192 */           modelblock1 = modelblock1.parent;
/*     */         }
/*     */         
/* 195 */         throw new LoopException();
/*     */       }
/*     */       catch (NullPointerException localNullPointerException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class Bookkeep
/*     */   {
/*     */     public final ModelBlock model;
/*     */     
/*     */     public ModelBlock modelExt;
/*     */     
/*     */ 
/*     */     private Bookkeep(ModelBlock p_i46223_1_)
/*     */     {
/* 211 */       this.model = p_i46223_1_;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Deserializer implements JsonDeserializer<ModelBlock>
/*     */   {
/*     */     public ModelBlock deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 219 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 220 */       List<BlockPart> list = getModelElements(p_deserialize_3_, jsonobject);
/* 221 */       String s = getParent(jsonobject);
/* 222 */       boolean flag = StringUtils.isEmpty(s);
/* 223 */       boolean flag1 = list.isEmpty();
/*     */       
/* 225 */       if ((flag1) && (flag))
/*     */       {
/* 227 */         throw new JsonParseException("BlockModel requires either elements or parent, found neither");
/*     */       }
/* 229 */       if ((!flag) && (!flag1))
/*     */       {
/* 231 */         throw new JsonParseException("BlockModel requires either elements or parent, found both");
/*     */       }
/*     */       
/*     */ 
/* 235 */       Map<String, String> map = getTextures(jsonobject);
/* 236 */       boolean flag2 = getAmbientOcclusionEnabled(jsonobject);
/* 237 */       ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;
/*     */       
/* 239 */       if (jsonobject.has("display"))
/*     */       {
/* 241 */         JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
/* 242 */         itemcameratransforms = (ItemCameraTransforms)p_deserialize_3_.deserialize(jsonobject1, ItemCameraTransforms.class);
/*     */       }
/*     */       
/* 245 */       return flag1 ? new ModelBlock(new ResourceLocation(s), map, flag2, true, itemcameratransforms) : new ModelBlock(list, map, flag2, true, itemcameratransforms);
/*     */     }
/*     */     
/*     */ 
/*     */     private Map<String, String> getTextures(JsonObject p_178329_1_)
/*     */     {
/* 251 */       Map<String, String> map = Maps.newHashMap();
/*     */       
/* 253 */       if (p_178329_1_.has("textures"))
/*     */       {
/* 255 */         JsonObject jsonobject = p_178329_1_.getAsJsonObject("textures");
/*     */         
/* 257 */         for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*     */         {
/* 259 */           map.put((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
/*     */         }
/*     */       }
/*     */       
/* 263 */       return map;
/*     */     }
/*     */     
/*     */     private String getParent(JsonObject p_178326_1_)
/*     */     {
/* 268 */       return JsonUtils.getString(p_178326_1_, "parent", "");
/*     */     }
/*     */     
/*     */     protected boolean getAmbientOcclusionEnabled(JsonObject p_178328_1_)
/*     */     {
/* 273 */       return JsonUtils.getBoolean(p_178328_1_, "ambientocclusion", true);
/*     */     }
/*     */     
/*     */     protected List<BlockPart> getModelElements(JsonDeserializationContext p_178325_1_, JsonObject p_178325_2_)
/*     */     {
/* 278 */       List<BlockPart> list = Lists.newArrayList();
/*     */       
/* 280 */       if (p_178325_2_.has("elements"))
/*     */       {
/* 282 */         for (JsonElement jsonelement : JsonUtils.getJsonArray(p_178325_2_, "elements"))
/*     */         {
/* 284 */           list.add((BlockPart)p_178325_1_.deserialize(jsonelement, BlockPart.class));
/*     */         }
/*     */       }
/*     */       
/* 288 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LoopException
/*     */     extends RuntimeException
/*     */   {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\ModelBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */