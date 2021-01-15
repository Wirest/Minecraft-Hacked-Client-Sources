// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class PotionSaver extends Module
{
    public PotionSaver() {
        super("PotionSaver", 0, Category.PLAYER);
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
    public void beforeMotion(final EventPreMotionUpdates event) {
        if (PotionSaver.mc.thePlayer.motionX == 0.0 && PotionSaver.mc.thePlayer.motionZ == 0.0 && PotionSaver.mc.thePlayer.onGround) {
            event.setCancelled(true);
            for (final Object effect : PotionSaver.mc.thePlayer.getActivePotionEffects()) {
                ((PotionEffect)effect).incrementDuration();
            }
        }
    }
}
