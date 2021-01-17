// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import java.util.Iterator;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ChunkCache;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.Vec3;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class PathNavigate
{
    protected EntityLiving theEntity;
    protected World worldObj;
    protected PathEntity currentPath;
    protected double speed;
    private final IAttributeInstance pathSearchRange;
    private int totalTicks;
    private int ticksAtLastPos;
    private Vec3 lastPosCheck;
    private float heightRequirement;
    private final PathFinder pathFinder;
    
    public PathNavigate(final EntityLiving entitylivingIn, final World worldIn) {
        this.lastPosCheck = new Vec3(0.0, 0.0, 0.0);
        this.heightRequirement = 1.0f;
        this.theEntity = entitylivingIn;
        this.worldObj = worldIn;
        this.pathSearchRange = entitylivingIn.getEntityAttribute(SharedMonsterAttributes.followRange);
        this.pathFinder = this.getPathFinder();
    }
    
    protected abstract PathFinder getPathFinder();
    
    public void setSpeed(final double speedIn) {
        this.speed = speedIn;
    }
    
    public float getPathSearchRange() {
        return (float)this.pathSearchRange.getAttributeValue();
    }
    
    public final PathEntity getPathToXYZ(final double x, final double y, final double z) {
        return this.getPathToPos(new BlockPos(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z)));
    }
    
    public PathEntity getPathToPos(final BlockPos pos) {
        if (!this.canNavigate()) {
            return null;
        }
        final float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos blockpos = new BlockPos(this.theEntity);
        final int i = (int)(f + 8.0f);
        final ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
        final PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, pos, f);
        this.worldObj.theProfiler.endSection();
        return pathentity;
    }
    
    public boolean tryMoveToXYZ(final double x, final double y, final double z, final double speedIn) {
        final PathEntity pathentity = this.getPathToXYZ(MathHelper.floor_double(x), (int)y, MathHelper.floor_double(z));
        return this.setPath(pathentity, speedIn);
    }
    
    public void setHeightRequirement(final float jumpHeight) {
        this.heightRequirement = jumpHeight;
    }
    
    public PathEntity getPathToEntityLiving(final Entity entityIn) {
        if (!this.canNavigate()) {
            return null;
        }
        final float f = this.getPathSearchRange();
        this.worldObj.theProfiler.startSection("pathfind");
        final BlockPos blockpos = new BlockPos(this.theEntity).up();
        final int i = (int)(f + 16.0f);
        final ChunkCache chunkcache = new ChunkCache(this.worldObj, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
        final PathEntity pathentity = this.pathFinder.createEntityPathTo(chunkcache, this.theEntity, entityIn, f);
        this.worldObj.theProfiler.endSection();
        return pathentity;
    }
    
    public boolean tryMoveToEntityLiving(final Entity entityIn, final double speedIn) {
        final PathEntity pathentity = this.getPathToEntityLiving(entityIn);
        return pathentity != null && this.setPath(pathentity, speedIn);
    }
    
    public boolean setPath(final PathEntity pathentityIn, final double speedIn) {
        if (pathentityIn == null) {
            this.currentPath = null;
            return false;
        }
        if (!pathentityIn.isSamePath(this.currentPath)) {
            this.currentPath = pathentityIn;
        }
        this.removeSunnyPath();
        if (this.currentPath.getCurrentPathLength() == 0) {
            return false;
        }
        this.speed = speedIn;
        final Vec3 vec3 = this.getEntityPosition();
        this.ticksAtLastPos = this.totalTicks;
        this.lastPosCheck = vec3;
        return true;
    }
    
    public PathEntity getPath() {
        return this.currentPath;
    }
    
    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3 vec3 = this.getEntityPosition();
                final Vec3 vec4 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
                if (vec3.yCoord > vec4.yCoord && !this.theEntity.onGround && MathHelper.floor_double(vec3.xCoord) == MathHelper.floor_double(vec4.xCoord) && MathHelper.floor_double(vec3.zCoord) == MathHelper.floor_double(vec4.zCoord)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            if (!this.noPath()) {
                final Vec3 vec5 = this.currentPath.getPosition(this.theEntity);
                if (vec5 != null) {
                    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(vec5.xCoord, vec5.yCoord, vec5.zCoord, vec5.xCoord, vec5.yCoord, vec5.zCoord).expand(0.5, 0.5, 0.5);
                    final List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this.theEntity, axisalignedbb1.addCoord(0.0, -1.0, 0.0));
                    double d0 = -1.0;
                    axisalignedbb1 = axisalignedbb1.offset(0.0, 1.0, 0.0);
                    for (final AxisAlignedBB axisalignedbb2 : list) {
                        d0 = axisalignedbb2.calculateYOffset(axisalignedbb1, d0);
                    }
                    this.theEntity.getMoveHelper().setMoveTo(vec5.xCoord, vec5.yCoord + d0, vec5.zCoord, this.speed);
                }
            }
        }
    }
    
    protected void pathFollow() {
        final Vec3 vec3 = this.getEntityPosition();
        int i = this.currentPath.getCurrentPathLength();
        for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j) {
            if (this.currentPath.getPathPointFromIndex(j).yCoord != (int)vec3.yCoord) {
                i = j;
                break;
            }
        }
        final float f = this.theEntity.width * this.theEntity.width * this.heightRequirement;
        for (int k = this.currentPath.getCurrentPathIndex(); k < i; ++k) {
            final Vec3 vec4 = this.currentPath.getVectorFromIndex(this.theEntity, k);
            if (vec3.squareDistanceTo(vec4) < f) {
                this.currentPath.setCurrentPathIndex(k + 1);
            }
        }
        final int j2 = MathHelper.ceiling_float_int(this.theEntity.width);
        final int k2 = (int)this.theEntity.height + 1;
        final int l = j2;
        for (int i2 = i - 1; i2 >= this.currentPath.getCurrentPathIndex(); --i2) {
            if (this.isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, i2), j2, k2, l)) {
                this.currentPath.setCurrentPathIndex(i2);
                break;
            }
        }
        this.checkForStuck(vec3);
    }
    
    protected void checkForStuck(final Vec3 positionVec3) {
        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25) {
                this.clearPathEntity();
            }
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = positionVec3;
        }
    }
    
    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }
    
    public void clearPathEntity() {
        this.currentPath = null;
    }
    
    protected abstract Vec3 getEntityPosition();
    
    protected abstract boolean canNavigate();
    
    protected boolean isInLiquid() {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }
    
    protected void removeSunnyPath() {
    }
    
    protected abstract boolean isDirectPathBetweenPoints(final Vec3 p0, final Vec3 p1, final int p2, final int p3, final int p4);
}
