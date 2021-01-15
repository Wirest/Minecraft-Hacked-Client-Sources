package info.spicyclient.modules.player;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.Timer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class Disabler extends Module {
	public Disabler() {
		super("Disabler", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	public static transient boolean watchdog = false;
	public static transient ArrayList<C0FPacketConfirmTransaction> C0FPackets = new ArrayList<C0FPacketConfirmTransaction>();
	public static transient Timer ping = new Timer();
	
	@Override
	public void onEnable() {
		NotificationManager.getNotificationManager().createNotification("Relog for the disabler to take effect", "", true, 5000, Type.INFO, Color.PINK);
		watchdog = false;
	}
	
	@Override
	public void onDisable() {
		watchdog = false;
		
		for (C0FPacketConfirmTransaction p : C0FPackets) {
			
			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
			
		}
		
		C0FPackets.clear();
		
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			this.additionalInformation = "Hypixel";
			
            PlayerCapabilities playerCapabilities = new PlayerCapabilities();
            playerCapabilities.isFlying = true;
            playerCapabilities.allowFlying = true;
            //playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
            playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
            playerCapabilities.isCreativeMode = true;
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
            
            // Added this
            if (SpicyClient.config.fly.isEnabled()) {
            	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
            }
            
            if (ping.hasTimeElapsed(500 + (new Random()).nextInt(750), true) && C0FPackets.size() > 0) {
            	mc.getNetHandler().getNetworkManager().sendPacketNoEvent(C0FPackets.get(0));
            	C0FPackets.remove(0);
            }
            
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
			if (((EventPacket)e).packet instanceof S00PacketDisconnect) {
				C0FPackets.clear();
			}
			
		}
		
		if (e instanceof EventSendPacket && e.isPre()) {
			
			EventSendPacket event = (EventSendPacket) e;
			
			if (event.packet instanceof C00Handshake) {
				C0FPackets.clear();
			}
			
            if (event.packet instanceof C0FPacketConfirmTransaction) {
            	
                C0FPacketConfirmTransaction packetConfirmTransaction = (C0FPacketConfirmTransaction)event.packet;
                
                //mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0FPacketConfirmTransaction(2147483647, packetConfirmTransaction.getUid(), false));
                //mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0FPacketConfirmTransaction(1147483647, packetConfirmTransaction.getUid(), false));
                
                
                //packetConfirmTransaction.setAccepted(new Random().nextBoolean());
                //packetConfirmTransaction.setWindowId(Integer.MIN_VALUE + new Random().nextInt(1000));
                //packetConfirmTransaction.setUid(Short.MAX_VALUE);
            	//mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0FPacketConfirmTransaction(Integer.MIN_VALUE, Short.MAX_VALUE, true));
                
                C0FPackets.add(packetConfirmTransaction);
                e.setCanceled(true);
            }

            if (event.packet instanceof C00PacketKeepAlive) {
            	
            	//mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C00PacketKeepAlive(-2147483648 + (new Random()).nextInt(100)));
                //e.setCanceled(true);
                
            }
            
			if (event.packet instanceof C0CPacketInput) {
				e.setCanceled(true);
			}
            
		}
		
	}
	
}