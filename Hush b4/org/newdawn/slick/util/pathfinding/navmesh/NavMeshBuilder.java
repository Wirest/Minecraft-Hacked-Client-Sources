// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding.navmesh;

import org.newdawn.slick.util.pathfinding.Mover;
import java.util.ArrayList;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;

public class NavMeshBuilder implements PathFindingContext
{
    private int sx;
    private int sy;
    private float smallestSpace;
    private boolean tileBased;
    
    public NavMeshBuilder() {
        this.smallestSpace = 0.2f;
    }
    
    public NavMesh build(final TileBasedMap map) {
        return this.build(map, true);
    }
    
    public NavMesh build(final TileBasedMap map, final boolean tileBased) {
        this.tileBased = tileBased;
        final ArrayList spaces = new ArrayList();
        if (tileBased) {
            for (int x = 0; x < map.getWidthInTiles(); ++x) {
                for (int y = 0; y < map.getHeightInTiles(); ++y) {
                    if (!map.blocked(this, x, y)) {
                        spaces.add(new Space((float)x, (float)y, 1.0f, 1.0f));
                    }
                }
            }
        }
        else {
            final Space space = new Space(0.0f, 0.0f, (float)map.getWidthInTiles(), (float)map.getHeightInTiles());
            this.subsection(map, space, spaces);
        }
        while (this.mergeSpaces(spaces)) {}
        this.linkSpaces(spaces);
        return new NavMesh(spaces);
    }
    
    private boolean mergeSpaces(final ArrayList spaces) {
        for (int source = 0; source < spaces.size(); ++source) {
            final Space a = spaces.get(source);
            for (int target = source + 1; target < spaces.size(); ++target) {
                final Space b = spaces.get(target);
                if (a.canMerge(b)) {
                    spaces.remove(a);
                    spaces.remove(b);
                    spaces.add(a.merge(b));
                    return true;
                }
            }
        }
        return false;
    }
    
    private void linkSpaces(final ArrayList spaces) {
        for (int source = 0; source < spaces.size(); ++source) {
            final Space a = spaces.get(source);
            for (int target = source + 1; target < spaces.size(); ++target) {
                final Space b = spaces.get(target);
                if (a.hasJoinedEdge(b)) {
                    a.link(b);
                    b.link(a);
                }
            }
        }
    }
    
    public boolean clear(final TileBasedMap map, final Space space) {
        if (this.tileBased) {
            return true;
        }
        float x = 0.0f;
        boolean donex = false;
        while (x < space.getWidth()) {
            float y = 0.0f;
            boolean doney = false;
            while (y < space.getHeight()) {
                this.sx = (int)(space.getX() + x);
                this.sy = (int)(space.getY() + y);
                if (map.blocked(this, this.sx, this.sy)) {
                    return false;
                }
                y += 0.1f;
                if (y <= space.getHeight() || doney) {
                    continue;
                }
                y = space.getHeight();
                doney = true;
            }
            x += 0.1f;
            if (x > space.getWidth() && !donex) {
                x = space.getWidth();
                donex = true;
            }
        }
        return true;
    }
    
    private void subsection(final TileBasedMap map, final Space space, final ArrayList spaces) {
        if (!this.clear(map, space)) {
            final float width2 = space.getWidth() / 2.0f;
            final float height2 = space.getHeight() / 2.0f;
            if (width2 < this.smallestSpace && height2 < this.smallestSpace) {
                return;
            }
            this.subsection(map, new Space(space.getX(), space.getY(), width2, height2), spaces);
            this.subsection(map, new Space(space.getX(), space.getY() + height2, width2, height2), spaces);
            this.subsection(map, new Space(space.getX() + width2, space.getY(), width2, height2), spaces);
            this.subsection(map, new Space(space.getX() + width2, space.getY() + height2, width2, height2), spaces);
        }
        else {
            spaces.add(space);
        }
    }
    
    @Override
    public Mover getMover() {
        return null;
    }
    
    @Override
    public int getSearchDistance() {
        return 0;
    }
    
    @Override
    public int getSourceX() {
        return this.sx;
    }
    
    @Override
    public int getSourceY() {
        return this.sy;
    }
}
