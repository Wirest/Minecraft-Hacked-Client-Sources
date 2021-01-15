package info.spicyclient.modules.player;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.Module.Category;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Hypixel5SecDisabler extends Module {
	
	public Hypixel5SecDisabler() {
		super("Hypixel5SecDisabler", Keyboard.KEY_NONE, Category.PLAYER);
		// TODO Auto-generated constructor stub
	}
	
	public static transient Timer timer = new Timer();
	
	public boolean watchdog = false;
	
	@Override
	public void onEnable() {
		
        if (MovementUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.21D, z, true));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11D, z, true));
            watchdog = true;
            NotificationManager.getNotificationManager().createNotification("Disabler", "Please wait 5s.", true, 5000, Type.INFO, Color.PINK);
            //mc.thePlayer.jump();
            //Notifications.getManager().post("Disabler", "Wait 5s.", Notifications.Type.INFO);
        } else {
        	watchdog = false;
        }
        
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket && e.isPre()) {
			
            if (e.isPre()) {
            	
                if (((EventSendPacket)e).packet instanceof C03PacketPlayer) {
                    if (watchdog) {
                        e.setCanceled(true);
                    }
                    
                }
			
            }
            
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
            if (e.isPre()) {
            	
                if (((EventPacket)e).packet instanceof S08PacketPlayerPosLook) {
                	
                    if (watchdog) {
                        toggle();
                        NotificationManager.getNotificationManager().createNotification("Disabler", "Watchdog has been disabled for 5 s.", true, 5000, Type.INFO, Color.PINK);
                        //mc.thePlayer.motionY += 1;
                        //SpicyClient.config.fly.toggle();
                    }
                    
                }
			
            }
            
		}
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			if (!watchdog) {
                if (MovementUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                    double x = mc.thePlayer.posX;
                    double y = mc.thePlayer.posY;
                    double z = mc.thePlayer.posZ;
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.21D, z, true));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11D, z, true));
                    watchdog = true;
                    NotificationManager.getNotificationManager().createNotification("Disabler: Wait 5s.", "", true, 5000, Type.INFO, Color.PINK);
                    //mc.thePlayer.jump();
                }
            } else {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                mc.thePlayer.jumpMovementFactor = 0;
                mc.thePlayer.noClip = true;
                mc.thePlayer.onGround = false;
            }
			
		}
		
	}
	
}
