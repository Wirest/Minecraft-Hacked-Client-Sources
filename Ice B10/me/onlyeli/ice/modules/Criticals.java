package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

public class Criticals extends Module
{
    public Criticals() {
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
    }
    
    public void onDisable() {
        Wrapper.mc.thePlayer.onGround = true;
        super.onDisable();
    }
    
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        if (Wrapper.mc.thePlayer.motionY > 0.03) {
            Wrapper.mc.thePlayer.motionY = -0.22;
        }
        else {
            if (Wrapper.mc.thePlayer.motionY > -0.19 && Wrapper.mc.thePlayer.onGround) {
                Wrapper.mc.thePlayer.jump();
            }
            super.onUpdate();
        }
    }
}
