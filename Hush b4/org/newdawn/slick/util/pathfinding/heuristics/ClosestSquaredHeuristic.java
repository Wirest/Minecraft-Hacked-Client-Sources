// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.AStarHeuristic;

public class ClosestSquaredHeuristic implements AStarHeuristic
{
    @Override
    public float getCost(final TileBasedMap map, final Mover mover, final int x, final int y, final int tx, final int ty) {
        final float dx = (float)(tx - x);
        final float dy = (float)(ty - y);
        return dx * dx + dy * dy;
    }
}
