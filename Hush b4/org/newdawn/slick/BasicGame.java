// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

public abstract class BasicGame implements Game, InputListener
{
    private static final int MAX_CONTROLLERS = 20;
    private static final int MAX_CONTROLLER_BUTTONS = 100;
    private String title;
    protected boolean[] controllerLeft;
    protected boolean[] controllerRight;
    protected boolean[] controllerUp;
    protected boolean[] controllerDown;
    protected boolean[][] controllerButton;
    
    public BasicGame(final String title) {
        this.controllerLeft = new boolean[20];
        this.controllerRight = new boolean[20];
        this.controllerUp = new boolean[20];
        this.controllerDown = new boolean[20];
        this.controllerButton = new boolean[20][100];
        this.title = title;
    }
    
    @Override
    public void setInput(final Input input) {
    }
    
    @Override
    public boolean closeRequested() {
        return true;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public abstract void init(final GameContainer p0) throws SlickException;
    
    @Override
    public void keyPressed(final int key, final char c) {
    }
    
    @Override
    public void keyReleased(final int key, final char c) {
    }
    
    @Override
    public void mouseMoved(final int oldx, final int oldy, final int newx, final int newy) {
    }
    
    @Override
    public void mouseDragged(final int oldx, final int oldy, final int newx, final int newy) {
    }
    
    @Override
    public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
    }
    
    @Override
    public void mousePressed(final int button, final int x, final int y) {
    }
    
    @Override
    public void controllerButtonPressed(final int controller, final int button) {
        this.controllerButton[controller][button] = true;
    }
    
    @Override
    public void controllerButtonReleased(final int controller, final int button) {
        this.controllerButton[controller][button] = false;
    }
    
    @Override
    public void controllerDownPressed(final int controller) {
        this.controllerDown[controller] = true;
    }
    
    @Override
    public void controllerDownReleased(final int controller) {
        this.controllerDown[controller] = false;
    }
    
    @Override
    public void controllerLeftPressed(final int controller) {
        this.controllerLeft[controller] = true;
    }
    
    @Override
    public void controllerLeftReleased(final int controller) {
        this.controllerLeft[controller] = false;
    }
    
    @Override
    public void controllerRightPressed(final int controller) {
        this.controllerRight[controller] = true;
    }
    
    @Override
    public void controllerRightReleased(final int controller) {
        this.controllerRight[controller] = false;
    }
    
    @Override
    public void controllerUpPressed(final int controller) {
        this.controllerUp[controller] = true;
    }
    
    @Override
    public void controllerUpReleased(final int controller) {
        this.controllerUp[controller] = false;
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
    }
    
    @Override
    public abstract void update(final GameContainer p0, final int p1) throws SlickException;
    
    @Override
    public void mouseWheelMoved(final int change) {
    }
    
    @Override
    public boolean isAcceptingInput() {
        return true;
    }
    
    @Override
    public void inputEnded() {
    }
    
    @Override
    public void inputStarted() {
    }
}
