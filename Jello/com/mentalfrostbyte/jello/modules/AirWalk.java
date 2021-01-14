package com.mentalfrostbyte.jello.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventMove;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.entity.EntityLivingBase;

public class AirWalk extends Module {

	public Random rand = new Random();
	public TimerUtil timer = new TimerUtil();
	public static boolean isEnabled;
	public static double y;
	public int counter;
	public static EntityLivingBase p;
	
	public AirWalk() {
        super("AirWalk", Keyboard.KEY_G);
        this.jelloCat = Jello.tabgui.cats.get(0);
    }
   
	public void onEnable(){
		EventManager.register(this);
		isEnabled = true;
		y = Jello.core.player().posY;
		
		timer.reset();

	}
	
	public void onDisable(){
		
		counter = 0;
		 mc.timer.timerSpeed = 1.0F;
	      Jello.core.player().motionX = 0.0D;
	      Jello.core.player().motionY = 0.0D;
	      Jello.core.player().motionZ = 0.0D;
		 Jello.core.player().capabilities.isFlying = false;
	        mc.timer.timerSpeed = 1.0F;
		

		
		
		if(p != null){
			    mc.theWorld.removeEntity(p);
		}
		isEnabled = false;
		EventManager.unregister(this);
		
	}
	
    public void onUpdate(){
    	if(!this.isToggled())
    		return;
    	
    	mc.gameSettings.keyBindJump.pressed = false;
    	if(Jello.core.player().hurtResistantTime == 19){
    		Jello.core.player().motionY = 0;
    		Jello.core.player().motionX = 0;
    				Jello.core.player().motionZ = 0;
    	}
    	
    				Jello.core.player().motionY = 0;
    				Jello.core.player().onGround = true;
    				 counter++;
    				if (counter  == 1) {
    					 Jello.core.player().setPosition(Jello.core.player().posX, Jello.core.player().posY - 0.0000000001, Jello.core.player().posZ);
    		            } else if (counter == 2) {
    		            	Jello.core.player().setPosition(Jello.core.player().posX, Jello.core.player().posY + 0.0000000001, Jello.core.player().posZ);
    		                counter = 0;
    		            }
    		            Jello.core.player().motionY = 0;
    	
    }
	@EventTarget
	public void onMove(EventMove event, double speed) {
    	
    	double forward = Jello.core.player().moveForward;
	    double strafe = Jello.core.player().moveStrafing;
	    float yaw =  Jello.core.player().rotationYaw;
	    if ((forward == 0.0D) && (strafe == 0.0D))
	    {
	      event.y = (0.0D);
	      event.z = (0.0D);
	    }
	    else
	    {
	      if (forward != 0.0D)
	      {
	        if (strafe > 0.0D) {
	          yaw += (forward > 0.0D ? -45 : 45);
	        } else if (strafe < 0.0D) {
	          yaw += (forward > 0.0D ? 45 : -45);
	        }
	        strafe = 0.0D;
	        if (forward > 0.0D) {
	          forward = 1.0D;
	        } else if (forward < 0.0D) {
	          forward = -1.0D;
	        }
	      }
	      event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
	      event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
	    }
    	
	}
	
	
	
}
