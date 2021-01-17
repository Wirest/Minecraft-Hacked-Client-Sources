// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.states;

import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.transition.CrossStateTransition;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.state.BasicGameState;

public class TestState1 extends BasicGameState
{
    public static final int ID = 1;
    private Font font;
    private StateBasedGame game;
    
    @Override
    public int getID() {
        return 1;
    }
    
    @Override
    public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
        this.game = game;
        this.font = new AngelCodeFont("testdata/demo2.fnt", "testdata/demo2_00.tga");
    }
    
    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g) {
        g.setFont(this.font);
        g.setColor(Color.white);
        g.drawString("State Based Game Test", 100.0f, 100.0f);
        g.drawString("Numbers 1-3 will switch between states.", 150.0f, 300.0f);
        g.setColor(Color.red);
        g.drawString("This is State 1", 200.0f, 50.0f);
    }
    
    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta) {
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        if (key == 3) {
            final GameState target = this.game.getState(2);
            final long start = System.currentTimeMillis();
            final CrossStateTransition t = new CrossStateTransition(target) {
                @Override
                public boolean isComplete() {
                    return System.currentTimeMillis() - start > 2000L;
                }
                
                @Override
                public void init(final GameState firstState, final GameState secondState) {
                }
            };
            this.game.enterState(2, t, new EmptyTransition());
        }
        if (key == 4) {
            this.game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }
}
