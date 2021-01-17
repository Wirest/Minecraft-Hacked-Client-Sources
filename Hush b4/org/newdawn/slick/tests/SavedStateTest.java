// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SavedState;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.BasicGame;

public class SavedStateTest extends BasicGame implements ComponentListener
{
    private TextField name;
    private TextField age;
    private String nameValue;
    private int ageValue;
    private SavedState state;
    private String message;
    private static AppGameContainer container;
    
    public SavedStateTest() {
        super("Saved State Test");
        this.nameValue = "none";
        this.ageValue = 0;
        this.message = "Enter a name and age to store";
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        this.state = new SavedState("testdata");
        this.nameValue = this.state.getString("name", "DefaultName");
        this.ageValue = (int)this.state.getNumber("age", 64.0);
        this.name = new TextField(container, container.getDefaultFont(), 100, 100, 300, 20, this);
        this.age = new TextField(container, container.getDefaultFont(), 100, 150, 201, 20, this);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        this.name.render(container, g);
        this.age.render(container, g);
        container.getDefaultFont().drawString(100.0f, 300.0f, "Stored Name: " + this.nameValue);
        container.getDefaultFont().drawString(100.0f, 350.0f, "Stored Age: " + this.ageValue);
        container.getDefaultFont().drawString(200.0f, 500.0f, this.message);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (key == 1) {
            System.exit(0);
        }
    }
    
    public static void main(final String[] argv) {
        try {
            (SavedStateTest.container = new AppGameContainer(new SavedStateTest())).setDisplayMode(800, 600, false);
            SavedStateTest.container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void componentActivated(final AbstractComponent source) {
        if (source == this.name) {
            this.nameValue = this.name.getText();
            this.state.setString("name", this.nameValue);
        }
        if (source == this.age) {
            try {
                this.ageValue = Integer.parseInt(this.age.getText());
                this.state.setNumber("age", this.ageValue);
            }
            catch (NumberFormatException ex) {}
        }
        try {
            this.state.save();
        }
        catch (Exception e) {
            this.message = String.valueOf(System.currentTimeMillis()) + " : Failed to save state";
        }
    }
}
