// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.MathHelper;

public class PathPoint
{
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    private final int hash;
    int index;
    float totalPathDistance;
    float distanceToNext;
    float distanceToTarget;
    PathPoint previous;
    public boolean visited;
    
    public PathPoint(final int x, final int y, final int z) {
        this.index = -1;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.hash = makeHash(x, y, z);
    }
    
    public static int makeHash(final int x, final int y, final int z) {
        return (y & 0xFF) | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | ((x < 0) ? Integer.MIN_VALUE : 0) | ((z < 0) ? 32768 : 0);
    }
    
    public float distanceTo(final PathPoint pathpointIn) {
        final float f = (float)(pathpointIn.xCoord - this.xCoord);
        final float f2 = (float)(pathpointIn.yCoord - this.yCoord);
        final float f3 = (float)(pathpointIn.zCoord - this.zCoord);
        return MathHelper.sqrt_float(f * f + f2 * f2 + f3 * f3);
    }
    
    public float distanceToSquared(final PathPoint pathpointIn) {
        final float f = (float)(pathpointIn.xCoord - this.xCoord);
        final float f2 = (float)(pathpointIn.yCoord - this.yCoord);
        final float f3 = (float)(pathpointIn.zCoord - this.zCoord);
        return f * f + f2 * f2 + f3 * f3;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PathPoint)) {
            return false;
        }
        final PathPoint pathpoint = (PathPoint)p_equals_1_;
        return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord;
    }
    
    @Override
    public int hashCode() {
        return this.hash;
    }
    
    public boolean isAssigned() {
        return this.index >= 0;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.xCoord) + ", " + this.yCoord + ", " + this.zCoord;
    }
}
