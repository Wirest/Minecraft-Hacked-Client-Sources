// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

public class Item
{
    protected String name;
    protected int condition;
    
    public void dump(final String prefix) {
        System.out.println(String.valueOf(prefix) + "Item " + this.name + "," + this.condition);
    }
}
