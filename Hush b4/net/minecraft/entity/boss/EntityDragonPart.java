// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss;

import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.Entity;

public class EntityDragonPart extends Entity
{
    public final IEntityMultiPart entityDragonObj;
    public final String partName;
    
    public EntityDragonPart(final IEntityMultiPart parent, final String partName, final float base, final float sizeHeight) {
        super(parent.getWorld());
        this.setSize(base, sizeHeight);
        this.entityDragonObj = parent;
        this.partName = partName;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.isEntityInvulnerable(source) && this.entityDragonObj.attackEntityFromPart(this, source, amount);
    }
    
    @Override
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn || this.entityDragonObj == entityIn;
    }
}
