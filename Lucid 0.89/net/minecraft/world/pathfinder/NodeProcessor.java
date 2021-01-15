package net.minecraft.world.pathfinder;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor
{
    protected IBlockAccess blockaccess;
    protected IntHashMap pointMap = new IntHashMap();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;

    public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
    {
        this.blockaccess = iblockaccessIn;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor_float(entityIn.width + 1.0F);
        this.entitySizeY = MathHelper.floor_float(entityIn.height + 1.0F);
        this.entitySizeZ = MathHelper.floor_float(entityIn.width + 1.0F);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     *  {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
     * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
     */
    public void postProcess() {}

    /**
     * Returns a mapped point or creates and adds one
     */
    protected PathPoint openPoint(int x, int y, int z)
    {
        int var4 = PathPoint.makeHash(x, y, z);
        PathPoint var5 = (PathPoint)this.pointMap.lookup(var4);

        if (var5 == null)
        {
            var5 = new PathPoint(x, y, z);
            this.pointMap.addKey(var4, var5);
        }

        return var5;
    }

    /**
     * Returns given entity's position as PathPoint
     */
    public abstract PathPoint getPathPointTo(Entity var1);

    /**
     * Returns PathPoint for given coordinates
     *  
     * @param entityIn entity which size will be used to center position
     * @param x target x coordinate
     * @param y target y coordinate
     * @param target z coordinate
     */
    public abstract PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6);

    public abstract int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5);
}
