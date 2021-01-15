package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.managers.*;
import net.minecraft.client.*;

public class AutoSneak extends Module
{
    public AutoSneak() {
        super("AutoSneak", Keyboard.KEY_NONE, Category.MOVEMENT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
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
        if (!Minecraft.getMinecraft().thePlayer.isSneaking()) {
            Minecraft.getMinecraft().thePlayer.setSneaking(true);
        }
        super.onUpdate();
    }
}
