// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.ArrayList;

public class NavPath
{
    private ArrayList links;
    
    public NavPath() {
        this.links = new ArrayList();
    }
    
    public void push(final Link link) {
        this.links.add(link);
    }
    
    public int length() {
        return this.links.size();
    }
    
    public float getX(final int step) {
        return this.links.get(step).getX();
    }
    
    public float getY(final int step) {
        return this.links.get(step).getY();
    }
    
    @Override
    public String toString() {
        return "[Path length=" + this.length() + "]";
    }
    
    public void remove(final int i) {
        this.links.remove(i);
    }
}
