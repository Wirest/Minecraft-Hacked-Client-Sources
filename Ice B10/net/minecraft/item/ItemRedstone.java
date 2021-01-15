package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstone extends Item
{
    private static final String __OBFID = "CL_00000058";

    public ItemRedstone()
    {
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        boolean var9 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        BlockPos var10 = var9 ? pos : pos.offset(side);

        if (!playerIn.func_175151_a(var10, side, stack))
        {
            return false;
        }
        else
        {
            Block var11 = worldIn.getBlockState(var10).getBlock();

            if (!worldIn.canBlockBePlaced(var11, var10, false, side, (Entity)null, stack))
            {
                return false;
            }
            else if (Blocks.redstone_wire.canPlaceBlockAt(worldIn, var10))
            {
                --stack.stackSize;
                worldIn.setBlockState(var10, Blocks.redstone_wire.getDefaultState());
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
