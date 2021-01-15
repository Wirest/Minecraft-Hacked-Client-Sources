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

public class AltManager extends Module {
	
	public AltManager() {
		super("Alt Manager", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@Override
	public void toggle() {
		toggled = !toggled;
		if (toggled) {
			onEnable();
		}else {
			onDisable();
		}
	}
	
	public void onEnable() {
		mc.displayGuiScreen(new NewAltManager(null));
		this.toggle();
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
	}
	
}
