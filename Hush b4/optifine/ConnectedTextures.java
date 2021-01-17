// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Arrays;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.BlockStateBase;
import java.util.IdentityHashMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.IBlockState;
import java.util.Map;

public class ConnectedTextures
{
    private static Map[] spriteQuadMaps;
    private static ConnectedProperties[][] blockProperties;
    private static ConnectedProperties[][] tileProperties;
    private static boolean multipass;
    private static final int Y_NEG_DOWN = 0;
    private static final int Y_POS_UP = 1;
    private static final int Z_NEG_NORTH = 2;
    private static final int Z_POS_SOUTH = 3;
    private static final int X_NEG_WEST = 4;
    private static final int X_POS_EAST = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    private static final String[] propSuffixes;
    private static final int[] ctmIndexes;
    public static final IBlockState AIR_DEFAULT_STATE;
    private static TextureAtlasSprite emptySprite;
    
    static {
        ConnectedTextures.spriteQuadMaps = null;
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.multipass = false;
        propSuffixes = new String[] { "", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        ctmIndexes = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 0, 0, 0, 0, 0 };
        AIR_DEFAULT_STATE = Blocks.air.getDefaultState();
        ConnectedTextures.emptySprite = null;
    }
    
    public static synchronized BakedQuad getConnectedTexture(final IBlockAccess p_getConnectedTexture_0_, final IBlockState p_getConnectedTexture_1_, final BlockPos p_getConnectedTexture_2_, final BakedQuad p_getConnectedTexture_3_, final RenderEnv p_getConnectedTexture_4_) {
        final TextureAtlasSprite textureatlassprite = p_getConnectedTexture_3_.getSprite();
        if (textureatlassprite == null) {
            return p_getConnectedTexture_3_;
        }
        final Block block = p_getConnectedTexture_1_.getBlock();
        final EnumFacing enumfacing = p_getConnectedTexture_3_.getFace();
        if (block instanceof BlockPane && textureatlassprite.getIconName().startsWith("minecraft:blocks/glass_pane_top")) {
            final IBlockState iblockstate = p_getConnectedTexture_0_.getBlockState(p_getConnectedTexture_2_.offset(p_getConnectedTexture_3_.getFace()));
            if (iblockstate == p_getConnectedTexture_1_) {
                return getQuad(ConnectedTextures.emptySprite, block, p_getConnectedTexture_1_, p_getConnectedTexture_3_);
            }
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureMultiPass(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, enumfacing, textureatlassprite, p_getConnectedTexture_4_);
        return (textureatlassprite2 == textureatlassprite) ? p_getConnectedTexture_3_ : getQuad(textureatlassprite2, block, p_getConnectedTexture_1_, p_getConnectedTexture_3_);
    }
    
    private static BakedQuad getQuad(final TextureAtlasSprite p_getQuad_0_, final Block p_getQuad_1_, final IBlockState p_getQuad_2_, final BakedQuad p_getQuad_3_) {
        if (ConnectedTextures.spriteQuadMaps == null) {
            return p_getQuad_3_;
        }
        final int i = p_getQuad_0_.getIndexInMap();
        if (i >= 0 && i < ConnectedTextures.spriteQuadMaps.length) {
            Map map = ConnectedTextures.spriteQuadMaps[i];
            if (map == null) {
                map = new IdentityHashMap(1);
                ConnectedTextures.spriteQuadMaps[i] = map;
            }
            BakedQuad bakedquad = map.get(p_getQuad_3_);
            if (bakedquad == null) {
                bakedquad = makeSpriteQuad(p_getQuad_3_, p_getQuad_0_);
                map.put(p_getQuad_3_, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuad_3_;
    }
    
    private static BakedQuad makeSpriteQuad(final BakedQuad p_makeSpriteQuad_0_, final TextureAtlasSprite p_makeSpriteQuad_1_) {
        final int[] aint = p_makeSpriteQuad_0_.getVertexData().clone();
        final TextureAtlasSprite textureatlassprite = p_makeSpriteQuad_0_.getSprite();
        for (int i = 0; i < 4; ++i) {
            fixVertex(aint, i, textureatlassprite, p_makeSpriteQuad_1_);
        }
        final BakedQuad bakedquad = new BakedQuad(aint, p_makeSpriteQuad_0_.getTintIndex(), p_makeSpriteQuad_0_.getFace(), p_makeSpriteQuad_1_);
        return bakedquad;
    }
    
    private static void fixVertex(final int[] p_fixVertex_0_, final int p_fixVertex_1_, final TextureAtlasSprite p_fixVertex_2_, final TextureAtlasSprite p_fixVertex_3_) {
        final int i = p_fixVertex_0_.length / 4;
        final int j = i * p_fixVertex_1_;
        final float f = Float.intBitsToFloat(p_fixVertex_0_[j + 4]);
        final float f2 = Float.intBitsToFloat(p_fixVertex_0_[j + 4 + 1]);
        final double d0 = p_fixVertex_2_.getSpriteU16(f);
        final double d2 = p_fixVertex_2_.getSpriteV16(f2);
        p_fixVertex_0_[j + 4] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedU(d0));
        p_fixVertex_0_[j + 4 + 1] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedV(d2));
    }
    
    private static TextureAtlasSprite getConnectedTextureMultiPass(final IBlockAccess p_getConnectedTextureMultiPass_0_, final IBlockState p_getConnectedTextureMultiPass_1_, final BlockPos p_getConnectedTextureMultiPass_2_, final EnumFacing p_getConnectedTextureMultiPass_3_, final TextureAtlasSprite p_getConnectedTextureMultiPass_4_, final RenderEnv p_getConnectedTextureMultiPass_5_) {
        final TextureAtlasSprite textureatlassprite = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, p_getConnectedTextureMultiPass_4_, true, p_getConnectedTextureMultiPass_5_);
        if (!ConnectedTextures.multipass) {
            return textureatlassprite;
        }
        if (textureatlassprite == p_getConnectedTextureMultiPass_4_) {
            return textureatlassprite;
        }
        TextureAtlasSprite textureatlassprite2 = textureatlassprite;
        for (int i = 0; i < 3; ++i) {
            final TextureAtlasSprite textureatlassprite3 = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, textureatlassprite2, false, p_getConnectedTextureMultiPass_5_);
            if (textureatlassprite3 == textureatlassprite2) {
                break;
            }
            textureatlassprite2 = textureatlassprite3;
        }
        return textureatlassprite2;
    }
    
    public static TextureAtlasSprite getConnectedTextureSingle(final IBlockAccess p_getConnectedTextureSingle_0_, final IBlockState p_getConnectedTextureSingle_1_, final BlockPos p_getConnectedTextureSingle_2_, final EnumFacing p_getConnectedTextureSingle_3_, final TextureAtlasSprite p_getConnectedTextureSingle_4_, final boolean p_getConnectedTextureSingle_5_, final RenderEnv p_getConnectedTextureSingle_6_) {
        final Block block = p_getConnectedTextureSingle_1_.getBlock();
        if (!(p_getConnectedTextureSingle_1_ instanceof BlockStateBase)) {
            return p_getConnectedTextureSingle_4_;
        }
        final BlockStateBase blockstatebase = (BlockStateBase)p_getConnectedTextureSingle_1_;
        if (ConnectedTextures.tileProperties != null) {
            final int i = p_getConnectedTextureSingle_4_.getIndexInMap();
            if (i >= 0 && i < ConnectedTextures.tileProperties.length) {
                final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
                if (aconnectedproperties != null) {
                    final int j = getSide(p_getConnectedTextureSingle_3_);
                    for (int k = 0; k < aconnectedproperties.length; ++k) {
                        final ConnectedProperties connectedproperties = aconnectedproperties[k];
                        if (connectedproperties != null && connectedproperties.matchesBlockId(blockstatebase.getBlockId())) {
                            final TextureAtlasSprite textureatlassprite = getConnectedTexture(connectedproperties, p_getConnectedTextureSingle_0_, blockstatebase, p_getConnectedTextureSingle_2_, j, p_getConnectedTextureSingle_4_, p_getConnectedTextureSingle_6_);
                            if (textureatlassprite != null) {
                                return textureatlassprite;
                            }
                        }
                    }
                }
            }
        }
        if (ConnectedTextures.blockProperties != null && p_getConnectedTextureSingle_5_) {
            final int l = p_getConnectedTextureSingle_6_.getBlockId();
            if (l >= 0 && l < ConnectedTextures.blockProperties.length) {
                final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[l];
                if (aconnectedproperties2 != null) {
                    final int i2 = getSide(p_getConnectedTextureSingle_3_);
                    for (int j2 = 0; j2 < aconnectedproperties2.length; ++j2) {
                        final ConnectedProperties connectedproperties2 = aconnectedproperties2[j2];
                        if (connectedproperties2 != null && connectedproperties2.matchesIcon(p_getConnectedTextureSingle_4_)) {
                            final TextureAtlasSprite textureatlassprite2 = getConnectedTexture(connectedproperties2, p_getConnectedTextureSingle_0_, blockstatebase, p_getConnectedTextureSingle_2_, i2, p_getConnectedTextureSingle_4_, p_getConnectedTextureSingle_6_);
                            if (textureatlassprite2 != null) {
                                return textureatlassprite2;
                            }
                        }
                    }
                }
            }
        }
        return p_getConnectedTextureSingle_4_;
    }
    
    public static int getSide(final EnumFacing p_getSide_0_) {
        if (p_getSide_0_ == null) {
            return -1;
        }
        switch (p_getSide_0_) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 1;
            }
            case EAST: {
                return 5;
            }
            case WEST: {
                return 4;
            }
            case NORTH: {
                return 2;
            }
            case SOUTH: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
    
    private static EnumFacing getFacing(final int p_getFacing_0_) {
        switch (p_getFacing_0_) {
            case 0: {
                return EnumFacing.DOWN;
            }
            case 1: {
                return EnumFacing.UP;
            }
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.SOUTH;
            }
            case 4: {
                return EnumFacing.WEST;
            }
            case 5: {
                return EnumFacing.EAST;
            }
            default: {
                return EnumFacing.UP;
            }
        }
    }
    
    private static TextureAtlasSprite getConnectedTexture(final ConnectedProperties p_getConnectedTexture_0_, final IBlockAccess p_getConnectedTexture_1_, final BlockStateBase p_getConnectedTexture_2_, final BlockPos p_getConnectedTexture_3_, final int p_getConnectedTexture_4_, final TextureAtlasSprite p_getConnectedTexture_5_, final RenderEnv p_getConnectedTexture_6_) {
        int i = 0;
        int k;
        final int j = k = p_getConnectedTexture_2_.getMetadata();
        final Block block = p_getConnectedTexture_2_.getBlock();
        if (block instanceof BlockRotatedPillar) {
            i = getWoodAxis(p_getConnectedTexture_4_, j);
            if (p_getConnectedTexture_0_.getMetadataMax() <= 3) {
                k = (j & 0x3);
            }
        }
        if (block instanceof BlockQuartz) {
            i = getQuartzAxis(p_getConnectedTexture_4_, j);
            if (p_getConnectedTexture_0_.getMetadataMax() <= 2 && k > 2) {
                k = 2;
            }
        }
        if (!p_getConnectedTexture_0_.matchesBlock(p_getConnectedTexture_2_.getBlockId(), k)) {
            return null;
        }
        if (p_getConnectedTexture_4_ >= 0 && p_getConnectedTexture_0_.faces != 63) {
            int l = p_getConnectedTexture_4_;
            if (i != 0) {
                l = fixSideByAxis(p_getConnectedTexture_4_, i);
            }
            if ((1 << l & p_getConnectedTexture_0_.faces) == 0x0) {
                return null;
            }
        }
        final int i2 = p_getConnectedTexture_3_.getY();
        if (i2 < p_getConnectedTexture_0_.minHeight || i2 > p_getConnectedTexture_0_.maxHeight) {
            return null;
        }
        if (p_getConnectedTexture_0_.biomes != null) {
            final BiomeGenBase biomegenbase = p_getConnectedTexture_1_.getBiomeGenForCoords(p_getConnectedTexture_3_);
            if (!p_getConnectedTexture_0_.matchesBiome(biomegenbase)) {
                return null;
            }
        }
        switch (p_getConnectedTexture_0_.method) {
            case 1: {
                return getConnectedTextureCtm(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j, p_getConnectedTexture_6_);
            }
            case 2: {
                return getConnectedTextureHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j);
            }
            case 3: {
                return getConnectedTextureTop(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j);
            }
            case 4: {
                return getConnectedTextureRandom(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_);
            }
            case 5: {
                return getConnectedTextureRepeat(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_);
            }
            case 6: {
                return getConnectedTextureVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j);
            }
            case 7: {
                return getConnectedTextureFixed(p_getConnectedTexture_0_);
            }
            case 8: {
                return getConnectedTextureHorizontalVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j);
            }
            case 9: {
                return getConnectedTextureVerticalHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j);
            }
            default: {
                return null;
            }
        }
    }
    
    private static int fixSideByAxis(final int p_fixSideByAxis_0_, final int p_fixSideByAxis_1_) {
        switch (p_fixSideByAxis_1_) {
            case 0: {
                return p_fixSideByAxis_0_;
            }
            case 1: {
                switch (p_fixSideByAxis_0_) {
                    case 0: {
                        return 2;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 1;
                    }
                    case 3: {
                        return 0;
                    }
                    default: {
                        return p_fixSideByAxis_0_;
                    }
                }
                break;
            }
            case 2: {
                switch (p_fixSideByAxis_0_) {
                    case 0: {
                        return 4;
                    }
                    case 1: {
                        return 5;
                    }
                    default: {
                        return p_fixSideByAxis_0_;
                    }
                    case 4: {
                        return 1;
                    }
                    case 5: {
                        return 0;
                    }
                }
                break;
            }
            default: {
                return p_fixSideByAxis_0_;
            }
        }
    }
    
    private static int getWoodAxis(final int p_getWoodAxis_0_, final int p_getWoodAxis_1_) {
        final int i = (p_getWoodAxis_1_ & 0xC) >> 2;
        switch (i) {
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static int getQuartzAxis(final int p_getQuartzAxis_0_, final int p_getQuartzAxis_1_) {
        switch (p_getQuartzAxis_1_) {
            case 3: {
                return 2;
            }
            case 4: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static TextureAtlasSprite getConnectedTextureRandom(final ConnectedProperties p_getConnectedTextureRandom_0_, final BlockPos p_getConnectedTextureRandom_1_, final int p_getConnectedTextureRandom_2_) {
        if (p_getConnectedTextureRandom_0_.tileIcons.length == 1) {
            return p_getConnectedTextureRandom_0_.tileIcons[0];
        }
        final int i = p_getConnectedTextureRandom_2_ / p_getConnectedTextureRandom_0_.symmetry * p_getConnectedTextureRandom_0_.symmetry;
        final int j = Config.getRandom(p_getConnectedTextureRandom_1_, i) & Integer.MAX_VALUE;
        int k = 0;
        if (p_getConnectedTextureRandom_0_.weights == null) {
            k = j % p_getConnectedTextureRandom_0_.tileIcons.length;
        }
        else {
            final int l = j % p_getConnectedTextureRandom_0_.sumAllWeights;
            final int[] aint = p_getConnectedTextureRandom_0_.sumWeights;
            for (int i2 = 0; i2 < aint.length; ++i2) {
                if (l < aint[i2]) {
                    k = i2;
                    break;
                }
            }
        }
        return p_getConnectedTextureRandom_0_.tileIcons[k];
    }
    
    private static TextureAtlasSprite getConnectedTextureFixed(final ConnectedProperties p_getConnectedTextureFixed_0_) {
        return p_getConnectedTextureFixed_0_.tileIcons[0];
    }
    
    private static TextureAtlasSprite getConnectedTextureRepeat(final ConnectedProperties p_getConnectedTextureRepeat_0_, final BlockPos p_getConnectedTextureRepeat_1_, final int p_getConnectedTextureRepeat_2_) {
        if (p_getConnectedTextureRepeat_0_.tileIcons.length == 1) {
            return p_getConnectedTextureRepeat_0_.tileIcons[0];
        }
        final int i = p_getConnectedTextureRepeat_1_.getX();
        final int j = p_getConnectedTextureRepeat_1_.getY();
        final int k = p_getConnectedTextureRepeat_1_.getZ();
        int l = 0;
        int i2 = 0;
        switch (p_getConnectedTextureRepeat_2_) {
            case 0: {
                l = i;
                i2 = k;
                break;
            }
            case 1: {
                l = i;
                i2 = k;
                break;
            }
            case 2: {
                l = -i - 1;
                i2 = -j;
                break;
            }
            case 3: {
                l = i;
                i2 = -j;
                break;
            }
            case 4: {
                l = k;
                i2 = -j;
                break;
            }
            case 5: {
                l = -k - 1;
                i2 = -j;
                break;
            }
        }
        l %= p_getConnectedTextureRepeat_0_.width;
        i2 %= p_getConnectedTextureRepeat_0_.height;
        if (l < 0) {
            l += p_getConnectedTextureRepeat_0_.width;
        }
        if (i2 < 0) {
            i2 += p_getConnectedTextureRepeat_0_.height;
        }
        final int j2 = i2 * p_getConnectedTextureRepeat_0_.width + l;
        return p_getConnectedTextureRepeat_0_.tileIcons[j2];
    }
    
    private static TextureAtlasSprite getConnectedTextureCtm(final ConnectedProperties p_getConnectedTextureCtm_0_, final IBlockAccess p_getConnectedTextureCtm_1_, final IBlockState p_getConnectedTextureCtm_2_, final BlockPos p_getConnectedTextureCtm_3_, final int p_getConnectedTextureCtm_4_, final int p_getConnectedTextureCtm_5_, final TextureAtlasSprite p_getConnectedTextureCtm_6_, final int p_getConnectedTextureCtm_7_, final RenderEnv p_getConnectedTextureCtm_8_) {
        final boolean[] aboolean = p_getConnectedTextureCtm_8_.getBorderFlags();
        switch (p_getConnectedTextureCtm_5_) {
            case 0: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 1: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 2: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 3: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 4: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 5: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
        }
        int i = 0;
        if (aboolean[0] & !aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 3;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 1;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 12;
        }
        else if (!aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 36;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 2;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 24;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 15;
        }
        else if (aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 39;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 13;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 37;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 25;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 27;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 38;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 14;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 26;
        }
        if (i == 0) {
            return p_getConnectedTextureCtm_0_.tileIcons[i];
        }
        if (!Config.isConnectedTexturesFancy()) {
            return p_getConnectedTextureCtm_0_.tileIcons[i];
        }
        switch (p_getConnectedTextureCtm_5_) {
            case 0: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 1: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 2: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 3: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().down(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.east().up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.west().up(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 4: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
            case 5: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.down().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().north(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_.up().south(), p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_);
                break;
            }
        }
        if (i == 13 && aboolean[0]) {
            i = 4;
        }
        else if (i == 15 && aboolean[1]) {
            i = 5;
        }
        else if (i == 37 && aboolean[2]) {
            i = 16;
        }
        else if (i == 39 && aboolean[3]) {
            i = 17;
        }
        else if (i == 14 && aboolean[0] && aboolean[1]) {
            i = 7;
        }
        else if (i == 25 && aboolean[0] && aboolean[2]) {
            i = 6;
        }
        else if (i == 27 && aboolean[3] && aboolean[1]) {
            i = 19;
        }
        else if (i == 38 && aboolean[3] && aboolean[2]) {
            i = 18;
        }
        else if (i == 14 && !aboolean[0] && aboolean[1]) {
            i = 31;
        }
        else if (i == 25 && aboolean[0] && !aboolean[2]) {
            i = 30;
        }
        else if (i == 27 && !aboolean[3] && aboolean[1]) {
            i = 41;
        }
        else if (i == 38 && aboolean[3] && !aboolean[2]) {
            i = 40;
        }
        else if (i == 14 && aboolean[0] && !aboolean[1]) {
            i = 29;
        }
        else if (i == 25 && !aboolean[0] && aboolean[2]) {
            i = 28;
        }
        else if (i == 27 && aboolean[3] && !aboolean[1]) {
            i = 43;
        }
        else if (i == 38 && !aboolean[3] && aboolean[2]) {
            i = 42;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 46;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 9;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 21;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 8;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 20;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 11;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 22;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 23;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 10;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 34;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 35;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 32;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 33;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 44;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 45;
        }
        return p_getConnectedTextureCtm_0_.tileIcons[i];
    }
    
    private static boolean isNeighbour(final ConnectedProperties p_isNeighbour_0_, final IBlockAccess p_isNeighbour_1_, final IBlockState p_isNeighbour_2_, final BlockPos p_isNeighbour_3_, final int p_isNeighbour_4_, final TextureAtlasSprite p_isNeighbour_5_, final int p_isNeighbour_6_) {
        final IBlockState iblockstate = p_isNeighbour_1_.getBlockState(p_isNeighbour_3_);
        if (p_isNeighbour_2_ == iblockstate) {
            return true;
        }
        if (p_isNeighbour_0_.connect != 2) {
            return p_isNeighbour_0_.connect == 3 && iblockstate != null && iblockstate != ConnectedTextures.AIR_DEFAULT_STATE && iblockstate.getBlock().getMaterial() == p_isNeighbour_2_.getBlock().getMaterial();
        }
        if (iblockstate == null) {
            return false;
        }
        if (iblockstate == ConnectedTextures.AIR_DEFAULT_STATE) {
            return false;
        }
        final TextureAtlasSprite textureatlassprite = getNeighbourIcon(p_isNeighbour_1_, p_isNeighbour_3_, iblockstate, p_isNeighbour_4_);
        return textureatlassprite == p_isNeighbour_5_;
    }
    
    private static TextureAtlasSprite getNeighbourIcon(final IBlockAccess p_getNeighbourIcon_0_, final BlockPos p_getNeighbourIcon_1_, IBlockState p_getNeighbourIcon_2_, final int p_getNeighbourIcon_3_) {
        p_getNeighbourIcon_2_ = p_getNeighbourIcon_2_.getBlock().getActualState(p_getNeighbourIcon_2_, p_getNeighbourIcon_0_, p_getNeighbourIcon_1_);
        final IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(p_getNeighbourIcon_2_);
        if (ibakedmodel == null) {
            return null;
        }
        final EnumFacing enumfacing = getFacing(p_getNeighbourIcon_3_);
        final List list = ibakedmodel.getFaceQuads(enumfacing);
        if (list.size() > 0) {
            final BakedQuad bakedquad1 = list.get(0);
            return bakedquad1.getSprite();
        }
        final List list2 = ibakedmodel.getGeneralQuads();
        for (int i = 0; i < list2.size(); ++i) {
            final BakedQuad bakedquad2 = list2.get(i);
            if (bakedquad2.getFace() == enumfacing) {
                return bakedquad2.getSprite();
            }
        }
        return null;
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontal(final ConnectedProperties p_getConnectedTextureHorizontal_0_, final IBlockAccess p_getConnectedTextureHorizontal_1_, final IBlockState p_getConnectedTextureHorizontal_2_, final BlockPos p_getConnectedTextureHorizontal_3_, final int p_getConnectedTextureHorizontal_4_, final int p_getConnectedTextureHorizontal_5_, final TextureAtlasSprite p_getConnectedTextureHorizontal_6_, final int p_getConnectedTextureHorizontal_7_) {
        boolean flag = false;
        boolean flag2 = false;
        Label_0634: {
            switch (p_getConnectedTextureHorizontal_4_) {
                case 0: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0:
                        case 1: {
                            return null;
                        }
                        case 2: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 3: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 4: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 1: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 2:
                        case 3: {
                            return null;
                        }
                        case 4: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0634;
                        }
                        case 1: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0634;
                        }
                        case 2: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0634;
                        }
                        case 3: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0634;
                        }
                        case 4:
                        case 5: {
                            return null;
                        }
                    }
                    break;
                }
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return p_getConnectedTextureHorizontal_0_.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureVertical(final ConnectedProperties p_getConnectedTextureVertical_0_, final IBlockAccess p_getConnectedTextureVertical_1_, final IBlockState p_getConnectedTextureVertical_2_, final BlockPos p_getConnectedTextureVertical_3_, final int p_getConnectedTextureVertical_4_, final int p_getConnectedTextureVertical_5_, final TextureAtlasSprite p_getConnectedTextureVertical_6_, final int p_getConnectedTextureVertical_7_) {
        boolean flag = false;
        boolean flag2 = false;
        switch (p_getConnectedTextureVertical_4_) {
            case 0: {
                if (p_getConnectedTextureVertical_5_ == 1 || p_getConnectedTextureVertical_5_ == 0) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
            case 1: {
                if (p_getConnectedTextureVertical_5_ == 3 || p_getConnectedTextureVertical_5_ == 2) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.south(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.north(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
            case 2: {
                if (p_getConnectedTextureVertical_5_ == 5 || p_getConnectedTextureVertical_5_ == 4) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.west(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.east(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return p_getConnectedTextureVertical_0_.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontalVertical(final ConnectedProperties p_getConnectedTextureHorizontalVertical_0_, final IBlockAccess p_getConnectedTextureHorizontalVertical_1_, final IBlockState p_getConnectedTextureHorizontalVertical_2_, final BlockPos p_getConnectedTextureHorizontalVertical_3_, final int p_getConnectedTextureHorizontalVertical_4_, final int p_getConnectedTextureHorizontalVertical_5_, final TextureAtlasSprite p_getConnectedTextureHorizontalVertical_6_, final int p_getConnectedTextureHorizontalVertical_7_) {
        final TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureHorizontalVertical_0_.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureHorizontal(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
        if (textureatlassprite != null && textureatlassprite != p_getConnectedTextureHorizontalVertical_6_ && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureVertical(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
        return (textureatlassprite2 == atextureatlassprite[0]) ? atextureatlassprite[4] : ((textureatlassprite2 == atextureatlassprite[1]) ? atextureatlassprite[5] : ((textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2));
    }
    
    private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(final ConnectedProperties p_getConnectedTextureVerticalHorizontal_0_, final IBlockAccess p_getConnectedTextureVerticalHorizontal_1_, final IBlockState p_getConnectedTextureVerticalHorizontal_2_, final BlockPos p_getConnectedTextureVerticalHorizontal_3_, final int p_getConnectedTextureVerticalHorizontal_4_, final int p_getConnectedTextureVerticalHorizontal_5_, final TextureAtlasSprite p_getConnectedTextureVerticalHorizontal_6_, final int p_getConnectedTextureVerticalHorizontal_7_) {
        final TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureVerticalHorizontal_0_.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureVertical(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
        if (textureatlassprite != null && textureatlassprite != p_getConnectedTextureVerticalHorizontal_6_ && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureHorizontal(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
        return (textureatlassprite2 == atextureatlassprite[0]) ? atextureatlassprite[4] : ((textureatlassprite2 == atextureatlassprite[1]) ? atextureatlassprite[5] : ((textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2));
    }
    
    private static TextureAtlasSprite getConnectedTextureTop(final ConnectedProperties p_getConnectedTextureTop_0_, final IBlockAccess p_getConnectedTextureTop_1_, final IBlockState p_getConnectedTextureTop_2_, final BlockPos p_getConnectedTextureTop_3_, final int p_getConnectedTextureTop_4_, final int p_getConnectedTextureTop_5_, final TextureAtlasSprite p_getConnectedTextureTop_6_, final int p_getConnectedTextureTop_7_) {
        boolean flag = false;
        switch (p_getConnectedTextureTop_4_) {
            case 0: {
                if (p_getConnectedTextureTop_5_ == 1 || p_getConnectedTextureTop_5_ == 0) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.up(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
            case 1: {
                if (p_getConnectedTextureTop_5_ == 3 || p_getConnectedTextureTop_5_ == 2) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.south(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
            case 2: {
                if (p_getConnectedTextureTop_5_ == 5 || p_getConnectedTextureTop_5_ == 4) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.east(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
        }
        if (flag) {
            return p_getConnectedTextureTop_0_.tileIcons[0];
        }
        return null;
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_) {
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.spriteQuadMaps = null;
        if (Config.isConnectedTextures()) {
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                updateIcons(p_updateIcons_0_, iresourcepack);
            }
            updateIcons(p_updateIcons_0_, Config.getDefaultResourcePack());
            final ResourceLocation resourcelocation = new ResourceLocation("mcpatcher/ctm/default/empty");
            ConnectedTextures.emptySprite = p_updateIcons_0_.registerSprite(resourcelocation);
            ConnectedTextures.spriteQuadMaps = new Map[p_updateIcons_0_.getCountRegisteredSprites() + 1];
            if (ConnectedTextures.blockProperties.length <= 0) {
                ConnectedTextures.blockProperties = null;
            }
            if (ConnectedTextures.tileProperties.length <= 0) {
                ConnectedTextures.tileProperties = null;
            }
        }
    }
    
    private static void updateIconEmpty(final TextureMap p_updateIconEmpty_0_) {
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_, final IResourcePack p_updateIcons_1_) {
        final String[] astring = ResUtils.collectFiles(p_updateIcons_1_, "mcpatcher/ctm/", ".properties", getDefaultCtmPaths());
        Arrays.sort(astring);
        final List list = makePropertyList(ConnectedTextures.tileProperties);
        final List list2 = makePropertyList(ConnectedTextures.blockProperties);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("ConnectedTextures: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = p_updateIcons_1_.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn("ConnectedTextures file not found: " + s);
                }
                else {
                    final Properties properties = new Properties();
                    properties.load(inputstream);
                    final ConnectedProperties connectedproperties = new ConnectedProperties(properties, s);
                    if (connectedproperties.isValid(s)) {
                        connectedproperties.updateIcons(p_updateIcons_0_);
                        addToTileList(connectedproperties, list);
                        addToBlockList(connectedproperties, list2);
                    }
                }
            }
            catch (FileNotFoundException var11) {
                Config.warn("ConnectedTextures file not found: " + s);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        ConnectedTextures.blockProperties = propertyListToArray(list2);
        ConnectedTextures.tileProperties = propertyListToArray(list);
        ConnectedTextures.multipass = detectMultipass();
        Config.dbg("Multipass connected textures: " + ConnectedTextures.multipass);
    }
    
    private static List makePropertyList(final ConnectedProperties[][] p_makePropertyList_0_) {
        final List list = new ArrayList();
        if (p_makePropertyList_0_ != null) {
            for (int i = 0; i < p_makePropertyList_0_.length; ++i) {
                final ConnectedProperties[] aconnectedproperties = p_makePropertyList_0_[i];
                List list2 = null;
                if (aconnectedproperties != null) {
                    list2 = new ArrayList(Arrays.asList(aconnectedproperties));
                }
                list.add(list2);
            }
        }
        return list;
    }
    
    private static boolean detectMultipass() {
        final List list = new ArrayList();
        for (int i = 0; i < ConnectedTextures.tileProperties.length; ++i) {
            final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
            if (aconnectedproperties != null) {
                list.addAll(Arrays.asList(aconnectedproperties));
            }
        }
        for (int k = 0; k < ConnectedTextures.blockProperties.length; ++k) {
            final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[k];
            if (aconnectedproperties2 != null) {
                list.addAll(Arrays.asList(aconnectedproperties2));
            }
        }
        final ConnectedProperties[] aconnectedproperties3 = list.toArray(new ConnectedProperties[list.size()]);
        final Set set1 = new HashSet();
        final Set set2 = new HashSet();
        for (int j = 0; j < aconnectedproperties3.length; ++j) {
            final ConnectedProperties connectedproperties = aconnectedproperties3[j];
            if (connectedproperties.matchTileIcons != null) {
                set1.addAll(Arrays.asList(connectedproperties.matchTileIcons));
            }
            if (connectedproperties.tileIcons != null) {
                set2.addAll(Arrays.asList(connectedproperties.tileIcons));
            }
        }
        set1.retainAll(set2);
        return !set1.isEmpty();
    }
    
    private static ConnectedProperties[][] propertyListToArray(final List p_propertyListToArray_0_) {
        final ConnectedProperties[][] aconnectedproperties = new ConnectedProperties[p_propertyListToArray_0_.size()][];
        for (int i = 0; i < p_propertyListToArray_0_.size(); ++i) {
            final List list = p_propertyListToArray_0_.get(i);
            if (list != null) {
                final ConnectedProperties[] aconnectedproperties2 = list.toArray(new ConnectedProperties[list.size()]);
                aconnectedproperties[i] = aconnectedproperties2;
            }
        }
        return aconnectedproperties;
    }
    
    private static void addToTileList(final ConnectedProperties p_addToTileList_0_, final List p_addToTileList_1_) {
        if (p_addToTileList_0_.matchTileIcons != null) {
            for (int i = 0; i < p_addToTileList_0_.matchTileIcons.length; ++i) {
                final TextureAtlasSprite textureatlassprite = p_addToTileList_0_.matchTileIcons[i];
                if (!(textureatlassprite instanceof TextureAtlasSprite)) {
                    Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + textureatlassprite + ", name: " + textureatlassprite.getIconName());
                }
                else {
                    final int j = textureatlassprite.getIndexInMap();
                    if (j < 0) {
                        Config.warn("Invalid tile ID: " + j + ", icon: " + textureatlassprite.getIconName());
                    }
                    else {
                        addToList(p_addToTileList_0_, p_addToTileList_1_, j);
                    }
                }
            }
        }
    }
    
    private static void addToBlockList(final ConnectedProperties p_addToBlockList_0_, final List p_addToBlockList_1_) {
        if (p_addToBlockList_0_.matchBlocks != null) {
            for (int i = 0; i < p_addToBlockList_0_.matchBlocks.length; ++i) {
                final int j = p_addToBlockList_0_.matchBlocks[i].getBlockId();
                if (j < 0) {
                    Config.warn("Invalid block ID: " + j);
                }
                else {
                    addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
                }
            }
        }
    }
    
    private static void addToList(final ConnectedProperties p_addToList_0_, final List p_addToList_1_, final int p_addToList_2_) {
        while (p_addToList_2_ >= p_addToList_1_.size()) {
            p_addToList_1_.add(null);
        }
        List list = p_addToList_1_.get(p_addToList_2_);
        if (list == null) {
            list = new ArrayList();
            p_addToList_1_.set(p_addToList_2_, list);
        }
        list.add(p_addToList_0_);
    }
    
    private static String[] getDefaultCtmPaths() {
        final List list = new ArrayList();
        final String s = "mcpatcher/ctm/default/";
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
            list.add(String.valueOf(s) + "glass.properties");
            list.add(String.valueOf(s) + "glasspane.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
            list.add(String.valueOf(s) + "bookshelf.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
            list.add(String.valueOf(s) + "sandstone.properties");
        }
        final String[] astring = { "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + s2 + ".png"))) {
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_" + s2 + ".properties");
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_pane_" + s2 + ".properties");
            }
        }
        final String[] astring2 = list.toArray(new String[list.size()]);
        return astring2;
    }
    
    public static int getPaneTextureIndex(final boolean p_getPaneTextureIndex_0_, final boolean p_getPaneTextureIndex_1_, final boolean p_getPaneTextureIndex_2_, final boolean p_getPaneTextureIndex_3_) {
        return (p_getPaneTextureIndex_1_ && p_getPaneTextureIndex_0_) ? (p_getPaneTextureIndex_2_ ? (p_getPaneTextureIndex_3_ ? 34 : 50) : (p_getPaneTextureIndex_3_ ? 18 : 2)) : ((p_getPaneTextureIndex_1_ && !p_getPaneTextureIndex_0_) ? (p_getPaneTextureIndex_2_ ? (p_getPaneTextureIndex_3_ ? 35 : 51) : (p_getPaneTextureIndex_3_ ? 19 : 3)) : ((!p_getPaneTextureIndex_1_ && p_getPaneTextureIndex_0_) ? (p_getPaneTextureIndex_2_ ? (p_getPaneTextureIndex_3_ ? 33 : 49) : (p_getPaneTextureIndex_3_ ? 17 : 1)) : (p_getPaneTextureIndex_2_ ? (p_getPaneTextureIndex_3_ ? 32 : 48) : (p_getPaneTextureIndex_3_ ? 16 : 0))));
    }
    
    public static int getReversePaneTextureIndex(final int p_getReversePaneTextureIndex_0_) {
        final int i = p_getReversePaneTextureIndex_0_ % 16;
        return (i == 1) ? (p_getReversePaneTextureIndex_0_ + 2) : ((i == 3) ? (p_getReversePaneTextureIndex_0_ - 2) : p_getReversePaneTextureIndex_0_);
    }
    
    public static TextureAtlasSprite getCtmTexture(final ConnectedProperties p_getCtmTexture_0_, final int p_getCtmTexture_1_, final TextureAtlasSprite p_getCtmTexture_2_) {
        if (p_getCtmTexture_0_.method != 1) {
            return p_getCtmTexture_2_;
        }
        if (p_getCtmTexture_1_ >= 0 && p_getCtmTexture_1_ < ConnectedTextures.ctmIndexes.length) {
            final int i = ConnectedTextures.ctmIndexes[p_getCtmTexture_1_];
            final TextureAtlasSprite[] atextureatlassprite = p_getCtmTexture_0_.tileIcons;
            return (i >= 0 && i < atextureatlassprite.length) ? atextureatlassprite[i] : p_getCtmTexture_2_;
        }
        return p_getCtmTexture_2_;
    }
}
