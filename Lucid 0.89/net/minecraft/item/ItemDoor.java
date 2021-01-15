package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemDoor extends Item
{
    private Block block;

    public ItemDoor(Block block)
    {
        this.block = block;
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side != EnumFacing.UP)
        {
            return false;
        }
        else
        {
            IBlockState var9 = worldIn.getBlockState(pos);
            Block var10 = var9.getBlock();

            if (!var10.isReplaceable(worldIn, pos))
            {
                pos = pos.offset(side);
            }

            if (!playerIn.canPlayerEdit(pos, side, stack))
            {
                return false;
            }
            else if (!this.block.canPlaceBlockAt(worldIn, pos))
            {
                return false;
            }
            else
            {
                placeDoor(worldIn, pos, EnumFacing.fromAngle(playerIn.rotationYaw), this.block);
                --stack.stackSize;
                return true;
            }
        }
    }

    public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door)
    {
        BlockPos var4 = pos.offset(facing.rotateY());
        BlockPos var5 = pos.offset(facing.rotateYCCW());
        int var6 = (worldIn.getBlockState(var5).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(var5.up()).getBlock().isNormalCube() ? 1 : 0);
        int var7 = (worldIn.getBlockState(var4).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(var4.up()).getBlock().isNormalCube() ? 1 : 0);
        boolean var8 = worldIn.getBlockState(var5).getBlock() == door || worldIn.getBlockState(var5.up()).getBlock() == door;
        boolean var9 = worldIn.getBlockState(var4).getBlock() == door || worldIn.getBlockState(var4.up()).getBlock() == door;
        boolean var10 = false;

        if (var8 && !var9 || var7 > var6)
        {
            var10 = true;
        }

        BlockPos var11 = pos.up();
        IBlockState var12 = door.getDefaultState().withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, var10 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
        worldIn.setBlockState(pos, var12.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
        worldIn.setBlockState(var11, var12.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
        worldIn.notifyNeighborsOfStateChange(pos, door);
        worldIn.notifyNeighborsOfStateChange(var11, door);
    }
}
