// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.render;

import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class FullBright extends Module
{
    public FullBright() {
        super("FullBright", "FullBright", 8863358, 0, Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        FullBright.mc.gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        FullBright.mc.gameSettings.gammaSetting = 0.0f;
    }
}
