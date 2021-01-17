// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.util.Iterator;
import java.util.BitSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import optifine.CustomColors;
import optifine.NaturalTextures;
import optifine.ConnectedTextures;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.Vec3i;
import net.minecraft.util.MathHelper;
import java.util.List;
import optifine.BetterSnow;
import optifine.BetterGrass;
import optifine.RenderEnv;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import optifine.SmartLeaves;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import optifine.Reflector;
import optifine.Config;

public class BlockModelRenderer
{
    private static final String __OBFID = "CL_00002518";
    private static float aoLightValueOpaque;
    
    static {
        BlockModelRenderer.aoLightValueOpaque = 0.2f;
    }
    
    public static void updateAoLightValue() {
        BlockModelRenderer.aoLightValueOpaque = 1.0f - Config.getAmbientOcclusionLevel() * 0.8f;
    }
    
    public BlockModelRenderer() {
        if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
            Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
        }
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn) {
        final Block block = blockStateIn.getBlock();
        block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        final boolean flag = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();
        try {
            final Block block = blockStateIn.getBlock();
            if (Config.isTreesSmart() && blockStateIn.getBlock() instanceof BlockLeavesBase) {
                modelIn = SmartLeaves.getLeavesModel(modelIn);
            }
            return flag ? this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides) : this.renderModelStandard(blockAccessIn, modelIn, block, blockStateIn, blockPosIn, worldRendererIn, checkSides);
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }
    
    public boolean renderModelAmbientOcclusion(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        return this.renderModelAmbientOcclusion(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
    }
    
    public boolean renderModelAmbientOcclusion(final IBlockAccess p_renderModelAmbientOcclusion_1_, final IBakedModel p_renderModelAmbientOcclusion_2_, final Block p_renderModelAmbientOcclusion_3_, final IBlockState p_renderModelAmbientOcclusion_4_, final BlockPos p_renderModelAmbientOcclusion_5_, final WorldRenderer p_renderModelAmbientOcclusion_6_, final boolean p_renderModelAmbientOcclusion_7_) {
        boolean flag = false;
        RenderEnv renderenv = null;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            List list = p_renderModelAmbientOcclusion_2_.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                final BlockPos blockpos = p_renderModelAmbientOcclusion_5_.offset(enumfacing);
                if (!p_renderModelAmbientOcclusion_7_ || p_renderModelAmbientOcclusion_3_.shouldSideBeRendered(p_renderModelAmbientOcclusion_1_, blockpos, enumfacing)) {
                    if (renderenv == null) {
                        renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
                    }
                    if (!renderenv.isBreakingAnimation(list) && Config.isBetterGrass()) {
                        list = BetterGrass.getFaceQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, enumfacing, list);
                    }
                    this.renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list, renderenv);
                    flag = true;
                }
            }
        }
        final List list2 = p_renderModelAmbientOcclusion_2_.getGeneralQuads();
        if (list2.size() > 0) {
            if (renderenv == null) {
                renderenv = RenderEnv.getInstance(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_);
            }
            this.renderModelAmbientOcclusionQuads(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, list2, renderenv);
            flag = true;
        }
        if (renderenv != null && Config.isBetterSnow() && !renderenv.isBreakingAnimation() && BetterSnow.shouldRender(p_renderModelAmbientOcclusion_1_, p_renderModelAmbientOcclusion_3_, p_renderModelAmbientOcclusion_4_, p_renderModelAmbientOcclusion_5_)) {
            final IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
            final IBlockState iblockstate = BetterSnow.getStateSnowLayer();
            this.renderModelAmbientOcclusion(p_renderModelAmbientOcclusion_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelAmbientOcclusion_5_, p_renderModelAmbientOcclusion_6_, true);
        }
        return flag;
    }
    
    public boolean renderModelStandard(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final Block blockIn, final BlockPos blockPosIn, final WorldRenderer worldRendererIn, final boolean checkSides) {
        return this.renderModelStandard(blockAccessIn, modelIn, blockIn, blockAccessIn.getBlockState(blockPosIn), blockPosIn, worldRendererIn, checkSides);
    }
    
    public boolean renderModelStandard(final IBlockAccess p_renderModelStandard_1_, final IBakedModel p_renderModelStandard_2_, final Block p_renderModelStandard_3_, final IBlockState p_renderModelStandard_4_, final BlockPos p_renderModelStandard_5_, final WorldRenderer p_renderModelStandard_6_, final boolean p_renderModelStandard_7_) {
        boolean flag = false;
        RenderEnv renderenv = null;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            List list = p_renderModelStandard_2_.getFaceQuads(enumfacing);
            if (!list.isEmpty()) {
                final BlockPos blockpos = p_renderModelStandard_5_.offset(enumfacing);
                if (!p_renderModelStandard_7_ || p_renderModelStandard_3_.shouldSideBeRendered(p_renderModelStandard_1_, blockpos, enumfacing)) {
                    if (renderenv == null) {
                        renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
                    }
                    if (!renderenv.isBreakingAnimation(list) && Config.isBetterGrass()) {
                        list = BetterGrass.getFaceQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, list);
                    }
                    final int i = p_renderModelStandard_3_.getMixedBrightnessForBlock(p_renderModelStandard_1_, blockpos);
                    this.renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, enumfacing, i, false, p_renderModelStandard_6_, list, renderenv);
                    flag = true;
                }
            }
        }
        final List list2 = p_renderModelStandard_2_.getGeneralQuads();
        if (list2.size() > 0) {
            if (renderenv == null) {
                renderenv = RenderEnv.getInstance(p_renderModelStandard_1_, p_renderModelStandard_4_, p_renderModelStandard_5_);
            }
            this.renderModelStandardQuads(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_5_, null, -1, true, p_renderModelStandard_6_, list2, renderenv);
            flag = true;
        }
        if (renderenv != null && Config.isBetterSnow() && !renderenv.isBreakingAnimation() && BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_) && BetterSnow.shouldRender(p_renderModelStandard_1_, p_renderModelStandard_3_, p_renderModelStandard_4_, p_renderModelStandard_5_)) {
            final IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
            final IBlockState iblockstate = BetterSnow.getStateSnowLayer();
            this.renderModelStandard(p_renderModelStandard_1_, ibakedmodel, iblockstate.getBlock(), iblockstate, p_renderModelStandard_5_, p_renderModelStandard_6_, true);
        }
        return flag;
    }
    
    private void renderModelAmbientOcclusionQuads(final IBlockAccess p_renderModelAmbientOcclusionQuads_1_, final Block p_renderModelAmbientOcclusionQuads_2_, final BlockPos p_renderModelAmbientOcclusionQuads_3_, final WorldRenderer p_renderModelAmbientOcclusionQuads_4_, final List p_renderModelAmbientOcclusionQuads_5_, final RenderEnv p_renderModelAmbientOcclusionQuads_6_) {
        final float[] afloat = p_renderModelAmbientOcclusionQuads_6_.getQuadBounds();
        final BitSet bitset = p_renderModelAmbientOcclusionQuads_6_.getBoundsFlags();
        final AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderModelAmbientOcclusionQuads_6_.getAoFace();
        final IBlockState iblockstate = p_renderModelAmbientOcclusionQuads_6_.getBlockState();
        double d0 = p_renderModelAmbientOcclusionQuads_3_.getX();
        double d2 = p_renderModelAmbientOcclusionQuads_3_.getY();
        double d3 = p_renderModelAmbientOcclusionQuads_3_.getZ();
        final Block.EnumOffsetType block$enumoffsettype = p_renderModelAmbientOcclusionQuads_2_.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            final long i = MathHelper.getPositionRandom(p_renderModelAmbientOcclusionQuads_3_);
            d0 += ((i >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            d3 += ((i >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d2 += ((i >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (final Object bakedquad0 : p_renderModelAmbientOcclusionQuads_5_) {
            BakedQuad bakedquad2 = (BakedQuad)bakedquad0;
            if (!p_renderModelAmbientOcclusionQuads_6_.isBreakingAnimation(bakedquad2)) {
                final BakedQuad bakedquad3 = bakedquad2;
                if (Config.isConnectedTextures()) {
                    bakedquad2 = ConnectedTextures.getConnectedTexture(p_renderModelAmbientOcclusionQuads_1_, iblockstate, p_renderModelAmbientOcclusionQuads_3_, bakedquad2, p_renderModelAmbientOcclusionQuads_6_);
                }
                if (bakedquad2 == bakedquad3 && Config.isNaturalTextures()) {
                    bakedquad2 = NaturalTextures.getNaturalTexture(p_renderModelAmbientOcclusionQuads_3_, bakedquad2);
                }
            }
            this.fillQuadBounds(p_renderModelAmbientOcclusionQuads_2_, bakedquad2.getVertexData(), bakedquad2.getFace(), afloat, bitset);
            blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_3_, bakedquad2.getFace(), afloat, bitset);
            if (p_renderModelAmbientOcclusionQuads_4_.isMultiTexture()) {
                p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad2.getVertexDataSingle());
                p_renderModelAmbientOcclusionQuads_4_.putSprite(bakedquad2.getSprite());
            }
            else {
                p_renderModelAmbientOcclusionQuads_4_.addVertexData(bakedquad2.getVertexData());
            }
            p_renderModelAmbientOcclusionQuads_4_.putBrightness4(blockmodelrenderer$ambientocclusionface.vertexBrightness[0], blockmodelrenderer$ambientocclusionface.vertexBrightness[1], blockmodelrenderer$ambientocclusionface.vertexBrightness[2], blockmodelrenderer$ambientocclusionface.vertexBrightness[3]);
            final int k = CustomColors.getColorMultiplier(bakedquad2, p_renderModelAmbientOcclusionQuads_2_, p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, p_renderModelAmbientOcclusionQuads_6_);
            if (!bakedquad2.hasTintIndex() && k == -1) {
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
            }
            else {
                int j;
                if (k != -1) {
                    j = k;
                }
                else {
                    j = p_renderModelAmbientOcclusionQuads_2_.colorMultiplier(p_renderModelAmbientOcclusionQuads_1_, p_renderModelAmbientOcclusionQuads_3_, bakedquad2.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    j = TextureUtil.anaglyphColor(j);
                }
                final float f = (j >> 16 & 0xFF) / 255.0f;
                final float f2 = (j >> 8 & 0xFF) / 255.0f;
                final float f3 = (j & 0xFF) / 255.0f;
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f3, 4);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f3, 3);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f3, 2);
                p_renderModelAmbientOcclusionQuads_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f3, 1);
            }
            p_renderModelAmbientOcclusionQuads_4_.putPosition(d0, d2, d3);
        }
    }
    
    private void fillQuadBounds(final Block blockIn, final int[] vertexData, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
        float f = 32.0f;
        float f2 = 32.0f;
        float f3 = 32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        float f6 = -32.0f;
        final int i = vertexData.length / 4;
        for (int j = 0; j < 4; ++j) {
            final float f7 = Float.intBitsToFloat(vertexData[j * i]);
            final float f8 = Float.intBitsToFloat(vertexData[j * i + 1]);
            final float f9 = Float.intBitsToFloat(vertexData[j * i + 2]);
            f = Math.min(f, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.min(f3, f9);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
            f6 = Math.max(f6, f9);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f4;
            quadBounds[EnumFacing.DOWN.getIndex()] = f2;
            quadBounds[EnumFacing.UP.getIndex()] = f5;
            quadBounds[EnumFacing.NORTH.getIndex()] = f3;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f6;
            quadBounds[EnumFacing.WEST.getIndex() + EnumFacing.VALUES.length] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + EnumFacing.VALUES.length] = 1.0f - f4;
            quadBounds[EnumFacing.DOWN.getIndex() + EnumFacing.VALUES.length] = 1.0f - f2;
            quadBounds[EnumFacing.UP.getIndex() + EnumFacing.VALUES.length] = 1.0f - f5;
            quadBounds[EnumFacing.NORTH.getIndex() + EnumFacing.VALUES.length] = 1.0f - f3;
            quadBounds[EnumFacing.SOUTH.getIndex() + EnumFacing.VALUES.length] = 1.0f - f6;
        }
        final float f10 = 1.0E-4f;
        final float f11 = 0.9999f;
        switch (BlockModelRenderer$1.field_178290_a[facingIn.ordinal()]) {
            case 1: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case 2: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || blockIn.isFullCube()) && f2 == f5);
                break;
            }
            case 3: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 < 1.0E-4f || blockIn.isFullCube()) && f3 == f6);
                break;
            }
            case 4: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f6 > 0.9999f || blockIn.isFullCube()) && f3 == f6);
                break;
            }
            case 5: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || blockIn.isFullCube()) && f == f4);
                break;
            }
            case 6: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || blockIn.isFullCube()) && f == f4);
                break;
            }
        }
    }
    
    private void renderModelStandardQuads(final IBlockAccess p_renderModelStandardQuads_1_, final Block p_renderModelStandardQuads_2_, final BlockPos p_renderModelStandardQuads_3_, final EnumFacing p_renderModelStandardQuads_4_, int p_renderModelStandardQuads_5_, final boolean p_renderModelStandardQuads_6_, final WorldRenderer p_renderModelStandardQuads_7_, final List p_renderModelStandardQuads_8_, final RenderEnv p_renderModelStandardQuads_9_) {
        final BitSet bitset = p_renderModelStandardQuads_9_.getBoundsFlags();
        final IBlockState iblockstate = p_renderModelStandardQuads_9_.getBlockState();
        double d0 = p_renderModelStandardQuads_3_.getX();
        double d2 = p_renderModelStandardQuads_3_.getY();
        double d3 = p_renderModelStandardQuads_3_.getZ();
        final Block.EnumOffsetType block$enumoffsettype = p_renderModelStandardQuads_2_.getOffsetType();
        if (block$enumoffsettype != Block.EnumOffsetType.NONE) {
            final int i = p_renderModelStandardQuads_3_.getX();
            final int j = p_renderModelStandardQuads_3_.getZ();
            long k = (long)(i * 3129871) ^ j * 116129781L;
            k = k * k * 42317861L + k * 11L;
            d0 += ((k >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
            d3 += ((k >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
            if (block$enumoffsettype == Block.EnumOffsetType.XYZ) {
                d2 += ((k >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
            }
        }
        for (final Object bakedquad0 : p_renderModelStandardQuads_8_) {
            BakedQuad bakedquad2 = (BakedQuad)bakedquad0;
            if (!p_renderModelStandardQuads_9_.isBreakingAnimation(bakedquad2)) {
                final BakedQuad bakedquad3 = bakedquad2;
                if (Config.isConnectedTextures()) {
                    bakedquad2 = ConnectedTextures.getConnectedTexture(p_renderModelStandardQuads_1_, iblockstate, p_renderModelStandardQuads_3_, bakedquad2, p_renderModelStandardQuads_9_);
                }
                if (bakedquad2 == bakedquad3 && Config.isNaturalTextures()) {
                    bakedquad2 = NaturalTextures.getNaturalTexture(p_renderModelStandardQuads_3_, bakedquad2);
                }
            }
            if (p_renderModelStandardQuads_6_) {
                this.fillQuadBounds(p_renderModelStandardQuads_2_, bakedquad2.getVertexData(), bakedquad2.getFace(), null, bitset);
                p_renderModelStandardQuads_5_ = (bitset.get(0) ? p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_.offset(bakedquad2.getFace())) : p_renderModelStandardQuads_2_.getMixedBrightnessForBlock(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_));
            }
            if (p_renderModelStandardQuads_7_.isMultiTexture()) {
                p_renderModelStandardQuads_7_.addVertexData(bakedquad2.getVertexDataSingle());
                p_renderModelStandardQuads_7_.putSprite(bakedquad2.getSprite());
            }
            else {
                p_renderModelStandardQuads_7_.addVertexData(bakedquad2.getVertexData());
            }
            p_renderModelStandardQuads_7_.putBrightness4(p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_, p_renderModelStandardQuads_5_);
            final int i2 = CustomColors.getColorMultiplier(bakedquad2, p_renderModelStandardQuads_2_, p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, p_renderModelStandardQuads_9_);
            if (bakedquad2.hasTintIndex() || i2 != -1) {
                int l;
                if (i2 != -1) {
                    l = i2;
                }
                else {
                    l = p_renderModelStandardQuads_2_.colorMultiplier(p_renderModelStandardQuads_1_, p_renderModelStandardQuads_3_, bakedquad2.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    l = TextureUtil.anaglyphColor(l);
                }
                final float f = (l >> 16 & 0xFF) / 255.0f;
                final float f2 = (l >> 8 & 0xFF) / 255.0f;
                final float f3 = (l & 0xFF) / 255.0f;
                p_renderModelStandardQuads_7_.putColorMultiplier(f, f2, f3, 4);
                p_renderModelStandardQuads_7_.putColorMultiplier(f, f2, f3, 3);
                p_renderModelStandardQuads_7_.putColorMultiplier(f, f2, f3, 2);
                p_renderModelStandardQuads_7_.putColorMultiplier(f, f2, f3, 1);
            }
            p_renderModelStandardQuads_7_.putPosition(d0, d2, d3);
        }
    }
    
    public void renderModelBrightnessColor(final IBakedModel bakedModel, final float p_178262_2_, final float p_178262_3_, final float p_178262_4_, final float p_178262_5_) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getFaceQuads(enumfacing));
        }
        this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, bakedModel.getGeneralQuads());
    }
    
    public void renderModelBrightness(final IBakedModel p_178266_1_, final IBlockState p_178266_2_, final float p_178266_3_, final boolean p_178266_4_) {
        final Block block = p_178266_2_.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor(i);
        }
        final float f = (i >> 16 & 0xFF) / 255.0f;
        final float f2 = (i >> 8 & 0xFF) / 255.0f;
        final float f3 = (i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0f);
        }
        this.renderModelBrightnessColor(p_178266_1_, p_178266_3_, f, f2, f3);
    }
    
    private void renderModelBrightnessColorQuads(final float p_178264_1_, final float p_178264_2_, final float p_178264_3_, final float p_178264_4_, final List p_178264_5_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        for (final Object bakedquad0 : p_178264_5_) {
            final BakedQuad bakedquad2 = (BakedQuad)bakedquad0;
            worldrenderer.begin(7, DefaultVertexFormats.ITEM);
            worldrenderer.addVertexData(bakedquad2.getVertexData());
            if (bakedquad2.hasTintIndex()) {
                worldrenderer.putColorRGB_F4(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
            }
            else {
                worldrenderer.putColorRGB_F4(p_178264_1_, p_178264_1_, p_178264_1_);
            }
            final Vec3i vec3i = bakedquad2.getFace().getDirectionVec();
            worldrenderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }
    
    public static float fixAoLightValue(final float p_fixAoLightValue_0_) {
        return (p_fixAoLightValue_0_ == 0.2f) ? BlockModelRenderer.aoLightValueOpaque : p_fixAoLightValue_0_;
    }
    
    public enum EnumNeighborInfo
    {
        DOWN("DOWN", 0, "DOWN", 0, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5f, true, new Orientation[] { Orientation.FLIP_WEST, Orientation.SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_WEST, Orientation.NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_EAST, Orientation.NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_EAST, Orientation.SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.SOUTH }), 
        UP("UP", 1, "UP", 1, new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0f, true, new Orientation[] { Orientation.EAST, Orientation.SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.SOUTH }, new Orientation[] { Orientation.EAST, Orientation.NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.NORTH }, new Orientation[] { Orientation.WEST, Orientation.NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.NORTH }, new Orientation[] { Orientation.WEST, Orientation.SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.SOUTH }), 
        NORTH("NORTH", 2, "NORTH", 2, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST }), 
        SOUTH("SOUTH", 3, "SOUTH", 3, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST }), 
        WEST("WEST", 4, "WEST", 4, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH }, new Orientation[] { Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH }), 
        EAST("EAST", 5, "EAST", 5, new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH });
        
        protected final EnumFacing[] field_178276_g;
        protected final float field_178288_h;
        protected final boolean field_178289_i;
        protected final Orientation[] field_178286_j;
        protected final Orientation[] field_178287_k;
        protected final Orientation[] field_178284_l;
        protected final Orientation[] field_178285_m;
        private static final EnumNeighborInfo[] field_178282_n;
        private static final EnumNeighborInfo[] $VALUES;
        private static final String __OBFID = "CL_00002516";
        
        static {
            field_178282_n = new EnumNeighborInfo[6];
            $VALUES = new EnumNeighborInfo[] { EnumNeighborInfo.DOWN, EnumNeighborInfo.UP, EnumNeighborInfo.NORTH, EnumNeighborInfo.SOUTH, EnumNeighborInfo.WEST, EnumNeighborInfo.EAST };
            EnumNeighborInfo.field_178282_n[EnumFacing.DOWN.getIndex()] = EnumNeighborInfo.DOWN;
            EnumNeighborInfo.field_178282_n[EnumFacing.UP.getIndex()] = EnumNeighborInfo.UP;
            EnumNeighborInfo.field_178282_n[EnumFacing.NORTH.getIndex()] = EnumNeighborInfo.NORTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.SOUTH.getIndex()] = EnumNeighborInfo.SOUTH;
            EnumNeighborInfo.field_178282_n[EnumFacing.WEST.getIndex()] = EnumNeighborInfo.WEST;
            EnumNeighborInfo.field_178282_n[EnumFacing.EAST.getIndex()] = EnumNeighborInfo.EAST;
        }
        
        private EnumNeighborInfo(final String name, final int ordinal, final String p_i6_3_, final int p_i6_4_, final EnumFacing[] p_i6_5_, final float p_i6_6_, final boolean p_i6_7_, final Orientation[] p_i6_8_, final Orientation[] p_i6_9_, final Orientation[] p_i6_10_, final Orientation[] p_i6_11_) {
            this.field_178276_g = p_i6_5_;
            this.field_178288_h = p_i6_6_;
            this.field_178289_i = p_i6_7_;
            this.field_178286_j = p_i6_8_;
            this.field_178287_k = p_i6_9_;
            this.field_178284_l = p_i6_10_;
            this.field_178285_m = p_i6_11_;
        }
        
        public static EnumNeighborInfo getNeighbourInfo(final EnumFacing p_178273_0_) {
            return EnumNeighborInfo.field_178282_n[p_178273_0_.getIndex()];
        }
    }
    
    public enum Orientation
    {
        DOWN("DOWN", 0, "DOWN", 0, EnumFacing.DOWN, false), 
        UP("UP", 1, "UP", 1, EnumFacing.UP, false), 
        NORTH("NORTH", 2, "NORTH", 2, EnumFacing.NORTH, false), 
        SOUTH("SOUTH", 3, "SOUTH", 3, EnumFacing.SOUTH, false), 
        WEST("WEST", 4, "WEST", 4, EnumFacing.WEST, false), 
        EAST("EAST", 5, "EAST", 5, EnumFacing.EAST, false), 
        FLIP_DOWN("FLIP_DOWN", 6, "FLIP_DOWN", 6, EnumFacing.DOWN, true), 
        FLIP_UP("FLIP_UP", 7, "FLIP_UP", 7, EnumFacing.UP, true), 
        FLIP_NORTH("FLIP_NORTH", 8, "FLIP_NORTH", 8, EnumFacing.NORTH, true), 
        FLIP_SOUTH("FLIP_SOUTH", 9, "FLIP_SOUTH", 9, EnumFacing.SOUTH, true), 
        FLIP_WEST("FLIP_WEST", 10, "FLIP_WEST", 10, EnumFacing.WEST, true), 
        FLIP_EAST("FLIP_EAST", 11, "FLIP_EAST", 11, EnumFacing.EAST, true);
        
        protected final int field_178229_m;
        private static final Orientation[] $VALUES;
        private static final String __OBFID = "CL_00002513";
        
        static {
            $VALUES = new Orientation[] { Orientation.DOWN, Orientation.UP, Orientation.NORTH, Orientation.SOUTH, Orientation.WEST, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_EAST };
        }
        
        private Orientation(final String name, final int ordinal, final String p_i8_3_, final int p_i8_4_, final EnumFacing p_i8_5_, final boolean p_i8_6_) {
            this.field_178229_m = p_i8_5_.getIndex() + (p_i8_6_ ? EnumFacing.values().length : 0);
        }
    }
    
    enum VertexTranslations
    {
        DOWN("DOWN", 0, "DOWN", 0, 0, 1, 2, 3), 
        UP("UP", 1, "UP", 1, 2, 3, 0, 1), 
        NORTH("NORTH", 2, "NORTH", 2, 3, 0, 1, 2), 
        SOUTH("SOUTH", 3, "SOUTH", 3, 0, 1, 2, 3), 
        WEST("WEST", 4, "WEST", 4, 3, 0, 1, 2), 
        EAST("EAST", 5, "EAST", 5, 1, 2, 3, 0);
        
        private final int field_178191_g;
        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final VertexTranslations[] field_178199_k;
        private static final VertexTranslations[] $VALUES;
        private static final String __OBFID = "CL_00002514";
        
        static {
            field_178199_k = new VertexTranslations[6];
            $VALUES = new VertexTranslations[] { VertexTranslations.DOWN, VertexTranslations.UP, VertexTranslations.NORTH, VertexTranslations.SOUTH, VertexTranslations.WEST, VertexTranslations.EAST };
            VertexTranslations.field_178199_k[EnumFacing.DOWN.getIndex()] = VertexTranslations.DOWN;
            VertexTranslations.field_178199_k[EnumFacing.UP.getIndex()] = VertexTranslations.UP;
            VertexTranslations.field_178199_k[EnumFacing.NORTH.getIndex()] = VertexTranslations.NORTH;
            VertexTranslations.field_178199_k[EnumFacing.SOUTH.getIndex()] = VertexTranslations.SOUTH;
            VertexTranslations.field_178199_k[EnumFacing.WEST.getIndex()] = VertexTranslations.WEST;
            VertexTranslations.field_178199_k[EnumFacing.EAST.getIndex()] = VertexTranslations.EAST;
        }
        
        private VertexTranslations(final String name, final int ordinal, final String p_i7_3_, final int p_i7_4_, final int p_i7_5_, final int p_i7_6_, final int p_i7_7_, final int p_i7_8_) {
            this.field_178191_g = p_i7_5_;
            this.field_178200_h = p_i7_6_;
            this.field_178201_i = p_i7_7_;
            this.field_178198_j = p_i7_8_;
        }
        
        public static VertexTranslations getVertexTranslations(final EnumFacing p_178184_0_) {
            return VertexTranslations.field_178199_k[p_178184_0_.getIndex()];
        }
    }
    
    static final class BlockModelRenderer$1
    {
        static final int[] field_178290_a;
        private static final String __OBFID = "CL_00002517";
        
        static {
            field_178290_a = new int[EnumFacing.values().length];
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                BlockModelRenderer$1.field_178290_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
    
    public static class AmbientOcclusionFace
    {
        private final float[] vertexColorMultiplier;
        private final int[] vertexBrightness;
        private static final String __OBFID = "CL_00002515";
        
        public AmbientOcclusionFace(final BlockModelRenderer p_i46235_1_) {
            this.vertexColorMultiplier = new float[4];
            this.vertexBrightness = new int[4];
        }
        
        public AmbientOcclusionFace() {
            this.vertexColorMultiplier = new float[4];
            this.vertexBrightness = new int[4];
        }
        
        public void updateVertexBrightness(final IBlockAccess blockAccessIn, final Block blockIn, final BlockPos blockPosIn, final EnumFacing facingIn, final float[] quadBounds, final BitSet boundsFlags) {
            final BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
            final EnumNeighborInfo blockmodelrenderer$enumneighborinfo = EnumNeighborInfo.getNeighbourInfo(facingIn);
            final BlockPos blockpos2 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[0]);
            final BlockPos blockpos3 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[1]);
            final BlockPos blockpos4 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
            final BlockPos blockpos5 = blockpos.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
            final int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
            final int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
            final int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
            final int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
            final float f = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue());
            final float f2 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue());
            final float f3 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue());
            final float f4 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue());
            final boolean flag = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag2 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag3 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
            final boolean flag4 = blockAccessIn.getBlockState(blockpos5.offset(facingIn)).getBlock().isTranslucent();
            float f5;
            int i2;
            if (!flag3 && !flag) {
                f5 = f;
                i2 = i;
            }
            else {
                final BlockPos blockpos6 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f5 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue());
                i2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }
            float f6;
            int j2;
            if (!flag4 && !flag) {
                f6 = f;
                j2 = i;
            }
            else {
                final BlockPos blockpos7 = blockpos2.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f6 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos7).getBlock().getAmbientOcclusionLightValue());
                j2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos7);
            }
            float f7;
            int k2;
            if (!flag3 && !flag2) {
                f7 = f2;
                k2 = j;
            }
            else {
                final BlockPos blockpos8 = blockpos3.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[2]);
                f7 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos8).getBlock().getAmbientOcclusionLightValue());
                k2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos8);
            }
            float f8;
            int l2;
            if (!flag4 && !flag2) {
                f8 = f2;
                l2 = j;
            }
            else {
                final BlockPos blockpos9 = blockpos3.offset(blockmodelrenderer$enumneighborinfo.field_178276_g[3]);
                f8 = BlockModelRenderer.fixAoLightValue(blockAccessIn.getBlockState(blockpos9).getBlock().getAmbientOcclusionLightValue());
                l2 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos9);
            }
            int i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube()) {
                i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
            }
            float f9 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
            f9 = BlockModelRenderer.fixAoLightValue(f9);
            final VertexTranslations blockmodelrenderer$vertextranslations = VertexTranslations.getVertexTranslations(facingIn);
            if (boundsFlags.get(1) && blockmodelrenderer$enumneighborinfo.field_178289_i) {
                final float f10 = (f4 + f + f6 + f9) * 0.25f;
                final float f11 = (f3 + f + f5 + f9) * 0.25f;
                final float f12 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f13 = (f4 + f2 + f8 + f9) * 0.25f;
                final float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
                final float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
                final float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
                final float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
                final float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
                final float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
                final float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
                final float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
                final float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
                final float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
                final float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
                final float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
                final float f26 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
                final float f27 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
                final float f28 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
                final float f29 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178191_g] = f10 * f14 + f11 * f15 + f12 * f16 + f13 * f17;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178200_h] = f10 * f18 + f11 * f19 + f12 * f20 + f13 * f21;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178201_i] = f10 * f22 + f11 * f23 + f12 * f24 + f13 * f25;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178198_j] = f10 * f26 + f11 * f27 + f12 * f28 + f13 * f29;
                final int j3 = this.getAoBrightness(l, i, j2, i3);
                final int k3 = this.getAoBrightness(k, i, i2, i3);
                final int l3 = this.getAoBrightness(k, j, k2, i3);
                final int i4 = this.getAoBrightness(l, j, l2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = this.getVertexBrightness(j3, k3, l3, i4, f14, f15, f16, f17);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = this.getVertexBrightness(j3, k3, l3, i4, f18, f19, f20, f21);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = this.getVertexBrightness(j3, k3, l3, i4, f22, f23, f24, f25);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = this.getVertexBrightness(j3, k3, l3, i4, f26, f27, f28, f29);
            }
            else {
                final float f30 = (f4 + f + f6 + f9) * 0.25f;
                final float f31 = (f3 + f + f5 + f9) * 0.25f;
                final float f32 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f33 = (f4 + f2 + f8 + f9) * 0.25f;
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = this.getAoBrightness(l, i, j2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = this.getAoBrightness(k, i, i2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = this.getAoBrightness(k, j, k2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = this.getAoBrightness(l, j, l2, i3);
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178191_g] = f30;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178200_h] = f31;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178201_i] = f32;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.field_178198_j] = f33;
            }
        }
        
        private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, final int p_147778_4_) {
            if (p_147778_1_ == 0) {
                p_147778_1_ = p_147778_4_;
            }
            if (p_147778_2_ == 0) {
                p_147778_2_ = p_147778_4_;
            }
            if (p_147778_3_ == 0) {
                p_147778_3_ = p_147778_4_;
            }
            return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 0xFF00FF;
        }
        
        private int getVertexBrightness(final int p_178203_1_, final int p_178203_2_, final int p_178203_3_, final int p_178203_4_, final float p_178203_5_, final float p_178203_6_, final float p_178203_7_, final float p_178203_8_) {
            final int i = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
            final int j = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
            return i << 16 | j;
        }
    }
}
