// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Sprint extends Module
{
    public Sprint() {
        super("Sprint", "Sprint", 13856298, 36, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = Sprint.mc;
        if (Minecraft.thePlayer != null) {
            final Minecraft mc2 = Sprint.mc;
            if (Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6 && !Sprint.mc.gameSettings.keyBindSneak.pressed && (Sprint.mc.gameSettings.keyBindForward.pressed || Sprint.mc.gameSettings.keyBindLeft.pressed || Sprint.mc.gameSettings.keyBindRight.pressed || Sprint.mc.gameSettings.keyBindBack.pressed)) {
                final Minecraft mc3 = Sprint.mc;
                Minecraft.thePlayer.setSprinting(true);
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        final Minecraft mc = Sprint.mc;
        Minecraft.thePlayer.setSprinting(false);
    }
}
