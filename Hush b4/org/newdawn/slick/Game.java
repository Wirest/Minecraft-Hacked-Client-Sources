// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

public interface Game
{
    void init(final GameContainer p0) throws SlickException;
    
    void update(final GameContainer p0, final int p1) throws SlickException;
    
    void render(final GameContainer p0, final Graphics p1) throws SlickException;
    
    boolean closeRequested();
    
    String getTitle();
}
