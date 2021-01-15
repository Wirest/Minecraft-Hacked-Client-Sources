// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class InstantDrop extends Module
{
    public InstantDrop() {
        super("InstantDrop", 0, Category.MOVEMENT);
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
            boolean firstime = true;
            if (!InstantDrop.mc.thePlayer.onGround && InstantDrop.mc.thePlayer.fallDistance > 2.6945615 && firstime) {
                Wrapper.damagePlayer(0.1);
                firstime = false;
            }
            if (InstantDrop.mc.thePlayer.onGround) {
                firstime = true;
            }
        }
        super.onUpdate();
    }
}
