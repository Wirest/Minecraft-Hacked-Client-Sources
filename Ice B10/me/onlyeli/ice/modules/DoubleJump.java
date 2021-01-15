package me.onlyeli.ice.modules;

import me.onlyeli.ice.ui.*;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.*;

public class DoubleJump extends Module
{
    public DoubleJump() {
        super("Double Jump", 0, Category.MOVEMENT);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
            Minecraft.getMinecraft().thePlayer.jump();
        }
        super.onUpdate();
    }
}
