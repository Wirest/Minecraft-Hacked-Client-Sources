package com.mentalfrostbyte.jello.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class NoFall extends Module {

	public Random rand = new Random();
	
	public TimerUtil timer = new TimerUtil();
	
	public NoFall() {
        super("NoFall", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(1);
    }
   
	public void onEnable(){
	}
	
	public void onDisable(){
	}
	
    
	  public void onUpdate()
	  {
		  if(!this.isToggled())
			  return; 
		if (mc.thePlayer.fallDistance > 2f && mc.thePlayer.fallDistance < 20 && !AirWalk.isEnabled && !Jello.getModule("Rush").isToggled()) {
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
		}
	
}
	
	
}
