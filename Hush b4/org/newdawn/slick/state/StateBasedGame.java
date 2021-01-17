// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.state;

import java.util.Iterator;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.GameContainer;
import java.util.HashMap;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.Game;

public abstract class StateBasedGame implements Game, InputListener
{
    private HashMap states;
    private GameState currentState;
    private GameState nextState;
    private GameContainer container;
    private String title;
    private Transition enterTransition;
    private Transition leaveTransition;
    
    public StateBasedGame(final String name) {
        this.states = new HashMap();
        this.title = name;
        this.currentState = new BasicGameState() {
            @Override
            public int getID() {
                return -1;
            }
            
            @Override
            public void init(final GameContainer container, final StateBasedGame game) throws SlickException {
            }
            
            public void render(final StateBasedGame game, final Graphics g) throws SlickException {
            }
            
            @Override
            public void update(final GameContainer container, final StateBasedGame game, final int delta) throws SlickException {
            }
            
            @Override
            public void render(final GameContainer container, final StateBasedGame game, final Graphics g) throws SlickException {
            }
        };
    }
    
    @Override
    public void inputStarted() {
    }
    
    public int getStateCount() {
        return this.states.keySet().size();
    }
    
    public int getCurrentStateID() {
        return this.currentState.getID();
    }
    
    public GameState getCurrentState() {
        return this.currentState;
    }
    
    @Override
    public void setInput(final Input input) {
    }
    
    public void addState(final GameState state) {
        this.states.put(new Integer(state.getID()), state);
        if (this.currentState.getID() == -1) {
            this.currentState = state;
        }
    }
    
    public GameState getState(final int id) {
        return this.states.get(new Integer(id));
    }
    
    public void enterState(final int id) {
        this.enterState(id, new EmptyTransition(), new EmptyTransition());
    }
    
    public void enterState(final int id, Transition leave, Transition enter) {
        if (leave == null) {
            leave = new EmptyTransition();
        }
        if (enter == null) {
            enter = new EmptyTransition();
        }
        this.leaveTransition = leave;
        this.enterTransition = enter;
        this.nextState = this.getState(id);
        if (this.nextState == null) {
            throw new RuntimeException("No game state registered with the ID: " + id);
        }
        this.leaveTransition.init(this.currentState, this.nextState);
    }
    
    @Override
    public final void init(final GameContainer container) throws SlickException {
        this.initStatesList(this.container = container);
        for (final GameState state : this.states.values()) {
            state.init(container, this);
        }
        if (this.currentState != null) {
            this.currentState.enter(container, this);
        }
    }
    
    public abstract void initStatesList(final GameContainer p0) throws SlickException;
    
    @Override
    public final void render(final GameContainer container, final Graphics g) throws SlickException {
        this.preRenderState(container, g);
        if (this.leaveTransition != null) {
            this.leaveTransition.preRender(this, container, g);
        }
        else if (this.enterTransition != null) {
            this.enterTransition.preRender(this, container, g);
        }
        this.currentState.render(container, this, g);
        if (this.leaveTransition != null) {
            this.leaveTransition.postRender(this, container, g);
        }
        else if (this.enterTransition != null) {
            this.enterTransition.postRender(this, container, g);
        }
        this.postRenderState(container, g);
    }
    
    protected void preRenderState(final GameContainer container, final Graphics g) throws SlickException {
    }
    
    protected void postRenderState(final GameContainer container, final Graphics g) throws SlickException {
    }
    
    @Override
    public final void update(final GameContainer container, final int delta) throws SlickException {
        this.preUpdateState(container, delta);
        if (this.leaveTransition != null) {
            this.leaveTransition.update(this, container, delta);
            if (!this.leaveTransition.isComplete()) {
                return;
            }
            this.currentState.leave(container, this);
            final GameState prevState = this.currentState;
            this.currentState = this.nextState;
            this.nextState = null;
            this.leaveTransition = null;
            this.currentState.enter(container, this);
            if (this.enterTransition != null) {
                this.enterTransition.init(this.currentState, prevState);
            }
        }
        if (this.enterTransition != null) {
            this.enterTransition.update(this, container, delta);
            if (!this.enterTransition.isComplete()) {
                return;
            }
            this.enterTransition = null;
        }
        this.currentState.update(container, this, delta);
        this.postUpdateState(container, delta);
    }
    
    protected void preUpdateState(final GameContainer container, final int delta) throws SlickException {
    }
    
    protected void postUpdateState(final GameContainer container, final int delta) throws SlickException {
    }
    
    private boolean transitioning() {
        return this.leaveTransition != null || this.enterTransition != null;
    }
    
    @Override
    public boolean closeRequested() {
        return true;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    public GameContainer getContainer() {
        return this.container;
    }
    
    @Override
    public void controllerButtonPressed(final int controller, final int button) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerButtonPressed(controller, button);
    }
    
    @Override
    public void controllerButtonReleased(final int controller, final int button) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerButtonReleased(controller, button);
    }
    
    @Override
    public void controllerDownPressed(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerDownPressed(controller);
    }
    
    @Override
    public void controllerDownReleased(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerDownReleased(controller);
    }
    
    @Override
    public void controllerLeftPressed(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerLeftPressed(controller);
    }
    
    @Override
    public void controllerLeftReleased(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerLeftReleased(controller);
    }
    
    @Override
    public void controllerRightPressed(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerRightPressed(controller);
    }
    
    @Override
    public void controllerRightReleased(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerRightReleased(controller);
    }
    
    @Override
    public void controllerUpPressed(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerUpPressed(controller);
    }
    
    @Override
    public void controllerUpReleased(final int controller) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.controllerUpReleased(controller);
    }
    
    @Override
    public void keyPressed(final int key, final char c) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.keyPressed(key, c);
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.keyReleased(key, c);
    }
    
    @Override
    public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseMoved(oldx, oldy, newx, newy);
    }
    
    @Override
    public void mouseDragged(final int oldx, final int oldy, final int newx, final int newy) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseDragged(oldx, oldy, newx, newy);
    }
    
    @Override
    public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseClicked(button, x, y, clickCount);
    }
    
    @Override
    public void mousePressed(final int button, final int x, final int y) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mousePressed(button, x, y);
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseReleased(button, x, y);
    }
    
    @Override
    public boolean isAcceptingInput() {
        return !this.transitioning() && this.currentState.isAcceptingInput();
    }
    
    @Override
    public void inputEnded() {
    }
    
    @Override
    public void mouseWheelMoved(final int newValue) {
        if (this.transitioning()) {
            return;
        }
        this.currentState.mouseWheelMoved(newValue);
    }
}
