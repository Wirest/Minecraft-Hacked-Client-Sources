// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding;

public interface TileBasedMap
{
    int getWidthInTiles();
    
    int getHeightInTiles();
    
    void pathFinderVisited(final int p0, final int p1);
    
    boolean blocked(final PathFindingContext p0, final int p1, final int p2);
    
    float getCost(final PathFindingContext p0, final int p1, final int p2);
}
