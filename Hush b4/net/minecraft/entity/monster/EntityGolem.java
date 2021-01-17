// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.world.World;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.EntityCreature;

public abstract class EntityGolem extends EntityCreature implements IAnimals
{
    public EntityGolem(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected String getLivingSound() {
        return "none";
    }
    
    @Override
    protected String getHurtSound() {
        return "none";
    }
    
    @Override
    protected String getDeathSound() {
        return "none";
    }
    
    @Override
    public int getTalkInterval() {
        return 120;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
}
