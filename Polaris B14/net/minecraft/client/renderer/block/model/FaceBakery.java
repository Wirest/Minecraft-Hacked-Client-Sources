/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.EnumFaceDirection;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelRotation;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ import org.lwjgl.util.vector.Vector4f;
/*     */ 
/*     */ public class FaceBakery
/*     */ {
/*  15 */   private static final float field_178418_a = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
/*  16 */   private static final float field_178417_b = 1.0F / (float)Math.cos(0.7853981633974483D) - 1.0F;
/*     */   private static final String __OBFID = "CL_00002490";
/*     */   
/*     */   public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
/*     */   {
/*  21 */     int[] aint = makeQuadVertexData(face, sprite, facing, getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
/*  22 */     EnumFacing enumfacing = getFacingFromVertexData(aint);
/*     */     
/*  24 */     if (uvLocked)
/*     */     {
/*  26 */       func_178409_a(aint, enumfacing, face.blockFaceUV, sprite);
/*     */     }
/*     */     
/*  29 */     if (partRotation == null)
/*     */     {
/*  31 */       func_178408_a(aint, enumfacing);
/*     */     }
/*     */     
/*  34 */     return new BakedQuad(aint, face.tintIndex, enumfacing, sprite);
/*     */   }
/*     */   
/*     */   private int[] makeQuadVertexData(BlockPartFace partFace, TextureAtlasSprite sprite, EnumFacing facing, float[] p_178405_4_, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
/*     */   {
/*  39 */     int[] aint = new int[28];
/*     */     
/*  41 */     for (int i = 0; i < 4; i++)
/*     */     {
/*  43 */       fillVertexData(aint, i, facing, partFace, p_178405_4_, sprite, modelRotationIn, partRotation, uvLocked, shade);
/*     */     }
/*     */     
/*  46 */     return aint;
/*     */   }
/*     */   
/*     */   private int getFaceShadeColor(EnumFacing facing)
/*     */   {
/*  51 */     float f = getFaceBrightness(facing);
/*  52 */     int i = MathHelper.clamp_int((int)(f * 255.0F), 0, 255);
/*  53 */     return 0xFF000000 | i << 16 | i << 8 | i;
/*     */   }
/*     */   
/*     */   private float getFaceBrightness(EnumFacing facing)
/*     */   {
/*  58 */     switch (FaceBakery.1.field_178400_a[facing.ordinal()])
/*     */     {
/*     */     case 1: 
/*  61 */       return 0.5F;
/*     */     
/*     */     case 2: 
/*  64 */       return 1.0F;
/*     */     
/*     */     case 3: 
/*     */     case 4: 
/*  68 */       return 0.8F;
/*     */     
/*     */     case 5: 
/*     */     case 6: 
/*  72 */       return 0.6F;
/*     */     }
/*     */     
/*  75 */     return 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2)
/*     */   {
/*  81 */     float[] afloat = new float[EnumFacing.values().length];
/*  82 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.WEST_INDEX] = (pos1.x / 16.0F);
/*  83 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.DOWN_INDEX] = (pos1.y / 16.0F);
/*  84 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.NORTH_INDEX] = (pos1.z / 16.0F);
/*  85 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.EAST_INDEX] = (pos2.x / 16.0F);
/*  86 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.UP_INDEX] = (pos2.y / 16.0F);
/*  87 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.SOUTH_INDEX] = (pos2.z / 16.0F);
/*  88 */     return afloat;
/*     */   }
/*     */   
/*     */   private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
/*     */   {
/*  93 */     EnumFacing enumfacing = modelRotationIn.rotateFace(facing);
/*  94 */     int i = shade ? getFaceShadeColor(enumfacing) : -1;
/*  95 */     net.minecraft.client.renderer.EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(facing).func_179025_a(vertexIndex);
/*  96 */     Vector3f vector3f = new Vector3f(p_178402_5_[enumfacedirection$vertexinformation.field_179184_a], p_178402_5_[enumfacedirection$vertexinformation.field_179182_b], p_178402_5_[enumfacedirection$vertexinformation.field_179183_c]);
/*  97 */     func_178407_a(vector3f, partRotation);
/*  98 */     int j = rotateVertex(vector3f, facing, vertexIndex, modelRotationIn, uvLocked);
/*  99 */     storeVertexData(faceData, j, vertexIndex, vector3f, i, sprite, partFace.blockFaceUV);
/*     */   }
/*     */   
/*     */   private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
/*     */   {
/* 104 */     int i = storeIndex * 7;
/* 105 */     faceData[i] = Float.floatToRawIntBits(position.x);
/* 106 */     faceData[(i + 1)] = Float.floatToRawIntBits(position.y);
/* 107 */     faceData[(i + 2)] = Float.floatToRawIntBits(position.z);
/* 108 */     faceData[(i + 3)] = shadeColor;
/* 109 */     faceData[(i + 4)] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex)));
/* 110 */     faceData[(i + 4 + 1)] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex)));
/*     */   }
/*     */   
/*     */   private void func_178407_a(Vector3f p_178407_1_, BlockPartRotation partRotation)
/*     */   {
/* 115 */     if (partRotation != null)
/*     */     {
/* 117 */       Matrix4f matrix4f = getMatrixIdentity();
/* 118 */       Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
/*     */       
/* 120 */       switch (FaceBakery.1.field_178399_b[partRotation.axis.ordinal()])
/*     */       {
/*     */       case 1: 
/* 123 */         Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
/* 124 */         vector3f.set(0.0F, 1.0F, 1.0F);
/* 125 */         break;
/*     */       
/*     */       case 2: 
/* 128 */         Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f, matrix4f);
/* 129 */         vector3f.set(1.0F, 0.0F, 1.0F);
/* 130 */         break;
/*     */       
/*     */       case 3: 
/* 133 */         Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4f, matrix4f);
/* 134 */         vector3f.set(1.0F, 1.0F, 0.0F);
/*     */       }
/*     */       
/* 137 */       if (partRotation.rescale)
/*     */       {
/* 139 */         if (Math.abs(partRotation.angle) == 22.5F)
/*     */         {
/* 141 */           vector3f.scale(field_178418_a);
/*     */         }
/*     */         else
/*     */         {
/* 145 */           vector3f.scale(field_178417_b);
/*     */         }
/*     */         
/* 148 */         Vector3f.add(vector3f, new Vector3f(1.0F, 1.0F, 1.0F), vector3f);
/*     */       }
/*     */       else
/*     */       {
/* 152 */         vector3f.set(1.0F, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 155 */       rotateScale(p_178407_1_, new Vector3f(partRotation.origin), matrix4f, vector3f);
/*     */     }
/*     */   }
/*     */   
/*     */   public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked)
/*     */   {
/* 161 */     if (modelRotationIn == ModelRotation.X0_Y0)
/*     */     {
/* 163 */       return vertexIndex;
/*     */     }
/*     */     
/*     */ 
/* 167 */     rotateScale(position, new Vector3f(0.5F, 0.5F, 0.5F), modelRotationIn.getMatrix4d(), new Vector3f(1.0F, 1.0F, 1.0F));
/* 168 */     return modelRotationIn.rotateVertex(facing, vertexIndex);
/*     */   }
/*     */   
/*     */ 
/*     */   private void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale)
/*     */   {
/* 174 */     Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0F);
/* 175 */     Matrix4f.transform(rotationMatrix, vector4f, vector4f);
/* 176 */     vector4f.x *= scale.x;
/* 177 */     vector4f.y *= scale.y;
/* 178 */     vector4f.z *= scale.z;
/* 179 */     position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
/*     */   }
/*     */   
/*     */   private Matrix4f getMatrixIdentity()
/*     */   {
/* 184 */     Matrix4f matrix4f = new Matrix4f();
/* 185 */     matrix4f.setIdentity();
/* 186 */     return matrix4f;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacingFromVertexData(int[] faceData)
/*     */   {
/* 191 */     Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
/* 192 */     Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(faceData[7]), Float.intBitsToFloat(faceData[8]), Float.intBitsToFloat(faceData[9]));
/* 193 */     Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[14]), Float.intBitsToFloat(faceData[15]), Float.intBitsToFloat(faceData[16]));
/* 194 */     Vector3f vector3f3 = new Vector3f();
/* 195 */     Vector3f vector3f4 = new Vector3f();
/* 196 */     Vector3f vector3f5 = new Vector3f();
/* 197 */     Vector3f.sub(vector3f, vector3f1, vector3f3);
/* 198 */     Vector3f.sub(vector3f2, vector3f1, vector3f4);
/* 199 */     Vector3f.cross(vector3f4, vector3f3, vector3f5);
/* 200 */     float f = (float)Math.sqrt(vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z);
/* 201 */     vector3f5.x /= f;
/* 202 */     vector3f5.y /= f;
/* 203 */     vector3f5.z /= f;
/* 204 */     EnumFacing enumfacing = null;
/* 205 */     float f1 = 0.0F;
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 207 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing1 = arrayOfEnumFacing[i];
/*     */       
/* 209 */       Vec3i vec3i = enumfacing1.getDirectionVec();
/* 210 */       Vector3f vector3f6 = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/* 211 */       float f2 = Vector3f.dot(vector3f5, vector3f6);
/*     */       
/* 213 */       if ((f2 >= 0.0F) && (f2 > f1))
/*     */       {
/* 215 */         f1 = f2;
/* 216 */         enumfacing = enumfacing1;
/*     */       }
/*     */     }
/*     */     
/* 220 */     if (f1 < 0.719F)
/*     */     {
/* 222 */       if ((enumfacing != EnumFacing.EAST) && (enumfacing != EnumFacing.WEST) && (enumfacing != EnumFacing.NORTH) && (enumfacing != EnumFacing.SOUTH))
/*     */       {
/* 224 */         enumfacing = EnumFacing.UP;
/*     */       }
/*     */       else
/*     */       {
/* 228 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*     */     }
/*     */     
/* 232 */     return enumfacing == null ? EnumFacing.UP : enumfacing;
/*     */   }
/*     */   
/*     */   public void func_178409_a(int[] p_178409_1_, EnumFacing facing, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_)
/*     */   {
/* 237 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 239 */       func_178401_a(i, p_178409_1_, facing, p_178409_3_, p_178409_4_);
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_)
/*     */   {
/* 245 */     int[] aint = new int[p_178408_1_.length];
/* 246 */     System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
/* 247 */     float[] afloat = new float[EnumFacing.values().length];
/* 248 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
/* 249 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
/* 250 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
/* 251 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
/* 252 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
/* 253 */     afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;
/*     */     
/* 255 */     for (int j = 0; j < 4; j++)
/*     */     {
/* 257 */       int i = 7 * j;
/* 258 */       float f1 = Float.intBitsToFloat(aint[i]);
/* 259 */       float f2 = Float.intBitsToFloat(aint[(i + 1)]);
/* 260 */       float f = Float.intBitsToFloat(aint[(i + 2)]);
/*     */       
/* 262 */       if (f1 < afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.WEST_INDEX])
/*     */       {
/* 264 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.WEST_INDEX] = f1;
/*     */       }
/*     */       
/* 267 */       if (f2 < afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.DOWN_INDEX])
/*     */       {
/* 269 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.DOWN_INDEX] = f2;
/*     */       }
/*     */       
/* 272 */       if (f < afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.NORTH_INDEX])
/*     */       {
/* 274 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.NORTH_INDEX] = f;
/*     */       }
/*     */       
/* 277 */       if (f1 > afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.EAST_INDEX])
/*     */       {
/* 279 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.EAST_INDEX] = f1;
/*     */       }
/*     */       
/* 282 */       if (f2 > afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.UP_INDEX])
/*     */       {
/* 284 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.UP_INDEX] = f2;
/*     */       }
/*     */       
/* 287 */       if (f > afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.SOUTH_INDEX])
/*     */       {
/* 289 */         afloat[net.minecraft.client.renderer.EnumFaceDirection.Constants.SOUTH_INDEX] = f;
/*     */       }
/*     */     }
/*     */     
/* 293 */     EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);
/*     */     
/* 295 */     for (int i1 = 0; i1 < 4; i1++)
/*     */     {
/* 297 */       int j1 = 7 * i1;
/* 298 */       net.minecraft.client.renderer.EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.func_179025_a(i1);
/* 299 */       float f8 = afloat[enumfacedirection$vertexinformation.field_179184_a];
/* 300 */       float f3 = afloat[enumfacedirection$vertexinformation.field_179182_b];
/* 301 */       float f4 = afloat[enumfacedirection$vertexinformation.field_179183_c];
/* 302 */       p_178408_1_[j1] = Float.floatToRawIntBits(f8);
/* 303 */       p_178408_1_[(j1 + 1)] = Float.floatToRawIntBits(f3);
/* 304 */       p_178408_1_[(j1 + 2)] = Float.floatToRawIntBits(f4);
/*     */       
/* 306 */       for (int k = 0; k < 4; k++)
/*     */       {
/* 308 */         int l = 7 * k;
/* 309 */         float f5 = Float.intBitsToFloat(aint[l]);
/* 310 */         float f6 = Float.intBitsToFloat(aint[(l + 1)]);
/* 311 */         float f7 = Float.intBitsToFloat(aint[(l + 2)]);
/*     */         
/* 313 */         if ((MathHelper.epsilonEquals(f8, f5)) && (MathHelper.epsilonEquals(f3, f6)) && (MathHelper.epsilonEquals(f4, f7)))
/*     */         {
/* 315 */           p_178408_1_[(j1 + 4)] = aint[(l + 4)];
/* 316 */           p_178408_1_[(j1 + 4 + 1)] = aint[(l + 4 + 1)];
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing facing, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_)
/*     */   {
/* 324 */     int i = 7 * p_178401_1_;
/* 325 */     float f = Float.intBitsToFloat(p_178401_2_[i]);
/* 326 */     float f1 = Float.intBitsToFloat(p_178401_2_[(i + 1)]);
/* 327 */     float f2 = Float.intBitsToFloat(p_178401_2_[(i + 2)]);
/*     */     
/* 329 */     if ((f < -0.1F) || (f >= 1.1F))
/*     */     {
/* 331 */       f -= MathHelper.floor_float(f);
/*     */     }
/*     */     
/* 334 */     if ((f1 < -0.1F) || (f1 >= 1.1F))
/*     */     {
/* 336 */       f1 -= MathHelper.floor_float(f1);
/*     */     }
/*     */     
/* 339 */     if ((f2 < -0.1F) || (f2 >= 1.1F))
/*     */     {
/* 341 */       f2 -= MathHelper.floor_float(f2);
/*     */     }
/*     */     
/* 344 */     float f3 = 0.0F;
/* 345 */     float f4 = 0.0F;
/*     */     
/* 347 */     switch (FaceBakery.1.field_178400_a[facing.ordinal()])
/*     */     {
/*     */     case 1: 
/* 350 */       f3 = f * 16.0F;
/* 351 */       f4 = (1.0F - f2) * 16.0F;
/* 352 */       break;
/*     */     
/*     */     case 2: 
/* 355 */       f3 = f * 16.0F;
/* 356 */       f4 = f2 * 16.0F;
/* 357 */       break;
/*     */     
/*     */     case 3: 
/* 360 */       f3 = (1.0F - f) * 16.0F;
/* 361 */       f4 = (1.0F - f1) * 16.0F;
/* 362 */       break;
/*     */     
/*     */     case 4: 
/* 365 */       f3 = f * 16.0F;
/* 366 */       f4 = (1.0F - f1) * 16.0F;
/* 367 */       break;
/*     */     
/*     */     case 5: 
/* 370 */       f3 = f2 * 16.0F;
/* 371 */       f4 = (1.0F - f1) * 16.0F;
/* 372 */       break;
/*     */     
/*     */     case 6: 
/* 375 */       f3 = (1.0F - f2) * 16.0F;
/* 376 */       f4 = (1.0F - f1) * 16.0F;
/*     */     }
/*     */     
/* 379 */     int j = p_178401_4_.func_178345_c(p_178401_1_) * 7;
/* 380 */     p_178401_2_[(j + 4)] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(f3));
/* 381 */     p_178401_2_[(j + 4 + 1)] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(f4));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\FaceBakery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */