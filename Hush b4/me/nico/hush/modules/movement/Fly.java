// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import net.minecraft.client.Minecraft;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Fly extends Module
{
    public Fly() {
        super("Fly", "Fly", 11462492, 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        final Minecraft mc = Fly.mc;
        Minecraft.thePlayer.capabilities.allowFlying = true;
        final Minecraft mc2 = Fly.mc;
        Minecraft.thePlayer.capabilities.isFlying = true;
    }
    
    @Override
    public void onDisable() {
        final Minecraft mc = Fly.mc;
        Minecraft.thePlayer.capabilities.allowFlying = false;
        final Minecraft mc2 = Fly.mc;
        Minecraft.thePlayer.capabilities.isFlying = false;
    }
}
