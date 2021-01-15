package VenusClient.online.Module.impl.Player;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventMove;
import VenusClient.online.Event.impl.EventReceivePacket;
import VenusClient.online.Event.impl.EventSendPacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;

public class AutoBypass extends Module{

	public AutoBypass() {
		super("AutoBypass", "AutoBypass", Category.PLAYER, Keyboard.KEY_NONE);
	}
	
	@Override
	public void setup() {
		
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		
	}

	@EventTarget
	public void onMove(EventMove event) {
		
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		
	}
	
	
	@Override
	protected void onDisable() {
		super.onDisable();
	}
	
}
