// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.ControllerButtonControl;
import org.newdawn.slick.command.MouseButtonControl;
import org.newdawn.slick.command.ControllerDirectionControl;
import org.newdawn.slick.command.Control;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.BasicGame;

public class InputProviderTest extends BasicGame implements InputProviderListener
{
    private Command attack;
    private Command jump;
    private Command run;
    private InputProvider provider;
    private String message;
    
    public InputProviderTest() {
        super("InputProvider Test");
        this.attack = new BasicCommand("attack");
        this.jump = new BasicCommand("jump");
        this.run = new BasicCommand("run");
        this.message = "";
    }
    
    @Override
    public void init(final GameContainer container) throws SlickException {
        (this.provider = new InputProvider(container.getInput())).addListener(this);
        this.provider.bindCommand(new KeyControl(203), this.run);
        this.provider.bindCommand(new KeyControl(30), this.run);
        this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.LEFT), this.run);
        this.provider.bindCommand(new KeyControl(200), this.jump);
        this.provider.bindCommand(new KeyControl(17), this.jump);
        this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.UP), this.jump);
        this.provider.bindCommand(new KeyControl(57), this.attack);
        this.provider.bindCommand(new MouseButtonControl(0), this.attack);
        this.provider.bindCommand(new ControllerButtonControl(0, 1), this.attack);
    }
    
    @Override
    public void render(final GameContainer container, final Graphics g) {
        g.drawString("Press A, W, Left, Up, space, mouse button 1,and gamepad controls", 10.0f, 50.0f);
        g.drawString(this.message, 100.0f, 150.0f);
    }
    
    @Override
    public void update(final GameContainer container, final int delta) {
    }
    
    @Override
    public void controlPressed(final Command command) {
        this.message = "Pressed: " + command;
    }
    
    @Override
    public void controlReleased(final Command command) {
        this.message = "Released: " + command;
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new InputProviderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
