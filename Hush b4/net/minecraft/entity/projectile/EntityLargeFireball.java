// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball
{
    public int explosionPower;
    
    public EntityLargeFireball(final World worldIn) {
        super(worldIn);
        this.explosionPower = 1;
    }
    
    public EntityLargeFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.explosionPower = 1;
    }
    
    public EntityLargeFireball(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.explosionPower = 1;
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition movingObject) {
        if (!this.worldObj.isRemote) {
            if (movingObject.entityHit != null) {
                movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0f);
                this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
            }
            final boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
            this.worldObj.newExplosion(null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionPower);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionPower = tagCompund.getInteger("ExplosionPower");
        }
    }
}
