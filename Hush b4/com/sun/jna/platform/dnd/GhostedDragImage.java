// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.dnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import com.sun.jna.platform.WindowUtils;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Icon;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;

public class GhostedDragImage
{
    private static final float DEFAULT_ALPHA = 0.5f;
    private Window dragImage;
    private Point origin;
    private static final int SLIDE_INTERVAL = 33;
    
    public GhostedDragImage(final Component dragSource, final Icon icon, final Point initialScreenLoc, final Point cursorOffset) {
        final Window parent = (Window)((dragSource instanceof Window) ? dragSource : SwingUtilities.getWindowAncestor(dragSource));
        final GraphicsConfiguration gc = parent.getGraphicsConfiguration();
        (this.dragImage = new Window(JOptionPane.getRootFrame(), gc) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void paint(final Graphics g) {
                icon.paintIcon(this, g, 0, 0);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }
            
            @Override
            public Dimension getMinimumSize() {
                return this.getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return this.getPreferredSize();
            }
        }).setFocusableWindowState(false);
        this.dragImage.setName("###overrideRedirect###");
        final Icon dragIcon = new Icon() {
            public int getIconHeight() {
                return icon.getIconHeight();
            }
            
            public int getIconWidth() {
                return icon.getIconWidth();
            }
            
            public void paintIcon(final Component c, Graphics g, final int x, final int y) {
                g = g.create();
                final Area area = new Area(new Rectangle(x, y, this.getIconWidth(), this.getIconHeight()));
                area.subtract(new Area(new Rectangle(x + cursorOffset.x - 1, y + cursorOffset.y - 1, 3, 3)));
                g.setClip(area);
                icon.paintIcon(c, g, x, y);
                g.dispose();
            }
        };
        this.dragImage.pack();
        WindowUtils.setWindowMask(this.dragImage, dragIcon);
        WindowUtils.setWindowAlpha(this.dragImage, 0.5f);
        this.move(initialScreenLoc);
        this.dragImage.setVisible(true);
    }
    
    public void setAlpha(final float alpha) {
        WindowUtils.setWindowAlpha(this.dragImage, alpha);
    }
    
    public void dispose() {
        this.dragImage.dispose();
        this.dragImage = null;
    }
    
    public void move(final Point screenLocation) {
        if (this.origin == null) {
            this.origin = screenLocation;
        }
        this.dragImage.setLocation(screenLocation.x, screenLocation.y);
    }
    
    public void returnToOrigin() {
        final Timer timer = new Timer(33, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Point location = GhostedDragImage.this.dragImage.getLocationOnScreen();
                final Point dst = new Point(GhostedDragImage.this.origin);
                final int dx = (dst.x - location.x) / 2;
                final int dy = (dst.y - location.y) / 2;
                if (dx != 0 || dy != 0) {
                    location.translate(dx, dy);
                    GhostedDragImage.this.move(location);
                }
                else {
                    timer.stop();
                    GhostedDragImage.this.dispose();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}
