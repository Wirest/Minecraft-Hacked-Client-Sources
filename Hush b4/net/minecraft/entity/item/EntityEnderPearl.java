// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;

public class EntityEnderPearl extends EntityThrowable
{
    private EntityLivingBase field_181555_c;
    
    public EntityEnderPearl(final World p_i46455_1_) {
        super(p_i46455_1_);
    }
    
    public EntityEnderPearl(final World worldIn, final EntityLivingBase p_i1783_2_) {
        super(worldIn, p_i1783_2_);
        this.field_181555_c = p_i1783_2_;
    }
    
    public EntityEnderPearl(final World worldIn, final double p_i1784_2_, final double p_i1784_4_, final double p_i1784_6_) {
        super(worldIn, p_i1784_2_, p_i1784_4_, p_i1784_6_);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        final EntityLivingBase entitylivingbase = this.getThrower();
        if (p_70184_1_.entityHit != null) {
            if (p_70184_1_.entityHit == this.field_181555_c) {
                return;
            }
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0f);
        }
        for (int i = 0; i < 32; ++i) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
        }
        if (!this.worldObj.isRemote) {
            if (entitylivingbase instanceof EntityPlayerMP) {
                final EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;
                if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping()) {
                    if (this.rand.nextFloat() < 0.05f && this.worldObj.getGameRules().getBoolean("doMobSpawning")) {
                        final EntityEndermite entityendermite = new EntityEndermite(this.worldObj);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                        this.worldObj.spawnEntityInWorld(entityendermite);
                    }
                    if (entitylivingbase.isRiding()) {
                        entitylivingbase.mountEntity(null);
                    }
                    entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entitylivingbase.fallDistance = 0.0f;
                    entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0f);
                }
            }
            else if (entitylivingbase != null) {
                entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entitylivingbase.fallDistance = 0.0f;
            }
            this.setDead();
        }
    }
    
    @Override
    public void onUpdate() {
        final EntityLivingBase entitylivingbase = this.getThrower();
        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive()) {
            this.setDead();
        }
        else {
            super.onUpdate();
        }
    }
}
