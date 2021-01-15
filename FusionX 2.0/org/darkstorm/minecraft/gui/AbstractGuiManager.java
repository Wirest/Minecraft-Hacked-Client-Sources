// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.component.Frame;
import java.util.List;

public abstract class AbstractGuiManager implements GuiManager
{
    private final List<Frame> frames;
    protected Theme theme;
    
    public AbstractGuiManager() {
        this.frames = new CopyOnWriteArrayList<Frame>();
    }
    
    @Override
    public abstract void setup();
    
    @Override
    public void addFrame(final Frame frame) {
        frame.setTheme(this.theme);
        this.frames.add(0, frame);
    }
    
    @Override
    public void removeFrame(final Frame frame) {
        this.frames.remove(frame);
    }
    
    @Override
    public Frame[] getFrames() {
        return this.frames.toArray(new Frame[this.frames.size()]);
    }
    
    @Override
    public void bringForward(final Frame frame) {
        if (this.frames.remove(frame)) {
            this.frames.add(0, frame);
        }
    }
    
    @Override
    public Theme getTheme() {
        return this.theme;
    }
    
    @Override
    public void setTheme(final Theme theme) {
        this.theme = theme;
        for (final Frame frame : this.frames) {
            frame.setTheme(theme);
        }
        this.resizeComponents();
    }
    
    protected abstract void resizeComponents();
    
    @Override
    public void render() {
        final Frame[] frames = this.getFrames();
        for (int i = frames.length - 1; i >= 0; --i) {
            frames[i].render();
        }
    }
    
    @Override
    public void renderPinned() {
        final Frame[] frames = this.getFrames();
        for (int i = frames.length - 1; i >= 0; --i) {
            if (frames[i].isPinned()) {
                frames[i].render();
            }
        }
    }
    
    @Override
    public void update() {
        final Frame[] frames = this.getFrames();
        for (int i = frames.length - 1; i >= 0; --i) {
            frames[i].update();
        }
    }
}
