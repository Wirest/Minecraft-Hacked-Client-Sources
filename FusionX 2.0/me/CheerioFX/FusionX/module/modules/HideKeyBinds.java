// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class HideKeyBinds extends Module
{
    public static boolean enabled;
    
    static {
        HideKeyBinds.enabled = false;
    }
    
    public HideKeyBinds() {
        super("HideKeyBinds", Category.OTHER);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    
    @Override
    public void onEnable() {
        HideKeyBinds.enabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        HideKeyBinds.enabled = false;
        super.onDisable();
    }
}
