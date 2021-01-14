package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class TargetHUD extends Module {
    public TargetHUD(){
        super("TargetHUD", Keyboard.KEY_NONE, Category.RENDER);
    }

}
