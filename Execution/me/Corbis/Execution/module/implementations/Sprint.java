package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import org.lwjgl.input.Keyboard;

public class    Sprint extends Module {
    public Sprint(){
        super("Sprint", Keyboard.KEY_K, Category.MOVEMENT);

    }

    @EventTarget
    public void onMotion(EventUpdate event){
        if(!MoveUtils.isMoving())
            return;
        mc.thePlayer.setSprinting(true);



    }
}
