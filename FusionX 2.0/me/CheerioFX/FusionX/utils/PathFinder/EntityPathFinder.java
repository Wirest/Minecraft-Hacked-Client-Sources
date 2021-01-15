// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;

public class EntityPathFinder extends PathFinder
{
    private final Entity entity;
    private final float distanceSq;
    
    public EntityPathFinder(final Entity entity, final float distance) {
        super(new BlockPos(entity));
        this.entity = entity;
        this.distanceSq = (float)Math.pow(distance, 2.0);
    }
    
    public EntityPathFinder(final EntityPathFinder pathFinder) {
        super(new BlockPos(pathFinder.entity));
        this.thinkSpeed = pathFinder.thinkSpeed;
        this.thinkTime = pathFinder.thinkTime;
        this.entity = pathFinder.entity;
        this.distanceSq = pathFinder.distanceSq;
    }
    
    @Override
    protected boolean checkDone() {
        return this.done = (this.entity.func_174831_c(this.current) <= this.distanceSq);
    }
    
    @Override
    public boolean isPathStillValid(final int index) {
        return super.isPathStillValid(index) && this.getGoal().equals(new BlockPos(this.entity));
    }
}
