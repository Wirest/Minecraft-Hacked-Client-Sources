package com.mentalfrostbyte.jello.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventReceivePacket;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiKnockback extends Module {

	public Random rand = new Random();
	public TimerUtil timer = new TimerUtil();
	public static boolean isEnabled;
	public static double y;

	public AntiKnockback() {
        super("AntiKnockback", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(0);
       
    }
   
	    @EventTarget
	    public void onKb(EventReceivePacket e) {

	    		 	 if (e.getPacket() instanceof S27PacketExplosion/* || e.getPacket() instanceof S12PacketEntityVelocity*/) {
	    		 	//	 Jello.addChatMessage("hi");
	    			e.setCancelled(true);
	    		 	 }
		}
	    
	    public void onEnable(){
			EventManager.register(this);
			isEnabled = true;
			y = mc.thePlayer.posY;
			timer.reset();
			
			//double yaw = mc.thePlayer.rotationYaw;
		       
			// mc.thePlayer.getEntityBoundingBox().offset(1 * Math.cos(Math.toRadians((double) (yaw + 90.0))), 01.0, 1 * Math.sin(Math.toRadians((double) (yaw + 90.0))));
		//	 mc.thePlayer.setPosition(mc.thePlayer.posX + (5 * Math.cos(Math.toRadians((double) (yaw + 90.0)))), mc.thePlayer.posY + 5, mc.thePlayer.posZ + (5 * Math.sin(Math.toRadians((double) (yaw + 90.0)))));
			//this.toggle();
		}
		
		public void onDisable(){
			isEnabled = false;
			EventManager.unregister(this);
			
		}
}
