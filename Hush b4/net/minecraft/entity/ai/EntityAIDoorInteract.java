// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.block.BlockDoor;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLiving;

public abstract class EntityAIDoorInteract extends EntityAIBase
{
    protected EntityLiving theEntity;
    protected BlockPos doorPosition;
    protected BlockDoor doorBlock;
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;
    
    public EntityAIDoorInteract(final EntityLiving entityIn) {
        this.doorPosition = BlockPos.ORIGIN;
        this.theEntity = entityIn;
        if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theEntity.isCollidedHorizontally) {
            return false;
        }
        final PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
        final PathEntity pathentity = pathnavigateground.getPath();
        if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors()) {
            for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
                final PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
                this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
                if (this.theEntity.getDistanceSq(this.doorPosition.getX(), this.theEntity.posY, this.doorPosition.getZ()) <= 2.25) {
                    this.doorBlock = this.getBlockDoor(this.doorPosition);
                    if (this.doorBlock != null) {
                        return true;
                    }
                }
            }
            this.doorPosition = new BlockPos(this.theEntity).up();
            this.doorBlock = this.getBlockDoor(this.doorPosition);
            return this.doorBlock != null;
        }
        return false;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }
    
    @Override
    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)(this.doorPosition.getX() + 0.5f - this.theEntity.posX);
        this.entityPositionZ = (float)(this.doorPosition.getZ() + 0.5f - this.theEntity.posZ);
    }
    
    @Override
    public void updateTask() {
        final float f = (float)(this.doorPosition.getX() + 0.5f - this.theEntity.posX);
        final float f2 = (float)(this.doorPosition.getZ() + 0.5f - this.theEntity.posZ);
        final float f3 = this.entityPositionX * f + this.entityPositionZ * f2;
        if (f3 < 0.0f) {
            this.hasStoppedDoorInteraction = true;
        }
    }
    
    private BlockDoor getBlockDoor(final BlockPos pos) {
        final Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
        return (block instanceof BlockDoor && block.getMaterial() == Material.wood) ? ((BlockDoor)block) : null;
    }
}
