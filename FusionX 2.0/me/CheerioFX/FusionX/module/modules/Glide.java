// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Glide extends Module
{
    public Glide() {
        super("Glide", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        for (int i = 0; i < 80; ++i) {
            Glide.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Glide.mc.thePlayer.posX, Glide.mc.thePlayer.posY + 0.05, Glide.mc.thePlayer.posZ, false));
            Glide.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Glide.mc.thePlayer.posX, Glide.mc.thePlayer.posY, Glide.mc.thePlayer.posZ, false));
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.getState()) {
            return;
        }
        if (!Glide.mc.gameSettings.keyBindJump.isPressed() && !Glide.mc.gameSettings.keyBindSneak.isPressed() && Glide.mc.thePlayer.movementInput.moveForward == 0.0 && Glide.mc.thePlayer.movementInput.moveStrafe == 0.0) {
            Glide.mc.thePlayer.motionX = 0.0;
            Glide.mc.thePlayer.motionY = 0.0;
            Glide.mc.thePlayer.motionZ = 0.0;
        }
        else if (Glide.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
            Glide.mc.thePlayer.motionY = -0.01;
        }
        else if (Glide.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
            Glide.mc.thePlayer.motionY = 0.01;
        }
        else if (Glide.mc.thePlayer.movementInput.moveForward != 0.0f || Glide.mc.thePlayer.movementInput.moveStrafe != 0.0f) {
            Glide.mc.thePlayer.motionY = -0.04;
        }
        super.onUpdate();
    }
}
