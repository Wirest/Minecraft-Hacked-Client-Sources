package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFireball extends Item
{
    public ItemFireball()
    {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        if (playerIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else
        {
            worldIn = worldIn.offset(hand);
            ItemStack itemstack = stack.getHeldItem(pos);

            if (!stack.canPlayerEdit(worldIn, hand, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                if (playerIn.getBlockState(worldIn).getMaterial() == Material.AIR)
                {
                    playerIn.playSound((EntityPlayer)null, worldIn, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F + 1.0F);
                    playerIn.setBlockState(worldIn, Blocks.FIRE.getDefaultState());
                }

                if (!stack.capabilities.isCreativeMode)
                {
                    itemstack.func_190918_g(1);
                }

                return EnumActionResult.SUCCESS;
            }
        }
    }
}
