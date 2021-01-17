// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart
{
    public EntityMinecartEmpty(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartEmpty(final World worldIn, final double p_i1723_2_, final double p_i1723_4_, final double p_i1723_6_) {
        super(worldIn, p_i1723_2_, p_i1723_4_, p_i1723_6_);
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        }
        if (this.riddenByEntity != null && this.riddenByEntity != playerIn) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
        }
        return true;
    }
    
    @Override
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
        if (receivingPower) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(null);
            }
            if (this.getRollingAmplitude() == 0) {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0f);
                this.setBeenAttacked();
            }
        }
    }
    
    @Override
    public EnumMinecartType getMinecartType() {
        return EnumMinecartType.RIDEABLE;
    }
}
