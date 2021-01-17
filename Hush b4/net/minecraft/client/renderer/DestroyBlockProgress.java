// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;

public class DestroyBlockProgress
{
    private final int miningPlayerEntId;
    private final BlockPos position;
    private int partialBlockProgress;
    private int createdAtCloudUpdateTick;
    
    public DestroyBlockProgress(final int miningPlayerEntIdIn, final BlockPos positionIn) {
        this.miningPlayerEntId = miningPlayerEntIdIn;
        this.position = positionIn;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setPartialBlockDamage(int damage) {
        if (damage > 10) {
            damage = 10;
        }
        this.partialBlockProgress = damage;
    }
    
    public int getPartialBlockDamage() {
        return this.partialBlockProgress;
    }
    
    public void setCloudUpdateTick(final int createdAtCloudUpdateTickIn) {
        this.createdAtCloudUpdateTick = createdAtCloudUpdateTickIn;
    }
    
    public int getCreationCloudUpdateTick() {
        return this.createdAtCloudUpdateTick;
    }
}
