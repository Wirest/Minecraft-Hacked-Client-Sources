// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable
{
    public EntityEgg(final World worldIn) {
        super(worldIn);
    }
    
    public EntityEgg(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    public EntityEgg(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != null) {
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0f);
        }
        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
            int i = 1;
            if (this.rand.nextInt(32) == 0) {
                i = 4;
            }
            for (int j = 0; j < i; ++j) {
                final EntityChicken entitychicken = new EntityChicken(this.worldObj);
                entitychicken.setGrowingAge(-24000);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                this.worldObj.spawnEntityInWorld(entitychicken);
            }
        }
        final double d0 = 0.08;
        for (int k = 0; k < 8; ++k) {
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand.nextFloat() - 0.5) * 0.08, (this.rand.nextFloat() - 0.5) * 0.08, (this.rand.nextFloat() - 0.5) * 0.08, Item.getIdFromItem(Items.egg));
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }
}
