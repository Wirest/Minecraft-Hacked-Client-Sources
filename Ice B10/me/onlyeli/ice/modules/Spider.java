package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Module;
import me.onlyeli.ice.Category;
import net.minecraft.client.*;

public class Spider extends Module
{
    public Spider() {
        super("Spider", Keyboard.KEY_NONE, Category.MOVEMENT);
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
        if (Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
            Minecraft.getMinecraft().thePlayer.motionY = 0.2;
        }
        super.onUpdate();
    }
}
