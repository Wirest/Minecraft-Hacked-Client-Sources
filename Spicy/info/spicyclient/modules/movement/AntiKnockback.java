package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventPacket;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.MathHelper;

public class AntiKnockback extends Module {
	
	public NumberSetting horizontalKnockback = new NumberSetting("Horizontal Knockback", 0, 0, 100, 1);
	public NumberSetting verticalKnockback = new NumberSetting("Vertical Knockback", 0, 0, 100, 1);
	
	public AntiKnockback() {
		super("AntiKnockback", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		addSettings(horizontalKnockback, verticalKnockback);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			this.additionalInformation = "H: " + horizontalKnockback.getValue() + " V: " + verticalKnockback.getValue();
		}
		
		if (e instanceof EventPacket && e.isPre()) {
			
			EventPacket event = (EventPacket) e;
			
			if (event.packet instanceof S12PacketEntityVelocity) {
				
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.packet;
				packet.setMotionX((int) ((packet.getMotionX() / 100) * horizontalKnockback.getValue()));
				packet.setMotionY((int) ((packet.getMotionY() / 100) * verticalKnockback.getValue()));
				packet.setMotionZ((int) ((packet.getMotionZ() / 100) * horizontalKnockback.getValue()));
				
				if (horizontalKnockback.getValue() == 0 && verticalKnockback.getValue() == 0) {
					e.setCanceled(true);
				}
				
				
			}
			else if (event.packet instanceof S27PacketExplosion) {
				e.setCanceled(true);
			}
			
		}
		
	}
	
}
