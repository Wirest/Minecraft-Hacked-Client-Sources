// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.world;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class SafeWalk extends Module
{
    public SafeWalk() {
        super("SafeWalk", "SafeWalk", 14620696, 0, Category.WORLD);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = SafeWalk.mc;
        if (Minecraft.thePlayer != null && SafeWalk.mc.theWorld != null) {
            final Minecraft mc2 = SafeWalk.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc3 = SafeWalk.mc;
            final double y = Minecraft.thePlayer.posY - 1.0;
            final Minecraft mc4 = SafeWalk.mc;
            final BlockPos BP = new BlockPos(posX, y, Minecraft.thePlayer.posZ);
            if (SafeWalk.mc.theWorld.getBlockState(BP).getBlock() == Blocks.air) {
                SafeWalk.mc.gameSettings.keyBindSneak.pressed = true;
            }
            else {
                SafeWalk.mc.gameSettings.keyBindSneak.pressed = false;
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
        SafeWalk.mc.gameSettings.keyBindSneak.pressed = false;
    }
}
