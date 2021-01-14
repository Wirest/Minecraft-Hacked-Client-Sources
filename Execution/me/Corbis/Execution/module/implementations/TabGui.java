package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event2D;
import me.Corbis.Execution.event.events.EventKey;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class TabGui extends Module {

    public TabGui(){
        super("TabGui", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onRender(Event2D event){
        Execution.instance.tabGui.draw();
    }

    @EventTarget
    public void onKey(EventKey event){

        Execution.instance.tabGui.onKeyPress(event.getKey());
    }
}
