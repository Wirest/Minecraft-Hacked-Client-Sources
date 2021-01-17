// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.pathfinding;

import java.util.ArrayList;
import java.io.Serializable;

public class Path implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ArrayList steps;
    
    public Path() {
        this.steps = new ArrayList();
    }
    
    public int getLength() {
        return this.steps.size();
    }
    
    public Step getStep(final int index) {
        return this.steps.get(index);
    }
    
    public int getX(final int index) {
        return this.getStep(index).x;
    }
    
    public int getY(final int index) {
        return this.getStep(index).y;
    }
    
    public void appendStep(final int x, final int y) {
        this.steps.add(new Step(x, y));
    }
    
    public void prependStep(final int x, final int y) {
        this.steps.add(0, new Step(x, y));
    }
    
    public boolean contains(final int x, final int y) {
        return this.steps.contains(new Step(x, y));
    }
    
    public class Step implements Serializable
    {
        private int x;
        private int y;
        
        public Step(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        @Override
        public int hashCode() {
            return this.x * this.y;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (other instanceof Step) {
                final Step o = (Step)other;
                return o.x == this.x && o.y == this.y;
            }
            return false;
        }
    }
}
