// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;
import java.awt.Point;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.AbstractContainer;

public class BasicFrame extends AbstractContainer implements Frame
{
    private String title;
    private Point dragOffset;
    private boolean pinned;
    private boolean pinnable;
    private boolean minimized;
    private boolean minimizable;
    private boolean closable;
    
    @Override
    public void render() {
        if (this.isDragging()) {
            if (Mouse.isButtonDown(0)) {
                final Point mouseLocation = RenderUtil.calculateMouseLocation();
                this.setX(mouseLocation.x - this.dragOffset.x);
                this.setY(mouseLocation.y - this.dragOffset.y);
            }
            else {
                this.setDragging(false);
            }
        }
        if (this.minimized) {
            if (this.ui != null) {
                this.ui.render(this);
            }
        }
        else {
            super.render();
        }
    }
    
    public BasicFrame() {
        this("");
    }
    
    public BasicFrame(final String title) {
        this.pinnable = true;
        this.minimizable = true;
        this.closable = true;
        this.setVisible(false);
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public void setTitle(final String title) {
        this.title = title;
    }
    
    @Override
    public boolean isDragging() {
        return this.dragOffset != null;
    }
    
    @Override
    public void setDragging(final boolean dragging) {
        if (dragging) {
            final Point mouseLocation = RenderUtil.calculateMouseLocation();
            this.dragOffset = new Point(mouseLocation.x - this.getX(), mouseLocation.y - this.getY());
        }
        else {
            this.dragOffset = null;
        }
    }
    
    @Override
    public boolean isPinned() {
        return this.pinned;
    }
    
    @Override
    public void setPinned(boolean pinned) {
        if (!this.pinnable) {
            pinned = false;
        }
        this.pinned = pinned;
    }
    
    @Override
    public boolean isPinnable() {
        return this.pinnable;
    }
    
    @Override
    public void setPinnable(final boolean pinnable) {
        if (!pinnable) {
            this.pinned = false;
        }
        this.pinnable = pinnable;
    }
    
    @Override
    public boolean isMinimized() {
        return this.minimized;
    }
    
    @Override
    public void setMinimized(boolean minimized) {
        if (!this.minimizable) {
            minimized = false;
        }
        this.minimized = minimized;
    }
    
    @Override
    public boolean isMinimizable() {
        return this.minimizable;
    }
    
    @Override
    public void setMinimizable(final boolean minimizable) {
        if (!minimizable) {
            this.minimized = false;
        }
        this.minimizable = minimizable;
    }
    
    @Override
    public void close() {
        if (this.closable) {
            this.setVisible(false);
        }
    }
    
    @Override
    public boolean isClosable() {
        return this.closable;
    }
    
    @Override
    public void setClosable(final boolean closable) {
        this.closable = closable;
    }
}
