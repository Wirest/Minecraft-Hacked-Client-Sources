// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state.transition;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class EmptyTransition implements Transition
{
    @Override
    public boolean isComplete() {
        return true;
    }
    
    @Override
    public void postRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public void preRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public void update(final StateBasedGame game, final GameContainer container, final int delta) throws SlickException {
    }
    
    @Override
    public void init(final GameState firstState, final GameState secondState) {
    }
}
