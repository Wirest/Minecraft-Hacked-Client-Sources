// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityCreature;

public abstract class EntityAIMoveToBlock extends EntityAIBase
{
    private final EntityCreature theEntity;
    private final double movementSpeed;
    protected int runDelay;
    private int timeoutCounter;
    private int field_179490_f;
    protected BlockPos destinationBlock;
    private boolean isAboveDestination;
    private int searchLength;
    
    public EntityAIMoveToBlock(final EntityCreature creature, final double speedIn, final int length) {
        this.destinationBlock = BlockPos.ORIGIN;
        this.theEntity = creature;
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        }
        this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
        return this.searchForDestination();
    }
    
    @Override
    public boolean continueExecuting() {
        return this.timeoutCounter >= -this.field_179490_f && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.theEntity.worldObj, this.destinationBlock);
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ((float)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (float)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
        this.timeoutCounter = 0;
        this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
    }
    
    @Override
    public void resetTask() {
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 == 0) {
                this.theEntity.getNavigator().tryMoveToXYZ((float)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (float)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
            }
        }
        else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }
    
    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }
    
    private boolean searchForDestination() {
        final int i = this.searchLength;
        final int j = 1;
        final BlockPos blockpos = new BlockPos(this.theEntity);
        for (int k = 0; k <= 1; k = ((k > 0) ? (-k) : (1 - k))) {
            for (int l = 0; l < i; ++l) {
                for (int i2 = 0; i2 <= l; i2 = ((i2 > 0) ? (-i2) : (1 - i2))) {
                    for (int j2 = (i2 < l && i2 > -l) ? l : 0; j2 <= l; j2 = ((j2 > 0) ? (-j2) : (1 - j2))) {
                        final BlockPos blockpos2 = blockpos.add(i2, k - 1, j2);
                        if (this.theEntity.isWithinHomeDistanceFromPosition(blockpos2) && this.shouldMoveTo(this.theEntity.worldObj, blockpos2)) {
                            this.destinationBlock = blockpos2;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    protected abstract boolean shouldMoveTo(final World p0, final BlockPos p1);
}
