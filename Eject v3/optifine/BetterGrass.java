package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMycelium;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class BetterGrass {
    private static IBakedModel modelEmpty = new SimpleBakedModel(new ArrayList(), new ArrayList(), false, false, (TextureAtlasSprite) null, (ItemCameraTransforms) null);
    private static IBakedModel modelCubeMycelium = null;
    private static IBakedModel modelCubeGrassSnowy = null;
    private static IBakedModel modelCubeGrass = null;

    public static void update() {
        modelCubeGrass = BlockModelUtils.makeModelCube("minecraft:blocks/grass_top", 0);
        modelCubeGrassSnowy = BlockModelUtils.makeModelCube("minecraft:blocks/snow", -1);
        modelCubeMycelium = BlockModelUtils.makeModelCube("minecraft:blocks/mycelium_top", -1);
    }

    public static List getFaceQuads(IBlockAccess paramIBlockAccess, Block paramBlock, BlockPos paramBlockPos, EnumFacing paramEnumFacing, List paramList) {
        if ((paramEnumFacing != EnumFacing.UP) && (paramEnumFacing != EnumFacing.DOWN)) {
            if ((paramBlock instanceof BlockMycelium)) {
                return Config.isBetterGrassFancy() ? paramList : getBlockAt(paramBlockPos.down(), paramEnumFacing, paramIBlockAccess) == Blocks.mycelium ? modelCubeMycelium.getFaceQuads(paramEnumFacing) : modelCubeMycelium.getFaceQuads(paramEnumFacing);
            }
            if ((paramBlock instanceof BlockGrass)) {
                Block localBlock = paramIBlockAccess.getBlockState(paramBlockPos.up()).getBlock();
                int i = (localBlock == Blocks.snow) || (localBlock == Blocks.snow_layer) ? 1 : 0;
                if (!Config.isBetterGrassFancy()) {
                    if (i != 0) {
                        return modelCubeGrassSnowy.getFaceQuads(paramEnumFacing);
                    }
                    return modelCubeGrass.getFaceQuads(paramEnumFacing);
                }
                if (i != 0) {
                    if (getBlockAt(paramBlockPos, paramEnumFacing, paramIBlockAccess) == Blocks.snow_layer) {
                        return modelCubeGrassSnowy.getFaceQuads(paramEnumFacing);
                    }
                } else if (getBlockAt(paramBlockPos.down(), paramEnumFacing, paramIBlockAccess) == Blocks.grass) {
                    return modelCubeGrass.getFaceQuads(paramEnumFacing);
                }
            }
            return paramList;
        }
        return paramList;
    }

    private static Block getBlockAt(BlockPos paramBlockPos, EnumFacing paramEnumFacing, IBlockAccess paramIBlockAccess) {
        BlockPos localBlockPos = paramBlockPos.offset(paramEnumFacing);
        Block localBlock = paramIBlockAccess.getBlockState(localBlockPos).getBlock();
        return localBlock;
    }
}




