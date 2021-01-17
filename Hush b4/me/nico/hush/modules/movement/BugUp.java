// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.nico.hush.events.EventUpdate;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class BugUp extends Module
{
    protected int trys;
    
    public BugUp() {
        super("BugUp", "BugUp", 3848670, 38, Category.MOVEMENT);
        this.trys = 0;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = BugUp.mc;
        if (Minecraft.thePlayer != null && BugUp.mc.theWorld != null) {
            final Minecraft mc2 = BugUp.mc;
            if (Minecraft.thePlayer.fallDistance > 0.0f && BugUp.mc.gameSettings.keyBindSneak.pressed) {
                final Minecraft mc3 = BugUp.mc;
                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                final Minecraft mc4 = BugUp.mc;
                final double posX = Minecraft.thePlayer.posX;
                final Minecraft mc5 = BugUp.mc;
                final double y = Minecraft.thePlayer.posY + 1.0;
                final Minecraft mc6 = BugUp.mc;
                thePlayer.setPosition(posX, y, Minecraft.thePlayer.posZ);
                this.trys = 0;
                final Minecraft mc7 = BugUp.mc;
                Minecraft.thePlayer.fallDistance = 0.0f;
                if (this.trys > 3) {
                    this.trys = 0;
                    BugUp.mc.gameSettings.keyBindSneak.pressed = false;
                }
                final Minecraft mc8 = BugUp.mc;
                if (Minecraft.thePlayer.onGround && this.trys > 0 && !BugUp.mc.gameSettings.keyBindSneak.pressed) {
                    this.trys = 0;
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
        this.trys = 0;
    }
}
