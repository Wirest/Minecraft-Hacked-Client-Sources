// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class InstantStop extends Module
{
    public static boolean motionY;
    
    static {
        InstantStop.motionY = false;
    }
    
    public boolean isMotionY() {
        return InstantStop.motionY;
    }
    
    public static void setMotionY(final boolean m) {
        InstantStop.motionY = m;
    }
    
    public InstantStop() {
        super("InstantStop", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onUpdate() {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (this.getState() && player.movementInput.moveForward == 0.0f && player.movementInput.moveStrafe == 0.0f) {
            player.motionX = 0.0;
            player.motionZ = 0.0;
            if (InstantStop.motionY) {
                player.motionY = 0.0;
            }
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
