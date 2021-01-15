package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBed extends Item
{

    public ItemBed()
    {
        this.setCreativeTab(CreativeTabs.tabDecorations);
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
        if (worldIn.isRemote)
        {
            return true;
        }
        else if (side != EnumFacing.UP)
        {
            return false;
        }
        else
        {
            IBlockState var9 = worldIn.getBlockState(pos);
            Block var10 = var9.getBlock();
            boolean var11 = var10.isReplaceable(worldIn, pos);

            if (!var11)
            {
                pos = pos.up();
            }

            int var12 = MathHelper.floor_double(playerIn.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            EnumFacing var13 = EnumFacing.getHorizontal(var12);
            BlockPos var14 = pos.offset(var13);
            boolean var15 = var10.isReplaceable(worldIn, var14);
            boolean var16 = worldIn.isAirBlock(pos) || var11;
            boolean var17 = worldIn.isAirBlock(var14) || var15;

            if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(var14, side, stack))
            {
                if (var16 && var17 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && World.doesBlockHaveSolidTopSurface(worldIn, var14.down()))
                {
                    var13.getHorizontalIndex();
                    IBlockState var19 = Blocks.bed.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockDirectional.FACING, var13).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

                    if (worldIn.setBlockState(pos, var19, 3))
                    {
                        IBlockState var20 = var19.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
                        worldIn.setBlockState(var14, var20, 3);
                    }

                    --stack.stackSize;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
