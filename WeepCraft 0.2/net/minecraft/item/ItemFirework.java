package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFirework extends Item
{
    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        if (!playerIn.isRemote)
        {
            ItemStack itemstack = stack.getHeldItem(pos);
            EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(playerIn, (double)((float)worldIn.getX() + facing), (double)((float)worldIn.getY() + hitX), (double)((float)worldIn.getZ() + hitY), itemstack);
            playerIn.spawnEntityInWorld(entityfireworkrocket);

            if (!stack.capabilities.isCreativeMode)
            {
                itemstack.func_190918_g(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn)
    {
        if (worldIn.isElytraFlying())
        {
            ItemStack itemstack = worldIn.getHeldItem(playerIn);

            if (!itemStackIn.isRemote)
            {
                EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(itemStackIn, itemstack, worldIn);
                itemStackIn.spawnEntityInWorld(entityfireworkrocket);

                if (!worldIn.capabilities.isCreativeMode)
                {
                    itemstack.func_190918_g(1);
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, worldIn.getHeldItem(playerIn));
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, worldIn.getHeldItem(playerIn));
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("Fireworks");

        if (nbttagcompound != null)
        {
            if (nbttagcompound.hasKey("Flight", 99))
            {
                tooltip.add(I18n.translateToLocal("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);

            if (!nbttaglist.hasNoTags())
            {
                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                    List<String> list = Lists.<String>newArrayList();
                    ItemFireworkCharge.addExplosionInfo(nbttagcompound1, list);

                    if (!list.isEmpty())
                    {
                        for (int j = 1; j < list.size(); ++j)
                        {
                            list.set(j, "  " + (String)list.get(j));
                        }

                        tooltip.addAll(list);
                    }
                }
            }
        }
    }
}
