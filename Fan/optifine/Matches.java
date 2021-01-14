package optifine;

import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.BiomeGenBase;

public class Matches
{
    public static boolean block(BlockStateBase p_block_0_, MatchBlock[] p_block_1_)
    {
        if (p_block_1_ == null)
        {
            return true;
        }
        else
        {
            for (MatchBlock matchblock : p_block_1_) {
                if (matchblock.matches(p_block_0_)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean block(int p_block_0_, int p_block_1_, MatchBlock[] p_block_2_)
    {
        if (p_block_2_ == null)
        {
            return true;
        }
        else
        {
            for (MatchBlock matchblock : p_block_2_) {
                if (matchblock.matches(p_block_0_, p_block_1_)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean blockId(int p_blockId_0_, MatchBlock[] p_blockId_1_)
    {
        if (p_blockId_1_ == null)
        {
            return true;
        }
        else
        {
            for (MatchBlock matchblock : p_blockId_1_) {
                if (matchblock.getBlockId() == p_blockId_0_) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean metadata(int p_metadata_0_, int[] p_metadata_1_)
    {
        if (p_metadata_1_ == null)
        {
            return true;
        }
        else
        {
            for (int j : p_metadata_1_) {
                if (j == p_metadata_0_) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean sprite(TextureAtlasSprite p_sprite_0_, TextureAtlasSprite[] p_sprite_1_)
    {
        if (p_sprite_1_ == null)
        {
            return true;
        }
        else
        {
            for (TextureAtlasSprite textureAtlasSprite : p_sprite_1_) {
                if (textureAtlasSprite == p_sprite_0_) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean biome(BiomeGenBase p_biome_0_, BiomeGenBase[] p_biome_1_)
    {
        if (p_biome_1_ == null)
        {
            return true;
        }
        else
        {
            for (BiomeGenBase biomeGenBase : p_biome_1_) {
                if (biomeGenBase == p_biome_0_) {
                    return true;
                }
            }

            return false;
        }
    }
}
