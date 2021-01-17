// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

import java.util.ArrayList;

public class GameData
{
    private ArrayList entities;
    
    public GameData() {
        this.entities = new ArrayList();
    }
    
    private void add(final Entity entity) {
        this.entities.add(entity);
    }
    
    public void dump(final String prefix) {
        System.out.println(String.valueOf(prefix) + "GameData");
        for (int i = 0; i < this.entities.size(); ++i) {
            this.entities.get(i).dump(String.valueOf(prefix) + "\t");
        }
    }
}
