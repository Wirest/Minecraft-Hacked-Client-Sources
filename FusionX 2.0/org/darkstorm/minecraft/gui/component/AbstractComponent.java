// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import java.awt.Dimension;
import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;
import org.darkstorm.minecraft.gui.listener.ComponentListener;
import java.util.List;
import java.awt.Color;
import org.darkstorm.minecraft.gui.theme.ComponentUI;
import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.theme.Theme;

public abstract class AbstractComponent implements Component
{
    private Container parent;
    private Theme theme;
    protected Rectangle area;
    protected ComponentUI ui;
    protected Color foreground;
    protected Color background;
    protected boolean enabled;
    protected boolean visible;
    private List<ComponentListener> listeners;
    
    public AbstractComponent() {
        this.parent = null;
        this.area = new Rectangle(0, 0, 0, 0);
        this.enabled = true;
        this.visible = true;
        this.listeners = new CopyOnWriteArrayList<ComponentListener>();
    }
    
    @Override
    public void render() {
        if (this.ui == null) {
            return;
        }
        this.ui.render(this);
    }
    
    @Override
    public void update() {
        if (this.ui == null) {
            return;
        }
        this.ui.handleUpdate(this);
    }
    
    protected ComponentUI getUI() {
        return this.theme.getUIForComponent(this);
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int button) {
        if (this.ui != null) {
            Rectangle[] interactableRegions;
            for (int length = (interactableRegions = this.ui.getInteractableRegions(this)).length, i = 0; i < length; ++i) {
                final Rectangle area = interactableRegions[i];
                if (area.contains(x, y)) {
                    this.ui.handleInteraction(this, new Point(x, y), button);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onMouseRelease(final int x, final int y, final int button) {
    }
    
    @Override
    public Theme getTheme() {
        return this.theme;
    }
    
    @Override
    public void setTheme(final Theme theme) {
        final Theme oldTheme = this.theme;
        this.theme = theme;
        if (theme == null) {
            this.ui = null;
            this.foreground = null;
            this.background = null;
            return;
        }
        this.ui = this.getUI();
        boolean changeArea;
        if (oldTheme != null) {
            final Dimension defaultSize = oldTheme.getUIForComponent(this).getDefaultSize(this);
            changeArea = (this.area.width == defaultSize.width && this.area.height == defaultSize.height);
        }
        else {
            changeArea = this.area.equals(new Rectangle(0, 0, 0, 0));
        }
        if (changeArea) {
            final Dimension defaultSize = this.ui.getDefaultSize(this);
            this.area = new Rectangle(this.area.x, this.area.y, defaultSize.width, defaultSize.height);
        }
        this.foreground = this.ui.getDefaultForegroundColor(this);
        this.background = this.ui.getDefaultBackgroundColor(this);
    }
    
    @Override
    public int getX() {
        return this.area.x;
    }
    
    @Override
    public int getY() {
        return this.area.y;
    }
    
    @Override
    public int getWidth() {
        return this.area.width;
    }
    
    @Override
    public int getHeight() {
        return this.area.height;
    }
    
    @Override
    public void setX(final int x) {
        this.area.x = x;
    }
    
    @Override
    public void setY(final int y) {
        this.area.y = y;
    }
    
    @Override
    public void setWidth(final int width) {
        this.area.width = width;
    }
    
    @Override
    public void setHeight(final int height) {
        this.area.height = height;
    }
    
    @Override
    public Color getBackgroundColor() {
        return this.background;
    }
    
    @Override
    public Color getForegroundColor() {
        return this.foreground;
    }
    
    @Override
    public void setBackgroundColor(final Color color) {
        this.background = color;
    }
    
    @Override
    public void setForegroundColor(final Color color) {
        this.foreground = color;
    }
    
    @Override
    public Point getLocation() {
        return this.area.getLocation();
    }
    
    @Override
    public Dimension getSize() {
        return this.area.getSize();
    }
    
    @Override
    public Rectangle getArea() {
        return this.area;
    }
    
    @Override
    public Container getParent() {
        return this.parent;
    }
    
    @Override
    public void setParent(final Container parent) {
        if (!parent.hasChild(this) || (this.parent != null && this.parent.hasChild(this))) {
            throw new IllegalArgumentException();
        }
        this.parent = parent;
    }
    
    @Override
    public void resize() {
        final Dimension defaultDimension = this.ui.getDefaultSize(this);
        this.setWidth(defaultDimension.width);
        this.setHeight(defaultDimension.height);
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        if (this.parent != null && !this.parent.isEnabled()) {
            this.enabled = false;
        }
        else {
            this.enabled = enabled;
        }
    }
    
    @Override
    public boolean isVisible() {
        return this.visible;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        if (this.parent != null && !this.parent.isVisible()) {
            this.visible = false;
        }
        else {
            this.visible = visible;
        }
    }
    
    protected void addListener(final ComponentListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
        // monitorexit(this.listeners)
    }
    
    protected void removeListener(final ComponentListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
        // monitorexit(this.listeners)
    }
    
    protected ComponentListener[] getListeners() {
        synchronized (this.listeners) {
            // monitorexit(this.listeners)
            return this.listeners.toArray(new ComponentListener[this.listeners.size()]);
        }
    }
}
