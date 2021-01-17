// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class PathFinder
{
    private Path path;
    private PathPoint[] pathOptions;
    private NodeProcessor nodeProcessor;
    
    public PathFinder(final NodeProcessor nodeProcessorIn) {
        this.path = new Path();
        this.pathOptions = new PathPoint[32];
        this.nodeProcessor = nodeProcessorIn;
    }
    
    public PathEntity createEntityPathTo(final IBlockAccess blockaccess, final Entity entityFrom, final Entity entityTo, final float dist) {
        return this.createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, dist);
    }
    
    public PathEntity createEntityPathTo(final IBlockAccess blockaccess, final Entity entityIn, final BlockPos targetPos, final float dist) {
        return this.createEntityPathTo(blockaccess, entityIn, targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f, dist);
    }
    
    private PathEntity createEntityPathTo(final IBlockAccess blockaccess, final Entity entityIn, final double x, final double y, final double z, final float distance) {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(blockaccess, entityIn);
        final PathPoint pathpoint = this.nodeProcessor.getPathPointTo(entityIn);
        final PathPoint pathpoint2 = this.nodeProcessor.getPathPointToCoords(entityIn, x, y, z);
        final PathEntity pathentity = this.addToPath(entityIn, pathpoint, pathpoint2, distance);
        this.nodeProcessor.postProcess();
        return pathentity;
    }
    
    private PathEntity addToPath(final Entity entityIn, final PathPoint pathpointStart, final PathPoint pathpointEnd, final float maxDistance) {
        pathpointStart.totalPathDistance = 0.0f;
        pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
        pathpointStart.distanceToTarget = pathpointStart.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(pathpointStart);
        PathPoint pathpoint = pathpointStart;
        while (!this.path.isPathEmpty()) {
            final PathPoint pathpoint2 = this.path.dequeue();
            if (pathpoint2.equals(pathpointEnd)) {
                return this.createEntityPath(pathpointStart, pathpointEnd);
            }
            if (pathpoint2.distanceToSquared(pathpointEnd) < pathpoint.distanceToSquared(pathpointEnd)) {
                pathpoint = pathpoint2;
            }
            pathpoint2.visited = true;
            for (int i = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, pathpoint2, pathpointEnd, maxDistance), j = 0; j < i; ++j) {
                final PathPoint pathpoint3 = this.pathOptions[j];
                final float f = pathpoint2.totalPathDistance + pathpoint2.distanceToSquared(pathpoint3);
                if (f < maxDistance * 2.0f && (!pathpoint3.isAssigned() || f < pathpoint3.totalPathDistance)) {
                    pathpoint3.previous = pathpoint2;
                    pathpoint3.totalPathDistance = f;
                    pathpoint3.distanceToNext = pathpoint3.distanceToSquared(pathpointEnd);
                    if (pathpoint3.isAssigned()) {
                        this.path.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
                    }
                    else {
                        pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                        this.path.addPoint(pathpoint3);
                    }
                }
            }
        }
        if (pathpoint == pathpointStart) {
            return null;
        }
        return this.createEntityPath(pathpointStart, pathpoint);
    }
    
    private PathEntity createEntityPath(final PathPoint start, final PathPoint end) {
        int i = 1;
        for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }
        final PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint2 = end;
        --i;
        apathpoint[i] = end;
        while (pathpoint2.previous != null) {
            pathpoint2 = pathpoint2.previous;
            --i;
            apathpoint[i] = pathpoint2;
        }
        return new PathEntity(apathpoint);
    }
}
