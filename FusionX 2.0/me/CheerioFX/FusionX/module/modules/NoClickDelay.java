// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoClickDelay extends Module
{
    public static double speed;
    
    static {
        NoClickDelay.speed = 10.0;
    }
    
    public NoClickDelay() {
        super("NoClickDelay", 0, Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            NoClickDelay.speed = 100.0;
            if (NoClickDelay.mc.gameSettings.keyBindAttack.pressed) {
                for (int i = 0; i < Math.abs(NoClickDelay.speed); ++i) {
                    NoClickDelay.mc.clickMouse();
                }
            }
            if (NoClickDelay.mc.gameSettings.keyBindUseItem.pressed) {
                for (int i = 0; i < NoClickDelay.speed; ++i) {
                    NoClickDelay.mc.rightClickMouse();
                }
            }
        }
    }
}
