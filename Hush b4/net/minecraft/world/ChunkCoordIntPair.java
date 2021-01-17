// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.util.BlockPos;

public class ChunkCoordIntPair
{
    public final int chunkXPos;
    public final int chunkZPos;
    private static final String __OBFID = "CL_00000133";
    private int cachedHashCode;
    
    public ChunkCoordIntPair(final int x, final int z) {
        this.cachedHashCode = 0;
        this.chunkXPos = x;
        this.chunkZPos = z;
    }
    
    public static long chunkXZ2Int(final int x, final int z) {
        return ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
    }
    
    @Override
    public int hashCode() {
        if (this.cachedHashCode == 0) {
            final int i = 1664525 * this.chunkXPos + 1013904223;
            final int j = 1664525 * (this.chunkZPos ^ 0xDEADBEEF) + 1013904223;
            this.cachedHashCode = (i ^ j);
        }
        return this.cachedHashCode;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChunkCoordIntPair)) {
            return false;
        }
        final ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)p_equals_1_;
        return this.chunkXPos == chunkcoordintpair.chunkXPos && this.chunkZPos == chunkcoordintpair.chunkZPos;
    }
    
    public int getCenterXPos() {
        return (this.chunkXPos << 4) + 8;
    }
    
    public int getCenterZPosition() {
        return (this.chunkZPos << 4) + 8;
    }
    
    public int getXStart() {
        return this.chunkXPos << 4;
    }
    
    public int getZStart() {
        return this.chunkZPos << 4;
    }
    
    public int getXEnd() {
        return (this.chunkXPos << 4) + 15;
    }
    
    public int getZEnd() {
        return (this.chunkZPos << 4) + 15;
    }
    
    public BlockPos getBlock(final int x, final int y, final int z) {
        return new BlockPos((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
    }
    
    public BlockPos getCenterBlock(final int y) {
        return new BlockPos(this.getCenterXPos(), y, this.getCenterZPosition());
    }
    
    @Override
    public String toString() {
        return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
    }
}
