// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;

public class PathNavigateClimber extends PathNavigateGround
{
    private BlockPos targetPosition;
    
    public PathNavigateClimber(final EntityLiving entityLivingIn, final World worldIn) {
        super(entityLivingIn, worldIn);
    }
    
    @Override
    public PathEntity getPathToPos(final BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }
    
    @Override
    public PathEntity getPathToEntityLiving(final Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }
    
    @Override
    public boolean tryMoveToEntityLiving(final Entity entityIn, final double speedIn) {
        final PathEntity pathentity = this.getPathToEntityLiving(entityIn);
        if (pathentity != null) {
            return this.setPath(pathentity, speedIn);
        }
        this.targetPosition = new BlockPos(entityIn);
        this.speed = speedIn;
        return true;
    }
    
    @Override
    public void onUpdateNavigation() {
        if (!this.noPath()) {
            super.onUpdateNavigation();
        }
        else if (this.targetPosition != null) {
            final double d0 = this.theEntity.width * this.theEntity.width;
            if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.theEntity.posY <= this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ())) >= d0)) {
                this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
            }
            else {
                this.targetPosition = null;
            }
        }
    }
}
