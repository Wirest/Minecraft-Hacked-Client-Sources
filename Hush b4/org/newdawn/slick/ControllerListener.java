// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

public interface ControllerListener extends ControlledInputReciever
{
    void controllerLeftPressed(final int p0);
    
    void controllerLeftReleased(final int p0);
    
    void controllerRightPressed(final int p0);
    
    void controllerRightReleased(final int p0);
    
    void controllerUpPressed(final int p0);
    
    void controllerUpReleased(final int p0);
    
    void controllerDownPressed(final int p0);
    
    void controllerDownReleased(final int p0);
    
    void controllerButtonPressed(final int p0, final int p1);
    
    void controllerButtonReleased(final int p0, final int p1);
}
