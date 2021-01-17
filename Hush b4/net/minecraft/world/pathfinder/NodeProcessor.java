// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.pathfinder;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor
{
    protected IBlockAccess blockaccess;
    protected IntHashMap<PathPoint> pointMap;
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    
    public NodeProcessor() {
        this.pointMap = new IntHashMap<PathPoint>();
    }
    
    public void initProcessor(final IBlockAccess iblockaccessIn, final Entity entityIn) {
        this.blockaccess = iblockaccessIn;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor_float(entityIn.width + 1.0f);
        this.entitySizeY = MathHelper.floor_float(entityIn.height + 1.0f);
        this.entitySizeZ = MathHelper.floor_float(entityIn.width + 1.0f);
    }
    
    public void postProcess() {
    }
    
    protected PathPoint openPoint(final int x, final int y, final int z) {
        final int i = PathPoint.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.lookup(i);
        if (pathpoint == null) {
            pathpoint = new PathPoint(x, y, z);
            this.pointMap.addKey(i, pathpoint);
        }
        return pathpoint;
    }
    
    public abstract PathPoint getPathPointTo(final Entity p0);
    
    public abstract PathPoint getPathPointToCoords(final Entity p0, final double p1, final double p2, final double p3);
    
    public abstract int findPathOptions(final PathPoint[] p0, final Entity p1, final PathPoint p2, final PathPoint p3, final float p4);
}
