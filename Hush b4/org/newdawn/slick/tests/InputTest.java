// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import org.newdawn.slick.BasicGame;

public class InputTest extends BasicGame
{
    private String message;
    private ArrayList lines;
    private boolean buttonDown;
    private float x;
    private float y;
    private Color[] cols;
    private int index;
    private Input input;
    private int ypos;
    private AppGameContainer app;
    private boolean space;
    private boolean lshift;
    private boolean rshift;
    
    public InputTest() {
        super("Input Test");
        this.message = "Press any key, mouse button, or drag the mouse";
        this.lines = new ArrayList();
        this.cols = new Color[] { Color.red, Color.green, Color.blue, Color.white, Color.magenta, Color.cyan };
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            this.app = (AppGameContainer)container;
        }
        this.input = container.getInput();
        this.x = 300.0f;
        this.y = 300.0f;
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.drawString("left shift down: " + this.lshift, 100.0f, 240.0f);
        g.drawString("right shift down: " + this.rshift, 100.0f, 260.0f);
        g.drawString("space down: " + this.space, 100.0f, 280.0f);
        g.setColor(Color.white);
        g.drawString(this.message, 10.0f, 50.0f);
        g.drawString(new StringBuilder().append(container.getInput().getMouseY()).toString(), 10.0f, 400.0f);
        g.drawString("Use the primary gamepad to control the blob, and hit a gamepad button to change the color", 10.0f, 90.0f);
        for (int i = 0; i < this.lines.size(); ++i) {
            final Line line = this.lines.get(i);
            line.draw(g);
        }
        g.setColor(this.cols[this.index]);
        g.fillOval((float)(int)this.x, (float)(int)this.y, 50.0f, 50.0f);
        g.setColor(Color.yellow);
        g.fillRect(50.0f, (float)(200 + this.ypos), 40.0f, 40.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
        this.lshift = container.getInput().isKeyDown(42);
        this.rshift = container.getInput().isKeyDown(54);
        this.space = container.getInput().isKeyDown(57);
        if (this.controllerLeft[0]) {
            this.x -= delta * 0.1f;
        }
        if (this.controllerRight[0]) {
            this.x += delta * 0.1f;
        }
        if (this.controllerUp[0]) {
            this.y -= delta * 0.1f;
        }
        if (this.controllerDown[0]) {
            this.y += delta * 0.1f;
        }
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(600, 600, false);
                this.app.reinit();
            }
            catch (Exception e) {
                Log.error(e);
            }
        }
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        this.message = "You pressed key code " + key + " (character = " + c + ")";
    }
    
    @Override
    public void mousePressed(final int button, final int x, final int y) {
        if (button == 0) {
            this.buttonDown = true;
        }
        this.message = "Mouse pressed " + button + " " + x + "," + y;
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
        if (button == 0) {
            this.buttonDown = false;
        }
        this.message = "Mouse released " + button + " " + x + "," + y;
    }
    
    @Override
    public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
        System.out.println("CLICKED:" + x + "," + y + " " + clickCount);
    }
    
    @Override
    public void mouseWheelMoved(final int change) {
        this.message = "Mouse wheel moved: " + change;
        if (change < 0) {
            this.ypos -= 10;
        }
        if (change > 0) {
            this.ypos += 10;
        }
    }
    
    @Override
    public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
        if (this.buttonDown) {
            this.lines.add(new Line(oldx, oldy, newx, newy));
        }
    }
    
    @Override
    public void controllerButtonPressed(final int controller, final int button) {
        super.controllerButtonPressed(controller, button);
        ++this.index;
        this.index %= this.cols.length;
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new InputTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    private class Line
    {
        private int oldx;
        private int oldy;
        private int newx;
        private int newy;
        
        public Line(final int oldx, final int oldy, final int newx, final int newy) {
            this.oldx = oldx;
            this.oldy = oldy;
            this.newx = newx;
            this.newy = newy;
        }
        
        public void draw(final Graphics g) {
            g.drawLine((float)this.oldx, (float)this.oldy, (float)this.newx, (float)this.newy);
        }
    }
}
