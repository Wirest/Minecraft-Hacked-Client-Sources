// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.util.Log;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.Frame;
import org.newdawn.slick.Game;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class CanvasSizeTest extends BasicGame
{
    public CanvasSizeTest() {
        super("Test");
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        System.out.println(String.valueOf(container.getWidth()) + ", " + container.getHeight());
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    public static void main(final String[] args) {
        try {
            final CanvasGameContainer container = new CanvasGameContainer(new CanvasSizeTest());
            container.setSize(640, 480);
            final Frame frame = new Frame("Test");
            frame.setLayout(new GridLayout(1, 2));
            frame.add(container);
            frame.pack();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(final WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.setVisible(true);
            container.start();
        }
        catch (Exception e) {
            Log.error(e);
        }
    }
}
