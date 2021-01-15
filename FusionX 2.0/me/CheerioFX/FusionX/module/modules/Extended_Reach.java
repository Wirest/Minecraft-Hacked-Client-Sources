// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Extended_Reach extends Module
{
    public static boolean isModEnabled;
    public static boolean creativeReach;
    public static float reach;
    
    static {
        Extended_Reach.isModEnabled = false;
        Extended_Reach.creativeReach = true;
        Extended_Reach.reach = 7.0f;
    }
    
    public Extended_Reach() {
        super("Extended Reach", 0, Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        Extended_Reach.isModEnabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Extended_Reach.isModEnabled = false;
        super.onDisable();
    }
}
