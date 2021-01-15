package info.spicyclient.modules.player;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventChatmessage;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.ui.NewAltManager;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Timer extends Module {
	
	public NumberSetting tps = new NumberSetting("TPS", 20, 1, 60, 1);
	
	public Timer() {
		super("Timer", Keyboard.KEY_NONE, Category.PLAYER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(tps);
	}
	
	public void onEnable() {
		mc.timer.ticksPerSecond = (float) tps.value;
	}
	
	public void onDisable() {
		mc.timer.ticksPerSecond = 20.0f;
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				this.additionalInformation = "" + tps.getValue();
				
				mc.timer.ticksPerSecond = (float) tps.value;
			}
			
		}
		
	}
	
}
