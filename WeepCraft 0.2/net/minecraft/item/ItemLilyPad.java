package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemLilyPad extends ItemColored
{
    public ItemLilyPad(Block block)
    {
        super(block, false);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, true);

        if (raytraceresult == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!itemStackIn.isBlockModifiable(worldIn, blockpos) || !worldIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }

                BlockPos blockpos1 = blockpos.up();
                IBlockState iblockstate = itemStackIn.getBlockState(blockpos);

                if (iblockstate.getMaterial() == Material.WATER && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0 && itemStackIn.isAirBlock(blockpos1))
                {
                    itemStackIn.setBlockState(blockpos1, Blocks.WATERLILY.getDefaultState(), 11);

                    if (worldIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)worldIn, blockpos1, itemstack);
                    }

                    if (!worldIn.capabilities.isCreativeMode)
                    {
                        itemstack.func_190918_g(1);
                    }

                    worldIn.addStat(StatList.getObjectUseStats(this));
                    itemStackIn.playSound(worldIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
}
