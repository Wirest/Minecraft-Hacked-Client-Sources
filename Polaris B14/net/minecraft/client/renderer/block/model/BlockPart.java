/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ 
/*     */ public class BlockPart
/*     */ {
/*     */   public final Vector3f positionFrom;
/*     */   public final Vector3f positionTo;
/*     */   public final Map<EnumFacing, BlockPartFace> mapFaces;
/*     */   public final BlockPartRotation partRotation;
/*     */   public final boolean shade;
/*     */   
/*     */   public BlockPart(Vector3f positionFromIn, Vector3f positionToIn, Map<EnumFacing, BlockPartFace> mapFacesIn, BlockPartRotation partRotationIn, boolean shadeIn)
/*     */   {
/*  28 */     this.positionFrom = positionFromIn;
/*  29 */     this.positionTo = positionToIn;
/*  30 */     this.mapFaces = mapFacesIn;
/*  31 */     this.partRotation = partRotationIn;
/*  32 */     this.shade = shadeIn;
/*  33 */     setDefaultUvs();
/*     */   }
/*     */   
/*     */   private void setDefaultUvs()
/*     */   {
/*  38 */     for (Map.Entry<EnumFacing, BlockPartFace> entry : this.mapFaces.entrySet())
/*     */     {
/*  40 */       float[] afloat = getFaceUvs((EnumFacing)entry.getKey());
/*  41 */       ((BlockPartFace)entry.getValue()).blockFaceUV.setUvs(afloat);
/*     */     }
/*     */   }
/*     */   
/*     */   private float[] getFaceUvs(EnumFacing p_178236_1_) {
/*     */     float[] afloat;
/*     */     float[] afloat;
/*     */     float[] afloat;
/*  49 */     switch (p_178236_1_)
/*     */     {
/*     */     case DOWN: 
/*     */     case EAST: 
/*  53 */       afloat = new float[] { this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z };
/*  54 */       break;
/*     */     case NORTH: 
/*     */     case SOUTH: 
/*  57 */       afloat = new float[] { this.positionFrom.x, 16.0F - this.positionTo.y, this.positionTo.x, 16.0F - this.positionFrom.y };
/*  58 */       break;
/*     */     case UP: 
/*     */     case WEST: 
/*  61 */       afloat = new float[] { this.positionFrom.z, 16.0F - this.positionTo.y, this.positionTo.z, 16.0F - this.positionFrom.y };
/*  62 */       break;
/*     */     default: 
/*  64 */       throw new NullPointerException();
/*     */     }
/*     */     float[] afloat;
/*  67 */     return afloat;
/*     */   }
/*     */   
/*     */   static class Deserializer implements com.google.gson.JsonDeserializer<BlockPart>
/*     */   {
/*     */     public BlockPart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/*  74 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/*  75 */       Vector3f vector3f = parsePositionFrom(jsonobject);
/*  76 */       Vector3f vector3f1 = parsePositionTo(jsonobject);
/*  77 */       BlockPartRotation blockpartrotation = parseRotation(jsonobject);
/*  78 */       Map<EnumFacing, BlockPartFace> map = parseFacesCheck(p_deserialize_3_, jsonobject);
/*     */       
/*  80 */       if ((jsonobject.has("shade")) && (!JsonUtils.isBoolean(jsonobject, "shade")))
/*     */       {
/*  82 */         throw new JsonParseException("Expected shade to be a Boolean");
/*     */       }
/*     */       
/*     */ 
/*  86 */       boolean flag = JsonUtils.getBoolean(jsonobject, "shade", true);
/*  87 */       return new BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
/*     */     }
/*     */     
/*     */ 
/*     */     private BlockPartRotation parseRotation(JsonObject p_178256_1_)
/*     */     {
/*  93 */       BlockPartRotation blockpartrotation = null;
/*     */       
/*  95 */       if (p_178256_1_.has("rotation"))
/*     */       {
/*  97 */         JsonObject jsonobject = JsonUtils.getJsonObject(p_178256_1_, "rotation");
/*  98 */         Vector3f vector3f = parsePosition(jsonobject, "origin");
/*  99 */         vector3f.scale(0.0625F);
/* 100 */         EnumFacing.Axis enumfacing$axis = parseAxis(jsonobject);
/* 101 */         float f = parseAngle(jsonobject);
/* 102 */         boolean flag = JsonUtils.getBoolean(jsonobject, "rescale", false);
/* 103 */         blockpartrotation = new BlockPartRotation(vector3f, enumfacing$axis, f, flag);
/*     */       }
/*     */       
/* 106 */       return blockpartrotation;
/*     */     }
/*     */     
/*     */     private float parseAngle(JsonObject p_178255_1_)
/*     */     {
/* 111 */       float f = JsonUtils.getFloat(p_178255_1_, "angle");
/*     */       
/* 113 */       if ((f != 0.0F) && (MathHelper.abs(f) != 22.5F) && (MathHelper.abs(f) != 45.0F))
/*     */       {
/* 115 */         throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
/*     */       }
/*     */       
/*     */ 
/* 119 */       return f;
/*     */     }
/*     */     
/*     */ 
/*     */     private EnumFacing.Axis parseAxis(JsonObject p_178252_1_)
/*     */     {
/* 125 */       String s = JsonUtils.getString(p_178252_1_, "axis");
/* 126 */       EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.byName(s.toLowerCase());
/*     */       
/* 128 */       if (enumfacing$axis == null)
/*     */       {
/* 130 */         throw new JsonParseException("Invalid rotation axis: " + s);
/*     */       }
/*     */       
/*     */ 
/* 134 */       return enumfacing$axis;
/*     */     }
/*     */     
/*     */ 
/*     */     private Map<EnumFacing, BlockPartFace> parseFacesCheck(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_)
/*     */     {
/* 140 */       Map<EnumFacing, BlockPartFace> map = parseFaces(p_178250_1_, p_178250_2_);
/*     */       
/* 142 */       if (map.isEmpty())
/*     */       {
/* 144 */         throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
/*     */       }
/*     */       
/*     */ 
/* 148 */       return map;
/*     */     }
/*     */     
/*     */ 
/*     */     private Map<EnumFacing, BlockPartFace> parseFaces(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_)
/*     */     {
/* 154 */       Map<EnumFacing, BlockPartFace> map = Maps.newEnumMap(EnumFacing.class);
/* 155 */       JsonObject jsonobject = JsonUtils.getJsonObject(p_178253_2_, "faces");
/*     */       
/* 157 */       for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
/*     */       {
/* 159 */         EnumFacing enumfacing = parseEnumFacing((String)entry.getKey());
/* 160 */         map.put(enumfacing, (BlockPartFace)p_178253_1_.deserialize((JsonElement)entry.getValue(), BlockPartFace.class));
/*     */       }
/*     */       
/* 163 */       return map;
/*     */     }
/*     */     
/*     */     private EnumFacing parseEnumFacing(String name)
/*     */     {
/* 168 */       EnumFacing enumfacing = EnumFacing.byName(name);
/*     */       
/* 170 */       if (enumfacing == null)
/*     */       {
/* 172 */         throw new JsonParseException("Unknown facing: " + name);
/*     */       }
/*     */       
/*     */ 
/* 176 */       return enumfacing;
/*     */     }
/*     */     
/*     */ 
/*     */     private Vector3f parsePositionTo(JsonObject p_178247_1_)
/*     */     {
/* 182 */       Vector3f vector3f = parsePosition(p_178247_1_, "to");
/*     */       
/* 184 */       if ((vector3f.x >= -16.0F) && (vector3f.y >= -16.0F) && (vector3f.z >= -16.0F) && (vector3f.x <= 32.0F) && (vector3f.y <= 32.0F) && (vector3f.z <= 32.0F))
/*     */       {
/* 186 */         return vector3f;
/*     */       }
/*     */       
/*     */ 
/* 190 */       throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + vector3f);
/*     */     }
/*     */     
/*     */ 
/*     */     private Vector3f parsePositionFrom(JsonObject p_178249_1_)
/*     */     {
/* 196 */       Vector3f vector3f = parsePosition(p_178249_1_, "from");
/*     */       
/* 198 */       if ((vector3f.x >= -16.0F) && (vector3f.y >= -16.0F) && (vector3f.z >= -16.0F) && (vector3f.x <= 32.0F) && (vector3f.y <= 32.0F) && (vector3f.z <= 32.0F))
/*     */       {
/* 200 */         return vector3f;
/*     */       }
/*     */       
/*     */ 
/* 204 */       throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + vector3f);
/*     */     }
/*     */     
/*     */ 
/*     */     private Vector3f parsePosition(JsonObject p_178251_1_, String p_178251_2_)
/*     */     {
/* 210 */       JsonArray jsonarray = JsonUtils.getJsonArray(p_178251_1_, p_178251_2_);
/*     */       
/* 212 */       if (jsonarray.size() != 3)
/*     */       {
/* 214 */         throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + jsonarray.size());
/*     */       }
/*     */       
/*     */ 
/* 218 */       float[] afloat = new float[3];
/*     */       
/* 220 */       for (int i = 0; i < afloat.length; i++)
/*     */       {
/* 222 */         afloat[i] = JsonUtils.getFloat(jsonarray.get(i), p_178251_2_ + "[" + i + "]");
/*     */       }
/*     */       
/* 225 */       return new Vector3f(afloat[0], afloat[1], afloat[2]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\BlockPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */