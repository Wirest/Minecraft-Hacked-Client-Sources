// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.client.settings.KeyBinding;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public abstract class PathProcessor
{
    protected final Minecraft mc;
    protected final ArrayList<PathPos> path;
    protected int index;
    protected boolean done;
    protected boolean failed;
    private final KeyBinding[] controls;
    
    public PathProcessor(final ArrayList<PathPos> path) {
        this.mc = Minecraft.getMinecraft();
        this.controls = new KeyBinding[] { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSneak };
        if (path.isEmpty()) {
            throw new IllegalStateException("There is no path!");
        }
        this.path = path;
    }
    
    public abstract void process();
    
    public void stop() {
        this.releaseControls();
    }
    
    public void lockControls() {
    }
    
    public final void releaseControls() {
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public final boolean isDone() {
        return this.done;
    }
    
    public final boolean isFailed() {
        return this.failed;
    }
}
