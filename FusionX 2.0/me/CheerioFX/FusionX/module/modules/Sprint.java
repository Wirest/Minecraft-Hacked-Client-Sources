// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Sprint extends Module
{
    private boolean always;
    private static boolean isSprintEnabled;
    
    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT);
        this.always = false;
    }
    
    @Override
    public void onEnable() {
        Sprint.isSprintEnabled = true;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Sprint.isSprintEnabled = false;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (this.always) {
                Sprint.mc.thePlayer.setSprinting(true);
            }
            else if (Sprint.mc.thePlayer.moveForward == 0.0f && Sprint.mc.thePlayer.moveStrafing == 0.0f) {
                Sprint.mc.thePlayer.setSprinting(false);
            }
            else {
                Sprint.mc.thePlayer.setSprinting(true);
            }
        }
    }
    
    public static boolean isSprintEnabled() {
        return Sprint.isSprintEnabled;
    }
}
