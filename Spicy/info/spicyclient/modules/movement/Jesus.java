package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventGetLiquidHitbox;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	private boolean riseUp = false;
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventGetLiquidHitbox) {
			
			if (e.isPre()) {
				
				EventGetLiquidHitbox event = (EventGetLiquidHitbox) e;
				event.returnValue = AxisAlignedBB.fromBounds(event.pos.getX() + event.minX, event.pos.getY() + event.minY, event.pos.getZ() + event.minZ, event.pos.getX() + event.maxX, event.pos.getY() + event.maxY, event.pos.getZ() + event.maxZ);
				
			}
			
		}
		
		if (e instanceof EventMotion) {
			
			if (e.isPost()) {
				/*
		        if (mc.thePlayer.worldObj.handleMaterialAcceleration(mc.thePlayer.getEntityBoundingBox().expand(0.0D, 0.05D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, mc.thePlayer)){
		        	if ((mc.thePlayer.fallDistance >= 0.0000000000000000000000000001 && !mc.thePlayer.isInWater()) || !riseUp) {
		        		
		        		riseUp = true;
		        		
		        		double y, y1;
						mc.thePlayer.motionY = 0;
						
						if (mc.thePlayer.ticksExisted % 3 ==0) {
							
							y = mc.thePlayer.posY - 1.0E-10D;
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
							
						}
						
						y1 = mc.thePlayer.posY + 1.0E-10D;
						mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		        		
		        	}else {
		        		mc.thePlayer.motionY = 0;
		        	}
		        }else {
		        	
		        	riseUp = false;
		        	
		        }
				*/
			}
			
		}
		
	}
	
}
