// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public class EntityPotion extends EntityThrowable
{
    private ItemStack potionDamage;
    
    public EntityPotion(final World worldIn) {
        super(worldIn);
    }
    
    public EntityPotion(final World worldIn, final EntityLivingBase throwerIn, final int meta) {
        this(worldIn, throwerIn, new ItemStack(Items.potionitem, 1, meta));
    }
    
    public EntityPotion(final World worldIn, final EntityLivingBase throwerIn, final ItemStack potionDamageIn) {
        super(worldIn, throwerIn);
        this.potionDamage = potionDamageIn;
    }
    
    public EntityPotion(final World worldIn, final double x, final double y, final double z, final int p_i1791_8_) {
        this(worldIn, x, y, z, new ItemStack(Items.potionitem, 1, p_i1791_8_));
    }
    
    public EntityPotion(final World worldIn, final double x, final double y, final double z, final ItemStack potionDamageIn) {
        super(worldIn, x, y, z);
        this.potionDamage = potionDamageIn;
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }
    
    @Override
    protected float getVelocity() {
        return 0.5f;
    }
    
    @Override
    protected float getInaccuracy() {
        return -20.0f;
    }
    
    public void setPotionDamage(final int potionId) {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        this.potionDamage.setItemDamage(potionId);
    }
    
    public int getPotionDamage() {
        if (this.potionDamage == null) {
            this.potionDamage = new ItemStack(Items.potionitem, 1, 0);
        }
        return this.potionDamage.getMetadata();
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        if (!this.worldObj.isRemote) {
            final List<PotionEffect> list = Items.potionitem.getEffects(this.potionDamage);
            if (list != null && !list.isEmpty()) {
                final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().expand(4.0, 2.0, 4.0);
                final List<EntityLivingBase> list2 = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb);
                if (!list2.isEmpty()) {
                    for (final EntityLivingBase entitylivingbase : list2) {
                        final double d0 = this.getDistanceSqToEntity(entitylivingbase);
                        if (d0 < 16.0) {
                            double d2 = 1.0 - Math.sqrt(d0) / 4.0;
                            if (entitylivingbase == p_70184_1_.entityHit) {
                                d2 = 1.0;
                            }
                            for (final PotionEffect potioneffect : list) {
                                final int i = potioneffect.getPotionID();
                                if (Potion.potionTypes[i].isInstant()) {
                                    Potion.potionTypes[i].affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d2);
                                }
                                else {
                                    final int j = (int)(d2 * potioneffect.getDuration() + 0.5);
                                    if (j <= 20) {
                                        continue;
                                    }
                                    entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
                                }
                            }
                        }
                    }
                }
            }
            this.worldObj.playAuxSFX(2002, new BlockPos(this), this.getPotionDamage());
            this.setDead();
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("Potion", 10)) {
            this.potionDamage = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
        }
        else {
            this.setPotionDamage(tagCompund.getInteger("potionValue"));
        }
        if (this.potionDamage == null) {
            this.setDead();
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.potionDamage != null) {
            tagCompound.setTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
        }
    }
}
