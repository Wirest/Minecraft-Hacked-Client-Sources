package me.Corbis.Execution.module.implementations.targets;

import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class Villagers extends Module {
    public Villagers(){
        super("Villagers", Keyboard.KEY_NONE, Category.TARGETS);
    }
}
