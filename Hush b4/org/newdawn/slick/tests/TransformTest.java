// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;

public class TransformTest extends BasicGame
{
    private float scale;
    private boolean scaleUp;
    private boolean scaleDown;
    
    public TransformTest() {
        super("Transform Test");
        this.scale = 1.0f;
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        container.setTargetFrameRate(100);
    }
    
    @Override
    public void render(final GameContainer contiainer, final Graphics g) {
        g.translate(320.0f, 240.0f);
        g.scale(this.scale, this.scale);
        g.setColor(Color.red);
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                g.fillRect((float)(-500 + x * 100), (float)(-500 + y * 100), 80.0f, 80.0f);
            }
        }
        g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.5f));
        g.fillRect(-320.0f, -240.0f, 640.0f, 480.0f);
        g.setColor(Color.white);
        g.drawRect(-320.0f, -240.0f, 640.0f, 480.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        if (this.scaleUp) {
            this.scale += delta * 0.001f;
        }
        if (this.scaleDown) {
            this.scale -= delta * 0.001f;
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 16) {
            this.scaleUp = true;
        }
        if (key == 30) {
            this.scaleDown = true;
        }
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        if (key == 16) {
            this.scaleUp = false;
        }
        if (key == 30) {
            this.scaleDown = false;
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new TransformTest());
            container.setDisplayMode(640, 480, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
