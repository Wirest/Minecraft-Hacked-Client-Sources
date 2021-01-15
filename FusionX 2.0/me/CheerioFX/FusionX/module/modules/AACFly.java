// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.events.KeyPressedEvent;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.material.Material;
import me.CheerioFX.FusionX.events.MoveEvent;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class AACFly extends Module
{
    public AACFly() {
        super("AACFly", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            AACFly.mc.thePlayer.setSprinting(false);
            AACFly.mc.thePlayer.setSneaking(false);
        }
        super.onUpdate();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onMove(final MoveEvent move) {
        if (AACFly.mc.thePlayer.isAirBorne && !AACFly.mc.thePlayer.isInWater() && !AACFly.mc.thePlayer.isOnLadder() && !AACFly.mc.thePlayer.isInsideOfMaterial(Material.lava)) {
            AACFly.mc.thePlayer.motionY = 1.0E-6;
            move.y *= 1.0E-6;
            final EntityPlayerSP thePlayer = AACFly.mc.thePlayer;
            thePlayer.jumpMovementFactor *= 1.21337f;
        }
    }
    
    @EventTarget
    public void onKeyPress(final KeyPressedEvent event) {
        if (event.getEventKey() == AACFly.mc.gameSettings.keyBindSneak.getKeyCode() || event.getEventKey() == AACFly.mc.gameSettings.keyBindSprint.getKeyCode()) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        AACFly.mc.thePlayer.setSprinting(false);
        AACFly.mc.thePlayer.setSneaking(false);
        event.setSneaking(false);
        event.setSprinting(false);
        event.pitch = 0.0f;
        event.yaw = 0.0f;
    }
}
