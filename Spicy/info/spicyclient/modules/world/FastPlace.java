package info.spicyclient.modules.world;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastPlace extends Module {

	public FastPlace() {
		super("Fast Place", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				mc.rightClickDelayTimer = 0;
				
			}
			
		}
		
	}
	
}
