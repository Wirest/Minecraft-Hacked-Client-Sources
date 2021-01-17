// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.util.plugins.test;

import javax.swing.JList;
import javax.swing.DefaultListModel;

class ListUpdater implements Runnable
{
    Object[] objList;
    DefaultListModel mdl;
    
    public ListUpdater(final JList jlist, final Object[] list) {
        this.objList = list;
        this.mdl = (DefaultListModel)jlist.getModel();
    }
    
    public void run() {
        this.mdl.clear();
        for (int i = 0; i < this.objList.length; ++i) {
            this.mdl.addElement(this.objList[i]);
        }
    }
}
