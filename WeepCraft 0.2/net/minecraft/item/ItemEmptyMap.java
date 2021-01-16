package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEmptyMap extends ItemMapBase
{
    protected ItemEmptyMap()
    {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        ItemStack itemstack = ItemMap.func_190906_a(itemStackIn, worldIn.posX, worldIn.posZ, (byte)0, true, false);
        ItemStack itemstack1 = worldIn.getHeldItem(playerIn);
        itemstack1.func_190918_g(1);

        if (itemstack1.func_190926_b())
        {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            if (!worldIn.inventory.addItemStackToInventory(itemstack.copy()))
            {
                worldIn.dropItem(itemstack, false);
            }

            worldIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack1);
        }
    }
}
