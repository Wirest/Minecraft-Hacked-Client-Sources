package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSeeds extends Item
{
    private final Block crops;

    /** BlockID of the block the seeds can be planted on. */
    private final Block soilBlockID;

    public ItemSeeds(Block crops, Block soil)
    {
        this.crops = crops;
        this.soilBlockID = soil;
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        ItemStack itemstack = stack.getHeldItem(pos);

        if (hand == EnumFacing.UP && stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack) && playerIn.getBlockState(worldIn).getBlock() == this.soilBlockID && playerIn.isAirBlock(worldIn.up()))
        {
            playerIn.setBlockState(worldIn.up(), this.crops.getDefaultState());

            if (stack instanceof EntityPlayerMP)
            {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn.up(), itemstack);
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
