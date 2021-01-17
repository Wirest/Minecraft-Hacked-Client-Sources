// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

public class Entity
{
    private float x;
    private float y;
    private Inventory invent;
    private Stats stats;
    
    private void add(final Inventory inventory) {
        this.invent = inventory;
    }
    
    private void add(final Stats stats) {
        this.stats = stats;
    }
    
    public void dump(final String prefix) {
        System.out.println(String.valueOf(prefix) + "Entity " + this.x + "," + this.y);
        this.invent.dump(String.valueOf(prefix) + "\t");
        this.stats.dump(String.valueOf(prefix) + "\t");
    }
}
