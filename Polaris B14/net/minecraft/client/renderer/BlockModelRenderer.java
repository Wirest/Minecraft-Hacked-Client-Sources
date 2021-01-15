/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.Block.EnumOffsetType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import optfine.BetterGrass;
/*     */ import optfine.BetterSnow;
/*     */ import optfine.Config;
/*     */ import optfine.ConnectedTextures;
/*     */ import optfine.CustomColorizer;
/*     */ import optfine.NaturalTextures;
/*     */ import optfine.RenderEnv;
/*     */ 
/*     */ public class BlockModelRenderer
/*     */ {
/*     */   private static final String __OBFID = "CL_00002518";
/*  31 */   private static float aoLightValueOpaque = 0.2F;
/*     */   
/*     */   public static void updateAoLightValue()
/*     */   {
/*  35 */     aoLightValueOpaque = 1.0F - Config.getAmbientOcclusionLevel() * 0.8F;
/*     */   }
/*     */   
/*     */   public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn)
/*     */   {
/*  40 */     Block block = blockStateIn.getBlock();
/*  41 */     block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
/*  42 */     return renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
/*     */   }
/*     */   
/*     */   public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
/*     */   {
/*  47 */     boolean flag = (Minecraft.isAmbientOcclusionEnabled()) && (blockStateIn.getBlock().getLightValue() == 0) && (modelIn.isAmbientOcclusion());
/*     */     
/*     */     try
/*     */     {
/*  51 */       Block block = blockStateIn.getBlock();
/*  52 */       return flag ? renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides) : renderModelStandard(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides);
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/*  56 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
/*  57 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
/*  58 */       CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
/*  59 */       crashreportcategory.addCrashSection("Using AO", Boolean.valueOf(flag));
/*  60 */       throw new net.minecraft.util.ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
/*     */   {
/*  66 */     return renderModelAmbientOcclusion(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
/*     */   }
/*     */   
/*     */   public boolean renderModelAmbientOcclusion(IBlockAccess p_renderModelAmbientOcclusion_1_, IBakedModel p_renderModelAmbientOcclusion_2_, Block p_renderModelAmbientOcclusion_3_, IBlockState p_renderModelAmbientOcclusion_4_, BlockPos p_renderModelAmbientOcclusion_5_, WorldRenderer p_renderModelAmbientOcclusion_6_, boolean p_renderModelAmbientOcclusion_7_)
/*     */   {
/*  71 */     boolean flag = false;
/*  72 */     RenderEnv renderenv = null;
/*     */     EnumFacing[] arrayOfEnumFacing;
/*  74 */     int j = (arrayOfEnumFacing = EnumFacing.VALUES).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/*  76 */       List list = p_renderModelAmbientOcclusion_2_.getFaceQuads(enumfacing);
/*     */       
/*  78 */       if (!list.isEmpty())
/*     */       {
/*  80 */         BlockPos blockpos = p_renderModelAmbientOcclusion_5_.offset(enumfacing);
/*     */         
/*  82 */         if ((!p_renderModelAmbientOcclusion_7_) || (p_renderModelAmbientOcclusion_3_.shouldSideBeRendered(p_renderModelAmbientOcclusion_1_, blockpos, enumfacing)))
/*     */         {
/*  84 */           if (renderenv == null)
/*     */           {
/*  86 */             renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
/*     */           }
/*     */           
/*  89 */           if ((!renderenv.isBreakingAnimation(list)) && (Config.isBetterGrass()))
/*     */           {
/*  91 */             list = BetterGrass.getFaceQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, enumfacing, list);
/*     */           }
/*     */           
/*  94 */           renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list, renderenv);
/*  95 */           flag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 100 */     List list1 = p_renderModelAmbientOcclusion_2_.getGeneralQuads();
/*     */     
/* 102 */     if (list1.size() > 0)
/*     */     {
/* 104 */       if (renderenv == null)
/*     */       {
/* 106 */         renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
/*     */       }
/*     */       
/* 109 */       renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list1, renderenv);
/* 110 */       flag = true;
/*     */     }
/*     */     
/* 113 */     if ((renderenv != null) && (Config.isBetterSnow()) && (!renderenv.isBreakingAnimation()) && (BetterSnow.shouldRender(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_)))
/*     */     {
/* 115 */       IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
/* 116 */       IBlockState iblockstate = BetterSnow.getStateSnowLayer();
/* 117 */       renderModelAmbientOcclusion(p_renderModelAmbientOcclusion_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, true);
/*     */     }
/*     */     
/* 120 */     return flag;
/*     */   }
/*     */   
/*     */   public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
/*     */   {
/* 125 */     return renderModelStandard(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
/*     */   }
/*     */   
/*     */   public boolean renderModelStandard(IBlockAccess p_renderModelStandard_1_, IBakedModel p_renderModelStandard_2_, Block p_renderModelStandard_3_, IBlockState p_renderModelStandard_4_, BlockPos p_renderModelStandard_5_, WorldRenderer p_renderModelStandard_6_, boolean p_renderModelStandard_7_)
/*     */   {
/* 130 */     boolean flag = false;
/* 131 */     RenderEnv renderenv = null;
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 133 */     int j = (arrayOfEnumFacing = EnumFacing.VALUES).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 135 */       List list = p_renderModelStandard_2_.getFaceQuads(enumfacing);
/*     */       
/* 137 */       if (!list.isEmpty())
/*     */       {
/* 139 */         BlockPos blockpos = p_renderModelStandard_5_.offset(enumfacing);
/*     */         
/* 141 */         if ((!p_renderModelStandard_7_) || (p_renderModelStandard_3_.shouldSideBeRendered(p_renderModelStandard_1_, blockpos, enumfacing)))
/*     */         {
/* 143 */           if (renderenv == null)
/*     */           {
/* 145 */             renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
/*     */           }
/*     */           
/* 148 */           if ((!renderenv.isBreakingAnimation(list)) && (Config.isBetterGrass()))
/*     */           {
/* 150 */             list = BetterGrass.getFaceQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, list);
/*     */           }
/*     */           
/* 153 */           int i = p_renderModelStandard_3_.getMixedBrightnessForBlock(p_renderModelStandard_1_, blockpos);
/* 154 */           renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, i, false, p_renderModelStandard_6_, list, renderenv);
/* 155 */           flag = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 160 */     List list1 = p_renderModelStandard_2_.getGeneralQuads();
/*     */     
/* 162 */     if (list1.size() > 0)
/*     */     {
/* 164 */       if (renderenv == null)
/*     */       {
/* 166 */         renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
/*     */       }
/*     */       
/* 169 */       renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, null, -1, true, p_renderModelStandard_6_, list1, renderenv);
/* 170 */       flag = true;
/*     */     }
/*     */     
/* 173 */     if ((renderenv != null) && (Config.isBetterSnow()) && (!renderenv.isBreakingAnimation()) && (BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_)) && (BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_)))
/*     */     {
/* 175 */       IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
/* 176 */       IBlockState iblockstate = BetterSnow.getStateSnowLayer();
/* 177 */       renderModelStandard(p_renderModelStandard_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelStandard_5_, p_renderModelStandard_6_, true);
/*     */     }
/*     */     
/* 180 */     return flag;
/*     */   }
/*     */   
/*     */   private void renderModelAmbientOcclusionQuads(IBlockAccess p_renderModelAmbientOcclusionQuads_1_, Block p_renderModelAmbientOcclusionQuads_2_, BlockPos p_renderModelAmbientOcclusionQuads_3_, WorldRenderer p_renderModelAmbientOcclusionQuads_4_, List p_renderModelAmbientOcclusionQuads_5_, RenderEnv p_renderModelAmbientOcclusionQuads_6_)
/*     */   {
/* 185 */     float[] afloat = p_renderModelAmbientOcclusionQuads_6_.getQuadBounds();
/* 186 */     BitSet bitset = p_renderModelAmbientOcclusionQuads_6_.getBoundsFlags();
/* 187 */     AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderModelAmbientOcclusionQuads_6_.getAoFace();
/* 188 */     IBlockState iblockstate = p_renderModelAmbientOcclusionQuads_6_.getBlockState();
/* 189 */     double d0 = p_renderModelAmbientOcclusionQuads_3_.getX();
/* 190 */     double d1 = p_renderModelAmbientOcclusionQuads_3_.getY();
/* 191 */     double d2 = p_renderModelAmbientOcclusionQuads_3_.getZ();
/* 192 */     Block.EnumOffsetType block$enumoffsettype = p_renderModelAmbientOcclusionQuads_2_.getOffsetType();
/*     */     
/* 194 */     if (block$enumoffsettype != Block.EnumOffsetType.NONE)
/*     */     {
/* 196 */       long i = MathHelper.getPositionRandom(p_renderModelAmbientOcclusionQuads_3_);
/* 197 */       d0 += ((float)(i >> 16 & 0xF) / 15.0F - 0.5D) * 0.5D;
/* 198 */       d2 += ((float)(i >> 24 & 0xF) / 15.0F - 0.5D) * 0.5D;
/*     */       
/* 200 */       if (block$enumoffsettype == Block.EnumOffsetType.XYZ)
/*     */       {
/* 202 */         d1 += ((float)(i >> 20 & 0xF) / 15.0F - 1.0D) * 0.2D;
/*     */       }
/*     */     }
/*     */     
/* 206 */     for (Object bakedquad0 : p_renderModelAmbientOcclusionQuads_5_)
/*     */     {
/* 208 */       BakedQuad bakedquad = (BakedQuad)bakedquad0;
/*     */       
/* 210 */       if (bakedquad.getSprite() != null)
/*     */       {
/* 212 */         BakedQuad bakedquad1 = bakedquad;
/*     */         
/* 214 */         if (Config.isConnectedTextures())
/*     */         {
/* 216 */           bakedquad = ConnectedTextures.getConnectedTexture(p_renderModelAmbientOcclusionQuads_1_, iblockstate, p_renderModelAmbientOcclusionQuads_3_, bakedquad, p_renderModelAmbientOcclusionQuads_6_);
/*     */         }
/*     */         
/* 219 */         if ((bakedquad == bakedquad1) && (Config.isNaturalTextures()))
/*     */         {
/* 221 */           bakedquad = NaturalTextures.getNaturalTexture(p_renderModelAmbientOcclusionQuads_3_, bakedquad);
/*     */         }
/*     */       }
/*     */       
/* 225 */       fillQuadBounds(p_renderModelAmbientOcclusionQuads_2_, bakedquad.getVertexData(), bakedquad.getFace(), afloat, bitset);
/* 226 */       blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_3_, bakedquad.getFace(), afloat, bitset);
/*     */       
/* 228 */       if (p_renderModelAmbientOcclusionQuads_4_.isMultiTexture())
/*     */       {
/* 230 */         p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad.getVertexDataSingle());
/* 231 */         p_renderModelAmbientOcclusionQuads_4_.putSprite(bakedquad.getSprite());
/*     */       }
/*     */       else
/*     */       {
/* 235 */         p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad.getVertexData());
/*     */       }
/*     */       
/* 238 */       p_renderModelAmbientOcclusionQuads_4_.putBrightness4(blockmodelrenderer$ambientocclusionface.vertexBrightness[0], blockmodelrenderer$ambientocclusionface.vertexBrightness[1], blockmodelrenderer$ambientocclusionface.vertexBrightness[2], blockmodelrenderer$ambientocclusionface.vertexBrightness[3]);
/* 239 */       int k = CustomColorizer.getColorMultiplier(bakedquad, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, p_renderModelAmbientOcclusionQuads_6_);
/*     */       
/* 241 */       if ((!bakedquad.hasTintIndex()) && (k < 0))
/*     */       {
/* 243 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
/* 244 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
/* 245 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
/* 246 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/*     */         int j;
/* 252 */         if (k >= 0)
/*     */         {
/* 254 */           j = k;
/*     */         }
/*     */         else
/*     */         {
/* 258 */           j = p_renderModelAmbientOcclusionQuads_2_.colorMultiplier(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, bakedquad.getTintIndex());
/*     */         }
/*     */         
/* 261 */         if (EntityRenderer.anaglyphEnable)
/*     */         {
/* 263 */           j = TextureUtil.anaglyphColor(j);
/*     */         }
/*     */         
/* 266 */         float f = (j >> 16 & 0xFF) / 255.0F;
/* 267 */         float f1 = (j >> 8 & 0xFF) / 255.0F;
/* 268 */         float f2 = (j & 0xFF) / 255.0F;
/* 269 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f1, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f2, 4);
/* 270 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f1, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f2, 3);
/* 271 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f1, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f2, 2);
/* 272 */         p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f1, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f2, 1);
/*     */       }
/*     */       
/* 275 */       p_renderModelAmbientOcclusionQuads_4_.putPosition(d0, d1, d2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fillQuadBounds(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
/*     */   {
/* 281 */     float f = 32.0F;
/* 282 */     float f1 = 32.0F;
/* 283 */     float f2 = 32.0F;
/* 284 */     float f3 = -32.0F;
/* 285 */     float f4 = -32.0F;
/* 286 */     float f5 = -32.0F;
/*     */     
/* 288 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 290 */       float f6 = Float.intBitsToFloat(vertexData[(i * 7)]);
/* 291 */       float f7 = Float.intBitsToFloat(vertexData[(i * 7 + 1)]);
/* 292 */       float f8 = Float.intBitsToFloat(vertexData[(i * 7 + 2)]);
/* 293 */       f = Math.min(f, f6);
/* 294 */       f1 = Math.min(f1, f7);
/* 295 */       f2 = Math.min(f2, f8);
/* 296 */       f3 = Math.max(f3, f6);
/* 297 */       f4 = Math.max(f4, f7);
/* 298 */       f5 = Math.max(f5, f8);
/*     */     }
/*     */     
/* 301 */     if (quadBounds != null)
/*     */     {
/* 303 */       quadBounds[EnumFacing.WEST.getIndex()] = f;
/* 304 */       quadBounds[EnumFacing.EAST.getIndex()] = f3;
/* 305 */       quadBounds[EnumFacing.DOWN.getIndex()] = f1;
/* 306 */       quadBounds[EnumFacing.UP.getIndex()] = f4;
/* 307 */       quadBounds[EnumFacing.NORTH.getIndex()] = f2;
/* 308 */       quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
/* 309 */       quadBounds[(EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f);
/* 310 */       quadBounds[(EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f3);
/* 311 */       quadBounds[(EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f1);
/* 312 */       quadBounds[(EnumFacing.UP.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f4);
/* 313 */       quadBounds[(EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f2);
/* 314 */       quadBounds[(EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length)] = (1.0F - f5);
/*     */     }
/*     */     
/* 317 */     float f10 = 1.0E-4F;
/* 318 */     float f9 = 0.9999F;
/*     */     
/* 320 */     switch (BlockModelRenderer.1.field_178290_a[facingIn.ordinal()])
/*     */     {
/*     */     case 1: 
/* 323 */       boundsFlags.set(1, (f >= 1.0E-4F) || (f2 >= 1.0E-4F) || (f3 <= 0.9999F) || (f5 <= 0.9999F));
/* 324 */       boundsFlags.set(0, ((f1 < 1.0E-4F) || (blockIn.isFullCube())) && (f1 == f4));
/* 325 */       break;
/*     */     
/*     */     case 2: 
/* 328 */       boundsFlags.set(1, (f >= 1.0E-4F) || (f2 >= 1.0E-4F) || (f3 <= 0.9999F) || (f5 <= 0.9999F));
/* 329 */       boundsFlags.set(0, ((f4 > 0.9999F) || (blockIn.isFullCube())) && (f1 == f4));
/* 330 */       break;
/*     */     
/*     */     case 3: 
/* 333 */       boundsFlags.set(1, (f >= 1.0E-4F) || (f1 >= 1.0E-4F) || (f3 <= 0.9999F) || (f4 <= 0.9999F));
/* 334 */       boundsFlags.set(0, ((f2 < 1.0E-4F) || (blockIn.isFullCube())) && (f2 == f5));
/* 335 */       break;
/*     */     
/*     */     case 4: 
/* 338 */       boundsFlags.set(1, (f >= 1.0E-4F) || (f1 >= 1.0E-4F) || (f3 <= 0.9999F) || (f4 <= 0.9999F));
/* 339 */       boundsFlags.set(0, ((f5 > 0.9999F) || (blockIn.isFullCube())) && (f2 == f5));
/* 340 */       break;
/*     */     
/*     */     case 5: 
/* 343 */       boundsFlags.set(1, (f1 >= 1.0E-4F) || (f2 >= 1.0E-4F) || (f4 <= 0.9999F) || (f5 <= 0.9999F));
/* 344 */       boundsFlags.set(0, ((f < 1.0E-4F) || (blockIn.isFullCube())) && (f == f3));
/* 345 */       break;
/*     */     
/*     */     case 6: 
/* 348 */       boundsFlags.set(1, (f1 >= 1.0E-4F) || (f2 >= 1.0E-4F) || (f4 <= 0.9999F) || (f5 <= 0.9999F));
/* 349 */       boundsFlags.set(0, ((f3 > 0.9999F) || (blockIn.isFullCube())) && (f == f3));
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderModelStandardQuads(IBlockAccess p_renderModelStandardQuads_1_, Block p_renderModelStandardQuads_2_, BlockPos p_renderModelStandardQuads_3_, EnumFacing p_renderModelStandardQuads_4_, int p_renderModelStandardQuads_5_, boolean p_renderModelStandardQuads_6_, WorldRenderer p_renderModelStandardQuads_7_, List p_renderModelStandardQuads_8_, RenderEnv p_renderModelStandardQuads_9_)
/*     */   {
/* 355 */     BitSet bitset = p_renderModelStandardQuads_9_.getBoundsFlags();
/* 356 */     IBlockState iblockstate = p_renderModelStandardQuads_9_.getBlockState();
/* 357 */     double d0 = p_renderModelStandardQuads_3_.getX();
/* 358 */     double d1 = p_renderModelStandardQuads_3_.getY();
/* 359 */     double d2 = p_renderModelStandardQuads_3_.getZ();
/* 360 */     Block.EnumOffsetType block$enumoffsettype = p_renderModelStandardQuads_2_.getOffsetType();
/*     */     int j;
/* 362 */     if (block$enumoffsettype != Block.EnumOffsetType.NONE)
/*     */     {
/* 364 */       int i = p_renderModelStandardQuads_3_.getX();
/* 365 */       j = p_renderModelStandardQuads_3_.getZ();
/* 366 */       long k = i * 3129871 ^ j * 116129781L;
/* 367 */       k = k * k * 42317861L + k * 11L;
/* 368 */       d0 += ((float)(k >> 16 & 0xF) / 15.0F - 0.5D) * 0.5D;
/* 369 */       d2 += ((float)(k >> 24 & 0xF) / 15.0F - 0.5D) * 0.5D;
/*     */       
/* 371 */       if (block$enumoffsettype == Block.EnumOffsetType.XYZ)
/*     */       {
/* 373 */         d1 += ((float)(k >> 20 & 0xF) / 15.0F - 1.0D) * 0.2D;
/*     */       }
/*     */     }
/*     */     
/* 377 */     for (Object bakedquad0 : p_renderModelStandardQuads_8_)
/*     */     {
/* 379 */       BakedQuad bakedquad = (BakedQuad)bakedquad0;
/*     */       
/* 381 */       if (bakedquad.getSprite() != null)
/*     */       {
/* 383 */         BakedQuad bakedquad1 = bakedquad;
/*     */         
/* 385 */         if (Config.isConnectedTextures())
/*     */         {
/* 387 */           bakedquad = ConnectedTextures.getConnectedTexture(p_renderModelStandardQuads_1_, iblockstate, p_renderModelStandardQuads_3_, bakedquad, p_renderModelStandardQuads_9_);
/*     */         }
/*     */         
/* 390 */         if ((bakedquad == bakedquad1) && (Config.isNaturalTextures()))
/*     */         {
/* 392 */           bakedquad = NaturalTextures.getNaturalTexture(p_renderModelStandardQuads_3_, bakedquad);
/*     */         }
/*     */       }
/*     */       
/* 396 */       if (p_renderModelStandardQuads_6_)
/*     */       {
/* 398 */         fillQuadBounds(p_renderModelStandardQuads_2_, bakedquad.getVertexData(), bakedquad.getFace(), null, bitset);
/* 399 */         p_renderModelStandardQuads_5_ = bitset.get(0) ? p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_.offset(bakedquad.getFace())) : p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_);
/*     */       }
/*     */       
/* 402 */       if (p_renderModelStandardQuads_7_.isMultiTexture())
/*     */       {
/* 404 */         p_renderModelStandardQuads_7_.addVertexData(bakedquad.getVertexDataSingle());
/* 405 */         p_renderModelStandardQuads_7_.putSprite(bakedquad.getSprite());
/*     */       }
/*     */       else
/*     */       {
/* 409 */         p_renderModelStandardQuads_7_.addVertexData(bakedquad.getVertexData());
/*     */       }
/*     */       
/* 412 */       p_renderModelStandardQuads_7_.putBrightness4(p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_);
/* 413 */       int i1 = CustomColorizer.getColorMultiplier(bakedquad, p_renderModelStandardQuads_2_, p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, p_renderModelStandardQuads_9_);
/*     */       
/* 415 */       if ((bakedquad.hasTintIndex()) || (i1 >= 0))
/*     */       {
/*     */         int l;
/*     */         int l;
/* 419 */         if (i1 >= 0)
/*     */         {
/* 421 */           l = i1;
/*     */         }
/*     */         else
/*     */         {
/* 425 */           l = p_renderModelStandardQuads_2_.colorMultiplier(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, bakedquad.getTintIndex());
/*     */         }
/*     */         
/* 428 */         if (EntityRenderer.anaglyphEnable)
/*     */         {
/* 430 */           l = TextureUtil.anaglyphColor(l);
/*     */         }
/*     */         
/* 433 */         float f = (l >> 16 & 0xFF) / 255.0F;
/* 434 */         float f1 = (l >> 8 & 0xFF) / 255.0F;
/* 435 */         float f2 = (l & 0xFF) / 255.0F;
/* 436 */         p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 4);
/* 437 */         p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 3);
/* 438 */         p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 2);
/* 439 */         p_renderModelStandardQuads_7_.putColorMultiplier(f, f1, f2, 1);
/*     */       }
/*     */       
/* 442 */       p_renderModelStandardQuads_7_.putPosition(d0, d1, d2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderModelBrightnessColor(IBakedModel bakedModel, float p_178262_2_, float p_178262_3_, float p_178262_4_, float p_178262_5_) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 448 */     int j = (arrayOfEnumFacing = EnumFacing.VALUES).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 450 */       renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getFaceQuads(enumfacing));
/*     */     }
/*     */     
/* 453 */     renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getGeneralQuads());
/*     */   }
/*     */   
/*     */   public void renderModelBrightness(IBakedModel p_178266_1_, IBlockState p_178266_2_, float p_178266_3_, boolean p_178266_4_)
/*     */   {
/* 458 */     Block block = p_178266_2_.getBlock();
/* 459 */     block.setBlockBoundsForItemRender();
/* 460 */     GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/* 461 */     int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
/*     */     
/* 463 */     if (EntityRenderer.anaglyphEnable)
/*     */     {
/* 465 */       i = TextureUtil.anaglyphColor(i);
/*     */     }
/*     */     
/* 468 */     float f = (i >> 16 & 0xFF) / 255.0F;
/* 469 */     float f1 = (i >> 8 & 0xFF) / 255.0F;
/* 470 */     float f2 = (i & 0xFF) / 255.0F;
/*     */     
/* 472 */     if (!p_178266_4_)
/*     */     {
/* 474 */       GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0F);
/*     */     }
/*     */     
/* 477 */     renderModelBrightnessColor(p_178266_1_, p_178266_3_, f, f1, f2);
/*     */   }
/*     */   
/*     */   private void renderModelBrightnessColorQuads(float p_178264_1_, float p_178264_2_, float p_178264_3_, float p_178264_4_, List p_178264_5_)
/*     */   {
/* 482 */     Tessellator tessellator = Tessellator.getInstance();
/* 483 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */     
/* 485 */     for (Object bakedquad0 : p_178264_5_)
/*     */     {
/* 487 */       BakedQuad bakedquad = (BakedQuad)bakedquad0;
/* 488 */       worldrenderer.begin(7, DefaultVertexFormats.ITEM);
/* 489 */       worldrenderer.addVertexData(bakedquad.getVertexData());
/*     */       
/* 491 */       if (bakedquad.hasTintIndex())
/*     */       {
/* 493 */         worldrenderer.putColorRGB_F4(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
/*     */       }
/*     */       else
/*     */       {
/* 497 */         worldrenderer.putColorRGB_F4(p_178264_1_, p_178264_1_, p_178264_1_);
/*     */       }
/*     */       
/* 500 */       Vec3i vec3i = bakedquad.getFace().getDirectionVec();
/* 501 */       worldrenderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/* 502 */       tessellator.draw();
/*     */     }
/*     */   }
/*     */   
/*     */   public static float fixAoLightValue(float p_fixAoLightValue_0_)
/*     */   {
/* 508 */     return p_fixAoLightValue_0_ == 0.2F ? aoLightValueOpaque : p_fixAoLightValue_0_;
/*     */   }
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
/*     */   public static class AmbientOcclusionFace
/*     */   {
/* 576 */     private final float[] vertexColorMultiplier = new float[4];
/* 577 */     private final int[] vertexBrightness = new int[4];
/*     */     
/*     */     private static final String __OBFID = "CL_00002515";
/*     */     
/*     */ 
/*     */     public AmbientOcclusionFace(BlockModelRenderer p_i46235_1_) {}
/*     */     
/*     */ 
/*     */     public AmbientOcclusionFace() {}
/*     */     
/*     */ 
/*     */     public void updateVertexBrightness(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
/*     */     {
/* 590 */       BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
/* 591 */       BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(facingIn);
/* 592 */       BlockPos blockpos1 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[0]);
/* 593 */       BlockPos blockpos2 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[1]);
/* 594 */       BlockPos blockpos3 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
/* 595 */       BlockPos blockpos4 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
/* 596 */       int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos1);
/* 597 */       int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
/* 598 */       int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
/* 599 */       int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
/* 600 */       float f = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos1).getBlock().getAmbientOcclusionLightValue());
/* 601 */       float f1 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue());
/* 602 */       float f2 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue());
/* 603 */       float f3 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue());
/* 604 */       boolean flag = blockAccessIn.getBlockState(blockpos1.offset(facingIn)).getBlock().isTranslucent();
/* 605 */       boolean flag1 = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
/* 606 */       boolean flag2 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
/* 607 */       boolean flag3 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
/*     */       int i1;
/*     */       float f4;
/*     */       int i1;
/* 611 */       if ((!flag2) && (!flag))
/*     */       {
/* 613 */         float f4 = f;
/* 614 */         i1 = i;
/*     */       }
/*     */       else
/*     */       {
/* 618 */         BlockPos blockpos5 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
/* 619 */         f4 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue());
/* 620 */         i1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
/*     */       }
/*     */       
/*     */       int j1;
/*     */       float f5;
/*     */       int j1;
/* 626 */       if ((!flag3) && (!flag))
/*     */       {
/* 628 */         float f5 = f;
/* 629 */         j1 = i;
/*     */       }
/*     */       else
/*     */       {
/* 633 */         BlockPos blockpos6 = blockpos1.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
/* 634 */         f5 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue());
/* 635 */         j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
/*     */       }
/*     */       
/*     */       int k1;
/*     */       float f6;
/*     */       int k1;
/* 641 */       if ((!flag2) && (!flag1))
/*     */       {
/* 643 */         float f6 = f1;
/* 644 */         k1 = j;
/*     */       }
/*     */       else
/*     */       {
/* 648 */         BlockPos blockpos7 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
/* 649 */         f6 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos7).getBlock().getAmbientOcclusionLightValue());
/* 650 */         k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos7);
/*     */       }
/*     */       
/*     */       int l1;
/*     */       float f7;
/*     */       int l1;
/* 656 */       if ((!flag3) && (!flag1))
/*     */       {
/* 658 */         float f7 = f1;
/* 659 */         l1 = j;
/*     */       }
/*     */       else
/*     */       {
/* 663 */         BlockPos blockpos8 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
/* 664 */         f7 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos8).getBlock().getAmbientOcclusionLightValue());
/* 665 */         l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos8);
/*     */       }
/*     */       
/* 668 */       int i2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
/*     */       
/* 670 */       if ((boundsFlags.get(0)) || (!blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()))
/*     */       {
/* 672 */         i2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
/*     */       }
/*     */       
/* 675 */       float f8 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
/* 676 */       f8 = BlockModelRenderer.fixAoLightValue(f8);
/* 677 */       BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations(facingIn);
/*     */       
/* 679 */       if ((boundsFlags.get(1)) && (blockmodelrenderer$enumneighborinfo.field_178289_i))
/*     */       {
/* 681 */         float f29 = (f3 + f + f5 + f8) * 0.25F;
/* 682 */         float f30 = (f2 + f + f4 + f8) * 0.25F;
/* 683 */         float f31 = (f2 + f1 + f6 + f8) * 0.25F;
/* 684 */         float f32 = (f3 + f1 + f7 + f8) * 0.25F;
/* 685 */         float f13 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
/* 686 */         float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
/* 687 */         float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
/* 688 */         float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
/* 689 */         float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
/* 690 */         float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
/* 691 */         float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
/* 692 */         float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
/* 693 */         float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
/* 694 */         float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
/* 695 */         float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
/* 696 */         float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
/* 697 */         float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
/* 698 */         float f26 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
/* 699 */         float f27 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
/* 700 */         float f28 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
/* 701 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$2(blockmodelrenderer$vertextranslations)] = (f29 * f13 + f30 * f14 + f31 * f15 + f32 * f16);
/* 702 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$3(blockmodelrenderer$vertextranslations)] = (f29 * f17 + f30 * f18 + f31 * f19 + f32 * f20);
/* 703 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$4(blockmodelrenderer$vertextranslations)] = (f29 * f21 + f30 * f22 + f31 * f23 + f32 * f24);
/* 704 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$5(blockmodelrenderer$vertextranslations)] = (f29 * f25 + f30 * f26 + f31 * f27 + f32 * f28);
/* 705 */         int j2 = getAoBrightness(l, i, j1, i2);
/* 706 */         int k2 = getAoBrightness(k, i, i1, i2);
/* 707 */         int l2 = getAoBrightness(k, j, k1, i2);
/* 708 */         int i3 = getAoBrightness(l, j, l1, i2);
/* 709 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$2(blockmodelrenderer$vertextranslations)] = getVertexBrightness(j2, k2, l2, i3, f13, f14, f15, f16);
/* 710 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$3(blockmodelrenderer$vertextranslations)] = getVertexBrightness(j2, k2, l2, i3, f17, f18, f19, f20);
/* 711 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$4(blockmodelrenderer$vertextranslations)] = getVertexBrightness(j2, k2, l2, i3, f21, f22, f23, f24);
/* 712 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$5(blockmodelrenderer$vertextranslations)] = getVertexBrightness(j2, k2, l2, i3, f25, f26, f27, f28);
/*     */       }
/*     */       else
/*     */       {
/* 716 */         float f9 = (f3 + f + f5 + f8) * 0.25F;
/* 717 */         float f10 = (f2 + f + f4 + f8) * 0.25F;
/* 718 */         float f11 = (f2 + f1 + f6 + f8) * 0.25F;
/* 719 */         float f12 = (f3 + f1 + f7 + f8) * 0.25F;
/* 720 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$2(blockmodelrenderer$vertextranslations)] = getAoBrightness(l, i, j1, i2);
/* 721 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$3(blockmodelrenderer$vertextranslations)] = getAoBrightness(k, i, i1, i2);
/* 722 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$4(blockmodelrenderer$vertextranslations)] = getAoBrightness(k, j, k1, i2);
/* 723 */         this.vertexBrightness[BlockModelRenderer.VertexTranslations.access$5(blockmodelrenderer$vertextranslations)] = getAoBrightness(l, j, l1, i2);
/* 724 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$2(blockmodelrenderer$vertextranslations)] = f9;
/* 725 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$3(blockmodelrenderer$vertextranslations)] = f10;
/* 726 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$4(blockmodelrenderer$vertextranslations)] = f11;
/* 727 */         this.vertexColorMultiplier[BlockModelRenderer.VertexTranslations.access$5(blockmodelrenderer$vertextranslations)] = f12;
/*     */       }
/*     */     }
/*     */     
/*     */     private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, int p_147778_4_)
/*     */     {
/* 733 */       if (p_147778_1_ == 0)
/*     */       {
/* 735 */         p_147778_1_ = p_147778_4_;
/*     */       }
/*     */       
/* 738 */       if (p_147778_2_ == 0)
/*     */       {
/* 740 */         p_147778_2_ = p_147778_4_;
/*     */       }
/*     */       
/* 743 */       if (p_147778_3_ == 0)
/*     */       {
/* 745 */         p_147778_3_ = p_147778_4_;
/*     */       }
/*     */       
/* 748 */       return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
/*     */     }
/*     */     
/*     */     private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_)
/*     */     {
/* 753 */       int i = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
/* 754 */       int j = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
/* 755 */       return i << 16 | j;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumNeighborInfo
/*     */   {
/* 761 */     DOWN("DOWN", 0, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5F, false, new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0]), 
/* 762 */     UP("UP", 1, new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0F, false, new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0], new BlockModelRenderer.Orientation[0]), 
/* 763 */     NORTH("NORTH", 2, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST }, 0.8F, true, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST }), 
/* 764 */     SOUTH("SOUTH", 3, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }, 0.8F, true, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST }), 
/* 765 */     WEST("WEST", 4, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6F, true, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH }), 
/* 766 */     EAST("EAST", 5, new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6F, true, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH }, new BlockModelRenderer.Orientation[] { BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH });
/*     */     
/*     */     protected final EnumFacing[] field_178276_g;
/*     */     protected final float field_178288_h;
/*     */     protected final boolean field_178289_i;
/*     */     protected final BlockModelRenderer.Orientation[] field_178286_j;
/*     */     protected final BlockModelRenderer.Orientation[] field_178287_k;
/*     */     protected final BlockModelRenderer.Orientation[] field_178284_l;
/*     */     protected final BlockModelRenderer.Orientation[] field_178285_m;
/*     */     private static final EnumNeighborInfo[] field_178282_n;
/*     */     private static final EnumNeighborInfo[] $VALUES;
/*     */     private static final String __OBFID = "CL_00002516";
/*     */     
/*     */     private EnumNeighborInfo(String p_i5_3_, int p_i5_4_, EnumFacing[] p_i5_5_, float p_i5_6_, boolean p_i5_7_, BlockModelRenderer.Orientation[] p_i5_8_, BlockModelRenderer.Orientation[] p_i5_9_, BlockModelRenderer.Orientation[] p_i5_10_, BlockModelRenderer.Orientation[] p_i5_11_)
/*     */     {
/* 781 */       this.field_178276_g = p_i5_5_;
/* 782 */       this.field_178288_h = p_i5_6_;
/* 783 */       this.field_178289_i = p_i5_7_;
/* 784 */       this.field_178286_j = p_i5_8_;
/* 785 */       this.field_178287_k = p_i5_9_;
/* 786 */       this.field_178284_l = p_i5_10_;
/* 787 */       this.field_178285_m = p_i5_11_;
/*     */     }
/*     */     
/*     */     public static EnumNeighborInfo getNeighbourInfo(EnumFacing p_178273_0_)
/*     */     {
/* 792 */       return field_178282_n[p_178273_0_.getIndex()];
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 775 */       field_178282_n = new EnumNeighborInfo[6];
/* 776 */       $VALUES = new EnumNeighborInfo[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
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
/* 796 */       field_178282_n[EnumFacing.DOWN.getIndex()] = DOWN;
/* 797 */       field_178282_n[EnumFacing.UP.getIndex()] = UP;
/* 798 */       field_178282_n[EnumFacing.NORTH.getIndex()] = NORTH;
/* 799 */       field_178282_n[EnumFacing.SOUTH.getIndex()] = SOUTH;
/* 800 */       field_178282_n[EnumFacing.WEST.getIndex()] = WEST;
/* 801 */       field_178282_n[EnumFacing.EAST.getIndex()] = EAST;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum Orientation
/*     */   {
/* 807 */     DOWN("DOWN", 0, EnumFacing.DOWN, false), 
/* 808 */     UP("UP", 1, EnumFacing.UP, false), 
/* 809 */     NORTH("NORTH", 2, EnumFacing.NORTH, false), 
/* 810 */     SOUTH("SOUTH", 3, EnumFacing.SOUTH, false), 
/* 811 */     WEST("WEST", 4, EnumFacing.WEST, false), 
/* 812 */     EAST("EAST", 5, EnumFacing.EAST, false), 
/* 813 */     FLIP_DOWN("FLIP_DOWN", 6, EnumFacing.DOWN, true), 
/* 814 */     FLIP_UP("FLIP_UP", 7, EnumFacing.UP, true), 
/* 815 */     FLIP_NORTH("FLIP_NORTH", 8, EnumFacing.NORTH, true), 
/* 816 */     FLIP_SOUTH("FLIP_SOUTH", 9, EnumFacing.SOUTH, true), 
/* 817 */     FLIP_WEST("FLIP_WEST", 10, EnumFacing.WEST, true), 
/* 818 */     FLIP_EAST("FLIP_EAST", 11, EnumFacing.EAST, true);
/*     */     
/*     */     protected final int field_178229_m;
/* 821 */     private static final Orientation[] $VALUES = { DOWN, UP, NORTH, SOUTH, WEST, EAST, FLIP_DOWN, FLIP_UP, FLIP_NORTH, FLIP_SOUTH, FLIP_WEST, FLIP_EAST };
/*     */     private static final String __OBFID = "CL_00002513";
/*     */     
/*     */     private Orientation(String p_i7_3_, int p_i7_4_, EnumFacing p_i7_5_, boolean p_i7_6_)
/*     */     {
/* 826 */       this.field_178229_m = (p_i7_5_.getIndex() + (p_i7_6_ ? EnumFacing.values().length : 0));
/*     */     }
/*     */   }
/*     */   
/*     */   static enum VertexTranslations
/*     */   {
/* 832 */     DOWN("DOWN", 0, 0, 1, 2, 3), 
/* 833 */     UP("UP", 1, 2, 3, 0, 1), 
/* 834 */     NORTH("NORTH", 2, 3, 0, 1, 2), 
/* 835 */     SOUTH("SOUTH", 3, 0, 1, 2, 3), 
/* 836 */     WEST("WEST", 4, 3, 0, 1, 2), 
/* 837 */     EAST("EAST", 5, 1, 2, 3, 0);
/*     */     
/*     */     private final int field_178191_g;
/*     */     private final int field_178200_h;
/*     */     private final int field_178201_i;
/*     */     private final int field_178198_j;
/*     */     private static final VertexTranslations[] field_178199_k;
/*     */     private static final VertexTranslations[] $VALUES;
/*     */     private static final String __OBFID = "CL_00002514";
/*     */     
/*     */     private VertexTranslations(String p_i6_3_, int p_i6_4_, int p_i6_5_, int p_i6_6_, int p_i6_7_, int p_i6_8_)
/*     */     {
/* 849 */       this.field_178191_g = p_i6_5_;
/* 850 */       this.field_178200_h = p_i6_6_;
/* 851 */       this.field_178201_i = p_i6_7_;
/* 852 */       this.field_178198_j = p_i6_8_;
/*     */     }
/*     */     
/*     */     public static VertexTranslations getVertexTranslations(EnumFacing p_178184_0_)
/*     */     {
/* 857 */       return field_178199_k[p_178184_0_.getIndex()];
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 843 */       field_178199_k = new VertexTranslations[6];
/* 844 */       $VALUES = new VertexTranslations[] { DOWN, UP, NORTH, SOUTH, WEST, EAST };
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
/* 861 */       field_178199_k[EnumFacing.DOWN.getIndex()] = DOWN;
/* 862 */       field_178199_k[EnumFacing.UP.getIndex()] = UP;
/* 863 */       field_178199_k[EnumFacing.NORTH.getIndex()] = NORTH;
/* 864 */       field_178199_k[EnumFacing.SOUTH.getIndex()] = SOUTH;
/* 865 */       field_178199_k[EnumFacing.WEST.getIndex()] = WEST;
/* 866 */       field_178199_k[EnumFacing.EAST.getIndex()] = EAST;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\BlockModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */