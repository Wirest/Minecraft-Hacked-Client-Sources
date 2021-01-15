/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ 
/*     */ public enum ModelRotation
/*     */ {
/*  12 */   X0_Y0(0, 0), 
/*  13 */   X0_Y90(0, 90), 
/*  14 */   X0_Y180(0, 180), 
/*  15 */   X0_Y270(0, 270), 
/*  16 */   X90_Y0(90, 0), 
/*  17 */   X90_Y90(90, 90), 
/*  18 */   X90_Y180(90, 180), 
/*  19 */   X90_Y270(90, 270), 
/*  20 */   X180_Y0(180, 0), 
/*  21 */   X180_Y90(180, 90), 
/*  22 */   X180_Y180(180, 180), 
/*  23 */   X180_Y270(180, 270), 
/*  24 */   X270_Y0(270, 0), 
/*  25 */   X270_Y90(270, 90), 
/*  26 */   X270_Y180(270, 180), 
/*  27 */   X270_Y270(270, 270);
/*     */   
/*     */   private static final Map<Integer, ModelRotation> mapRotations;
/*     */   private final int combinedXY;
/*     */   private final Matrix4f matrix4d;
/*     */   private final int quartersX;
/*     */   private final int quartersY;
/*     */   
/*     */   private static int combineXY(int p_177521_0_, int p_177521_1_)
/*     */   {
/*  37 */     return p_177521_0_ * 360 + p_177521_1_;
/*     */   }
/*     */   
/*     */   private ModelRotation(int p_i46087_3_, int p_i46087_4_)
/*     */   {
/*  42 */     this.combinedXY = combineXY(p_i46087_3_, p_i46087_4_);
/*  43 */     this.matrix4d = new Matrix4f();
/*  44 */     Matrix4f matrix4f = new Matrix4f();
/*  45 */     matrix4f.setIdentity();
/*  46 */     Matrix4f.rotate(-p_i46087_3_ * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
/*  47 */     this.quartersX = MathHelper.abs_int(p_i46087_3_ / 90);
/*  48 */     Matrix4f matrix4f1 = new Matrix4f();
/*  49 */     matrix4f1.setIdentity();
/*  50 */     Matrix4f.rotate(-p_i46087_4_ * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f1, matrix4f1);
/*  51 */     this.quartersY = MathHelper.abs_int(p_i46087_4_ / 90);
/*  52 */     Matrix4f.mul(matrix4f1, matrix4f, this.matrix4d);
/*     */   }
/*     */   
/*     */   public Matrix4f getMatrix4d()
/*     */   {
/*  57 */     return this.matrix4d;
/*     */   }
/*     */   
/*     */   public EnumFacing rotateFace(EnumFacing p_177523_1_)
/*     */   {
/*  62 */     EnumFacing enumfacing = p_177523_1_;
/*     */     
/*  64 */     for (int i = 0; i < this.quartersX; i++)
/*     */     {
/*  66 */       enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
/*     */     }
/*     */     
/*  69 */     if (enumfacing.getAxis() != EnumFacing.Axis.Y)
/*     */     {
/*  71 */       for (int j = 0; j < this.quartersY; j++)
/*     */       {
/*  73 */         enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
/*     */       }
/*     */     }
/*     */     
/*  77 */     return enumfacing;
/*     */   }
/*     */   
/*     */   public int rotateVertex(EnumFacing facing, int vertexIndex)
/*     */   {
/*  82 */     int i = vertexIndex;
/*     */     
/*  84 */     if (facing.getAxis() == EnumFacing.Axis.X)
/*     */     {
/*  86 */       i = (vertexIndex + this.quartersX) % 4;
/*     */     }
/*     */     
/*  89 */     EnumFacing enumfacing = facing;
/*     */     
/*  91 */     for (int j = 0; j < this.quartersX; j++)
/*     */     {
/*  93 */       enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
/*     */     }
/*     */     
/*  96 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/*  98 */       i = (i + this.quartersY) % 4;
/*     */     }
/*     */     
/* 101 */     return i;
/*     */   }
/*     */   
/*     */   public static ModelRotation getModelRotation(int p_177524_0_, int p_177524_1_)
/*     */   {
/* 106 */     return (ModelRotation)mapRotations.get(Integer.valueOf(combineXY(MathHelper.normalizeAngle(p_177524_0_, 360), MathHelper.normalizeAngle(p_177524_1_, 360))));
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  29 */     mapRotations = com.google.common.collect.Maps.newHashMap();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ModelRotation[] arrayOfModelRotation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     int j = (arrayOfModelRotation = values()).length; for (int i = 0; i < j; i++) { ModelRotation modelrotation = arrayOfModelRotation[i];
/*     */       
/* 112 */       mapRotations.put(Integer.valueOf(modelrotation.combinedXY), modelrotation);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\ModelRotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */