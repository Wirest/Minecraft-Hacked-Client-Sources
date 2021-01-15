// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.material.Material;
import me.CheerioFX.FusionX.events.MoveEvent;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class MoonWalk extends Module
{
    public MoonWalk() {
        super("MoonWalk", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onEvent(final MoveEvent move) {
        if (MoonWalk.mc.thePlayer.onGround && MoonWalk.mc.gameSettings.keyBindJump.isPressed()) {
            MoonWalk.mc.thePlayer.motionY = 0.25;
        }
        else if (MoonWalk.mc.thePlayer.isAirBorne && !MoonWalk.mc.thePlayer.isInWater() && !MoonWalk.mc.thePlayer.isOnLadder() && !MoonWalk.mc.thePlayer.isInsideOfMaterial(Material.lava)) {
            MoonWalk.mc.thePlayer.motionY = 1.0E-6;
            move.y *= 1.0E-6;
            final EntityPlayerSP thePlayer = MoonWalk.mc.thePlayer;
            thePlayer.jumpMovementFactor *= 1.21337f;
        }
    }
}
