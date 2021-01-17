// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

public abstract class EntityAIBase
{
    private int mutexBits;
    
    public abstract boolean shouldExecute();
    
    public boolean continueExecuting() {
        return this.shouldExecute();
    }
    
    public boolean isInterruptible() {
        return true;
    }
    
    public void startExecuting() {
    }
    
    public void resetTask() {
    }
    
    public void updateTask() {
    }
    
    public void setMutexBits(final int mutexBitsIn) {
        this.mutexBits = mutexBitsIn;
    }
    
    public int getMutexBits() {
        return this.mutexBits;
    }
}
