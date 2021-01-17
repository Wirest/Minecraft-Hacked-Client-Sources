// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.gui;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import java.util.Iterator;
import org.newdawn.slick.InputListener;
import java.util.HashSet;
import org.newdawn.slick.Input;
import java.util.Set;
import org.newdawn.slick.util.InputAdapter;

public abstract class AbstractComponent extends InputAdapter
{
    private static AbstractComponent currentFocus;
    protected GUIContext container;
    protected Set listeners;
    private boolean focus;
    protected Input input;
    
    static {
        AbstractComponent.currentFocus = null;
    }
    
    public AbstractComponent(final GUIContext container) {
        this.focus = false;
        this.container = container;
        this.listeners = new HashSet();
        (this.input = container.getInput()).addPrimaryListener(this);
        this.setLocation(0, 0);
    }
    
    public void addListener(final ComponentListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final ComponentListener listener) {
        this.listeners.remove(listener);
    }
    
    protected void notifyListeners() {
        final Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().componentActivated(this);
        }
    }
    
    public abstract void render(final GUIContext p0, final Graphics p1) throws SlickException;
    
    public abstract void setLocation(final int p0, final int p1);
    
    public abstract int getX();
    
    public abstract int getY();
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public void setFocus(final boolean focus) {
        if (focus) {
            if (AbstractComponent.currentFocus != null) {
                AbstractComponent.currentFocus.setFocus(false);
            }
            AbstractComponent.currentFocus = this;
        }
        else if (AbstractComponent.currentFocus == this) {
            AbstractComponent.currentFocus = null;
        }
        this.focus = focus;
    }
    
    public boolean hasFocus() {
        return this.focus;
    }
    
    protected void consumeEvent() {
        this.input.consumeEvent();
    }
    
    @Override
    public void mouseReleased(final int button, final int x, final int y) {
        this.setFocus(Rectangle.contains((float)x, (float)y, (float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight()));
    }
}
