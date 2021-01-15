/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ 
/*     */ public class ActiveRenderInfo
/*     */ {
/*  20 */   private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
/*     */   
/*     */ 
/*  23 */   private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
/*     */   
/*     */ 
/*  26 */   private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
/*     */   
/*     */ 
/*  29 */   private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
/*  30 */   private static Vec3 position = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*     */ 
/*     */ 
/*     */   private static float rotationX;
/*     */   
/*     */ 
/*     */ 
/*     */   private static float rotationXZ;
/*     */   
/*     */ 
/*     */ 
/*     */   private static float rotationZ;
/*     */   
/*     */ 
/*     */ 
/*     */   private static float rotationYZ;
/*     */   
/*     */ 
/*     */ 
/*     */   private static float rotationXY;
/*     */   
/*     */ 
/*     */ 
/*     */   public static void updateRenderInfo(EntityPlayer entityplayerIn, boolean p_74583_1_)
/*     */   {
/*  56 */     GlStateManager.getFloat(2982, MODELVIEW);
/*  57 */     GlStateManager.getFloat(2983, PROJECTION);
/*  58 */     GL11.glGetInteger(2978, VIEWPORT);
/*  59 */     float f = (VIEWPORT.get(0) + VIEWPORT.get(2)) / 2;
/*  60 */     float f1 = (VIEWPORT.get(1) + VIEWPORT.get(3)) / 2;
/*  61 */     GLU.gluUnProject(f, f1, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
/*  62 */     position = new Vec3(OBJECTCOORDS.get(0), OBJECTCOORDS.get(1), OBJECTCOORDS.get(2));
/*  63 */     int i = p_74583_1_ ? 1 : 0;
/*  64 */     float f2 = entityplayerIn.rotationPitch;
/*  65 */     float f3 = entityplayerIn.rotationYaw;
/*  66 */     rotationX = MathHelper.cos(f3 * 3.1415927F / 180.0F) * (1 - i * 2);
/*  67 */     rotationZ = MathHelper.sin(f3 * 3.1415927F / 180.0F) * (1 - i * 2);
/*  68 */     rotationYZ = -rotationZ * MathHelper.sin(f2 * 3.1415927F / 180.0F) * (1 - i * 2);
/*  69 */     rotationXY = rotationX * MathHelper.sin(f2 * 3.1415927F / 180.0F) * (1 - i * 2);
/*  70 */     rotationXZ = MathHelper.cos(f2 * 3.1415927F / 180.0F);
/*     */   }
/*     */   
/*     */   public static Vec3 projectViewFromEntity(Entity p_178806_0_, double p_178806_1_)
/*     */   {
/*  75 */     double d0 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
/*  76 */     double d1 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
/*  77 */     double d2 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
/*  78 */     double d3 = d0 + position.xCoord;
/*  79 */     double d4 = d1 + position.yCoord;
/*  80 */     double d5 = d2 + position.zCoord;
/*  81 */     return new Vec3(d3, d4, d5);
/*     */   }
/*     */   
/*     */   public static Block getBlockAtEntityViewpoint(World worldIn, Entity p_180786_1_, float p_180786_2_)
/*     */   {
/*  86 */     Vec3 vec3 = projectViewFromEntity(p_180786_1_, p_180786_2_);
/*  87 */     BlockPos blockpos = new BlockPos(vec3);
/*  88 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*  89 */     Block block = iblockstate.getBlock();
/*     */     
/*  91 */     if (block.getMaterial().isLiquid())
/*     */     {
/*  93 */       float f = 0.0F;
/*     */       
/*  95 */       if ((iblockstate.getBlock() instanceof BlockLiquid))
/*     */       {
/*  97 */         f = BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue()) - 0.11111111F;
/*     */       }
/*     */       
/* 100 */       float f1 = blockpos.getY() + 1 - f;
/*     */       
/* 102 */       if (vec3.yCoord >= f1)
/*     */       {
/* 104 */         block = worldIn.getBlockState(blockpos.up()).getBlock();
/*     */       }
/*     */     }
/*     */     
/* 108 */     return block;
/*     */   }
/*     */   
/*     */   public static Vec3 getPosition()
/*     */   {
/* 113 */     return position;
/*     */   }
/*     */   
/*     */   public static float getRotationX()
/*     */   {
/* 118 */     return rotationX;
/*     */   }
/*     */   
/*     */   public static float getRotationXZ()
/*     */   {
/* 123 */     return rotationXZ;
/*     */   }
/*     */   
/*     */   public static float getRotationZ()
/*     */   {
/* 128 */     return rotationZ;
/*     */   }
/*     */   
/*     */   public static float getRotationYZ()
/*     */   {
/* 133 */     return rotationYZ;
/*     */   }
/*     */   
/*     */   public static float getRotationXY()
/*     */   {
/* 138 */     return rotationXY;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\ActiveRenderInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */