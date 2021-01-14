package me.Corbis.Execution.module.implementations.targets;

import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class Teams extends Module {
    public Teams(){
        super("Teams", Keyboard.KEY_NONE, Category.TARGETS);
    }
}
