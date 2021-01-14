package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    public FullBright(){
        super("FullBright", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        mc.gameSettings.gammaSetting = 10f;
    }

}
