package com.mentalfrostbyte.jello.modules;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventMotion;
import com.mentalfrostbyte.jello.event.events.EventMove;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.events.EventPreMotionUpdates;
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
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class BunnyHop extends Module {

	
	public TimerUtil timer = new TimerUtil();
	public static boolean enabled;
	
	private double moveSpeed;
	  private double lastDist;
	  public static int stage;
	
	public BunnyHop() {
        super("Bunny Hop", Keyboard.KEY_V);
        this.jelloCat = Jello.tabgui.cats.get(0);
    }
   
	 private double getBaseMoveSpeed() {
	        double baseSpeed = 0.2873;
	        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
	        }
	        return baseSpeed;
	    }
	
	public void onEnable(){
		EventManager.register(this);
		enabled = true;
		if (mc.thePlayer != null) {
		      this.moveSpeed = getBaseMoveSpeed();
		    }
		    this.lastDist = 0.0D;
		    stage = 2;
		    mc.timer.timerSpeed = 1.0F;
	}
	
	public void onDisable(){
		EventManager.unregister(this);
		enabled = false;
		 mc.timer.timerSpeed = 1.0f;
	}
	
	@EventTarget
    public void onMotion(EventMotion event){  
    	
    	
         double d5;
          double d1 = mc.thePlayer.posX - mc.thePlayer.prevPosX;
          d5 = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
          this.lastDist = Math.sqrt(d1 * d1 + d5 * d5);
          
    	
    	}
    
	
    @EventTarget
    public void onMove(EventMove event){  
    	
    	if(!mc.thePlayer.isInWater()){
    	if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
            this.moveSpeed = getBaseMoveSpeed();
          }
          if ((stage == 1) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
            this.moveSpeed = (1.35D + getBaseMoveSpeed() - 0.01D);
          }
          if ((stage == 2) && (mc.thePlayer.isCollidedVertically) && (isNotCollidingBelow(0.01D)) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
          {
        	  if(mc.gameSettings.keyBindJump.getIsKeyPressed()){
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
              event.setY(mc.thePlayer.motionY = 0.41999998688698D + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1D);
            } else {
            	event.setY(mc.thePlayer.motionY = 0.41999998688698D);
            }
            this.moveSpeed *= 1.6D;
        	  }
          }
          else if (stage == 3)
          {
            double d2 = 0.66D * (this.lastDist - getBaseMoveSpeed());
            this.moveSpeed = (this.lastDist - d2);
          }
          else
          {
            if ((stage == 5) && (mc.thePlayer.motionY > 0.1D) && (mc.thePlayer.motionY < 0.2D)) {
              mc.thePlayer.motionY = 0.0D;
            }
            List localList1 = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
            if (((localList1.size() > 0) || (mc.thePlayer.isCollidedVertically)) && (stage > 0)) {
              stage = (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
            }
            this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
          }
          this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
          if (stage > 0) {
            eventMove(event, this.moveSpeed);
          }
          if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
            stage += 1;
          }
          
    	}
    	}
    
    private void eventMove(EventMove paramEventMove, double paramDouble)
    {
      double d1 = mc.thePlayer.movementInput.moveForward;
      double d2 = mc.thePlayer.movementInput.moveStrafe;
      float f = mc.thePlayer.rotationYaw;
      if ((d1 == 0.0D) && (d2 == 0.0D))
      {
        paramEventMove.setX(0.0D);
        paramEventMove.setZ(0.0D);
      }
      else
      {
        if (d1 != 0.0D)
        {
          if (d2 > 0.0D) {
            f += (d1 > 0.0D ? -45 : 45);
          } else if (d2 < 0.0D) {
            f += (d1 > 0.0D ? 45 : -45);
          }
          d2 = 0.0D;
          if (d1 > 0.0D) {
            d1 = 1.0D;
          } else if (d1 < 0.0D) {
            d1 = -1.0D;
          }
        }
        paramEventMove.setX(d1 * paramDouble * Math.cos(Math.toRadians(f + 90.0F)) + d2 * paramDouble * Math.sin(Math.toRadians(f + 90.0F)));
        paramEventMove.setZ(d1 * paramDouble * Math.sin(Math.toRadians(f + 90.0F)) - d2 * paramDouble * Math.cos(Math.toRadians(f + 90.0F)));
      }
    }
    
    public static boolean isNotCollidingBelow(double paramDouble)
    {
      if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -paramDouble, 0.0D)).isEmpty()) {
        return true;
      }
      return false;
    }
    
	
}
