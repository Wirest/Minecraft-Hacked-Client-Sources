// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.BasicGame;

public class GUITest extends BasicGame implements ComponentListener
{
    private Image image;
    private MouseOverArea[] areas;
    private GameContainer container;
    private String message;
    private TextField field;
    private TextField field2;
    private Image background;
    private Font font;
    private AppGameContainer app;
    
    public GUITest() {
        super("GUI Test");
        this.areas = new MouseOverArea[4];
        this.message = "Demo Menu System with stock images";
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            (this.app = (AppGameContainer)container).setIcon("testdata/icon.tga");
        }
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
        this.field = new TextField(container, this.font, 150, 20, 500, 35, new ComponentListener() {
            @Override
            public void componentActivated(final AbstractComponent source) {
                GUITest.access$1(GUITest.this, "Entered1: " + GUITest.this.field.getText());
                GUITest.this.field2.setFocus(true);
            }
        });
        (this.field2 = new TextField(container, this.font, 150, 70, 500, 35, new ComponentListener() {
            @Override
            public void componentActivated(final AbstractComponent source) {
                GUITest.access$1(GUITest.this, "Entered2: " + GUITest.this.field2.getText());
                GUITest.this.field.setFocus(true);
            }
        })).setBorderColor(Color.red);
        this.container = container;
        this.image = new Image("testdata/logo.tga");
        this.background = new Image("testdata/dungeontiles.gif");
        container.setMouseCursor("testdata/cursor.tga", 0, 0);
        for (int i = 0; i < 4; ++i) {
            (this.areas[i] = new MouseOverArea(container, this.image, 300, 100 + i * 100, 200, 90, this)).setNormalColor(new Color(1.0f, 1.0f, 1.0f, 0.8f));
            this.areas[i].setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 0.9f));
        }
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        this.background.draw(0.0f, 0.0f, 800.0f, 500.0f);
        for (int i = 0; i < 4; ++i) {
            this.areas[i].render(container, g);
        }
        this.field.render(container, g);
        this.field2.render(container, g);
        g.setFont(this.font);
        g.drawString(this.message, 200.0f, 550.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 60) {
            this.app.setDefaultMouseCursor();
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(640, 480, false);
            }
            catch (SlickException e) {
                Log.error(e);
            }
        }
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new GUITest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void componentActivated(final AbstractComponent source) {
        System.out.println("ACTIVL : " + source);
        for (int i = 0; i < 4; ++i) {
            if (source == this.areas[i]) {
                this.message = "Option " + (i + 1) + " pressed!";
            }
        }
    }
    
    static /* synthetic */ void access$1(final GUITest guiTest, final String message) {
        guiTest.message = message;
    }
}
