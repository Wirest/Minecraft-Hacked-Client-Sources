package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.Category;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;

public class NoClip extends Module
{
    public NoClip() {
        super("NoClip", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    
    @Override
    public void onDisable() {
        Minecraft.getMinecraft().thePlayer.noClip = false;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.noClip = true;
        Minecraft.getMinecraft().thePlayer.fallDistance = 0.0f;
        Minecraft.getMinecraft().thePlayer.onGround = false;
        Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
        Minecraft.getMinecraft().thePlayer.motionX = 0.0;
        Minecraft.getMinecraft().thePlayer.motionY = 0.0;
        Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
        final float speed = 1.0f;
        Minecraft.getMinecraft().thePlayer.jumpMovementFactor = speed;
        if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            thePlayer.motionY += speed;
        }
        if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed()) {
            final EntityPlayerSP thePlayer2 = Minecraft.getMinecraft().thePlayer;
            thePlayer2.motionY -= speed;
        }
        super.onUpdate();
    }
}
