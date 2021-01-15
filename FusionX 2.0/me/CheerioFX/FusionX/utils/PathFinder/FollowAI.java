// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.entity.Entity;

public class FollowAI
{
    private EntityPathFinder pathFinder;
    private PathProcessor processor;
    private boolean done;
    private boolean failed;
    
    public FollowAI(final Entity entity, final float distance) {
        (this.pathFinder = new EntityPathFinder(entity, distance)).setThinkTime(40);
    }
    
    public void update() {
        if (!this.pathFinder.isDone() && !this.pathFinder.isFailed()) {
            if (this.processor != null) {
                this.processor.lockControls();
            }
            this.pathFinder.think();
            if (!this.pathFinder.isDone() && !this.pathFinder.isFailed()) {
                return;
            }
            this.pathFinder.formatPath();
            this.processor = this.pathFinder.getProcessor();
        }
        if (this.processor != null && !this.pathFinder.isPathStillValid(this.processor.getIndex())) {
            this.pathFinder = new EntityPathFinder(this.pathFinder);
            return;
        }
        if (!this.processor.isFailed() && !this.processor.isDone()) {
            this.processor.process();
        }
        else {
            this.processor.lockControls();
            if (this.processor.isFailed()) {
                this.failed = true;
            }
            else if (this.processor.isDone()) {
                this.done = true;
            }
        }
    }
    
    public void stop() {
        if (this.processor != null) {
            this.processor.stop();
        }
    }
    
    public final boolean isDone() {
        return this.done;
    }
    
    public final boolean isFailed() {
        return this.failed;
    }
}
