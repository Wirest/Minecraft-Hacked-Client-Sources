// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.util.BlockPos;

public class GoRandomAI
{
    private RandomPathFinder pathFinder;
    private PathProcessor processor;
    private boolean done;
    
    public GoRandomAI(final BlockPos start, final float range) {
        (this.pathFinder = new RandomPathFinder(start, range)).setThinkTime(10);
        this.pathFinder.setFallingAllowed(false);
    }
    
    public void update() {
        this.done = false;
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
            this.pathFinder = new RandomPathFinder(this.pathFinder);
            return;
        }
        if (!this.processor.isFailed() && !this.processor.isDone()) {
            this.processor.process();
        }
        else {
            this.pathFinder = new RandomPathFinder(this.pathFinder);
        }
        if (this.processor.isDone()) {
            this.done = true;
        }
    }
    
    public final boolean isDone() {
        return this.done;
    }
    
    public void stop() {
        if (this.processor != null) {
            this.processor.stop();
        }
    }
}
