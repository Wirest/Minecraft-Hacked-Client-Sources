package net.minecraft.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item
{
    public ItemGlassBottle()
    {
        this.setCreativeTab(CreativeTabs.BREWING);
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        List<EntityAreaEffectCloud> list = itemStackIn.<EntityAreaEffectCloud>getEntitiesWithinAABB(EntityAreaEffectCloud.class, worldIn.getEntityBoundingBox().expandXyz(2.0D), new Predicate<EntityAreaEffectCloud>()
        {
            public boolean apply(@Nullable EntityAreaEffectCloud p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && p_apply_1_.getOwner() instanceof EntityDragon;
            }
        });
        ItemStack itemstack = worldIn.getHeldItem(playerIn);

        if (!list.isEmpty())
        {
            EntityAreaEffectCloud entityareaeffectcloud = list.get(0);
            entityareaeffectcloud.setRadius(entityareaeffectcloud.getRadius() - 0.5F);
            itemStackIn.playSound((EntityPlayer)null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, worldIn, new ItemStack(Items.DRAGON_BREATH)));
        }
        else
        {
            RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, true);

            if (raytraceresult == null)
            {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
            else
            {
                if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = raytraceresult.getBlockPos();

                    if (!itemStackIn.isBlockModifiable(worldIn, blockpos) || !worldIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                    {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    }

                    if (itemStackIn.getBlockState(blockpos).getMaterial() == Material.WATER)
                    {
                        itemStackIn.playSound(worldIn, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, worldIn, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)));
                    }
                }

                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack p_185061_1_, EntityPlayer player, ItemStack stack)
    {
        p_185061_1_.func_190918_g(1);
        player.addStat(StatList.getObjectUseStats(this));

        if (p_185061_1_.func_190926_b())
        {
            return stack;
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(stack))
            {
                player.dropItem(stack, false);
            }

            return p_185061_1_;
        }
    }
}
