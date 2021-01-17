// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.tests.states.TestState3;
import org.newdawn.slick.tests.states.TestState2;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.tests.states.TestState1;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class StateBasedTest extends StateBasedGame
{
    public StateBasedTest() {
        super("State Based Test");
    }
    
    @Override
    public void initStatesList(final GameContainer container) {
        this.addState(new TestState1());
        this.addState(new TestState2());
        this.addState(new TestState3());
    }
    
    public static void main(final String[] argv) {
        try {
            final AppGameContainer container = new AppGameContainer(new StateBasedTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
