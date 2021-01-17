// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class DoubleClickTest extends BasicGame
{
    private String message;
    
    public DoubleClickTest() {
        super("Double Click Test");
        this.message = "Click or Double Click";
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) throws SlickException {
        g.drawString(this.message, 100.0f, 100.0f);
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new DoubleClickTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
        if (clickCount == 1) {
            this.message = "Single Click: " + button + " " + x + "," + y;
        }
        if (clickCount == 2) {
            this.message = "Double Click: " + button + " " + x + "," + y;
        }
    }
}
