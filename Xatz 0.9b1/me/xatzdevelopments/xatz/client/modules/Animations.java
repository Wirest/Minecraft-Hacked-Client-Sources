package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class Animations extends Module
{
    public static String animmode;
    
    
    
    @Override
    public String[] getModes() {
        return new String[] { "Vanilla", "Smooth" };
    }
    
    @Override
    public String getModeName() {
        return "Hit: ";
    }
    
    public Animations() {
        super("Animations", 0, Category.FUN, "Changes some animations.");
    }
    
    @Override
    public void onUpdate() {
        if (this.currentMode.equals("Vanilla")) {
            Animations.animmode = "Vanilla";
        }
        if (this.currentMode.equals("Smooth")) {
            Animations.animmode = "Smooth";
        }
    }
    
    @Override
    public void onToggle() {
        super.onToggle();
    }
}
