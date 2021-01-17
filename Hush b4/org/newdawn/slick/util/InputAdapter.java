// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

public class InputAdapter implements InputListener
{
    private boolean acceptingInput;
    
    public InputAdapter() {
        this.acceptingInput = true;
    }
    
    @Override
    public void controllerButtonPressed(final int controller, final int button) {
    }
    
    @Override
    public void controllerButtonReleased(final int controller, final int button) {
    }
    
    @Override
    public void controllerDownPressed(final int controller) {
    }
    
    @Override
    public void controllerDownReleased(final int controller) {
    }
    
    @Override
    public void controllerLeftPressed(final int controller) {
    }
    
    @Override
    public void controllerLeftReleased(final int controller) {
    }
    
    @Override
    public void controllerRightPressed(final int controller) {
    }
    
    @Override
    public void controllerRightReleased(final int controller) {
    }
    
    @Override
    public void controllerUpPressed(final int controller) {
    }
    
    @Override
    public void controllerUpReleased(final int controller) {
    }
    
    @Override
    public void inputEnded() {
    }
    
    @Override
    public boolean isAcceptingInput() {
        return this.acceptingInput;
    }
    
    public void setAcceptingInput(final boolean acceptingInput) {
        this.acceptingInput = acceptingInput;
    }
    
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
    public void mousePressed(final int button, final int x, final int y) {
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
    }
    
    @Override
    public void mouseWheelMoved(final int change) {
    }
    
    @Override
    public void setInput(final Input input) {
    }
    
    @Override
    public void mouseClicked(final int button, final int x, final int y, final int clickCount) {
    }
    
    @Override
    public void mouseDragged(final int oldx, final int oldy, final int newx, final int newy) {
    }
    
    @Override
    public void inputStarted() {
    }
}
