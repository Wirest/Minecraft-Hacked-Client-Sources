package optifine;

import net.minecraft.block.*;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BetterSnow {
    private static IBakedModel modelSnowLayer = null;

    public static void update() {
        modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.snow_layer.getDefaultState());
    }

    public static IBakedModel getModelSnowLayer() {
        return modelSnowLayer;
    }

    public static IBlockState getStateSnowLayer() {
        return Blocks.snow_layer.getDefaultState();
    }

    public static boolean shouldRender(IBlockAccess paramIBlockAccess, Block paramBlock, IBlockState paramIBlockState, BlockPos paramBlockPos) {
        return !checkBlock(paramBlock, paramIBlockState) ? false : hasSnowNeighbours(paramIBlockAccess, paramBlockPos);
    }

    private static boolean hasSnowNeighbours(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos) {
        Block localBlock = Blocks.snow_layer;
        return (paramIBlockAccess.getBlockState(paramBlockPos.north()).getBlock() != localBlock) && (paramIBlockAccess.getBlockState(paramBlockPos.south()).getBlock() != localBlock) && (paramIBlockAccess.getBlockState(paramBlockPos.west()).getBlock() != localBlock) && (paramIBlockAccess.getBlockState(paramBlockPos.east()).getBlock() != localBlock) ? false : paramIBlockAccess.getBlockState(paramBlockPos.down()).getBlock().isOpaqueCube();
    }

    private static boolean checkBlock(Block paramBlock, IBlockState paramIBlockState) {
        if (paramBlock.isFullCube()) {
            return false;
        }
        if (paramBlock.isOpaqueCube()) {
            return false;
        }
        if ((paramBlock instanceof BlockSnow)) {
            return false;
        }
        if ((!(paramBlock instanceof BlockBush)) || ((!(paramBlock instanceof BlockDoublePlant)) && (!(paramBlock instanceof BlockFlower)) && (!(paramBlock instanceof BlockMushroom)) && (!(paramBlock instanceof BlockSapling)) && (!(paramBlock instanceof BlockTallGrass)))) {
            if ((!(paramBlock instanceof BlockFence)) && (!(paramBlock instanceof BlockFenceGate)) && (!(paramBlock instanceof BlockFlowerPot)) && (!(paramBlock instanceof BlockPane)) && (!(paramBlock instanceof BlockReed)) && (!(paramBlock instanceof BlockWall))) {
                if (((paramBlock instanceof BlockRedstoneTorch)) && (paramIBlockState.getValue(BlockTorch.FACING) == EnumFacing.UP)) {
                    return true;
                }
                if ((paramBlock instanceof BlockLever)) {
                    Comparable localComparable = paramIBlockState.getValue(BlockLever.FACING);
                    if ((localComparable == BlockLever.EnumOrientation.UP_X) || (localComparable == BlockLever.EnumOrientation.UP_Z)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        return true;
    }
}




