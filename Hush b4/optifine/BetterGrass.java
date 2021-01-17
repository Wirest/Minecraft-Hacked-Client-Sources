// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockMycelium;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.ArrayList;
import net.minecraft.client.resources.model.IBakedModel;

public class BetterGrass
{
    private static IBakedModel modelEmpty;
    private static IBakedModel modelCubeMycelium;
    private static IBakedModel modelCubeGrassSnowy;
    private static IBakedModel modelCubeGrass;
    
    static {
        BetterGrass.modelEmpty = new SimpleBakedModel(new ArrayList<BakedQuad>(), new ArrayList<List<BakedQuad>>(), false, false, null, null);
        BetterGrass.modelCubeMycelium = null;
        BetterGrass.modelCubeGrassSnowy = null;
        BetterGrass.modelCubeGrass = null;
    }
    
    public static void update() {
        BetterGrass.modelCubeGrass = BlockModelUtils.makeModelCube("minecraft:blocks/grass_top", 0);
        BetterGrass.modelCubeGrassSnowy = BlockModelUtils.makeModelCube("minecraft:blocks/snow", -1);
        BetterGrass.modelCubeMycelium = BlockModelUtils.makeModelCube("minecraft:blocks/mycelium_top", -1);
    }
    
    public static List getFaceQuads(final IBlockAccess p_getFaceQuads_0_, final Block p_getFaceQuads_1_, final BlockPos p_getFaceQuads_2_, final EnumFacing p_getFaceQuads_3_, final List p_getFaceQuads_4_) {
        if (p_getFaceQuads_3_ == EnumFacing.UP || p_getFaceQuads_3_ == EnumFacing.DOWN) {
            return p_getFaceQuads_4_;
        }
        if (p_getFaceQuads_1_ instanceof BlockMycelium) {
            return Config.isBetterGrassFancy() ? ((getBlockAt(p_getFaceQuads_2_.down(), p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.mycelium) ? BetterGrass.modelCubeMycelium.getFaceQuads(p_getFaceQuads_3_) : p_getFaceQuads_4_) : BetterGrass.modelCubeMycelium.getFaceQuads(p_getFaceQuads_3_);
        }
        if (p_getFaceQuads_1_ instanceof BlockGrass) {
            final Block block = p_getFaceQuads_0_.getBlockState(p_getFaceQuads_2_.up()).getBlock();
            final boolean flag = block == Blocks.snow || block == Blocks.snow_layer;
            if (!Config.isBetterGrassFancy()) {
                if (flag) {
                    return BetterGrass.modelCubeGrassSnowy.getFaceQuads(p_getFaceQuads_3_);
                }
                return BetterGrass.modelCubeGrass.getFaceQuads(p_getFaceQuads_3_);
            }
            else if (flag) {
                if (getBlockAt(p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.snow_layer) {
                    return BetterGrass.modelCubeGrassSnowy.getFaceQuads(p_getFaceQuads_3_);
                }
            }
            else if (getBlockAt(p_getFaceQuads_2_.down(), p_getFaceQuads_3_, p_getFaceQuads_0_) == Blocks.grass) {
                return BetterGrass.modelCubeGrass.getFaceQuads(p_getFaceQuads_3_);
            }
        }
        return p_getFaceQuads_4_;
    }
    
    private static Block getBlockAt(final BlockPos p_getBlockAt_0_, final EnumFacing p_getBlockAt_1_, final IBlockAccess p_getBlockAt_2_) {
        final BlockPos blockpos = p_getBlockAt_0_.offset(p_getBlockAt_1_);
        final Block block = p_getBlockAt_2_.getBlockState(blockpos).getBlock();
        return block;
    }
}
