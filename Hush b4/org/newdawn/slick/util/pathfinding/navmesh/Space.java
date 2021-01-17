// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.ArrayList;
import java.util.HashMap;

public class Space
{
    private float x;
    private float y;
    private float width;
    private float height;
    private HashMap links;
    private ArrayList linksList;
    private float cost;
    
    public Space(final float x, final float y, final float width, final float height) {
        this.links = new HashMap();
        this.linksList = new ArrayList();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void link(final Space other) {
        if (this.inTolerance(this.x, other.x + other.width) || this.inTolerance(this.x + this.width, other.x)) {
            float linkx = this.x;
            if (this.x + this.width == other.x) {
                linkx = this.x + this.width;
            }
            final float top = Math.max(this.y, other.y);
            final float bottom = Math.min(this.y + this.height, other.y + other.height);
            final float linky = top + (bottom - top) / 2.0f;
            final Link link = new Link(linkx, linky, other);
            this.links.put(other, link);
            this.linksList.add(link);
        }
        if (this.inTolerance(this.y, other.y + other.height) || this.inTolerance(this.y + this.height, other.y)) {
            float linky2 = this.y;
            if (this.y + this.height == other.y) {
                linky2 = this.y + this.height;
            }
            final float left = Math.max(this.x, other.x);
            final float right = Math.min(this.x + this.width, other.x + other.width);
            final float linkx2 = left + (right - left) / 2.0f;
            final Link link = new Link(linkx2, linky2, other);
            this.links.put(other, link);
            this.linksList.add(link);
        }
    }
    
    private boolean inTolerance(final float a, final float b) {
        return a == b;
    }
    
    public boolean hasJoinedEdge(final Space other) {
        if (this.inTolerance(this.x, other.x + other.width) || this.inTolerance(this.x + this.width, other.x)) {
            if (this.y >= other.y && this.y <= other.y + other.height) {
                return true;
            }
            if (this.y + this.height >= other.y && this.y + this.height <= other.y + other.height) {
                return true;
            }
            if (other.y >= this.y && other.y <= this.y + this.height) {
                return true;
            }
            if (other.y + other.height >= this.y && other.y + other.height <= this.y + this.height) {
                return true;
            }
        }
        if (this.inTolerance(this.y, other.y + other.height) || this.inTolerance(this.y + this.height, other.y)) {
            if (this.x >= other.x && this.x <= other.x + other.width) {
                return true;
            }
            if (this.x + this.width >= other.x && this.x + this.width <= other.x + other.width) {
                return true;
            }
            if (other.x >= this.x && other.x <= this.x + this.width) {
                return true;
            }
            if (other.x + other.width >= this.x && other.x + other.width <= this.x + this.width) {
                return true;
            }
        }
        return false;
    }
    
    public Space merge(final Space other) {
        final float minx = Math.min(this.x, other.x);
        final float miny = Math.min(this.y, other.y);
        float newwidth = this.width + other.width;
        float newheight = this.height + other.height;
        if (this.x == other.x) {
            newwidth = this.width;
        }
        else {
            newheight = this.height;
        }
        return new Space(minx, miny, newwidth, newheight);
    }
    
    public boolean canMerge(final Space other) {
        return this.hasJoinedEdge(other) && ((this.x == other.x && this.width == other.width) || (this.y == other.y && this.height == other.height));
    }
    
    public int getLinkCount() {
        return this.linksList.size();
    }
    
    public Link getLink(final int index) {
        return this.linksList.get(index);
    }
    
    public boolean contains(final float xp, final float yp) {
        return xp >= this.x && xp < this.x + this.width && yp >= this.y && yp < this.y + this.height;
    }
    
    public void fill(final Space target, final float sx, final float sy, final float cost) {
        if (cost >= this.cost) {
            return;
        }
        this.cost = cost;
        if (target == this) {
            return;
        }
        for (int i = 0; i < this.getLinkCount(); ++i) {
            final Link link = this.getLink(i);
            final float extraCost = link.distance2(sx, sy);
            final float nextCost = cost + extraCost;
            link.getTarget().fill(target, link.getX(), link.getY(), nextCost);
        }
    }
    
    public void clearCost() {
        this.cost = Float.MAX_VALUE;
    }
    
    public float getCost() {
        return this.cost;
    }
    
    public boolean pickLowestCost(final Space target, final NavPath path) {
        if (target == this) {
            return true;
        }
        if (this.links.size() == 0) {
            return false;
        }
        Link bestLink = null;
        for (int i = 0; i < this.getLinkCount(); ++i) {
            final Link link = this.getLink(i);
            if (bestLink == null || link.getTarget().getCost() < bestLink.getTarget().getCost()) {
                bestLink = link;
            }
        }
        path.push(bestLink);
        return bestLink.getTarget().pickLowestCost(target, path);
    }
    
    @Override
    public String toString() {
        return "[Space " + this.x + "," + this.y + " " + this.width + "," + this.height + "]";
    }
}
