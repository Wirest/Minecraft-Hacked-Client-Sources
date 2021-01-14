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

public class Fly extends Module {

	public Random rand = new Random();
	
	public TimerUtil timer = new TimerUtil();
	
	public Fly() {
        super("Fly", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(0);
    }
   
	public void onEnable(){
	}
	
	public void onDisable(){
		  mc.thePlayer.capabilities.allowFlying = false;
		  mc.thePlayer.capabilities.isFlying = false;
	}
	
    
	  public void onUpdate()
	  {
		  if(!this.isToggled())
			  return; 
		  mc.thePlayer.capabilities.allowFlying = true;
		  mc.thePlayer.capabilities.isFlying = true;
}
	
	
}
