/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ 
/*     */ public class BlockFaceUV
/*     */ {
/*     */   public float[] uvs;
/*     */   public final int rotation;
/*     */   
/*     */   public BlockFaceUV(float[] uvsIn, int rotationIn)
/*     */   {
/*  19 */     this.uvs = uvsIn;
/*  20 */     this.rotation = rotationIn;
/*     */   }
/*     */   
/*     */   public float func_178348_a(int p_178348_1_)
/*     */   {
/*  25 */     if (this.uvs == null)
/*     */     {
/*  27 */       throw new NullPointerException("uvs");
/*     */     }
/*     */     
/*     */ 
/*  31 */     int i = func_178347_d(p_178348_1_);
/*  32 */     return (i != 0) && (i != 1) ? this.uvs[2] : this.uvs[0];
/*     */   }
/*     */   
/*     */ 
/*     */   public float func_178346_b(int p_178346_1_)
/*     */   {
/*  38 */     if (this.uvs == null)
/*     */     {
/*  40 */       throw new NullPointerException("uvs");
/*     */     }
/*     */     
/*     */ 
/*  44 */     int i = func_178347_d(p_178346_1_);
/*  45 */     return (i != 0) && (i != 3) ? this.uvs[3] : this.uvs[1];
/*     */   }
/*     */   
/*     */ 
/*     */   private int func_178347_d(int p_178347_1_)
/*     */   {
/*  51 */     return (p_178347_1_ + this.rotation / 90) % 4;
/*     */   }
/*     */   
/*     */   public int func_178345_c(int p_178345_1_)
/*     */   {
/*  56 */     return (p_178345_1_ + (4 - this.rotation / 90)) % 4;
/*     */   }
/*     */   
/*     */   public void setUvs(float[] uvsIn)
/*     */   {
/*  61 */     if (this.uvs == null)
/*     */     {
/*  63 */       this.uvs = uvsIn;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Deserializer implements JsonDeserializer<BlockFaceUV>
/*     */   {
/*     */     public BlockFaceUV deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/*  71 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*  72 */       float[] afloat = parseUV(jsonobject);
/*  73 */       int i = parseRotation(jsonobject);
/*  74 */       return new BlockFaceUV(afloat, i);
/*     */     }
/*     */     
/*     */     protected int parseRotation(JsonObject p_178291_1_)
/*     */     {
/*  79 */       int i = JsonUtils.getInt(p_178291_1_, "rotation", 0);
/*     */       
/*  81 */       if ((i >= 0) && (i % 90 == 0) && (i / 90 <= 3))
/*     */       {
/*  83 */         return i;
/*     */       }
/*     */       
/*     */ 
/*  87 */       throw new JsonParseException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
/*     */     }
/*     */     
/*     */ 
/*     */     private float[] parseUV(JsonObject p_178292_1_)
/*     */     {
/*  93 */       if (!p_178292_1_.has("uv"))
/*     */       {
/*  95 */         return null;
/*     */       }
/*     */       
/*     */ 
/*  99 */       JsonArray jsonarray = JsonUtils.getJsonArray(p_178292_1_, "uv");
/*     */       
/* 101 */       if (jsonarray.size() != 4)
/*     */       {
/* 103 */         throw new JsonParseException("Expected 4 uv values, found: " + jsonarray.size());
/*     */       }
/*     */       
/*     */ 
/* 107 */       float[] afloat = new float[4];
/*     */       
/* 109 */       for (int i = 0; i < afloat.length; i++)
/*     */       {
/* 111 */         afloat[i] = JsonUtils.getFloat(jsonarray.get(i), "uv[" + i + "]");
/*     */       }
/*     */       
/* 114 */       return afloat;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\BlockFaceUV.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */