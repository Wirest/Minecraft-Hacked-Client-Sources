package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable {
    private static final String __OBFID = "CL_00001724";

    public EntityEgg(World worldIn) {
        super(worldIn);
    }

    public EntityEgg(World worldIn, EntityLivingBase p_i1780_2_) {
        super(worldIn, p_i1780_2_);
    }

    public EntityEgg(World worldIn, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) {
        super(worldIn, p_i1781_2_, p_i1781_4_, p_i1781_6_);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != null) {
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
            byte var2 = 1;

            if (this.rand.nextInt(32) == 0) {
                var2 = 4;
            }

            for (int var3 = 0; var3 < var2; ++var3) {
                EntityChicken var4 = new EntityChicken(this.worldObj);
                var4.setGrowingAge(-24000);
                var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(var4);
            }
        }

        double var5 = 0.08D;

        for (int var6 = 0; var6 < 8; ++var6) {
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, new int[]{Item.getIdFromItem(Items.egg)});
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
