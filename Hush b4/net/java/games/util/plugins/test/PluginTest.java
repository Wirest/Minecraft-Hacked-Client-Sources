// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.util.plugins.test;

import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import java.awt.Component;
import javax.swing.JScrollPane;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import java.io.IOException;
import java.io.File;
import javax.swing.JList;
import net.java.games.util.plugins.Plugins;

public class PluginTest
{
    static final boolean DEBUG = false;
    Plugins plugins;
    JList plist;
    Class[] piList;
    
    public PluginTest() {
        try {
            this.plugins = new Plugins(new File("test_plugins"));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        final JFrame f = new JFrame("PluginTest");
        (this.plist = new JList((ListModel<E>)new DefaultListModel<Object>())).setCellRenderer(new ClassRenderer());
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(new JScrollPane(this.plist), "Center");
        final JPanel p = new JPanel();
        c.add(p, "South");
        p.setLayout(new FlowLayout());
        f.pack();
        f.setDefaultCloseOperation(3);
        f.setVisible(true);
        this.doListAll();
    }
    
    private void doListAll() {
        SwingUtilities.invokeLater(new ListUpdater(this.plist, this.plugins.get()));
    }
    
    public static void main(final String[] args) {
        new PluginTest();
    }
}
