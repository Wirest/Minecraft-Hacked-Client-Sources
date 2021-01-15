// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.EventCameraShake;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoCameraShake extends Module
{
    public NoCameraShake() {
        super("NoCameraShake", 0, Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEvent(final EventCameraShake event) {
        event.setCancelled(true);
    }
}
