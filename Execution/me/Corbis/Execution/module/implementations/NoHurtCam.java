package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class NoHurtCam extends Module {

    public NoHurtCam(){
        super("NoHurtCam", Keyboard.KEY_NONE, Category.RENDER);
    }

    
}
