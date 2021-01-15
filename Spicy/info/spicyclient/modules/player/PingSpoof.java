package info.spicyclient.modules.player;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventSendPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.Timer;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PingSpoof extends Module {
	
	private NumberSetting ping = new NumberSetting("Ping", 100, 20, 1000, 1);
	
	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	private Packet sendPacket = null;
	
	public PingSpoof() {
		super("PingSpoof", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(ping);
	}
	
	public void onEnable() {
		packets.clear();
	}
	
	public void onDisable() {
		packets.clear();
	}
	
	private Timer timer = new Timer();
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSendPacket) {
			
			if (e.isPre()) {
				
				EventSendPacket sendPacket = (EventSendPacket) e;
				
				if (this.sendPacket != null && sendPacket.packet instanceof C00PacketKeepAlive && sendPacket.packet == this.sendPacket) {
					
					this.sendPacket = null;
					
				}
				
				else if (sendPacket.packet instanceof C00PacketKeepAlive) {
					
					packets.add(sendPacket.packet);
					sendPacket.setCanceled(true);
					
				}
				
			}
			
		}
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				if (packets.size() > 500) {
					
					for (int i = 1; i < packets.size(); i++) {
						
						packets.remove(i);
						
					}
					
				}
				
				this.additionalInformation =  "" + ping.getValue();
				
				if (timer.hasTimeElapsed((long) (ping.getValue()), true)) {
					
					if (packets.size() >= 1) {
						
						this.sendPacket = packets.get(0);
						mc.thePlayer.sendQueue.addToSendQueue(packets.get(0));
						packets.remove(0);
						
					}
					
				}
				
				
			}
			
		}
		
	}
	
}
