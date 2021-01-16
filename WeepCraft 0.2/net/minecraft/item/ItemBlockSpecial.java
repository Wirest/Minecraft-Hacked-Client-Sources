package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSpecial extends Item
{
    private final Block block;

    public ItemBlockSpecial(Block block)
    {
        this.block = block;
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        Block block = iblockstate.getBlock();

        if (block == Blocks.SNOW_LAYER && ((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue() < 1)
        {
            hand = EnumFacing.UP;
        }
        else if (!block.isReplaceable(playerIn, worldIn))
        {
            worldIn = worldIn.offset(hand);
        }

        ItemStack itemstack = stack.getHeldItem(pos);

        if (!itemstack.func_190926_b() && stack.canPlayerEdit(worldIn, hand, itemstack) && playerIn.func_190527_a(this.block, worldIn, false, hand, (Entity)null))
        {
            IBlockState iblockstate1 = this.block.onBlockPlaced(playerIn, worldIn, hand, facing, hitX, hitY, 0, stack);

            if (!playerIn.setBlockState(worldIn, iblockstate1, 11))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                iblockstate1 = playerIn.getBlockState(worldIn);

                if (iblockstate1.getBlock() == this.block)
                {
                    ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack);
                    iblockstate1.getBlock().onBlockPlacedBy(playerIn, worldIn, iblockstate1, stack, itemstack);

                    if (stack instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                    }
                }

                SoundType soundtype = this.block.getSoundType();
                playerIn.playSound(stack, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.func_190918_g(1);
                return EnumActionResult.SUCCESS;
            }
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}
