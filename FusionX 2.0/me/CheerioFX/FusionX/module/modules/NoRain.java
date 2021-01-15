// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class NoRain extends Module
{
    boolean isRain;
    
    public NoRain() {
        super("NoRain", 0, Category.RENDER);
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && NoRain.mc.theWorld.isRaining()) {
            if (NoRain.mc.isSingleplayer()) {
                NoRain.mc.theWorld.setRainStrength(0.0f);
            }
            else {
                NoRain.mc.theWorld.setRainStrength(0.0f);
                NoRain.mc.theWorld.setRainStrength(0.0f);
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.isRain = NoRain.mc.theWorld.isRaining();
    }
    
    @Override
    public void onDisable() {
        if (this.isRain) {
            NoRain.mc.theWorld.setRainStrength(1.0f);
        }
        super.onDisable();
    }
}
