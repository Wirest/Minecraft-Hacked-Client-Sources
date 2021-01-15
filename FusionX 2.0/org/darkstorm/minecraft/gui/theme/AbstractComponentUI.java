// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme;

import org.lwjgl.opengl.GL11;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.component.Container;
import java.awt.Color;
import org.darkstorm.minecraft.gui.component.Component;

public abstract class AbstractComponentUI<T extends Component> implements ComponentUI
{
    protected final Class<T> handledComponentClass;
    protected Color foreground;
    protected Color background;
    
    public AbstractComponentUI(final Class<T> handledComponentClass) {
        this.handledComponentClass = handledComponentClass;
    }
    
    @Override
    public void render(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        if (!component.isVisible()) {
            return;
        }
        this.renderComponent(this.handledComponentClass.cast(component));
    }
    
    protected abstract void renderComponent(final T p0);
    
    @Override
    public Rectangle getChildRenderArea(final Container container) {
        if (!Container.class.isAssignableFrom(this.handledComponentClass)) {
            throw new UnsupportedOperationException();
        }
        if (container == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(container)) {
            throw new IllegalArgumentException();
        }
        return this.getContainerChildRenderArea(this.handledComponentClass.cast(container));
    }
    
    protected Rectangle getContainerChildRenderArea(final T container) {
        return new Rectangle(new Point(0, 0), container.getSize());
    }
    
    @Override
    public Dimension getDefaultSize(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        return this.getDefaultComponentSize(this.handledComponentClass.cast(component));
    }
    
    protected abstract Dimension getDefaultComponentSize(final T p0);
    
    protected void translateComponent(final Component component, final boolean reverse) {
        for (Component parent = component.getParent(); parent != null; parent = parent.getParent()) {
            GL11.glTranslated((double)((reverse ? -1 : 1) * parent.getX()), (double)((reverse ? -1 : 1) * parent.getY()), 0.0);
        }
        GL11.glTranslated((double)((reverse ? -1 : 1) * component.getX()), (double)((reverse ? -1 : 1) * component.getY()), 0.0);
    }
    
    @Override
    public Color getDefaultBackgroundColor(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        return this.getBackgroundColor(this.handledComponentClass.cast(component));
    }
    
    protected Color getBackgroundColor(final T component) {
        return this.background;
    }
    
    @Override
    public Color getDefaultForegroundColor(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        return this.getForegroundColor(this.handledComponentClass.cast(component));
    }
    
    protected Color getForegroundColor(final T component) {
        return this.foreground;
    }
    
    @Override
    public Rectangle[] getInteractableRegions(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        return this.getInteractableComponentRegions(this.handledComponentClass.cast(component));
    }
    
    protected Rectangle[] getInteractableComponentRegions(final T component) {
        return new Rectangle[0];
    }
    
    @Override
    public void handleInteraction(final Component component, final Point location, final int button) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        this.handleComponentInteraction(this.handledComponentClass.cast(component), location, button);
    }
    
    protected void handleComponentInteraction(final T component, final Point location, final int button) {
    }
    
    @Override
    public void handleUpdate(final Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (!this.handledComponentClass.isInstance(component)) {
            throw new IllegalArgumentException();
        }
        this.handleComponentUpdate(this.handledComponentClass.cast(component));
    }
    
    protected void handleComponentUpdate(final T component) {
    }
}
