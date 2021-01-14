package net.minecraft.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityExpBottle extends EntityThrowable {
    private static final String __OBFID = "CL_00001726";

    public EntityExpBottle(World worldIn) {
        super(worldIn);
    }

    public EntityExpBottle(World worldIn, EntityLivingBase p_i1786_2_) {
        super(worldIn, p_i1786_2_);
    }

    public EntityExpBottle(World worldIn, double p_i1787_2_, double p_i1787_4_, double p_i1787_6_) {
        super(worldIn, p_i1787_2_, p_i1787_4_, p_i1787_6_);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity() {
        return 0.07F;
    }

    protected float func_70182_d() {
        return 0.7F;
    }

    protected float func_70183_g() {
        return -20.0F;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            this.worldObj.playAuxSFX(2002, new BlockPos(this), 0);
            int var2 = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);

            while (var2 > 0) {
                int var3 = EntityXPOrb.getXPSplit(var2);
                var2 -= var3;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var3));
            }

            this.setDead();
        }
    }
}
