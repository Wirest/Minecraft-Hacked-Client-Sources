// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.player;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Parkour extends Module
{
    public Parkour() {
        super("Parkour", "Parkour", 14620696, 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.isEnabled()) {
            final Minecraft mc = Parkour.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc2 = Parkour.mc;
            final double y = Minecraft.thePlayer.posY - 1.0;
            final Minecraft mc3 = Parkour.mc;
            final BlockPos pos = new BlockPos(posX, y, Minecraft.thePlayer.posZ);
            if (Parkour.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
                final Minecraft mc4 = Parkour.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc5 = Parkour.mc;
                    Minecraft.thePlayer.jump();
                    final Minecraft mc6 = Parkour.mc;
                    Minecraft.thePlayer.motionY = 0.4300000007157268;
                }
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
    }
}
