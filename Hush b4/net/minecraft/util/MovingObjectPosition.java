// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.entity.Entity;

public class MovingObjectPosition
{
    private BlockPos blockPos;
    public MovingObjectType typeOfHit;
    public EnumFacing sideHit;
    public Vec3 hitVec;
    public Entity entityHit;
    
    public MovingObjectPosition(final Vec3 hitVecIn, final EnumFacing facing, final BlockPos blockPosIn) {
        this(MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
    }
    
    public MovingObjectPosition(final Vec3 p_i45552_1_, final EnumFacing facing) {
        this(MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
    }
    
    public MovingObjectPosition(final Entity p_i2304_1_) {
        this(p_i2304_1_, new Vec3(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
    }
    
    public MovingObjectPosition(final MovingObjectType typeOfHitIn, final Vec3 hitVecIn, final EnumFacing sideHitIn, final BlockPos blockPosIn) {
        this.typeOfHit = typeOfHitIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }
    
    public MovingObjectPosition(final Entity entityHitIn, final Vec3 hitVecIn) {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.entityHit = entityHitIn;
        this.hitVec = hitVecIn;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    @Override
    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }
    
    public enum MovingObjectType
    {
        MISS("MISS", 0), 
        BLOCK("BLOCK", 1), 
        ENTITY("ENTITY", 2);
        
        private MovingObjectType(final String name, final int ordinal) {
        }
    }
}
