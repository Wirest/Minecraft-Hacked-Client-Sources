package net.minecraft.client.renderer;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.RenderEnv;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockFluidRenderer
{
    private TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];

    public BlockFluidRenderer()
    {
        this.initAtlasSprites();
    }

    protected void initAtlasSprites()
    {
        TextureMap var1 = Minecraft.getMinecraft().getTextureMapBlocks();
        this.atlasSpritesLava[0] = var1.getAtlasSprite("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = var1.getAtlasSprite("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = var1.getAtlasSprite("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = var1.getAtlasSprite("minecraft:blocks/water_flow");
    }

    public boolean renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn)
    {
        BlockLiquid var5 = (BlockLiquid)blockStateIn.getBlock();
        var5.setBlockBoundsBasedOnState(blockAccess, blockPosIn);
        TextureAtlasSprite[] var6 = var5.getMaterial() == Material.lava ? this.atlasSpritesLava : this.atlasSpritesWater;
        int var7 = CustomColorizer.getFluidColor(var5, blockAccess, blockPosIn);
        float var8 = (var7 >> 16 & 255) / 255.0F;
        float var9 = (var7 >> 8 & 255) / 255.0F;
        float var10 = (var7 & 255) / 255.0F;
        boolean var11 = var5.shouldSideBeRendered(blockAccess, blockPosIn.up(), EnumFacing.UP);
        boolean var12 = var5.shouldSideBeRendered(blockAccess, blockPosIn.down(), EnumFacing.DOWN);
        RenderEnv renderEnv = RenderEnv.getInstance(blockAccess, blockStateIn, blockPosIn);
        boolean[] var13 = renderEnv.getBorderFlags();
        var13[0] = var5.shouldSideBeRendered(blockAccess, blockPosIn.north(), EnumFacing.NORTH);
        var13[1] = var5.shouldSideBeRendered(blockAccess, blockPosIn.south(), EnumFacing.SOUTH);
        var13[2] = var5.shouldSideBeRendered(blockAccess, blockPosIn.west(), EnumFacing.WEST);
        var13[3] = var5.shouldSideBeRendered(blockAccess, blockPosIn.east(), EnumFacing.EAST);

        if (!var11 && !var12 && !var13[0] && !var13[1] && !var13[2] && !var13[3])
        {
            return false;
        }
        else
        {
            boolean var14 = false;
            float var15 = 0.5F;
            float var16 = 1.0F;
            float var17 = 0.8F;
            float var18 = 0.6F;
            Material var19 = var5.getMaterial();
            float var20 = this.getFluidHeight(blockAccess, blockPosIn, var19);
            float var21 = this.getFluidHeight(blockAccess, blockPosIn.south(), var19);
            float var22 = this.getFluidHeight(blockAccess, blockPosIn.east().south(), var19);
            float var23 = this.getFluidHeight(blockAccess, blockPosIn.east(), var19);
            double var24 = blockPosIn.getX();
            double var26 = blockPosIn.getY();
            double var28 = blockPosIn.getZ();
            float var30 = 0.001F;
            TextureAtlasSprite var31;
            float var32;
            float var33;
            float var34;
            float var35;
            float var36;
            float var37;

            if (var11)
            {
                var14 = true;
                var31 = var6[0];
                var32 = (float)BlockLiquid.getFlowDirection(blockAccess, blockPosIn, var19);

                if (var32 > -999.0F)
                {
                    var31 = var6[1];
                }

                worldRendererIn.setSprite(var31);
                var20 -= var30;
                var21 -= var30;
                var22 -= var30;
                var23 -= var30;
                float var52;
                float var53;
                float var54;

                if (var32 < -999.0F)
                {
                    var33 = var31.getInterpolatedU(0.0D);
                    var37 = var31.getInterpolatedV(0.0D);
                    var34 = var33;
                    var52 = var31.getInterpolatedV(16.0D);
                    var35 = var31.getInterpolatedU(16.0D);
                    var53 = var52;
                    var36 = var35;
                    var54 = var37;
                }
                else
                {
                    float var55 = MathHelper.sin(var32) * 0.25F;
                    float var44 = MathHelper.cos(var32) * 0.25F;
                    var33 = var31.getInterpolatedU(8.0F + (-var44 - var55) * 16.0F);
                    var37 = var31.getInterpolatedV(8.0F + (-var44 + var55) * 16.0F);
                    var34 = var31.getInterpolatedU(8.0F + (-var44 + var55) * 16.0F);
                    var52 = var31.getInterpolatedV(8.0F + (var44 + var55) * 16.0F);
                    var35 = var31.getInterpolatedU(8.0F + (var44 + var55) * 16.0F);
                    var53 = var31.getInterpolatedV(8.0F + (var44 - var55) * 16.0F);
                    var36 = var31.getInterpolatedU(8.0F + (var44 - var55) * 16.0F);
                    var54 = var31.getInterpolatedV(8.0F + (-var44 - var55) * 16.0F);
                }

                worldRendererIn.setBrightness(var5.getMixedBrightnessForBlock(blockAccess, blockPosIn));
                worldRendererIn.setColorOpaque_F(var16 * var8, var16 * var9, var16 * var10);
                worldRendererIn.addVertexWithUV(var24 + 0.0D, var26 + var20, var28 + 0.0D, var33, var37);
                worldRendererIn.addVertexWithUV(var24 + 0.0D, var26 + var21, var28 + 1.0D, var34, var52);
                worldRendererIn.addVertexWithUV(var24 + 1.0D, var26 + var22, var28 + 1.0D, var35, var53);
                worldRendererIn.addVertexWithUV(var24 + 1.0D, var26 + var23, var28 + 0.0D, var36, var54);

                if (var5.func_176364_g(blockAccess, blockPosIn.up()))
                {
                    worldRendererIn.addVertexWithUV(var24 + 0.0D, var26 + var20, var28 + 0.0D, var33, var37);
                    worldRendererIn.addVertexWithUV(var24 + 1.0D, var26 + var23, var28 + 0.0D, var36, var54);
                    worldRendererIn.addVertexWithUV(var24 + 1.0D, var26 + var22, var28 + 1.0D, var35, var53);
                    worldRendererIn.addVertexWithUV(var24 + 0.0D, var26 + var21, var28 + 1.0D, var34, var52);
                }
            }

            if (var12)
            {
                worldRendererIn.setBrightness(var5.getMixedBrightnessForBlock(blockAccess, blockPosIn.down()));
                worldRendererIn.setColorOpaque_F(var15, var15, var15);
                var32 = var6[0].getMinU();
                var33 = var6[0].getMaxU();
                var34 = var6[0].getMinV();
                var35 = var6[0].getMaxV();
                worldRendererIn.addVertexWithUV(var24, var26, var28 + 1.0D, var32, var35);
                worldRendererIn.addVertexWithUV(var24, var26, var28, var32, var34);
                worldRendererIn.addVertexWithUV(var24 + 1.0D, var26, var28, var33, var34);
                worldRendererIn.addVertexWithUV(var24 + 1.0D, var26, var28 + 1.0D, var33, var35);
                var14 = true;
            }

            for (int var571 = 0; var571 < 4; ++var571)
            {
                int var581 = 0;
                int var59 = 0;

                if (var571 == 0)
                {
                    --var59;
                }

                if (var571 == 1)
                {
                    ++var59;
                }

                if (var571 == 2)
                {
                    --var581;
                }

                if (var571 == 3)
                {
                    ++var581;
                }

                BlockPos var60 = blockPosIn.add(var581, 0, var59);
                var31 = var6[1];
                worldRendererIn.setSprite(var31);

                if (var13[var571])
                {
                    double var56;
                    double var57;
                    double var58;
                    double var61;

                    if (var571 == 0)
                    {
                        var36 = var20;
                        var37 = var23;
                        var56 = var24;
                        var58 = var24 + 1.0D;
                        var57 = var28 + var30;
                        var61 = var28 + var30;
                    }
                    else if (var571 == 1)
                    {
                        var36 = var22;
                        var37 = var21;
                        var56 = var24 + 1.0D;
                        var58 = var24;
                        var57 = var28 + 1.0D - var30;
                        var61 = var28 + 1.0D - var30;
                    }
                    else if (var571 == 2)
                    {
                        var36 = var21;
                        var37 = var20;
                        var56 = var24 + var30;
                        var58 = var24 + var30;
                        var57 = var28 + 1.0D;
                        var61 = var28;
                    }
                    else
                    {
                        var36 = var23;
                        var37 = var22;
                        var56 = var24 + 1.0D - var30;
                        var58 = var24 + 1.0D - var30;
                        var57 = var28;
                        var61 = var28 + 1.0D;
                    }

                    var14 = true;
                    float var46 = var31.getInterpolatedU(0.0D);
                    float var47 = var31.getInterpolatedU(8.0D);
                    float var48 = var31.getInterpolatedV((1.0F - var36) * 16.0F * 0.5F);
                    float var49 = var31.getInterpolatedV((1.0F - var37) * 16.0F * 0.5F);
                    float var50 = var31.getInterpolatedV(8.0D);
                    worldRendererIn.setBrightness(var5.getMixedBrightnessForBlock(blockAccess, var60));
                    float var51 = 1.0F;
                    var51 *= var571 < 2 ? var17 : var18;
                    worldRendererIn.setColorOpaque_F(var16 * var51 * var8, var16 * var51 * var9, var16 * var51 * var10);
                    worldRendererIn.addVertexWithUV(var56, var26 + var36, var57, var46, var48);
                    worldRendererIn.addVertexWithUV(var58, var26 + var37, var61, var47, var49);
                    worldRendererIn.addVertexWithUV(var58, var26 + 0.0D, var61, var47, var50);
                    worldRendererIn.addVertexWithUV(var56, var26 + 0.0D, var57, var46, var50);
                    worldRendererIn.addVertexWithUV(var56, var26 + 0.0D, var57, var46, var50);
                    worldRendererIn.addVertexWithUV(var58, var26 + 0.0D, var61, var47, var50);
                    worldRendererIn.addVertexWithUV(var58, var26 + var37, var61, var47, var49);
                    worldRendererIn.addVertexWithUV(var56, var26 + var36, var57, var46, var48);
                }
            }

            worldRendererIn.setSprite((TextureAtlasSprite)null);
            return var14;
        }
    }

    private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial)
    {
        int var4 = 0;
        float var5 = 0.0F;

        for (int var6 = 0; var6 < 4; ++var6)
        {
            BlockPos var7 = blockPosIn.add(-(var6 & 1), 0, -(var6 >> 1 & 1));

            if (blockAccess.getBlockState(var7.up()).getBlock().getMaterial() == blockMaterial)
            {
                return 1.0F;
            }

            IBlockState var8 = blockAccess.getBlockState(var7);
            Material var9 = var8.getBlock().getMaterial();

            if (var9 == blockMaterial)
            {
                int var10 = ((Integer)var8.getValue(BlockLiquid.LEVEL)).intValue();

                if (var10 >= 8 || var10 == 0)
                {
                    var5 += BlockLiquid.getLiquidHeightPercent(var10) * 10.0F;
                    var4 += 10;
                }

                var5 += BlockLiquid.getLiquidHeightPercent(var10);
                ++var4;
            }
            else if (!var9.isSolid())
            {
                ++var5;
                ++var4;
            }
        }

        return 1.0F - var5 / var4;
    }
}
