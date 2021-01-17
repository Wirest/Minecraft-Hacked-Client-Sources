// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.opengl.GL11;
import me.nico.hush.events.EventRender;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class WallHack extends Module
{
    public WallHack() {
        super("WallHack", "WallHack", 16777215, 0, Category.RENDER);
    }
    
    @EventTarget
    public void onRender(final EventRender event) {
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1100000.0f);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
