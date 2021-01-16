package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemEnderPearl extends Item
{
    public ItemEnderPearl()
    {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);

        if (!worldIn.capabilities.isCreativeMode)
        {
            itemstack.func_190918_g(1);
        }

        itemStackIn.playSound((EntityPlayer)null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        worldIn.getCooldownTracker().setCooldown(this, 20);

        if (!itemStackIn.isRemote)
        {
            EntityEnderPearl entityenderpearl = new EntityEnderPearl(itemStackIn, worldIn);
            entityenderpearl.setHeadingFromThrower(worldIn, worldIn.rotationPitch, worldIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            itemStackIn.spawnEntityInWorld(entityenderpearl);
        }

        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
