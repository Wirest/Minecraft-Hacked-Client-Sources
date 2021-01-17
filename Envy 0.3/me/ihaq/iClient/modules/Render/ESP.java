package me.ihaq.iClient.modules.Render;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.values.BooleanValue;

public class ESP extends Module {

	public BooleanValue chestESP = new BooleanValue(this, "CHESTESP", "ChestESP", Boolean.valueOf(false));
	public BooleanValue playerESP = new BooleanValue(this, "PLAYERESP", "PlayerESP", Boolean.valueOf(false));
	public BooleanValue mobESP = new BooleanValue(this, "MOBESP", "MobESP", Boolean.valueOf(false));
	public static boolean playerESPStatus;
	public static boolean mobESPStatus;
	public static boolean chestESPStatus;
	

	public ESP() {
		super("ESP", Keyboard.KEY_NONE, Category.RENDER, "");
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		
		playerESPStatus = (Boolean) playerESP.getValue() ? true : false;
		mobESPStatus = (Boolean) mobESP.getValue() ? true : false;
		chestESPStatus = (Boolean) chestESP.getValue() ? true : false;

	}

}
