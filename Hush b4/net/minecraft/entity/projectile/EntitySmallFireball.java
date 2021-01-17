// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{
    public EntitySmallFireball(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntitySmallFireball(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntitySmallFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition movingObject) {
        if (!this.worldObj.isRemote) {
            if (movingObject.entityHit != null) {
                final boolean flag = movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0f);
                if (flag) {
                    this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
                    if (!movingObject.entityHit.isImmuneToFire()) {
                        movingObject.entityHit.setFire(5);
                    }
                }
            }
            else {
                boolean flag2 = true;
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
                    flag2 = this.worldObj.getGameRules().getBoolean("mobGriefing");
                }
                if (flag2) {
                    final BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
                    if (this.worldObj.isAirBlock(blockpos)) {
                        this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
                    }
                }
            }
            this.setDead();
        }
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
}
