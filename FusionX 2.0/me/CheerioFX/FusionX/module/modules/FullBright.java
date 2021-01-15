// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class FullBright extends Module
{
    public FullBright() {
        super("Fullbright", 0, Category.RENDER);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            Wrapper.mc.gameSettings.gammaSetting = 10.0f;
        }
        else {
            Wrapper.mc.gameSettings.gammaSetting = 1.0f;
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
