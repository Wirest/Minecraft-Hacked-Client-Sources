package optifine;

import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.BiomeGenBase;

public class Matches {
    public static boolean block(BlockStateBase paramBlockStateBase, MatchBlock[] paramArrayOfMatchBlock) {
        if (paramArrayOfMatchBlock == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfMatchBlock.length; i++) {
            MatchBlock localMatchBlock = paramArrayOfMatchBlock[i];
            if (localMatchBlock.matches(paramBlockStateBase)) {
                return true;
            }
        }
        return false;
    }

    public static boolean block(int paramInt1, int paramInt2, MatchBlock[] paramArrayOfMatchBlock) {
        if (paramArrayOfMatchBlock == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfMatchBlock.length; i++) {
            MatchBlock localMatchBlock = paramArrayOfMatchBlock[i];
            if (localMatchBlock.matches(paramInt1, paramInt2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean blockId(int paramInt, MatchBlock[] paramArrayOfMatchBlock) {
        if (paramArrayOfMatchBlock == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfMatchBlock.length; i++) {
            MatchBlock localMatchBlock = paramArrayOfMatchBlock[i];
            if (localMatchBlock.getBlockId() == paramInt) {
                return true;
            }
        }
        return false;
    }

    public static boolean metadata(int paramInt, int[] paramArrayOfInt) {
        if (paramArrayOfInt == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfInt.length; i++) {
            if (paramArrayOfInt[i] == paramInt) {
                return true;
            }
        }
        return false;
    }

    public static boolean sprite(TextureAtlasSprite paramTextureAtlasSprite, TextureAtlasSprite[] paramArrayOfTextureAtlasSprite) {
        if (paramArrayOfTextureAtlasSprite == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfTextureAtlasSprite.length; i++) {
            if (paramArrayOfTextureAtlasSprite[i] == paramTextureAtlasSprite) {
                return true;
            }
        }
        return false;
    }

    public static boolean biome(BiomeGenBase paramBiomeGenBase, BiomeGenBase[] paramArrayOfBiomeGenBase) {
        if (paramArrayOfBiomeGenBase == null) {
            return true;
        }
        for (int i = 0; i < paramArrayOfBiomeGenBase.length; i++) {
            if (paramArrayOfBiomeGenBase[i] == paramBiomeGenBase) {
                return true;
            }
        }
        return false;
    }
}




