// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class NoBob extends Module
{
    public NoBob() {
        super("NoBob", "NoBob", 38653, 0, Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        NoBob.mc.gameSettings.viewBobbing = false;
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        NoBob.mc.gameSettings.viewBobbing = true;
    }
}
