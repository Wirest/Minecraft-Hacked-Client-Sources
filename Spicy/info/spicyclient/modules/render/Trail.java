package info.spicyclient.modules.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventPlayerRender;
import info.spicyclient.events.listeners.EventRender3D;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.util.PlayerUtils;
import info.spicyclient.util.RenderUtils;
import info.spicyclient.util.RotationUtils;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class Trail extends Module {
	
	private transient static ArrayList<Vec3> trailList = new ArrayList<Vec3>();
	private transient static Timer timer = new Timer();
	
	public Trail() {
		super("Trail", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@Override
	public void onDisable() {
		trailList.clear();
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			this.additionalInformation = trailList.size() + " Lines drawn";
			
		}
		
		if (e instanceof EventRender3D) {
			
			//((EventRender3D) e).reset();
			EventRender3D render3d = (EventRender3D) e;
			
			if (timer.hasTimeElapsed(250, true)){
				
				//trailList.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
				
			}
			
			if (trailList.size() == 0) {
				trailList.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
			}
			else if (trailList.get(trailList.size() - 1).xCoord == mc.thePlayer.posX && trailList.get(trailList.size() - 1).yCoord == mc.thePlayer.posY && trailList.get(trailList.size() - 1).zCoord == mc.thePlayer.posZ) {
				
			}
			else {
				trailList.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
			}
			
			Vec3 lastLoc = null;
			
			for (Vec3 loc: trailList) {
				
				if (lastLoc == null) {
					lastLoc = loc;
				}else {
					
					if (mc.thePlayer.getDistance(loc.xCoord, loc.yCoord, loc.zCoord) > 75) {
						
					}else {
						
						RenderUtils.drawLine(lastLoc.xCoord, lastLoc.yCoord, lastLoc.zCoord, loc.xCoord, loc.yCoord, loc.zCoord);
						
					}
					
					lastLoc = loc;
				}
				
			}
			
			//RenderUtils.drawLine(render3d.getX(), render3d.getY(), render3d.getZ(), render3d.getX(), render3d.getY() + 10, render3d.getZ());
			//RenderUtils.drawLine(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY + 100000, mc.thePlayer.posZ);
			
		}
		
	}
	
}
