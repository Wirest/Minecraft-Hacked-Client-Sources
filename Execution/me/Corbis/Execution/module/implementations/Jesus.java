package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {

    public Jesus(){
        super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);
    }


}
