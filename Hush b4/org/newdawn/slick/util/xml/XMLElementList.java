// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.xml;

import java.util.Collection;
import java.util.ArrayList;

public class XMLElementList
{
    private ArrayList list;
    
    public XMLElementList() {
        this.list = new ArrayList();
    }
    
    public void add(final XMLElement element) {
        this.list.add(element);
    }
    
    public int size() {
        return this.list.size();
    }
    
    public XMLElement get(final int i) {
        return this.list.get(i);
    }
    
    public boolean contains(final XMLElement element) {
        return this.list.contains(element);
    }
    
    public void addAllTo(final Collection collection) {
        collection.addAll(this.list);
    }
}
