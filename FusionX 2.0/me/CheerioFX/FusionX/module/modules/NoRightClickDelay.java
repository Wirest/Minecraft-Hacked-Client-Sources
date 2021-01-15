// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoRightClickDelay extends Module
{
    public NoRightClickDelay() {
        super("NoRightClickDelay", 0, Category.PLAYER);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            Wrapper.mc.rightClickDelayTimer = 0;
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Wrapper.mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
