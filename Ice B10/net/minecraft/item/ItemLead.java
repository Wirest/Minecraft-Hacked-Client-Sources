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
    private static final String __OBFID = "CL_00000045";

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
                func_180618_a(playerIn, worldIn, pos);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean func_180618_a(EntityPlayer p_180618_0_, World worldIn, BlockPos p_180618_2_)
    {
        EntityLeashKnot var3 = EntityLeashKnot.func_174863_b(worldIn, p_180618_2_);
        boolean var4 = false;
        double var5 = 7.0D;
        int var7 = p_180618_2_.getX();
        int var8 = p_180618_2_.getY();
        int var9 = p_180618_2_.getZ();
        List var10 = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)var7 - var5, (double)var8 - var5, (double)var9 - var5, (double)var7 + var5, (double)var8 + var5, (double)var9 + var5));
        Iterator var11 = var10.iterator();

        while (var11.hasNext())
        {
            EntityLiving var12 = (EntityLiving)var11.next();

            if (var12.getLeashed() && var12.getLeashedToEntity() == p_180618_0_)
            {
                if (var3 == null)
                {
                    var3 = EntityLeashKnot.func_174862_a(worldIn, p_180618_2_);
                }

                var12.setLeashedToEntity(var3, true);
                var4 = true;
            }
        }

        return var4;
    }
}
