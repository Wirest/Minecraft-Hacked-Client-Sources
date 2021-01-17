// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class ItemContainer extends Item
{
    private ArrayList items;
    
    public ItemContainer() {
        this.items = new ArrayList();
    }
    
    private void add(final Item item) {
        this.items.add(item);
    }
    
    private void setName(final String name) {
        this.name = name;
    }
    
    private void setCondition(final int condition) {
        this.condition = condition;
    }
    
    @Override
    public void dump(final String prefix) {
        System.out.println(String.valueOf(prefix) + "Item Container " + this.name + "," + this.condition);
        for (int i = 0; i < this.items.size(); ++i) {
            this.items.get(i).dump(String.valueOf(prefix) + "\t");
        }
    }
}
