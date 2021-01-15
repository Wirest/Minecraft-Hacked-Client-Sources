package info.spicyclient.modules.player;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

public class NoFall extends Module {
	
	public ModeSetting noFallMode = new ModeSetting("NoFall Mode", "Vanilla", "Vanilla", "Packet");
	
	public NoFall() {
		super("No Fall", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(noFallMode);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventMotion && e.isPre() && noFallMode.is("Vanilla") && mc.thePlayer.fallDistance > 2) {
			
			EventMotion event = (EventMotion) e;
			event.onGround = true;
			
		}
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				if (mc.thePlayer.fallDistance > 3 && noFallMode.is("Packet") && !SpicyClient.config.fly.isEnabled()) {
					
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
					
				}
				
			}
			
		}
		
	}
	
}
