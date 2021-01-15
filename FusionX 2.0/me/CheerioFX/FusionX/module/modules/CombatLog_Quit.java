// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class CombatLog_Quit extends Module
{
    int delay;
    boolean done;
    
    public CombatLog_Quit() {
        super("Multi-Server", 0, Category.SERVER, false);
        this.delay = 0;
        this.done = false;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            ++this.delay;
            if (this.done && this.delay >= 10) {
                this.toggleModule();
                this.done = false;
                this.delay = 0;
            }
        }
    }
    
    @Override
    public void onEnable() {
        CombatLog_Quit.mc.freeMemory();
        this.done = true;
        super.onEnable();
    }
}
