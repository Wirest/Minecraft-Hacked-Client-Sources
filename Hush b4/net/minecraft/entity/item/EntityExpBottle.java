// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityThrowable;

public class EntityExpBottle extends EntityThrowable
{
    public EntityExpBottle(final World worldIn) {
        super(worldIn);
    }
    
    public EntityExpBottle(final World worldIn, final EntityLivingBase p_i1786_2_) {
        super(worldIn, p_i1786_2_);
    }
    
    public EntityExpBottle(final World worldIn, final double p_i1787_2_, final double p_i1787_4_, final double p_i1787_6_) {
        super(worldIn, p_i1787_2_, p_i1787_4_, p_i1787_6_);
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.07f;
    }
    
    @Override
    protected float getVelocity() {
        return 0.7f;
    }
    
    @Override
    protected float getInaccuracy() {
        return -20.0f;
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            this.worldObj.playAuxSFX(2002, new BlockPos(this), 0);
            int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
            while (i > 0) {
                final int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }
            this.setDead();
        }
    }
}
