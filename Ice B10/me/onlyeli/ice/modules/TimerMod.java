package me.onlyeli.ice.modules;

import me.onlyeli.ice.Module;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.client.*;

public class TimerMod extends Module
{
    public TimerMod() {
        super("Timer", 34, Category.MOVEMENT);
    }
    
    @Override
    public void onDisable() {
        Minecraft.getMinecraft().timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        Minecraft.getMinecraft().timer.timerSpeed = 2.0f;
        super.onUpdate();
    }
}
