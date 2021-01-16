package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemLingeringPotion extends ItemPotion
{
    public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("lingering_potion.effect."));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
        PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        ItemStack itemstack1 = worldIn.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);
        itemStackIn.playSound((EntityPlayer)null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_LINGERINGPOTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!itemStackIn.isRemote)
        {
            EntityPotion entitypotion = new EntityPotion(itemStackIn, worldIn, itemstack1);
            entitypotion.setHeadingFromThrower(worldIn, worldIn.rotationPitch, worldIn.rotationYaw, -20.0F, 0.5F, 1.0F);
            itemStackIn.spawnEntityInWorld(entitypotion);
        }

        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
