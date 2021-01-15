/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ 
/*     */ public class ItemCameraTransforms
/*     */ {
/*  13 */   public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms();
/*  14 */   public static float field_181690_b = 0.0F;
/*  15 */   public static float field_181691_c = 0.0F;
/*  16 */   public static float field_181692_d = 0.0F;
/*  17 */   public static float field_181693_e = 0.0F;
/*  18 */   public static float field_181694_f = 0.0F;
/*  19 */   public static float field_181695_g = 0.0F;
/*  20 */   public static float field_181696_h = 0.0F;
/*  21 */   public static float field_181697_i = 0.0F;
/*  22 */   public static float field_181698_j = 0.0F;
/*     */   public final ItemTransformVec3f thirdPerson;
/*     */   public final ItemTransformVec3f firstPerson;
/*     */   public final ItemTransformVec3f head;
/*     */   public final ItemTransformVec3f gui;
/*     */   public final ItemTransformVec3f ground;
/*     */   public final ItemTransformVec3f fixed;
/*     */   
/*     */   private ItemCameraTransforms()
/*     */   {
/*  32 */     this(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
/*     */   }
/*     */   
/*     */   public ItemCameraTransforms(ItemCameraTransforms p_i46443_1_)
/*     */   {
/*  37 */     this.thirdPerson = p_i46443_1_.thirdPerson;
/*  38 */     this.firstPerson = p_i46443_1_.firstPerson;
/*  39 */     this.head = p_i46443_1_.head;
/*  40 */     this.gui = p_i46443_1_.gui;
/*  41 */     this.ground = p_i46443_1_.ground;
/*  42 */     this.fixed = p_i46443_1_.fixed;
/*     */   }
/*     */   
/*     */   public ItemCameraTransforms(ItemTransformVec3f p_i46444_1_, ItemTransformVec3f p_i46444_2_, ItemTransformVec3f p_i46444_3_, ItemTransformVec3f p_i46444_4_, ItemTransformVec3f p_i46444_5_, ItemTransformVec3f p_i46444_6_)
/*     */   {
/*  47 */     this.thirdPerson = p_i46444_1_;
/*  48 */     this.firstPerson = p_i46444_2_;
/*  49 */     this.head = p_i46444_3_;
/*  50 */     this.gui = p_i46444_4_;
/*  51 */     this.ground = p_i46444_5_;
/*  52 */     this.fixed = p_i46444_6_;
/*     */   }
/*     */   
/*     */   public void applyTransform(TransformType p_181689_1_)
/*     */   {
/*  57 */     ItemTransformVec3f itemtransformvec3f = getTransform(p_181689_1_);
/*     */     
/*  59 */     if (itemtransformvec3f != ItemTransformVec3f.DEFAULT)
/*     */     {
/*  61 */       GlStateManager.translate(itemtransformvec3f.translation.x + field_181690_b, itemtransformvec3f.translation.y + field_181691_c, itemtransformvec3f.translation.z + field_181692_d);
/*  62 */       GlStateManager.rotate(itemtransformvec3f.rotation.y + field_181694_f, 0.0F, 1.0F, 0.0F);
/*  63 */       GlStateManager.rotate(itemtransformvec3f.rotation.x + field_181693_e, 1.0F, 0.0F, 0.0F);
/*  64 */       GlStateManager.rotate(itemtransformvec3f.rotation.z + field_181695_g, 0.0F, 0.0F, 1.0F);
/*  65 */       GlStateManager.scale(itemtransformvec3f.scale.x + field_181696_h, itemtransformvec3f.scale.y + field_181697_i, itemtransformvec3f.scale.z + field_181698_j);
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemTransformVec3f getTransform(TransformType p_181688_1_)
/*     */   {
/*  71 */     switch (p_181688_1_)
/*     */     {
/*     */     case FIXED: 
/*  74 */       return this.thirdPerson;
/*     */     
/*     */     case GROUND: 
/*  77 */       return this.firstPerson;
/*     */     
/*     */     case GUI: 
/*  80 */       return this.head;
/*     */     
/*     */     case HEAD: 
/*  83 */       return this.gui;
/*     */     
/*     */     case NONE: 
/*  86 */       return this.ground;
/*     */     
/*     */     case THIRD_PERSON: 
/*  89 */       return this.fixed;
/*     */     }
/*     */     
/*  92 */     return ItemTransformVec3f.DEFAULT;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_181687_c(TransformType p_181687_1_)
/*     */   {
/*  98 */     return !getTransform(p_181687_1_).equals(ItemTransformVec3f.DEFAULT);
/*     */   }
/*     */   
/*     */   static class Deserializer implements com.google.gson.JsonDeserializer<ItemCameraTransforms>
/*     */   {
/*     */     public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 105 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 106 */       ItemTransformVec3f itemtransformvec3f = func_181683_a(p_deserialize_3_, jsonobject, "thirdperson");
/* 107 */       ItemTransformVec3f itemtransformvec3f1 = func_181683_a(p_deserialize_3_, jsonobject, "firstperson");
/* 108 */       ItemTransformVec3f itemtransformvec3f2 = func_181683_a(p_deserialize_3_, jsonobject, "head");
/* 109 */       ItemTransformVec3f itemtransformvec3f3 = func_181683_a(p_deserialize_3_, jsonobject, "gui");
/* 110 */       ItemTransformVec3f itemtransformvec3f4 = func_181683_a(p_deserialize_3_, jsonobject, "ground");
/* 111 */       ItemTransformVec3f itemtransformvec3f5 = func_181683_a(p_deserialize_3_, jsonobject, "fixed");
/* 112 */       return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5);
/*     */     }
/*     */     
/*     */     private ItemTransformVec3f func_181683_a(JsonDeserializationContext p_181683_1_, JsonObject p_181683_2_, String p_181683_3_)
/*     */     {
/* 117 */       return p_181683_2_.has(p_181683_3_) ? (ItemTransformVec3f)p_181683_1_.deserialize(p_181683_2_.get(p_181683_3_), ItemTransformVec3f.class) : ItemTransformVec3f.DEFAULT;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum TransformType
/*     */   {
/* 123 */     NONE, 
/* 124 */     THIRD_PERSON, 
/* 125 */     FIRST_PERSON, 
/* 126 */     HEAD, 
/* 127 */     GUI, 
/* 128 */     GROUND, 
/* 129 */     FIXED;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\ItemCameraTransforms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */