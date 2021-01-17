package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
 
public class AirJump extends Module{
	
    public AirJump() {
        super("AirJump", Keyboard.KEY_NONE ,Category.MOVEMENT, "");
    }
    
    @Override
    public void onEnable() {
    }
       
    @Override
    public void onDisable() {
    }
   
    @EventTarget
    public void onUpdate(EventUpdate e) {
       
        if(!this.isToggled())
            return;                     
        
        if(mc.gameSettings.keyBindJump.isPressed()){
        	mc.thePlayer.motionX *= 1.5;
        	mc.thePlayer.motionY = 0.4;
        	mc.thePlayer.motionZ *= 1.5;
        	mc.thePlayer.onGround =  true;
        }      
        	
    }  
}