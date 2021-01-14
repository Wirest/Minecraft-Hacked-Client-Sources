package me.Corbis.Execution.utils;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

import java.security.Key;

public class NameProtect extends Module {

    public NameProtect(){
        super("NameProtect", Keyboard.KEY_NONE, Category.RENDER);
    }
}
