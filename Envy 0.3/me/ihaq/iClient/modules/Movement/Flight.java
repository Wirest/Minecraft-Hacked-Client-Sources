package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
 
public class Flight extends Module{
	private static String mode;
    public Flight() {
        super("Flight",Keyboard.KEY_NONE,Category.MOVEMENT, mode);
    }
       
    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
    }
   
    @EventTarget
    public void onUpdate(EventUpdate e) {
       
        if(!this.isToggled())
            return;
              
        vanilla();
		
    }
    
    public void vanilla(){
    	setMode(": \u00A7fVanilla");
    	if(mc.gameSettings.keyBindJump.isPressed()){
			mc.thePlayer.motionY += 0.2;
		}
		
		if(mc.gameSettings.keyBindSneak.isPressed()){
			mc.thePlayer.motionY -= 0.2;
		}
		mc.thePlayer.capabilities.isFlying = true;
    }
   
}