package net.minecraft.block;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import optifine.Config;

public class BlockLeavesBase extends Block {
    protected boolean field_150121_P;
    private static final String __OBFID = "CL_00000326";
    private static Map mapOriginalOpacity = new IdentityHashMap();

    protected BlockLeavesBase(Material materialIn, boolean fancyGraphics) {
        super(materialIn);
        this.field_150121_P = fancyGraphics;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return Config.isCullFacesLeaves() && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }

    public static void setLightOpacity(Block block, int opacity) {
        if (!mapOriginalOpacity.containsKey(block)) {
            mapOriginalOpacity.put(block, Integer.valueOf(block.getLightOpacity()));
        }

        block.setLightOpacity(opacity);
    }

    public static void restoreLightOpacity(Block block) {
        if (mapOriginalOpacity.containsKey(block)) {
            int opacity = ((Integer) mapOriginalOpacity.get(block)).intValue();
            setLightOpacity(block, opacity);
        }
    }
}
