// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.events.MoveEvent;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Safewalk extends Module
{
    public Safewalk() {
        super("SafeWalk", 44, Category.MOVEMENT);
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
    public void onPlayerMove(final MoveEvent event) {
        double x = event.getX();
        final double y = event.getY();
        double z = event.getZ();
        if (Safewalk.mc.thePlayer.onGround) {
            final double increment = 0.05;
            while (x != 0.0) {
                if (!Safewalk.mc.theWorld.getCollidingBoundingBoxes(Safewalk.mc.thePlayer, Safewalk.mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, 0.0)).isEmpty()) {
                    break;
                }
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
            }
            while (z != 0.0) {
                if (!Safewalk.mc.theWorld.getCollidingBoundingBoxes(Safewalk.mc.thePlayer, Safewalk.mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, z)).isEmpty()) {
                    break;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
            while (x != 0.0 && z != 0.0 && Safewalk.mc.theWorld.getCollidingBoundingBoxes(Safewalk.mc.thePlayer, Safewalk.mc.thePlayer.getEntityBoundingBox().offset(x, -1.0, z)).isEmpty()) {
                if (x < increment && x >= -increment) {
                    x = 0.0;
                }
                else if (x > 0.0) {
                    x -= increment;
                }
                else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0;
                }
                else if (z > 0.0) {
                    z -= increment;
                }
                else {
                    z += increment;
                }
            }
        }
        event.setX(x);
        event.setY(y);
        event.setZ(z);
    }
}
