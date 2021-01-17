// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.Collection;
import java.util.ArrayList;

public class NavMesh
{
    private ArrayList spaces;
    
    public NavMesh() {
        this.spaces = new ArrayList();
    }
    
    public NavMesh(final ArrayList spaces) {
        (this.spaces = new ArrayList()).addAll(spaces);
    }
    
    public int getSpaceCount() {
        return this.spaces.size();
    }
    
    public Space getSpace(final int index) {
        return this.spaces.get(index);
    }
    
    public void addSpace(final Space space) {
        this.spaces.add(space);
    }
    
    public Space findSpace(final float x, final float y) {
        for (int i = 0; i < this.spaces.size(); ++i) {
            final Space space = this.getSpace(i);
            if (space.contains(x, y)) {
                return space;
            }
        }
        return null;
    }
    
    public NavPath findPath(final float sx, final float sy, final float tx, final float ty, final boolean optimize) {
        final Space source = this.findSpace(sx, sy);
        final Space target = this.findSpace(tx, ty);
        if (source == null || target == null) {
            return null;
        }
        for (int i = 0; i < this.spaces.size(); ++i) {
            this.spaces.get(i).clearCost();
        }
        target.fill(source, tx, ty, 0.0f);
        if (target.getCost() == Float.MAX_VALUE) {
            return null;
        }
        if (source.getCost() == Float.MAX_VALUE) {
            return null;
        }
        final NavPath path = new NavPath();
        path.push(new Link(sx, sy, null));
        if (source.pickLowestCost(target, path)) {
            path.push(new Link(tx, ty, null));
            if (optimize) {
                this.optimize(path);
            }
            return path;
        }
        return null;
    }
    
    private boolean isClear(final float x1, final float y1, final float x2, final float y2, final float step) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        final float len = (float)Math.sqrt(dx * dx + dy * dy);
        dx *= step;
        dx /= len;
        dy *= step;
        dy /= len;
        for (int steps = (int)(len / step), i = 0; i < steps; ++i) {
            final float x3 = x1 + dx * i;
            final float y3 = y1 + dy * i;
            if (this.findSpace(x3, y3) == null) {
                return false;
            }
        }
        return true;
    }
    
    private void optimize(final NavPath path) {
        int pt = 0;
        while (pt < path.length() - 2) {
            final float sx = path.getX(pt);
            final float sy = path.getY(pt);
            final float nx = path.getX(pt + 2);
            final float ny = path.getY(pt + 2);
            if (this.isClear(sx, sy, nx, ny, 0.1f)) {
                path.remove(pt + 1);
            }
            else {
                ++pt;
            }
        }
    }
}
