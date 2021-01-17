// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.entity.EntityLiving;

public class EntitySenses
{
    EntityLiving entityObj;
    List<Entity> seenEntities;
    List<Entity> unseenEntities;
    
    public EntitySenses(final EntityLiving entityObjIn) {
        this.seenEntities = (List<Entity>)Lists.newArrayList();
        this.unseenEntities = (List<Entity>)Lists.newArrayList();
        this.entityObj = entityObjIn;
    }
    
    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }
    
    public boolean canSee(final Entity entityIn) {
        if (this.seenEntities.contains(entityIn)) {
            return true;
        }
        if (this.unseenEntities.contains(entityIn)) {
            return false;
        }
        this.entityObj.worldObj.theProfiler.startSection("canSee");
        final boolean flag = this.entityObj.canEntityBeSeen(entityIn);
        this.entityObj.worldObj.theProfiler.endSection();
        if (flag) {
            this.seenEntities.add(entityIn);
        }
        else {
            this.unseenEntities.add(entityIn);
        }
        return flag;
    }
}
