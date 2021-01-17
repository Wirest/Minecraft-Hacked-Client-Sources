// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state.transition;

import org.newdawn.slick.state.GameState;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import java.util.ArrayList;

public class CombinedTransition implements Transition
{
    private ArrayList transitions;
    
    public CombinedTransition() {
        this.transitions = new ArrayList();
    }
    
    public void addTransition(final Transition t) {
        this.transitions.add(t);
    }
    
    @Override
    public boolean isComplete() {
        for (int i = 0; i < this.transitions.size(); ++i) {
            if (!this.transitions.get(i).isComplete()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void postRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
        for (int i = this.transitions.size() - 1; i >= 0; --i) {
            this.transitions.get(i).postRender(game, container, g);
        }
    }
    
    @Override
    public void preRender(final StateBasedGame game, final GameContainer container, final Graphics g) throws SlickException {
        for (int i = 0; i < this.transitions.size(); ++i) {
            this.transitions.get(i).postRender(game, container, g);
        }
    }
    
    @Override
    public void update(final StateBasedGame game, final GameContainer container, final int delta) throws SlickException {
        for (int i = 0; i < this.transitions.size(); ++i) {
            final Transition t = this.transitions.get(i);
            if (!t.isComplete()) {
                t.update(game, container, delta);
            }
        }
    }
    
    @Override
    public void init(final GameState firstState, final GameState secondState) {
        for (int i = this.transitions.size() - 1; i >= 0; --i) {
            this.transitions.get(i).init(firstState, secondState);
        }
    }
}
