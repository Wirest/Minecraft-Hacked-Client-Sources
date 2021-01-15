package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class PathFinder
{
    /** The path being generated */
    private Path path = new Path();

    /** Selection of path points to add to the path */
    private PathPoint[] pathOptions = new PathPoint[32];
    private NodeProcessor nodeProcessor;

    public PathFinder(NodeProcessor nodeProcessorIn)
    {
        this.nodeProcessor = nodeProcessorIn;
    }

    /**
     * Creates a path from one entity to another within a minimum distance
     */
    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityFrom, Entity entityTo, float dist)
    {
        return this.createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, dist);
    }

    /**
     * Creates a path from an entity to a specified location within a minimum distance
     */
    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, BlockPos targetPos, float dist)
    {
        return this.createEntityPathTo(blockaccess, entityIn, targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, dist);
    }

    /**
     * Internal implementation of creating a path from an entity to a point
     *  
     * @param x target x coordinate
     * @param y target y coordinate
     * @param z target z coordinate
     * @param distance max distance
     */
    private PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, double x, double y, double z, float distance)
    {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(blockaccess, entityIn);
        PathPoint var10 = this.nodeProcessor.getPathPointTo(entityIn);
        PathPoint var11 = this.nodeProcessor.getPathPointToCoords(entityIn, x, y, z);
        PathEntity var12 = this.addToPath(entityIn, var10, var11, distance);
        this.nodeProcessor.postProcess();
        return var12;
    }

    /**
     * Adds a path from start to end and returns the whole path
     */
    private PathEntity addToPath(Entity entityIn, PathPoint pathpointStart, PathPoint pathpointEnd, float maxDistance)
    {
        pathpointStart.totalPathDistance = 0.0F;
        pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
        pathpointStart.distanceToTarget = pathpointStart.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(pathpointStart);
        PathPoint var5 = pathpointStart;

        while (!this.path.isPathEmpty())
        {
            PathPoint var6 = this.path.dequeue();

            if (var6.equals(pathpointEnd))
            {
                return this.createEntityPath(pathpointStart, pathpointEnd);
            }

            if (var6.distanceToSquared(pathpointEnd) < var5.distanceToSquared(pathpointEnd))
            {
                var5 = var6;
            }

            var6.visited = true;
            int var7 = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, var6, pathpointEnd, maxDistance);

            for (int var8 = 0; var8 < var7; ++var8)
            {
                PathPoint var9 = this.pathOptions[var8];
                float var10 = var6.totalPathDistance + var6.distanceToSquared(var9);

                if (var10 < maxDistance * 2.0F && (!var9.isAssigned() || var10 < var9.totalPathDistance))
                {
                    var9.previous = var6;
                    var9.totalPathDistance = var10;
                    var9.distanceToNext = var9.distanceToSquared(pathpointEnd);

                    if (var9.isAssigned())
                    {
                        this.path.changeDistance(var9, var9.totalPathDistance + var9.distanceToNext);
                    }
                    else
                    {
                        var9.distanceToTarget = var9.totalPathDistance + var9.distanceToNext;
                        this.path.addPoint(var9);
                    }
                }
            }
        }

        if (var5 == pathpointStart)
        {
            return null;
        }
        else
        {
            return this.createEntityPath(pathpointStart, var5);
        }
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private PathEntity createEntityPath(PathPoint start, PathPoint end)
    {
        int var3 = 1;
        PathPoint var4;

        for (var4 = end; var4.previous != null; var4 = var4.previous)
        {
            ++var3;
        }

        PathPoint[] var5 = new PathPoint[var3];
        var4 = end;
        --var3;

        for (var5[var3] = end; var4.previous != null; var5[var3] = var4)
        {
            var4 = var4.previous;
            --var3;
        }

        return new PathEntity(var5);
    }
}
