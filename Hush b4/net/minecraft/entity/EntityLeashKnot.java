// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.block.BlockFence;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging
{
    public EntityLeashKnot(final World worldIn) {
        super(worldIn);
    }
    
    public EntityLeashKnot(final World worldIn, final BlockPos hangingPositionIn) {
        super(worldIn, hangingPositionIn);
        this.setPosition(hangingPositionIn.getX() + 0.5, hangingPositionIn.getY() + 0.5, hangingPositionIn.getZ() + 0.5);
        final float f = 0.125f;
        final float f2 = 0.1875f;
        final float f3 = 0.25f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    public void updateFacingWithBoundingBox(final EnumFacing facingDirectionIn) {
    }
    
    @Override
    public int getWidthPixels() {
        return 9;
    }
    
    @Override
    public int getHeightPixels() {
        return 9;
    }
    
    @Override
    public float getEyeHeight() {
        return -0.0625f;
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 1024.0;
    }
    
    @Override
    public void onBroken(final Entity brokenEntity) {
    }
    
    @Override
    public boolean writeToNBTOptional(final NBTTagCompound tagCompund) {
        return false;
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        final ItemStack itemstack = playerIn.getHeldItem();
        boolean flag = false;
        if (itemstack != null && itemstack.getItem() == Items.lead && !this.worldObj.isRemote) {
            final double d0 = 7.0;
            for (final EntityLiving entityliving : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0))) {
                if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn) {
                    entityliving.setLeashedToEntity(this, true);
                    flag = true;
                }
            }
        }
        if (!this.worldObj.isRemote && !flag) {
            this.setDead();
            if (playerIn.capabilities.isCreativeMode) {
                final double d2 = 7.0;
                for (final EntityLiving entityliving2 : this.worldObj.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, new AxisAlignedBB(this.posX - d2, this.posY - d2, this.posZ - d2, this.posX + d2, this.posY + d2, this.posZ + d2))) {
                    if (entityliving2.getLeashed() && entityliving2.getLeashedToEntity() == this) {
                        entityliving2.clearLeashed(true, false);
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onValidSurface() {
        return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }
    
    public static EntityLeashKnot createKnot(final World worldIn, final BlockPos fence) {
        final EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
        entityleashknot.forceSpawn = true;
        worldIn.spawnEntityInWorld(entityleashknot);
        return entityleashknot;
    }
    
    public static EntityLeashKnot getKnotForPosition(final World worldIn, final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        for (final EntityLeashKnot entityleashknot : worldIn.getEntitiesWithinAABB((Class<? extends EntityLeashKnot>)EntityLeashKnot.class, new AxisAlignedBB(i - 1.0, j - 1.0, k - 1.0, i + 1.0, j + 1.0, k + 1.0))) {
            if (entityleashknot.getHangingPosition().equals(pos)) {
                return entityleashknot;
            }
        }
        return null;
    }
}
