package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Module;
import me.onlyeli.ice.Category;
import net.minecraft.client.*;

public class NoPush extends Module
{
    public NoPush() {
        super("NoPush", Keyboard.KEY_NONE, Category.COMBAT);
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        if (this.isToggled() && Minecraft.getMinecraft().thePlayer.hurtResistantTime > 0 && Minecraft.getMinecraft().thePlayer.hurtTime > 0) {
            Minecraft.getMinecraft().thePlayer.motionX = 0.0;
            Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
        }
    }
}
