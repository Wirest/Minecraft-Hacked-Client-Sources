package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventOnLadder;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Spider extends Module {
	
	private ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla");
	
	public Spider() {
		super("Spider", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(mode);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = mode.getMode();
				
			}
			
		}
		
		if (e instanceof EventOnLadder) {
			if (e.isPost()) {
				if (mc.thePlayer.isCollidedHorizontally && mode.is("Vanilla")) {
					((EventOnLadder) e).onLadder = true;
				}
			}
		}
		
	}
	
}
