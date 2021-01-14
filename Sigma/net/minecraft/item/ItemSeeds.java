package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeeds extends Item {
    private Block cropBlock;

    /**
     * BlockID of the block the seeds can be planted on.
     */
    private Block soilBlockID;
    private static final String __OBFID = "CL_00000061";

    public ItemSeeds(Block cropBlock, Block soilBlock) {
        this.cropBlock = cropBlock;
        soilBlockID = soilBlock;
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    public Block getCropBlock() {
        return cropBlock;
    }

    /**
     * Called when a Block is right-clicked with this Item
     *
     * @param pos  The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.UP) {
            return false;
        } else if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        } else if (worldIn.getBlockState(pos).getBlock() == soilBlockID && worldIn.isAirBlock(pos.offsetUp())) {
            worldIn.setBlockState(pos.offsetUp(), cropBlock.getDefaultState());
            --stack.stackSize;
            return true;
        } else {
            return false;
        }
    }
}
