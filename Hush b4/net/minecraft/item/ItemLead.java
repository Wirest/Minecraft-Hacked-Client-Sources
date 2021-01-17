// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemLead extends Item
{
    public ItemLead() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (!(block instanceof BlockFence)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        attachToFence(playerIn, worldIn, pos);
        return true;
    }
    
    public static boolean attachToFence(final EntityPlayer player, final World worldIn, final BlockPos fence) {
        EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        final double d0 = 7.0;
        final int i = fence.getX();
        final int j = fence.getY();
        final int k = fence.getZ();
        for (final EntityLiving entityliving : worldIn.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0))) {
            if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == player) {
                if (entityleashknot == null) {
                    entityleashknot = EntityLeashKnot.createKnot(worldIn, fence);
                }
                entityliving.setLeashedToEntity(entityleashknot, true);
                flag = true;
            }
        }
        return flag;
    }
}
