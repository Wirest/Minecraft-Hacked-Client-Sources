// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Timer extends Module
{
    public Timer() {
        super("Timer", 0, Category.MOVEMENT);
    }
    
    public float getMultiplier() {
        return FusionX.theClient.setmgr.getSetting(this, "Multiplier").getValFloat();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Multiplier", this, 2.0, 0.1, 5.0, false));
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            final net.minecraft.util.Timer timer = Timer.mc.timer;
            net.minecraft.util.Timer.timerSpeed = this.getMultiplier();
        }
        else {
            final net.minecraft.util.Timer timer2 = Timer.mc.timer;
            net.minecraft.util.Timer.timerSpeed = 1.0f;
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
