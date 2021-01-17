// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class Inventory
{
    private ArrayList items;
    
    public Inventory() {
        this.items = new ArrayList();
    }
    
    private void add(final Item item) {
        this.items.add(item);
    }
    
    public void dump(final String prefix) {
        System.out.println(String.valueOf(prefix) + "Inventory");
        for (int i = 0; i < this.items.size(); ++i) {
            this.items.get(i).dump(String.valueOf(prefix) + "\t");
        }
    }
}
