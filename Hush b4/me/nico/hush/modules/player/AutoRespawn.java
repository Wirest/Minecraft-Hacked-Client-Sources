// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class AutoRespawn extends Module
{
    public AutoRespawn() {
        super("AutoRespawn", "AutoRespawn", 14425552, 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = AutoRespawn.mc;
        Minecraft.thePlayer.respawnPlayer();
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
