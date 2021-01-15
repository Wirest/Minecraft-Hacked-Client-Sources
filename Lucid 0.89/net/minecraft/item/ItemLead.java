package net.minecraft.item;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLead extends Item
{

    public ItemLead()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        Block var9 = worldIn.getBlockState(pos).getBlock();

        if (var9 instanceof BlockFence)
        {
            if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                attachToFence(playerIn, worldIn, pos);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence)
    {
        EntityLeashKnot var3 = EntityLeashKnot.getKnotForPosition(worldIn, fence);
        boolean var4 = false;
        double var5 = 7.0D;
        int var7 = fence.getX();
        int var8 = fence.getY();
        int var9 = fence.getZ();
        List var10 = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(var7 - var5, var8 - var5, var9 - var5, var7 + var5, var8 + var5, var9 + var5));
        Iterator var11 = var10.iterator();

        while (var11.hasNext())
        {
            EntityLiving var12 = (EntityLiving)var11.next();

            if (var12.getLeashed() && var12.getLeashedToEntity() == player)
            {
                if (var3 == null)
                {
                    var3 = EntityLeashKnot.createKnot(worldIn, fence);
                }

                var12.setLeashedToEntity(var3, true);
                var4 = true;
            }
        }

        return var4;
    }
}
