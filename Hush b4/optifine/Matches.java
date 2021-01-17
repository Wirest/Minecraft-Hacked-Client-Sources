// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.BlockStateBase;

public class Matches
{
    public static boolean block(final BlockStateBase p_block_0_, final MatchBlock[] p_block_1_) {
        if (p_block_1_ == null) {
            return true;
        }
        for (int i = 0; i < p_block_1_.length; ++i) {
            final MatchBlock matchblock = p_block_1_[i];
            if (matchblock.matches(p_block_0_)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean block(final int p_block_0_, final int p_block_1_, final MatchBlock[] p_block_2_) {
        if (p_block_2_ == null) {
            return true;
        }
        for (int i = 0; i < p_block_2_.length; ++i) {
            final MatchBlock matchblock = p_block_2_[i];
            if (matchblock.matches(p_block_0_, p_block_1_)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean blockId(final int p_blockId_0_, final MatchBlock[] p_blockId_1_) {
        if (p_blockId_1_ == null) {
            return true;
        }
        for (int i = 0; i < p_blockId_1_.length; ++i) {
            final MatchBlock matchblock = p_blockId_1_[i];
            if (matchblock.getBlockId() == p_blockId_0_) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean metadata(final int p_metadata_0_, final int[] p_metadata_1_) {
        if (p_metadata_1_ == null) {
            return true;
        }
        for (int i = 0; i < p_metadata_1_.length; ++i) {
            if (p_metadata_1_[i] == p_metadata_0_) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean sprite(final TextureAtlasSprite p_sprite_0_, final TextureAtlasSprite[] p_sprite_1_) {
        if (p_sprite_1_ == null) {
            return true;
        }
        for (int i = 0; i < p_sprite_1_.length; ++i) {
            if (p_sprite_1_[i] == p_sprite_0_) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean biome(final BiomeGenBase p_biome_0_, final BiomeGenBase[] p_biome_1_) {
        if (p_biome_1_ == null) {
            return true;
        }
        for (int i = 0; i < p_biome_1_.length; ++i) {
            if (p_biome_1_[i] == p_biome_0_) {
                return true;
            }
        }
        return false;
    }
}
