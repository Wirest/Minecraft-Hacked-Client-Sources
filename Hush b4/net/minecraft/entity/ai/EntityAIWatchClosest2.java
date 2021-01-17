// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityAIWatchClosest2 extends EntityAIWatchClosest
{
    public EntityAIWatchClosest2(final EntityLiving entitylivingIn, final Class<? extends Entity> watchTargetClass, final float maxDistance, final float chanceIn) {
        super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
        this.setMutexBits(3);
    }
}
