package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemEgg extends Item
{
    public ItemEgg()
    {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);

        if (!worldIn.capabilities.isCreativeMode)
        {
            itemstack.func_190918_g(1);
        }

        itemStackIn.playSound((EntityPlayer)null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!itemStackIn.isRemote)
        {
            EntityEgg entityegg = new EntityEgg(itemStackIn, worldIn);
            entityegg.setHeadingFromThrower(worldIn, worldIn.rotationPitch, worldIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            itemStackIn.spawnEntityInWorld(entityegg);
        }

        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
