package me.onlyeli.ice.modules;

import org.lwjgl.input.*;
import me.onlyeli.ice.*;
import me.onlyeli.ice.utils.*;

public class AutoMine extends Module
{
    public AutoMine() {
        super("AutoMine", Keyboard.KEY_NONE, Category.WORLD);
    }
    
    public void onDisable() {
        Wrapper.mc.gameSettings.keyBindAttack.pressed = false;
        super.onDisable();
    }
    
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        Wrapper.mc.gameSettings.keyBindAttack.pressed = true;
        super.onUpdate();
    }
}
