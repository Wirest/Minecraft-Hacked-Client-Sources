// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball
{
    public EntityWitherSkull(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntityWitherSkull(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73f : super.getMotionFactor();
    }
    
    public EntityWitherSkull(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    public boolean isBurning() {
        return false;
    }
    
    @Override
    public float getExplosionResistance(final Explosion explosionIn, final World worldIn, final BlockPos pos, final IBlockState blockStateIn) {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        final Block block = blockStateIn.getBlock();
        if (this.isInvulnerable() && EntityWither.func_181033_a(block)) {
            f = Math.min(0.8f, f);
        }
        return f;
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition movingObject) {
        if (!this.worldObj.isRemote) {
            if (movingObject.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (movingObject.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0f)) {
                        if (!movingObject.entityHit.isEntityAlive()) {
                            this.shootingEntity.heal(5.0f);
                        }
                        else {
                            this.applyEnchantments(this.shootingEntity, movingObject.entityHit);
                        }
                    }
                }
                else {
                    movingObject.entityHit.attackEntityFrom(DamageSource.magic, 5.0f);
                }
                if (movingObject.entityHit instanceof EntityLivingBase) {
                    int i = 0;
                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                        i = 10;
                    }
                    else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        i = 40;
                    }
                    if (i > 0) {
                        ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * i, 1));
                    }
                }
            }
            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0f, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
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
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(10, (Byte)0);
    }
    
    public boolean isInvulnerable() {
        return this.dataWatcher.getWatchableObjectByte(10) == 1;
    }
    
    public void setInvulnerable(final boolean invulnerable) {
        this.dataWatcher.updateObject(10, (byte)(invulnerable ? 1 : 0));
    }
}
