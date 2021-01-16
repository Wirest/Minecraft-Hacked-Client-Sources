package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRedstone extends Item
{
    public ItemRedstone()
    {
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        boolean flag = playerIn.getBlockState(worldIn).getBlock().isReplaceable(playerIn, worldIn);
        BlockPos blockpos = flag ? worldIn : worldIn.offset(hand);
        ItemStack itemstack = stack.getHeldItem(pos);

        if (stack.canPlayerEdit(blockpos, hand, itemstack) && playerIn.func_190527_a(playerIn.getBlockState(blockpos).getBlock(), blockpos, false, hand, (Entity)null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(playerIn, blockpos))
        {
            playerIn.setBlockState(blockpos, Blocks.REDSTONE_WIRE.getDefaultState());

            if (stack instanceof EntityPlayerMP)
            {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, blockpos, itemstack);
            }

            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}
