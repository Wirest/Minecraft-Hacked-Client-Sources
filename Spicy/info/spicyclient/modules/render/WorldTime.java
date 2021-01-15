package info.spicyclient.modules.render;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventServerSettingWorldTime;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.network.play.client.C03PacketPlayer;

public class WorldTime extends Module {
	
	public NumberSetting time = new NumberSetting("Time", 1, 0, 24, 0.5);
	
	public WorldTime() {
		super("World Time", Keyboard.KEY_NONE, Category.RENDER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(time);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	public void onEvent(Event e) {
		
		if (e instanceof EventServerSettingWorldTime) {
			
			e.setCanceled(true);
			mc.theWorld.setWorldTime((long) (time.getValue() * 1000));
			
		}
		
	}
	
}
