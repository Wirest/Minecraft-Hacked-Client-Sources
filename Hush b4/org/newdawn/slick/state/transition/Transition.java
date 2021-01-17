// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state.transition;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface Transition
{
    void update(final StateBasedGame p0, final GameContainer p1, final int p2) throws SlickException;
    
    void preRender(final StateBasedGame p0, final GameContainer p1, final Graphics p2) throws SlickException;
    
    void postRender(final StateBasedGame p0, final GameContainer p1, final Graphics p2) throws SlickException;
    
    boolean isComplete();
    
    void init(final GameState p0, final GameState p1);
}
