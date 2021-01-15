package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable
{
    private static final String __OBFID = "CL_00001725";

    public EntityEnderPearl(World worldIn, EntityLivingBase p_i1783_2_)
    {
        super(worldIn, p_i1783_2_);
    }

    public EntityEnderPearl(World worldIn, double p_i1784_2_, double p_i1784_4_, double p_i1784_6_)
    {
        super(worldIn, p_i1784_2_, p_i1784_4_, p_i1784_6_);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        EntityLivingBase var2 = this.getThrower();

        if (p_70184_1_.entityHit != null)
        {
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, var2), 0.0F);
        }

        for (int var3 = 0; var3 < 32; ++var3)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            if (var2 instanceof EntityPlayerMP)
            {
                EntityPlayerMP var5 = (EntityPlayerMP)var2;

                if (var5.playerNetServerHandler.getNetworkManager().isChannelOpen() && var5.worldObj == this.worldObj && !var5.isPlayerSleeping())
                {
                    if (this.rand.nextFloat() < 0.05F && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobSpawning"))
                    {
                        EntityEndermite var4 = new EntityEndermite(this.worldObj);
                        var4.setSpawnedByPlayer(true);
                        var4.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
                        this.worldObj.spawnEntityInWorld(var4);
                    }

                    if (var2.isRiding())
                    {
                        var2.mountEntity((Entity)null);
                    }

                    var2.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    var2.fallDistance = 0.0F;
                    var2.attackEntityFrom(DamageSource.fall, 5.0F);
                }
            }

            this.setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        EntityLivingBase var1 = this.getThrower();

        if (var1 != null && var1 instanceof EntityPlayer && !var1.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }
}
