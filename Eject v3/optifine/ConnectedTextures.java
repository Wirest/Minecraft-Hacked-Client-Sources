package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ConnectedTextures {
    public static final IBlockState AIR_DEFAULT_STATE = Blocks.air.getDefaultState();
    private static final int Y_NEG_DOWN = 0;
    private static final int Y_POS_UP = 1;
    private static final int Z_NEG_NORTH = 2;
    private static final int Z_POS_SOUTH = 3;
    private static final int X_NEG_WEST = 4;
    private static final int X_POS_EAST = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    private static final String[] propSuffixes = {"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final int[] ctmIndexes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 0, 0, 0, 0, 0};
    private static Map[] spriteQuadMaps = null;
    private static ConnectedProperties[][] blockProperties = (ConnectedProperties[][]) null;
    private static ConnectedProperties[][] tileProperties = (ConnectedProperties[][]) null;
    private static boolean multipass = false;
    private static TextureAtlasSprite emptySprite = null;

    public static synchronized BakedQuad getConnectedTexture(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, BakedQuad paramBakedQuad, RenderEnv paramRenderEnv) {
        TextureAtlasSprite localTextureAtlasSprite = paramBakedQuad.getSprite();
        if (localTextureAtlasSprite == null) {
            return paramBakedQuad;
        }
        Block localBlock = paramIBlockState.getBlock();
        EnumFacing localEnumFacing = paramBakedQuad.getFace();
        if (((localBlock instanceof BlockPane)) && (localTextureAtlasSprite.getIconName().startsWith("minecraft:blocks/glass_pane_top"))) {
            localObject = paramIBlockAccess.getBlockState(paramBlockPos.offset(paramBakedQuad.getFace()));
            if (localObject == paramIBlockState) {
                return getQuad(emptySprite, localBlock, paramIBlockState, paramBakedQuad);
            }
        }
        Object localObject = getConnectedTextureMultiPass(paramIBlockAccess, paramIBlockState, paramBlockPos, localEnumFacing, localTextureAtlasSprite, paramRenderEnv);
        return localObject == localTextureAtlasSprite ? paramBakedQuad : getQuad((TextureAtlasSprite) localObject, localBlock, paramIBlockState, paramBakedQuad);
    }

    private static BakedQuad getQuad(TextureAtlasSprite paramTextureAtlasSprite, Block paramBlock, IBlockState paramIBlockState, BakedQuad paramBakedQuad) {
        if (spriteQuadMaps == null) {
            return paramBakedQuad;
        }
        int i = paramTextureAtlasSprite.getIndexInMap();
        if ((i >= 0) && (i < spriteQuadMaps.length)) {
            Object localObject = spriteQuadMaps[i];
            if (localObject == null) {
                localObject = new IdentityHashMap(1);
                spriteQuadMaps[i] = localObject;
            }
            BakedQuad localBakedQuad = (BakedQuad) ((Map) localObject).get(paramBakedQuad);
            if (localBakedQuad == null) {
                localBakedQuad = makeSpriteQuad(paramBakedQuad, paramTextureAtlasSprite);
                ((Map) localObject).put(paramBakedQuad, localBakedQuad);
            }
            return localBakedQuad;
        }
        return paramBakedQuad;
    }

    private static BakedQuad makeSpriteQuad(BakedQuad paramBakedQuad, TextureAtlasSprite paramTextureAtlasSprite) {
        int[] arrayOfInt = (int[]) paramBakedQuad.getVertexData().clone();
        TextureAtlasSprite localTextureAtlasSprite = paramBakedQuad.getSprite();
        for (int i = 0; i < 4; i++) {
            fixVertex(arrayOfInt, i, localTextureAtlasSprite, paramTextureAtlasSprite);
        }
        BakedQuad localBakedQuad = new BakedQuad(arrayOfInt, paramBakedQuad.getTintIndex(), paramBakedQuad.getFace(), paramTextureAtlasSprite);
        return localBakedQuad;
    }

    private static void fixVertex(int[] paramArrayOfInt, int paramInt, TextureAtlasSprite paramTextureAtlasSprite1, TextureAtlasSprite paramTextureAtlasSprite2) {
        int i = -4;
        int j = i * paramInt;
        float f1 = Float.intBitsToFloat(paramArrayOfInt[(j | 0x4)]);
        float f2 = Float.intBitsToFloat(paramArrayOfInt[(j | 0x4 | 0x1)]);
        double d1 = paramTextureAtlasSprite1.getSpriteU16(f1);
        double d2 = paramTextureAtlasSprite1.getSpriteV16(f2);
        paramArrayOfInt[(j | 0x4)] = Float.floatToRawIntBits(paramTextureAtlasSprite2.getInterpolatedU(d1));
        paramArrayOfInt[(j | 0x4 | 0x1)] = Float.floatToRawIntBits(paramTextureAtlasSprite2.getInterpolatedV(d2));
    }

    private static TextureAtlasSprite getConnectedTextureMultiPass(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, EnumFacing paramEnumFacing, TextureAtlasSprite paramTextureAtlasSprite, RenderEnv paramRenderEnv) {
        TextureAtlasSprite localTextureAtlasSprite1 = getConnectedTextureSingle(paramIBlockAccess, paramIBlockState, paramBlockPos, paramEnumFacing, paramTextureAtlasSprite, true, paramRenderEnv);
        if (!multipass) {
            return localTextureAtlasSprite1;
        }
        if (localTextureAtlasSprite1 == paramTextureAtlasSprite) {
            return localTextureAtlasSprite1;
        }
        Object localObject = localTextureAtlasSprite1;
        for (int i = 0; i < 3; i++) {
            TextureAtlasSprite localTextureAtlasSprite2 = getConnectedTextureSingle(paramIBlockAccess, paramIBlockState, paramBlockPos, paramEnumFacing, (TextureAtlasSprite) localObject, false, paramRenderEnv);
            if (localTextureAtlasSprite2 == localObject) {
                break;
            }
            localObject = localTextureAtlasSprite2;
        }
        return (TextureAtlasSprite) localObject;
    }

    public static TextureAtlasSprite getConnectedTextureSingle(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, EnumFacing paramEnumFacing, TextureAtlasSprite paramTextureAtlasSprite, boolean paramBoolean, RenderEnv paramRenderEnv) {
        Block localBlock = paramIBlockState.getBlock();
        if (!(paramIBlockState instanceof BlockStateBase)) {
            return paramTextureAtlasSprite;
        }
        BlockStateBase localBlockStateBase = (BlockStateBase) paramIBlockState;
        int i;
        ConnectedProperties[] arrayOfConnectedProperties;
        int j;
        int k;
        ConnectedProperties localConnectedProperties;
        TextureAtlasSprite localTextureAtlasSprite;
        if (tileProperties != null) {
            i = paramTextureAtlasSprite.getIndexInMap();
            if ((i >= 0) && (i < tileProperties.length)) {
                arrayOfConnectedProperties = tileProperties[i];
                if (arrayOfConnectedProperties != null) {
                    j = getSide(paramEnumFacing);
                    for (k = 0; k < arrayOfConnectedProperties.length; k++) {
                        localConnectedProperties = arrayOfConnectedProperties[k];
                        if ((localConnectedProperties != null) && (localConnectedProperties.matchesBlockId(localBlockStateBase.getBlockId()))) {
                            localTextureAtlasSprite = getConnectedTexture(localConnectedProperties, paramIBlockAccess, localBlockStateBase, paramBlockPos, j, paramTextureAtlasSprite, paramRenderEnv);
                            if (localTextureAtlasSprite != null) {
                                return localTextureAtlasSprite;
                            }
                        }
                    }
                }
            }
        }
        if ((blockProperties != null) && (paramBoolean)) {
            i = paramRenderEnv.getBlockId();
            if ((i >= 0) && (i < blockProperties.length)) {
                arrayOfConnectedProperties = blockProperties[i];
                if (arrayOfConnectedProperties != null) {
                    j = getSide(paramEnumFacing);
                    for (k = 0; k < arrayOfConnectedProperties.length; k++) {
                        localConnectedProperties = arrayOfConnectedProperties[k];
                        if ((localConnectedProperties != null) && (localConnectedProperties.matchesIcon(paramTextureAtlasSprite))) {
                            localTextureAtlasSprite = getConnectedTexture(localConnectedProperties, paramIBlockAccess, localBlockStateBase, paramBlockPos, j, paramTextureAtlasSprite, paramRenderEnv);
                            if (localTextureAtlasSprite != null) {
                                return localTextureAtlasSprite;
                            }
                        }
                    }
                }
            }
        }
        return paramTextureAtlasSprite;
    }

    public static int getSide(EnumFacing paramEnumFacing) {
        if (paramEnumFacing == null) {
            return -1;
        }
        switch (paramEnumFacing) {
            case DOWN:
                return 0;
            case UP:
                return 1;
            case EAST:
                return 5;
            case WEST:
                return 4;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
        }
        return -1;
    }

    private static EnumFacing getFacing(int paramInt) {
        switch (paramInt) {
            case 0:
                return EnumFacing.DOWN;
            case 1:
                return EnumFacing.UP;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.SOUTH;
            case 4:
                return EnumFacing.WEST;
            case 5:
                return EnumFacing.EAST;
        }
        return EnumFacing.UP;
    }

    private static TextureAtlasSprite getConnectedTexture(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, BlockStateBase paramBlockStateBase, BlockPos paramBlockPos, int paramInt, TextureAtlasSprite paramTextureAtlasSprite, RenderEnv paramRenderEnv) {
        int i = 0;
        int j = paramBlockStateBase.getMetadata();
        int k = j;
        Block localBlock = paramBlockStateBase.getBlock();
        if ((localBlock instanceof BlockRotatedPillar)) {
            i = getWoodAxis(paramInt, j);
            if (paramConnectedProperties.getMetadataMax() <= 3) {
                k = j >> 3;
            }
        }
        if ((localBlock instanceof BlockQuartz)) {
            i = getQuartzAxis(paramInt, j);
            if ((paramConnectedProperties.getMetadataMax() <= 2) && (k > 2)) {
                k = 2;
            }
        }
        if (!paramConnectedProperties.matchesBlock(paramBlockStateBase.getBlockId(), k)) {
            return null;
        }
        if ((paramInt >= 0) && (paramConnectedProperties.faces != 63)) {
            m = paramInt;
            if (i != 0) {
                m = fixSideByAxis(paramInt, i);
            }
            if (1 >>> m >> paramConnectedProperties.faces == 0) {
                return null;
            }
        }
        int m = paramBlockPos.getY();
        if ((m >= paramConnectedProperties.minHeight) && (m <= paramConnectedProperties.maxHeight)) {
            if (paramConnectedProperties.biomes != null) {
                BiomeGenBase localBiomeGenBase = paramIBlockAccess.getBiomeGenForCoords(paramBlockPos);
                if (!paramConnectedProperties.matchesBiome(localBiomeGenBase)) {
                    return null;
                }
            }
            switch (paramConnectedProperties.method) {
                case 1:
                    return getConnectedTextureCtm(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j, paramRenderEnv);
                case 2:
                    return getConnectedTextureHorizontal(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j);
                case 3:
                    return getConnectedTextureTop(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j);
                case 4:
                    return getConnectedTextureRandom(paramConnectedProperties, paramBlockPos, paramInt);
                case 5:
                    return getConnectedTextureRepeat(paramConnectedProperties, paramBlockPos, paramInt);
                case 6:
                    return getConnectedTextureVertical(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j);
                case 7:
                    return getConnectedTextureFixed(paramConnectedProperties);
                case 8:
                    return getConnectedTextureHorizontalVertical(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j);
                case 9:
                    return getConnectedTextureVerticalHorizontal(paramConnectedProperties, paramIBlockAccess, paramBlockStateBase, paramBlockPos, i, paramInt, paramTextureAtlasSprite, j);
            }
            return null;
        }
        return null;
    }

    private static int fixSideByAxis(int paramInt1, int paramInt2) {
        switch (paramInt2) {
            case 0:
                return paramInt1;
            case 1:
                switch (paramInt1) {
                    case 0:
                        return 2;
                    case 1:
                        return 3;
                    case 2:
                        return 1;
                    case 3:
                        return 0;
                }
                return paramInt1;
            case 2:
                switch (paramInt1) {
                    case 0:
                        return 4;
                    case 1:
                        return 5;
                    case 2:
                    case 3:
                    default:
                        return paramInt1;
                    case 4:
                        return 1;
                }
                return 0;
        }
        return paramInt1;
    }

    private static int getWoodAxis(int paramInt1, int paramInt2) {
        int i = paramInt2 >> 12 & 0x2;
        switch (i) {
            case 1:
                return 2;
            case 2:
                return 1;
        }
        return 0;
    }

    private static int getQuartzAxis(int paramInt1, int paramInt2) {
        switch (paramInt2) {
            case 3:
                return 2;
            case 4:
                return 1;
        }
        return 0;
    }

    private static TextureAtlasSprite getConnectedTextureRandom(ConnectedProperties paramConnectedProperties, BlockPos paramBlockPos, int paramInt) {
        if (paramConnectedProperties.tileIcons.length == 1) {
            return paramConnectedProperties.tileIcons[0];
        }
        int i = -paramConnectedProperties.symmetry * paramConnectedProperties.symmetry;
        int j = Config.getRandom(paramBlockPos, i) >> Integer.MAX_VALUE;
        int k = 0;
        k = j << paramConnectedProperties.tileIcons.length;
        int m = j << paramConnectedProperties.sumAllWeights;
        int[] arrayOfInt = paramConnectedProperties.sumWeights;
        for (int n = 0; n < arrayOfInt.length; n++) {
            if (m < arrayOfInt[n]) {
                k = n;
                break;
            }
        }
        return paramConnectedProperties.tileIcons[k];
    }

    private static TextureAtlasSprite getConnectedTextureFixed(ConnectedProperties paramConnectedProperties) {
        return paramConnectedProperties.tileIcons[0];
    }

    private static TextureAtlasSprite getConnectedTextureRepeat(ConnectedProperties arg0, BlockPos arg1, int arg2) {
        // Byte code:
        //   0: aload_0
        //   1: getfield 320	optifine/ConnectedProperties:tileIcons	[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
        //   4: arraylength
        //   5: iconst_1
        //   6: if_icmpne +10 -> 16
        //   9: aload_0
        //   10: getfield 320	optifine/ConnectedProperties:tileIcons	[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
        //   13: iconst_0
        //   14: aaload
        //   15: areturn
        //   16: aload_1
        //   17: invokevirtual 342	net/minecraft/util/BlockPos:getX	()I
        //   20: istore_3
        //   21: aload_1
        //   22: invokevirtual 264	net/minecraft/util/BlockPos:getY	()I
        //   25: istore 4
        //   27: aload_1
        //   28: invokevirtual 345	net/minecraft/util/BlockPos:getZ	()I
        //   31: istore 5
        //   33: iconst_0
        //   34: istore 6
        //   36: iconst_0
        //   37: istore 7
        //   39: iload_2
        //   40: tableswitch	default:+109->149, 0:+40->80, 1:+50->90, 2:+60->100, 3:+74->114, 4:+85->125, 5:+97->137
        //   80: iload_3
        //   81: istore 6
        //   83: iload 5
        //   85: istore 7
        //   87: goto +62 -> 149
        //   90: iload_3
        //   91: istore 6
        //   93: iload 5
        //   95: istore 7
        //   97: goto +52 -> 149
        //   100: iload_3
        //   101: idiv
        //   102: iconst_1
        //   103: isub
        //   104: istore 6
        //   106: iload 4
        //   108: idiv
        //   109: istore 7
        //   111: goto +38 -> 149
        //   114: iload_3
        //   115: istore 6
        //   117: iload 4
        //   119: idiv
        //   120: istore 7
        //   122: goto +27 -> 149
        //   125: iload 5
        //   127: istore 6
        //   129: iload 4
        //   131: idiv
        //   132: istore 7
        //   134: goto +15 -> 149
        //   137: iload 5
        //   139: idiv
        //   140: iconst_1
        //   141: isub
        //   142: istore 6
        //   144: iload 4
        //   146: idiv
        //   147: istore 7
        //   149: iload 6
        //   151: aload_0
        //   152: getfield 348	optifine/ConnectedProperties:width	I
        //   155: ishl
        //   156: istore 6
        //   158: iload 7
        //   160: aload_0
        //   161: getfield 351	optifine/ConnectedProperties:height	I
        //   164: ishl
        //   165: istore 7
        //   167: iload 6
        //   169: ifge +12 -> 181
        //   172: iload 6
        //   174: aload_0
        //   175: getfield 348	optifine/ConnectedProperties:width	I
        //   178: ior
        //   179: istore 6
        //   181: iload 7
        //   183: ifge +12 -> 195
        //   186: iload 7
        //   188: aload_0
        //   189: getfield 351	optifine/ConnectedProperties:height	I
        //   192: ior
        //   193: istore 7
        //   195: iload 7
        //   197: aload_0
        //   198: getfield 348	optifine/ConnectedProperties:width	I
        //   201: imul
        //   202: iload 6
        //   204: ior
        //   205: istore 8
        //   207: aload_0
        //   208: getfield 320	optifine/ConnectedProperties:tileIcons	[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
        //   211: iload 8
        //   213: aaload
        //   214: areturn
    }

    private static TextureAtlasSprite getConnectedTextureCtm(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3, RenderEnv paramRenderEnv) {
        boolean[] arrayOfBoolean = paramRenderEnv.getBorderFlags();
        switch (paramInt2) {
            case 0:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 1:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 2:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 3:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 4:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 5:
                arrayOfBoolean[0] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[1] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[2] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                arrayOfBoolean[3] = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
        }
        int i = 0;
        if (arrayOfBoolean[0] >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 3;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> arrayOfBoolean[1] >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 1;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> arrayOfBoolean[2] >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 12;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> arrayOfBoolean[3] != 0) {
            i = 36;
        } else if (arrayOfBoolean[0] >> arrayOfBoolean[1] >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 2;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> arrayOfBoolean[2] >> arrayOfBoolean[3] != 0) {
            i = 24;
        } else if (arrayOfBoolean[0] >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> arrayOfBoolean[2] >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 15;
        } else if (arrayOfBoolean[0] >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> arrayOfBoolean[3] != 0) {
            i = 39;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> arrayOfBoolean[1] >> arrayOfBoolean[2] >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 13;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> arrayOfBoolean[1] >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> arrayOfBoolean[3] != 0) {
            i = 37;
        } else if ((arrayOfBoolean[0] == 0 ? 1 : 0) >> arrayOfBoolean[1] >> arrayOfBoolean[2] >> arrayOfBoolean[3] != 0) {
            i = 25;
        } else if (arrayOfBoolean[0] >> (arrayOfBoolean[1] == 0 ? 1 : 0) >> arrayOfBoolean[2] >> arrayOfBoolean[3] != 0) {
            i = 27;
        } else if (arrayOfBoolean[0] >> arrayOfBoolean[1] >> (arrayOfBoolean[2] == 0 ? 1 : 0) >> arrayOfBoolean[3] != 0) {
            i = 38;
        } else if (arrayOfBoolean[0] >> arrayOfBoolean[1] >> arrayOfBoolean[2] >> (arrayOfBoolean[3] == 0 ? 1 : 0) != 0) {
            i = 14;
        } else if (arrayOfBoolean[0] >> arrayOfBoolean[1] >> arrayOfBoolean[2] >> arrayOfBoolean[3] != 0) {
            i = 26;
        }
        if (i == 0) {
            return paramConnectedProperties.tileIcons[i];
        }
        if (!Config.isConnectedTexturesFancy()) {
            return paramConnectedProperties.tileIcons[i];
        }
        switch (paramInt2) {
            case 0:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                break;
            case 1:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                break;
            case 2:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().down(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().down(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().up(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().up(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                break;
            case 3:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().down(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().down(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east().up(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west().up(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                break;
            case 4:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                break;
            case 5:
                arrayOfBoolean[0] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[1] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[2] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up().north(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
                arrayOfBoolean[3] = (!isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up().south(), paramInt2, paramTextureAtlasSprite, paramInt3) ? 1 : false);
        }
        if ((i == 13) && (arrayOfBoolean[0] != 0)) {
            i = 4;
        } else if ((i == 15) && (arrayOfBoolean[1] != 0)) {
            i = 5;
        } else if ((i == 37) && (arrayOfBoolean[2] != 0)) {
            i = 16;
        } else if ((i == 39) && (arrayOfBoolean[3] != 0)) {
            i = 17;
        } else if ((i == 14) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] != 0)) {
            i = 7;
        } else if ((i == 25) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[2] != 0)) {
            i = 6;
        } else if ((i == 27) && (arrayOfBoolean[3] != 0) && (arrayOfBoolean[1] != 0)) {
            i = 19;
        } else if ((i == 38) && (arrayOfBoolean[3] != 0) && (arrayOfBoolean[2] != 0)) {
            i = 18;
        } else if ((i == 14) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] != 0)) {
            i = 31;
        } else if ((i == 25) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[2] == 0)) {
            i = 30;
        } else if ((i == 27) && (arrayOfBoolean[3] == 0) && (arrayOfBoolean[1] != 0)) {
            i = 41;
        } else if ((i == 38) && (arrayOfBoolean[3] != 0) && (arrayOfBoolean[2] == 0)) {
            i = 40;
        } else if ((i == 14) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] == 0)) {
            i = 29;
        } else if ((i == 25) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[2] != 0)) {
            i = 28;
        } else if ((i == 27) && (arrayOfBoolean[3] != 0) && (arrayOfBoolean[1] == 0)) {
            i = 43;
        } else if ((i == 38) && (arrayOfBoolean[3] == 0) && (arrayOfBoolean[2] != 0)) {
            i = 42;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] != 0)) {
            i = 46;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] != 0)) {
            i = 9;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] != 0)) {
            i = 21;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] != 0)) {
            i = 8;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] == 0)) {
            i = 20;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] == 0)) {
            i = 11;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] != 0)) {
            i = 22;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] != 0)) {
            i = 23;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] == 0)) {
            i = 10;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] != 0)) {
            i = 34;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] == 0)) {
            i = 35;
        } else if ((i == 26) && (arrayOfBoolean[0] != 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] == 0)) {
            i = 32;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] != 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] == 0)) {
            i = 33;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] != 0) && (arrayOfBoolean[3] == 0)) {
            i = 44;
        } else if ((i == 26) && (arrayOfBoolean[0] == 0) && (arrayOfBoolean[1] == 0) && (arrayOfBoolean[2] == 0) && (arrayOfBoolean[3] != 0)) {
            i = 45;
        }
        return paramConnectedProperties.tileIcons[i];
    }

    private static boolean isNeighbour(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, TextureAtlasSprite paramTextureAtlasSprite, int paramInt2) {
        IBlockState localIBlockState = paramIBlockAccess.getBlockState(paramBlockPos);
        if (paramIBlockState == localIBlockState) {
            return true;
        }
        if (paramConnectedProperties.connect == 2) {
            if (localIBlockState == null) {
                return false;
            }
            if (localIBlockState == AIR_DEFAULT_STATE) {
                return false;
            }
            TextureAtlasSprite localTextureAtlasSprite = getNeighbourIcon(paramIBlockAccess, paramBlockPos, localIBlockState, paramInt1);
            return localTextureAtlasSprite == paramTextureAtlasSprite;
        }
        return localIBlockState != null;
    }

    private static TextureAtlasSprite getNeighbourIcon(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos, IBlockState paramIBlockState, int paramInt) {
        paramIBlockState = paramIBlockState.getBlock().getActualState(paramIBlockState, paramIBlockAccess, paramBlockPos);
        IBakedModel localIBakedModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(paramIBlockState);
        if (localIBakedModel == null) {
            return null;
        }
        EnumFacing localEnumFacing = getFacing(paramInt);
        List localList = localIBakedModel.getFaceQuads(localEnumFacing);
        if (localList.size() > 0) {
            localObject = (BakedQuad) localList.get(0);
            return ((BakedQuad) localObject).getSprite();
        }
        Object localObject = localIBakedModel.getGeneralQuads();
        for (int i = 0; i < ((List) localObject).size(); i++) {
            BakedQuad localBakedQuad = (BakedQuad) ((List) localObject).get(i);
            if (localBakedQuad.getFace() == localEnumFacing) {
                return localBakedQuad.getSprite();
            }
        }
        return null;
    }

    private static TextureAtlasSprite getConnectedTextureHorizontal(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3) {
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1) {
            case 0:
                switch (paramInt2) {
                    case 0:
                    case 1:
                        return null;
                    case 2:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 3:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 4:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 5:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                }
                break;
            case 1:
                switch (paramInt2) {
                    case 0:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 1:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 2:
                    case 3:
                        return null;
                    case 4:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 5:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                }
                break;
            case 2:
                switch (paramInt2) {
                    case 0:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 1:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 2:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 3:
                        bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                        break;
                    case 4:
                    case 5:
                        return null;
                }
                break;
        }
        int i = 3;
        if (bool1) {
            if (bool2) {
                i = 1;
            } else {
                i = 2;
            }
        } else if (bool2) {
            i = 0;
        } else {
            i = 3;
        }
        return paramConnectedProperties.tileIcons[i];
    }

    private static TextureAtlasSprite getConnectedTextureVertical(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3) {
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1) {
            case 0:
                if ((paramInt2 == 1) || (paramInt2 == 0)) {
                    return null;
                }
                bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.down(), paramInt2, paramTextureAtlasSprite, paramInt3);
                bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 1:
                if ((paramInt2 == 3) || (paramInt2 == 2)) {
                    return null;
                }
                bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.north(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 2:
                if ((paramInt2 == 5) || (paramInt2 == 4)) {
                    return null;
                }
                bool1 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.west(), paramInt2, paramTextureAtlasSprite, paramInt3);
                bool2 = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
        }
        int i = 3;
        if (bool1) {
            if (bool2) {
                i = 1;
            } else {
                i = 2;
            }
        } else if (bool2) {
            i = 0;
        } else {
            i = 3;
        }
        return paramConnectedProperties.tileIcons[i];
    }

    private static TextureAtlasSprite getConnectedTextureHorizontalVertical(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3) {
        TextureAtlasSprite[] arrayOfTextureAtlasSprite = paramConnectedProperties.tileIcons;
        TextureAtlasSprite localTextureAtlasSprite1 = getConnectedTextureHorizontal(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos, paramInt1, paramInt2, paramTextureAtlasSprite, paramInt3);
        if ((localTextureAtlasSprite1 != null) && (localTextureAtlasSprite1 != paramTextureAtlasSprite) && (localTextureAtlasSprite1 != arrayOfTextureAtlasSprite[3])) {
            return localTextureAtlasSprite1;
        }
        TextureAtlasSprite localTextureAtlasSprite2 = getConnectedTextureVertical(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos, paramInt1, paramInt2, paramTextureAtlasSprite, paramInt3);
        return localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[2] ? arrayOfTextureAtlasSprite[6] : localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[1] ? arrayOfTextureAtlasSprite[5] : localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[0] ? arrayOfTextureAtlasSprite[4] : localTextureAtlasSprite2;
    }

    private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3) {
        TextureAtlasSprite[] arrayOfTextureAtlasSprite = paramConnectedProperties.tileIcons;
        TextureAtlasSprite localTextureAtlasSprite1 = getConnectedTextureVertical(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos, paramInt1, paramInt2, paramTextureAtlasSprite, paramInt3);
        if ((localTextureAtlasSprite1 != null) && (localTextureAtlasSprite1 != paramTextureAtlasSprite) && (localTextureAtlasSprite1 != arrayOfTextureAtlasSprite[3])) {
            return localTextureAtlasSprite1;
        }
        TextureAtlasSprite localTextureAtlasSprite2 = getConnectedTextureHorizontal(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos, paramInt1, paramInt2, paramTextureAtlasSprite, paramInt3);
        return localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[2] ? arrayOfTextureAtlasSprite[6] : localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[1] ? arrayOfTextureAtlasSprite[5] : localTextureAtlasSprite2 == arrayOfTextureAtlasSprite[0] ? arrayOfTextureAtlasSprite[4] : localTextureAtlasSprite2;
    }

    private static TextureAtlasSprite getConnectedTextureTop(ConnectedProperties paramConnectedProperties, IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, int paramInt1, int paramInt2, TextureAtlasSprite paramTextureAtlasSprite, int paramInt3) {
        boolean bool = false;
        switch (paramInt1) {
            case 0:
                if ((paramInt2 == 1) || (paramInt2 == 0)) {
                    return null;
                }
                bool = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.up(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 1:
                if ((paramInt2 == 3) || (paramInt2 == 2)) {
                    return null;
                }
                bool = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.south(), paramInt2, paramTextureAtlasSprite, paramInt3);
                break;
            case 2:
                if ((paramInt2 == 5) || (paramInt2 == 4)) {
                    return null;
                }
                bool = isNeighbour(paramConnectedProperties, paramIBlockAccess, paramIBlockState, paramBlockPos.east(), paramInt2, paramTextureAtlasSprite, paramInt3);
        }
        if (bool) {
            return paramConnectedProperties.tileIcons[0];
        }
        return null;
    }

    public static void updateIcons(TextureMap paramTextureMap) {
        blockProperties = (ConnectedProperties[][]) null;
        tileProperties = (ConnectedProperties[][]) null;
        spriteQuadMaps = null;
        if (Config.isConnectedTextures()) {
            IResourcePack[] arrayOfIResourcePack = Config.getResourcePacks();
            for (int i = arrayOfIResourcePack.length - 1; i >= 0; i--) {
                IResourcePack localIResourcePack = arrayOfIResourcePack[i];
                updateIcons(paramTextureMap, localIResourcePack);
            }
            updateIcons(paramTextureMap, Config.getDefaultResourcePack());
            ResourceLocation localResourceLocation = new ResourceLocation("mcpatcher/ctm/default/empty");
            emptySprite = paramTextureMap.registerSprite(localResourceLocation);
            spriteQuadMaps = new Map[paramTextureMap.getCountRegisteredSprites() | 0x1];
            if (blockProperties.length <= 0) {
                blockProperties = (ConnectedProperties[][]) null;
            }
            if (tileProperties.length <= 0) {
                tileProperties = (ConnectedProperties[][]) null;
            }
        }
    }

    private static void updateIconEmpty(TextureMap paramTextureMap) {
    }

    public static void updateIcons(TextureMap paramTextureMap, IResourcePack paramIResourcePack) {
        String[] arrayOfString = ResUtils.collectFiles(paramIResourcePack, "mcpatcher/ctm/", ".properties", getDefaultCtmPaths());
        Arrays.sort((Object[]) arrayOfString);
        List localList1 = makePropertyList(tileProperties);
        List localList2 = makePropertyList(blockProperties);
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            Config.dbg("ConnectedTextures: " + str);
            try {
                ResourceLocation localResourceLocation = new ResourceLocation(str);
                InputStream localInputStream = paramIResourcePack.getInputStream(localResourceLocation);
                if (localInputStream == null) {
                    Config.warn("ConnectedTextures file not found: " + str);
                } else {
                    Properties localProperties = new Properties();
                    localProperties.load(localInputStream);
                    ConnectedProperties localConnectedProperties = new ConnectedProperties(localProperties, str);
                    if (localConnectedProperties.isValid(str)) {
                        localConnectedProperties.updateIcons(paramTextureMap);
                        addToTileList(localConnectedProperties, localList1);
                        addToBlockList(localConnectedProperties, localList2);
                    }
                }
            } catch (FileNotFoundException localFileNotFoundException) {
                Config.warn("ConnectedTextures file not found: " + str);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        blockProperties = propertyListToArray(localList2);
        tileProperties = propertyListToArray(localList1);
        multipass = detectMultipass();
        Config.dbg("Multipass connected textures: " + multipass);
    }

    private static List makePropertyList(ConnectedProperties[][] paramArrayOfConnectedProperties) {
        ArrayList localArrayList1 = new ArrayList();
        if (paramArrayOfConnectedProperties != null) {
            for (int i = 0; i < paramArrayOfConnectedProperties.length; i++) {
                ConnectedProperties[] arrayOfConnectedProperties = paramArrayOfConnectedProperties[i];
                ArrayList localArrayList2 = null;
                if (arrayOfConnectedProperties != null) {
                    localArrayList2 = new ArrayList(Arrays.asList(arrayOfConnectedProperties));
                }
                localArrayList1.add(localArrayList2);
            }
        }
        return localArrayList1;
    }

    private static boolean detectMultipass() {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < tileProperties.length; i++) {
            localObject = tileProperties[i];
            if (localObject != null) {
                localArrayList.addAll(Arrays.asList((Object[]) localObject));
            }
        }
        for (i = 0; i < blockProperties.length; i++) {
            localObject = blockProperties[i];
            if (localObject != null) {
                localArrayList.addAll(Arrays.asList((Object[]) localObject));
            }
        }
        ConnectedProperties[] arrayOfConnectedProperties = (ConnectedProperties[]) (ConnectedProperties[]) localArrayList.toArray(new ConnectedProperties[localArrayList.size()]);
        Object localObject = new HashSet();
        HashSet localHashSet = new HashSet();
        for (int j = 0; j < arrayOfConnectedProperties.length; j++) {
            ConnectedProperties localConnectedProperties = arrayOfConnectedProperties[j];
            if (localConnectedProperties.matchTileIcons != null) {
                ((Set) localObject).addAll(Arrays.asList(localConnectedProperties.matchTileIcons));
            }
            if (localConnectedProperties.tileIcons != null) {
                localHashSet.addAll(Arrays.asList(localConnectedProperties.tileIcons));
            }
        }
        ((Set) localObject).retainAll(localHashSet);
        return !((Set) localObject).isEmpty();
    }

    private static ConnectedProperties[][] propertyListToArray(List paramList) {
        ConnectedProperties[][] arrayOfConnectedProperties = new ConnectedProperties[paramList.size()][];
        for (int i = 0; i < paramList.size(); i++) {
            List localList = (List) paramList.get(i);
            if (localList != null) {
                ConnectedProperties[] arrayOfConnectedProperties1 = (ConnectedProperties[]) (ConnectedProperties[]) localList.toArray(new ConnectedProperties[localList.size()]);
                arrayOfConnectedProperties[i] = arrayOfConnectedProperties1;
            }
        }
        return arrayOfConnectedProperties;
    }

    private static void addToTileList(ConnectedProperties paramConnectedProperties, List paramList) {
        if (paramConnectedProperties.matchTileIcons != null) {
            for (int i = 0; i < paramConnectedProperties.matchTileIcons.length; i++) {
                TextureAtlasSprite localTextureAtlasSprite = paramConnectedProperties.matchTileIcons[i];
                if (!(localTextureAtlasSprite instanceof TextureAtlasSprite)) {
                    Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + localTextureAtlasSprite + ", name: " + localTextureAtlasSprite.getIconName());
                } else {
                    int j = localTextureAtlasSprite.getIndexInMap();
                    if (j < 0) {
                        Config.warn("Invalid tile ID: " + j + ", icon: " + localTextureAtlasSprite.getIconName());
                    } else {
                        addToList(paramConnectedProperties, paramList, j);
                    }
                }
            }
        }
    }

    private static void addToBlockList(ConnectedProperties paramConnectedProperties, List paramList) {
        if (paramConnectedProperties.matchBlocks != null) {
            for (int i = 0; i < paramConnectedProperties.matchBlocks.length; i++) {
                int j = paramConnectedProperties.matchBlocks[i].getBlockId();
                if (j < 0) {
                    Config.warn("Invalid block ID: " + j);
                } else {
                    addToList(paramConnectedProperties, paramList, j);
                }
            }
        }
    }

    private static void addToList(ConnectedProperties paramConnectedProperties, List paramList, int paramInt) {
        while (paramInt >= paramList.size()) {
            paramList.add(null);
        }
        Object localObject = (List) paramList.get(paramInt);
        if (localObject == null) {
            localObject = new ArrayList();
            paramList.set(paramInt, localObject);
        }
        ((List) localObject).add(paramConnectedProperties);
    }

    private static String[] getDefaultCtmPaths() {
        ArrayList localArrayList = new ArrayList();
        String str1 = "mcpatcher/ctm/default/";
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
            localArrayList.add(str1 + "glass.properties");
            localArrayList.add(str1 + "glasspane.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
            localArrayList.add(str1 + "bookshelf.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
            localArrayList.add(str1 + "sandstone.properties");
        }
        String[] arrayOfString1 = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
        for (int i = 0; i < arrayOfString1.length; i++) {
            String str2 = arrayOfString1[i];
            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + str2 + ".png"))) {
                localArrayList.add(str1 + i + "_glass_" + str2 + "/glass_" + str2 + ".properties");
                localArrayList.add(str1 + i + "_glass_" + str2 + "/glass_pane_" + str2 + ".properties");
            }
        }
        String[] arrayOfString2 = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        return arrayOfString2;
    }

    public static int getPaneTextureIndex(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
        return paramBoolean4 ? 16 : paramBoolean3 ? 48 : paramBoolean4 ? 32 : (!paramBoolean2) && (paramBoolean1) ? 1 : paramBoolean4 ? 17 : paramBoolean3 ? 49 : paramBoolean4 ? 33 : (paramBoolean2) && (!paramBoolean1) ? 3 : paramBoolean4 ? 19 : paramBoolean3 ? 51 : paramBoolean4 ? 35 : (paramBoolean2) && (paramBoolean1) ? 2 : paramBoolean4 ? 18 : paramBoolean3 ? 50 : paramBoolean4 ? 34 : 0;
    }

    public static int getReversePaneTextureIndex(int paramInt) {
        int i = paramInt << 16;
        return i == 3 ? paramInt - 2 : i == 1 ? paramInt | 0x2 : paramInt;
    }

    public static TextureAtlasSprite getCtmTexture(ConnectedProperties paramConnectedProperties, int paramInt, TextureAtlasSprite paramTextureAtlasSprite) {
        if (paramConnectedProperties.method != 1) {
            return paramTextureAtlasSprite;
        }
        if ((paramInt >= 0) && (paramInt < ctmIndexes.length)) {
            int i = ctmIndexes[paramInt];
            TextureAtlasSprite[] arrayOfTextureAtlasSprite = paramConnectedProperties.tileIcons;
            return (i >= 0) && (i < arrayOfTextureAtlasSprite.length) ? arrayOfTextureAtlasSprite[i] : paramTextureAtlasSprite;
        }
        return paramTextureAtlasSprite;
    }
}




