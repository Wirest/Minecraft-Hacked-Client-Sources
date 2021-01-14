package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate {
    protected EntityLiving theEntity;
    protected World worldObj;

    /**
     * The PathEntity being followed.
     */
    protected PathEntity currentPath;
    protected double speed;

    /**
     * The number of blocks (extra) +/- in each axis that get pulled out as
     * cache for the pathfinder's search space
     */
    private final IAttributeInstance pathSearchRange;

    /**
     * Time, in number of ticks, following the current path
     */
    private int totalTicks;

    /**
     * The time when the last position check was done (to detect successful
     * movement)
     */
    private int ticksAtLastPos;

    /**
     * Coordinates of the entity's position last time a check was done (part of
     * monitoring getting 'stuck')
     */
    private Vec3 lastPosCheck = new Vec3(0.0D, 0.0D, 0.0D);
    private float field_179682_i = 1.0F;
    private final PathFinder field_179681_j;
    private static final String __OBFID = "CL_00001627";

    public PathNavigate(EntityLiving p_i1671_1_, World worldIn) {
        theEntity = p_i1671_1_;
        worldObj = worldIn;
        pathSearchRange = p_i1671_1_.getEntityAttribute(SharedMonsterAttributes.followRange);
        field_179681_j = func_179679_a();
    }

    protected abstract PathFinder func_179679_a();

    /**
     * Sets the speed
     */
    public void setSpeed(double p_75489_1_) {
        speed = p_75489_1_;
    }

    /**
     * Gets the maximum distance that the path finding will search in.
     */
    public float getPathSearchRange() {
        return (float) pathSearchRange.getAttributeValue();
    }

    /**
     * Returns the path to the given coordinates. Args : x, y, z
     */
    public final PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_, double p_75488_5_) {
        return func_179680_a(new BlockPos(MathHelper.floor_double(p_75488_1_), (int) p_75488_3_, MathHelper.floor_double(p_75488_5_)));
    }

    public PathEntity func_179680_a(BlockPos p_179680_1_) {
        if (!canNavigate()) {
            return null;
        } else {
            float var2 = getPathSearchRange();
            worldObj.theProfiler.startSection("pathfind");
            BlockPos var3 = new BlockPos(theEntity);
            int var4 = (int) (var2 + 8.0F);
            ChunkCache var5 = new ChunkCache(worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
            PathEntity var6 = field_179681_j.func_180782_a(var5, theEntity, p_179680_1_, var2);
            worldObj.theProfiler.endSection();
            return var6;
        }
    }

    /**
     * Try to find and set a path to XYZ. Returns true if successful. Args : x,
     * y, z, speed
     */
    public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
        PathEntity var9 = getPathToXYZ(MathHelper.floor_double(p_75492_1_), ((int) p_75492_3_), MathHelper.floor_double(p_75492_5_));
        return setPath(var9, p_75492_7_);
    }

    public void func_179678_a(float p_179678_1_) {
        field_179682_i = p_179678_1_;
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
        if (!canNavigate()) {
            return null;
        } else {
            float var2 = getPathSearchRange();
            worldObj.theProfiler.startSection("pathfind");
            BlockPos var3 = (new BlockPos(theEntity)).offsetUp();
            int var4 = (int) (var2 + 16.0F);
            ChunkCache var5 = new ChunkCache(worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
            PathEntity var6 = field_179681_j.func_176188_a(var5, theEntity, p_75494_1_, var2);
            worldObj.theProfiler.endSection();
            return var6;
        }
    }

    /**
     * Try to find and set a path to EntityLiving. Returns true if successful.
     * Args : entity, speed
     */
    public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
        PathEntity var4 = getPathToEntityLiving(p_75497_1_);
        return var4 != null ? setPath(var4, p_75497_2_) : false;
    }

    /**
     * Sets a new path. If it's diferent from the old path. Checks to adjust
     * path for sun avoiding, and stores start coords. Args : path, speed
     */
    public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
        if (p_75484_1_ == null) {
            currentPath = null;
            return false;
        } else {
            if (!p_75484_1_.isSamePath(currentPath)) {
                currentPath = p_75484_1_;
            }

            removeSunnyPath();

            if (currentPath.getCurrentPathLength() == 0) {
                return false;
            } else {
                speed = p_75484_2_;
                Vec3 var4 = getEntityPosition();
                ticksAtLastPos = totalTicks;
                lastPosCheck = var4;
                return true;
            }
        }
    }

    /**
     * gets the actively used PathEntity
     */
    public PathEntity getPath() {
        return currentPath;
    }

    public void onUpdateNavigation() {
        ++totalTicks;

        if (!noPath()) {
            Vec3 var1;

            if (canNavigate()) {
                pathFollow();
            } else if (currentPath != null && currentPath.getCurrentPathIndex() < currentPath.getCurrentPathLength()) {
                var1 = getEntityPosition();
                Vec3 var2 = currentPath.getVectorFromIndex(theEntity, currentPath.getCurrentPathIndex());

                if (var1.yCoord > var2.yCoord && !theEntity.onGround && MathHelper.floor_double(var1.xCoord) == MathHelper.floor_double(var2.xCoord) && MathHelper.floor_double(var1.zCoord) == MathHelper.floor_double(var2.zCoord)) {
                    currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
                }
            }

            if (!noPath()) {
                var1 = currentPath.getPosition(theEntity);

                if (var1 != null) {
                    theEntity.getMoveHelper().setMoveTo(var1.xCoord, var1.yCoord, var1.zCoord, speed);
                }
            }
        }
    }

    protected void pathFollow() {
        Vec3 var1 = getEntityPosition();
        int var2 = currentPath.getCurrentPathLength();

        for (int var3 = currentPath.getCurrentPathIndex(); var3 < currentPath.getCurrentPathLength(); ++var3) {
            if (currentPath.getPathPointFromIndex(var3).yCoord != (int) var1.yCoord) {
                var2 = var3;
                break;
            }
        }

        float var8 = theEntity.width * theEntity.width * field_179682_i;
        int var4;

        for (var4 = currentPath.getCurrentPathIndex(); var4 < var2; ++var4) {
            Vec3 var5 = currentPath.getVectorFromIndex(theEntity, var4);

            if (var1.squareDistanceTo(var5) < var8) {
                currentPath.setCurrentPathIndex(var4 + 1);
            }
        }

        var4 = MathHelper.ceiling_float_int(theEntity.width);
        int var9 = (int) theEntity.height + 1;
        int var6 = var4;

        for (int var7 = var2 - 1; var7 >= currentPath.getCurrentPathIndex(); --var7) {
            if (isDirectPathBetweenPoints(var1, currentPath.getVectorFromIndex(theEntity, var7), var4, var9, var6)) {
                currentPath.setCurrentPathIndex(var7);
                break;
            }
        }

        func_179677_a(var1);
    }

    protected void func_179677_a(Vec3 p_179677_1_) {
        if (totalTicks - ticksAtLastPos > 100) {
            if (p_179677_1_.squareDistanceTo(lastPosCheck) < 2.25D) {
                clearPathEntity();
            }

            ticksAtLastPos = totalTicks;
            lastPosCheck = p_179677_1_;
        }
    }

    /**
     * If null path or reached the end
     */
    public boolean noPath() {
        return currentPath == null || currentPath.isFinished();
    }

    /**
     * sets active PathEntity to null
     */
    public void clearPathEntity() {
        currentPath = null;
    }

    protected abstract Vec3 getEntityPosition();

    /**
     * If on ground or swimming and can swim
     */
    protected abstract boolean canNavigate();

    /**
     * Returns true if the entity is in water or lava, false otherwise
     */
    protected boolean isInLiquid() {
        return theEntity.isInWater() || theEntity.func_180799_ab();
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    protected void removeSunnyPath() {
    }

    /**
     * Returns true when an entity of specified size could safely walk in a
     * straight line between the two points. Args: pos1, pos2, entityXSize,
     * entityYSize, entityZSize
     */
    protected abstract boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_);
}
