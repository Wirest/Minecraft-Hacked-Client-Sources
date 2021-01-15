/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import optfine.RenderEnv;
/*     */ 
/*     */ public class BlockFluidRenderer
/*     */ {
/*  18 */   private TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
/*  19 */   private TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
/*     */   private static final String __OBFID = "CL_00002519";
/*     */   
/*     */   public BlockFluidRenderer()
/*     */   {
/*  24 */     initAtlasSprites();
/*     */   }
/*     */   
/*     */   protected void initAtlasSprites()
/*     */   {
/*  29 */     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
/*  30 */     this.atlasSpritesLava[0] = texturemap.getAtlasSprite("minecraft:blocks/lava_still");
/*  31 */     this.atlasSpritesLava[1] = texturemap.getAtlasSprite("minecraft:blocks/lava_flow");
/*  32 */     this.atlasSpritesWater[0] = texturemap.getAtlasSprite("minecraft:blocks/water_still");
/*  33 */     this.atlasSpritesWater[1] = texturemap.getAtlasSprite("minecraft:blocks/water_flow");
/*     */   }
/*     */   
/*     */   public boolean renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn)
/*     */   {
/*  38 */     BlockLiquid blockliquid = (BlockLiquid)blockStateIn.getBlock();
/*  39 */     blockliquid.setBlockBoundsBasedOnState(blockAccess, blockPosIn);
/*  40 */     TextureAtlasSprite[] atextureatlassprite = blockliquid.getMaterial() == Material.lava ? this.atlasSpritesLava : this.atlasSpritesWater;
/*  41 */     int i = optfine.CustomColorizer.getFluidColor(blockliquid, blockAccess, blockPosIn);
/*  42 */     float f = (i >> 16 & 0xFF) / 255.0F;
/*  43 */     float f1 = (i >> 8 & 0xFF) / 255.0F;
/*  44 */     float f2 = (i & 0xFF) / 255.0F;
/*  45 */     boolean flag = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.up(), EnumFacing.UP);
/*  46 */     boolean flag1 = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.down(), EnumFacing.DOWN);
/*  47 */     RenderEnv renderenv = RenderEnv.getInstance(blockAccess, blockStateIn, blockPosIn);
/*  48 */     boolean[] aboolean = renderenv.getBorderFlags();
/*  49 */     aboolean[0] = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.north(), EnumFacing.NORTH);
/*  50 */     aboolean[1] = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.south(), EnumFacing.SOUTH);
/*  51 */     aboolean[2] = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.west(), EnumFacing.WEST);
/*  52 */     aboolean[3] = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.east(), EnumFacing.EAST);
/*     */     
/*  54 */     if ((!flag) && (!flag1) && (aboolean[0] == 0) && (aboolean[1] == 0) && (aboolean[2] == 0) && (aboolean[3] == 0))
/*     */     {
/*  56 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  60 */     boolean flag2 = false;
/*  61 */     float f3 = 0.5F;
/*  62 */     float f4 = 1.0F;
/*  63 */     float f5 = 0.8F;
/*  64 */     float f6 = 0.6F;
/*  65 */     Material material = blockliquid.getMaterial();
/*  66 */     float f7 = getFluidHeight(blockAccess, blockPosIn, material);
/*  67 */     float f8 = getFluidHeight(blockAccess, blockPosIn.south(), material);
/*  68 */     float f9 = getFluidHeight(blockAccess, blockPosIn.east().south(), material);
/*  69 */     float f10 = getFluidHeight(blockAccess, blockPosIn.east(), material);
/*  70 */     double d0 = blockPosIn.getX();
/*  71 */     double d1 = blockPosIn.getY();
/*  72 */     double d2 = blockPosIn.getZ();
/*  73 */     float f11 = 0.001F;
/*     */     
/*  75 */     if (flag)
/*     */     {
/*  77 */       flag2 = true;
/*  78 */       TextureAtlasSprite textureatlassprite = atextureatlassprite[0];
/*  79 */       float f12 = (float)BlockLiquid.getFlowDirection(blockAccess, blockPosIn, material);
/*     */       
/*  81 */       if (f12 > -999.0F)
/*     */       {
/*  83 */         textureatlassprite = atextureatlassprite[1];
/*     */       }
/*     */       
/*  86 */       worldRendererIn.setSprite(textureatlassprite);
/*  87 */       f7 -= f11;
/*  88 */       f8 -= f11;
/*  89 */       f9 -= f11;
/*  90 */       f10 -= f11;
/*     */       float f21;
/*     */       float f13;
/*     */       float f17;
/*     */       float f14;
/*     */       float f19;
/*     */       float f15;
/*     */       float f20;
/*     */       float f16;
/*     */       float f21;
/* 100 */       if (f12 < -999.0F)
/*     */       {
/* 102 */         float f13 = textureatlassprite.getInterpolatedU(0.0D);
/* 103 */         float f17 = textureatlassprite.getInterpolatedV(0.0D);
/* 104 */         float f14 = f13;
/* 105 */         float f19 = textureatlassprite.getInterpolatedV(16.0D);
/* 106 */         float f15 = textureatlassprite.getInterpolatedU(16.0D);
/* 107 */         float f20 = f19;
/* 108 */         float f16 = f15;
/* 109 */         f21 = f17;
/*     */       }
/*     */       else
/*     */       {
/* 113 */         float f22 = MathHelper.sin(f12) * 0.25F;
/* 114 */         float f23 = MathHelper.cos(f12) * 0.25F;
/* 115 */         float f24 = 8.0F;
/* 116 */         f13 = textureatlassprite.getInterpolatedU(8.0F + (-f23 - f22) * 16.0F);
/* 117 */         f17 = textureatlassprite.getInterpolatedV(8.0F + (-f23 + f22) * 16.0F);
/* 118 */         f14 = textureatlassprite.getInterpolatedU(8.0F + (-f23 + f22) * 16.0F);
/* 119 */         f19 = textureatlassprite.getInterpolatedV(8.0F + (f23 + f22) * 16.0F);
/* 120 */         f15 = textureatlassprite.getInterpolatedU(8.0F + (f23 + f22) * 16.0F);
/* 121 */         f20 = textureatlassprite.getInterpolatedV(8.0F + (f23 - f22) * 16.0F);
/* 122 */         f16 = textureatlassprite.getInterpolatedU(8.0F + (f23 - f22) * 16.0F);
/* 123 */         f21 = textureatlassprite.getInterpolatedV(8.0F + (-f23 - f22) * 16.0F);
/*     */       }
/*     */       
/* 126 */       int k2 = blockliquid.getMixedBrightnessForBlock(blockAccess, blockPosIn);
/* 127 */       int l2 = k2 >> 16 & 0xFFFF;
/* 128 */       int i3 = k2 & 0xFFFF;
/* 129 */       float f25 = f4 * f;
/* 130 */       float f26 = f4 * f1;
/* 131 */       float f18 = f4 * f2;
/* 132 */       worldRendererIn.pos(d0 + 0.0D, d1 + f7, d2 + 0.0D).color(f25, f26, f18, 1.0F).tex(f13, f17).lightmap(l2, i3).endVertex();
/* 133 */       worldRendererIn.pos(d0 + 0.0D, d1 + f8, d2 + 1.0D).color(f25, f26, f18, 1.0F).tex(f14, f19).lightmap(l2, i3).endVertex();
/* 134 */       worldRendererIn.pos(d0 + 1.0D, d1 + f9, d2 + 1.0D).color(f25, f26, f18, 1.0F).tex(f15, f20).lightmap(l2, i3).endVertex();
/* 135 */       worldRendererIn.pos(d0 + 1.0D, d1 + f10, d2 + 0.0D).color(f25, f26, f18, 1.0F).tex(f16, f21).lightmap(l2, i3).endVertex();
/*     */       
/* 137 */       if (blockliquid.func_176364_g(blockAccess, blockPosIn.up()))
/*     */       {
/* 139 */         worldRendererIn.pos(d0 + 0.0D, d1 + f7, d2 + 0.0D).color(f25, f26, f18, 1.0F).tex(f13, f17).lightmap(l2, i3).endVertex();
/* 140 */         worldRendererIn.pos(d0 + 1.0D, d1 + f10, d2 + 0.0D).color(f25, f26, f18, 1.0F).tex(f16, f21).lightmap(l2, i3).endVertex();
/* 141 */         worldRendererIn.pos(d0 + 1.0D, d1 + f9, d2 + 1.0D).color(f25, f26, f18, 1.0F).tex(f15, f20).lightmap(l2, i3).endVertex();
/* 142 */         worldRendererIn.pos(d0 + 0.0D, d1 + f8, d2 + 1.0D).color(f25, f26, f18, 1.0F).tex(f14, f19).lightmap(l2, i3).endVertex();
/*     */       }
/*     */     }
/*     */     
/* 146 */     if (flag1)
/*     */     {
/* 148 */       float f35 = atextureatlassprite[0].getMinU();
/* 149 */       float f36 = atextureatlassprite[0].getMaxU();
/* 150 */       float f37 = atextureatlassprite[0].getMinV();
/* 151 */       float f38 = atextureatlassprite[0].getMaxV();
/* 152 */       int i1 = blockliquid.getMixedBrightnessForBlock(blockAccess, blockPosIn.down());
/* 153 */       int k1 = i1 >> 16 & 0xFFFF;
/* 154 */       int i2 = i1 & 0xFFFF;
/* 155 */       worldRendererIn.pos(d0, d1, d2 + 1.0D).color(f3, f3, f3, 1.0F).tex(f35, f38).lightmap(k1, i2).endVertex();
/* 156 */       worldRendererIn.pos(d0, d1, d2).color(f3, f3, f3, 1.0F).tex(f35, f37).lightmap(k1, i2).endVertex();
/* 157 */       worldRendererIn.pos(d0 + 1.0D, d1, d2).color(f3, f3, f3, 1.0F).tex(f36, f37).lightmap(k1, i2).endVertex();
/* 158 */       worldRendererIn.pos(d0 + 1.0D, d1, d2 + 1.0D).color(f3, f3, f3, 1.0F).tex(f36, f38).lightmap(k1, i2).endVertex();
/* 159 */       flag2 = true;
/*     */     }
/*     */     
/* 162 */     for (int j1 = 0; j1 < 4; j1++)
/*     */     {
/* 164 */       int l1 = 0;
/* 165 */       int j2 = 0;
/*     */       
/* 167 */       if (j1 == 0)
/*     */       {
/* 169 */         j2--;
/*     */       }
/*     */       
/* 172 */       if (j1 == 1)
/*     */       {
/* 174 */         j2++;
/*     */       }
/*     */       
/* 177 */       if (j1 == 2)
/*     */       {
/* 179 */         l1--;
/*     */       }
/*     */       
/* 182 */       if (j1 == 3)
/*     */       {
/* 184 */         l1++;
/*     */       }
/*     */       
/* 187 */       BlockPos blockpos = blockPosIn.add(l1, 0, j2);
/* 188 */       TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];
/* 189 */       worldRendererIn.setSprite(textureatlassprite1);
/*     */       
/* 191 */       if (aboolean[j1] != 0)
/*     */       {
/*     */         double d4;
/*     */         float f39;
/*     */         float f40;
/*     */         double d5;
/*     */         double d3;
/*     */         double d6;
/*     */         double d4;
/* 200 */         if (j1 == 0)
/*     */         {
/* 202 */           float f39 = f7;
/* 203 */           float f40 = f10;
/* 204 */           double d5 = d0;
/* 205 */           double d3 = d0 + 1.0D;
/* 206 */           double d6 = d2 + f11;
/* 207 */           d4 = d2 + f11;
/*     */         } else { double d4;
/* 209 */           if (j1 == 1)
/*     */           {
/* 211 */             float f39 = f9;
/* 212 */             float f40 = f8;
/* 213 */             double d5 = d0 + 1.0D;
/* 214 */             double d3 = d0;
/* 215 */             double d6 = d2 + 1.0D - f11;
/* 216 */             d4 = d2 + 1.0D - f11;
/*     */           } else { double d4;
/* 218 */             if (j1 == 2)
/*     */             {
/* 220 */               float f39 = f8;
/* 221 */               float f40 = f7;
/* 222 */               double d5 = d0 + f11;
/* 223 */               double d3 = d0 + f11;
/* 224 */               double d6 = d2 + 1.0D;
/* 225 */               d4 = d2;
/*     */             }
/*     */             else
/*     */             {
/* 229 */               f39 = f10;
/* 230 */               f40 = f9;
/* 231 */               d5 = d0 + 1.0D - f11;
/* 232 */               d3 = d0 + 1.0D - f11;
/* 233 */               d6 = d2;
/* 234 */               d4 = d2 + 1.0D;
/*     */             }
/*     */           } }
/* 237 */         flag2 = true;
/* 238 */         float f41 = textureatlassprite1.getInterpolatedU(0.0D);
/* 239 */         float f27 = textureatlassprite1.getInterpolatedU(8.0D);
/* 240 */         float f28 = textureatlassprite1.getInterpolatedV((1.0F - f39) * 16.0F * 0.5F);
/* 241 */         float f29 = textureatlassprite1.getInterpolatedV((1.0F - f40) * 16.0F * 0.5F);
/* 242 */         float f30 = textureatlassprite1.getInterpolatedV(8.0D);
/* 243 */         int j = blockliquid.getMixedBrightnessForBlock(blockAccess, blockpos);
/* 244 */         int k = j >> 16 & 0xFFFF;
/* 245 */         int l = j & 0xFFFF;
/* 246 */         float f31 = j1 < 2 ? f5 : f6;
/* 247 */         float f32 = f4 * f31 * f;
/* 248 */         float f33 = f4 * f31 * f1;
/* 249 */         float f34 = f4 * f31 * f2;
/* 250 */         worldRendererIn.pos(d5, d1 + f39, d6).color(f32, f33, f34, 1.0F).tex(f41, f28).lightmap(k, l).endVertex();
/* 251 */         worldRendererIn.pos(d3, d1 + f40, d4).color(f32, f33, f34, 1.0F).tex(f27, f29).lightmap(k, l).endVertex();
/* 252 */         worldRendererIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex(f27, f30).lightmap(k, l).endVertex();
/* 253 */         worldRendererIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex(f41, f30).lightmap(k, l).endVertex();
/* 254 */         worldRendererIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F).tex(f41, f30).lightmap(k, l).endVertex();
/* 255 */         worldRendererIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F).tex(f27, f30).lightmap(k, l).endVertex();
/* 256 */         worldRendererIn.pos(d3, d1 + f40, d4).color(f32, f33, f34, 1.0F).tex(f27, f29).lightmap(k, l).endVertex();
/* 257 */         worldRendererIn.pos(d5, d1 + f39, d6).color(f32, f33, f34, 1.0F).tex(f41, f28).lightmap(k, l).endVertex();
/*     */       }
/*     */     }
/*     */     
/* 261 */     worldRendererIn.setSprite(null);
/* 262 */     return flag2;
/*     */   }
/*     */   
/*     */ 
/*     */   private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial)
/*     */   {
/* 268 */     int i = 0;
/* 269 */     float f = 0.0F;
/*     */     
/* 271 */     for (int j = 0; j < 4; j++)
/*     */     {
/* 273 */       BlockPos blockpos = blockPosIn.add(-(j & 0x1), 0, -(j >> 1 & 0x1));
/*     */       
/* 275 */       if (blockAccess.getBlockState(blockpos.up()).getBlock().getMaterial() == blockMaterial)
/*     */       {
/* 277 */         return 1.0F;
/*     */       }
/*     */       
/* 280 */       IBlockState iblockstate = blockAccess.getBlockState(blockpos);
/* 281 */       Material material = iblockstate.getBlock().getMaterial();
/*     */       
/* 283 */       if (material != blockMaterial)
/*     */       {
/* 285 */         if (!material.isSolid())
/*     */         {
/* 287 */           f += 1.0F;
/* 288 */           i++;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 293 */         int k = ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue();
/*     */         
/* 295 */         if ((k >= 8) || (k == 0))
/*     */         {
/* 297 */           f += BlockLiquid.getLiquidHeightPercent(k) * 10.0F;
/* 298 */           i += 10;
/*     */         }
/*     */         
/* 301 */         f += BlockLiquid.getLiquidHeightPercent(k);
/* 302 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 306 */     return 1.0F - f / i;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\BlockFluidRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */