package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable {
    private static final String __OBFID = "CL_00001722";

    public EntitySnowball(World worldIn) {
        super(worldIn);
    }

    public EntitySnowball(World worldIn, EntityLivingBase p_i1774_2_) {
        super(worldIn, p_i1774_2_);
    }

    public EntitySnowball(World worldIn, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(worldIn, p_i1775_2_, p_i1775_4_, p_i1775_6_);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != null) {
            byte var2 = 0;

            if (p_70184_1_.entityHit instanceof EntityBlaze) {
                var2 = 3;
            }

            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) var2);
        }

        for (int var3 = 0; var3 < 8; ++var3) {
            this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
