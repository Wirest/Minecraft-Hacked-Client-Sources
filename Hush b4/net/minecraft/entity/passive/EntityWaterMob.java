// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class EntityWaterMob extends EntityLiving implements IAnimals
{
    public EntityWaterMob(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return true;
    }
    
    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }
    
    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return true;
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return 1 + this.worldObj.rand.nextInt(3);
    }
    
    @Override
    public void onEntityUpdate() {
        int i = this.getAir();
        super.onEntityUpdate();
        if (this.isEntityAlive() && !this.isInWater()) {
            --i;
            this.setAir(i);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2.0f);
            }
        }
        else {
            this.setAir(300);
        }
    }
    
    @Override
    public boolean isPushedByWater() {
        return false;
    }
}
