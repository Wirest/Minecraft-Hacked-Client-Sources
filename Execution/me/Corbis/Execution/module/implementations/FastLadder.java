package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

public class FastLadder extends Module {
    public FastLadder(){
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onMotion(EventMotion event){
        if(mc.thePlayer.isOnLadder()){
            if(mc.thePlayer.movementInput.moveForward > 0){
                mc.thePlayer.motionY *= 2;
            }
        }
    }
}
