// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.util.plugins.test;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

class ClassRenderer implements ListCellRenderer
{
    JLabel label;
    
    ClassRenderer() {
        this.label = new JLabel();
    }
    
    public Component getListCellRendererComponent(final JList jList, final Object obj, final int param, final boolean param3, final boolean param4) {
        this.label.setText(((Class)obj).getName());
        this.label.setForeground(Color.BLACK);
        this.label.setBackground(Color.WHITE);
        return this.label;
    }
}
