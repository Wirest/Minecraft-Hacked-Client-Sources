package com.mentalfrostbyte.jello.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventMotion;
import com.mentalfrostbyte.jello.event.events.EventMove;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.events.EventPreMotionUpdates;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class LongJump extends Module {

	
	public TimerUtil timer = new TimerUtil();
	public static boolean enabled;
    public static float stage;
    
    public boolean wasOnGround;
    public boolean wasNotOnGround;
    
    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(0);
    }
   
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
	//EventManager.unregister(this);
	enabled = false;
}

public void onEnable() {
	wasOnGround = false;
	wasNotOnGround = false;
	//EventManager.register(this);
	stage = 0;
	enabled = true;
}
	
public static int getSpeedAmplifier()
{
  if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
    return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
  }
  return 0;
}

public void onUpdate(){
	if(!this.isToggled())
		return;
	
	if(mc.thePlayer.onGround && wasOnGround && wasNotOnGround){
		toggle();
	}
	
	if(mc.thePlayer.onGround){
		wasOnGround = true;
	}else{
		wasNotOnGround = true;
	}
	
	float f1;
    float f2;
      f1 = 0.7F + getSpeedAmplifier() * 0.45F;
      if (((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) && (mc.thePlayer.onGround))
      {
        mc.thePlayer.motionX *= 1.3D;
        mc.thePlayer.motionZ *= 1.3D;
        if(!(mc.thePlayer.onGround && wasOnGround && wasNotOnGround)){
        mc.thePlayer.jump();
        }
      }
      if (mc.thePlayer.onGround)
      {
        this.stage = 0.0F;
      }
      else
      {
        mc.thePlayer.motionX *= 0.0D;
        mc.thePlayer.motionZ *= 0.0D;
        f2 = 0.95F + getSpeedAmplifier() * 0.2F - this.stage / 25.0F;
        mc.thePlayer.jumpMovementFactor = (f2 > 0.28F ? f2 : 0.28F);
        if (mc.thePlayer.motionY > 0.0D)
        {
          mc.thePlayer.motionY += 0.006D;
          mc.thePlayer.motionY *= 1.002D;
        }
        else if (mc.thePlayer.fallDistance <= 2.6D)
        {
          mc.thePlayer.motionY += 0.0366D;
          if (isNotCollidingBelow(0.4D)) {
            mc.thePlayer.jumpMovementFactor = 0.2F;
          }
        }
        this.stage += f1;
      
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
