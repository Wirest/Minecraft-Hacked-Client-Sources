// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.theme.Theme;
import java.util.Iterator;
import org.darkstorm.minecraft.gui.layout.BasicLayoutManager;
import java.util.LinkedHashMap;
import org.darkstorm.minecraft.gui.layout.LayoutManager;
import org.darkstorm.minecraft.gui.layout.Constraint;
import java.util.Map;

public abstract class AbstractContainer extends AbstractComponent implements Container
{
    private final Map<Component, Constraint[]> children;
    private LayoutManager layoutManager;
    
    public AbstractContainer() {
        this.children = new LinkedHashMap<Component, Constraint[]>();
        this.layoutManager = new BasicLayoutManager();
    }
    
    @Override
    public void render() {
        super.render();
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                child.render();
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public LayoutManager getLayoutManager() {
        return this.layoutManager;
    }
    
    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager == null) {
            layoutManager = new BasicLayoutManager();
        }
        this.layoutManager = layoutManager;
        this.layoutChildren();
    }
    
    @Override
    public Component[] getChildren() {
        synchronized (this.children) {
            // monitorexit(this.children)
            return this.children.keySet().toArray(new Component[this.children.size()]);
        }
    }
    
    @Override
    public void add(final Component child, final Constraint... constraints) {
        synchronized (this.children) {
            final Container parent = child.getParent();
            if (parent != null && parent.hasChild(child)) {
                parent.remove(child);
            }
            this.children.put(child, constraints);
            if (!this.enabled) {
                child.setEnabled(false);
            }
            if (!this.visible) {
                child.setVisible(false);
            }
            child.setParent(this);
            child.setTheme(this.getTheme());
            this.layoutChildren();
        }
        // monitorexit(this.children)
    }
    
    @Override
    public Constraint[] getConstraints(final Component child) {
        if (child == null) {
            throw new NullPointerException();
        }
        synchronized (this.children) {
            final Constraint[] constraints = this.children.get(child);
            // monitorexit(this.children)
            return (constraints != null) ? constraints : new Constraint[0];
        }
    }
    
    @Override
    public Component getChildAt(final int x, final int y) {
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                if (child.getArea().contains(x, y)) {
                    // monitorexit(this.children)
                    return child;
                }
            }
            // monitorexit(this.children)
            return null;
        }
    }
    
    @Override
    public boolean remove(final Component child) {
        synchronized (this.children) {
            if (this.children.remove(child) != null) {
                this.layoutChildren();
                // monitorexit(this.children)
                return true;
            }
            // monitorexit(this.children)
            return false;
        }
    }
    
    @Override
    public boolean hasChild(final Component child) {
        synchronized (this.children) {
            // monitorexit(this.children)
            return this.children.get(child) != null;
        }
    }
    
    @Override
    public void setTheme(final Theme theme) {
        super.setTheme(theme);
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                child.setTheme(theme);
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void layoutChildren() {
        synchronized (this.children) {
            final Component[] components = this.children.keySet().toArray(new Component[this.children.size()]);
            final Rectangle[] areas = new Rectangle[components.length];
            for (int i = 0; i < components.length; ++i) {
                areas[i] = components[i].getArea();
            }
            final Constraint[][] allConstraints = this.children.values().toArray(new Constraint[this.children.size()][]);
            if (this.getTheme() != null) {
                this.layoutManager.reposition(this.ui.getChildRenderArea(this), areas, allConstraints);
            }
            Component[] array;
            for (int length = (array = components).length, j = 0; j < length; ++j) {
                final Component child = array[j];
                if (child instanceof Container) {
                    ((Container)child).layoutChildren();
                }
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void onMousePress(final int x, final int y, final int button) {
        super.onMousePress(x, y, button);
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                if (!child.isVisible()) {
                    continue;
                }
                if (child.getArea().contains(x, y)) {
                    continue;
                }
                Rectangle[] interactableRegions;
                for (int length = (interactableRegions = child.getTheme().getUIForComponent(child).getInteractableRegions(child)).length, i = 0; i < length; ++i) {
                    final Rectangle area = interactableRegions[i];
                    if (area.contains(x - child.getX(), y - child.getY())) {
                        child.onMousePress(x - child.getX(), y - child.getY(), button);
                        // monitorexit(this.children)
                        return;
                    }
                }
            }
            for (final Component child : this.children.keySet()) {
                if (!child.isVisible()) {
                    continue;
                }
                if (child.getArea().contains(x, y)) {
                    child.onMousePress(x - child.getX(), y - child.getY(), button);
                    // monitorexit(this.children)
                    return;
                }
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void onMouseRelease(final int x, final int y, final int button) {
        super.onMouseRelease(x, y, button);
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                if (!child.isVisible()) {
                    continue;
                }
                if (child.getArea().contains(x, y)) {
                    continue;
                }
                Rectangle[] interactableRegions;
                for (int length = (interactableRegions = child.getTheme().getUIForComponent(child).getInteractableRegions(child)).length, i = 0; i < length; ++i) {
                    final Rectangle area = interactableRegions[i];
                    if (area.contains(x - child.getX(), y - child.getY())) {
                        child.onMouseRelease(x - child.getX(), y - child.getY(), button);
                        // monitorexit(this.children)
                        return;
                    }
                }
            }
            for (final Component child : this.children.keySet()) {
                if (!child.isVisible()) {
                    continue;
                }
                if (child.getArea().contains(x, y)) {
                    child.onMouseRelease(x - child.getX(), y - child.getY(), button);
                    // monitorexit(this.children)
                    return;
                }
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        enabled = this.isEnabled();
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                child.setEnabled(enabled);
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        visible = this.isVisible();
        synchronized (this.children) {
            for (final Component child : this.children.keySet()) {
                child.setVisible(visible);
            }
        }
        // monitorexit(this.children)
    }
    
    @Override
    public void update() {
        Component[] children;
        for (int length = (children = this.getChildren()).length, i = 0; i < length; ++i) {
            final Component child = children[i];
            child.update();
        }
    }
}
