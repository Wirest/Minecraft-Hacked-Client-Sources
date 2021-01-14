package com.mentalfrostbyte.jello.modules;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventEntityStep;
import com.mentalfrostbyte.jello.event.events.EventMotion;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.events.EventPreMotionUpdates;
import com.mentalfrostbyte.jello.event.events.EventSlowDown;
import com.mentalfrostbyte.jello.event.events.EventTick;
import com.mentalfrostbyte.jello.event.types.EventType;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Step extends Module {

	
	public static double moveSpeed;
	public TimerUtil timer = new TimerUtil();
	public static boolean enabled;
	private boolean resetNextTick;
    private double preY;
	
    
    
	public Step() {
        super("Step", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(0);
    }
   
	public void onEnable(){
		EventManager.register(this);
		enabled = true;
		
	}
	
	public void onDisable(){
		EventManager.unregister(this);
		enabled = false;
	}
	
    
    //STEP
    @EventTarget
    public void onStep(EventEntityStep event) {
    	
    	
        if (event.getEntity() != this.mc.thePlayer) {
            return;
        }
        
        if(mc.thePlayer.isInLiquid())
        	return;
        if(KillAura.attacking && KillAura.enabled)
        	return;
        
        if (event.getType() == EventType.PRE) {
            this.preY = this.mc.thePlayer.posY;
            if (this.canStep()) {
                event.setStepHeight(1.5f);
            }
        } else {
            if (event.getStepHeight() != 1.5f) {
                return;
            }
            double offset = this.mc.thePlayer.boundingBox.minY - this.preY;
            if (offset > 0.6) {
                double[] arrd = new double[6];
                arrd[0] = 0.42;
                arrd[1] = offset < 1.0 && offset > 0.8 ? 0.73 : 0.75;
                arrd[2] = 1.0;
                arrd[3] = 1.16;
                arrd[4] = 1.23;
                arrd[5] = 1.2;
                double[] offsets = arrd;
                int i = 0;
                while (i < (offset > 1.0 ? offsets.length : 2)) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offsets[i], this.mc.thePlayer.posZ, false));
                    ++i;
                }
                //mc.timer.timerSpeed = offset > 1.0 ? 0.15f : 0.37f;
                this.resetNextTick = true;
            }
        }
		
    }
	@EventTarget
    public void onPostTick(EventTick event) {
        if (this.resetNextTick) {
        	 //mc.timer.timerSpeed = 1.0f;
            this.resetNextTick = false;
        }
	
    }
	private boolean canStep() {
        if (this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0 && !this.mc.thePlayer.movementInput.jump) {
            return true;
        }
        return false;
    }
    
	
}
