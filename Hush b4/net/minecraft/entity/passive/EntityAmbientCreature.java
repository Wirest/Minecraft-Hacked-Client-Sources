// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals
{
    public EntityAmbientCreature(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean allowLeashing() {
        return false;
    }
    
    @Override
    protected boolean interact(final EntityPlayer player) {
        return false;
    }
}
